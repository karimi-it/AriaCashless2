package com.mcac.ariacashless

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mcac.ariacashless.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val publicKeyF = "fam-public-key.pem"
//        val sr = InputStreamReader(assets.open(publicKeyF))
//        val br = BufferedReader(sr)
//        val sb = StringBuilder()
//        var s: String?
//        while (br.readLine().also { s = it } != null) {
//            sb.append(s)
//        }
//
//        var result = EncryptionUtil.encByPublicKey("1234",sb.toString())
//        br.close()
//        sr.close()
        //setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
    }


}