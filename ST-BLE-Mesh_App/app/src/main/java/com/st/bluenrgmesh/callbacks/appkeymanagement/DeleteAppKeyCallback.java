/**
 ******************************************************************************
 * @file    DeleteAppKeyCallback.java
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
package com.st.bluenrgmesh.callbacks.appkeymanagement;

import android.content.Context;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;

public class DeleteAppKeyCallback {

    private final Context context;
    private final String appKeyStr;
    private final Element element;
    private final AppKey appKey;
    private final ApplicationParameters.KeyPair keyPair;
    private final int appKeyIndex;
    byte[] bytes;

    public DeleteAppKeyCallback(final Context context, Element element, AppKey appKey)
    {
        this.context = context;
        this.appKey = appKey;
        this.appKeyStr = appKey.getKey();
        this.element = element;
        bytes = Utils.hexStringToByteArray(appKeyStr);
        this.appKeyIndex = appKey.getIndex();
        keyPair = new ApplicationParameters.KeyPair(0, appKey.getIndex());


        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) context).showPopUpForProxy(context, "Deleting AppKey...", true);
            }
        });

        deleteKey();

    }

    private void deleteKey() {

        UserApplication.trace("Deleting AppKey...: " + element.getParentNodeAddress());
        //short mValueParent = mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress())).mValue;
        int mValueParent = Integer.parseInt(element.getParentNodeAddress());

        MainActivity.network.getConfigurationModelClient().deleteAppKey(
                new ApplicationParameters.Address(mValueParent),
                keyPair,
                new ConfigurationModelClient.AppKeyStatusCallback() {

                    @Override
                    public void onAppKeyStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.KeyPair index) {
                        if (!timeout) {
                            if (status == ApplicationParameters.Status.SUCCESS) {
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MainActivity) context).showPopUpForProxy(context, "Appkey Deleted Successfully.", false);
                                    }
                                });
                                UserApplication.trace("Deleting Appkey Successfull for Key : " + appKeyIndex);
                                ((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(), "Delete Done", context.getResources().getInteger(R.integer.APP_KEY_DELETE), null,false, status);

                            }  else {
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MainActivity) context).showPopUpForProxy(context, "Delete Operation Failed.", false);
                                    }
                                });
                                ((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(), "Delete Failed", context.getResources().getInteger(R.integer.APP_KEY_DELETE), null,false, status);
                                UserApplication.trace("Deleting Appkey Failed for Key : " + appKeyIndex);
                            }
                        } else {
                            ((MainActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) context).showPopUpForProxy(context, "Timeout.", false);
                                }
                            });
                            //((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(), "Delete Timeout", context.getResources().getInteger(R.integer.APP_KEY_DELETE), null,false, status);
                            UserApplication.trace("Deleting Appkey Timeout for Key : " + appKeyIndex);
                        }
                    }
                });
    }
}
