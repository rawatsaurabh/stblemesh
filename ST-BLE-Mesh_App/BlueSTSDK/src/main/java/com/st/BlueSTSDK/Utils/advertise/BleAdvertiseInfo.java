package com.st.BlueSTSDK.Utils.advertise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.st.BlueSTSDK.Node;

public interface BleAdvertiseInfo {
    @NonNull String getName();

    byte getTxPower();

    @Nullable String getAddress();

    int getFeatureMap();

    byte getDeviceId();

    short getProtocolVersion();

    Node.Type getBoardType();

    boolean isBoardSleeping();

    boolean isHasGeneralPurpose();
}
