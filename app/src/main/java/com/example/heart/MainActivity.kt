package com.example.heart

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

        apiService = Retrofit.api

        val sexOptions = arrayOf("Male", "Female")
        val smokerOptions = arrayOf("No", "Yes")
        val strokeOptions = arrayOf("No", "Yes")
        val exangOptions = arrayOf("No", "Yes")
        val diabetesOptions = arrayOf("No", "Yes")
        val bpOptions = arrayOf("No", "Yes")

        val sexAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexOptions)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = sexAdapter

        val smokerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, smokerOptions)
        smokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSmoker.adapter = smokerAdapter

        val strokeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strokeOptions)
        strokeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStroke.adapter = strokeAdapter

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
        val cp = binding.edtCigs
        val thalach = binding.edtHr
        val oldpeak = binding.edtOldpeak
        val ca = binding.edtCa
        val age = binding.edtAge
        val height = binding.edtHeight
        val weight = binding.edtWeight
        val thal = binding.edtThal

        if (cp.text.toString().isEmpty() || !(cp.text.toString() in arrayOf("0", "1", "2", "3"))) {
            cp.error = "Should be in 0-3 range"
            return false
        }
        if (thalach.text.toString().isEmpty() || thalach.text.toString().toInt() < 0) {
            thalach.error = "Cannot be empty or negative"
            return false
        }
        if (oldpeak.text.toString().isEmpty() || oldpeak.text.toString().toFloat() < 0) {
            oldpeak.error = "Cannot be empty or negative"
            return false
        }
        if (ca.text.toString().isEmpty() || !(ca.text.toString() in arrayOf("0", "1", "2", "3", "4"))) {
            ca.error = "Should be in 0-4 range"
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
        if (thal.text.toString().isEmpty() || !(thal.text.toString() in arrayOf("0", "1", "2", "3"))) {
            thal.error = "Should be in 0-3 range "
            return false
        }
        return true
    }

    private fun submitPredictionRequest() {
        val sex = binding.mySpinner.selectedItemPosition
        val age = binding.edtAge.text.toString().toIntOrNull() ?: 0
        val currentSmoker = binding.spinnerSmoker.selectedItemPosition
        val chestPainType = binding.edtCigs.text.toString().toIntOrNull() ?: 0
        val stroke = binding.spinnerStroke.selectedItemPosition
        val exang = binding.spinnerHype.selectedItemPosition
        val diabetes = binding.spinnerDia.selectedItemPosition
        val bpMedication = binding.spinnerBp.selectedItemPosition
        val height = binding.edtHeight.text.toString().toFloatOrNull() ?: 0f
        val weight = binding.edtWeight.text.toString().toFloatOrNull() ?: 0f
        val heartRate = binding.edtHr.text.toString().toIntOrNull() ?: 0
        val oldPeak = binding.edtOldpeak.text.toString().toFloatOrNull() ?: 0f
        val thalassemia = binding.edtThal.text.toString().toIntOrNull() ?: 0
        val ca = binding.edtCa.text.toString().toIntOrNull() ?: 0

        val request = HeartDiseaseRequest(
            SEX = sex,
            age = age,
            currentSmoker,
            chestPainType,
            Stroke = stroke,
            bpMedication,
            Diabetes = diabetes,
            Height = height,
            Weight = weight,
            heartRate,
            Exang = exang,
            Oldpeak = oldPeak,
            ca = ca,
            thalassemia = thalassemia
        )

        apiService.getPrediction(request).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val predictionResponse = response.body()
                    if (predictionResponse != null) {
                        val intent = Intent(this@MainActivity, ResultActivity::class.java)
                        intent.putExtra("result", predictionResponse.result)
                        intent.putExtra("sex", predictionResponse.sex)
                        intent.putExtra("age", predictionResponse.age)
                        intent.putExtra("hrv", predictionResponse.maxHeartRate)
                        intent.putExtra("Smoking",predictionResponse.currentSmoker)
                        intent.putExtra("Exang",predictionResponse.exang)
                        intent.putExtra("Stroke",predictionResponse.stroke)
                        intent.putExtra("Diabetes",predictionResponse.diabetes)
                        intent.putExtra("bpMedication",predictionResponse.bpMedication)
                        intent.putExtra("bodyMassIndex",predictionResponse.bmi)
                        intent.putExtra("maxHeartRate",predictionResponse.maxHeartRate)
                        startActivity(intent)
                    } else {
                        showToast("Prediction response is null")
                    }
                } else {
                    showToast("Failed to get prediction: ${response.message()}")
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
