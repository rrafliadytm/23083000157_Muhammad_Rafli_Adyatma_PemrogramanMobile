package com.example.profilmahasiswa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profilmahasiswa.R
import com.example.profilmahasiswa.ui.theme.ProfilMahasiswaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen() {
    // State management
    var isEditing by remember { mutableStateOf(false) }
    
    // Data profil
    var email by remember { mutableStateOf("ahmad.fauzi@student.ac.id") }
    var telepon by remember { mutableStateOf("+62 812-3456-7890") }
    var alamat by remember { mutableStateOf("Malang, Jawa Timur") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profil Mahasiswa",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    //colorResource(id = R.color.colorOrange)
                    //containerColor = MaterialTheme.colorScheme.primaryContainer,
                    containerColor = colorResource(id = R.color.colorPrimary),
                    //titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    titleContentColor = colorResource(id = R.color.colorPrimaryonContainer)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Re-implementing ProfilePhotoSection to keep it standalone
            ProfilePhotoSectionStandalone(isEditing = isEditing)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ahmad Fauzi Rahman",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "NIM: 20210001",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Info Edit Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "📞 Informasi Kontak",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    EditRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        enabled = isEditing
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    EditRow(
                        icon = Icons.Default.Phone,
                        label = "Telepon",
                        value = telepon,
                        onValueChange = { telepon = it },
                        enabled = isEditing
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    EditRow(
                        icon = Icons.Default.LocationOn,
                        label = "Alamat",
                        value = alamat,
                        onValueChange = { alamat = it },
                        enabled = isEditing
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Toggle Edit/Simpan Button
            Button(
                onClick = { isEditing = !isEditing },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEditing) 
                        colorResource(id = R.color.colorSubmit) // Green for Simpan
                    else 
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Simpan" else "Edit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ProfilePhotoSectionStandalone(isEditing: Boolean) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto Profil",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (isEditing) MaterialTheme.colorScheme.primary else colorResource(id = R.color.colorSubmit)
                )
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.CameraAlt else Icons.Default.Check,
                contentDescription = if (isEditing) "Ubah Foto" else "Aktif",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun EditRow(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontSize = 12.sp) },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = Color.Transparent,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileEditScreenPreview() {
    ProfilMahasiswaTheme {
        ProfileEditScreen()
    }
}
