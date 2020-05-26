/**
 * *****************************************************************************
 *
 * @file GroupRecylerAdapter.java
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

package com.st.bluenrgmesh.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.util.Util;
import com.msi.moble.ApplicationParameters;
import com.msi.moble.GenericLevelModelClient;
import com.msi.moble.LightCTLModelClient;
import com.msi.moble.LightHSLModelClient;
import com.msi.moble.LightLightnessModelClient;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.colorpicker.CustomDialog;
import com.st.bluenrgmesh.datamap.Nucleo;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.meshdata.Group;
import com.st.bluenrgmesh.utils.AppSharedPrefs;
import com.st.bluenrgmesh.utils.NegativeSeekBar;
import com.st.bluenrgmesh.view.fragments.setting.group.GroupInformationFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.GroupSettingFragment;

import java.util.ArrayList;


public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder> {
    private static final int LIGHTING_LIGHTNESS_MAX =65535 ;
    private static final int GENERIC_LEVEL_MAX = 32767;
    private final ArrayList<Group> groups;
    private Context context;
    private Object listener;
    private Boolean toggle = false;
    private static ApplicationParameters.OnOff state = ApplicationParameters.OnOff.DISABLED;
    //static SharedPreferences pref_model_selection;

    private static boolean mDimmingState = false;
    private String typeModel;
    private GenericLevelModelClient mGenericLevelModel;
    private boolean is_ctl_selected = true;
    private LightCTLModelClient lightCTLModelClient;
    private LightLightnessModelClient lightLightnessModelClient;
    private LightHSLModelClient lightHSLModelClient;
    private int which_hsl_tab_selected=1;
    private boolean send_hsl_default_in_HSL_TAB = false;
    public GroupRecyclerAdapter(String typeModel, Context context, ArrayList<Group> groups, GroupRecyclerAdapter.IRecyclerViewHolderClicks l) {
        this.typeModel = typeModel;

        this.context = context;
        this.groups = groups;
        this.listener = l;

    }

    public void fromWhere(String typeModel) {

        this.typeModel = typeModel;
    }


    public interface IRecyclerViewHolderClicks {
        /**
         * On click recycler item.
         *
         * @param v          the v
         * @param position   the position
         * @param item       the item
         * @param isSelected the str selected
         */
        public void onClickRecyclerItem(View v, int position, String item, String mAutoAddress, boolean isSelected);
    }

    @Override
    public GroupRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_child_row_nodes, parent, false);

        GroupRecyclerAdapter.ViewHolder vh = new GroupRecyclerAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final GroupRecyclerAdapter.ViewHolder holder, final int position) {
        //Log.e("Dinesh==>","insinde groupAdapter");

        if (context.getResources().getBoolean(R.bool.bool_isElementFunctionality)) {
            holder.imageItem.setVisibility(View.VISIBLE);
            holder.seekBar.setVisibility(View.GONE);
        } else {
            // group list
            holder.seekBar.setVisibility(View.GONE);
            holder.imageItem.setVisibility(View.VISIBLE);

        }

        holder.textViewTitle.setText(groups.get(position).getName());
        holder.textViewSubtitle.setText(groups.get(position).getAddress().substring(0, 1).toUpperCase() + groups.get(position).getAddress().substring(1));

        holder.group_toggle_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean onCommand = false;
                if (Utils.getVibrator(context) != null) {
                    Utils.getVibrator(context).vibrate(50);
                }
                final CompoundButton toggleButton = (CompoundButton) buttonView.findViewById(R.id.group_toggle_switch);
                if (toggleButton.isChecked()) {
                    onCommand = true;
                }


                if (position == 0) {

                    new android.os.Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setDefaultGroupToggle_ONOFF(context, ((MainActivity)context).meshRootClass, toggleButton.isChecked());
                        }
                    });

                    allToggle(null, null, groups.get(position).getAddress(), onCommand);

                } else {
                    new android.os.Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setGroupToggle_ONOFF(context, ((MainActivity)context).meshRootClass, groups.get(position).getAddress(), toggleButton.isChecked());
                        }
                    });

                    groupToggle(null, null, groups.get(position).getAddress(), onCommand);
                }
            }
        });

        holder.imageSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switchToSettingsPage(v, event, groups.get(position).getAddress(), position);
                return true;
            }
        });


        holder.intensityLL.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.seekbar_res_file);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                dialog.show();

                SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
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

                        // String seekbarValue = String.valueOf((seekBar.getProgress())*20);
                        int seekbar_val = (seekBar.getProgress() * 100) / GENERIC_LEVEL_MAX;

                        Utils.contextU = context;

                        //ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(node.getElements().get(position).getUnicastAddress().toString(),16));
                        ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(groups.get(position).getAddress().toString(), 16));
                        UserApplication.trace("NavBar SeekBar Value Generic Model = " + (seekBar.getProgress()));

                        ApplicationParameters.Level level = new ApplicationParameters.Level((seekBar.getProgress()));

                        if (((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork() != null) {
                            mGenericLevelModel = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().getLevelModel();
                        }
                        mGenericLevelModel.setGenericLevel(Utils.isReliableEnabled(context),
                                elementAddress,
                                level,
                                new ApplicationParameters.TID(Utils.getTIDValue(context)),
                                null,
                                null,
                                Utils.isReliableEnabled(context)?mLevelCallback:null);

                        TextView txtIntensityValue = (TextView) dialog.findViewById(R.id.txtIntensityValue);
                        txtIntensityValue.setText(seekbar_val + "%");

                    }
                });
            }
        });

        holder.ctl_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLightingPopup(position, groups.get(position).getAddress(), "CTL");
            }
        });
        holder.vendor_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLightingPopup(position, groups.get(position).getAddress(), "Lightness");
            }
        });
        holder.lightessLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLightingPopup(position, groups.get(position).getAddress(), "Lightness");
            }
        });
        holder.hsl_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLightingPopup(position, groups.get(position).getAddress(), "HSL");
            }
        });

        if (typeModel == null) {
            holder.intensityLL.setVisibility(View.GONE);
            holder.vendor_model.setVisibility(View.GONE);
            holder.ctl_LL.setVisibility(View.GONE);
            holder.hsl_LL.setVisibility(View.GONE);
            holder.lightessLL.setVisibility(View.GONE);
            holder.imageSettings.setVisibility(View.GONE);

        } else {
            if (typeModel.equals(context.getString(R.string.str_genericmodel_label))) {
                holder.intensity_TV.setText("Level");
                holder.intensityLL.setVisibility(View.VISIBLE);
                holder.vendor_model.setVisibility(View.GONE);
                holder.ctl_LL.setVisibility(View.GONE);
                holder.hsl_LL.setVisibility(View.GONE);
                holder.lightessLL.setVisibility(View.GONE);
                holder.imageSettings.setVisibility(View.VISIBLE);


            } else if (typeModel.equals(context.getString(R.string.str_vendormodel_label))) {
                holder.intensity_TV.setText("Intensity");
                holder.intensityLL.setVisibility(View.VISIBLE);
                holder.vendor_model.setVisibility(View.GONE);
                holder.ctl_LL.setVisibility(View.GONE);
                holder.hsl_LL.setVisibility(View.GONE);
                holder.lightessLL.setVisibility(View.GONE);
                holder.imageSettings.setVisibility(View.VISIBLE);

            }else if (typeModel.equals(context.getString(R.string.str_lighting_model_label))) {
                holder.intensityLL.setVisibility(View.GONE);
                holder.vendor_model.setVisibility(View.GONE);
                holder.ctl_LL.setVisibility(View.VISIBLE);
                holder.hsl_LL.setVisibility(View.VISIBLE);
                holder.lightessLL.setVisibility(View.VISIBLE);
                holder.imageSettings.setVisibility(View.VISIBLE);

            } else if (typeModel.equals(context.getString(R.string.str_sensormodel_label))) {
                holder.intensityLL.setVisibility(View.GONE);
                holder.vendor_model.setVisibility(View.GONE);
                holder.ctl_LL.setVisibility(View.GONE);
                holder.hsl_LL.setVisibility(View.GONE);
                holder.lightessLL.setVisibility(View.GONE);
                holder.imageSettings.setVisibility(View.VISIBLE);

            }
        }

        if (position == 0) {
            holder.intensityLL.setVisibility(View.GONE);
            holder.vendor_model.setVisibility(View.GONE);
            holder.ctl_LL.setVisibility(View.GONE);
            holder.imageSettings.setVisibility(View.GONE);
            holder.hsl_LL.setVisibility(View.GONE);
            holder.lightessLL.setVisibility(View.GONE);
        }


        holder.txtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.moveToFragment((MainActivity)context, new GroupInformationFragment(), groups.get(position), 0);
            }
        });

    }


    private void allToggle(View v, MotionEvent event, String address, boolean isOnCommand) {

        String x = address;


        if (Utils.getProxyNode(context) == null) {
            return;
        }

        if (event != null && MotionEvent.ACTION_DOWN == event.getAction()) {
            if (Utils.getVibrator(context) != null) {
                Utils.getVibrator(context).vibrate(50);
            }
        }
        if (null == address)
            return;

        //pref_model_selection = context.getSharedPreferences("Model_Selection", MODE_PRIVATE);

        mobleNetwork network = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork();
        //SharedPreferences.Editor e = pref_model_selection.edit();

        if (Utils.isVendorModelCommand(context)) {
            if (((MainActivity) context).rel_unrel) {
                ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(Utils.mGroupReadCallback);
            }
            UserApplication.trace("NavBar All Toggle Vendor Model ");
            ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("Vendor OnOff command sent ==>"+"All Toggle", LoggerConstants.TYPE_SEND);

            network.getApplication().setRemoteData(mobleAddress.BROADCAST, Nucleo.APPLI_CMD_LED_CONTROL, 1, isOnCommand ? new byte[]{Nucleo.APPLI_CMD_LED_ON} : new byte[]{Nucleo.APPLI_CMD_LED_OFF}, ((MainActivity) context).rel_unrel);
        } else {
            ApplicationParameters.Address targetAddress = new ApplicationParameters.Address(0xFFFF);
            ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));

            UserApplication.trace("testnavBar All Toggle Generic Model ");
            state = (isOnCommand ? ApplicationParameters.OnOff.ENABLED : ApplicationParameters.OnOff.DISABLED);
            ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("Generic All OnOff command sent ==>"+targetAddress, LoggerConstants.TYPE_SEND);
            network.getOnOffModel().setGenericOnOff(((MainActivity) context).rel_unrel
                    , targetAddress,
                    state,
                    tid,
                    null,
                    null,
                    ((MainActivity) context).rel_unrel ? Utils.mOnOffCallback : null);
        }
    }


    private void groupToggle(View v, MotionEvent event, String address, boolean isOnCommand) {

        try {
            String x = address;
            //int adr = Integer.parseInt("C004", 16);
            int adr = Integer.parseInt(x, 16);
            mobleAddress addre = mobleAddress.groupAddress(adr);

            if (Utils.getProxyNode(context) == null) {
                //Utils.(new Utils.LostConnectionDialog()).mLostConnectionDialog.createDialog();
                return;
            }

            if (event != null && MotionEvent.ACTION_DOWN == event.getAction()) {
                if (Utils.getVibrator(context) != null) {
                    Utils.getVibrator(context).vibrate(50);
                }
            }
            /*  else if (MotionEvent.ACTION_UP == event.getAction()) {*/
            if (null == address)
                return;


            //pref_model_selection = context.getSharedPreferences("Model_Selection", MODE_PRIVATE);

            if (Utils.isVendorModelCommand(context)) {

                if (Utils.isReliableEnabled(context))
                    //((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(((MainActivity) context).mGroupReadCallback);
                    ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(Utils.mGroupReadCallback);

                UserApplication.trace("NavBar group toggle model vendor model selected");

                mobleNetwork network = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork();
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Vendor OnOff Command Sent to ==>" + String.valueOf(addre), LoggerConstants.TYPE_SEND);

                network.getApplication().setRemoteData(addre, Nucleo.APPLI_CMD_LED_CONTROL, 1, isOnCommand ? new byte[]{Nucleo.APPLI_CMD_LED_ON} : new byte[]{Nucleo.APPLI_CMD_LED_OFF}, Utils.isReliableEnabled(context));

            } else {

                UserApplication.trace("NavBar group toggle  generic model selected");

                state = (isOnCommand ? ApplicationParameters.OnOff.ENABLED : ApplicationParameters.OnOff.DISABLED);

                ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                ApplicationParameters.Time transitionTime = ApplicationParameters.Time.NONE;
                ApplicationParameters.Delay del = new ApplicationParameters.Delay(20);

                UserApplication app = (UserApplication) context.getApplicationContext();
                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Generic OnOff command sent to ==>" + adr, LoggerConstants.TYPE_SEND);
                ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().getOnOffModel().setGenericOnOff(Utils.isReliableEnabled(context)
                        , new ApplicationParameters.Address(adr),
                        state,
                        tid,
                        null,
                        null,
                        Utils.isReliableEnabled(context) ? Utils.mOnOffCallback : null);
            }
        }catch (Exception e){}

    }


    private void groupON(View v, MotionEvent event, String address) {

        String x = address;
        int adr = Integer.parseInt(x, 16);
        mobleAddress addre = mobleAddress.groupAddress(adr);

        if (Utils.getProxyNode(context) == null) {
            //((MainActivity) context).mLostConnectionDialog.createDialog();
            return;
        }

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (Utils.getVibrator(context) != null) {
                Utils.getVibrator(context).vibrate(50);
            }
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            if (null == address)
                return;


            //pref_model_selection = context.getSharedPreferences("Model_Selection", MODE_PRIVATE);

            if (Utils.isVendorModelCommand(context)) {

                if (((MainActivity) context).rel_unrel)
                    ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(Utils.mGroupReadCallback);
                //((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(((MainActivity) context).mGroupReadCallback);

                UserApplication.trace("model vendor model selected");

                mobleNetwork network = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork();
                network.getApplication().setRemoteData(addre, Nucleo.APPLI_CMD_LED_CONTROL, 1, new byte[]{Nucleo.APPLI_CMD_LED_ON}, ((MainActivity) context).rel_unrel);

            } else {

                UserApplication.trace("generic model selected");

                ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                ApplicationParameters.Time transitionTime = ApplicationParameters.Time.NONE;
                ApplicationParameters.Delay del = new ApplicationParameters.Delay(20);

                UserApplication app = (UserApplication) context.getApplicationContext();
                ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().getOnOffModel().setGenericOnOff(Utils.isReliableEnabled(context),
                        new ApplicationParameters.Address(adr),
                        ApplicationParameters.OnOff.ENABLED,
                        tid,
                        transitionTime,
                        del,
                        Utils.isReliableEnabled(context)?Utils.mOnOffCallback:null);
            }
        }
    }

    private void groupOFF(View v, MotionEvent event, String address) {

        String x = address;
        int adr = Integer.parseInt(x, 16);
        mobleAddress addre = mobleAddress.groupAddress(adr);

        if (Utils.getProxyNode(context) == null) {
            //((MainActivity) context).mLostConnectionDialog.createDialog();
            return;
        }

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (Utils.getVibrator(context) != null) {
                Utils.getVibrator(context).vibrate(50);
            }
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            if (null == address)
                return;

            //pref_model_selection = context.getSharedPreferences("Model_Selection", MODE_PRIVATE);

            if (Utils.isVendorModelCommand(context)) {

                if (((MainActivity) context).rel_unrel)
                    ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(Utils.mGroupReadCallback);
                //((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().advise(((MainActivity) context).mGroupReadCallback);
                UserApplication.trace("model vendor model selected");

                mobleNetwork network = ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork();
                network.getApplication().setRemoteData(addre, Nucleo.APPLI_CMD_LED_CONTROL, 1, new byte[]{Nucleo.APPLI_CMD_LED_OFF}, ((MainActivity) context).rel_unrel);

            } else {

                UserApplication.trace("generic model selected");

                ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                ApplicationParameters.Time transitionTime = ApplicationParameters.Time.NONE;
                ApplicationParameters.Delay del = new ApplicationParameters.Delay(20);

                UserApplication app = (UserApplication) context.getApplicationContext();
                ((UserApplication) ((MainActivity) context).getApplication()).mConfiguration.getNetwork().getOnOffModel().setGenericOnOff(Utils.isReliableEnabled(context)
                        , new ApplicationParameters.Address(adr), //=>new ApplicationParameters.Address(mAddress.mValue),
                        ApplicationParameters.OnOff.DISABLED,
                        tid,
                        transitionTime,
                        del,
                        Utils.isReliableEnabled(context)?Utils.mOnOffCallback : null);
            }

        }
    }

    private void switchToSettingsPage(View v, MotionEvent event, String address, int position) {

        String x = address;
        int adr = Integer.parseInt(x, 16);
        mobleAddress addre = mobleAddress.groupAddress(adr);
        short adrs = addre.mValue;


        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (Utils.getVibrator(context) != null) {
                Utils.getVibrator(context).vibrate(50);
            }
        } else if (MotionEvent.ACTION_UP == event.getAction()) {

            //via fragment
            Group grp = new Group();
            grp.setAddress(groups.get(position).getAddress());
            grp.setParentAddress("C000");
            grp.setName(groups.get(position).getName());
            Utils.moveToFragment(((MainActivity)context), new GroupSettingFragment(), grp, 0);
            //Utils.moveToFragment(((MainActivity)context), new NewGroupSettingFragment(), grp, 0);

            //via activity
            /*Intent setting = new Intent(context, AddGroupActivity.class);
            setting.putExtra("device", false);
            setting.putExtra("address", adrs);
            setting.putExtra("isAddNewGroup", "false");
            setting.putExtra("isLightsGroupSetting", "true");
            setting.putExtra("group_name", groups.get(position).getName());
            setting.putExtra("group_address", groups.get(position).getAddress());
            ((MainActivity) context).startActivityForResult(setting,
                    ((MainActivity) context).UPDATE_GROUP_DATA_RESULT_CODE);*/
        }

    }

    private final GenericLevelModelClient.GenericLevelStatusCallback mLevelCallback = new GenericLevelModelClient.GenericLevelStatusCallback() {
        @Override
        public void onLevelStatus(boolean timeout,
                                  ApplicationParameters.Level level,
                                  ApplicationParameters.Level targetLevel,
                                  ApplicationParameters.Time remainingTime) {
            if (timeout) {
                UserApplication.trace("Generic Level Timeout");
            } else {
                UserApplication.trace("Generic Level status = SUCCESS ");

                UserApplication.trace("Generic Level current Level = " + level);
                UserApplication.trace("Generic Level Target Level = " + targetLevel);
                int mDimming = level.getValue();
                UserApplication.trace("Level is " + Integer.toString(level.getValue()));
            }
        }
    };

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private ImageView txtInfo;
        private TextView textViewSubtitle, intensity_TV;
        private ImageView imageSettings;
        private RelativeLayout imageItem;
        private ImageView intensity_IV,imageSettings_ctl_IV;
        private SeekBar seekBar;
        private Switch group_toggle_switch;
        private LinearLayout intensityLL, vendor_model,ctl_LL,hsl_LL,lightessLL;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            txtInfo = (ImageView) itemView.findViewById(R.id.txtInfo);
            textViewSubtitle = (TextView) itemView.findViewById(R.id.textViewSubtitle);
            imageItem = (RelativeLayout) itemView.findViewById(R.id.imageItem);
            imageSettings = (ImageView) itemView.findViewById(R.id.imageSettings);
            seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
            group_toggle_switch = (Switch) itemView.findViewById(R.id.group_toggle_switch);
            intensity_IV = (ImageView) itemView.findViewById(R.id.intensity_IV);
            intensity_TV = (TextView) itemView.findViewById(R.id.intensity_TV);
            intensityLL = (LinearLayout) itemView.findViewById(R.id.intensityLL);
            vendor_model = (LinearLayout) itemView.findViewById(R.id.vendor_model);
            ctl_LL=(LinearLayout)itemView.findViewById(R.id.ctl_LL);
            lightessLL=(LinearLayout)itemView.findViewById(R.id.lightessLL);

            hsl_LL=(LinearLayout)itemView.findViewById(R.id.hsl_LL);


        }
    }

    void showLightingPopup(final int position, final String group_address, String from) {

        try {
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
                        //dialog.dismiss();
                        if (is_ctl_selected) {
                            ApplicationParameters.Address Address = new ApplicationParameters.Address(Integer.parseInt(group_address, 16));

                            ApplicationParameters.Temperature temperature = new ApplicationParameters.Temperature((int) ((Double.parseDouble(TTV1.getText().toString())))
                                    /* TseekBar2.getProgress()*/);
                            ApplicationParameters.TemperatureDeltaUV temperatureDeltaUV = new ApplicationParameters.TemperatureDeltaUV((int) ((Double.parseDouble(cTV1.getText().toString()))));
                            ApplicationParameters.Lightness lightness1 = new ApplicationParameters.Lightness((int) ((Double.parseDouble(LTV1.getText().toString())))/*LseekBar3.getProgress()*/);
                            ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                            ApplicationParameters.Delay delay = new ApplicationParameters.Delay(1);
                            lightCTLModelClient.setLightCTL(Utils.isReliableEnabled(context), Address, lightness1, temperature, temperatureDeltaUV, tid, delay,
                                    Utils.isReliableEnabled(context) ? mLightCTLStatusCallback : null);
                        } else {
                            ApplicationParameters.Address Address = new ApplicationParameters.Address(Integer.parseInt(group_address, 16));

                            ApplicationParameters.Temperature temperature = new ApplicationParameters.Temperature((int) ((Double.parseDouble(only_TTV1.getText().toString())))/*only_TseekBar2.getProgress()*/);
                            ApplicationParameters.TemperatureDeltaUV temperatureDeltaUV = new ApplicationParameters.TemperatureDeltaUV((int) ((Double.parseDouble(only_deltaTV1.getText().toString())))/*only_deltaseekBar2.getProgress()*/);
                            ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                            ApplicationParameters.Delay delay = new ApplicationParameters.Delay(1);
                            UserApplication.trace("temperatureDeltaUV_1 = " + temperatureDeltaUV);
                            lightCTLModelClient.setLightCTLTemperature(Utils.isReliableEnabled(context), Address, temperature, temperatureDeltaUV, tid, delay,
                                    Utils.isReliableEnabled(context) ? mLightCTLTemperatureStatusCallback : null);
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

                        LTV.setText("" + (progress * 100) / 65535 + " %");
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
                showColorWheel(group_address);

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
                seekBar.setMax(65535);
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

                        //String seekbarValue = String.valueOf((seekBar.getProgress())*20);
                        int seekbar_val = (seekBar.getProgress() * 100) / 65535;


                        Utils.contextU = context;

                        ApplicationParameters.Address groupAddress = new ApplicationParameters.Address(Integer.parseInt(group_address, 16));
                        sendLightnessCommand(groupAddress, seekBar.getProgress());
                        TextView txtIntensityValue = (TextView) dialog.findViewById(R.id.txtIntensityValue);
                        txtIntensityValue.setText(seekbar_val + "%");

                    }
                });
            }

        }catch (Exception e){}

    }

    public LightCTLModelClient.LightCTLStatusCallback mLightCTLStatusCallback = new LightCTLModelClient.LightCTLStatusCallback() {

        @Override
        public void onLightCTLStatus(boolean b, ApplicationParameters.Lightness lightness, ApplicationParameters.Temperature temperature, ApplicationParameters.Lightness lightness1, ApplicationParameters.Temperature temperature1, ApplicationParameters.Time time) {
            Log.e("CTL", "paramter ::" + lightness);

        }

    };

    public LightCTLModelClient.LightCTLTemperatureStatusCallback mLightCTLTemperatureStatusCallback = new LightCTLModelClient.LightCTLTemperatureStatusCallback() {
        @Override
        public void onLightCTLTemperatureStatus(boolean b, ApplicationParameters.Temperature temperature, ApplicationParameters.TemperatureDeltaUV temperatureDeltaUV, ApplicationParameters.Temperature temperature1, ApplicationParameters.TemperatureDeltaUV temperatureDeltaUV1, ApplicationParameters.Time time) {
            Log.e("CTL Temp", "paramter ::" + temperature);

        }
    };

    public final LightLightnessModelClient.LightingLightnessStatusCallback mLightnessStatusCallback = new LightLightnessModelClient.LightingLightnessStatusCallback() {
        @Override
        public void onLightnessStatus(boolean timeout, ApplicationParameters.Lightness lightness, ApplicationParameters.Lightness lightness1, ApplicationParameters.Time time) {

            if (timeout) {
                UserApplication.trace("Lighting Lightness Timeout");
            } else {
                UserApplication.trace("Lighting Lightness status = SUCCESS ");
            }
        }
    };


    public void sendLightnessCommand(ApplicationParameters.Address address, int value) {

        ApplicationParameters.Lightness lightness = new ApplicationParameters.Lightness(value);
        ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
        ApplicationParameters.Delay delay = new ApplicationParameters.Delay(1);


        lightLightnessModelClient.setLightnessLevel(Utils.isReliableEnabled(context), address, lightness, tid, delay,Utils.isReliableEnabled(context)? mLightnessStatusCallback:null);


    }



    public  LightHSLModelClient.LightHSLDefaultStatusCallback mLightHSLDefaultStatusCallback = new LightHSLModelClient.LightHSLDefaultStatusCallback() {
        @Override

        public void onLightHSLDefaultStatus(boolean b, ApplicationParameters.Lightness lightness, ApplicationParameters.Hue hue,
                                            ApplicationParameters.Saturation saturation) {
            if(b){
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);

                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==> timeout");


            }else
            {
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>"+"Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>"+"lightness :" + lightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>"+"hue :" + hue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLDefaultStatusCallback ==>"+"saturation :" + saturation, LoggerConstants.TYPE_RECEIVE);

                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>"+"Success");
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>"+"lightness :" + lightness);
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>"+"hue :" + hue);
                    UserApplication.trace("HSL LightHSLDefaultStatusCallback ==>"+"saturation :" + saturation);


            }

        }
    };


    public  LightHSLModelClient.LightHSLStatusCallback mLightHSLStatusCallback = new LightHSLModelClient.LightHSLStatusCallback() {
        @Override

        public void onLightHSLStatus(boolean timeout, ApplicationParameters.Lightness lightness, ApplicationParameters.Hue hue,
                                     ApplicationParameters.Saturation saturation,
                                     ApplicationParameters.Time remainingTime) {
            if(timeout){
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);
                    UserApplication.trace("HSL LightHSLStatusCallback ==> timeout");

            }else
            {
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>"+"Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>"+"lightness :" + lightness, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>"+"hue :" + hue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>"+"saturation :" + saturation, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLStatusCallback ==>"+"remainingTime :" + remainingTime, LoggerConstants.TYPE_RECEIVE);


                    UserApplication.trace("HSL LightHSLStatusCallback ==>"+"Success");
                    UserApplication.trace("HSL LightHSLStatusCallback ==>"+"lightness :" + lightness);
                    UserApplication.trace("HSL LightHSLStatusCallback ==>"+"hue :" + hue);
                    UserApplication.trace("HSL LightHSLStatusCallback ==>"+"saturation :" + saturation);
                    UserApplication.trace("HSL LightHSLStatusCallback ==>"+"remainingTime :" + remainingTime);


            }

        }
    };


    public  LightHSLModelClient.LightHSLSaturationStatusCallback mLightHSLSaturationStatusCallback = new LightHSLModelClient.LightHSLSaturationStatusCallback() {
        @Override

        public void onLightHSLStaurationStatus(boolean timeout, ApplicationParameters.Saturation hslPresentSaturation,
                                               ApplicationParameters.Saturation hslTargetSaturation, ApplicationParameters.Time remainingTime) {
            if(timeout){
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);

                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==> timeout");


            }else
            {
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>"+"Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>"+"hslPresentSaturation :" + hslPresentSaturation, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>"+"hslTargetSaturation : "  + hslTargetSaturation, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLSaturationStatusCallback ==>"+"remainingTime :  " + remainingTime, LoggerConstants.TYPE_RECEIVE);

                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>"+"Success");
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>"+"hslPresentSaturation :" + hslPresentSaturation);
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>"+"hslTargetSaturation :" + hslTargetSaturation);
                    UserApplication.trace("HSL LightHSLSaturationStatusCallback ==>"+"remainingTime :" + remainingTime);



            }

        }
    };

    public LightHSLModelClient.LightHSLHueStatusCallback mLightHSLHueStatusCallback= new LightHSLModelClient.LightHSLHueStatusCallback() {
        @Override

        public void onLightHSLHueStatus(boolean timeout, ApplicationParameters.Hue hslPresentHue, ApplicationParameters.Hue hslTargetHue, ApplicationParameters.Time remainingTime) {
            if(timeout){
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback timeout==>", LoggerConstants.TYPE_RECEIVE);

                    UserApplication.trace("HSL LightHSLHueStatusCallback ==> timeout");


            }else
            {
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>"+"Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>"+"hslPresentHue :" + hslPresentHue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>"+"hslTargetHue :" + hslTargetHue, LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)context).mUserDataRepository.getNewDataFromRemote("HSL LightHSLHueStatusCallback ==>"+"remainingTime :  " + remainingTime, LoggerConstants.TYPE_RECEIVE);

                    UserApplication.trace("HSL LightHSLHueStatusCallback ==>"+"Success");
                    UserApplication.trace("HSL LightHSLHueStatusCallback ==>"+"hslPresentHue :" + hslPresentHue);
                    UserApplication.trace("HSL LightHSLHueStatusCallback ==>"+"hslTargetHue :" + hslTargetHue);
                    UserApplication.trace("HSL LightCTLTemperatureStatusCallback ==>"+"remainingTime :" + remainingTime);


            }

        }
    };

    private void showColorWheel(String groupaddress) {

        CustomDialog cdd=new CustomDialog((MainActivity)context ,Integer.parseInt(groupaddress,16));
        cdd.show();

    }
}
