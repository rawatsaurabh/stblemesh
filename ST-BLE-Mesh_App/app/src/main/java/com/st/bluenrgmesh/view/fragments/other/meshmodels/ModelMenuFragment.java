/**
 * *****************************************************************************
 *
 * @file ModelMenuFragment.java
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

package com.st.bluenrgmesh.view.fragments.other.meshmodels;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ModelTabFragment;

import org.json.JSONObject;

import java.util.ArrayList;


public class ModelMenuFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private LinearLayout lytGenericModel;
    private LinearLayout lytVenderModel, lytlightlingModel;
    private LinearLayout lytSensorModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_model_menu, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new ModelMenuFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        lytGenericModel = (LinearLayout) view.findViewById(R.id.lytGenericModel);
        lytVenderModel = (LinearLayout) view.findViewById(R.id.lytVenderModel);
        lytlightlingModel = (LinearLayout) view.findViewById(R.id.lytlightlingModel);
        lytSensorModel = (LinearLayout) view.findViewById(R.id.lytSensorModel);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkTypesOfModelsExist();
            }
        });

        lytlightlingModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                loader.show();
                Utils.setSelectedModel(getActivity(), getString(R.string.str_lighting_model_label));

                try {
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().size() > 0) {
                        ((MainActivity) getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), getString(R.string.str_lighting_model_label), 0, null, false, null);
                        ((MainActivity) getActivity()).fragmentCommunication(new ModelTabFragment().getClass().getName(), getString(R.string.str_lighting_model_label), 0, null, false, null);
                        loader.hide();
                        String topFragmentInBackStack = Utils.getTopFragmentInBackStack(getActivity());
                        if (topFragmentInBackStack.equalsIgnoreCase(new ModelMenuFragment().getClass().getName())) {
                            ((MainActivity) getActivity()).onBackPressed();
                        }
                        Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_lighting_model_label));
                    }
                } catch (Exception e) {
                    loader.hide();
                }
            }
        });

        lytGenericModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                Utils.setSelectedModel(getActivity(), getString(R.string.str_genericmodel_label));

                try {
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().size() > 0) {
                        ((MainActivity) getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), getString(R.string.str_genericmodel_label), 0, null, false, null);
                        ((MainActivity) getActivity()).fragmentCommunication(new ModelTabFragment().getClass().getName(), getString(R.string.str_genericmodel_label), 0, null, false, null);
                        String topFragmentInBackStack = Utils.getTopFragmentInBackStack(getActivity());
                        if (topFragmentInBackStack.equalsIgnoreCase(new ModelMenuFragment().getClass().getName())) {
                            ((MainActivity) getActivity()).onBackPressed();
                        }
                        Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_genericmodel_label));
                    }
                } catch (Exception e) {
                }
            }
        });

        lytVenderModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                Utils.setSelectedModel(getActivity(), getString(R.string.str_vendormodel_label));

                try {
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().size() > 0) {
                        ((MainActivity) getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), getString(R.string.str_vendormodel_label), 0, null, false, null);
                        ((MainActivity) getActivity()).fragmentCommunication(new ModelTabFragment().getClass().getName(), getString(R.string.str_vendormodel_label), 0, null, false, null);
                        String topFragmentInBackStack = Utils.getTopFragmentInBackStack(getActivity());
                        if (topFragmentInBackStack.equalsIgnoreCase(new ModelMenuFragment().getClass().getName())) {
                            ((MainActivity) getActivity()).onBackPressed();
                        }
                        Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_vendormodel_label));
                    }
                } catch (Exception e) {
                }
            }
        });


        lytSensorModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.setSelectedModel(getActivity(), getString(R.string.str_sensormodel_label));

                try {

                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(150);
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().size() > 0) {
                        ((MainActivity) getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), getString(R.string.str_sensormodel_label), 0, null, false, null);
                        ((MainActivity) getActivity()).fragmentCommunication(new ModelTabFragment().getClass().getName(), getString(R.string.str_sensormodel_label), 0, null, false, null);
                        String topFragmentInBackStack = Utils.getTopFragmentInBackStack(getActivity());
                        if (topFragmentInBackStack.equalsIgnoreCase(new ModelMenuFragment().getClass().getName())) {
                            ((MainActivity) getActivity()).onBackPressed();
                        }
                        Utils.updateActionBarForFeatures(getActivity(), new ModelTabFragment().getClass().getName(), getString(R.string.str_sensormodel_label));
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    private void checkTypesOfModelsExist() {

        lytGenericModel.setVisibility(View.GONE);
        lytVenderModel.setVisibility(View.GONE);
        lytlightlingModel.setVisibility(View.GONE);
        lytSensorModel.setVisibility(View.GONE);


        if (((MainActivity)getActivity()).meshRootClass != null && ((MainActivity)getActivity()).meshRootClass.getNodes() != null) {
            try {
                for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                    if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements() != null) {
                        for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                            if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels() != null) {
                                for (int k = 0; k < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                                    String modelId = ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId();
                                    if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1300_LIGHT_LIGHTNESS_MODEL))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1301_LIGHT_LIGHTNESS_SETUP_MODEL))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1303_LIGHT_CTL_SERVER))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1304_LIGHT_CTL_SETUP_SERVER))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1306_LIGHT_CTL_TEMPERATURE_SERVER))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1307_LIGHT_HSL_SERVER))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1308_LIGHT_HSL_SETUP_SERVER))
                                    ) {
                                        //Light Model
                                        lytlightlingModel.setVisibility(View.VISIBLE);
                                    } else if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1002_GENERIC_LEVEL))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1000_GENERIC_STATUS))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_100c_GENERIC_BATTERY))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1000_GENERIC_ONOFF))) {
                                        //Generic model
                                        lytGenericModel.setVisibility(View.VISIBLE);
                                    } else if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_00010030_VENDOR_MODEL))
                                            || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_10030_VENDOR_MODEL)) || modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_00300001_VENDOR_MODEL))) {
                                        //vendor model
                                        lytVenderModel.setVisibility(View.VISIBLE);
                                    } else if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_0000_CONFIGURATION_SERVER))) {
                                        //configuration Server
                                    }
                                    else if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_0001_CONFIGURATION_CLIENT))) {
                                        //configuration Client
                                    }
                                    else if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_0002_HEALTH_SERVER))) {
                                        //health
                                    } else if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1100_SENSOR_MODEL))) {
                                        //sensor
                                        lytSensorModel.setVisibility(View.VISIBLE);
                                    } else if (modelId.equalsIgnoreCase(getString(R.string.MODEL_ID_1200_TIME_MODEL))) {
                                        //time
                                    }
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){}
        }
    }
}

