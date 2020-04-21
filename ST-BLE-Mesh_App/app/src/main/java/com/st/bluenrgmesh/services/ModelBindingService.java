/**
 ******************************************************************************
 * @file    ModelBindingService.java
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
package com.st.bluenrgmesh.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ApplicationParameters.ModelID;
import com.msi.moble.ConfigurationModelClient;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.utils.ModelRepository;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.utils.AllConstants;

import java.util.ArrayList;

public class ModelBindingService extends IntentService
{

    protected ResultReceiver receiver;
    private Nodes node;
    private Element element;
    private String modelName;
    private String modelId;
    private int appKeyIndex = 0 ;
    private BindingThread bindingThread;
    private Model modelRunning;
    private boolean isBindingSuccess = false;
    private boolean isVendorSelected = false;
    private boolean isIntentSeviceServiceRunning;
    private boolean isQuickProcess;

    public ModelBindingService() {
        super("DisplayNotification");
    }
    public ModelBindingService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            if (!isIntentSeviceServiceRunning)
                isIntentSeviceServiceRunning = true;


            isVendorSelected = false;
            Utils.DEBUG(">>>  Binding Started inside onHandleIntent");
            receiver = intent.getParcelableExtra(AllConstants.RECEIVER);
            appKeyIndex = intent.getIntExtra(AllConstants.APPKEYINDEX, 0);
            isQuickProcess = intent.getBooleanExtra(AllConstants.BOOL_ISQUICKPROCESS, false);
            node = (Nodes) intent.getSerializableExtra(AllConstants.RESULT_DATA_KEY);

            if (node.getElements() != null && node.getElements().size() > 0) {
                for (int i = 0; i < node.getElements().size(); i++) {
                //for (int i = node.getElements().size()-1; i >= 0 ; i--) {
                    if (isIntentSeviceServiceRunning) {
                        element = node.getElements().get(i);

                        startBindThread(element);
                        if (bindingThread.isAlive()) {
                            bindingThread.stop();
                        }
                        //update the running element in node
                        if (i == node.getElements().size() - 1) {
                            //if(MainActivity.isCustomModelBinding)
                            if (!isQuickProcess) {
                                if (isBindingSuccess) {
                                    node.getElements().set(i, element);
                                    Utils.DEBUG(">>>  Binding Delivered ");
                                    deliverResultToReceiver(AllConstants.SUCCESS_RESULT, "Binding Done");
                                } else {
                                    if (i == node.getElements().size() - 1) {
                                        deliverResultToReceiver(AllConstants.SUCCESS_RESULT, "Binding Failed");
                                    }
                                }
                            } else {
                                //quickprocess
                                node.getElements().set(i, element);
                                Utils.DEBUG(">>>  Binding Delivered ");
                                deliverResultToReceiver(AllConstants.SUCCESS_RESULT, "Binding Done");
                            }
                        }
                    }
                }
            } else {
                deliverResultToReceiver(AllConstants.SUCCESS_RESULT, "Binding Done");
            }
        }catch (Exception e){}

    }

    private void startBindThread(Element element) {

        if(element.getModels() == null)
            return;

        if(element.getModels().size() > 0)
        {
            for (int j = 0; j < element.getModels().size(); j++)
            {
                modelRunning = element.getModels().get(j);
                modelName = element.getModels().get(j).getModelName();
                modelId = element.getModels().get(j).getModelId();

                if (isQuickProcess ? modelName.contains("GENERIC ONOFF SERVER") ||
                        modelName.contains("GENERIC LEVEL SERVER") ||
                        modelName.contains("LIGHT LIGHTNESS SERVER") ||
                        modelName.contains("LIGHT LIGHTNESS SETUP SERVER") ||
                        modelName.contains("SENSOR MODEL SERVER") ||
                        modelName.contains("SENSOR SETUP SERVER") ||
                        modelName.contains("LIGHT HSL SERVER") ||
                        modelName.contains("LIGHT HSL SETUP SERVER") ||
                        modelName.contains("LIGHT CTL SERVER") ||
                        modelName.contains("LIGHT CTL SETUP SERVER") ||
                        modelName.contains("LIGHT CTL TEMPERATURE SERVER") ||
                        modelName.contains("LIGHT LC SETUP") ||
                        modelName.contains("LIGHT LC SETUP SERVER") ||
                        modelName.contains("GENERIC POWER ON ONOFF SERVER") ||
                        modelName.contains("GENERIC POWER ON SETUP SERVER") ||
                        modelName.contains("ST VENDOR SERVER") : true)
                {
                    String replaceModelName = modelName.replace(" ", "_");
                    bindingThread = new BindingThread(element.getUnicastAddress(), replaceModelName, modelId);
                    bindingThread.start();
                    synchronized (bindingThread)
                    {
                        try {
                            bindingThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //update the running model in element
                        if(isBindingSuccess)
                        {
                            element.getModels().set(j, modelRunning);
                            Utils.DEBUG(">>>  Binding Done For Model Name : " + modelName + " For appKeyIndex : " + appKeyIndex);
                        }
                        else
                        {
                            Utils.DEBUG(">>>  Binding Not Done For Model Name : " + modelName + " For appKeyIndex : " + appKeyIndex);
                        }
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        isIntentSeviceServiceRunning = false;
        Utils.DEBUG(">>> Publication Service Ends");
        if(bindingThread != null && bindingThread.isAlive())
        {
            bindingThread.stop();
        }
        super.onDestroy();
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(AllConstants.RESULT_DATA_KEY, message);
        bundle.putSerializable("QuickBindedNode", node);
        receiver.send(resultCode, bundle);
    }

    private void deliverMessageToReceiver(int resultCode, String message) {
        if(isIntentSeviceServiceRunning)
        {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putString(AllConstants.RESULT_DATA_KEY, message);
                    receiver.send(resultCode, bundle);
                }
            });
        }
    }

    public class BindingThread extends Thread
    {
        private String modelId;
        private String modelName;
        private String elementUnicast;

        public BindingThread(String elementUnicast, String modelName, String modelId) {
            this.elementUnicast = elementUnicast;
            this.modelName = modelName;
            this.modelId = modelId;
        }

        public void run()
        {
            synchronized (this) {
                Utils.DEBUG(">>> >> Model Name :  "  + modelName);
                sendAllModelBindKeyCommand(modelName, modelId, elementUnicast);
            }
        }
    }

    public void sendAllModelBindKeyCommand(String modelName, String modelId, final String elementUnicast)
    {
        ApplicationParameters.GenericModelID modelSelected = null;
        ApplicationParameters.VendorModelID modelVendorSelected = null;
        if(modelName.contains("VENDOR")) {
            isVendorSelected = true;
            modelVendorSelected = ModelID.ST_VENDOR_SERVER;
        } else {
            isVendorSelected = false;
            modelSelected =  ModelRepository.getInstance().getModelSelected(modelName);
        }

        //if(MainActivity.mCid !=  BluetoothAssignedNumbers.ST_MICROELECTRONICS)
        //if(MainActivity.mCid ==  BluetoothAssignedNumbers.NORDIC_SEMICONDUCTOR)
        {
            UserApplication.trace("Appkey Binding.");
            Utils.DEBUG("Element Address : " + (element.getUnicastAddress()));
            Utils.DEBUG("Element Address : " + Integer.parseInt(element.getUnicastAddress(), 16));
            int mValue = Integer.parseInt(element.getUnicastAddress());
            int mValueParent = Integer.parseInt(element.getParentNodeAddress());
            //short mValueParent = mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress())).mValue;

            try {
                MainActivity.network.getConfigurationModelClient().bindModelApp(
                        new ApplicationParameters.Address(mValueParent),
                        new ApplicationParameters.Address(mValue),
                        new ApplicationParameters.KeyIndex(appKeyIndex),
                        /*ApplicationParameters.ModelID.GENERIC_ONOFF_SERVER*/
                        isVendorSelected ? modelVendorSelected : modelSelected,
                        new ConfigurationModelClient.ModelAppStatusCallback() {
                            @Override
                            public void onModelAppStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.Address elementAddress, ApplicationParameters.KeyIndex index, ApplicationParameters.GenericModelID model) {

                                if (!timeout) {
                                    if (status == ApplicationParameters.Status.SUCCESS)
                                    {
                                        Utils.DEBUG(">>>  Binding Success : " + elementUnicast);
                                        isBindingSuccess = true;
                                        if (modelRunning.getBind() != null && modelRunning.getBind().size() > 0) {
                                            boolean isAlreadyPresent = false;
                                            for (int i = 0; i < modelRunning.getBind().size(); i++) {
                                                if (modelRunning.getBind().get(i) == appKeyIndex) {
                                                    isAlreadyPresent = true;
                                                }
                                            }
                                            if (!isAlreadyPresent) {
                                                modelRunning.getBind().add(appKeyIndex);
                                            }
                                        } else {
                                            ArrayList<Integer> bindKey = new ArrayList<>();
                                            bindKey.add(appKeyIndex);
                                            modelRunning.setBind(bindKey);
                                        }
                                        deliverMessageToReceiver(AllConstants.SUCCESS_RESULT, "Element Unicast : " + elementUnicast + "\nBinding Success For Model : " + modelRunning.getModelName());

                                    } else if (status == ApplicationParameters.Status.INSUFFICIENT_RESOURCES) {
                                        isBindingSuccess = false;
                                        deliverMessageToReceiver(AllConstants.FAILURE_RESULT, "Element Unicast : " + elementUnicast + "\nCannot Bind Insufficient Resources : " + modelRunning.getModelName() + "\nFor AppKeyIndex : " + appKeyIndex);
                                        Utils.DEBUG(">>>  Binding Failed (INSUFFICIENT_RESOURCES) : INVALID_APP_KEY_INDEX " + elementUnicast + " keyIndex : " + appKeyIndex);
                                    } else if (status == ApplicationParameters.Status.INVALID_APP_KEY_INDEX) {
                                        isBindingSuccess = false;
                                        deliverMessageToReceiver(AllConstants.FAILURE_RESULT, "Element Unicast : " + elementUnicast + "\nInvalid Index or Binded Before : " + modelRunning.getModelName() + "\nFor AppKeyIndex : " + appKeyIndex);
                                        Utils.DEBUG(">>>  Binding Failed (INVALID_APP_KEY_INDEX) : INVALID_APP_KEY_INDEX " + elementUnicast + " keyIndex : " + appKeyIndex);
                                    } else if (status == ApplicationParameters.Status.CANNOT_BIND) {
                                        isBindingSuccess = false;
                                        deliverMessageToReceiver(AllConstants.FAILURE_RESULT, "Element Unicast : " + elementUnicast + "\nCannot Bind Something Wrong : " + modelRunning.getModelName() + "\nFor AppKeyIndex : " + appKeyIndex);
                                        Utils.DEBUG(">>>  Binding Failed (Included Already Bind Case) : INVALID_APP_KEY_INDEX " + elementUnicast + " keyIndex : " + appKeyIndex);
                                    } else {
                                        isBindingSuccess = false;
                                        deliverMessageToReceiver(AllConstants.FAILURE_RESULT, "Element Unicast : " + elementUnicast + "\nBinding skipped for " + modelRunning.getModelName() + "\nFor AppKeyIndex : " + appKeyIndex);
                                        Utils.DEBUG(">>>  Binding Failed : " + elementUnicast);
                                    }
                                } else {
                                    isBindingSuccess = false;
                                    deliverMessageToReceiver(AllConstants.FAILURE_RESULT, "Element Unicast : " + elementUnicast + "\nBinding Timeout For : " + modelRunning.getModelName() + "\nFor AppKeyIndex : " + appKeyIndex);
                                    Utils.DEBUG(">>>  Binding Failed : TIMEOUT " + elementUnicast + " keyIndex : " + appKeyIndex + " Model Running :" + modelRunning.getModelName());
                                }
                                synchronized (bindingThread) {
                                    bindingThread.notify();
                                }
                            }
                        });
            }catch (Exception e){}

        }
        /*else if(MainActivity.mCid ==  BluetoothAssignedNumbers.ST_MICROELECTRONICS)
        {
            //st board
            //((MainActivity) context).showPopUpForProxy(context, "ST_MICROELECTRONICS Board Appkey Binding.", false);
            deliverMessageToReceiver(AllConstants.FAILURE_RESULT,
                    "Binding STM board Under Development For : " + modelRunning.getModelName() + " For AppKeyIndex : " + appKeyIndex);
        }*/
    }
}
