/**
 ******************************************************************************
 * @file    PublicationService.java
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
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.models.meshdata.Publish;
import com.st.bluenrgmesh.utils.AllConstants;

public class PublicationService extends IntentService {
    private Nodes node;
    private ResultReceiver receiver;
    private Element element;
    private Model modelRunning;
    private String modelName;
    private String modelId;
    private PublicationMgmtThread publicationMgmtThread;
    private mobleAddress nodeAddress;
    private String pubAddress;
    private DeviceEntry mAutoDevice;
    private boolean isPublicationSuccess = false;
    private boolean isIntentSeviceServiceRunning;
    private int appKeyIndex;

    public PublicationService() {
        super("DisplayNotification");
    }
    public PublicationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(!isIntentSeviceServiceRunning)
            isIntentSeviceServiceRunning = true;

        appKeyIndex = Integer.parseInt(intent.getStringExtra(AllConstants.APPKEYINDEX));
        receiver = intent.getParcelableExtra(AllConstants.RECEIVER);
        pubAddress = intent.getStringExtra(AllConstants.RESULT_ADDRESS_KEY);
        node = (Nodes) intent.getSerializableExtra(AllConstants.RESULT_DATA_KEY);
        nodeAddress  = mobleAddress.deviceAddress(Integer.parseInt(node.getElements().get(0).getUnicastAddress()));
        mAutoDevice = new DeviceEntry(node.getName(), mobleAddress.deviceAddress(Integer.parseInt(node.getElements().get(0).getUnicastAddress())));

        try {
            for (int i = 0; i < node.getElements().size(); i++) {
                element = node.getElements().get(i);
                for (int j = 0; j < element.getModels().size(); j++) {
                    if (isIntentSeviceServiceRunning) {
                        modelRunning = element.getModels().get(j);
                        modelName = element.getModels().get(j).getModelName();
                        modelId = element.getModels().get(j).getModelId();

                        if (modelName.contains("GENERIC ONOFF SERVER") ||
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
                                modelName.contains("ST VENDOR SERVER") ) {
                            Utils.DEBUG(">--> pub -- " + modelName);
                            String replaceModelName = modelName.replace(" ", "_");
                            publicationMgmtThread = new PublicationMgmtThread(element.getUnicastAddress(), replaceModelName, modelId);
                            publicationMgmtThread.start();
                            synchronized (publicationMgmtThread) {
                                try {
                                    publicationMgmtThread.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //update the running model in element
                                if (isPublicationSuccess) {
                                    element.getModels().set(j, modelRunning);
                                    Utils.DEBUG(">>>  Publication Done For Model Name : " + modelName + " For Address : " + pubAddress);
                                } else {
                                    Utils.DEBUG(">>>  Publication Not Done For Model Name : " + modelName + " For Address : " + pubAddress);
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

                if (isIntentSeviceServiceRunning) {
                    if (isPublicationSuccess) {
                        node.getElements().set(i, element);
                    }

                    if (i == node.getElements().size() - 1) {
                        if (publicationMgmtThread != null && publicationMgmtThread.isAlive()) {
                            publicationMgmtThread.stop();
                        }

                        Utils.DEBUG(">>>  Binding Delivered ");
                        deliverResultToReceiver(AllConstants.SUCCESS_RESULT,
                                "Publication Done");
                    }
                }
            }
        }catch (Exception e){}
    }

    @Override
    public void onDestroy() {
        isIntentSeviceServiceRunning = false;
        Utils.DEBUG(">>> Publication Service Ends");
        if(publicationMgmtThread != null && publicationMgmtThread.isAlive())
        {
            publicationMgmtThread.stop();
        }
        super.onDestroy();
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(AllConstants.RESULT_DATA_KEY, message);
        bundle.putSerializable("QuickPublishedNode", node);
        receiver.send(resultCode, bundle);
    }

    private void deliverMessageToReceiver(int resultCode, String message) {
        if(isIntentSeviceServiceRunning)
        {
            Bundle bundle = new Bundle();
            bundle.putString(AllConstants.RESULT_DATA_KEY, message);
            bundle.putString(AllConstants.RESULT_ADDRESS_KEY, pubAddress);
            receiver.send(resultCode, bundle);
        }
    }

    public class PublicationMgmtThread extends Thread
    {
        private String modelId;
        private String modelName;
        private String elementUnicast;
        private mobleAddress elementAddress;

        public PublicationMgmtThread(String elementUnicast, String modelName, String modelId) {
            this.elementUnicast = elementUnicast;
            this.modelName = modelName;
            this.modelId = modelId;
            elementAddress = mobleAddress.deviceAddress(Integer.parseInt(elementUnicast));
        }

        public void run()
        {
            synchronized (this) {
                startPublication(nodeAddress, elementAddress, pubAddress, modelName);
            }
        }
    }

    private void startPublication(mobleAddress nodeAddress,
                                  mobleAddress nextElementAddress, String pubAddress, String modelName) {
        final ApplicationParameters.GenericModelID modelSelected = ModelRepository.getInstance().getModelSelected(modelName);
        if(modelSelected == null)
        {
            UserApplication.trace("doRestProvision=>subs null");
            isPublicationSuccess = false;
            deliverMessageToReceiver(AllConstants.FAILURE_RESULT,
                    "Publication Failed For : " + modelRunning.getModelName() + " For Address : " + pubAddress);
            synchronized (publicationMgmtThread)
            {
                publicationMgmtThread.notify();
            }
        }


        //if(MainActivity.mCid == BluetoothAssignedNumbers.ST_MICROELECTRONICS)
        //if(Integer.parseInt(node.getCid()) == BluetoothAssignedNumbers.ST_MICROELECTRONICS)
        if(modelSelected != null)
        {
            if(Integer.parseInt(node.getCid()) == BluetoothAssignedNumbers.ST_MICROELECTRONICS)
            {
                if (pubAddress.startsWith("c") || pubAddress.startsWith("C")) {
                    UserApplication.trace("doRestProvision=> subs publication default called");
                    MainActivity.mSettings.setPublicationAddress(Utils.contextMainActivity, appKeyIndex, modelSelected, nodeAddress,
                            nextElementAddress, Integer.parseInt(pubAddress, 16), mPublicationListener);

                } else {
                    UserApplication.trace("doRestProvision=> subs publication element called");
                    MainActivity.mSettings.setPublicationAddress(Utils.contextMainActivity, appKeyIndex, modelSelected, nodeAddress,
                            nextElementAddress, Integer.parseInt(pubAddress),
                            mPublicationListener);

                }
            }
            else
            {
                //String address_str = Utils.getPublicationAddressOnProvsioning(Utils.contextMainActivity);
                //mobleAddress Nodeaddres = mAutoDevice.getAddress();
                if (pubAddress != null && !pubAddress.equalsIgnoreCase("")) {
                    UserApplication.trace("doRestProvision=> subs publication default called");
                    MainActivity.network.getSettings().setPublicationAddress(Utils.contextMainActivity, nodeAddress,
                            nextElementAddress, Integer.parseInt(pubAddress, 16),modelSelected,
                            mPublicationListener);
                }
                //((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("Publication Sent for ==>" + address_str, LoggerConstants.TYPE_SEND);
            }
        }
    }

    public final ConfigurationModelClient.ConfigModelPublicationStatusCallback mPublicationListener = new ConfigurationModelClient.ConfigModelPublicationStatusCallback() {

        @Override
        public void onModelPublicationStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.Address address,
                                             ApplicationParameters.Address address1, ApplicationParameters.KeyIndex keyIndex,
                                             ApplicationParameters.TTL ttl, ApplicationParameters.Time time,
                                             ApplicationParameters.GenericModelID genericModelID) {
            if (timeout) {
                //((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("Subscription Status timeout ==>" + address, LoggerConstants.TYPE_RECEIVE);
                UserApplication.trace("doRestProvision=> subs timeout retry");
                isPublicationSuccess = false;
                deliverMessageToReceiver(AllConstants.FAILURE_RESULT,
                        "Publication Timeout For : " + modelRunning.getModelName() + " For Address : " + pubAddress);
                Utils.DEBUG(">>>  Publication Timeout For  " + modelRunning.getModelName() + " For Address : " + pubAddress);
                //((MainActivity)getActivity()).showPopUpForProxy(getActivity(), getString(R.string.ERROR_CONFIG_SUB_TIMEOUT) + "\n" + nodeData.getName(), false);

                //showRetryOption("Subscription");
            } else {

                if (status == ApplicationParameters.Status.SUCCESS) {
                    isPublicationSuccess = true;
                    UserApplication.trace("doRestProvision=> subs success for address " + address);
                    if(modelRunning.getPublish() != null)
                    {
                        modelRunning.getPublish().setAddress(pubAddress);
                    }
                    else
                    {
                        Publish publish = new Publish();
                        publish.setAddress(pubAddress);
                        modelRunning.setPublish(publish);
                    }


                    deliverMessageToReceiver(AllConstants.SUCCESS_RESULT,
                            "Publication Model : " + modelRunning.getModelName() + " Address : " + pubAddress);
                    //((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("Subscription Status Success ==>" + address, LoggerConstants.TYPE_RECEIVE);
                    //Utils.DEBUG(">>Config Subscription Count : " + elementsCount);

                } else {
                    UserApplication.trace("doRestProvision=>subs null");
                    isPublicationSuccess = false;
                    deliverMessageToReceiver(AllConstants.FAILURE_RESULT,
                            "Publication Failed For : " + modelRunning.getModelName() + " For Address : " + pubAddress);
                    Utils.DEBUG(">>>  Publication Failed For  " + modelRunning.getModelName() + " For Address : " + pubAddress);
                    //((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("Subscription Status Null ==>" + address, LoggerConstants.TYPE_RECEIVE);
                    //Utils.showToast(getActivity(),"Timeout on Subscription");
                }
            }

            synchronized (publicationMgmtThread)
            {
                publicationMgmtThread.notify();
            }
        }
    };
}
