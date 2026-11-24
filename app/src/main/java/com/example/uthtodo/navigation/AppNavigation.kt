package com.example.uthtodo.navigation



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uthtodo.screens.*
import com.example.uthtodo.viewmodel.TodoViewModel

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val HOME = "home"
    const val TASK_LIST = "task_list"
    const val NOTIFICATIONS = "notifications"
    const val SETTINGS = "settings"
    const val ADD_TASK = "add_task"
    const val TASK_DETAIL = "task_detail/{taskId}"
    const val PROFILE = "profile"

    // Các màn có thanh điều hướng dưới
    val TABS = listOf(HOME, TASK_LIST, NOTIFICATIONS, SETTINGS)

    fun taskDetail(taskId: Int) = "task_detail/$taskId"
}

// Chuyển giữa các tab, không chồng màn, nhớ vị trí cuộn
fun NavHostController.goToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

// Sang màn mới và xóa hết màn cũ
fun NavHostController.goAndClearBackStack(route: String) {
    navigate(route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
fun AppNavHost(navController: NavHostController, viewModel: TodoViewModel, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Routes.SPLASH, modifier = modifier) {
        composable(Routes.SPLASH) { SplashScreen(viewModel, navController) }
        composable(Routes.ONBOARDING) { OnboardingScreen(viewModel, navController) }
        composable(Routes.LOGIN) { LoginScreen(viewModel, navController) }
        composable(Routes.HOME) { HomeScreen(viewModel, navController) }
        composable(Routes.TASK_LIST) { TaskListScreen(viewModel, navController) }
        composable(Routes.NOTIFICATIONS) { NotificationScreen(viewModel) }
        composable(Routes.SETTINGS) { SettingsScreen(viewModel, navController) }
        composable(Routes.ADD_TASK) { AddTaskScreen(viewModel, navController) }
        composable(
            route = Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { entry ->
            TaskDetailScreen(viewModel, navController, entry.arguments?.getInt("taskId") ?: -1)
        }
        composable(Routes.PROFILE) { ProfileScreen(viewModel, navController) }
    }
}