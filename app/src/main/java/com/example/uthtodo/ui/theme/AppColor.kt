package com.example.uthtodo.ui.theme

import androidx.compose.ui.graphics.Color

// Gom toàn bộ màu phụ thuộc Sáng/Tối vào 1 chỗ.
// Cách dùng trong màn hình:  val colors = AppColors(viewModel.isDarkTheme())
class AppColors(isDark: Boolean) {
    val background = if (isDark) Color(0xFF0D0D0D) else Color(0xFFF7F8FA)   // nền màn hình
    val card = if (isDark) Color(0xFF1E1E1E) else Color.White              // nền thẻ
    val text = if (isDark) Color.White else Color.Black                    // chữ chính
    val divider = if (isDark) Color(0xFF2E2E2E) else Color(0xFFF0F0F0)     // đường kẻ
    val pill = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF2F4F7)        // ô Ngày / Giờ
    val segmentTrack = if (isDark) Color(0xFF2A2A2A) else Color(0xFFEAECF0) // nền thanh Thấp/Vừa/Cao
    val segmentSelected = if (isDark) Color(0xFF3A3A3A) else Color.White   // nút đang chọn
    val avatar = if (isDark) Color(0xFF253459) else Color(0xFFE8F0FE)      // nền avatar
    val categorySelected = if (isDark) Color(0xFF1E2942) else Color(0xFFE8F0FE) // nút nhóm đang chọn
    val inactiveBar = if (isDark) Color(0xFF2E2E2E) else Color(0xFFE5E7EB) // cột biểu đồ chưa chọn
}
