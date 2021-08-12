package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.api.New
import com.example.newsapp.api.NewsApiJSON
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


const val BASE_URL = "https://api.currentsapi.services"
class MainActivity : AppCompatActivity() {


    lateinit var countDownTimer: CountDownTimer

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeAPIRequest()
    }


    private fun fadeInFromBlack() {
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun setUpRecylerView() {
        rv_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList,descList,imagesList,linksList)

    }
    private fun addToList(title: String, description: String, image: String, link: String) {
        linksList.add(link)
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
    }


    private fun makeAPIRequest() {
        progressBar.visibility = View.VISIBLE
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create( APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response: NewsApiJSON = api.getNews()

                for (article:New in response.news) {
                    Log.i("MainActivity ", "result = $article")
                    addToList(article.title,article.description,article.image,article.url)
                }
                withContext(Dispatchers.Main){
                    setUpRecylerView()
                    fadeInFromBlack()
                    progressBar.visibility = View.GONE

                }
            }catch (e: Exception) {
                Log.e("MainActivity",  e.toString() )
                withContext(Dispatchers.Main) {
                    attemptRequestAgain()
                }
            }

        }
    }

    private fun attemptRequestAgain() {
         countDownTimer = object: CountDownTimer(5*1000,1000){
             override fun onTick(p0: Long) {
                 makeAPIRequest()
                 countDownTimer.cancel()
             }

             override fun onFinish() {
                 Log.i("Mainactivity", "internet ishlamayati  ")
                 Toast.makeText(this@MainActivity, "internet iwlamaypti", Toast.LENGTH_SHORT).show()
             }

         }
        countDownTimer.start()
    }
}