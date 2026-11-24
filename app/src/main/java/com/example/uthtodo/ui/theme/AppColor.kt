package com.example.uthtodo.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.uthtodo.model.TaskCategory
import com.example.uthtodo.model.TaskPriority

class AppColors(val isDark: Boolean) {

    //  1. Nền và chữ
    val background = if (isDark) Color(0xFF0D0D0D) else Color(0xFFF7F8FA)
    val card = if (isDark) Color(0xFF1E1E1E) else Color.White
    val bottomBar = if (isDark) Color(0xFF161616) else Color.White
    val text = if (isDark) Color.White else Color.Black
    val subText = Color.Gray
    val mutedText = if (isDark) Color.LightGray else Color.DarkGray
    val inactive = Color.LightGray
    val divider = if (isDark) Color(0xFF2E2E2E) else Color(0xFFF0F0F0)
    val softDivider = Color.LightGray.copy(alpha = 0.25f)

    // 2. Thành phần giao diện
    val pill = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF2F4F7)
    val segmentTrack = if (isDark) Color(0xFF2A2A2A) else Color(0xFFEAECF0)
    val segmentSelected = if (isDark) Color(0xFF3A3A3A) else Color.White
    val avatar = if (isDark) Color(0xFF253459) else Color(0xFFE8F0FE)
    val categorySelected = if (isDark) Color(0xFF1E2942) else Color(0xFFE8F0FE)
    val inactiveBar = if (isDark) Color(0xFF2E2E2E) else Color(0xFFE5E7EB)
    val switchOn = Color(0xFF34C759)

    //  3. Trạng thái
    val danger = Color(0xFFE02424)
    val dangerBg = Color(0xFFFDE8E8)
    val warning = Color(0xFFB45309)
    val warningBg = Color(0xFFFEF3C7)
    val success = Color(0xFF137333)
    val successBg = Color(0xFFE6F4EA)
    val orange = Color(0xFFD96B27)
    val orangeSoft = Color(0xFFFEF0E6)

    //  4. Hàm trả màu
    // Chip lọc: Sáng chọn đen / chưa chọn trắng. Tối chọn trắng / chưa chọn xám đậm.
    fun chipBg(selected: Boolean): Color = when {
        selected && isDark -> Color.White
        selected -> Color(0xFF1A1A1A)
        isDark -> Color(0xFF1E1E1E)
        else -> Color.White
    }

    fun chipText(selected: Boolean): Color = when {
        selected && isDark -> Color.Black
        selected -> Color.White
        isDark -> Color.LightGray
        else -> Color.DarkGray
    }

    fun categoryBg(c: TaskCategory): Color = when (c) {
        TaskCategory.STUDY -> if (isDark) Color(0xFF1E2942) else Color(0xFFE8F0FE)
        TaskCategory.LIFE -> if (isDark) Color(0xFF38261A) else Color(0xFFFEF0E6)
    }

    fun categoryText(c: TaskCategory): Color = when (c) {
        TaskCategory.STUDY -> if (isDark) Color(0xFF6C8CFF) else Color(0xFF2555FF)
        TaskCategory.LIFE -> if (isDark) Color(0xFFF0904F) else Color(0xFFD96B27)
    }

    fun priorityBg(p: TaskPriority): Color = when (p) {
        TaskPriority.HIGH -> dangerBg
        TaskPriority.MEDIUM -> warningBg
        TaskPriority.LOW -> successBg
    }

    fun priorityText(p: TaskPriority): Color = when (p) {
        TaskPriority.HIGH -> danger
        TaskPriority.MEDIUM -> warning
        TaskPriority.LOW -> success
    }

    //  5. Màu chủ đạo ( nằm ở phần cài đặt)
    companion object {
        val primaryOptions = listOf(
            Color(0xFF2555FF), // Blue (mặc định)
            Color(0xFF10B981), // Green
            Color(0xFFC2410C), // Rust
            Color(0xFF7C3AED)  // Purple
        )
    }
}