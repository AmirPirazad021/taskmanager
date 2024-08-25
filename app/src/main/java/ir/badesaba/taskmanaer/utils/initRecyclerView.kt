package ir.badesaba.taskmanaer.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.initRecyclerView(
    context: Context,
    adapterInstance: RecyclerView.Adapter<*>,
    isReversed: Boolean = false
) {

    this.apply {

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, isReversed)

        setHasFixedSize(false)
        adapter = adapterInstance
    }
}
