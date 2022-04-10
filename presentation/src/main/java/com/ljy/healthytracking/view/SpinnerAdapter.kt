package com.ljy.healthytracking.view

import android.content.Context
import android.graphics.Rect
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.lang.ref.WeakReference

class SpinnerAdapter(context: Context,
                     @param:LayoutRes @field:LayoutRes
                     private val layoutId: Int,
                     private val viewModelVariable: Int,
                     objects: List<Any>) : ArrayAdapter<Any>(context,layoutId,objects){

    private val bindings = SparseArray<ViewDataBinding>()
    private var parent = WeakReference<Spinner>(null)

    private val getViewTouchListener = { view: View, t: MotionEvent ->
        if (t.getAction() == MotionEvent.ACTION_UP && t.getPointerCount() == 1) {
            this.parent.get()?.let {
                val r = Rect()
                it.getLocalVisibleRect(r)
                if (r.contains(t.getX().toInt(), t.getY().toInt())) {
                    it.performClick()
                }
            }
        }
        false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        this.parent = WeakReference(parent as Spinner)
        var viewBining  = convertView?.let {
            DataBindingUtil.bind<ViewDataBinding>(it)
        } ?: DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false)
        //viewBining.root.setOnTouchListener(getViewTouchListener)
        //viewBining.setVariable(viewModelVariable, getItem(position))
        //viewBining.executePendingBindings()
        return viewBining.root
    }


}