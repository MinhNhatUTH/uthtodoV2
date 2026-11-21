package com.example.uthtodo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthtodo.model.AppScreen
import com.example.uthtodo.model.TaskCategory
import com.example.uthtodo.model.TaskItem
import com.example.uthtodo.model.TaskPriority
import com.example.uthtodo.viewmodel.TodoViewModel

// ================== 1. MÀN HÌNH TRANG CHỦ (HOME SCREEN) ==================
@Composable
fun HomeScreen(viewModel: TodoViewModel) {
    val isDark = viewModel.isDarkTheme()
    val bgColor = if (isDark) Color(0xFF0D0D0D) else Color(0xFFF7F8FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val str = viewModel.strings
    val scale = viewModel.fontSizeScale

    val totalCount = viewModel.taskList.size
    val doneCount = viewModel.taskList.count { it.isCompleted }
    val progressRatio = if (totalCount > 0) doneCount.toFloat() / totalCount.toFloat() else 0f
    val percentNumber = (progressRatio * 100).toInt()

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(46.dp).clickable { viewModel.navigateTo(AppScreen.PROFILE) },
                        shape = CircleShape,
                        color = if (isDark) Color(0xFF253459) else Color(0xFFE8F0FE)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(viewModel.userProfile.getShortName(), color = viewModel.primaryColor, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(str.dateSample, fontSize = (12 * scale).sp, color = Color.Gray)
                        Text("${str.greetingPrefix} ${viewModel.userProfile.fullName}!", fontSize = (20 * scale).sp, fontWeight = FontWeight.Bold, color = textColor)
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = cardColor,
                    modifier = Modifier.size(42.dp).clickable { viewModel.navigateTo(AppScreen.NOTIFICATIONS) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = textColor)
                    }
                }
            }
        }

        item {
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = viewModel.primaryColor) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(str.todayProgress, color = Color.White.copy(alpha = 0.85f), fontSize = (13 * scale).sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("$doneCount/$totalCount ${str.tasksUnit}", color = Color.White, fontSize = (26 * scale).sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(str.startSmallHint, color = Color.White.copy(alpha = 0.85f), fontSize = (13 * scale).sp)
                    }
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier.size(66.dp),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.25f),
                            strokeWidth = 6.dp
                        )
                        Text("$percentNumber%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(18.dp), color = cardColor) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(modifier = Modifier.size(38.dp), shape = RoundedCornerShape(10.dp), color = if (isDark) Color(0xFF1E2942) else Color(0xFFE8F0FE)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF2555FF), modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(TaskCategory.STUDY.getName(viewModel.appLanguage), fontWeight = FontWeight.Bold, fontSize = (16 * scale).sp, color = textColor)
                        Text("${viewModel.countRemainingByCategory(TaskCategory.STUDY)} ${str.remainingTasks}", fontSize = (13 * scale).sp, color = Color.Gray)
                    }
                }
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(18.dp), color = cardColor) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(modifier = Modifier.size(38.dp), shape = RoundedCornerShape(10.dp), color = if (isDark) Color(0xFF38261A) else Color(0xFFFEF0E6)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFFD96B27), modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(TaskCategory.LIFE.getName(viewModel.appLanguage), fontWeight = FontWeight.Bold, fontSize = (16 * scale).sp, color = textColor)
                        Text("${viewModel.countRemainingByCategory(TaskCategory.LIFE)} ${str.remainingTasks}", fontSize = (13 * scale).sp, color = Color.Gray)
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(str.todayTasksTitle, fontSize = (18 * scale).sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(
                    text = str.seeAll,
                    fontSize = (14 * scale).sp,
                    color = viewModel.primaryColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.TASK_LIST) }
                )
            }
        }

        items(viewModel.taskList) { task ->
            TaskCardItem(
                task = task,
                viewModel = viewModel,
                cardColor = cardColor,
                textColor = textColor,
                onCheckClick = { viewModel.toggleTaskCompleted(task.id) },
                onItemClick = { viewModel.openTaskDetail(task.id) }
            )
        }
    }
}

