package com.example.practica1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.practica1.API.retrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    private lateinit var  btnDetectLanguage:Button
    private lateinit var  etDescription: EditText
    private lateinit var  progressbar: ProgressBar


    var allLanguages = emptyList<Language>()

    override fun onCreate(savedInstanceState: Bundle?) { //metodo principal
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
        getLanguages()
    }

    private fun initListener() {
        btnDetectLanguage.setOnClickListener {
            val text:String = etDescription.text.toString()
            if(text.isNotEmpty()){//Se realiza una funcion
                showLoading()
                getTextLanguage(text)
            }
        }
    }

    private fun showLoading() {
        progressbar.visibility = View.VISIBLE
    }
    private fun hideLoading() {
        runOnUiThread { //correrlo en el hilo principal
            progressbar.visibility = View.GONE //invisible
        }
    }

    private fun getTextLanguage(text: String) {
        CoroutineScope(Dispatchers.IO).launch { //correr en otro hilo
            val result = retrofitService.getTextLanguage(text)
            if (result.isSuccessful){
                checkResult(result.body())
            }else{
                showError()
            }
            cleanText()
            hideLoading()
        }
    }

    private fun cleanText() {
        etDescription.setText("")
    }

    private fun checkResult(detectionResponse: DetectionResponse?) {
        if(detectionResponse != null && !detectionResponse.data.detections.isNullOrEmpty()){
            val correctLanguage = detectionResponse.data.detections.filter { it.isReliable }
            if(correctLanguage.isNotEmpty()){
                val languageName =allLanguages.find{it.code == correctLanguage.first().language}
                if(languageName != null){
                    runOnUiThread{ // salir al hilo principal
                        Toast.makeText(this, "El idioma es ${languageName.name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getLanguages() {
        CoroutineScope(Dispatchers.IO).launch{ //correr en otro hilo
            val languages:Response<List<Language>>  =retrofitService.getLanguage()
            if(languages.isSuccessful){ //controlar llamada por si algo falla en la peticion
                allLanguages = languages.body() ?: emptyList()
                showSuccess()
            }else{
                showError()
            }
        }
    }

    private fun showSuccess() {
        runOnUiThread { //correrlo en el hilo principal
            Toast.makeText(this,"Petición correcta", Toast.LENGTH_SHORT).show() //mostrar un mensaje flotante
        }
    }

    private fun showError() {
        runOnUiThread { //correrlo en el hilo principal
            Toast.makeText(this,"Error al hacer la llamada", Toast.LENGTH_SHORT).show() //mostrar un mensaje flotante
        }
    }

    private fun initView() {
        btnDetectLanguage = findViewById(R.id.btnDetectLanguage)
        etDescription = findViewById(R.id.etDescription)
        progressbar = findViewById(R.id.progressbar)
    }
}