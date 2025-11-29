package utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.studygroup.ui.theme.LocalCustomColors

@Composable
fun ConnectivityStatus(){
    val context = LocalContext.current
    val colors = LocalCustomColors.current
    val networkObserver = remember { NetworkConnectivityObserver(context) }
    val status by networkObserver.observe().collectAsState(
        initial = ConnectivityObserver.Status.Unavailable
    )

    val message = when(status){
        ConnectivityObserver.Status.Available -> null // online state needs no message
        ConnectivityObserver.Status.Losing -> "Network: Unstable"
        ConnectivityObserver.Status.Lost -> "Network: Lost"
        ConnectivityObserver.Status.Unavailable -> "Network: Unavailable"
    }

    message?.let {
        Text(
            text = it,
            color = colors.dangerActive
        )
    }

    //if(status == ConnectivityObserver.Status.Lost || status == ConnectivityObserver.Status.Unavailable){
    //    OfflineOverlay(message = "You are offline")
    //}

}


