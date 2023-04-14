package com.lazyhat.novsuapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.novsuapp.data.DataSource
import com.lazyhat.novsuapp.navigation.NovsuNavGraph
import com.lazyhat.novsuapp.ui.theme.NovsuTheme
import com.lazyhat.novsuapp.viewmodels.MainEvent
import com.lazyhat.novsuapp.viewmodels.MainViewModel
import com.lazyhat.novsuapp.viewmodels.UpdateInfo
import com.lazyhat.novsuapp.viewmodels.UpdateProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val mainViewModel: MainViewModel = hiltViewModel()
            val uiState by mainViewModel.uiState.collectAsState()
            mainViewModel.onCreate()
            NovsuTheme(scheme = uiState.settings.theme.scheme) {
                NovsuNavGraph(modifier = Modifier.background(MaterialTheme.colorScheme.background))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AnimatedVisibility(
                        visible = uiState.updateInfo is UpdateInfo.Available || uiState.updateInfo == UpdateInfo.InProgress(
                            UpdateProgress.Downloaded
                        ),
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Column(
                                Modifier.padding(5.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (uiState.updateInfo.toString(context) != null)
                                    Text(
                                        uiState.updateInfo.toString(context)!!,
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                                    )
                                Column(
                                    Modifier.fillMaxWidth(),
                                    Arrangement.Center,
                                    Alignment.CenterHorizontally
                                ) {
                                    if (uiState.updateInfo.toStringInstallButton(context) != null)
                                        Button(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            onClick = {
                                                mainViewModel.createEvent(MainEvent.Install)
                                                (context as? Activity)?.finish()
                                            },
                                            shape = RoundedCornerShape(3.dp)
                                        ) {
                                            Text(
                                                text = uiState.updateInfo.toStringInstallButton(
                                                    context
                                                )!!
                                            )
                                        }
                                    if (uiState.updateInfo.toStringGetButton(context) != null)
                                        Button(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            onClick = { mainViewModel.createEvent(MainEvent.GetUpdate) },
                                            shape = RoundedCornerShape(3.dp)
                                        ) {
                                            Text(text = uiState.updateInfo.toStringGetButton(context)!!)
                                        }
                                    if (uiState.updateInfo.toStringCancelButton(context) != null)
                                        Button(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            onClick = { mainViewModel.createEvent(MainEvent.CancelUpdate) },
                                            shape = RoundedCornerShape(3.dp)
                                        ) {
                                            Text(
                                                text = uiState.updateInfo.toStringCancelButton(
                                                    context
                                                )!!
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NovsuTheme(DataSource.ColorSchemes.Default.scheme) {
        NovsuNavGraph()
    }
}


