package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var curencyDetails: CurrencyDetails? = null
    lateinit var editText:EditText
    lateinit var tvResult:TextView

    //for spinner selection
    var spSelect: Int = 0
    val currencyList = arrayListOf("SAR", "AUD", "CNY", "INR", "USD", "JPY")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setup the spinner
        spinnerSetup()
        val dateText=findViewById<TextView>(R.id.tvDate)
        editText = findViewById(R.id.etEnter)
        val convertBT= findViewById<Button>(R.id.btConvert)

        //setup the api
        getApiResult()

        convertBT.setOnClickListener{
            if(editText.text.isNotEmpty()) {
                val curr = editText.text.toString().toDouble()
                Log.d("KLLL", dateText.text.toString())
                dateText.text = curencyDetails?.date.toString()
                convertCurrency(spSelect,curr)
            }else
            Toast.makeText(this,"Enter a value for EURO to convert", Toast.LENGTH_SHORT).show()
        }
    }
    private fun convertCurrency(position: Int, curr: Double) {
        tvResult=findViewById(R.id.tvResult)
        when(position) {
            0 -> {
                tvResult.text="Result: "+ calculateExchange(curr,curencyDetails?.eur?.sar?.toDouble()).toString()
            }
            1 -> {
                tvResult.text="Result: "+  calculateExchange(curr,curencyDetails?.eur?.aud?.toDouble()).toString()
            }
            2 ->{
                tvResult.text="Result: "+  calculateExchange(curr,curencyDetails?.eur?.cny?.toDouble()).toString()
            }
            3 -> {
                tvResult.text="Result: "+  calculateExchange(curr,curencyDetails?.eur?.inr?.toDouble()).toString()
            }
            4 -> {
                tvResult.text="Result: "+  calculateExchange(curr,curencyDetails?.eur?.usd?.toDouble()).toString()
            }

            5 -> {
                tvResult.text="Result: "+  calculateExchange(curr,curencyDetails?.eur?.jpy?.toDouble()).toString()
            }
        }
    }
    fun calculateExchange(amount: Double, exchangeRate: Double?): Double{

        val result= amount* exchangeRate!!
        return result
    }

    private fun spinnerSetup() {
        val spinner: Spinner = findViewById(R.id.spinner)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currencyList
        )

            spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Select a Currency", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spSelect = position
            }

        }
    }

    private fun getApiResult(){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        val call: Call<CurrencyDetails?>? = apiInterface!!.getFromJson()
            call?.enqueue(object : Callback<CurrencyDetails?> {
            override fun onResponse(
                call: Call<CurrencyDetails?>?,
                response: Response<CurrencyDetails?>
            ) {

                curencyDetails= response.body()
            }

            override fun onFailure(call: Call<CurrencyDetails?>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }

        })
    }
}