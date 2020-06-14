package com.example.weatherforecast.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.ClientError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.ConnectionManager
import org.json.JSONException
import org.w3c.dom.Text

class WeatherFragment : Fragment() {

    lateinit var txtLabel : TextView

    lateinit var relativeWeatherLayout : RelativeLayout
    lateinit var etCity : EditText
    lateinit var etState : EditText
    lateinit var etCountry : EditText
    lateinit var btnSubmit : Button
    lateinit var relativeProgressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar

    lateinit var relativeAnswerLayout : RelativeLayout
    lateinit var txtCity : TextView
    lateinit var txtAnswerTempCel : TextView
    lateinit var txtAnswerTempFar : TextView
    lateinit var txtAnswerHumanTempCel : TextView
    lateinit var txtAnswerHumanTempFar : TextView
    lateinit var txtAnswerMinTempCel : TextView
    lateinit var txtAnswerMinTempFar : TextView
    lateinit var txtAnswerMaxTempCel : TextView
    lateinit var txtAnswerMaxTempFar : TextView
    lateinit var txtHum :TextView
    lateinit var txtAnsHum : TextView
    lateinit var txtPressure : TextView
    lateinit var txtAnsPressurer : TextView
    lateinit var txtAnsWindSpeedKmh : TextView
    lateinit var txtAnsWindSpeedMph : TextView
    lateinit var txtAnsWindDirection : TextView
    lateinit var txtAnsCloud : TextView
    lateinit var btnGoBack : Button

    lateinit var url : String

    val apiKey = "760a3ffbcf127429d5f7eca65262b470"
    val windDirArray = arrayListOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

       txtLabel = view.findViewById(R.id.txtLabel)

       relativeWeatherLayout = view.findViewById(R.id.relativeWeatherLayout)
       etCity = view.findViewById(R.id.etCity)
       etState = view.findViewById(R.id.etState)
       etCountry = view.findViewById(R.id.etCountry)
       btnSubmit = view.findViewById(R.id.btnSubmit)
       progressBar = view.findViewById(R.id.progressBar)
       relativeProgressLayout = view.findViewById(R.id.relativeProgressLayout)

       relativeAnswerLayout = view.findViewById(R.id.relativeAnswerLayout)
       txtCity = view.findViewById(R.id.txtCity)
       txtAnswerTempCel = view.findViewById(R.id.txtAnsTempCel)
       txtAnswerTempFar = view.findViewById(R.id.txtAnsTempFar)
       txtAnswerHumanTempCel = view.findViewById(R.id.txtAnsHumanTempCel)
       txtAnswerHumanTempFar = view.findViewById(R.id.txtAnsHumanTempFar)
       txtAnswerMinTempCel = view.findViewById(R.id.txtAnsMinTempCel)
       txtAnswerMinTempFar = view.findViewById(R.id.txtAnsMinTempFar)
       txtAnswerMaxTempCel = view.findViewById(R.id.txtAnsMaxTempCel)
       txtAnswerMaxTempFar = view.findViewById(R.id.txtAnsMaxTempFar)
       txtHum = view.findViewById(R.id.txtHum)
       txtAnsHum = view.findViewById(R.id.txtAnsHum)
       txtPressure = view.findViewById(R.id.txtPressure)
       txtAnsPressurer = view.findViewById(R.id.txtAnsPressure)
       txtAnsWindSpeedKmh = view.findViewById(R.id.txtAnsWindSpeedKmh)
       txtAnsWindSpeedMph = view.findViewById(R.id.txtAnsWindSpeedMph)
       txtAnsWindDirection = view.findViewById(R.id.txtAnsWindDirection)
       txtAnsCloud = view.findViewById(R.id.txtAnsCloud)
       btnGoBack = view.findViewById(R.id.btnGoBack)

