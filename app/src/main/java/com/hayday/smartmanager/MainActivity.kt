package com.hayday.smartmanager

import android.app.Activity
import android.os.Bundle
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.Color

data class Product(val name: String, val factory: String, val minutes: Int)

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("manager_settings", MODE_PRIVATE) }
    private val products = listOf(
        Product("خبز", "المخبز", 5),
        Product("خبز الذرة", "المخبز", 30),
        Product("كعكة الجزر", "المخبز", 60),
        Product("سكر بني", "مطحنة السكر", 20),
        Product("سكر أبيض", "مطحنة السكر", 25),
        Product("شراب", "مطحنة السكر", 90),
        Product("كريمة", "مصنع الألبان", 20),
        Product("زبدة", "مصنع الألبان", 30),
        Product("جبن", "مصنع الألبان", 60),
        Product("علف الدجاج", "مطحنة الأعلاف", 5),
        Product("علف الأبقار", "مطحنة الأعلاف", 10),
        Product("علف الخنازير", "مطحنة الأعلاف", 20)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    private fun render() {
        val scroll = ScrollView(this)
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 24, 20, 28)
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            setBackgroundColor(Color.rgb(247, 249, 244))
        }
        scroll.addView(page)
        setContentView(scroll)

        val title = TextView(this).apply {
            text = "Hay Day Smart Manager"
            textSize = 24f
            setTextColor(Color.rgb(48, 88, 54))
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }
        page.addView(title)
        addText(page, "إدارة المصانع والمنتجات", 17f, true)
        addText(page, "اختر المنتجات التي تريد متابعتها وحدد الكمية المطلوبة.", 14f, false)

        val note = TextView(this).apply {
            text = "تنبيه: هذه نسخة إدارة ومتابعة فقط. لا تتصل بخوادم Hay Day ولا تنفذ التصنيع أو الجمع داخل اللعبة."
            textSize = 13f
            setTextColor(Color.rgb(130, 83, 20))
            setPadding(14, 14, 14, 14)
            setBackgroundColor(Color.rgb(255, 241, 210))
        }
        page.addView(note)

        products.groupBy { it.factory }.forEach { (factory, items) ->
            val header = TextView(this).apply {
                text = factory
                textSize = 19f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.rgb(47, 91, 55))
                setPadding(0, 22, 0, 8)
            }
            page.addView(header)

            items.forEach { product ->
                val card = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(14, 10, 14, 10)
                    setBackgroundColor(Color.WHITE)
                }
                val row = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                }
                val check = CheckBox(this).apply {
                    text = product.name
                    textSize = 16f
                    isChecked = prefs.getBoolean("enabled_${product.name}", false)
                    setOnCheckedChangeListener { _, checked ->
                        prefs.edit().putBoolean("enabled_${product.name}", checked).apply()
                    }
                }
                row.addView(check, LinearLayout.LayoutParams(0, -2, 1f))
                val qty = EditText(this).apply {
                    hint = "الكمية"
                    inputType = android.text.InputType.TYPE_CLASS_NUMBER
                    textSize = 14f
                    setSingleLine(true)
                    setEms(4)
                    setText(prefs.getInt("qty_${product.name}", 1).toString())
                    setSelectAllOnFocus(true)
                    setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            val n = text.toString().toIntOrNull()?.coerceIn(1, 999) ?: 1
                            prefs.edit().putInt("qty_${product.name}", n).apply()
                            setText(n.toString())
                        }
                    }
                }
                row.addView(qty)
                card.addView(row)
                val detail = TextView(this).apply {
                    text = "وقت التصنيع المرجعي: ${product.minutes} دقيقة"
                    textSize = 12f
                    setTextColor(Color.GRAY)
                }
                card.addView(detail)
                page.addView(card)
                val divider = View(this).apply { setBackgroundColor(Color.rgb(230, 234, 225)) }
                page.addView(divider, LinearLayout.LayoutParams(-1, 1))
            }
        }

        val footer = TextView(this).apply {
            text = "الإعدادات تُحفظ تلقائياً على هذا الهاتف."
            textSize = 13f
            setTextColor(Color.DKGRAY)
            setPadding(0, 24, 0, 8)
            gravity = Gravity.CENTER
        }
        page.addView(footer)
    }

    private fun addText(parent: LinearLayout, value: String, size: Float, bold: Boolean) {
        val tv = TextView(this).apply {
            text = value
            textSize = size
            setTextColor(Color.rgb(45, 55, 45))
            if (bold) typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 8)
        }
        parent.addView(tv)
    }
}
