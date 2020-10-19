package williamlopes.project.rtcontrol.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.databinding.ActivityDetailBinding

class DetailActivity :  AppCompatActivity() {
    private lateinit var dataBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        IntentIntegrator(this).initiateScan()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         val result: IntentResult? = IntentIntegrator.parseActivityResult(
             requestCode,
             resultCode,
             data
         )
        if(result != null) {
            if(result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                dataBinding.webview.settings.javaScriptEnabled = true
                dataBinding.webview.loadUrl(result.contents)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}