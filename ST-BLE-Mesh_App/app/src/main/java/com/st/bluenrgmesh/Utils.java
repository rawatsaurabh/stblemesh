/**
 * *****************************************************************************
 *
 * @file Utils.java
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

package com.st.bluenrgmesh;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.msi.moble.ApplicationParameters;
import com.msi.moble.Capabilities;
import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.GenericOnOffModelClient;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;
import com.msi.moble.mobleSettings;
import com.st.bluenrgmesh.adapter.RowAdapter;
import com.st.bluenrgmesh.adapter.elementsettings.ElementsRecyclerAdapter;
import com.st.bluenrgmesh.adapter.HealthStatusPopupAdapter;
import com.st.bluenrgmesh.adapter.ModelsCustomAdapter;
import com.st.bluenrgmesh.adapter.UnprovisionedRecyclerAdapter;
import com.st.bluenrgmesh.datamap.Nucleo;
import com.st.bluenrgmesh.fota.CallbackToFragment;
import com.st.bluenrgmesh.fota.OTAModeFragment;
import com.st.bluenrgmesh.logger.LoggerFragment;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.DataNodeProvisioned;
import com.st.bluenrgmesh.models.capabilities.InformationOOB;
import com.st.bluenrgmesh.models.capabilities.InputActions;
import com.st.bluenrgmesh.models.capabilities.OutputActions;
import com.st.bluenrgmesh.models.compositiondata.ModelsData;
import com.st.bluenrgmesh.models.clouddata.LoginData;
import com.st.bluenrgmesh.models.compositiondata.ModelsDataByElement;
import com.st.bluenrgmesh.models.meshdata.AllocatedGroupRange;
import com.st.bluenrgmesh.models.meshdata.AllocatedUnicastRange;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Group;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.NetKey;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.models.meshdata.Provisioner;
import com.st.bluenrgmesh.models.meshdata.Publish;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppSharedPrefs;
import com.st.bluenrgmesh.view.fragments.other.About;
import com.st.bluenrgmesh.view.fragments.other.LicenceAgreementFragment;
import com.st.bluenrgmesh.view.fragments.other.appintro.IntroFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.DeviceInfoFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.SoftwareComponents;
import com.st.bluenrgmesh.view.fragments.setting.AppSettingsFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.CapabilitySelectionFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.LoadConfigFeaturesFragment;
import com.st.bluenrgmesh.view.fragments.other.SplashScreenFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.ModelMenuFragment;
import com.st.bluenrgmesh.view.fragments.base.BFragment;
import com.st.bluenrgmesh.view.fragments.cloud.SyncAndInviteUserFragment;
import com.st.bluenrgmesh.view.fragments.cloud.JoinAndRegisterNetworkFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.ChangePasswordFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.LoginDetailsFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.RecoverPasswordFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.SignUpFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.heartbeat.HeartBeatInfoFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.sensor.SensorConfigFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.GroupInformationFragment;
import com.st.bluenrgmesh.view.fragments.setting.managekey.AddAppKeyFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.AddGroupFragment;
import com.st.bluenrgmesh.view.fragments.setting.managekey.KeyManagementFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;
import com.st.bluenrgmesh.view.fragments.setting.ExchangeConfigFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.health.HealthConfigFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.heartbeat.HeartBeatConfigFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.GroupSettingFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.NodeFeaturesFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.NodeInfoFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.NodeSettingFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.MainViewPagerFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ModelTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.UnprovisionedFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;
import static com.msi.moble.ApplicationParameters.Status.NOT_A_SUBSCRIBE_MODEL;
import static com.st.bluenrgmesh.MainActivity.network;


public class Utils {

    public static Configuration configuration;
    public static Context contextU;
    public static Context contextMainActivity;
    public static Context contextSettingFrag;
    public static boolean isPublicationStart;
    private static boolean toggleON = true;
    private static ApplicationParameters.OnOff state = ApplicationParameters.OnOff.ENABLED;
    private static boolean mDimmingState = false;
    //static SharedPreferences pref_model_selection;
    public static String BROADCAST_MESSAGE = "broadcast_message";
    private static ElementsRecyclerAdapter.IRecyclerViewHolderClicks listener;
    private static int element_position = -1;
    private static int TID_MAX = 255;

    /*
     * All Callbacks here
     *
     * */

    public static final ConfigurationModelClient.ConfigModelPublicationStatusCallback mPublicationListenerForGroupSettings = new ConfigurationModelClient.ConfigModelPublicationStatusCallback() {

        @Override
        public void onModelPublicationStatus(boolean b, ApplicationParameters.Status status, ApplicationParameters.Address address, ApplicationParameters.Address address1, ApplicationParameters.KeyIndex keyIndex, ApplicationParameters.TTL ttl, ApplicationParameters.Time time, ApplicationParameters.GenericModelID genericModelID) {
            if (status == ApplicationParameters.Status.SUCCESS) {
                Log.i("publicationStatus", "PUBLISH_SUCCESS_GROUP");
                //((AddGroupActivity) contextU).updateUi_PublicationForGroup(status, address);
                //((MainActivity)contextMainActivity).fragmentCommunication(new GroupTabFragment().getClass().getName(),String.valueOf(address),((MainActivity)contextMainActivity).PUBLICATION_CASE, null, false);

            } else {
                Log.i("publicationStatus", "PUBLISH_FAIL_GROUP");

            }
            ((MainActivity) contextMainActivity).fragmentCommunication(new GroupSettingFragment().getClass().getName(), String.valueOf(address), ((MainActivity) contextMainActivity).PUBLICATION_CASE, null, false, status);

        }
    };

    public static final defaultAppCallback mGroupReadCallback = new defaultAppCallback() {
        @Override
        public void onResponse(mobleAddress peer, Object cookies, byte status, byte[] data) {
            Log.i("OnResponseStatus", status + "");
            if (status == STATUS_SUCCESS) {
                if (listener != null && Utils.isReliableEnabled(contextMainActivity)) {
                    listener.onElementToggle(String.valueOf(peer.mValue), element_position, status);
                }
                Log.i("Response: From", String.valueOf(peer));
                Log.i("Response: cookies", String.valueOf(cookies));
                Log.i("Response: status", String.valueOf(status));
                Log.i("Response: Data", Utils.array2string(data));


                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Response: From ==>" + String.valueOf(peer), LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Response: status ==> SUCCESS ", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Response: data ==>" + Utils.array2string(data), LoggerConstants.TYPE_RECEIVE);


            } else if (status == STATUS_FAIL_TIMEOUT) {
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Response: status ==> Timeout ", LoggerConstants.TYPE_RECEIVE);

                /*int error_type = (int)cookies;
                if(error_type == STATUS_FAIL_COMMAND)
                {
                    //invalid command send
                }
                else  if(error_type == STATUS_FAIL_DEVICE)
                {
                    //device not found or device error
                }
                else*/
                {
                    //connection timeout
                    if (listener != null && Utils.isVendorModelCommand(contextMainActivity)) {
                        if (((MainActivity) contextMainActivity).rel_unrel) {
                            listener.onElementToggle(String.valueOf(peer.mValue), element_position, status);
                        }
                    }
                    /* if(listener!=null  && ((MainActivity)contextMainActivity).rel_unrel) {
                    listener.onElementToggle(String.valueOf(peer.mValue), element_position, status);
                    }*/
                    Log.i("Status Failed", String.valueOf(peer));
                }
            }
        }
    };

    public static final defaultAppCallback mLibraryVersionCallback = new defaultAppCallback() {
        @Override
        public void onResponse(mobleAddress peer, Object cookies, byte status, byte[] data) {
            Log.i("OnResponseStatus", status + "");
            if (status == STATUS_SUCCESS) {

                if (listener != null && Utils.isVendorModelCommand(contextMainActivity)) {
                    if (Utils.isReliableEnabled(contextMainActivity)) {
                        listener.onElementToggle(String.valueOf(peer.mValue), element_position, status);
                    }
                }
                /*if(listener!=null && ((MainActivity)contextMainActivity).rel_unrel) {
                    listener.onElementToggle(String.valueOf(peer.mValue), element_position, status);
                }*/
                Log.i("Response: From", String.valueOf(peer));
                Log.i("Response: status", String.valueOf(status));
                Log.i("Response: Data", Utils.array2string(data));

            } else {
                if (listener != null && Utils.isVendorModelCommand(contextMainActivity)) {
                    if (Utils.isReliableEnabled(contextMainActivity)) {
                        listener.onElementToggle(String.valueOf(peer.mValue), element_position, status);
                    }
                }
               /* if(listener!=null  && ((MainActivity)contextMainActivity).rel_unrel) {
                    listener.onElementToggle(String.valueOf(peer.mValue), element_position, status);
                }*/
                Log.i("Status Failed", String.valueOf(peer));
            }

            final Dialog dialog = new Dialog((MainActivity) contextU);
            dialog.setContentView(R.layout.version);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView libversion = (TextView) dialog.findViewById(R.id.txtVersionValue);
            try {
                String meshLibVersion = Byte.toString(data[1]) + Byte.toString(data[2]) + "." + Byte.toString(data[3]) + Byte.toString(data[4])
                        + "." + Byte.toString(data[5]) + Byte.toString(data[6]) + Byte.toString(data[7]);
                libversion.setText(meshLibVersion);
            } catch (Exception e) {
            }

            dialog.show();
        }
    };

    /*
     * This(mSubscriptionListener) callback is used in following cases :
     * 1) removeDeviceFromGroup (Not used in current versions)
     * 2) removeElementFromGroup
     * 3) addElementToGroup
     * 4) addDeviceToGroup (Not used in current versions)
     * Note : Used in group settings
     * */
    private static final ConfigurationModelClient.ConfigModelSubscriptionStatusCallback mSubscriptionListener = new ConfigurationModelClient.ConfigModelSubscriptionStatusCallback() {
        @Override
        public void onModelSubscriptionStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.Address address, ApplicationParameters.Address subAddress, ApplicationParameters.GenericModelID model) {

            if (timeout) {
                Utils.DEBUG(">> Subscription Failed >>>>>>>>>>>> ");
                Utils.showToast(contextMainActivity, "Timeout. Try Again.");
            } else {
                Utils.DEBUG(">> Subscription Done >>>>>>>>>>>> ");
                if (status == NOT_A_SUBSCRIBE_MODEL) {
                    Utils.showToast(((MainActivity) contextMainActivity), "NOT_A_SUBSCRIBE_MODEL");
                } else {
                    Utils.DEBUG(">>Subscription Status >>>>>>>>>>>> : " + status + " address : " + address + " subaddrss : " + subAddress);
                    //((AddGroupActivity) contextU).updateUi_SubListElementsForGroup(status);
                    if (Utils.getSettingType((MainActivity) contextMainActivity).equals(contextMainActivity.getString(R.string.str_element_setting))) {
                        ((MainActivity) contextMainActivity).fragmentCommunication(new ElementSettingFragment().getClass().getName(), String.valueOf(address), contextMainActivity.getResources().getInteger(R.integer.ELEMENT_SUBSCRIPTION_CASE), null, false, status);
                    } else if (Utils.getSettingType((MainActivity) contextMainActivity).equals(contextMainActivity.getString(R.string.add_group))) {
                        ((MainActivity) contextMainActivity).fragmentCommunication(new AddGroupFragment().getClass().getName(), String.valueOf(address), ((MainActivity) contextMainActivity).SUBSCRIPTION_CASE, null, false, status);
                    } else {
                        ((MainActivity) contextMainActivity).fragmentCommunication(new GroupSettingFragment().getClass().getName(), String.valueOf(address), ((MainActivity) contextMainActivity).SUBSCRIPTION_CASE, null, false, status);
                    }
                }
            }


        }
    };

    private static final ConfigurationModelClient.ConfigModelPublicationStatusCallback mPublicationListener = new ConfigurationModelClient.ConfigModelPublicationStatusCallback() {

        @Override
        public void onModelPublicationStatus(boolean b, final ApplicationParameters.Status status, final ApplicationParameters.Address address, ApplicationParameters.Address address1, ApplicationParameters.KeyIndex keyIndex, ApplicationParameters.TTL ttl, ApplicationParameters.Time time, ApplicationParameters.GenericModelID genericModelID) {
            if (status == ApplicationParameters.Status.SUCCESS) {
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Publication Set Status For Element==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                Log.i("publicationStatus", "PUBLISH_SUCCESS_GROUP");
                if (isPublicationStart) {
                    isPublicationStart = false;
                }
            } else {
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Publication Set Status For Element==>" + "Timeout", LoggerConstants.TYPE_RECEIVE);
                Log.i("publicationStatus", "PUBLISH_FAIL_GROUP");
            }

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) contextMainActivity).fragmentCommunication(new ElementSettingFragment().getClass().getName(), String.valueOf(address), ((MainActivity) contextMainActivity).getResources().getInteger(R.integer.ELEMENT_PUBLICATION_CASE), null, false, status);
                }
            });
        }
    };

    public static final GenericOnOffModelClient.GenericOnOffStatusCallback mOnOffCallback = new GenericOnOffModelClient.GenericOnOffStatusCallback() {
        @Override
        public void onOnOffStatus(boolean timeout,
                                  ApplicationParameters.OnOff state,
                                  ApplicationParameters.OnOff targetState,
                                  ApplicationParameters.Time remainingTime, ApplicationParameters.Address nodeAddress) {
            if (timeout) {
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Generic OnOff command Status==>" + "timeout", LoggerConstants.TYPE_RECEIVE);
                UserApplication.trace("Generic OnOff Timeout");
            } else {
                UserApplication.trace("Generic OnOff nodeAddress = SUCCESS " + String.valueOf(nodeAddress));
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Generic OnOff command Status==>" + "Success", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Generic OnOff command Source Address ==>" + nodeAddress, LoggerConstants.TYPE_RECEIVE);
                mDimmingState = (state == ApplicationParameters.OnOff.ENABLED);
                UserApplication.trace("Dimming is " + (mDimmingState ? "On" : "Off"));
            }
        }
    };


    public static void turnGPSOn(final Context context) {
        LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder
                    .setIcon(context.getResources().getDrawable(R.drawable.ic_settings_bluetooth_black_24dp))
                    .setMessage(context.getString(R.string.str_gps_enable_label))
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    /** Here it's leading to GPS setting options*/
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    context.startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            ((MainActivity) context).finishAffinity();
                        }
                    });
            final AlertDialog alert = alertDialogBuilder.create();
            ((MainActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert.show();
                }
            });
        }
    }

    public static void updateActionBarForFeatures(final Activity parent, final String className, String heading) {
        DEBUG("Utils >> updateActionBarForFeatures() called : " + className);
        if (parent == null)
            return;

        ImageView imgActionBarAppLogoLarge = (ImageView) parent.findViewById(R.id.imgActionBarAppLogoLarge);
        final TextView txtActionBarAppFeatureName = (TextView) parent.findViewById(R.id.txtActionBarAppFeatureName);
        ImageView imgActionModel = (ImageView) parent.findViewById(R.id.imgActionModel);
        ImageView imgActionBarAddGroup = (ImageView) parent.findViewById(R.id.imgActionBarAddGroup);
        final ImageView imgActionBarDrawerIcon = (ImageView) parent.findViewById(R.id.imgActionBarDrawerIcon);
        LinearLayout lytActionBarHelp = (LinearLayout) parent.findViewById(R.id.lytActionBarHelp);
        lytActionBarHelp.setVisibility(View.VISIBLE);
        imgActionBarAppLogoLarge.setVisibility(View.GONE);
        imgActionBarAddGroup.setVisibility(View.GONE);
        imgActionModel.setVisibility(View.GONE);

        imgActionBarAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(parent, AddGroupActivity.class);
                intent.putExtra("isAddNewGroup", "true");
                intent.putExtra("isLightsGroupSetting", "false");
                parent.startActivityForResult(intent, ((MainActivity) parent).UPDATE_GROUP_DATA_RESULT_CODE);*/
            }
        });

        if (className.equalsIgnoreCase(new ModelTabFragment().getClass().getName())) {
            imgActionModel.setClickable(true);
        } else {
            imgActionModel.setClickable(false);
        }

        if (className.equals(new GroupTabFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(heading);
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);

            try {
                if (((MainActivity) parent).meshRootClass.getGroups() == null) {
                    imgActionBarAddGroup.setVisibility(View.GONE);
                    imgActionModel.setVisibility(View.GONE);
                } else {
                    imgActionModel.setVisibility(View.VISIBLE);
                    imgActionBarAddGroup.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                imgActionModel.setVisibility(View.GONE);
                imgActionBarAddGroup.setVisibility(View.GONE);
            }

            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).openDrawer();
                }
            });

        } else if (className.equals(new ModelTabFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(heading);
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
            try {
                if (((MainActivity) parent).meshRootClass.getGroups() == null) {
                    imgActionBarAddGroup.setVisibility(View.GONE);
                    imgActionModel.setVisibility(View.GONE);
                } else {
                    imgActionModel.setVisibility(View.VISIBLE);
                    imgActionBarAddGroup.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                imgActionModel.setVisibility(View.GONE);
                imgActionBarAddGroup.setVisibility(View.GONE);
            }

            imgActionModel.setVisibility(View.VISIBLE);
            imgActionModel.setImageResource(R.drawable.category);
            imgActionModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vibe = (Vibrator) contextU.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(150);
                    Utils.moveToFragment(parent, new ModelMenuFragment(), null, 0);
                }
            });

            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).openDrawer();
                }
            });

        } else if (className.equals(new SensorConfigFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(heading);
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarAddGroup.setVisibility(View.GONE);
            imgActionModel.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                }
            });
        }
    }


    public static void updateActionBarForFeatures(final Activity parent, final String className) {

        DEBUG("Utils >> updateActionBarForFeatures() called : " + className);
        if (parent == null)
            return;

        RelativeLayout lytTopBar = (RelativeLayout) parent.findViewById(R.id.lytTopBar);
        //RelativeLayout fab = (RelativeLayout) parent.findViewById(R.id.fabLayout);
        ImageView imgActionRefresh = (ImageView) parent.findViewById(R.id.imgActionRefresh);
        ImageView imgActionBarAppLogoLarge = (ImageView) parent.findViewById(R.id.imgActionBarAppLogoLarge);
        final TextView txtActionBarAppFeatureName = (TextView) parent.findViewById(R.id.txtActionBarAppFeatureName);
        final ImageView imgActionModel = (ImageView) parent.findViewById(R.id.imgActionModel);
        final ImageView imgActionBarAddGroup = (ImageView) parent.findViewById(R.id.imgActionBarAddGroup);
        final ImageView imgActionBarDrawerIcon = (ImageView) parent.findViewById(R.id.imgActionBarDrawerIcon);
        LinearLayout lytActionBarHelp = (LinearLayout) parent.findViewById(R.id.lytActionBarHelp);
        final TextView txtSave = (TextView) parent.findViewById(R.id.txtSave);
        lytActionBarHelp.setVisibility(View.VISIBLE);
        imgActionBarAppLogoLarge.setVisibility(View.GONE);
        imgActionRefresh.setVisibility(View.GONE);
        imgActionBarAddGroup.setVisibility(View.GONE);
        imgActionModel.setVisibility(View.GONE);
        txtSave.setVisibility(View.GONE);


        // click event for show-dismiss bottom sheet
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((MainActivity)parent).sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    ((MainActivity)parent).sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    ((MainActivity)parent).sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });*/

        imgActionRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) parent).restartNetwork();

            }
        });

        //update this situation
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.getSettingType(parent).equals(parent.getString(R.string.str_group_setting))) {
                    //group name
                    Utils.updateGroupNameInJson(parent, ((MainActivity) parent).meshRootClass, ((MainActivity) parent).getGroupData().getAddress(), ((MainActivity) parent).getGroupData().getName());
                } else if (Utils.getSettingType(parent).equals(parent.getString(R.string.str_nodessettings_label))) {
                    //node name
                    Utils.updateDeviceNameInJson(parent, ((MainActivity) parent).meshRootClass, ((MainActivity) parent).getNodeData().getNodesUnicastAddress(), ((MainActivity) parent).getNodeData().getNodesName(), ((MainActivity) parent).getResources().getBoolean(R.bool.bool_isElementFunctionality));
                } else if (Utils.getSettingType(parent).equalsIgnoreCase(parent.getString(R.string.str_element_setting))) {
                    ((MainActivity) parent).fragmentCommunication(new ElementSettingFragment().getClass().getName(),
                            null, parent.getResources().getInteger(R.integer.ELEMENT_NAME_UPDATE_CASE), null, false, null);
                }
            }
        });

        //final MeshRootClass finalMeshRootClass1 = meshRootClass[0];
        imgActionBarAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (className.equalsIgnoreCase(new GroupTabFragment().getClass().getName())) {
                    int count = Utils.getNextGroupCount(((MainActivity) parent).meshRootClass);
                    mobleAddress addr = mobleAddress.groupAddress(mobleAddress.GROUP_HEADER + count);
                    String grpName = "Group " + count;
                    showPopUpForNewGroup(parent, count, addr, grpName);

                } else {
                    ((MainActivity) parent).fragmentCommunication(new AddAppKeyFragment().getClass().getName(),
                            null, 0, null, false, null);
                }

            }
        });

        imgActionModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Vibrator myVib = (Vibrator) contextMainActivity.getSystemService(VIBRATOR_SERVICE);
                if (className.equalsIgnoreCase(new ModelTabFragment().getClass().getName())) {
                    Utils.moveToFragment(parent, new ModelMenuFragment(), null, 0);
                } else if (className.equalsIgnoreCase(new GroupTabFragment().getClass().getName())) {
                    ((MainActivity) parent).fragmentCommunication(new ModelTabFragment().getClass().getName(), null, 3, null, true, null);
                } else if (className.equalsIgnoreCase(new SensorConfigFragment().getClass().getName())) {
                    imgActionModel.setHapticFeedbackEnabled(true);
                    myVib.vibrate(100);
                    ((MainActivity) parent).fragmentCommunication(new SensorConfigFragment().getClass().getName(), null, 1, null, false, null);
                }
            }
        });

        if ((className.equalsIgnoreCase(new ModelTabFragment().getClass().getName()))
                || (className.equalsIgnoreCase(new GroupTabFragment().getClass().getName()))
                || (className.equalsIgnoreCase(new SensorConfigFragment().getClass().getName()))) {
            imgActionModel.setClickable(true);
        } else {
            imgActionModel.setClickable(false);
        }


        //Fab button and Actionbar visibility
        if (className.equals(new SplashScreenFragment().getClass().getName()) || className.equals(new IntroFragment().getClass().getName())) {

            ((MainActivity) parent).showInternetMsg(false);
            //fab.setVisibility(View.GONE);
            lytTopBar.setVisibility(View.GONE);
        } else {

            if (lytTopBar.getVisibility() == View.GONE) {
                lytTopBar.setVisibility(View.VISIBLE);
                lytTopBar.setAnimation(((MainActivity) parent).in_animation);
            }

            if (className.equals(new UnprovisionedFragment().getClass().getName()) ||
                    className.equals(new ProvisionedTabFragment().getClass().getName()) ||
                    className.equals(new GroupTabFragment().getClass().getName()) ||
                    className.equals(new ModelTabFragment().getClass().getName())) {

                //fab.setVisibility(View.GONE);
            } else {

                //fab.setVisibility(View.GONE);
            }
        }

        if (className.equals(new MainActivity().getClass().getName()) || className.equals(new MainViewPagerFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).openDrawer();
                }
            });

        } else if (className.equals(new GroupTabFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_group_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
            txtSave.setVisibility(View.GONE);

            try {
                if (((MainActivity) parent).meshRootClass.getGroups() == null) {
                    imgActionBarAddGroup.setVisibility(View.GONE);
                    imgActionModel.setVisibility(View.GONE);
                } else {
                    imgActionModel.setVisibility(View.VISIBLE);
                    imgActionBarAddGroup.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                imgActionModel.setVisibility(View.GONE);
                imgActionBarAddGroup.setVisibility(View.GONE);
            }

            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).openDrawer();
                }
            });


        } else if (className.equals(new UnprovisionedFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_devices_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
            imgActionBarAddGroup.setVisibility(View.GONE);
            imgActionRefresh.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).openDrawer();
                }
            });


        } else if (className.equals(new ProvisionedTabFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
            imgActionBarAddGroup.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).openDrawer();
                }
            });


        } else if (className.equals(new SyncAndInviteUserFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_cloud_invitation_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_cloud_interaction_label));
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);

                }
            });
        } else if (className.equals(new JoinAndRegisterNetworkFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_cloud_join_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_cloud_interaction_label));
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
                }
            });
        } else if (className.equals(new LoginDetailsFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_cloud_login_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        } else if (className.equals(new SignUpFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_cloud_signup_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        } else if (className.equals(new About().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_about_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        }
        else if (className.equals(new DeviceInfoFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.device_info));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        }
        else if (className.equals(new LicenceAgreementFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_licence_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_about_label));
                }
            });
        } else if (className.equals(new SoftwareComponents().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.software_components_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_about_label));
                }
            });
        } else if (className.equals(new ModelTabFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_model_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
            imgActionModel.setImageResource(R.drawable.category);
            try {
                Vibrator vibe = (Vibrator) contextU.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);

                if (((MainActivity) parent).meshRootClass.getGroups() == null) {
                    imgActionBarAddGroup.setVisibility(View.GONE);
                    imgActionModel.setVisibility(View.GONE);
                } else {
                    imgActionModel.setVisibility(View.VISIBLE);
                    imgActionBarAddGroup.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                imgActionModel.setVisibility(View.GONE);
                imgActionBarAddGroup.setVisibility(View.GONE);
            }

            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).openDrawer();
                }
            });

        } else if (className.equals(new ModelMenuFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_mesh_model_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    Utils.updateActionBarForFeatures(parent, new ModelTabFragment().getClass().getName());
                }
            });
        } else if (className.equals(new ChangePasswordFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_changepassword_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_recoverpassword_label));
                }
            });
        } else if (className.equals(new RecoverPasswordFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_recoverpassword_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_cloud_login_label));
                }
            });
        } else if (className.equals(new ExchangeConfigFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_importexportconfig_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));

                    //update actionbar
                    ((MainActivity) parent).fragmentCommunication(new ModelTabFragment().getClass().getName(), null, /*ProvisionedTabFragment*/1, null, true, null);
                }
            });
        } else if (className.equals(new AppSettingsFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_appsettings_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        } else if (className.equals(new KeyManagementFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_keymgmt_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });
        } else if (className.equals(new NodeSettingFragment().getClass().getName())) {

            txtSave.setVisibility(View.VISIBLE);
            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_nodessettings_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    txtSave.setVisibility(View.GONE);
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        } else if (className.equals(new NodeFeaturesFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_nodefeatures_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_nodessettings_label));
                }
            });
        } else if (className.equals(new HeartBeatConfigFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_heartbeatconfig_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_nodessettings_label));
                }
            });
        } else if (className.equals(new HealthConfigFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_healthConfig_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_nodessettings_label));
                }
            });
        } else if (className.equals(new HeartBeatInfoFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_heartbeatinfo_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        } else if (className.equals(new NodeInfoFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_nodeinfo_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_nodessettings_label));
                }
            });
        } else if (className.equals(new LoggerFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_logs_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        } else if (className.equals(new GroupSettingFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_groupsettings_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            txtSave.setVisibility(View.VISIBLE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    txtSave.setVisibility(View.GONE);
                    imgActionBarAddGroup.setVisibility(View.VISIBLE);
                    imgActionModel.setVisibility(View.VISIBLE);
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_group_label));
                }
            });
        } else if (className.equals(new AddGroupFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_add_group));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    imgActionBarAddGroup.setVisibility(View.VISIBLE);
                    imgActionModel.setVisibility(View.VISIBLE);
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_group_label));
                }
            });
        } else if (className.equals(new SensorConfigFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_bluenrg_sensor_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionModel.setImageResource(R.drawable.ic_refresh_white_24dp);
            imgActionModel.setVisibility(View.VISIBLE);
          /*  imgActionModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vibrator vibe = (Vibrator) contextU.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                }
            });*/
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgActionBarDrawerIcon.setHapticFeedbackEnabled(true);
                    //((MainActivity) parent).fragmentCommunication(new ModelTabFragment().getClass().getName(), Utils.getSelectedModel(parent), 0, null, false, null);
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    txtActionBarAppFeatureName.setText(Utils.getSelectedModel(parent));
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    imgActionModel.setImageResource(R.drawable.category);
                }
            });
        } else if (className.equals(new LoadConfigFeaturesFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_load_configuration_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionModel.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });
        } else if (className.equals(new AddAppKeyFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_app_key_management_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionModel.setVisibility(View.GONE);
            imgActionBarAddGroup.setVisibility(View.VISIBLE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });
        } else if (className.equals(new ElementSettingFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_element_setting_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionModel.setVisibility(View.GONE);
            txtSave.setVisibility(View.VISIBLE);

            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });
        } else if (className.equals(new GroupInformationFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_group_info_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            txtSave.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    txtSave.setVisibility(View.GONE);
                    imgActionBarAddGroup.setVisibility(View.VISIBLE);
                    imgActionModel.setVisibility(View.VISIBLE);
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_group_label));
                }
            });
        } else if (className.equals(new CapabilitySelectionFragment().getClass().getName())) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_oob_authentication_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionModel.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });


        } else if (className.equals("OTAModeFragment")) {

            controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.str_firmwareupgrade_label));
            imgActionBarDrawerIcon.setImageResource(R.drawable.ic_arrow_back_24dp);
            imgActionBarDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) parent).onBackPressed();
                    controlNavigationDrawer(parent, className, DrawerLayout.LOCK_MODE_UNLOCKED);
                    imgActionBarDrawerIcon.setImageResource(R.drawable.ic_menu_black_24dp);
                    txtActionBarAppFeatureName.setText(parent.getApplicationContext().getResources().getString(R.string.app_name));
                }
            });
        }

    }


    /**
     * used to add the given fragment, also adds in back stack
     *
     * @param activity
     * @param fragment
     * @param data
     */
    public static void moveToFragment(Activity activity, Fragment fragment, Object data, int viewType) {
        Utils.DEBUG("moveToFragment() called: " + fragment);
        if (activity == null || fragment == null) {
            return;
        }

        FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.lytMain, fragment, fragment.getClass().getName());

        Bundle bundle = new Bundle();

        if (data != null) {
            bundle.putSerializable(activity.getString(R.string.key_serializable), (Serializable) data);
        }

        {
            bundle.putInt(activity.getString(R.string.key_view_type), viewType);
        }

        /*if(!fragment.getClass().getSimpleName().equals(new MainViewPagerFragment().getClass().getSimpleName()))
        {
            //disable view pager and disable navigation drawer
            ((MainActivity)activity).closeDrawer();
            controlNavigationDrawer(activity, null, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }*/

        fragment.setArguments(bundle);
        transaction.addToBackStack(fragment.getClass().getName());
        /*transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left, R.anim.anim_slide_out_right);*/
        transaction.commit();

        if (fragment instanceof BFragment) {
            ((BFragment) fragment).onFocusEvent();
        }
    }

    /**
     * control navigation drawer lock w.r.t perticular fragment in front.
     *
     * @param parent
     * @param className
     */
    public static void controlNavigationDrawer(Activity parent, String className, Integer lockMode) {
        ((MainActivity) parent).setDrawableLockMode((DrawerLayout) parent.findViewById(R.id.drawer_layout), lockMode);
    }

    /**
     * String to array
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] arrayFromString(String str) {
        if ((str.length() & 1) != 0) {
            return null;
        }
        byte[] result = new byte[str.length() / 2];

        for (int i = 0; i < result.length; ++i) {
            String number = str.substring(i * 2, i * 2 + 2);
            result[i] = (byte) Integer.parseInt(number, 16);
        }
        return result;
    }


    /**
     * Array 2 string string.
     *
     * @param data the data
     * @return the string
     */
    public static String array2string(byte[] data) {
        char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer buffer = new StringBuffer();
        if (data != null) {
            for (int i = 0; i < data.length; ++i) {
                buffer.append(hex[(data[i] >> 4) & 0x0F]);
                buffer.append(hex[(data[i] >> 0) & 0x0F]);
            }
        }
        return buffer.toString();
    }

    /**
     * Decode temperature double.
     *
     * @param value the value
     * @return the double
     */
    public static double decodeTemperature(byte[] value) {
        int iValue = value[1] & 0xFF;
        iValue = (iValue << 8) | (value[0] & 0xFF);
        return 0.1 * iValue;
    }

    /**
     * Format c string.
     *
     * @param value the value
     * @return the string
     */
    static String formatC(double value) {
        return String.format("%+.1f", value) + "\u00b0" + "C";
    }

    /**
     * used to check if fragment exist
     *
     * @param activity
     */
    public static boolean isFragmentPresent(Activity activity, String className) {
        if (activity == null)
            return false;
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            FragmentManager.BackStackEntry backStackEntryAt = fm.getBackStackEntryAt(i);

            if (backStackEntryAt.getName().equalsIgnoreCase(className)) {
                return true;
            }
        }

        return false;
    }


    /**
     * used to clear all back stack
     *
     * @param activity
     */
    public static void clearAllBackStack(Activity activity) {
        if (activity == null)
            return;
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    /**
     * used to print given message in debug mode
     *
     * @param sb
     */
    public static void DEBUG(String sb) {
        if (contextMainActivity == null)
            return;

        if (contextMainActivity.getResources().getBoolean(R.bool.isDebugMode)) {
            if (sb.length() > 4000) {
                int chunkCount = sb.length() / 4000;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= sb.length()) {
                        Log.d("ST >> ", "ST >> " + sb.substring(4000 * i));
                    } else {
                        Log.d("ST >> ", "ST >> " + sb.substring(4000 * i, max));
                    }
                }
            } else {
                Log.d("ST >> ", "ST >> " + sb.toString());
            }

            Log.v("MoBLEapp", Thread.currentThread().getStackTrace()[3].getClassName() + "::" + Thread.currentThread().getStackTrace()[3].getMethodName() + ": " + sb);
        }

    }

    public static boolean checkConfiguration(Context context) {
        if (((UserApplication) context.getApplicationContext()).mConfiguration != null) {
            return true;
        }
        return false;
    }

    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Search device boolean.
     *
     * @param context       the context
     * @param inGroup       the in group
     * @param deviceAddress the device address
     * @return the boolean
     */
    public static boolean searchDevice(Context context, mobleAddress inGroup, String deviceAddress) {

        contextU = context;

        GroupEntry entry = ((UserApplication) context.getApplicationContext()).mConfiguration.getGroup(inGroup);
        if (null == entry)
            return false;
        Collection<String> devices = entry.getDevices();
        if (devices.contains(deviceAddress))
            return true;

        return false;
    }


    public static void removeDeviceFromGroup(Context context, mobleAddress addr, String mAutoAddress, String adress, ApplicationParameters.GenericModelID modelSelected) {
        contextU = context;


        UserApplication app = (UserApplication) context.getApplicationContext();
        try {
            app.mConfiguration.removeDeviceFromGroup(addr, mAutoAddress);
            /*to be reviewed*/
            app.mConfiguration.getNetwork().getSettings().removeGroup(context, modelSelected, mobleAddress.deviceAddress(Integer.parseInt(adress)), mobleAddress.deviceAddress(Integer.parseInt(adress)),
                    addr, mSubscriptionListener);
            app.save();
            Utils.DEBUG(">> Removed Subscribed Node or Elements >>>>>>>>>>>>> Dev Add : " + String.valueOf(adress) + " Grp Add : " + addr);
        } catch (Exception e) {
        }
    }

    public static void removeElementFromGroup(Context context, mobleAddress addr, Element element, ApplicationParameters.GenericModelID modelSelected) {
        contextU = context;

        UserApplication app = (UserApplication) context.getApplicationContext();
        try {
            app.mConfiguration.getNetwork().getSettings().removeGroup(context, modelSelected, mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress())), mobleAddress.deviceAddress(Integer.parseInt(element.getUnicastAddress())),
                    addr, mSubscriptionListener);
            Log.i("uncheckElemntAddress is", mobleAddress.deviceAddress(Integer.parseInt(element.getUnicastAddress())) + "");
            Log.i("uncheckgroupAddress is", addr + "");
        } catch (Exception e) {
        }
    }

    public static void addElementToGroup(Context context, ApplicationParameters.GenericModelID modelSelected, mobleAddress addr, Element element) {

        contextU = context;
        UserApplication app = (UserApplication) context.getApplicationContext();
        try {
            //DeviceEntry mAutoDevice = new DeviceEntry("", mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress())));
            Utils.DEBUG(">>Subscription Element Add : " + element.getUnicastAddress() + " Node Address : " + element.getParentNodeAddress() + " Grp Add : " + addr);
            //MainActivity.network.getSettings().addGroup(context, mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress())), mobleAddress.deviceAddress(Integer.parseInt(element.getUnicastAddress())), addr, mSubscriptionListener);

            network.getSettings().addGroup(
                    context,
                    mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress())),
                    mobleAddress.deviceAddress(Integer.parseInt(element.getUnicastAddress())),
                    addr,
                    //ApplicationParameters.ModelID.GENERIC_ONOFF_SERVER,
                    modelSelected,
                    mSubscriptionListener);
        } catch (Exception e) {
        }

        Log.i("ElemetAdress is", element.getUnicastAddress() + "");
        Log.i("GrpAddress is", addr + "");
    }

    /**
     * Method is used to add group in json
     *
     * @param context
     * @param addr
     * @param groupName
     */
    public static void addNewGroupToJson(Context context, mobleAddress addr, String groupName) {
        MeshRootClass messRootClass = null;
        messRootClass = ((MainActivity) context).meshRootClass;
        Group group = new Group();
        group.setAddress(String.valueOf(addr));
        group.setName(groupName);
        group.setParentAddress("C000");
        if (messRootClass.getGroups() == null) {
            ArrayList<Group> groups = new ArrayList<>();
            groups.add(group);
            messRootClass.setGroups(groups);
        } else {

            messRootClass.getGroups().add(group);
        }

        Utils.setBLEMeshDataToLocal(context, ParseManager.getInstance().toJSON(messRootClass));
        DEBUG("Group Added Successfully : " + groupName);
    }


    /**
     * Method used for removing of group from Json
     *
     * @param context
     * @param addr
     */
    public static void removeGroupFromJson(Context context, mobleAddress addr) {
        try {
            MeshRootClass messRootClass = null;
            try {
                messRootClass = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getBLEMeshDataFromLocal(context)), MeshRootClass.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (messRootClass.getGroups().size() == 0) {
                return;
            }

            for (int i = 0; i < messRootClass.getGroups().size(); i++) {

                if (messRootClass.getGroups().get(i).getAddress().equalsIgnoreCase(String.valueOf(addr))) {
                    messRootClass.getGroups().remove(i);
                }
            }
            Utils.setBLEMeshDataToLocal(context, ParseManager.getInstance().toJSON(messRootClass));
            DEBUG("Group Removed Successfully : " + addr);
        } catch (Exception e) {
        }
    }


    public static void addGroupNameToConfiguration(Context context, mobleAddress addr, String name) {
        UserApplication app = (UserApplication) context.getApplicationContext();
        app.mConfiguration.getGroup(addr).setName(name);
        app.save();
    }

    public static void setPublicationForElement(Context context, ApplicationParameters.GenericModelID modelSelected, mobleAddress nodeAddress, mobleAddress elementAddress, int selected_position, ArrayList<Publish> publicationList) {
        ((MainActivity) contextMainActivity).mUserDataRepository.getNewDataFromRemote("Set Publication For Element==>" + elementAddress, LoggerConstants.TYPE_SEND);
        UserApplication app = (UserApplication) context.getApplicationContext();

        MeshRootClass meshRootClass = null;
        try {
            meshRootClass = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getBLEMeshDataFromLocal(context)), MeshRootClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isGroup = false;
        for (int i = 0; i < meshRootClass.getGroups().size(); i++) {

            if (meshRootClass.getGroups().get(i).getAddress().equals(publicationList.get(selected_position).getAddress())) {
                isGroup = true;
                break;
            }
        }

        int appKeyIndex = Utils.getDefaultAppKeyIndex(context);

        if (isGroup) {
            app.mConfiguration.getNetwork().getSettings().setPublicationAddress(context, appKeyIndex, modelSelected, nodeAddress, elementAddress, Integer.parseInt(publicationList.get(selected_position).getAddress(), 16), mPublicationListener);

        } else {
            app.mConfiguration.getNetwork().getSettings().setPublicationAddress(context, appKeyIndex, modelSelected, nodeAddress, elementAddress, Integer.parseInt(publicationList.get(selected_position).getAddress()), mPublicationListener);
        }

    }


    public static void setPublicationToConfiguration(Context context, mobleAddress peerAddress, int selected_position, List<String> groupList) {
        UserApplication app = (UserApplication) context.getApplicationContext();
        app.mConfiguration.setPublication(String.valueOf(peerAddress), groupList.get(selected_position));
        app.save();
    }

    public static void removeGroupFromConfiguration(Context context, mobleAddress addr) {
        try {
            UserApplication app = (UserApplication) context.getApplicationContext();
            app.mConfiguration.removeGroup(addr);
            app.save();
        } catch (Exception e) {
            UserApplication.trace("Exception >>>>>>> Utils : removeGroupFromConfiguration");
        }
    }


    /**
     * Child row creater linear layout.
     *
     * @param context              the context
     * @param parent               the parent
     * @param mDataNodeProvisioned the m data node provisioned
     * @return the linear layout
     */
    public static LinearLayout childRowCreater(Context context, LinearLayout parent, ArrayList<DataNodeProvisioned> mDataNodeProvisioned) {
        LayoutInflater layoutInfralte = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        System.out.println("Size >>>>" + mDataNodeProvisioned.size());
        List views = new ArrayList();
        views.clear();
        try {
            if (parent.getChildCount() > 0) {
                parent.removeAllViews();
            }
        } catch (Exception e) {
        }

        for (int i = 0; i < mDataNodeProvisioned.size(); i++) {
            View view = layoutInfralte.inflate(R.layout.child_one_row_add, null);
            TextView txtRowChild = (TextView) view.findViewById(R.id.txtRowChild);
            txtRowChild.setText(i + 1 + ") " + mDataNodeProvisioned.get(i).getM_address() + " : " + mDataNodeProvisioned.get(i).getTitle().toString());
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            views.add(view);
        }

        for (int i = 0; i < views.size(); i++) {
            parent.addView((View) views.get(i));
        }

        return parent;
    }


    /**
     * method used to set the height of layout
     *
     * @param arraySize
     * @param lytLinear
     */
    public static void calculateHeight1(int arraySize, RecyclerView lytLinear) {
        int each = 80;

        ViewGroup.LayoutParams layoutParams = lytLinear.getLayoutParams();
        if (arraySize >= 1) {
            layoutParams.height = (int) Utils.conertDpToPixel(lytLinear.getContext(), each * arraySize);
            lytLinear.setLayoutParams(layoutParams);
        } else {
            layoutParams.height = (int) Utils.conertDpToPixel(lytLinear.getContext(), 0);
            lytLinear.setLayoutParams(layoutParams);
        }
    }

    /**
     * method used to set the height of elements inside recycler
     *
     * @param arraySize
     * @param lytLinear
     */
    public static void calculateHeight(int arraySize, RecyclerView lytLinear) {
        int each = 40;

        ViewGroup.LayoutParams layoutParams = lytLinear.getLayoutParams();

        if (arraySize > 0) {
            layoutParams.height = (int) Utils.conertDpToPixel(lytLinear.getContext(), each * arraySize > 160 ? 160 : each * arraySize);
            lytLinear.setLayoutParams(layoutParams);
        } else {
            layoutParams.height = (int) Utils.conertDpToPixel(lytLinear.getContext(), 0);
            lytLinear.setLayoutParams(layoutParams);
        }
    }

    /**
     * method used to convert dp to pixel
     *
     * @param context
     * @param dp
     * @return
     */
    public static float conertDpToPixel(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics mat = context.getResources().getDisplayMetrics();
        float px = dp * ((float) mat.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        return px;
    }

    public static String getBLEMeshDataFromLocal(Context context) {
        if (context == null)
            return null;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_mesh_data));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setBLEMeshDataToLocal(Context context, String messStringJson) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put(context.getString(R.string.key_mesh_data), messStringJson);

    }

    public static String getProxyNode(Context context) {
        if (context == null)
            return null;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_proxy_address));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setProxyNode(Context context, String proxyAddress) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_proxy_address), proxyAddress);

    }

    public static void setProxyUUID(Context context, String uuid) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_proxy_uuid), uuid);

    }

    /*
     * check keyboard status
     * */

    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Utils.ERROR("Error in hideKeyboard() : " + e.toString());
        }

    }

    public static void ERROR(String message) {

        Log.e("ST >> ", "ST >> " + message);
    }

    public static void onNodeConfigured(Context context, Nodes mDataCurrentNode, MeshRootClass meshRootClass) {

        if (mDataCurrentNode == null)
            return;

        for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
            if (meshRootClass.getNodes().get(i).getUUID().equals(mDataCurrentNode.getUUID())) {
                meshRootClass.getNodes().set(i, mDataCurrentNode);
            }
        }
        String meshStringJson = ParseManager.getInstance().toJSON(meshRootClass);
        if (meshStringJson != null) {
            Utils.setBLEMeshDataToLocal(context, meshStringJson);

            Utils.saveModelInfoOfMultiElement(context, null);
            Utils.saveModelInfo(context, null);
            Utils.setNodeFeatures(context, null);
        }
    }

    public static void setProvisionedNodeData(Context context, Nodes mDataCurrentNode, MeshRootClass meshRootClass) {

        if (mDataCurrentNode == null)
            return;

        String meshStringJson = null;

        if (meshRootClass.getNodes() == null) {
            ArrayList<Nodes> currentNode = new ArrayList<>();
            currentNode.add(mDataCurrentNode);
            meshRootClass.setNodes(currentNode);
            meshStringJson = ParseManager.getInstance().toJSON(meshRootClass);

            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject(meshStringJson);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {

            meshRootClass.getNodes().add(mDataCurrentNode);
            meshStringJson = ParseManager.getInstance().toJSON(meshRootClass);
        }


        if (meshStringJson != null) {
            Utils.setBLEMeshDataToLocal(context, meshStringJson);
        }
    }

    public static void updateProvisionerNetAppKeys(Context context, MeshRootClass meshRootClass, String meshName, String meshUUID, String appKeysStr, String networkKey, boolean isNewJoiner) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ArrayList<NetKey> arrayList = new ArrayList<>();
        NetKey netKey = new NetKey();
        netKey.setIndex(0);
        netKey.setKey(networkKey);
        netKey.setTimestamp("" + timestamp);
        netKey.setMinSecurity("High");
        netKey.setPhase(0);
        arrayList.add(netKey);

        ArrayList<AppKey> appKeys = new ArrayList<>();
        AppKey appKey = new AppKey();
        appKey.setIndex(0);
        appKey.setBoundNetKey(0);
        appKey.setName("AppKey 0");
        appKey.setKey(appKeysStr);
        appKeys.add(appKey);

        try {
            Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
            if (meshRootClass == null) {

                MeshRootClass localRootClass = new MeshRootClass();
                localRootClass.setSchema("http://json-schema.org/draft-04/schema#");
                localRootClass.setId("http://www.bluetooth.com/specifications/assigned-numbers/mesh-profile/cdb-schema.json#");
                localRootClass.setVersion("1.0.0");
                localRootClass.setTimestamp(""+timestamp1);
                localRootClass.setMeshUUID(meshUUID);
                localRootClass.setMeshName(meshName);
                localRootClass.setAppKeys(appKeys);
                localRootClass.setNetKeys(arrayList);
                localRootClass.setIVindex(0);//TODO : take it fro mthe new JSON
                meshRootClass = localRootClass;
            } else {

                meshRootClass.setSchema("http://json-schema.org/draft-04/schema#");
                meshRootClass.setMeshUUID(meshUUID);
                meshRootClass.setId("http://www.bluetooth.com/specifications/assigned-numbers/mesh-profile/cdb-schema.json#");
                meshRootClass.setVersion("1.0.0");
                meshRootClass.setTimestamp(""+timestamp1);

                meshRootClass.setMeshName(meshName);
                meshRootClass.setIVindex(0);//TODO : take it from the new JSON
                meshRootClass.getAppKeys().add(appKey);
                meshRootClass.getNetKeys().add(netKey);
            }

            MeshRootClass meshRootClass1 = Utils.addProvisioner(context, meshRootClass, false);
            String data = ParseManager.getInstance().toJSON(meshRootClass1);
            Utils.setBLEMeshDataToLocal(context, data);
        } catch (Exception e) {
        }
    }

    public static ArrayList<Element> designElementsForCurrentNode(Context context, Nodes currentNode, MeshRootClass messRootClass) {

        Integer numberOfElements = currentNode.getNumberOfElements();

        String address = currentNode.getM_address();   //0002
        UserApplication.trace("Address : " + address);
        int intVal = Integer.valueOf(address, 16);
        int firstElementAddress = intVal;
        int count = intVal;

        ArrayList<Element> elements = new ArrayList<>();

        for (int i = 0; i < numberOfElements; i++) {

            Element element = new Element();
            element.setUnicastAddress(String.valueOf(count));
            element.setModels(null);
            //element.setModels(getCompleteModelsForCurrentElement(context, element, messRootClass, true));
            //element.setParentNodeAddress(currentNode.getM_address());
            element.setParentNodeAddress(String.valueOf(firstElementAddress));
            element.setParentNodeName(currentNode.getName());
            element.setElementName("Element " + (i + 1));
            element.setName("Element " + (i + 1));
            elements.add(element);
            count++;
        }
        return elements;
    }

    public static ArrayList<Element> updateAllElementsByModel(Context context, ArrayList<Element> elements, ModelsDataByElement modelsDataByElement) {

        try {
            if (Utils.getModelInfoOfMultiElement(context) != null) {
                //ModelsDataByElement modelsDataByElem = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getModelInfoOfMultiElement(context)), ModelsDataByElement.class);
                for (int i = 0; i < elements.size(); i++) {
                    for (int j = 0; j < modelsDataByElement.getModelDataByElement().size(); j++) {
                        if (i == j) {
                            ArrayList<Model> models = new ArrayList<>();
                            ModelsData modelsData = modelsDataByElement.getModelDataByElement().get(j);
                            for (int k = 0; k < modelsData.getModelInfos().size(); k++) {
                                Model model = new Model();
                                model.setModelId(modelsData.getModelInfos().get(k).getModelID());
                                model.setModelName(modelsData.getModelInfos().get(k).getModelName());
                                models.add(model);
                            }
                            elements.get(i).setModels(models);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Utils.DEBUG("Model Parsing Issue");
        }
        return elements;
    }

    public static String removeProvisionNodeFromJson(Context context, String peerAddress) {

        MeshRootClass messRootClass = null;
        String uuid = null;

        try {
            messRootClass = ((MainActivity) context).meshRootClass;
            if (messRootClass != null) {

                for (int i = 0; i < messRootClass.getNodes().size(); i++) {

                    if (messRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(String.valueOf(peerAddress))) {
                        uuid = messRootClass.getNodes().get(i).getUUID();
                        messRootClass.getNodes().remove(messRootClass.getNodes().get(i));
                    }
                }

                if (messRootClass.getNodes().size() == 0) {
                    messRootClass.getGroups().clear();
                }

                String meshStringJson = ParseManager.getInstance().toJSON(messRootClass);
                Utils.setBLEMeshDataToLocal(context, meshStringJson);
                ((MainActivity) context).updateJsonData();
            }

        } catch (Exception e) {
        }

        return uuid;
    }

    public static int getNextNodeNumber(ArrayList<Nodes> nodes, MeshRootClass meshRootClass) {
        int nextNodeCount = 1;

        if (nodes != null && nodes.size() > 0) {
            for (int i = 0; i < nodes.size(); i++) {

                try {
                    if (nodes.get(i).getNodeNumber() == nextNodeCount) {
                        nextNodeCount++;
                    } else if (nodes.get(i).getNodeNumber() > nextNodeCount) {
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
        return nextNodeCount;
    }

    public static int getNextElementNumber(Context context, ArrayList<Nodes> nodes) {

        int nextElementCount = MainActivity.provisionerUnicastLowAddress + 1;

        try {
            for (int i = 0; i < nodes.size(); i++) {

                for (int j = 0; j < nodes.get(i).getElements().size(); j++) {

                    try {
                        int number = Integer.parseInt(nodes.get(i).getElements().get(j).getUnicastAddress());

                        if (number == nextElementCount) {
                            UserApplication.trace("getNextElementNumber : nextElement Address = " + nextElementCount);
                            nextElementCount++;
                        } else if (number > nextElementCount) {
                            break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println(" is not a valid integer number");
                    }
                }

            }
        } catch (Exception em) {

            UserApplication.trace("nextElementCount : " + nextElementCount + " :: " + em.getMessage());
        }


        return nextElementCount;
    }

    private static ArrayList<Publish> getPublisherListForCurrentModel(String pubAddress, MeshRootClass messRootClass, boolean isDefault) {


        ArrayList<Publish> publishes = new ArrayList<>();
        boolean isElementSelected = false;

        try {
            for (int i = 0; i < messRootClass.getNodes().size(); i++) {

                for (int j = 0; j < messRootClass.getNodes().get(i).getElements().size(); j++) {

                    Publish address = new Publish();
                    address.setAddress(messRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress());
                    if (contextU.getResources().getBoolean(R.bool.bool_isElementFunctionality)) {

                        address.setCurrentParentNodeName(messRootClass.getNodes().get(i).getName());
                        address.setName(messRootClass.getNodes().get(i).getElements().get(j).getElementName());

                        /*
                         * IMPORTANT :-
                         *
                         * This case runs for selecting publisher selected at 70% during provisioning.
                         *
                         * Note 1 (Currently Working):  Index 0 for models has been taken to get publisher which is same present in
                         * all models of this element. This situation is working inpresent scenerio in which all models in an element
                         * having same publication and subscription data.
                         *
                         * Note 2 (Will be updated in future):  If publication and subscription data in an model independent of each other (or not same) then kindly update
                         * this scenerio in that condition.
                         *
                         * Hence for note 1 below condition "publicationAddress" will be assigned at once and for note 2 case it will be dynamic.
                         * */

                        String thisElementAddress = messRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress();
                        if (pubAddress.equals(thisElementAddress) || pubAddress.contains(thisElementAddress)) {
                            isElementSelected = true;
                            address.setChecked(true);
                        } else {
                            address.setChecked(false);
                        }

                    } else {
                        address.setName(messRootClass.getNodes().get(i).getName());
                    }

                    publishes.add(address);

                }

            }
        } catch (Exception e) {
        }

        try {
            for (int i = 0; i < messRootClass.getGroups().size(); i++) {
                Publish address = new Publish();
                address.setCurrentParentNodeName(messRootClass.getGroups().get(i).getName());
                address.setAddress(messRootClass.getGroups().get(i).getAddress());
                address.setName(messRootClass.getGroups().get(i).getName());

                if (!isElementSelected) {
                    if (pubAddress.equals(messRootClass.getGroups().get(i).getAddress())) {
                        address.setChecked(true);
                    } else {
                        address.setChecked(false);
                    }
                } else {
                    address.setChecked(false);
                }

                publishes.add(address);
            }
        } catch (Exception e) {
        }

        return publishes;
    }

    public static void json_UpdatePublisherForCurrentModel(Context context, MeshRootClass messRootClass, String nodeAddress, String elementUnicastAddress, ArrayList<Publish> elePublicationList, Model modelSelected) {

        String nodeAddr = "";

        if (Utils.isStringIsHex(nodeAddress)) {
            nodeAddr = String.valueOf(Integer.parseInt(nodeAddress, 16));

        } else {
            nodeAddr = String.valueOf(Integer.parseInt(nodeAddress));
        }
        for (int i = 0; i < messRootClass.getNodes().size(); i++) {

            if (messRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(nodeAddr)) {
                for (int j = 0; j < messRootClass.getNodes().get(i).getElements().size(); j++) {
                    if (messRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(elementUnicastAddress)) {
                        ArrayList<Model> models = new ArrayList<>();

                        /*
                         * IMPORTANT
                         *
                         * As per note 1 (written in method "getModelsForCurrentElement2")
                         * So all models publication and subscription will be reset to same value as selected by the user.
                         * So traversing the loop for complete models in an element.
                         *
                         * */

                        for (int k = 0; k < messRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                            //if note 2 : implement based on model-id

                            if (messRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId().equalsIgnoreCase(modelSelected.getModelId())) {
                                messRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setPublish(modelSelected.getPublish());
                            }
                        }
                    }
                }
            }
        }

        Utils.setBLEMeshDataToLocal(context, ParseManager.getInstance().toJSON(messRootClass));

        return;
    }

    public static void updateGroupNameInJson(final Context context, MeshRootClass meshRootClass, String addr, String grpName) {

        int position = 0;
        try {
            for (int i = 0; i < meshRootClass.getGroups().size(); i++) {
                if (meshRootClass.getGroups().get(i).getAddress().equalsIgnoreCase(addr)) {
                    meshRootClass.getGroups().get(i).setName(grpName);
                }
            }
        } catch (Exception e) {
        }

        Utils.setBLEMeshDataToLocal(context, ParseManager.getInstance().toJSON(meshRootClass));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) context).updateJsonData();
            }
        });
        ((MainActivity) context).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
        ((MainActivity) context).onBackPressed();
    }

    public static void updateDeviceNameInJson(final Context context, MeshRootClass meshRootClass, String deviceAddr, String s, boolean isElementVersion) {

        try {
            Utils.DEBUG("Change Name");
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                if (meshRootClass.getNodes().get(i) != null && meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(deviceAddr)) {
                    meshRootClass.getNodes().get(i).setName(s);
                    //Log.e("Dinesh==>Utils 2213  ",s);

                    for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                        meshRootClass.getNodes().get(i).getElements().get(j).setParentNodeName(s);
                    }
                }
            }

            Utils.setBLEMeshDataToLocal(context, ParseManager.getInstance().toJSON(meshRootClass));
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) context).updateJsonData();
                }
            });
            ((MainActivity) context).fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
            ((MainActivity) context).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
            ((MainActivity) context).fragmentCommunication(new ModelTabFragment().getClass().getName(), null, 0, null, false, null);
            ((MainActivity) context).onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Vibrator getVibrator(Context context) {

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        return vibrator;
    }

    //>>>>>>>>>>>> Sync Json For Sharing >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    /**
     * used to read given file from asset folder of application
     *
     * @param context
     * @param fileName
     * @return content of file
     */
    public static String readFromAssets(Context context, String fileName) {
        Utils.DEBUG(">> ST readFromAssets() >> " + fileName);
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            //reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName), StandardCharsets.UTF_8));
            //InputStream is = ClassLoader.class.getClassLoader().getResourceAsStream("assets/MeshRootClass.txt");
            //reader = new BufferedReader(new InputStreamReader(is));
            //AssetManager assetManager = new AssetManager();
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                builder.append(mLine);
            }
        } catch (IOException e) {
            Utils.DEBUG("Error while reading data from file : " + fileName + ", " + e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return builder.toString();
    }

    public static void syncWithCloud(Context context, MeshRootClass messRootClass, String urlStr, String response) {

        //get data from cloud as a string format
        //store data in local
        String url = "getMeshDataURL";
        String fileName = null;
        String meshStringJson = null;
        MeshRootClass meshRootClass = null;

        try {
            if (context.getResources().getBoolean(R.bool.bool_readFromAssets)) {
                if (url.contains(context.getString(R.string.URL_GET_MESH_DATA))) {
                    fileName = "MeshRootClass.txt";
                }
            } else {
                Utils.DEBUG("json request : " + response.toString());
                Utils.DEBUG("url : " + url);
            }
        } catch (Exception e) {
        }

        try {
            meshRootClass = ParseManager.getInstance().fromJSON(new JSONObject(Utils.readFromAssets(context, fileName)), MeshRootClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        meshStringJson = ParseManager.getInstance().toJSON(meshRootClass);

        if (meshStringJson != null) {
            Utils.setBLEMeshDataToLocal(context, meshStringJson);
        }

    }

    public static boolean buildData(Context context, MeshRootClass meshRootClass) {
        String s = ParseManager.getInstance().toJSON(meshRootClass);

        Utils.DEBUG(" >> JSON Data Invited : " + s);
        if (s != null) {
            Utils.setBLEMeshDataToLocal(context, s);
            return true;
        } else {
            return false;
        }
    }

    private static ArrayList<Publish> add_getPublisherListForCurrentModelJson(Context context, String peerAddress, MeshRootClass messRootClass, String publishAddressChecked) {

        // set provisioned node and group data to publish array

        ArrayList<Publish> publishes = new ArrayList<>();

        try {
            for (int i = 0; i < messRootClass.getNodes().size(); i++) {

                for (int j = 0; j < messRootClass.getNodes().get(i).getElements().size(); j++) {

                    Publish address = new Publish();
                    address.setAddress(messRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress());
                    if (context.getResources().getBoolean(R.bool.bool_isElementFunctionality)) {
                        address.setName("Element");
                    } else {
                        address.setName(messRootClass.getNodes().get(i).getName());
                    }

                    if (address.getAddress().equalsIgnoreCase(publishAddressChecked)) {
                        address.setChecked(true);
                    } else {
                        address.setChecked(false);
                    }

                    publishes.add(address);
                }

            }
        } catch (Exception e) {
        }

        try {
            for (int i = 0; i < messRootClass.getGroups().size(); i++) {
                Publish address = new Publish();
                address.setAddress(messRootClass.getGroups().get(i).getAddress());
                address.setName(messRootClass.getGroups().get(i).getName());

                //assign default group to checked true
                if (messRootClass.getGroups().get(i).getAddress().equalsIgnoreCase(context.getResources().getString(R.string.str_lightsgroup_address))) {
                    address.setChecked(true);
                } else {
                    address.setChecked(false);
                }

                if (address.getAddress().equalsIgnoreCase(publishAddressChecked)) {
                    address.setChecked(true);
                } else {
                    address.setChecked(false);
                }

                publishes.add(address);
            }
        } catch (Exception e) {
        }

        return publishes;
    }

    private static boolean checkIfGroupExistForThisElement(String elementAddres, String grpAddres, MeshRootClass meshRootClass) {

        for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
            for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                if (meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress()
                        .equalsIgnoreCase(elementAddres)) {
                    for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                        for (int l = 0; l < meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().size(); l++) {
                            if (meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().get(l)
                                    .equalsIgnoreCase(grpAddres)) {
                                return true;
                            }
                        }
                    }
                    break;
                }
            }
        }

        return false;
    }

    public static void sensorModelEvents(Context context, MotionEvent event, ElementsRecyclerAdapter.IRecyclerViewHolderClicks listener
            , int ele_pos, Nodes node, ApplicationParameters.SensorDataPropertyId propertyID, int nodePosition, boolean switchScreen) {

        if (event == null) {

        }
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (Utils.getVibrator(context) != null) {
                Utils.getVibrator(context).vibrate(100);
            }
            //((ImageView) (v)).setImageResource(R.drawable.bulb);
        } else if (MotionEvent.ACTION_UP == event.getAction()) {

            if (Utils.getSelectedModel(context).equalsIgnoreCase(context.getString(R.string.str_sensormodel_label))) {
                //listener.showFrameOverNode(node.getElements().get(ele_pos).getUnicastAddress(), ele_pos);
                listener.onSensorRefresh(node, ele_pos, propertyID, nodePosition);

                if (switchScreen) {
                    Utils.moveToFragment((MainActivity) context, new SensorConfigFragment(), node.getElements().get(ele_pos), 0);
                }
            }
        }

    }


    public static void toggleDevice(String from ,final Context context, final View v, MotionEvent event,
                                    final Element element, final ElementsRecyclerAdapter.IRecyclerViewHolderClicks listener, final int ele_pos,String selectedmodel) {

        final String address = element.getUnicastAddress();

        //Set remote Data :TODO
        try {
            if (Utils.getProxyNode(context) == null) {
                //((MainActivity) context).mLostConnectionDialog.createDialog();
                return;
            }

            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                if (Utils.getVibrator(context) != null) {
                    Utils.getVibrator(context).vibrate(50);
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateAppKey(context, element);
                        updateElementToggleState(context, element.getUnicastAddress(), element.isChecked() ? false : true);
                    }
                });

            } else if (MotionEvent.ACTION_UP == event.getAction()) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        Switch onofSwitch = (Switch) v.findViewById(R.id.butSwitch);
                        Boolean onCommand = false;
                        if (onofSwitch != null) {
                            if (!onofSwitch.isChecked()) {
                                onCommand = true;
                            }
                        }

                        mobleNetwork network = MainActivity.network;
                        Utils.listener = listener;
                        element_position = ele_pos;
                        //pref_model_selection = context.getSharedPreferences("Model_Selection", MODE_PRIVATE);
                        //if (listener != null && pref_model_selection.getBoolean("Selected_Model", false)) {
                        if (listener != null && Utils.isVendorModelCommand(context)) {
                            if (Utils.isReliableEnabled(context)) {
                                listener.showFrameOverNode(address, ele_pos);
                            }
                        }

                        if (!from.equalsIgnoreCase("Model")) {
                            if (Utils.isVendorModelCommand(context)) {
                                if (Utils.isReliableEnabled(context)) {
                                    network.advise(mGroupReadCallback);
                                }

                                UserApplication.trace("NavBar Device toggle  model vendor model selected");
                                network.getApplication().setRemoteData(mobleAddress.deviceAddress(Integer.parseInt(address)),
                                        Nucleo.APPLI_CMD_LED_CONTROL, 1, onCommand ?
                                                new byte[]{Nucleo.APPLI_CMD_LED_ON} : new byte[]{Nucleo.APPLI_CMD_LED_OFF}, Utils.isReliableEnabled(context));
                                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Vendor OnOff command sent to ==>" + address, LoggerConstants.TYPE_SEND);
                            } else {
                                try {
                                    UserApplication.trace("NavBar Device toggle generic model selected");
                                    state = onCommand ? ApplicationParameters.OnOff.ENABLED : ApplicationParameters.OnOff.DISABLED;
                                    ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                                    // ApplicationParameters.Time transitionTime = ApplicationParameters.Time.NONE;
                                    // ApplicationParameters.Delay del = new ApplicationParameters.Delay(20);

                                    UserApplication app = (UserApplication) context.getApplicationContext();
                                    network.getOnOffModel().setGenericOnOff((Utils.isReliableEnabled(context))
                                            , new ApplicationParameters.Address(Integer.parseInt(address)), //=>new ApplicationParameters.Address(mAddress.mValue),
                                            state,
                                            tid,
                                            null,
                                            null,
                                            (Utils.isReliableEnabled(context)) ? mOnOffCallback : null);
                                    ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Generic OnOff command sent to ==>" + address, LoggerConstants.TYPE_SEND);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            if (Utils.getSelectedModel(context).equalsIgnoreCase("Vendor Model")) {
                                if (Utils.isReliableEnabled(context)) {
                                    network.advise(mGroupReadCallback);
                                }

                                UserApplication.trace("NavBar Device toggle  model vendor model selected");
                                network.getApplication().setRemoteData(mobleAddress.deviceAddress(Integer.parseInt(address)),
                                        Nucleo.APPLI_CMD_LED_CONTROL, 1, onCommand ?
                                                new byte[]{Nucleo.APPLI_CMD_LED_ON} : new byte[]{Nucleo.APPLI_CMD_LED_OFF}, Utils.isReliableEnabled(context));
                                ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Vendor OnOff command sent to ==>" + address, LoggerConstants.TYPE_SEND);
                            } else {
                                try {
                                    UserApplication.trace("NavBar Device toggle generic model selected");
                                    state = onCommand ? ApplicationParameters.OnOff.ENABLED : ApplicationParameters.OnOff.DISABLED;
                                    ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(context));
                                    // ApplicationParameters.Time transitionTime = ApplicationParameters.Time.NONE;
                                    // ApplicationParameters.Delay del = new ApplicationParameters.Delay(20);

                                    UserApplication app = (UserApplication) context.getApplicationContext();
                                    network.getOnOffModel().setGenericOnOff((Utils.isReliableEnabled(context))
                                            , new ApplicationParameters.Address(Integer.parseInt(address)), //=>new ApplicationParameters.Address(mAddress.mValue),
                                            state,
                                            tid,
                                            null,
                                            null,
                                            (Utils.isReliableEnabled(context)) ? mOnOffCallback : null);
                                    ((MainActivity) context).mUserDataRepository.getNewDataFromRemote("Generic OnOff command sent to ==>" + address, LoggerConstants.TYPE_SEND);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
        }
    }


    public static void updateAppKey(Context context, Element element) {

        if (element.getModels() != null) {
            for (int i = 0; i < element.getModels().size(); i++) {

                if (element.getModels().get(i).getModelName().equalsIgnoreCase((context.getString(R.string.MODEL_SUB_NAME_GENERIC_ONOFF)).replace("_", " "))) {
                    if (element.getModels().get(i).getBind() != null && element.getModels().get(i).getBind().size() > 0) {

                        int activeKeyIndex = element.getModels().get(i).getActiveKeyIndex();
                        //Integer index = element.getModels().get(i).getBind().get(0);

                        for (int j = 0; j < ((MainActivity) context).meshRootClass.getAppKeys().size(); j++) {
                            if (((MainActivity) context).meshRootClass.getAppKeys().get(j).getIndex() == activeKeyIndex) {
                                byte[] bytes = Utils.hexStringToByteArray(((MainActivity) context).meshRootClass.getAppKeys().get(j).getKey());

                                network.setAppKey(bytes);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    public static void updateElementToggleState(Context context, String unicastAddress, boolean isCheck) {
        try {

            MeshRootClass cloneMeshRoot = (MeshRootClass) ((MainActivity) context).meshRootClass.clone();

            for (int i = 0; i < cloneMeshRoot.getNodes().size(); i++) {
                for (int j = 0; j < cloneMeshRoot.getNodes().get(i).getElements().size(); j++) {

                    if (cloneMeshRoot.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(unicastAddress)) {
                        cloneMeshRoot.getNodes().get(i).getElements().get(j).setChecked(isCheck);
                        break;
                    }
                }
            }

            String s = ParseManager.getInstance().toJSON(cloneMeshRoot);
            Utils.setBLEMeshDataToLocal(context, s);
            ((MainActivity) context).updateJsonData();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();

        } catch (Exception e) {
        }
    }

    public static String readDataFromFile(Context context) {
        BufferedReader input = null;
        File file = null;
        String stringData = null;
        try {
            file = new File(context.getCacheDir(), "AAABleCache"); // Pass getFilesDir() and "MyFile" to read file

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }

            stringData = buffer.toString();
            Utils.DEBUG(" >> Read Data From File : " + buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringData;
    }


    public static void sendMail(Context context, String[] mailTo, String[] cc, String subject, String body, String attachmentFilePath) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.setType("plain/text");

        if (mailTo != null)
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mailTo);
        if (cc != null)
            emailIntent.putExtra(android.content.Intent.EXTRA_CC, cc);
        if (subject != null)
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        if (body != null)
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
        if (mailTo != null)
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentFilePath));

        context.startActivity(emailIntent);
    }

    public static String filterJsonObject(Context context) {

        String bleMeshDataFromLocal = Utils.getBLEMeshDataFromLocal(context);
        JSONObject requestObject = null;

        try {
            requestObject = new JSONObject(bleMeshDataFromLocal);

            if (requestObject == null)
                return null;

            JSONArray groupArray = requestObject.getJSONArray("groups");
            JSONArray nodesArray = requestObject.getJSONArray("nodes");
            JSONArray appKeys = requestObject.getJSONArray("appKeys");

            for (int i = 0; i < appKeys.length(); i++) {
                JSONObject appKeyObject = appKeys.getJSONObject(i);
                try {
                    appKeyObject.remove("appKeyBytes");
                    appKeyObject.remove("isChecked");
                } catch (Exception e) {
                }
            }

            for (int i = 0; i < groupArray.length(); i++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                try {
                    groupObject.remove("elements");
                    groupObject.remove("nodes");
                    groupObject.remove("isChecked");
                } catch (Exception e) {
                }
            }

            for (int i = 0; i < nodesArray.length(); i++) {
                JSONObject nodeObject = nodesArray.getJSONObject(i);
                //nodeObject.remove("blacklisted");
                try {
                    nodeObject.remove("address");
                    nodeObject.remove("configured");
                    nodeObject.remove("in_range");
                    nodeObject.remove("isChecked");
                    nodeObject.remove("publish_address");
                    nodeObject.remove("subgroup");
                    nodeObject.remove("subtitle");
                    nodeObject.remove("title");
                    nodeObject.remove("type");
                    nodeObject.remove("showProgress");
                    nodeObject.remove("numberOfElements");
                    nodeObject.remove("m_address");
                    nodeObject.remove("nodeNumber");
                } catch (Exception e) {
                }


                try {
                    JSONArray appKeyNode = nodeObject.getJSONArray("appKeys");
                    if (appKeyNode != null && appKeyNode.length() > 0) {
                        for (int a = 0; a < appKeyNode.length(); a++) {
                            JSONObject appKeyObj = appKeyNode.getJSONObject(a);
                            try {
                                appKeyObj.remove("appKeyBytes");
                                appKeyObj.remove("isChecked");
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (Exception e) {
                }

                JSONArray elementsArray = nodeObject.getJSONArray("elements");
                for (int j = 0; j < elementsArray.length(); j++) {
                    JSONObject elementObject = elementsArray.getJSONObject(j);
                    //convert to hex
                    String unicastAddress = elementObject.getString("unicastAddress");
                    boolean stringIsHex = isStringIsHex(unicastAddress);
                    if (stringIsHex) {
                        String intUnicast = convertIntToHex(Integer.parseInt(unicastAddress));
                        elementObject.put("unicastAddress", intUnicast);
                    }

                    try {
                        elementObject.remove("elementName");
                        elementObject.remove("isChecked");
                        elementObject.remove("isConfigured");
                        elementObject.remove("isPublished");
                        elementObject.remove("isSubscribed");
                        elementObject.remove("parentNodeAddress");
                        elementObject.remove("parentNodeName");
                        elementObject.remove("showProgress");
                        elementObject.remove("tempSensorValue");
                        elementObject.remove("pressureSensorValue");
                        elementObject.remove("accelerometerValue");
                        elementObject.remove("completeSensorData");
                    } catch (Exception e) {
                    }

                    try {
                        JSONArray modelsArray = elementObject.getJSONArray("models");
                        if (modelsArray != null && modelsArray.length() > 0) {
                            for (int k = 0; k < modelsArray.length(); k++) {
                                JSONObject modelObject = modelsArray.getJSONObject(k);
                                try {
                                    modelObject.remove("publishingAddressList");
                                    modelObject.remove("isChecked");
                                    modelObject.remove("modelName");
                                    modelObject.remove("activeKeyIndex");
                                } catch (Exception e) {
                                }
                                try {
                                    JSONObject publishObject = modelObject.getJSONObject("publish");
                                    try {
                                        publishObject.remove("isChecked");
                                        publishObject.remove("isTypeNode");
                                        publishObject.remove("modelName");
                                    } catch (Exception e) {
                                    }

                                    /*JSONArray publishArray = modelObject.getJSONArray("publish");
                                    if(publishArray != null && publishArray.length() > 0)
                                    {
                                        for (int l = 0; l < publishArray.length(); l++) {
                                            JSONObject publishObject = modelsArray.getJSONObject(l);
                                            try {
                                                publishObject.remove("isChecked");
                                                publishObject.remove("isTypeNode");
                                                publishObject.remove("modelName");
                                            }catch (Exception e){}
                                        }
                                    }*/

                                } catch (JSONException e) {
                                }
                            }
                        }
                    } catch (JSONException e) {
                    } catch (Exception e) {
                    }
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return requestObject.toString();
    }


    public static boolean writeDataIntoFile(Context context, String meshRootClass, boolean isSavingInDownloads) {

        final String content = meshRootClass;
        Utils.DEBUG("Filtered Json Transfer>>>>> : " + content);


        File dir = null;
        if (isSavingInDownloads) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "BlueNRG_Mesh");
        } else {
            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "BlueNrgCache");
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }


        File file = null;

        if (isSavingInDownloads) {
            //File oldFile = new File(dir.getAbsolutePath() + "bluenrg-mesh_configuration.json");
            File oldFile = new File(dir.getAbsolutePath() + context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
            if (oldFile.exists()) {
                oldFile.delete();
            }

            file = new File(dir, context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
        } else {
            file = new File(dir, context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(content.getBytes());
            } finally {
                stream.close();
            }
            Utils.DEBUG(" >> Writing >> pathhh : " + file.getAbsolutePath());

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }


        if (!file.exists()) {
            Utils.DEBUG(" >> file path doesnt exist : " + file.getAbsolutePath());
        }
        return false;
    }

    public static void saveUniversalFilterDataToFile(final Context context) {

        final String meshStringData = filterJsonObject(context);
        Utils.DEBUG("Json Object Exported : " + meshStringData);

        //Utils.DEBUG(" >> Data Provided for cloud : " + ParseManager.getInstance().toJSON(meshRootClass));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                writeDataIntoFile(context, meshStringData, false);
            }
        }, 400);

    }


    public static boolean syncJson(Context context, boolean isFromAssests) throws IOException {

        if (isFromAssests) {
            //READ LOCAL ASSETS
            Utils.DEBUG("-----------------READING BLUE-NRG MESH JSON FROM ASSET----------------");
            MeshRootClass meshRootClass = null;
            try {

                //String fileName = "MeshRootClass.txt";
                String fileName = "MeshRootClass.txt";
                meshRootClass = ParseManager.getInstance().fromJSON(new JSONObject(Utils.readFromAssets(context, fileName)), MeshRootClass.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            buildData(context, meshRootClass);
            return true;
        } else {
            //MAIL JSON TRANSFER
            Utils.DEBUG("-----------------READING BLUE-NRG MESH JSON FROM MAIL----------------");
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = null;
            boolean deleteOldDownload = false;
            File downloaded_File = new File(dir + "/" + context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
            if (downloaded_File.exists()) {
                deleteOldDownload = true;
                file = new File(dir, context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
            } else {
                deleteOldDownload = false;
                dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "BlueNRG_Mesh");
                if (dir.exists()) {
                    file = new File(dir + "/" + context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
                }

            }

            try {
                if (file.exists() && context.getResources().getBoolean(R.bool.bool_readFromExternalMemory)) {
                    Utils.DEBUG(" >> File is present : " + file.exists());
                    int length = (int) file.length();
                    Utils.DEBUG(">> Length file :  " + file.length());
                    byte[] bytes = new byte[length];

                    FileInputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        in.read(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        in.close();
                    }

                    String contents = new String(bytes);
                    Utils.DEBUG(" >> File data : " + contents);

                    //store content to other folder in downloads
                    //File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "BlueNrgCache");
                    Utils.DEBUG("-----------------WRITING BLUE-NRG MESH MAIL JSON TO TEMP FOLDER----------------");
                    boolean isFileBackedUp = writeDataIntoFile(context, contents, true);

                    if (contents != null && isFileBackedUp) {
                        try {
                            if (deleteOldDownload) {
                                file.delete();
                                Utils.DEBUG("File deleted.");
                            }

                            //String data = contents.replace("/", "");
                            String data = contents.replace("\n", "").replace("\r", "");

                            MeshRootClass updatedMeshRootDownloaded = updateTransferedDataAfterImport(context, data, false);
                            Utils.DEBUG("-----------------UPDATED BLUE-NRG MESH MAIL JSON AS PER REQUIREMENT----------------");
                            if (updatedMeshRootDownloaded != null) {
                                String s = ParseManager.getInstance().toJSON(updatedMeshRootDownloaded);
                                Utils.DEBUG(" >> JSON Data Invited : " + s);
                                Utils.setBLEMeshDataToLocal(context, s);
                                Utils.DEBUG("-----------------SAVED BLUE-NRG MESH MAIL JSON TO SHARED-PREF----------------");
                                return true;
                            }
                        } catch (SecurityException e) {
                            Utils.DEBUG("SecurityException : failed to delete file.");
                        }
                    }
                }
            } catch (Exception e) {
                Utils.DEBUG("Exception in Utils : syncJson : " + e);
            }
            return false;
        }
    }

    private static MeshRootClass updateTransferedDataAfterImport(Context context, String contents, boolean isFromCloud) {

        MeshRootClass meshRootDownloaded = new MeshRootClass();
        try {
            meshRootDownloaded = ParseManager.getInstance().fromJSON(new JSONObject(contents), MeshRootClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        meshRootDownloaded.setIVindex(0);

        //remove eatra repeated nodes if coming from cloud
        //this is an error from cloud
        if (isFromCloud) {
            ArrayList<Nodes> nodesList = new ArrayList<>();
            if (meshRootDownloaded != null && meshRootDownloaded.getNodes() != null && meshRootDownloaded.getNodes().size() > 0) {
                for (int i = 0; i < meshRootDownloaded.getNodes().size(); i++) {
                    boolean isAddedAlready = false;
                    for (int j = 0; j < nodesList.size(); j++) {
                        if (meshRootDownloaded.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(nodesList.get(j).getElements().get(0).getUnicastAddress())) {
                            isAddedAlready = true;
                            break;
                        }
                    }

                    if (!isAddedAlready) {
                        nodesList.add(meshRootDownloaded.getNodes().get(i));
                    }
                }

                Utils.DEBUG(">> Removed Extra Nodes Of Cloud");
                meshRootDownloaded.getNodes().clear();
                meshRootDownloaded.setNodes(nodesList);
            }
        }


        //set provisioner section
        MeshRootClass meshRootClass = Utils.addProvisioner(context, meshRootDownloaded, false);
        //setNodeNumbering
        int count = 1;
        for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
            boolean isNodeIsProvisioner = false;
            for (int j = 0; j < meshRootClass.getProvisioners().size(); j++) {
                if (meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(meshRootClass.getProvisioners().get(j).getUUID())) {
                    isNodeIsProvisioner = true;
                    break;
                }
            }

            //assigning appkey name in node appkey object
            if (meshRootClass.getAppKeys() != null && meshRootClass.getAppKeys().size() > 0) {
                for (int j = 0; j < meshRootClass.getAppKeys().size(); j++) {
                    if (meshRootClass.getNodes().get(i).getAppKeys() != null && meshRootClass.getNodes().get(i).getAppKeys().size() > 0) {
                        for (int k = 0; k < meshRootClass.getNodes().get(i).getAppKeys().size(); k++) {
                            if (meshRootClass.getAppKeys().get(j).getIndex() == meshRootClass.getNodes().get(i).getAppKeys().get(k).getIndex()) {
                                meshRootClass.getNodes().get(i).getAppKeys().get(k).setName(meshRootClass.getAppKeys().get(j).getName());
                                meshRootClass.getNodes().get(i).getAppKeys().get(k).setKey(meshRootClass.getAppKeys().get(j).getKey());
                            }
                        }
                    }
                }
            }


            if (!isNodeIsProvisioner) {
                meshRootClass.getNodes().get(i).setNodeNumber(count);
                //convert all hex unicast address to int
                for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    String unicastAddress = meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress();
                    boolean stringIsHex = isStringIsHex(unicastAddress);
                    if (stringIsHex) {
                        int intUnicast = convertHexToInt(unicastAddress);
                        meshRootClass.getNodes().get(i).getElements().get(j).setUnicastAddress(String.valueOf(intUnicast));
                    }

                    meshRootClass.getNodes().get(i).getElements().get(j).setParentNodeName(meshRootClass.getNodes().get(i).getName());
                    meshRootClass.getNodes().get(i).getElements().get(j).setParentNodeAddress(meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress());

                    //update model name if empty
                    if (meshRootClass.getNodes().get(i).getElements().get(j).getModels() != null
                            && meshRootClass.getNodes().get(i).getElements().get(j).getModels().size() > 0) {
                        for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                            Model model = meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k);
                            if (meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelName() == null
                                    || meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelName().isEmpty()) {
                                String modelName = getModelNemeById(context, model).replace("_", " ");
                                meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(modelName != null ? modelName : " ");
                            }
                        }
                    }

                }
                count++;
            }
        }

        return meshRootClass;
    }

    private static String getModelNemeById(Context context, Model model) {

        String name = null;
        if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_0000_CONFIGURATION_SERVER))) {
            //configuration server
            name = context.getString(R.string.MODEL_SUB_NAME_CONFIGURATION_SERVER);
        }
        if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_0001_CONFIGURATION_CLIENT))) {
            //configuration server
            name = context.getString(R.string.MODEL_SUB_NAME_CONFIGURATION_CLIENT);
        }
        if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_0002_HEALTH_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_HEALTH_SERVER);
        }
        if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_0003_HEALTH_CLIENT))) {
            name = context.getString(R.string.MODEL_SUB_NAME_HEALTH_CLIENT);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1000_GENERIC_ONOFF))) {
            name = context.getString(R.string.MODEL_SUB_NAME_GENERIC_ONOFF);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1002_GENERIC_LEVEL))) {
            name = context.getString(R.string.MODEL_SUB_NAME_GENERIC_LEVEL);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1004_GENERIC_DEFAULT_TRANSITION_TIME))) {
            name = context.getString(R.string.MODEL_SUB_NAME_GENERIC_DEFAULT_TRANSITION_TIME);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1006_GENERIC_POWER_ONOFF))) {
            name = context.getString(R.string.MODEL_SUB_NAME_GENERIC_POWER_ON_OFF_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1007_GENERIC_POWER_SETUP_ONOFF))) {
            name = context.getString(R.string.MODEL_SUB_NAME_GENERIC_POWER_ON_OFF_SETUP_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1300_LIGHT_LIGHTNESS_MODEL))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_LIGHTNESS);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1301_LIGHT_LIGHTNESS_SETUP_MODEL))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_LIGHTNESS_SETUP);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1303_LIGHT_CTL_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_CTL_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1304_LIGHT_CTL_SETUP_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_CTL_SETUP_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1306_LIGHT_CTL_TEMPERATURE_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_CTL_TEMPERATURE_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1307_LIGHT_HSL_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_HSL_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1308_LIGHT_HSL_SETUP_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_HSL_SETUP_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_130a_LIGHT_HSL_HUE_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_HSL_HUE_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_130b_LIGHT_HSL_SATURATION_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_LIGHT_HSL_SATURATION_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1100_SENSOR_MODEL))) {
            name = context.getString(R.string.MODEL_SUB_NAME_SENSOR_MODEL_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_1101_SENSOR_SETUP_SERVER))) {
            name = context.getString(R.string.MODEL_SUB_NAME_SENSOR_SETUP_SERVER);
        } else if (model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_00300001_VENDOR_MODEL))
                || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_00010030_VENDOR_MODEL))
                || model.getModelId().equalsIgnoreCase(context.getString(R.string.MODEL_ID_10030_VENDOR_MODEL))) {
            name = context.getString(R.string.MODEL_SUB_NAME_ST_VENDOR_SERVER_MODEL);
        } else {
            name = context.getString(R.string.MODEL_SUB_NAME_CONFIGURATION_SERVER);
        }

        return name;
    }

    public static boolean isStringIsHex(String n) {
        boolean ret;
        try {
            int t = Integer.parseInt(n, 16);
            ret = true;
        } catch (NumberFormatException e) {
            ret = false;
        }
        return (ret);
    }

    public static MeshRootClass addProvisioner(Context context, MeshRootClass meshRootClassCloudData, boolean isFromCloud) {
        //provisioner
        Provisioner currentProvisner = new Provisioner();
        try {
            currentProvisner = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getProvisioner(context)), Provisioner.class);
        } catch (JSONException e) {
        }
        try {
            boolean isNewProvsioner = true;
            if (meshRootClassCloudData != null && meshRootClassCloudData.getProvisioners() != null) {
                for (int i = 0; i < meshRootClassCloudData.getProvisioners().size(); i++) {
                    if (meshRootClassCloudData.getProvisioners().get(i).getUUID().equals(currentProvisner.getUUID())) {
                        isNewProvsioner = false;
                    }
                }
            }

            if (isNewProvsioner) {
                //if new provisioner
                if (meshRootClassCloudData.getNodes() != null && meshRootClassCloudData.getNodes().size() >= 0) {
                    if (meshRootClassCloudData.getProvisioners() != null && meshRootClassCloudData.getProvisioners().size() >= 0) {
                        //case : if provisioning is done already
                        //retreive last provisioner address range and update this new provisioner address ranges
                        Provisioner maxUnicastRangeProvisioner = getMaxUnicastProvisioner(context, meshRootClassCloudData, currentProvisner, isFromCloud);
                        currentProvisner.setAllocatedUnicastRange(maxUnicastRangeProvisioner.getAllocatedUnicastRange());
                        currentProvisner.setAllocatedGroupRange(maxUnicastRangeProvisioner.getAllocatedGroupRange());
                        currentProvisner.setUnicastAddress(maxUnicastRangeProvisioner.getAllocatedUnicastRange().get(0).getLowAddress());
                        meshRootClassCloudData.getProvisioners().add(currentProvisner);
                        //add this provisioner as a node in mesh
                        Nodes provisionerAsNode = getProvisionerAsNode(context, currentProvisner);
                        meshRootClassCloudData.getNodes().add(provisionerAsNode);
                        //update provisioner in shared preference
                    }

                } else if (meshRootClassCloudData.getNodes() == null) {
                    //case : first if provisioning is not done
                    //add this provisioner to mesh on first start
                    ArrayList<Provisioner> provisionerss = new ArrayList<>();
                    provisionerss.add(currentProvisner);
                    meshRootClassCloudData.setProvisioners(provisionerss);

                    //add this provisioner as a node in mesh
                    ArrayList<Nodes> nodes = new ArrayList<>();
                    Nodes provisionerAsNode = getProvisionerAsNode(context, currentProvisner);
                    nodes.add(provisionerAsNode);
                    meshRootClassCloudData.setNodes(nodes);
                }
            } else {
                //if provisoner already present : since server already added current provisioner data in mesh.
                //update its unicast range for high value : since low value is already updated from server
            }

            updateProvisionerLocals(context, currentProvisner);
            Utils.setProvisioner(context, ParseManager.getInstance().toJSON(currentProvisner));
        } catch (Exception e) {
        }

        return meshRootClassCloudData;
    }

    private static void updateProvisionerLocals(Context context, Provisioner currentProvisner) {

        MainActivity.provisionerUnicastLowAddress = Integer.parseInt(currentProvisner.getAllocatedUnicastRange().get(0).getLowAddress());
        MainActivity.provisionerUnicastHighAddress = Integer.parseInt(currentProvisner.getAllocatedUnicastRange().get(0).getHighAddress());
        MainActivity.provisionerGroupLowAddress = Integer.parseInt(currentProvisner.getAllocatedGroupRange().get(0).getLowAddress());
        MainActivity.provisionerGroupHighAddress = Integer.parseInt(currentProvisner.getAllocatedGroupRange().get(0).getHighAddress());
        Utils.setProvisionerUnicastLowAddress(context, String.valueOf(MainActivity.provisionerUnicastLowAddress));
        Utils.setProvisionerUnicastHighAddress(context, String.valueOf(MainActivity.provisionerUnicastHighAddress));
        Utils.setProvisionerGroupLowAddress(context, String.valueOf(MainActivity.provisionerGroupLowAddress));
        Utils.setProvisionerGroupHighAddress(context, String.valueOf(MainActivity.provisionerGroupHighAddress));

        int unicastRange = MainActivity.provisionerUnicastHighAddress - MainActivity.provisionerUnicastHighAddress;
        Utils.setProvisionerUnicastRange(context, String.valueOf(unicastRange));

    }

    private static Provisioner getMaximumUnicasProvisioner(Context context, MeshRootClass meshRootClassCloudData, Provisioner currentProvisner) {

        Provisioner provisioner = new Provisioner();
        provisioner = currentProvisner;
        int currentProv_UnicasHighValue = Integer.parseInt(provisioner.getAllocatedUnicastRange().get(0).getHighAddress());
        for (int i = 0; i < meshRootClassCloudData.getProvisioners().size(); i++) {

            /*
             * Issue 1 : if highAddres is in hex format convert to digit
             * Issue 2 : if lowAddres is in hex format convert to digit
             *
             * Note :
             * server value is not coming correct for any unicast value.
             * */

            String unicastHighAddr = meshRootClassCloudData.getProvisioners().get(i).getAllocatedUnicastRange().get(0).getHighAddress();
            String unicastLowAddr = meshRootClassCloudData.getProvisioners().get(i).getAllocatedUnicastRange().get(0).getLowAddress();

            //Case for unicast range
            if (unicastHighAddr.contains("C") || unicastHighAddr.contains("c")
                    || unicastHighAddr.contains("B") || unicastHighAddr.contains("b")) {

                //setting up default range of highUnicastValue if highUnicast value id incorrect (i.e contains c, b, C,B in the data from cloud)
                currentProv_UnicasHighValue = Integer.parseInt(meshRootClassCloudData.getProvisioners().get(i)
                        .getAllocatedUnicastRange().get(0).getLowAddress()) + Integer.parseInt(Utils.getProvisionerUnicastRange(context));
                if (currentProv_UnicasHighValue > Integer.parseInt(provisioner.getAllocatedUnicastRange().get(0).getHighAddress())) {
                    provisioner = meshRootClassCloudData.getProvisioners().get(i);
                }
            } else {
                if (Integer.parseInt(unicastHighAddr) > Integer.parseInt(provisioner.getAllocatedUnicastRange().get(0).getHighAddress())) {
                    //currentProv_UnicasHighValue = Integer.parseInt(unicastHighAddr);
                    provisioner = meshRootClassCloudData.getProvisioners().get(i);
                }
            }

            if (unicastLowAddr.contains("C") || unicastLowAddr.contains("c")
                    || unicastLowAddr.contains("B") || unicastLowAddr.contains("b")) {
                int currentProv_unicastLowAddr = Integer.parseInt(meshRootClassCloudData.getProvisioners().get(i)
                        .getAllocatedUnicastRange().get(0).getHighAddress()) - Integer.parseInt(Utils.getProvisionerUnicastRange(context));
                //provisioner = meshRootClassCloudData.getProvisioners().get(i);
                provisioner.getAllocatedUnicastRange().get(0).setLowAddress(String.valueOf(currentProv_unicastLowAddr));
            }

        }

        return provisioner;
    }

    private static Nodes getProvisionerAsNode(Context context, Provisioner currentProvisner) {


        //int nodeNumber = Utils.getNextNodeNumber(meshRootClassCloudData.getNodes(), meshRootClassCloudData);
        Nodes node = new Nodes(0);
        ArrayList<Element> elements = new ArrayList<>();
        Element element = new Element();
        //element.setUnicastAddress(currentProvisner.getUnicastAddress());
        element.setUnicastAddress(Utils.getProvisionerUnicastLowAddress(context));
        elements.add(element);
        node.setElements(elements);
        node.setUUID(currentProvisner.getUUID().toString());
        node.setName(currentProvisner.getProvisionerName());
        node.setSecurity("high");
        node.setUnicastAddress(Utils.getProvisionerUnicastLowAddress(context));
        node.setDefaultTTL(3);
       // Log.e("Dinesh==>Utils 3282  ",currentProvisner.getProvisionerName());
        node.setBlacklisted(false);
        node.setConfigComplete(false);
        //node.setConfigured("false");

        return node;
    }

    private static ArrayList<AllocatedGroupRange> getAllocatedGroupData(Context context, MeshRootClass meshRootClassCloudData) {

        ArrayList<AllocatedGroupRange> allocatedGroupRange = meshRootClassCloudData.getProvisioners().get(meshRootClassCloudData.getProvisioners().size() - 1).getAllocatedGroupRange();
        String lowGrpAddress = allocatedGroupRange.get(0).getLowAddress();
        String highGrpAddress = allocatedGroupRange.get(0).getHighAddress();
        int grpAddressRange = Integer.parseInt(highGrpAddress) - Integer.parseInt(lowGrpAddress);

        ArrayList<AllocatedGroupRange> allocatedGrpRangeNewProvisioner = new ArrayList<>();
        int lowGrpAdd = Integer.parseInt(highGrpAddress) + 1;
        int highGrpAdd = grpAddressRange + lowGrpAdd;
        AllocatedGroupRange allocatedGroupRange1 = new AllocatedGroupRange();
        allocatedGroupRange1.setLowAddress(String.valueOf(lowGrpAdd));
        allocatedGroupRange1.setHighAddress(String.valueOf(highGrpAdd));
        allocatedGrpRangeNewProvisioner.add(allocatedGroupRange1);

        return allocatedGrpRangeNewProvisioner;
    }

    private static Provisioner getMaxUnicastProvisioner(Context context, MeshRootClass meshRootClassCloudData, Provisioner currentProvisner, boolean isFromCloud) {


        //This provisioner is the one with maximum unicast data
        Provisioner provisioner = getMaximumUnicasProvisioner(context, meshRootClassCloudData, currentProvisner);
        ArrayList<AllocatedUnicastRange> allocatedUnicastRange = provisioner.getAllocatedUnicastRange();
        ArrayList<AllocatedGroupRange> allocatedGroupRange = provisioner.getAllocatedGroupRange();
        ArrayList<AllocatedUnicastRange> newAllocatedUnicastRange = new ArrayList<>();
        ArrayList<AllocatedGroupRange> newAllocatedGroupRange = new ArrayList<>();

        if (!isFromCloud) {
            //since the server already updated the unicast data in json
            //but if the data sent from email it should be updated manually;

            //setting unicast range
            try {
                String lowAddress = allocatedUnicastRange.get(0).getLowAddress();
                String highAddress = allocatedUnicastRange.get(0).getHighAddress();
                int unicastAddressRange = Integer.parseInt(highAddress) - Integer.parseInt(lowAddress);
                int currentUserLowUnicastAddress = Integer.parseInt(highAddress) + 1;
                int currentUserHighUnicastAddress = unicastAddressRange + currentUserLowUnicastAddress;
                AllocatedUnicastRange allocatedUnicastRange1 = new AllocatedUnicastRange();
                allocatedUnicastRange1.setLowAddress(String.valueOf(currentUserLowUnicastAddress));
                allocatedUnicastRange1.setHighAddress(String.valueOf(currentUserHighUnicastAddress));
                newAllocatedUnicastRange.add(allocatedUnicastRange1);
                provisioner.setAllocatedUnicastRange(newAllocatedUnicastRange);
            } catch (Exception e) {
                Utils.DEBUG("Method Error : getMaxUnicastProvisioner : unicast range");
            }

            //setting unicast group range
            try {
                String lowGrpAddress = allocatedGroupRange.get(0).getLowAddress();
                String highGrpAddress = allocatedGroupRange.get(0).getHighAddress();
                int unicastGrpAddressRange = Integer.parseInt(highGrpAddress) - Integer.parseInt(lowGrpAddress);
                int currentUserLowGrpAddress = Integer.parseInt(highGrpAddress) + 1;
                int currentUserHighGrpAddress = unicastGrpAddressRange + currentUserLowGrpAddress;
                AllocatedGroupRange allocatedGrpUnicastRange1 = new AllocatedGroupRange();
                allocatedGrpUnicastRange1.setLowAddress(String.valueOf(currentUserLowGrpAddress));
                allocatedGrpUnicastRange1.setHighAddress(String.valueOf(currentUserHighGrpAddress));
                newAllocatedGroupRange.add(allocatedGrpUnicastRange1);
                provisioner.setAllocatedGroupRange(newAllocatedGroupRange);
            } catch (Exception e) {
                Utils.DEBUG("Method Error : getMaxUnicastProvisioner : group range");
            }
        }

        //return isFromCloud ? allocatedUnicastRange : new_AllocatedRange;
        return provisioner;
    }


    public static void showPopUp(final Context context, final String className, boolean showPopup, final boolean isDeleteMesh, String msg1, String msg2) {
        if (context == null)
            return;

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_deletedata);
        Button butOk = (Button) dialog.findViewById(R.id.butOk);
        Button butCancel = (Button) dialog.findViewById(R.id.butCancel);
        TextView txtMsgOne = (TextView) dialog.findViewById(R.id.txtMsgOne);
        final TextView txtMsgTwo = (TextView) dialog.findViewById(R.id.txtMsgTwo);
        txtMsgOne.setText(msg1);
        txtMsgTwo.setText(msg2);
        final LinearLayout butLayout = (LinearLayout) dialog.findViewById(R.id.butLayout);
        final LinearLayout layoutMeshName = (LinearLayout) dialog.findViewById(R.id.layoutMeshName);
        final ImageView butBack = (ImageView) dialog.findViewById(R.id.butBack);
        Button butGo = (Button) dialog.findViewById(R.id.butGo);
        final EditText editMeshName = (EditText) dialog.findViewById(R.id.editMeshName);

        final LinearLayout butImportLayout = (LinearLayout) dialog.findViewById(R.id.butImportLayout);
        Button butMail = (Button) dialog.findViewById(R.id.butMail);
        Button butCloud = (Button) dialog.findViewById(R.id.butCloud);

        if (Utils.isUserLoggedIn(context)) {
            dialog.setCancelable(false);
        } else {
            //dialog.setCancelable(true);
        }
        MeshRootClass meshRootClass = null;
        try {
            meshRootClass = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getBLEMeshDataFromLocal(context)), MeshRootClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final MeshRootClass meshRootClasss = meshRootClass;

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                dialog.dismiss();
            }
        });

        if (showPopup) {
            dialog.show();
        } else {
            dialog.dismiss();
        }

        if (className.equalsIgnoreCase(new MainActivity().getClass().getName())) {
            if (isDeleteMesh) {
                butCancel.setText(context.getResources().getString(R.string.str_cancel_label));
                butOk.setText(context.getResources().getString(R.string.str_ok_label));
                butLayout.setVisibility(View.VISIBLE);
                txtMsgTwo.setVisibility(View.VISIBLE);
                layoutMeshName.setVisibility(View.GONE);
                butImportLayout.setVisibility(View.GONE);
            } else {
                butCancel.setText(context.getResources().getString(R.string.str_import_label));
                butOk.setText(context.getResources().getString(R.string.str_create_network_label));
                butLayout.setVisibility(View.VISIBLE);
                txtMsgTwo.setVisibility(View.VISIBLE);
                butImportLayout.setVisibility(View.GONE);
                layoutMeshName.setVisibility(View.GONE);
            }
        } else if (className.equalsIgnoreCase(new LoginDetailsFragment().getClass().getName())) {
            butCancel.setText(context.getResources().getString(R.string.str_cancel_label));
            butOk.setText(context.getResources().getString(R.string.str_ok_label));
            butLayout.setVisibility(View.VISIBLE);
            txtMsgTwo.setVisibility(View.VISIBLE);
            butCancel.setVisibility(View.GONE);
            butOk.setText(context.getResources().getString(R.string.str_tryagain_label));
            layoutMeshName.setVisibility(View.GONE);
            butImportLayout.setVisibility(View.GONE);
        }


        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (className.equalsIgnoreCase(new MainActivity().getClass().getName())) {
                    if (!isDeleteMesh) {
                        //import previous settings
                        if (Utils.isUserLoggedIn(context)) {
                            butImportLayout.setVisibility(butImportLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        } else {
                            ((MainActivity) context).isFromCreateNetwork = true;
                            Utils.moveToFragment(((MainActivity) context), new LoginDetailsFragment(), null, 0);
                            dialog.dismiss();
                        }

                    } else {
                        dialog.dismiss();
                    }
                }

            }
        });

        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do operation
                if (className.equalsIgnoreCase(new MainActivity().getClass().getName())) {
                    if (!isDeleteMesh) {

                        //if(Utils.isUserLoggedIn(context))
                        {
                            layoutMeshName.setVisibility(View.VISIBLE);
                            butLayout.setVisibility(View.GONE);
                            txtMsgTwo.setVisibility(View.GONE);
                            butImportLayout.setVisibility(View.GONE);
                        }
                        /*else
                        {
                            ((MainActivity)context).isFromCreateNetwork = true;
                            Utils.moveToFragment(((MainActivity)context), new LoginDetailsFragment(), null, 0);
                            dialog.dismiss();
                        }*/


                    } else {
                        clearAllData(context);
                        dialog.dismiss();
                    }
                } else if (className.equalsIgnoreCase(new LoginDetailsFragment().getClass().getName())) {
                    dialog.dismiss();
                }

            }
        });

        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDeleteMesh) {
                    layoutMeshName.setVisibility(View.GONE);
                    butLayout.setVisibility(View.VISIBLE);
                    txtMsgTwo.setVisibility(View.VISIBLE);
                } else {

                }
            }
        });

        butGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (className.equalsIgnoreCase(new MainActivity().getClass().getName())) {
                    if (!isDeleteMesh) {
                        //create new network
                        if (editMeshName.length() > 2) {
                            dialog.dismiss();
                            ((MainActivity) context).loader.show();

                            ((MainActivity) context).createNewNetwork(editMeshName.getText().toString(), null, false);

                            /*if(Utils.isUserLoggedIn(context))
                            {
                                ((MainActivity) context).createNewNetwork(editMeshName.getText().toString(),null, false);
                            }
                            else
                            {
                                Utils.moveToFragment(((MainActivity)context), new LoginDetailsFragment(), null, 0);
                            }*/

                        } else {
                            Utils.showToast(context, "Mention valid mesh name.");
                        }
                    }
                } /*else if (className.equalsIgnoreCase(new CloudInteractionFragment().getClass().getName())) {
                    dialog.dismiss();
                    ((MainActivity) context).loader.show();
                    if (Utils.isUserLoggedIn(context)) {
                        ((MainActivity) context).createNewNetwork(editMeshName.getText().toString(), null, false);
                    } else {
                        Utils.moveToFragment(((MainActivity) context), new LoginDetailsFragment(), null, 0);
                    }
                }*/

            }
        });

        butMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                action_config_setting(context);
            }
        });

        butCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (Utils.getUserLoginKey(context) == null) {
                    ((MainActivity) context).isFromCreateNetwork = true;
                    Utils.moveToFragment(((MainActivity) context), new LoginDetailsFragment(), null, 0);
                } else {
                    //Utils.moveToFragment(((MainActivity) context), new CloudInteractionFragment(), null, 0);
                }
            }
        });
    }

    public static void clearAllData(Context context) {

        try {

            Utils.setBLEMeshDataToLocal(context, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isDeviceAlreadyAdded(final Context context, final UnprovisionedRecyclerAdapter unprovisionedRecyclerAdapter, RecyclerView recyclerView, String mAddr) {

        boolean isPresent = false;

        try {
            for (int i = 0; i < unprovisionedRecyclerAdapter.getItemCount(); i++) {

                RelativeLayout v = (RelativeLayout) recyclerView.getLayoutManager().findViewByPosition(i);
                //CardView cardView = (CardView) v.getChildAt(0);
                //RelativeLayout relativeLay1 = (RelativeLayout) cardView.getChildAt(0);
                RelativeLayout relativeLay1 = (RelativeLayout) v.getChildAt(0);
                RelativeLayout relativeLay2 = (RelativeLayout) relativeLay1.getChildAt(1);
                LinearLayout linearLayout = (LinearLayout) relativeLay2.getChildAt(0);

                RelativeLayout relativeLayAddress = (RelativeLayout) linearLayout.getChildAt(0);
                TextView txtAddress = (TextView) relativeLayAddress.getChildAt(1);
                if (txtAddress.getText().toString().equals(mAddr)) {
                    isPresent = true;
                    break;
                }

            }
        } catch (Exception e) {
        }

        return isPresent;

    }

    public static int getRowAdapterPosition(final Context context, final UnprovisionedRecyclerAdapter unprovisionedRecyclerAdapter, RecyclerView recyclerView, String bt_addr, int mRssi) {

        int position = 0;
        final String rssiData = String.valueOf(mRssi);
        if (unprovisionedRecyclerAdapter.getItemCount() > 0) {
            try {

                for (int i = 0; i < unprovisionedRecyclerAdapter.getItemCount(); i++) {
                    RelativeLayout v = (RelativeLayout) recyclerView.getLayoutManager().findViewByPosition(i);
                    if (v != null) {
                        /*CardView cardView = (CardView) v.getChildAt(0);
                        RelativeLayout relativeLay1 = (RelativeLayout) cardView.getChildAt(0);*/
                        RelativeLayout relativeLay1 = (RelativeLayout) v.getChildAt(0);
                        RelativeLayout relativeLay2 = (RelativeLayout) relativeLay1.getChildAt(1);
                        LinearLayout linearLayout = (LinearLayout) relativeLay2.getChildAt(0);

                        RelativeLayout relativeLayAddress = (RelativeLayout) linearLayout.getChildAt(0);
                        TextView txtAddress = (TextView) relativeLayAddress.getChildAt(1);
                        String[] array = txtAddress.getText().toString().split("\n");
                        try {
                            //Utils.DEBUG(">> MAC ADDRESS : " + array[0]);
                            //Utils.DEBUG(">> Bits : " + array[1]);
                        } catch (Exception e) {
                        }

                        if (bt_addr.equalsIgnoreCase(txtAddress.getTag().toString())) {
                            //RelativeLayout relativeLayRssi = (RelativeLayout) linearLayout.getChildAt(1);
                            LinearLayout relativeLayRssi = (LinearLayout) linearLayout.getChildAt(1);
                            final TextView txtRssi = (TextView) relativeLayRssi.getChildAt(1);
                            position = i;
                        /*((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                unprovisionedRecyclerAdapter.notifyItemChanged(position, rssiData);
                                //txtRssi.setText("" + rssiData);
                                //txtRssi.setTextColor(context.getResources().getColor(R.color.st_black));
                            }
                        });*/

                            break;
                        }
                    }

                }
            } catch (NullPointerException e) {
            } catch (Exception e) {
            }
        }
        return position;

    }


    public static boolean getHelpGuideData(Context context) {
        if (context == null)
            return false;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (boolean) sp.get(context.getString(R.string.key_user_help_data));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void setHelpGuideData(Context context, boolean is_help_guideon) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put(context.getString(R.string.key_user_help_data), is_help_guideon);

    }

    /*
     * Login Credentials
     * */
    public static String getLoginData(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_user_login_data));
    }

    public static void setLoginData(Context context, String loginData) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_user_login_data), loginData);

    }

    /*
     * Login Credentials
     * */
    public static boolean isRestart(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("Restart") == null) {
            return false;
        } else {
            return (boolean) sp.get("Restart");
        }

    }

    public static void setRestart(Context context, boolean res) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put("Restart", res);

    }

    /*
     * Intro Screen
     * */
    public static boolean isShowIntroScreen(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("Started") == null) {
            return true;
        } else {
            return (boolean) sp.get("Started");
        }
    }

    public static void setShowIntroScreen(Context context, boolean res) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put("Started", res);

    }

    /*
     * Intro Screen
     * */
    public static boolean isAppInit(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("AppInit") == null) {
            return false;
        } else {
            return (boolean) sp.get("AppInit");
        }

    }

    public static void setAppInit(Context context, boolean res) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put("AppInit", res);

    }

    /*
     * Set Auto Provisioning
     * */
    public static boolean isAutoProvisioning(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("OneStepProvisionig") == null) {
            return false;
        } else {
            return (boolean) sp.get("OneStepProvisionig");
        }

    }

    public static void setAutoProvisioning(Context context, boolean res) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put("OneStepProvisionig", res);

    }

    /*
     * Login Credentials
     * */
    public static boolean isFwUpgrade(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        if (sp.get("FwUpgrade") == null) {
            return false;

        } else {
            return (boolean) sp.get("FwUpgrade");
        }

    }

    public static void setFwUpgrade(Context context, boolean res) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put("FwUpgrade", res);

    }

    public static boolean isUnicastAddress(int value) {
        return (value < 0xC000);
    }

    /**
     * used to get login status of user
     *
     * @param context
     * @return
     */
    public static boolean isUserLoggedIn(Context context) {
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        Object object = sp.get(context.getString(R.string.key_user_login_data));

        return (object == null || object.equals("") ? false : true);
    }

    /*
     * User Key
     * */
    public static String getUserLoginKey(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_user_userkey_data));
    }

    public static void setUserLoginKey(Context context, String userkey) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_user_userkey_data), userkey);

    }

    /*
     * User Key
     * */
    public static String getPreviousUserLoginKey(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_previous_userkey_data));
    }

    public static void setPreviousUserLoginKey(Context context, String userkey) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_previous_userkey_data), userkey);

    }

    /*
     * UUID
     * */
    public static String getProvisionerUUID(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_uuid_data));
    }

    public static void setProvisionerUUID(Context context, String userkey) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_uuid_data), userkey);

    }

    public static boolean isProviosnerUUIDSet(Context context) {
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        Object object = sp.get(context.getString(R.string.key_uuid_data));

        return (object == null || object.equals("") ? false : true);
    }

    /*
     * Provisioner
     * */
    public static String getProvisioner(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_data));
    }

    public static void setProvisioner(Context context, String Provisioner) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_data), Provisioner);

    }


    /*
     * Sensor Data
     * */
    public static String getSensorData(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_sensor_data));
    }

    public static void setSensorData(Context context, String sensorData) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_sensor_data), sensorData);

    }


    /*
     * Cloud Data
     * */
    public static String getCloudData(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_cloud_data));
    }

    public static void setCloudData(Context context, String cloudData) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_cloud_data), cloudData);

    }

    /*
     * Current User Net Key Data
     * */
    public static String getNetKey(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_current_netkey));
    }

    public static void setNetKey(Context context, String userkey) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_current_netkey), userkey);

    }

    /*
     * Current User App Key Data
     * */
    public static Integer getDefaultAppKeyIndex(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (Integer) sp.get(context.getString(R.string.key_current_appkey));
    }

    public static void setDefaultAppKeyIndex(Context context, Integer userkey) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_current_appkey), userkey);

    }

    /*
     * Current User Registration Status
     * */

    public static boolean isUserRegisteredToDownloadJson(Context context) {
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        Object object = sp.get(context.getString(R.string.key_isuser_register));

        if (object == null || object.equals("")) {
            return false;
        } else {
            if (((String) sp.get(context.getString(R.string.key_isuser_register))).equals("true")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void setUserRegisteredToDownloadJson(Context context, String isUserRegisteredToDownload) {

        if (context == null)
            return;
        String setFlag = "false";
        if (isUserRegisteredToDownload.length() > 0) {
            setFlag = isUserRegisteredToDownload;
        }
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_isuser_register), setFlag);

    }

    public static String getSubscriptionGroupAddressOnProvsioning(Context context) {
        if (context == null)
            return "";
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_subscription_prov_data));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setSubscriptionAddressOnProvsioning(Context context, String subscription_data) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put(context.getString(R.string.key_subscription_prov_data), subscription_data);

    }


    public static String getPublicationAddressOnProvsioning(Context context) {
        if (context == null)
            return "";
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_publication_prov_data));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setPublicationAddressOnProvsioning(Context context, String publication_data) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put(context.getString(R.string.key_publication_prov_data), publication_data);

    }

    /*
     * key_setting_type
     * */
    public static String getSettingType(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_setting_type));
    }

    public static void setSettingType(Context context, String settingStr) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_setting_type), settingStr);

    }


    /**
     * Setter and getter for model selected
     *
     * @param context
     * @param model
     */
    public static void setSelectedModel(Context context, String model) {
        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_modelselected), model);
    }

    public static String getSelectedModel(Context context) {

        if (context == null)
            return "";
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            String modelname = (String) sp.get(context.getString(R.string.key_modelselected));
            if (modelname != null) {
                return modelname;
            } else {
                return context.getString(R.string.str_genericmodel_label);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setReliable(Context context, boolean setReliable) {
        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_isReliable), setReliable);
    }

    public static boolean isReliableEnabled(Context context) {
        if (context == null)
            return false;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            Object obj = sp.get(context.getString(R.string.key_isReliable));
            if (obj != null) {
                return (boolean) sp.get(context.getString(R.string.key_isReliable));
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void setVendorModelCommand(Context context, boolean setReliable) {
        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_isVendorModelCommand), setReliable);
    }

    public static boolean isVendorModelCommand(Context context) {
        if (context == null)
            return false;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            //return (boolean) sp.get(context.getString(R.string.key_isVendorModelCommand));
            Object obj = sp.get(context.getString(R.string.key_isVendorModelCommand));
            if (obj != null) {
                return (boolean) sp.get(context.getString(R.string.key_isVendorModelCommand));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
     * Current provisionerName Key Data
     * */
    public static String getProvisionerName(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_name));
    }

    public static void setProvisionerName(Context context, String provisionerName) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_name), provisionerName);

    }

    /*
     * Current key_network_name Key Data
     * */
    public static String getNetworkName(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_network_name));
    }

    public static void setNetworkName(Context context, String networkName) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_network_name), networkName);

    }


    public static void setScreenIntroStatus(Context context, boolean setStatus) {
        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_isScreenIntro), setStatus);
    }

    public static boolean isScreenIntroDone(Context context) {
        if (context == null)
            return false;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            //return (boolean) sp.get(context.getString(R.string.key_isVendorModelCommand));
            Object obj = sp.get(context.getString(R.string.key_isScreenIntro));
            if (obj != null) {
                return (boolean) sp.get(context.getString(R.string.key_isScreenIntro));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }


    /*
     * User Key
     * */
    public static String getClassName(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_class_name));
    }

    public static void setClassName(Context context, String className) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_class_name), className);

    }

    /*
     * Capability type selected
     * */
    public static String getSelectedCapability(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_capability_type));
    }

    public static void setSelectedCapability(Context context, String capabilityName) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_capability_type), capabilityName);

    }

    public static void screenWidgetsIntro(Context context, int Id) {

        /*new MaterialTapTargetPrompt.Builder((MainActivity)context)
                .setTarget(Id)
                .setPrimaryText("Send your first email")
                .setSecondaryText("Tap the envelope to start composing your first email")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            // User has pressed the prompt target
                        }
                    }
                })
                .show();*/
    }

    public static void updateElementNameInJson(Context context, String eName, String elementUnicastAddress) {

        try {
            for (int i = 0; i < ((MainActivity) context).meshRootClass.getNodes().size(); i++) {
                for (int j = 0; j < ((MainActivity) context).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    if (Integer.parseInt(((MainActivity) context).meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress()) == Integer.parseInt(elementUnicastAddress)) {
                        ((MainActivity) context).meshRootClass.getNodes().get(i).getElements().get(j).setName(eName);
                    }
                }
            }

            String s = ParseManager.getInstance().toJSON(((MainActivity) context).meshRootClass);
            Utils.DEBUG(" >> Element Name Updated : " + eName);
            Utils.setBLEMeshDataToLocal(context, s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * used to decompress given gzip to string
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static byte[] decompress(byte[] str) throws IOException, UnsupportedEncodingException {
        if (str == null || str.length == 0) {
            return str;
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder outStr = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            outStr.append(line);
        }
        return new String(outStr).getBytes();
    }

    /**
     * Method used to clear fragment from back stack
     *
     * @param activity
     * @param fragName
     */
    public static void clearFragmentFromBackStack(FragmentActivity activity, String fragName) {

        if (activity == null)
            return;

        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();

        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            String fragmentName = (fm.getBackStackEntryAt(i)).getName();

            //Utils.DEBUG("Utils >> clearFragmentFromBackStack() >> " + fragmentName + " is at position in back stack : " + i);
            if (fragmentName.equals(fragName)) {
                fm.popBackStack();
                Utils.DEBUG("Utils >> clearFragmentFromBackStack() >> removed fragment : " + fragmentName);
                if (fragName.equals(new SignUpFragment().getClass().getName())
                        || fragName.equals(new LoginDetailsFragment().getClass().getName())) {
                    Utils.updateActionBarForFeatures(activity, new MainViewPagerFragment().getClass().getName());
                }
            }
        }
    }

    /**
     * Method used to check fragment present in stack or not.
     *
     * @param activity
     * @param fragName
     * @return
     */
    public static boolean isFragmentPresentInBackStack(FragmentActivity activity, String fragName) {

        if (activity == null)
            return false;

        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();

        boolean isPresent = false;
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            String fragmentName = (fm.getBackStackEntryAt(i)).getName();

            //Utils.DEBUG("Utils >> clearFragmentFromBackStack() >> " + fragmentName + " is at position in back stack : " + i);
            if (fragmentName.equals(fragName)) {
                isPresent = true;
                break;
            }
        }

        if (isPresent) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Methos used to generate mesh-UUID.
     *
     * @param context
     * @param networkKey
     */
    public static String generateMeshUUID(Context context, String networkKey) {

        if (networkKey == null)
            return null;

        StringBuilder strBuilderUUID = new StringBuilder();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(networkKey);
        strBuilder = strBuilder.reverse();
        String strUUID = null;

        //for (int i = strArray.length-1; i>=0; i--)

        char[] strArray = String.valueOf(strBuilder).toCharArray();
        for (int i = 0; i < strArray.length; i++) {
            if (i == 8 || i == 12 || i == 16 || i == 20) {
                strBuilderUUID.append("-");
            }
            strBuilderUUID.append(strArray[i]);
        }
        Utils.DEBUG("Mesh UUID : " + String.valueOf(strBuilderUUID));
        return String.valueOf(strBuilderUUID);
    }


    public static String onGetUUID(Context context, String networkKey) {
        if (networkKey == null)
            return null;

        StringBuilder strBuilderUUID = new StringBuilder();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(networkKey);
        //strBuilder = strBuilder.reverse();
        String strUUID = null;

        //for (int i = strArray.length-1; i>=0; i--)

        char[] strArray = String.valueOf(strBuilder).toCharArray();
        for (int i = 0; i < strArray.length; i++) {
            //if(i == 8 || i == 12 || i == 16 || i == 20)
            if (i == 16 || i == 20 || i == 24 || i == 28) {
                strBuilderUUID.append(" - ");
            }
            strBuilderUUID.append(strArray[i]);
        }
        Utils.DEBUG("Mesh UUID : " + String.valueOf(strBuilderUUID));
        return String.valueOf(strBuilderUUID);
    }

    private static Dialog dialogx;

    public static Dialog getDialogInstance(Context context) {

       /* if( dialog == null ) {
            dialog = new Dialog(context);
        }*/
        return dialogx;
    }

    public static void showPopUpForNewGroup(final Context context, int count, final mobleAddress addr, final String grpName) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_error);
        TextView txt = (TextView) dialog.findViewById(R.id.txtErrorMsg);
        Button but = (Button) dialog.findViewById(R.id.but);
        if (!dialog.isShowing()) {
            dialog.show();
        }

        txt.setText("-> Adding New Group <-" + "\n\n" +
                "Group Name : " + grpName + "\n" +
                "Group Address : " + addr.toString() + "\n");

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.addNewGroupToJson(context, addr, grpName); //remove if no elements are checked
                ((MainActivity) context).updateJsonData();
                ((MainActivity) context).fragmentCommunication(new GroupTabFragment().getClass().getName(), Utils.getSelectedModel(context), 0, null, false, null);
                //update group list

                dialog.dismiss();
            }
        });

    }

    public static void showPopUpForMessage(final Context context, final String responseMessage) {
        if (context != null) {
            final Dialog dialog = new Dialog(context);
            if (dialog != null && dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                dialog.setContentView(R.layout.dialog_error);
                TextView txt = (TextView) dialog.findViewById(R.id.txtErrorMsg);
                Button but = (Button) dialog.findViewById(R.id.but);
                if (!dialog.isShowing()) {
                    dialog.show();
                }

                txt.setText(responseMessage);

                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public static void terminateProvisioning(Context context, String msg) {

        MainActivity.isProvisioningProcessLive = false;
        Utils.saveModelInfoOfMultiElement(context, null);
        Utils.saveModelInfo(context, null);
        Utils.setNodeFeatures(context, null);
        Utils.showToast(context, msg);
        MainActivity.mSettings.cancel();
    }

    public static void showPopForGattError(final Context context, final String responseMessage) {

        if (context == null)
            return;

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_error_gatt);
        dialog.show();
        TextView txtErrorMsg = (TextView) dialog.findViewById(R.id.txtErrorMsg);
        txtErrorMsg.setText(context.getString(R.string.str_error_Gatt_Not_Responding));
        Button but = (Button) dialog.findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((MainActivity) context).restartNetwork();
            }
        });

    }


    public static void showPopForUserInvitation(final Context context, final String responseMessage) {

        if (context == null)
            return;

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_userinvitation);
        dialog.show();
        TextView txtInvitationCode = (TextView) dialog.findViewById(R.id.txtInvitationCode);
        txtInvitationCode.setText(responseMessage);
        Button but = (Button) dialog.findViewById(R.id.but);
        but.setText("Copy");
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(responseMessage);
                Utils.showToast(context, "Copied to clipboard");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });

    }

    public static String getNodeFeatures(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_node_feature));
    }

    public static void setNodeFeatures(Context context, String features) {
        if (context == null)
            return;

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_node_feature), ParseManager.getInstance().toJSON(features));

    }

    public static void showPopUpForSeesionExpire(final Context context) {

        if (context == null)
            return;

        LoginData loginData = null;
        try {
            loginData = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getLoginData(context)), LoginData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_session_expire);
        dialog.show();
        TextView txtInvitationCode = (TextView) dialog.findViewById(R.id.txtInvitationCode);
        Button but = (Button) dialog.findViewById(R.id.but);
        but.setText("OK");
        final LoginData finalLoginData = loginData;
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) context).isFromJoinNetwork = true;
                ((MainActivity) context).callLoginApi(finalLoginData.getUserName(), finalLoginData.getUserPassword());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);

            }
        });

    }

    public static MeshRootClass createEmptyDataForNewJoiner(MeshRootClass meshRootClass1) {

        ArrayList<NetKey> netKeys = new ArrayList<>();
        NetKey netKey = new NetKey();
        netKey.setKey("");
        netKeys.add(netKey);
        ArrayList<AppKey> appKeys = new ArrayList<>();
        AppKey appKey = new AppKey();
        appKey.setKey("");
        appKey.setOldKey("");
        appKeys.add(appKey);

        //meshRootClass1.setMeshUUID("");
        meshRootClass1.setNetKeys(netKeys);
        meshRootClass1.setAppKeys(appKeys);
        meshRootClass1.setProvisioners(null);
        meshRootClass1.setGroups(null);

        return meshRootClass1;
    }

    public static ArrayList<Nodes> getModelData(Context context, String modelType) {
        MeshRootClass meshRootClass = null;
        try {
            meshRootClass = (MeshRootClass) ((MainActivity) context).meshRootClass.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        ArrayList<Nodes> nodes = new ArrayList<>();
        try {
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {

                boolean isSelectedModelSupported = false;
                boolean isNodeIsProvisioner = false;
                for (int j = 0; j < meshRootClass.getProvisioners().size(); j++) {
                    if (meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(meshRootClass.getProvisioners().get(j).getUUID())) {
                        isNodeIsProvisioner = true;
                        break;
                    }
                }

                if (!isNodeIsProvisioner) {
                    ArrayList<Element> elements = new ArrayList<>();
                    for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {

                        ArrayList<Model> models = new ArrayList<>();
                        for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {

                            if (modelType.equalsIgnoreCase(context.getString(R.string.str_genericmodel_label))) {
                                if (context.getString(R.string.MODEL_ID_1002_GENERIC_LEVEL).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1000_GENERIC_STATUS).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_100c_GENERIC_BATTERY).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1000_GENERIC_ONOFF).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())) {
                                    isSelectedModelSupported = true;
                                    //meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(context.getString(R.string.MODEL_GROUP_NAME_GENERIC));
                                    //meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(modelType);
                                    models.add(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k));
                                }

                            } else if (modelType.equalsIgnoreCase(context.getString(R.string.str_vendormodel_label))) {
                                if (context.getString(R.string.MODEL_ID_00010030_VENDOR_MODEL).
                                        equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_10030_VENDOR_MODEL).
                                        equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_00300001_VENDOR_MODEL).
                                        equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())) {
                                    isSelectedModelSupported = true;
                                    // meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(context.getString(R.string.MODEL_GROUP_NAME_VENDOR));
                                    //meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(modelType);
                                    models.add(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k));
                                }

                            } else if (modelType.equalsIgnoreCase(context.getString(R.string.str_lighting_model_label))) {
                                if (context.getString(R.string.MODEL_ID_1303_LIGHT_CTL_SERVER).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1304_LIGHT_CTL_SETUP_SERVER).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1306_LIGHT_CTL_TEMPERATURE_SERVER).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1307_LIGHT_HSL_SERVER).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1308_LIGHT_HSL_SETUP_SERVER).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1300_LIGHT_LIGHTNESS_MODEL).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())
                                        || context.getString(R.string.MODEL_ID_1301_LIGHT_LIGHTNESS_SETUP_MODEL).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())) {
                                    isSelectedModelSupported = true;
                                    //meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(context.getString(R.string.MODEL_GROUP_NAME_LIGHT));
                                    //meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(modelType);
                                    models.add(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k));
                                }
                            } else if (modelType.equalsIgnoreCase(context.getString(R.string.str_sensormodel_label))) {
                                if (context.getString(R.string.MODEL_ID_1100_SENSOR_MODEL).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId())) {
                                    isSelectedModelSupported = true;
                                    //meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(context.getString(R.string.MODEL_GROUP_NAME_SENSOR));
                                    //meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setModelName(modelType);
                                    models.add(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k));
                                }

                            }
                        }

                        Element element = new Element();
                        element.setModels(models);
                        element.setName(meshRootClass.getNodes().get(i).getElements().get(j).getName());
                        element.setElementName(meshRootClass.getNodes().get(i).getElements().get(j).getName());
                        element.setParentNodeAddress(meshRootClass.getNodes().get(i).getElements().get(j).getParentNodeAddress());
                        element.setUnicastAddress(meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress());
                        element.setPublished(meshRootClass.getNodes().get(i).getElements().get(j).isPublished);
                        element.setSubscribed(meshRootClass.getNodes().get(i).getElements().get(j).isSubscribed);
                        element.setIndex(meshRootClass.getNodes().get(i).getElements().get(j).getIndex());
                        elements.add(element);
                    }

                    if (isSelectedModelSupported) {
                        Nodes nodeSelected = new Nodes(meshRootClass.getNodes().get(i).getNodeNumber() == null ? i : meshRootClass.getNodes().get(i).getNodeNumber());
                        nodeSelected.setElements(elements);
                        nodeSelected.setM_address(meshRootClass.getNodes().get(i).getM_address());
                        nodeSelected.setConfigComplete(meshRootClass.getNodes().get(i).getConfigComplete());
                        nodeSelected.setAddress(meshRootClass.getNodes().get(i).getAddress());
                        nodeSelected.setDeviceKey(meshRootClass.getNodes().get(i).getDeviceKey());
                        nodeSelected.setUUID(meshRootClass.getNodes().get(i).getUUID());
                        nodeSelected.setBlacklisted(meshRootClass.getNodes().get(i).getBlacklisted());
                        nodeSelected.setFeatures(meshRootClass.getNodes().get(i).getFeatures());
                        nodeSelected.setGroup(meshRootClass.getNodes().get(i).getGroup());
                        nodeSelected.setName(meshRootClass.getNodes().get(i).getName());
                      //  Log.e("Dinesh==>Utils 4759  ",meshRootClass.getNodes().get(i).getName());

                        nodeSelected.setPid(meshRootClass.getNodes().get(i).getPid());
                        nodeSelected.setRssi(meshRootClass.getNodes().get(i).getRssi());
                        nodeSelected.setTitle(meshRootClass.getNodes().get(i).getTitle());
                        nodeSelected.setType(meshRootClass.getNodes().get(i).getType());
                        nodeSelected.setSubtitle(meshRootClass.getNodes().get(i).getSubtitle());
                        nodeSelected.setVid(meshRootClass.getNodes().get(i).getVid());
                        nodeSelected.setSecurity("high");
                        nodeSelected.setUnicastAddress(meshRootClass.getNodes().get(i).getM_address());
                        nodeSelected.setDefaultTTL(3);
                        nodes.add(nodeSelected);
                    }
                }
            }
        } catch (Exception e) {
        }
        return nodes;
    }

    public static void saveAppKeys(Context context, String str) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put(context.getString(R.string.key_AppKeyData), str);

    }


    public static String getAppKeys(Context context) {
        if (context == null)
            return null;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_AppKeyData));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void saveModelInfo(Context context, String str) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put(context.getString(R.string.key_ModelInfo), str);

    }

    public static String getModelInfo(Context context) {
        if (context == null)
            return null;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_ModelInfo));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getModelInfoOfMultiElement(Context context) {
        if (context == null)
            return null;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_ModelInfo_Multi_Element));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveModelInfoOfMultiElement(Context context, String str) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put(context.getString(R.string.key_ModelInfo_Multi_Element), str);

    }

    public static ArrayList<Model> getSubModelsForSelectedGroupModel(Context context, Model model, String modelStr, Element element, ArrayList<Model> listModels) {
        ArrayList<Model> subModels = new ArrayList<>();

        for (int i = 0; i < listModels.size(); i++) {

            try {
                if (context.getString(R.string.MODEL_GROUP_NAME_GENERIC).contains(modelStr)
                        && listModels.get(i).getModelName().contains(modelStr)) {
                    subModels.add(listModels.get(i));
                } else if (context.getString(R.string.MODEL_GROUP_NAME_LIGHT).contains(modelStr)
                        && listModels.get(i).getModelName().contains(modelStr)) {
                    subModels.add(listModels.get(i));
                } else if (context.getString(R.string.MODEL_GROUP_NAME_VENDOR).contains(modelStr)
                        && listModels.get(i).getModelName().contains(modelStr)) {
                    subModels.add(listModels.get(i));
                } else if (context.getString(R.string.MODEL_GROUP_NAME_HEALTH).contains(modelStr)
                        && listModels.get(i).getModelName().contains(modelStr)) {
                    //subModels.add(listModels.get(i));
                } else if (context.getString(R.string.MODEL_GROUP_NAME_CONFIGURATION).contains(modelStr)
                        && listModels.get(i).getModelName().contains(modelStr)) {
                    //subModels.add(listModels.get(i));
                } else if (context.getString(R.string.MODEL_GROUP_NAME_SENSOR).contains(modelStr)
                        && listModels.get(i).getModelName().contains(modelStr)) {
                    subModels.add(listModels.get(i));
                } else if (context.getString(R.string.MODEL_GROUP_NAME_TIME).contains(modelStr)
                        && listModels.get(i).getModelName().contains(modelStr)) {
                    subModels.add(listModels.get(i));
                }
            } catch (NullPointerException e) {
            }
        }


        return subModels;
    }


    /**
     * used to check Internet connection in device
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }


    public static String getTopFragmentInBackStack(Activity activity) {
        if (activity == null) {
            return "";
        }

        try {
            FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
            Utils.DEBUG("getTopFragmentInBackStack() >> " + (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1)).getName());
            return (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1)).getName();
        } catch (Exception e) {
            return new MainViewPagerFragment().getClass().getName();
        }
    }

    public static LinearLayout layLoopForElements(final Context context, LinearLayout parent, Nodes node) {

        LayoutInflater layoutInfralte = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List views = new ArrayList();
        final ArrayList<Element> modelsLocal = new ArrayList<>();
        if (node.getElements().size() == 0) {
            parent.removeAllViews();
            return parent;
        }

        try {
            for (int i = 0; i < node.getElements().size(); i++) {

                final View view = layoutInfralte.inflate(R.layout.row_dialog_pubsub, null);
                TextView txtElement = (TextView) view.findViewById(R.id.txtElement);
                txtElement.setText(String.valueOf(node.getElements().get(i).getElementName()));

                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                views.add(view);

            }

            for (int i = 0; i < views.size(); i++) {
                parent.addView((View) views.get(i));
            }

        } catch (Exception e) {
        }


        return parent;
    }

    public static void showModelsDetailsPopup(Context context, ArrayList<Model> arrayList, String which_model) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView OKTV = (TextView) dialog.findViewById(R.id.OKTV);
        TextView titleTV = (TextView) dialog.findViewById(R.id.titleTV);
        titleTV.setText(Utils.capitalize(which_model + " " + context.getString(R.string.MODEL) + " " + "GROUP"));
        RecyclerView modelsRV = (RecyclerView) dialog.findViewById(R.id.modelsRV);
        ModelsCustomAdapter modelsCustomAdapter = new ModelsCustomAdapter(context, arrayList, which_model);
        modelsRV.setLayoutManager(new LinearLayoutManager(context));
        modelsRV.setAdapter(modelsCustomAdapter);

        OKTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void showElementByModelPopup(Context context, ArrayList<Element> arrayList, String which_model) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView OKTV = (TextView) dialog.findViewById(R.id.OKTV);
        TextView titleTV = (TextView) dialog.findViewById(R.id.titleTV);
        TextView txtModelName = (TextView) dialog.findViewById(R.id.txtModelName);
        TextView txtModelId = (TextView) dialog.findViewById(R.id.txtModelId);
        titleTV.setText(Utils.capitalize(which_model + " " + context.getString(R.string.MODEL) + " " + "GROUP"));
        RecyclerView modelsRV = (RecyclerView) dialog.findViewById(R.id.modelsRV);
        RowAdapter rowAdapter = new RowAdapter(context, arrayList);
        modelsRV.setLayoutManager(new LinearLayoutManager(context));
        modelsRV.setAdapter(rowAdapter);
        txtModelId.setText(which_model);
        txtModelId.setVisibility(View.GONE);

        OKTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static String insertDashUUID(String uuid) {

        if (uuid != null) {
            StringBuffer sb = new StringBuffer(uuid);

            try {

                sb.insert(8, "-");
            } catch (Exception e1) {
            }

            try {
                sb = new StringBuffer(sb.toString());
                sb.insert(13, "-");
            } catch (Exception e2) {
            }

            try {
                sb = new StringBuffer(sb.toString());
                sb.insert(18, "-");
            } catch (Exception e3) {
            }

            try {
                sb = new StringBuffer(sb.toString());
                sb.insert(23, "-");
            } catch (Exception e4) {
            }

            return sb.toString();

        }

        return null;

    }


    public static void addPublishToGroup(Context context, String node_address, String element_address, String group_address) {

        int appKeyIndex = Utils.getDefaultAppKeyIndex(context);
        contextU = context;
        UserApplication app = (UserApplication) context.getApplicationContext();
        try {

            network.getSettings().setPublicationAddress(context, appKeyIndex, null, mobleAddress.deviceAddress(Integer.parseInt(node_address)), mobleAddress.deviceAddress(Integer.parseInt(element_address)), Integer.parseInt(group_address, 16), mPublicationListenerForGroupSettings);

        } catch (Exception e) {
            e.printStackTrace();
        }

       /* Log.i("ElemetAdres is", element.getUnicastAddress() + "");
        Log.i("GrpAddres is", addr + "");*/
    }

    public static boolean isGroupIsPresentInElement(Element element, String new_groupAddress) {

        //According to note 1
        boolean isGroupPresentInElement = false;

        for (int i = 0; i < element.getModels().size(); i++) {
            if (element.getModels().get(i).getSubscribe() != null && element.getModels().get(i).getSubscribe().size() > 0) {
                for (int j = 0; j < element.getModels().get(i).getSubscribe().size(); j++) {

                    if (new_groupAddress.equalsIgnoreCase(element.getModels().get(i).getSubscribe().get(j))) {
                        isGroupPresentInElement = true;
                    }
                }
            }
        }


        return isGroupPresentInElement;
    }

    public static void closeKeyboard(Context context, EditText edtName) {
        try {
            InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (edtName != null) {
                mgr.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
            } else {
                mgr.hideSoftInputFromInputMethod(null, 0);
            }

        } catch (Exception e) {

        }
    }

    public static String firstLetterCaps(String data) {
        String firstLetter = data.substring(0, 1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    public static String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capital(manufacturer) + " " + model;
    }

    private static String capital(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static void onGetNewProvisioner(Context context) {

        if (!Utils.isProviosnerUUIDSet(context)) {
            onGenerateProvisionerUUID(context);
            String deviceName = getDeviceName();
            Utils.DEBUG("Provisoner Name : " + getDeviceName());

            Provisioner provisioner = new Provisioner();
            provisioner.setUUID(Utils.getProvisionerUUID(context));
            provisioner.setProvisionerName(deviceName);
            //Log.e("Dinesh==> 5141",deviceName);
            provisioner.setUnicastAddress(String.valueOf(((MainActivity) context).provisionerUnicastLowAddress));

            ArrayList<AllocatedGroupRange> allocatedGroupRanges = new ArrayList<>();
            ArrayList<AllocatedUnicastRange> allocatedUnicastRanges = new ArrayList<>();

            AllocatedGroupRange allocatedGroupRange = new AllocatedGroupRange();
            allocatedGroupRange.setLowAddress(Utils.getProvisionerGroupLowAddressRange(context));
            allocatedGroupRange.setHighAddress(Utils.getProvisionerGroupHighAddressRange(context));
            allocatedGroupRanges.add(allocatedGroupRange);

            AllocatedUnicastRange allocatedUnicastRange = new AllocatedUnicastRange();
            allocatedUnicastRange.setLowAddress(Utils.getProvisionerUnicastLowAddress(context));
            allocatedUnicastRange.setHighAddress(Utils.getProvisionerUnicastHighAddress(context));
            allocatedUnicastRanges.add(allocatedUnicastRange);

            provisioner.setAllocatedGroupRange(allocatedGroupRanges);
            provisioner.setAllocatedUnicastRange(allocatedUnicastRanges);

            String provisionerData = ParseManager.getInstance().toJSON(provisioner);
            Utils.setProvisioner(context, provisionerData);
            Utils.DEBUG("Provisoner DATA : " + provisionerData);
        }
    }

    public static void onGenerateProvisionerUUID(Context context) {

        if (!Utils.isProviosnerUUIDSet(context)) {
            String uniqueID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            UUID uuid = UUID.nameUUIDFromBytes(uniqueID.getBytes());
            Utils.setProvisionerUUID(context, uuid.toString());
            Utils.DEBUG("Provisioner UUID Generated : " + Utils.getProvisionerUUID(context));
        } else {
            Utils.DEBUG("UUID Alloted : " + Utils.getProvisionerUUID(context));
        }
    }


    /*
     * Provisioner Unicast LowAddress
     * */
    public static String getProvisionerGroupLowAddressRange(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_group_low_address));
    }

    public static void setProvisionerGroupLowAddress(Context context, String lowAddress) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_group_low_address), lowAddress);

    }

    /*
     * Provisioner Unicast HighAddress
     * */
    public static String getProvisionerGroupHighAddressRange(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_group_high_address));
    }

    public static void setProvisionerGroupHighAddress(Context context, String highAddress) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_group_high_address), highAddress);

    }

    /*
     * Provisioner Unicast Range
     * */
    public static String getProvisionerGroupRange(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_group_range));
    }

    public static void setProvisionerGroupRange(Context context, String range) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_group_range), range);

    }


    /*
     * Provisioner Unicast LowAddress
     * */
    public static String getProvisionerUnicastLowAddress(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_low_address));
    }

    public static void setProvisionerUnicastLowAddress(Context context, String lowAddress) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_low_address), lowAddress);

    }

    /*
     * Provisioner Unicast HighAddress
     * */
    public static String getProvisionerUnicastHighAddress(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_high_address));
    }

    public static void setProvisionerUnicastHighAddress(Context context, String highAddress) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_high_address), highAddress);

    }

    /*
     * Provisioner Unicast Range
     * */
    public static String getProvisionerUnicastRange(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        return (String) sp.get(context.getString(R.string.key_provisioner_unicast_range));
    }

    public static void setProvisionerUnicastRange(Context context, String range) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_provisioner_unicast_range), range);

    }

    public static void action_config_setting(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!");
        builder.setMessage(R.string.warning);
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.showFileChooser(context, false);
            }
        });
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public static void showFileChooser(final Context context, boolean isFromCloud) {

        /*final AppDialogLoader loader = new AppDialogLoader(context);
        loader.show();*/
        boolean isSyncSuccesfull = false;

        try {
            if (isFromCloud) {
                //setup invited cloud data

                Utils.DEBUG("-->---------------START UPDATING BLUE-NRG MESH CLOUD JSON----------------");
                final MeshRootClass meshRootClassCompleteData = updateTransferedDataAfterImport(context, Utils.getCloudData(context), true);
                Utils.DEBUG("-->---------------UPDATED BLUE-NRG MESH MAIL JSON AS PER REQUIREMENT----------------");
                if (meshRootClassCompleteData != null) {
                    Utils.setUserRegisteredToDownloadJson(context, "true");
                    String s = ParseManager.getInstance().toJSON(meshRootClassCompleteData);
                    Utils.setBLEMeshDataToLocal(context, s);
                    Utils.DEBUG("-->---------------SAVED BLUE-NRG MESH MAIL JSON TO SHARED-PREF----------------");
                }

                if (meshRootClassCompleteData != null) {

                    updateUIAfterImport(context, meshRootClassCompleteData, isFromCloud);
                } else {
                    Utils.showToast(context, "Data error please share and download again.");
                }
            } else {

                Utils.DEBUG("-->---------------START UPDATING BLUE-NRG MESH MAIL/LOCAL JSON----------------");
                isSyncSuccesfull = Utils.syncJson(context, context.getResources().getBoolean(R.bool.bool_readFromAssets));
                ((MainActivity) context).updateJsonData();

                MeshRootClass meshRootDownloaded = new MeshRootClass();
                try {
                    meshRootDownloaded = ParseManager.getInstance().fromJSON(
                            new JSONObject(Utils.getBLEMeshDataFromLocal(context)), MeshRootClass.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isSyncSuccesfull) {

                    updateUIAfterImport(context, meshRootDownloaded, isFromCloud);
                } else {
                    Toast.makeText(context, "You dont have shared file to overwrite mesh.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
        }
    }

    private static void updateUIAfterImport(final Context context, final MeshRootClass finalMeshRootDownloaded, final boolean isFromCloud) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < finalMeshRootDownloaded.getProvisioners().size(); i++) {
                    if (Utils.getProvisionerUUID(context).equals(finalMeshRootDownloaded.getProvisioners().get(i).getUUID())) {
                        Utils.setProvisionerUnicastLowAddress(context, finalMeshRootDownloaded.getProvisioners().get(i).getAllocatedUnicastRange().get(0).getLowAddress());
                        Utils.setProvisionerUnicastHighAddress(context, finalMeshRootDownloaded.getProvisioners().get(i).getAllocatedUnicastRange().get(0).getHighAddress());
                    }
                }
                ((MainActivity) context).updateJsonData();
                Utils.setRestart(context, true);
                network.stop();
                ((MainActivity) context).startNetwork();

                //update actionbar
                /*((MainActivity) context).fragmentCommunication(new ModelTabFragment().getClass().getName(), null, 1, null, true, null);*/
                Utils.updateActionBarForFeatures(((MainActivity) context), isFromCloud ? new ExchangeConfigFragment().getClass().getName() : new ProvisionedTabFragment().getClass().getName());
                //update data in all tabs
                ((MainActivity) context).fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
                ((MainActivity) context).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
                Utils.showToast(context, context.getString(R.string.str_data_build_success_label));
                //((MainActivity) context).onBackPressed();
            }
        }, 800);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {

            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isNodesExistInMesh(FragmentActivity activity, MeshRootClass meshRootClass) {

        int countNode = 0;

        try {
            if (meshRootClass != null && meshRootClass.getNodes() != null && meshRootClass.getNodes().size() > 1) {
                for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                    boolean isNodeIsProvisioner = false;
                    for (int j = 0; j < meshRootClass.getProvisioners().size(); j++) {
                        if (meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(meshRootClass.getProvisioners().get(j).getUUID())) {
                            isNodeIsProvisioner = true;
                            break;
                        }
                    }
                    if (!isNodeIsProvisioner) {
                        countNode++;
                    }
                }
            }
        } catch (Exception e) {
        }

        if (countNode > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static void updateActionBarForTabs(final Activity parent, final int position) {

        if (position == 0) {
            Utils.updateActionBarForFeatures(parent, new UnprovisionedFragment().getClass().getName());
        } else if (position == 1) {
            Utils.updateActionBarForFeatures(parent, new ProvisionedTabFragment().getClass().getName());
        } else if (position == 2) {
            Utils.updateActionBarForFeatures(parent, new GroupTabFragment().getClass().getName());
        } else if (position == 3) {
            Utils.updateActionBarForFeatures(parent, new ModelTabFragment().getClass().getName());
        } else {
            Utils.updateActionBarForFeatures(parent, new MainViewPagerFragment().getClass().getName());
        }

    }

    public static int convertHexToIntValue(String s) {

        //for more than or equal to 4 bytes data
        int i = 0;
        try {
            Long l = Long.parseLong(s, 16);
            i = l.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public static Float convertHexToFloat(String s) {

        //for more than 4bytes
        Long i = Long.parseLong(s, 16);
        Float f = Float.intBitsToFloat(i.intValue());

        return f;
    }

    public static int convertHexToInt(String s) {

        int i = Integer.parseInt(s, 16);
        return i;
    }

    public static String convertIntToHex(int intValue) {
        String s = Integer.toHexString(intValue);

        return s;
    }

    public static void updateSensorEvents(final String sensorData, int mPropertyId) {

        if (sensorData != null) {
            ((MainActivity) contextMainActivity).fragmentCommunication(Utils.getClassName(contextMainActivity), sensorData,
                    mPropertyId, null, false, null);
        }
    }


    public static String getStringFromHashValue(String s) {
        char[] chars = s.toCharArray();
        String str = "";
        int count = 1;
        for (int i = 0; i < chars.length; i++) {
            if (i >= 3 && count < 5) {
                str = str + String.valueOf(chars[i]);
                count++;
            }

        }
        return str;
    }

    /*public static mobleAddress getNextGroupAddress(Context context, mobleAddress addr, int count, MeshRootClass meshRootClass) {

        mobleAddress newGrpAddress = null;
        int nextGrpAddress = Integer.parseInt(Utils.getProvisionerGroupLowAddressRange(context)) + count;
        newGrpAddress = mobleAddress.groupAddress(Integer.parseInt(Utils.convertDecimalToHexadecimal(nextGrpAddress)));

        return newGrpAddress;
    }*/

    public static String intToHexForFourDigit(int number) {
        String hexStr = convertDecimalToHexadecimal(number);
        int b16outLen = hexStr.length();
        int remainingSize = 4 - b16outLen;
        for (int i = 0; i < remainingSize; i++) {
            hexStr = ('0' + hexStr);
        }
        return hexStr;
    }


    public static String convertDecimalToHexadecimal(int nmbr) {
        return Integer.toHexString(nmbr);
    }

    public static int getNextGroupCount(MeshRootClass messRootClass) {

        int count = 0;
        try {
            for (int i = 0; i < messRootClass.getGroups().size(); i++) {
                if (!messRootClass.getGroups().get(i).getAddress().equalsIgnoreCase("FFFF")) {
                    count++;
                }
            }
        } catch (Exception e) {
        }
        return count;
    }

    public static void showAlertForCount(final Context context, int count) {

        if (count > 9) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setCancelable(false);
            alert.setMessage("You can create maximum 10 groups");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((MainActivity) context).onBackPressed();
                }
            });
            alert.show();
        } else {
            return;
        }
    }

    public static boolean checkGrpHasSubElements(final Context context, ArrayList<Element> eleSubscriptionList) {

        boolean isChecked = false;
        for (int i = 0; i < eleSubscriptionList.size(); i++) {

            if (eleSubscriptionList.get(i).isChecked()) {
                isChecked = true;
                break;
            }
        }

        return isChecked;
    }

    public static void checkGrpHasSubElements(final Context context, ArrayList<Element> eleSubscriptionList, final mobleAddress addr, String msg) {

        boolean isChecked = false;
        for (int i = 0; i < eleSubscriptionList.size(); i++) {

            if (eleSubscriptionList.get(i).isChecked()) {
                isChecked = true;
                break;
            }
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        if (isChecked) {

            alertDialog.setMessage(String.valueOf(addr) + " " + msg);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    if (Utils.getSettingType(context).equals(context.getString(R.string.add_group))) {
                        ((MainActivity) context).onBackPressed();
                    }

                    dialog.cancel();
                }
            });
        } else {

            alertDialog.setMessage("Please note that this action will remove this group permanently. Continue ?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Utils.removeGroupFromJson(context, addr);
                    //refresh grouptab ui
                    ((MainActivity) context).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
                    Utils.updateActionBarForFeatures((MainActivity) context, new GroupTabFragment().getClass().getName());
                    ((MainActivity) context).onBackPressed();
                    dialog.cancel();

                    /* final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) context).onBackPressed();
                            dialog.cancel();
                        }
                    }, 1000);*/

                }
            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        }
        alertDialog.show();
    }


    public static void showHealthSettingPopup(Context context, ArrayList<String> faultArrays, String which_model) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView OKTV = (TextView) dialog.findViewById(R.id.OKTV);
        LinearLayout belowtitleLL = (LinearLayout) dialog.findViewById(R.id.belowtitleLL);
        belowtitleLL.setVisibility(View.GONE);
        TextView titleTV = (TextView) dialog.findViewById(R.id.titleTV);
        titleTV.setText(Utils.capitalize(which_model));
        RecyclerView modelsRV = (RecyclerView) dialog.findViewById(R.id.modelsRV);


        HealthStatusPopupAdapter healthStatusPopupAdapter = new HealthStatusPopupAdapter(context, faultArrays, which_model);
        modelsRV.setLayoutManager(new LinearLayoutManager(context));
        modelsRV.setAdapter(healthStatusPopupAdapter);

        OKTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void setDefaultGroupToggle_ONOFF(Context context, MeshRootClass meshRootClass, boolean isToggleOn) {
        for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
            if (meshRootClass.getNodes().get(i).getElements().size() > 0) {
                for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    meshRootClass.getNodes().get(i).getElements().get(j).setChecked(isToggleOn);
                }
            }
        }

        ((MainActivity) context).meshRootClass = meshRootClass;
        String s = ParseManager.getInstance().toJSON(meshRootClass);
        Utils.setBLEMeshDataToLocal(context, s);

        ((MainActivity) context).updateProvisionedTab(null, 0);
        //((MainActivity) context).updateModelTab();  //commented because we don't want to update the header name if All Node toggle
    }

    public static void setGroupToggle_ONOFF(Context context, MeshRootClass meshRootClass, String grpAddress, boolean isToggleON) {

        try {
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                if (meshRootClass.getNodes().get(i).getElements().size() > 0) {
                    for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                        boolean isSubscribedToGrp = false;
                        if (meshRootClass.getNodes().get(i).getElements().get(j).getModels() != null && meshRootClass.getNodes().get(i).getElements().get(j).getModels().size() > 0) {
                            for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                                for (int l = 0; l < meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().size(); l++) {
                                    if (meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().get(l).toString().equalsIgnoreCase(grpAddress)) {
                                        isSubscribedToGrp = true;
                                        break;
                                    }
                                }

                                if (isSubscribedToGrp) {
                                    break;
                                }
                            }
                        }
                        if (isSubscribedToGrp) {
                            meshRootClass.getNodes().get(i).getElements().get(j).setChecked(isToggleON);
                        }
                    }
                }
            }

            ((MainActivity) context).meshRootClass = meshRootClass;
            String s = ParseManager.getInstance().toJSON(meshRootClass);
            Utils.setBLEMeshDataToLocal(context, s);
            ((MainActivity) context).updateProvisionedTab(null, 0);
            //   ((MainActivity) context).updateModelTab();
        } catch (Exception e) {
        }
    }

    //RUNTIME PERMISSION **************************************************************************
    public static boolean checkPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Context context) {

        ActivityCompat.requestPermissions((MainActivity) context, new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
    }

    public static void sendDataOverMail(Context context) {

        //updateJsonData();
        if (Build.VERSION.SDK_INT >= 23) {
            if (Utils.checkPermission(context)) {
                Utils.saveUniversalFilterDataToFile(context);

                //updateJsonData();
                String[] mailAdd = {"e-mail id"};
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "BlueNrgCache");
                File file = new File(dir, context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
                Utils.DEBUG(">> File Exist : " + dir.getAbsolutePath() + " " + file.getAbsolutePath());
                Utils.sendMail(context, mailAdd, null, "ST BLE Mesh Configuration File", "Please find configuration file as attachment!.", file.getAbsolutePath().toString());

            } else {
                Utils.requestPermission(context); // Code for permission
            }
        } else {

            String[] mailAdd = {"e-mail id"};
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "BlueNrgCache");
            File file = new File(dir, context.getResources().getString(R.string.FILE_bluenrg_mesh_json));
            Utils.DEBUG(">> File Exist : " + dir.getAbsolutePath() + " " + file.getAbsolutePath());
            Utils.sendMail(context, mailAdd, null, "BLE MESH FILE", "ATTACHMENT", file.getAbsolutePath().toString());

        }

    }

    public static void addAppKeyInJson(Context context, String appKey, int appKeyIndex, final Element element) {

        MeshRootClass meshRootClass = null;

        try {
            meshRootClass = (MeshRootClass) ((MainActivity) context).meshRootClass.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        try {
            if (meshRootClass != null) {
                AppKey newAppKey = new AppKey();
                newAppKey.setIndex(appKeyIndex);
                //newAppKey.setName(context.getString(R.string.str_app_key_name));
                newAppKey.setName("App Key " + appKeyIndex);
                newAppKey.setKey(appKey);

                //Add in main object
                if (meshRootClass.getAppKeys() != null && meshRootClass.getAppKeys().size() > 0) {
                    boolean isAlreadyAdded = false;
                    for (int i = 0; i < meshRootClass.getAppKeys().size(); i++) {
                        if (meshRootClass.getAppKeys().get(i).getKey().equals(appKey)) {
                            isAlreadyAdded = true;
                        }
                    }

                    if (!isAlreadyAdded) {
                        meshRootClass.getAppKeys().add(newAppKey);
                    }
                } else {
                    ArrayList<AppKey> appKeys = new ArrayList<>();
                    appKeys.add(newAppKey);
                    meshRootClass.setAppKeys(appKeys);
                }

                //add in node object
                for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                    for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                        if (meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress())) {
                            if (meshRootClass.getNodes().get(i).getAppKeys() != null && meshRootClass.getNodes().get(i).getAppKeys().size() > 0) {
                                boolean isAlreadyAdded = false;
                                for (int k = 0; k < meshRootClass.getNodes().get(i).getAppKeys().size(); k++) {
                                    if (meshRootClass.getNodes().get(i).getAppKeys().get(k).getKey().equals(appKey)) {
                                        isAlreadyAdded = true;
                                    }
                                }

                                if (!isAlreadyAdded) {
                                    meshRootClass.getNodes().get(i).getAppKeys().add(newAppKey);
                                    break;
                                }
                            } else {
                                ArrayList<AppKey> appKeys = new ArrayList<>();
                                appKeys.add(newAppKey);
                                meshRootClass.getNodes().get(i).setAppKeys(appKeys);
                            }

                            break;
                        }
                    }
                }


                String s = ParseManager.getInstance().toJSON(meshRootClass);
                Utils.setBLEMeshDataToLocal(context, s);
                ((MainActivity) context).updateJsonData();
            }
        } catch (Exception e) {
        }
    }

    public static InformationOOB getTypeOfPublicKeyType_OOBInformation(int oobInformation) {

        InformationOOB informationOOB = new InformationOOB();

        if ((oobInformation & 0x01) >> 0 == 1) {
            informationOOB.setOther(true);
            System.out.println("Other");
        }

        if ((oobInformation & 0x02) >> 1 == 1) {
            informationOOB.setElectronicURI(true);
            System.out.println("Electronic/URI");
        }

        if ((oobInformation & 0x04) >> 2 == 1) {
            informationOOB.setMachineReadableCode(true);
            System.out.println("2D machine-readable code");
        }

        if ((oobInformation & 0x08) >> 3 == 1) {
            informationOOB.setBarCode(true);
            System.out.println("Bar code");
        }

        if ((oobInformation & 0x10) >> 4 == 1) {
            informationOOB.setNFC(true);
            System.out.println("Near Field Communication(NFC)");
        }

        if ((oobInformation & 0x20) >> 5 == 1) {
            informationOOB.setNumber(true);
            System.out.println("Number");
        }

        if ((oobInformation & 0x40) >> 6 == 1) {
            informationOOB.setString(true);
            System.out.println("String");
        }

        if ((oobInformation & 0x80) >> 11 == 1) {
            informationOOB.setOnBox(true);
            System.out.println("On box");
        }

        if ((oobInformation & 0x16) >> 12 == 1) {
            informationOOB.setInsideBox(true);
            System.out.println("Inside box");
        }

        if ((oobInformation & 0x32) >> 13 == 1) {
            informationOOB.setOnPieceOfPaper(true);
            System.out.println("On piece of paper");
        }

        if ((oobInformation & 0x64) >> 14 == 1) {
            informationOOB.setInsideManual(true);
            System.out.println("Inside manual");
        }

        if ((oobInformation & 0x128) >> 15 == 1) {
            informationOOB.setOnDevice(true);
            System.out.println("On device");
        }

        return informationOOB;
    }

    public static InputActions getInputTypeActions(Context context, int actionSize, int inputOOBAction) {

        InputActions inputActions = new InputActions();


        if ((inputOOBAction & 0x01) >> 0 == 1) {
            System.out.println("Push Action");
            inputActions.setPush(true);
        }

        if ((inputOOBAction & 0x02) >> 1 == 1) {
            inputActions.setTwist(true);
            System.out.println("Twist Action");
        }

        if ((inputOOBAction & 0x04) >> 2 == 1) {
            inputActions.setInputNumeric(true);
            System.out.println("Input Numeric");
        }

        if ((inputOOBAction & 0x08) >> 3 == 1) {
            inputActions.setInputAlphanumeric(true);
            System.out.println("Input Alphanumeric");
        }
        return inputActions;
    }

    public static OutputActions getOutputTypeAction(Context context, int actionSize, int outputOOBAction) {

        OutputActions outputActions = new OutputActions();


        if ((outputOOBAction & 0x01) >> 0 == 1) {
            outputActions.setBlink(true);
        }

        if ((outputOOBAction & 0x02) >> 1 == 1) {
            outputActions.setBeep(true);
        }

        if ((outputOOBAction & 0x04) >> 2 == 1) {
            outputActions.setVibrate(true);
        }

        if ((outputOOBAction & 0x08) >> 3 == 1) {
            outputActions.setOutputNumeric(true);
        }

        if ((outputOOBAction & 0x16) >> 4 == 1) {
            outputActions.setOutputAlphanumeric(true);
        }

        return outputActions;
    }

    public static void updateActiveKeyForModelSelected(final Context context, final AppKey appKeyUsed, final boolean isAddingKey, final Element element, final Model modelSelected) {


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                MeshRootClass clonedMeshRoot = null;

                try {
                    clonedMeshRoot = (MeshRootClass) ((MainActivity) context).meshRootClass.clone();
                } catch (CloneNotSupportedException e) {
                }

                for (int i = 0; i < clonedMeshRoot.getNodes().size(); i++) {
                    for (int j = 0; j < clonedMeshRoot.getNodes().get(i).getElements().size(); j++) {
                        if (clonedMeshRoot.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress())) {
                            for (int k = 0; k < clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                                //models
                                if (clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId().equalsIgnoreCase(modelSelected.getModelId())) {
                                    try {

                                        if (clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).getBind() != null && clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).getBind().size() > 0) {
                                            for (int l = 0; l < clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).getBind().size(); l++) {

                                                if (clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).getBind().get(l) == appKeyUsed.getIndex()) {
                                                    if (isAddingKey) {
                                                        Utils.DEBUG("Setting AppKey Index : " + appKeyUsed.getIndex());
                                                        clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).setActiveKeyIndex(appKeyUsed.getIndex());

                                                    } else {
                                                        //setting default key
                                                        clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).setActiveKeyIndex(-1);
                                                    }

                                                }
                                            }
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }
                }

                String s = ParseManager.getInstance().toJSON(clonedMeshRoot);
                Utils.setBLEMeshDataToLocal(context, s);
                ((MainActivity) context).updateJsonData();
            }
        });
    }

    public static ArrayList<Model> getAllAvailableModels(Context context) {

        MeshRootClass meshRootClass = null;
        try {
            meshRootClass = (MeshRootClass) ((MainActivity) context).meshRootClass.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        ArrayList<Model> models = new ArrayList<>();

        for (int i = 0; i < meshRootClass.getNodes().size(); i++) {

            boolean isProvisioner = false;
            for (int j = 0; j < meshRootClass.getProvisioners().size(); j++) {
                if (meshRootClass.getProvisioners().get(j).getUUID().equalsIgnoreCase(meshRootClass.getNodes().get(i).getUUID())) {
                    isProvisioner = true;
                    break;
                }
            }

            if (!isProvisioner) {
                for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    try {
                        if (meshRootClass.getNodes().get(i).getElements().get(j).getModels() != null && meshRootClass.getNodes().get(i).getElements().get(j).getModels().size() > 0) {
                            for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                                Model model = meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k);
                                boolean isPresent = false;
                                for (int l = 0; l < models.size(); l++) {
                                    if (models.get(l).getModelName().equalsIgnoreCase(model.getModelName())) {
                                        isPresent = true;
                                        break;
                                    }
                                }

                                if (!isPresent) {
                                    models.add(model);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return models;
    }

    public static ArrayList<Element> getElemntsContainsModel(Context context, Model model, Group groupData) {
        MeshRootClass meshRootClass = null;
        try {
            meshRootClass = (MeshRootClass) ((MainActivity) context).meshRootClass.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        ArrayList<Element> elements = new ArrayList<>();

        for (int i = 0; i < meshRootClass.getNodes().size(); i++) {

            boolean isProvisioner = false;
            for (int j = 0; j < meshRootClass.getProvisioners().size(); j++) {
                if (meshRootClass.getProvisioners().get(j).getUUID().equalsIgnoreCase(meshRootClass.getNodes().get(i).getUUID())) {
                    isProvisioner = true;
                    break;
                }
            }

            if (!isProvisioner) {
                for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    boolean isPresent = false;
                    if (meshRootClass.getNodes().get(i).getElements().get(j).getModels() != null && meshRootClass.getNodes().get(i).getElements().get(j).getModels().size() > 0) {
                        boolean isSubscribed = false;
                        for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                            if (meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelName().equalsIgnoreCase(model.getModelName())) {
                                isPresent = true;
                            }

                            //check subscription data
                            if (isPresent) {
                                if (meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe() != null && meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().size() > 0) {
                                    for (int l = 0; l < meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().size(); l++) {
                                        if (groupData.getAddress().equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().get(l))) {
                                            isSubscribed = true;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }

                        if (isPresent) {
                            if (isSubscribed) {
                                meshRootClass.getNodes().get(i).getElements().get(j).setChecked(true);
                            } else {
                                meshRootClass.getNodes().get(i).getElements().get(j).setChecked(false);
                            }

                            elements.add(meshRootClass.getNodes().get(i).getElements().get(j));
                        }
                    }
                }
            }
        }
        return elements;
    }

    public Fragment getFragmentInstanceOnMainActivity(Context context) {
        FragmentManager.BackStackEntry a = ((MainActivity) context).getSupportFragmentManager().getBackStackEntryAt(((MainActivity) context).getSupportFragmentManager().getBackStackEntryCount() - 1);//top
        Fragment baseFrag = (Fragment) ((MainActivity) context).getSupportFragmentManager().findFragmentByTag(a.getName());
        return baseFrag;
    }


    /*public static String getHeartBeatData(Context context) {
        if (context == null)
            return null;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            return (String) sp.get(context.getString(R.string.key_heartbeat));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setHearBeatData(Context context, String data) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_heartbeat), data);

    }*/


/*    public static void saveHashMap(String key ,  ArrayList<HearBeatData> obj,Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key,json);
        editor.apply();     // This line is IMPORTANT !!!
    }


    public static  HashMap<String,ArrayList<HearBeatData>> getHashMap(String key,Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key,"");
        java.lang.reflect.Type type = new TypeToken<HashMap<String,ArrayList<HearBeatData>>>(){}.getType();
        HashMap<String,ArrayList<HearBeatData>> obj = gson.fromJson(json, type);
        return obj;
    }*/


    public static Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }


    public static final defaultAppCallback otaserviceCallback = new defaultAppCallback() {
        @Override
        public void onResponse(mobleAddress peer, Object cookies, byte status, byte[] data) {
            Log.i("OnResponseStatus", status + "");
            if (status == STATUS_SUCCESS) {


                callbackToFragment.onOTAReceivedCallback("SUCCESS", "OTA");
                Log.i("Response: From", String.valueOf(status));


            } else {
                callbackToFragment.onOTAReceivedCallback("FAILED", "OTA");
                Log.i("Status Failed", String.valueOf(status));
            }

        }
    };


    public static final defaultAppCallback otaCheckserviceCallback = new defaultAppCallback() {
        @Override
        public void onResponse(mobleAddress peer, Object cookies, byte status, byte[] data) {
            Log.i("OnResponseStatus", status + "");
            if (status == STATUS_SUCCESS) {


                callbackToFragment.onOTAReceivedCallback("SUCCESS", "checkOTA");
                Log.i("Response: From", String.valueOf(status));

            } else {

                callbackToFragment.onOTAReceivedCallback("FAILED", "checkOTA");
                Log.i("Status Failed", String.valueOf(status));
            }

        }
    };

    static CallbackToFragment callbackToFragment;


    public static void startOTAServiceMessage(int address, CallbackToFragment callbackToFragment, byte sub) {
        Utils.callbackToFragment = callbackToFragment;
        network.getApplication().setRemoteData(mobleAddress.deviceAddress(address), Nucleo.APPLI_TEST_CMD, 1, new byte[]{sub}, true);

    }

    public static Integer getTIDValue(Context context) {
        if (context == null)
            return 0;
        try {

            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

            int val = (Integer) (sp.get(context.getString(R.string.key_tid_value))) + 1;
            if (val < TID_MAX) {
                setTIDValue(context, val);
            } else {
                setTIDValue(context, 0);

            }


            return ((Integer) (sp.get(context.getString(R.string.key_tid_value))) + 1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setTIDValue(Context context, int value) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_tid_value), value);

    }


    public static ArrayList<String> getSpellingOnForVoice(Context context) {
        if (context == null)
            return null;

        Gson gson = new Gson();
        ArrayList<String> allOn = new ArrayList<>();
        String arrayData = "";
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("OnSpelling") == null) {
            return null;
        } else {
            allOn = gson.fromJson(String.valueOf(sp.get("OnSpelling")).toLowerCase(), new ArrayList<String>().getClass());
            return allOn;
        }
    }


    public static ArrayList<String> getSpellingOFFForVoice(Context context) {

        if (context == null)
            return null;

        Gson gson = new Gson();
        ArrayList<String> allOn = new ArrayList<>();
        String arrayData = "";
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("OFFSpelling") == null) {
            return null;
        } else {
            allOn = gson.fromJson(String.valueOf(sp.get("OFFSpelling")).toLowerCase(), new ArrayList<String>().getClass());
            return allOn;
        }
    }


    public static void setGroupAddressForVoice(Context context, String res) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        sp.put("groupvoiceaddress", res);

    }

    public static void setSpellingOnForVoice(Context context, String res) {

        if (context == null)
            return;

        Gson gson = new Gson();
        ArrayList<String> allOn = new ArrayList<>();
        String arrayData = "";
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("OnSpelling") == null) {
            allOn.add(res);
        } else {
            allOn = gson.fromJson(String.valueOf(sp.get("OnSpelling")).toLowerCase(), new ArrayList<String>().getClass());
            if (!allOn.contains(res)) {
                allOn.add(res);
                Utils.DEBUG("String " + res + " is added successfully.");
            }
        }

        arrayData = gson.toJson(allOn);
        AppSharedPrefs spSave = AppSharedPrefs.getInstance(context);
        spSave.put("OnSpelling", arrayData);
    }


    public static void setSpellingOFFForVoice(Context context, String res) {

        if (context == null)
            return;

        Gson gson = new Gson();
        ArrayList<String> allOn = new ArrayList<>();
        String arrayData = "";
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("OFFSpelling") == null) {
            allOn.add(res.toLowerCase());
        } else {
            allOn = gson.fromJson(String.valueOf(sp.get("OFFSpelling")).toLowerCase(), new ArrayList<String>().getClass());
            if (!allOn.contains(res.toLowerCase())) {
                allOn.add(res.toLowerCase());
                Utils.DEBUG("String " + res + " is added successfully.");
            }
        }

        arrayData = gson.toJson(allOn);
        AppSharedPrefs spSave = AppSharedPrefs.getInstance(context);
        spSave.put("OFFSpelling", arrayData);
    }

    public static String getGroupAddressForVoice(Context context) {

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        if (sp.get("groupvoiceaddress") == null) {
            return "FFFF";
        } else {
            return (String) sp.get("groupvoiceaddress");
        }

    }

    public static void setEnableLogs(Context context, boolean value) {

        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_enable_logs_value), value);

    }


    public static boolean getEnableLogs(Context context) {

        if (context == null)
            return false;

        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);

        Object value = sp.get(context.getString(R.string.key_enable_logs_value));
        if (value instanceof String || value == null) {
            return false;
        }
        return (boolean) (sp.get(context.getString(R.string.key_enable_logs_value)));

    }

    public static void runLayoutAnimation(final RecyclerView recyclerView, RecyclerView.Adapter provisionedRecyclerAdapter) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(provisionedRecyclerAdapter);
        recyclerView.scheduleLayoutAnimation();
    }
    public static void setVendorModelTabCommand(Context context, boolean setReliable) {
        if (context == null)
            return;
        AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
        sp.put(context.getString(R.string.key_isVendorModelTabCommand), setReliable);
    }

    public static boolean isVendorModelTabCommand(Context context) {
        if (context == null)
            return false;
        try {
            AppSharedPrefs sp = AppSharedPrefs.getInstance(context);
            //return (boolean) sp.get(context.getString(R.string.key_isVendorModelCommand));
            Object obj = sp.get(context.getString(R.string.key_isVendorModelTabCommand));
            if (obj != null) {
                return (boolean) sp.get(context.getString(R.string.key_isVendorModelTabCommand));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }


}






