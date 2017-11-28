package com.example.bloold.buildp.api

import com.example.bloold.buildp.api.data.BaseResponse
import com.example.bloold.buildp.api.data.BaseResponseWithDataObject
import com.example.bloold.buildp.api.data.CatalogObject
import com.example.bloold.buildp.api.data.CurrentUserCondition
import com.example.bloold.buildp.model.Category
import com.example.bloold.buildp.model.ConditionMark
import com.example.bloold.buildp.model.MyItem
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 24.05.16.
 */
interface ServerApi {
    @GET("directory/type-catalog-structure/")
    fun getCategories(): Single<BaseResponse<Category>>
    @GET("object/list/")
    fun getCatalogObjects(@QueryMap filter:Map<String, String>?,
                          @Query("limit") limit: Int, @Query("page") page: Int,
                          @Query("filter[IBLOCK_SECTION_ID][0]") categoryId: String?=null,
                          @Query("filter[INCLUDE_SUBSECTIONS]") includeSubsections: String="Y",
                          @Query("select[]") selectParams: Array<String> = ApiHelper.defaultParams): Single<BaseResponseWithDataObject<CatalogObject>>
    @GET("directory/type-map-structure/")
    fun getMapStructure(): Single<BaseResponse<CatalogObject>>
    @POST("object/edit/")
    @FormUrlEncoded
    fun editObject(@Field("FORM[ID]") objectId:Int,
                   @Field("FORM[PROPS][CONDITION][DETAIL_TEXT]") conditionDescription:String,
                   @Field("FORM[PROPS][CONDITION][PROPS][MARK]") conditionMarkId:Int,
                   @FieldMap extraFields:Map<String,String>): Single<Response<Void>>
    //------ Состояния объектов ---------
    @GET("directory/condition-mark/")
    fun getConditionMark(): Single<BaseResponse<ConditionMark>>
    /*** Текущее состояние объекта */
    @GET("object/index/")
    fun getObjectConditionMark(@Query("OBJECT_ID") objectId: Int,
                               @Query("limit") limit: Int=1,
                               @Query("page") page: Int=1,
                               @Query("select[]") condition: String="CONDITION"): Single<CurrentUserCondition>
    @POST("object/condition/send/")
    fun sendCondition(@Query("OBJECT_ID") objectId:Int,
                      @Query("MESSAGE") message:String,
                      @Query("MARK_ID") int:Int,
                      @Query("DATE") date_dd_MM_YY:String,
                      @Query("PHOTO_ID") photoId:Int?=null): Single<Response<Void>>
    //-------
    @GET("object/index/")
    fun searchMapObjects(@QueryMap filter:Map<String, String>?,
                         @Query("limit") limit: Int?=null, @Query("page") page: Int?=null,
                         @Query("select[]") selectParams: Array<String> = ApiHelper.mapParams): Single<BaseResponseWithDataObject<MyItem>>
    @GET("object/index/")
    fun searchMapObjectsByName(@Query("filter[0][?NAME]=") query: String,
                               @Query("select[]") selectParams: Array<String> = ApiHelper.mapParams): Single<BaseResponseWithDataObject<MyItem>>
    @POST("object/favorite/toggle/")
    @FormUrlEncoded
    fun toggleFavourite(@Field("OBJECT_ID") objectId:Int): Single<BaseResponseWithDataObject<Void>>
    @POST("file/upload/")
    @FormUrlEncoded
    fun uploadFile(@FieldMap uploadFileData:Map<String,String>): Single<Response<BaseResponseWithDataObject<Long>>>
}
