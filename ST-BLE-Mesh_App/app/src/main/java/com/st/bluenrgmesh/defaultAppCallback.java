/**
 * *****************************************************************************
 *
 * @file defaultAppCallback.java
 * @author BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
 * @brief User Application file
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

package com.st.bluenrgmesh;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.Device;
import com.msi.moble.Provisioner;
import com.msi.moble.mobleAddress;
import com.msi.moble.MotorolaApplicationCallback;

/**
 * The type Default app callback.
 */
public class defaultAppCallback implements MotorolaApplicationCallback {

    @Override
    public void onWriteLocalData(mobleAddress peer,mobleAddress dst, Object cookies, short offset, byte count, byte[] data) {
    }

    @Override
    public void onUpdateRemoteData(mobleAddress peer, Object cookies, short offset, byte count, byte[] data) {
    }

    @Override
    public void onResponse(mobleAddress peer, Object cookies, byte status, byte[] data) {
    }

    @Override
    public void onDongleStateChanged(boolean enabled) {
    }

    @Override
    public void onBufferLoadChanged(int count) {
    }

    @Override
    public void onProxyConnectionEvent(boolean process, String proxyAddress, Device address) {

    }


    /**
     * On model publication status.
     *
     * @param b              the b
     * @param status         the status
     * @param address        the address
     * @param address1       the address 1
     * @param keyIndex       the key index
     * @param ttl            the ttl
     * @param time           the time
     * @param genericModelID the generic model id
     */
    public void onModelPublicationStatus(boolean b, ApplicationParameters.Status status, ApplicationParameters.Address address, ApplicationParameters.Address address1, ApplicationParameters.KeyIndex keyIndex, ApplicationParameters.TTL ttl, ApplicationParameters.Time time, ApplicationParameters.GenericModelID genericModelID) {

    }

    @Override
    public void onDeviceAppeared(String bt_addr, String name) {
    }

    @Override
    public void onError(String text) {
    }

    @Override
    public void onDeviceRssiChanged(String s, int i, String name) {

    }



    /*@Override
    public void onGattError(int status) {

    }*/
    @Override
    public void modelCallback(ApplicationParameters.Address src,ApplicationParameters.Address dst)
    {

    }

    @Override
    public void onHeartBeatRecievedCallback(ApplicationParameters.Address address, ApplicationParameters.TTL ttl, ApplicationParameters.Features features) {

    }

    @Override
    public void isOOB_Output(Provisioner provisioner, boolean b) {

    }

    @Override
    public void isOOB_Input(Provisioner provisioner, String s) {

    }


}
