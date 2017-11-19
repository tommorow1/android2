package com.example.bloold.buildp.ui.fragments

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bloold.buildp.R
import com.example.bloold.buildp.adapter.CatalogObjectAdapter
import com.example.bloold.buildp.api.ServiceGenerator
import com.example.bloold.buildp.api.data.BaseResponse
import com.example.bloold.buildp.api.data.CatalogObject
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.common.RxHelper
import com.example.bloold.buildp.common.Settings
import com.example.bloold.buildp.components.LazyScrollPageUploader
import com.example.bloold.buildp.components.NetworkFragment
import com.example.bloold.buildp.components.OnItemClickListener
import com.example.bloold.buildp.databinding.FragmentCatalogObjectBinding
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.model.SortObject
import com.example.bloold.buildp.single.`object`.SingleObjectActivity
import com.example.bloold.buildp.ui.MainActivity
import io.reactivex.observers.DisposableSingleObserver
import java.net.ConnectException
import java.net.UnknownHostException

class CatalogObjectListFragment : NetworkFragment(), OnItemClickListener<CatalogObject>, LazyScrollPageUploader.OnLazyScrollUploaderListener {
    private lateinit var mBinding: FragmentCatalogObjectBinding
    private var catalogObjectAdapter = CatalogObjectAdapter(this)
    private var lazyScrollPageUploader = LazyScrollPageUploader(this)
    private var objectsArray: ArrayList<CatalogObjectsModel> = ArrayList()
    private var isHaveCatalog: Boolean = false
    var sortedObject: SortObject? = null

    companion object {
        private val ITEMS_ON_PAGE = 5
        private val KEY_RESPONSE_ARRAY_OBJECTS = "catalog_response"
        private val KEY_RESPONSE_HAVE_CATALOG = "catalog"
        private val KEY_RESPONSE_SORTED_OBJECTS = "sorted"

        fun newInstance(): CatalogObjectListFragment = CatalogObjectListFragment()

/*
        fun newInstance(items: ArrayList<String>): CatalogObjectListFragment {
            return CatalogObjectListFragment()
                    .apply { arguments = Bundle().apply { putStringArrayList(KEY_RESPONSE, items) } }
        }
*/

        fun newInstance(items: ArrayList<CatalogObjectsModel>, isHave: Boolean = true): CatalogObjectListFragment {
            return CatalogObjectListFragment()
                    .apply { arguments = Bundle().apply { putParcelableArrayList(KEY_RESPONSE_ARRAY_OBJECTS, items)
                    putBoolean(KEY_RESPONSE_HAVE_CATALOG, isHave)} }
        }

        fun newInstance(sortObject: SortObject?): CatalogObjectListFragment {
            return CatalogObjectListFragment()
                    .apply { arguments = Bundle().apply {
                        putParcelable(KEY_RESPONSE_SORTED_OBJECTS, sortObject)
                    } }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments != null){
            if (arguments.containsKey(KEY_RESPONSE_ARRAY_OBJECTS)) {
                objectsArray = arguments.getParcelableArrayList(KEY_RESPONSE_ARRAY_OBJECTS)
                isHaveCatalog = arguments.getBoolean(KEY_RESPONSE_HAVE_CATALOG)
            }
            if(arguments.containsKey(KEY_RESPONSE_SORTED_OBJECTS)){
                sortedObject = arguments.getParcelable(KEY_RESPONSE_SORTED_OBJECTS)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog_object, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvCatalogObjects.addOnScrollListener(lazyScrollPageUploader)
        mBinding.rvCatalogObjects.layoutManager=LinearLayoutManager(activity)
        mBinding.rvCatalogObjects.adapter=catalogObjectAdapter

        showProgress(true)
        loadCatalogObjects(1)
        /*if (view is RecyclerView) {
            if(!isHaveCatalog) {
                if(urlResponse != null)
                    presenter.getCatalogObjects(urlResponse!!)
            } else
                onObjectsLoaded(objectsArray)
            if(sortedObject != null){

            }
        }*/
    }

    private fun showProgress(showProgress: Boolean) {
        mBinding.pbLoading.visibility = if (showProgress) View.VISIBLE else View.GONE
    }
    private fun updateNoItemsView() {
        if (mBinding.rvCatalogObjects.adapter.itemCount == 0) {
            mBinding.rvCatalogObjects.visibility = View.GONE
            mBinding.tvNothingToShow.visibility = View.VISIBLE
        } else {
            mBinding.rvCatalogObjects.visibility = View.VISIBLE
            mBinding.tvNothingToShow.visibility = View.GONE
        }
    }


    override fun onItemClick(item: CatalogObject) {
        startActivity(Intent(context, SingleObjectActivity::class.java)
                .putExtra(IntentHelper.EXTRA_OBJECT_ID, item.id))
    }

    override fun onLoadData(totalItems: Int) {
        loadCatalogObjects(totalItems/ITEMS_ON_PAGE+1)
    }

    private fun loadCatalogObjects(page: Int)
    {
        val filters = HashMap<String,String>()
        Settings.catalogFilters?.forEach { filters.put("filter[$it]","Y") }
        getCompositeDisposable().add(ServiceGenerator.serverApi.getCatalogObjects(filters, ITEMS_ON_PAGE, page)
                .compose(RxHelper.applySchedulers())
                .doFinally {
                    showProgress(false)
                    lazyScrollPageUploader.setLoading(false)
                    updateNoItemsView()
                }
                .subscribeWith(object : DisposableSingleObserver<BaseResponse<CatalogObject>>() {
                    override fun onSuccess(result: BaseResponse<CatalogObject>) {
                        result.data?.items?.let {
                            val allItemsLoaded = it.size < ITEMS_ON_PAGE
                            catalogObjectAdapter.isShowLoadingFooter = !allItemsLoaded
                            lazyScrollPageUploader.noMoreElements=allItemsLoaded

                            if(page==1)
                                catalogObjectAdapter.setData(it)
                            else
                                catalogObjectAdapter.addData(it)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if (e is UnknownHostException || e is ConnectException)
                            Toast.makeText(activity, R.string.error_check_internet, Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(activity, R.string.server_error, Toast.LENGTH_SHORT).show()
                    }
                }))

    }
}
