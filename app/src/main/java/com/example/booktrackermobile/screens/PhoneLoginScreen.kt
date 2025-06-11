package com.example.booktrackermobile.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun PhoneLoginScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current as Activity
    var phoneNumber by remember { mutableStateOf("") }
    var smsCode by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Logowanie przez telefon", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Numer telefonu (+48...)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (verificationId == null) {
            Button(
                onClick = {
                    val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context)
                        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            onLoginSuccess()
                                        } else {
                                            error = "Nie udało się automatycznie zalogować"
                                        }
                                    }
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                error = e.localizedMessage
                            }

                            override fun onCodeSent(vid: String, token: PhoneAuthProvider.ForceResendingToken) {
                                verificationId = vid
                            }
                        })
                        .build()

                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
            ) {
                Text("Wyślij kod SMS")
            }
        } else {
            OutlinedTextField(
                value = smsCode,
                onValueChange = { smsCode = it },
                label = { Text("Kod SMS") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val credential = PhoneAuthProvider.getCredential(verificationId!!, smsCode)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                onLoginSuccess()
                            } else {
                                error = "Nieprawidłowy kod"
                            }
                        }
                }
            ) {
                Text("Zaloguj się")
            }
        }

        error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) {
            Text("← Wróć")
        }
    }
}
