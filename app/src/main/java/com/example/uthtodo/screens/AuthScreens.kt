package com.example.uthtodo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uthtodo.model.AppStrings
import com.example.uthtodo.model.TaskCategory
import com.example.uthtodo.navigation.Routes
import com.example.uthtodo.navigation.goAndClearBackStack
import com.example.uthtodo.ui.theme.AppColors
import com.example.uthtodo.viewmodel.TodoViewModel
import kotlinx.coroutines.delay

@Composable
fun AppLogo(size: Dp, background: Color, accent: Color) {
    Surface(
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(size * 0.26f),
        color = background,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(size * 0.2f),
            verticalArrangement = Arrangement.spacedBy(size * 0.08f, Alignment.CenterVertically)
        ) {
            LogoRow(Icons.Default.CheckCircle, accent, 1f, size)
            LogoRow(Icons.Default.RadioButtonUnchecked, accent.copy(alpha = 0.5f), 0.7f, size)
        }
    }
}

@Composable
private fun LogoRow(icon: ImageVector, color: Color, barFraction: Float, size: Dp) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(size * 0.22f))
        Spacer(modifier = Modifier.width(size * 0.05f))
        Box(
            modifier = Modifier
                .width(size * 0.3f * barFraction)
                .height(size * 0.09f)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

//  1. MÀN HÌNH SPLASH
@Composable
fun SplashScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val str = viewModel.strings
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(if (started) 1f else 0f, tween(1800), label = "splash")

    // Sau 1,8 giây sang Onboarding và xóa Splash khỏi lịch sử
    LaunchedEffect(Unit) {
        started = true
        delay(1800)
        navController.navigate(Routes.ONBOARDING) { popUpTo(Routes.SPLASH) { inclusive = true } }
    }

    Box(modifier = Modifier.fillMaxSize().background(viewModel.primaryColor)) {
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            AppLogo(88.dp, Color.White, viewModel.primaryColor)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("UTH") }
                    withStyle(SpanStyle(fontWeight = FontWeight.Light)) { append(" Todo") }
                },
                color = Color.White,
                fontSize = 34.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(str.splashTagline, color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.width(90.dp).height(3.dp).clip(CircleShape),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(str.versionLabel, color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
        }
    }
}

// 1a-1c. MÀN HÌNH ONBOARDING (3 bước)
@Composable
fun OnboardingScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val colors = AppColors(false)
    val str = viewModel.strings
    val primary = viewModel.primaryColor
    var step by rememberSaveable { mutableIntStateOf(0) }
    val lastStep = 2

    BackHandler(enabled = step > 0) { step-- }

    val title = listOf(str.onboardTitle1, str.onboardTitle2, str.onboardTitle3)[step]
    val desc = listOf(str.onboardDesc1, str.onboardDesc2, str.onboardDesc3)[step]
    val goLogin = { navController.navigate(Routes.LOGIN) { launchSingleTop = true } }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppLogo(28.dp, primary, Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("UTH ") }
                        withStyle(SpanStyle(color = primary, fontWeight = FontWeight.Medium)) { append("Todo") }
                    },
                    fontSize = 16.sp
                )
            }
            Text(str.skip, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.clickable { goLogin() })
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.width(170.dp).height(190.dp),
                shape = RoundedCornerShape(24.dp),
                color = colors.avatar
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when (step) {
                        0 -> IllustrationChecklist(colors, primary)
                        1 -> IllustrationGroups(colors, viewModel)
                        else -> IllustrationReminder(colors, primary, str)
                    }
                }
            }
        }

        Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(desc, fontSize = 15.sp, color = Color.Gray, lineHeight = 21.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            for (i in 0..lastStep) {
                Box(
                    modifier = Modifier
                        .width(if (i == step) 24.dp else 6.dp)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(if (i == step) primary else colors.inactiveBar)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { if (step < lastStep) step++ else goLogin() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary)
        ) {
            Text(if (step < lastStep) str.continueBtn else str.startNow, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
private fun IllustrationChecklist(colors: AppColors, primary: Color) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in 0..2) {
            Surface(shape = RoundedCornerShape(10.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (i == 0) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (i == 0) primary else colors.inactive,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.height(5.dp).weight(1f).clip(CircleShape).background(colors.inactiveBar))
                }
            }
        }
    }
}

