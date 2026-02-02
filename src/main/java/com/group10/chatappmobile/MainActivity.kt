package com.group10.chatappmobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.group10.chatappmobile.ui.theme.ChatappMobileTheme
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribe
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                connectClient()
            }
        }

        setContent {
            ChatappMobileTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    suspend fun connectClient() {

        val stompClient = StompClient(OkHttpWebSocketClient())

        val url = ""

        val session = stompClient.connect(url)

        session.sendText(destination = "/queue/a", body = "Basic text message")

        session.subscribe("/queue/a").collect { value ->
            Log.d("ChatApp", value.bodyAsText)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatappMobileTheme {
        Greeting("Android")
    }
}