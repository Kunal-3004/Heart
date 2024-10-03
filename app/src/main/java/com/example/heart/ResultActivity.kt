package com.example.heart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.heart.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra("result") ?: "No result"
        val sex = intent.getIntExtra("sex", -1)
        val age = intent.getIntExtra("age", -1)
        val maxHeartRate = intent.getIntExtra("hrv", -1)
        val smoking = intent.getIntExtra("Smoking", -1)
        val exang = intent.getIntExtra("Exang", -1)
        val stroke = intent.getIntExtra("Stroke", -1)
        val diabetes = intent.getIntExtra("Diabetes", -1)
        val bpMedication = intent.getIntExtra("bpMedication", -1)
        val bodyMassIndex = intent.getFloatExtra("bodyMassIndex", -1f)

        // Set the data to the views
        binding.edtRes.text = "Prediction Result: $result"
        binding.edtSex.text = "Sex: $sex"
        binding.edtAge.text = "Age: $age"
        binding.edtMaxHr.text = "Max Heart Rate: $maxHeartRate"
        binding.edtCurrSmoker.text = "Current Smoker: $smoking"
        binding.edtExng.text = "Exercise Induced Angina: $exang"
        binding.edtStroke.text = "Stroke: $stroke"
        binding.edtDiab.text = "Diabetes: $diabetes"
        binding.edtBpm.text = "Blood Pressure Medication: $bpMedication"
        binding.edtBmi.text = "Body Mass Index: $bodyMassIndex"
    }
}