@Composable
private fun IllustrationGroups(colors: AppColors, viewModel: TodoViewModel) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TaskCategory.entries.forEach { cat ->
            Surface(shape = RoundedCornerShape(10.dp), color = colors.card, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Surface(color = colors.categoryBg(cat), shape = RoundedCornerShape(5.dp)) {
                        Text(
                            cat.getName(viewModel.appLanguage),
                            color = colors.categoryText(cat),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.height(5.dp).fillMaxWidth(0.8f).clip(CircleShape).background(colors.inactiveBar))
                }
            }
        }
    }
}

@Composable
private fun IllustrationReminder(colors: AppColors, primary: Color, str: AppStrings) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = colors.card) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = primary, modifier = Modifier.size(28.dp))
            }
        }
        Surface(shape = RoundedCornerShape(8.dp), color = colors.card) {
            Text(str.onboardReminderSample, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
        }
    }
}

//  9. MÀN HÌNH ĐĂNG NHẬP (GOOGLE)
@Composable
fun LoginScreen(viewModel: TodoViewModel, navController: NavHostController) {
    val colors = AppColors(false)
    val str = viewModel.strings
    val primary = viewModel.primaryColor
    // Mũi tên quay lại chỉ hiện khi còn màn trước (sau Đăng xuất thì không có)
    val canGoBack = navController.previousBackStackEntry != null

    if (viewModel.showGoogleLoginDialog) {
        GoogleAccountPickerDialog(
            primaryColor = primary,
            str = str,
            onDismiss = { viewModel.showGoogleLoginDialog = false },
            onConfirm = { name, email ->
                viewModel.connectGoogleAccount(name, email)
                navController.goAndClearBackStack(Routes.HOME)
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)) {
        if (canGoBack) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.offset(x = (-12).dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        } else {
            Spacer(modifier = Modifier.height(48.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        AppLogo(72.dp, primary, Color.White)
        Spacer(modifier = Modifier.height(24.dp))
        Text(str.welcomeTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(str.welcomeDesc, fontSize = 15.sp, color = Color.Gray, lineHeight = 21.sp)
        Spacer(modifier = Modifier.height(20.dp))
        LoginPoint(Icons.Default.Sync, str.loginPoint1, primary)
        Spacer(modifier = Modifier.height(10.dp))
        LoginPoint(Icons.Default.PhoneIphone, str.loginPoint2, primary)
        Spacer(modifier = Modifier.weight(1.2f))

        OutlinedButton(
            onClick = { viewModel.showGoogleLoginDialog = true },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, colors.inactiveBar)
        ) {
            Surface(shape = CircleShape, color = colors.pill, modifier = Modifier.size(24.dp)) {
                Box(contentAlignment = Alignment.Center) { Text("G", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(str.continueGoogle, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = colors.inactiveBar)
            Text(str.orText, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 12.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = colors.inactiveBar)
        }

        Text(
            text = str.tryWithoutAccount,
            color = primary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.loginAsGuest()
                    navController.goAndClearBackStack(Routes.HOME)
                }
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(str.termsText, fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun LoginPoint(icon: ImageVector, text: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, fontSize = 13.sp, color = Color.DarkGray)
    }
}

// Hộp thoại giả lập chọn tài khoản Google (nhập tên + Gmail)
@Composable
fun GoogleAccountPickerDialog(
    primaryColor: Color,
    str: AppStrings,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(str.googlePickerTitle, fontWeight = FontWeight.Bold, color = Color.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name, onValueChange = { name = it }, singleLine = true,
                    label = { Text(str.nameLabel) }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email, onValueChange = { email = it }, singleLine = true,
                    label = { Text(str.emailLabel) }, modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name, email) }) {
                Text(str.confirm, color = primaryColor, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(str.cancel, color = Color.Gray) }
        }
    )
}