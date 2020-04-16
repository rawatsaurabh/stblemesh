/**
 ******************************************************************************
 * @file    SubGrpSettingAdapter.java
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
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Nodes;

import java.util.ArrayList;



public class SubGrpSettingAdapter extends RecyclerView.Adapter<SubGrpSettingAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Element> eleSubList;
    private final String group_addr;
    private final SubGrpSettingAdapter.IRecyclerViewHolderClicks listener;
    private final MeshRootClass meshRootClass;
    private final boolean isAddGroup;


    public SubGrpSettingAdapter(Context context, ArrayList<Element> eleSelected, String group_addr, MeshRootClass meshRootClass, boolean isAddGroup,  IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {

        this.context = context;
        this.eleSubList = eleSelected;
        this.group_addr = group_addr;
        this.isAddGroup = isAddGroup;
        this.listener = iRecyclerViewHolderClicks;
        this.meshRootClass = meshRootClass;
    }


    public static interface IRecyclerViewHolderClicks {
        public void onClickRecyclerItem(View v, int position, Element element, String address, boolean b);
        public void onClickNodeRecyclerItem(View v, int position, Nodes node, String address, boolean b);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_row, parent, false);
        SubGrpSettingAdapter.ViewHolder vh = new SubGrpSettingAdapter.ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(eleSubList.size() == 0)
            return;

        holder.txtView.setVisibility(View.GONE);
        /*if (("c000".equalsIgnoreCase(group_addr))) {
            holder.radioButton.setChecked(true);
        } else {

        }*/
        if(!isAddGroup)
        {
            holder.checkbox.setChecked(eleSubList.get(position).isChecked() ? true : false);
        }
        else
        {
            holder.checkbox.setChecked(eleSubList.get(position).isChecked() ? true : false);
        }

        holder.checkbox.setText(eleSubList.get(position).getParentNodeName() + "/" + eleSubList.get(position).getName());
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!"c000".equalsIgnoreCase(group_addr))
                {
                    listener.onClickRecyclerItem(v, position, eleSubList.get(position), eleSubList.get(position).getUnicastAddress(), true);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return eleSubList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkbox;
        private final TextView txtView;

        public ViewHolder(View itemView) {
            super(itemView);

            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            txtView = (TextView) itemView.findViewById(R.id.txtView);

        }
    }
}
