package com.ian.grithaul.ui.screens.resident

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
import com.ian.grithaul.navigation.ROUT_FEEDBACK
import com.ian.grithaul.navigation.ROUT_CONFIRM_PICKUP
import com.ian.grithaul.navigation.ROUT_DEMAND_REQUEST
import com.ian.grithaul.navigation.ROUT_NOTIFICATIONS
import com.ian.grithaul.navigation.ROUT_PROFILE
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResidentHomeScreen(navController: NavController) {

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
                            text = "John Mwangi",
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

            // Next Pickup Card
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
                        text = "Next Scheduled Pickup",
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📍", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Westlands Zone",
                                fontSize = 13.sp,
                                color = White.copy(alpha = 0.9f)
                            )
                        }

                        // Status badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(White.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Not Confirmed",
                                fontSize = 11.sp,
                                color = White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Quick Actions
            Text(
                text = "Quick Actions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Confirm Pickup
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = "✅",
                    title = "Confirm\nPickup",
                    color = PrimaryGreen,
                    onClick = { navController.navigate(ROUT_CONFIRM_PICKUP) }
                )

                // Request Pickup
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = "🚛",
                    title = "Request\nPickup",
                    color = AccentGreen,
                    onClick = { navController.navigate(ROUT_DEMAND_REQUEST) }
                )

                // Report Issue
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = "⚠️",
                    title = "Report\nIssue",
                    color = StatusError,
                    onClick = { navController.navigate(ROUT_FEEDBACK) }
                )
            }

            // Last Pickup Status
            Text(
                text = "Last Pickup",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LightGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🗑️", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "General Waste",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Tuesday, 21 Apr · 7:30 AM",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(StatusSuccess.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Collected",
                            fontSize = 11.sp,
                            color = StatusSuccess,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Recent Complaints
            Text(
                text = "Recent Complaints",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Complaint Item 1
            ComplaintItem(
                icon = "❌",
                title = "Missed Pickup",
                date = "Monday, 20 Apr",
                status = "Resolved",
                statusColor = StatusSuccess
            )

            // Complaint Item 2
            ComplaintItem(
                icon = "⚠️",
                title = "Incomplete Collection",
                date = "Saturday, 18 Apr",
                status = "Pending",
                statusColor = StatusWarning
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = White,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun ComplaintItem(
    icon: String,
    title: String,
    date: String,
    status: String,
    statusColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = date,
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
    }
}

@Preview(showBackground = true)
@Composable
fun ResidentHomeScreenPreview() {
    ResidentHomeScreen(rememberNavController())
}