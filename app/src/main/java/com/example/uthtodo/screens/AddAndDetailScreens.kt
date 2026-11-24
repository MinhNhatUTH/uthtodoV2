// FILE: AddAndDetailScreens.kt - Màn 4 Thêm việc mới, 5 Chi tiết công việc
package com.example.uthtodo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uthtodo.model.TaskCategory
import com.example.uthtodo.model.TaskPriority
import com.example.uthtodo.navigation.Routes
import com.example.uthtodo.navigation.goToTab
import com.example.uthtodo.ui.theme.AppColors
import com.example.uthtodo.viewmodel.TodoViewModel

// ===== 4. MÀN HÌNH THÊM VIỆC MỚI =====
@Composable
fun AddTaskScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings
    val primary = viewModel.primaryColor
    val lang = viewModel.appLanguage

    var title by remember { mutableStateOf("Ôn chương 4 – Giải thuật") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(TaskCategory.STUDY) }
    var dateText by remember { mutableStateOf("Thứ Tư, 21/10") }
    var timeText by remember { mutableStateOf("19:00") }
    var remind by remember { mutableStateOf(true) }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }

    // Lưu thành công thì đóng màn rồi chuyển sang tab Danh sách
    val save = {
        val ok = viewModel.addNewTask(title, note, category, dateText, timeText, remind, priority)
        if (ok) {
            navController.popBackStack()
            navController.goToTab(Routes.TASK_LIST)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(colors.background).imePadding().padding(20.dp)) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(str.cancel, color = primary, fontSize = 16.sp, modifier = Modifier.clickable { navController.popBackStack() })
                Text(str.newTask, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(str.save, color = primary, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { save() })
            }

            // Thẻ 2 ô nhập, ngăn cách bằng đường kẻ
            Surface(shape = RoundedCornerShape(16.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Text(str.taskTitleLabel, fontSize = 12.sp, color = colors.subText)
                        Spacer(modifier = Modifier.height(2.dp))
                        BasicTextField(
                            value = title,
                            onValueChange = { title = it },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = colors.text),
                            cursorBrush = SolidColor(primary),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    HorizontalDivider(color = colors.divider)
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Text(str.noteLabel, fontSize = 12.sp, color = colors.subText)
                        Spacer(modifier = Modifier.height(2.dp))
                        BasicTextField(
                            value = note,
                            onValueChange = { note = it },
                            textStyle = TextStyle(fontSize = 14.sp, color = colors.text),
                            cursorBrush = SolidColor(primary),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { inner ->
                                if (note.isEmpty()) Text(str.notePlaceholder, fontSize = 14.sp, color = colors.subText)
                                inner()
                            }
                        )
                    }
                }
            }

            Text(str.categoryLabel, fontSize = 13.sp, color = colors.subText, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TaskCategory.entries.forEach { cat ->
                    val isSelected = category == cat
                    Surface(
                        modifier = Modifier.weight(1f).height(44.dp).clickable { category = cat },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) colors.categorySelected else colors.card,
                        border = BorderStroke(1.5.dp, if (isSelected) primary else Color.Transparent)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(cat.getName(lang), color = if (isSelected) primary else colors.text, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Ngày, giờ, nhắc nhở
            Surface(shape = RoundedCornerShape(16.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PillRow(Icons.Default.CalendarToday, str.dateLabel, dateText, colors)
                    HorizontalDivider(color = colors.divider)
                    PillRow(Icons.Default.Schedule, str.timeLabel, timeText, colors)
                    HorizontalDivider(color = colors.divider)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = colors.subText, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(str.remindMe, fontSize = 15.sp)
                                Text(str.remind15, fontSize = 12.sp, color = colors.subText)
                            }
                        }
                        Switch(
                            checked = remind,
                            onCheckedChange = { remind = it },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = colors.switchOn,
                                checkedThumbColor = Color.White,
                                checkedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            Text(str.priorityLabel, fontSize = 13.sp, color = colors.subText, fontWeight = FontWeight.Medium)
            Surface(shape = RoundedCornerShape(12.dp), color = colors.segmentTrack, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(4.dp)) {
                    TaskPriority.entries.forEach { p ->
                        val isSelected = priority == p
                        Surface(
                            modifier = Modifier.weight(1f).height(36.dp).clickable { priority = p },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) colors.segmentSelected else Color.Transparent
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(p.getName(lang), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { save() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary)
        ) {
            Text(str.saveTask, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
private fun PillRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, colors: AppColors) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = colors.subText, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(label, fontSize = 15.sp)
        }
        Surface(color = colors.pill, shape = RoundedCornerShape(8.dp)) {
            Text(value, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
        }
    }
}

// ===== 5. MÀN HÌNH CHI TIẾT CÔNG VIỆC =====
@Composable
fun TaskDetailScreen(viewModel: TodoViewModel, navController: NavHostController, taskId: Int) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings
    val primary = viewModel.primaryColor
    val lang = viewModel.appLanguage
    val task = viewModel.getTask(taskId)
    var newSubTitle by remember { mutableStateOf("") }

    // Việc vừa bị xóa: chỉ vẽ nền trống trong lúc màn đang đóng (không tự pop thêm lần nữa)
    if (task == null) {
        Box(modifier = Modifier.fillMaxSize().background(colors.background))
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(colors.background).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(str.navList, color = primary, fontSize = 16.sp)
                }
                // TODO: chức năng sửa công việc
                Text(str.editBtn, color = primary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(color = colors.categoryBg(task.category), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        task.category.getName(lang), color = colors.categoryText(task.category),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Surface(color = colors.priorityBg(task.priority), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        str.priorityBadge(task.priority.getName(lang)), color = colors.priorityText(task.priority),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(task.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Surface(shape = RoundedCornerShape(16.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow(str.deadline, "${task.date}, ${task.time}", colors)
                    HorizontalDivider(color = colors.divider)
                    InfoRow(str.reminder, if (task.hasReminder) task.reminderText else str.reminderOff, colors)
                    HorizontalDivider(color = colors.divider)
                    InfoRow(str.repeat, str.repeatNone, colors)
                }
            }
        }

        item {
            Text(str.notes, fontSize = 13.sp, color = colors.subText, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Surface(shape = RoundedCornerShape(16.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = task.note.ifBlank { str.noNotes },
                    fontSize = 14.sp,
                    color = colors.mutedText,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Việc nhỏ
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(str.subtasks, fontSize = 13.sp, color = colors.subText, fontWeight = FontWeight.Medium)
                Text("${task.countCompletedSubTasks()}/${task.subTasks.size}", fontSize = 13.sp, color = colors.subText, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { task.calculateSubTaskProgress() },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = primary,
                trackColor = colors.segmentTrack
            )
            Spacer(modifier = Modifier.height(10.dp))

            Surface(shape = RoundedCornerShape(16.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    for (sub in task.subTasks) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = sub.isDone,
                                onCheckedChange = { viewModel.toggleSubTask(task.id, sub.id) },
                                colors = CheckboxDefaults.colors(checkedColor = primary, uncheckedColor = colors.inactive)
                            )
                            Text(
                                text = sub.title,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                color = if (sub.isDone) colors.subText else colors.text,
                                textDecoration = if (sub.isDone) TextDecoration.LineThrough else TextDecoration.None
                            )
                            // TODO: sửa việc nhỏ
                            IconButton(onClick = { }, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Outlined.Edit, contentDescription = null, tint = colors.subText, modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = { viewModel.deleteSubTask(task.id, sub.id) }, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = colors.danger, modifier = Modifier.size(18.dp))
                            }
                        }
                        HorizontalDivider(color = colors.divider)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp)) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = primary)
                        TextField(
                            value = newSubTitle,
                            onValueChange = { newSubTitle = it },
                            placeholder = { Text(str.addSubtask, fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = colors.text,
                                unfocusedTextColor = colors.text,
                                focusedPlaceholderColor = colors.subText,
                                unfocusedPlaceholderColor = colors.subText,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        Text(
                            text = str.addBtn,
                            color = primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                viewModel.addSubTask(task.id, newSubTitle)
                                newSubTitle = ""
                            }
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    viewModel.toggleTaskCompleted(task.id)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) {
                Text(if (task.isCompleted) str.markUndone else str.markDone, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = str.deleteTask,
                    color = colors.danger,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        viewModel.deleteTask(task.id)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, colors: AppColors) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = colors.subText)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}