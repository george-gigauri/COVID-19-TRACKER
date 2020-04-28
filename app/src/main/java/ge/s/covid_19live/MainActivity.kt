package ge.s.covid_19live

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val url: String = "https://api.covid19api.com/summary"
    private var countries : ArrayList<String> = ArrayList()
    private var response: String = ""
    private lateinit var progress : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = ProgressDialog(this)

        progress.setMessage("Retrieving Data...")
        progress.show()
        volley()
        spinner_select_country.setSelection(80)


        spinner_select_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //volleyFun("Georgia")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                volleyFun(parent?.selectedItem.toString())
            }

        }

        refresh.setOnClickListener {
            recreate()
        }

        srulad.setOnClickListener {
            startActivity(Intent(this, ActivityGlobal::class.java))
        }
    }

    private fun volley()
    {
        val strReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                countries = getCountries(response)
                setGlobalParams(response)

                spinner_select_country.adapter = ArrayAdapter<String>(applicationContext,
                    R.layout.spinner_item, countries)

                spinner_select_country.setSelection(80)

                progress.dismiss()
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                progress.dismiss()
            })

        val reqQueue = Volley.newRequestQueue(this)
        reqQueue.add(strReq)
    }

    private fun volleyFun(country : String)
    {
        progress.show()
            val strReq = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    setCountryParams(response, country)
                    progress.dismiss()
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    progress.dismiss()
                })

            val reqQueue = Volley.newRequestQueue(this)
            reqQueue.add(strReq)
    }

    @SuppressLint("SetTextI18n")
    private fun setGlobalParams(response: String) {
        val arr = JSONObject(response).get("Global")
        val obj = JSONObject(arr.toString())

        val totalConfirmed = obj.getString("TotalConfirmed")
        val newConfirmed = obj.getString("NewConfirmed")
        val newDeaths = obj.getString("NewDeaths")
        val totalDeaths = obj.getString("TotalDeaths")
        val newRecovered = obj.getString("NewRecovered")
        val totalRecovered = obj.getString("TotalRecovered")

        global_total.text = NumberFormat.getNumberInstance(Locale.US).format(totalConfirmed.toInt())
        global_new.text = "+$newConfirmed"

        global_deaths.text = NumberFormat.getNumberInstance(Locale.US).format(totalDeaths.toInt())
        global_deaths_new.text = "+$newDeaths"

        global_recovered.text =
            NumberFormat.getNumberInstance(Locale.US).format(totalRecovered.toInt())
        global_recovered_new.text = "+$newRecovered"
    }

    @SuppressLint("SetTextI18n")
    private fun setCountryParams(response: String, countryCode: String) {
        val arr: JSONArray = JSONObject(response).getJSONArray("Countries")

        if (countryCode.isNotEmpty() || countryCode != "") {
            for (i in 0..arr.length()) {
                if (countryCode == arr.getJSONObject(i).getString("Country")) {
                    //country_title.text = countryCode

                    val obj = JSONObject(arr[i].toString())

                    val totalConfirmed = obj.getString("TotalConfirmed")
                    val newConfirmed = obj.getString("NewConfirmed")
                    val newDeaths = obj.getString("NewDeaths")
                    val totalDeaths = obj.getString("TotalDeaths")
                    val newRecovered = obj.getString("NewRecovered")
                    val totalRecovered = obj.getString("TotalRecovered")

                    country_total.text =
                        NumberFormat.getNumberInstance(Locale.US).format(totalConfirmed.toInt())
                    country_new.text = "+$newConfirmed"

                    country_total_deaths.text =
                        NumberFormat.getNumberInstance(Locale.US).format(totalDeaths.toInt())
                    country_new_deaths.text = "+$newDeaths"

                    country_total_recovered.text =
                        NumberFormat.getNumberInstance(Locale.US).format(totalRecovered.toInt())
                    country_new_recovered.text = "+$newRecovered"

                    break
                }
            }
        }
    }

    private fun getCountries(response: String) : ArrayList<String>
    {
        val arrayList : ArrayList<String> = ArrayList()
        val arr: JSONArray = JSONObject(response).getJSONArray("Countries")

        if(arr.length() >= 0)
        {
            for(i in 0 until arr.length())
            {
                val country : String = arr.getJSONObject(i).getString("Country")
                arrayList.add(country)
            }
        }
        return arrayList
    }
}
