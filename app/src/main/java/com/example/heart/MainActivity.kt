package com.example.heart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.heart.BACKENED.Api
import com.example.heart.BACKENED.HeartDiseaseRequest
import com.example.heart.BACKENED.PredictionResponse
import com.example.heart.BACKENED.RetrofitInstance
import com.example.heart.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitInstance.api

        val sexOptions = arrayOf("Male", "Female")
        val smokerOptions = arrayOf("No", "Yes")
        val chestPainType= arrayOf("Typical Angina","Atypical Angina","Non-Anginal Pain","Asymptomatic")
        val exangOptions = arrayOf("No", "Yes")
        val diabetesOptions = arrayOf("No", "Yes")
        val bpOptions = arrayOf("No", "Yes")
        val ca= arrayOf("No vessel colored","1 vessel colored","2 vessel colored","3 vessel colored","4 vessel colored")
        val thal= arrayOf("Normal","Fixed Defect","Reversible Defect","No evidence")

        val sexAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexOptions)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = sexAdapter

        val smokerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, smokerOptions)
        smokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSmoker.adapter = smokerAdapter

        val cpAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, chestPainType)
        smokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCpt.adapter = cpAdapter

        val caAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ca)
        smokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCa.adapter = caAdapter

        val thalAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, thal)
        smokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerThal.adapter = thalAdapter

        val exangAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exangOptions)
        exangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHype.adapter = exangAdapter

        val diabetesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diabetesOptions)
        diabetesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDia.adapter = diabetesAdapter

        val bpAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bpOptions)
        bpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBp.adapter = bpAdapter

        binding.btnPredict.setOnClickListener {
            if(check()){
                submitPredictionRequest()
            }
        }
    }

    private fun check(): Boolean {
        val oldpeak=binding.edtOlp
        val age=binding.edtAge
        val height=binding.edtHeight
        val weight=binding.edtWeight
        val heartRate=binding.edtHr

        if (oldpeak.text.toString().isEmpty() || oldpeak.text.toString().toFloat() < 0) {
            oldpeak.error = "Cannot be empty or negative"
            return false
        }
        if (age.text.toString().isEmpty() || age.text.toString().toInt() <= 0) {
            age.error = "Age cannot be empty or zero"
            return false
        }
        if (height.text.toString().isEmpty() || height.text.toString().toFloat() <= 0) {
            height.error = "Height cannot be empty or zero"
            return false
        }
        if (weight.text.toString().isEmpty() || weight.text.toString().toFloat() <= 0) {
            weight.error = "Weight cannot be empty or zero"
            return false
        }
        if (heartRate.text.toString().isEmpty() || heartRate.text.toString().toFloat() <= 0) {
            heartRate.error = "Heart Rate cannot be empty or Zero "
            return false
        }
        return true
    }

    private fun submitPredictionRequest() {
        val sex = binding.mySpinner.selectedItem.toString()
        val age = binding.edtAge.text.toString().toIntOrNull() ?: 0
        val currentSmoker = binding.spinnerSmoker.selectedItemPosition
        val chestPainType = binding.spinnerCpt.selectedItemPosition
        val exang = binding.spinnerHype.selectedItemPosition
        val diabetes = binding.spinnerDia.selectedItemPosition
        val bpMedication = binding.spinnerBp.selectedItemPosition
        val height = binding.edtHeight.text.toString().toFloatOrNull()
        val weight = binding.edtWeight.text.toString().toFloatOrNull()
        val heartRate = binding.edtHr.text.toString().toIntOrNull()
        val oldPeak = binding.edtOlp.text.toString().toFloatOrNull()
        val thalassemia = binding.spinnerThal.selectedItemPosition
        val ca=binding.spinnerCa.selectedItemPosition

        if (height == null || height <= 0) {
            showToast("Height must be a positive number.")
            return
        }
        if (weight == null || weight <= 0) {
            showToast("Weight must be a positive number.")
            return
        }
        if (heartRate == null || heartRate <= 0) {
            showToast("Heart Rate must be a positive number.")
            return
        }
        if (oldPeak == null || oldPeak < 0) {
            showToast("Old Peak must be a non-negative number.")
            return
        }

        val request = HeartDiseaseRequest(
            chestPainType = chestPainType,
            heartRate = heartRate,
            exang = exang,
            oldPeak = oldPeak,
            ca = ca,
            thalassemia = thalassemia
        )

        apiService.getPrediction(request).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                Log.d("API_REQUEST", "Request data: chestPainType=$chestPainType, heartRate=$heartRate, exang=$exang, oldPeak=$oldPeak, ca=$ca, thalassemia=$thalassemia")
                if (response.isSuccessful) {
                    val predictionResponse = response.body()
                    if (predictionResponse != null) {
                        Log.d("PredictionResult", "Received result: ${predictionResponse.prediction}")
                        val intent = Intent(this@MainActivity, ResultActivity::class.java).apply {
                            putExtra("result", predictionResponse.prediction)
                            putExtra("sex", sex)
                            putExtra("age", age)
                            putExtra("chestPainType", chestPainType)
                            putExtra("heartRate", heartRate)
                            putExtra("smoker", currentSmoker)
                            putExtra("exang", exang)
                            putExtra("diabetes", diabetes)
                            putExtra("bpMedication", bpMedication)
                            putExtra("height", height)
                            putExtra("weight", weight)
                            putExtra("oldPeak", oldPeak)
                            putExtra("ca", ca)
                            putExtra("thalassemia", thalassemia)
                        }

                        startActivity(intent)
                    } else {
                        showToast("Prediction response is null")
                    }
                } else {
                    if (response.code() == 500) {
                        Log.e("PredictionError", "Internal Server Error: ${response.message()}")
                        showToast("Internal Server Error: Please try again later.")
                    } else {
                        Log.e("PredictionError", "Code: ${response.code()}, Message: ${response.message()}")
                        showToast("Failed to get prediction: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
