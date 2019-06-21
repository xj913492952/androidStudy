package com.style.base;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.StringRes;
import android.util.Log
import com.style.app.AppManager

import com.style.app.LogManager;
import com.style.app.ToastManager;
import com.style.data.db.AppDatabase
import com.style.data.http.core.RetrofitImpl
import com.style.data.http.exception.HttpThrowableUtil;
import com.style.data.prefs.AppPrefsManager

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


abstract class BasePresenter {
    protected val TAG = this.javaClass.simpleName
    private val tasks = CompositeDisposable()
    private var singleTasks: MutableList<Disposable>? = null

    fun getApplication(): Application {
        return AppManager.getInstance().getContext() as Application
    }

    protected fun getPreferences(): AppPrefsManager {
        return AppPrefsManager.getInstance()
    }

    protected fun getHttpApi(): RetrofitImpl {
        return RetrofitImpl.getInstance()
    }

    protected fun getDataBase(): AppDatabase {
        return AppDatabase.getInstance(getApplication())
    }

    fun onCleared() {
        Log.e(TAG, "onCleared")
        removeAllTask()
        removeSingleTask()
    }

    protected fun addTask(d: Disposable) {
        tasks.add(d)
    }

    protected fun removeAllTask() {
        tasks?.dispose()
    }

    /**
     * 频繁重复请求时
     *
     * @param d
     * @return
     */
    protected fun addSingleTask(d: Disposable): Boolean {
        removeSingleTask()
        if (singleTasks == null)
            singleTasks = ArrayList()
        return singleTasks!!.add(d)
    }

    protected fun removeSingleTask() {
        if (singleTasks != null) {
            for (d in singleTasks!!) {
                d.dispose()
            }
        }
    }

    protected fun showToast(str: CharSequence) {
        ToastManager.showToast(getApplication(), str)
    }

    protected fun showToast(@StringRes resId: Int) {
        ToastManager.showToast(getApplication(), resId)
    }

    protected fun logI(tag: String, msg: String) {
        LogManager.logI(tag, msg)
    }

    protected fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    fun handleHttpError(e: Throwable) {
        HttpThrowableUtil.handleHttpError(getApplication(), e)
    }
}
