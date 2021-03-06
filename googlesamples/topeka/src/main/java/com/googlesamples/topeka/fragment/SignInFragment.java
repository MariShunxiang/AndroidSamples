package com.googlesamples.topeka.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import com.googlesamples.topeka.R;
import com.googlesamples.topeka.activity.CategorySelectionActivity;
import com.googlesamples.topeka.adapter.AvatarAdapter;
import com.googlesamples.topeka.helper.ApiLevelHelper;
import com.googlesamples.topeka.helper.PreferencesHelper;
import com.googlesamples.topeka.helper.TransitionHelper;
import com.googlesamples.topeka.model.Avatar;
import com.googlesamples.topeka.model.Player;
import com.googlesamples.topeka.widget.TransitionListenerAdapter;

/**
 *
 * Enable selection of an {@link Avatar} and use name.
 *
 * Created by Administrator on 2016/8/16 0016.
 */
public class SignInFragment extends Fragment {

  private static final String ARG_EDIT = "EDIT";
  private static final String KEY_SELECTED_AVATAR_INDEX = "selectAvatarIndex";
  private Player mPlayer;
  private EditText mFirstName;
  private EditText mLastInitial;
  private Avatar mSelectAvatar;
  private View mSelectedAvatarView;
  private GridView mAvatarGrid;
  private FloatingActionButton mDoneFab;
  private boolean edit;

  public static SignInFragment newInstance(boolean edit) {
    Bundle args = new Bundle();
    args.putBoolean(ARG_EDIT, edit);
    SignInFragment fragment = new SignInFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      final int saveAvatarIndex = savedInstanceState.getInt(KEY_SELECTED_AVATAR_INDEX);
      if (saveAvatarIndex != GridView.INVALID_POSITION) {
        mSelectAvatar = Avatar.values()[saveAvatarIndex];
      }
    }
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View contentView = inflater.inflate(R.layout.fragment_sign_in, container, false);
    contentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6,
          int i7) {
        view.removeOnLayoutChangeListener(this);
        setUpGridView(getView());
      }
    });
    return contentView;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    if (mAvatarGrid != null) {
      outState.putInt(KEY_SELECTED_AVATAR_INDEX, mAvatarGrid.getCheckedItemPosition());
    } else {
      outState.putInt(KEY_SELECTED_AVATAR_INDEX, GridView.INVALID_POSITION);
    }
    super.onSaveInstanceState(outState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    assurePlayerInit();
    checkIsInEditMode();

    if (mPlayer == null || edit) {
      view.findViewById(R.id.empty).setVisibility(View.GONE);
      view.findViewById(R.id.content).setVisibility(View.VISIBLE);
      initContentViews(view);
      initContents();
    } else {
      final Activity activity = getActivity();
      CategorySelectionActivity.start(activity, mPlayer);
      activity.finish();
    }
    super.onViewCreated(view, savedInstanceState);
  }

  private void checkIsInEditMode() {
    final Bundle arguments = getArguments();
    // noinspection SimplifiableIfStatement
    if (arguments == null) {
      edit = false;
    } else {
      edit = arguments.getBoolean(ARG_EDIT, false);
    }
  }

  private void initContentViews(View view) {
    TextWatcher textWatcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        /* no-op */
      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // hiding the floating action button if text is empty
        if (charSequence.length() == 0) {
          mDoneFab.hide();
        }
      }

      @Override public void afterTextChanged(Editable editable) {
        if (isAvatarSelected() && isInputDataValid()) {
          mDoneFab.show();
        }
      }
    };

    mFirstName = (EditText) view.findViewById(R.id.first_name);
    mFirstName.addTextChangedListener(textWatcher);
    mLastInitial = (EditText) view.findViewById(R.id.last_initial);
    mLastInitial.addTextChangedListener(textWatcher);
    mDoneFab = (FloatingActionButton) view.findViewById(R.id.done);
    mDoneFab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        switch (view.getId()) {
          case R.id.done:
            savePlayer(getActivity());
            removeDoneFab(new Runnable() {
              @Override public void run() {
                if (null == mSelectedAvatarView) {
                  performSignInWithTransition(mAvatarGrid.getChildAt(mSelectAvatar.ordinal()));
                } else {
                  performSignInWithTransition(mSelectedAvatarView);
                }
              }
            });
            break;
          default:
            throw new UnsupportedOperationException(
                "The onCLick method had noew been implements for "
                    + getResources().getResourceEntryName(view.getId()));
        }
      }
    });
  }

  /**
   * 确定按钮点击时动画.
   * 2016/8/21 0021 14:43
   */
  private void removeDoneFab(@NonNull Runnable endAction) {
    ViewCompat.animate(mDoneFab)
        .scaleX(0)
        .scaleY(0)
        .setInterpolator(new FastOutLinearInInterpolator())
        .withEndAction(endAction)
        .start();
  }

  private void setUpGridView(View container) {
    mAvatarGrid = (GridView) container.findViewById(R.id.avatars);
    mAvatarGrid.setAdapter(new AvatarAdapter(getActivity()));
    mAvatarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        mSelectedAvatarView = view;
        mSelectAvatar = Avatar.values()[position];
        // showing the floating action button if input data is valid
        if (isInputDataValid()) {
          mDoneFab.show();
        }
      }
    });
    mAvatarGrid.setNumColumns(calculateSpanCount());
    if (mSelectAvatar != null) {
      mAvatarGrid.setItemChecked(mSelectAvatar.ordinal(), true);
    }
  }

  private void performSignInWithTransition(View v) {
    final Activity activity = getActivity();

    if (v == null || ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
      // Don't run a transition if the passed view is null;
      CategorySelectionActivity.start(activity, mPlayer);
      activity.finish();
      return;
    }

    if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
      activity.getWindow().getSharedElementEnterTransition().addListener(
          new TransitionListenerAdapter(){
            @Override public void onTransitionEnd(Transition transition) {
              activity.finish();
            }
          });

      final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true,
          new Pair<>(v, activity.getString(R.string.transition_avatar)));

      @SuppressWarnings("unchecked") ActivityOptionsCompat activityOptions =
          ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
      CategorySelectionActivity.start(activity, mPlayer, activityOptions);
    }
  }

  private void initContents() {
    assurePlayerInit();
    if (mPlayer != null) {
      mFirstName.setText(mPlayer.getFirstName());
      mLastInitial.setText(mPlayer.getLastInitial());
      mSelectAvatar = mPlayer.getAvatar();
    }
  }

  private void assurePlayerInit() {
    if (mPlayer == null) {
      mPlayer = PreferencesHelper.getPlayer(getActivity());
    }
  }

  private void savePlayer(Activity activity) {
    mPlayer = new Player(mFirstName.getText().toString(), mLastInitial.getText().toString(),
        mSelectAvatar);
    PreferencesHelper.writeToPreferences(activity, mPlayer);
  }

  private boolean isAvatarSelected() {
    return mSelectedAvatarView != null || mSelectAvatar != null;
  }

  private boolean isInputDataValid() {
    return PreferencesHelper.isInputDataValid(mFirstName.getText(), mLastInitial.getText());
  }

  /**
   * Calculate spans for avatar dynamically.
   * 2016/8/20 0020 14:35
   *
   * @return The recommended amount of columns.
   */
  private int calculateSpanCount() {
    int avatarSize = getResources().getDimensionPixelSize(R.dimen.size_fab);
    int avatarPadding = getResources().getDimensionPixelSize(R.dimen.spacing_double);
    return mAvatarGrid.getWidth() / (avatarSize + avatarPadding);
  }
}
