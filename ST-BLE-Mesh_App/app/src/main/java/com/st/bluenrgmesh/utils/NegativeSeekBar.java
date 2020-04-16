/**
 ******************************************************************************
 * @file    NegativeSeekBar.java
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
import android.widget.SeekBar;

public class NegativeSeekBar extends SeekBar {

    protected int minimumValue = 0;
    protected int maximumValue = 0;
    protected OnSeekBarChangeListener listener;

    public NegativeSeekBar(Context context){
        super(context);
        setUpInternalListener();
    }

    public NegativeSeekBar(Context context, AttributeSet attrs){
        super(context, attrs);
        setUpInternalListener();
    }

    public NegativeSeekBar(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        setUpInternalListener();
    }

    public void setMin(int min){
        this.minimumValue = min;
        super.setMax(maximumValue - minimumValue);
    }

    public void setMax(int max){
        this.maximumValue = max;
        super.setMax(maximumValue - minimumValue);
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener){
        this.listener = listener;
    }

    private void setUpInternalListener(){
        super.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(listener != null){
                    listener.onProgressChanged(seekBar, minimumValue + i, b);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(listener != null)
                    listener.onStartTrackingTouch(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(listener != null)
                    listener.onStopTrackingTouch(seekBar);
            }
        });
    }
}