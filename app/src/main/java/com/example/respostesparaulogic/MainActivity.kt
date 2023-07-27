package com.example.respostesparaulogic

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var strOutput = "https://vilaweb.cat/paraulogic/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lezzgo()
    }

    suspend fun displaySolution(){
    }

    fun changeText(){
        val textView: TextView = findViewById(R.id.textView)
        //println(dicc)
        textView.setText(strOutput)
    }

    fun lezzgo() = runBlocking<Unit> {
        lifecycleScope.launch(Dispatchers.IO) {
            strOutput = URL("https://vilaweb.cat/paraulogic/").readText()
            var dicc = Jsoup.parse(strOutput)
            var scriptElements = dicc.body().select("script")
            for(scriptElement in scriptElements) {
                val scriptContent = scriptElement.data()
                if (scriptContent.contains("var y={")) {
                    var today = scriptContent.toString().split("var t={")[1].split("}")[0]
                    val lletres = today.split("[")[1].split("]")[0].split(",")
                    val paraules = today.split("{")[1].split(",")
                    strOutput = "Tutis:\n"
                    var no_tutis = mutableListOf<String>()
                    for (paraula in paraules) {
                        var key = paraula.split(":")[0]
                        var poss_tuti = true
                        for (lletra in lletres) {
                            poss_tuti = poss_tuti && key.contains(lletra.toCharArray()[1])
                        }
                        if (poss_tuti == true) {
                            strOutput = strOutput + key + "\n"
                        } else {
                            no_tutis.add(key)
                        }
                    }
                    strOutput = strOutput + "\nResta:\n"
                    for (no_tuti in no_tutis) {
                        strOutput = strOutput + no_tuti + ", "
                    }
                }
            }
        }.invokeOnCompletion{runOnUiThread { changeText() } }
    }
}

