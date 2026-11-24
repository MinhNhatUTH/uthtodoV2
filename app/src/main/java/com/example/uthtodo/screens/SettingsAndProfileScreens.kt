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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.navigation.NavHostController
import com.example.uthtodo.model.ThemeMode
import com.example.uthtodo.navigation.Routes
import com.example.uthtodo.navigation.goAndClearBackStack
import com.example.uthtodo.ui.theme.AppColors
import com.example.uthtodo.viewmodel.TodoViewModel

// 6. MÀN HÌNH CÀI ĐẶT (ĐỔI GIAO DIỆN)
@Composable
fun SettingsScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings
    val primary = viewModel.primaryColor

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(colors.background).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(str.navSettings, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Surface(
                    modifier = Modifier.size(42.dp).clickable { navController.navigate(Routes.PROFILE) { launchSingleTop = true } },
                    shape = CircleShape,
                    color = colors.avatar
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(viewModel.userProfile.getShortName(), color = primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // GIAO DIỆN: Sáng / Tối / Tự động, màu chủ đạo, cỡ chữ
        item {
            Text(str.sectionAppearance, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.subText)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(18.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ThemeMode.entries.forEach { mode ->
                            val isSelected = viewModel.themeMode == mode
                            Column(
                                modifier = Modifier.weight(1f).clickable { viewModel.themeMode = mode },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ThemePreviewBox(mode, isSelected, primary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = mode.getName(viewModel.appLanguage),
                                    fontSize = 13.sp,
                                    color = if (isSelected) primary else colors.subText,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = colors.softDivider)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(str.primaryColorLabel, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AppColors.primaryOptions.forEach { c ->
                                val isCurrent = viewModel.primaryColor == c
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .border(if (isCurrent) 2.5.dp else 0.dp, if (isCurrent) c else Color.Transparent, CircleShape)
                                        .padding(if (isCurrent) 4.dp else 0.dp)
                                        .clip(CircleShape)
                                        .background(c)
                                        .clickable { viewModel.primaryColor = c }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = colors.softDivider)

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(str.fontSizeLabel, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                        Text("A", fontSize = 12.sp, color = colors.subText)
                        Slider(
                            value = viewModel.fontSizeScale,
                            onValueChange = { viewModel.fontSizeScale = it },
                            valueRange = 0.85f..1.2f,
                            modifier = Modifier.width(130.dp).padding(horizontal = 6.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = primary,
                                activeTrackColor = primary,
                                inactiveTrackColor = colors.segmentTrack
                            )
                        )
                        Text("A", fontSize = 18.sp, color = colors.subText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // THÔNG BÁO
        item {
            Text(str.sectionNotification, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.subText)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(18.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingSwitchRowFull(str.remindTitle, str.remindSub, viewModel.notifyReminder, colors) { viewModel.notifyReminder = it }
                    HorizontalDivider(color = colors.softDivider)
                    SettingSwitchRowFull(str.soundTitle, str.soundSub, viewModel.notifySound, colors) { viewModel.notifySound = it }
                    HorizontalDivider(color = colors.softDivider)
                    SettingSwitchRowFull(str.summaryTitle, str.summarySub, viewModel.notifySummary, colors) { viewModel.notifySummary = it }
                }
            }
        }

        // CHUNG: Ngôn ngữ
        item {
            Text(str.sectionGeneral, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.subText)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = colors.card,
                modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleLanguage() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(str.languageLabel, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(viewModel.appLanguage.displayName, fontSize = 14.sp, color = primary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = colors.subText)
                    }
                }
            }
        }
    }
}

// Khung xem trước giao diện (luôn vẽ đúng màu Sáng / Tối, không phụ thuộc chế độ đang chọn)
@Composable
private fun ThemePreviewBox(mode: ThemeMode, isSelected: Boolean, primary: Color) {
    val light = AppColors(false)
    val dark = AppColors(true)
    Surface(
        modifier = Modifier.fillMaxWidth().height(68.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) primary else light.inactiveBar),
        color = if (mode == ThemeMode.DARK) dark.bottomBar else light.pill
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            if (mode == ThemeMode.DARK) {
                PreviewHalf(dark.bottomBar, dark.divider, dark.card, Modifier.weight(1f))
            } else {
                PreviewHalf(light.pill, light.inactiveBar, light.card, Modifier.weight(1f))
            }
            if (mode == ThemeMode.AUTO) {
                PreviewHalf(dark.bottomBar, dark.divider, dark.card, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PreviewHalf(bg: Color, header: Color, row: Color, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxHeight().background(bg).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(modifier = Modifier.width(28.dp).height(6.dp).background(header, RoundedCornerShape(3.dp)))
        Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(row, RoundedCornerShape(4.dp)))
        Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(row, RoundedCornerShape(4.dp)))
    }
}

@Composable
fun SettingSwitchRowFull(
    title: String,
    subtitle: String,
    checked: Boolean,
    colors: AppColors,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = colors.subText)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = colors.switchOn,
                checkedThumbColor = Color.White,
                checkedBorderColor = Color.Transparent
            )
        )
    }
}

//  10. MÀN HÌNH HỒ SƠ CÁ NHÂN
@Composable
fun ProfileScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings
    val primary = viewModel.primaryColor
    val profile = viewModel.userProfile
    val weekStats = viewModel.getCurrentWeekStats()
    val selectedIndex = viewModel.selectedDayIndex.coerceIn(0, weekStats.lastIndex)
    val selectedDay = weekStats[selectedIndex]

    if (viewModel.showGoogleLoginDialog) {
        GoogleAccountPickerDialog(
            primaryColor = primary,
            str = str,
            onDismiss = { viewModel.showGoogleLoginDialog = false },
            onConfirm = { name, email -> viewModel.connectGoogleAccount(name, email) }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(colors.background).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Thanh trên: quay lại, tiêu đề, Sửa
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(str.navSettings, color = primary, fontSize = 16.sp)
                }
                Text(str.profileTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(str.editBtn, color = primary, fontSize = 16.sp, modifier = Modifier.clickable { viewModel.showGoogleLoginDialog = true })
            }
        }

        // Avatar, tên, trạng thái Google
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(modifier = Modifier.size(84.dp), shape = CircleShape, color = colors.avatar) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(profile.getShortName(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = primary)
                        }
                    }
                    Surface(modifier = Modifier.size(28.dp), shape = CircleShape, color = primary) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(profile.fullName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(profile.email, fontSize = 14.sp, color = colors.subText)
                Spacer(modifier = Modifier.height(8.dp))

                val badgeColor = if (profile.isGoogleLinked) colors.success else colors.orange
                Surface(
                    color = if (profile.isGoogleLinked) colors.successBg else colors.orangeSoft,
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
                            tint = badgeColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (profile.isGoogleLinked) str.googleLinkedText else str.googleUnlinkedText,
                            fontSize = 12.sp,
                            color = badgeColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 3 thẻ số liệu
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCardBox(Modifier.weight(1f), "${profile.completedCount}", str.statDone, colors)
                StatCardBox(Modifier.weight(1f), "${profile.streakDays} ${str.daysUnit}", str.statStreak, colors)
                StatCardBox(Modifier.weight(1f), "${profile.onTimePercent}%", str.statOnTime, colors)
            }
        }

        // Biểu đồ tuần
        item {
            val totalDone = weekStats.sumOf { it.completedTasks }
            val totalAll = weekStats.sumOf { it.totalTasks }

            Surface(shape = RoundedCornerShape(18.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
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
                            Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = primary, modifier = Modifier.size(18.dp))
                        }
                        Text("$totalDone/$totalAll ${str.tasksUnit}", fontSize = 13.sp, color = colors.subText)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = str.dayProgress(selectedDay.getLabel(viewModel.appLanguage), selectedDay.completedTasks, selectedDay.totalTasks),
                        fontSize = 12.sp,
                        color = primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weekStats.forEachIndexed { i, day ->
                            val isSelectedDay = i == selectedIndex
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { viewModel.selectedDayIndex = i }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(26.dp)
                                        .height((day.completedTasks * 10 + 10).dp)
                                        .clip(RoundedCornerShape(7.dp))
                                        .background(if (isSelectedDay) primary else colors.inactiveBar)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = day.getLabel(viewModel.appLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelectedDay) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelectedDay) colors.text else colors.subText
                                )
                            }
                        }
                    }
                }
            }
        }

        // Đồng bộ + Quyền riêng tư
        item {
            Surface(shape = RoundedCornerShape(18.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            viewModel.userProfile = profile.copy(lastSyncTime = str.justNow)
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Sync, contentDescription = null, tint = colors.subText, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(str.syncData, fontSize = 15.sp)
                        }
                        Text(profile.lastSyncTime, fontSize = 13.sp, color = colors.subText)
                    }

                    HorizontalDivider(color = colors.divider)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = colors.subText, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(str.privacyPolicy, fontSize = 15.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = colors.subText)
                    }
                }
            }
        }

        // Đăng xuất: xóa hết lịch sử, về màn Đăng nhập
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().height(52.dp).clickable {
                    viewModel.logout()
                    navController.goAndClearBackStack(Routes.LOGIN)
                },
                shape = RoundedCornerShape(14.dp),
                color = colors.card
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = colors.danger, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(str.logoutBtn, color = colors.danger, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun StatCardBox(modifier: Modifier, valueText: String, labelText: String, colors: AppColors) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), color = colors.card) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(valueText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(labelText, fontSize = 12.sp, color = colors.subText)
        }
    }
}