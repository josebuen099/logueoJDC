package com.example.logueojdc

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button

class BibliotecaVirtual : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblioteca_virtual)
        //variable para llamar el web view desde el layout
        val myWebView: WebView = findViewById(R.id.jdcbiblioteca)
        // Habilitar JavaScript si es necesario
        myWebView.getSettings().setJavaScriptEnabled(true);
        // cargar la Url de la biblioteca... problema para cargar JSP
        myWebView.loadUrl("https://www.jdc.edu.co/biblioteca")
    }
}