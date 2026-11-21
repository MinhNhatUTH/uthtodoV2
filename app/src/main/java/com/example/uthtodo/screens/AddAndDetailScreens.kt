package com.example.uthtodo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthtodo.model.AppScreen
import com.example.uthtodo.model.TaskCategory
import com.example.uthtodo.model.TaskPriority
import com.example.uthtodo.viewmodel.TodoViewModel

// ================== 1. MÀN HÌNH THÊM VIỆC MỚI (ADD TASK SCREEN) ==================
@Composable
fun AddTaskScreen(viewModel: TodoViewModel) {
    var title by remember { mutableStateOf("Ôn chương 4 – Giải thuật") }
    var note by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.STUDY) }
    var dateText by remember { mutableStateOf("Thứ Tư, 21/10") }
    var timeText by remember { mutableStateOf("19:00") }
    var remindSwitch by remember { mutableStateOf(true) }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    val isVi = viewModel.strings.isVi

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF7F8FA)).padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (isVi) "Hủy" else "Cancel", color = viewModel.primaryColor, fontSize = 16.sp, modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.HOME) })
                Text(if (isVi) "Việc mới" else "New Task", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = if (isVi) "Lưu" else "Save",
                    color = viewModel.primaryColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        viewModel.addNewTask(title, note, selectedCategory, dateText, timeText, remindSwitch, selectedPriority)
                    }
                )
            }

            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(if (isVi) "Tên công việc" else "Task title", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(value = title, onValueChange = { title = it }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (isVi) "Ghi chú" else "Note", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        placeholder = { Text(if (isVi) "Thêm ghi chú (không bắt buộc)" else "Add note (optional)", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Text(if (isVi) "Nhóm" else "Category", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TaskCategory.entries.forEach { cat ->
                    val isSelected = (selectedCategory == cat)
                    Surface(
                        modifier = Modifier.weight(1f).height(44.dp).clickable { selectedCategory = cat },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFFE8F0FE) else Color.White,
                        border = BorderStroke(1.5.dp, if (isSelected) viewModel.primaryColor else Color.Transparent)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(cat.getName(viewModel.appLanguage), color = if (isSelected) viewModel.primaryColor else Color.Black, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isVi) "Ngày" else "Date", fontSize = 15.sp)
                        }
                        Surface(color = Color(0xFFF2F4F7), shape = RoundedCornerShape(8.dp)) {
                            Text(dateText, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isVi) "Giờ" else "Time", fontSize = 15.sp)
                        }
                        Surface(color = Color(0xFFF2F4F7), shape = RoundedCornerShape(8.dp)) {
                            Text(timeText, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(if (isVi) "Nhắc tôi" else "Remind me", fontSize = 15.sp)
                                Text(if (isVi) "Trước 15 phút" else "15 mins before", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Switch(checked = remindSwitch, onCheckedChange = { remindSwitch = it })
                    }
                }
            }

            Text(if (isVi) "Mức ưu tiên" else "Priority", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFEAECF0), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(4.dp)) {
                    TaskPriority.entries.forEach { p ->
                        val isSelected = (selectedPriority == p)
                        Surface(
                            modifier = Modifier.weight(1f).height(36.dp).clickable { selectedPriority = p },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color.White else Color.Transparent
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(p.getName(viewModel.appLanguage), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { viewModel.addNewTask(title, note, selectedCategory, dateText, timeText, remindSwitch, selectedPriority) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = viewModel.primaryColor)
        ) {
            Text(if (isVi) "Lưu công việc" else "Save Task", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

// ================== 2. MÀN HÌNH CHI TIẾT CÔNG VIỆC (TASK DETAIL SCREEN) ==================
@Composable
fun TaskDetailScreen(viewModel: TodoViewModel) {
    val task = viewModel.getSelectedTask()
    var newSubTitle by remember { mutableStateOf("") }
    val isVi = viewModel.strings.isVi

    if (task == null) {
        viewModel.navigateTo(AppScreen.TASK_LIST)
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF7F8FA)).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.TASK_LIST) }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = viewModel.primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(viewModel.strings.navList, color = viewModel.primaryColor, fontSize = 16.sp)
                }
                Text(viewModel.strings.editBtn, color = viewModel.primaryColor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(color = task.category.badgeColor, shape = RoundedCornerShape(6.dp)) {
                    Text(task.category.getName(viewModel.appLanguage), color = task.category.textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
                Surface(color = Color(0xFFFDE8E8), shape = RoundedCornerShape(6.dp)) {
                    Text("Ưu tiên ${task.priority.getName(viewModel.appLanguage).lowercase()}", color = Color(0xFFE02424), fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(task.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (isVi) "Hạn chót" else "Deadline", color = Color.Gray)
                        Text("${task.date}, ${task.time}", fontWeight = FontWeight.SemiBold)
                    }
                    HorizontalDivider(color = Color(0xFFF2F2F2))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (isVi) "Nhắc nhở" else "Reminder", color = Color.Gray)
                        Text(if (task.hasReminder) task.reminderText else (if (isVi) "Tắt" else "Off"), fontWeight = FontWeight.SemiBold)
                    }
                    HorizontalDivider(color = Color(0xFFF2F2F2))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (isVi) "Lặp lại" else "Repeat", color = Color.Gray)
                        Text(if (isVi) "Không" else "None", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        item {
            Text(if (isVi) "Ghi chú" else "Notes", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = task.note.ifBlank { if (isVi) "Không có ghi chú." else "No notes." },
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(if (isVi) "Việc nhỏ" else "Subtasks", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Text("${task.countCompletedSubTasks()}/${task.subTasks.size}", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { task.calculateSubTaskProgress() },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = viewModel.primaryColor,
                trackColor = Color(0xFFE0E0E0)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    for (sub in task.subTasks) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = sub.isDone, onCheckedChange = { viewModel.toggleSubTask(task.id, sub.id) })
                            Text(
                                text = sub.title,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                color = if (sub.isDone) Color.Gray else Color.Black,
                                textDecoration = if (sub.isDone) TextDecoration.LineThrough else TextDecoration.None
                            )
                            IconButton(onClick = { viewModel.deleteSubTask(task.id, sub.id) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFE02424))
                            }
                        }
                        HorizontalDivider(color = Color(0xFFF5F5F5))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp)) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = viewModel.primaryColor)
                        TextField(
                            value = newSubTitle,
                            onValueChange = { newSubTitle = it },
                            placeholder = { Text(if (isVi) "Thêm việc nhỏ" else "Add subtask", fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        Text(
                            text = if (isVi) "Thêm" else "Add",
                            color = viewModel.primaryColor,
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
                    viewModel.navigateTo(AppScreen.TASK_LIST)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = viewModel.primaryColor)
            ) {
                Text(if (task.isCompleted) "Đánh dấu chưa xong" else "Đánh dấu hoàn thành", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Xóa công việc", color = Color(0xFFE02424), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { viewModel.deleteTask(task.id) })
            }
        }
    }
}