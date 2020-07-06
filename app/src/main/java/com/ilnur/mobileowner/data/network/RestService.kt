package com.ilnur.mobileowner.data.network

import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.*
import com.ilnur.mobileowner.domain.models.response.CarInfo
import com.ilnur.mobileowner.domain.models.response.CustomResult
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.domain.models.response.Token
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.itis.kalimullinri.itisowner.domain.models.*
import retrofit2.http.GET
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap


interface RestService {
    //@FormUrlEncoded @FieldMap params: Map<String, String>
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @POST("/api/user/token")
    fun authorize(@Body params: Map<String, String>): Single<Token>

    @GET("/api/v1/Vehicle")
    fun getCarsOnMap(@Header("Authorization") token: String): Flowable<MainResponceArray<CarOnMap>>

    @GET("/api/v1/Packet/object/{imei}")
    fun getCarInfo(@Header("Authorization") token: String,
                   @Path("imei") imei: String): Flowable<MainResponceObject<CarInfo>>


    @GET("/api/v1/Profile")
    fun getProfile(@Header("Authorization") token: String): Single<MainResponceObject<Profile>>

    @POST("/api/v1/Packet/trackinfo")
    fun getMonitoringInfo(@Header("Authorization") token: String,
                          @Body monitoringCarInfoRequest: MonitoringCarInfoRequest
    ): Flowable<MonitoringRequest>

    @POST("/api/v1/Packet/track")
    fun getTrackInfo(@Header("Authorization") token: String,
                     @Body monitoringCarInfoRequest: MonitoringCarInfoRequest
    ): Flowable<TrackInfoRequest>


    //region CHART
    //endregion

    // region CHAT
    //endregion

    // region USERS
    // endregion

    // region REPORT
    @GET("/api/v1/Report/types")
    fun getReportType(@Header("Authorization") token: String): Flowable<MainResponceArray<DefaultRequest>>

    @GET("/api/v1/Vehicle/selectlist")
    fun getReportCars(@Header("Authorization") token: String): Flowable<MainResponceArray<ReportCarsList>>

    @POST("/api/v1/Report/order")
    fun getReport(@Header("Authorization") token: String,
                  @Body reportOrderRequest: ReportOrderRequest
    ): Flowable<MainResponceObject<DefaultObjectRequest>>

    @GET("/api/v1/Report/formats")
    fun getReportFormat(@Header("Authorization") token: String): Flowable<MainResponceArray<DefaultRequest>>

    @GET("/api/v1/Report")
    fun getReportList(@Header("Authorization") token: String): Flowable<MainResponceArray<ReportRequest>>

    @Streaming
    @GET ("/api/v1/Report/download")
    fun downloadReport(@Header("Authorization") token: String,
                       @Query("reportId") reportId: Int,
                       @Query("type") type:Int): Call<ResponseBody>

    @Streaming
    @GET("/uploads/posts/2016-08/medium/1472042903_31.jpg")
    fun downloadTestReport(): Call<ResponseBody>
    // endregion

    //region RECOVERY PASSWORD
    //endregion

    //region FIREBASE
    @POST("/api/v1/Firebase/token")
    fun pushToken(@Header("Authorization") token: String,
                  @Body firebaseModel: FirebaseModel
    ): Single<CustomResult>
    //endregion
}