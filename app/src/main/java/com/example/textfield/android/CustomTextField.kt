package com.example.textfield.android

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.text.InputType
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.textfield.R
import org.intellij.lang.annotations.JdkConstants.FlowLayoutAlignment


class CustomTextField(context: Context) : androidx.appcompat.widget.AppCompatEditText(context) {

    companion object {
        private val STYLE_ATTR = com.example.textfield.R.styleable.CustomTextField
    }

    private lateinit var gradientDrawable: GradientDrawable
    private lateinit var underlineDrawable: GradientDrawable

    private var shape: LayerDrawable? = (ContextCompat.getDrawable(
        this.context, com.example.textfield.R.drawable.bg_custom_text_border
    ) as LayerDrawable?)

    private val DEFAULT_STROKE_WIDTH = 0
    private val DEFAULT_TEXT_COLOR = R.color.black
    private val DEFAULT_TYPE_FACE = R.font.steagal_regular
    private val DEFAULT_FONT_SIZE = 16
    private val DEFAULT_UNDERLINE_THICKNESS = 0

    private var strokeColor: Int? = null
    private var strokeWidth: Int? = null
    private var paddingBottom: Int? = null
    private var paddingRim: Int? = null
    private var paddingLeft: Int? = null
    private var paddingRight: Int? = null
    private var currentTypeface: Int? = null
    private var underThickness: Int? = null
    private var underColor: Int? = null
    private var urlColor: Int? = null
    private var urlAlignLocal: Layout.Alignment? = null
    private var urlLinkLocal: String? = null
    private var urlTextLocal: String? = null
    private var urlTextFontLocal: Int? = null
    private var urlTextFontSizeLocal: Int? = null
    private var urlUnderlineThicknessLocal: Int? = null
    private var urlTextUnderlineColorLocal: Int? = null
    private var urlTextUnderlineColorClearLocal: Boolean? = null

