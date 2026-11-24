// FILE: HomeAndListScreens.kt - Màn 2 Trang chủ, 3 Danh sách, 8 Thông báo
package com.example.uthtodo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uthtodo.model.TaskCategory
import com.example.uthtodo.model.TaskItem
import com.example.uthtodo.model.TaskPriority
import com.example.uthtodo.navigation.Routes
import com.example.uthtodo.navigation.goToTab
import com.example.uthtodo.ui.theme.AppColors
import com.example.uthtodo.viewmodel.TodoViewModel

// ===== 2. MÀN HÌNH TRANG CHỦ (7 = giao diện tối của màn này) =====
@Composable
fun HomeScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings
    val hasUnread = viewModel.notificationList.any { !it.isRead }
    val percent = (viewModel.todayProgress * 100).toInt()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(colors.background).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Lời chào + chuông
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(46.dp).clickable { navController.navigate(Routes.PROFILE) { launchSingleTop = true } },
                        shape = CircleShape,
                        color = colors.avatar
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(viewModel.userProfile.getShortName(), color = viewModel.primaryColor, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(str.dateSample, fontSize = 12.sp, color = colors.subText)
                        Text("${str.greetingPrefix} ${viewModel.userProfile.fullName}!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box {
                    Surface(
                        shape = CircleShape,
                        color = colors.card,
                        modifier = Modifier.size(42.dp).clickable { navController.goToTab(Routes.NOTIFICATIONS) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.NotificationsNone, contentDescription = null)
                        }
                    }
                    if (hasUnread) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 9.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(colors.danger)
                        )
                    }
                }
            }
        }

        // Thẻ tiến độ hôm nay
        item {
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = viewModel.primaryColor) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(str.todayProgress, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("${viewModel.todayDoneCount}/${viewModel.todayTasks.size} ${str.tasksUnit}", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(str.startSmallHint, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                    }
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { viewModel.todayProgress },
                            modifier = Modifier.size(66.dp),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.25f),
                            strokeWidth = 6.dp
                        )
                        Text("$percent%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }

        // 2 nhóm
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                CategoryCard(Modifier.weight(1f), TaskCategory.STUDY, Icons.Default.School, viewModel, colors)
                CategoryCard(Modifier.weight(1f), TaskCategory.LIFE, Icons.Default.Home, viewModel, colors)
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(str.todayTasksTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = str.seeAll,
                    fontSize = 14.sp,
                    color = viewModel.primaryColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { navController.goToTab(Routes.TASK_LIST) }
                )
            }
        }

        items(viewModel.todayTasks, key = { it.id }) { task ->
            TaskCardItem(
                task = task,
                viewModel = viewModel,
                colors = colors,
                showDate = false,
                onCheckClick = { viewModel.toggleTaskCompleted(task.id) },
                onItemClick = { navController.navigate(Routes.taskDetail(task.id)) }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    modifier: Modifier,
    category: TaskCategory,
    icon: ImageVector,
    viewModel: TodoViewModel,
    colors: AppColors
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = colors.card) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(modifier = Modifier.size(38.dp), shape = RoundedCornerShape(10.dp), color = colors.categoryBg(category)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = colors.categoryText(category), modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(category.getName(viewModel.appLanguage), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "${viewModel.countRemainingByCategory(category)} ${viewModel.strings.remainingTasks}",
                fontSize = 13.sp,
                color = colors.subText
            )
        }
    }
}

// Thẻ một công việc, dùng ở Trang chủ và Danh sách
@Composable
fun TaskCardItem(
    task: TaskItem,
    viewModel: TodoViewModel,
    colors: AppColors,
    showDate: Boolean,
    onCheckClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onItemClick() },
        shape = RoundedCornerShape(16.dp),
        color = colors.card
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onCheckClick, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = null,
                    tint = if (task.isCompleted) viewModel.primaryColor else colors.inactive,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.isCompleted) colors.subText else colors.text,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = colors.categoryBg(task.category), shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = task.category.getName(viewModel.appLanguage),
                            color = colors.categoryText(task.category),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (showDate) "${task.date}, ${task.time}" else task.time,
                        fontSize = 12.sp,
                        color = colors.subText
                    )
                }
            }
            if (task.priority == TaskPriority.HIGH && !task.isCompleted) {
                Surface(color = colors.dangerBg, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = viewModel.strings.urgentBadge,
                        color = colors.danger,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            } else {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = colors.inactive)
            }
        }
    }
}

// ===== 3. MÀN HÌNH DANH SÁCH VIỆC =====
@Composable
fun TaskListScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings
    val lang = viewModel.appLanguage
    val uncompleted = viewModel.getFilteredTasks(wantCompleted = false)
    val completed = viewModel.getFilteredTasks(wantCompleted = true)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(colors.background).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(str.navList, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("${uncompleted.size} ${str.unfinishedTasks}", fontSize = 13.sp, color = colors.subText)
            }
        }

        item {
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.searchQuery = it },
                placeholder = { Text(str.searchPlaceholder, color = colors.subText) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = colors.subText) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colors.pill,
                    unfocusedContainerColor = colors.pill,
                    focusedTextColor = colors.text,
                    unfocusedTextColor = colors.text,
                    cursorColor = viewModel.primaryColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChipItem(str.filterAll, viewModel.selectedFilter == null, colors) { viewModel.selectedFilter = null }
                TaskCategory.entries.forEach { cat ->
                    FilterChipItem(cat.getName(lang), viewModel.selectedFilter == cat, colors) { viewModel.selectedFilter = cat }
                }
            }
        }

        item { Text(str.sectionTodo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.subText) }
        items(uncompleted, key = { it.id }) { task ->
            TaskCardItem(
                task, viewModel, colors, showDate = true,
                onCheckClick = { viewModel.toggleTaskCompleted(task.id) },
                onItemClick = { navController.navigate(Routes.taskDetail(task.id)) }
            )
        }

        if (completed.isNotEmpty()) {
            item { Text(str.sectionDone, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.subText) }
            items(completed, key = { it.id }) { task ->
                TaskCardItem(
                    task, viewModel, colors, showDate = true,
                    onCheckClick = { viewModel.toggleTaskCompleted(task.id) },
                    onItemClick = { navController.navigate(Routes.taskDetail(task.id)) }
                )
            }
        }
    }
}

@Composable
fun FilterChipItem(label: String, isSelected: Boolean, colors: AppColors, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = colors.chipBg(isSelected)
    ) {
        Text(
            text = label,
            color = colors.chipText(isSelected),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

// ===== 8. MÀN HÌNH THÔNG BÁO =====
@Composable
fun NotificationScreen(viewModel: TodoViewModel) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(colors.background).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(str.navNotify, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = str.markAllRead,
                    color = viewModel.primaryColor,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { viewModel.markAllNotificationsRead() }
                )
            }
        }

        items(viewModel.notificationList, key = { it.id }) { item ->
            Surface(shape = RoundedCornerShape(16.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (item.isRead) colors.inactiveBar else viewModel.primaryColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = if (item.isRead) colors.subText else viewModel.primaryColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.message, fontSize = 13.sp, color = colors.subText)
                    }
                    Text(item.time, fontSize = 11.sp, color = colors.subText)
                }
            }
        }
    }
}