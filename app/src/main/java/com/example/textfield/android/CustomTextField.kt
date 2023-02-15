package com.example.textfield.android

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.InputType
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.example.textfield.R
import kotlin.properties.Delegates


class CustomTextField(context: Context) : androidx.appcompat.widget.AppCompatEditText(context) {

    companion object {
        private val STYLE_ATTR = com.example.textfield.R.styleable.CustomTextField
    }

    private lateinit var gradientDrawable: GradientDrawable

    private var shape: LayerDrawable? = (ContextCompat.getDrawable(
        this.context, com.example.textfield.R.drawable.bg_custom_text_border
    ) as LayerDrawable?)

    private val DEFAULT_STROKE_WIDTH = 0
    private val DEFAULT_TEXT_COLOR = R.color.black

    private var strokeColor: Int? = null
    private var strokeWidth: Int? = null
    private var paddingBottom: Int? = null
    private var paddingRim: Int? = null
    private var paddingLeft: Int? = null
    private var paddingRight: Int? = null

    init {
        gradientDrawable =
            shape!!.findDrawableByLayerId(com.example.textfield.R.id.shape) as GradientDrawable

        // if you want to change the color of background of textview dynamically
        //gradientDrawable.setColor(ContextCompat.getColor(demo.this,R.color.colorAccent));

        // This is mangage the storke width and it's color by this shape.setStroke(strokeWidth,color);

        // if you want to change the color of background of textview dynamically
        //gradientDrawable.setColor(ContextCompat.getColor(demo.this,R.color.colorAccent));

        // This is mangage the storke width and it's color by this shape.setStroke(strokeWidth,color);
        gradientDrawable.setStroke(
            2, ContextCompat.getColor(this.context, com.example.textfield.R.color.purple_200)
        )

        this.background = shape
    }


    var align: Int?
        set(value) {
            this.gravity = when (align) {
                0 -> Gravity.START
                1 -> Gravity.END
                2 -> Gravity.CENTER
                else -> Gravity.CENTER
            }
        }
        get() = this.align

    var bgColor: Triple<Int, Int, Int>?
        set(value) {
            val color = getColor(value)
            gradientDrawable.setColor(color);
        }
        get() {
            val colorCode = gradientDrawable.color?.defaultColor!!
            return Triple(Color.red(colorCode), Color.green(colorCode), Color.blue(colorCode))
        }

    var bgColorClear: Boolean?
        set(value) {
            gradientDrawable.alpha = if (value != false) 0 else 255
        }
        get() = gradientDrawable.alpha == 0


    var borderColor: Triple<Int, Int, Int>?
        set(value) {
            val color = getColor(value)
            val width = strokeWidth ?: DEFAULT_STROKE_WIDTH
            gradientDrawable.setStroke(width, color)
            strokeColor = color
        }
        get() {

            val colorCode = strokeColor ?: Color.TRANSPARENT
            return Triple(Color.red(colorCode), Color.green(colorCode), Color.blue(colorCode))
        }

    var borderColorClear: Boolean?
        set(value) {
            if (value != false) {
                gradientDrawable.setStroke(DEFAULT_STROKE_WIDTH, Color.TRANSPARENT)
                strokeColor = Color.TRANSPARENT
            }

        }
        get() = strokeColor == Color.TRANSPARENT


    var borderWidth: Int?
        set(value) {
            val color = strokeColor ?: Color.TRANSPARENT
            gradientDrawable.setStroke(value ?: DEFAULT_STROKE_WIDTH, color)
            strokeWidth = value
        }
        get() {
            return strokeWidth ?: DEFAULT_STROKE_WIDTH
        }

    var bottomPadding: Int?
        set(value) {
            if (paddingRim == null) {
                val tempLeft = paddingLeft ?: 0
                val tempRight = paddingRight ?: 0
                this.setPadding(tempLeft, 0, tempRight, value ?: 0)
            } else {
                val tempLeft = paddingLeft ?: paddingRim
                val tempRight = paddingRight ?: paddingRim
                this.setPadding(tempLeft ?: 0, 0, tempRight ?: 0, value ?: 0)
            }

        }
        get() {
            return if (paddingRim == null) paddingBottom ?: 0 else paddingBottom ?: paddingRim
        }

