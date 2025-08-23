# Thay đổi cấu trúc Quản lý Sản phẩm

## Tổng quan
Đã chỉnh sửa phần "QUẢN LÝ SẢN PHẨM" theo yêu cầu, chia thành 3 mục chính:

1. **Danh sách sản phẩm** (`/products`)
2. **Chi tiết sản phẩm** (`/products/details`) 
3. **Thuộc tính sản phẩm** (`/products/attributes`)

## Chi tiết thay đổi

### 1. Menu Navigation (App.vue)
- Cập nhật menu "QUẢN LÝ SẢN PHẨM" chỉ hiển thị 3 mục chính
- Các thuộc tính chi tiết được gom vào trang "Thuộc tính sản phẩm"

### 2. Trang mới: Thuộc tính sản phẩm
- **File**: `src/pages/products/ThuocTinhSanPham.vue`
- **Route**: `/products/attributes`
- **Chức năng**: Hiển thị tất cả 13 thuộc tính sản phẩm dưới dạng card grid

### 3. Các thuộc tính được gom nhóm
Trang "Thuộc tính sản phẩm" bao gồm:
- Xuất xứ
- Nhà sản xuất  
- Màu sắc
- Kích thước
- Đế giày
- Chất liệu
- Đệm giày
- Trọng lượng
- Môn thể thao
- Loại mùa
- Độ bền
- Chống nước
- Ảnh sản phẩm

### 4. Routing (main.js)
- Thêm route mới: `/products/attributes` → `ThuocTinhSanPham.vue`
- Giữ nguyên tất cả route hiện có cho các thuộc tính

### 5. Page Titles (App.vue)
- Cập nhật mapping page title cho route mới
- Sắp xếp lại thứ tự hiển thị

## Đặc điểm kỹ thuật

### Giữ nguyên
- ✅ Tất cả code CRUD đã có trong các thuộc tính
- ✅ API endpoints hiện có
- ✅ Cấu trúc database
- ✅ Các component Vue đã tồn tại

### Mới thêm
- ✅ Trang "Thuộc tính sản phẩm" với giao diện card grid
- ✅ Navigation từ trang thuộc tính đến từng thuộc tính cụ thể
- ✅ Responsive design cho mobile và desktop

## Cách sử dụng

1. **Truy cập menu**: QUẢN LÝ SẢN PHẨM → Thuộc tính sản phẩm
2. **Xem tổng quan**: Tất cả thuộc tính được hiển thị dưới dạng card
3. **Quản lý chi tiết**: Click vào từng card để vào trang quản lý thuộc tính cụ thể
4. **Quay lại**: Sử dụng breadcrumb hoặc menu để quay lại

## Lợi ích

- **Giao diện gọn gàng**: Menu chính chỉ hiển thị 3 mục chính
- **Dễ truy cập**: Tất cả thuộc tính được tập trung tại một nơi
- **Không mất dữ liệu**: Giữ nguyên toàn bộ chức năng CRUD
- **Responsive**: Hoạt động tốt trên mọi thiết bị
- **UX tốt hơn**: Người dùng dễ dàng tìm và quản lý thuộc tính

## Cấu trúc file

```
src/
├── pages/
│   └── products/
│       ├── ThuocTinhSanPham.vue (MỚI)
│       ├── XuatXu.vue
│       ├── NhaSanXuat.vue
│       ├── MauSac.vue
│       ├── KichThuoc.vue
│       ├── DeGiay.vue
│       ├── ChatLieu.vue
│       ├── DemGiay.vue
│       ├── TrongLuong.vue
│       ├── MonTheThao.vue
│       ├── LoaiMua.vue
│       ├── DoBen.vue
│       ├── ChongNuoc.vue
│       └── AnhSanPham.vue
├── App.vue (ĐÃ CẬP NHẬT)
└── main.js (ĐÃ CẬP NHẬT)
```

## Lưu ý

- Tất cả API calls vẫn sử dụng endpoints hiện có
- Không có thay đổi về database schema
- Không có thay đổi về business logic
- Chỉ thay đổi giao diện và navigation
