/**
 ******************************************************************************
 * @file    Nucleo.java
 * @author  BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
 * @brief   Commands Application file
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

package com.st.bluenrgmesh.datamap;

import com.msi.moble.DataMap.Base;

/**
 * This is Set of Application Commands to be sent to the Nodes.
 */
public final class Nucleo extends Base {


    public final static byte APPLI_CMD_UNPROVISION= 0;

    public final static byte APPLI_CMD_TEST_RSVD = 1;
    public final static byte APPLI_CMD_TEST_RSVD_TEST_RESET_PARAMS = 1;
    public final static byte APPLI_CMD_TEST_RSVD_TEST_ECHO = 2;
    public final static byte APPLI_CMD_TEST_RSVD_TEST_RANDMZD_RESP = 3;
    public final static byte APPLI_CMD_TEST_RSVD_TEST_COUNTER = 4;
    public final static byte APPLI_CMD_TEST_RSVD_TEST_INC_COUNTER = 5;


    public final static byte APPLI_CMD_DEVICE = 2;
    public final static byte APPLI_CMD_DEVICE_IC_TYPE = 1;
    public final static byte APPLI_CMD_DEVICE_BMESH_LIBVER = 2;
    public final static byte APPLI_CMD_DEVICE_BMESH_LIBVER_SUB = 3;
    public final static byte APPLI_CMD_DEVICE_BMESH_APPVER = 4;



    public final static byte APPLI_CMD_LED_CONTROL = 3;
    public final static byte APPLI_CMD_LED_ON = 1;
    public final static byte APPLI_CMD_LED_OFF = 2;
    public final static byte APPLI_CMD_LED_TOGGLE = 3;
    public final static byte APPLI_CMD_LED_BULB = 5;
    public final static byte APPLI_CMD_LED_INTENSITY = 6;



    public final static byte APPLI_CMD_DEVICE_TYPE = 4;


    public final static byte APPLI_CMD_SENSORS = 5;
    public final static byte APPLI_CMD_TEMPERATURE = 1;
    public final static byte APPLI_CMD_SENSORS_PRESSURE = 2;
    public final static byte APPLI_CMD_SENSORS_ACCEL = 3;

    public final static byte APPLI_TEST_INVALID_CMD = 6;


    public final static byte APPLI_OTA_CHECK = 0x07;
    public final static byte APPLI_TEST_CMD = 0x1;
    public final static byte APPLI_ENABLE_OTA = 0x08;


}

