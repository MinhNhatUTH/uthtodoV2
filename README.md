# UTH Todo V2 - Tóm tắt dự án

Ứng dụng quản lý công việc cho Android, viết bằng Kotlin + Jetpack Compose, kiến trúc MVVM, chuyển màn bằng Navigation Compose.

| Mục | Giá trị |
|---|---|
| Package | `com.example.uthtodo` |
| minSdk / targetSdk | 30 / 37 |
| Giao diện | Jetpack Compose + Material 3 |
| Điều hướng | `androidx.navigation:navigation-compose` 2.9.8 (khai báo trong `gradle/libs.versions.toml`) |
| Trạng thái | `ViewModel` + `mutableStateOf` (Compose State) |
| Dữ liệu | Dữ liệu mẫu nạp trong ViewModel, **chưa lưu bộ nhớ**, thoát app là mất |
| Thiết kế | Figma: Splash, Onboarding 3 bước, Trang chủ, Danh sách, Thêm mới, Chi tiết, Cài đặt, Giao diện tối, Đăng nhập, Hồ sơ |

---

## 1. Cấu trúc thư mục

```
app/src/main/java/com/example/uthtodo/
├── MainActivity.kt                  Điểm vào, khung chung, thanh điều hướng dưới
├── navigation/
│   └── AppNavigation.kt             Routes + AppNavHost (sơ đồ chuyển màn)
├── model/
│   └── TodoModels.kt                Dữ liệu: enum, TaskItem, UserProfile..., AppStrings
├── viewmodel/
│   └── TodoViewModel.kt             Trạng thái và logic của cả app
├── ui/theme/
│   └── AppColor.kt                  Nơi duy nhất khai báo màu
└── screens/
    ├── AuthScreens.kt               Màn 1, 2, 3
    ├── HomeAndListScreens.kt        Màn 4, 5, 6
    ├── SettingsAndProfileScreens.kt Màn 7, 10
    └── AddAndDetailScreens.kt       Màn 8, 9
```

Luồng MVVM: **View** (`screens/`) đọc state từ **ViewModel** và gọi hàm của nó. ViewModel giữ **Model** (`model/`). Việc **chuyển màn** do View gọi `navController`, ViewModel không biết gì về màn hình.

---

## 2. 10 màn hình, nằm ở file nào

| # | Màn hình | Route | Hàm chính | File | Thanh dưới | Nền |
|---|---|---|---|---|---|---|
| 1 | Splash | `splash` | `SplashScreen` | `AuthScreens.kt` | Không | Màu chủ đạo |
| 2 | Onboarding (3 bước) | `onboarding` | `OnboardingScreen` | `AuthScreens.kt` | Không | Trắng |
| 3 | Đăng nhập | `login` | `LoginScreen`, `GoogleAccountPickerDialog` | `AuthScreens.kt` | Không | Trắng |
| 4 | Trang chủ | `home` | `HomeScreen`, `TaskCardItem` | `HomeAndListScreens.kt` | Có (tab) | Sáng/Tối |
| 5 | Danh sách việc | `task_list` | `TaskListScreen`, `FilterChipItem` | `HomeAndListScreens.kt` | Có (tab) | Sáng/Tối |
| 6 | Thông báo | `notifications` | `NotificationScreen` | `HomeAndListScreens.kt` | Có (tab) | Sáng/Tối |
| 7 | Cài đặt | `settings` | `SettingsScreen`, `SettingSwitchRowFull` | `SettingsAndProfileScreens.kt` | Có (tab) | Sáng/Tối |
| 8 | Thêm việc mới | `add_task` | `AddTaskScreen` | `AddAndDetailScreens.kt` | Không | Sáng/Tối |
| 9 | Chi tiết công việc | `task_detail/{taskId}` | `TaskDetailScreen` | `AddAndDetailScreens.kt` | Không | Sáng/Tối |
| 10 | Hồ sơ cá nhân | `profile` | `ProfileScreen`, `StatCardBox` | `SettingsAndProfileScreens.kt` | Không | Sáng/Tối |

Trong code, mỗi màn có tiêu đề dạng `// ===== 4. MÀN HÌNH TRANG CHỦ =====`, số trùng với bảng này. Đầu mỗi file có khối `// FILE: ...` liệt kê các màn nằm trong file đó.

---

## 3. MainActivity nối các màn hình thế nào

