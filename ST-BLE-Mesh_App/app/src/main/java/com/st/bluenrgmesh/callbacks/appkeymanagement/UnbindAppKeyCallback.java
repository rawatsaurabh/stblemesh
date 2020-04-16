/**
 ******************************************************************************
 * @file    UnbindAppKeyCallback.java
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
package com.st.bluenrgmesh.callbacks.appkeymanagement;

import android.content.Context;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.utils.ModelRepository;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;

public class UnbindAppKeyCallback {

    private final Context context;
    private final int appKeyIndex;
    private final String appKey;
    private final Model model;
    private final Element element;
    private boolean isUnbindingSuccess = false;
    private boolean isVendorSelected = false;

    public UnbindAppKeyCallback(final Context context, Element element, AppKey appKey, Model model)
    {
        this.context = context;
        this.appKeyIndex = appKey.getIndex();
        this.appKey = appKey.getKey();
        this.model = model;
        this.element = element;

        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) context).showPopUpForProxy(context, "Unbinding AppKey w.r.t the model...", true);
            }
        });

        unbindKey(model.getModelName(), model.getModelId(), element.getUnicastAddress());
    }

    private void unbindKey(String modelName, String modelId, final String elementUnicast) {

        ApplicationParameters.GenericModelID modelSelected = null;
        ApplicationParameters.VendorModelID modelVendorSelected = null;
        if(modelName.contains("VENDOR"))
        {
            isVendorSelected = true;
            //modelVendorSelected = new ApplicationParameters.VendorModelID(BluetoothAssignedNumbers.ST_MICROELECTRONICS, 0x0000, "Data Model");
            //modelVendorSelected = new ApplicationParameters.VendorModelID(BluetoothAssignedNumbers.ST_MICROELECTRONICS, 0x0000, "ST_VENDOR_SERVER");
            modelVendorSelected = ApplicationParameters.ModelID.ST_VENDOR_SERVER;
        }
        else
        {
            isVendorSelected = false;
            String replaceModelName = modelName.replace(" ", "_");
            modelSelected =  ModelRepository.getInstance().getModelSelected(replaceModelName);
        }

        UserApplication.trace(" Board Appkey UnBinding.... Model : " + modelName);
        int mValue = Integer.parseInt(element.getUnicastAddress());
        int mValueParent = Integer.parseInt(element.getParentNodeAddress());
        //short mValue = mobleAddress.deviceAddress(Integer.parseInt(element.getUnicastAddress())).mValue;
        //short mValueParent = mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress())).mValue;

        MainActivity.network.getConfigurationModelClient().unbindModelApp(
                new ApplicationParameters.Address(mValueParent),
                new ApplicationParameters.Address(mValue),
                new ApplicationParameters.KeyIndex(appKeyIndex),
                /*ApplicationParameters.ModelID.GENERIC_ONOFF_SERVER*/
                isVendorSelected ? modelVendorSelected : modelSelected,
                new ConfigurationModelClient.ModelAppStatusCallback() {
                    @Override
                    public void onModelAppStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.Address elementAddress, ApplicationParameters.KeyIndex index, ApplicationParameters.GenericModelID model) {

                        if (!timeout) {
                            if (status == ApplicationParameters.Status.SUCCESS) {
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MainActivity) context).showPopUpForProxy(context, "Unbinding Done w.r.t the model...", false);
                                    }
                                });
                                UserApplication.trace("SEMICONDUCTOR Model Unbinding Successfull for Key : " + appKeyIndex);
                                ((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(), "Unbind Done", context.getResources().getInteger(R.integer.CUSTOM_APP_KEY_UNBIND), null,false, status);

                            }  else {
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MainActivity) context).showPopUpForProxy(context, "Unbind Failed..", false);
                                    }
                                });
                                ((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(), "Unbind Failed", context.getResources().getInteger(R.integer.CUSTOM_APP_KEY_UNBIND), null,false, status);
                                UserApplication.trace("Model Binding Fail for Key : " + appKeyIndex);
                            }
                        } else {
                            ((MainActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) context).showPopUpForProxy(context, "Timeout..", false);
                                }
                            });
                            ((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(), "Unbind Timeout", context.getResources().getInteger(R.integer.CUSTOM_APP_KEY_UNBIND), null,false, status);
                            UserApplication.trace("Model Binding Fail for Key : " + appKeyIndex);
                        }

                    }
                });
    }
}