    var leftPadding: Int?
        set(value) {
            if (paddingRim == null) {
                val tempBottom = paddingBottom ?: 0
                val tempRight = paddingRight ?: 0
                this.setPadding(value ?: 0, 0, tempRight, tempBottom)
            } else {
                val tempBottom = paddingBottom ?: paddingRim
                val tempRight = paddingRight ?: paddingRim
                this.setPadding(value ?: 0, 0, tempRight ?: 0, tempBottom ?: 0)
            }

        }
        get() {
            return if (paddingRim == null) paddingLeft ?: 0 else paddingLeft ?: paddingRim
        }

    var rightPadding: Int?
        set(value) {
            if (paddingRim == null) {
                val tempBottom = paddingBottom ?: 0
                val tempLeft = paddingLeft ?: 0
                this.setPadding(tempLeft, 0, value ?: 0, tempBottom)
            } else {
                val tempBottom = paddingBottom ?: paddingRim
                val tempLeft = paddingLeft ?: paddingRim
                this.setPadding(tempLeft ?: 0, 0, value ?: 0, tempBottom ?: 0)
            }

        }
        get() {
            return if (paddingRim == null) paddingRight ?: 0 else paddingRight ?: paddingRim
        }

    var rimPadding: Int?
        set(value) {
            if (paddingLeft == null) paddingLeft = value
            if (paddingRight == null) paddingRight = value
            if (paddingBottom == null) paddingBottom = value
            paddingRim = value ?: 0
            this.setPadding(paddingLeft ?: 0, 0, paddingRight ?: 0, paddingBottom ?: 0)

        }
        get() {
            return paddingRim
        }

    var color: Triple<Int, Int, Int>?
        set(value) {
            val color = getColor(value)
            this.setTextColor(color)
        }
        get() {
            val colorCode = this.textColors.defaultColor
            return Triple(Color.red(colorCode), Color.green(colorCode), Color.blue(colorCode))
        }

    var colorClear: Boolean?
        set(value) {
            val color = this.context.getColor(DEFAULT_TEXT_COLOR)
            if (value == true) this.setTextColor(color)
        }
        get() {
            val color = this.context.getColor(DEFAULT_TEXT_COLOR)
            val colorCode = this.textColors.defaultColor
            return colorCode == color
        }

    var input: Boolean?
        set(value) {
            if (input == true) this.inputType =
                TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_NORMAL else InputType.TYPE_NULL
        }
        get() {
            return this.inputType != InputType.TYPE_NULL
        }

    var keyboardType: Int?
        set(value) {
            when (value) {
                0 -> this.inputType = TYPE_CLASS_NUMBER or TYPE_NUMBER_VARIATION_NORMAL
                1 -> this.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
        }
        get() {
            return this.inputType
        }

    var contentLocal: CharSequence?
        set(value) {
            if (input == false) this.text = SpannableStringBuilder(value)
        }
        get() {
            return this.text
        }

    var defaultText: CharSequence?
        set(value) {
            if (input == true) this.hint = value
        }
        get() {
            return this.hint
        }


    var defaultTextColor: Triple<Int, Int, Int>?
        set(value) {
            val color = getColor(value)
            this.setHintTextColor(color)
        }
        get() {
            val colorCode = this.hintTextColors.defaultColor
            return Triple(Color.red(colorCode), Color.green(colorCode), Color.blue(colorCode))
        }

    var defaultTextColorClear: Boolean?
        set(value) {
            val color = this.context.getColor(DEFAULT_TEXT_COLOR)
            if (value == true) this.setHintTextColor(color)
        }
        get() {
            val color = this.context.getColor(DEFAULT_TEXT_COLOR)
            val colorCode = this.hintTextColors.defaultColor
            return colorCode == color
        }


    var radius: Int?
        set(value) {
           gradientDrawable.cornerRadius = radius?.toFloat()?:0F
        }
        get() {
            return  gradientDrawable.cornerRadius.toInt()
        }

    var fontLocal: Int?
        set(value) {
            when(value){
                0 -> this.typeface = this.context.resources.getFont(R.font.)
            }
        }
        get() {
            return  gradientDrawable.cornerRadius.toInt()
        }




    private fun getColor(value: Triple<Int, Int, Int>?): Int {
        return Color.rgb(value?.first ?: 0, value?.second ?: 0, value?.third ?: 0)
    }
}