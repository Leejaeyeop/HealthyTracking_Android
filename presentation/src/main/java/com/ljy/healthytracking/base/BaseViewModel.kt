package com.ljy.healthytracking.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel :ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) { // RxJava로 Obsevable을 Observing 할 때 사용할 함수이다
        compositeDisposable.add(disposable)
    }

    override fun onCleared() { //bserving을 그만두게 될 때(뷰가 사라질 때) 메모리 누수를 방지하기 위해 compositeDisposable을 비우는 함수이다
        compositeDisposable.clear()
        super.onCleared()
    }
}