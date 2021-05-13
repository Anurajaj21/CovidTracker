package com.example.covidtracker.Utils

import android.content.Context
import android.util.Log
import java.lang.Exception

class LoadingUtils {
    companion object{
        private var loader: CustomLoader? = null
        fun  showDialog(context: Context?, isCancelable: Boolean){
            hideDialog()
            Log.d("loader util", context.toString())
            if(context != null){
                try {
                    loader = CustomLoader(context)

                    loader?.toString()?.let { Log.d("loading", it) }
                    loader?.let {
                        it.setCanceledOnTouchOutside(true)
                        it.setCancelable(isCancelable)
                        it.show()
                        Log.d("loader", "showing")
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        fun hideDialog() {
            if (loader != null && loader?.isShowing!!){
                loader= try {
                    loader?.dismiss()
                    null
                }catch (e: Exception){
                    null
                }
            }
        }
    }
}