package com.example.covid19_report;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EndpointInterface {
    @GET("/dayone/country/IN")
    Call<String> getAll();
}
