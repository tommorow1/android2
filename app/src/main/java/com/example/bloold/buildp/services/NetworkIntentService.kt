package com.example.bloold.buildp.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.text.TextUtils
import com.example.bloold.buildp.R
import com.example.bloold.buildp.api.ServiceGenerator
import com.example.bloold.buildp.api.data.BaseResponse
import com.example.bloold.buildp.common.IntentHelper
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Created by sagus on 19.11.2017.
 */
class NetworkIntentService : IntentService(TAG) {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            when (action) {
                IntentHelper.ACTION_TOGGLE_FAVOURITE -> toggleFavourite(intent.getIntExtra(IntentHelper.EXTRA_OBJECT_ID,-1))
            }
        }
    }

    private fun toggleFavourite(objectId: Int) {
        ServiceGenerator.serverApi.toggleFavourite(objectId)
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : DisposableSingleObserver<BaseResponse<Void>>() {
                    override fun onSuccess(result: BaseResponse<Void>) {
                      /*  if (result.isSuccessful)
                            sendEvent(IntentHelper.ACTION_ARCHIVE_PRODUCT, null)
                        else {
                            val error = ErrorUtils.parseError(result)
                            if (error != null && !TextUtils.isEmpty(error.error_text))
                                sendEvent(IntentHelper.ACTION_ARCHIVE_PRODUCT, error.error_text)
                            else
                                sendEvent(IntentHelper.ACTION_ARCHIVE_PRODUCT, R.string.server_error)
                        }*/
                    }

                    override fun onError(error: Throwable) {
                        error.printStackTrace()
                        detectAndSendError(IntentHelper.ACTION_TOGGLE_FAVOURITE, error)
                    }
                })
    }

    private fun detectAndSendError(action: String, error: Throwable) {
        if (error is UnknownHostException || error is ConnectException)
            sendEvent(action, R.string.error_check_internet)
        else
            sendEvent(action, R.string.server_error)
    }

    /*** Отправка сообщения о завершении загрузки  */
    private fun sendEvent(action: String, errorMsg: String?) {
        val intent = Intent(action)
        if (!TextUtils.isEmpty(errorMsg)) intent.putExtra(IntentHelper.EXTRA_ERROR_MSG, errorMsg)
        sendBroadcast(intent)
    }

    private fun sendEvent(action: String, errorMsg: String, value: Int) {
        val intent = Intent(action)
        if (!TextUtils.isEmpty(errorMsg)) intent.putExtra(IntentHelper.EXTRA_ERROR_MSG, errorMsg)
        intent.putExtra(IntentHelper.EXTRA_VALUE, value)
        sendBroadcast(intent)
    }

    private fun sendEvent(action: String, @StringRes errorId: Int) {
        sendEvent(action, getString(errorId))
    }

    companion object {

        private val TAG = "NetworkIntentService"

        fun toogleFavourite(context: Context, objectId: Int) {
            context.startService(Intent(context, NetworkIntentService::class.java)
                    .setAction(IntentHelper.ACTION_TOGGLE_FAVOURITE)
                    .putExtra(IntentHelper.EXTRA_OBJECT_ID, objectId))
        }
    }
}