/**
 ******************************************************************************
 * @file    DeviceInfoFragment.java
 * @author  BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
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


package com.st.bluenrgmesh.view.fragments.other.meshmodels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

public class DeviceInfoFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_info, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new DeviceInfoFragment().getClass().getName());
        initUi();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private void initUi() {

    try {
        //String deviceName = Build.DEVICE;
        //String reqString = Build.MANUFACTURER
        //        + " " + Build.MODEL + " " + Build.VERSION.RELEASE
        //        + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();


        //==============hardware information===================
        TextView name = (TextView) view.findViewById(R.id.device_name);
        name.setText(Build.MANUFACTURER + " " + Build.DEVICE);

        TextView version = (TextView) view.findViewById(R.id.android_version);
        version.setText(Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());
        //version.setText(Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());
        version.setText(Build.VERSION.RELEASE);

        TextView manufacturer = (TextView) view.findViewById(R.id.manufacturer);
        manufacturer.setText(Build.MANUFACTURER);

        TextView model = (TextView) view.findViewById(R.id.model_name);
        model.setText(Build.MODEL);

        TextView build_version = (TextView) view.findViewById(R.id.build_version);
        build_version.setText(Build.VERSION.RELEASE);
        build_version.setText(Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());

        TextView board = (TextView) view.findViewById(R.id.board);
        board.setText(Build.BOARD);

        TextView product = (TextView) view.findViewById(R.id.product_name);
        product.setText(Build.MANUFACTURER + " " + Build.MODEL);

        //===============BLE information ===================


            //BLE support
            //boolean flag=true;
            TextView bleSupport = (TextView) view.findViewById(R.id.ble_supported);
            if (!BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported()){
                if (BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser()!=null){
                    bleSupport.setTextColor(Color.RED);
                    bleSupport.setText("NO");
                }
            }
            else {
                bleSupport.setTextColor(Color.GREEN);
                bleSupport.setText("YES");
            }


            //Native HID supported
            TextView nativeSupport = (TextView) view.findViewById(R.id.native_hid_support);
            if (BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser()!=null && Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT ){
                nativeSupport.setTextColor(Color.GREEN);
                nativeSupport.setText("YES");
            }
            else {
                nativeSupport.setTextColor(Color.RED);
                nativeSupport.setText("NO");
            }

            //Lollipop level api supported
        TextView lollipopSupport = (TextView) view.findViewById(R.id.lollipop_scanner_api_support);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            lollipopSupport.setTextColor(Color.GREEN);
            lollipopSupport.setText("YES");
        }
        else {
            lollipopSupport.setTextColor(Color.RED);
            lollipopSupport.setText("NO");
        }

            //Peripheral Mode
            TextView peripheralmode = (TextView) view.findViewById(R.id.peripheral_support);
            if (BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser()!=null){
                peripheralmode.setTextColor(Color.GREEN);
                peripheralmode.setText("YES");
            }
            else {
                peripheralmode.setTextColor(Color.RED);
                peripheralmode.setText("NO");
            }

            //Multiple advertisement Support
            TextView multipleAdvSupp = (TextView) view.findViewById(R.id.multiple_adv_support);
            if (!BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported()) {
                multipleAdvSupp.setTextColor(Color.RED);
                multipleAdvSupp.setText("NO");
            } else {
                multipleAdvSupp.setTextColor(Color.GREEN);
                multipleAdvSupp.setText("YES");
            }

            //High Speed (PHY 2M) Support
            TextView le2msupport = (TextView) view.findViewById(R.id.high_speed_support);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!BluetoothAdapter.getDefaultAdapter().isLe2MPhySupported()) {
                le2msupport.setTextColor(Color.RED);
                le2msupport.setText("NO");
            } else {
                le2msupport.setTextColor(Color.GREEN);
                le2msupport.setText("YES");
            }
        }
        else{
            TextView le2msupportHead = (TextView)view.findViewById(R.id.high_speed_support_head);
            le2msupport.setVisibility(View.GONE);
            le2msupportHead.setVisibility(View.GONE);
        }
            //Long Range (PHY 2M) supported
            TextView longrangeSupp = (TextView) view.findViewById(R.id.long_range_support);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!BluetoothAdapter.getDefaultAdapter().isLeCodedPhySupported()) {
                longrangeSupp.setTextColor(Color.RED);
                longrangeSupp.setText("NO");
            } else {
                longrangeSupp.setTextColor(Color.GREEN);
                longrangeSupp.setText("YES");
            }
        }
        else{
            TextView longrangeSuppHead = (TextView)view.findViewById(R.id.long_range_support_head);
            longrangeSupp.setVisibility(View.GONE);
            longrangeSuppHead.setVisibility(View.GONE);
        }

            //Offloaded Filtering Supported
            TextView offFilterSupport = (TextView) view.findViewById(R.id.offloaded_filtering_supported);
            if (!BluetoothAdapter.getDefaultAdapter().isOffloadedFilteringSupported()) {
                offFilterSupport.setTextColor(Color.RED);
                offFilterSupport.setText("NO");
            } else {
                offFilterSupport.setTextColor(Color.GREEN);
                offFilterSupport.setText("YES");
            }


            //Offloaded Scan Batching Supported
            TextView offloadedScanSupport = (TextView) view.findViewById(R.id.offloaded_scan_support);
            if (!BluetoothAdapter.getDefaultAdapter().isOffloadedScanBatchingSupported()) {
                offloadedScanSupport.setTextColor(Color.RED);
                offloadedScanSupport.setText("NO");
            } else {
                offloadedScanSupport.setTextColor(Color.GREEN);
                offloadedScanSupport.setText("YES");
            }

            //Extended Advertisement Supported
        TextView extendAdvSupp = (TextView) view.findViewById(R.id.extended_adv_support);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (BluetoothAdapter.getDefaultAdapter().isLeExtendedAdvertisingSupported()) {
                extendAdvSupp.setTextColor(Color.GREEN);
                extendAdvSupp.setText("YES");
            } else {
                extendAdvSupp.setTextColor(Color.RED);
                extendAdvSupp.setText("NO");
            }
        }
        else {
            TextView extendAdvSuppHead = (TextView) view.findViewById(R.id.extended_adv_support_head);
            extendAdvSupp.setVisibility(View.GONE);
            extendAdvSuppHead.setVisibility(View.GONE);
        }

            //Periodic Advertisement Supported
            TextView periodicSupport = (TextView) view.findViewById(R.id.periodic_adv_support);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!BluetoothAdapter.getDefaultAdapter().isLePeriodicAdvertisingSupported()) {
                periodicSupport.setTextColor(Color.RED);
                periodicSupport.setText("NO");
            } else {
                periodicSupport.setTextColor(Color.GREEN);
                periodicSupport.setText("YES");
            }
        }
            else {
            TextView periodicSupportHead = (TextView) view.findViewById(R.id.periodic_adv_support_head);
            periodicSupport.setVisibility(View.GONE);
            periodicSupportHead.setVisibility(View.GONE);
        }

            //==============Maximum advertising data length==================
            TextView advdatalen = (TextView) view.findViewById(R.id.adv_data_len);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            advdatalen.setText(String.valueOf(BluetoothAdapter.getDefaultAdapter().getLeMaximumAdvertisingDataLength()));

        }
        else {
            TextView advdatalenHead = (TextView) view.findViewById(R.id.adv_data_len_head);
            advdatalen.setVisibility(View.GONE);
            advdatalenHead.setVisibility(View.GONE);
        }

        //==============Screen Information==================

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = height / density;
        float dpWidth  = width / density;

        //Resolution(Density)

        int dens= getResources().getDisplayMetrics().densityDpi;
        String out="";
        switch(dens)
        {
            case DisplayMetrics.DENSITY_LOW:
                out = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                out = "MDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                out = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                out = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                out = "XXHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                out = "XXXHDPI";
                break;
        }
        TextView resolution = (TextView) view.findViewById(R.id.resolution);
        resolution.setText(out);

        //Dimensions in pixels
        TextView dimPix = (TextView) view.findViewById(R.id.dim_pix);
        dimPix.setText(width + " X " + height);

        //Dimensions in dip
        TextView dimDip = (TextView) view.findViewById(R.id.dim_dip);
        dimDip.setText((int) dpWidth + " X " + (int) dpHeight);























        //Size of the screen
        TextView size = (TextView) view.findViewById(R.id.size);
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String msg="";
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                msg = "Large";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                msg = "Normal";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                msg = "Small";
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                msg = "XLarge";
                break;
        }
        size.setText(msg);

        //wide color gamut
        TextView colorGamut = (TextView) view.findViewById(R.id.wide_color_gamut);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            if (!getResources().getConfiguration().isScreenWideColorGamut()) {
                colorGamut.setText("Not Supported");
            }
            else {
                colorGamut.setText("Supported");
            }
        }
        else {
            TextView colorGamutHead = (TextView) view.findViewById(R.id.wide_color_gamut_head);
            colorGamut.setVisibility(View.GONE);
            colorGamutHead.setVisibility(View.GONE);
        }

        //HDR
        TextView hdr = (TextView) view.findViewById(R.id.hdr);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            if (!getResources().getConfiguration().isScreenHdr()) {
                hdr.setText("Not Supported");
            }
            else {
                hdr.setText("Supported");
            }
        }
        else {
            TextView hdrHead = (TextView) view.findViewById(R.id.hdr_head);
            hdr.setVisibility(View.GONE);
            hdrHead.setVisibility(View.GONE);
        }













    }
    catch(Exception e){
      e.printStackTrace();
        }
    }
}
