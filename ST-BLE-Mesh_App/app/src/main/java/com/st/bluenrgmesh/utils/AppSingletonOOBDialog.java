
/**
 ******************************************************************************
 * @file    AppSingletonOOBDialog.java
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
package com.st.bluenrgmesh.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;

import java.nio.ByteBuffer;

public class AppSingletonOOBDialog{

    private static Dialog dialog;
    private Context context;

    public Dialog getOOBDialogInstance(Context context)
    {
        this.context = context;
        if(dialog == null)
        {
            dialog = createOOBDialog();
        }

        return dialog;
    }

    public Dialog createOOBDialog()
    {
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_oob);
        return dialog;
    }

    public void dissmissOOBDialog()
    {
        if(dialog != null)
        {
            dialog.dismiss();
        }
    }

    public void showPopUpForOOB(final Context context, final String responseMessage, String strData) {

        if(dialog == null)
        {
            getOOBDialogInstance(context);
        }

        TextView txt = (TextView) dialog.findViewById(R.id.txtErrorMsg);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMsg);
        TextView txtName = (TextView) dialog.findViewById(R.id.txtName);
        final EditText edtInput = (EditText) dialog.findViewById(R.id.edtInput);
        Button but = (Button) dialog.findViewById(R.id.but);

        if(responseMessage.equals("OUTPUT OOB"))
        {
            txtName.setVisibility(View.GONE);
            txt.setText("Output OOB");
            txt.setTextSize(15);
            txt.setTextColor(context.getColor(R.color.ST_BLUE_DARK));
            but.setVisibility(View.VISIBLE);
            edtInput.setVisibility(View.VISIBLE);
            if(Utils.getSelectedCapability(context)!= null && Utils.getSelectedCapability(context).equalsIgnoreCase(context.getString(R.string.str_output_numeric_oob)))
            {
                edtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(8);
                edtInput.setFilters(filters);
            }
            else if(Utils.getSelectedCapability(context)!= null && Utils.getSelectedCapability(context).equalsIgnoreCase(context.getString(R.string.str_output_numeric_oob)))
            {
                edtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(1);
                edtInput.setFilters(filters);
            }
            txtMsg.setText(context.getResources().getString(R.string.str_output_oob_msg));
        }
        else
        {
            txtName.setText("Input OOB Authentication");
            txtName.setGravity(View.TEXT_ALIGNMENT_CENTER);
            but.setVisibility(View.GONE);
            edtInput.setVisibility(View.GONE);
            txt.setText(strData+"");
            txt.setTextSize(50);
            txt.setTextColor(context.getColor(R.color.ST_primary_blue));
            txtMsg.setText(context.getResources().getString(R.string.str_input_oob_msg));
        }

        if(context != null)
        {
            dialog.show();
        }


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(responseMessage.equals("OUTPUT OOB"))
                {
                    if(edtInput.getText().toString().length() > 0)
                    {
                        String s = edtInput.getText().toString();
                        //short val = Short.parseShort(s);
                        long val = Long.parseLong(s);
                        ByteBuffer dbuf = ByteBuffer.allocate(8);
                        //dbuf.putShort(val);
                        dbuf.putLong(val);
                        byte[] bytes = dbuf.array(); // { 0, 1 }
                        dialog.dismiss();
                        dialog = null;
                        ((MainActivity)context).provisionerOOB.setOutputOOBData(bytes);
                    }
                    else {
                        edtInput.setError("Field cannot be empty.");
                    }

                }
            }
        });

    }

}
