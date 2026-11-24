package com.example.uthtodo.model

enum class AppLanguage(val displayName: String) {
    VI("Tiếng Việt"),
    EN("English")
}

enum class TaskCategory(private val nameVi: String, private val nameEn: String) {
    STUDY("Học tập", "Study"),
    LIFE("Cuộc sống", "Life");

    fun getName(lang: AppLanguage): String = if (lang == AppLanguage.VI) nameVi else nameEn
}

enum class TaskPriority(private val nameVi: String, private val nameEn: String) {
    LOW("Thấp", "Low"),
    MEDIUM("Vừa", "Medium"),
    HIGH("Cao", "High");

    fun getName(lang: AppLanguage): String = if (lang == AppLanguage.VI) nameVi else nameEn
}

enum class ThemeMode(private val nameVi: String, private val nameEn: String) {
    LIGHT("Sáng", "Light"),
    DARK("Tối", "Dark"),
    AUTO("Tự động", "Auto");

    fun getName(lang: AppLanguage): String = if (lang == AppLanguage.VI) nameVi else nameEn
}

//  2. Công việc
data class SubTask(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)

data class TaskItem(
    val id: Int,
    val title: String,
    val note: String = "",
    val category: TaskCategory = TaskCategory.STUDY,
    val date: String = TODAY,
    val time: String = "09:00",
    val reminderText: String = "Trước 15 phút",
    val hasReminder: Boolean = true,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val subTasks: List<SubTask> = emptyList()
) {
    val isToday: Boolean get() = date == TODAY

    fun countCompletedSubTasks(): Int = subTasks.count { it.isDone }

    // Tiến độ việc nhỏ từ 0.0 đến 1.0
    fun calculateSubTaskProgress(): Float {
        if (subTasks.isEmpty()) return 0f
        return countCompletedSubTasks().toFloat() / subTasks.size
    }

    companion object {
        const val TODAY = "Hôm nay"
    }
}

//  3. Thống kê, thông báo, hồ sơ
// Thống kê một ngày cho biểu đồ tuần
data class DayStat(
    private val labelVi: String,
    private val labelEn: String,
    val completedTasks: Int,
    val totalTasks: Int
) {
    fun getLabel(lang: AppLanguage): String = if (lang == AppLanguage.VI) labelVi else labelEn
}

data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)

data class UserProfile(
    val fullName: String = "Minh Anh",
    val email: String = "minhanh@gmail.com",
    val isGoogleLinked: Boolean = true,
    val completedCount: Int = 128,
    val streakDays: Int = 12,
    val onTimePercent: Int = 86,
    val lastSyncTime: String = "Vừa xong"
) {
    // Lấy chữ cái đầu làm avatar, ví dụ "Minh Nhat" -> "MN"
    fun getShortName(): String {
        val parts = fullName.trim().split(" ").filter { it.isNotEmpty() }
        if (parts.isEmpty()) return "U"
        if (parts.size == 1) return parts[0].take(2).uppercase()
        return "${parts.first().first().uppercaseChar()}${parts.last().first().uppercaseChar()}"
    }
}

//  4. Chữ hiển thị theo ngôn ngữ
class AppStrings(lang: AppLanguage) {
    val isVi = (lang == AppLanguage.VI)
    private fun t(vi: String, en: String) = if (isVi) vi else en

    // Thanh dưới
    val navHome = t("Trang chủ", "Home")
    val navList = t("Danh sách", "Tasks")
    val navNotify = t("Thông báo", "Alerts")
    val navSettings = t("Cài đặt", "Settings")
    val addTaskDesc = t("Thêm việc mới", "Add new task")

    // Chung
    val skip = t("Bỏ qua", "Skip")
    val continueBtn = t("Tiếp tục", "Continue")
    val cancel = t("Hủy", "Cancel")
    val confirm = t("Xác nhận", "Confirm")
    val save = t("Lưu", "Save")
    val justNow = t("Vừa xong", "Just now")
    val notSynced = t("Chưa đồng bộ", "Not synced")

    // 1. Splash, 2. Onboarding
    val splashTagline = t("Việc học, việc nhà — gọn gàng mỗi ngày", "Study, home — neat every day")
    val versionLabel = t("Phiên bản 2.1.0", "Version 2.1.0")
    val startNow = t("Bắt đầu ngay", "Get started")
    val onboardTitle1 = t("Mọi việc, gọn trong một chỗ", "Everything in one place")
    val onboardDesc1 = t("Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.", "Note down homework, chores, appointments. Never forget a thing.")
    val onboardTitle2 = t("Chia theo Học tập và Cuộc sống", "Split into Study and Life")
    val onboardDesc2 = t("Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.", "Each task has its own group, see at a glance what to do today.")
    val onboardTitle3 = t("Nhắc đúng lúc", "Reminders on time")
    val onboardDesc3 = t("Bật thông báo để UTH Todo nhắc bạn trước hạn chót.", "Turn on notifications so UTH Todo reminds you before deadlines.")
    val onboardReminderSample = t("Ôn bài Toán lúc 19:00", "Math review at 19:00")

