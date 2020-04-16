/**
 ******************************************************************************
 * @file    PublicationListAdapter.java
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

package com.st.bluenrgmesh.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Publish;

import java.util.List;


public class PublicationListAdapter extends RecyclerView.Adapter<PublicationListAdapter.ViewHolder> {

    private  MeshRootClass meshRootClass;
    private List<Publish> publicationList;
    private String type;
    private IRecyclerViewHolderClicks listener;
    private Context context;


    public PublicationListAdapter(Context context,List<Publish> elePublicationList, String publicationAddress, String type, MeshRootClass meshRootClass, IRecyclerViewHolderClicks listener) {

        this.context = context;
        this.publicationList = elePublicationList;
        this.type = type;
        this.listener = listener;
        //this.meshRootClass=meshRootClass;

    }

    public static interface IRecyclerViewHolderClicks {

        public void onClickRecyclerItem(View view, Publish item, String mAutoAddress, boolean isSelected, int position);
    }


    @Override
    public PublicationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_publication_item, parent, false);

        PublicationListAdapter.ViewHolder vh = new PublicationListAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final PublicationListAdapter.ViewHolder holder, final int position) {
        Log.e("Error","55size===>"+publicationList.size());

        holder.txtView.setVisibility(View.GONE);
        holder.radioButton.setChecked(publicationList.get(position).isChecked() ? true : false);

        if(publicationList.get(position).isTypeNode())
        {
            //Element type
            holder.radioButton.setText(publicationList.get(position).getCurrentParentNodeName()+ "/" + publicationList.get(position).getName());
        }
        else
        {
            //Group type
            holder.radioButton.setText(publicationList.get(position).getName() + " : " + publicationList.get(position).getAddress());
        }


        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("isLightsGroupSetting")) {
                    RadioButton viewById = (RadioButton) v.findViewById(R.id.radioButton);

                    if(viewById.isChecked())
                    {
                        listener.onClickRecyclerItem(holder.radioButton,publicationList.get(position), "",true, position);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return publicationList.size();
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
