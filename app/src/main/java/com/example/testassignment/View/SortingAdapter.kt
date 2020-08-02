package com.example.testassignment.View

import android.R.color
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.testassignment.R


class SortingAdapter(
    context: Context,
    var listItemsTxt: List<String>) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.sorting_view, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        vh.label.text = listItemsTxt[position]

        return view
    }

    override fun getItem(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(row: View?) {
        val label: TextView
        init {
            this.label = row?.findViewById(R.id.sortTv) as TextView
        }
    }
}