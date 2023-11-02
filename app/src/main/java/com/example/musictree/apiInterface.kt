package com.example.musictree

import retrofit2.http.GET

interface apiInterface {

    @GET("items/songs")
    suspend fun getSong():retrofit2.Response<songData>
}