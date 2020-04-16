/**
 ******************************************************************************
 * @file    GroupEntry.java
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
import java.util.ArrayList;
import java.util.Collection;

/**
 * Device Entry for internal use. Stores the name and address
 */
public class GroupEntry {
    private String mName;
    private final Collection<String> mDevices;
    /**
     * The constant LIGHTS_GROUP.
     */
    public final static int LIGHTS_GROUP = mobleAddress.GROUP_HEADER;
    /**
     * The constant ACCELEROMETERS_GROUP.
     */
    public final static int ACCELEROMETERS_GROUP = 1 | mobleAddress.GROUP_HEADER;
    /**
     * The constant THERMOMETER_GROUP.
     */
    public final static int THERMOMETER_GROUP = 2 | mobleAddress.GROUP_HEADER;

    /**
     * Test Groups.
     */
    public final static int TEST_GROUP_1 = 0xC005;

    public final static int TEST_GROUP_2 = 0xC006;

    public final static int TEST_GROUP_3 = 0xC007;



    /**
     * Instantiates a new Group entry.
     *
     * @param name the name
     */
    public GroupEntry(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        mName = name;
        mDevices = new ArrayList<>();
    }

    /**
     * Instantiates a new Group entry.
     *
     * @param name    the name
     * @param devices the devices
     */
    GroupEntry(String name, Collection<String> devices) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        mName = name;
        mDevices = devices;
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
    public void setName (String mName){
        this.mName = mName;
    }

    /**
     * Gets devices.
     *
     * @return the devices
     */
    public Collection<String> getDevices() {
        Collection<String> tmp = new ArrayList<>();
        for (String mac : mDevices) {
            tmp.add(mac);
        }
        return tmp;
    }

    /**
     * Save.
     *
     * @param ostm the ostm
     * @throws IOException the io exception
     */
    void save(DataOutputStream ostm) throws IOException {
        ostm.writeUTF(mName);
        ostm.writeInt(mDevices.size());
        for (String mac : mDevices) {
            ostm.writeUTF(mac);
        }
    }

    /**
     * Load group entry.
     *
     * @param istm the istm
     * @return the group entry
     * @throws IOException the io exception
     */
    static GroupEntry load(DataInputStream istm) throws IOException {
        String name = istm.readUTF();
        int count = istm.readInt();
        Collection<String> list = new ArrayList<>();
        for (int i=0; i<count; ++i) {
            list.add(istm.readUTF());
        }
        return new GroupEntry(name, list);
    }
}
