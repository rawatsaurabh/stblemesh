/*
 * Copyright (c) 2017  STMicroelectronics – All rights reserved
 * The STMicroelectronics corporate logo is a trademark of STMicroelectronics
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 *   and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 *   conditions and the following disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * - Neither the name nor trademarks of STMicroelectronics International N.V. nor any other
 *   STMicroelectronics company nor the names of its contributors may be used to endorse or
 *   promote products derived from this software without specific prior written permission.
 *
 * - All of the icons, pictures, logos and other images that are provided with the source code
 *   in a directory whose title begins with st_images may only be used for internal purposes and
 *   shall not be redistributed to any third party or modified in any way.
 *
 * - Any redistributions in binary form shall not include the capability to display any of the
 *   icons, pictures, logos and other images that are provided with the source code in a directory
 *   whose title begins with st_images.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package com.st.BlueSTSDK.gui.fwUpgrade.uploadFwFile;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;
import com.st.BlueSTSDK.Utils.FwVersion;
import com.st.BlueSTSDK.gui.NodeConnectionService;
import com.st.BlueSTSDK.gui.R;
import com.st.BlueSTSDK.gui.fwUpgrade.FirmwareType;
import com.st.BlueSTSDK.gui.fwUpgrade.FwUpgradeService;
import com.st.BlueSTSDK.gui.fwUpgrade.FwVersionViewModel;
import com.st.BlueSTSDK.gui.fwUpgrade.RequestFileUtil;
import com.st.BlueSTSDK.gui.util.InputChecker.CheckHexNumber;
import com.st.BlueSTSDK.gui.util.InputChecker.CheckMultipleOf;
import com.st.BlueSTSDK.gui.util.InputChecker.CheckNumberRange;
import com.st.BlueSTSDK.gui.util.SimpleFragmentDialog;

public class UploadOtaFileFragment extends Fragment implements UploadOtaFileActionReceiver.UploadFinishedListener{

    private static final String FINISH_DIALOG_TAG = UploadOtaFileFragment.class.getCanonicalName()+".FINISH_DIALOG_TAG";

    private static final String FW_URI_KEY = UploadOtaFileFragment.class.getCanonicalName()+".FW_URI_KEY";
    private static final String SHOW_ADDRESS_KEY = UploadOtaFileFragment.class.getCanonicalName()+".SHOW_ADDRESS_KEY";
    private static final String ADDRESS_KEY = UploadOtaFileFragment.class.getCanonicalName()+".ADDRESS_KEY";

    private static final String FW_TYPE_KEY = UploadOtaFileFragment.class.getCanonicalName()+".FW_TYPE_KEY";
    private static final String SHOW_FW_TYPE_KEY = UploadOtaFileFragment.class.getCanonicalName()+".SHOW_FW_TYPE_KEY";

    private static final String UPLOAD_PROGRESS_VISIBILITY_KEY = UploadOtaFileFragment.class.getCanonicalName()+".UPLOAD_PROGRESS_VISIBILITY_KEY";

    private static final String NODE_PARAM = UploadOtaFileFragment.class.getCanonicalName()+".NODE_PARAM";
    private static final String FILE_PARAM = UploadOtaFileFragment.class.getCanonicalName()+".FILE_PARAM";
    private static final String ADDRESS_PARAM = UploadOtaFileFragment.class.getCanonicalName()+".ADDRESS_PARAM";

    private static final int MIN_MEMORY_ADDRESS = 0x7000;
    private static final int MAX_MEMORY_ADDRESS = 0x089000;

    public static UploadOtaFileFragment build(@NonNull Node node, @Nullable Uri file,
                                              @Nullable Long address){
        return build(node,file,address,true);
    }

    public static UploadOtaFileFragment build(@NonNull Node node, @Nullable Uri file,
                                              @Nullable Long address, boolean showAddressField ){
        return build(node,file,address,showAddressField,null,false);
    }

    public static UploadOtaFileFragment build(@NonNull Node node, @Nullable Uri file,
                                              @Nullable Long address, boolean showAddressField,
                                              @FirmwareType @Nullable Integer fwType,
                                              boolean showFwType){
        Bundle args = new Bundle();
        args.putString(NODE_PARAM,node.getTag());
        args.putBoolean(SHOW_ADDRESS_KEY,showAddressField);
        if(file!=null)
            args.putParcelable(FILE_PARAM,file);

        if(address!=null)
            args.putLong(ADDRESS_PARAM, address);

        args.putBoolean(SHOW_FW_TYPE_KEY,showFwType);
        if(fwType!=null){
            args.putInt(FW_TYPE_KEY,fwType);
        }

        UploadOtaFileFragment f = new UploadOtaFileFragment();
        f.setArguments(args);
        return f;
    }

    public UploadOtaFileFragment() {
        // Required empty public constructor
    }

    private Node mNode;
    private RequestFileUtil mRequestFile;
    private View mRootView;
    private TextView mFileNameText;
    private ProgressBar mUploadProgress;
    private TextView mUploadMessage;
    private Uri mSelectedFw;
    private TextView mAddressText;
    private View mProgressViewGroup;
    private RadioGroup mFirmwareTypeView;

    private FwVersionViewModel mVersionViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView =  inflater.inflate(R.layout.fragment_upload_ota_file, container, false);

        mFileNameText = mRootView.findViewById(R.id.otaUpload_selectFileName);
        mProgressViewGroup = mRootView.findViewById(R.id.otaUpload_uploadProgressGroup);
        mUploadProgress = mRootView.findViewById(R.id.otaUpload_uploadProgress);
        mUploadMessage = mRootView.findViewById(R.id.otaUpload_uploadMessage);
        mAddressText = mRootView.findViewById(R.id.otaUpload_addressText);

        setupSelectFileButton(mRootView.findViewById(R.id.otaUpload_selectFileButton));
        setupStartUploadButton(mRootView.findViewById(R.id.otaUpload_startUploadButton));
        setupAddressText(mAddressText,mRootView.findViewById(R.id.otaUpload_addressTextLayout),
                getFlashAddress(savedInstanceState,getArguments()));
        if(!showFleshAddress(getArguments())){
            mAddressText.setVisibility(View.GONE);
        }

        setupFwTypeSelector(mRootView.findViewById(R.id.otaUpload_fwTypeSelector),
                savedInstanceState,getArguments());

        mRequestFile = new RequestFileUtil(this,mRootView);
        onFileSelected(getFirmwareLocation(savedInstanceState,getArguments()));
        return  mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVersionViewModel = ViewModelProviders.of(requireActivity()).get(FwVersionViewModel.class);
    }

    private void setupFwTypeSelector(RadioGroup selector, Bundle savedInstance, Bundle args) {
        mFirmwareTypeView = selector;
        if(showFwTypeSelector(args)){
            mFirmwareTypeView.setVisibility(View.VISIBLE);
        }else{
            mFirmwareTypeView.setVisibility(View.GONE);
        }

        @IdRes int selected = getSelectedFwType(savedInstance,args) == FirmwareType.BLE_FW ? R.id.otaUpload_bleType : R.id.otaUpload_applicationType;
        mFirmwareTypeView.check(selected);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mAddressText.getText().length()!=0) {
            outState.putString(ADDRESS_KEY,mAddressText.getText().toString());
        }
        if(mFirmwareTypeView.getVisibility() == View.VISIBLE){
            outState.putInt(FW_TYPE_KEY,getSelectedFwType());
        }
        outState.putInt(UPLOAD_PROGRESS_VISIBILITY_KEY,mProgressViewGroup.getVisibility());
        outState.putParcelable(FW_URI_KEY,mSelectedFw);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.containsKey(UPLOAD_PROGRESS_VISIBILITY_KEY)){
            mProgressViewGroup.setVisibility(savedInstanceState.getInt(UPLOAD_PROGRESS_VISIBILITY_KEY));
        }
    }

    private long getFlashAddress(@Nullable Bundle savedInstanceState, @Nullable Bundle arguments){
        if(savedInstanceState!=null && savedInstanceState.containsKey(ADDRESS_KEY)){
            try {
                return Long.decode(savedInstanceState.getString(ADDRESS_KEY));
            }catch (NumberFormatException e){
                return MIN_MEMORY_ADDRESS;
            }
        }
        if(arguments!=null && arguments.containsKey(ADDRESS_PARAM)){
            return arguments.getLong(ADDRESS_PARAM);
        }
        return MIN_MEMORY_ADDRESS;
    }

    private boolean showFleshAddress(@Nullable Bundle arguments){
        if(arguments!=null){
            return arguments.getBoolean(SHOW_ADDRESS_KEY,false);
        }else{
            return false;
        }
    }

    private boolean showFwTypeSelector(@Nullable Bundle arguments){
        if(arguments!=null){
            return arguments.getBoolean(SHOW_FW_TYPE_KEY,false);
        }else{
            return false;
        }
    }

    private @FirmwareType int getSelectedFwType(@Nullable Bundle savedInstanceState, @Nullable Bundle arguments){
        if(savedInstanceState!=null && savedInstanceState.containsKey(FW_TYPE_KEY)){
                return savedInstanceState.getInt(FW_TYPE_KEY);
        }
        if(arguments!=null && arguments.containsKey(FW_TYPE_KEY)){
            return arguments.getInt(FW_TYPE_KEY);
        }
        return FirmwareType.BOARD_FW;
    }

    private @Nullable Uri getFirmwareLocation(@Nullable Bundle savedInstanceState, @Nullable Bundle arguments){
        if(savedInstanceState!=null && savedInstanceState.containsKey(FW_URI_KEY))
            return savedInstanceState.getParcelable(FW_URI_KEY);
        if(arguments!=null && arguments.containsKey(FILE_PARAM))
            return arguments.getParcelable(FILE_PARAM);
        return null;
    }

    private void setupAddressText(TextView addressText, TextInputLayout addressLayout, long initialValue) {
        addressText.addTextChangedListener(new CheckMultipleOf(addressLayout,R.string.otaUpload_invalidBlockAddress,
                0x1000));
        addressText.addTextChangedListener(new CheckNumberRange(addressLayout,R.string.otaUpload_invalidMemoryAddress,
                MIN_MEMORY_ADDRESS, MAX_MEMORY_ADDRESS));
        addressText.addTextChangedListener(new CheckHexNumber(addressLayout,R.string.otaUpload_invalidHex));

        addressText.setText("0x"+Long.toHexString(initialValue));

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        mNode = Manager.getSharedInstance().getNodeWithTag(args.getString(NODE_PARAM));
    }

    private BroadcastReceiver mMessageReceiver;

    @Override
    public void onResume() {
        super.onResume();
        mMessageReceiver = new UploadOtaFileActionReceiver(mUploadProgress,mUploadMessage,this);
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                FwUpgradeService.getServiceActionFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }

    private Long getFwAddress(){
        try{
            long address = Long.decode(mAddressText.getText().toString());
            //clamp
            return Math.max(MIN_MEMORY_ADDRESS,Math.min(address,MAX_MEMORY_ADDRESS));
        }catch (NumberFormatException e){
            return null;
        }
    }

    private void setupStartUploadButton(View button) {
        button.setOnClickListener(v -> {
            Long address = getFwAddress();
            @FirmwareType int selectedType = getSelectedFwType();
            FwVersion currentVersion = mVersionViewModel.getFwVersion().getValue();
            if(mSelectedFw!=null) {
                if(address!=null) {
                    startUploadFile(mSelectedFw, selectedType,address,currentVersion);
                }else{
                    Snackbar.make(mRootView,R.string.otaUpload_invalidMemoryAddress,Snackbar.LENGTH_SHORT).show();
                }
            }else{
                Snackbar.make(mRootView,R.string.otaUpload_invalidFile,Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private @FirmwareType int getSelectedFwType() {
        if( mFirmwareTypeView.getCheckedRadioButtonId() == R.id.otaUpload_bleType)
            return FirmwareType.BLE_FW;
        else
            return FirmwareType.BOARD_FW;
    }

    private void startUploadFile(@NonNull Uri selectedFile, @FirmwareType int type,
                                 long address,@Nullable FwVersion currentVersion) {
        FwUpgradeService.startUploadService(requireContext(),mNode,selectedFile,type,address,currentVersion);
        mProgressViewGroup.setVisibility(View.VISIBLE);
    }

    private void setupSelectFileButton(View button) {
        button.setOnClickListener(v -> mRequestFile.openFileSelector());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onFileSelected(mRequestFile.onActivityResult(requestCode,resultCode,data));
    }

    private void onFileSelected(@Nullable Uri fwFile){
        if(fwFile==null)
            return;
        mSelectedFw = fwFile;
        mFileNameText.setText(RequestFileUtil.getFileName(requireContext(),fwFile));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mRequestFile.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }//onRequestPermissionsResult

    @Override
    public void onUploadFinished(float time_s) {
        NodeConnectionService.disconnect(requireContext(),mNode);
        SimpleFragmentDialog dialog = SimpleFragmentDialog.newInstance(R.string.otaUpload_completed,
                getString(R.string.otaUpload_finished,time_s));
        dialog.setOnclickListener((dialog1, which) -> {
            //UploadOtaFileFragment.this
            NavUtils.navigateUpFromSameTask(requireActivity());
        });
        dialog.show(requireFragmentManager(),FINISH_DIALOG_TAG);
    }

}
