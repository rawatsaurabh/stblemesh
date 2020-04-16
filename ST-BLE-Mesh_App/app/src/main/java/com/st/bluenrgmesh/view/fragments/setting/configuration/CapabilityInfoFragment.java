/**
 * *****************************************************************************
 *
 * @file CapabilityInfoFragment.java
 * @author BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
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
package com.st.bluenrgmesh.view.fragments.setting.configuration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msi.moble.Capabilities;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import java.util.HashMap;

import static com.google.android.gms.vision.L.TAG;

public class CapabilityInfoFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private TextView txtElementCount;
    private TextView txtAlgorithType;
    private TextView txtPublicKeyOOBSupport;
    private TextView txtStaticOOBtype;
    private TextView txtOutputOOBSize;
    private TextView txtOutputOOBAction;
    private TextView txtInputOOBSize;
    private Capabilities capabilities;
    public static HashMap<Integer, String> mOOBOutputAction = new HashMap<Integer, String>();
    private int val;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_capability_info, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        initUI();

        return view;
    }

    private void initUI() {

        capabilities = (Capabilities) getArguments().getSerializable(getString(R.string.key_serializable));
        txtElementCount = (TextView) view.findViewById(R.id.txtElementCount);
        txtAlgorithType = (TextView) view.findViewById(R.id.txtAlgorithType);
        txtPublicKeyOOBSupport = (TextView) view.findViewById(R.id.txtPublicKeyOOBSupport);
        txtStaticOOBtype = (TextView) view.findViewById(R.id.txtStaticOOBtype);
        txtOutputOOBSize = (TextView) view.findViewById(R.id.txtOutputOOBSize);
        txtOutputOOBAction = (TextView) view.findViewById(R.id.txtOutputOOBAction);
        txtInputOOBSize = (TextView) view.findViewById(R.id.txtInputOOBSize);

        if(capabilities != null)
        {
            txtElementCount.setText(capabilities.getElementCount()+"");
            txtAlgorithType.setText(capabilities.getAlgorithms()+"");
           String pubkey_str;
            int value = Integer.parseInt(Utils.array2string(capabilities.getPkTypes()));
            if (value>0) {
                pubkey_str = "Public Key OOB (0x01)";
            } else {
                pubkey_str = "Not Available";

            }
            txtPublicKeyOOBSupport.setText(pubkey_str+"");
            String staticoob_str ="Not Available";
            if(capabilities.getStaticOOBTypes()==1){
                staticoob_str="Static OOB Information Available";
            }
            txtStaticOOBtype.setText(staticoob_str+"");
            txtOutputOOBSize.setText(capabilities.getOutputOOBSize()+"");

            mOOBOutputAction.put(0,"Not Available");         // value = 0(int)
            mOOBOutputAction.put(1,"Blink (0x01)");
            mOOBOutputAction.put(2,"Beep (0x02)");
            mOOBOutputAction.put(3,"Blink, Beep (0x03)");
            mOOBOutputAction.put(4,"Vibrate (0x04)");
            mOOBOutputAction.put(5,"Blink, Vibrate (0x05)");
            mOOBOutputAction.put(6,"Beep, Vibrate (0x06)");
            mOOBOutputAction.put(7,"Blink, Beep, Vibrate (0x07)");
            mOOBOutputAction.put(8,"Output Numeric (0x08)");
            mOOBOutputAction.put(9,"Blink, Output Numeric (0x09)");
            mOOBOutputAction.put(10,"Beep, Output Numeric (0x0A)");
            mOOBOutputAction.put(11,"Blink, Beep, Output Numeric (0x0B)");                 // 1 at pos 1
            mOOBOutputAction.put(12,"Vibrate, Output Numeric (0x0C) ");
            mOOBOutputAction.put(13,"Blink, Vibrate, Output Numeric (0x0D)");
            mOOBOutputAction.put(14,"Beep, Vibrate, Output Numeric (0x0E)");
            mOOBOutputAction.put(15,"Blink, Beep, Vibrate, Output Numeric (0x0F)");
            mOOBOutputAction.put(16,"Output Alphanumeric (0x10)");
            mOOBOutputAction.put(17,"Blink, Output Alphanumeric (0x11)");
            mOOBOutputAction.put(18,"Beep, Output Alphanumeric (0x12)");
            mOOBOutputAction.put(19,"Blink, Beep, Output Alphanumeric (0x13)");
            mOOBOutputAction.put(20,"Vibrate, Output Alphanumeric (0x14)");
            mOOBOutputAction.put(21,"Blink, Vibrate, Output Alphanumeric (0x15)");
            mOOBOutputAction.put(22,"Beep, Vibrate, Output Alphanumeric (0x16)");
            mOOBOutputAction.put(23,"Blink, Beep, Vibrate, Output Alphanumeric (0x17)");
            mOOBOutputAction.put(24,"Output Numeric, Output Alphanumeric (0x18)");
            mOOBOutputAction.put(25,"Blink, Output Numeric, Output Alphanumeric (0x19)");
            mOOBOutputAction.put(26,"Beep, Output Numeric, Output Alphanumeric (0x1A)");
            mOOBOutputAction.put(27,"Blink, Beep, Output Numeric, Output Alphanumeric (0x1B)");
            mOOBOutputAction.put(28,"Vibrate, Output Numeric, Output Alphanumeric (0x1C)");
            mOOBOutputAction.put(29,"Blink, Vibrate, Output Numeric, Output Alphanumeric (0x1D)");
            mOOBOutputAction.put(30,"Beep, Vibrate, Output Numeric, Output Alphanumeric (0x1E)");
            mOOBOutputAction.put(31,"Blink, Beep, Vibrate, Output Numeric, Output Alphanumeric (0x1F)");


            val = capabilities.getOutputOOBActions();
            txtOutputOOBAction.setText(mOOBOutputAction.get(val));
            //txtOutputOOBAction.setText("No way");

            //Log.i(TAG, "initUI: "+ val+ " str:  "+ mOOBOutputAction.get(val));

            /*if(capabilities.getOutputOOBActions()==0){
                txtOutputOOBAction.setText("Blink (0x01)");

            }else{
                txtOutputOOBAction.setText(capabilities.getOutputOOBActions()+"");

            }
            txtInputOOBSize.setText(capabilities.getInputOOBSize()+"");*/
        }
    }
}
