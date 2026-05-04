package com.ian.grithaul.ui.screens.resident

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ian.grithaul.ui.theme.AccentGreen
import com.ian.grithaul.ui.theme.BackgroundLight
import com.ian.grithaul.ui.theme.BorderLight
import com.ian.grithaul.ui.theme.PrimaryGreen
import com.ian.grithaul.ui.theme.StatusInfo
import com.ian.grithaul.ui.theme.TextPrimary
import com.ian.grithaul.ui.theme.TextSecondary
import com.ian.grithaul.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnDemandRequestScreen(navController: NavController) {

    var selectedWasteType by remember { mutableStateOf("") }
    var selectedVolume by remember { mutableStateOf("") }
    var preferredDate by remember { mutableStateOf("") }
    var selectedTimeWindow by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    val wasteTypes = listOf(
        "🗑️  General Waste",
        "🌿  Organic Waste",
        "♻️  Recyclable",
        "📦  Bulk / Special"
    )

    val volumes = listOf(
        "Small — 1 bag",
        "Medium — 2 to 3 bags",
        "Large — 4 or more bags",
        "Extra Large — Furniture / Construction"
    )

    val timeWindows = listOf(
        "🌅  Morning · 6AM to 10AM",
        "☀️  Midday · 10AM to 2PM",
        "🌇  Afternoon · 2PM to 6PM"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Request Pickup",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen
                )
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = StatusInfo.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "ℹ️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "On-demand requests are reviewed by the admin and assigned to an available truck. You will be notified once approved.",
                        fontSize = 12.sp,
                        color = StatusInfo,
                        lineHeight = 18.sp
                    )
                }
            }

            // Waste Type
            Text(
                text = "Select Waste Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            wasteTypes.forEach { type ->
                SelectableItem(
                    label = type,
                    selected = selectedWasteType == type,
                    selectedColor = PrimaryGreen,
                    onClick = { selectedWasteType = type }
                )
            }

            // Volume
            Text(
                text = "Estimate Volume",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            volumes.forEach { volume ->
                SelectableItem(
                    label = volume,
                    selected = selectedVolume == volume,
                    selectedColor = AccentGreen,
                    onClick = { selectedVolume = volume }
                )
            }

            // Preferred Date
            Text(
                text = "Preferred Date",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            OutlinedTextField(
                value = preferredDate,
                onValueChange = { preferredDate = it },
                placeholder = {
                    Text(
                        text = "e.g. Friday 25 Apr 2026",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = BorderLight,
                    focusedLabelColor = PrimaryGreen,
                    cursorColor = PrimaryGreen,
                    unfocusedContainerColor = White,
                    focusedContainerColor = White
                ),
                singleLine = true
            )

            // Time Window
            Text(
                text = "Preferred Time Window",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            timeWindows.forEach { window ->
                SelectableItem(
                    label = window,
                    selected = selectedTimeWindow == window,
                    selectedColor = PrimaryGreen,
                    onClick = { selectedTimeWindow = window }
                )
            }

            // Additional Notes
            Text(
                text = "Additional Notes (Optional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            OutlinedTextField(
                value = additionalNotes,
                onValueChange = { additionalNotes = it },
                placeholder = {
                    Text(
                        text = "e.g. Old sofa and mattress, access via side gate...",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = BorderLight,
                    focusedLabelColor = PrimaryGreen,
                    cursorColor = PrimaryGreen,
                    unfocusedContainerColor = White,
                    focusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Submit Button
            Button(
                onClick = { submitted = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (submitted) AccentGreen else PrimaryGreen
                )
            ) {
                Text(
                    text = if (submitted) "✓  Request Submitted" else "Submit Request",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            // Success message
            if (submitted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AccentGreen.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "✅", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Your request has been submitted. Admin will review and assign a truck shortly.",
                            fontSize = 13.sp,
                            color = AccentGreen,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SelectableItem(
    label: String,
    selected: Boolean,
    selectedColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) selectedColor else White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (selected) White else TextPrimary
            )
            if (selected) {
                Text(text = "✓", fontSize = 16.sp, color = White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnDemandRequestScreenPreview() {
    OnDemandRequestScreen(rememberNavController())
}