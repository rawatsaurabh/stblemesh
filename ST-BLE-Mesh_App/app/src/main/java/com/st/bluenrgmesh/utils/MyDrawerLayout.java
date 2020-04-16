/**
 ******************************************************************************
 * @file    MyDrawerLayout.java
 * @author  BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
 * @brief   User Application file
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; COPYRIGHT(c) 2017 STMicroelectronics</center></h2>
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of STMicroelectronics nor the names of its contributors
 *      may be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
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
 *
 * BlueNRG-Mesh is based on Motorola’s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 *
 ******************************************************************************
 */

package com.st.bluenrgmesh.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.drawerlayout.widget.DrawerLayout;

public class MyDrawerLayout extends DrawerLayout {
    Context context;

    public MyDrawerLayout(Context context) {
        this(context, null);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        // if the drawer is open already, pass motionEvent upwards
        if (this.isDrawerOpen(Gravity.START)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        // sensitive area is set to 1/4 of the drawer's full width
        // adopt to your needs
        int sensitiveWidth = ((Activity) context).findViewById(R.id.drawer_layout).getWidth() / 8;
        // if the drawer is not (fully) open AND
        // motionEvent.getX() is close to the left edge,
        // fully open the drawer and pass motionEvent upwards
        if (motionEvent.getX() < sensitiveWidth) {
            this.openDrawer(Gravity.START);
            return super.onInterceptTouchEvent(motionEvent);
        }
        // otherwise eat motionEvent
        return false;
    }*/
}
