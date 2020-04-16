/**
 * *****************************************************************************
 *
 * @file ModelTabFragment.java
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

package com.st.bluenrgmesh.view.fragments.tabs;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.ProvisionedRecyclerAdapter;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;


public class ModelTabFragment extends BaseFragment {

    private static ModelTabFragment fragment;
    private View view;
    private RecyclerView recyclerView;
    private AppDialogLoader loader;
    public ProvisionedRecyclerAdapter provisionedRecyclerAdapter;
    private SwipeRefreshLayout swiperefresh;
    public static ArrayList<Nodes> nodes = new ArrayList<>();
    public static String modelTypeSelected;
    public int elementPosition = 0;
    public Nodes sensorNode = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_model, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        initUi();

        return view;
    }

    private void initUi() {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initRecyclerView();
            }
        });
    }


    private void initRecyclerView() {

        try {
            Collections.sort(nodes);
        } catch (Exception e) {
        }

        recyclerView.setVisibility(View.VISIBLE);
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        provisionedRecyclerAdapter = new ProvisionedRecyclerAdapter(getActivity(), new ModelTabFragment().getClass().getName(), nodes, null, false, new ProvisionedRecyclerAdapter.IRecyclerViewHolderClicks() {

            @Override
            public void onClickRecyclerItem(View v, int position, String item, String mAutoAddress, boolean isSelected) {

            }

            @Override
            public void notifyAdapter(String selected_element_address, boolean is_command_error) {

            }

            @Override
            public void notifyPosition(int elementPos, Nodes node) {
                elementPosition = elementPos;
                sensorNode = node;
                //enableProgressBar(node);
            }
        });
        recyclerView.setAdapter(provisionedRecyclerAdapter);
        Utils.runLayoutAnimation(recyclerView, provisionedRecyclerAdapter);
        loader.hide();

    }

    private void enableProgressBar(Nodes node) {
        for (int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(node.getElements().get(0).getUnicastAddress()))
            {
                nodes.get(i).setShowProgress(true);
            }
        }
    }

    public void cleanModelUi()
    {
        try {
            nodes.clear();
            if(provisionedRecyclerAdapter != null)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        provisionedRecyclerAdapter.notifyDataSetChanged();
                    }
                });
            }
        }catch (Exception e){}
    }

    public void updateModelRecycler(String modelType) {

        recyclerView.setVisibility(View.GONE);
        //loader.show();

        modelTypeSelected = modelType;
        Utils.DEBUG(">> Model type selected : " + modelTypeSelected);

        if (modelTypeSelected == null) {
            Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_vendormodel_label));
        } else {
            if (modelTypeSelected.equals(getString(R.string.str_genericmodel_label))) {
                Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_genericmodel_label));
            } else if (modelTypeSelected.equalsIgnoreCase(getString(R.string.str_lighting_model_label))) {
                Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_lighting_model_label));
            } else if (modelTypeSelected.equalsIgnoreCase(getString(R.string.str_vendormodel_label))) {
                Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_vendormodel_label));
            } else if (modelTypeSelected.equalsIgnoreCase(getString(R.string.str_sensormodel_label))) {
                Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_sensormodel_label));
            }

        }
        nodes = Utils.getModelData(getActivity() , modelTypeSelected);
        //((MainActivity)getActivity()).updateJsonData();


        initRecyclerView();
    }

    private void notifyDataChange() {
        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                provisionedRecyclerAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                loader.hide();
            }
        });*/
    }

    public static Fragment newInstance() {

        if( fragment == null ) {
            fragment = new ModelTabFragment();
        }
        return fragment;
    }

    /**
     *
     * @param sensorValue : Sensor Data
     * @param propertyId : sensorPropertyId
     */
    public void updateSensorValues(String sensorValue, int propertyId) {

        //loader.show();
        if(sensorValue != null)
        {
            if(provisionedRecyclerAdapter != null)
            {
                provisionedRecyclerAdapter.updateElementData(sensorValue, elementPosition, sensorNode, recyclerView);
            }
        }
        else
        {
            for (int i = 0; i < nodes.size(); i++) {
                if(nodes.get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(sensorNode.getElements().get(0).getUnicastAddress()))
                {
                    nodes.get(i).setShowProgress(false);
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(provisionedRecyclerAdapter != null)
                    {
                        provisionedRecyclerAdapter.notifyDataSetChanged();
                    }
                    loader.hide();
                }
            });

        }


    }
}
