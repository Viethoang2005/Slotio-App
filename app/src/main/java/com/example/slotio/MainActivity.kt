package com.example.slotio

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.slotio.components.NoInternetScreen
import com.example.slotio.navigation.AppNavGraph
import com.example.slotio.theme.SlotioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isLoading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { isLoading }

        Handler(Looper.getMainLooper()).postDelayed({
            isLoading = false
        }, 2000)

        enableEdgeToEdge()
        setContent {
            SlotioTheme {
                val window = this.window
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

                windowInsetsController.isAppearanceLightStatusBars = false
                App()
            }
        }
    }
}


@Composable
fun App() {
    val context = LocalContext.current
    val isConnected = remember {
        mutableStateOf(checkInternet(context))
    }

    if(isConnected.value) AppNavGraph()
    else NoInternetScreen()
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun checkInternet(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false

    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_INTERNET
    ) && capabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_VALIDATED
    )

}


