/**
 * *****************************************************************************
 *
 * @file ColorPickerView.java
 * @author BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
 * @brief User Application file
 * *****************************************************************************
 * @attention <h2><center>&copy; COPYRIGHT(c) 2017 STMicroelectronics</center></h2>
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3. Neither the name of STMicroelectronics nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * BlueNRG-Mesh is based on Motorolaâ€™s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 * <p>
 * *****************************************************************************
 */

package com.st.bluenrgmesh.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.st.bluenrgmesh.BuildConfig;
import com.st.bluenrgmesh.R;

import java.util.ArrayList;
import java.util.List;


public class ColorPickerView extends LinearLayout implements ColorObservable {

    private ColorWheelView colorWheelView;
    private BrightnessSliderView brightnessSliderView;
    private AlphaSliderView alphaSliderView;
    private ColorObservable observableOnDuty;
    private boolean onlyUpdateOnTouchEventUp;

    private int initialColor = Color.GRAY;

    private int sliderMargin;
    private int sliderHeight;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
        boolean enableAlpha = typedArray.getBoolean(R.styleable.ColorPickerView_enableAlpha, false);
        boolean enableBrightness = typedArray.getBoolean(R.styleable.ColorPickerView_enableBrightness, true);
        onlyUpdateOnTouchEventUp = typedArray.getBoolean(R.styleable.ColorPickerView_onlyUpdateOnTouchEventUp, false);
        typedArray.recycle();

        colorWheelView = new ColorWheelView(context);
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (8 * density);
        sliderMargin = 2 * margin;
        sliderHeight = (int) (24 * density);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(colorWheelView, params);

        setEnabledBrightness(enableBrightness);
        setEnabledAlpha(enableAlpha);
        setInitialColor(initialColor);
        setPadding(margin, margin, margin, margin);
        brightnessSliderView.setVisibility(GONE);
     //   alphaSliderView.setVisibility(GONE);
    }

    public void setOnlyUpdateOnTouchEventUp(boolean onlyUpdateOnTouchEventUp) {
        this.onlyUpdateOnTouchEventUp = onlyUpdateOnTouchEventUp;
        updateObservableOnDuty();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (BuildConfig.DEBUG) {
            //Log.d("maxWidth: %d, maxHeight: %d", maxWidth +"==>"+ maxHeight);
        }

        int desiredWidth = maxHeight - (getPaddingTop() + getPaddingBottom()) + (getPaddingLeft() + getPaddingRight());
        if (brightnessSliderView != null) {
            desiredWidth -= (sliderMargin + sliderHeight);
        }
        if (alphaSliderView != null){
            desiredWidth -= (sliderMargin + sliderHeight);
        }

        if (BuildConfig.DEBUG) {
            Log.d("desiredWidth: %d", ""+desiredWidth);
        }

        int width = Math.min(maxWidth, desiredWidth);
        int height = width - (getPaddingLeft() + getPaddingRight()) + (getPaddingTop() + getPaddingBottom());
        if (brightnessSliderView != null) {
            height += (sliderMargin + sliderHeight);
        }
        if (alphaSliderView != null) {
            height += (sliderMargin + sliderHeight);
        }

        if (BuildConfig.DEBUG) {
            Log.d("width: %d, height: %d", width+"==>"+ height);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    public void setInitialColor(int color) {
        initialColor = color;
        colorWheelView.setColor(color, true);
    }

    public void setEnabledBrightness(boolean enable) {
        if (enable) {
            if (brightnessSliderView == null) {
                brightnessSliderView = new BrightnessSliderView(getContext());
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight);
                params.topMargin = sliderMargin;
                addView(brightnessSliderView, 1, params);
            }
            brightnessSliderView.bind(colorWheelView);
            updateObservableOnDuty();
        } else {
            if (brightnessSliderView != null) {
                brightnessSliderView.unbind();
                removeView(brightnessSliderView);
                brightnessSliderView = null;
            }
            updateObservableOnDuty();
        }

        if (alphaSliderView != null) {
            setEnabledAlpha(true);
        }
    }

    public void setEnabledAlpha(boolean enable) {
        if (enable) {
            if (alphaSliderView == null) {
                alphaSliderView = new AlphaSliderView(getContext());
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight);
                params.topMargin = sliderMargin;
                addView(alphaSliderView, params);
            }

            ColorObservable bindTo = brightnessSliderView;
            if (bindTo == null) {
                bindTo = colorWheelView;
            }
            alphaSliderView.bind(bindTo);
            updateObservableOnDuty();
        } else {
            if (alphaSliderView != null) {
                alphaSliderView.unbind();
                removeView(alphaSliderView);
                alphaSliderView = null;
            }
            updateObservableOnDuty();
        }
    }

    private void updateObservableOnDuty() {
        if (observableOnDuty != null) {
            for (ColorObserver observer: observers) {
                observableOnDuty.unsubscribe(observer);
            }
        }

        colorWheelView.setOnlyUpdateOnTouchEventUp(false);
        if (brightnessSliderView != null) {
            brightnessSliderView.setOnlyUpdateOnTouchEventUp(false);
        }
        if (alphaSliderView != null) {
            alphaSliderView.setOnlyUpdateOnTouchEventUp(false);
        }

        if (brightnessSliderView == null && alphaSliderView == null) {
            observableOnDuty = colorWheelView;
            colorWheelView.setOnlyUpdateOnTouchEventUp(onlyUpdateOnTouchEventUp);
        } else {
            if (alphaSliderView != null) {
                observableOnDuty = alphaSliderView;
                alphaSliderView.setOnlyUpdateOnTouchEventUp(onlyUpdateOnTouchEventUp);
            } else {
                observableOnDuty = brightnessSliderView;
                brightnessSliderView.setOnlyUpdateOnTouchEventUp(onlyUpdateOnTouchEventUp);
            }
        }

        if (observers != null) {
            for (ColorObserver observer : observers) {
                observableOnDuty.subscribe(observer);
                observer.onColor(observableOnDuty.getColor(), false, true);
            }
        }
    }

    public void reset() {
        colorWheelView.setColor(initialColor, true);
    }

    List<ColorObserver> observers = new ArrayList<>();

    @Override
    public void subscribe(ColorObserver observer) {
        observableOnDuty.subscribe(observer);
        observers.add(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        observableOnDuty.unsubscribe(observer);
        observers.remove(observer);
    }

    @Override
    public int getColor() {
        return observableOnDuty.getColor();
    }
}
