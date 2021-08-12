package com.example.newsapp

import com.example.newsapp.api.NewsApiJSON
import retrofit2.http.GET

interface APIRequest {
    @GET("/v1/latest-news?language=en&apiKey=tFenMSFjp5MGsM7LTEPuQzY-aPz8_kSHcPWURQ0_hgeukrmo")
    suspend fun getNews(): NewsApiJSON
}