package com.example.week4_inventarisbarang.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.week4_inventarisbarang.data.BarangRepository
import com.example.week4_inventarisbarang.model.Barang
import androidx.compose.ui.tooling.preview.Preview
import com.example.week4_inventarisbarang.ui.theme.Week4_InventarisBarangTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahBarangScreen(navController: NavController, repository: BarangRepository, editBarangId: Long? = null) {
    val existingBarang = editBarangId?.let { repository.getBarangById(it) }

    TambahBarangContent(
        existingBarang = existingBarang,
        onBackClick = { navController.popBackStack() },
        onSaveClick = { barang ->
            if (editBarangId == null) {
                repository.tambahBarang(barang)
            } else {
                repository.updateBarang(barang)
            }
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahBarangContent(
    existingBarang: Barang? = null,
    onBackClick: () -> Unit,
    onSaveClick: (Barang) -> Unit
) {
    val context = LocalContext.current

    var nama by remember { mutableStateOf(existingBarang?.nama ?: "") }
    var kategori by remember { mutableStateOf(existingBarang?.kategori ?: "") }
    var harga by remember { mutableStateOf(existingBarang?.harga?.toString() ?: "") }
    var stok by remember { mutableStateOf(existingBarang?.stok?.toString() ?: "") }
    var sku by remember { mutableStateOf(existingBarang?.sku ?: "") }
    var berat by remember { mutableStateOf(existingBarang?.berat?.toString() ?: "") }
    var deskripsi by remember { mutableStateOf(existingBarang?.deskripsi ?: "") }
    var gambarUri by remember { mutableStateOf(existingBarang?.gambarUri ?: "") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            gambarUri = it.toString()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (existingBarang == null) "Tambah Barang" else "Edit Barang", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Picker UI
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { 
                        try {
                            launcher.launch(arrayOf("image/*"))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (gambarUri.isNotEmpty()) {
                    AsyncImage(
                        model = gambarUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Overlay icon
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.White)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tambah Foto",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            ModernTextField(
                value = nama,
                onValueChange = { nama = it },
                label = "Nama Barang",
                leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
            )
            ModernTextField(
                value = kategori,
                onValueChange = { kategori = it },
                label = "Kategori",
                leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
            )
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    ModernTextField(
                        value = harga,
                        onValueChange = { harga = it },
                        label = "Harga (Rp)",
                        keyboardType = KeyboardType.Number,
                        leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    ModernTextField(
                        value = stok,
                        onValueChange = { stok = it },
                        label = "Stok",
                        keyboardType = KeyboardType.Number,
                        leadingIcon = { Icon(Icons.Default.Inventory, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    ModernTextField(
                        value = sku,
                        onValueChange = { sku = it },
                        label = "SKU",
                        leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    ModernTextField(
                        value = berat,
                        onValueChange = { berat = it },
                        label = "Berat (kg)",
                        keyboardType = KeyboardType.Decimal,
                        leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )
                }
            }

            ModernTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = "Deskripsi (Opsional)",
                singleLine = false,
                modifier = Modifier.height(120.dp),
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (nama.isBlank()) return@Button
                    
                    val barang = Barang(
                        id = existingBarang?.id ?: System.currentTimeMillis(),
                        nama = nama,
                        kategori = kategori,
                        harga = harga.toDoubleOrNull() ?: 0.0,
                        stok = stok.toIntOrNull() ?: 0,
                        sku = sku,
                        berat = berat.toDoubleOrNull() ?: 0.0,
                        deskripsi = deskripsi,
                        gambarUri = gambarUri
                    )
                    
                    onSaveClick(barang)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan Inventaris", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = singleLine,
                leadingIcon = leadingIcon,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
    }
}

@Preview(showBackground = true)
@Composable
fun TambahBarangScreenPreview() {
    Week4_InventarisBarangTheme {
        TambahBarangContent(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TambahBarangScreenEditPreview() {
    Week4_InventarisBarangTheme {
        TambahBarangContent(
            existingBarang = Barang(
                id = 1,
                nama = "Kamera Mirrorless",
                kategori = "Elektronik",
                harga = 15000000.0,
                stok = 5,
                sku = "CAM-001",
                berat = 0.8,
                deskripsi = "Kamera mirrorless dengan sensor full-frame.",
                gambarUri = ""
            ),
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ModernTextFieldPreview() {
    Week4_InventarisBarangTheme {
        ModernTextField(
            value = "Sample Text",
            onValueChange = {},
            label = "Nama Barang"
        )
    }
}
