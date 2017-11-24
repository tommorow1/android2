package com.example.bloold.buildp.components

import android.content.Context
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.bloold.buildp.R
import com.example.bloold.buildp.api.ErrorUtils
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL


/**
 * Created by sagus on 21.11.2017.
 */
object UIHelper
{
    /*** Есть ли ошибка в ответе сервера  */
    fun showServerError(response: Response<*>, cntx: Context) {
        val error = ErrorUtils.parseError(response)
        if (error != null && !TextUtils.isEmpty(error.message))
            Toast.makeText(cntx.applicationContext, error.message, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(cntx.applicationContext, R.string.server_error, Toast.LENGTH_LONG).show()
    }
    fun makeEditTextScrollable(editText:EditText)
    {
        editText.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
    }

    fun hideKeyboard(view: View?) {
        view?.let {
            val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    fun getYoutubePreviewImage(youtubeUrl: String?): String?
    {
        youtubeUrl?.let {
            try {
                val videoId = extractYoutubeId(youtubeUrl)
                return "http://img.youtube.com/vi/$videoId/0.jpg" // this is link which will give u thumnail image of that video
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        }
        return null
    }

    @Throws(MalformedURLException::class)
    private fun extractYoutubeId(url: String): String? {
        val query:String? = URL(url).query
        val param:List<String>? = query?.split("&".toRegex())?.dropLastWhile({ it.isEmpty() })
        var id: String? = null
        param?.let { it.map { row -> row.split("=".toRegex()).dropLastWhile({ it.isEmpty() }) }
                .filter { it[0] == "v" }
                .forEach { id = it[1] } }
        if(id==null)
        {
            val splashPos=url.lastIndexOf("/")
            if(splashPos!=-1&&splashPos!=url.length-1)
                id=url.substring(splashPos+1)
        }
        return id
    }
}