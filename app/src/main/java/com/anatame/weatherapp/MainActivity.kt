package com.anatame.weatherapp

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallApiLoginAsyncTask().execute()
    }

    private inner class CallApiLoginAsyncTask : AsyncTask<Any, Void, String>(){
        private lateinit var customProgressDialog: Dialog

        override fun doInBackground(vararg params: Any?): String {
            var result: String

            var connection: HttpURLConnection? = null
            try{
                val url = URL("https://run.mocky.io/v3/075d140a-dce9-406f-bd95-786a82aa5327")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true

                val httpResult: Int = connection.responseCode
                if(httpResult == 200){
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    try {
                        while(reader.readLine().also{ line = it} != null){
                            stringBuilder.append(line + "\n")
                        }
                    } catch (e: IOException){
                        e.printStackTrace()
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: IOException){
                            e.printStackTrace()
                        }
                    }

                    result = stringBuilder.toString()
                } else {
                    result = connection.responseMessage
                }

            }catch (e: IOException){
                result = "Connection Timeout"

            } finally {
                connection?.disconnect()
            }

            return result;

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.i("Response result", result!! )
        }

    };
}