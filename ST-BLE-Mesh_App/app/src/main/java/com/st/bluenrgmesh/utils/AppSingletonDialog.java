/**
 ******************************************************************************
 * @file    AppSingletonDialog.java
 * @author  BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.services.PublicationService;
import com.st.bluenrgmesh.services.SubscriptionService;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;

public class AppSingletonDialog {

    private static Dialog dialog;
    private Nodes nodeData;
    private ProgressBar progressBarSub;
    private ProgressBar progressBarPub;
    private TextView txtSubMsg;
    private TextView txtPubMsg;
    private LinearLayout lytSubscription;
    private LinearLayout lytPublication;
    private LinearLayout lytChildSubscription;
    private LinearLayout lytChildPublication;
    private Button but;
    //private static int count;
    private static int subCount;
    private static int pubCount;
    private LinearLayout lytUI;
    private ProgressBar progressUI;
    private Context contextActivity;

    public AppSingletonDialog(Context context, final Nodes node)
    {
        this.contextActivity = context;
        this.nodeData = node;
        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            dialog.setContentView(R.layout.dialog_app_singleton);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    pubCount = 0;
                    subCount = 0;
                    if(dialog != null)
                    {
                        Utils.saveModelInfoOfMultiElement(contextActivity, null);
                        Utils.saveModelInfo(contextActivity, null);
                        Utils.setNodeFeatures(contextActivity, null);
                        contextActivity.stopService(new Intent(contextActivity, SubscriptionService.class));
                        contextActivity.stopService(new Intent(contextActivity, PublicationService.class));
                        dialog.dismiss();
                        dialog = null;
                    }
                }
            });

            lytUI = (LinearLayout) dialog.findViewById(R.id.lytUI);
            lytUI.setVisibility(View.GONE);
            progressUI = (ProgressBar) dialog.findViewById(R.id.progressUI);
            progressUI.setMax(4); //consider 4 step for updating final node data

            lytSubscription = (LinearLayout) dialog.findViewById(R.id.lytSubscription);
            progressBarSub = (ProgressBar) dialog.findViewById(R.id.progressSubscriber);
            lytChildSubscription = (LinearLayout) dialog.findViewById(R.id.lytChildSubscription);
            txtSubMsg = (TextView) dialog.findViewById(R.id.txtSubMsg);

            lytPublication = (LinearLayout) dialog.findViewById(R.id.lytPublication);
            progressBarPub = (ProgressBar) dialog.findViewById(R.id.progressPublish);
            lytChildPublication = (LinearLayout) dialog.findViewById(R.id.lytChildPublication);
            txtPubMsg = (TextView) dialog.findViewById(R.id.txtPubMsg);

            but = (Button) dialog.findViewById(R.id.but);
            but.setVisibility(View.GONE);
            final Context finalContext = context;
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //set configured node
                    if(subCount == 0 && pubCount == 0)
                    {
                        //merge pub and sub settings
                        lytUI.setVisibility(View.GONE);
                        contextActivity.stopService(new Intent(contextActivity, SubscriptionService.class));
                        contextActivity.stopService(new Intent(contextActivity, PublicationService.class));
                        updateNodeData(finalContext, nodeData);
                    }
                    else
                    {
                        Utils.showToast(contextActivity, "Configuration is in process.");
                    }
                }
            });

            if(nodeData != null)
            {
                Utils.layLoopForElements(dialog.getContext(), lytChildSubscription, nodeData);
                Utils.layLoopForElements(dialog.getContext(), lytChildPublication, nodeData);
            }

            if(subCount == 0 && pubCount == 0)
            {
                subCount = 1;
                pubCount = 1;
                //count = 0;
                int subCountValue = 0;
                int pubCountValue = 0;
                if(nodeData != null && nodeData.getElements() != null)
                {
                    for (int i = 0; i < nodeData.getElements().size(); i++) {
                        for (int j = 0; j <  nodeData.getElements().get(i).getModels().size(); j++)
                        {
                            Model model = nodeData.getElements().get(i).getModels().get(j);
                            //replace this case w.r.t model id check
                            if (model.getModelName().contains("GENERIC ONOFF SERVER") ||
                                    model.getModelName().contains("ST VENDOR SERVER") ||
                                    model.getModelName().contains("LIGHT LIGHTNESS SERVER") ||
                                    model.getModelName().contains("LIGHT HSL SERVER") ||
                                    model.getModelName().contains("GENERIC LEVEL SERVER")) {
                                subCountValue++;
                                pubCountValue++;
                                //count++;
                            }

                            /*if(model.getModelName().contains("CLIENT") || model.getModelName().contains("VENDOR"))
                            {
                                pubCountValue++;
                            }*/
                        }

                    }
                    progressBarSub.setMax(subCountValue);
                    progressBarPub.setMax(pubCountValue);
                }
            }

            if(!dialog.isShowing()){
                dialog.show();
            }
        }
    }

    public /*synchronized*/ void showPopUpForConfig(final Context context, final String responseMessage, boolean showDialog, final Nodes node) {

        if (context == null)
            return;

        if (showDialog) {
            if(dialog != null && !dialog.isShowing())
            {
                dialog.show();
            }
        } else {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, 1000);

        }

        if(responseMessage.contains("Sub"))
        {
            subscriptionUi(dialog, responseMessage, showDialog, node);
        }
        else if(responseMessage.contains("Pub"))
        {
            publicationUi(dialog, responseMessage, showDialog, node);
        }
    }

    private void updateNodeData(final Context context, final Nodes node) {

        ((MainActivity)context).isProvisioningProcessLive = false;
        Utils.showToast(context,"Configuration Done");
        Utils.DEBUG(">>Config Completed for " + node.getName());
        //((MainActivity)context).mUserDataRepository.getNewDataFromRemote("Set Current Node ==>" + address, LoggerConstants.TYPE_SEND);
        node.setConfigComplete(true);
        node.setConfigured("true");
        //((MainActivity)context).showPopUpForProxy(context, context.getString(R.string.str_update_features) + "\n" + nodeData.getName(), true);
        //updateFeaturesAndModels(context, node);
        progressUI.setProgress(2);
        Utils.onNodeConfigured(context, node, ((MainActivity)context).meshRootClass);
        ((MainActivity)context).updateJsonData();
        progressUI.setProgress(4);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ((MainActivity)context).updateModelTab();
                ((MainActivity)context).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
                ((MainActivity)context).updateProvisionedTab(node.getUUID(), context.getResources().getInteger(R.integer.PROVISIONED_FEATURES_UPDATE));
                //((MainActivity)context).showPopUpForProxy(context, "Configuration Done For... " + "\n" + node.getName(), false);
                progressUI.setProgress(5);
                if(dialog != null)
                {
                    dialog.dismiss();
                    dialog = null;
                }
                //((MainActivity)context).isQuickBinding = false;

                ((MainActivity)context).onBackPressed(); // loadconfiguration fragment
                ((MainActivity)context).onBackPressed();  //configurationfragment
            }
        }, 1000);
    }

    public void subscriptionUi(Dialog dialog, String responseMessage, boolean showDialog, Nodes node) {

        if(dialog == null)
            return;

        if(progressBarSub != null)
        {
            if(responseMessage.equalsIgnoreCase("Subscription Done"))
            {
                subCount = 0;
                but.setVisibility(View.VISIBLE);
                txtSubMsg.setTextColor(dialog.getContext().getColor(R.color.colorPrimaryDark));
                setUpConfigData(node, true);
                //progressBarSub.setIndeterminateDrawable(dialog.getContext().getDrawable(R.drawable.ic_check_black_24dp));
            }

            if(subCount != 0)
            {
                progressBarSub.setProgress(subCount);
                subCount++;
            }
            txtSubMsg.setText(responseMessage);
        }
    }

    public void publicationUi(Dialog dialog, String responseMessage, boolean showDialog, Nodes node) {
        if(dialog == null)
            return;

        if(progressBarPub != null)
        {
            if(responseMessage.equalsIgnoreCase("Publication Done"))
            {
                pubCount = 0;
                but.setVisibility(View.VISIBLE);
                txtPubMsg.setTextColor(dialog.getContext().getColor(R.color.colorPrimaryDark));
                setUpConfigData(node, false);

                if(subCount == 0 && pubCount == 0 && contextActivity.getResources().getBoolean(R.bool.bool_isAutoProvisioning) && Utils.isAutoProvisioning(contextActivity))
                {
                    //merge pub and sub settings
                    lytUI.setVisibility(View.GONE);
                    contextActivity.stopService(new Intent(contextActivity, SubscriptionService.class));
                    contextActivity.stopService(new Intent(contextActivity, PublicationService.class));
                    updateNodeData(contextActivity, nodeData);
                }
                //progressBarPub.setIndeterminateDrawable(dialog.getContext().getDrawable(R.drawable.ic_check_black_24dp));
            }

            if(pubCount != 0)
            {
                progressBarPub.setProgress(pubCount);
                pubCount++;
            }

            txtPubMsg.setText(responseMessage);
        }
    }

    private void setUpConfigData(Nodes node, boolean isSubscription) {

        // element -> models -> set subscription and publication

        if(nodeData.getUUID().equals(node.getUUID()))
        {
            for (int i = 0; i < nodeData.getElements().size(); i++) {
                for (int j = 0; j < nodeData.getElements().get(i).getModels().size(); j++) {
                    try {
                        if (isSubscription) {
                            nodeData.getElements().get(i).getModels().get(j).
                                    setSubscribe(node.getElements().get(i).getModels().get(j).getSubscribe());
                        } else {
                            nodeData.getElements().get(i).getModels().get(j).
                                    setPublish(node.getElements().get(i).getModels().get(j).getPublish());
                        }
                    }catch (Exception e){}
                }
            }
        }
    }

}
