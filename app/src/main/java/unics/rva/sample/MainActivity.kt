package unics.rva.sample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import unics.rva.sample.ui.SampleActivity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.v1).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v11).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v12).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v13).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v2).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v21).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v22).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v23).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v3).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v31).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v32).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v33).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v4).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v41).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v42).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.v43).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h1).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h11).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h12).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h13).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h2).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h21).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h22).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h23).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h3).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h31).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h32).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h33).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h4).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h41).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h42).setOnClickListener(::onViewClick)
        findViewById<View>(R.id.h43).setOnClickListener(::onViewClick)
    }

    fun onViewClick(v: View) {
        val summary = (v as TextView).text.toString()
        when (v.id) {
            R.id.v1 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample_side_false,
                        false, summary
                    )
                )
            }

            R.id.v11 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample_side_false,
                        true, summary
                    )
                )
            }

            R.id.v12 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample_side_false,
                        false, summary
                    )
                )
            }

            R.id.v13 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample_side_false,
                        true, summary
                    )
                )
            }

            R.id.v2 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample,
                        false, summary
                    )
                )
            }

            R.id.v21 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample,
                        true, summary
                    )
                )
            }

            R.id.v22 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample,
                        false, summary
                    )
                )
            }

            R.id.v23 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample,
                        true, summary
                    )
                )
            }

            R.id.v3 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample_all_true,
                        false, summary
                    )
                )
            }

            R.id.v31 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample_all_true,
                        true, summary
                    )
                )
            }

            R.id.v32 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample_all_true,
                        false, summary
                    )
                )
            }

            R.id.v33 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample_all_true,
                        true, summary
                    )
                )
            }

            R.id.v4 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample_all_false,
                        false, summary
                    )
                )
            }

            R.id.v41 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_vertical_sample_all_false,
                        true, summary
                    )
                )
            }

            R.id.v42 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample_all_false,
                        false, summary
                    )
                )
            }

            R.id.v43 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_vertical_sample_all_false,
                        true, summary
                    )
                )
            }

            R.id.h1 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample_side_false,
                        false, summary
                    )
                )
            }

            R.id.h11 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample_side_false,
                        true, summary
                    )
                )
            }

            R.id.h12 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample_side_false,
                        false, summary
                    )
                )
            }

            R.id.h13 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample_side_false,
                        true, summary
                    )
                )
            }

            R.id.h2 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample,
                        false, summary
                    )
                )
            }

            R.id.h21 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample,
                        true, summary
                    )
                )
            }

            R.id.h22 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample,
                        false,summary
                    )
                )
            }

            R.id.h23 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample,
                        true,summary
                    )
                )
            }

            R.id.h3 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_true,
                        false,summary
                    )
                )
            }

            R.id.h31 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_true,
                        true,summary
                    )
                )
            }

            R.id.h32 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_true,
                        false,summary
                    )
                )
            }

            R.id.h33 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_true,
                        true,summary
                    )
                )
            }

            R.id.h4 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_false,
                        false,summary
                    )
                )
            }

            R.id.h41 -> {
                startActivity(
                    SampleActivity.newIntent(
                        1,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_false,
                        true,summary
                    )
                )
            }

            R.id.h42 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_false,
                        false,summary
                    )
                )
            }

            R.id.h43 -> {
                startActivity(
                    SampleActivity.newIntent(
                        3,
                        0,
                        R.layout.activity_rval_horizontal_sample_all_false,
                        true,summary
                    )
                )
            }
        }
    }
}
