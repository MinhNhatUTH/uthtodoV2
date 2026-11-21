package com.example.uthtodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uthtodo.model.AppScreen
import com.example.uthtodo.screens.*
import com.example.uthtodo.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val todoViewModel: TodoViewModel = viewModel()
            MainAppContainer(viewModel = todoViewModel)
        }
    }
}

@Composable
fun MainAppContainer(viewModel: TodoViewModel) {
    val showBottomBar = viewModel.currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.TASK_LIST,
        AppScreen.NOTIFICATIONS,
        AppScreen.SETTINGS
    )

    // Màn Splash / Onboarding / Đăng nhập luôn nền sáng nên chữ luôn đen.
    // Các màn còn lại: chữ trắng khi giao diện Tối, đen khi Sáng.
    val alwaysLight = viewModel.currentScreen in listOf(AppScreen.SPLASH, AppScreen.ONBOARDING, AppScreen.LOGIN)
    val defaultTextColor = if (viewModel.isDarkTheme() && !alwaysLight) Color.White else Color.Black

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                FiveIconBottomBar(viewModel = viewModel)
            }
        }
    ) { _ ->
        // Nội dung phủ kín màn hình, thanh dưới nằm đè lên (các màn đã chừa contentPadding bottom = 100.dp)
        Box(modifier = Modifier.fillMaxSize()) {
            // Mọi Text/Icon không đặt màu riêng sẽ lấy màu này
            CompositionLocalProvider(LocalContentColor provides defaultTextColor) {
                when (viewModel.currentScreen) {
                    AppScreen.SPLASH -> SplashScreen(viewModel)
                    AppScreen.ONBOARDING -> OnboardingScreen(viewModel)
                    AppScreen.LOGIN -> LoginScreen(viewModel)
                    AppScreen.HOME -> HomeScreen(viewModel)
                    AppScreen.TASK_LIST -> TaskListScreen(viewModel)
                    AppScreen.NOTIFICATIONS -> NotificationScreen(viewModel)
                    AppScreen.SETTINGS -> SettingsScreen(viewModel)
                    AppScreen.ADD_TASK -> AddTaskScreen(viewModel)
                    AppScreen.TASK_DETAIL -> TaskDetailScreen(viewModel)
                    AppScreen.PROFILE -> ProfileScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun FiveIconBottomBar(viewModel: TodoViewModel) {
    val isDark = viewModel.isDarkTheme()
    val barColor = if (isDark) Color(0xFF161616) else Color.White
    val str = viewModel.strings

    // Box ngoài cao hơn thanh 26dp để chừa chỗ cho nút "+" nổi lên (và vẫn bấm được)
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Surface(
            color = barColor,
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth().padding(top = 26.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarTabItem(
                    icon = Icons.Default.Home,
                    label = str.navHome,
                    isSelected = (viewModel.currentScreen == AppScreen.HOME),
                    activeColor = viewModel.primaryColor,
                    onClick = { viewModel.navigateTo(AppScreen.HOME) }
                )

                BottomBarTabItem(
                    icon = Icons.Default.FormatListBulleted,
                    label = str.navList,
                    isSelected = (viewModel.currentScreen == AppScreen.TASK_LIST),
                    activeColor = viewModel.primaryColor,
                    onClick = { viewModel.navigateTo(AppScreen.TASK_LIST) }
                )

                // Chỗ trống dành cho nút "+" ở giữa
                Spacer(modifier = Modifier.width(56.dp))

                BottomBarTabItem(
                    icon = Icons.Default.NotificationsNone,
                    label = str.navNotify,
                    isSelected = (viewModel.currentScreen == AppScreen.NOTIFICATIONS),
                    activeColor = viewModel.primaryColor,
                    onClick = { viewModel.navigateTo(AppScreen.NOTIFICATIONS) }
                )

                BottomBarTabItem(
                    icon = Icons.Default.Tune,
                    label = str.navSettings,
                    isSelected = (viewModel.currentScreen == AppScreen.SETTINGS),
                    activeColor = viewModel.primaryColor,
                    onClick = { viewModel.navigateTo(AppScreen.SETTINGS) }
                )
            }
        }

        FloatingActionButton(
            onClick = { viewModel.navigateTo(AppScreen.ADD_TASK) },
            shape = CircleShape,
            containerColor = viewModel.primaryColor,
            contentColor = Color.White,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm việc mới", modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun BottomBarTabItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else Color.Gray
        )
    }
}