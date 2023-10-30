package unics.rva.sample.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.FocusHighlightHelper
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.VerticalGridView
import unics.rva.sample.R
import unics.rval.RvalHorizontalGridView
import unics.rval.RvalVerticalGridView
import unics.rval.rval
import kotlin.random.Random

/**
 * Create by luochao
 * on 2023/10/27
 */

open class SampleActivity() : Activity() {

    companion object {
        fun newIntent(numCount: Int, type: Int,layoutId:Int,focusOpt:Boolean,sumary:String): Intent {
            val intent = Intent("rva.intent.action.VIEW")
            intent.data = Uri.parse("rva://sample?count=$numCount&type=$type&layoutId=$layoutId&focusOpt=$focusOpt&sumary=$sumary")
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = intent.data!!.getQueryParameter("type")
        val count = intent.data?.getQueryParameter("count")?.toIntOrNull() ?: 1
        setContentView(intent.data!!.getQueryParameter("layoutId")!!.toInt())
        findViewById<TextView>(R.id.tipText).text = intent.data!!.getQueryParameter("sumary")
        val focusOpt = intent.data?.getQueryParameter("focusOpt")?.toBooleanStrictOrNull() ?: false

        if (type == "1") {
            val gridView = findViewById<RvalHorizontalGridView>(R.id.gridView)
            gridView.setNumRows(count)
            gridView.focusSearchOptimization = focusOpt
            setupGridView(gridView)
        } else {
            val gridView = findViewById<RvalVerticalGridView>(R.id.gridView)
            gridView.setNumColumns(count)
            gridView.focusSearchOptimization = focusOpt
            setupGridView(gridView)
        }
    }

    private fun setupGridView(gridView: BaseGridView) {
        val data = mutableListOf<Any>()
        repeat(50) {
            val random = Random.nextInt()
            if (random % 2 == 0) {
                data.add(Text("Text$it"))
            }else{
                data.add(Image)
            }
        }
        gridView.setItemSpacing(24)
        gridView.rval {
            FocusHighlightHelper.setupBrowseItemFocusHighlight(this,FocusHighlight.ZOOM_FACTOR_MEDIUM,true)
            addClassPresenter<Text> {
                itemViewCreate {
                    TextView(it.context).apply {
                        isFocusable = true
                        gravity = Gravity.CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            120
                        )
                        setBackgroundResource(R.drawable.text_select)
                    }
                }
                itemBind {
                    (view as TextView).text = getModel<Text>().content
                }
            }
            addClassPresenter<Image> {
                itemViewCreate {
                    ImageView(it.context).apply {
                        isFocusable = true
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            240
                        )
                        setBackgroundResource(R.drawable.image_select)
                        setImageResource(R.drawable.hellowrold)
                    }
                }
            }

            setItems(data)
        }
    }

    private object Image
    private class Text(val content: String)
}