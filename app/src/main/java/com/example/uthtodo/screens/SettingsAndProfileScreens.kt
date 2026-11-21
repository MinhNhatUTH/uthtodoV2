
package com.example.uthtodo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthtodo.model.AppScreen
import com.example.uthtodo.model.ThemeMode
import com.example.uthtodo.model.UserProfile
import com.example.uthtodo.viewmodel.TodoViewModel

// ================== 1. MÀN HÌNH CÀI ĐẶT (SETTINGS SCREEN) ==================
@Composable
fun SettingsScreen(viewModel: TodoViewModel) {
    val isDark = viewModel.isDarkTheme()
    val bgColor = if (isDark) Color(0xFF0D0D0D) else Color(0xFFF7F8FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val str = viewModel.strings
    val scale = viewModel.fontSizeScale

    val themeColors = listOf(
        Color(0xFF2555FF),
        Color(0xFF10B981),
        Color(0xFFC2410C),
        Color(0xFF7C3AED)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(bgColor).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(str.navSettings, fontSize = (26 * scale).sp, fontWeight = FontWeight.Bold, color = textColor)
                Surface(
                    modifier = Modifier.size(42.dp).clickable { viewModel.navigateTo(AppScreen.PROFILE) },
                    shape = CircleShape,
                    color = Color(0xFFE8F0FE)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(viewModel.userProfile.getShortName(), color = viewModel.primaryColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Mục GIAO DIỆN: Sáng / Tối / Tự động + Màu chủ đạo + Cỡ chữ
        item {
            Text(str.sectionAppearance, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(18.dp), color = cardColor, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ThemeMode.entries.forEach { mode ->
                            val isSelected = (viewModel.themeMode == mode)
                            Column(
                                modifier = Modifier.weight(1f).clickable { viewModel.themeMode = mode },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth().height(68.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) viewModel.primaryColor else Color.LightGray.copy(alpha = 0.4f)),
                                    color = when (mode) {
                                        ThemeMode.LIGHT -> Color(0xFFF2F4F7)
                                        ThemeMode.DARK -> Color(0xFF121212)
                                        ThemeMode.AUTO -> Color(0xFFF2F4F7)
                                    }
                                ) {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        Column(
                                            modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(modifier = Modifier.width(28.dp).height(6.dp).background(if (mode == ThemeMode.DARK) Color.DarkGray else Color.LightGray, RoundedCornerShape(3.dp)))
                                            Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(if (mode == ThemeMode.DARK) Color(0xFF2C2C2C) else Color.White, RoundedCornerShape(4.dp)))
                                            Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(if (mode == ThemeMode.DARK) Color(0xFF2C2C2C) else Color.White, RoundedCornerShape(4.dp)))
                                        }
                                        if (mode == ThemeMode.AUTO) {
                                            Column(
                                                modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF121212)).padding(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp)))
                                                Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp)))
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = mode.getName(viewModel.appLanguage),
                                    fontSize = (13 * scale).sp,
                                    color = if (isSelected) viewModel.primaryColor else Color.Gray,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.25f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(str.primaryColorLabel, fontSize = (15 * scale).sp, fontWeight = FontWeight.Medium, color = textColor)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            themeColors.forEach { c ->
                                val isCurrent = (viewModel.primaryColor == c)
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (isCurrent) 2.5.dp else 0.dp,
                                            color = if (isCurrent) c else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .padding(if (isCurrent) 4.dp else 0.dp)
                                        .clip(CircleShape)
                                        .background(c)
                                        .clickable { viewModel.primaryColor = c }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.25f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(str.fontSizeLabel, fontSize = (15 * scale).sp, fontWeight = FontWeight.Medium, color = textColor, modifier = Modifier.weight(1f))
                        Text("A", fontSize = 12.sp, color = Color.Gray)
                        Slider(
                            value = viewModel.fontSizeScale,
                            onValueChange = { viewModel.fontSizeScale = it },
                            valueRange = 0.85f..1.2f,
                            modifier = Modifier.width(130.dp).padding(horizontal = 6.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = viewModel.primaryColor
                            )
                        )
                        Text("A", fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Mục THÔNG BÁO
        item {
            Text(str.sectionNotification, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(18.dp), color = cardColor, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingSwitchRowFull(
                        title = str.remindTitle,
                        subtitle = str.remindSub,
                        checked = viewModel.notifyReminder,
                        textColor = textColor,
                        scale = scale,
                        onCheckedChange = { viewModel.notifyReminder = it }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.25f))
                    SettingSwitchRowFull(
                        title = str.soundTitle,
                        subtitle = str.soundSub,
                        checked = viewModel.notifySound,
                        textColor = textColor,
                        scale = scale,
                        onCheckedChange = { viewModel.notifySound = it }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.25f))
                    SettingSwitchRowFull(
                        title = str.summaryTitle,
                        subtitle = str.summarySub,
                        checked = viewModel.notifySummary,
                        textColor = textColor,
                        scale = scale,
                        onCheckedChange = { viewModel.notifySummary = it }
                    )
                }
            }
        }

        // Mục CHUNG -> Ngôn ngữ
        item {
            Text(str.sectionGeneral, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = cardColor,
                modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleLanguage() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(str.languageLabel, fontSize = (15 * scale).sp, fontWeight = FontWeight.Medium, color = textColor)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(viewModel.appLanguage.displayName, fontSize = (14 * scale).sp, color = viewModel.primaryColor, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingSwitchRowFull(
    title: String,
    subtitle: String,
    checked: Boolean,
    textColor: Color,
    scale: Float,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = (15 * scale).sp, fontWeight = FontWeight.Medium, color = textColor)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = (12 * scale).sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF34C759))
        )
    }
}

