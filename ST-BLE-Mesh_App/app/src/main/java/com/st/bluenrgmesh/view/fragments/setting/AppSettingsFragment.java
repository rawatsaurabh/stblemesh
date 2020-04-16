/**
 * *****************************************************************************
 *
 * @file AppSettingsFragment.java
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

package com.st.bluenrgmesh.view.fragments.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.DeviceInfoFragment;
import com.st.bluenrgmesh.view.fragments.setting.managekey.AddAppKeyFragment;
import com.st.bluenrgmesh.view.fragments.setting.managekey.KeyManagementFragment;
import com.st.bluenrgmesh.voice_assist.VoiceAssistAcivity;
//import com.st.bluenrgmesh.voicereceiver.RecyclerViewHorizontalListAdapter;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.st.bluenrgmesh.MainActivity.network;


public class AppSettingsFragment extends BaseFragment {

    public AppDialogLoader loader;
    //public boolean rel_unrel = false;
    private EditText network_name,group_address;
    private EditText provisioner_name;
    public MeshRootClass meshRootClass;
    //SharedPreferences pref_model_selection;
    //SharedPreferences.Editor editor_model_selection;
    private View view;
    private Button butChangeName;
    private String net_name;
    private String name = android.os.Build.MODEL;
    private String prov_name;
    private TextView txtKeyManagement;
    private EditText edtVoiceTextInputOFF;
    private EditText edtVoiceTextInput;
    private ImageView imgSaveTextVoice;
    private ImageView imgSaveTextVoiceOFF;
//private RecyclerView onvaluesRecyclerView ;

    private File MainLogDirectory;
    private File LogDirectory;
    private File logFileName = null;
    private Boolean fileloggingEnable = true;
    public Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_changename, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.contextSettingFrag = getContext();
        Utils.updateActionBarForFeatures(getActivity(), new AppSettingsFragment().getClass().getName());
        //updateJsonData();
        initUi();
        saveVoiceTexts();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    private void saveVoiceTexts() {
        if(Utils.getSpellingOnForVoice(getActivity()) == null )
        {
            Utils.setSpellingOnForVoice(getActivity(), "on");
            Utils. setSpellingOnForVoice(getActivity(), "al");
            Utils. setSpellingOnForVoice(getActivity(), "aa");
            Utils. setSpellingOnForVoice(getActivity(), "Aaun");
            Utils. setSpellingOnForVoice(getActivity(), "uu");
            Utils. setSpellingOnForVoice(getActivity(), "own");
            Utils. setSpellingOnForVoice(getActivity(), "Light on");
            Utils. setSpellingOnForVoice(getActivity(), "Luci accese");
            Utils.setSpellingOnForVoice(getActivity(), "Luci accesa");
            Utils.setSpellingOnForVoice(getActivity(), "Accendi la luce");
            Utils.setSpellingOnForVoice(getActivity(), "Accendi");
            Utils. setSpellingOnForVoice(getActivity(), "Accende");
            Utils. setSpellingOnForVoice(getActivity(), "Accendere");
        }

        if(Utils.getSpellingOFFForVoice(getActivity()) == null )
        {
            Utils.setSpellingOFFForVoice(getActivity(), "off");
            Utils. setSpellingOFFForVoice(getActivity(), "Luci spente");
            Utils. setSpellingOFFForVoice(getActivity(), "Luce spenta");
            Utils. setSpellingOFFForVoice(getActivity(), "Spegni la luce");
            Utils. setSpellingOFFForVoice(getActivity(), "Spegni");
            Utils. setSpellingOFFForVoice(getActivity(), "Spegne");
            Utils. setSpellingOFFForVoice(getActivity(), "Spegnere");
        }
    }
    private Button voice_AssistBT;
    private CheckBox enableLogs_chkbox;
    private Switch switchHelperGuide;
    private Switch vendor_generic_switch;
    private Switch reliable_switch;
    private TextView lytDeviceInfo;

    private void initUi() {

        switchHelperGuide = view.findViewById(R.id.switchHelperGuide);
        switchHelperGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utils.isShowIntroScreen(getActivity()))
                {
                    Utils.setShowIntroScreen(getActivity(), true);
                }
                else {
                    Utils.setShowIntroScreen(getActivity(), false);
                }
            }
        });

        if(Utils.isShowIntroScreen(getActivity()))
        {
            switchHelperGuide.setChecked(true);
        }

        enableLogs_chkbox = view.findViewById(R.id.enableLogs_chkbox);
        enableLogs_chkbox.setChecked(Utils.getEnableLogs(getActivity()));

        enableLogs_chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.setEnableLogs(getActivity(),b);
                InitLog(b);
            }
        });
        voice_AssistBT = view.findViewById(R.id.voice_AssistBT);
        voice_AssistBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VoiceAssistAcivity.class);
                startActivity(i);
            }
        });
        net_name =  Utils.getNetworkName(getActivity());
        prov_name =  Utils.getProvisionerName(getActivity());
        if(meshRootClass != null){
            for (int i = 0; i < meshRootClass.getProvisioners().size(); i++) {
                if (meshRootClass.getProvisioners().get(i).getUUID().equals(Utils.getProvisionerUUID(getActivity()))) {
                    net_name = meshRootClass.getProvisioners().get(i).getProvisionerName();
                }
            }
        }

        txtKeyManagement = (TextView) view.findViewById(R.id.txtKeyManagement);
        txtKeyManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utils.moveToFragment(getActivity(), new AddAppKeyFragment(), null, 0);
                Utils.moveToFragment(getActivity(), new KeyManagementFragment(), null, 0);
            }
        });

        network_name = (EditText) view.findViewById(R.id.network_name);
        group_address = (EditText) view.findViewById(R.id.group_address);
        group_address.setText(  Utils.getGroupAddressForVoice(getActivity()));
        /*pref_model_selection = getActivity().getApplicationContext().getSharedPreferences("Model_Selection", getActivity().MODE_PRIVATE);
        if(pref_model_selection != null){
            if(pref_model_selection.getString("Network_Name",null)!=null){
                net_name = pref_model_selection.getString("Network_Name",null);
            }
            if(pref_model_selection.getString("Provisioner_Name",null) != null){
                name = pref_model_selection.getString("Provisioner_Name",null);
            }
        }*/


        if(net_name != null){
            network_name.setText(net_name);
        }else{
            network_name.setText("Default_Mesh");
        }
        network_name.setSelection(network_name.getText().length());

        provisioner_name = (EditText) view.findViewById(R.id.provisioner_name);
        if(meshRootClass != null){
            provisioner_name.setText(meshRootClass.getMeshName());
        }else{
            provisioner_name.setText(name);
        }
        provisioner_name.setSelection(provisioner_name.getText().length());

        network_name = (EditText) view.findViewById(R.id.network_name);
        provisioner_name = (EditText) view.findViewById(R.id.provisioner_name);

        butChangeName = (Button) view.findViewById(R.id.butChangeName);
        butChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*pref_model_selection = getActivity().getApplicationContext().getSharedPreferences("Model_Selection", getActivity().MODE_PRIVATE);
                editor_model_selection = pref_model_selection.edit();
                String nameee = String.valueOf(network_name.getText());
                editor_model_selection.putString("Network_Name",nameee);

                String prov_namee = String.valueOf(provisioner_name.getText());
                editor_model_selection.putString("Provisioner_Name",prov_namee);
                editor_model_selection.commit();*/
                try {
                    meshRootClass = ParseManager.getInstance().fromJSON(
                            new JSONObject(Utils.getBLEMeshDataFromLocal(getActivity())), MeshRootClass.class);
                    Utils.DEBUG(">> Json Data : " + Utils.getBLEMeshDataFromLocal(getActivity()));


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(meshRootClass != null) {
                    for (int i = 0; i < meshRootClass.getProvisioners().size(); i++) {
                        if (meshRootClass.getProvisioners().get(i).getUUID().equals(Utils.getProvisionerUUID(getActivity()))) {
                            meshRootClass.getProvisioners().get(i).setProvisionerName(net_name);
                        }
                    }
                    meshRootClass.setMeshName(name);
                }

                Utils.setBLEMeshDataToLocal(getActivity(), ParseManager.getInstance().toJSON(meshRootClass));
                Utils.setGroupAddressForVoice(getActivity(),group_address.getText().toString());
                Toast.makeText(getActivity(),"Update Success!!",Toast.LENGTH_SHORT).show();
                network_name.clearFocus();
                provisioner_name.clearFocus();
                group_address.clearFocus();
            }
        });


        vendor_generic_switch = (Switch) view.findViewById(R.id.vendor_generic_switch);
        if (Utils.isVendorModelCommand(getContext())) {
            vendor_generic_switch.setChecked(true);
        } else {
            vendor_generic_switch.setChecked(false);
        }

        vendor_generic_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean vendorModelSelection) {

                Utils.setVendorModelCommand(getContext(), vendorModelSelection);
            }
        });

        reliable_switch = (Switch) view.findViewById(R.id.reliable_switch);
        if (Utils.isReliableEnabled(getContext())) {
            reliable_switch.setChecked(true);
        } else {
            reliable_switch.setChecked(false);
        }

        reliable_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //rel_unrel = b;
                ((MainActivity) Objects.requireNonNull(getContext())).rel_unrel=b;
                Utils.setReliable(getContext(), b);
            }
        });

        edtVoiceTextInput = view.findViewById(R.id.edtVoiceTextInput);
        imgSaveTextVoice = (ImageView)view.findViewById(R.id.imgSaveTextVoice);
        imgSaveTextVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtVoiceTextInput.length()>0)
                {
                    String s = edtVoiceTextInput.getText().toString();
                    Utils.setSpellingOnForVoice(getActivity(), s);
                    edtVoiceTextInput.setText("");
                    edtVoiceTextInput.setHint("Saved in memory");
                    edtVoiceTextInput.clearFocus();
                }
                else
                {
                    edtVoiceTextInput.setError("Enter characters matched with voice for phrase Light ON.");
                }


            }
        });

        edtVoiceTextInputOFF = view.findViewById(R.id.edtVoiceTextInputOFF);
        imgSaveTextVoiceOFF = (ImageView)view.findViewById(R.id.imgSaveTextVoiceOFF);
        imgSaveTextVoiceOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtVoiceTextInputOFF.length()>0)
                {
                    String s = edtVoiceTextInputOFF.getText().toString();
                    Utils.setSpellingOFFForVoice(getActivity(), s);
                    edtVoiceTextInputOFF.setText("");
                    edtVoiceTextInputOFF.setHint("Saved in memory");
                    edtVoiceTextInput.clearFocus();
                }
                else
                {
                    edtVoiceTextInputOFF.setError("Enter characters matched with voice for phrase Light OFF.");
                }


            }
        });

        lytDeviceInfo = view.findViewById(R.id.phoneInfo);
        lytDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (vibrator != null) vibrator.vibrate(40);
                Utils.moveToFragment(getActivity(), new DeviceInfoFragment(), null, 0);

            }
        });

