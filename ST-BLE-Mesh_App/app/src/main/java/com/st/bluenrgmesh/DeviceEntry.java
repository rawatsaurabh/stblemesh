/**
 ******************************************************************************
 * @file    DeviceEntry.java
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

package com.st.bluenrgmesh;

import com.msi.moble.mobleAddress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Device Entry for internal use. Stores the name, address and dimming of the light
 */
public class DeviceEntry implements Serializable{
    private String mName;
    private final mobleAddress mAddress;
    private Byte mDimming;
    private int mDeviceType;
    /**
     * The constant UNKNOWN_DEVICE_TYPE.
     */
    public static final int UNKNOWN_DEVICE_TYPE = 0x0000ffff;
    /**
     * The constant BULB_DEVICE_TYPE.
     */
    public static final int BULB_DEVICE_TYPE = 0;
    /**
     * The constant ACCELEROMETER_DEVICE_TYPE.
     */
    public static final int ACCELEROMETER_DEVICE_TYPE = 1;
    /**
     * The constant THERMOMETER_DEVICE_TYPE.
     */
    public static final int THERMOMETER_DEVICE_TYPE = 2;

    /**
     * Instantiates a new Device entry.
     *
     * @param name    the name
     * @param address the address
     */
    
    public DeviceEntry(String name, mobleAddress address) {
        if (name == null || address == null) {
            throw new IllegalArgumentException();
        }
        mName = name;
        mAddress = address;
        mDeviceType = UNKNOWN_DEVICE_TYPE;

    }

    /**
     * Instantiates a new Device entry.
     *
     * @param name     the name
     * @param address  the address
     * @param dev_type the dev type
     */
    DeviceEntry(String name, mobleAddress address, int dev_type) {
        if (name == null || address == null) {
            throw new IllegalArgumentException();
        }
        mName = name;
        mAddress = address;
        mDeviceType = dev_type;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    
    public String getName() {
        return mName;
    }

    /**
     * Set name.
     *
     * @param mName the m name
     */
    void setName(String mName){
        this.mName = mName;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    
    public mobleAddress getAddress() {
        return mAddress;
    }

    /**
     * Save.
     *
     * @param ostm the ostm
     * @throws IOException the io exception
     */
    void save(DataOutputStream ostm) throws IOException {
        ostm.writeUTF(mName);
        ostm.writeInt(mDeviceType);
        mAddress.save(ostm);
    }

    /**
     * Gets device type.
     *
     * @return the device type
     */
    
    public int getDeviceType() {
        return mDeviceType;
    }

    /**
     * Sets device type.
     *
     * @param deviceType the device type
     */
    
    public void setDeviceType(int deviceType) {
        mDeviceType = deviceType;
    }

    /**
     * Load device entry.
     *
     * @param istm the istm
     * @return the device entry
     * @throws IOException the io exception
     */
    static DeviceEntry load(DataInputStream istm) throws IOException {
        String name = istm.readUTF();
        int dev_type = istm.readInt();
        mobleAddress address = mobleAddress.load(istm);
        return new DeviceEntry(name, address, dev_type);
    }

}
