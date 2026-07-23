package com.example.week1_helloworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.week1_helloworld.ui.theme.Week1_HelloWorldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week1_HelloWorldTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Memanggil GreetingScreen sebagai UI utama
                    GreetingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Komponen GreetingScreen: Form Sederhana Data Mahasiswa.
 * Sesuai dengan brief: Input Nama, NIM (8-10 digit), Validasi, dan Animated Result Card.
 */
@Composable
fun GreetingScreen(modifier: Modifier = Modifier) {
    // 2. State Management
    var nameInput by remember { mutableStateOf("") }
    var nimInput by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    // Logic Validasi
    val isNameValid by remember { derivedStateOf { nameInput.isNotBlank() } }
    val isNimValid by remember { 
        derivedStateOf { 
            nimInput.isNotEmpty() && nimInput.all { it.isDigit() } && nimInput.length in 8..15
        } 
    }
    
    // Tombol Submit hanya aktif jika semua input valid
    val isFormValid by remember { derivedStateOf { isNameValid && isNimValid } }

    // Styling: Background Gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Judul Form
        Text(
            text = "Form Data Mahasiswa",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Universitas Merdeka Malang",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Silahkan lengkapi data diri Anda",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 1. Input Fields (Nama & NIM)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Input Nama Lengkap
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nama Lengkap") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    // 3. Validasi Nama
                    isError = nameInput.isNotEmpty() && !isNameValid,
                    supportingText = {
                        if (nameInput.isNotEmpty() && !isNameValid) {
                            Text("Nama tidak boleh kosong")
                        }
                    },
                    singleLine = true
                )

                // Input NIM
                OutlinedTextField(
                    value = nimInput,
                    onValueChange = { 
                        // Hanya menerima input angka
                        if (it.all { char -> char.isDigit() }) nimInput = it 
                    },
                    label = { Text("NIM") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    // 3. Validasi NIM (8-10 digit)
                    isError = nimInput.isNotEmpty() && !isNimValid,
                    supportingText = {
                        if (nimInput.isNotEmpty() && !isNimValid) {
                            Text("Inputkan NIM Anda")
                        }
                    },
                    singleLine = true
                )

                // 3. Tombol Submit
                Button(
                    onClick = { isSubmitted = true },
                    enabled = isFormValid, // Aktif jika valid
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Submit Data", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 4. Tampilan Hasil (Card Results)
        AnimatedVisibility(
            visible = isSubmitted,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon Mahasiswa
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Selamat Datang",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "di Universitas Merdeka Malang!",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Nama & NIM yang di-submit
                    Text(
                        text = nameInput,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "NIM: $nimInput",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )

                    // Tombol Reset
                    TextButton(
                        onClick = {
                            isSubmitted = false
                            nameInput = ""
                            nimInput = ""
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset Form", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingScreenPreview() {
    Week1_HelloWorldTheme {
        GreetingScreen()
    }
}
