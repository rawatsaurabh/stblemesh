/**
 ******************************************************************************
 * @file    ModelListAdapter.java
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
package com.st.bluenrgmesh.adapter.elementsettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.Model;

import java.util.ArrayList;

public class ModelListAdapter extends RecyclerView.Adapter<ModelListAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Model> models;
    private final IRecyclerViewHolderClicks listener;
    //private final ArrayList<Integer> bind;

    public ModelListAdapter(Context context, ArrayList<Model> models/*, ArrayList<Integer> bind*/, IRecyclerViewHolderClicks listener) {
        this.context = context;
        this.models = models;
        //this.bind = bind;
        this.listener = listener;
    }

    public static interface IRecyclerViewHolderClicks {
        public void onClickRecyclerItem(View v, int position, Model model);
    }

    @Override
    public ModelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_model_list, parent, false);

        ModelListAdapter.ViewHolder vh = new ModelListAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ModelListAdapter.ViewHolder holder, final int position) {

        holder.txtModelRow.setText(models.get(position).getModelName());
        holder.txtModelIndex.setText(models.get(position).getModelId());
        holder.txtModelRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickRecyclerItem(view, position, models.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtModelIndex;
        private TextView txtModelRow;

        public ViewHolder(View itemView) {
            super(itemView);

            txtModelRow = (TextView) itemView.findViewById(R.id.txtModelRow);
            txtModelIndex = (TextView) itemView.findViewById(R.id.txtModelIndex);
        }
    }
}
