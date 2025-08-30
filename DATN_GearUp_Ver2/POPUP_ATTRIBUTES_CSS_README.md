# CSS POPUP THUỘC TÍNH SẢN PHẨM - KHÔNG XUNG ĐỘT

## Tổng quan

File CSS `popupAttributes.css` đã được cập nhật để tránh xung đột với các file Vue khác trong dự án. CSS sử dụng prefix rõ ràng để phân biệt giữa các loại popup khác nhau.

## Cấu trúc CSS

### 1. CSS cho Popup Thuộc Tính (Màu sắc, Kích thước)

Sử dụng prefix `attribute-` để tránh xung đột:

```css
.attribute-popup-overlay      /* Overlay cho popup thuộc tính */
/* Overlay cho popup thuộc tính */
.attribute-popup-content      /* Content cho popup thuộc tính */
.attribute-popup-header       /* Header cho popup thuộc tính */
.attribute-popup-body         /* Body cho popup thuộc tính */
.attribute-popup-footer       /* Footer cho popup thuộc tính */
.attribute-close-btn          /* Nút đóng popup thuộc tính */
.attribute-search-input       /* Input tìm kiếm thuộc tính */
.attribute-item               /* Item trong danh sách thuộc tính */
.attribute-create-new-btn; /* Nút tạo mới thuộc tính */
```

### 2. CSS cho Popup Khác (Sửa nhanh, Xóa, Ảnh)

Sử dụng prefix `product-` để tránh xung đột:

```css
.product-popup-overlay        /* Overlay cho popup khác */
/* Overlay cho popup khác */
.product-popup-content        /* Content cho popup khác */
.product-popup-header         /* Header cho popup khác */
.product-popup-body           /* Body cho popup khác */
.product-popup-footer         /* Footer cho popup khác */
.product-close-btn            /* Nút đóng popup khác */
.product-quick-edit-popup     /* Popup sửa nhanh */
.product-delete-confirm-popup /* Popup xác nhận xóa */
.product-anh-popup; /* Popup chọn ảnh */
```

## Cách sử dụng

### 1. Import CSS

Thêm vào các file Vue cần sử dụng:

```vue
<style>
@import "../../../styles/cssSanPham/popupAttributes.css";
</style>
```

### 2. Sử dụng trong Template

```vue
<!-- Popup thuộc tính -->
<div class="attribute-popup-overlay">
  <div class="attribute-popup-content">
    <div class="attribute-popup-header">
      <h3>Chọn thuộc tính</h3>
      <button class="attribute-close-btn">×</button>
    </div>
    <div class="attribute-popup-body">
      <!-- Nội dung popup -->
    </div>
    <div class="attribute-popup-footer">
      <!-- Footer popup -->
    </div>
  </div>
</div>

<!-- Popup khác -->
<div class="product-popup-overlay">
  <div class="product-popup-content">
    <div class="product-popup-header">
      <h3>Tiêu đề popup</h3>
      <button class="product-close-btn">×</button>
    </div>
    <div class="product-popup-body">
      <!-- Nội dung popup -->
    </div>
    <div class="product-popup-footer">
      <!-- Footer popup -->
    </div>
  </div>
</div>
```

## Lợi ích

1. **Không xung đột**: CSS sử dụng prefix rõ ràng, không ảnh hưởng đến các file khác
2. **Dễ bảo trì**: Mỗi loại popup có CSS riêng biệt
3. **Tái sử dụng**: Có thể sử dụng cho nhiều component khác nhau
4. **Responsive**: CSS đã được thiết kế responsive cho mobile và desktop

## Responsive Design

CSS đã được thiết kế responsive với các breakpoint:

```css
@media (max-width: 768px) {
  /* Tablet và mobile */
}

@media (max-width: 480px) {
  /* Mobile nhỏ */
}
```

## Animation

CSS bao gồm các animation mượt mà:

- `attributeFadeIn`: Fade in cho popup thuộc tính
- `attributeSlideUp`: Slide up cho popup thuộc tính
- `productFadeIn`: Fade in cho popup khác
- `productSlideUp`: Slide up cho popup khác

## Lưu ý

- Không sửa đổi CSS của `.breadcrumb-nav` (theo quy tắc dự án)
- Tất cả CSS đã được prefix để tránh xung đột
- Sử dụng đúng class name để đảm bảo hoạt động chính xác
- CSS đã được tối ưu hóa cho hiệu suất
