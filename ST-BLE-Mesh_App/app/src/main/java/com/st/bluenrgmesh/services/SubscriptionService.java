/**
 ******************************************************************************
 * @file    SubscriptionService.java
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
import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.DeviceEntry;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.utils.ModelRepository;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.utils.AllConstants;

import java.util.ArrayList;

public class SubscriptionService extends IntentService {
    private Nodes node;
    private ResultReceiver receiver;
    private Element element;
    private Model modelRunning;
    private String modelName;
    private String modelId;
    private SubscriptionMgmtThread subscriptionThread;
    private mobleAddress nodeAddress;
    private String subAddress;
    private DeviceEntry mAutoDevice;
    private boolean isSubscriptionSuccess = false;
    private int modelIndex;
    private boolean isIntentServiceRunning;

    public SubscriptionService() {
        super("DisplayNotification");
    }
    public SubscriptionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(!isIntentServiceRunning)
            isIntentServiceRunning = true;

        receiver = intent.getParcelableExtra(AllConstants.RECEIVER);
        subAddress = intent.getStringExtra(AllConstants.RESULT_ADDRESS_KEY);
        node = (Nodes) intent.getSerializableExtra(AllConstants.RESULT_DATA_KEY);

        if(node.getElements() == null)
            return;

        nodeAddress  = mobleAddress.deviceAddress(Integer.parseInt(node.getElements().get(0).getUnicastAddress()));
        mAutoDevice = new DeviceEntry(node.getName(), mobleAddress.deviceAddress(Integer.parseInt(node.getElements().get(0).getUnicastAddress())));

        try {
            for (int i = 0; i < node.getElements().size(); i++) {
                element = node.getElements().get(i);
                for (int j = 0; j < element.getModels().size(); j++) {
                    if (isIntentServiceRunning) {
                        modelIndex = i + 1;
                        modelRunning = element.getModels().get(j);
                        modelName = element.getModels().get(j).getModelName();
                        modelId = element.getModels().get(j).getModelId();
                        //if(modelName.contains("SERVER") && !modelName.contains("HEALTH") && !modelName.contains("CONFIGURATION"))  /*replace this case w.r.t model id check*/
                        if (modelName.contains("GENERIC ONOFF SERVER") ||
                                modelName.contains("GENERIC LEVEL SERVER") ||
                                modelName.contains("ST VENDOR SERVER") ||
                                modelName.contains("LIGHT LIGHTNESS SERVER") ||
                                modelName.contains("LIGHT HSL SERVER") ||
                                modelName.contains("SENSOR MODEL SERVER") ) {
                            Utils.DEBUG(">--> sub -- " + modelName);
                            String replaceModelName = modelName.replace(" ", "_");
                            subscriptionThread = new SubscriptionMgmtThread(element.getUnicastAddress(), replaceModelName, modelId);
                            subscriptionThread.start();
                            synchronized (subscriptionThread) {
                                try {
                                    subscriptionThread.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //update the running model in element
                                if (isSubscriptionSuccess) {
                                    element.getModels().set(j, modelRunning);
                                    Utils.DEBUG(">>>  Subscription For Model : " + modelName + " For Address : " + subAddress);
                                } else {
                                    Utils.DEBUG(">>>  Subscription Not Done For Model Name : " + modelName + " For Address : " + subAddress);
                                }


                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                //update the running element in node

                if (isIntentServiceRunning) {
                    if (isSubscriptionSuccess) {
                        node.getElements().set(i, element);
                    }

                    if (i == node.getElements().size() - 1) {
                        if (subscriptionThread != null && subscriptionThread.isAlive()) {
                            subscriptionThread.stop();
                        }
                        Utils.DEBUG(">>>  Binding Delivered ");
                        deliverResultToReceiver(AllConstants.SUCCESS_RESULT,
                                "Subscription Done");
                    }
                }

            }
        }catch (Exception e){}
    }

    @Override
    public void onDestroy() {
        isIntentServiceRunning = false;
        Utils.DEBUG(">>> Subscription Service Ends");
        if(subscriptionThread != null && subscriptionThread.isAlive())
        {
            subscriptionThread.stop();
        }
        super.onDestroy();
    }

    private void deliverResultToReceiver(int resultCode, String message) {

        Bundle bundle = new Bundle();
        bundle.putString(AllConstants.RESULT_DATA_KEY, message);
        bundle.putSerializable("QuickSubscribedNode", node);
        receiver.send(resultCode, bundle);
    }

    private void deliverMessageToReceiver(int resultCode, String message) {
        if(isIntentServiceRunning)
        {
            Bundle bundle = new Bundle();
            bundle.putString(AllConstants.RESULT_DATA_KEY, message);
            bundle.putString(AllConstants.RESULT_ADDRESS_KEY, subAddress);
            receiver.send(resultCode, bundle);
        }
    }

    public class SubscriptionMgmtThread extends Thread
    {
        private String modelId;
        private String modelName;
        private String elementUnicast;
        private mobleAddress elementAddress;

        public SubscriptionMgmtThread(String elementUnicast, String modelName, String modelId) {
            this.elementUnicast = elementUnicast;
            this.modelName = modelName;
            this.modelId = modelId;

            elementAddress = mobleAddress.deviceAddress(Integer.parseInt(elementUnicast));
        }

        public void run()
        {
            synchronized (this) {
                try{
                    startSubscription(nodeAddress, elementAddress, subAddress, modelName);
                }
                catch (Exception e){}
            }
        }
    }

    public void startSubscription(mobleAddress nodeAddress,
                                  mobleAddress nextElementAddress, String subAddress, String modelName)
    {
        //final ApplicationParameters.ModelID modelSelected = getModelSelected(modelName);
        final ApplicationParameters.GenericModelID modelSelected = ModelRepository.getInstance().getModelSelected(modelName);
        if(modelSelected == null)
        {
            UserApplication.trace("doRestProvision=>subs null");
            isSubscriptionSuccess = false;
            deliverMessageToReceiver(AllConstants.FAILURE_RESULT,
                    "Subscription Failed For : " + modelRunning.getModelName() + " Address : " + subAddress + " " + modelIndex);
            synchronized (subscriptionThread)
            {
                subscriptionThread.notify();
            }
        }

        //if(MainActivity.mCid == BluetoothAssignedNumbers.ST_MICROELECTRONICS)
        //if(Integer.parseInt(node.getCid()) == BluetoothAssignedNumbers.ST_MICROELECTRONICS)
        if(modelSelected != null)
        {
            if(Integer.parseInt(node.getCid()) == BluetoothAssignedNumbers.ST_MICROELECTRONICS)
            {
                //((MainActivity)getActivity()).showPopUpForProxy(getActivity(), "Setting Configuration for..." + "\n" + String.valueOf(nodeData.getElements().get(elementsCount).getElementName()), true);
                if (subAddress.startsWith("c") || subAddress.startsWith("C")) {
                    UserApplication.trace("doRestProvision=> default");
                    MainActivity.mSettings.addGroup(Utils.contextMainActivity, nodeAddress, nextElementAddress, mobleAddress.groupAddress
                            (Integer.parseInt(subAddress, 16)), modelSelected, mSubListener);
                } else {
                    UserApplication.trace("doRestProvision=> element");
                    MainActivity.mSettings.addGroup(Utils.contextMainActivity, mAutoDevice.getAddress(), mAutoDevice.getAddress(),
                            mobleAddress.groupAddress(Integer.parseInt(subAddress)), modelSelected, mSubListener);
                }

                ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("Subscription started for ==>" + subAddress, LoggerConstants.TYPE_SEND);
            }
            else
            {
                MainActivity.network.getConfigurationModelClient().addSubscription(new ApplicationParameters.Address(mAutoDevice.getAddress().mValue),
                        new ApplicationParameters.Address(mAutoDevice.getAddress().mValue),
                        new ApplicationParameters.Address(Integer.parseInt(subAddress,16)),
                        /*ApplicationParameters.ModelID.GENERIC_ONOFF_SERVER*/modelSelected,
                        mSubListener);

                //startPublication();
            }
        }
    }

    public final ConfigurationModelClient.ConfigModelSubscriptionStatusCallback mSubListener = new ConfigurationModelClient.ConfigModelSubscriptionStatusCallback() {
        @Override
        public void onModelSubscriptionStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.Address address, ApplicationParameters.Address Address, ApplicationParameters.GenericModelID model) {

            if (timeout) {
                //((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("Subscription Status timeout ==>" + address, LoggerConstants.TYPE_RECEIVE);
                UserApplication.trace("doRestProvision=> subs timeout retry");
                UserApplication.trace("Retrying to subscribe group");
                isSubscriptionSuccess = false;
                deliverMessageToReceiver(AllConstants.FAILURE_RESULT,
                        "Subscription Timeout For : " + modelRunning.getModelName() + " For Address : " + subAddress + " " + modelIndex);
                Utils.DEBUG(">>>  Subscription Timeout For  " + modelRunning.getModelName() + " For Address : " + subAddress);
                //((MainActivity)getActivity()).showPopUpForProxy(getActivity(), getString(R.string.ERROR_CONFIG_SUB_TIMEOUT) + "\n" + nodeData.getName(), false);

                //showRetryOption("Subscription");
            } else {

                if (status == ApplicationParameters.Status.SUCCESS) {
                    isSubscriptionSuccess = true;
                    UserApplication.trace("doRestProvision=> subs success for address " + address);
                    if(modelRunning.getSubscribe() != null && modelRunning.getSubscribe().size() > 0)
                    {
                        boolean isAlreadyPresent = false;
                        for (int i = 0; i < modelRunning.getSubscribe().size(); i++) {
                            if(modelRunning.getSubscribe().get(i).equalsIgnoreCase(subAddress))
                            {
                                isAlreadyPresent = true;
                            }
                        }
                        if(!isAlreadyPresent)
                        {
                            modelRunning.getSubscribe().add(subAddress);
                        }

                    }
                    else
                    {
                        ArrayList<String> subAddressList = new ArrayList<>();
                        subAddressList.add(subAddress);
                        modelRunning.setSubscribe(subAddressList);
                    }

                    deliverMessageToReceiver(AllConstants.SUCCESS_RESULT,
                            "Subscription Model : " + modelRunning.getModelName() + " Address : " + subAddress + " " + modelIndex);
                    //((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("Subscription Status Success ==>" + address, LoggerConstants.TYPE_RECEIVE);
                    //Utils.DEBUG(">>Config Subscription Count : " + elementsCount);

                } else {
                    UserApplication.trace("doRestProvision=>subs null");
                    isSubscriptionSuccess = false;
                    deliverMessageToReceiver(AllConstants.FAILURE_RESULT,
                            "Subscription Failed For : " + modelRunning.getModelName() + " Address : " + subAddress + " " + modelIndex);
                    Utils.DEBUG(">>>  Subscription Failed For  " + modelRunning.getModelName() + " For Address : " + subAddress);
                    //((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("Subscription Status Null ==>" + address, LoggerConstants.TYPE_RECEIVE);
                    //Utils.showToast(getActivity(),"Timeout on Subscription");
                }
            }

            synchronized (subscriptionThread)
            {
                subscriptionThread.notify();
            }
        }
    };
}
