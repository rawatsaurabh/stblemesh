/**
 * *****************************************************************************
 *
 * @file UnprovisionedFragment.java
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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.msi.moble.CustomProvisioning;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;
import com.st.bluenrgmesh.DeviceEntry;
import com.st.bluenrgmesh.GroupEntry;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.UnprovisionedRecyclerAdapter;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.setting.configuration.ConnectionSetupFragment;

import java.util.ArrayList;
import java.util.Collections;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;


public class UnprovisionedFragment extends BaseFragment {


    private static UnprovisionedFragment fragment;
    private View view;
    private RecyclerView layRecycler;
    public UnprovisionedRecyclerAdapter unprovisionedRecyclerAdapter;
    public UserApplication app;
    public ArrayList<Nodes> mUnprovisionedData = new ArrayList<>();
    private LinearLayout lytWarningDeviceDiscovery;
    private PulsatorLayout pulsator;
    private int itemPosition;
    private SwipeRefreshLayout swiperefresh;
    public static final String DEFAULT_NAME = "New Node";
    private int STATE_INVITATION_0 = 0;
    private int STATE_MTU_PACKET_CHECK_1 = 1;
    private int STATE_CAPABILITIES_SELECTION_2 = 2;
    private int STATE_PUBLIC_KEY_EXCHANGE_3 = 3;
    private int STATE_AUTHENTICATION_4 = 4;
    private int STATE_DISTRIBUTION_5 = 5;
    private int STATE_DEVICE_AUTENTICATION_COMPLETE_6 = 6;
    private int STATE_DISCONNECTION_7 = 7;
    private int STATE_PROVISIONING_COMPLETE_8 = 8;
    private Dialog provisioningDialog;
    private TextView txtMsgDialog;
    private TextView txtPercentage;
    private SeekBar progressHorizontal;
    private Button butOk;
    private Context context;
    private Nodes nodeSelected = null;
    private String mAutoAddress = null;
    private int nodeNumber ;
    private Nodes currentNode;
    private DeviceEntry mAutoDevice;
    private ImageView imgBle;
    private String mAutoName;
    private ArrayList<Element> newElements;
    private AppDialogLoader loader;

    @Override
    public void onAttach(Context context) {
        context=this.context;
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_unprovisioned, container, false);

        Utils.contextU = getActivity();
        loader = AppDialogLoader.getLoader(getActivity());
        initUi();

        return view;
    }

    private void initUi() {

        pulsator = (PulsatorLayout) view.findViewById(R.id.pulsator);
        pulsator.start();

        imgBle = (ImageView) view.findViewById(R.id.imgBle);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.ST_primary_blue), getResources().getColor(R.color.ST_primary_blue), getResources().getColor(R.color.ST_primary_blue));
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        //MainActivity.network.stop();
                        //MainActivity.network.start(getActivity());
                        if(!pulsator.isStarted())
                        {
                            pulsator.start();
                        }

                        updateUnprovisionedList(null, false);
//                        ((MainActivity)getActivity()).adviseCallbacks();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swiperefresh.setRefreshing(false);
                            }
                        }, 500);
                    }
                }
        );

        lytWarningDeviceDiscovery = (LinearLayout) view.findViewById(R.id.lytWarningDeviceDiscovery);
        layRecycler = (RecyclerView) view.findViewById(R.id.layRecycler);
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        layRecycler.setLayoutManager(gridLayoutManager);


        unprovisionedRecyclerAdapter = new UnprovisionedRecyclerAdapter(getActivity(), mUnprovisionedData/*((MainActivity)getActivity()).mUnprovisionedList*/, "",
                new UnprovisionedRecyclerAdapter.IRecyclerViewHolderClicks() {

            @Override
            public void onClickRecyclerItemNode(View v, int position, final Nodes nodeSelected) {
                Utils.DEBUG(">> Unprovision Address Selected : " + nodeSelected.getAddress());
                itemPosition = position;
                updateUnprovisionedList(nodeSelected, true);
                if(getResources().getBoolean(R.bool.bool_isAutoProvisioning))
                {
                    showPopUpProvisioningType(getActivity(), nodeSelected);
                }
                else {
                    Utils.moveToFragment(getActivity(), new ConnectionSetupFragment(), nodeSelected, 0);
                }

            }
        });
        layRecycler.setAdapter(unprovisionedRecyclerAdapter);

    }

    public void startScanAnimation() {
        if(getActivity()!=null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pulsator.start();
                }
            });
        }else if (context != null) {
            ((MainActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pulsator.start();
                }
            });
        }

    }

    public void updateUnprovisionedList(Nodes deviceDiscovered, boolean isRemove) {

        if(deviceDiscovered != null)
        {
            boolean isAddedInList = false;
            boolean isAddedInAdapter = false;
            boolean isProvisioned = false;
            int position = -1;
            for (int i = 0; i < mUnprovisionedData.size(); i++) {
                if (mUnprovisionedData.get(i).getAddress().equals(deviceDiscovered.getAddress())) {
                    position = i;
                    isAddedInList = true;
                    break;
                }
            }

            if(unprovisionedRecyclerAdapter.getItemCount() > 0)
            {
                isAddedInAdapter = Utils.isDeviceAlreadyAdded(getActivity(),unprovisionedRecyclerAdapter, layRecycler, deviceDiscovered.getAddress());
            }

            try {
                if (((MainActivity) getActivity()).meshRootClass.getNodes() != null) {
                    for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                        if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getAddress() != null) {
                            if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getAddress().
                                    equalsIgnoreCase(deviceDiscovered.getAddress())) {
                                isProvisioned = true;
                                break;
                            }
                        }
                    }
                }
            }catch (Exception e){}


            if(!isRemove)
            {
                //add
                if (!isAddedInList && !isAddedInAdapter && !isProvisioned) {
                 //   Utils.DEBUG(">>>>>>>>>>>>>>>>>>>>> DEVICE APPEARED : >>>>>>>>> " + deviceDiscovered.getAddress());
                    mUnprovisionedData.add(deviceDiscovered);
                    final int positionX = mUnprovisionedData.size() - 1;
                    if(getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lytWarningDeviceDiscovery.setVisibility(View.GONE);
                                imgBle.setImageDrawable(getActivity().getDrawable(R.drawable.ic_bluetooth_searching_white_24dp));
                                unprovisionedRecyclerAdapter.notifyItemInserted(unprovisionedRecyclerAdapter.getItemCount());
                            }
                        });
                    }else if (context != null) {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lytWarningDeviceDiscovery.setVisibility(View.GONE);
                                imgBle.setImageDrawable(getActivity().getDrawable(R.drawable.ic_bluetooth_searching_white_24dp));
                                unprovisionedRecyclerAdapter.notifyItemInserted(unprovisionedRecyclerAdapter.getItemCount());
                            }
                        });
                    }

                }
            }
            else
            {
                //remove
                if (isAddedInList && isAddedInAdapter) {
                    Utils.DEBUG(">>>>>>>>>>>>>>>>>>>>> DEVICE Removed : >>>>>>>>> " + deviceDiscovered.getAddress());
                    if(mUnprovisionedData.size() > 0)
                    {
                        try{
                            mUnprovisionedData.remove(position);
                        }catch (Exception e){}
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(unprovisionedRecyclerAdapter != null && unprovisionedRecyclerAdapter.getItemCount() > 0)
                                {
                                    unprovisionedRecyclerAdapter.notifyItemRemoved(itemPosition);
                                    if (unprovisionedRecyclerAdapter.getItemCount() == 0) {
                                        imgBle.setImageDrawable(getActivity().getDrawable(R.drawable.ic_settings_bluetooth_black_24dp));
                                        lytWarningDeviceDiscovery.setVisibility(View.VISIBLE);
                                    }
                                }
                            }catch (Exception e){}
                        }
                    });

                }
            }
        }
        else
        {
            mUnprovisionedData.clear();
            unprovisionedRecyclerAdapter.notifyDataSetChanged();
        }

    }

    public void updateRssiUI(final String bt_addr, final int mRssi) {

        boolean isDevicePresent = false;

        if (mUnprovisionedData.size() > 0) {
            for (int i = 0; i < mUnprovisionedData.size(); i++) {

                if (mUnprovisionedData.get(i).getAddress().equalsIgnoreCase(bt_addr)) {
                    isDevicePresent = true;
                    //mUnprovisionedData.get(i).setRssi(String.valueOf(mRssi));
                    break;
                }
            }

            if (isDevicePresent) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        final int position = Utils.getRowAdapterPosition(getActivity(), unprovisionedRecyclerAdapter, layRecycler, bt_addr, mRssi);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    unprovisionedRecyclerAdapter.notifyItemChanged(position, String.valueOf(mRssi));
                                }
                            });
                        } else if (context != null) {
                            ((MainActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    unprovisionedRecyclerAdapter.notifyItemChanged(position, String.valueOf(mRssi));
                                }
                            });
                        }

                    }
                });

            }
        }

    }

    public static UnprovisionedFragment newInstance() {

        if( fragment == null ) {
            fragment = new UnprovisionedFragment();
        }
        return fragment;
    }

    public void showPopUpProvisioningType(final Context context, Nodes nodeSelected) {
        if (context != null) {
            final Dialog dialog = new Dialog(context);
            if (dialog != null && dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                dialog.setContentView(R.layout.dialog_provisioningtype);
                TextView txt = (TextView) dialog.findViewById(R.id.txtErrorMsg);
                Button butOneStep = (Button) dialog.findViewById(R.id.butOneStep);
                Button butNormal = (Button) dialog.findViewById(R.id.butNormal);
                if (!dialog.isShowing()) {
                    dialog.show();
                }


                butOneStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Utils.setAutoProvisioning(getActivity(), true);
                        Utils.moveToFragment(getActivity(), new ConnectionSetupFragment(), nodeSelected, 0);
                    }
                });

                butNormal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Utils.setAutoProvisioning(getActivity(), false);
                        Utils.moveToFragment(getActivity(), new ConnectionSetupFragment(), nodeSelected, 0);
                    }
                });
            }
        }
    }

}
