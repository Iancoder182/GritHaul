package com.ian.grithaul.ui.screens.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.ian.grithaul.ui.theme.StatusError
import com.ian.grithaul.ui.theme.StatusSuccess
import com.ian.grithaul.ui.theme.StatusWarning
import com.ian.grithaul.ui.theme.TextPrimary
import com.ian.grithaul.ui.theme.TextSecondary
import com.ian.grithaul.ui.theme.White
import com.ian.grithaul.navigation.ROUT_NOTIFICATIONS
import com.ian.grithaul.navigation.ROUT_PROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverDashboardScreen(navController: NavController) {

    // Track stop states
    var stop1Status by remember { mutableStateOf("Pending") }
    var stop2Status by remember { mutableStateOf("Pending") }
    var stop3Status by remember { mutableStateOf("Pending") }
    var stop4Status by remember { mutableStateOf("Pending") }
    var stop5Status by remember { mutableStateOf("Pending") }

    val completedCount = listOf(
        stop1Status, stop2Status, stop3Status, stop4Status, stop5Status
    ).count { it == "Collected" }

    val totalStops = 5
    val progress = completedCount.toFloat() / totalStops.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Good Morning 👋",
                            fontSize = 13.sp,
                            color = White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Moses Kariuki",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(ROUT_NOTIFICATIONS)
                    }) {
                        Text(text = "🔔", fontSize = 22.sp)
                    }
                    IconButton(onClick = {
                        navController.navigate(ROUT_PROFILE)
                    }) {
                        Text(text = "👤", fontSize = 22.sp)
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

            // Truck Info Card
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Truck T01 · Westlands Zone",
                                fontSize = 13.sp,
                                color = White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Thursday, 23 Apr",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                            Text(
                                text = "Capacity: 8 Tonnes",
                                fontSize = 13.sp,
                                color = White.copy(alpha = 0.8f)
                            )
                        }
                        Text(text = "🚛", fontSize = 44.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = White.copy(alpha = 0.2f))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Route Progress",
                            fontSize = 13.sp,
                            color = White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "$completedCount / $totalStops stops",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = LightGreen,
                        trackColor = White.copy(alpha = 0.3f)
                    )
                }
            }

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DriverStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "✅",
                    value = "$completedCount",
                    label = "Collected",
                    color = StatusSuccess
                )
                DriverStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "⏳",
                    value = "${totalStops - completedCount}",
                    label = "Remaining",
                    color = StatusWarning
                )
                DriverStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "⚖️",
                    value = "2.4T",
                    label = "Collected",
                    color = PrimaryGreen
                )
            }

            // Route Stops
            Text(
                text = "Today's Route",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Stop 1
            DriverStopCard(
                stopNumber = 1,
                address = "14 Rhapta Road, Westlands",
                resident = "John Mwangi",
                wasteType = "🗑️ General Waste",
                volume = "Large — 4+ bags",
                time = "7:00 AM",
                status = stop1Status,
                onCollected = { stop1Status = "Collected" },
                onSkipped = { stop1Status = "Skipped" }
            )

            // Stop 2
            DriverStopCard(
                stopNumber = 2,
                address = "Westlands Mall, Back Gate",
                resident = "Westlands Mall Mgmt",
                wasteType = "📦 Bulk Waste",
                volume = "Extra Large",
                time = "7:30 AM",
                status = stop2Status,
                onCollected = { stop2Status = "Collected" },
                onSkipped = { stop2Status = "Skipped" }
            )

            // Stop 3
            DriverStopCard(
                stopNumber = 3,
                address = "Peponi Road, Plot 22",
                resident = "Grace Achieng",
                wasteType = "🌿 Organic Waste",
                volume = "Small — 1 bag",
                time = "8:00 AM",
                status = stop3Status,
                onCollected = { stop3Status = "Collected" },
                onSkipped = { stop3Status = "Skipped" }
            )

            // Stop 4
            DriverStopCard(
                stopNumber = 4,
                address = "Ring Road Westlands 7A",
                resident = "Brian Otieno",
                wasteType = "♻️ Recyclable",
                volume = "Medium — 2 to 3 bags",
                time = "8:30 AM",
                status = stop4Status,
                onCollected = { stop4Status = "Collected" },
                onSkipped = { stop4Status = "Skipped" }
            )

            // Stop 5
            DriverStopCard(
                stopNumber = 5,
                address = "Woodvale Grove, House 3",
                resident = "Patricia Wanjiku",
                wasteType = "🗑️ General Waste",
                volume = "Medium — 2 to 3 bags",
                time = "9:00 AM",
                status = stop5Status,
                onCollected = { stop5Status = "Collected" },
                onSkipped = { stop5Status = "Skipped" }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DriverStatCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DriverStopCard(
    stopNumber: Int,
    address: String,
    resident: String,
    wasteType: String,
    volume: String,
    time: String,
    status: String,
    onCollected: () -> Unit,
    onSkipped: () -> Unit
) {
    val statusColor = when (status) {
        "Collected" -> StatusSuccess
        "Skipped" -> StatusError
        else -> StatusWarning
    }

    val containerColor = when (status) {
        "Collected" -> StatusSuccess.copy(alpha = 0.05f)
        "Skipped" -> StatusError.copy(alpha = 0.05f)
        else -> White
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Stop header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$stopNumber",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = time,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = status,
                        fontSize = 11.sp,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Address
            Text(
                text = address,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Resident
            Text(
                text = "👤 $resident",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Waste details
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = wasteType,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "· $volume",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // Action buttons — only show if pending
            if (status == "Pending") {
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = BorderLight)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Collected button
                    Button(
                        onClick = onCollected,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusSuccess
                        )
                    ) {
                        Text(
                            text = "✓ Collected",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }

                    // Skipped button
                    OutlinedButton(
                        onClick = onSkipped,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = StatusError
                        ),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Text(
                            text = "✕ Skip",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusError
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriverDashboardScreenPreview() {
    DriverDashboardScreen(rememberNavController())
}