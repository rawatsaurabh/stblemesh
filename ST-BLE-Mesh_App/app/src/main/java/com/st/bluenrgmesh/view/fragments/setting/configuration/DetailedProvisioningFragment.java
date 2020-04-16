/**
 * *****************************************************************************
 *
 * @file DetailedProvisioningFragment.java
 * @author BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
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
package com.st.bluenrgmesh.view.fragments.setting.configuration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

public class DetailedProvisioningFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private Element element;
    private RecyclerView recyclerAppKey;
    private RecyclerView recyclerModel;
    private CardView cdModelSelection;
    private TextView txtBindMoreKey;
    private TextView txtModelName;
    private TextView txtModelcount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detailed_provisioning, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        initUI();

        return view;
    }

    private void initUI() {

        element = (Element) getArguments().getSerializable(getString(R.string.key_serializable));
        recyclerAppKey = (RecyclerView) view.findViewById(R.id.recyclerAppKey);
        recyclerModel = (RecyclerView) view.findViewById(R.id.recyclerModel);
        recyclerModel.setVisibility(View.GONE);
        cdModelSelection = (CardView) view.findViewById(R.id.cdModelSelection);
        txtBindMoreKey = (TextView) view.findViewById(R.id.txtBindMoreKey);
        txtBindMoreKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpForKeys(getActivity());
            }
        });
        txtModelName = (TextView) view.findViewById(R.id.txtModelName);
        txtModelcount = (TextView) view.findViewById(R.id.txtModelcount);
        txtModelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerModel.setVisibility(recyclerModel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

    }

    private void showPopUpForKeys(FragmentActivity activity) {

    }
}
