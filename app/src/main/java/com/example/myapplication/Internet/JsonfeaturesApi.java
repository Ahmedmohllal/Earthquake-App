package com.example.myapplication.Internet;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonfeaturesApi {
    @GET("query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=10")
    Observable<ResponseBody> getAllFeatures();


}
