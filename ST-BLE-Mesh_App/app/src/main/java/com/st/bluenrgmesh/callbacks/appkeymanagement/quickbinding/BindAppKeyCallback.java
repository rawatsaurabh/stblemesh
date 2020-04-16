/**
 ******************************************************************************
 * @file    BindAppKeyCallback.java
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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.msi.moble.ApplicationParameters;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.compositiondata.ModelsData;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.services.ModelBindingService;
import com.st.bluenrgmesh.utils.AllConstants;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.ConfigurationFragment;

public class BindAppKeyCallback {


    private boolean isQuickProcess;
    private BindingResultReceiver bindingResultReceiver;
    private String modelId;
    private String modelName;
    private ModelsData modelsData = null;
    private String appKey;
    private Context context;
    private int appKeyIndex;
    private Nodes node;
    private Element element;

    protected void startIntentService(boolean isQuickProcess) {
        Intent intent = new Intent(context, ModelBindingService.class);
        intent.putExtra(AllConstants.RECEIVER, bindingResultReceiver);
        intent.putExtra(AllConstants.APPKEYINDEX, appKeyIndex);
        intent.putExtra(AllConstants.BOOL_ISQUICKPROCESS, isQuickProcess);
        intent.putExtra(AllConstants.RESULT_DATA_KEY, node);
        context.startService(intent);
    }

    public BindAppKeyCallback(final Context context, AppKey appKey, Nodes node, boolean isQuickProcess)
    {
        this.context = context;
        this.appKeyIndex = appKey.getIndex();
        this.node = node;
        this.appKey = appKey.getKey();
        this.isQuickProcess = isQuickProcess;

        //temporary data present in node : Features and models (Unsaved in Json) will be permanent after configuration.
        //permanent data present in node : Elements Data without models (Saved in Json)
        bindingResultReceiver = new BindingResultReceiver(new Handler());
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) context).showPopUpForProxy(context, "Binding AppKey with Models", true);
            }
        });
        startIntentService(isQuickProcess);
    }

    class BindingResultReceiver extends ResultReceiver {
        public BindingResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }
            Utils.DEBUG(">>  Binding Done inside ResultReceiver ");
            // Display the address string
            // or an error message sent from the intent service.
            final String output = resultData.getString(AllConstants.RESULT_DATA_KEY);
            try {
                if (output != null) {
                    if (resultCode == AllConstants.SUCCESS_RESULT) {
                        if (output.equalsIgnoreCase("Binding Done") || output.contains("Binding Done")) {
                            showPopUp(context, output, false);
                            Nodes quickBindedNode = (Nodes) resultData.getSerializable("QuickBindedNode");
                            Utils.DEBUG(">> Current Node : " + ParseManager.getInstance().toJSON(quickBindedNode));
                            if (!isQuickProcess) {
                                ((MainActivity) context).fragmentCommunication(new ElementSettingFragment().getClass().getName(),
                                        null, context.getResources().getInteger(R.integer.CUSTOM_APP_KEY_BIND), null, false, ApplicationParameters.Status.SUCCESS);
                            } else {
                                //quick binding
                                Utils.moveToFragment((MainActivity) context, new ConfigurationFragment(), quickBindedNode, 0);
                            }
                        } else {
                            Utils.DEBUG(">> Current Node : ok");
                            showPopUp(context, output, true);
                        }
                    } else {
                        Utils.DEBUG(">> Current Node : okk");
                        showPopUp(context, output, true);
                    }
                }
            }catch (Exception e){}
        }

        private void showPopUp(final Context context, final String responseMessage, boolean isVisible)
        {
            ((MainActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) context).showPopUpForProxy(context, responseMessage, isVisible);
                }
            });
        }
    }

}
