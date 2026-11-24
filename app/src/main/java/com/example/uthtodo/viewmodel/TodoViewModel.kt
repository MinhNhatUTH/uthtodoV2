package com.example.uthtodo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uthtodo.model.*
import com.example.uthtodo.ui.theme.AppColors

class TodoViewModel : ViewModel() {

    //  1. Cài đặt
    var themeMode by mutableStateOf(ThemeMode.LIGHT)
    var primaryColor by mutableStateOf(AppColors.primaryOptions[0])
    var fontSizeScale by mutableFloatStateOf(1.0f)
    var appLanguage by mutableStateOf(AppLanguage.VI)
    var notifyReminder by mutableStateOf(true)
    var notifySound by mutableStateOf(true)
    var notifySummary by mutableStateOf(false)
    var systemDark by mutableStateOf(false)

    private val viStrings = AppStrings(AppLanguage.VI)
    private val enStrings = AppStrings(AppLanguage.EN)
    val strings: AppStrings
        get() = if (appLanguage == AppLanguage.VI) viStrings else enStrings

    fun toggleLanguage() {
        appLanguage = if (appLanguage == AppLanguage.VI) AppLanguage.EN else AppLanguage.VI
    }

    fun isDarkTheme(): Boolean =
        themeMode == ThemeMode.DARK || (themeMode == ThemeMode.AUTO && systemDark)

    //  2. Tài khoản, thống kê
    var userProfile by mutableStateOf(UserProfile())
    var showGoogleLoginDialog by mutableStateOf(false)
    var isViewingThisWeek by mutableStateOf(true)
    var selectedDayIndex by mutableIntStateOf(1)

    private val thisWeekStats = listOf(
        DayStat("T2", "Mon", 3, 4), DayStat("T3", "Tue", 5, 5), DayStat("T4", "Wed", 2, 3),
        DayStat("T5", "Thu", 5, 6), DayStat("T6", "Fri", 3, 4), DayStat("T7", "Sat", 1, 2),
        DayStat("CN", "Sun", 1, 2)
    )
    private val lastWeekStats = listOf(
        DayStat("T2", "Mon", 4, 4), DayStat("T3", "Tue", 3, 4), DayStat("T4", "Wed", 4, 5),
        DayStat("T5", "Thu", 2, 3), DayStat("T6", "Fri", 5, 5), DayStat("T7", "Sat", 2, 2),
        DayStat("CN", "Sun", 2, 3)
    )

    fun getCurrentWeekStats(): List<DayStat> =
        if (isViewingThisWeek) thisWeekStats else lastWeekStats

    fun connectGoogleAccount(inputName: String, inputEmail: String) {
        userProfile = UserProfile(
            fullName = inputName.trim().ifBlank { "Minh Anh" },
            email = inputEmail.trim().ifBlank { "minhanh@gmail.com" },
            isGoogleLinked = true,
            lastSyncTime = strings.justNow
        )
        showGoogleLoginDialog = false
    }

    fun loginAsGuest() {
        userProfile = UserProfile(
            fullName = strings.guestName,
            email = strings.guestEmail,
            isGoogleLinked = false,
            completedCount = 2,
            streakDays = 1,
            onTimePercent = 100,
            lastSyncTime = strings.notSynced
        )
    }

    fun logout() {
        userProfile = UserProfile(
            fullName = strings.loggedOutName,
            email = strings.loggedOutEmail,
            isGoogleLinked = false
        )
    }

    // 3. Công việc
    var searchQuery by mutableStateOf("")
    var selectedFilter by mutableStateOf<TaskCategory?>(null)
    val taskList = mutableStateListOf<TaskItem>()
    private var nextTaskId = 100
    private var nextSubTaskId = 1000

    val todayTasks: List<TaskItem> get() = taskList.filter { it.isToday }
    val todayDoneCount: Int get() = todayTasks.count { it.isCompleted }
    val todayProgress: Float
        get() = if (todayTasks.isEmpty()) 0f else todayDoneCount.toFloat() / todayTasks.size

    fun getTask(taskId: Int): TaskItem? = taskList.firstOrNull { it.id == taskId }

    fun getFilteredTasks(wantCompleted: Boolean): List<TaskItem> = taskList.filter { task ->
        task.isCompleted == wantCompleted &&
                (selectedFilter == null || task.category == selectedFilter) &&
                task.title.contains(searchQuery, ignoreCase = true)
    }

