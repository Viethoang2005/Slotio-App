package com.example.slotio.ui.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slotio.theme.xanhNgocLucBao
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val uiState by authViewModel.uiState.collectAsState()
    LaunchedEffect(uiState.isSuccess) {
        if(uiState.isSuccess) onLoginSuccess()
    }
    LoginScreenContent(
        uiState = uiState,
        onNavigateToRegister = onNavigateToRegister,
        onLoginClick = { email, pass ->
            authViewModel.login(email = email, password = pass)
        }
    )
}

@Composable
fun LoginScreenContent(
    uiState: AuthUiState,
    onNavigateToRegister: () -> Unit,
    onLoginClick: (String, String) -> Unit
) {
    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    Scaffold(
        containerColor = xanhNgocLucBao
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Card(
                modifier = Modifier.padding(25.dp).padding(top = 150.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    15.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        text = "SLOTIO APP",
                        color = xanhNgocLucBao,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Phần mềm đặt lịch sân thể thao trực tuyến",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = "Email",
                        fontSize = 15.sp
                    )
                    OutlinedTextField(
                        value = inputEmail,
                        onValueChange = {inputEmail = it},
                        placeholder = { Text("Nhap email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = "Mat khau",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        value = inputPassword,
                        onValueChange = {inputPassword = it},
                        placeholder = { Text("Nhap mat khau") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if(passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if(passwordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff
                            val des = if(passwordVisible) "An mat khau" else "Hien mat khau"
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = image, contentDescription = des
                                )
                            }
                        }
                    )

                    uiState.errorMessage?.let { error ->
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(Modifier.height(20.dp))

                    if(uiState.isLoading) {
                        Box(Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = xanhNgocLucBao)
                        }
                    }else {
                        ElevatedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if(inputEmail.isNotBlank() && inputPassword.isNotBlank())
                                    onLoginClick(inputEmail.trim(), inputPassword.trim())
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = xanhNgocLucBao
                            )
                        ) {
                            Text(
                                text = "Dang nhap",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Ban chua co tai khoan ?"
                        )
                        TextButton(
                            onClick = onNavigateToRegister
                        ) {
                            Text(
                                text = "Dang ky"
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(
) {
    LoginScreenContent(
        uiState = AuthUiState(),
        onNavigateToRegister = {},
        onLoginClick = {_, _ -> }
    )
}



