// Copyright 2012 Square, Inc.
package com.apollo.edus.uilibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.apollo.edus.uilibrary.R;

/** Maintains an aspect ratio based on either width or height. Disabled by default. */
public class AspectRatioImageView extends ImageView {
  // NOTE: These must be kept in sync with the AspectRatioImageView attributes in attrs.xml.
  public static final int MEASUREMENT_WIDTH = 0;
  public static final int MEASUREMENT_HEIGHT = 1;

  private static final float DEFAULT_ASPECT_RATIO = 1f;
  private static final boolean DEFAULT_ASPECT_RATIO_ENABLED = false;
  private static final int DEFAULT_DOMINANT_MEASUREMENT = MEASUREMENT_WIDTH;

  private float heightWidthRatio;//height / width
  private boolean heightWidthRatioEnabled;
  private int dominantMeasurement;

  public AspectRatioImageView(Context context) {
    this(context, null);
  }

  public AspectRatioImageView(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
    heightWidthRatio = a.getFloat(R.styleable.AspectRatioImageView_heightWidthRatio, DEFAULT_ASPECT_RATIO);
    heightWidthRatioEnabled = a.getBoolean(R.styleable.AspectRatioImageView_heightWidthRatioEnabled,
        DEFAULT_ASPECT_RATIO_ENABLED);
    dominantMeasurement = a.getInt(R.styleable.AspectRatioImageView_dominantMeasurement,
        DEFAULT_DOMINANT_MEASUREMENT);
    a.recycle();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (!heightWidthRatioEnabled) return;

    int newWidth;
    int newHeight;
    switch (dominantMeasurement) {
      case MEASUREMENT_WIDTH:
        newWidth = getMeasuredWidth();
        newHeight = (int) (newWidth * heightWidthRatio);
        break;

      case MEASUREMENT_HEIGHT:
        newHeight = getMeasuredHeight();
        if(heightWidthRatio == 0){
            return;
        }else{
          newWidth = (int) (newHeight * 1.0 / heightWidthRatio);
        }
        break;

      default:
        throw new IllegalStateException("Unknown measurement with ID " + dominantMeasurement);
    }

    setMeasuredDimension(newWidth, newHeight);
  }

  /** Get the aspect ratio for this image view. */
  public float getAspectRatio() {
    return heightWidthRatio;
  }

  /** Set the aspect ratio for this image view. This will update the view instantly. */
  public void setHeightWidthRatio(float aspectRatio) {
    this.heightWidthRatio = aspectRatio;
    if (heightWidthRatioEnabled) {
      requestLayout();
    }
  }

  /** Get whether or not forcing the aspect ratio is enabled. */
  public boolean getHeightWidthRatioEnabled() {
    return heightWidthRatioEnabled;
  }

  /** set whether or not forcing the aspect ratio is enabled. This will re-layout the view. */
  public void setHeightWidthRatioEnabled(boolean heightWidthRatioEnabled) {
    if(this.heightWidthRatioEnabled != heightWidthRatioEnabled){
        this.heightWidthRatioEnabled = heightWidthRatioEnabled;
        requestLayout();
    }
  }

  /** Get the dominant measurement for the aspect ratio. */
  public int getDominantMeasurement() {
    return dominantMeasurement;
  }

  /**
   * Set the dominant measurement for the aspect ratio.
   *
   * @see #MEASUREMENT_WIDTH
   * @see #MEASUREMENT_HEIGHT
   */
  public void setDominantMeasurement(int dominantMeasurement) {
    if (dominantMeasurement != MEASUREMENT_HEIGHT && dominantMeasurement != MEASUREMENT_WIDTH) {
      throw new IllegalArgumentException("Invalid measurement type.");
    }
    this.dominantMeasurement = dominantMeasurement;
    if(this.heightWidthRatioEnabled){
        requestLayout();
    }
  }
}