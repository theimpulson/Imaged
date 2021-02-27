package dev.theimpulson.imaged

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.theimpulson.imaged.backend.ImageManagement
import dev.theimpulson.imaged.backend.NetworkConnection
import dev.theimpulson.imaged.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ImageManagement.loadLastImage(binding.imageView, this)

        binding.button.setOnClickListener {
            if (!NetworkConnection.hasNetworkConnection(this)) {
                Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show()
            } else {
                GlobalScope.launch {
                    ImageManagement.loadAndSaveImage(binding.imageView, binding.progressBar, this@MainActivity)
                }
            }
        }
    }
}