package com.example.uthtodo.model

import androidx.compose.ui.graphics.Color

// 1. Enum định nghĩa 10 màn hình trong ứng dụng
enum class AppScreen {
    SPLASH, ONBOARDING, LOGIN,
    HOME, TASK_LIST, NOTIFICATIONS, SETTINGS,
    ADD_TASK, TASK_DETAIL, PROFILE
}

// 2. Enum ngôn ngữ (Hỗ trợ chuyển đổi Tiếng Việt <-> Tiếng Anh toàn ứng dụng)
enum class AppLanguage(val displayName: String) {
    VI("Tiếng Việt"),
    EN("English")
}

// 3. Enum phân nhóm công việc: Học tập & Cuộc sống
enum class TaskCategory(
    private val nameVi: String,
    private val nameEn: String,
    val badgeColor: Color,
    val textColor: Color,
    val darkBadgeColor: Color,   // màu nền badge khi giao diện Tối
    val darkTextColor: Color     // màu chữ badge khi giao diện Tối
) {
    STUDY("Học tập", "Study", Color(0xFFE8F0FE), Color(0xFF2555FF), Color(0xFF1E2942), Color(0xFF6C8CFF)),
    LIFE("Cuộc sống", "Life", Color(0xFFFEF0E6), Color(0xFFD96B27), Color(0xFF38261A), Color(0xFFF0904F));

    fun getName(lang: AppLanguage): String {
        return if (lang == AppLanguage.VI) nameVi else nameEn
    }

    fun badgeBg(isDark: Boolean): Color = if (isDark) darkBadgeColor else badgeColor
    fun badgeText(isDark: Boolean): Color = if (isDark) darkTextColor else textColor
}

// 4. Enum mức độ ưu tiên: Thấp, Vừa, Cao
enum class TaskPriority(private val nameVi: String, private val nameEn: String) {
    LOW("Thấp", "Low"),
    MEDIUM("Vừa", "Medium"),
    HIGH("Cao", "High");

    fun getName(lang: AppLanguage): String {
        return if (lang == AppLanguage.VI) nameVi else nameEn
    }
}

// 5. Enum chế độ giao diện: Sáng, Tối, Tự động
enum class ThemeMode(private val nameVi: String, private val nameEn: String) {
    LIGHT("Sáng", "Light"),
    DARK("Tối", "Dark"),
    AUTO("Tự động", "Auto");

    fun getName(lang: AppLanguage): String {
        return if (lang == AppLanguage.VI) nameVi else nameEn
    }
}

// 6. Class Việc nhỏ (SubTask) nằm trong một công việc lớn
class SubTask(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)

// 7. Class Công việc chính (TaskItem) đóng gói thuộc tính và phương thức tính tiến độ
class TaskItem(
    val id: Int,
    val title: String,
    val note: String = "",
    val category: TaskCategory = TaskCategory.STUDY,
    val date: String = "Hôm nay",
    val time: String = "09:00",
    val reminderText: String = "Trước 15 phút",
    val hasReminder: Boolean = true,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val subTasks: List<SubTask> = emptyList()
) {
    // Phương thức đếm số việc nhỏ đã hoàn thành
    fun countCompletedSubTasks(): Int {
        var count = 0
        for (sub in subTasks) {
            if (sub.isDone) count++
        }
        return count
    }

    // Phương thức tính phần trăm tiến độ việc nhỏ (0.0f -> 1.0f)
    fun calculateSubTaskProgress(): Float {
        if (subTasks.isEmpty()) return 0f
        return countCompletedSubTasks().toFloat() / subTasks.size.toFloat()
    }
}

// 8. Class thống kê từng ngày trong tuần (Dùng cho biểu đồ màn Hồ sơ)
class DayStat(
    private val dayLabelVi: String,
    private val dayLabelEn: String,
    val completedTasks: Int,
    val totalTasks: Int
) {
    fun getLabel(lang: AppLanguage): String {
        return if (lang == AppLanguage.VI) dayLabelVi else dayLabelEn
    }
}

// 9. Class Thông báo (Hiển thị ở tab Thông báo)
class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)

// 10. Class Hồ sơ người dùng (Cập nhật linh hoạt khi kết nối Google)
class UserProfile(
    val fullName: String = "Minh Nhat",
    val email: String = "minhNhat@gmail.com",
    val isGoogleLinked: Boolean = true,
    val completedCount: Int = 128,
    val streakDays: Int = 12,
    val onTimePercent: Int = 86,
    val lastSyncTime: String = "Vừa xong"
) {
    // Phương thức tự động lấy chữ cái đầu làm Avatar (VD: "Minh Anh" -> "MA")
    fun getShortName(): String {
        val parts = fullName.trim().split(" ").filter { it.isNotEmpty() }
        if (parts.isEmpty()) return "U"
        if (parts.size == 1) return parts[0].take(2).uppercase()
        val firstChar = parts.first().first().uppercaseChar()
        val lastChar = parts.last().first().uppercaseChar()
        return "$firstChar$lastChar"
    }
}

