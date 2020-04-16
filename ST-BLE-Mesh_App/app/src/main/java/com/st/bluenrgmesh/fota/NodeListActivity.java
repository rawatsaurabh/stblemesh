/**
 ******************************************************************************
 * @file    NodeListActivity.java
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

package com.st.bluenrgmesh.fota;


import android.widget.Toast;

import androidx.annotation.NonNull;

import com.st.BlueNRG.fwUpgrade.BlueNRGAdvertiseFilter;
import com.st.BlueNRG.fwUpgrade.feature.BlueNRGOTASupport;
import com.st.BlueSTSDK.Node;
import com.st.BlueSTSDK.Utils.ConnectionOption;
import com.st.BlueSTSDK.Utils.advertise.AdvertiseFilter;
import com.st.BlueSTSDK.gui.fwUpgrade.FwUpgradeActivity;

import java.util.List;

public class NodeListActivity extends com.st.BlueSTSDK.gui.NodeListActivity
{
    @Override
    public boolean displayNode(@NonNull com.st.BlueSTSDK.Node n) {
        return true;
    }


    @Override
    public void onNodeSelected(@NonNull Node n) {

        ConnectionOption.ConnectionOptionBuilder optionsBuilder = ConnectionOption.builder()
                .setFeatureMap(BlueNRGOTASupport.getOTAFeatures());

        ConnectionOption options = optionsBuilder.build();
        startActivity(FwUpgradeActivity.getStartIntent(this,n,true,options));

    }

    @Override
    protected List<AdvertiseFilter> buildAdvertiseFilter() {
        List<AdvertiseFilter> defaultList =  super.buildAdvertiseFilter();
        defaultList.add(new BlueNRGAdvertiseFilter());
        return defaultList;
    }
}