    fun countRemainingByCategory(category: TaskCategory): Int =
        taskList.count { it.category == category && !it.isCompleted }

    private fun updateTask(taskId: Int, change: (TaskItem) -> TaskItem) {
        val index = taskList.indexOfFirst { it.id == taskId }
        if (index >= 0) taskList[index] = change(taskList[index])
    }

    fun toggleTaskCompleted(taskId: Int) = updateTask(taskId) { it.copy(isCompleted = !it.isCompleted) }

    // Trả true nếu thêm được, màn hình tự quyết định có đóng màn hay không
    fun addNewTask(
        title: String, note: String, category: TaskCategory, date: String,
        time: String, hasReminder: Boolean, priority: TaskPriority
    ): Boolean {
        if (title.isBlank()) return false
        taskList.add(
            0,
            TaskItem(
                id = nextTaskId++, title = title.trim(), note = note.trim(), category = category,
                date = date, time = time, hasReminder = hasReminder, priority = priority
            )
        )
        notificationList.add(
            0,
            NotificationItem(nextNotificationId++, strings.createdTitle, "${title.trim()} ($date, $time)", strings.justNow, false)
        )
        return true
    }

    fun deleteTask(taskId: Int) {
        taskList.removeAll { it.id == taskId }
    }

    fun toggleSubTask(taskId: Int, subTaskId: Int) = updateTask(taskId) { t ->
        t.copy(subTasks = t.subTasks.map { if (it.id == subTaskId) it.copy(isDone = !it.isDone) else it })
    }

    fun addSubTask(taskId: Int, subTitle: String) {
        if (subTitle.isBlank()) return
        updateTask(taskId) { it.copy(subTasks = it.subTasks + SubTask(nextSubTaskId++, subTitle.trim())) }
    }

    fun deleteSubTask(taskId: Int, subTaskId: Int) = updateTask(taskId) { t ->
        t.copy(subTasks = t.subTasks.filter { it.id != subTaskId })
    }

    //  4. Thông báo
    val notificationList = mutableStateListOf<NotificationItem>()
    private var nextNotificationId = 100

    fun markAllNotificationsRead() {
        for (i in notificationList.indices) notificationList[i] = notificationList[i].copy(isRead = true)
    }

    //  5. Dữ liệu mẫu
    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        taskList.add(
            TaskItem(
                id = 1, title = "Nộp bài tập Lập trình di động",
                note = "Nộp file .zip lên hệ thống của trường. Nhớ kèm ảnh chụp màn hình app chạy trên máy ảo.",
                category = TaskCategory.STUDY, time = "09:00", priority = TaskPriority.HIGH,
                subTasks = listOf(
                    SubTask(101, "Hoàn thiện màn hình đăng nhập", true),
                    SubTask(102, "Chụp ảnh màn hình app", true),
                    SubTask(103, "Nén file và nộp bài", false)
                )
            )
        )
        taskList.add(TaskItem(2, "Ôn chương 3 – Cấu trúc dữ liệu", category = TaskCategory.STUDY, time = "14:00"))
        taskList.add(TaskItem(3, "Đi chợ mua đồ cho tuần", category = TaskCategory.LIFE, time = "17:30"))
        taskList.add(TaskItem(4, "Họp nhóm đồ án", category = TaskCategory.STUDY, date = "Thứ Năm", time = "20:00", reminderText = "Trước 30 phút"))
        taskList.add(TaskItem(5, "Tập thể dục 30 phút", category = TaskCategory.LIFE, time = "06:30", hasReminder = false, priority = TaskPriority.LOW, isCompleted = true))
        taskList.add(TaskItem(6, "Đọc 20 trang sách", category = TaskCategory.LIFE, time = "21:00", hasReminder = false, priority = TaskPriority.LOW, isCompleted = true))

        notificationList.add(NotificationItem(1, "Sắp đến hạn nộp bài!", "Nộp bài tập Lập trình di động lúc 09:00 hôm nay.", "08:45", false))
        notificationList.add(NotificationItem(2, "Nhắc việc buổi chiều", "Ôn chương 3 – Cấu trúc dữ liệu lúc 14:00.", "13:45", false))
        notificationList.add(NotificationItem(3, "Hoàn thành xuất sắc", "Bạn đã hoàn thành chuỗi 12 ngày liên tiếp!", "Hôm qua", true))
    }
}