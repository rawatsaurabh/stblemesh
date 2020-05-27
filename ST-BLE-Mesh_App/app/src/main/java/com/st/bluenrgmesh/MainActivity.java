/**
 * *****************************************************************************
 *
 * @file MainActivity.java
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
 * BlueNRG-Mesh is based on MotorolaÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 * <p>
 * *****************************************************************************
 */

package com.st.bluenrgmesh;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.msi.moble.ApplicationParameters;
import com.msi.moble.Capabilities;
import com.msi.moble.CustomProvisioning;
import com.msi.moble.Device;
import com.msi.moble.DeviceCollection;
import com.msi.moble.Provisioner;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;
import com.msi.moble.mobleSettings;
import com.msi.moble.mobleStatus;
import com.st.bluenrgmesh.callbacks.HeartBeatCallbacks;
import com.st.bluenrgmesh.utils.ModelRepository;
import com.st.bluenrgmesh.fota.FotaActivity;
import com.st.bluenrgmesh.fota.OTAModeFragment;
import com.st.bluenrgmesh.logger.JsonUtil;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.logger.RepositoryObserver;
import com.st.bluenrgmesh.logger.Subject;
import com.st.bluenrgmesh.logger.UserDataRepository;
import com.st.bluenrgmesh.models.clouddata.CloudResponseData;
import com.st.bluenrgmesh.models.clouddata.LoginData;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Group;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.models.meshdata.settings.NodeSettings;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.services.ModelBindingService;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.AppSingletonOOBDialog;
import com.st.bluenrgmesh.utils.MsgDialogLoader;
import com.st.bluenrgmesh.utils.MyDrawerLayout;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.logger.LoggerFragment;
import com.st.bluenrgmesh.view.fragments.other.About;
import com.st.bluenrgmesh.view.fragments.other.appintro.IntroFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.DeviceInfoFragment;
import com.st.bluenrgmesh.view.fragments.setting.AppSettingsFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.CapabilitySelectionFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.ConfigurationFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.LoadConfigFeaturesFragment;
import com.st.bluenrgmesh.view.fragments.other.SplashScreenFragment;
import com.st.bluenrgmesh.view.fragments.base.BFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.ChangePasswordFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.LoginDetailsFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.SignUpFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.ModelMenuFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.sensor.SensorConfigFragment;
import com.st.bluenrgmesh.view.fragments.setting.managekey.AddAppKeyFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.AddGroupFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;
import com.st.bluenrgmesh.view.fragments.setting.ExchangeConfigFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.GroupSettingFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.NodeSettingFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.ConnectionSetupFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.MainViewPagerFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ModelTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.UnprovisionedFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Provide the main activity of the Application.
 */
public class MainActivity extends AppCompatActivity implements RepositoryObserver {

