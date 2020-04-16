/**
 ******************************************************************************
 * @file    KeyClassificationAdapter.java
 * @author  BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
 * @brief   User Application file
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; COPYRIGHT(c) 2017 STMicroelectronics</center></h2>
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of STMicroelectronics nor the names of its contributors
 *      may be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
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
 *
 * BlueNRG-Mesh is based on Motorola’s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 *
 ******************************************************************************
 */
package com.st.bluenrgmesh.adapter.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyClassificationAdapter extends RecyclerView.Adapter<KeyClassificationAdapter.ViewHolder> {

    private Context context;
    private Element element;
    private AppKey appkey;
    private HashMap<AppKey, ArrayList<Element>> elementsByKey;
    private ArrayList<AppKey> appKeys = new ArrayList<>();

    public KeyClassificationAdapter(Context context, Element element, AppKey appKey) {
        this.context = context;
        this.element = element;
        this.appkey = appKey;
    }

    public KeyClassificationAdapter(Context context, HashMap<AppKey, ArrayList<Element>> elementsByKey) {
        this.context = context;
        this.elementsByKey = elementsByKey;
        for(AppKey key : elementsByKey.keySet())
        {
            appKeys.add(key);
        }
    }

    public interface IRecyclerViewHolderClicks {

        void notifyAdapter(int position, AppKey appKey);
    }

    @NonNull
    @Override
    public KeyClassificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_keyclassification, parent, false);
        KeyClassificationAdapter.ViewHolder vh = new KeyClassificationAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull KeyClassificationAdapter.ViewHolder viewHolder, int position) {

        //elementsByKey.keySet()
        viewHolder.txtKey.setText(appKeys.get(position).getName());
        segmentationByModel(appKeys.get(position), elementsByKey.get(appKeys.get(position)), viewHolder);
    }

    private void segmentationByModel(AppKey appKey, ArrayList<Element> elements, ViewHolder viewHolder) {

        //hash map for model array
        ArrayList<String> models = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            for (int j = 0; j < elements.get(i).getModels().size(); j++) {
                boolean isPresent = false;
                for (int k = 0; k < models.size(); k++) {
                    if(models.get(k).equalsIgnoreCase(elements.get(i).getModels().get(j).getModelName()))
                    {
                        isPresent = true;
                        break;
                    }
                }

                if(!isPresent)
                {
                    models.add(elements.get(i).getModels().get(j).getModelName());
                }
            }
        }


        final HashMap<String, ArrayList<Element>> hashModel = new HashMap<>();
        for (int i = 0; i < models.size(); i++) {
            ArrayList<Element> elementInModel = new ArrayList<>();
            for (int j = 0; j < elements.size(); j++) {
                boolean isPresent = false;
                for (int k = 0; k < elements.get(j).getModels().size(); k++) {
                    if(models.get(i).equalsIgnoreCase(elements.get(j).getModels().get(k).getModelName()))
                    {
                        if(elements.get(j).getModels().get(k).getBind() != null && elements.get(j).getModels().get(k).getBind().size() > 0)
                        {
                            for (int l = 0; l < elements.get(j).getModels().get(k).getBind().size(); l++) {
                                if(elements.get(j).getModels().get(k).getBind().get(l) == appKey.getIndex())
                                {
                                    isPresent = true;
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }

                if(isPresent)
                {
                    elementInModel.add(elements.get(j));
                }
            }

            if(elementInModel.size() > 0)
            {
                hashModel.put(models.get(i), elementInModel);
            }
        }

        if(hashModel.size() > 0)
        {
            NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false);
            viewHolder.modelsByKeyRecycler.setLayoutManager(gridLayoutManager);
            ModelClassificationAdapter modelClassificationAdapter = new ModelClassificationAdapter(context, hashModel, new ModelClassificationAdapter.IRecyclerViewHolderClicks() {
                @Override
                public void onClickListener(int position, String modelName) {
                    Utils.showElementByModelPopup(context, hashModel.get(modelName) , modelName);
                }
            });
            viewHolder.modelsByKeyRecycler.setAdapter(modelClassificationAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return appKeys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtKey;
        private final ImageView imgShare;
        private final RecyclerView modelsByKeyRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtKey = (TextView) itemView.findViewById(R.id.txtKey);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            modelsByKeyRecycler = (RecyclerView) itemView.findViewById(R.id.modelsByKeyRecycler);
        }
    }
}