    // 3. Đăng nhập
    val welcomeTitle = t("Chào mừng đến với UTH Todo", "Welcome to UTH Todo")
    val welcomeDesc = t("Đăng nhập để lưu công việc và đồng bộ trên mọi thiết bị của bạn.", "Sign in to save your tasks and sync across all your devices.")
    val loginPoint1 = t("Tự động sao lưu, không lo mất dữ liệu", "Automatic backup, never lose data")
    val loginPoint2 = t("Dùng trên iPhone, iPad và Mac", "Works on iPhone, iPad and Mac")
    val continueGoogle = t("Tiếp tục với Google", "Continue with Google")
    val orText = t("hoặc", "or")
    val tryWithoutAccount = t("Dùng thử không cần tài khoản", "Try without an account")
    val termsText = t("Khi tiếp tục, bạn đồng ý với Điều khoản sử dụng và Chính sách quyền riêng tư của UTH Todo.", "By continuing, you agree to the Terms of Use and Privacy Policy of UTH Todo.")
    val googlePickerTitle = t("Chọn tài khoản Google", "Choose Google account")
    val nameLabel = t("Tên hiển thị", "Display name")
    val emailLabel = "Gmail"
    val guestName = "UTH Guest"
    val guestEmail = t("Chưa liên kết tài khoản Google", "Google account not linked")
    val loggedOutName = t("Khách", "Guest")
    val loggedOutEmail = t("Chưa kết nối email", "No email connected")

    // 4-5. Trang chủ và Danh sách
    val dateSample = t("Thứ Ba, 20 tháng 10", "Tuesday, Oct 20")
    val greetingPrefix = t("Chào", "Hello")
    val todayProgress = t("Tiến độ hôm nay", "Today's Progress")
    val tasksUnit = t("việc", "tasks")
    val startSmallHint = t("Bắt đầu từ việc nhỏ nhất nhé.", "Start with the smallest task.")
    val remainingTasks = t("việc còn lại", "tasks left")
    val todayTasksTitle = t("Việc hôm nay", "Today's Tasks")
    val seeAll = t("Xem tất cả", "See all")
    val searchPlaceholder = t("Tìm công việc", "Search tasks...")
    val filterAll = t("Tất cả", "All")
    val sectionTodo = t("CẦN LÀM", "TO DO")
    val sectionDone = t("ĐÃ XONG", "COMPLETED")
    val urgentBadge = t("Gấp", "Urgent")
    val unfinishedTasks = t("việc chưa xong", "tasks to do")

    // 6. Thông báo
    val markAllRead = t("Đánh dấu đã đọc", "Mark all read")
    val createdTitle = t("Đã tạo việc mới", "New task created")

    // 7. Cài đặt
    val sectionAppearance = t("GIAO DIỆN", "APPEARANCE")
    val primaryColorLabel = t("Màu chủ đạo", "Primary Color")
    val fontSizeLabel = t("Cỡ chữ", "Font Size")
    val sectionNotification = t("THÔNG BÁO", "NOTIFICATIONS")
    val remindTitle = t("Nhắc việc", "Task Reminder")
    val remindSub = t("Báo trước hạn chót 15 phút", "Notify 15 mins before deadline")
    val soundTitle = t("Âm thanh", "Sound")
    val soundSub = t("Phát âm khi có thông báo", "Play sound on notification")
    val summaryTitle = t("Tổng kết cuối ngày", "Daily Summary")
    val summarySub = t("Gửi lúc 21:00 mỗi tối", "Sent at 21:00 every night")
    val sectionGeneral = t("CHUNG", "GENERAL")
    val languageLabel = t("Ngôn ngữ", "Language")

    // 8. Thêm việc mới
    val newTask = t("Việc mới", "New Task")
    val taskTitleLabel = t("Tên công việc", "Task title")
    val noteLabel = t("Ghi chú", "Note")
    val notePlaceholder = t("Thêm ghi chú (không bắt buộc)", "Add note (optional)")
    val categoryLabel = t("Nhóm", "Category")
    val dateLabel = t("Ngày", "Date")
    val timeLabel = t("Giờ", "Time")
    val remindMe = t("Nhắc tôi", "Remind me")
    val remind15 = t("Trước 15 phút", "15 mins before")
    val priorityLabel = t("Mức ưu tiên", "Priority")
    val saveTask = t("Lưu công việc", "Save Task")

    // 9. Chi tiết công việc
    val deadline = t("Hạn chót", "Deadline")
    val reminder = t("Nhắc nhở", "Reminder")
    val reminderOff = t("Tắt", "Off")
    val repeat = t("Lặp lại", "Repeat")
    val repeatNone = t("Không", "None")
    val notes = t("Ghi chú", "Notes")
    val noNotes = t("Không có ghi chú.", "No notes.")
    val subtasks = t("Việc nhỏ", "Subtasks")
    val addSubtask = t("Thêm việc nhỏ", "Add subtask")
    val addBtn = t("Thêm", "Add")
    val markDone = t("Đánh dấu hoàn thành", "Mark as done")
    val markUndone = t("Đánh dấu chưa xong", "Mark as not done")
    val deleteTask = t("Xóa công việc", "Delete task")
    fun priorityBadge(priorityName: String): String =
        if (isVi) "Ưu tiên ${priorityName.lowercase()}" else "$priorityName priority"

    // 10. Hồ sơ
    val profileTitle = t("Hồ sơ", "Profile")
    val editBtn = t("Sửa", "Edit")
    val googleLinkedText = t("Đã liên kết tài khoản Google", "Google Account Linked")
    val googleUnlinkedText = t("Chưa liên kết Google (Bấm để kết nối)", "Not linked to Google (Tap to connect)")
    val statDone = t("Việc đã xong", "Completed")
    val statStreak = t("Chuỗi liên tiếp", "Day Streak")
    val statOnTime = t("Đúng hạn", "On Time")
    val thisWeek = t("Tuần này", "This Week")
    val lastWeek = t("Tuần trước", "Last Week")
    val syncData = t("Đồng bộ dữ liệu", "Sync Data")
    val privacyPolicy = t("Quyền riêng tư", "Privacy Policy")
    val logoutBtn = t("Đăng xuất", "Log Out")
    val daysUnit = t("ngày", "days")
    fun dayProgress(dayLabel: String, done: Int, total: Int): String =
        if (isVi) "Ngày $dayLabel: Đã xong $done/$total công việc" else "$dayLabel: $done/$total tasks done"
}