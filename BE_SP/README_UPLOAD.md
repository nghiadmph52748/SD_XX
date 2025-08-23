# Hướng dẫn sử dụng File Upload cho AnhSanPham

## Cấu hình đã thêm

### 1. Backend Configuration
- **File Upload Config**: Đã cấu hình trong `application.properties`
  - `spring.servlet.multipart.max-file-size=10MB`
  - `spring.servlet.multipart.max-request-size=10MB`
  - `spring.servlet.multipart.enabled=true`

- **Static File Serving**: Đã tạo `WebConfig.java` để serve files từ thư mục `uploads/`

### 2. Thư mục Upload
- Thư mục: `BE_SP/uploads/images/`
- Files sẽ được lưu với tên duy nhất (UUID + extension)

### 3. API Endpoints
- **POST** `/api/anh-san-pham-management/add` - Thêm ảnh mới
- **PUT** `/api/anh-san-pham-management/update/{id}` - Cập nhật ảnh
- **PUT** `/api/anh-san-pham-management/update/status/{id}` - Cập nhật trạng thái

## Cách sử dụng

### Frontend (Vue.js)
```javascript
// Tạo FormData
const formData = new FormData();
formData.append('file', fileInput.files[0]);
formData.append('loaiAnh', 'main');
formData.append('moTa', 'Ảnh chính sản phẩm');
formData.append('deleted', false);

// Gửi request
await fetchCreateAnhSanPham(formData);
```

### Backend (Java)
```java
@PostMapping("/add")
public ResponseObject<?> add(
    @RequestParam("file") MultipartFile file,
    @RequestParam("loaiAnh") String loaiAnh,
    @RequestParam(value = "moTa", required = false) String moTa,
    @RequestParam(value = "deleted", defaultValue = "false") Boolean deleted) {
    
    // File sẽ được tự động upload và lưu vào thư mục uploads/images/
    String filePath = anhSanPhamService.uploadFile(file);
    
    // Lưu đường dẫn vào database
    AnhSanPhamRequest request = new AnhSanPhamRequest();
    request.setDuongDanAnh(filePath);
    // ... set các field khác
    
    anhSanPhamService.add(request);
    return new ResponseObject<>(null, "Thêm ảnh sản phẩm thành công");
}
```

## Lưu ý quan trọng

1. **File Size**: Giới hạn 10MB cho mỗi file
2. **File Types**: Chỉ chấp nhận file ảnh (jpg, png, gif, etc.)
3. **Security**: Files được lưu với tên duy nhất để tránh conflict
4. **Path**: Files được serve qua `/uploads/images/filename`

## Troubleshooting

### Lỗi "Failed to create product image"
- Kiểm tra thư mục `uploads/images/` có tồn tại không
- Kiểm tra quyền ghi vào thư mục
- Kiểm tra log backend để xem lỗi chi tiết

### Files không hiển thị
- Kiểm tra `WebConfig.java` đã được load
- Kiểm tra đường dẫn trong database có đúng format `/uploads/images/filename`
- Kiểm tra backend có đang chạy không
