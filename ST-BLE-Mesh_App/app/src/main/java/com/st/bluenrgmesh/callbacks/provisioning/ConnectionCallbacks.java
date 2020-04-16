/**
 ******************************************************************************
 * @file    ConnectionCallbacks.java
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
package com.st.bluenrgmesh.callbacks.provisioning;

import android.os.Handler;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.mobleProvisioningStatus;
import com.msi.moble.mobleSettings;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.view.fragments.setting.configuration.ConnectionSetupFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.LoadConfigFeaturesFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;

public class ConnectionCallbacks {

    public static boolean isCapabilitiesAssigned = false;

    public static final mobleSettings.onProvisionComplete mProvisionCallback = new mobleSettings.onProvisionComplete() {
        @Override
        public void onCompleted(byte status) {
            //callback after disconnection during provisioning
            Utils.DEBUG(">>Provisioning States :: Done");
            ((MainActivity)Utils.contextMainActivity).mProvisioningInProgress = false;
            ((MainActivity)Utils.contextMainActivity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (status == mobleProvisioningStatus.SUCCESS) {
                        UserApplication.trace("ConnectionCallBack Provisioning Completed Callback recieved ....");
                        ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote(" provisioning successful", LoggerConstants.TYPE_RECEIVE);
                        ((MainActivity)Utils.contextMainActivity).fragmentCommunication(new ConnectionSetupFragment().getClass().getName(), null,
                                -1, null,false, ApplicationParameters.Status.SUCCESS);

                    } else {
                        Utils.showToast(((MainActivity)Utils.contextMainActivity),"Provisioning unsuccessful. Please reset the device !");
                    }
                }
            });

        }
    };

    public static final mobleSettings.capabilitiesListener mCapabilitiesLstnr = new mobleSettings.capabilitiesListener() {
        @Override
        public void onCapabilitiesReceived(mobleSettings.Identifier identifier, Byte elementsNumber) {

            if(!isCapabilitiesAssigned)
            {
                isCapabilitiesAssigned = true;
                ((MainActivity)Utils.contextMainActivity).elementsSize = elementsNumber;
                ((MainActivity)Utils.contextMainActivity).identifier = identifier;
                ((MainActivity)Utils.contextMainActivity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("Capabilities Listener==>" + "element supported==>" + ((MainActivity)Utils.contextMainActivity).elementsSize, LoggerConstants.TYPE_RECEIVE);
                        ((MainActivity)Utils.contextMainActivity).fragmentCommunication(new ConnectionSetupFragment().getClass().getName(), null,
                                -2, null, true, null);
                    }
                });

            }
        }
    };

    public static final mobleSettings.provisionerStateChanged mProvisionerStateChanged = new mobleSettings.provisionerStateChanged() {
        @Override
        public void onStateChanged(final int state, final String label) {
            final int stateValue = state + 1;
            Utils.DEBUG(">>Provisioning States :: " + stateValue);
            if(stateValue > 2)
            {
                isCapabilitiesAssigned = false;
            }
            if(!isCapabilitiesAssigned)
            {
                ((MainActivity)Utils.contextMainActivity).provisioningStep = state;
                ((MainActivity)Utils.contextMainActivity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("Provisioning ==>" + (state + 1) * 10, LoggerConstants.TYPE_RECEIVE);
                        ((MainActivity)Utils.contextMainActivity).fragmentCommunication(new ConnectionSetupFragment().getClass().getName(), null,
                                stateValue, null, true, null);
                    }
                });
            }

        }
    };
}
