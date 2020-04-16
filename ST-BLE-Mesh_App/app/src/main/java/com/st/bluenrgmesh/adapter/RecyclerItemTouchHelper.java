/**
 ******************************************************************************
 * @file    RecyclerItemTouchHelper.java
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

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.adapter.elementsettings.AppKeyBindListAdapter;
import com.st.bluenrgmesh.adapter.elementsettings.ElementsRecyclerAdapter;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;

import java.util.logging.Handler;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final RecyclerItemTouchHelperListener listener;

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     */
    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((AppKeyBindListAdapter.ViewHolder) viewHolder).view_forground;
            final View backgroundView = ((AppKeyBindListAdapter.ViewHolder) viewHolder).view_background;

            getDefaultUIUtil().onSelected(foregroundView);
            getDefaultUIUtil().onSelected(backgroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((AppKeyBindListAdapter.ViewHolder) viewHolder).view_forground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((AppKeyBindListAdapter.ViewHolder) viewHolder).view_forground;
        //final View backgroundView = ((AppKeyBindListAdapter.ViewHolder) viewHolder).view_background;
        getDefaultUIUtil().clearView(foregroundView);
        //getDefaultUIUtil().clearView(backgroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, final float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((AppKeyBindListAdapter.ViewHolder) viewHolder).view_forground;
        final View backgroundView = ((AppKeyBindListAdapter.ViewHolder) viewHolder).view_background;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);

        /*if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {



        } else {

            //foregroundView.setTranslationX(dX);
            //getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, -dX, dY, actionState, true);
            super.onChildDraw(c, recyclerView, viewHolder, -dX, dY, actionState, true);
        }*/


         /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



            }
        }, 700);*/


       // super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);



        //
    }



    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }
}
