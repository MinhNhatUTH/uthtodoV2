package com.example.uthtodo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.uthtodo.model.*

class TodoViewModel : ViewModel() {

    //  1. QUẢN LÝ LUỒNG MÀN HÌNH
    var currentScreen by mutableStateOf(AppScreen.SPLASH)
    var onboardingStep by mutableIntStateOf(0)
    var selectedTaskId by mutableIntStateOf(1)
    var showGoogleLoginDialog by mutableStateOf(false)

    fun navigateTo(screen: AppScreen) {
        currentScreen = screen
    }

    fun openTaskDetail(taskId: Int) {
        selectedTaskId = taskId
        currentScreen = AppScreen.TASK_DETAIL
    }
    fun goBack() {
        currentScreen = when (currentScreen) {
            AppScreen.ADD_TASK -> AppScreen.HOME
            AppScreen.TASK_DETAIL -> AppScreen.TASK_LIST
            AppScreen.PROFILE -> AppScreen.SETTINGS
            AppScreen.LOGIN -> AppScreen.ONBOARDING
            AppScreen.TASK_LIST, AppScreen.NOTIFICATIONS, AppScreen.SETTINGS -> AppScreen.HOME
            else -> currentScreen
        }
    }

    // 2. QUẢN LÝ CÀI ĐẶT, NGÔN NGỮ & GIAO DIỆN
    var themeMode by mutableStateOf(ThemeMode.LIGHT)
    var primaryColor by mutableStateOf(Color(0xFF2555FF))
    var fontSizeScale by mutableFloatStateOf(1.0f)
    var appLanguage by mutableStateOf(AppLanguage.VI)

    private val viStrings = AppStrings(AppLanguage.VI)
    private val enStrings = AppStrings(AppLanguage.EN)
    val strings: AppStrings
        get() = if (appLanguage == AppLanguage.VI) viStrings else enStrings

    fun toggleLanguage() {
        appLanguage = if (appLanguage == AppLanguage.VI) AppLanguage.EN else AppLanguage.VI
    }

    var notifyReminder by mutableStateOf(true)
    var notifySound by mutableStateOf(true)
    var notifySummary by mutableStateOf(false)

    var systemDark by mutableStateOf(false)

    fun isDarkTheme(): Boolean {
        return themeMode == ThemeMode.DARK || (themeMode == ThemeMode.AUTO && systemDark)
    }

    //  3. QUẢN LÝ TÀI KHOẢN GOOGLE & BIỂU ĐỒ TUẦN/NGÀY
    var userProfile by mutableStateOf(UserProfile())

    fun connectGoogleAccount(inputName: String, inputEmail: String) {
        val cleanName = inputName.ifBlank { "Minh Nhat" }.trim()
        val cleanEmail = inputEmail.ifBlank { "minhNhat@gmail.com" }.trim()

        userProfile = UserProfile(
            fullName = cleanName,
            email = cleanEmail,
            isGoogleLinked = true,
            completedCount = 128,
            streakDays = 12,
            onTimePercent = 86,
            lastSyncTime = if (appLanguage == AppLanguage.VI) "Vừa xong" else "Just now"
        )
        showGoogleLoginDialog = false
    }

    fun loginAsGuest() {
        userProfile = UserProfile(
            fullName = "UTH Guest",
            email = "Chưa liên kết tài khoản Google",
            isGoogleLinked = false,
            completedCount = 2,
            streakDays = 1,
            onTimePercent = 100,
            lastSyncTime = "Chưa đồng bộ"
        )
        navigateTo(AppScreen.HOME)
    }

    fun logout() {
        userProfile = UserProfile(
            fullName = "Khách",
            email = "Chưa kết nối email",
            isGoogleLinked = false
        )
        navigateTo(AppScreen.LOGIN)
    }

    var isViewingThisWeek by mutableStateOf(true)
    var selectedDayIndex by mutableIntStateOf(1)

    private val thisWeekStats = listOf(
        DayStat("T2", "Mon", 3, 4),
        DayStat("T3", "Tue", 5, 5),
        DayStat("T4", "Wed", 2, 3),
        DayStat("T5", "Thu", 5, 6),
        DayStat("T6", "Fri", 3, 4),
        DayStat("T7", "Sat", 1, 2),
        DayStat("CN", "Sun", 1, 2)
    )

    private val lastWeekStats = listOf(
        DayStat("T2", "Mon", 4, 4),
        DayStat("T3", "Tue", 3, 4),
        DayStat("T4", "Wed", 4, 5),
        DayStat("T5", "Thu", 2, 3),
        DayStat("T6", "Fri", 5, 5),
        DayStat("T7", "Sat", 2, 2),
        DayStat("CN", "Sun", 2, 3)
    )

    fun getCurrentWeekStats(): List<DayStat> {
        return if (isViewingThisWeek) thisWeekStats else lastWeekStats
    }

    //  4. QUẢN LÝ CÔNG VIỆC & THÔNG BÁO
    var searchQuery by mutableStateOf("")
    var selectedFilter by mutableStateOf<TaskCategory?>(null)

