/**
 ******************************************************************************
 * @file    ShareAppKeyCallback.java
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
package com.st.bluenrgmesh.callbacks.appkeymanagement.quickbinding;

import android.content.Context;
import android.os.Handler;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;

import java.util.ArrayList;

public class ShareAppKeyCallback {

    private final AppKey appKey;
    private boolean isQuickProcess;
    private Element element;
    byte[] bytes;
    String appKeyStr;
    Context context;
    Nodes node;
    int appKeyIndex;

    public ShareAppKeyCallback(Context context, AppKey appKey, Nodes nodes, boolean isQuickProcess) {
        this.appKey = appKey;
        this.appKeyStr = appKey.getKey();
        this.context = context;
        this.node = nodes;
        this.isQuickProcess = isQuickProcess;
        this.element = node.getElements().get(0);
        appKeyIndex = appKey.getIndex();
        bytes = Utils.hexStringToByteArray(appKeyStr);

    }

    public ShareAppKeyCallback(Context context, AppKey appKey, Element element) {

        this.appKey = appKey;
        this.appKeyStr = appKey.getKey();
        this.context = context;
        this.node = null;
        this.element = element;
        appKeyIndex = appKey.getIndex();
        bytes = Utils.hexStringToByteArray(appKeyStr);

    }

    public void shareAppKey() {
        //((MotorolaApplicationCallbackProxy) MainActivity.network.getAppCallback()).unadvise(context);
        //short mValue = mobleAddress.deviceAddress(Integer.parseInt(element.getUnicastAddress())).mValue;
        //int mValue = Integer.parseInt(element.getUnicastAddress());
        int mValue = Integer.parseInt(element.getParentNodeAddress());

        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) context).showPopUpForProxy(context, "Sharing App Key..", true);
            }
        });

        Utils.DEBUG(">>> App Key address : " + mValue);
        Utils.DEBUG(">>> App Key : " + appKeyStr.getBytes());
        Utils.DEBUG(">>> App Key index : " + appKeyIndex);

        (MainActivity.network).getConfigurationModelClient().addAppKey(new ApplicationParameters.Address(mValue),
                new ApplicationParameters.KeyPair(0, appKeyIndex),
                new ApplicationParameters.Key(bytes),
                new ConfigurationModelClient.AppKeyStatusCallback() {
                    @Override
                    public void onAppKeyStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.KeyPair pair) {

                        if(!timeout) {
                            if (status == ApplicationParameters.Status.SUCCESS) {
                                //add this key to json
                                if(MainActivity.isCustomAppKeyShare) {
                                    ((MainActivity) context).showPopUpForProxy(context, "App key shared Sucessfully.", false);
                                    Utils.addAppKeyInJson(context, appKeyStr, appKeyIndex, element);
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //((MainActivity)context).updateJsonData();
                                            ((MainActivity)context).onBackPressed();
                                            ((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(),null, context.getResources().getInteger(R.integer.ELEMENT_APPKEY_DIALOG_CASE), null,false,ApplicationParameters.Status.SUCCESS);
                                            //((MainActivity)context).fragmentCommunication(new AddAppKeyFragment().getClass().getName(),null,0,null,false,ApplicationParameters.Status.SUCCESS);
                                        }
                                    });
                                }
                                else {
                                    //QuickBinding
                                    //((MainActivity) context).showPopUpForProxy(context, "Binding App key for Index.." + appKeyIndex, true);
                                    ((MainActivity) context).showPopUpForProxy(context, "App key shared Sucessfully.", true);
                                    ArrayList<AppKey> appKeys = new ArrayList<>();
                                    appKeys.add(appKey);
                                    node.setAppKeys(appKeys);
                                    Utils.addAppKeyInJson(context, appKeyStr, appKeyIndex, element);
                                    if(isQuickProcess) {
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                new BindAppKeyCallback(context, appKey, node, isQuickProcess);
                                            }
                                        });
                                    }
                                    else
                                    {
                                        //onclick of config case
                                        //Utils.moveToFragment(((MainActivity) context), new LoadConfigFeaturesFragment(), node, 0);
                                    }
                                }
                            }
                            else if (status == ApplicationParameters.Status.KEY_INDEX_ALREADY_STORED)
                            {
                                MainActivity.isCustomAppKeyShare = false;
                                ((MainActivity) context).showPopUpForProxy(context, "Key Index Already Stored.", false);
                                //((MainActivity) context).showPopUpForProxy(context, "App key shared..", false);
                                Utils.addAppKeyInJson(context, appKeyStr, appKeyIndex, element);
                                if(isQuickProcess) {
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            new BindAppKeyCallback(context, appKey, node, isQuickProcess);
                                        }
                                    });
                                }
                                else
                                {
                                    //detail
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //((MainActivity)context).updateJsonData();
                                            ((MainActivity)context).onBackPressed();
                                            //((MainActivity)context).fragmentCommunication(new ElementSettingFragment().getClass().getName(),null, context.getResources().getInteger(R.integer.ELEMENT_APPKEY_DIALOG_CASE), null,false,ApplicationParameters.Status.SUCCESS);
                                            //((MainActivity)context).fragmentCommunication(new AddAppKeyFragment().getClass().getName(),null,0,null,false,ApplicationParameters.Status.SUCCESS);
                                        }
                                    });
                                }

                            }
                            else {
                                MainActivity.isCustomAppKeyShare = false;
                                ((MainActivity) context).showPopUpForProxy(context, "Failed to share app key.", false);
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MainActivity)context).onBackPressed();
                                    }
                                });
                            }
                        }
                        else
                        {
                            //timeout
                            MainActivity.isCustomAppKeyShare = false;
                            ((MainActivity) context).showPopUpForProxy(context, "Timeout. Failed to share app key..", false);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity)context).onBackPressed();
                                }
                            });
                        }

                    }
                });
    }
}