// 11. Class Từ điển Đa ngôn ngữ cho toàn bộ ứng dụng
class AppStrings(lang: AppLanguage) {
    val isVi = (lang == AppLanguage.VI)

    // Thanh điều hướng 5 nút
    val navHome = if (isVi) "Trang chủ" else "Home"
    val navList = if (isVi) "Danh sách" else "Tasks"
    val navNotify = if (isVi) "Thông báo" else "Alerts"
    val navSettings = if (isVi) "Cài đặt" else "Settings"

    // Trang chủ & Danh sách
    val dateSample = if (isVi) "Thứ Ba, 20 tháng 10" else "Tuesday, Oct 20"
    val greetingPrefix = if (isVi) "Chào" else "Hello"
    val todayProgress = if (isVi) "Tiến độ hôm nay" else "Today's Progress"
    val tasksUnit = if (isVi) "việc" else "tasks"
    val startSmallHint = if (isVi) "Bắt đầu từ việc nhỏ nhất nhé." else "Start with the smallest task."
    val remainingTasks = if (isVi) "việc còn lại" else "tasks left"
    val todayTasksTitle = if (isVi) "Việc hôm nay" else "Today's Tasks"
    val seeAll = if (isVi) "Xem tất cả" else "See all"
    val searchPlaceholder = if (isVi) "Tìm công việc" else "Search tasks..."
    val filterAll = if (isVi) "Tất cả" else "All"
    val sectionTodo = if (isVi) "CẦN LÀM" else "TO DO"
    val sectionDone = if (isVi) "ĐÃ XONG" else "COMPLETED"
    val urgentBadge = if (isVi) "Gấp" else "Urgent"
    val unfinishedTasks = if (isVi) "việc chưa xong" else "tasks to do"

    // Chi tiết công việc
    val markDone = if (isVi) "Đánh dấu hoàn thành" else "Mark as done"
    val markUndone = if (isVi) "Đánh dấu chưa xong" else "Mark as not done"
    val deleteTask = if (isVi) "Xóa công việc" else "Delete task"
    fun priorityBadge(priorityName: String): String =
        if (isVi) "Ưu tiên ${priorityName.lowercase()}" else "$priorityName priority"

    // Cài đặt
    val sectionAppearance = if (isVi) "GIAO DIỆN" else "APPEARANCE"
    val primaryColorLabel = if (isVi) "Màu chủ đạo" else "Primary Color"
    val fontSizeLabel = if (isVi) "Cỡ chữ" else "Font Size"
    val sectionNotification = if (isVi) "THÔNG BÁO" else "NOTIFICATIONS"
    val remindTitle = if (isVi) "Nhắc việc" else "Task Reminder"
    val remindSub = if (isVi) "Báo trước hạn chót 15 phút" else "Notify 15 mins before deadline"
    val soundTitle = if (isVi) "Âm thanh" else "Sound"
    val soundSub = if (isVi) "Phát âm khi có thông báo" else "Play sound on notification"
    val summaryTitle = if (isVi) "Tổng kết cuối ngày" else "Daily Summary"
    val summarySub = if (isVi) "Gửi lúc 21:00 mỗi tối" else "Sent at 21:00 every night"
    val sectionGeneral = if (isVi) "CHUNG" else "GENERAL"
    val languageLabel = if (isVi) "Ngôn ngữ" else "Language"

    // Hồ sơ
    val profileTitle = if (isVi) "Hồ sơ" else "Profile"
    val editBtn = if (isVi) "Sửa" else "Edit"
    val googleLinkedText = if (isVi) "Đã liên kết tài khoản Google" else "Google Account Linked"
    val googleUnlinkedText = if (isVi) "Chưa liên kết Google (Bấm để kết nối)" else "Not linked to Google (Tap to connect)"
    val statDone = if (isVi) "Việc đã xong" else "Completed"
    val statStreak = if (isVi) "Chuỗi liên tiếp" else "Day Streak"
    val statOnTime = if (isVi) "Đúng hạn" else "On Time"
    val thisWeek = if (isVi) "Tuần này" else "This Week"
    val lastWeek = if (isVi) "Tuần trước" else "Last Week"
    val syncData = if (isVi) "Đồng bộ dữ liệu" else "Sync Data"
    val privacyPolicy = if (isVi) "Quyền riêng tư" else "Privacy Policy"
    val logoutBtn = if (isVi) "Đăng xuất" else "Log Out"
    val daysUnit = if (isVi) "ngày" else "days"
    fun dayProgress(dayLabel: String, done: Int, total: Int): String =
        if (isVi) "Ngày $dayLabel: Đã xong $done/$total công việc" else "$dayLabel: $done/$total tasks done"
}