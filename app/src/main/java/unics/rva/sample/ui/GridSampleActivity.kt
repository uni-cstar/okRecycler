package unics.rva.sample.ui

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.leanback.widget.VerticalGridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import unics.rva.sample.R
import unics.rval.RvalPresenter
import unics.rval.RvalViewHolder
import unics.rval.rval
import unics.rval.rvalSingle

/**
 * Create by luochao
 * on 2023/11/23
 */
class GridSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvScene()
    }

    private fun vrvScene(){
        val rv = VerticalGridView(this)
        rv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(rv)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            resources.displayMetrics.heightPixels/6
        )

        var total:Int = 0
        rv.setNumColumns(6)
        rv.rvalSingle{
            itemCreate {
                total++
                Log.i("TAG", "itemCreated: $total")
                val view = ImageView(it.context).apply {
                    isFocusable = true
                    isFocusableInTouchMode = true
                    layoutParams = lp
                    setImageResource(R.drawable.hellowrold)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                RvalViewHolder(view)
            }
            itemBind {
                Log.i("TAG", "itemBind: ${this.getAdapterPositionUnsafe()}")
            }
        }

        rv.post {
            rv.rval.setItems( (0..1000).map {
                it
            }.toList())
        }
    }

    private fun rvScene(){
        val rv = RecyclerView(this)
        rv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(rv)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            resources.displayMetrics.heightPixels/6
        )
        rv.setItemViewCacheSize(20)
        val rvp = RecycledViewPool()
//        rvp.setMaxRecycledViews(0,50)
        rv.setRecycledViewPool(rvp)
        rv.layoutManager = GridLayoutManager(this,6)
        var total:Int = 0
        rv.adapter = object:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                total++
                Log.i("TAG", "itemCreated: $total")
                val view = ImageView(parent.context).apply {
                    isFocusable = true
                    isFocusableInTouchMode = true
                    layoutParams = lp
                    setImageResource(R.drawable.hellowrold)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                return object :RecyclerView.ViewHolder(view){}
            }

            override fun getItemCount(): Int {
                return 1000
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                Log.i("TAG", "itemBind: $position")
            }

        }
//
////        rv.setItemViewCacheSize(100)
//        rv.rvalSingle{
//            itemCreate {
//                total++
//                Log.i("TAG", "itemCreated: $total")
//                val view = ImageView(it.context).apply {
//                    isFocusable = true
//                    isFocusableInTouchMode = true
//                    layoutParams = lp
//                    setImageResource(R.drawable.hellowrold)
//                    scaleType = ImageView.ScaleType.CENTER_CROP
//                }
//                RvalViewHolder(view)
//            }
//            itemBind {
//                Log.i("TAG", "itemBind: ${this.getAdapterPositionUnsafe()}")
//            }
//        }
//
//        rv.post {
//            rv.rval.setItems( (0..1000).map {
//                it
//            }.toList())
//        }
    }
}