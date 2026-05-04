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
import androidx.compose.ui.draw.clip
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
import com.ian.grithaul.ui.theme.LightGreen
import com.ian.grithaul.ui.theme.PrimaryGreen
import com.ian.grithaul.ui.theme.StatusWarning
import com.ian.grithaul.ui.theme.TextPrimary
import com.ian.grithaul.ui.theme.TextSecondary
import com.ian.grithaul.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPickupScreen(navController: NavController) {

    var selectedWasteType by remember { mutableStateOf("") }
    var selectedVolume by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }
    var confirmed by remember { mutableStateOf(false) }

    val wasteTypes = listOf(
        "🗑️  General Waste",
        "🌿  Organic Waste",
        "♻️  Recyclable",
        "📦  Bulk / Special"
    )

    val volumes = listOf(
        "Small — 1 bag",
        "Medium — 2 to 3 bags",
        "Large — 4 or more bags"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Confirm Pickup",
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

            // Pickup Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Scheduled Pickup",
                        fontSize = 13.sp,
                        color = White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tomorrow",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                            Text(
                                text = "Thursday, 23 Apr · 7:00 AM",
                                fontSize = 13.sp,
                                color = White.copy(alpha = 0.8f)
                            )
                        }
                        Text(text = "📅", fontSize = 40.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(color = White.copy(alpha = 0.2f))

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📍", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Westlands Zone · Truck T01",
                            fontSize = 13.sp,
                            color = White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Cutoff warning
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = StatusWarning.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⏰", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Confirmation Cutoff",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusWarning
                        )
                        Text(
                            text = "Please confirm before 8:00 PM tonight to be included in tomorrow's route.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Waste Type Selection
            Text(
                text = "Select Waste Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            wasteTypes.forEach { type ->
                WasteTypeItem(
                    label = type,
                    selected = selectedWasteType == type,
                    onClick = { selectedWasteType = type }
                )
            }

            // Volume Selection
            Text(
                text = "Estimate Volume",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            volumes.forEach { volume ->
                VolumeItem(
                    label = volume,
                    selected = selectedVolume == volume,
                    onClick = { selectedVolume = volume }
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
                        text = "e.g. Gate will be open, waste is near the fence...",
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

            // Confirm Button
            Button(
                onClick = { confirmed = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (confirmed) AccentGreen else PrimaryGreen
                )
            ) {
                Text(
                    text = if (confirmed) "✓  Confirmed" else "Confirm Pickup",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            if (confirmed) {
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
                            text = "Your pickup has been confirmed. The driver has been notified.",
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
fun WasteTypeItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) PrimaryGreen else White
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

@Composable
fun VolumeItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) AccentGreen else White
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
fun ConfirmPickupScreenPreview() {
    ConfirmPickupScreen(rememberNavController())
}