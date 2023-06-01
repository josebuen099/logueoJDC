package com.example.logueojdc


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
/*Autor: Jose Esteban Bueno Sierra
Fecha:23/05/2023
Comentario:se realiza clase biblioteca virtual la cual contiene un webview que hace la redireccion a la pagina de la juan de
castellanos al apartado de biblioteca
*/class BibliotecaVirtual : AppCompatActivity() {
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