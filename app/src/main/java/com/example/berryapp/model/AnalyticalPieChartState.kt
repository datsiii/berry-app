package com.example.berryapp.model

import android.os.Parcelable
import android.view.View

class AnalyticalPieChartState(
    private val superSavedState: Parcelable?,
    val dataList: List<Pair<Int, String>>
) : View.BaseSavedState(superSavedState), Parcelable {
}