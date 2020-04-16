/**
 ******************************************************************************
 * @file    PublicationCallback.java
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
package com.st.bluenrgmesh.callbacks.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.services.PublicationService;
import com.st.bluenrgmesh.services.SubscriptionService;
import com.st.bluenrgmesh.utils.AllConstants;
import com.st.bluenrgmesh.utils.AppSingletonDialog;

public class PublicationCallback{

    private final AppSingletonDialog appSingletonDialog;
    private PublicationCallback.PublicationResultReceiver publicationResultReceiver;
    private Nodes nodeData;
    private Context context;
    private Handler mHandler;

    public PublicationCallback(final Context context, Nodes nodeData, AppSingletonDialog appSingletonDialog) {
        this.context = context;
        this.nodeData = nodeData;
        this.appSingletonDialog = appSingletonDialog;
    }

    public void startPublication()
    {
        publicationResultReceiver = new PublicationCallback.PublicationResultReceiver(new Handler());
        startIntentService();
    }

    private void startIntentService() {
        Intent intent = new Intent(context, PublicationService.class);
        intent.putExtra(AllConstants.APPKEYINDEX, String.valueOf(Utils.getDefaultAppKeyIndex(context)));
        intent.putExtra(AllConstants.RECEIVER, publicationResultReceiver);
        intent.putExtra(AllConstants.RESULT_ADDRESS_KEY, Utils.getPublicationAddressOnProvsioning(context));
        intent.putExtra(AllConstants.RESULT_DATA_KEY, nodeData);
        context.startService(intent);
    }

    class PublicationResultReceiver extends ResultReceiver {
        public PublicationResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }
            Utils.DEBUG(">>>  Publication Done inside ResultReceiver ");

            // Display the address string
            // or an error message sent from the intent service.
            final String output = resultData.getString(AllConstants.RESULT_DATA_KEY);
            if(output != null)
            {
                if(resultCode == AllConstants.SUCCESS_RESULT)
                {
                    if(output.equalsIgnoreCase("Publication Done"))
                    {
                        final Nodes quickPublishedNode = (Nodes) resultData.getSerializable("QuickPublishedNode");
                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //((MainActivity) context).showPopUpForProxy(context, output, false);
                                appSingletonDialog.showPopUpForConfig(context, output, true, quickPublishedNode);
                                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote(output, LoggerConstants.TYPE_RECEIVE);
                            }
                        });
                        Utils.DEBUG(">>>  Current Node : " + ParseManager.getInstance().toJSON(quickPublishedNode));
                        //((MainActivity)context).currentNode = quickPublishedNode;
                        //start publication step
                    }
                    else
                    {
                        //final String address = resultData.getString(AllConstants.RESULT_ADDRESS_KEY);
                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appSingletonDialog.showPopUpForConfig(context, output, true, nodeData);
                                //((MainActivity) context).showPopUpForProxy(context, output, true);
                                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote(output, LoggerConstants.TYPE_RECEIVE);
                            }
                        });
                    }
                }
                else
                {
                    //FAILURE_RESULT
                    ((MainActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            appSingletonDialog.showPopUpForConfig(context, output, true, nodeData);
                            //((MainActivity) context).showPopUpForProxy(context, output, true);
                            ((MainActivity)context).mUserDataRepository.getNewDataFromRemote(output, LoggerConstants.TYPE_RECEIVE);
                        }
                    });
                }
            }
        }
    }
}