    init {
        gradientDrawable =
            shape!!.findDrawableByLayerId(com.example.textfield.R.id.shape) as GradientDrawable

        underlineDrawable =
            shape!!.findDrawableByLayerId(com.example.textfield.R.id.underline) as GradientDrawable

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
            this.gravity = when (value) {
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
            if (value == true) this.inputType =
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
            gradientDrawable.cornerRadius = value?.toFloat() ?: 0F
        }
        get() {
            return gradientDrawable.cornerRadius.toInt()
        }

    var fontLocal: Int?
        set(value) {
            this.typeface = this.context.resources.getFont(value ?: DEFAULT_TYPE_FACE)
        }
        get() {
            return currentTypeface ?: DEFAULT_TYPE_FACE
        }

    var fontSize: Int?
        set(value) {
            this.textSize = (value ?: DEFAULT_FONT_SIZE).toFloat()
        }
        get() {
            return this.textSize.toInt()
        }


    var identifier: Int?
        set(value) {
            this.id = value ?: generateViewId()
        }
        get() {
            return this.id
        }

    var lineSpace: Int?
        set(value) {
            this.setLineSpacing(value?.toFloat() ?: 5F, 1F)
        }
        get() {
            return this.lineSpacingExtra.toInt()
        }

    var lines: Int?
        set(value) {
            if (value != null) {
                if (value == 1) this.maxLines = 1 else this.maxLines = (value - 1)
            }
            this.ellipsize = TextUtils.TruncateAt.END
        }
        get() {
            return if (this.maxLines == 1) 1 else maxLines + 1
        }

    var scroll: Boolean?
        set(value) {
            if (value == false) {
                this.movementMethod = null
            } else if (value == true) {
                this.isVerticalScrollBarEnabled = true
                this.movementMethod = ScrollingMovementMethod()
            }
        }
        get() {
            return this.movementMethod != null
        }

    var underlineThickness: Int?
        set(value) {
            underThickness = value ?: DEFAULT_UNDERLINE_THICKNESS
            underlineDrawable.setStroke(
                underThickness!!,
                underColor ?: this.context.getColor(DEFAULT_TEXT_COLOR)
            )
        }
        get() {
            return underThickness ?: DEFAULT_UNDERLINE_THICKNESS
        }

    var underlineColor: Triple<Int, Int, Int>?
        set(value) {
            val color = getColor(value)
            underColor = color
        }
        get() {
            val colorCode = underColor ?: DEFAULT_TEXT_COLOR
            return Triple(Color.red(colorCode), Color.green(colorCode), Color.blue(colorCode))
        }

    var underlineColorClear: Boolean?
        set(value) {
            if (value == true) {
                underlineDrawable.setStroke(
                    0,
                    underColor ?: this.context.getColor(DEFAULT_TEXT_COLOR)
                )
                underThickness = 0
            }
        }
        get() {
            return underThickness == 0
        }

    var urlTextColor: Triple<Int, Int, Int>?
        set(value) {
            val color = getColor(value)
            urlColor = color
        }
        get() {
            val colorCode = urlColor ?: DEFAULT_TEXT_COLOR
            return Triple(Color.red(colorCode), Color.green(colorCode), Color.blue(colorCode))
        }

    var urlColorClear: Boolean?
        set(value) {
            if (value == true) {
                urlColor = DEFAULT_TEXT_COLOR
            }
        }
        get() {
            return urlColor == DEFAULT_TEXT_COLOR
        }

    var urlAlign: Int?
        set(value) {
            urlAlignLocal = when (value) {
                0 -> Layout.Alignment.ALIGN_NORMAL
                1 -> Layout.Alignment.ALIGN_OPPOSITE
                2 -> Layout.Alignment.ALIGN_CENTER
                else -> Layout.Alignment.ALIGN_CENTER
            }
        }
        get() {
            return when (urlAlignLocal) {
                Layout.Alignment.ALIGN_NORMAL -> 0
                Layout.Alignment.ALIGN_OPPOSITE -> 1
                Layout.Alignment.ALIGN_CENTER -> 2
                else -> 2
            }
        }

    var urlLink: CharSequence?
        set(value) {
            urlLinkLocal = value.toString()
        }
        get() {
            return urlLinkLocal
        }

    var urlTextContent: CharSequence?
        set(value) {
            urlTextLocal = value.toString()
            val spannableString = SpannableString(this.text)
            val url = value.toString()
            val start = this.text.toString().indexOf(url)
            val end = start + url.length
            spannableString.setSpan(
                URLSpan(urlLinkLocal),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            spannableString.setSpan(
                AlignmentSpan.Standard(urlAlignLocal!!),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(urlColor ?: DEFAULT_TEXT_COLOR), start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                AbsoluteSizeSpan(urlTextFontSizeLocal ?: DEFAULT_FONT_SIZE), start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ColoredUnderlineSpan(
                    urlTextUnderlineColorLocal ?: this.context.getColor(
                        DEFAULT_TEXT_COLOR
                    ),
                    urlUnderlineThicknessLocal?.toFloat() ?: DEFAULT_UNDERLINE_THICKNESS.toFloat()
                ), start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        get() {
            return urlTextLocal
        }

    var urlTextFont: Int?
        set(value) {
            this.urlTextFontLocal = value ?: DEFAULT_TYPE_FACE
        }
        get() {
            return urlTextFontLocal ?: DEFAULT_TYPE_FACE
        }

    var urlTextFontSize: Int?
        set(value) {
            this.urlTextFontSizeLocal = value ?: DEFAULT_FONT_SIZE
        }
        get() {
            return urlTextFontSizeLocal ?: DEFAULT_FONT_SIZE
        }


    var urlUnderlineThickness: Int?
        set(value) {
            urlUnderlineThicknessLocal = value ?: DEFAULT_UNDERLINE_THICKNESS
        }
        get() {
            return urlUnderlineThicknessLocal ?: DEFAULT_UNDERLINE_THICKNESS
        }

    var urlTextUnderlineColor: Triple<Int, Int, Int>?
        set(value) {
            val color = getColor(value)
            urlTextUnderlineColorLocal = color
        }
        get() {
            val colorCode = urlTextUnderlineColorLocal ?: DEFAULT_TEXT_COLOR
            return Triple(Color.red(colorCode), Color.green(colorCode), Color.blue(colorCode))
        }

    var urlTextUnderlineColorClear: Boolean?
        set(value) {
            urlTextUnderlineColorClearLocal = value
        }
        get() {
            return urlTextUnderlineColorClearLocal
        }


    private fun getColor(value: Triple<Int, Int, Int>?): Int {
        return Color.rgb(value?.first ?: 0, value?.second ?: 0, value?.third ?: 0)
    }

    class ColoredUnderlineSpan constructor(
        @ColorInt private val underlineColor: Int,
        private val underlineThickness: Float,
    ) : CharacterStyle() {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun updateDrawState(textPaint: TextPaint) {
            textPaint.underlineColor = underlineColor
            textPaint.underlineThickness = underlineThickness
        }
    }
}