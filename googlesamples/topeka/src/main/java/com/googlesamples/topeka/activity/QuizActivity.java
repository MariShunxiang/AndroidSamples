package com.googlesamples.topeka.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlesamples.topeka.R;
import com.googlesamples.topeka.fragment.QuizFragment;
import com.googlesamples.topeka.helper.ApiLevelHelper;
import com.googlesamples.topeka.model.Category;
import com.googlesamples.topeka.model.JsonAttributes;
import com.googlesamples.topeka.persistence.TopekaDatabaseHelper;
import com.googlesamples.topeka.widget.TextSharedElementCallback;
import com.sync.logger.Logger;
import java.util.List;

import static com.googlesamples.topeka.adapter.CategoryAdapter.DRAWABLE;

/**
 * Created by YH on 2016/8/13.
 */
public class QuizActivity extends AppCompatActivity {

  private static final String TAG              = "QuizActivity";
  private static final String IMAGE_CATEGORY   = "image_category_";
  private static final String STATE_IS_PLAYING = "isPlaying";
  private static final String FRAGMENT_TAG     = "Quiz";

  private Interpolator           mInterpolator;
  private Category               mCategory;
  private QuizFragment           mQuizFragment;
  private FloatingActionButton   mQuizFab;
  private boolean                mSavedStateIsPlaying;
  private ImageView              mIcon;
  private Animator               mCircularReveal;
  private ObjectAnimator         mColorChange;
  private CountingIdlingResource mCountingIdlingResource;
  private View                   mToolbarBack;

  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      switch (v.getId()) {
        case R.id.fab_quiz:

          break;
        case R.id.submitAnswer:

          break;
        case R.id.quiz_done:

          break;
        case R.id.back:
          onBackPressed();
          break;
        default:
          throw new UnsupportedOperationException(
              "OnClick has not been implemented for " + getResources().getResourceName(v.getId()));
      }
    }
  };

  public static Intent getStartIntent(Context context, Category category) {
    Intent starter = new Intent(context, QuizActivity.class);
    starter.putExtra(Category.TAG, category.getId());
    return starter;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    mCountingIdlingResource = new CountingIdlingResource("Quiz");
    String categoryId = getIntent().getStringExtra(Category.TAG);
    mInterpolator = new FastOutSlowInInterpolator();
    if (null != savedInstanceState) {
      mSavedStateIsPlaying = savedInstanceState.getBoolean(STATE_IS_PLAYING);
    }
    super.onCreate(savedInstanceState);
    populate(categoryId);
    int categoryNameTextSize = getResources() // 14sp
        .getDimensionPixelSize(R.dimen.category_item_text_size);
    int paddingStart = getResources().getDimensionPixelSize(R.dimen.spacing_double); // 16dp
    final int startDelay = getResources().getInteger(R.integer.toolbar_transition_duration);
    ActivityCompat.setEnterSharedElementCallback(this,
        new TextSharedElementCallback(categoryNameTextSize, paddingStart) {
          @Override public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements,
              List<View> sharedElementSnapshots) {
            super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
            mToolbarBack.setScaleX(0f);
            mToolbarBack.setScaleY(0f);
          }

          @Override public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements,
              List<View> sharedElementSnapshots) {
            super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            // Make sure to perform this animation after the transition has ended.
            ViewCompat.animate(mToolbarBack).setStartDelay(startDelay).scaleX(1f).scaleY(1f).alpha(1);
          }
        });
  }

  @Override protected void onResume() {
    if (mSavedStateIsPlaying) {
      mQuizFragment = (QuizFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
      if (!mQuizFragment.hasSolvedStateListener()) {
        mQuizFragment.setSolvedStateListener(getSolvedStateListener());
      }
      findViewById(R.id.quiz_fragment_container).setVisibility(View.VISIBLE);
      mQuizFab.hide();
      mIcon.setVisibility(View.GONE);
    } else {
      initQuizFragment();
    }
    super.onResume();
  }

  @Override public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    outState.putBoolean(STATE_IS_PLAYING, mQuizFab.getVisibility() == View.GONE);
    super.onSaveInstanceState(outState, outPersistentState);
  }

  @Override public void onBackPressed() {
    if (mIcon == null || mQuizFab == null) {
      // Skip the animation if icon or fab are not initialized.
      super.onBackPressed();
      return;
    }

    ViewCompat.animate(mToolbarBack).scaleX(0f).scaleY(0f).alpha(0).setDuration(100).start();

    // Scale the icon and fab to 0 size before calling onBackPressed calling onBackPressed if it exists.
    ViewCompat.animate(mIcon).scaleX(.7f).scaleY(.7f).alpha(0f).setInterpolator(mInterpolator).start();

    ViewCompat.animate(mQuizFab)
        .scaleX(0)
        .scaleY(0)
        .setInterpolator(mInterpolator)
        .setStartDelay(100)
        .setListener(new ViewPropertyAnimatorListenerAdapter() {
          @Override public void onAnimationEnd(View view) {
            if (isFinishing() || ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
              return;
            }
            QuizActivity.super.onBackPressed();
          }
        })
        .start();
  }

  private void startQuizFromClickOn(final View clickedView) {

  }

  @SuppressLint("NewApi") public void setToolbarElevation(boolean shouldElevate) {
    if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
      mToolbarBack.setElevation(shouldElevate ? getResources().getDimension(R.dimen.elevation_header) : 0);
    }
  }

  private void initQuizFragment() {
    if (mQuizFab != null) {
      return;
    }
    mQuizFragment = QuizFragment.newInstance(mCategory.getId(), getSolvedStateListener());
    // the toolbar should not hove more elevation than the content while playing
    setToolbarElevation(false);
  }

  private QuizFragment.SolvedStateListener getSolvedStateListener() {
    return new QuizFragment.SolvedStateListener() {
      @Override public void onCategorySolved() {
        setResultSolved();
        setToolbarElevation(true);
        displayDoneFab();
      }

      private void displayDoneFab() {
        /**
         *  We're re-using the already existing fab and give it some new values.
         *  This has to run delayed due to the queued animation to hide the fab initially.
         */
        if (null != mCircularReveal && mCircularReveal.isRunning()) {
          mCircularReveal.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
              showQuizFabWithDoneIcon();
              mCircularReveal.removeListener(this);
            }
          });
        } else {
          showQuizFabWithDoneIcon();
        }
      }

      private void showQuizFabWithDoneIcon() {
        mQuizFab.setImageResource(R.drawable.ic_tick);
        mQuizFab.setId(R.id.quiz_done);
        mQuizFab.setVisibility(View.VISIBLE);
        mQuizFab.setScaleX(0f);
        mQuizFab.setScaleY(0f);
        ViewCompat.animate(mQuizFab)
            .scaleX(1)
            .scaleY(1)
            .setInterpolator(mInterpolator)
            .setListener(null)
            .start();
      }

    };
  }

  private void setResultSolved() {
    Intent categoryIntent = new Intent();
    categoryIntent.putExtra(JsonAttributes.ID, mCategory.getId());
    setResult(R.id.solved, categoryIntent);
  }

  /**
   * Proceeds the quiz to it's next state.
   */
  public void proceed() {

  }

  /**
   * 只存在于测试目的，确保Espresso不会被混淆
   * Solely exists for testing purposes and making sure Espresso dose not get confused.
   */
  public void lockIdlingResource() {
    mCountingIdlingResource.increment();
  }

  private void submitAnswer() {
    mCountingIdlingResource.decrement();
    //if (!mQuizFragment.sh)

    setToolbarElevation(false);
  }

  private void populate(String categoryId) {
    if (null == categoryId) {
      Logger.w("Didn't find a category. Finishing");
      finish();
    }
    mCategory = TopekaDatabaseHelper.getCategoryWith(this, categoryId);
    setTheme(mCategory.getTheme().getStyleId());
    if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
      Window window = getWindow();
      window.setStatusBarColor(ContextCompat.getColor(this, mCategory.getTheme().getPrimaryDarkColor()));
    }
    initLayout(mCategory.getId());
    initToolbar(mCategory);
  }

  private void initLayout(String categoryId) {
    setContentView(R.layout.activity_quiz);
    //noinspection PrivateResource
    mIcon = (ImageView) findViewById(R.id.icon);
    int resId =
        getResources().getIdentifier(IMAGE_CATEGORY + categoryId, DRAWABLE, getApplicationContext().getPackageName());
    mIcon.setImageResource(resId);
    ViewCompat.animate(mIcon).scaleX(1).scaleY(1).alpha(1).setInterpolator(mInterpolator).setStartDelay(300).start();
    mQuizFab = (FloatingActionButton) findViewById(R.id.fab_quiz);
    mQuizFab.setImageResource(R.drawable.ic_play);
    if (mSavedStateIsPlaying) {
      mQuizFab.hide();
    } else {
      mQuizFab.show();
    }
    mQuizFab.setOnClickListener(mOnClickListener);
  }

  private void initToolbar(Category category) {
    mToolbarBack = findViewById(R.id.back);
    mToolbarBack.setOnClickListener(mOnClickListener);
    TextView titleView = (TextView) findViewById(R.id.category_title);
    titleView.setText(category.getName());
    titleView.setTextColor(ContextCompat.getColor(this, category.getTheme().getTextPrimaryColor()));
    if (mSavedStateIsPlaying) {
      // the toolbar should not have more elevation than the content while playing
      setToolbarElevation(false);
    }
  }

  @SuppressWarnings("unused") @VisibleForTesting public CountingIdlingResource getCountingIdlingResource() {
    return mCountingIdlingResource;
  }
}
