<template>
  <div class="thuoc-tinh-container">
    <div class="header">
      <h1>Thuộc tính sản phẩm</h1>
      <p>Quản lý các thuộc tính cơ bản của sản phẩm</p>
    </div>

là     <div class="menu-container">
      <!-- Main Menu Item -->
      <div class="main-menu-item">
        <div class="menu-header">
          <div class="menu-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2L2 7L12 12L22 7L12 2ZM2 17L12 22L22 17M2 12L12 17L22 12"/>
            </svg>
          </div>
          <div class="menu-title">
            <h2>Thuộc tính sản phẩm</h2>
            <p>Chọn thuộc tính cần quản lý</p>
          </div>
          <div class="menu-arrow">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M7 10l5 5 5-5z"/>
            </svg>
          </div>
        </div>

        <!-- Submenu with 13 attributes -->
        <div class="submenu">
          <div class="submenu-grid">
            <!-- Xuất xứ -->
            <div class="submenu-item" @click="navigateTo('/products/xuat-xu')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Xuất xứ</h3>
                <p>Quản lý nguồn gốc xuất xứ sản phẩm</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Nhà sản xuất -->
            <div class="submenu-item" @click="navigateTo('/products/nha-san-xuat')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2L2 7L12 12L22 7L12 2ZM2 17L12 22L22 17M2 12L12 17L22 12"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Nhà sản xuất</h3>
                <p>Quản lý thông tin nhà sản xuất</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Màu sắc -->
            <div class="submenu-item" @click="navigateTo('/products/mau-sac')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Màu sắc</h3>
                <p>Quản lý bảng màu sản phẩm</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Kích thước -->
            <div class="submenu-item" @click="navigateTo('/products/kich-thuoc')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 3H5C3.89 3 3 3.89 3 5V19C3 20.11 3.89 21 5 21H19C20.11 21 21 20.11 21 19V5C21 3.89 20.11 3 19 3M19 19H5V5H19V19M17 17H7V7H17V17M15 15H9V9H15V15Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Kích thước</h3>
                <p>Quản lý các kích thước sản phẩm</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Đế giày -->
            <div class="submenu-item" @click="navigateTo('/products/de-giay')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Đế giày</h3>
                <p>Quản lý các loại đế giày</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Chất liệu -->
            <div class="submenu-item" @click="navigateTo('/products/chat-lieu')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2L2 7L12 12L22 7L12 2ZM2 17L12 22L22 17M2 12L12 17L22 12"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Chất liệu</h3>
                <p>Quản lý các loại chất liệu</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Đệm giày -->
            <div class="submenu-item" @click="navigateTo('/products/dem-giay')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Đệm giày</h3>
                <p>Quản lý các loại đệm giày</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Trọng lượng -->
            <div class="submenu-item" @click="navigateTo('/products/trong-luong')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2A10 10 0 0 0 2 12A10 10 0 0 0 12 22A10 10 0 0 0 22 12A10 10 0 0 0 12 2M12 4A8 8 0 0 1 20 12A8 8 0 0 1 12 20A8 8 0 0 1 4 12A8 8 0 0 1 12 4M12 6A6 6 0 0 0 6 12A6 6 0 0 0 12 18A6 6 0 0 0 18 12A6 6 0 0 0 12 6M12 8A4 4 0 0 1 16 12A4 4 0 0 1 12 16A4 4 0 0 1 8 12A4 4 0 0 1 12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Trọng lượng</h3>
                <p>Quản lý trọng lượng sản phẩm</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Môn thể thao -->
            <div class="submenu-item" @click="navigateTo('/products/mon-the-thao')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Môn thể thao</h3>
                <p>Quản lý các môn thể thao</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Loại mùa -->
            <div class="submenu-item" @click="navigateTo('/products/loai-mua')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Loại mùa</h3>
                <p>Quản lý các loại mùa</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Độ bền -->
            <div class="submenu-item" @click="navigateTo('/products/do-ben')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Độ bền</h3>
                <p>Quản lý các mức độ bền</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Chống nước -->
            <div class="submenu-item" @click="navigateTo('/products/chong-nuoc')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 4V6L21 9ZM3 9L9 6V4L3 7V9ZM15 18V20L21 17V15L15 18ZM3 15V17L9 20V18L3 15ZM12 8L18 11V13L12 16L6 13V11L12 8Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Chống nước</h3>
                <p>Quản lý các mức chống nước</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>

            <!-- Ảnh sản phẩm -->
            <div class="submenu-item" @click="navigateTo('/products/anh-san-pham')">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.5 13.5L11 16.5L14.5 12L19 18H5M21 19V5C21 3.89 20.1 3 19 3H5A2 2 0 0 0 3 5V19A2 2 0 0 0 5 21H19A2 2 0 0 0 21 19Z"/>
                </svg>
              </div>
              <div class="item-content">
                <h3>Ảnh sản phẩm</h3>
                <p>Quản lý hình ảnh sản phẩm</p>
              </div>
              <div class="item-arrow">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z"/>
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.thuoc-tinh-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  text-align: center;
  margin-bottom: 32px;
}

.header h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 8px;
}

.header p {
  font-size: 1.1rem;
  color: #6b7280;
  margin: 0;
}

.menu-container {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.main-menu-item {
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  overflow: hidden;
}

.menu-header {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  border-bottom: 1px solid #e5e7eb;
}

.menu-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  border-radius: 16px;
  color: white;
  flex-shrink: 0;
}

.menu-title {
  flex: 1;
}

.menu-title h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
}

.menu-title p {
  font-size: 0.95rem;
  color: #6b7280;
  margin: 0;
}

.menu-arrow {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  color: #6b7280;
  transition: transform 0.3s ease;
}

.submenu {
  background: white;
  padding: 24px;
}

.submenu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.submenu-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #f8fafc;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.submenu-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #3b82f6, #8b5cf6);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.submenu-item:hover {
  background: #f1f5f9;
  border-color: #3b82f6;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.15);
}

.submenu-item:hover::before {
  transform: scaleX(1);
}

.item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #dbeafe, #bfdbfe);
  border-radius: 10px;
  color: #1d4ed8;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.submenu-item:hover .item-icon {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  color: white;
  transform: scale(1.1);
}

.item-content {
  flex: 1;
  min-width: 0;
}

.item-content h3 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
  line-height: 1.3;
}

.item-content p {
  font-size: 0.875rem;
  color: #6b7280;
  margin: 0;
  line-height: 1.4;
}

.item-arrow {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  color: #9ca3af;
  transition: all 0.3s ease;
}

.submenu-item:hover .item-arrow {
  color: #3b82f6;
  transform: translateX(4px);
}

/* Responsive Design */
@media (max-width: 768px) {
  .thuoc-tinh-container {
    padding: 16px;
  }
  
  .header h1 {
    font-size: 2rem;
  }
  
  .menu-header {
    padding: 20px;
    gap: 16px;
  }
  
  .menu-icon {
    width: 48px;
    height: 48px;
  }
  
  .menu-title h2 {
    font-size: 1.25rem;
  }
  
  .submenu {
    padding: 20px;
  }
  
  .submenu-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .submenu-item {
    padding: 14px;
  }
}

@media (max-width: 480px) {
  .header h1 {
    font-size: 1.75rem;
  }
  
  .menu-header {
    padding: 16px;
    flex-direction: column;
    text-align: center;
  }
  
  .menu-icon {
    width: 40px;
    height: 40px;
  }
  
  .submenu {
    padding: 16px;
  }
  
  .submenu-item {
    padding: 12px;
  }
  
  .item-icon {
    width: 32px;
    height: 32px;
  }
}
</style>
