package com.ljy.healthytracking.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<T:ViewDataBinding>(
        @LayoutRes val layoutID: Int //레이아웃 id
) :AppCompatActivity(){
    lateinit var binding: T
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //초기화된 layoutId로 databinding 객체 생성
        binding = DataBindingUtil.setContentView(this,layoutID)
        binding.lifecycleOwner = this
    }

}