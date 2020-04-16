/**
 ******************************************************************************
 * @file    SoftwareComponents.java
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


package com.st.bluenrgmesh.view.fragments.other.meshmodels;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.other.LicenceAgreementFragment;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class SoftwareComponents extends BaseFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.software_components, container, false);

        Utils.updateActionBarForFeatures(getActivity(), new SoftwareComponents().getClass().getName());

        initUi();

        return view;
    }

    private void initUi() {
        TextView url1 = (TextView) view.findViewById(R.id.url1);
        url1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView url2 = (TextView) view.findViewById(R.id.url2);
        url2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView url3 = (TextView) view.findViewById(R.id.url3);
        url3.setMovementMethod(LinkMovementMethod.getInstance());

        TextView url4 = (TextView) view.findViewById(R.id.url4);
        url4.setMovementMethod(LinkMovementMethod.getInstance());

        TextView url5 = (TextView) view.findViewById(R.id.url5);
        url5.setMovementMethod(LinkMovementMethod.getInstance());

        TextView url6 = (TextView) view.findViewById(R.id.url6);
        url6.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
