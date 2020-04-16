/**
 ******************************************************************************
 * @file    AppSharedPrefs.java
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

import android.content.Context;
import android.content.SharedPreferences;

import com.st.bluenrgmesh.R;

import java.util.Map;
import java.util.Set;

public class AppSharedPrefs
{
    private static AppSharedPrefs sharedPrefs = null;
    private SharedPreferences sf;

    public AppSharedPrefs(Context context)
    {
        sf = context.getSharedPreferences(context.getString(R.string.key_shared_prefs), Context.MODE_PRIVATE);
    }

    public static AppSharedPrefs getInstance(Context context)
    {
        if(sharedPrefs == null)
        {
            sharedPrefs = new AppSharedPrefs(context);
        }

        return sharedPrefs;
    }

    public Object get(String key)
    {
        Map<String, ?> map = sf.getAll();
        return map.get((String)key);
    }

    public void put(String key, Object value)
    {
        SharedPreferences.Editor edit = sf.edit();

        if(value == null)
        {

            edit.putString(key, null);
        }
        else if(value instanceof Boolean)
        {
            edit.putBoolean(key, (boolean)value);
        }
        else if(value instanceof Float)
        {
            edit.putFloat(key, (float)value);
        }
        else if(value instanceof Integer)
        {
            edit.putInt(key, (int)value);
        }
        else if(value instanceof Long)
        {
            edit.putLong(key, (long) value);
        }
        else if(value instanceof String)
        {
            edit.putString(key, (String)value);
        }
        else if(value instanceof Set)
        {
            edit.putStringSet(key, (Set)value);
        }

        edit.commit();
    }

    public void clearAll()
    {
        sf.edit().clear().commit();
    }

    public void clear(String key)
    {
        sf.edit().remove(key).commit();
    }
}
