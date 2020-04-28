package ge.s.covid_19live

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_global.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class ActivityGlobal : AppCompatActivity() {
    private val url = "https://api.covid19api.com/summary"
    private val arr :ArrayList<CoronaItem>  = ArrayList()
    private lateinit var progress : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global)

        progress = ProgressDialog(this)
        progress.setMessage("Please wait...")
        progress.show()

        val strReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                setData(response)

                Toast.makeText(this, arr.size.toString(), Toast.LENGTH_LONG).show()
                val adapter = CoronaAdapter(this, arr)
                list.adapter = adapter

                progress.dismiss()
            },
            Response.ErrorListener {
                progress.dismiss()
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })

        progress.dismiss()
        val reqQueue = Volley.newRequestQueue(this)
        reqQueue.add(strReq)


    }

    private fun setData(response:String)
    {
        progress.show()
        val arr: JSONArray = JSONObject(response).getJSONArray("Countries")

        for(i in 0 until arr.length())
        {
            val j = CoronaItem()
            val obj = JSONObject(arr[i].toString())

            j.country = obj.getString("Country")
            j.newConfirmed = obj.getString("NewConfirmed")
            j.newDeaths = obj.getString("NewDeaths")
            j.newRecovered = obj.getString("NewRecovered")
            j.totalConfirmed = obj.getString("TotalRecovered")
            j.totalDeaths = obj.getString("TotalDeaths")
            j.totalRecovered = obj.getString("TotalRecovered")

            this.arr.add(j)
        }
        progress.dismiss()
    }
}
