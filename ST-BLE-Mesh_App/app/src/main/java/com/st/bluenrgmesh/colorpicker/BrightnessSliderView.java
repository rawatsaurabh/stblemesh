/**
 * *****************************************************************************
 *
 * @file BrightnessSliderView.java
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

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class BrightnessSliderView extends ColorSliderView {

    public BrightnessSliderView(Context context) {
        super(context);
    }

    public BrightnessSliderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BrightnessSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected float resolveValue(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv[2];
    }

    protected void configurePaint(Paint colorPaint) {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        hsv[2] = 0;
        int startColor = Color.HSVToColor(hsv);
        hsv[2] = 1;
        int endColor = Color.HSVToColor(hsv);
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);
    }

    protected int assembleColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        hsv[2] = currentValue;
        return Color.HSVToColor(hsv);
    }
}
