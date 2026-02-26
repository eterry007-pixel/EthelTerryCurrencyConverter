package com.example.ethelterrycurrencyconverter.data

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("latest/{base}")
    suspend fun getRates(@Path("base") base: String): CurrencyResponse
}