/*
    onvaluesRecyclerView = view.findViewById(R.id.onvaluesRecyclerView);
 groceryRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.HORIZONTAL));
    groceryAdapter = new RecyclerViewHorizontalListAdapter(groceryList, getApplicationContext());
    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        groceryRecyclerView.setLayoutManager(horizontalLayoutManager);
        groceryRecyclerView.setAdapter(groceryAdapter);
    populategroceryList();*/
    }

/*    private void populategroceryList(){
        Grocery potato = new Grocery("Potato", R.drawable.potato);
        Grocery onion = new Grocery("Onion", R.drawable.onion);
        Grocery cabbage = new Grocery("Cabbage", R.drawable.cabbage);
        Grocery cauliflower = new Grocery("Cauliflower", R.drawable.cauliflower);
        groceryList.add(potato);
        groceryList.add(onion);
        groceryList.add(cabbage);
        groceryList.add(cauliflower);
        groceryAdapter.notifyDataSetChanged();
    }*/


    private void InitLog(boolean islogEnabled) {

        LogDirectory = new File(Environment.getExternalStorageDirectory(), "DebugLogs");
        if (!LogDirectory.exists()) {
            if (!LogDirectory.mkdirs())
                UserApplication.trace("Failed to create Logs Directory");
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        logFileName = new File(LogDirectory + "/logs_" + timeStamp);
        if (islogEnabled) {
            MainActivity.mSettings.setLogFileName(logFileName);
        } else {
            MainActivity.mSettings.setLogFileName(null);

        }


    }
}




