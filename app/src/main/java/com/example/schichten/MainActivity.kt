package com.example.schichten

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.CalendarView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Kalenderinstanz erzeugen
        val kal = Calendar.getInstance()
        val datum = kal.time
        val lokal = Locale.getDefault()
        val sdfJahr = SimpleDateFormat("yyyy", lokal)
        val sdfMonat = SimpleDateFormat("MM", lokal)
        val sdfTag = SimpleDateFormat("dd", lokal)


        val cvKalender = findViewById<CalendarView>(R.id.cvKalender)
        var jahr:Int = sdfJahr.format(datum).toInt()
        var monat:Int = sdfMonat.format(datum).toInt()
        var tag:Int = sdfTag.format(datum).toInt()
        var wochenTag:Int = kal[Calendar.DAY_OF_WEEK]

        cvKalender?.setOnDateChangeListener { _, year, month, dayOfMonth  ->
            jahr = year
            monat = month
            tag = dayOfMonth
            //Wochentag ermitteln für Heinrich Flato Schichten
            val calendar = Calendar.getInstance()
            calendar[year, month] = dayOfMonth
            //Mo=2,Di=3..Sa=7,So=1 O.o
            wochenTag = calendar[Calendar.DAY_OF_WEEK]

        }

        buEintragen.setOnClickListener {

            //Kalenderintent erzeugen
            val ev = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)

            //Termintitel aus Schicht und Stellwerk erzeugen
            val idSchicht = rgSchicht.checkedRadioButtonId
            val rbSchicht = findViewById<RadioButton>(idSchicht)
            var titel:String = rbSchicht.text as String
            val idStellwerk = rgStellwerk.checkedRadioButtonId
            val rbStellwerk = findViewById<RadioButton>(idStellwerk)
            titel += " " + rbStellwerk.text as String

            //Termintitel an das Intent übergeben
            ev.putExtra(CalendarContract.Events.TITLE, titel)

            //Umbau Stellwerk -> Schicht -> Flatho
            when (rbStellwerk.text as String){
                //Abschnitt Linsburg
                "Linsburg" -> {
                    when (rbSchicht.text as String){
                        "Früh" -> {
                            //Abfrage Linsburg Montag(wochenTag==2) Früh Flatho
                            if (wochenTag == 2){
                                kal.set(jahr, monat, tag, 6, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                kal.set(jahr, monat, tag, 14, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                startActivity(ev)
                            }
                            else {
                                //Beginn der normalen Frühschicht
                                kal.set(jahr, monat, tag, 6, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                kal.set(jahr, monat, tag, 12, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                startActivity(ev)
                            }
                        }
                        "Spät" -> {
                            //Abfrage Flatho ablösen
                            if (wochenTag == 2){
                                kal.set(jahr, monat, tag, 14, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                kal.set(jahr, monat, tag, 20, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                startActivity(ev)
                            }
                            else {
                                //Beginn der normalen Spätschicht
                                kal.set(jahr, monat, tag, 12, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                kal.set(jahr, monat, tag, 20, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                startActivity(ev)
                            }
                        }
                        "Nacht" -> {

                            kal.set(jahr, monat, tag, 20, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            kal.set(jahr, monat, tag + 1, 6, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            startActivity(ev)
                        }
                        "Früh/Nacht" -> {

                            kal.set(jahr, monat, tag, 6, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            kal.set(jahr, monat, tag + 1, 6, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            startActivity(ev)
                        }
                        "Langer Tag" -> {
                            kal.set(jahr, monat, tag, 6, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            kal.set(jahr, monat, tag, 18, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            startActivity(ev)
                        }
                        "Lange Nacht" -> {
                            kal.set(jahr, monat, tag, 18, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            kal.set(jahr, monat, tag + 1, 6, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            startActivity(ev)
                        }
                        else -> Toast.makeText(this, "Irgendwas ging schief", Toast.LENGTH_LONG).show()
                    }
                }
            }

/*
//##################################Alter Quelltext######################################
            //Dörverden extra für andere Zeiten
            if (rbStellwerk.text as String == "Dörverden"){
                when(rbSchicht.text as String) {
                    "Früh" -> {
                        //Beginn der Frühschicht
                        //Montag Frühschicht eine Stunde später
                        if (wochenTag == 2) {
                            kal.set(jahr, monat, tag, 6, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            //Ende der Frühschicht
                            kal.set(jahr, monat, tag, 11, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                            startActivity(ev)
                        }
                        else {
                            kal.set(jahr, monat, tag, 5, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            //Ende der Frühschicht
                            kal.set(jahr, monat, tag, 11, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                            startActivity(ev)
                        }
                    }

                    "Spät" -> {
                        kal.set(jahr, monat, tag, 11, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag, 19, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    "Nacht" -> {

                        kal.set(jahr, monat, tag, 19, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag+1, 5, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    "Früh/Nacht" -> {

                        kal.set(jahr, monat, tag, 5, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag+1, 5, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    "Langer Tag" -> {
                        kal.set(jahr, monat, tag, 5, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag, 17, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    "Lange Nacht" -> {
                        kal.set(jahr, monat, tag, 17, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag+1, 5, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    else -> Toast.makeText(this, "Irgendwas ging schief", Toast.LENGTH_LONG).show()
                }
            }
            else {

                //Schichtbegin und -ende anhand der ausgewählten Schicht ermitteln
                when (rbSchicht.text as String) {
                    "Früh" -> {


                        //Heinrich Flato Schicht für Linsburg
                        if (wochenTag == 2 && rbStellwerk.text as String == "Linsburg"){
                            kal.set(jahr, monat, tag, 6, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            //Ende der Frühschicht
                            kal.set(jahr, monat, tag, 14, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                            startActivity(ev)
                        }
                        else
                            //Heinrich Flato Schicht für Rohrsen
                            if (wochenTag == 5 && rbStellwerk.text as String == "Rohrsen"){
                                kal.set(jahr, monat, tag, 6, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                //Ende der Frühschicht
                                kal.set(jahr, monat, tag, 14, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                                startActivity(ev)
                            }
                            else {
                                //Beginn der Frühschicht
                                kal.set(jahr, monat, tag, 6, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                //Ende der Frühschicht
                                kal.set(jahr, monat, tag, 12, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                                startActivity(ev)
                            }
                    }

                    "Spät" -> {
                        if (wochenTag == 2 && rbStellwerk.text as String == "Linsburg"){
                            kal.set(jahr, monat, tag, 14, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                            //Ende der Frühschicht
                            kal.set(jahr, monat, tag, 20, 30)
                            ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                            //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                            startActivity(ev)
                        }
                        else
                        //Heinrich Flato Schicht für Rohrsen
                            if (wochenTag == 5 && rbStellwerk.text as String == "Rohrsen"){
                                kal.set(jahr, monat, tag, 14, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                //Ende der Frühschicht
                                kal.set(jahr, monat, tag, 20, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                                startActivity(ev)
                            }
                            else {
                                //Beginn der Frühschicht
                                kal.set(jahr, monat, tag, 12, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                                //Ende der Frühschicht
                                kal.set(jahr, monat, tag, 20, 30)
                                ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                                //Kalenderintent ausführen und Daten an den Systemkalender übergeben
                                startActivity(ev)
                            }

                    }

                    "Nacht" -> {

                        kal.set(jahr, monat, tag, 20, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag + 1, 6, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    "Früh/Nacht" -> {

                        kal.set(jahr, monat, tag, 6, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag + 1, 6, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    "Langer Tag" -> {
                        kal.set(jahr, monat, tag, 6, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag, 18, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    "Lange Nacht" -> {
                        kal.set(jahr, monat, tag, 18, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, kal.time.time)

                        kal.set(jahr, monat, tag + 1, 6, 30)
                        ev.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, kal.time.time)

                        startActivity(ev)
                    }

                    else -> Toast.makeText(this, "Irgendwas ging schief", Toast.LENGTH_LONG).show()
                }
            }*/
        }
    }
}