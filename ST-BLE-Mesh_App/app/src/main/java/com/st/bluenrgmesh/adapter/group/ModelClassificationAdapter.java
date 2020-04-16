/**
 ******************************************************************************
 * @file    ModelClassificationAdapter.java
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ModelClassificationAdapter extends RecyclerView.Adapter<ModelClassificationAdapter.ViewHolder> {

    private final Context context;
    private final HashMap<String, ArrayList<Element>> models;
    private final IRecyclerViewHolderClicks listener;
    private ArrayList<String> model = new ArrayList<>();

    public ModelClassificationAdapter(Context context, HashMap<String, ArrayList<Element>> models, IRecyclerViewHolderClicks listener) {

        this.context = context;
        this.models = models;
        this.listener = listener;

        for (String modelName : models.keySet())
        {
            model.add(modelName);
        }
    }

    public interface IRecyclerViewHolderClicks {

        void onClickListener(int position, String modelname);
    }

    @NonNull
    @Override
    public ModelClassificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_modelclassification, parent, false);
        ModelClassificationAdapter.ViewHolder vh = new ModelClassificationAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ModelClassificationAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txtModelName.setText(model.get(i));
        viewHolder.txtModelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(i, model.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtModelName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtModelName = (TextView) itemView.findViewById(R.id.txtModelName);
        }
    }
}
