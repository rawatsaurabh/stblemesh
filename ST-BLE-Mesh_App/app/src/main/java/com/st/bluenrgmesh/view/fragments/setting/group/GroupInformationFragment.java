/**
 * *****************************************************************************
 *
 * @file GroupInformationFragment.java
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
package com.st.bluenrgmesh.view.fragments.setting.group;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.group.KeyClassificationAdapter;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Group;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupInformationFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private Group group;
    private RecyclerView mainRecyclerView;
    private HashMap<AppKey, ArrayList<Element>> elementsByKey = new HashMap<>();
    private KeyClassificationAdapter keyClassificationAdapter;
    private NpaGridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_groupkeymodel, container, false);


        Utils.updateActionBarForFeatures(getActivity(), new GroupInformationFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        group = (Group) getArguments().getSerializable(getString(R.string.key_serializable));
        mainRecyclerView = (RecyclerView) view.findViewById(R.id.mainRecyclerView);

        gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        mainRecyclerView.setLayoutManager(gridLayoutManager);

        prepareDataByKey();
        keyClassificationAdapter = new KeyClassificationAdapter(getActivity(), elementsByKey);
        mainRecyclerView.setAdapter(keyClassificationAdapter);


        //get the elements w.r.t group
        //get the model w.r.t element
        //get the keys w.r.t model
    }

    private void prepareDataByKey() {

        MeshRootClass meshRootClass = null;
        try {
            meshRootClass = (MeshRootClass) ((MainActivity)getActivity()).meshRootClass.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < meshRootClass.getAppKeys().size(); i++) {

            ArrayList<Element> elements = new ArrayList<>();

            for (int j = 0; j < meshRootClass.getNodes().size(); j++) {
                for (int k = 0; k < meshRootClass.getNodes().get(j).getElements().size(); k++) {
                    Element element = isKeyPresentInElementByModel(getActivity(), meshRootClass.getNodes().get(j).getElements().get(k), group, meshRootClass.getAppKeys().get(i));
                    if(element != null && element.isChecked())
                    {
                        elements.add(element);
                    }
                }
            }

            if(!elementsByKey.containsKey(meshRootClass.getAppKeys().get(i)))
            {
                elementsByKey.put(meshRootClass.getAppKeys().get(i), elements);
            }
        }

        /*for (AppKey appKey : elementsByKey.keySet())
        {
            ArrayList<Element> elements = elementsByKey.get(appKey);

        }*/

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                keyClassificationAdapter.notifyDataSetChanged();
            }
        });
    }

    public Element isKeyPresentInElementByModel(Context context, Element element, Group groupData, AppKey key) {

        if(element.getModels() != null && element.getModels().size() > 0)
        {

            for (int k = 0; k < element.getModels().size(); k++)
            {
                boolean isKeyPresent = false;
                boolean isSubscribed = false;
                if(element.getModels().get(k).getBind() != null && element.getModels().get(k).getBind().size() > 0)
                {
                    for (int l = 0; l < element.getModels().get(k).getBind().size(); l++) {
                        if(element.getModels().get(k).getBind().get(l) == key.getIndex())
                        {
                            isKeyPresent = true;
                            break;
                        }
                    }
                }

                //show element in this model icon
                element.getModels().get(k).setChecked(isKeyPresent ? true : false);

                if(element.getModels().get(k).getSubscribe() != null && element.getModels().get(k).getSubscribe().size() > 0)
                {
                    for (int l = 0; l < element.getModels().get(k).getSubscribe().size(); l++) {
                        if(groupData.getAddress().equalsIgnoreCase(element.getModels().get(k).getSubscribe().get(l)))
                        {
                            isSubscribed = true;
                            break;
                        }
                    }
                }

                if(isKeyPresent)
                {
                    if(isSubscribed)
                    {
                        element.setChecked(true);
                    }
                    else
                    {
                        element.setChecked(false);
                    }
                }
            }
        }

        return element;
    }
}
