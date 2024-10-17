package com.example.notifications

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.notifications.ui.theme.NotificationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           ActivityCompat.requestPermissions(
               this,
               arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
               111
           )
        }

        val intent = Intent(this, CounterReceiver::class.java)
        intent.action = CounterNotification.CounterActions.START.name

        enableEdgeToEdge()
        setContent {
            NotificationsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Notifications(
                        modifier = Modifier.padding(innerPadding),
                        onClick = {
                            sendBroadcast(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Notifications(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onClick() }
        ) {
            Text(text = "Show Counter Notifications")
        }
    }
}