```
MainActivity
 └─ viewModel()  ->  TodoViewModel (1 bản duy nhất, dùng chung cho mọi màn)
 └─ MainAppContainer(viewModel)
      ├─ rememberNavController()           bộ điều khiển chuyển màn
      ├─ currentRoute                       màn đang hiển thị (đọc từ back stack)
      ├─ LaunchedEffect(systemDark)         báo cho ViewModel biết điện thoại đang Sáng hay Tối
      ├─ defaultTextColor                   chữ đen ở Splash/Onboarding/Đăng nhập, còn lại theo Sáng/Tối
      └─ Scaffold
           ├─ bottomBar = BottomBar         chỉ hiện khi currentRoute thuộc Routes.TABS
           └─ nội dung  = AppNavHost        (navigation/AppNavigation.kt) chọn màn theo route
```

`BottomBar` có 4 tab (Trang chủ, Danh sách, Thông báo, Cài đặt) và nút "+" ở giữa mở màn 8. Mỗi tab gọi `navController.goToTab(route)`.

### Bảng chuyển màn

| Từ màn | Hành động | Đến | Kiểu |
|---|---|---|---|
| 1 Splash | Sau 1,8 giây | 2 Onboarding | Xóa Splash khỏi lịch sử |
| 2 Onboarding | "Bỏ qua" hoặc nút ở bước cuối | 3 Đăng nhập | Thêm màn |
| 3 Đăng nhập | Xác nhận tài khoản Google, hoặc "Dùng thử không cần tài khoản" | 4 Trang chủ | Xóa hết lịch sử |
| 3 Đăng nhập | Mũi tên quay lại | Màn trước | Chỉ hiện khi còn màn trước |
| 4 Trang chủ | Avatar | 10 Hồ sơ | Thêm màn |
| 4 Trang chủ | Chuông | 6 Thông báo | Chuyển tab |
| 4 Trang chủ | "Xem tất cả" | 5 Danh sách | Chuyển tab |
| 4, 5 | Bấm thẻ công việc | 9 Chi tiết (truyền `taskId`) | Thêm màn |
| 7 Cài đặt | Avatar | 10 Hồ sơ | Thêm màn |
| Thanh dưới | 4 tab | 4, 5, 6, 7 | Chuyển tab (nhớ vị trí cuộn) |
| Thanh dưới | Nút "+" | 8 Thêm việc | Thêm màn |
| 8 Thêm việc | "Hủy" | Màn trước | Quay lại |
| 8 Thêm việc | "Lưu" (tên không trống) | 5 Danh sách | Quay lại rồi chuyển tab |
| 9 Chi tiết | Quay lại, "Đánh dấu hoàn thành", "Xóa công việc" | Màn trước | Quay lại |
| 10 Hồ sơ | Quay lại | Màn trước | Quay lại |
| 10 Hồ sơ | "Đăng xuất" | 3 Đăng nhập | Xóa hết lịch sử |
| Bất kỳ | Nút Back của điện thoại | Màn trước | `NavController` tự xử lý |

Các hàm chuyển màn dùng chung nằm ở `AppNavigation.kt`:
- `goToTab(route)`: chuyển giữa các tab, không chồng màn.
- `goAndClearBackStack(route)`: sang màn mới và xóa hết màn cũ.

---

## 4. AppColors - các mã màu

File: `ui/theme/AppColor.kt`. Màn hình **không tự ghi mã màu**, chỉ lấy từ đây:

```kotlin
val colors = AppColors(viewModel.isDarkTheme())
Text("...", color = colors.text)
```

Màn Splash/Onboarding/Đăng nhập luôn nền sáng nên dùng `AppColors(isDark = false)`.

### 4.1. Nền và chữ

| Token | Sáng | Tối | Dùng ở |
|---|---|---|---|
| `background` | `F7F8FA` | `0D0D0D` | Nền màn 4-10 |
| `card` | `FFFFFF` | `1E1E1E` | Thẻ, ô nhập |
| `bottomBar` | `FFFFFF` | `161616` | Thanh điều hướng dưới |
| `text` | Đen | Trắng | Chữ chính |
| `subText` | Xám | Xám | Chữ phụ |
| `mutedText` | Xám đậm | Xám nhạt | Ghi chú ở màn Chi tiết, chip chưa chọn |
| `inactive` | Xám nhạt | Xám nhạt | Icon / chấm chưa chọn |
| `divider` | `F0F0F0` | `2E2E2E` | Đường kẻ |
| `softDivider` | Xám nhạt 25% | Xám nhạt 25% | Đường kẻ trong thẻ Cài đặt |

### 4.2. Thành phần giao diện

| Token | Sáng | Tối | Dùng ở |
|---|---|---|---|
| `pill` | `F2F4F7` | `2A2A2A` | Ô Ngày / Giờ (màn 8) |
| `segmentTrack` | `EAECF0` | `2A2A2A` | Thanh chọn ưu tiên (màn 8), thanh tiến độ (màn 9) |
| `segmentSelected` | `FFFFFF` | `3A3A3A` | Nút ưu tiên đang chọn |
| `avatar` | `E8F0FE` | `253459` | Avatar ở màn 3, 4, 7, 10 |
| `categorySelected` | `E8F0FE` | `1E2942` | Nút nhóm đang chọn (màn 8) |
| `inactiveBar` | `E5E7EB` | `2E2E2E` | Cột biểu đồ chưa chọn (màn 10), khung xem trước giao diện (màn 7) |
| `switchOn` | `34C759` | `34C759` | Công tắc bật (màn 7, 8) |

