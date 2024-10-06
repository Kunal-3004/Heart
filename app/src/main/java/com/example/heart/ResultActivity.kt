package com.example.heart

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.heart.databinding.ActivityResultBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val height = intent.getFloatExtra("height", 0f)
        val weight = intent.getFloatExtra("weight", 0f)
        val sex = intent.getIntExtra("sex", 0)
        val age = intent.getIntExtra("age", 0)
        val smoker = intent.getIntExtra("smoker", 0)
        val chestPainType = intent.getIntExtra("chestPainType", 0)
        val exang = intent.getIntExtra("exang", 0)
        val diabetes = intent.getIntExtra("diabetes", 0)
        val bpMedication = intent.getIntExtra("bpMedication", 0)
        val heartRate = intent.getIntExtra("heartRate", 0)
        val result = intent.getStringExtra("result")


        if (weight > 0 && height > 0) {
            val bmi = ((weight * 100 * 100) / (height * height))
            val formattedBmi = String.format("%.2f", bmi)
            binding.edtBmi.setText(formattedBmi)
        }
        binding.edtAge.setText(age.toString())
        binding.edtMaxHr.setText(heartRate.toString())
        binding.edtRes.setText(result)
        binding.edtSex.setText(if (sex == 0) "Male" else "Female")
        binding.edtCurrSmoker.setText(if (smoker == 0) "No" else "Yes")
        binding.edtExng.setText(if (exang == 0) "No" else "Yes")
        binding.edtDiab.setText(if (diabetes == 0) "No" else "Yes")
        binding.edtBpm.setText(if (bpMedication == 0) "No" else "Yes")

        if (result.equals("no need to worry", ignoreCase = true)) {
            binding.edtRes.setTextColor(Color.GREEN)
        } else {
            binding.edtRes.setTextColor(Color.RED)
        }

        when (chestPainType) {
            0 -> binding.edtCpt.setText("Typical Angina")
            1 -> binding.edtCpt.setText("Atypical Angina")
            2 -> binding.edtCpt.setText("Non-Anginal Pain")
            3 -> binding.edtCpt.setText("Asymptomatic")
            else -> binding.edtCpt.setText("Unknown")
        }
    }
}


       /* binding.btnPdf.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                generatePdf()
            }
        }
    }

    private fun generatePdf() {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val content = binding.root
        content.draw(page.canvas)

        pdfDocument.finishPage(page)

        val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ResultReport.pdf")
        try {
            pdfFile.createNewFile()
            val fos = FileOutputStream(pdfFile)
            pdfDocument.writeTo(fos)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            generatePdf()
        }
    }
}*/
