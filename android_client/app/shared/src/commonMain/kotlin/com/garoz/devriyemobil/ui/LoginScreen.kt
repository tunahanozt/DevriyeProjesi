package com.garoz.devriyemobil.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit // Token'ı alıp ana sayfaya geçiş yapacak fonksiyon
) {
    var sicilNo by remember { mutableStateOf("") }
    var sifre by remember { mutableStateOf("") }

    // ViewModel'deki State'i dinliyoruz
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Devriye Sistemi",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        OutlinedTextField(
            value = sicilNo,
            onValueChange = { sicilNo = it },
            label = { Text("Sicil Numarası") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = sifre,
            onValueChange = { sifre = it },
            label = { Text("Şifre") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // State'e göre UI tepkileri
        when (uiState) {
            is LoginState.Idle -> {
                Button(
                    onClick = {
                        if (sicilNo.isNotBlank() && sifre.isNotBlank()) {
                            viewModel.login(sicilNo, sifre)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Sisteme Giriş Yap")
                }
            }
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginState.Error -> {
                Text(
                    text = (uiState as LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { viewModel.login(sicilNo, sifre) },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Tekrar Dene")
                }
            }
            is LoginState.Success -> {
                // Token alındı; composition sırasında değil, yan etki olarak üst ekrana bildir
                val token = (uiState as LoginState.Success).token
                LaunchedEffect(token) {
                    onLoginSuccess(token)
                }
            }
        }
    }
}