@Composable
fun TaskCardItem(
    task: TaskItem,
    viewModel: TodoViewModel,
    cardColor: Color,
    textColor: Color,
    onCheckClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val scale = viewModel.fontSizeScale
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onItemClick() },
        shape = RoundedCornerShape(16.dp),
        color = cardColor
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onCheckClick, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = null,
                    tint = if (task.isCompleted) viewModel.primaryColor else Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = (15 * scale).sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.isCompleted) Color.Gray else textColor,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = task.category.badgeColor, shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = task.category.getName(viewModel.appLanguage),
                            color = task.category.textColor,
                            fontSize = (11 * scale).sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${task.date}, ${task.time}", fontSize = (12 * scale).sp, color = Color.Gray)
                }
            }
            if (task.priority == TaskPriority.HIGH && !task.isCompleted) {
                Surface(color = Color(0xFFFDE8E8), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = viewModel.strings.urgentBadge,
                        color = Color(0xFFE02424),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            } else {
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
            }
        }
    }
}

// ================== 2. MÀN HÌNH DANH SÁCH (TASK LIST SCREEN) ==================
@Composable
fun TaskListScreen(viewModel: TodoViewModel) {
    val isDark = viewModel.isDarkTheme()
    val bgColor = if (isDark) Color(0xFF0D0D0D) else Color(0xFFF7F8FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val str = viewModel.strings
    val scale = viewModel.fontSizeScale

    val uncompletedTasks = viewModel.getFilteredTasks(wantCompleted = false)
    val completedTasks = viewModel.getFilteredTasks(wantCompleted = true)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(bgColor).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text(str.navList, fontSize = (26 * scale).sp, fontWeight = FontWeight.Bold, color = textColor)
                Text("${uncompletedTasks.size} ${str.remainingTasks}", fontSize = (13 * scale).sp, color = Color.Gray)
            }
        }

        item {
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.searchQuery = it },
                placeholder = { Text(str.searchPlaceholder, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cardColor,
                    unfocusedContainerColor = cardColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChipItem(str.filterAll, viewModel.selectedFilter == null) { viewModel.selectedFilter = null }
                FilterChipItem(TaskCategory.STUDY.getName(viewModel.appLanguage), viewModel.selectedFilter == TaskCategory.STUDY) { viewModel.selectedFilter = TaskCategory.STUDY }
                FilterChipItem(TaskCategory.LIFE.getName(viewModel.appLanguage), viewModel.selectedFilter == TaskCategory.LIFE) { viewModel.selectedFilter = TaskCategory.LIFE }
            }
        }

        item { Text(str.sectionTodo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray) }
        items(uncompletedTasks) { task ->
            TaskCardItem(task, viewModel, cardColor, textColor, { viewModel.toggleTaskCompleted(task.id) }, { viewModel.openTaskDetail(task.id) })
        }

        if (completedTasks.isNotEmpty()) {
            item { Text(str.sectionDone, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray) }
            items(completedTasks) { task ->
                TaskCardItem(task, viewModel, cardColor, textColor, { viewModel.toggleTaskCompleted(task.id) }, { viewModel.openTaskDetail(task.id) })
            }
        }
    }
}

@Composable
fun FilterChipItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF1A1A1A) else Color.White
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

// ================== 3. MÀN HÌNH THÔNG BÁO (TAB ICON THỨ 4) ==================
@Composable
fun NotificationScreen(viewModel: TodoViewModel) {
    val isDark = viewModel.isDarkTheme()
    val bgColor = if (isDark) Color(0xFF0D0D0D) else Color(0xFFF7F8FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val str = viewModel.strings

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(bgColor).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(str.navNotify, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(
                    text = if (str.isVi) "Đánh dấu đã đọc" else "Mark all read",
                    color = viewModel.primaryColor,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { viewModel.markAllNotificationsRead() }
                )
            }
        }

        items(viewModel.notificationList) { item ->
            Surface(shape = RoundedCornerShape(16.dp), color = cardColor, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (item.isRead) Color.LightGray.copy(alpha = 0.3f) else viewModel.primaryColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = if (item.isRead) Color.Gray else viewModel.primaryColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.message, fontSize = 13.sp, color = Color.Gray)
                    }
                    Text(item.time, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}