/**
 * *****************************************************************************
 *
 * @file ElementsRecylerAdapter.java
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

package com.st.bluenrgmesh.adapter.elementsettings;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.util.Util;
import com.msi.moble.ApplicationParameters;
import com.msi.moble.GenericLevelModelClient;
import com.msi.moble.GenericPowerOnOffModelClient;
import com.msi.moble.LightCTLModelClient;
import com.msi.moble.LightHSLModelClient;
import com.msi.moble.LightLightnessModelClient;
import com.msi.moble.SensorModelClient;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.models.SensorHorizontalRecyclerAdapter;
import com.st.bluenrgmesh.adapter.models.SensorModelRecyclerAdapter;
import com.st.bluenrgmesh.colorpicker.CustomDialog;
import com.st.bluenrgmesh.datamap.Nucleo;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.compositiondata.ModelsData;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.models.sensor.CompleteSensorData;
import com.st.bluenrgmesh.models.sensor.SensorData;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.NegativeSeekBar;
import com.st.bluenrgmesh.view.fragments.other.GenericPowerFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.sensor.SensorConfigFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.LoadConfigFeaturesFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ModelTabFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ElementsRecyclerAdapter extends RecyclerView.Adapter<ElementsRecyclerAdapter.ViewHolder> {

    private static final int LIGHTING_LIGHTNESS_MAX = 65535;
    private  String from;
    private ModelsData modelsData;
    //private final ArrayList<String> models_list = new ArrayList<String>();
    private String classname;
    private String element_Address = null;
    private int nodePosition = -1;
    private boolean is_command_error = false;
    private mobleAddress address;
    private Nodes node;
    private ArrayList<Element> elements;
    private Context context;
    private IRecyclerViewHolderClicks listener;
    private boolean is_lyt_warning = false;
    private MeshRootClass meshRootClass;
    private int GENERIC_LEVEL_MAX = 32767;
    GenericLevelModelClient mGenericLevelModel;

    SharedPreferences pref_model_selection;
    private LightCTLModelClient lightCTLModelClient;
    private boolean is_ctl_selected = true;
    private LightLightnessModelClient lightLightnessModelClient;
    private LightHSLModelClient lightHSLModelClient;
    private int which_hsl_tab_selected = 1;
    private boolean send_hsl_default_in_HSL_TAB = false;

    private String selectedModel;

    private Boolean enableLogs = false;
    private String data_name;
    private String element_name = "Element";
HashMap<String,ArrayList<Model>>hashMap_model = new HashMap<>();
    public ElementsRecyclerAdapter(String from,Context context, String classname, Nodes node, boolean is_command_error, int command_position, String element_Address, IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {
this.from = from;
        this.classname = classname;
        this.context = context;
        this.node = node;
        this.listener = iRecyclerViewHolderClicks;
        this.elements = node.getElements();
        this.is_command_error = is_command_error;
        this.nodePosition = command_position;
        this.element_Address = element_Address;
        selectedModel = Utils.getSelectedModel(context);
        updateJsonData();
        setHasStableIds(true);


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateElementSensorData(String sensor, int position) {
        elements.get(position).setCompleteSensorData(sensor);
    }

    public void showErrorCommand(int position, String address) {
        updateJsonData();
        is_lyt_warning = true;
        int position_notify = -1;

        if (meshRootClass != null && meshRootClass.getNodes().size() > 0) {
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    if (address.equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress())) {
                        position_notify = j;
                        break;
                    }
                }
            }
        }
        if (position_notify > -1) {
            listener.onClickRecyclerItem(address, position_notify);
            //notifyItemChanged(position_notify);
        } else {
            Utils.showToast(context, "Unable to Execute Command! Kindly refersh the Page.");
        }


    }


    private void updateJsonData() {

        try {
            meshRootClass = (MeshRootClass) ParseManager.getInstance().fromJSON(
                    new JSONObject(Utils.getBLEMeshDataFromLocal(context)), MeshRootClass.class).clone();
        if(meshRootClass!=null) {
            for (int j = 0; j < elements.size(); j++) {
                for (int i = 0; i < meshRootClass.getNodes().get(j + 1).getElements().size(); i++) {
                    for (int k = 0; k < meshRootClass.getNodes().get(j + 1).getElements().get(i).getModels().size(); k++) {

                        hashMap_model.put(elements.get(i).getUnicastAddress(),
                                meshRootClass.getNodes().get(j + 1).getElements().get(i).getModels());

                    }
                }
    }
}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface IRecyclerViewHolderClicks {

        void onClickRecyclerItem(String element_address, int nodePosition);

        void onElementToggle(String addres, int ele_pos, byte status);

        void showFrameOverNode(String addres, int ele_pos);

        void onSensorRefresh(Nodes node, int ele_pos, ApplicationParameters.SensorDataPropertyId propertyID, int nodePosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_recycler_row, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.imageSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(150);
                    MainActivity.mCid = Integer.parseInt(node.getCid());
                    elements.get(position).setParentNodeAddress(elements.get(0).getUnicastAddress());
                    elements.get(position).setParentNodeName(node.getName());
                    switchToSettingsPage(v, event, elements.get(0).getUnicastAddress(), position);
                } catch (Exception e) {
                    Utils.showToast(context, "First configure your device.");
                }
                return true;
            }
        });

        if (elements != null && elements.size() > 0) {
            data_name = elements.get(position).getName();
            if (data_name.length() > 12) {
                element_name = data_name.substring(0, 12) + "\u2026";

            } else {

                element_name = data_name;

            }
        }
        holder.textViewTitle.setText("" + element_name);

        holder.lytWarning.setOnClickListener(new View.OnClickListener() {
            boolean visible;

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(holder.transitionsContainer);
                    visible = !visible;
                    holder.lytWarningMsg.setVisibility(visible ? View.VISIBLE : View.GONE);
                } else {
                    holder.lytWarningMsg.setVisibility(holder.lytWarningMsg.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            }
        });


        if (is_command_error && element_Address != null && element_Address.equalsIgnoreCase(elements.get(position).getUnicastAddress())) {
            holder.lytWarning.setVisibility(View.VISIBLE);
            holder.lytWarningMsg.setVisibility(View.VISIBLE);
            is_lyt_warning = false;
            is_command_error = false;
            holder.butSwitch.setChecked(false);
        } else {
            holder.lytWarning.setVisibility(View.GONE);
            holder.lytWarningMsg.setVisibility(View.GONE);
            is_lyt_warning = false;
            is_command_error = false;
        }

        if (classname.equalsIgnoreCase(new ModelTabFragment().getClass().getName())) {
            //for model tab
            holder.imageButtons.setVisibility(View.GONE);
            holder.lytModelSupport.setVisibility(View.VISIBLE);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //updateSensorList(holder, elements.get(position));
                    updateUiForModels(holder, elements.get(position));
                }
            });

        } else {
            //for provisioned tab
            holder.imageItemDevice.setVisibility(View.GONE);
            holder.imageButtons.setVisibility(View.VISIBLE);
            holder.lytModelSupport.setVisibility(View.GONE);

            //holder.lytAllModels = null;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Element clone = null;
                    try {
                        clone = (Element) elements.get(position).clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    layLoopForModels(context, holder.lytAllModels, clone);
                }
            });
        }


        if (((MainActivity) context).tabSelected == ((MainActivity) context).PROVISIONED_TAB) {
            holder.imageItemDevice.setVisibility(View.GONE);
            holder.butSwitch.setVisibility(View.VISIBLE);
        }

        holder.lytTemperature.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                ((MainActivity) context).sensorPropertyId = context.getResources().getInteger(R.integer.SENSOR_MODEL_TEMP_PROPERTYID);
                ApplicationParameters.SensorDataPropertyId propertyID = new ApplicationParameters.SensorDataPropertyId(0x0071);
                Utils.sensorModelEvents(context, event, listener, position, node, propertyID, nodePosition, false);

                return true;
            }
        });

        holder.lytPressure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                ((MainActivity) context).sensorPropertyId = context.getResources().getInteger(R.integer.SENSOR_MODEL_PRESSURE_PROPERTYID);
                ApplicationParameters.SensorDataPropertyId propertyID = new ApplicationParameters.SensorDataPropertyId(0x2a6d);
                Utils.sensorModelEvents(context, event, listener, position, node, propertyID, nodePosition, false);

                return true;
            }
        });
     /*   holder.imageItemDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSensorDescriptor();
            }
        });*/
        holder.imageItemDevice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //refresh
                        Utils.setClassName(context, new ModelTabFragment().getClass().getName());
                        ((MainActivity) context).sensorPropertyId = context.getResources().getInteger(R.integer.SENSOR_MODEL_DEFAULT_PROPERTYID);
                        ApplicationParameters.SensorDataPropertyId propertyID = null;
                        Utils.sensorModelEvents(context, event, listener, position, node, propertyID, nodePosition, false);
                    }
                });


                return true;
            }
        });

        holder.imgRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                if (selectedModel.equalsIgnoreCase(context.getString(R.string.str_genericmodel_label))) {
                    Utils.setClassName(context, new GenericPowerFragment().getClass().getName());

                    Utils.moveToFragment((MainActivity) context, new GenericPowerFragment(), node.getElements().get(0).getUnicastAddress(), 0);

                } else {
                    //sensor model
                    Utils.setClassName(context, new SensorConfigFragment().getClass().getName());
                    ((MainActivity) context).sensorPropertyId = context.getResources().getInteger(R.integer.SENSOR_MODEL_DEFAULT_PROPERTYID);
                    ApplicationParameters.SensorDataPropertyId propertyID = null;
                    //listener.showFrameOverNode(elements.get(position).getUnicastAddress(), position);
                    listener.onSensorRefresh(node, position, propertyID, nodePosition);
                    Utils.moveToFragment((MainActivity) context, new SensorConfigFragment(), node.getElements().get(position), 0);
                }
            }
        });

        holder.butSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                holder.lytWarning.setVisibility(View.GONE);
                holder.lytWarningMsg.setVisibility(View.GONE);

                if (Utils.getProxyNode(context) != null) {
                    //check activated key w.r.t element
                    Utils.toggleDevice(from,context, v, event, elements.get(position), listener, position,selectedModel);

                } else {
                    Utils.showToast(context, "Trying to connect with proxy device.");
                    if(node != null && node.getAddress() != null)
                    {
                        MainActivity.mSettings.setCustomProxy(node.getAddress());
                    }
                }

                return false;
            }
        });


        holder.linear_Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                int addr = Integer.parseInt(node.getElements().get(position).getUnicastAddress().toString(), 16);

                ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().unadvise(Utils.mLibraryVersionCallback);
                mobleNetwork network = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork();
                network.getApplication().setRemoteData(mobleAddress.deviceAddress(addr), Nucleo.APPLI_CMD_LED_CONTROL, 1, new byte[]{Nucleo.APPLI_CMD_LED_TOGGLE}, true);
            }
        });

        holder.linear_Version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                int addr = Integer.parseInt(node.getElements().get(position).getUnicastAddress().toString(), 16);
                Utils.contextU = context;
                ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(Utils.mLibraryVersionCallback);
                mobleNetwork network = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork();
                network.getApplication().readRemoteData(mobleAddress.deviceAddress(addr), Nucleo.APPLI_CMD_DEVICE, 1, new byte[]{Nucleo.APPLI_CMD_DEVICE_BMESH_LIBVER}, true);

            }
        });

        holder.linear_Level.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.seekbar_res_file);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();
                SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
                final TextView txtIntensityValue1 = (TextView) dialog.findViewById(R.id.txtIntensityValue1);
                seekBar.setMax(GENERIC_LEVEL_MAX);
                Button closeBT = (Button) dialog.findViewById(R.id.closeBT);
                closeBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        //    String seekbarValue = String.valueOf(((seekBar.getProgress())*20);
                        txtIntensityValue1.setText(String.valueOf(seekBar.getProgress()));
                        int seekbar_val = (seekBar.getProgress() * 100) / GENERIC_LEVEL_MAX;
                        int intAddress = Integer.parseInt(node.getElements().get(position).getUnicastAddress().toString(), 16);

                        Utils.contextU = context;

                        if (selectedModel.equalsIgnoreCase("Vendor Model")) {
                            UserApplication.trace("NavBar Device toggle  model vendor model selected");

                            MainActivity.network.getApplication().setRemoteData(mobleAddress.deviceAddress(intAddress),
                                    Nucleo.APPLI_CMD_LED_CONTROL, 1, new byte[]{(byte) Nucleo.APPLI_CMD_LED_INTENSITY, (byte) (seekBar.getProgress() & 0xFF), (byte) ((seekBar.getProgress() >> 8) & 0xFF)}, Utils.isReliableEnabled(context));


                            ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Vendor Intensity command sent to ==>" + intAddress, LoggerConstants.TYPE_SEND);

                        } else {
                            //ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(node.getElements().get(position).getUnicastAddress().toString(), 16));
                            ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(node.getElements().get(position).getUnicastAddress().toString()));
                            UserApplication.trace("NavBar SeekBar Value Generic Model = " + txtIntensityValue1.getText().toString());

                            ApplicationParameters.Level level = new ApplicationParameters.Level(Integer.parseInt(txtIntensityValue1.getText().toString()));

                            if (((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork() != null) {
                                mGenericLevelModel = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().getLevelModel();
                            }
                            ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("GenericLevelModel data send==>" + elementAddress, LoggerConstants.TYPE_SEND);
                            mGenericLevelModel.setGenericLevel(Utils.isReliableEnabled(context),
                                    elementAddress,
                                    level,
                                    new ApplicationParameters.TID(Utils.getTIDValue(context)),
                                    null,
                                    null,
                                    Utils.isReliableEnabled(context) ? mLevelCallback : null);


                        }
                        TextView txtIntensityValue = (TextView) dialog.findViewById(R.id.txtIntensityValue);
                        txtIntensityValue.setText(seekbar_val + " %");
                    }
                });
            }
        });

        holder.imgBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.light_ctl_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLightingPopup(position, "CTL");
            }
        });


        holder.lightessLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLightingPopup(position, "Lightness");

            }
        });

        holder.light_hsl_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLightingPopup(position, "HSL");
            }
        });
        //holder.butSwitch.setChecked(elements.get(position).isChecked() == true ? true : false);
    }

    public void updateSensorList(ViewHolder holder, Element element) {
        CompleteSensorData completeSensorData = null;

        if (element.getCompleteSensorData() != null) {
            try {
                completeSensorData = ParseManager.getInstance().fromJSON(new JSONObject(element.getCompleteSensorData()), CompleteSensorData.class);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (completeSensorData == null) {
            return;
        }

        ArrayList<SensorData> sensorList = completeSensorData.getSensorList();
        initRecyclerAdapter(holder, sensorList);

    }


    private void initRecyclerAdapter(ViewHolder holder, ArrayList<SensorData> sensorList) {

        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false);
        holder.sensorRecyclerView.setLayoutManager(gridLayoutManager);

        SensorHorizontalRecyclerAdapter sensorHorizontalRecyclerAdapter = new SensorHorizontalRecyclerAdapter(context, sensorList, "fromModelTabScreen", new SensorModelRecyclerAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(final ApplicationParameters.SensorDataPropertyId propertyID, int position) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        /*SensorModelClient mSensorModelClient = ((MainActivity) context).network.getSensorModel();
                        ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(elementData.getUnicastAddress()));
                        ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("GetSensor command sent for==>" + elementAddress, LoggerConstants.TYPE_SEND);
                        mSensorModelClient.getSensor(elementAddress, propertyID, SensorModelCallbacks.mGetSensorStatusCallback);*/
                    }
                });
            }
        });
        holder.sensorRecyclerView.setAdapter(sensorHorizontalRecyclerAdapter);
    }

    private void updateUiForModels(ViewHolder holder, Element element) {

        if (selectedModel.equalsIgnoreCase(context.getString(R.string.str_genericmodel_label))) {
            holder.txtLevelIntensity.setText("Level");
            holder.linear_Toggle.setVisibility(View.GONE);
            holder.linear_Version.setVisibility(View.GONE);
            holder.linear_Level.setVisibility(View.VISIBLE);

            holder.light_ctl_LL.setVisibility(View.GONE);
            holder.lightessLL.setVisibility(View.GONE);
            holder.light_hsl_LL.setVisibility(View.GONE);
            holder.lytAccelerometer.setVisibility(View.GONE);
            holder.lytPressure.setVisibility(View.GONE);
            holder.lytTemperature.setVisibility(View.GONE);
            holder.imageItemDevice.setVisibility(View.GONE);
            holder.butSwitch.setVisibility(View.VISIBLE);
            holder.sensorRecyclerView.setVisibility(View.GONE);
            holder.imgRightArrow.setClickable(true);

            updateElementsUIAccordingoModel(holder,element, holder.linear_Level,null,null,context.getString(R.string.MODEL_ID_1002_GENERIC_LEVEL));


            if (hashMap_model != null && hashMap_model.size() > 0) {
                for (int i = 0; i < hashMap_model.get(element.getUnicastAddress()).size(); i++) {

                    if (hashMap_model.get(element.getUnicastAddress()).get(i)

                            .getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1006_GENERIC_POWER_ONOFF))) {
                        holder.imgRightArrow.setVisibility(View.VISIBLE);
                        break;
                    }else {
                        holder.imgRightArrow.setVisibility(View.GONE);

                    }
                }
            }

        } else if (selectedModel.equalsIgnoreCase(context.getString(R.string.str_lighting_model_label))) {
            for (int i = 0; i < element.getModels().size(); i++) {
                String modelId = element.getModels().get(i).getModelId();
                if (modelId.equalsIgnoreCase(context.getString(R.string.MODEL_ID_1303_LIGHT_CTL_SERVER))
                        || modelId.equalsIgnoreCase(context.getString(R.string.MODEL_ID_1304_LIGHT_CTL_SETUP_SERVER))
                        || modelId.equalsIgnoreCase(context.getString(R.string.MODEL_ID_1306_LIGHT_CTL_TEMPERATURE_SERVER))) {
                    //CTL UI
                    holder.light_ctl_LL.setVisibility(View.VISIBLE);
                }

                if (modelId.equalsIgnoreCase(context.getString(R.string.MODEL_ID_1307_LIGHT_HSL_SERVER))
                        || modelId.equalsIgnoreCase(context.getString(R.string.MODEL_ID_1308_LIGHT_HSL_SETUP_SERVER))) {
                    //HSL UI
                    holder.light_hsl_LL.setVisibility(View.VISIBLE);
                }

            }

            holder.lightessLL.setVisibility(View.VISIBLE);
            holder.butSwitch.setVisibility(View.VISIBLE);
            holder.lytAccelerometer.setVisibility(View.GONE);
            holder.lytPressure.setVisibility(View.GONE);
            holder.lytTemperature.setVisibility(View.GONE);
            holder.imageItemDevice.setVisibility(View.GONE);
            holder.linear_Toggle.setVisibility(View.GONE);
            holder.linear_Version.setVisibility(View.GONE);
            holder.linear_Level.setVisibility(View.GONE);
            holder.sensorRecyclerView.setVisibility(View.GONE);
            holder.imgRightArrow.setClickable(false);


            updateElementsUIAccordingoModel(holder,element, holder.lightessLL,null,null,context.getString(R.string.MODEL_ID_1300_LIGHT_LIGHTNESS_MODEL));

        } else if (selectedModel.equalsIgnoreCase(context.getString(R.string.str_vendormodel_label))) {
            holder.txtLevelIntensity.setText("Intensity");
            holder.linear_Toggle.setVisibility(View.VISIBLE);
            holder.linear_Version.setVisibility(View.VISIBLE);
            holder.linear_Level.setVisibility(View.VISIBLE);
            holder.light_ctl_LL.setVisibility(View.GONE);
            holder.lightessLL.setVisibility(View.GONE);
            holder.light_hsl_LL.setVisibility(View.GONE);
            holder.lytAccelerometer.setVisibility(View.GONE);
            holder.lytPressure.setVisibility(View.GONE);
            holder.lytTemperature.setVisibility(View.GONE);
            holder.imageItemDevice.setVisibility(View.GONE);
            holder.butSwitch.setVisibility(View.VISIBLE);
            holder.sensorRecyclerView.setVisibility(View.GONE);
            holder.imgRightArrow.setClickable(false);

            updateElementsUIAccordingoModel(holder,element,holder.linear_Toggle,holder.linear_Level,holder.linear_Version,context.getString(R.string.MODEL_ID_00010030_VENDOR_MODEL));



        } else if (selectedModel.equalsIgnoreCase(context.getString(R.string.str_sensormodel_label))) {
            updateSensorList(holder, element);

            holder.linear_Toggle.setVisibility(View.GONE);
            holder.linear_Version.setVisibility(View.GONE);
            holder.linear_Level.setVisibility(View.GONE);
            holder.light_ctl_LL.setVisibility(View.GONE);
            holder.lightessLL.setVisibility(View.GONE);
            holder.light_hsl_LL.setVisibility(View.GONE);
            holder.lytAccelerometer.setVisibility(View.GONE);
            holder.lytPressure.setVisibility(View.GONE);
            holder.lytTemperature.setVisibility(View.GONE);
            holder.imageItemDevice.setVisibility(View.VISIBLE);
            holder.butSwitch.setVisibility(View.GONE);
            holder.sensorRecyclerView.setVisibility(View.VISIBLE);
            holder.imgRightArrow.setClickable(true);

            Utils.DEBUG("Sensor Value Updated");
            holder.txtTemperatureValue.setText((element.getTempSensorValue() == null || element.getTempSensorValue().isEmpty()) ? context.getString(R.string.str_temperature_label) : (element.getTempSensorValue() + " " + context.getResources().getString(R.string.str_degree)));
            holder.txtPressureValue.setText((element.getPressureSensorValue() == null || element.getPressureSensorValue().isEmpty()) ? context.getString(R.string.str_pressure_label) : element.getPressureSensorValue());
        }
    }

    private void switchToSettingsPage(View v, MotionEvent event, String address, int position) {
        try {
            //mobleAddress address1 = mobleAddress.deviceAddress(Integer.parseInt(address, 16));

            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                if (Utils.getVibrator(context) != null) {
                    Utils.getVibrator(context).vibrate(50);
                }
            } else if (MotionEvent.ACTION_UP == event.getAction()) {

                if(Utils.getProxyNode(context) == null)
                {
                    ((MainActivity) context).isCustomProxy = false;
                    ((MainActivity) context).showPopUpForProxy((MainActivity) context, "Connecting Proxy Device..", false);
                    MainActivity.mSettings.setCustomProxy(node.getAddress());
                    ((MainActivity)context).mAutoAddress = node.getAddress();
                    MainActivity.mSettings.setCustomProxy(node.getAddress());
                    Utils.moveToFragment((MainActivity) context, new ElementSettingFragment(), elements.get(position), 0);
                }
                else
                {
                    Utils.moveToFragment((MainActivity) context, new ElementSettingFragment(), elements.get(position), 0);
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtAccelerometerValue;
        private TextView txtTemperatureValue;
        private TextView txtPressureValue;
        private LinearLayout lytAccelerometer;
        private LinearLayout lytTemperature;
        private LinearLayout lytPressure;
        private LinearLayout lytAllModels;
        private ImageView imgToggle;
        private ImageView imgVersion;
        private ImageView imgLevel;
        private ImageView imgBattery;
        private ImageView imgRightArrow;
        private LinearLayout linear_Toggle;
        private LinearLayout linear_Version;
        private LinearLayout linear_Level;
        private LinearLayout lytModelSupport;
        private LinearLayout imageButtons;
        private Switch butSwitch;
        private TextView txtLevelIntensity;
        private TextView textViewTitle;
        private ImageView imageItemDevice;
        private ImageView imageSettings;
        private LinearLayout lytWarning, light_ctl_LL, lightessLL, light_hsl_LL;
        private RelativeLayout lytWarningMsg;
        private ViewGroup transitionsContainer;
        private HorizontalScrollView textScrollTitle;
        private RecyclerView sensorRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            linear_Toggle = (LinearLayout) itemView.findViewById(R.id.linear_Toggle);
            linear_Version = (LinearLayout) itemView.findViewById(R.id.linear_Version);
            linear_Level = (LinearLayout) itemView.findViewById(R.id.linear_Level);
            imageSettings = (ImageView) itemView.findViewById(R.id.imageSettings);
            imageItemDevice = (ImageView) itemView.findViewById(R.id.imageItemDevice);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            butSwitch = (Switch) itemView.findViewById(R.id.butSwitch);
            lytWarning = (LinearLayout) itemView.findViewById(R.id.lytWarning);
            //lytWarningMsg = (RelativeLayout) itemView.findViewById(R.id.lytWarningMsg);
            transitionsContainer = (ViewGroup) itemView.findViewById(R.id.lyt_Title);
            textScrollTitle = (HorizontalScrollView) transitionsContainer.findViewById(R.id.textScrollTitle);
            lytWarningMsg = (RelativeLayout) transitionsContainer.findViewById(R.id.lytWarningMsg);
            imageButtons = (LinearLayout) itemView.findViewById(R.id.imageButtons);
            lytModelSupport = (LinearLayout) itemView.findViewById(R.id.lytModelSupport);
            imgToggle = (ImageView) itemView.findViewById(R.id.imgToggle);
            imgVersion = (ImageView) itemView.findViewById(R.id.imgVersion);
            imgLevel = (ImageView) itemView.findViewById(R.id.imgLevel);
            imgBattery = (ImageView) itemView.findViewById(R.id.imgBattery);
            imgRightArrow = (ImageView) itemView.findViewById(R.id.imgRightArrow);

            lytAllModels = (LinearLayout) itemView.findViewById(R.id.lytAllModels);
            light_ctl_LL = (LinearLayout) itemView.findViewById(R.id.light_ctl_LL);
            lightessLL = (LinearLayout) itemView.findViewById(R.id.lightessLL);
            light_hsl_LL = (LinearLayout) itemView.findViewById(R.id.light_hsl_LL);
            lytAccelerometer = (LinearLayout) itemView.findViewById(R.id.lytAccelerometer);
            lytTemperature = (LinearLayout) itemView.findViewById(R.id.lytTemperature);
            lytPressure = (LinearLayout) itemView.findViewById(R.id.lytPressure);
            txtAccelerometerValue = (TextView) itemView.findViewById(R.id.txtAccelerometerValue);
            txtTemperatureValue = (TextView) itemView.findViewById(R.id.txtTemperatureValue);
            txtPressureValue = (TextView) itemView.findViewById(R.id.txtPressureValue);
            txtLevelIntensity = (TextView) itemView.findViewById(R.id.txtLevelIntensity);
            sensorRecyclerView = (RecyclerView) itemView.findViewById(R.id.sensorRecyclerView);


        }
    }

    private final GenericLevelModelClient.GenericLevelStatusCallback mLevelCallback = new GenericLevelModelClient.GenericLevelStatusCallback() {
        @Override
        public void onLevelStatus(boolean timeout,
                                  ApplicationParameters.Level level,
                                  ApplicationParameters.Level targetLevel,
                                  ApplicationParameters.Time remainingTime) {
            if (timeout) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Level Status Callback==>" + "timeout", LoggerConstants.TYPE_RECEIVE);
                UserApplication.trace("Generic Level Timeout");
            } else {
                UserApplication.trace("Generic Level status = SUCCESS ");

                UserApplication.trace("Generic Level current Level = " + level);
                UserApplication.trace("Generic Level Target Level = " + targetLevel);
                int mDimming = level.getValue();
                UserApplication.trace("Level is " + Integer.toString(level.getValue()));
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Level Status Callback==>" + "Success", LoggerConstants.TYPE_RECEIVE);
            }
        }
    };


    void showLightingPopup(final int position, String from) {

        final Dialog dialog = new Dialog(context);
        if ("CTL".equalsIgnoreCase(from)) {
            lightCTLModelClient = ((MainActivity) context).app.mConfiguration.getNetwork().getLightnessCTLModel();

            dialog.setContentView(R.layout.activity_color_picker_view_example);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            final NegativeSeekBar cseekBar1, LseekBar3, only_TseekBar2, only_deltaseekBar2;
            final NegativeSeekBar TseekBar2;
            final EditText cTV, TTV, LTV, only_TTV, only_deltaTV, cTV1, TTV1, LTV1, only_TTV1, only_deltaTV1;
            cseekBar1 = (NegativeSeekBar) dialog.findViewById(R.id.cseekBar1);
            TseekBar2 = (NegativeSeekBar) dialog.findViewById(R.id.TseekBar2);
            LseekBar3 = (NegativeSeekBar) dialog.findViewById(R.id.LseekBar3);
            only_TseekBar2 = (NegativeSeekBar) dialog.findViewById(R.id.only_TseekBar2);
            only_deltaseekBar2 = (NegativeSeekBar) dialog.findViewById(R.id.only_deltaseekBar2);
            cseekBar1.setMin(-32768);
            cseekBar1.setMax(32768);
            only_deltaseekBar2.setMin(-32768);
            only_deltaseekBar2.setMax(32768);
            TseekBar2.setMax(20000);
            TseekBar2.setMin(800);
            LseekBar3.setMax(65535);
            LseekBar3.setMin(1);
            only_TseekBar2.setMin(800);
            only_TseekBar2.setMax(20000);
            cTV = (EditText) dialog.findViewById(R.id.cTV);
            TTV = (EditText) dialog.findViewById(R.id.TTV);
            LTV = (EditText) dialog.findViewById(R.id.LTV);

            cTV1 = (EditText) dialog.findViewById(R.id.cTV1);
            TTV1 = (EditText) dialog.findViewById(R.id.TTV1);
            LTV1 = (EditText) dialog.findViewById(R.id.LTV1);


            only_deltaTV = (EditText) dialog.findViewById(R.id.only_deltaTV);

            only_TTV = (EditText) dialog.findViewById(R.id.only_TTV);


            only_deltaTV1 = (EditText) dialog.findViewById(R.id.only_deltaTV1);

            only_TTV1 = (EditText) dialog.findViewById(R.id.only_TTV1);
            Button sendcommandBTCTL = (Button) dialog.findViewById(R.id.sendcommandBTCTL);
            Button close_BT = (Button) dialog.findViewById(R.id.closeBT);

            final Button ctl_tab_BT = (Button) dialog.findViewById(R.id.ctl_tab_BT);
            final Button temp_tab_BT = (Button) dialog.findViewById(R.id.temp_tab_BT);
            final LinearLayout ctl_LL = (LinearLayout) dialog.findViewById(R.id.ctl_LL);
            final LinearLayout only_temp_LL = (LinearLayout) dialog.findViewById(R.id.only_temp_LL);
            final LinearLayout only_delta_LL = (LinearLayout) dialog.findViewById(R.id.only_delta_LL);
            cTV.setText("1");
            TTV.setText("800");
            LTV.setText("1");
            cTV1.setText("1");
            TTV1.setText("800");
            LTV1.setText("1");
            only_TTV.setText("800");
            only_deltaTV.setText("1");
            only_TTV1.setText("800");
            only_deltaTV1.setText("1");
            cseekBar1.setProgress(1);
            TseekBar2.setProgress(800);
            LseekBar3.setProgress(1);
            dialog.setCancelable(false);
            ctl_tab_BT.setBackgroundColor(context.getResources().getColor(R.color.ST_primary_blue));
            temp_tab_BT.setBackgroundColor(context.getResources().getColor(R.color.white1));
            close_BT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    is_ctl_selected = true;
                }
            });
            ctl_tab_BT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctl_tab_BT.setBackgroundColor(context.getResources().getColor(R.color.ST_primary_blue));
                    temp_tab_BT.setBackgroundColor(context.getResources().getColor(R.color.white1));
                    is_ctl_selected = true;
                    only_temp_LL.setVisibility(View.GONE);
                    ctl_LL.setVisibility(View.VISIBLE);
                    only_delta_LL.setVisibility(View.GONE);
                }
            });

            temp_tab_BT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    is_ctl_selected = false;
                    temp_tab_BT.setBackgroundColor(context.getResources().getColor(R.color.ST_primary_blue));
                    ctl_tab_BT.setBackgroundColor(context.getResources().getColor(R.color.white1));
                    only_temp_LL.setVisibility(View.VISIBLE);
                    ctl_LL.setVisibility(View.GONE);
                    only_delta_LL.setVisibility(View.VISIBLE);
                }
            });
            sendcommandBTCTL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog.dismiss();
                    try {
                        if (is_ctl_selected) {

                            ApplicationParameters.Address Address = new ApplicationParameters.Address(Integer.parseInt(node.getElements().get(position).getUnicastAddress()));

                            ApplicationParameters.Temperature temperature = new ApplicationParameters.Temperature((int) ((Double.parseDouble(TTV1.getText().toString())))
                                    /* TseekBar2.getProgress()*/);
                            ApplicationParameters.TemperatureDeltaUV temperatureDeltaUV = new ApplicationParameters.TemperatureDeltaUV((int) ((Double.parseDouble(cTV1.getText().toString()))));
                            ApplicationParameters.Lightness lightness1 = new ApplicationParameters.Lightness((int) ((Double.parseDouble(LTV1.getText().toString())))/*LseekBar3.getProgress()*/);
                            ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                            ApplicationParameters.Delay delay = new ApplicationParameters.Delay(1);
                            ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL set Light CTL Command Send==>" + Address, LoggerConstants.TYPE_SEND);
                            lightCTLModelClient.setLightCTL(Utils.isReliableEnabled(context), Address, lightness1, temperature, temperatureDeltaUV, tid, delay, Utils.isReliableEnabled(context) ? mLightCTLStatusCallback : null);
                        } else {
                            ApplicationParameters.Address Address = new ApplicationParameters.Address(Integer.parseInt(node.getElements().get(position).getUnicastAddress()));

                            ApplicationParameters.Temperature temperature = new ApplicationParameters.Temperature((int) ((Double.parseDouble(only_TTV1.getText().toString())))/*only_TseekBar2.getProgress()*/);
                            ApplicationParameters.TemperatureDeltaUV temperatureDeltaUV = new ApplicationParameters.TemperatureDeltaUV((int) ((Double.parseDouble(only_deltaTV1.getText().toString())))/*only_deltaseekBar2.getProgress()*/);
                            ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                            ApplicationParameters.Delay delay = new ApplicationParameters.Delay(Utils.getTIDValue(context));

                            UserApplication.trace("temperatureDeltaUV_1 = " + temperatureDeltaUV);
                            ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL set Light CTL Temperature Command Send==>" + Address, LoggerConstants.TYPE_SEND);
                            lightCTLModelClient.setLightCTLTemperature(Utils.isReliableEnabled(context), Address, temperature, temperatureDeltaUV, tid, delay,
                                    Utils.isReliableEnabled(context) ? mLightCTLTemperatureStatusCallback : null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            cseekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //deltauv
                    cTV1.setText("" + progress);

                    if (progress > 0) {
                        double pos = Math.abs(progress);
                        double v = pos / 32768;
                        cTV.setText("" + String.format("%.2f", v));
                    } else {
                        double pos = Math.abs(progress);
                        double val = pos / 32768;
                        cTV.setText("-" + String.format("%.2f", Math.abs(val)));
                    }


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            TseekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //temp ctl
                    TTV1.setText("" + progress);

                    TTV.setText("" + progress + " k");

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            LseekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //light ctl
                    LTV1.setText("" + progress);

                    LTV.setText("" + (progress * 100) / LIGHTING_LIGHTNESS_MAX + " %");
                    // int seekbar_val = (seekBar.getProgress()*100)/65535;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            only_TseekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //temp
                    only_TTV.setText("" + progress + " k");
                    only_TTV1.setText("" + progress);


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            only_deltaseekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //delta temp
                    // only_deltaTV.setText("" + progress);
                    only_deltaTV1.setText("" + progress);
                    if (progress > 0) {
                        double pos = Math.abs(progress);
                        double v = pos / 32768;
                        only_deltaTV.setText("" + String.format("%.2f", v));
                    } else {
                        double pos = Math.abs(progress);
                        double val = pos / 32768;
                        only_deltaTV.setText("-" + String.format("%.2f", Math.abs(val)));
                    }


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


        } else if ("HSL".equalsIgnoreCase(from)) {
            showColorWheel(position);
        } else {
            lightLightnessModelClient = ((MainActivity) context).app.mConfiguration.getNetwork().getLightnessModel();

            dialog.setContentView(R.layout.seekbar_res_file);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog.show();

            TextView headingTV = (TextView) dialog.findViewById(R.id.headingTV);
            Button closeBT = (Button) dialog.findViewById(R.id.closeBT);
            headingTV.setText("Lighting");
            SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
            seekBar.setMax(LIGHTING_LIGHTNESS_MAX);
            closeBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    // String seekbarValue = String.valueOf((seekBar.getProgress()) * 20);
                    int seekbar_val = (seekBar.getProgress() * 100) / LIGHTING_LIGHTNESS_MAX;

                    Utils.contextU = context;
                    ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(node.getElements().get(position).getUnicastAddress().toString(), 16));
                  /*  ApplicationParameters.Lightness lightness = new ApplicationParameters.Lightness(Integer.parseInt(seekbarValue));
                    ApplicationParameters.TID tid = new ApplicationParameters.TID(1);
                    ApplicationParameters.Delay delay = new ApplicationParameters.Delay(1);

                    mLightingLightnessModel. setLightnessLevel(true, elementAddress, lightness, tid, delay, mLightnessStatusCallback);

*/

                    sendLightnessCommand(elementAddress, seekBar.getProgress());
                    TextView txtIntensityValue = (TextView) dialog.findViewById(R.id.txtIntensityValue);
                    txtIntensityValue.setText(seekbar_val + " %");

                }
            });
        }

    }

    public LightCTLModelClient.LightCTLStatusCallback mLightCTLStatusCallback = new LightCTLModelClient.LightCTLStatusCallback() {

        @Override

        public void onLightCTLStatus(boolean timeout, ApplicationParameters.Lightness presentCTLLightness, ApplicationParameters.Temperature presentCTLtemperature,
                                     ApplicationParameters.Lightness targetCTLLightness, ApplicationParameters.Temperature targetCTLtemperature, ApplicationParameters.Time remainingTime) {
            if (timeout) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("CTL LightCTLStatusCallback ==> timeout");

                }

            } else {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLStatusCallback ==>" + "presentCTLLightness :" + presentCTLLightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLStatusCallback ==>" + "presentCTLtemperature :" + presentCTLtemperature, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLStatusCallback ==>" + "targetCTLLightness :" + targetCTLLightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLStatusCallback ==>" + "targetCTLtemperature :" + targetCTLtemperature, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLStatusCallback ==>" + "remainingTime :" + remainingTime, LoggerConstants.TYPE_RECEIVE);


                if (enableLogs) {
                    UserApplication.trace("CTL LightCTLStatusCallback ==>" + "Success");
                    UserApplication.trace("CTL LightCTLStatusCallback ==>" + "presentCTLLightness :" + presentCTLLightness);
                    UserApplication.trace("CTL LightCTLStatusCallback ==>" + "presentCTLtemperature :" + presentCTLtemperature);
                    UserApplication.trace("CTL LightCTLStatusCallback ==>" + "targetCTLLightness :" + targetCTLLightness);
                    UserApplication.trace("CTL LightCTLStatusCallback ==>" + "targetCTLtemperature :" + targetCTLtemperature);
                    UserApplication.trace("CTL LightCTLStatusCallback ==>" + "remainingTime :" + remainingTime);

                }
            }

        }

    };

    public LightCTLModelClient.LightCTLTemperatureStatusCallback mLightCTLTemperatureStatusCallback = new LightCTLModelClient.LightCTLTemperatureStatusCallback() {
        @Override

        public void onLightCTLTemperatureStatus(boolean timeout, ApplicationParameters.Temperature presentCTLtemperature, ApplicationParameters.TemperatureDeltaUV presentCTLDeltaUV,
                                                ApplicationParameters.Temperature targetCTLtemperature, ApplicationParameters.TemperatureDeltaUV targetCTLDeltaUV, ApplicationParameters.Time remainingTime) {
            if (timeout) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLTemperatureStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("CTL LightCTLTemperatureStatusCallback ==> timeout");

                }
            } else {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLTemperatureStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLTemperatureStatusCallback ==>" + "presentCTLtemperature :" + presentCTLtemperature, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLTemperatureStatusCallback ==>" + "presentCTLDeltaUV :" + presentCTLDeltaUV, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLTemperatureStatusCallback ==>" + "targetCTLtemperature :" + targetCTLtemperature, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLTemperatureStatusCallback ==>" + "targetCTLDeltaUV :" + targetCTLDeltaUV, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("CTL LightCTLTemperatureStatusCallback ==>" + "remainingTime :" + remainingTime, LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("CTL LightCTLTemperatureStatusCallback ==>" + "Success");
                    UserApplication.trace("CTL LightCTLTemperatureStatusCallback ==>" + "presentCTLtemperature :" + presentCTLtemperature);
                    UserApplication.trace("CTL LightCTLTemperatureStatusCallback ==>" + "presentCTLDeltaUV :" + presentCTLDeltaUV);
                    UserApplication.trace("CTL LightCTLTemperatureStatusCallback ==>" + "targetCTLtemperature :" + targetCTLtemperature);
                    UserApplication.trace("CTL LightCTLTemperatureStatusCallback ==>" + "targetCTLDeltaUV :" + targetCTLDeltaUV);
                    UserApplication.trace("CTL LightCTLTemperatureStatusCallback ==>" + "remainingTime :" + remainingTime);

                }
            }

        }
    };

    public LightHSLModelClient.LightHSLDefaultStatusCallback mLightHSLDefaultStatusCallback = new LightHSLModelClient.LightHSLDefaultStatusCallback() {
        @Override

        public void onLightHSLDefaultStatus(boolean b, ApplicationParameters.Lightness lightness, ApplicationParameters.Hue hue,
                                            ApplicationParameters.Saturation saturation) {
            if (b) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==> timeout");

                }
            } else {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>" + "lightness :" + lightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>" + "hue :" + hue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>" + "saturation :" + saturation, LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>" + "Success");
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>" + "lightness :" + lightness);
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>" + "hue :" + hue);
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>" + "saturation :" + saturation);

                }
            }

        }
    };


    public LightHSLModelClient.LightHSLStatusCallback mLightHSLStatusCallback = new LightHSLModelClient.LightHSLStatusCallback() {
        @Override

        public void onLightHSLStatus(boolean timeout, ApplicationParameters.Lightness lightness, ApplicationParameters.Hue hue,
                                     ApplicationParameters.Saturation saturation,
                                     ApplicationParameters.Time remainingTime) {
            if (timeout) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLStatusCallback ==> timeout");

                }
            } else {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>" + "lightness :" + lightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>" + "hue :" + hue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>" + "saturation :" + saturation, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>" + "remainingTime :" + remainingTime, LoggerConstants.TYPE_RECEIVE);

                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLStatusCallback ==>" + "Success");
                    UserApplication.trace("HSL LightHSLStatusCallback ==>" + "lightness :" + lightness);
                    UserApplication.trace("HSL LightHSLStatusCallback ==>" + "hue :" + hue);
                    UserApplication.trace("HSL LightHSLStatusCallback ==>" + "saturation :" + saturation);
                    UserApplication.trace("HSL LightHSLStatusCallback ==>" + "remainingTime :" + remainingTime);

                }
            }

        }
    };


    public LightHSLModelClient.LightHSLSaturationStatusCallback mLightHSLSaturationStatusCallback = new LightHSLModelClient.LightHSLSaturationStatusCallback() {
        @Override

        public void onLightHSLStaurationStatus(boolean timeout, ApplicationParameters.Saturation hslPresentSaturation,
                                               ApplicationParameters.Saturation hslTargetSaturation, ApplicationParameters.Time remainingTime) {
            if (timeout) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==> timeout");

                }
            } else {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>" + "hslPresentSaturation :" + hslPresentSaturation, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>" + "hslTargetSaturation : " + hslTargetSaturation, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>" + "remainingTime :  " + remainingTime, LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>" + "Success");
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>" + "hslPresentSaturation :" + hslPresentSaturation);
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>" + "hslTargetSaturation :" + hslTargetSaturation);
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>" + "remainingTime :" + remainingTime);

                }

            }

        }
    };

    public LightHSLModelClient.LightHSLHueStatusCallback mLightHSLHueStatusCallback = new LightHSLModelClient.LightHSLHueStatusCallback() {
        @Override

        public void onLightHSLHueStatus(boolean timeout, ApplicationParameters.Hue hslPresentHue, ApplicationParameters.Hue hslTargetHue, ApplicationParameters.Time remainingTime) {
            if (timeout) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLHueStatusCallback ==> timeout");

                }
            } else {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>" + "hslPresentHue :" + hslPresentHue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>" + "hslTargetHue :" + hslTargetHue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>" + "remainingTime :  " + remainingTime, LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("HSL LightHSLHueStatusCallback ==>" + "Success");
                    UserApplication.trace("HSL LightHSLHueStatusCallback ==>" + "hslPresentHue :" + hslPresentHue);
                    UserApplication.trace("HSL LightHSLHueStatusCallback ==>" + "hslTargetHue :" + hslTargetHue);
                    UserApplication.trace("HSL LightCTLTemperatureStatusCallback ==>" + "remainingTime :" + remainingTime);

                }
            }

        }
    };

    public final LightLightnessModelClient.LightingLightnessStatusCallback mLightnessStatusCallback = new LightLightnessModelClient.LightingLightnessStatusCallback() {
        @Override

        public void onLightnessStatus(boolean timeout, ApplicationParameters.Lightness currentLightness, ApplicationParameters.Lightness targetLightness, ApplicationParameters.Time remainingTime) {

            if (timeout) {
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("LightnessStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("LightnessStatusCallback ==> timeout");

                }
            } else {
                UserApplication.trace("Lighting Lightness status = SUCCESS ");
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("LightnessStatusCallback ==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("LightnessStatusCallback ==>" + "currentLightness :" + currentLightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("LightnessStatusCallback ==>" + "targetLightness :" + targetLightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("LightnessStatusCallback ==>" + "remainingTime :  " + remainingTime, LoggerConstants.TYPE_RECEIVE);
                if (enableLogs) {
                    UserApplication.trace("LightnessStatusCallback ==>" + "Success");
                    UserApplication.trace("LightnessStatusCallback ==>" + "currentLightness :" + currentLightness);
                    UserApplication.trace("LightnessStatusCallback ==>" + "targetLightness :" + targetLightness);
                    UserApplication.trace("LightCTLTemperatureStatusCallback ==>" + "remainingTime :" + remainingTime);

                }
            }
        }
    };


    public void sendLightnessCommand(ApplicationParameters.Address address, int value) {

        ApplicationParameters.Lightness lightness = new ApplicationParameters.Lightness(value);
        ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
        ApplicationParameters.Delay delay = new ApplicationParameters.Delay(1);

        ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Lightness data send==>" + address, LoggerConstants.TYPE_SEND);

        lightLightnessModelClient.setLightnessLevel(Utils.isReliableEnabled(context), address, lightness, tid, delay, Utils.isReliableEnabled(context) ? mLightnessStatusCallback : null);


    }

    private void showColorWheel(int position) {

        CustomDialog cdd = new CustomDialog((MainActivity) context, Integer.parseInt(node.getElements().get(position).getUnicastAddress()));
        cdd.show();

    }

    public LinearLayout layLoopForModels(final Context context, LinearLayout parent, final Element element) {

        LayoutInflater layoutInfralte = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }

        if (element.getModels() == null)
            return null;

        System.out.println("Size >>>>" + element.getModels().size());
        List views = new ArrayList();

        boolean isGeneric = false;
        boolean isLights = false;
        boolean isVendor = false;
        boolean isHealth = false;
        boolean isConfiguration = false;
        boolean isSensor = false;
        boolean isTime = false;
        final ArrayList<Model> modelsLocal = new ArrayList<>();

        try {
            for (int i = 0; i < element.getModels().size(); i++) {

                Model model1 = new Model();

                final View view = layoutInfralte.inflate(R.layout.child_one_models, null);
                LinearLayout layChildModel = (LinearLayout) view.findViewById(R.id.layChildModel);
                TextView txtModel = (TextView) view.findViewById(R.id.txtModel);
                final Model model = element.getModels().get(i);

                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layChildModel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.DEBUG("Position : " + v);
                        //ArrayList<Model> models = Utils.getModelsForCurrentModelGroup(context, elements.get(position), context.getResources().getString(R.string.MODEL_GROUP_NAME_LIGHT));

                        TextView viewById = (TextView) v.findViewById(R.id.txtModel);
                        String modelStr = viewById.getText().toString();
                        String str = "";
                        if (modelStr.equals("G")) {
                            str = context.getString(R.string.MODEL_GROUP_NAME_GENERIC);
                        } else if (modelStr.equals("V")) {
                            str = context.getString(R.string.MODEL_GROUP_NAME_VENDOR);
                        } else if (modelStr.equals("L")) {
                            str = context.getString(R.string.MODEL_GROUP_NAME_LIGHT);
                        } else if (modelStr.equals("S")) {
                            str = context.getString(R.string.MODEL_GROUP_NAME_SENSOR);
                        } else if (modelStr.equals("T")) {
                            str = context.getString(R.string.MODEL_GROUP_NAME_TIME);
                        }

                        ArrayList<Model> models = Utils.getSubModelsForSelectedGroupModel(context, model, str, element, modelsLocal);
                        Utils.showModelsDetailsPopup(context, models, str);
                    }
                });

                //if(model.getModelName().contains(context.getString(R.string.MODEL_GROUP_NAME_GENERIC)))
                if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1002_GENERIC_LEVEL))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1000_GENERIC_STATUS))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_100c_GENERIC_BATTERY))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1000_GENERIC_ONOFF))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1004_GENERIC_DEFAULT_TRANSITION_TIME))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1006_GENERIC_POWER_ONOFF))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1007_GENERIC_POWER_SETUP_ONOFF))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1001_GENERIC_ONOFF_CLIENT))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1003_GENERIC_LEVEL_CLIENT))
                ) {
                    if (!isGeneric) {
                        txtModel.setText("G");
                        views.add(view);
                        isGeneric = true;

                    }

                    if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1002_GENERIC_LEVEL))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_LEVEL));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1000_GENERIC_STATUS))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_ONOFF));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_100c_GENERIC_BATTERY))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_BATTERY));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1004_GENERIC_DEFAULT_TRANSITION_TIME))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_DEFAULT_TRANSITION_TIME));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1006_GENERIC_POWER_ONOFF))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_POWER_ON_OFF_SERVER));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1007_GENERIC_POWER_SETUP_ONOFF))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_POWER_ON_OFF_SETUP_SERVER));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1001_GENERIC_ONOFF_CLIENT))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_ON_OFF_CLIENT));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1003_GENERIC_LEVEL_CLIENT))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_GENERIC_LEVEL_CLIENT));
                    }
                } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1300_LIGHT_LIGHTNESS_MODEL))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1301_LIGHT_LIGHTNESS_SETUP_MODEL))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1303_LIGHT_CTL_SERVER))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1304_LIGHT_CTL_SETUP_SERVER))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1306_LIGHT_CTL_TEMPERATURE_SERVER))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1307_LIGHT_HSL_SERVER))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1308_LIGHT_HSL_SETUP_SERVER))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_130f_LIGHT_LC_SERVER))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1310_LIGHT_LC_SETUP_SERVER))
                ) {
                    if (!isLights) {
                        txtModel.setText("L");
                        views.add(view);
                        isLights = true;

                    }

                    if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1300_LIGHT_LIGHTNESS_MODEL))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_LIGHTNESS));

                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1301_LIGHT_LIGHTNESS_SETUP_MODEL))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_LIGHTNESS_SETUP));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1303_LIGHT_CTL_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_CTL_SERVER));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1304_LIGHT_CTL_SETUP_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_CTL_SETUP_SERVER));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1306_LIGHT_CTL_TEMPERATURE_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_CTL_TEMPERATURE_SERVER));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1307_LIGHT_HSL_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_HSL_SERVER));
                    } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1308_LIGHT_HSL_SETUP_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_HSL_SETUP_SERVER));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_130f_LIGHT_LC_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_LC_SETUP));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1310_LIGHT_LC_SETUP_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_LIGHT_LC_SETUP_SERVER));
                    }

                } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_00010030_VENDOR_MODEL))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_00300001_VENDOR_MODEL))
                        || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_10030_VENDOR_MODEL))) {
                    if (!isVendor) {
                        txtModel.setText("V");
                        views.add(view);
                        isVendor = true;

                    }


                    if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_00010030_VENDOR_MODEL))
                            || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_00300001_VENDOR_MODEL))
                            || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_10030_VENDOR_MODEL))) {
                        //model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_VENDOR_MODEL));
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_ST_VENDOR_SERVER_MODEL));
                    }
                } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_0000_CONFIGURATION_SERVER))) {
                    if (!isConfiguration) {

                    }
                }
                else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_0001_CONFIGURATION_CLIENT))) {
                    if (!isConfiguration) {

                    }
                }
                else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_0002_HEALTH_SERVER))) {
                    if (!isHealth) {

                    }
                } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1100_SENSOR_MODEL))
                        || (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1101_SENSOR_SETUP_SERVER))
                        || (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1102_SENSOR_CLIENT))
                ))) {
                    if (!isSensor) {
                        txtModel.setText("S");
                        views.add(view);
                        isSensor = true;

                    }

                    if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1100_SENSOR_MODEL))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_SENSOR_MODEL_SERVER));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1101_SENSOR_SETUP_SERVER))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_SENSOR_SERVER));
                    }else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1102_SENSOR_CLIENT))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_SENSOR_CLIENT));
                    }
                } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1200_TIME_MODEL))) {
                    if (!isTime) {
                        txtModel.setText("T");
                        views.add(view);
                        isTime = true;

                    }

                    if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1200_TIME_MODEL))) {
                        model1.setModelName(context.getString(R.string.MODEL_SUB_NAME_TIME_AND_SCENE_SERVER));
                    }
                }

                model1.setModelId(model.getModelId());
                modelsLocal.add(model1);

            }



            for (int i = 0; i < views.size(); i++) {
                parent.addView((View) views.get(i));
            }
        } catch (Exception e) {
        }


        return parent;
    }
    //Sensor Descriptor commands
    private void getSensorDescriptor() {
        SensorModelClient mSensorModelClient = ((MainActivity)context).network.getSensorModel();
        ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(elements.get(0).getUnicastAddress()));
        mSensorModelClient.getSensorDescriptor(elementAddress,null,sensorDescriptorStatusCallback);
    }


    SensorModelClient.SensorDescriptorStatusCallback sensorDescriptorStatusCallback = new SensorModelClient.SensorDescriptorStatusCallback() {

        @Override
        public void onSensorDescriptorStatus(boolean b,
                                             ArrayList<ApplicationParameters.SensorDescriptorSensorPropertyId> pid_arr,
                                             ArrayList<ApplicationParameters.SensorDescriptorSensorSamplingFunction>sam_arr,
                                             ArrayList<ApplicationParameters.SensorDescriptorMeasureMentperiod>mp_arr ,
                                             ArrayList<ApplicationParameters.SensorDescriptorSensorUpdateInterval>ui_arr,
                                             ArrayList<ApplicationParameters.SensorDescriptorSensorTolerance>pTol_arr ,
                                             ArrayList<ApplicationParameters.SensorDescriptorSensorTolerance>nTol_arr) {
            if (!b) {
                UserApplication.trace("SensorDescriptorSensorPropertyId==>" + pid_arr.size());
                UserApplication.trace("SensorDescriptorSensorSamplingFunction==>>" + sam_arr.size());
                UserApplication.trace("SensorDescriptorMeasureMentperiod==>>" + mp_arr.size());
                UserApplication.trace("SensorDescriptorSensorUpdateInterval==>>" + ui_arr.size());
                UserApplication.trace("SensorDescriptorSensorTolerance==>>" + pTol_arr.size());
                UserApplication.trace("SensorDescriptorSensorTolerance==>>" + nTol_arr.size());



                for (ApplicationParameters.SensorDescriptorSensorPropertyId pid : pid_arr) {
                    UserApplication.trace("SensorDescriptorSensorPropertyId==>>" + pid);
                }

            }else {
                UserApplication.trace("Sensors Descriptor TimeOut !!");
            }
        }
    };


    private void updateElementsUIAccordingoModel(ViewHolder holder,Element element,View view1,View view2 ,View view3,String model){
        if (hashMap_model != null && hashMap_model.size() > 0) {
            for (int i = 0; i < hashMap_model.get(element.getUnicastAddress()).size(); i++) {

                if (hashMap_model.get(element.getUnicastAddress()).get(i)
                        .getModelId().equalsIgnoreCase(model)) {
                    view1.setVisibility(View.VISIBLE);

                    if (view2 != null) {
                        view2.setVisibility(View.VISIBLE);
                    }
                    if (view3 != null) {
                        view3.setVisibility(View.VISIBLE);

                    }



                    break;
                } else {
                    view1.setVisibility(View.GONE);
                    if (view2 != null) {
                        view2.setVisibility(View.GONE);
                    }
                    if (view3 != null) {
                        view3.setVisibility(View.GONE);

                    }
                }


            }
        }

    }
}
