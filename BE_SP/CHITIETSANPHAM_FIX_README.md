# Khắc Phục Lỗi ChiTietSanPham API

## Vấn đề
API `/api/chi-tiet-san-pham-management/playlist` trả về lỗi 400 khi gọi từ frontend.

## Nguyên nhân
1. **Null Pointer Exception**: Khi tạo `ChiTietSanPhamResponse`, một số thuộc tính có thể null gây ra lỗi
2. **Missing Images**: Một số chi tiết sản phẩm không có ảnh, gây ra exception
3. **Lazy Loading Issues**: Các relationship không được load đúng cách

## Các thay đổi đã thực hiện

### 1. Sửa ChiTietSanPhamResponse.java
- Thêm null safety cho tất cả các thuộc tính
- Xử lý trường hợp không có ảnh một cách nhẹ nhàng
- Không throw exception khi thiếu dữ liệu

### 2. Sửa ChiTietSanPhamFullResponse.java
- Thêm null safety tương tự
- Xử lý ảnh sản phẩm an toàn

### 3. Sửa ChiTietSanPhamService.java
- Thêm logging để debug
- Sử dụng method mới `findAllWithValidData()` từ repository
- Thêm try-catch để xử lý lỗi

### 4. Sửa ChiTietSanPhamRepository.java
- Thêm method `findAllWithValidData()` với JOIN FETCH
- Đảm bảo tất cả relationship được load đúng cách

### 5. Sửa ChiTietSanPhamController.java
- Thêm try-catch và logging
- Xử lý lỗi một cách nhẹ nhàng

### 6. Sửa ChiTietSanPham.java (Entity)
- Thêm `@JsonIgnore` cho các field có thể gây vấn đề
- Tránh infinite loop khi serialize

### 7. Sửa ChiTietSanPhamService.js (Frontend)
- Thêm logging chi tiết
- Xử lý lỗi tốt hơn

### 8. Sửa ChiTietSanPham.vue (Frontend)
- Cập nhật error handling
- Thêm validation cho response

## Cách test

### 1. Sử dụng file test-api.html
```bash
# Mở file test-api.html trong browser
# Click các button để test API
```

### 2. Kiểm tra logs
```bash
# Xem logs của Spring Boot application
# Tìm các log liên quan đến ChiTietSanPham
```

### 3. Kiểm tra database
```sql
-- Kiểm tra dữ liệu trong bảng chi_tiet_san_pham
SELECT * FROM chi_tiet_san_pham WHERE id_san_pham IS NULL;

-- Kiểm tra các relationship
SELECT ctsp.*, sp.ten_san_pham 
FROM chi_tiet_san_pham ctsp 
LEFT JOIN san_pham sp ON ctsp.id_san_pham = sp.id;
```

## Các bước tiếp theo

1. **Restart Spring Boot application** sau khi thay đổi code
2. **Clear browser cache** và reload trang
3. **Kiểm tra console** của browser để xem logs
4. **Kiểm tra logs** của Spring Boot application

## Lưu ý
- Đảm bảo tất cả các bảng liên quan có dữ liệu hợp lệ
- Kiểm tra foreign key constraints
- Đảm bảo không có dữ liệu null trong các trường bắt buộc

## Troubleshooting
Nếu vẫn gặp lỗi:
1. Kiểm tra logs chi tiết
2. Sử dụng file test-api.html để test riêng lẻ
3. Kiểm tra database connection
4. Kiểm tra CORS configuration
