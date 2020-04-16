/**
 * *****************************************************************************
 *
 * @file ColorWheelSelector.java
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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import static com.st.bluenrgmesh.colorpicker.Constants.SELECTOR_RADIUS_DP;

public class ColorWheelSelector extends View {

    private Paint selectorPaint;
    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 5;
    private PointF currentPoint = new PointF();

    public ColorWheelSelector(Context context) {
        this(context, null);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setColor(Color.WHITE);
        selectorPaint.setStyle(Paint.Style.STROKE);
        selectorPaint.setStrokeWidth(15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
      /* canvas.drawLine(currentPoint.x - selectorRadiusPx, currentPoint.y,
                currentPoint.x + selectorRadiusPx, currentPoint.y, selectorPaint);
        canvas.drawLine(currentPoint.x, currentPoint.y - selectorRadiusPx,
                currentPoint.x, currentPoint.y + selectorRadiusPx, selectorPaint);*/
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 0.66f, selectorPaint);
    }

    public void setSelectorRadiusPx(float selectorRadiusPx) {
        this.selectorRadiusPx = selectorRadiusPx;
    }

    public void setCurrentPoint(PointF currentPoint) {
        this.currentPoint = currentPoint;
        invalidate();
    }
}