       if(ConnectionManager().checkConnectivity(activity as Context)){
           relativeWeatherLayout.visibility = View.VISIBLE
           relativeAnswerLayout.visibility = View.GONE

           btnSubmit.setOnClickListener {
               val city = etCity.text.toString()
               val state = etState.text.toString()
               val country = etCountry.text.toString()

               if (city.isNullOrEmpty()){
                   etCity.error = "City is Mandatory"
                   etCity.requestFocus()
                   return@setOnClickListener
               }

               if (city.isNotEmpty() && state.isNotEmpty() && country.isNotEmpty()){
                   url = "https://api.openweathermap.org/data/2.5/weather?q=$city,$state,$country&appid=$apiKey"
               }

               else if (city.isNotEmpty() && state.isNotEmpty() && country.isNullOrEmpty()){
                   url = "https://api.openweathermap.org/data/2.5/weather?q=$city,$state&appid=$apiKey"
               }

               else if (city.isNotEmpty() && state.isNullOrEmpty() && country.isNullOrEmpty()){
                   url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"
               }

               weather(url,city)

           }

           btnGoBack.setOnClickListener {
               relativeWeatherLayout.visibility = View.VISIBLE
               relativeAnswerLayout.visibility = View.GONE
               txtLabel.visibility = View.VISIBLE
           }

       }
       else{
           val dialog = AlertDialog.Builder(activity as Context)
           dialog.setTitle("Fail")
           dialog.setMessage("Internet is Not Active")
           dialog.setPositiveButton("OK"){_,_ ->

           }
           dialog.setNegativeButton("Cancel"){_,_ ->

           }
           dialog.create()
           dialog.show()
       }

        return view
    }

    private fun weather(url: String, city : String) {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = url


        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            Response.Listener {

                try{
                    relativeWeatherLayout.visibility = View.GONE
                    relativeAnswerLayout.visibility = View.VISIBLE
                    txtLabel.visibility = View.GONE

                    txtCity.text = "Weather Forecast for $city"

                    val cod = it.getInt("cod")
                    if (cod != 200){
                        println("City Not Found")
                    }
                    else{
                        val main = it.getJSONObject("main")
                        val temp = main.getInt("temp")
                        val human_feel = main.getInt("feels_like")
                        val temp_min = main.getInt("temp_min")
                        val temp_max = main.getInt("temp_max")
                        val humidity = main.getInt("humidity")
                        val pressure = main.getInt("pressure")
                        val wind = it.getJSONObject("wind")
                        val speed = wind.getInt("speed")
                        val winddir = wind.getInt("deg")
                        val clouds = it.getJSONObject("clouds")
                        val cloud = clouds.getInt("all")

                        txtAnswerTempCel.text = "${String.format("%.2f",celsius(temp))} °C"
                        txtAnswerTempFar.text = "${String.format("%.2f",fahrenheit(temp))} F"
                        txtAnswerHumanTempCel.text = "${String.format("%.2f",celsius(human_feel))} °C"
                        txtAnswerHumanTempFar.text = "${String.format("%.2f",fahrenheit(human_feel))} F"
                        txtAnswerMinTempCel.text = "${String.format("%.2f",celsius(temp_min))} °C"
                        txtAnswerMinTempFar.text = "${String.format("%.2f",fahrenheit(temp_min))} F"
                        txtAnswerMaxTempCel.text = "${String.format("%.2f",celsius(temp_max))} °C"
                        txtAnswerMaxTempFar.text = "${String.format("%.2f",fahrenheit(temp_max))} F"
                        txtAnsHum.text = "$humidity %"
                        txtAnsPressurer.text = "$pressure Pa"
                        txtAnsWindSpeedKmh.text = "${String.format("%.2f",kmph(speed))} KMPH"
                        txtAnsWindSpeedMph.text = "${String.format("%.2f",mph(speed))} MPH"
                        txtAnsWindDirection.text = "${windDir(winddir)}"
                        txtAnsCloud.text = "$cloud %"
                    }


                }
                catch (e: JSONException){
                    println("JSON Error is $e")
                }
            },
            Response.ErrorListener {

                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Fail")
                dialog.setMessage("Some Error Occurred")
                dialog.setPositiveButton("OK"){_,_ ->

                }
                dialog.setNegativeButton("Cancel"){_,_ ->

                }
                dialog.create()
                dialog.show()
            }
        ){
            override fun getHeaders() : MutableMap<String,String>{
                val headers = HashMap<String,String>()
                headers["Content-type"] = "application/json"
                return headers
            }
        }

        queue.add(jsonObjectRequest)
    }

    fun fahrenheit(number : Int) : Double{
        return ((number - 273.15) * 9 / 5 + 32)
    }

    fun celsius(number : Int) : Double{
        return (number - 273.15)
    }

    fun kmph(speed : Int): Double{
        return speed*3.6
    }

    fun mph(speed: Int) : Double {
        return speed * 2.237
    }

    fun windDir(winddir : Int ) : String{
        val dir = 360/windDirArray.size
        val funmath = ((winddir+(dir/2))/dir)

        return windDirArray[funmath % windDirArray.size]
    }
}