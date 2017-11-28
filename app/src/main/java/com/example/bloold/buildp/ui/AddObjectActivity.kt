package com.example.bloold.buildp.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import com.example.bloold.buildp.R
import com.example.bloold.buildp.api.ServiceGenerator
import com.example.bloold.buildp.api.data.BaseResponse
import com.example.bloold.buildp.api.data.BaseResponseWithoutData
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.common.RxHelper
import com.example.bloold.buildp.components.NetworkActivity
import com.example.bloold.buildp.components.UIHelper
import com.example.bloold.buildp.databinding.ActivityAddObjectBinding
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException


class AddObjectActivity : NetworkActivity() {
    private lateinit var mBinding: ActivityAddObjectBinding
    private var latitude:Double?=null
    private var longitude:Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_object)
        mBinding.listener=this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.tvAddObjectRules.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==ChooseLocationActivity.REQUEST_CHOOSE_LOCATION)
            {
                data?.let {
                    mBinding.tvAddress.text=data.getStringExtra(IntentHelper.EXTRA_ADDRESS)
                    latitude=data.getSerializableExtra(IntentHelper.EXTRA_LATITUDE) as Double
                    longitude=data.getSerializableExtra(IntentHelper.EXTRA_LONGITUDE) as Double
                }
            }
        }
    }
    fun onGoToFullFormClick(v:View)
    {
        //TODO
    }
    fun onAddObjectClick(v:View)
    {
        var error = UIHelper.setErrorIfEmpty(mBinding.etName, getString(R.string.need_fill_this_field))
        if(latitude==null||longitude==null)
        {
            error=true
            Toast.makeText(this, R.string.need_address_for_adding_object, Toast.LENGTH_LONG).show()
        }
        if(error) return

        compositeDisposable.add(ServiceGenerator.serverApi.addObject(mBinding.etName.text.toString(),
                mBinding.etFolkName.text.toString(),
                mBinding.tvAddress.text.toString(),
                latitude.toString()+","+longitude.toString())
                .compose(RxHelper.applySchedulers())
                .doOnSubscribe { showProgress(true) }
                .subscribeWith(object : DisposableSingleObserver<Response<BaseResponseWithoutData>>() {
                    override fun onSuccess(result: Response<BaseResponseWithoutData>) {
                        if(result.isSuccessful&&result.body()?.code==200)
                        {
                            Toast.makeText(this@AddObjectActivity, R.string.object_added, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else
                        {
                            UIHelper.showServerError(result, this@AddObjectActivity)
                            showProgress(false)
                        }
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        showProgress(false)
                        if (e is UnknownHostException || e is ConnectException)
                            Toast.makeText(applicationContext, R.string.error_check_internet, Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(applicationContext, R.string.server_error, Toast.LENGTH_SHORT).show()
                    }
                }))
    }
    fun onChooseAddressClick(v:View)
    {
        ChooseLocationActivity.launch(this, ChooseLocationActivity.REQUEST_CHOOSE_LOCATION, latitude, longitude)
    }

    private fun showProgress(showProgress: Boolean) {
        mBinding.flLoading.visibility = if (showProgress) View.VISIBLE else View.GONE
    }
}
