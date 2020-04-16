/**
 * *****************************************************************************
 *
 * @file KeyManagementFragment.java
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
package com.st.bluenrgmesh.view.fragments.setting.managekey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.appkey.AppKeyRecyclerAdapter;
import com.st.bluenrgmesh.callbacks.appkeymanagement.quickbinding.ShareAppKeyCallback;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

public class KeyManagementFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private RecyclerView recycler;
    private AppKeyRecyclerAdapter appKeyRecyclerAdapter;
    private byte[] newAppKeyBytes;
    private Element element;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_keymanagement, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new KeyManagementFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        element = (Element) getArguments().getSerializable(getString(R.string.key_serializable));
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(gridLayoutManager);
        appKeyRecyclerAdapter = new AppKeyRecyclerAdapter(getActivity(), ((MainActivity) getActivity()).meshRootClass.getAppKeys() , new AppKeyRecyclerAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void notifyAdapter(int position, AppKey appKey) {

                try {
                    newAppKeyBytes = Utils.hexStringToByteArray(appKey.getKey());
                    Utils.DEBUG("Old Key sharing : " + newAppKeyBytes.length);
                    Utils.setDefaultAppKeyIndex(getActivity(), appKey.getIndex());
                    //appKeyAssign = appKey;
                    //((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Activating Key at Index.." + appKey.getIndex(), true);
                    byte[] bytes = Utils.hexStringToByteArray(appKey.getKey());
                    Utils.showToast(getActivity(), "Key activated w.r.t Index : " + appKey.getIndex());
                    MainActivity.network.setAppKey(bytes);
                }catch (Exception e){}

            }
        });
        recycler.setAdapter(appKeyRecyclerAdapter);

        updateRecyler();

    }

    public void updateRecyler() {

        //save to local
        MainActivity.isCustomAppKeyShare = false;
        String s = ParseManager.getInstance().toJSON(((MainActivity) getActivity()).meshRootClass.getAppKeys());
        Utils.saveAppKeys(getActivity(), s);
        if(appKeyRecyclerAdapter != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //appKeyRecyclerAdapter.notifyItemInserted(((MainActivity) getActivity()).meshRootClass.getAppKeys().size());
                    appKeyRecyclerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void shareNewAppKey()
    {
        //loader.show();
        newAppKeyBytes = MainActivity.network.genrateAppKey();
        Utils.DEBUG("New App Key Generated : " + newAppKeyBytes.length);
        //new ShareAppKeyCallbackOld(getActivity(), Utils.array2string(newAppKeyBytes), element, ((MainActivity)getActivity()).meshRootClass.getAppKeys().size());
        AppKey newAppKey = new AppKey();
        newAppKey.setIndex(((MainActivity)getActivity()).meshRootClass.getAppKeys().size());
        //newAppKey.setName(getActivity().getString(R.string.str_app_key_name));
        newAppKey.setName("App Key "+((MainActivity)getActivity()).meshRootClass.getAppKeys().size());
        newAppKey.setKey(Utils.array2string(newAppKeyBytes));
        MainActivity.isCustomAppKeyShare = true;
        new ShareAppKeyCallback(getActivity(), newAppKey, element).shareAppKey();
    }
}
