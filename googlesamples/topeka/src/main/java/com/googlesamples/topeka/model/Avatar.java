package com.googlesamples.topeka.model;

import android.support.annotation.DrawableRes;
import com.googlesamples.topeka.R;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public enum Avatar {

  ONE(R.drawable.avator_1), TWO(R.drawable.avator_2), THREE(R.drawable.avator_3), FOUR(R.drawable.avator_4), FIVE(
      R.drawable.avator_5), SIX(R.drawable.avator_6), SEVEN(R.drawable.avator_7), EIGHT(R.drawable.avator_8), NINE(
      R.drawable.avator_9), TEN(R.drawable.avator_10), ELEVEN(R.drawable.avator_11), TWELVE(
      R.drawable.avator_12), THIRTEEN(R.drawable.avator_13), FOURTEEN(R.drawable.avator_14), FIFTEEN(
      R.drawable.avator_15), SIXTEEN(R.drawable.avator_16);

  private static final String TAG = "Avatar";

  private final int mResId;

  Avatar(@DrawableRes final int resId) {
    mResId = resId;
  }

  @DrawableRes public int getDrawableId() {
    return mResId;
  }

  public String getNameForAccessibility() {
    return TAG + " " + ordinal() + 1;
  }

  @Override public String toString() {
    return super.toString();
  }
}
