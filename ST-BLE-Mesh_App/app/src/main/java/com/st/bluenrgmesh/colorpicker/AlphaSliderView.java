/**
 * *****************************************************************************
 *
 * @file AlphaSliderView.java
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;


public class AlphaSliderView extends ColorSliderView {

    private Bitmap backgroundBitmap;
    private Canvas backgroundCanvas;

    public AlphaSliderView(Context context) {
        super(context);
    }

    public AlphaSliderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        backgroundBitmap = Bitmap.createBitmap((int) (w - 2 * selectorSize),
                (int) (h - selectorSize), Bitmap.Config.ARGB_8888);
        backgroundCanvas = new Canvas(backgroundBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       // Drawable drawable = CheckerboardDrawable.create();
      //  drawable.setBounds(0, 0, backgroundCanvas.getWidth(), backgroundCanvas.getHeight());
      //  drawable.draw(backgroundCanvas);
        canvas.drawBitmap(backgroundBitmap, selectorSize, selectorSize, null);
        super.onDraw(canvas);
    }

    @Override
    protected float resolveValue(int color) {
        return Color.alpha(color) / 255.f;
    }

    protected void configurePaint(Paint colorPaint) {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        int startColor = Color.HSVToColor(0, hsv);
        int endColor = Color.HSVToColor(255, hsv);
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);
    }

    protected int assembleColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        int alpha = (int) (currentValue * 255);
        return Color.HSVToColor(alpha, hsv);
    }
}
