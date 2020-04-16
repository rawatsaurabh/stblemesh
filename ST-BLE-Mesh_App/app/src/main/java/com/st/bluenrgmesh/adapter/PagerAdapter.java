/**
 * *****************************************************************************
 *
 * @file PagerAdapter.java
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

package com.st.bluenrgmesh.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ModelTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.UnprovisionedFragment;

import java.util.HashMap;
import java.util.Map;



public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return UnprovisionedFragment.newInstance();
            case 1:
                //ProvisionedTabFragment tab2 = new ProvisionedTabFragment();
                return ProvisionedTabFragment.newInstance();
            case 2:
                //GroupTabFragment tab3 = new GroupTabFragment();
                return GroupTabFragment.newInstance();
            case 3:
                //ModelTabFragment tab4 = new ModelTabFragment();
                return ModelTabFragment.newInstance();
            default:
                return null;
        }

    }

    @Override
    public int getItemPosition(Object object) {

        if (object instanceof UnprovisionedFragment) {
            //((Page0Fragment) object).updateDate(mDate);
        } else if (object instanceof ProvisionedTabFragment) {
            ((ProvisionedTabFragment) object).updateProvisioneRecycler(null, 0, null);
        } else if (object instanceof GroupTabFragment) {
            ((GroupTabFragment) object).updateGroupRecycler(null);
        }
        return super.getItemPosition(object);
    }

    // Remove a page for the given position. The adapter is responsible for removing the view from its container.
    @Override
    public void destroyItem(android.view.ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        Utils.DEBUG("destroyItem(" + position + ")");
    };

    @Override
    public int getCount() {
        return mNumOfTabs;
    }



}

