/**
 * *****************************************************************************
 *
 * @file GenericPowerFragment.java
 * @author BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
 * @brief About Application file
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
 * BlueNRG-Mesh is based on Motorola’s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 * <p>
 * *****************************************************************************
 */
package com.st.bluenrgmesh.view.fragments.other;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.GenericPowerOnOffModelClient;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

public class GenericPowerFragment extends BaseFragment implements View.OnClickListener {


    private LinearLayout getstatusrefreshLL;
    private TextView restoreTV,onTV,offTV;
    private Context context;
    private String address="";
    GenericPowerOnOffModelClient genericPowerOnOffModelClient;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.updateActionBarForFeatures((MainActivity)context, new GenericPowerFragment().getClass().getName());
        return inflater.inflate(R.layout.genericpower_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
    }

    private void initUi(View view) {
        try{
            address = (String) getArguments().getSerializable(getString(R.string.key_serializable));
        }catch (Exception e){}

        getstatusrefreshLL = (LinearLayout) view.findViewById(R.id.getstatusrefreshLL);
        restoreTV = (TextView) view.findViewById(R.id.restoreTV);
        onTV = (TextView) view.findViewById(R.id.onTV);
        offTV = (TextView) view.findViewById(R.id.offTV);
        getstatusrefreshLL.setOnClickListener(this);
        restoreTV.setOnClickListener(this);
        onTV.setOnClickListener(this);
        offTV.setOnClickListener(this);
        genericPowerOnOffModelClient = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().getGenericPowerOnOffModel();

    }


    final GenericPowerOnOffModelClient.GenericOnPowerUpStatusCallback genericOnPowerUpStatusCallback = new GenericPowerOnOffModelClient.GenericOnPowerUpStatusCallback() {
        @Override
        public void onOnPowerUpStatus(boolean b, ApplicationParameters.GenericOnPowerUpStatus genericOnPowerUpStatus) {
            ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("GenericPower command Response ==>" + genericOnPowerUpStatus, LoggerConstants.TYPE_RECEIVE);

            if(genericOnPowerUpStatus == ApplicationParameters.GenericOnPowerUpStatus.OFF) {
                Utils.showToast(context, "status==>" + genericOnPowerUpStatus);
                offTV.setBackgroundResource( R.drawable.round_background_type_three );
                restoreTV.setBackgroundResource(R.drawable.round_background_type_four);
                onTV.setBackgroundResource(R.drawable.round_background_type_four);
            }
            else if(genericOnPowerUpStatus == ApplicationParameters.GenericOnPowerUpStatus.RESTORE)
            {
                Utils.showToast(context, "status==>" + genericOnPowerUpStatus);
                restoreTV.setBackgroundResource( R.drawable.round_background_type_three );
                onTV.setBackgroundResource(R.drawable.round_background_type_four);
                offTV.setBackgroundResource(R.drawable.round_background_type_four);

            }else if(genericOnPowerUpStatus == ApplicationParameters.GenericOnPowerUpStatus.ON)
            {
                Utils.showToast(context, "status==>" + genericOnPowerUpStatus);
                onTV.setBackgroundResource( R.drawable.round_background_type_three);
                restoreTV.setBackgroundResource(R.drawable.round_background_type_four);
                offTV.setBackgroundResource(R.drawable.round_background_type_four);
            }else
            {
              //  xvxvczcz
               // Utils.showToast(context, "status==>" + "Error");
            }
        }
    };


    @Override
    public void onClick(View v) {

       if(v.getId()== R.id.getstatusrefreshLL){
           ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("GenericPower command sent ==>" + "GET", LoggerConstants.TYPE_SEND);

           ApplicationParameters.Address address_val = new ApplicationParameters.Address(Integer.parseInt(address));
            genericPowerOnOffModelClient.getGenericOnPowerUpGet(address_val,genericOnPowerUpStatusCallback);
       }else if(v.getId()== R.id.restoreTV){
           ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("GenericPower command sent ==>" + "RESTORE", LoggerConstants.TYPE_SEND);

           ApplicationParameters.Address address_val = new ApplicationParameters.Address(Integer.parseInt(address));
           ApplicationParameters.GenericOnPowerUpStatus genericOnPowerUpStatus_value = new ApplicationParameters.GenericOnPowerUpStatus(2,"RESTORE");
           genericPowerOnOffModelClient.setGenericOnPowerUpGet(true,address_val,genericOnPowerUpStatus_value,genericOnPowerUpStatusCallback);

       }else if(v.getId()== R.id.offTV){
           ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("GenericPower command sent ==>" + "OFF", LoggerConstants.TYPE_SEND);

           ApplicationParameters.Address address_val = new ApplicationParameters.Address(Integer.parseInt(address));
           ApplicationParameters.GenericOnPowerUpStatus genericOnPowerUpStatus_value = new ApplicationParameters.GenericOnPowerUpStatus(0,"OFF");
           genericPowerOnOffModelClient.setGenericOnPowerUpGet(true,address_val,genericOnPowerUpStatus_value,genericOnPowerUpStatusCallback);

       }else
       {
           ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("GenericPower command sent ==>" + "ON", LoggerConstants.TYPE_SEND);

           ApplicationParameters.Address address_val = new ApplicationParameters.Address(Integer.parseInt(address));
           ApplicationParameters.GenericOnPowerUpStatus genericOnPowerUpStatus_value = new ApplicationParameters.GenericOnPowerUpStatus(1,"ON");
           genericPowerOnOffModelClient.setGenericOnPowerUpGet(true,address_val,genericOnPowerUpStatus_value,genericOnPowerUpStatusCallback);
       }
    }
}
