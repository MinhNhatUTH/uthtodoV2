package com.example.uthtodo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthtodo.model.AppScreen
import com.example.uthtodo.viewmodel.TodoViewModel
import kotlinx.coroutines.delay

// ================== 1. MÀN HÌNH CHỜ (SPLASH SCREEN) ==================
@Composable
fun SplashScreen(viewModel: TodoViewModel) {
    LaunchedEffect(Unit) {
        delay(1800)
        viewModel.navigateTo(AppScreen.ONBOARDING)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(viewModel.primaryColor)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(modifier = Modifier.size(88.dp), shape = RoundedCornerShape(24.dp), color = Color.White) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Checklist, contentDescription = null, tint = viewModel.primaryColor, modifier = Modifier.size(48.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("UTH Todo", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Việc học, việc nhà – gọn gàng mỗi ngày", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { 0.45f },
                modifier = Modifier.width(100.dp).height(4.dp).clip(RoundedCornerShape(2.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Phiên bản 2.1.0", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
        }
    }
}

// ================== 2. MÀN HÌNH ONBOARDING 3 BƯỚC ==================
@Composable
fun OnboardingScreen(viewModel: TodoViewModel) {
    val step = viewModel.onboardingStep
    val titles = listOf("Mọi việc, gọn trong một chỗ", "Chia theo Học tập và Cuộc sống", "Nhắc đúng lúc")
    val descs = listOf(
        "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
        "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
        "Bật thông báo để UTH Todo nhắc bạn trước hạn chót."
    )

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(32.dp), shape = RoundedCornerShape(8.dp), color = viewModel.primaryColor) {
                    Icon(Icons.Default.Checklist, contentDescription = null, tint = Color.White, modifier = Modifier.padding(6.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("UTH ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Todo", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = viewModel.primaryColor)
            }
            Text("Bỏ qua", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.LOGIN) })
        }

        Box(
            modifier = Modifier.size(240.dp).align(Alignment.CenterHorizontally).clip(RoundedCornerShape(32.dp)).background(Color(0xFFEEF2FF)).padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            when (step) {
                0 -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (i in 0..2) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().height(42.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp)) {
                                Icon(if (i == 0) Icons.Default.CheckCircle else Icons.Outlined.Circle, contentDescription = null, tint = if (i == 0) viewModel.primaryColor else Color.LightGray)
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(modifier = Modifier.height(8.dp).fillMaxWidth(0.75f).background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp)))
                            }
                        }
                    }
                }
                1 -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Surface(color = Color(0xFFE8F0FE), shape = RoundedCornerShape(6.dp)) {
                                Text("Học tập", color = Color(0xFF2555FF), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.height(8.dp).fillMaxWidth(0.8f).background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp)))
                        }
                    }
                    Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Surface(color = Color(0xFFFEF0E6), shape = RoundedCornerShape(6.dp)) {
                                Text("Cuộc sống", color = Color(0xFFD96B27), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.height(8.dp).fillMaxWidth(0.65f).background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp)))
                        }
                    }
                }
                else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(72.dp), shadowElevation = 4.dp) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = viewModel.primaryColor, modifier = Modifier.size(36.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 2.dp) {
                        Text("Ôn bài Toán lúc 19:00", fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(titles[step], fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(descs[step], fontSize = 15.sp, color = Color.Gray, lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in 0..2) {
                    Box(modifier = Modifier.height(6.dp).width(if (i == step) 20.dp else 6.dp).clip(CircleShape).background(if (i == step) viewModel.primaryColor else Color.LightGray))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (step < 2) viewModel.onboardingStep++ else viewModel.navigateTo(AppScreen.LOGIN)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = viewModel.primaryColor)
            ) {
                Text(if (step < 2) "Tiếp tục" else "Bắt đầu ngay", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ================== 3. MÀN HÌNH ĐĂNG NHẬP GOOGLE TƯƠNG TÁC THẬT ==================
@Composable
fun LoginScreen(viewModel: TodoViewModel) {
    if (viewModel.showGoogleLoginDialog) {
        GoogleAccountPickerDialog(
            primaryColor = viewModel.primaryColor,
            onDismiss = { viewModel.showGoogleLoginDialog = false },
            onConfirm = { name, email ->
                viewModel.connectGoogleAccount(name, email)
                viewModel.navigateTo(AppScreen.HOME)
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            IconButton(onClick = { viewModel.navigateTo(AppScreen.ONBOARDING) }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Quay lại")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Surface(modifier = Modifier.size(76.dp), shape = RoundedCornerShape(20.dp), color = viewModel.primaryColor, shadowElevation = 6.dp) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Checklist, contentDescription = null, tint = Color.White, modifier = Modifier.size(42.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Chào mừng đến với\nUTH Todo", fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 36.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Đăng nhập để lưu công việc và đồng bộ trên mọi thiết bị của bạn.", fontSize = 15.sp, color = Color.Gray, lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Sync, contentDescription = null, tint = viewModel.primaryColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Tự động sao lưu, không lo mất dữ liệu", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PhoneIphone, contentDescription = null, tint = viewModel.primaryColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Dùng trên iPhone, iPad và Mac", fontSize = 14.sp)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedButton(
                onClick = { viewModel.showGoogleLoginDialog = true },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Surface(shape = CircleShape, border = BorderStroke(1.dp, Color.LightGray), modifier = Modifier.size(24.dp), color = Color.White) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("G", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Tiếp tục với Google", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("hoặc", fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Dùng thử không cần tài khoản",
                color = viewModel.primaryColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.clickable { viewModel.loginAsGuest() }
            )

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Khi tiếp tục, bạn đồng ý với Điều khoản sử dụng và Chính sách quyền riêng tư của UTH Todo.",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun GoogleAccountPickerDialog(
    primaryColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var inputName by remember { mutableStateOf("Minh Anh") }
    var inputEmail by remember { mutableStateOf("minhanh@gmail.com") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color(0xFFE8F0FE), modifier = Modifier.size(32.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("G", fontWeight = FontWeight.Bold, color = primaryColor)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Kết nối tài khoản Google", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Nhập thông tin tài khoản Google của bạn để đồng bộ dữ liệu:", fontSize = 13.sp, color = Color.Gray)
                OutlinedTextField(
                    value = inputName,
                    onValueChange = { inputName = it },
                    label = { Text("Họ và tên hiển thị") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inputEmail,
                    onValueChange = { inputEmail = it },
                    label = { Text("Địa chỉ Gmail") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(inputName, inputEmail) },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Xác nhận kết nối", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}