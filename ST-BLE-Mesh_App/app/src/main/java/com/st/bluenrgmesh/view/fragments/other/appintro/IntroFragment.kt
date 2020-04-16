/**
 ******************************************************************************
 * @file    IntroFragment.kt
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


package com.st.bluenrgmesh.view.fragments.other.appintro

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.st.bluenrgmesh.MainActivity
import com.st.bluenrgmesh.R
import com.st.bluenrgmesh.Utils
import com.st.bluenrgmesh.view.fragments.base.BaseFragment
import java.util.ArrayList

public class IntroFragment : BaseFragment()
{
    private var fade_out: Animation? = null
    private var fade_in: Animation? = null
    private var heading: TextView? = null
    private var txtMessage: TextView? = null
    private var tvSkip: TextView? = null
    private var position: Int = 0
    private var btnNext: Button? = null
    private var btnGetStarted: Button? = null
    private var tabIndicator: TabLayout? = null
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private var screenPager: ViewPager? = null
    private var btnAnim: Animation? = null
    private var v: View? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        initUi()
    }*/
    public override fun onAttach(context: Context?) {
        //this.context = activity
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.activity_intro, container, false)
        Utils.updateActionBarForFeatures(activity, IntroFragment().javaClass.name)
        initUi()
        return v
    }

    private fun initUi() {
      //  btnAnim = AnimationUtils.loadAnimation(activity, R.anim.button_animation)
      //  fade_out = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
      //  fade_in = AnimationUtils.loadAnimation(activity, R.anim.fade_in)

        // fill list screen

        val mList = ArrayList<ScreenItem>()
        mList.add(ScreenItem("", "", R.drawable.device_tab))
        mList.add(ScreenItem("", "", R.drawable.provisioning))
        mList.add(ScreenItem("", "", R.drawable.provisioningdoneimg))
 /*       mList.add(ScreenItem("", "", R.drawable.device_capabilities))
        mList.add(ScreenItem("", "", R.drawable.device_features_and_models))
        mList.add(ScreenItem("", "", R.drawable.quick_configuration))
        mList.add(ScreenItem("", "", R.drawable.selectappkeyimg))
        mList.add(ScreenItem("", "", R.drawable.bindingmodelsimg))
        mList.add(ScreenItem("", "", R.drawable.defconfigurationimg))
        mList.add(ScreenItem("", "", R.drawable.defaultpubsubimg))
        mList.add(ScreenItem("", "", R.drawable.provisioningdoneimg))*/
        mList.add(ScreenItem("","",  R.drawable.group_page))
        //mList.add(ScreenItem("","",  R.drawable.group_settings))
        //mList.add(ScreenItem("","",  R.drawable.model_tab))
        mList.add(ScreenItem("","",  R.drawable.models1))
       // mList.add(ScreenItem("","",  R.drawable.model_vendor))
        mList.add(ScreenItem("","",  R.drawable.model_hsl))
      /*  mList.add(ScreenItem("","",  R.drawable.model_lighting))
        mList.add(ScreenItem("","",  R.drawable.model_ctl_temperature))
        mList.add(ScreenItem("","",  R.drawable.model_ctl))
        mList.add(ScreenItem("","",  R.drawable.model_generic_level))*/
        //mList.add(ScreenItem("","",  R.drawable.model_sensor))
      //  mList.add(ScreenItem("","",  R.drawable.app_setting_option))
       // mList.add(ScreenItem("","",  R.drawable.app_settings_snapshot))
        mList.add(ScreenItem("","",  R.drawable.voice_assistance_feature1))



        // setup viewpager
        screenPager =  v?.findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(activity, mList)
        screenPager?.setAdapter(introViewPagerAdapter)

        btnNext =  v?.findViewById(R.id.btn_next)
        btnGetStarted =  v?.findViewById(R.id.btn_get_started)
        tabIndicator =  v?.findViewById<TabLayout>(R.id.tab_indicator)
        tabIndicator?.setupWithViewPager(screenPager)

        btnNext?.setOnClickListener(View.OnClickListener {
            position = screenPager?.getCurrentItem()!!
            if (position < mList.size) {

                position++
                screenPager?.setCurrentItem(position)
            }

            if (position == mList.size - 1) { // when we rech to the last screen
                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen()
            }

        })

        tabIndicator?.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabSelected(tab: TabLayout.Tab) {

                if (tab.position === mList.size - 1) {
                    loaddLastScreen()
                }

                //heading?.animation = fade_out;
                Handler().postDelayed(Runnable { updateStoryPoints(tab.position) }, 300)

            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        btnGetStarted?.setOnClickListener(View.OnClickListener {

            (activity as MainActivity).enablePermissions()
            Utils.setShowIntroScreen(context, false);
        })

        heading =  v?.findViewById<TextView>(R.id.heading)
        heading?.animation = btnAnim;
        txtMessage =  v?.findViewById<TextView>(R.id.txtMessage)
        txtMessage?.animation = btnAnim;
        heading?.setText(R.string.txthead1)
        txtMessage?.setText(R.string.txtmsg1)
        tvSkip =  v?.findViewById<TextView>(R.id.tv_skip)
        tvSkip?.setOnClickListener(View.OnClickListener {
            //screenPager?.setCurrentItem(mList.size)
            Utils.setShowIntroScreen(context, false)
            (activity as MainActivity).enablePermissions()
        })
    }

    private fun updateStoryPoints(position: Int) {

        heading?.setAnimation(fade_in)
        if(position == 0)
        {
            heading?.setText(getString(R.string.txthead1))
            txtMessage?.setText(getString(R.string.txtmsg1))
        }
        else if(position == 1)
        {
            heading?.setText("Provisioning Started")
            txtMessage?.setText("Shows Provisoning Started.")
        }
        else if(position == 2)
        {
            heading?.setText(getString(R.string.node_head))
            txtMessage?.setText(getString(R.string.node_msg))
        }
        /*else if(position == 2)
        {
            heading?.setText(getString(R.string.txthead2))
            txtMessage?.setText(getString(R.string.txtmsg2))
        }*/
        /*else if(position == 3)
        {
            heading?.setText(getString(R.string.txthead3))
            txtMessage?.setText(getString(R.string.txtmsg3))
        }
        else if(position == 4)
        {
            heading?.setText(getString(R.string.txthead4))
            txtMessage?.setText(getString(R.string.txtmsg4))
        }
        else if(position == 5)
        {
            heading?.setText(getString(R.string.txthead5))
            txtMessage?.setText(getString(R.string.txtmsg5))
        }
        else if(position == 6)
        {
            heading?.setText(getString(R.string.txthead6))
            txtMessage?.setText(getString(R.string.txtmsg6))
        }
        else if(position == 7)
        {
            heading?.setText(getString(R.string.txthead7))
            txtMessage?.setText(getString(R.string.txtmsg7))
        }
        else if(position == 8)
        {
            heading?.setText(getString(R.string.txthead8))
            txtMessage?.setText(getString(R.string.txtmsg8))
        }
        else if(position == 9)
        {
            heading?.setText(getString(R.string.txthead9))
            txtMessage?.setText(getString(R.string.txtmsg9))
        }*/
        /*else if(position == 10)
        {
            heading?.setText(getString(R.string.txthead9))
            txtMessage?.setText(getString(R.string.txtmsg9))
        }*/
        else if(position == 3)
        {
            heading?.setText(getString(R.string.txthead10))
            txtMessage?.setText(getString(R.string.txtmsg10))
        }
        /*else if(position == 11)
        {
            heading?.setText(getString(R.string.txthead11))
            txtMessage?.setText(getString(R.string.txtmsg11))
        }*/
        /*else if(position == 3)
        {
            heading?.setText(getString(R.string.txthead12))
            txtMessage?.setText(getString(R.string.txtmsg12))
        }*/
        else if(position == 4)
        {
            heading?.setText(getString(R.string.txthead13))
            txtMessage?.setText(getString(R.string.txtmsg13))
        }
        /*else if(position == 14)
        {
            heading?.setText(getString(R.string.txthead14))
            txtMessage?.setText(getString(R.string.txtmsg14))
        }*/
        else if(position == 5)
        {
            heading?.setText(getString(R.string.txthead15))
            txtMessage?.setText(getString(R.string.txtmsg15))
        }
        /*else if(position == 16)
        {
            heading?.setText(getString(R.string.txthead16))
            txtMessage?.setText(getString(R.string.txtmsg16))
        }
        else if(position == 17)
        {
            heading?.setText(getString(R.string.txthead17))
            txtMessage?.setText(getString(R.string.txtmsg17))
        }
        else if(position == 18)
        {
            heading?.setText(getString(R.string.txthead18))
            txtMessage?.setText(getString(R.string.txtmsg18))
        }
        else if(position == 19)
        {
            heading?.setText(getString(R.string.txthead19))
            txtMessage?.setText(getString(R.string.txtmsg19))
        }*/
        /*else if(position == 20)
        {
            heading?.setText(getString(R.string.txthead20))
            txtMessage?.setText(getString(R.string.txtmsg20))
        }*/
        /*else if(position == 20)
        {
            heading?.setText(getString(R.string.txthead21))
            txtMessage?.setText(getString(R.string.txtmsg21))
        }
        else if(position == 21)
        {
            heading?.setText(getString(R.string.txthead25))
            txtMessage?.setText(getString(R.string.txtmsg23))
        }
        else if(position == 22)
        {
            heading?.setText(getString(R.string.txthead23))
            txtMessage?.setText(getString(R.string.txtmsg25))
        }*/
        else if(position == 6)
        {
            heading?.setText(getString(R.string.txthead22))
            txtMessage?.setText(getString(R.string.txtmsg22))
        }

    }

    private fun loaddLastScreen() {

        btnNext?.setVisibility(View.INVISIBLE)
        btnGetStarted?.setVisibility(View.VISIBLE)
        tvSkip?.setVisibility(View.INVISIBLE)
        tabIndicator?.setVisibility(View.INVISIBLE)
        // TODO : ADD an animation the getstarted button
        // setup animation
       // btnGetStarted?.setAnimation(btnAnim)

    }
}
