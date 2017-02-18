package com.glam.service;

import com.glam.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ericbasendra on 10/06/16.
 */
public interface WebService {


    @GET(AppConstants.GET_EXCHANGE_RATES)
    Observable<Response<ResponseBody>> getExchangeRates();
}