    val taskList = mutableStateListOf<TaskItem>()
    val notificationList = mutableStateListOf<NotificationItem>()
    private var nextTaskId = 100
    private var nextSubTaskId = 1000
    private var nextNotificationId = 100

    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        taskList.add(
            TaskItem(
                id = 1,
                title = "Nộp bài tập Lập trình di động",
                note = "Nộp file .zip lên hệ thống của trường. Nhớ kèm ảnh chụp màn hình app chạy trên máy ảo.",
                category = TaskCategory.STUDY,
                date = "Hôm nay",
                time = "09:00",
                priority = TaskPriority.HIGH,
                isCompleted = false,
                subTasks = listOf(
                    SubTask(101, "Hoàn thiện màn hình đăng nhập", true),
                    SubTask(102, "Chụp ảnh màn hình app", true),
                    SubTask(103, "Nén file và nộp bài", false)
                )
            )
        )
        taskList.add(TaskItem(2, "Ôn chương 3 – Cấu trúc dữ liệu", "", TaskCategory.STUDY, "Hôm nay", "14:00", "Trước 15 phút", true, TaskPriority.MEDIUM, false))
        taskList.add(TaskItem(3, "Đi chợ mua đồ cho tuần", "", TaskCategory.LIFE, "Hôm nay", "17:30", "Trước 15 phút", true, TaskPriority.MEDIUM, false))
        taskList.add(TaskItem(4, "Họp nhóm đồ án", "", TaskCategory.STUDY, "Thứ Năm", "20:00", "Trước 30 phút", true, TaskPriority.MEDIUM, false))
        taskList.add(TaskItem(5, "Tập thể dục 30 phút", "", TaskCategory.LIFE, "Hôm nay", "06:30", "Trước 15 phút", false, TaskPriority.LOW, true))
        taskList.add(TaskItem(6, "Đọc 20 trang sách", "", TaskCategory.LIFE, "Hôm nay", "21:00", "Trước 15 phút", false, TaskPriority.LOW, true))

        notificationList.add(NotificationItem(1, "Sắp đến hạn nộp bài!", "Nộp bài tập Lập trình di động lúc 09:00 hôm nay.", "08:45", false))
        notificationList.add(NotificationItem(2, "Nhắc việc buổi chiều", "Ôn chương 3 – Cấu trúc dữ liệu lúc 14:00.", "13:45", false))
        notificationList.add(NotificationItem(3, "Hoàn thành xuất sắc", "Bạn đã hoàn thành chuỗi 12 ngày liên tiếp!", "Hôm qua", true))
    }

    fun getSelectedTask(): TaskItem? {
        for (task in taskList) {
            if (task.id == selectedTaskId) return task
        }
        return null
    }

    fun toggleTaskCompleted(taskId: Int) {
        for (i in 0 until taskList.size) {
            val t = taskList[i]
            if (t.id == taskId) {
                taskList[i] = TaskItem(t.id, t.title, t.note, t.category, t.date, t.time, t.reminderText, t.hasReminder, t.priority, !t.isCompleted, t.subTasks)
                break
            }
        }
    }

    fun addNewTask(title: String, note: String, category: TaskCategory, date: String, time: String, hasReminder: Boolean, priority: TaskPriority) {
        if (title.isBlank()) return
        val newId = nextTaskId++
        val newTask = TaskItem(newId, title, note, category, date, time, "Trước 15 phút", hasReminder, priority, false)
        taskList.add(0, newTask)
        notificationList.add(0, NotificationItem(nextNotificationId++, "Đã tạo việc mới", "$title ($date, $time)", "Vừa xong", false))
        navigateTo(AppScreen.TASK_LIST)
    }

    fun deleteTask(taskId: Int) {
        for (i in 0 until taskList.size) {
            if (taskList[i].id == taskId) {
                taskList.removeAt(i)
                break
            }
        }
        navigateTo(AppScreen.TASK_LIST)
    }

    fun toggleSubTask(taskId: Int, subTaskId: Int) {
        for (i in 0 until taskList.size) {
            val t = taskList[i]
            if (t.id == taskId) {
                val updatedSubs = t.subTasks.map { sub ->
                    if (sub.id == subTaskId) SubTask(sub.id, sub.title, !sub.isDone) else sub
                }
                taskList[i] = TaskItem(t.id, t.title, t.note, t.category, t.date, t.time, t.reminderText, t.hasReminder, t.priority, t.isCompleted, updatedSubs)
                break
            }
        }
    }

    fun addSubTask(taskId: Int, subTitle: String) {
        if (subTitle.isBlank()) return
        for (i in 0 until taskList.size) {
            val t = taskList[i]
            if (t.id == taskId) {
                val updatedSubs = t.subTasks + SubTask(nextSubTaskId++, subTitle, false)
                taskList[i] = TaskItem(t.id, t.title, t.note, t.category, t.date, t.time, t.reminderText, t.hasReminder, t.priority, t.isCompleted, updatedSubs)
                break
            }
        }
    }

    fun deleteSubTask(taskId: Int, subTaskId: Int) {
        for (i in 0 until taskList.size) {
            val t = taskList[i]
            if (t.id == taskId) {
                val updatedSubs = t.subTasks.filter { it.id != subTaskId }
                taskList[i] = TaskItem(t.id, t.title, t.note, t.category, t.date, t.time, t.reminderText, t.hasReminder, t.priority, t.isCompleted, updatedSubs)
                break
            }
        }
    }

    fun getFilteredTasks(wantCompleted: Boolean): List<TaskItem> {
        return taskList.filter { task ->
            val matchStatus = (task.isCompleted == wantCompleted)
            val matchCategory = (selectedFilter == null || task.category == selectedFilter)
            val matchSearch = task.title.contains(searchQuery, ignoreCase = true)
            matchStatus && matchCategory && matchSearch
        }
    }

    fun countRemainingByCategory(category: TaskCategory): Int {
        return taskList.count { it.category == category && !it.isCompleted }
    }

    fun markAllNotificationsRead() {
        for (i in 0 until notificationList.size) {
            val item = notificationList[i]
            notificationList[i] = NotificationItem(item.id, item.title, item.message, item.time, true)
        }
    }
}