package com.example.uthtodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uthtodo.navigation.AppNavHost
import com.example.uthtodo.navigation.Routes
import com.example.uthtodo.navigation.goToTab
import com.example.uthtodo.ui.theme.AppColors
import com.example.uthtodo.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val todoViewModel: TodoViewModel = viewModel()
                MainAppContainer(todoViewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: TodoViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Báo cho ViewModel biết điện thoại đang Sáng hay Tối (cho chế độ Tự động)
    val systemDark = isSystemInDarkTheme()
    LaunchedEffect(systemDark) { viewModel.systemDark = systemDark }

    val showBottomBar = currentRoute in Routes.TABS
    val alwaysLight = currentRoute in listOf(Routes.SPLASH, Routes.ONBOARDING, Routes.LOGIN)
    val colors = AppColors(viewModel.isDarkTheme() && !alwaysLight)

    // Cỡ chữ người dùng chọn nhân vào fontScale, áp dụng cho mọi màn
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density.density, density.fontScale * viewModel.fontSizeScale),
        LocalContentColor provides colors.text
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = { if (showBottomBar) BottomBar(navController, currentRoute, viewModel) }
        ) { innerPadding ->
            val bg = when (currentRoute) {
                Routes.SPLASH -> viewModel.primaryColor
                Routes.ONBOARDING, Routes.LOGIN -> Color.White
                else -> colors.background
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .padding(innerPadding)
                    .statusBarsPadding()
                    .then(if (showBottomBar) Modifier else Modifier.navigationBarsPadding())
            ) {
                AppNavHost(navController, viewModel)
            }
        }
    }
}

private data class BottomBarTab(val route: String, val icon: ImageVector, val label: String)

@Composable
fun BottomBar(navController: NavHostController, currentRoute: String?, viewModel: TodoViewModel) {
    val colors = AppColors(viewModel.isDarkTheme())
    val str = viewModel.strings
    val tabs = listOf(
        BottomBarTab(Routes.HOME, Icons.Default.Home, str.navHome),
        BottomBarTab(Routes.TASK_LIST, Icons.AutoMirrored.Filled.FormatListBulleted, str.navList),
        BottomBarTab(Routes.NOTIFICATIONS, Icons.Default.NotificationsNone, str.navNotify),
        BottomBarTab(Routes.SETTINGS, Icons.Default.Tune, str.navSettings)
    )

    // Box cao hơn thanh 26dp để nút "+" nổi lên mà vẫn bấm được
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Surface(
            color = colors.bottomBar,
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth().padding(top = 26.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.take(2).forEach { tab ->
                    BottomBarTabItem(tab, currentRoute == tab.route, viewModel.primaryColor) { navController.goToTab(tab.route) }
                }
                Spacer(modifier = Modifier.width(56.dp)) // chỗ cho nút "+"
                tabs.drop(2).forEach { tab ->
                    BottomBarTabItem(tab, currentRoute == tab.route, viewModel.primaryColor) { navController.goToTab(tab.route) }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Routes.ADD_TASK) { launchSingleTop = true } },
            shape = CircleShape,
            containerColor = viewModel.primaryColor,
            contentColor = Color.White,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = str.addTaskDesc, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun BottomBarTabItem(tab: BottomBarTab, isSelected: Boolean, activeColor: Color, onClick: () -> Unit) {
    val tint = if (isSelected) activeColor else Color.Gray
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Icon(tab.icon, contentDescription = tab.label, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = tab.label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = tint
        )
    }
}