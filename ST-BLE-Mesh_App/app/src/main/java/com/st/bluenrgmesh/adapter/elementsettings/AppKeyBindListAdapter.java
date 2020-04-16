/**
 ******************************************************************************
 * @file    AppKeyBindListAdapter.java
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.RecyclerItemTouchHelper;
import com.st.bluenrgmesh.callbacks.appkeymanagement.DeleteAppKeyCallback;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AppKeyBindListAdapter extends RecyclerView.Adapter<AppKeyBindListAdapter.ViewHolder> implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private final Context context;
    private final ArrayList<AppKey> appKeys;
    private final AppKeyBindListAdapter.IRecyclerViewHolderClicks listener;
    private final ArrayList<Integer> bind;
    private final boolean isDialogContext;
    private final Element element;
    private final String fromwhere;
    public ViewHolder holderView;

    public AppKeyBindListAdapter(Context context, Element element, boolean isDialogContext, ArrayList<AppKey> appKeys, ArrayList<Integer> bind, IRecyclerViewHolderClicks listener ,String fromwhere) {
        this.context = context;
        this.element = element;
        this.isDialogContext = isDialogContext;
        this.appKeys = appKeys;
        this.bind = bind;
        this.listener = listener;
        this.fromwhere = fromwhere;
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(element == null)
            return;
        Utils.DEBUG(">> Delete Appkey");
        //used listener to select item
        if(direction == ItemTouchHelper.LEFT)
        {
            listener.onUnbindRecyclerItem(null, position, appKeys.get(position));
            new DeleteAppKeyCallback(context, element, appKeys.get(position));
        }
        else
        {
            this.onViewRecycled(holderView);
            this.notifyItemChanged(position);
        }

    }

    public static interface IRecyclerViewHolderClicks {
        public void onClickRecyclerItem(View v, int position, AppKey data, boolean isChecked);
        public void onUnbindRecyclerItem(View v, int position, AppKey data);
    }

    @Override
    public AppKeyBindListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_appkey_list, parent, false);

        AppKeyBindListAdapter.ViewHolder vh = new AppKeyBindListAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final AppKeyBindListAdapter.ViewHolder holder, final int position) {

        holder.txtAppKeyName.setText(appKeys.get(position).getName());
        holder.txtAppKey.setText(appKeys.get(position).getKey());
        holder.txtAppKeyIndex.setText("Index : " + appKeys.get(position).getIndex()+"");

        //if(Utils.getDefaultAppKeyIndex(context) == appKeys.get(position).getIndex())
        if(appKeys.get(position).isChecked())
        {
            holder.chkAppKey.setChecked(true);
            holder.chkAppKey.setClickable(false);
        }
        else
        {
            holder.chkAppKey.setChecked(false);
        }

        /*if(isDialogContext)
        {
            if(appKeys.get(position).isChecked())
            {
                holder.chkAppKey.setChecked(true);
                holder.chkAppKey.setClickable(false);
            }
            else
            {
                holder.chkAppKey.setChecked(false);
            }
        }
        else
        {
            if(appKeys.get(position).isChecked())
            {
                holder.chkAppKey.setChecked(true);
                holder.chkAppKey.setClickable(false);
            }
            else
            {
                holder.chkAppKey.setChecked(false);
            }
            *//*if(appKeys.get(position).getKey().equalsIgnoreCase(Utils.getDefaultAppKeyIndex(context)))
            {
                holder.chkAppKey.setChecked(true);
                holder.chkAppKey.setClickable(false);
            }*//*
        }*/

        holder.chkAppKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.chkAppKey.isChecked())
                {
                    holder.chkAppKey.setChecked(holder.chkAppKey.isChecked());
                    listener.onClickRecyclerItem(null, position,appKeys.get(position), holder.chkAppKey.isChecked());
                }
                else {
                    holder.chkAppKey.setChecked(true);
                }
            }
        });

        holder.view_forground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holderView = holder;
                listener.onUnbindRecyclerItem(null, position,appKeys.get(position));
                return true;
            }
        });

        if ("elementpopup".equalsIgnoreCase(fromwhere)) {
            holder.unbindkeyTV.setText("Delete this key");
        } else {
            holder.unbindkeyTV.setText("Unbind this key");
        }
    }

    @Override
    public int getItemCount() {
        return appKeys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtAppKey;
        private TextView txtAppKeyName;
        private TextView txtAppKeyIndex;
        private RadioButton chkAppKey;
        public LinearLayout view_forground;
        public LinearLayout view_background;
        public LinearLayout lytUnbindKey;
        public TextView unbindkeyTV;

        public ViewHolder(View itemView) {
            super(itemView);

            txtAppKey = (TextView) itemView.findViewById(R.id.txtAppKey);
            txtAppKeyName = (TextView) itemView.findViewById(R.id.txtAppKeyName);
            txtAppKeyIndex = (TextView) itemView.findViewById(R.id.txtAppKeyIndex);
            chkAppKey = (RadioButton) itemView.findViewById(R.id.chkAppKey);
            view_background = (LinearLayout) itemView.findViewById(R.id.view_background);
            view_forground = (LinearLayout) itemView.findViewById(R.id.view_forground);
            lytUnbindKey = (LinearLayout) itemView.findViewById(R.id.lytUnbindKey);
            unbindkeyTV  = (TextView) itemView.findViewById(R.id.unbindkeyTV);
        }
        public Context getContextReference(){
            return context;
        }
    }



}
