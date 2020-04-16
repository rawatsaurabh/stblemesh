package com.st.BlueSTSDK.Utils.advertise;

import androidx.annotation.Nullable;

public interface AdvertiseFilter {
    @Nullable
    BleAdvertiseInfo filter(byte[] advData);
}
