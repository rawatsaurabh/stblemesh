/**
 * *****************************************************************************
 *
 * @file MainViewPagerFragment.java
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

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.PagerAdapter;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.other.SplashScreenFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.ModelMenuFragment;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import java.util.ArrayList;


public class MainViewPagerFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private Fragment fragment;
    private PagerAdapter adapter;
    private ViewPager viewPager;
    private ProvisionedTabFragment provisionedTabFragment;

    private int[] navIcons = {
            R.drawable.unprovisionedimgi,
            R.drawable.nodesi,
            R.drawable.groupimgi,
            R.drawable.modelsi,
    };
    private int[] navLabels = {
            R.string.str_unprovisioned_label,
            R.string.str_provisioned_label,
            R.string.str_group_label,
            R.string.str_model_label
    };

    // another resouces array for active state for the icon
    private int[] navIconsActive = {
            R.drawable.unprovisionedimg,
            R.drawable.nodes,
            R.drawable.groupimg,
            R.drawable.models,
    };
    private TabLayout tabLayout;
    private ModelTabFragment modelTabFragment;
    private GroupTabFragment groupTabFragment;
    private static MeshRootClass meshRootClass;
    private static Fragment myTabFragment;
    private static HandlerOnPosition handlerOnPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  LayoutInflater.from(getActivity()).inflate(R.layout.fragment_viewpager, null);
        Utils.updateActionBarForFeatures(getActivity(), new MainViewPagerFragment().getClass().getName());
        initUi();
        return view;
    }

    private void initUi() {

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layoutt);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        setTabsLayout(tabLayout, 0, false);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) view.findViewById(R.id.pagerr);
        adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(1);
                //By setting setOffscreenPageLimit(2) you’ll allow all of your pages to stay in memory all the time.
                viewPager.setOffscreenPageLimit(4);
                viewPager.setPageMargin(5);
                if (!Utils.isOnline(getActivity())) {
                    ((MainActivity)getActivity()).showInternetMsg(true);
                }
            }
        },200);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                ((MainActivity) getActivity()).tabSelected = position;
                setTabsLayout(tabLayout, position, true);
                updateFragmentsGui(position);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new ControlCallbacksThread(getActivity(), position).start();
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //tabLayout.setAnimation(MainActivity.in_animation);
        viewPager.setAnimation(MainActivity.in_animation);

        //handlerOnPosition = new HandlerOnPosition(getActivity(), adapter, viewPager, 1);
        //Utils.screenWidgetsIntro(getActivity(), R.id.group_toggle_switch);

    }

    private void updateFragmentsGui(final int position) {

        if(handlerOnPosition == null)
        {
            handlerOnPosition = new HandlerOnPosition(getActivity(), adapter, viewPager, position);
        }

        Message msg = new Message();
        msg.arg1 = position;
        handlerOnPosition.sendMessage(msg);
        //new UpdateJsonObjectThread(getActivity()).start();
        return;
    }

    private static class ControlCallbacksThread extends Thread{

        private final Context context;
        private final int position;

        public ControlCallbacksThread(Context context, int position) {
            this.context = context;
            this.position = position;
        }

        @Override
        public void run() {
            controlCallBacks_ScanFlag(context,position);
        }
    }

    private static class HandlerOnPosition extends Handler{

        private static Context context;
        private static PagerAdapter adapter;
        private static ViewPager viewpager;
        private static int position;


        public HandlerOnPosition(Context context, PagerAdapter adapter, ViewPager viewPager, int position) {
            this.context = context;
            this.adapter = adapter;
            this.viewpager = viewPager;
            this.position = position;
        }

        @Override
        public void handleMessage(Message msg) {
            getFragmentOnPosition(context, adapter, viewpager, msg.arg1);
        }

    }

    private static void getFragmentOnPosition(Context context, PagerAdapter adapter, ViewPager viewPager, int position) {

        myTabFragment = (Fragment) adapter.instantiateItem(viewPager, position);
        if (position==0)
        {
            Utils.updateActionBarForTabs((MainActivity)context,position);
            UnprovisionedFragment myUnprovisionedTab = (UnprovisionedFragment) myTabFragment;
            myUnprovisionedTab.startScanAnimation();
            ((MainActivity)context).clearUnprovisionList();
            myUnprovisionedTab.mUnprovisionedData.clear();
            myUnprovisionedTab.updateUnprovisionedList(null, false);
        }
        else if (position==1)
        {
            Utils.updateActionBarForTabs((MainActivity)context,position);
        }
        else if (position==2)
        {
            if(!Utils.isNodesExistInMesh((MainActivity)context, ((MainActivity)context).meshRootClass))
            {
                GroupTabFragment groupTabFragment = (GroupTabFragment) myTabFragment;
                groupTabFragment.cleanGroupUi();
            }
            Utils.updateActionBarForTabs((MainActivity)context,position);

        }
        else if(position==3)
        {
            //String topFragmentInBackStack = Utils.getTopFragmentInBackStack(getActivity());
            ModelTabFragment modelTabFragment = (ModelTabFragment) ((MainActivity)context).getSupportFragmentManager().findFragmentByTag(new ModelTabFragment().getClass().getName());
            if(modelTabFragment.nodes.size() == 0)
            {

                if(!Utils.isFragmentPresent((MainActivity)context, new ModelMenuFragment().getClass().getName()))
                {
                    Utils.moveToFragment((MainActivity)context, new ModelMenuFragment(), null, 0);
                }
                Utils.updateActionBarForTabs((MainActivity)context,position);
            }
            else
            {
                if(modelTabFragment.modelTypeSelected == null)
                {
                    Utils.updateActionBarForFeatures((MainActivity)context, new ModelTabFragment().getClass().getName(), context.getString(R.string.str_vendormodel_label));
                }
                else
                {
                    if(modelTabFragment.modelTypeSelected.equals(context.getString(R.string.str_genericmodel_label)))
                    {
                        Utils.updateActionBarForFeatures((MainActivity)context, new ModelTabFragment().getClass().getName(), context.getString(R.string.str_genericmodel_label));
                    }
                    else if(modelTabFragment.modelTypeSelected.equals(context.getString(R.string.str_lighting_model_label)))
                    {
                        Utils.updateActionBarForFeatures((MainActivity)context, new ModelTabFragment().getClass().getName(), context.getString(R.string.str_lighting_model_label));
                    }
                    else if(modelTabFragment.modelTypeSelected.equals(context.getString(R.string.str_vendormodel_label)))
                    {
                        Utils.updateActionBarForFeatures((MainActivity)context, new ModelTabFragment().getClass().getName(), context.getString(R.string.str_vendormodel_label));
                    }
                    else if(modelTabFragment.modelTypeSelected.equals(context.getString(R.string.str_sensormodel_label)))
                    {
                        Utils.updateActionBarForFeatures((MainActivity)context, new ModelTabFragment().getClass().getName(), context.getString(R.string.str_sensormodel_label));
                    }
                }

                if(!Utils.isNodesExistInMesh((MainActivity)context, ((MainActivity)context).meshRootClass))
                {
                    ModelTabFragment modelTabFragmentt = (ModelTabFragment) myTabFragment;
                    modelTabFragmentt.cleanModelUi();
                }
            }
        }
    }

    private static void controlCallBacks_ScanFlag(Context context, final int position) {

        int positionn = position;
        if (context.getResources().getBoolean(R.bool.bool_controlRssiCallBack)) {
            if (positionn == 0 || positionn == 1) {
                //advise
                if(position == 0)
                {
                    ((MainActivity)context).startRssiUpdate = true;
                }
                else
                {
                    ((MainActivity)context).startRssiUpdate = false;
                }
                ((MainActivity) context).isTabUpdating = false;
                //((MainActivity) context).adviseCallbacks();
            } else {
                ((MainActivity)context).startRssiUpdate = false;
                ((MainActivity) context).isTabUpdating = true;
                //unadvise
                //((MainActivity) context).unAdviseCallbacks();
            }
        }
    }

    private void setTabsLayout(final TabLayout tabLayout, final int position, final boolean isManual) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (isManual) {
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {

                        TabLayout.Tab tabAt = tabLayout.getTabAt(i);
                        TextView tab_label = (TextView) tabAt.getCustomView().findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tabAt.getCustomView().findViewById(R.id.nav_icon);
                        if (i == position) {
                            tab_label.setTextColor(getResources().getColor(R.color.sharpwhite));
                            tab_icon.setImageResource(navIconsActive[i]);

                        } else {

                            tab_label.setTextColor(getResources().getColor(R.color.black_overlay));
                            tab_icon.setImageResource(navIcons[i]);

                        }

                        tab_label.setTextSize(10);
                        tab_icon.setMaxWidth(10);
                        tab_icon.setMaxHeight(10);
                    }
                } else {
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        LinearLayout tab = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.nav_tab, null);
                        TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
                        tab_label.setText(getResources().getString(navLabels[i]));
                        if (i == position) {
                            tab_label.setTextColor(getResources().getColor(R.color.sharpwhite));
                            tab_icon.setImageResource(navIconsActive[i]);

                            //Utils.updateAppTitle(getResources().getString(navLabels[i]),getActivity());

                        } else {
                            tab_label.setTextColor(getResources().getColor(R.color.black_overlay));
                            tab_icon.setImageResource(navIcons[i]);
                        }

                        tab_label.setTextSize(10);
                        tab_icon.setMaxWidth(10);
                        tab_icon.setMaxHeight(10);
                        tabLayout.getTabAt(i).setCustomView(tab);
                    }
                }
            }
        });

        return;
    }

    public void stopViewPagerScroll() {
        viewPager.beginFakeDrag();
    }

    public void startViewPagerScroll() {
        viewPager.endFakeDrag();
    }


    /**
     *
     * @param bt_addr : : This parameter used to store any string data like : address, model name, UUID
     * @param someInt : : This parameter can be used as a number data(RSSI value) , a number that represent case type, a tab number.
     * @param className
     * @param deviceDiscovered
     * @param listData
     * @param anyBool
     */
    public void updateFragmentUi(String bt_addr, int someInt, String className, Nodes deviceDiscovered, ArrayList<String> listData, boolean anyBool) {
        if (className.equals(new UnprovisionedFragment().getClass().getName())) {
            UnprovisionedFragment myFragment = (UnprovisionedFragment) adapter.instantiateItem(viewPager, 0);
            if (bt_addr == null || someInt == 0) {
                myFragment.updateUnprovisionedList(deviceDiscovered, false);
            } else {
                myFragment.updateRssiUI(bt_addr, someInt);
            }

        } else if (className.equals(new ProvisionedTabFragment().getClass().getName())) {
            provisionedTabFragment = (ProvisionedTabFragment) adapter.instantiateItem(viewPager, 1);
            provisionedTabFragment.updateProvisioneRecycler(bt_addr, someInt, deviceDiscovered);
        } else if (className.equals(new GroupTabFragment().getClass().getName())) {
            groupTabFragment = (GroupTabFragment) adapter.instantiateItem(viewPager, 2);
            groupTabFragment.updateGroupRecycler(/*model name*/bt_addr);
        } else if (className.equals(new ModelTabFragment().getClass().getName())) {
            modelTabFragment = (ModelTabFragment) adapter.instantiateItem(viewPager, 3);
            if(someInt == 0) {
                //default case
                modelTabFragment.updateModelRecycler(/*model name*/bt_addr);
            } else {
                //sensor model case
                modelTabFragment.updateSensorValues(/*sensor data*/bt_addr, /*proprtyId*/someInt);
            }

        }

    }


    public void scrollViewPager(int scrollPage) {

        viewPager.setCurrentItem(scrollPage);

    }

    @Override
    public void onBackEventPre() {
        Utils.showToast(getActivity(), "On Back Event");
    }

}
