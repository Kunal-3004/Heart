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

        intent?.let {
            binding.edtSex.text = it.getStringExtra("SEX")
            binding.edtAge.text = it.getStringExtra("AGE")
            binding.edtCurrSmoker.text = it.getStringExtra("Current Smoker")
            binding.edtStroke.text = it.getStringExtra("Chest Pain Type")
            binding.edtExng.text = it.getStringExtra("Exang")
            binding.edtDiab.text = it.getStringExtra("Diabetes")
            binding.edtBpm.text = it.getStringExtra("BP Medication")
            binding.edtBmi.text = it.getStringExtra("BMI")
            binding.edtMaxHr.text = it.getStringExtra("Heart Rate")
            binding.edtRes.text = it.getStringExtra("Result")
        }

        /*binding.btnPdf.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
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
        }*/
    }
}