// ================== 2. MÀN HÌNH HỒ SƠ CÁ NHÂN (PROFILE SCREEN) ==================
@Composable
fun ProfileScreen(viewModel: TodoViewModel) {
    val profile = viewModel.userProfile
    val str = viewModel.strings
    val weekStats = viewModel.getCurrentWeekStats()
    val selectedDay = weekStats[viewModel.selectedDayIndex]

    if (viewModel.showGoogleLoginDialog) {
        GoogleAccountPickerDialog(
            primaryColor = viewModel.primaryColor,
            onDismiss = { viewModel.showGoogleLoginDialog = false },
            onConfirm = { name, email -> viewModel.connectGoogleAccount(name, email) }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF7F8FA)).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.SETTINGS) }
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = viewModel.primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(str.navSettings, color = viewModel.primaryColor, fontSize = 16.sp)
                }
                Text(str.profileTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = str.editBtn,
                    color = viewModel.primaryColor,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { viewModel.showGoogleLoginDialog = true }
                )
            }
        }

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(modifier = Modifier.size(84.dp), shape = CircleShape, color = Color(0xFFE8F0FE)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(profile.getShortName(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = viewModel.primaryColor)
                        }
                    }
                    Surface(modifier = Modifier.size(28.dp), shape = CircleShape, color = viewModel.primaryColor) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(profile.fullName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(profile.email, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = if (profile.isGoogleLinked) Color(0xFFE6F4EA) else Color(0xFFFEF0E6),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable { viewModel.showGoogleLoginDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (profile.isGoogleLinked) Icons.Default.Check else Icons.Default.Link,
                            contentDescription = null,
                            tint = if (profile.isGoogleLinked) Color(0xFF137333) else Color(0xFFD96B27),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (profile.isGoogleLinked) str.googleLinkedText else str.googleUnlinkedText,
                            fontSize = 12.sp,
                            color = if (profile.isGoogleLinked) Color(0xFF137333) else Color(0xFFD96B27),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCardBox(Modifier.weight(1f), "${profile.completedCount}", str.statDone)
                StatCardBox(Modifier.weight(1f), "${profile.streakDays} ngày", str.statStreak)
                StatCardBox(Modifier.weight(1f), "${profile.onTimePercent}%", str.statOnTime)
            }
        }

        item {
            val totalWeekCompleted = weekStats.sumOf { it.completedTasks }
            val totalWeekAll = weekStats.sumOf { it.totalTasks }

            Surface(shape = RoundedCornerShape(18.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { viewModel.isViewingThisWeek = !viewModel.isViewingThisWeek }
                        ) {
                            Text(
                                text = if (viewModel.isViewingThisWeek) str.thisWeek else str.lastWeek,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = viewModel.primaryColor, modifier = Modifier.size(18.dp))
                        }
                        Text("$totalWeekCompleted/$totalWeekAll ${str.tasksUnit}", fontSize = 13.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ngày ${selectedDay.getLabel(viewModel.appLanguage)}: Đã xong ${selectedDay.completedTasks}/${selectedDay.totalTasks} công việc",
                        fontSize = 12.sp,
                        color = viewModel.primaryColor,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().height(96.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        for (i in weekStats.indices) {
                            val dayStat = weekStats[i]
                            val isSelectedDay = (i == viewModel.selectedDayIndex)
                            val barHeight = (dayStat.completedTasks * 12 + 12).dp

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { viewModel.selectedDayIndex = i }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(26.dp)
                                        .height(barHeight)
                                        .clip(RoundedCornerShape(7.dp))
                                        .background(
                                            when {
                                                isSelectedDay -> viewModel.primaryColor
                                                i == 0 -> viewModel.primaryColor.copy(alpha = 0.35f)
                                                else -> Color(0xFFE5E7EB)
                                            }
                                        )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = dayStat.getLabel(viewModel.appLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelectedDay) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelectedDay) Color.Black else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(18.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            viewModel.userProfile = UserProfile(
                                profile.fullName, profile.email, profile.isGoogleLinked,
                                profile.completedCount, profile.streakDays, profile.onTimePercent,
                                if (str.isVi) "Vừa xong" else "Just now"
                            )
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Sync, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(str.syncData, fontSize = 15.sp)
                        }
                        Text(profile.lastSyncTime, fontSize = 13.sp, color = Color.Gray)
                    }

                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(str.privacyPolicy, fontSize = 15.sp)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth().height(52.dp).clickable { viewModel.logout() },
                shape = RoundedCornerShape(14.dp),
                color = Color.White
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFE02424), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(str.logoutBtn, color = Color(0xFFE02424), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun StatCardBox(modifier: Modifier, valueText: String, labelText: String) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), color = Color.White) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(valueText, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(labelText, fontSize = 12.sp, color = Color.Gray)
        }
    }
}