### 4.3. Trạng thái

| Token | Mã | Dùng ở |
|---|---|---|
| `danger` / `dangerBg` | `E02424` / `FDE8E8` | Nút xóa, đăng xuất, nhãn "Gấp" |
| `warning` / `warningBg` | `B45309` / `FEF3C7` | Ưu tiên Vừa (qua `priorityText/priorityBg`) |
| `success` / `successBg` | `137333` / `E6F4EA` | Ưu tiên Thấp, trạng thái "Đã liên kết Google" |
| `orange` / `orangeSoft` | `D96B27` / `FEF0E6` | Nhóm Cuộc sống (Sáng), trạng thái "Chưa liên kết" |

### 4.4. Hàm trả màu

| Hàm | Trả về | Dùng ở |
|---|---|---|
| `chipBg(selected)`, `chipText(selected)` | Màu chip lọc (Sáng: chọn đen / chưa chọn trắng. Tối: chọn trắng / chưa chọn xám đậm) | Màn 5 |
| `categoryBg(category)`, `categoryText(category)` | Học tập: nền `E8F0FE` (Tối `1E2942`), chữ `2555FF` (Tối `6C8CFF`). Cuộc sống: nền `FEF0E6` (Tối `38261A`), chữ `D96B27` (Tối `F0904F`) | Màn 3, 4, 9 |
| `priorityBg(p)`, `priorityText(p)` | Cao = `danger`, Vừa = `warning`, Thấp = `success` | Màn 9 |

### 4.5. Màu chủ đạo (người dùng chọn ở màn 7)

`AppColors.primaryOptions` gồm 4 màu: `Blue 2555FF` (mặc định), `Green 10B981`, `Rust C2410C`, `Purple 7C3AED`. Giá trị đang chọn nằm ở `viewModel.primaryColor`.

### 4.6. Màu cố ý giữ trong màn hình

`Color.White` dùng làm màu chữ/icon **trên nền màu chủ đạo** (nút, thẻ tiến độ, Splash) và làm nền trắng của Onboarding/Đăng nhập theo thiết kế. `Color.Transparent` dùng cho viền/nền ẩn.

---

## 5. Model - khai báo gì, dùng thư viện nào

### 5.1. `model/TodoModels.kt`

File này **không import thư viện nào** (Kotlin thuần), không chứa màu hay tên màn hình.

| Khai báo | Loại | Nội dung |
|---|---|---|
| `AppLanguage` | enum | `VI`, `EN` |
| `TaskCategory` | enum | `STUDY`, `LIFE`, kèm `getName(lang)` |
| `TaskPriority` | enum | `LOW`, `MEDIUM`, `HIGH`, kèm `getName(lang)` |
| `ThemeMode` | enum | `LIGHT`, `DARK`, `AUTO`, kèm `getName(lang)` |
| `SubTask` | data class | `id`, `title`, `isDone` |
| `TaskItem` | data class | `id`, `title`, `note`, `category`, `date`, `time`, `reminderText`, `hasReminder`, `priority`, `isCompleted`, `subTasks`. Có `isToday`, `countCompletedSubTasks()`, `calculateSubTaskProgress()`, hằng `TODAY = "Hôm nay"` |
| `DayStat` | class | Thống kê một ngày cho biểu đồ tuần: nhãn VI/EN, số việc xong, tổng số việc |
| `NotificationItem` | data class | `id`, `title`, `message`, `time`, `isRead` |
| `UserProfile` | data class | `fullName`, `email`, `isGoogleLinked`, `completedCount`, `streakDays`, `onTimePercent`, `lastSyncTime`, `getShortName()` |
| `AppStrings` | class | Toàn bộ chữ hiển thị theo ngôn ngữ (xem lưu ý mục 8) |

### 5.2. Thư viện mỗi file đang dùng

| File | Thư viện |
|---|---|
| `TodoModels.kt` | Không có |
| `AppColor.kt` | `androidx.compose.ui` (Color) |
| `TodoViewModel.kt` | `androidx.compose.runtime` (state), `androidx.lifecycle` (ViewModel) |
| `AppNavigation.kt` | `androidx.navigation` (NavHost, NavController, NavType), `androidx.compose.runtime`, `androidx.compose.ui` |
| `MainActivity.kt` | `androidx.activity.compose`, `androidx.navigation.compose`, `androidx.lifecycle.viewmodel.compose`, `androidx.compose.foundation / material / material3 / runtime / ui` |
| `screens/*.kt` | `androidx.compose.foundation / material (icons) / material3 / runtime / ui`, `androidx.navigation` (NavController). Riêng `AuthScreens.kt` thêm `kotlinx.coroutines` (delay) |