    private static final int PERMISSION_SETTING = 3000;
    public static final int SUBSCRIPTION_CASE = 0;
    public static final int PUBLICATION_CASE = 1;
    public static int mCid = 0;
    public LostConnectionDialog mLostConnectionDialog = new LostConnectionDialog(this);
    public UnknownDeviceNotificationDialog mUnknownDeviceDialog = new UnknownDeviceNotificationDialog(this);
    public IdentifyDialog mIdentifyDialog = new IdentifyDialog(this);
    public MyHandler mHandler = new MyHandler(this);
    public static final int MSG_UPDATE_LIST = 0;
    public static final int MSG_TOAST = 1;
    public static final int MSG_CONFIG = 2;
    public static final int MSG_DEVICE_APPEARED = 3;
    public static final int MSG_EXCEPTION = 4;
    public static final int MSG_HIDE_PROGRESS = 5;
    public final static int MSG_ENABLE_BLUETOOTH = 7;
    public final static int MSG_UNKNOWN_DEVICE_NOTIFICATION = 8;
    public static final int MSG_UPDATE_RSSI = 9;
    public static final int FILE_SELECT_CODE = 10;
    public final static int MSG_UPDATE_NETWORK_STATUS = 11;
    public final static int MSG_OUT_OF_RANGE = 12;
    private static final int PROVSIONING_RESULT_CODE = 200;
    public static final int UPDATE_GROUP_DATA_RESULT_CODE = 101;
    public static final int UNPROVISION_SUCCESS_RESULT_CODE = 102;
    public BottomNavigationView navigation;
    public static final int PROVISIONING_IDENTIFY_DURATION = 10;
    public static final int COMPLETION_DELAY = 2000; //User Configurable as per Different Phones
    public static int mCustomGroupId = 0xC004; //This needs to be chenaged for every new group.Increment it.
    public String elementUnicast_Model = null;
    public int sensorPropertyId;
    public int nodePositionSelected = 0;
    public static boolean isCustomAppKeyShare = false;
    public static mobleSettings mSettings;
    public String mAutoAddress;
    public String mAutoName;
    public DeviceEntry mAutoDevice;
    DeviceCollection collectionD;
    public int provisioningStep;
    public boolean isCustomProxy = false;
    public boolean rel_unrel = false;
    public static final int PERMISSION_REQUEST_CODE = 1;
    public boolean mProvisioningInProgress = false;
    public int count = 0;
    final static int MSG_UPDATE_RELIABLE_NODE_ROW = 13;
    public MeshRootClass meshRootClass;
    public int elementsSize;
    public Nodes currentNode;
    public CustomProvisioning mcp;
    public AppDialogLoader loader;
    public int nodeNumber;
    private MyDrawerLayout drawer;
    private NavigationView navigationView;
    private static final int ENABLE_BT_ACTIVITY = 4;
    public int tabSelected;
    private View headerView;
    private Vibrator vibrator;
    private TextView txtLogin;
    private TextView txtSignUp;
    private RelativeLayout lytShareConfig;
    private RelativeLayout lytLoadConfig;
    private RelativeLayout lytForgotNetwork;
    private RelativeLayout lytAbout;
    private RelativeLayout lytAppSettings;
    private RelativeLayout lytPrivacyPolicy;
    //private RelativeLayout lytDeviceInfo;
    private RelativeLayout lytHelp;
    private RelativeLayout lytNotLoggedIn;
    private RelativeLayout lytLoggedIn, loggerRL;
    private TextView txtLogout;
    private TextView txtLoginName;
    private Integer mGreetingTip = null;
    public static boolean isFromCreateNetwork;
    public static boolean isFromJoinNetwork;
    public static int provisionerUnicastLowAddress = 1;        ////default provisioner low range
    public static int provisionerUnicastHighAddress = 100;     ////default provisioner high range
    public static int provisionerGroupLowAddress = 49153;     ////default provisioner high range
    public static int provisionerGroupHighAddress = 49353;     ////default provisioner high range
    public ArrayList<Nodes> mUnprovisionedList = new ArrayList<>();
    public static boolean isProvisioningProcessLive = false;
    public boolean isTabUpdating = true;
    private RelativeLayout lytCloudInteraction;
    //private Switch vendor_generic_switch;
    private RelativeLayout lytExchngMeshConfig;
    private TextView txtForgotPassword;
    public static mobleNetwork network;
    public UserApplication app;
    private boolean is_bluetooth_pop = false;
    public String name;
    public int element_counter = 2; //element_counter is treated as element address
    private boolean tabUiInitialised = false;
    public Subject mUserDataRepository;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isAdvised = false;
    public int PROVISIONED_TAB = 1;
    //private Switch reliable_switch;
    private Future<DeviceCollection> futureED = null;
    public mobleSettings.Identifier identifier;
    public static boolean isPublicKeyEnabled = false;
    public Uri imageUri;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int CAMERA_REQUEST = 500;
    private static final String TAG = "API123";
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private File MainLogDirectory;
    private File LogDirectory;
    private File logFileName = null;
    private Boolean fileloggingEnable = true;
    BluetoothConnectionReceiver bluetoothConnectionReceiver;
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        == BluetoothAdapter.STATE_OFF) {
                    is_bluetooth_pop = false;
                    enableBluetooth();
                }
            }
        }
    };

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(action)) {
                //enableBluetooth();
                // Refresh main activity upon close of dialog box
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finishAffinity();
            }
        }
    };

    BroadcastReceiver internetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utils.isOnline(context)) {
                showInternetMsg(false);
            } else {
                showInternetMsg(true);
            }
        }
    };
    private boolean isExit = false;
    public boolean startRssiUpdate = false;
    private MainViewPagerFragment mainViewPagerFragment;
    private Dialog mobleMsgDialog;
    private TextView txtMsgHeading;
    private TextView txtMsg;
    private TextView txtInternetWarning;
    private Button butOk;
    private Dialog proxydialog;
    private MsgDialogLoader proxyLoader;
    private RelativeLayout relUpgradeFota,relservicemanager;
    public Provisioner provisionerOOB;
    public Capabilities capabilities;
    public static Animation in_animation;
    public BottomSheetBehavior sheetBehavior;
    public LinearLayout bottom_sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        registerObersever();

        Utils.setFwUpgrade(MainActivity.this, false);
        Utils.contextMainActivity = MainActivity.this;
        loader = AppDialogLoader.getLoader(MainActivity.this);

        getViewReference();
        registerReceiver();
        updateJsonData();
        moveToSplashFragment();
        Utils.setTIDValue(this,0);
    }

    public void restartApp() {
        //android.os.Process.killProcess(android.os.Process.myPid());
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(i);
    }

    private void moveToSplashFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment f = new SplashScreenFragment();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.lytMain, f);
        transaction.commit();
    }

    public void moveToIntroFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment f = new IntroFragment();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.lytMain, f);
        //transaction.commit();
        transaction.commitAllowingStateLoss();
    }

    public void moveToTabFragment() {

        if (!tabUiInitialised) {
            tabUiInitialised = true;
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment f = new MainViewPagerFragment();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.add(R.id.lytMain, f, new MainViewPagerFragment().getClass().getName());
            transaction.commitAllowingStateLoss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (internetReceiver != null && getResources().getBoolean(R.bool.bool_isCloudFunctionality)) {
                        registerReceiver(internetReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                    }
                    mainViewPagerFragment = (MainViewPagerFragment) getSupportFragmentManager().findFragmentByTag(new MainViewPagerFragment().getClass().getName());
                }
            }, 300);
        }
    }

    private void registerReceiver() {

        if (bluetoothReceiver != null) {
            registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
        if (gpsReceiver != null) {
            registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        }
        bluetoothConnectionReceiver = new BluetoothConnectionReceiver();
        registerReceiver(bluetoothConnectionReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));

    }

    public void enablePermissions() {


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.showToast(this, getResources().getString(R.string.str_ble_not_supported));
            finish();
        }
        if (!checkGPS()) {
            //turn on gps from onResume
            //turnGPSOn();
        } else if (BluetoothAdapter.getDefaultAdapter().getState() != BluetoothAdapter.STATE_ON) {
            enableBluetooth();
        } else {

            moveToTabFragment();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    startNetwork();
                }
            });
        }
    }

    public void enableBluetooth() {
        if (mProvisioningInProgress) {
            mProvisioningInProgress = false;
            mSettings.cancel();
        }

        if (!is_bluetooth_pop) {
            is_bluetooth_pop = true;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Utils.showToast(MainActivity.this, "Device doesn't support bluetooth.");
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, ENABLE_BT_ACTIVITY);
                }
            }

        }
    }

    public boolean checkGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GpsStatus) {
            return getLocationMode(MainActivity.this) != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            return GpsStatus;
        }
    }

    private static int getLocationMode(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
    }

    public void startNetwork() {

        LoginData loginData = null;
        try {
            loginData = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getLoginData(MainActivity.this)), LoginData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        try {
            if (Utils.isUserLoggedIn(MainActivity.this) && getResources().getBoolean(R.bool.bool_isCloudFunctionality)) {
                //restart session
                if (Utils.isOnline(MainActivity.this)) {
                    //Utils.showToast(MainActivity.this, "Establishing cloud network...");
                    callLoginApi(loginData.getUserName(), loginData.getUserPassword());
                } else {
                    resumeNetworkAndCallbacks("", meshRootClass.getMeshUUID(), false);
                }

            } else {
                //Utils.showToast(MainActivity.this, "Establishing local network...");
                if (Utils.getNetKey(MainActivity.this) == null) {

                    if (meshRootClass == null) {
                        //first
                        createNewNetwork(getString(R.string.str_ble_mesh_label), "", true);
                    }
                } else {

                    if (Utils.isRestart(MainActivity.this)) {
                        Utils.setRestart(MainActivity.this, false);
                        createNetworkAfterImport();
                    } else {
                        resumeNetworkAndCallbacks("", meshRootClass.getMeshUUID(), false);
                    }

                }
            }

            ModelRepository.getInstance();

        } catch (Exception e) {
        }
        InitLog(Utils.getEnableLogs(this));

    }

    private void getViewReference() {

        if(!Utils.isAppInit(this)||Utils.isShowIntroScreen(this))
        {
            Utils.setAppInit(this, true);
            Utils.setShowIntroScreen(this, true);
        }
        else if(Utils.isAppInit(this)||!Utils.isShowIntroScreen(this))
        {
            Utils.setAppInit(this, true);
            Utils.setShowIntroScreen(this, false);
        }

       /* else if(Utils.isAppInit(this)){
            Utils.setShowIntroScreen(this,false);
            boolean toShow = Utils.isShowIntroScreen(this);
           // Utils.setShowIntroScreen(this,false);
            if (toShow){
                Utils.setShowIntroScreen(this,true);
            }
        }*/

        in_animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_animation);
        vibrator = Utils.getVibrator(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imgDrawerIcon = (ImageView) findViewById(R.id.imgActionBarDrawerIcon);
        imgDrawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
        drawer = (MyDrawerLayout) findViewById(R.id.drawer_layout);
        //setDrawableLockMode(drawer, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {

            @Override
            public void onDrawerClosed(View drawerView) {

                //super.onDrawerClosed(drawerView);
                closeDrawer();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //super.onDrawerOpened(drawerView);
                openDrawer();
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        lytCloudInteraction = (RelativeLayout) headerView.findViewById(R.id.lytCloudInteraction);
        lytCloudInteraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                //Utils.moveToFragment(MainActivity.this, new CloudInteractionFragment(), null, 0);

            }
        });
        relservicemanager = (RelativeLayout) headerView.findViewById(R.id.relservicemanager);
        relUpgradeFota = (RelativeLayout) headerView.findViewById(R.id.relUpgradeFota);
        relUpgradeFota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //network.close();
                //Utils.setFwUpgrade(MainActivity.this, true);
                //unregisterReceiver(internetReceiver);
                //unregisterReceiver(bluetoothReceiver);
                //unregisterReceiver(gpsReceiver);
                //restartApp();

             //   startActivity(new Intent(MainActivity.this, FotaActivity.class));

             closeDrawer();
                Utils.moveToFragment(MainActivity.this, new OTAModeFragment(), meshRootClass.getNodes(), 0);


            }
        });

        relservicemanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //network.close();
                //Utils.setFwUpgrade(MainActivity.this, true);
                //unregisterReceiver(internetReceiver);
                //unregisterReceiver(bluetoothReceiver);
                //unregisterReceiver(gpsReceiver);
                //restartApp();

                startActivity(new Intent(MainActivity.this, FotaActivity.class));

                //   closeDrawer();
                // Utils.moveToFragment(MainActivity.this, new OTAModeFragment(), meshRootClass.getNodes(), 0);


            }
        });
        lytNotLoggedIn = (RelativeLayout) headerView.findViewById(R.id.lytNotLoggedIn);
        lytLoggedIn = (RelativeLayout) headerView.findViewById(R.id.lytLoggedIn);
        txtForgotPassword = (TextView) headerView.findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Utils.moveToFragment(MainActivity.this, new ChangePasswordFragment(), null, 0);
            }
        });
        txtLoginName = (TextView) headerView.findViewById(R.id.txtLoginName);
        txtLogout = (TextView) headerView.findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout api
                closeDrawer();
                Utils.DEBUG("Userkey : " + Utils.getUserLoginKey(MainActivity.this));
                callLogoutApi(Utils.getUserLoginKey(MainActivity.this));
            }
        });
        txtLogin = (TextView) headerView.findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                if (getResources().getBoolean(R.bool.bool_isCloudFunctionality)) {
                    Utils.moveToFragment(MainActivity.this, new LoginDetailsFragment(), null, 0);
                } else {
                    Utils.showPopUpForMessage(MainActivity.this, getString(R.string.str_error_Gatt_Not_Responding));
                }

            }
        });
        txtSignUp = (TextView) headerView.findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                if (getResources().getBoolean(R.bool.bool_isCloudFunctionality)) {
                    Utils.moveToFragment(MainActivity.this, new SignUpFragment(), null, 0);
                } else {
                    Utils.showPopUpForMessage(MainActivity.this, getString(R.string.str_error_Gatt_Not_Responding));
                }
            }
        });
        loggerRL = (RelativeLayout) headerView.findViewById(R.id.loggerRL);
        lytShareConfig = (RelativeLayout) headerView.findViewById(R.id.lytShareConfig);
        lytLoadConfig = (RelativeLayout) headerView.findViewById(R.id.lytLoadConfig);
        lytForgotNetwork = (RelativeLayout) headerView.findViewById(R.id.lytForgotNetwork);
        lytAbout = (RelativeLayout) headerView.findViewById(R.id.lytAbout);
        loggerRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent  i = new Intent(MainActivity.this, LoggerActivity.class);*/
                Utils.moveToFragment(MainActivity.this, new LoggerFragment(), null, 0);
            }
        });
        lytShareConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                Utils.sendDataOverMail(MainActivity.this);
            }
        });
        lytLoadConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                Utils.action_config_setting(MainActivity.this);
            }
        });
        lytForgotNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPopUp(MainActivity.this, new MainActivity().getClass().getName(), true, true, getResources().getString(R.string.str_delete_mesh_data), getResources().getString(R.string.str_setting_reset));
            }
        });
        lytAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                Utils.moveToFragment(MainActivity.this, new About(), null, 0);

            }
        });
        /*lytDeviceInfo = (RelativeLayout) headerView.findViewById(R.id.phone_info);
        lytDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                Utils.moveToFragment(MainActivity.this, new DeviceInfoFragment(), null, 0);

            }
        });*/
        lytAppSettings = (RelativeLayout) headerView.findViewById(R.id.lytAppSettings);
        lytAppSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrator != null) vibrator.vibrate(40);
                Utils.moveToFragment(MainActivity.this, new AppSettingsFragment(), null, 0);
            }
        });

        lytHelp = (RelativeLayout) headerView.findViewById(R.id.lytHelp);
        lytHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) vibrator.vibrate(40);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.str_helppdf)));
                startActivity(intent);
            }
        });

        lytPrivacyPolicy = (RelativeLayout) headerView.findViewById(R.id.lytPrivacyPolicy);
        lytPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_proxy);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                CardView lytPrivacyPolicy = (CardView) dialog.findViewById(R.id.lytPrivacyPolicy);
                lytPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vibrator != null) vibrator.vibrate(40);

                        String url = getString(R.string.str_policy_html);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                CardView lytUrRights = (CardView) dialog.findViewById(R.id.lytUrRights);
                lytUrRights.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vibrator != null) vibrator.vibrate(40);

                        String url = getString(R.string.str_trust_hash);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                dialog.show();
            }
        });

        /*vendor_generic_switch = (Switch) headerView.findViewById(R.id.vendor_generic_switch);*/
        /*reliable_switch = (Switch) headerView.findViewById(R.id.reliable_switch);
        if (Utils.isReliableEnabled(MainActivity.this)) {
            reliable_switch.setChecked(true);
        } else {
            reliable_switch.setChecked(false);
        }*/

        /*if (Utils.isVendorModelCommand(MainActivity.this)) {
            vendor_generic_switch.setChecked(true);
        } else {
            vendor_generic_switch.setChecked(false);
        }

        vendor_generic_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean vendorModelSelection) {

                Utils.setVendorModelCommand(MainActivity.this, vendorModelSelection);
            }
        });*/

        lytExchngMeshConfig = (RelativeLayout) headerView.findViewById(R.id.lytExchngMeshConfig);
        lytExchngMeshConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.moveToFragment(MainActivity.this, new ExchangeConfigFragment(), null, 0);
            }
        });

        /*reliable_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rel_unrel = b;
                Utils.setReliable(MainActivity.this, b);
            }
        });*/

        Switch user_help_switch = (Switch) headerView.findViewById(R.id.user_help_switch);
        user_help_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    Utils.setHelpGuideData(getApplicationContext(), true);
                } else {
                    Utils.setHelpGuideData(getApplicationContext(), false);
                }
            }
        });

        initMobleMsgDialog();
        ((UserApplication) getApplication()).setActivity(this);
        Utils.controlNavigationDrawer(MainActivity.this, null, DrawerLayout.LOCK_MODE_UNLOCKED);
        Utils.setProxyNode(MainActivity.this, null);

        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottom_sheet.setVisibility(View.GONE);

        txtInternetWarning = (TextView) findViewById(R.id.txtInternetWarning);

    }

    public void closeDrawer() {

        drawer.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        Utils.DEBUG("MainActivity >> openDrawer() >> called ");
        //first check user login status and change UI accordingly
        updateUIForLogin();
        drawer.openDrawer(GravityCompat.START);
    }

    private void updateUIForLogin() {

        if (Utils.isUserLoggedIn(this) && getResources().getBoolean(R.bool.bool_isCloudFunctionality)) {
            lytLoggedIn.setVisibility(View.VISIBLE);
            lytNotLoggedIn.setVisibility(View.GONE);
            try {
                LoginData loginData = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getLoginData(this)), LoginData.class);
                txtLoginName.setText("Welcome " + loginData.getUserName() + " !!");
            } catch (Exception e) {
                Utils.ERROR("MainActivity >> Error while parsing json : " + e.toString());
            }

        } else {
            lytLoggedIn.setVisibility(View.GONE);
            lytNotLoggedIn.setVisibility(View.VISIBLE);
        }

    }

    /* Used to lock navigation drawer*/
    public void setDrawableLockMode(DrawerLayout drawer, int lock) {
        drawer.setDrawerLockMode(lock);
    }

    public void updateJsonData() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    String meshData = null;
                    boolean isSyncSuccesfull = false;
                    if(getResources().getBoolean(R.bool.bool_readFromAssets))
                    {
                        isSyncSuccesfull = Utils.syncJson(MainActivity.this, getResources().getBoolean(R.bool.bool_readFromAssets));
                        meshData = Utils.getBLEMeshDataFromLocal(MainActivity.this);
                    }
                    else {
                        meshData = Utils.getBLEMeshDataFromLocal(MainActivity.this);
                    }

                    if (meshData != null || isSyncSuccesfull) {
                        meshRootClass = ParseManager.getInstance().fromJSON(new JSONObject(meshData), MeshRootClass.class);
                        Utils.DEBUG(">> Json Data : " + meshData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {

        loader.dismiss();

        if (proxydialog != null && proxydialog.isShowing()) {
            proxydialog.dismiss();
        }

        try {
            if (internetReceiver != null && bluetoothReceiver != null) {
                unregisterReceiver(internetReceiver);
                unregisterReceiver(bluetoothReceiver);
                unregisterReceiver(gpsReceiver);
                unregisterReceiver(bluetoothConnectionReceiver);
            }
            JsonUtil.ClearLoggerFile(this);
            if (HeartBeatCallbacks.heartBeatMap != null) {
                HeartBeatCallbacks.heartBeatMap.clear();
                HeartBeatCallbacks.heartBeatMap = null;
            }
        } catch (IllegalArgumentException e) {
        }
        Utils.setTIDValue(this,0);

        super.onDestroy();
    }

    public void resumeNetworkAndCallbacks(final String meshName, final String meshUUIDD,
                                          final boolean isNewJoiner) {

        if (BluetoothAdapter.getDefaultAdapter().getState() != BluetoothAdapter.STATE_ON) {
            enableBluetooth();
        } else if (meshRootClass != null) {
            resumeAllCallBacks(meshName, meshUUIDD, isNewJoiner);
        } else {
        }

        if (mGreetingTip != null) {
            Toast.makeText(this, mGreetingTip, Toast.LENGTH_SHORT).show();
        }

    }

    public void resumeAllCallBacks(String meshName, String meshUUIDD,
                                   boolean isNewJoiner) {

        Utils.setProxyNode(MainActivity.this, null);
        Utils.setProxyUUID(MainActivity.this, null);

        try {
            if (Utils.getProvisionerUnicastLowAddress(MainActivity.this) != null) {
                provisionerUnicastLowAddress = Integer.parseInt(Utils.getProvisionerUnicastLowAddress(MainActivity.this));
            }

            Utils.DEBUG(">>>Current Provisioner Address : " + provisionerUnicastLowAddress);
            //Utils.showToast(MainActivity.this, "Resuming previous network state...");
            network = mobleNetwork.restoreNetwork(mobleAddress.deviceAddress(provisionerUnicastLowAddress), meshRootClass.getNetKeys().get(0).getKey(), meshRootClass.getAppKeys().get(0).getKey(), ParseManager.getInstance().toJSON(meshRootClass));
            getNetworkStatusResume();
            app = (UserApplication) getApplication();
            app.mConfiguration = new Configuration();
            app.mConfiguration.setmNetwork(network);
            network.advise(mOnDeviceAppearedCallback);
            network.advise(mProxyConnectionEventCallback);
            //   network.advise(mOnProxyAddressChanged);
            network.advise(mOnDongleStateChanged);
            network.advise(onDeviceRssiChangedCallback);
            network.advise(HeartBeatCallbacks.onHeartBeatRecievedCallback);
            network.advise(mMobleError);
            network.advise(onOOB_Output);
            network.advise(onOOB_Input);
            mSettings = network.getSettings();
            if (this.getResources().getBoolean(R.bool.bool_packetSniffing)) {
                network.advise(mWriteLocalCallback);
                network.advise(modelCallback_cb);
            }

            if (((UserApplication) getApplication()).start().failed()) {
                mGreetingTip = R.string.moble_start_fail;
                finish();
            }

            String appKeys = Utils.array2string(mobleNetwork.getaAppKey());
            String networkKey = Utils.array2string(mobleNetwork.getaNetworkKey());
            Utils.setNetKey(MainActivity.this, networkKey);

            if (meshRootClass != null && meshRootClass.getAppKeys() != null) {
                AppKey appKey = meshRootClass.getAppKeys().get(0);
                Utils.setDefaultAppKeyIndex(MainActivity.this, appKey.getIndex());
                byte[] bytes = Utils.hexStringToByteArray(appKey.getKey());
                //Utils.showToast(MainActivity.this, "Default key activated for all events.");
                MainActivity.network.setAppKey(bytes);
            }

            String meshUUID = isNewJoiner ? Utils.generateMeshUUID(MainActivity.this, networkKey) : meshUUIDD;
            if (isNewJoiner) {
                Utils.updateProvisionerNetAppKeys(MainActivity.this, meshRootClass, meshName, meshUUID, appKeys, networkKey, false);
            }
            updateJsonData();

        } catch (Exception e) {
        }
        InitLog(Utils.getEnableLogs(this));

    }

    public void updateProvisionedTab(String nodeData, int intCase) {
        fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), nodeData, intCase, null, false, null);
    }

    public void updateModelTab() {
        fragmentCommunication(new ModelTabFragment().getClass().getName(), Utils.getSelectedModel(MainActivity.this), 0, null, false, null);
    }

    public void unAdviseCallbacks() {
        if (isAdvised) {
            isAdvised = false;
            try {
                Utils.DEBUG(">> RSSI Call Stopped");

                network.unadvise(mOnDeviceAppearedCallback);
                network.unadvise(onDeviceRssiChangedCallback);

            } catch (Exception e) {
            }
        }
    }

    public void adviseCallbacks() {
        Utils.DEBUG(">> RSSI Call Resume");
        if (!isAdvised) {
            isAdvised = true;
            try {
                network.advise(mOnDeviceAppearedCallback);
                network.advise(mProxyConnectionEventCallback);
                network.advise(mOnDongleStateChanged);
                network.advise(onDeviceRssiChangedCallback);
                if (this.getResources().getBoolean(R.bool.bool_packetSniffing)) {

                    network.advise(mWriteLocalCallback);
                    app.mConfiguration.getNetwork().advise(modelCallback_cb);

                }
                // app.start();

            } catch (Exception e) {
            }
        }
    }

    /**
     * Following method used to handle various events w.r.t fragment communication when we need to dynamically update
     * the fragment UI for any fragment screen.
     *
     * @param className
     * @param strData                      : This parameter used to store any string data like : address, model name, UUID
     * @param anyCase_Rssi                 : This parameter can be used as a number data(RSSI value) , a number that represent case type, a tab number.
     * @param deviceDiscovered
     * @param isScrollViewPager_anyBoolean : This parameter used to swipe tab page when needed automatically by system.
     * @param status
     */
    public void fragmentCommunication(final String className, final String strData, final int anyCase_Rssi, final Nodes deviceDiscovered,
                                      final boolean isScrollViewPager_anyBoolean, final ApplicationParameters.Status status) {

        try {
            if (className.equals(new UnprovisionedFragment().getClass().getName())) {
                if (isScrollViewPager_anyBoolean) {
                    //provisioning case
                    mainViewPagerFragment.updateFragmentUi(strData, /*mRssi*/anyCase_Rssi, className, null, null, true);
                } else {
                    if (strData == null || /*mRssi*/anyCase_Rssi == 0) {
                        //update node
                        mainViewPagerFragment.updateFragmentUi(null, 0, className, deviceDiscovered, null, false);
                    } else {
                        //update rssi
                        mainViewPagerFragment.updateFragmentUi(strData, /*mRssi*/anyCase_Rssi, className, null, null, false);
                    }
                }
            } else if (className.equals(new ProvisionedTabFragment().getClass().getName())) {
                mainViewPagerFragment.updateFragmentUi(strData, anyCase_Rssi, className, deviceDiscovered, null, false);

            } else if (className.equals(new GroupTabFragment().getClass().getName())) {
                mainViewPagerFragment.updateFragmentUi(strData, 0, className, null, null, false);

            } else if (className.equals(new ModelTabFragment().getClass().getName())) {
                if (isScrollViewPager_anyBoolean) {
                    mainViewPagerFragment.scrollViewPager(/*tab number*/anyCase_Rssi);
                } else {
                    //strData : modelname , sensorData
                    mainViewPagerFragment.updateFragmentUi(/*model name*/strData, anyCase_Rssi, className, null, null, false);
                }
            } else if (className.equals(new GroupSettingFragment().getClass().getName())) {
                GroupSettingFragment groupSettingFragment = (GroupSettingFragment) getSupportFragmentManager().findFragmentByTag(new GroupSettingFragment().getClass().getName());
                groupSettingFragment.updateFragmentUi(strData, anyCase_Rssi, status);
            } else if (className.equals(new AddGroupFragment().getClass().getName())) {
                AddGroupFragment addGroupFragment = (AddGroupFragment) getSupportFragmentManager().findFragmentByTag(new AddGroupFragment().getClass().getName());
                addGroupFragment.updateFragmentUi(strData, anyCase_Rssi, status);

            } else if (className.equals(new SensorConfigFragment().getClass().getName())) {
                SensorConfigFragment sensorConfigFragment = (SensorConfigFragment) getSupportFragmentManager().findFragmentByTag(new SensorConfigFragment().getClass().getName());
                if (anyCase_Rssi == 1) {
                    //start action bar refresh
                    sensorConfigFragment.updateSensorCallback();
                } else {
                    //anyCase_Rssi : propertyId
                    //strData contains sensor data
                    sensorConfigFragment.updateFragmentUi(strData);
                }
            } else if (className.equals(new LoadConfigFeaturesFragment().getClass().getName())) {
                LoadConfigFeaturesFragment loadConfigFeaturesFragment = (LoadConfigFeaturesFragment) getSupportFragmentManager().findFragmentByTag(new LoadConfigFeaturesFragment().getClass().getName());
                //loadConfigFeaturesFragment.updateConfiguration();
            } else if (className.equals(new ElementSettingFragment().getClass().getName())) {
                ElementSettingFragment elementSettingFragment = (ElementSettingFragment) getSupportFragmentManager().findFragmentByTag(new ElementSettingFragment().getClass().getName());
                if (anyCase_Rssi == getResources().getInteger(R.integer.CUSTOM_APP_KEY_BIND)) {
                    elementSettingFragment.updateModelAfterBindingKey(status, true);
                } else if (anyCase_Rssi == getResources().getInteger(R.integer.CUSTOM_APP_KEY_UNBIND)) {
                    elementSettingFragment.updateModelAfterBindingKey(status, false);
                } else if (anyCase_Rssi == getResources().getInteger(R.integer.APP_KEY_DELETE)) {
                    elementSettingFragment.updateNodeAfterDeletingKey(status, false);
                } else if (anyCase_Rssi == getResources().getInteger(R.integer.ELEMENT_SUBSCRIPTION_CASE)) {
                    elementSettingFragment.updateSubscribedData(status);
                } else if (anyCase_Rssi == getResources().getInteger(R.integer.ELEMENT_APPKEY_DIALOG_CASE)) {
                    elementSettingFragment.openAppKeyDialog();
                } else if (anyCase_Rssi == getResources().getInteger(R.integer.ELEMENT_NAME_UPDATE_CASE)) {
                    elementSettingFragment.updateElementName();
                } else if (anyCase_Rssi == getResources().getInteger(R.integer.ELEMENT_PUBLICATION_CASE)) {
                    //publication case
                    elementSettingFragment.updateUi(status);
                }

            } else if (className.equals(new AddAppKeyFragment().getClass().getName())) {
                AddAppKeyFragment appKeyFragment = (AddAppKeyFragment) getSupportFragmentManager().findFragmentByTag(new AddAppKeyFragment().getClass().getName());
                if (status == ApplicationParameters.Status.SUCCESS) {
                    appKeyFragment.updateRecyler();
                } else {
                    appKeyFragment.shareNewAppKey();
                }

            } else if (className.equals(new ConnectionSetupFragment().getClass().getName())) {
                ConnectionSetupFragment connectionSetupFragment = (ConnectionSetupFragment) getSupportFragmentManager().findFragmentByTag(new ConnectionSetupFragment().getClass().getName());
                if (anyCase_Rssi == -1) {
                    connectionSetupFragment.onProvisioningCompleted();
                } else if (anyCase_Rssi == -2) {
                    connectionSetupFragment.onGettingCapabilities();
                } else if (anyCase_Rssi == -3) {
                    //public key case
                    connectionSetupFragment.updateCapabilityUI(capabilities);
                } else if (anyCase_Rssi == -4) {
                    //no public key
                    connectionSetupFragment.updateCapabilityUI(capabilities);
                    identifier.setIdentified(true, capabilities);
                } else {

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    connectionSetupFragment.updateProgressBar(anyCase_Rssi);
                                }
                            });
                        }
                    });


                }

            }  else if (className.equals(new CapabilitySelectionFragment().getClass().getName())) {
                CapabilitySelectionFragment capabilitySelectionFragment = (CapabilitySelectionFragment) getSupportFragmentManager().findFragmentByTag(new CapabilitySelectionFragment().getClass().getName());
                if(anyCase_Rssi == 0)
                {
                    capabilitySelectionFragment.initialiseDetectorsAndSources();
                }
                else
                {
                    capabilitySelectionFragment.setBarCodeData();
                }

            }
        } catch (Exception e) {
            Utils.DEBUG("Fragment Communication Failed");
        }
    }

    private static class DialogListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
        private Runnable clickTask;
        private Runnable cancelTask;

        DialogListener(Runnable clickListener, Runnable cancelListener) {
            clickTask = clickListener;
            cancelTask = cancelListener;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (clickTask != null) {
                clickTask.run();
                clickTask = null;
            }
            cancelTask = null;
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (cancelTask != null) {
                cancelTask.run();
                cancelTask = null;
            }
            clickTask = null;
        }
    }

    @Override
    public void onBackPressed() {

        Utils.closeKeyboard(MainActivity.this, null);
        //updateJsonData();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count > 0) {
            FragmentManager.BackStackEntry a = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);//top
            Fragment baseFrag = (Fragment) getSupportFragmentManager().findFragmentByTag(a.getName());
            if (baseFrag instanceof BFragment) {
                ((BFragment) baseFrag).onBackEventPre();
            }
            //control navigation drawer
            //Utils.controlNavigationDrawer(MainActivity.this, a.getName(),DrawerLayout.LOCK_MODE_UNLOCKED);

            if (baseFrag.getClass().getName().equalsIgnoreCase(new ConnectionSetupFragment().getClass().getName())) {
                if(isProvisioningProcessLive)
                {
                    showAlertDialog_TerminateProvisioning();
                }
                else {
                    getSupportFragmentManager().popBackStack();
                }
            }
            else {
                getSupportFragmentManager().popBackStack();
            }

            //now update action bar, depending upon screen
            try {
                FragmentManager.BackStackEntry entry = null;
                Fragment baseFragment = null;
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

                    if (baseFrag.getClass().getName().equalsIgnoreCase(new AddGroupFragment().getClass().getName())) {
                        if (!Utils.checkGrpHasSubElements(MainActivity.this, AddGroupFragment.eleSubscriptionList)) {
                            Utils.removeGroupFromJson(MainActivity.this, AddGroupFragment.addr);
                            Utils.showPopUpForMessage(MainActivity.this, AddGroupFragment.addr + " " + getString(R.string.str_addgroupmsg_label));
                        }
                        fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
                        Utils.updateActionBarForFeatures(MainActivity.this, new GroupTabFragment().getClass().getName());
                        updateJsonData();
                    } else if (baseFrag.getClass().getName().equalsIgnoreCase(new GroupSettingFragment().getClass().getName())) {
                        Utils.updateActionBarForFeatures(MainActivity.this, new GroupTabFragment().getClass().getName());
                    } else if (baseFrag.getClass().getName().equalsIgnoreCase(new ConfigurationFragment().getClass().getName())) {


                    } else if (baseFrag.getClass().getName().equalsIgnoreCase(new SensorConfigFragment().getClass().getName())) {
                        //fragmentCommunication(new ModelTabFragment().getClass().getName(), Utils.getSelectedModel(MainActivity.this), 0, null, false, null);
                        Utils.updateActionBarForFeatures(MainActivity.this, new ModelTabFragment().getClass().getName(), Utils.getSelectedModel(MainActivity.this));
                    } else if (baseFrag.getClass().getName().equalsIgnoreCase(new ModelMenuFragment().getClass().getName())) {
                        Utils.updateActionBarForFeatures(MainActivity.this, new ModelTabFragment().getClass().getName(), Utils.getSelectedModel(MainActivity.this));
                    } else if (baseFrag.getClass().getName().equalsIgnoreCase(new LoadConfigFeaturesFragment().getClass().getName())) {
                        Utils.updateActionBarForFeatures(MainActivity.this, new ProvisionedTabFragment().getClass().getName());

                        fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
                    } else {
                        Utils.updateActionBarForFeatures(this, new MainViewPagerFragment().getClass().getName());
                        //back from login page
                        try {
                            if (!Utils.isUserLoggedIn(MainActivity.this) && meshRootClass.getMeshUUID().equals("")) {
                                Utils.showPopUp(MainActivity.this, new MainActivity().getClass().getName(), true, false, getResources().getString(R.string.str_create_network_msg_label), getResources().getString(R.string.str_login_import_label));
                            }
                        } catch (Exception e) {
                            //Utils.showPopUp(MainActivity.this, new MainActivity().getClass().getName(), true, false, getResources().getString(R.string.str_create_network_msg_label), getResources().getString(R.string.str_login_import_label));
                        }
                    }
                } else {

                    entry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2);
                    baseFragment = (Fragment) getSupportFragmentManager().findFragmentByTag(entry.getName());
                    Utils.updateActionBarForFeatures(this, entry.getName());
                    if (baseFrag instanceof BFragment) {
                        ((BFragment) baseFragment).onFocusEvent();
                    }
                }
            } catch (Exception e) {
            }
            if (baseFrag instanceof BFragment) {
                ((BFragment) baseFrag).onBackEventPost();
            }
            Utils.hideKeyboard(this, baseFrag.getView());
        } else {
            showPopUpForExitApp();
            if (isExit) {
                isExit = false;
                //super.onBackPressed();
                finish();
            }
        }
    }

    private void showAlertDialog_TerminateProvisioning() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Terminate current provisioning process ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utils.terminateProvisioning(MainActivity.this, "Terminating Provisioning Process.");
                        getSupportFragmentManager().popBackStack();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void showPopUpForExitApp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setIcon(getResources().getDrawable(R.drawable.ic_settings_bluetooth_black_24dp))
                .setMessage(getString(R.string.str_exit_app))
                .setCancelable(false)
                .setPositiveButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                isExit = true;
                                onBackPressed();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        if (!MainActivity.this.isFinishing()) {
            alert.show();
        }
    }

    @Override
    protected void onResume() {

        //if user clicks cancel button on gps dialog
        if (!checkGPS()) {
            Utils.turnGPSOn(MainActivity.this);
        }

        if (Utils.isFwUpgrade(MainActivity.this)) {
            Utils.setFwUpgrade(MainActivity.this, false);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateJsonData();
                    registerReceiver();
                    //restartApp();
                    //resumeNetworkAndCallbacks("", meshRootClass.getMeshUUID(), false);
                    createNetworkAfterImport();
                }
            });
        }

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((UserApplication) getApplication()).setActivity(this);

        switch (requestCode) {
            case PERMISSION_SETTING:
                enablePermissions();
                break;
            case ENABLE_BT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    moveToTabFragment();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            startNetwork();
                        }
                    });
                } else {
                    makeToast("Unable to start Bluetooth");
                    UserApplication.trace("System request to turn Bluetooth on has been denied by user");
                    finish();
                }
                break;
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();

                        String path = Utils.getPath(MainActivity.this, uri);

                        ((UserApplication) getApplication()).load(path);
                        ((UserApplication) getApplication()).setActivity(this);

                        if (Utils.checkConfiguration(MainActivity.this)) {
                            ((UserApplication) getApplication()).stop();

                            network.advise(mOnDeviceAppearedCallback);
                            network.advise(mProxyConnectionEventCallback);
//                            network.advise(mOnProxyAddressChanged);
                            network.advise(mOnDongleStateChanged);
                            network.advise(onDeviceRssiChangedCallback);
                            ((UserApplication) getApplication()).start();
                            mProgress.show(MainActivity.this, false);

                            //updateRequest(true);
                        } else {
                            try {
                                createNewNetwork("", meshRootClass.getMeshUUID(), false);
                            } catch (Exception e) {
                            }

                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 600:
            case 700:
                if (resultCode == RESULT_OK) {
                    mProgress.show(MainActivity.this, false);

                    //updateRequest(true);
                }
                break;
            case UPDATE_GROUP_DATA_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    //Update GroupTabFragment to show new added group
                    fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
                }
                break;
            case UNPROVISION_SUCCESS_RESULT_CODE:
                try {
                    if (resultCode == RESULT_OK) {
                        //update unprovision data in all list
                        clearUnprovisionList();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 60:
                break;
            case PROVSIONING_RESULT_CODE:

                break;

            case CAMERA_REQUEST:

                if(resultCode == RESULT_OK)
                {
                    launchMediaScanIntent();
                    fragmentCommunication(new CapabilitySelectionFragment().getClass().getName(), null, 1, null, false, null);
                }

                break;
        }
    }

    private void takeBarcodePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
        imageUri = FileProvider.getUriForFile(MainActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public void clearUnprovisionList() {
        //mData.clear();
        //mDataUnprovisioned.clear();
        mUnprovisionedList.clear();
    }

    public void createNewNetwork(String meshName, String meshUUIDD, boolean isNewJoiner) {

        app = (UserApplication) getApplication();
        app.mConfiguration = new Configuration(mobleAddress.deviceAddress(provisionerUnicastLowAddress));
        network = app.mConfiguration.getNetwork();
        getNetworkStatusBefore();
        network.advise(mOnDeviceAppearedCallback);
        network.advise(mProxyConnectionEventCallback);
//        network.advise(mOnProxyAddressChanged);
        network.advise(mOnDongleStateChanged);
        network.advise(onDeviceRssiChangedCallback);
        network.advise(mWriteLocalCallback);
        network.advise(HeartBeatCallbacks.onHeartBeatRecievedCallback);
        network.advise(mMobleError);
        network.advise(onOOB_Output);
        network.advise(onOOB_Input);
        mSettings = network.getSettings();
        app.start();
        getNetworkStatusAfter();
        mobleAddress address = mobleAddress.groupAddress(((UserApplication) getApplication()).mConfiguration.getGroupMinAvailableAddress() | mobleAddress.GROUP_HEADER);
        app.mConfiguration.addGroup(address, new GroupEntry(getString(R.string.str_default_group_label)));
        app.save();
        updateProvisionerProperties(meshName, meshUUIDD, isNewJoiner);

    }

    public void createNetworkAfterImport() {

        Utils.showToast(MainActivity.this, "Importing Settings");
        app = (UserApplication) getApplication();

        try {
            network = mobleNetwork.restoreNetwork(mobleAddress.deviceAddress(provisionerUnicastLowAddress), meshRootClass.getNetKeys().get(0).getKey(), meshRootClass.getAppKeys().get(0).getKey(), ParseManager.getInstance().toJSON(meshRootClass));
            app.mConfiguration.setmNetwork(network);

            if (meshRootClass != null && meshRootClass.getAppKeys() != null) {
                AppKey appKeyy = meshRootClass.getAppKeys().get(0);
                Utils.setDefaultAppKeyIndex(MainActivity.this, appKeyy.getIndex());
                byte[] bytes = Utils.hexStringToByteArray(appKeyy.getKey());
                //Utils.showToast(MainActivity.this, "Default key activated for all events.");
                network.setAppKey(bytes);
            }

            network.advise(mOnDeviceAppearedCallback);
            network.advise(mProxyConnectionEventCallback);
//        network.advise(mOnProxyAddressChanged);
            network.advise(mOnDongleStateChanged);
            network.advise(onDeviceRssiChangedCallback);
            network.advise(mWriteLocalCallback);
            network.advise(HeartBeatCallbacks.onHeartBeatRecievedCallback);
            network.advise(mMobleError);
            network.advise(onOOB_Output);
            network.advise(onOOB_Input);
            mSettings = network.getSettings();
            app.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        InitLog(Utils.getEnableLogs(this));

    }

    private void getNetworkStatusAfter() {
        if (network.start(this) == mobleStatus.FAIL) {
            mUserDataRepository.getNewDataFromRemote("Network Started Failed!\n" + "Lib Ver=>" + mobleNetwork.getlibversion() + "\nApp Ver=>" + BuildConfig.VERSION_NAME, LoggerConstants.TYPE_NONE);
        } else {
            mUserDataRepository.getNewDataFromRemote("Network Started!\n" + "Lib Ver=>" + mobleNetwork.getlibversion() + "\nApp Ver=>" + BuildConfig.VERSION_NAME, LoggerConstants.TYPE_NONE);
        }
    }

    public void getNetworkStatusBefore() {
        if (app.mConfiguration != null && network != null) {
            mUserDataRepository.getNewDataFromRemote("Network Created!\n" + "Lib Ver=>" + mobleNetwork.getlibversion() + "\nApp Ver=>" + BuildConfig.VERSION_NAME, LoggerConstants.TYPE_NONE);
        } else {
            mUserDataRepository.getNewDataFromRemote("Network failed!\n" + "Lib Ver=>" + mobleNetwork.getlibversion() + "\nApp Ver=>" + BuildConfig.VERSION_NAME, LoggerConstants.TYPE_NONE);

        }
    }

    public void getNetworkStatusResume() {
        if (network != null) {
            mUserDataRepository.getNewDataFromRemote("Network Restored!\n" + "Lib Ver=>" + mobleNetwork.getlibversion() + "\nApp Version=>" + BuildConfig.VERSION_NAME, LoggerConstants.TYPE_NONE);
        } else {
            mUserDataRepository.getNewDataFromRemote("Network Restored failed!\n" + "Lib Ver=>" + mobleNetwork.getlibversion() + "\nApp Version=>" + BuildConfig.VERSION_NAME, LoggerConstants.TYPE_NONE);
        }

    }

    private void updateProvisionerProperties(String meshName, String meshUUIDD, boolean isNewJoiner) {

        String appKeys = Utils.array2string(mobleNetwork.getaAppKey());
        String networkKey = Utils.array2string(mobleNetwork.getaNetworkKey());
        String meshUUID = isNewJoiner ? Utils.generateMeshUUID(MainActivity.this, networkKey) : meshUUIDD;
        Utils.setNetKey(MainActivity.this, networkKey);
        Utils.setDefaultAppKeyIndex(MainActivity.this, 0);
        //setup provisioner
        int provisonerUnicastRange = provisionerUnicastHighAddress - provisionerUnicastLowAddress;
        int provisonerGroupRange = provisionerGroupHighAddress - provisionerGroupLowAddress;
        Utils.setProvisionerUnicastLowAddress(MainActivity.this, String.valueOf(provisionerUnicastLowAddress));
        Utils.setProvisionerUnicastHighAddress(MainActivity.this, String.valueOf(provisionerUnicastHighAddress));
        Utils.setProvisionerGroupLowAddress(MainActivity.this, String.valueOf(provisionerGroupLowAddress));
        Utils.setProvisionerGroupHighAddress(MainActivity.this, String.valueOf(provisionerGroupHighAddress));
        Utils.setProvisionerUnicastRange(MainActivity.this, String.valueOf(provisonerUnicastRange));
        Utils.setProvisionerGroupRange(MainActivity.this, String.valueOf(provisonerGroupRange));
        Utils.onGetNewProvisioner(MainActivity.this);
        Utils.updateProvisionerNetAppKeys(MainActivity.this, meshRootClass, meshName, meshUUID, appKeys, networkKey, true);
        updateJsonData();
        if (!Utils.isUserLoggedIn(MainActivity.this) && getResources().getBoolean(R.bool.bool_isCloudFunctionality)) {
            //Utils.showToast(MainActivity.this, getString(R.string.str_login_to_use_cloud_label));
        }
    }

    public class MyHandler extends Handler {

        private final WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MainActivity activity = mActivity.get();
            Object[] args;
            if (activity == null) return;

            switch (msg.what) {
                case MSG_UPDATE_LIST:

                    break;
                case MSG_HIDE_PROGRESS:
                    long dt = System.currentTimeMillis() - activity.mProgress.startTime;
                    if (dt < progress.PROGRESS_MIN_DELAY) {
                        activity.mHandler.sendEmptyMessageDelayed(MSG_HIDE_PROGRESS, dt);
                        break;
                    }
                    activity.mProgress.hide();
                    break;
                case MSG_CONFIG:
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.autoConfigure();
                        }
                    }, 100);*/
                    break;
                case MSG_TOAST:
                    Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    msg.obj = null;
                    break;
                case MSG_DEVICE_APPEARED:

                    //activity.updateRequest((boolean) msg.obj);
                    break;
                case MSG_EXCEPTION:
                    activity.makeToast("Exception");
                    break;
                case MSG_ENABLE_BLUETOOTH:
                    enableBluetooth();
                    break;
                case MSG_UNKNOWN_DEVICE_NOTIFICATION:
                    activity.mUnknownDeviceDialog.createDialog();
                    break;


                case MSG_UPDATE_RELIABLE_NODE_ROW:
                    fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
                    break;
            }
        }
    }

    public void restartNetwork() {
        //Utils.showToast(MainActivity.this, "Restablishing Network Again.");

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }

        InitLog(Utils.getEnableLogs(this));

    }

    public defaultAppCallback mWriteLocalCallback = new defaultAppCallback() {
        @Override
        public void onWriteLocalData(mobleAddress peer, mobleAddress dst, Object cookies, short offset, byte count, byte[] data) {

            Utils.DEBUG(" VendorWrite Async CallBack source  : " + peer.toString());
            Utils.DEBUG(" VendorWrite Async CallBack destination  : " + dst.toString());
            Utils.DEBUG(" VendorWrite Async CallBack data : " + Utils.array2string(data));
            mUserDataRepository.getNewDataFromRemote("Vendor Async CallBack source  ==>" + peer, LoggerConstants.TYPE_RECEIVE);
            mUserDataRepository.getNewDataFromRemote("Vendor Async CallBack destination  ==>" + dst, LoggerConstants.TYPE_RECEIVE);
            mUserDataRepository.getNewDataFromRemote("Vendor Async CallBack data ==>" + Utils.array2string(data), LoggerConstants.TYPE_RECEIVE);
        }
    };

    public defaultAppCallback modelCallback_cb = new defaultAppCallback() {
        @Override
        public void modelCallback(ApplicationParameters.Address src, ApplicationParameters.Address dst) {

            Utils.DEBUG(" GenericOnOff Async CallBack source : " + src);
            Utils.DEBUG(" GenericOnOff Async CallBack  dst : " + dst);

            mUserDataRepository.getNewDataFromRemote("GenericOnOff Async CallBack source  ==>" + src, LoggerConstants.TYPE_RECEIVE);
            mUserDataRepository.getNewDataFromRemote("GenericOnOff Async CallBack destination ==>" + dst, LoggerConstants.TYPE_RECEIVE);


        }
    };

    public defaultAppCallback mOnDeviceAppearedCallback = new defaultAppCallback() {
        @Override
        public void onDeviceAppeared(final String bt_addr, String name) {

            //Utils.DEBUG(">>>>>>>>>>>>>>>>>>>>> DEVICE APPEARED : >>>>>>>>> " + bt_addr);
            //onScanningDevice(bt_addr);
            //Device provisioned and then unprovisioned from device
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        removeProvisionedDevice(bt_addr);
                    } catch (Exception e) {
                    }
                }
            });
        }
    };

    private synchronized void removeProvisionedDevice(String bt_addr) {

        if (meshRootClass.getNodes() != null && meshRootClass.getNodes().size() > 0) {
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                boolean isNodeIsProvisioner = false;
                for (int j = 0; j < meshRootClass.getProvisioners().size(); j++) {
                    if (meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(meshRootClass.getProvisioners().get(j).getUUID())) {
                        isNodeIsProvisioner = true;
                        break;
                    }
                }

                if (!isNodeIsProvisioner) {
                    if (bt_addr.equalsIgnoreCase(meshRootClass.getNodes().get(i).getAddress())) {
                        String deviceName = meshRootClass.getNodes().get(i).getName();
                        final String uuid = Utils.removeProvisionNodeFromJson(MainActivity.this, meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress());
                        Utils.showToast(MainActivity.this, deviceName + " device is unprovisioned from board");
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                updateProvisionedTab(uuid, getResources().getInteger(R.integer.PROVISIONED_NODE_REMOVED));
                            }
                        });
                    }
                }
            }
        }
    }

    public void onScanningDevice(String bt_addr, String name) {
        Device device = null;
        try {
            try {
                futureED = network.enumerateDevices();

                try {
                    collectionD = futureED.get();
                    device = collectionD.getDevice(bt_addr);

                } catch (InterruptedException e) {
                    UserApplication.trace("Interrupted exception");

                } catch (ExecutionException e) {
                    UserApplication.trace("Execution exception");
                }

            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

        try {
            if (device != null && device.getUuid() != null &&
                    !device.getUuid().toString().isEmpty()
                    && !device.getUuid().equals("") && device.getUuid().length < 30) {
                final Nodes node = new Nodes(0);
                node.setUUID(Utils.array2string(device.getUuid()) + "");
                node.setSecurity("high");
                node.setUnicastAddress(device.getAddress().toString());
                node.setDefaultTTL(3);
                node.setAddress(device.getAddress().toString());
                node.setRssi(String.valueOf(device.getmRssi()));
                node.setmOOBInformation(device.getOOBInformation());
                node.setName(name);
                //Log.e("Dinesh==>MainActivity ",name);
                node.setChecked(false);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentCommunication(new UnprovisionedFragment().getClass().getName(), null,
                                0, node, false, null);
                    }
                });
            }
        } catch (Exception e) {
        }

    }

    public defaultAppCallback onDeviceRssiChangedCallback = new defaultAppCallback() {

        @Override
        public void onDeviceRssiChanged(final String bt_addr, final int mRssi, String name) {
            super.onDeviceRssiChanged(bt_addr, mRssi, name);

           // Utils.DEBUG(">>> MSG : " + name);

            //Utils.DEBUG(">> RSSI Call Continue . . . ");
            if (startRssiUpdate) {
                if (bt_addr != null && !bt_addr.isEmpty()) {
                    onScanningDevice(bt_addr, name);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            fragmentCommunication(new UnprovisionedFragment().getClass().getName(),
                                    bt_addr, mRssi, null, false, null);
                        }
                    });
                }
            }

        }
    };



    private static class UnknownDeviceNotificationDialog {

        WeakReference<Activity> mActivity;
        AlertDialog mDialog;

        UnknownDeviceNotificationDialog(Activity a) {
            mActivity = new WeakReference<Activity>(a);
        }

        void createDialog() {
            final MainActivity activity = (MainActivity) mActivity.get();
            if (activity == null)
                return;
            if (mDialog != null)
                return;
            if (activity.isFinishing())
                return;
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(activity);
            builder
                    .setTitle(R.string.unknown)
                    .setCancelable(false)
                    .setMessage("Some of devices in the list is unknown because of incorrect removal. Please try to add or remove them again.")
                    .setPositiveButton(android.R.string.ok, new DialogListener(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();
                        }
                    }, null));
            mDialog = builder.create();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);

            //mDialog.show();
        }

        void hideDialog() {
            MainActivity activity = (MainActivity) mActivity.get();
            if (activity == null)
                return;

            if (mDialog == null)
                return;
            mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(null);
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public progress mProgress = new progress(MainActivity.this, new progress.onEventListener() {

        @Override
        public void onCancel() {
            mProgress.hide();
            if (mProvisioningInProgress) {
                mProvisioningInProgress = false;
                UserApplication.trace("mProgress onCancel");
                mSettings.cancel();
            }

        }

        @Override
        public void onShow() {

        }

        @Override
        public void onHide() {

        }
    });

    public static class LostConnectionDialog {

        WeakReference<Activity> mActivity;
        AlertDialog mDialog;

        LostConnectionDialog(Activity a) {
            mActivity = new WeakReference<Activity>(a);
        }

        public void createDialog() {
            final MainActivity activity = (MainActivity) mActivity.get();
            if (activity == null)
                return;
            if (mDialog != null)
                return;
            mDialog = new ProgressDialog(activity);
            mDialog.setTitle(R.string.disconectedDialogTitle);
            mDialog.setMessage("Network is not in range. Please check if proxy node turned on and wait for connection");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnCancelListener(new DialogListener(null, new Runnable() {
                @Override
                public void run() {
                    activity.onBackPressed();
                }
            }));


            if (!mDialog.isShowing()) {

                mDialog.show();
            }

        }


        void hideDialog() {
            MainActivity activity = (MainActivity) mActivity.get();
            if (activity == null)
                return;

            if (mDialog == null)
                return;
            mDialog.setOnCancelListener(null);
            mDialog.dismiss();
            mDialog = null;
        }
    }


    public static class IdentifyDialog {

        WeakReference<Activity> mActivity;

        AlertDialog mDialog;

        IdentifyDialog(Activity a) {
            mActivity = new WeakReference<Activity>(a);
        }

        public void createDialog(final mobleSettings.Identifier identifier) {
            final MainActivity activity = (MainActivity) mActivity.get();
            if (activity == null)
                return;
            if (mDialog != null)
                return;
            if (activity.isFinishing())
                return;

            if(MainActivity.isPublicKeyEnabled)
            {
                activity.capabilities.setOobTypeSelected(4);
                identifier.setIdentified(true, activity.capabilities);
            }
            else
            {
                identifier.setIdentified(true, null);
            }

        }

        void hideDialog() {
            MainActivity activity = (MainActivity) mActivity.get();
            if (activity == null)
                return;

            if (mDialog == null)
                return;
            mDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(null);
            mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(null);
            mDialog.dismiss();
            mDialog = null;
        }

    }

    void makeToast(String text) {
        Message msg = Message.obtain();
        msg.what = MSG_TOAST;
        msg.obj = text;
        mHandler.sendMessage(msg);
    }

    public final mobleSettings.provisionerStateChanged mProvisionerStateChanged = new mobleSettings.provisionerStateChanged() {
        @Override
        public void onStateChanged(final int state, final String label) {
            final int stateValue = state + 1;
            Utils.DEBUG(">>Provisioning States :: " + stateValue);
            provisioningStep = state;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //fragmentCommunication(new UnprovisionedFragment().getClass().getName(), null, stateValue, null, true, null);
                    fragmentCommunication(new ConnectionSetupFragment().getClass().getName(), null, stateValue, null, false, null);
                    //mUserDataRepository.getNewDataFromRemote("Provisioning ==>" + (state + 1) * 10, LoggerConstants.TYPE_RECEIVE);
                    //mProgress.setProgress(state + 1, "Provisioning . . . \n" + label);
                }
            });
        }
    };

    // Listener for requests to MoBLE Settings. Handles autoConfigurecompleted
    public static final mobleSettings.onProvisionComplete mProvisionCallback = new mobleSettings.onProvisionComplete() {
        @Override
        public void onCompleted(byte status) {

            UserApplication.trace("MainActivity Provisioning Completed Callback recieved ....");

            //callback after disconnection during provisioning
            ((MainActivity) Utils.contextMainActivity).mProvisioningInProgress = false;
            // Listener for requests to MoBLE Settings. Handles autoConfigurecompleted

            /*if (status == mobleProvisioningStatus.SUCCESS) {
                ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote(" provisioning successful", LoggerConstants.TYPE_RECEIVE);
                ((MainActivity)Utils.contextMainActivity).isCustomProxy = true;
                MainActivity.mSettings.setCustomProxy(((MainActivity)Utils.contextMainActivity).mAutoAddress);

            } else {
                Utils.showToast(((MainActivity)Utils.contextMainActivity),"Provisioning unsuccessful,Please reset the device !");
            }*/
        }
    };

