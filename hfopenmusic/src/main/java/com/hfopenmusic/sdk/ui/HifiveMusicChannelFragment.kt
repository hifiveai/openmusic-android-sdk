package com.hfopenmusic.sdk.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.hfopen.sdk.entity.ChannelItem
import com.hfopen.sdk.hInterface.DataResponse
import com.hfopen.sdk.manager.HFOpenApi
import com.hfopen.sdk.rx.BaseException
import com.hfopenmusic.sdk.R
import com.hfopenmusic.sdk.adapter.HifiveViewPagerAdapter
import com.hfopenmusic.sdk.HFOpenMusic
import com.hfopenmusic.sdk.util.HifiveDisplayUtils
import com.hfopenmusic.sdk.view.magicindicator.*
import com.hfopenmusic.sdk.view.magicindicator.CommonPagerTitleView.OnPagerTitleChangeListener
import com.hfopenmusic.sdk.view.magicindicator.abs.IPagerIndicator
import com.hfopenmusic.sdk.view.magicindicator.abs.IPagerTitleView
import java.util.*

/**
 * 电台列表
 *
 * @author lsh 2021-3-15 13:41:20
 */
class HifiveMusicChannelFragment : DialogFragment() {
    private var magicIndicator: MagicIndicator? = null
    private var viewPager: ViewPager? = null
    private var channelLists: List<ChannelItem?>? = ArrayList()
    private var mContext: Context? = null
    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val window = dialog!!.window
            if (window != null && mContext != null) {
                val params = window.attributes
                params.gravity = Gravity.BOTTOM
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = HifiveDisplayUtils.getPlayerHeight(mContext)
                window.attributes = params
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.HifiveSdkDialogStyly)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context
        if (dialog != null) {
            val window = dialog!!.window
            if (window != null) {
                window.setWindowAnimations(R.style.AnimationRightFade)
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
            }
        }
        val view = inflater.inflate(R.layout.hifive_dialog_music_sheet, container)
        initView(view)
        getChannelData()
        HFOpenMusic.getInstance().addDialog(this)
        return view
    }

    //初始化view
    private fun initView(view: View) {
        view.findViewById<View>(R.id.iv_back).setOnClickListener {
            dismiss()
            HFOpenMusic.getInstance().removeDialog(1)
        }
        magicIndicator = view.findViewById(R.id.magic_indicator)
        viewPager = view.findViewById(R.id.viewpager)
    }

    //初始化指示器
    private fun initMagicIndicator() {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.isAdjustMode = false
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return if (channelLists == null) 0 else channelLists!!.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val cptv = CommonPagerTitleView(context)
                cptv.setContentView(R.layout.hifive_layout_indecator)
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                cptv.layoutParams = lp
                // 初始化
                val titleText = cptv.findViewById<TextView>(R.id.tv_indicator)
                val vvDown = cptv.findViewById<View>(R.id.vv_line)
                if (channelLists!![index] != null && !TextUtils.isEmpty(channelLists!![index]!!.groupName)) titleText.text = channelLists!![index]!!.groupName
                cptv.onPagerTitleChangeListener = object : OnPagerTitleChangeListener {
                    override fun onSelected(index: Int, totalCount: Int) {
                        titleText.setTextColor(Color.parseColor("#FFFFFF"))
                        titleText.typeface = Typeface.defaultFromStyle(Typeface.BOLD) //加粗
                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                        vvDown.visibility = View.VISIBLE
                    }

                    override fun onDeselected(index: Int, totalCount: Int) {
                        titleText.setTextColor(Color.parseColor("#888888"))
                        titleText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                        vvDown.visibility = View.INVISIBLE
                    }

                    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
                        cptv.scaleX = 1.0f - 0.1f * leavePercent
                        cptv.scaleY = 1.0f - 0.1f * leavePercent
                    }

                    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
                        cptv.scaleX = 0.9f + 0.1f * enterPercent
                        cptv.scaleY = 0.9f + 0.1f * enterPercent
                    }
                }
                cptv.setOnClickListener { viewPager!!.currentItem = index }
                return cptv
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        magicIndicator!!.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewPager)
    }

    //初始化viewpager页卡
    private fun initPage() {
        val fragments: MutableList<Fragment> = ArrayList()
        for (model in channelLists!!) {
            if (model != null) {
                val radioStationFragment = HifiveMusicChannelSheetFragment()
                val recommendBundle = Bundle()
                recommendBundle.putString(HifiveMusicChannelSheetFragment.TYPE_ID, model.groupId)
                radioStationFragment.arguments = recommendBundle
                fragments.add(radioStationFragment)
            }
        }
        val adapter = HifiveViewPagerAdapter(childFragmentManager, fragments)
        viewPager!!.adapter = adapter
        val firstPageIndex = 0
        viewPager!!.currentItem = firstPageIndex
    }

    //获取电台列表
    private fun getChannelData() {
        try {
            if (mContext == null) return
            HFOpenApi.getInstance().channel(object : DataResponse<ArrayList<ChannelItem>> {
                override fun onError(exception: BaseException) {
                    HFOpenMusic.getInstance().showToast(activity, exception.msg)
                }

                override fun onSuccess(data: ArrayList<ChannelItem>, taskId: String) {
                    channelLists = data
                    initMagicIndicator()
                    initPage()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}