/**
 * *****************************************************************************
 *
 * @file SubListForGroupAdapter.java
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

package com.st.bluenrgmesh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Nodes;

import java.util.ArrayList;


public class SubListForGroupAdapter extends RecyclerView.Adapter<SubListForGroupAdapter.ViewHolder> {


    private final ArrayList<Nodes> nodeSubscriptionList;
    private final MeshRootClass meshRootClass;
    private Context context;
    private ArrayList<Element> eleSubList;
    private String group_addr;
    private String type;
    private IRecyclerViewHolderClicks listener;

    public static interface IRecyclerViewHolderClicks {

        public void onClickRecyclerItem(View v, int position, Element element, String address, boolean b);

        public void onClickNodeRecyclerItem(View v, int position, Nodes node, String address, boolean b);
    }

    //remove this
    public SubListForGroupAdapter(Context context, ArrayList<Nodes> nodeSubscriptionList, ArrayList<Element> eleSelected, String group_addr, String type, MeshRootClass meshRootClass, IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {

        this.context = context;
        this.nodeSubscriptionList = nodeSubscriptionList;
        this.eleSubList = eleSelected;
        this.group_addr = group_addr;
        this.type = type;
        this.listener = iRecyclerViewHolderClicks;
        this.meshRootClass = meshRootClass;
    }

    public SubListForGroupAdapter(Context context, ArrayList<Element> eleSelected, String group_addr, String type, MeshRootClass meshRootClass, IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {

        this.context = context;
        this.eleSubList = eleSelected;
        this.nodeSubscriptionList = null;
        this.group_addr = group_addr;
        this.type = type;
        this.listener = iRecyclerViewHolderClicks;
        this.meshRootClass = meshRootClass;
    }

    @Override
    public SubListForGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_row, parent, false);

        SubListForGroupAdapter.ViewHolder vh = new SubListForGroupAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(SubListForGroupAdapter.ViewHolder holder, final int position) {

        if (context.getResources().getBoolean(R.bool.bool_isElementFunctionality)) {
            holder.txtView.setVisibility(View.GONE);
            if (type.equals("isLightsGroupSetting") && ("c000".equalsIgnoreCase(group_addr))) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(eleSubList.get(position).isChecked() ? true : false);
            }

            /*String parentNodeName = null;
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                //if(meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(eleSubList.get(position).getUnicastAddress()))
                if(meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(eleSubList.get(position).getParentNodeAddress()))
                {
                    parentNodeName = meshRootClass.getNodes().get(i).getName();
                }
            }*/
            //holder.radioButton.setText(parentNodeName + "/" + eleSubList.get(position).getName());
            holder.radioButton.setText(eleSubList.get(position).getParentNodeName() + "/" + eleSubList.get(position).getName());
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equalsIgnoreCase("Add Group")) {
                        listener.onClickRecyclerItem(v, position, eleSubList.get(position), eleSubList.get(position).getUnicastAddress(), true);

                    } else {
                        if (!"c000".equalsIgnoreCase(group_addr)) {
                            listener.onClickRecyclerItem(v, position, eleSubList.get(position), eleSubList.get(position).getUnicastAddress(), true);
                        }
                    }
                }
            });
        } else {
            holder.txtView.setVisibility(View.GONE);
            if (type.equals("isLightsGroupSetting")) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(nodeSubscriptionList.get(position).isChecked() ? true : false);
            }

            holder.radioButton.setText(nodeSubscriptionList.get(position).getName() + " : " + nodeSubscriptionList.get(position).getM_address());
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!type.equals("isLightsGroupSetting")) {
                        listener.onClickNodeRecyclerItem(v, position, nodeSubscriptionList.get(position), nodeSubscriptionList.get(position).getM_address(), true);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (context.getResources().getBoolean(R.bool.bool_isElementFunctionality)) {
            return eleSubList.size();
        } else {
            return nodeSubscriptionList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton radioButton;
        private final TextView txtView;

        public ViewHolder(View itemView) {
            super(itemView);

            radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
            txtView = (TextView) itemView.findViewById(R.id.txtView);

        }
    }
}
