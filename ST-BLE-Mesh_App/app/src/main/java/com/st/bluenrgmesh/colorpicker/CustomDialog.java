/**
 * *****************************************************************************
 *
 * @file CustomDialog.java
 * @author BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
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

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.graphics.ColorUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.LightHSLModelClient;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.logger.LoggerConstants;

import java.util.Locale;



public class CustomDialog extends Dialog implements ColorObserver, SeekBar.OnSeekBarChangeListener {
    private final int node_address;
    public Activity activity;
    public Dialog d;
    public TextView yes, no;
    private ColorPickerView colorPicker;
    private View pickedColor;
    private float[] hsl;
    private LightHSLModelClient lightHSLModelClient;
    private static final int LIGHTING_LIGHTNESS_MAX = 65535;
    private boolean is_command_inprogress = false;
    private EditText hueET, saturationET, LightnessET;
    private SeekBar hueseekbar, saturationseekBar, LightnessseekBar;
    private float lightness_value_text,saturation_value_text,hue_value_text;

    public CustomDialog(Activity a, int node_address) {
        super(a);
        this.activity = a;
        this.node_address = node_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_color_wheel);
        init();
    }

    private void init() {
        lightHSLModelClient = ((MainActivity) activity).app.mConfiguration.getNetwork().getLightnessHSLModel();
        hsl = new float[3];
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        hueET = findViewById(R.id.hueET);
        saturationET = findViewById(R.id.saturationET);
        LightnessET = findViewById(R.id.LightnessET);
        hueseekbar = findViewById(R.id.hueseekbar);
        saturationseekBar = findViewById(R.id.saturationseekBar);
        LightnessseekBar = findViewById(R.id.LightnessseekBar);
        pickedColor = findViewById(R.id.pickedColor);
        colorPicker = findViewById(R.id.colorPicker);
        colorPicker.subscribe(CustomDialog.this);

        setdefaultValues();
        setListeners();

       /* if (!is_command_inprogress) {
            is_command_inprogress = true;
            sendHSLCommand(true);
        }*/
    }

    private void setListeners() {
        hueseekbar.setOnSeekBarChangeListener(this);
        saturationseekBar.setOnSeekBarChangeListener(this);
        LightnessseekBar.setOnSeekBarChangeListener(this);
    }

    private void setdefaultValues() {
       // hueseekbar.setMin(0);
        hueseekbar.setMax(359);
       // saturationseekBar.setMin(0);
        saturationseekBar.setMax(100);
       // LightnessseekBar.setMin(0);
        LightnessseekBar.setMax(100);
        LightnessseekBar.setProgress(50);
        LightnessET.setText("50" +"%");

    }

    private String colorHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        Log.e("COLOR=>", "\nR=>" + r + "\n G=>" + g + " \nB=>" + b);
        ColorUtils.RGBToHSL(r, g, b, hsl);
        Log.i("COLOR=>", "\nH=>" + hsl[0] + "\n S=>" + hsl[1] + " \nL=>" + hsl[2]);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }


    @Override
    public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
        colorHex(color);
        pickedColor.setBackgroundColor(color);
        if (shouldPropagate && !is_command_inprogress) {
            is_command_inprogress = true;
            sendHSLCommand(false);
        }
    }

    private void sendHSLCommand(boolean is_firs_time) {
        int hue_original_value = getHueOriginalValueFromDegree();
        hue_value_text =  hsl[0];
        saturation_value_text = hsl[1];
        if(is_firs_time) {
            lightness_value_text = 0.50f;
        }else
        {
            lightness_value_text = hsl[2];
        }
        ApplicationParameters.Address address = new ApplicationParameters.Address(node_address);
        ApplicationParameters.Hue hue = new ApplicationParameters.Hue(hue_original_value);
        ApplicationParameters.Saturation saturation = new ApplicationParameters.Saturation((int) (saturation_value_text * LIGHTING_LIGHTNESS_MAX));
        ApplicationParameters.Lightness lightness = new ApplicationParameters.Lightness((int) (lightness_value_text * LIGHTING_LIGHTNESS_MAX));

        ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(activity));
        ((MainActivity) activity).mUserDataRepository.getNewDataFromRemote("HSL set Light Command Send==>" + node_address, LoggerConstants.TYPE_SEND);
        lightHSLModelClient.setLightHSL(Utils.isReliableEnabled(activity), address, lightness, hue, saturation, tid, null, Utils.isReliableEnabled(activity) ? mLightHSLStatusCallback : null);


       updateUI(hue_original_value);
    }

    private int getHueOriginalValueFromDegree() {
        float hue_degree_value = (float) (hsl[0]);
        float hue_degree = (float) (hue_degree_value / 360f);
        return  (int) (hue_degree * LIGHTING_LIGHTNESS_MAX);
    }

    private void updateUI(int hue_original_value) {



        hueET.setText((int)hue_value_text+"°");
        saturationET.setText(""+(int) (saturation_value_text * 100) +" %");
        LightnessET.setText(""+(int) (lightness_value_text * 100)+" %");

        hueseekbar.setProgress((int)hue_value_text);
        saturationseekBar.setProgress((int) (saturation_value_text * 100));
        LightnessseekBar.setProgress((int) (lightness_value_text * 100));
        if(!Utils.isReliableEnabled(activity)){
            is_command_inprogress=false;
        }
    }

    public LightHSLModelClient.LightHSLStatusCallback mLightHSLStatusCallback = new LightHSLModelClient.LightHSLStatusCallback() {
        @Override

        public void onLightHSLStatus(boolean timeout, ApplicationParameters.Lightness lightness, ApplicationParameters.Hue hue,
                                     ApplicationParameters.Saturation saturation,
                                     ApplicationParameters.Time remainingTime) {
            is_command_inprogress = false;
            if (timeout) {
                ((MainActivity) activity).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>" + "timeout", LoggerConstants.TYPE_RECEIVE);
            } else {
                ((MainActivity) activity).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);

            }

        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.hueseekbar) {
            hueET.setText(""+seekBar.getProgress() + "°");
            hueseekbar.setProgress(seekBar.getProgress());
            float hsl_new [] = new float[3];
            hsl_new[0] =(float) seekBar.getProgress();
            hsl_new[1] =(float) saturation_value_text;
            hsl_new[2] = (float) lightness_value_text;
             colorPicker.setInitialColor(ColorUtils.HSLToColor(hsl_new));
        }else  if (seekBar.getId() == R.id.saturationseekBar) {
            saturationET.setText(""+seekBar.getProgress() + "%");
            saturationseekBar.setProgress(seekBar.getProgress());
            float hsl_new [] = new float[3];
            hsl_new[0] =(float) hue_value_text;
            hsl_new[1] =(float) seekBar.getProgress()/100;
            hsl_new[2] = (float) lightness_value_text;
            colorPicker.setInitialColor(ColorUtils.HSLToColor(hsl_new));
        }else
        {
            LightnessET.setText(""+seekBar.getProgress() + "%");
            LightnessseekBar.setProgress(seekBar.getProgress());
            float hsl_new [] = new float[3];
            hsl_new[0] =(float) hue_value_text;
            hsl_new[1] =(float) saturation_value_text;
            hsl_new[2] = (float) seekBar.getProgress()/100;
            colorPicker.setInitialColor(ColorUtils.HSLToColor(hsl_new));
        }
    }
}