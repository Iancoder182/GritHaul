package com.ian.grithaul.ui.screens.admin

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
import com.ian.grithaul.ui.theme.StatusInfo
import com.ian.grithaul.ui.theme.StatusSuccess
import com.ian.grithaul.ui.theme.StatusWarning
import com.ian.grithaul.ui.theme.TextPrimary
import com.ian.grithaul.ui.theme.TextSecondary
import com.ian.grithaul.ui.theme.White
import com.ian.grithaul.navigation.ROUT_NOTIFICATIONS
import com.ian.grithaul.navigation.ROUT_PROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {

    var request1Status by remember { mutableStateOf("Pending") }
    var request2Status by remember { mutableStateOf("Pending") }
    var complaint1Status by remember { mutableStateOf("Pending") }
    var complaint2Status by remember { mutableStateOf("Pending") }
    var complaint3Status by remember { mutableStateOf("Pending") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Admin Dashboard",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                        Text(
                            text = "Thursday, 23 Apr 2026",
                            fontSize = 12.sp,
                            color = White.copy(alpha = 0.8f)
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

            // Overview Stats
            Text(
                text = "Today's Overview",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "📋",
                    value = "24",
                    label = "Total Pickups",
                    color = PrimaryGreen
                )
                AdminStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "✅",
                    value = "18",
                    label = "Completed",
                    color = StatusSuccess
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "🚛",
                    value = "3",
                    label = "Active Trucks",
                    color = StatusInfo
                )
                AdminStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "⚠️",
                    value = "5",
                    label = "Open Complaints",
                    color = StatusError
                )
            }

            // Zone Breakdown
            Text(
                text = "Zone Breakdown",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            ZoneProgressCard(
                zone = "Westlands",
                completed = 8,
                total = 10,
                truckId = "T01",
                color = PrimaryGreen
            )

            ZoneProgressCard(
                zone = "Kibera North",
                completed = 5,
                total = 8,
                truckId = "T02",
                color = AccentGreen
            )

            ZoneProgressCard(
                zone = "CBD East",
                completed = 3,
                total = 6,
                truckId = "T03",
                color = StatusInfo
            )

            ZoneProgressCard(
                zone = "Karen Ridge",
                completed = 2,
                total = 4,
                truckId = "T04",
                color = StatusWarning
            )

            // On Demand Requests
            Text(
                text = "Pending On-Demand Requests",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            AdminRequestCard(
                resident = "Peter Kamau",
                zone = "Eastleigh",
                wasteType = "📦 Bulk Waste",
                date = "Saturday, 25 Apr",
                timeWindow = "Morning · 6AM to 10AM",
                status = request1Status,
                onApprove = { request1Status = "Approved" },
                onReject = { request1Status = "Rejected" }
            )

            AdminRequestCard(
                resident = "Mary Njeri",
                zone = "Kasarani",
                wasteType = "🌿 Organic Waste",
                date = "Friday, 24 Apr",
                timeWindow = "Afternoon · 2PM to 6PM",
                status = request2Status,
                onApprove = { request2Status = "Approved" },
                onReject = { request2Status = "Rejected" }
            )

            // Complaints Panel
            Text(
                text = "Complaints Panel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            AdminComplaintCard(
                resident = "John Mwangi",
                zone = "Westlands",
                issue = "Missed Pickup",
                date = "Monday, 20 Apr",
                status = complaint1Status,
                onResolve = { complaint1Status = "Resolved" }
            )

            AdminComplaintCard(
                resident = "Grace Achieng",
                zone = "CBD East",
                issue = "Incomplete Collection",
                date = "Saturday, 18 Apr",
                status = complaint2Status,
                onResolve = { complaint2Status = "Resolved" }
            )

            AdminComplaintCard(
                resident = "Brian Otieno",
                zone = "Kibera North",
                issue = "Overflowing Public Bin",
                date = "Friday, 17 Apr",
                status = complaint3Status,
                onResolve = { complaint3Status = "Resolved" }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ZoneProgressCard(
    zone: String,
    completed: Int,
    total: Int,
    truckId: String,
    color: androidx.compose.ui.graphics.Color
) {
    val progress = completed.toFloat() / total.toFloat()
    val percentage = (progress * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = zone,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Truck $truckId · $completed of $total stops",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Text(
                    text = "$percentage%",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = BorderLight
            )
        }
    }
}

@Composable
fun AdminRequestCard(
    resident: String,
    zone: String,
    wasteType: String,
    date: String,
    timeWindow: String,
    status: String,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val statusColor = when (status) {
        "Approved" -> StatusSuccess
        "Rejected" -> StatusError
        else -> StatusWarning
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📦", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = resident,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = zone,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
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

            HorizontalDivider(color = BorderLight)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Waste Type",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = wasteType,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Preferred Date",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = date,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⏰ $timeWindow",
                fontSize = 12.sp,
                color = TextSecondary
            )

            if (status == "Pending") {
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = BorderLight)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusSuccess
                        )
                    ) {
                        Text(
                            text = "✓ Approve",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                    OutlinedButton(
                        onClick = onReject,
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
                            text = "✕ Reject",
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

@Composable
fun AdminComplaintCard(
    resident: String,
    zone: String,
    issue: String,
    date: String,
    status: String,
    onResolve: () -> Unit
) {
    val statusColor = when (status) {
        "Resolved" -> StatusSuccess
        "Reviewed" -> AccentGreen
        else -> StatusWarning
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(StatusError.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚠️", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = issue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "$resident · $zone",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = date,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
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

            if (status == "Pending") {
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = BorderLight)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onResolve,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    )
                ) {
                    Text(
                        text = "Mark as Resolved",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    AdminDashboardScreen(rememberNavController())
}