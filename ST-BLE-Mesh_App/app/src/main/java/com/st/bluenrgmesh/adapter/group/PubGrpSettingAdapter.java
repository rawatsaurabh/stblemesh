/**
 * *****************************************************************************
 *
 * @file PublicationListForGroupSettingsAdapter.java
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

package com.st.bluenrgmesh.adapter.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.ApplicationParameters;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.Element;

import java.util.ArrayList;
import java.util.List;


public class PubGrpSettingAdapter extends RecyclerView.Adapter<PubGrpSettingAdapter.ViewHolder> {

    private final Context context;
    private final String type;
    private final String grpAddress;
    private final String parentnODEnAME;
    //private  ArrayList<Publish> final_publication_list;
    private  ArrayList<Element> final_publication_list;
    private IRecyclerViewHolderClicks listener;
    private ApplicationParameters.Address address = null;

    public void setPubList(ApplicationParameters.Address address) {
        this.address = address;
    }

    public interface IRecyclerViewHolderClicks {

        void notifyAdapter(int position, List<Element> final_publication_list, boolean isSelected);

    }

    public PubGrpSettingAdapter(Context context, String parentnODEnAME, String grpAddress, String type, ArrayList<Element> final_publication_list, IRecyclerViewHolderClicks IRecyclerViewHolderClicks ) {

        this.context = context;
        this.parentnODEnAME = parentnODEnAME;
        this.type = type;
        this.grpAddress = grpAddress;
        this.final_publication_list = final_publication_list;
        this.listener=IRecyclerViewHolderClicks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(final_publication_list.isEmpty() || final_publication_list.size() == 0)
            return;

        holder.txtView.setText(/*parentnODEnAME*/ final_publication_list.get(position).getParentNodeName() + "/" + final_publication_list.get(position).getName());
        //According to note 1 : as given in utils class

        if(final_publication_list.get(position).getModels() != null)
        {
            try {
                String pubAddress = final_publication_list.get(position).getModels().get(0).getPublish().getAddress();
                if (pubAddress.equalsIgnoreCase(grpAddress)) {
                    holder.radioButton.setChecked(true);
                } else {
                    holder.radioButton.setChecked(false);
                }
            }catch (Exception e){}
        }
        else
        {
            //node is not configured
            holder.radioButton.setVisibility(View.GONE);
            holder.txtView.setVisibility(View.GONE);
        }


        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    RadioButton viewById = (RadioButton) v.findViewById(R.id.radioButton);
                    if(viewById.isChecked())
                    {
                        listener.notifyAdapter(position,final_publication_list,true);
                    }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (final_publication_list != null ) {
            if (final_publication_list.size() == 0) {
                return 1;
            } else {
                return final_publication_list.size();
            }
        }
        return 0;
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