//    public final mobleSettings.oobCallbackListener outputOOBCallback = new mobleSettings.oobCallbackListener() {
//
//        @Override
//        public void onOOBReceived(mobleSettings.Identifier identifier, Provisioner provisioner) {
//
//            provisionerOOB = provisioner;
//            Utils.showToast(MainActivity.this, "OOB OUTPUT");
//            Utils.showPopUp_OOB_PublicKey_Authentication(MainActivity.this, "OOB OUTPUT");
//        }
//    };

    public defaultAppCallback onOOB_Input = new defaultAppCallback() {

        @Override
        public void isOOB_Input(com.msi.moble.Provisioner provisioner, String str) {

            provisionerOOB = provisioner;
            //Utils.showPopUp_OOB_PublicKey_Authentication(MainActivity.this, "INPUT OOB", num, true);
            onBackPressed();
            Utils.DEBUG(">> OOB Input Dialog");
            (new AppSingletonOOBDialog()).showPopUpForOOB(MainActivity.this, "INPUT OOB", str);
        }
    };

    public defaultAppCallback onOOB_Output = new defaultAppCallback() {

        @Override
        public void isOOB_Output(Provisioner provisioner, boolean b) {

            onBackPressed();
            provisionerOOB = provisioner;
            //Utils.showPopUp_OOB_PublicKey_Authentication(MainActivity.this, "OUTPUT OOB", -1, true);
            Utils.DEBUG(">> OOB Output Dialog");
            (new AppSingletonOOBDialog()).showPopUpForOOB(MainActivity.this, "OUTPUT OOB", "");
        }
    };

    public final mobleSettings.capabilitiesListener mCapabilitiesLstnr = new mobleSettings.capabilitiesListener() {
        @Override
        public void onCapabilitiesReceived(mobleSettings.Identifier identifier, Byte elementsNumber) {
            mUserDataRepository.getNewDataFromRemote("Capabilities Listener==>" + "element supported==>" + elementsSize, LoggerConstants.TYPE_RECEIVE);
            if (getResources().getBoolean(R.bool.bool_isElementFunctionality)) {
                elementsSize = elementsNumber;
            } else {
                elementsSize = 1;
            }

            mIdentifyDialog.createDialog(identifier);
        }
    };


    public final defaultAppCallback mOnDongleStateChanged = new defaultAppCallback() {
        @Override
        public void onDongleStateChanged(boolean enabled) {

            //Utils.showToast(MainActivity.this, "Dongle State : " + enabled);
        }
    };

    public final defaultAppCallback mProxyConnectionEventCallback = new defaultAppCallback() {

        @Override
        public void onProxyConnectionEvent(boolean process, String proxyAddress, Device device) {
            //  mProxyAddress = proxyAddress;

            Utils.setProxyNode(MainActivity.this, proxyAddress);

            if (proxyAddress != null) {
                Utils.DEBUG("Proxy Mac = " + proxyAddress);

                Utils.setProxyNode(MainActivity.this, proxyAddress);
                mLostConnectionDialog.hideDialog();

                //update ui for proxy node
                fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);


            } else {
                if (mLostConnectionDialog.mDialog != null) {
                    if (!mLostConnectionDialog.mDialog.isShowing()) {
                        mLostConnectionDialog.createDialog();
                    }
                }

            }
        }
    };

    public void callLogoutApi(final String userKey) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE) + getString(R.string.URL_MED) + getString(R.string.API_LogOut);

        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userKey", userKey);

        } catch (Exception e) {
            Utils.ERROR("Error while creating json request : " + e.toString());
        }
        MyJsonObjectRequest jsonObjReq = new MyJsonObjectRequest(
                false,
                MainActivity.this,
                Request.Method.POST,
                url,
                requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            return;
                        }
                        Utils.DEBUG("Logout onResponse() called : " + response.toString());

                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if (cloudResponseData.getStatusCode() == 110) {
                                Utils.showToast(MainActivity.this, cloudResponseData.getErrorMessage());
                            } else {
                                Utils.setLoginData(MainActivity.this, "");
                                Utils.setUserLoginKey(MainActivity.this, "");
                                Utils.setProvisioner(MainActivity.this, "");
                                //Utils.setProvisionerUUID(MainActivity.this, "");
                                Utils.setUserRegisteredToDownloadJson(MainActivity.this, "false");
                                /*if(meshRootClass.getNodes() == null || meshRootClass.getNodes().size() == 0)
                                {
                                    Utils.setUserRegisteredToDownloadJson(MainActivity.this,"false");
                                }*/
                                Utils.showToast(MainActivity.this, getString(R.string.str_logout_success_label));
                            }

                            openDrawer();
                        } catch (Exception e) {
                        }
                        loader.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.ERROR("Error: " + error);
                //Utils.showToast(getActivity(), getString(R.string.string_common_error_message));
                loader.hide();
            }
        }
        );
        // Adding request to request queue
        UserApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void callLoginApi(final String username, final String password) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE) + getString(R.string.URL_MED) + getString(R.string.API_LoginDetails);

        if (loader != null) loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("lUserName", username);
            requestObject.put("lPassword", password);

        } catch (Exception e) {
            Utils.ERROR("Error while creating json request : " + e.toString());
        }
        MyJsonObjectRequest jsonObjReq = new MyJsonObjectRequest(
                false,
                MainActivity.this,
                Request.Method.POST,
                url,
                requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            return;
                        }
                        Utils.DEBUG("Login onResponse() called : " + response.toString());

                        try {
                            LoginData loginData = ParseManager.getInstance().fromJSON(response, LoginData.class);
                            if (loginData.getStatusCode() == 101) {
                                Utils.showToast(MainActivity.this, getString(R.string.str_login_warning_label));
                                //error show pop
                                if (meshRootClass.getMeshUUID() == null) {
                                    Utils.showPopUp(MainActivity.this, new MainActivity().getClass().getName(), true, false, getResources().getString(R.string.str_create_network_msg_label), getResources().getString(R.string.str_login_import_label));
                                }
                            } else if (loginData.getStatusCode() == 0) {
                                //success
                                LoginData loginData1 = new LoginData();
                                loginData1.setUserName(username);
                                loginData1.setUserPassword(password);
                                loginData1.setUserKey(loginData.getResponseMessage());
                                String loginDataString = ParseManager.getInstance().toJSON(loginData1);
                                Utils.setLoginData(MainActivity.this, loginDataString);
                                Utils.setUserLoginKey(MainActivity.this, loginData.getResponseMessage());
                                Utils.setPreviousUserLoginKey(MainActivity.this, loginData.getResponseMessage());

                                Utils.DEBUG("Login Data : " + Utils.getLoginData(MainActivity.this));
                                Utils.showToast(MainActivity.this, getString(R.string.str_session_restarted_label));
                                updateJsonData();

                                loader.hide();
                                try {
                                    resumeNetworkAndCallbacks("", meshRootClass.getMeshUUID(), false);
                                } catch (Exception e) {
                                }

                            }

                        } catch (Exception e) {
                        }
                        loader.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.ERROR("Error: " + error);
                //Utils.showToast(getActivity(), getString(R.string.string_common_error_message));
                loader.hide();
            }
        }
        );
        // Adding request to request queue
        UserApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    void registerObersever() {
        mUserDataRepository = UserDataRepository.getInstance();
        mUserDataRepository.registerObserver(this);
    }

    @Override
    public void onUserDataChanged(String logs, String type) {


    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    public Group getGroupData() {
        GroupSettingFragment fragmentByTag = (GroupSettingFragment) getSupportFragmentManager().findFragmentByTag(new GroupSettingFragment().getClass().getName());
        return fragmentByTag.getGroupData();
    }

    public NodeSettings getNodeData() {
        NodeSettingFragment fragmentByTag = (NodeSettingFragment) getSupportFragmentManager().findFragmentByTag(new NodeSettingFragment().getClass().getName());
        return fragmentByTag.getNodeData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && storageAccepted) {
                        //Snackbar.make(navigationView, "Permission Granted.", Snackbar.LENGTH_LONG).show();
                        if(Utils.isShowIntroScreen(MainActivity.this))
                        {
                            moveToIntroFragment();
                        }
                        else {
                            enablePermissions();
                        }
                    } else {

                        Snackbar.make(navigationView, "Permission Denied, Please Goto Setting-->Permission-->Turn On permissions to access the App", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("", "You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        }, true);
                                return;
                            } else {


                                showMessageOKCancel("Permission Denied !", "Please Goto Settings ---> find Permission option ---> Turn On both the permissions to access the App",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, PERMISSION_SETTING);
                                            }
                                        }, true);


                            }
                        }

                    }
                }


                break;

            case REQUEST_CAMERA_PERMISSION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED/* && grantResults[1] == PackageManager.PERMISSION_GRANTED*/) {
                    //takeBarcodePicture();
                    fragmentCommunication(new CapabilitySelectionFragment().getClass().getName(), null, 0, null, false, null);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showMessageOKCancel(String title, String
            message, DialogInterface.OnClickListener okListener, boolean is_request_permission) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "permission is required !", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void initMobleMsgDialog() {

        mobleMsgDialog = new Dialog(MainActivity.this);
        mobleMsgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mobleMsgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mobleMsgDialog.setCanceledOnTouchOutside(false);
        mobleMsgDialog.getWindow().setGravity(Gravity.BOTTOM);
        mobleMsgDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mobleMsgDialog.setContentView(R.layout.dialog_moblemsgdialog);
        txtMsgHeading = (TextView) mobleMsgDialog.findViewById(R.id.txtMsgHeading);
        txtMsg = (TextView) mobleMsgDialog.findViewById(R.id.txtMsg);
        butOk = (Button) mobleMsgDialog.findViewById(R.id.butOk);
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobleMsgDialog.hide();
            }
        });
    }

    public void showPopUpForProxy(final Context context, final String responseMessage, boolean isVisible) {

        TextView txt = null;
        Button but = null;
        ImageView gifImageView = null;
        ImageView imgError = null;
        if (proxydialog == null) {
            proxydialog = new Dialog(context);
            proxydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            //proxydialog.getWindow().setDimAmount(0.0f);
            proxydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            proxydialog.setCanceledOnTouchOutside(false);
            proxydialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            proxydialog.setContentView(R.layout.dialog_custom_proxy);
            proxydialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {

                    //kill all binding process and reset composition data
                    //kill all binding thread and service
                    Utils.saveModelInfoOfMultiElement(MainActivity.this, null);
                    Utils.saveModelInfo(MainActivity.this, null);
                    Utils.setNodeFeatures(MainActivity.this, null);
                    stopService(new Intent(getApplicationContext(), ModelBindingService.class));
                    proxydialog.dismiss();
                    onBackPressed();
                }
            });
        }

        imgError = (ImageView) proxydialog.findViewById(R.id.imgError);
        gifImageView = (ImageView) proxydialog.findViewById(R.id.gif);
       /* Glide.with(this).load(R.drawable.gifsimpleloader).asGif().into(gifImageView);*/
        //Glide.with(this).load(R.drawable.gifsimpleloader).into(gifImageView);

        txt = (TextView) proxydialog.findViewById(R.id.txtErrorMsg);
        but = (Button) proxydialog.findViewById(R.id.but);

        if (isVisible) {
            gifImageView.setVisibility(View.VISIBLE);
            imgError.setVisibility(View.GONE);
            if (!proxydialog.isShowing()) {
                proxydialog.show();
            }
        } else {
            if (responseMessage.contains("Timeout")) {
                gifImageView.setVisibility(View.GONE);
                imgError.setVisibility(View.VISIBLE);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    proxydialog.dismiss();
                }
            }, 500);

        }


        txt.setText(responseMessage);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxydialog.dismiss();
            }
        });
        but.setVisibility(View.GONE);

    }

    public void showInternetMsg(Boolean show){

        txtInternetWarning.setVisibility(show == true ? View.VISIBLE : View.GONE);

        return;
    }

    public void initShowMobleMsgDialog(String msg1, String msg2) {
        txtMsgHeading.setText(msg1);
        txtMsg.setText(msg2);
        mobleMsgDialog.show();
    }

    public void initHideMobleMsgDialog() {
        mobleMsgDialog.hide();
    }

    public defaultAppCallback mMobleError = new defaultAppCallback() {
        @Override
        public void onError(String text) {

            //if (text.equals(getString(R.string.str_error_gatt_connection_failed))) {
            if (text.contains("Gatt Connection")) {

                String proxyNode = Utils.getProxyNode(MainActivity.this);
                Utils.setProxyNode(MainActivity.this, null);
                Utils.setProxyUUID(MainActivity.this, null);
                fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), proxyNode, getResources().getInteger(R.integer.PROVISIONED_PROXY_UPDATE), null, false, null);
                if (!isProvisioningProcessLive) {
                    proxyLoader = MsgDialogLoader.getProxyLoader(MainActivity.this, "Proxy Disconnected. Retrying...\nMake sure your device is switched on and in range.");
                    if (!proxyLoader.isShowing()) {
                        //proxyLoader.show();
                    }

                    if (network != null && !isCustomProxy) {
                        //network.getSettings().startProxy();
                        //network.start(MainActivity.this);
                    }
                }
            } else if (text.equals(getString(R.string.str_error_acl_disconnected))) {
                //disable popup
                //ACL_DISCONNECTED State : Add Node
                //initShowMobleMsgDialog(text, getString(R.string.str_error_acl_disconnected));
            } else if (text.contains("BluetoothGattStatus")) {
                //disable popup
                //ACL_DISCONNECTED State : Add Node
                //initShowMobleMsgDialog(text, getString(R.string.str_error_acl_disconnected));
                //initShowMobleMsgDialog(text, getString(R.string.str_error_Gatt_Not_Responding));
            } else if (text.contains(getString(R.string.str_err_oob))) {
                //OOB Error Exception
                //stop all current provisioning steps
                Utils.showToast(MainActivity.this, getString(R.string.str_msg_oob_try_again));


            }
        }
    };

    private void InitLog(boolean islogEnabled) {

        try {
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
        }catch (Exception e){}

    }
}