Gradle (`app/build.gradle.kts`): `compose-bom`, `activity-compose`, `material3`, `ui`, `ui-graphics`, `ui-tooling-preview`, `core-ktx`, `lifecycle-runtime-ktx`, **`navigation-compose`**, `material-icons-extended`, `lifecycle-viewmodel-compose`.

---

## 6. TodoViewModel - giữ gì, làm gì

| Nhóm | State | Hàm |
|---|---|---|
| 1. Cài đặt | `themeMode`, `primaryColor`, `fontSizeScale`, `appLanguage`, `notifyReminder`, `notifySound`, `notifySummary`, `systemDark`, `strings` | `toggleLanguage()`, `isDarkTheme()` |
| 2. Tài khoản, thống kê | `userProfile`, `showGoogleLoginDialog`, `isViewingThisWeek`, `selectedDayIndex` | `connectGoogleAccount()`, `loginAsGuest()`, `logout()`, `getCurrentWeekStats()` |
| 3. Công việc | `taskList`, `searchQuery`, `selectedFilter`, `todayTasks`, `todayDoneCount`, `todayProgress` | `getTask()`, `getFilteredTasks()`, `countRemainingByCategory()`, `toggleTaskCompleted()`, `addNewTask()` (trả `true/false`), `deleteTask()`, `toggleSubTask()`, `addSubTask()`, `deleteSubTask()` |
| 4. Thông báo | `notificationList` | `markAllNotificationsRead()` |
| 5. Dữ liệu mẫu | | `loadSampleData()` (gọi trong `init`) |

ViewModel **không** chuyển màn. `addNewTask()` trả về `true/false` để màn hình tự quyết định có đóng màn hay không.

---

## 7. Quy ước comment trong code

- Đầu file: khối `// FILE: tên file - mô tả` và danh sách màn nằm trong file.
- Màn hình: `// ===== số. TÊN MÀN HÌNH =====`, số từ 1 đến 10, không lặp lại giữa các file.
- Nhóm trong ViewModel / Model / AppColors: `// ---------- số. Tên nhóm ----------`.
- Comment trong dòng: ngắn, 1-2 dòng, nói **làm gì**, không nhắc "như thiết kế".

---

## 8. Việc còn lại / hạn chế đã biết

- **Không lưu dữ liệu:** chưa có Room/DataStore. Muốn lưu cần thêm tầng Repository.
- **Đăng nhập Google chỉ là giả lập:** hộp thoại nhập tên + Gmail, số liệu 128 / 12 ngày / 86% gán cứng. Chữ "Dùng trên iPhone, iPad và Mac" và "Phiên bản 2.1.0" lấy theo bản thiết kế Figma.
- **Ngày dạng chữ:** `TaskItem.date` là `String`. Việc "hôm nay" nhận biết bằng `"Hôm nay"` nên màn Thêm việc đang để ngày mẫu "Thứ Tư, 21/10" sẽ không hiện ở Trang chủ.
- **Màn 8 có sẵn dữ liệu mẫu** (tên "Ôn chương 4 - Giải thuật", giờ 19:00) lấy từ thiết kế.
- **Chữ đa ngôn ngữ chưa phủ hết:** `AppStrings` dùng ở các màn 4-7, 10. Màn 1-3 ghi chữ Việt trực tiếp, màn 8-9 vừa dùng `AppStrings` vừa viết `if (isVi)` tại chỗ, thông báo "Đã tạo việc mới" trong ViewModel ghi chữ Việt cứng.
- **Nút sửa việc nhỏ** (có `TODO` trong code) và chữ "Sửa" ở đầu màn Chi tiết chưa có chức năng.

---

## 9. Thêm một màn hình mới

1. Viết hàm `@Composable fun TenManScreen(viewModel: TodoViewModel, navController: NavController)` trong file thuộc nhóm phù hợp ở `screens/`, đặt tiêu đề `// ===== 11. ... =====`.
2. Thêm hằng số vào `object Routes` trong `AppNavigation.kt`.
3. Thêm một dòng `composable(Routes.XXX) { TenManScreen(viewModel, navController) }` trong `AppNavHost`.
4. Nếu là tab của thanh dưới: thêm vào `Routes.TABS` và thêm một `BottomBarTab` trong `MainActivity.kt`.
5. Màu mới: thêm vào `AppColors`, không ghi `Color(0x...)` trong màn hình.
