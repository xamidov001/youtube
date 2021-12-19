package uz.pdp.youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.google.android.gms.ads.MobileAds
import uz.pdp.youtube.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}


    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_support_nav).navigateUp()
    }
}