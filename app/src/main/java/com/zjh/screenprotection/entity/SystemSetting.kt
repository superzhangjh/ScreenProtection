package com.zjh.screenprotection.entity

import android.widget.ImageView.ScaleType
import com.youth.banner.Banner

data class SystemSetting(
    //设置的id，具有唯一性
    val id: Int,
    //是否多选
    val isMulti: Boolean,
    //选项
    val options: List<Option>,
) {
    companion object {
        const val ID_LOOP_TIME = 0x01
        const val ID_SCROLL_TIME = 0x02
        const val ID_ORIENTATION = 0x03
        const val ID_TRANSFORMERS = 0x04
        const val ID_SCALE_TYPE = 0x05

        fun fromBannerSettings(settings: BannerSettings?): List<SystemSetting>? {
            if (settings == null) return null
            return listOf(
                SystemSetting(ID_ORIENTATION, false, listOf(
                    Option(settings.orientation, "垂直", Banner.VERTICAL),
                    Option(settings.orientation, "水平", Banner.HORIZONTAL)
                )),
                SystemSetting(ID_LOOP_TIME, false, rangeSecondOptions(2000L, 20000L, 1000L, settings.loopTime)),
                SystemSetting(ID_SCROLL_TIME, false, rangeSecondOptions(100L, 2000L, 100L, settings.scrollTime.toLong())),
                SystemSetting(ID_SCALE_TYPE, false, listOf(
                    Option(settings.scaleType, "居中裁剪", ScaleType.CENTER_CROP),
                    Option(settings.scaleType, "拉伸铺满", ScaleType.FIT_XY),
                    Option(settings.scaleType, "居中拉伸", ScaleType.FIT_CENTER),
                    Option(settings.scaleType, "居中内部", ScaleType.CENTER_INSIDE),
                    Option(settings.scaleType, "矩阵拉升", ScaleType.MATRIX),
                    Option(settings.scaleType, "按头部拉升", ScaleType.FIT_START),
                    Option(settings.scaleType, "按尾部拉升", ScaleType.FIT_END),
                )),
                SystemSetting(ID_TRANSFORMERS, false, BannerSettings.Transformer.entries.map {
                    Option(settings.transformer, it.des, it)
                }),
            )
        }

        /**
         * 转化为选项值
         */
        private fun rangeSecondOptions(min: Long, max: Long, step: Long, value: Long): List<Option> {
            return (min..max step step).mapTo(mutableListOf()) {
                Option(value, "$it", it)
            }
        }

        fun toBannerSetting(data: List<SystemSetting>?): BannerSettings {
            val def = BannerSettings()
            if (data == null) return def
            var orientation = def.orientation
            var loopTime = def.loopTime
            var scrollTime = def.scrollTime
            var scareType = def.scaleType
            var transformer = def.transformer
            data.forEach { setting ->
                val selectedOptions = setting.options.filter { it.isSelected }
                when (setting.id) {
                    ID_ORIENTATION -> orientation = selectedOptions[0].value as Int
                    ID_LOOP_TIME -> loopTime = selectedOptions[0].value as Long
                    ID_SCROLL_TIME -> scrollTime = (selectedOptions[0].value as Long).toInt()
                    ID_SCALE_TYPE ->  scareType = selectedOptions[0].value as ScaleType
                    ID_TRANSFORMERS -> transformer = selectedOptions[0].value as BannerSettings.Transformer
                }
            }
            return BannerSettings(
                loopTime, scrollTime, orientation, transformer, scareType
            )
        }
    }

    fun getName() = when (id) {
        ID_ORIENTATION -> "滚动方向"
        ID_LOOP_TIME -> "轮播间隔"
        ID_SCROLL_TIME -> "滚动时长"
        ID_SCALE_TYPE -> "图片缩放"
        ID_TRANSFORMERS -> "动画效果"
        else -> "未知"
    }
}

class Option(
    val name: String,
    val value: Any,
    var isSelected: Boolean
) {
    constructor(
        selectedValue: Any,
        name: String,
        value: Any,
    ) : this(name, value, selectedValue == value)
}