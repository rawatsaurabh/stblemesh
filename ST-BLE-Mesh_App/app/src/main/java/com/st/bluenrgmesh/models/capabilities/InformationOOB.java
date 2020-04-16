/**
 ******************************************************************************
 * @file    InformationOOB.java
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
package com.st.bluenrgmesh.models.capabilities;

public class InformationOOB {

    private boolean Other;
    private boolean ElectronicURI;
    private boolean MachineReadableCode;
    private boolean BarCode;
    private boolean NFC;
    private boolean Number;
    private boolean String;
    private boolean OnBox;
    private boolean InsideBox;
    private boolean OnPieceOfPaper;
    private boolean InsideManual;
    private boolean OnDevice;

    public boolean isOther() {
        return Other;
    }

    public void setOther(boolean other) {
        Other = other;
    }

    public boolean isElectronicURI() {
        return ElectronicURI;
    }

    public void setElectronicURI(boolean electronicURI) {
        ElectronicURI = electronicURI;
    }

    public boolean isMachineReadableCode() {
        return MachineReadableCode;
    }

    public void setMachineReadableCode(boolean machineReadableCode) {
        MachineReadableCode = machineReadableCode;
    }

    public boolean isBarCode() {
        return BarCode;
    }

    public void setBarCode(boolean barCode) {
        BarCode = barCode;
    }

    public boolean isNFC() {
        return NFC;
    }

    public void setNFC(boolean NFC) {
        this.NFC = NFC;
    }

    public boolean isNumber() {
        return Number;
    }

    public void setNumber(boolean number) {
        Number = number;
    }

    public boolean isString() {
        return String;
    }

    public void setString(boolean string) {
        String = string;
    }

    public boolean isOnBox() {
        return OnBox;
    }

    public void setOnBox(boolean onBox) {
        OnBox = onBox;
    }

    public boolean isInsideBox() {
        return InsideBox;
    }

    public void setInsideBox(boolean insideBox) {
        InsideBox = insideBox;
    }

    public boolean isOnPieceOfPaper() {
        return OnPieceOfPaper;
    }

    public void setOnPieceOfPaper(boolean onPieceOfPaper) {
        OnPieceOfPaper = onPieceOfPaper;
    }

    public boolean isInsideManual() {
        return InsideManual;
    }

    public void setInsideManual(boolean insideManual) {
        InsideManual = insideManual;
    }

    public boolean isOnDevice() {
        return OnDevice;
    }

    public void setOnDevice(boolean onDevice) {
        OnDevice = onDevice;
    }


}
