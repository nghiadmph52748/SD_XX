<template>
  <div class="product-details">
    <!-- Modern Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">QUẢN LÝ CHI TIẾT SẢN PHẨM</h1>
          <p class="page-subtitle">
            Quản lý biến thể sản phẩm, giá bán và số lượng tồn kho
          </p>
        </div>
        <div class="header-actions">
          <button class="btn-refresh" @click="refreshData">
            <span class="btn-icon">🔄</span>
            Làm mới
          </button>
          <button class="btn-export" @click="forceRefreshImageData" title="Làm mới dữ liệu ảnh">
            <span class="btn-icon">🖼️</span>
            Làm mới ảnh
          </button>
          <button class="btn-export" @click="exportData">
            <span class="btn-icon">📊</span>
            Xuất báo cáo
          </button>
          <button class="btn-export" @click="exportDetailsToExcel">
            <span class="btn-icon">📗</span>
            Xuất Excel
          </button>
          <button class="btn-add" @click="openAddModal">
            <span class="btn-icon">➕</span>
            Thêm chi tiết SP
          </button>
        </div>
      </div>
    </div>

    <!-- Modern Filter Section -->
    <div class="filter-section">
      <div class="filter-card">
        <div class="filter-header">
          <div class="filter-title">
            <span class="filter-icon">👟</span>
            <h3>Tìm kiếm & Lọc chi tiết sản phẩm</h3>
          </div>
          <div class="filter-stats">
            {{ filteredDetails.length }} / {{ chiTietSanPhams.length }} chi tiết sản phẩm
          </div>
        </div>

        <div class="filter-content">
          <div class="search-section">
            <div class="input-group">
              <span class="input-icon">🔍</span>
              <input v-model="searchQuery" type="text" placeholder="Tìm kiếm tên sản phẩm, màu sắc, kích thước..."
                class="form-control search-input" />
              <button v-if="searchQuery" @click="searchQuery = ''" class="clear-btn">
                <span>✕</span>
              </button>
            </div>
          </div>

          <div class="filters-grid">
            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">📦</span>
                Sản phẩm
              </label>
              <select v-model="selectedSanPham" class="form-select">
                <option value="">Tất cả sản phẩm</option>
                <option v-for="product in sanPhams" :key="product.id" :value="product.id">
                  {{ product.tenSanPham }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🎨</span>
                Màu sắc
              </label>
              <select v-model="selectedMauSac" class="form-select">
                <option value="">Tất cả màu sắc</option>
                <option v-for="color in mauSacs" :key="color.id" :value="color.id">
                  {{ color.tenMauSac }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">📏</span>
                Kích thước
              </label>
              <select v-model="selectedKichThuoc" class="form-select">
                <option value="">Tất cả kích thước</option>
                <option v-for="size in kichThuocs" :key="size.id" :value="size.id">
                  {{ size.tenKichThuoc }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🧵</span>
                Chất liệu
              </label>
              <select v-model="selectedChatLieu" class="form-select">
                <option value="">Tất cả chất liệu</option>
                <option v-for="material in chatLieus" :key="material.id" :value="material.id">
                  {{ material.tenChatLieu }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">👟</span>
                Đế giày
              </label>
              <select v-model="selectedDeGiay" class="form-select">
                <option value="">Tất cả đế giày</option>
                <option v-for="sole in deGiays" :key="sole.id" :value="sole.id">
                  {{ sole.tenDeGiay }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🦶</span>
                Đệm giày
              </label>
              <select v-model="selectedDemGiay" class="form-select">
                <option value="">Tất cả đệm giày</option>
                <option v-for="insole in demGiays" :key="insole.id" :value="insole.id">
                  {{ insole.tenDemGiay }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">⚖️</span>
                Trọng lượng
              </label>
              <select v-model="selectedTrongLuong" class="form-select">
                <option value="">Tất cả trọng lượng</option>
                <option v-for="weight in trongLuongs" :key="weight.id" :value="weight.id">
                  {{ weight.tenTrongLuong }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🏃</span>
                Môn thể thao
              </label>
              <select v-model="selectedMonTheThao" class="form-select">
                <option value="">Tất cả môn thể thao</option>
                <option v-for="sport in monTheThaos" :key="sport.id" :value="sport.id">
                  {{ sport.tenMonTheThao }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🌤️</span>
                Loại mùa
              </label>
              <select v-model="selectedLoaiMua" class="form-select">
                <option value="">Tất cả loại mùa</option>
                <option v-for="season in loaiMuas" :key="season.id" :value="season.id">
                  {{ season.tenLoaiMua }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🛡️</span>
                Độ bền
              </label>
              <select v-model="selectedDoBen" class="form-select">
                <option value="">Tất cả độ bền</option>
                <option v-for="durability in doBens" :key="durability.id" :value="durability.id">
                  {{ durability.tenDoBen }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">💧</span>
                Chống nước
              </label>
              <select v-model="selectedChongNuoc" class="form-select">
                <option value="">Tất cả chống nước</option>
                <option v-for="waterproof in chongNuocs" :key="waterproof.id" :value="waterproof.id">
                  {{ waterproof.tenChongNuoc }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">⚡</span>
                Trạng thái
              </label>
              <select v-model="statusFilter" class="form-select">
                <option value="">Tất cả trạng thái</option>
                <option value="1">✅ Hoạt động</option>
                <option value="0">❌ Ngừng hoạt động</option>
              </select>
            </div>

                         <div class="filter-actions">
               <button @click="clearFilters" class="btn btn-outline">
                 <span class="btn-icon">🔄</span>
                 Đặt lại
               </button>
             </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Product Details Table -->
    <div class="card">
      <div class="card-body">
        <div class="table-container">
          <table class="product-table">
            <thead>
              <tr>
                <th class="stt-col">STT</th>
                <th class="product-col">Tên Sản Phẩm</th>
                <th class="color-col">Màu sắc</th>
                <th class="size-col">Kích thước</th>
                <th class="sole-col">Đế giày</th>
                <th class="material-col">Chất liệu</th>
                <th class="insole-col">Đệm giày</th>
                <th class="weight-col">Trọng lượng</th>
                <th class="sport-col">Môn thể thao</th>
                <th class="season-col">Loại mùa</th>
                <th class="durability-col">Độ bền</th>
                <th class="waterproof-col">Chống nước</th>
                <th class="image-col">Ảnh sản phẩm</th>
                <th class="quantity-col">Số lượng</th>
                <th class="price-col">Giá bán</th>
                <th class="status-col">Trạng thái</th>
                <th class="action-col">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(detail, index) in paginatedDetails" :key="detail.id">
                <td class="stt-col">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
                <td class="product-col">
                  <div class="product-info">
                    <strong>{{ detail.tenSanPham || detail.sanPham?.tenSanPham || 'N/A' }}</strong>
                  </div>
                </td>
                <td class="color-col">
                  <span class="color-badge"
                    :style="{ backgroundColor: getColorCode(detail.tenMauSac || detail.mauSac?.tenMauSac) }">
                    {{ detail.tenMauSac || detail.mauSac?.tenMauSac || 'N/A' }}
                  </span>
                </td>
                <td class="size-col">
                  <span class="size-badge">{{ detail.tenKichThuoc || detail.kichThuoc?.tenKichThuoc || 'N/A' }}</span>
                </td>
                <td class="sole-col">
                  <span class="attribute-text">{{ detail.tenDeGiay || detail.deGiay?.tenDeGiay || 'N/A' }}</span>
                </td>
                <td class="material-col">
                  <span class="attribute-text">{{ detail.tenChatLieu || detail.chatLieu?.tenChatLieu || 'N/A' }}</span>
                </td>
                <td class="insole-col">
                  <span class="attribute-text">{{ detail.tenDemGiay || detail.demGiay?.tenDemGiay || 'N/A' }}</span>
                </td>
                <td class="weight-col">
                  <span class="attribute-text">{{ detail.tenTrongLuong || detail.trongLuong?.tenTrongLuong || 'N/A'
                  }}</span>
                </td>
                <td class="sport-col">
                  <span class="attribute-text">{{ detail.tenMonTheThao || detail.monTheThao?.tenMonTheThao || 'N/A'
                  }}</span>
                </td>
                <td class="season-col">
                  <span class="attribute-text">{{ detail.tenLoaiMua || detail.loaiMua?.tenLoaiMua || 'N/A' }}</span>
                </td>
                <td class="durability-col">
                  <span class="attribute-text">{{ detail.tenDoBen || detail.doBen?.tenDoBen || 'N/A' }}</span>
                </td>
                <td class="waterproof-col">
                  <span class="attribute-text">{{ detail.tenChongNuoc || detail.chongNuoc?.tenChongNuoc || 'N/A'
                  }}</span>
                </td>
                <td class="image-col" :key="`image-${detail.id}-${imageDataKey.timestamp}`">
                  <div v-if="getImagesForChiTietSanPham(detail.id).length > 0" class="image-preview">
                    <img :src="getImageUrl(getImagesForChiTietSanPham(detail.id)[0].duongDanAnh)" :alt="detail.tenSanPham || detail.sanPham?.tenSanPham" class="product-image" />
                    <span v-if="getImagesForChiTietSanPham(detail.id).length > 1" class="image-count">+{{ getImagesForChiTietSanPham(detail.id).length - 1 }}</span>
                  </div>
                  <span v-else class="no-image">Không có ảnh</span>
                </td>
                <td class="quantity-col">
                  <span
                    :class="[
                      'stock-badge',
                      { 'low-stock': detail.soLuong < 10 },
                    ]"
                  >
                    {{ detail.soLuong }}
                  </span>
                </td>
                <td class="price-col">
                  <span class="price-text">{{
                    formatCurrency(detail.giaBan)
                  }}</span>
                </td>
                <td class="status-col">
                  <span
                    class="status-badge"
                    :class="
                      detail.trangThai ? 'status-active' : 'status-inactive'
                    "
                  >
                    {{ detail.trangThai ? "Hoạt động" : "Ngừng hoạt động" }}
                  </span>
                </td>
                <td class="action-col">
                  <div class="action-buttons">
                    <button
                      class="btn-edit"
                      @click="editDetail(detail)"
                      title="Sửa"
                    >
                      <span class="btn-icon">✏️</span>
                    </button>
                    <button
                      class="btn-delete"
                      @click="deleteDetail(detail.id)"
                      title="Xóa"
                    >
                      <span class="btn-icon">🗑️</span>
                    </button>
                  </div>
                </td>
              </tr>
              <tr v-if="paginatedDetails.length === 0">
                <td colspan="17" class="no-data">Không có dữ liệu</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của
            {{ filteredDetails.length }} chi tiết sản phẩm
          </div>
          <div class="pagination">
            <button class="btn-export" @click="previousPage" :disabled="currentPage === 1">
              <span class="btn-icon">❮</span>
            </button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button class="btn-export" @click="nextPage" :disabled="currentPage === totalPages">
              <span class="btn-icon">❯</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Add/Edit Modal -->
    <div
      v-if="showAddModal || showEditModal"
      class="modal-overlay"
      @click="closeModals"
    >
      <div class="modal-content large" @click.stop>
        <div class="modal-header">
          <h3>
            {{
              showAddModal
                ? "Thêm chi tiết sản phẩm"
                : "Cập nhật chi tiết sản phẩm"
            }}
          </h3>
          <button class="modal-close" @click="closeModals">×</button>
        </div>
                 <div class="modal-body">
           <div class="form-grid">
             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">📦</span>
                 Sản phẩm *
               </label>
               <select v-model="newChiTietSanPham.idSanPham" class="form-control" required>
                 <option value="">Chọn sản phẩm</option>
                 <option v-for="product in sanPhams" :key="product.id" :value="product.id">
                   {{ product.tenSanPham }} ({{ product.maSanPham }})
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">🎨</span>
                 Màu sắc *
               </label>
               <select v-model="newChiTietSanPham.idMauSac" class="form-control" required>
                 <option value="">Chọn màu sắc</option>
                 <option v-for="color in mauSacs" :key="color.id" :value="color.id">
                   {{ color.tenMauSac }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">📏</span>
                 Kích thước *
               </label>
               <select v-model="newChiTietSanPham.idKichThuoc" class="form-control" required>
                 <option value="">Chọn kích thước</option>
                 <option v-for="size in kichThuocs" :key="size.id" :value="size.id">
                   {{ size.tenKichThuoc }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">🧵</span>
                 Chất liệu *
               </label>
               <select v-model="newChiTietSanPham.idChatLieu" class="form-control" required>
                 <option value="">Chọn chất liệu</option>
                 <option v-for="material in chatLieus" :key="material.id" :value="material.id">
                   {{ material.tenChatLieu }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">👟</span>
                 Đế giày
               </label>
               <select v-model="newChiTietSanPham.idDeGiay" class="form-control">
                 <option value="">Chọn đế giày</option>
                 <option v-for="sole in deGiays" :key="sole.id" :value="sole.id">
                   {{ sole.tenDeGiay }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">🦶</span>
                 Đệm giày
               </label>
               <select v-model="newChiTietSanPham.idDemGiay" class="form-control">
                 <option value="">Chọn đệm giày</option>
                 <option v-for="insole in demGiays" :key="insole.id" :value="insole.id">
                   {{ insole.tenDemGiay }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">⚖️</span>
                 Trọng lượng
               </label>
               <select v-model="newChiTietSanPham.idTrongLuong" class="form-control">
                 <option value="">Chọn trọng lượng</option>
                 <option v-for="weight in trongLuongs" :key="weight.id" :value="weight.id">
                   {{ weight.tenTrongLuong }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">🏃</span>
                 Môn thể thao
               </label>
               <select v-model="newChiTietSanPham.idMonTheThao" class="form-control">
                 <option value="">Chọn môn thể thao</option>
                 <option v-for="sport in monTheThaos" :key="sport.id" :value="sport.id">
                   {{ sport.tenMonTheThao }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">🌤️</span>
                 Loại mùa
               </label>
               <select v-model="newChiTietSanPham.idLoaiMua" class="form-control">
                 <option value="">Chọn loại mùa</option>
                 <option v-for="season in loaiMuas" :key="season.id" :value="season.id">
                   {{ season.tenLoaiMua }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">🛡️</span>
                 Độ bền
               </label>
               <select v-model="newChiTietSanPham.idDoBen" class="form-control">
                 <option value="">Chọn độ bền</option>
                 <option v-for="durability in doBens" :key="durability.id" :value="durability.id">
                   {{ durability.tenDoBen }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">💧</span>
                 Chống nước
               </label>
               <select v-model="newChiTietSanPham.idChongNuoc" class="form-control">
                 <option value="">Chọn chống nước</option>
                 <option v-for="waterproof in chongNuocs" :key="waterproof.id" :value="waterproof.id">
                   {{ waterproof.tenChongNuoc }}
                 </option>
               </select>
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">💰</span>
                 Giá bán *
               </label>
               <input v-model="newChiTietSanPham.giaBan" type="number" class="form-control" placeholder="Nhập giá bán"
                 min="0" step="1000" required />
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">📦</span>
                 Số lượng *
               </label>
               <input v-model="newChiTietSanPham.soLuong" type="number" class="form-control" placeholder="Nhập số lượng"
                 min="0" required />
             </div>

             <div class="form-group">
               <label class="form-label">
                 <span class="label-icon">⚡</span>
                 Trạng thái
               </label>
               <select v-model="newChiTietSanPham.trangThai" class="form-control">
                 <option :value="true">✅ Hoạt động</option>
                 <option :value="false">❌ Ngừng hoạt động</option>
               </select>
             </div>

                           <div class="form-group span-2">
                <label class="form-label">
                  <span class="label-icon">🖼️</span>
                  Ảnh sản phẩm
                </label>
                <div class="image-upload-section">
                  <div class="image-preview-grid">
                    <div v-for="(image, index) in selectedImages" :key="index" class="image-preview-item">
                      <img :src="getImageUrl(image)" :alt="`Ảnh ${index + 1}`" class="preview-image" />
                      <button type="button" @click="removeImage(index)" class="remove-image-btn">×</button>
                    </div>
                    <div v-if="selectedImages.length < 5" class="image-upload-btn" @click="openImageSelector">
                      <span class="upload-icon">📷</span>
                      <span class="upload-text">Thêm ảnh</span>
                    </div>
                  </div>
                  <p class="image-help-text">Chọn tối đa 5 ảnh cho sản phẩm</p>
                </div>
              </div>

              <div class="form-group span-2">
                <label class="form-label">
                  <span class="label-icon">📝</span>
                  Ghi chú
                </label>
                <textarea v-model="newChiTietSanPham.ghiChu" class="form-control" rows="3"
                  placeholder="Nhập ghi chú..."></textarea>
              </div>
           </div>
         </div>
        <div class="modal-footer">
          <button class="btn-save" @click="saveDetail">
            <span class="btn-icon">{{ showAddModal ? "➕" : "💾" }}</span>
            {{ showAddModal ? "Thêm" : "Cập nhật" }}
          </button>
                 </div>
       </div>
     </div>

     <!-- Image Selector Modal -->
     <div v-if="showImageSelector" class="modal-overlay" @click="closeImageSelector">
       <div class="modal-content image-selector" @click.stop>
         <div class="modal-header">
           <h3>Chọn ảnh sản phẩm</h3>
           <button class="modal-close" @click="closeImageSelector">×</button>
         </div>
         <div class="modal-body">
           <div class="image-grid">
             <div v-for="image in availableImages" :key="image.id" 
                  class="image-item" 
                  :class="{ 'selected': selectedImageIds.includes(image.id) }"
                  @click="toggleImageSelection(image.id)">
               <img :src="getImageUrl(image.duongDanAnh)" :alt="image.loaiAnh || 'Ảnh sản phẩm'" class="grid-image" />
               <div class="image-overlay">
                 <span class="check-icon">✓</span>
               </div>
               <div class="image-info">
                 <span class="image-type">{{ image.loaiAnh || 'N/A' }}</span>
               </div>
             </div>
           </div>
         </div>
         <div class="modal-footer">
           <button class="btn-save" @click="confirmImageSelection">
             <span class="btn-icon">✅</span>
             Xác nhận
           </button>
         </div>
       </div>
     </div>
   </div>
 </template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { fetchAllChiTietSanPham, fetchCreateChiTietSanPham, fetchUpdateChiTietSanPham, fetchUpdateStatusChiTietSanPham } from '../../services/SanPham/ChiTietSanPhamService'
import { fetchAllAnhSanPham } from '../../services/ThuocTinh/AnhSanPhamService'
import { fetchAllChiTietSanPhamAnh, fetchCreateChiTietSanPhamAnh, fetchCreateMultipleChiTietSanPhamAnh, fetchUpdateChiTietSanPhamAnh, fetchDeleteChiTietSanPhamAnh } from '../../services/ThuocTinh/ChiTietSanPhamAnhService'
import { fetchAllMauSac } from '../../services/ThuocTinh/MauSacService'
import { fetchAllKichThuoc } from '../../services/ThuocTinh/KichThuocService'
import { fetchAllDeGiay } from '../../services/ThuocTinh/DeGiayService'
import { fetchAllChatLieu } from '../../services/ThuocTinh/ChatLieuService'
import { fetchAllDemGiay } from '../../services/ThuocTinh/DemGiayService'
import { fetchAllTrongLuong } from '../../services/ThuocTinh/TrongLuongService'
import { fetchAllMonTheThao } from '../../services/ThuocTinh/MonTheThaoService'
import { fetchAllLoaiMua } from '../../services/ThuocTinh/LoaiMuaService'
import { fetchAllDoBen } from '../../services/ThuocTinh/DoBenService'
import { fetchAllChongNuoc } from '../../services/ThuocTinh/ChongNuocService'
import { fetchAllSanPham } from '../../services/SanPham/SanPhamService'
// Reactive data
const searchQuery = ref('')
const selectedSanPham = ref('')
const selectedMauSac = ref('')
const selectedKichThuoc = ref('')
const selectedChatLieu = ref('')
const selectedDeGiay = ref('')
const selectedDemGiay = ref('')
const selectedTrongLuong = ref('')
const selectedMonTheThao = ref('')
const selectedLoaiMua = ref('')
const selectedDoBen = ref('')
const selectedChongNuoc = ref('')
const statusFilter = ref('')
const showAddModal = ref(false)
const showEditModal = ref(false)
const showImageSelector = ref(false)
const currentPage = ref(1)
const itemsPerPage = ref(10)
const selectedImages = ref([])
const selectedImageIds = ref([])
const availableImages = ref([])
// ... existing code ...
const newChiTietSanPham = ref({
  id: '',
  idSanPham: '',
  idMauSac: '',
  idKichThuoc: '',
  idChatLieu: '',
  idDeGiay: '',
  idDemGiay: '',
  idTrongLuong: '',
  idMonTheThao: '',
  idLoaiMua: '',
  idDoBen: '',
  idChongNuoc: '',
  soLuong: 0,
  giaBan: 0,
  trangThai: true,
  ghiChu: ''
})
// ... existing code ...
// Data for dropdowns
const sanPhams = ref([])
const anhSanPhams = ref([])
const chiTietSanPhams = ref([])
const chiTietSanPhamAnhs = ref([])
const mauSacs = ref([])
const kichThuocs = ref([])
const chatLieus = ref([])
const deGiays = ref([])
const demGiays = ref([])
const trongLuongs = ref([])
const monTheThaos = ref([])
const loaiMuas = ref([])
const doBens = ref([])
const chongNuocs = ref([])
// API call
// Fetch all
const fetchChiTietSanPham = async () => {
  try {
    const response = await fetchAllChiTietSanPham();
    chiTietSanPhams.value = response.data
  } catch (error) {
    console.error('Error fetching product details:', error)
  }
}

const fetchAllThuocTinh = async () => {
  try {
    let res = await fetchAllAnhSanPham();
    anhSanPhams.value = res.data
    let res2 = await fetchAllMauSac();
    mauSacs.value = res2.data
    let res3 = await fetchAllKichThuoc();
    kichThuocs.value = res3.data
    let res4 = await fetchAllDeGiay();
    deGiays.value = res4.data
    let res5 = await fetchAllChatLieu();
    chatLieus.value = res5.data
    let res6 = await fetchAllDemGiay();
    demGiays.value = res6.data
    let res7 = await fetchAllTrongLuong();
    trongLuongs.value = res7.data
    let res8 = await fetchAllMonTheThao();
    monTheThaos.value = res8.data
    let res9 = await fetchAllLoaiMua();
    loaiMuas.value = res9.data
    let res10 = await fetchAllDoBen();
    doBens.value = res10.data
    let res11 = await fetchAllChongNuoc();
    chongNuocs.value = res11.data
    let res12 = await fetchAllSanPham();
    sanPhams.value = res12.data
    let res13 = await fetchAllChiTietSanPhamAnh();
    chiTietSanPhamAnhs.value = res13.data
    
    // Cập nhật imageDataKey để đảm bảo table re-render
    imageDataKey.value = {
      chiTietSanPhamAnhsLength: chiTietSanPhamAnhs.value?.length || 0,
      anhSanPhamsLength: anhSanPhams.value?.length || 0,
      timestamp: Date.now()
    }
  } catch (error) {
    console.error('Error fetching product details:', error)
  }
}

// Computed
const filteredDetails = computed(() => {
  return chiTietSanPhams.value.filter(detail => {
    const matchesSearch = !searchQuery.value ||
      (detail.tenSanPham || detail.sanPham?.tenSanPham || '')?.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      (detail.tenMauSac || detail.mauSac?.tenMauSac || '')?.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      (detail.tenKichThuoc || detail.kichThuoc?.tenKichThuoc || '')?.toLowerCase().includes(searchQuery.value.toLowerCase())

    // Sử dụng logic tìm kiếm dựa trên tên thay vì ID để nhất quán với hiển thị
    const matchesProduct = !selectedSanPham.value ||
      (detail.tenSanPham === sanPhams.value.find(p => p.id === selectedSanPham.value)?.tenSanPham) ||
      (detail.sanPham?.tenSanPham === sanPhams.value.find(p => p.id === selectedSanPham.value)?.tenSanPham)

    const matchesColor = !selectedMauSac.value ||
      (detail.tenMauSac === mauSacs.value.find(m => m.id === selectedMauSac.value)?.tenMauSac) ||
      (detail.mauSac?.tenMauSac === mauSacs.value.find(m => m.id === selectedMauSac.value)?.tenMauSac)

    const matchesSize = !selectedKichThuoc.value ||
      (detail.tenKichThuoc === kichThuocs.value.find(k => k.id === selectedKichThuoc.value)?.tenKichThuoc) ||
      (detail.kichThuoc?.tenKichThuoc === kichThuocs.value.find(k => k.id === selectedKichThuoc.value)?.tenKichThuoc)

    const matchesChatLieu = !selectedChatLieu.value ||
      (detail.tenChatLieu === chatLieus.value.find(c => c.id === selectedChatLieu.value)?.tenChatLieu) ||
      (detail.chatLieu?.tenChatLieu === chatLieus.value.find(c => c.id === selectedChatLieu.value)?.tenChatLieu)

    const matchesDeGiay = !selectedDeGiay.value ||
      (detail.tenDeGiay === deGiays.value.find(d => d.id === selectedDeGiay.value)?.tenDeGiay) ||
      (detail.deGiay?.tenDeGiay === deGiays.value.find(d => d.id === selectedDeGiay.value)?.tenDeGiay)

    const matchesDemGiay = !selectedDemGiay.value ||
      (detail.tenDemGiay === demGiays.value.find(d => d.id === selectedDemGiay.value)?.tenDemGiay) ||
      (detail.demGiay?.tenDemGiay === demGiays.value.find(d => d.id === selectedDemGiay.value)?.tenDemGiay)

    const matchesTrongLuong = !selectedTrongLuong.value ||
      (detail.tenTrongLuong === trongLuongs.value.find(t => t.id === selectedTrongLuong.value)?.tenTrongLuong) ||
      (detail.trongLuong?.tenTrongLuong === trongLuongs.value.find(t => t.id === selectedTrongLuong.value)?.tenTrongLuong)

    const matchesMonTheThao = !selectedMonTheThao.value ||
      (detail.tenMonTheThao === monTheThaos.value.find(m => m.id === selectedMonTheThao.value)?.tenMonTheThao) ||
      (detail.monTheThao?.tenMonTheThao === monTheThaos.value.find(m => m.id === selectedMonTheThao.value)?.tenMonTheThao)

    const matchesLoaiMua = !selectedLoaiMua.value ||
      (detail.tenLoaiMua === loaiMuas.value.find(l => l.id === selectedLoaiMua.value)?.tenLoaiMua) ||
      (detail.loaiMua?.tenLoaiMua === loaiMuas.value.find(l => l.id === selectedLoaiMua.value)?.tenLoaiMua)

    const matchesDoBen = !selectedDoBen.value ||
      (detail.tenDoBen === doBens.value.find(d => d.id === selectedDoBen.value)?.tenDoBen) ||
      (detail.doBen?.tenDoBen === doBens.value.find(d => d.id === selectedDoBen.value)?.tenDoBen)

    const matchesChongNuoc = !selectedChongNuoc.value ||
      (detail.tenChongNuoc === chongNuocs.value.find(c => c.id === selectedChongNuoc.value)?.tenChongNuoc) ||
      (detail.chongNuoc?.tenChongNuoc === chongNuocs.value.find(c => c.id === selectedChongNuoc.value)?.tenChongNuoc)

    const matchesStatus = statusFilter.value === '' || detail.trangThai == statusFilter.value

    return matchesSearch && matchesProduct && matchesColor && matchesSize && matchesChatLieu &&
      matchesDeGiay && matchesDemGiay && matchesTrongLuong && matchesMonTheThao &&
      matchesLoaiMua && matchesDoBen && matchesChongNuoc && matchesStatus
  })
})

const totalPages = computed(() =>
  Math.ceil(filteredDetails.value.length / itemsPerPage.value)
);
const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);
const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, filteredDetails.value.length)
);

const paginatedDetails = computed(() => {
  return filteredDetails.value.slice(startIndex.value, startIndex.value + itemsPerPage.value)
})

// Computed để theo dõi thay đổi trạng thái
const statusCounts = computed(() => {
  const active = chiTietSanPhams.value.filter(detail => detail.trangThai === 1).length
  const inactive = chiTietSanPhams.value.filter(detail => detail.trangThai === 0).length
  return { active, inactive, total: chiTietSanPhams.value.length }
})

// Ref để theo dõi thay đổi dữ liệu ảnh và đảm bảo table re-render
const imageDataKey = ref({
  chiTietSanPhamAnhsLength: 0,
  anhSanPhamsLength: 0,
  timestamp: Date.now()
})

// Methods
const formatCurrency = (amount) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    minimumFractionDigits: 0,
  })
    .format(amount)
    .replace("₫", " VND");
};

const getColorCode = (colorName) => {
  if (!colorName) return '#E5E7EB'

  const colorMap = {
    'Đen': '#000000',
    'Trắng': '#FFFFFF',
    'Đỏ': '#FF0000',
    'Xanh': '#0000FF',
    'Vàng': '#FFFF00',
    'Xám': '#808080',
    'Nâu': '#8B4513',
    'Hồng': '#FFC0CB',
    'Xanh dương': '#0066CC',
    'Xanh lá': '#00CC00',
    'Cam': '#FF8C00',
    'Tím': '#800080',
    'Xanh ngọc': '#00CED1',
    'Xanh navy': '#000080',
    'Xanh lá cây': '#228B22'
  }

  // Exact match first
  if (colorMap[colorName]) {
    return colorMap[colorName]
  }

  // Fuzzy matching for similar names
  const normalizedColorName = colorName.toLowerCase()
  for (const [key, value] of Object.entries(colorMap)) {
    if (normalizedColorName.includes(key.toLowerCase()) ||
      key.toLowerCase().includes(normalizedColorName)) {
      return value
    }
  }

  return '#E5E7EB' // Default color
}

const editDetail = async (data) => {
  try {
    // Đảm bảo dữ liệu dropdown đã được load
    if (sanPhams.value.length === 0 || mauSacs.value.length === 0 || kichThuocs.value.length === 0 || chatLieus.value.length === 0) {
      await fetchAllThuocTinh()
    }

    // Tìm ID tương ứng từ các mảng dropdown
    const productId = sanPhams.value.find(p => p.tenSanPham === data.tenSanPham || p.tenSanPham === data.sanPham?.tenSanPham)?.id || ''
    const mauSacId = mauSacs.value.find(m => m.tenMauSac === data.tenMauSac || m.tenMauSac === data.mauSac?.tenMauSac)?.id || ''
    const kichThuocId = kichThuocs.value.find(k => k.tenKichThuoc === data.tenKichThuoc || k.tenKichThuoc === data.kichThuoc?.tenKichThuoc)?.id || ''
    const chatLieuId = chatLieus.value.find(c => c.tenChatLieu === data.tenChatLieu || c.tenChatLieu === data.chatLieu?.tenChatLieu)?.id || ''
    const deGiayId = deGiays.value.find(d => d.tenDeGiay === data.tenDeGiay || d.tenDeGiay === data.deGiay?.tenDeGiay)?.id || ''
    const demGiayId = demGiays.value.find(d => d.tenDemGiay === data.tenDemGiay || d.tenDemGiay === data.demGiay?.tenDemGiay)?.id || ''
    const trongLuongId = trongLuongs.value.find(t => t.tenTrongLuong === data.tenTrongLuong || t.tenTrongLuong === data.trongLuong?.tenTrongLuong)?.id || ''
    const monTheThaoId = monTheThaos.value.find(m => m.tenMonTheThao === data.tenMonTheThao || m.tenMonTheThao === data.monTheThao?.tenMonTheThao)?.id || ''
    const loaiMuaId = loaiMuas.value.find(l => l.tenLoaiMua === data.tenLoaiMua || l.tenLoaiMua === data.loaiMua?.tenLoaiMua)?.id || ''
    const doBenId = doBens.value.find(d => d.tenDoBen === data.tenDoBen || d.tenDoBen === data.doBen?.tenDoBen)?.id || ''
    const chongNuocId = chongNuocs.value.find(c => c.tenChongNuoc === data.tenChongNuoc || c.tenChongNuoc === data.chongNuoc?.tenChongNuoc)?.id || ''

    newChiTietSanPham.value = {
      id: data.id,
      idSanPham: productId,
      idMauSac: mauSacId,
      idKichThuoc: kichThuocId,
      idChatLieu: chatLieuId,
      idDeGiay: deGiayId,
      idDemGiay: demGiayId,
      idTrongLuong: trongLuongId,
      idMonTheThao: monTheThaoId,
      idLoaiMua: loaiMuaId,
      idDoBen: doBenId,
      idChongNuoc: chongNuocId,
      soLuong: data.soLuong || 0,
      giaBan: data.giaBan || 0,
      trangThai: data.trangThai === 1 || data.trangThai === true,
      ghiChu: data.ghiChu || ''
    }
                  // Load ảnh sản phẩm từ AnhSanPham
     const images = getImagesForChiTietSanPham(data.id)
     if (images.length > 0) {
       selectedImages.value = images.map(img => img.duongDanAnh)
       selectedImageIds.value = images.map(img => img.id)
     } else {
       selectedImages.value = []
       selectedImageIds.value = []
     }
     
     showEditModal.value = true
   } catch (error) {
     console.error('Error in editDetail:', error)
     alert('Có lỗi xảy ra khi mở form chỉnh sửa!')
   }
 }

const deleteDetail = async (id) => {
  if (confirm('Bạn có chắc chắn muốn xóa chi tiết sản phẩm này?')) {
    try {
      await fetchUpdateStatusChiTietSanPham(id)

      // Cập nhật trạng thái ngay lập tức trong giao diện
      const detailIndex = chiTietSanPhams.value.findIndex(detail => detail.id === id)
      if (detailIndex !== -1) {
        chiTietSanPhams.value[detailIndex].trangThai = 0
      }

      // Refresh lại dữ liệu từ server để đảm bảo đồng bộ
      await fetchChiTietSanPham()
      alert('Xóa chi tiết sản phẩm thành công!')
    } catch (error) {
      console.error('Error deleting product details:', error)
      alert('Có lỗi xảy ra khi xóa!')
    }
  }
};

const saveDetail = async () => {
  try {
    // Validate required fields
    if (!newChiTietSanPham.value.idSanPham ||
      !newChiTietSanPham.value.idMauSac ||
      !newChiTietSanPham.value.idKichThuoc ||
      !newChiTietSanPham.value.idChatLieu ||
      !newChiTietSanPham.value.giaBan ||
      !newChiTietSanPham.value.soLuong) {
      alert('Vui lòng nhập đầy đủ thông tin bắt buộc')
      return
    }

    // Chuẩn bị dữ liệu để gửi, đảm bảo trạng thái đúng định dạng
    const dataToSend = {
      ...newChiTietSanPham.value,
      trangThai: newChiTietSanPham.value.trangThai ? 1 : 0
    }

    let chiTietSanPhamId = null

    if (showAddModal.value) {
      // Create new
      const response = await fetchCreateChiTietSanPham(dataToSend)
      alert('Thêm chi tiết sản phẩm thành công!')
      // Lấy ID của chi tiết sản phẩm vừa tạo từ response.data
      chiTietSanPhamId = response?.data
      console.log('Created chiTietSanPham with ID:', chiTietSanPhamId)
    } else if (showEditModal.value) {
      // Update existing
      await fetchUpdateChiTietSanPham(dataToSend.id, dataToSend)
      alert('Cập nhật chi tiết sản phẩm thành công!')
      chiTietSanPhamId = dataToSend.id
      console.log('Updated chiTietSanPham with ID:', chiTietSanPhamId)
    }

    // Xử lý ảnh sản phẩm
    if (chiTietSanPhamId && selectedImageIds.value.length > 0) {
      try {
        // Xóa tất cả liên kết ảnh cũ
        const existingImages = chiTietSanPhamAnhs.value.filter(item => 
          item.idChiTietSanPham === chiTietSanPhamId
        )
        
        console.log('Existing images to delete:', existingImages)
        
        for (const existingImage of existingImages) {
          if (existingImage.id) {
            console.log('Deleting image link with ID:', existingImage.id)
            await fetchDeleteChiTietSanPhamAnh(existingImage.id)
          } else {
            console.warn('Skipping image with undefined ID:', existingImage)
          }
        }

        // Tạo liên kết ảnh mới - sử dụng method tạo nhiều ảnh cùng lúc
        console.log('Creating multiple image links for image IDs:', selectedImageIds.value)
        await fetchCreateMultipleChiTietSanPhamAnh({
          idChiTietSanPham: chiTietSanPhamId,
          idAnhSanPhamList: selectedImageIds.value,
          deleted: false
        })
      } catch (imageError) {
        console.error('Error handling images:', imageError)
        alert('Lưu thông tin thành công nhưng có lỗi khi xử lý ảnh!')
      }
    }

    // Refresh data từ server để đảm bảo đồng bộ
    console.log('Bắt đầu refresh dữ liệu...')
    
    // Đảm bảo thứ tự refresh để dữ liệu ảnh được cập nhật đúng
    await fetchChiTietSanPham()
    console.log('Đã refresh chiTietSanPham, số lượng:', chiTietSanPhams.value.length)
    
    // Force refresh dữ liệu ảnh để đảm bảo table được cập nhật
    await forceRefreshImageData()
    
    console.log('Hoàn thành refresh dữ liệu')
    closeModals()
  } catch (error) {
    console.error('Error saving product details:', error)
    alert('Có lỗi xảy ra khi lưu dữ liệu!')
  }
}

const closeModals = () => {
  showAddModal.value = false
  showEditModal.value = false
  // Reset form về trạng thái ban đầu
  newChiTietSanPham.value = {
    id: '',
    idSanPham: '',
    idMauSac: '',
    idKichThuoc: '',
    idChatLieu: '',
    idDeGiay: '',
    idDemGiay: '',
    idTrongLuong: '',
    idMonTheThao: '',
    idLoaiMua: '',
    idDoBen: '',
    idChongNuoc: '',
    soLuong: 0,
    giaBan: 0,
    trangThai: true,
    ghiChu: ''
  }
  selectedImages.value = []
  selectedImageIds.value = []
  console.log('Đã đóng modal và reset form')
}

const clearFilters = () => {
  searchQuery.value = ''
  selectedSanPham.value = ''
  selectedMauSac.value = ''
  selectedKichThuoc.value = ''
  selectedChatLieu.value = ''
  selectedDeGiay.value = ''
  selectedDemGiay.value = ''
  selectedTrongLuong.value = ''
  selectedMonTheThao.value = ''
  selectedLoaiMua.value = ''
  selectedDoBen.value = ''
  selectedChongNuoc.value = ''
  statusFilter.value = ''
  currentPage.value = 1
}

const applyFilters = () => {
  currentPage.value = 1;
};

const previousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
}

const exportData = () => {
  alert('Xuất báo cáo thành công! (Chức năng đang được phát triển)')
}

const exportDetailsToExcel = () => {
  alert('Xuất Excel thành công! (Chức năng đang được phát triển)')
}

const refreshData = async () => {
  try {
    // Reset về trang đầu tiên
    currentPage.value = 1

    // Load lại dữ liệu
    await Promise.all([
      fetchChiTietSanPham(),
      fetchAllThuocTinh()
    ])

    // Clear các filter
    clearFilters()

    alert('Làm mới dữ liệu thành công!')
    console.log('Đã refresh dữ liệu thành công')
  } catch (error) {
    console.error('Error refreshing data:', error)
    alert('Có lỗi xảy ra khi làm mới dữ liệu!')
  }
}

const openAddModal = () => {
  // Reset form về trạng thái ban đầu
  newChiTietSanPham.value = {
    id: '',
    idSanPham: '',
    idMauSac: '',
    idKichThuoc: '',
    idChatLieu: '',
    idDeGiay: '',
    idDemGiay: '',
    idTrongLuong: '',
    idMonTheThao: '',
    idLoaiMua: '',
    idDoBen: '',
    idChongNuoc: '',
    soLuong: 0,
    giaBan: 0,
    trangThai: true,
    ghiChu: ''
  }
  selectedImages.value = []
  selectedImageIds.value = []
  showAddModal.value = true
  console.log('Đã mở modal thêm mới')
}

const openImageSelector = () => {
  try {
    // Chỉ hiển thị những ảnh chưa bị xóa
    availableImages.value = anhSanPhams.value.filter(img => !img.deleted)
    showImageSelector.value = true
    console.log('Đã mở image selector với', availableImages.value.length, 'ảnh')
  } catch (error) {
    console.error('Error opening image selector:', error)
    alert('Có lỗi khi mở image selector!')
  }
}

const closeImageSelector = () => {
  showImageSelector.value = false
}

const toggleImageSelection = (imageId) => {
  const index = selectedImageIds.value.indexOf(imageId)
  if (index > -1) {
    // Bỏ chọn ảnh
    selectedImageIds.value.splice(index, 1)
    selectedImages.value.splice(index, 1)
  } else {
    // Chọn ảnh mới
    if (selectedImageIds.value.length < 5) {
      const image = anhSanPhams.value.find(img => img.id === imageId)
      if (image) {
        selectedImageIds.value.push(imageId)
        selectedImages.value.push(image.duongDanAnh)
      }
    } else {
      alert('Chỉ được chọn tối đa 5 ảnh!')
    }
  }
}

const removeImage = (index) => {
  selectedImages.value.splice(index, 1)
  selectedImageIds.value.splice(index, 1)
  console.log('Đã xóa ảnh tại vị trí:', index)
}

const confirmImageSelection = () => {
  showImageSelector.value = false
}

// Method để lấy ảnh cho một chi tiết sản phẩm
const getImagesForChiTietSanPham = (chiTietSanPhamId) => {
  try {
    // Đảm bảo dữ liệu đã được load
    if (!chiTietSanPhamAnhs.value || !anhSanPhams.value) {
      console.log('Dữ liệu chưa sẵn sàng, đang chờ...')
      return []
    }
    
    // Lọc các liên kết ảnh cho chi tiết sản phẩm này
    const imageLinks = chiTietSanPhamAnhs.value.filter(item => 
      item.idChiTietSanPham === chiTietSanPhamId && !item.deleted
    );
    
 
    
    // Map để lấy thông tin ảnh đầy đủ
    const images = imageLinks.map(item => {
      const anhSanPham = anhSanPhams.value.find(anh => anh.id === item.idAnhSanPham);
      if (anhSanPham) {
        return {
          id: anhSanPham.id,
          duongDanAnh: anhSanPham.duongDanAnh,
          loaiAnh: anhSanPham.loaiAnh,
          moTa: anhSanPham.moTa
        }
      } else {
        console.log(`Không tìm thấy ảnh với ID: ${item.idAnhSanPham}`)
        return null
      }
    }).filter(img => img !== null);
    

    return images
  } catch (error) {
    console.error('Error getting images for chi tiet san pham:', error)
    return []
  }
};

// Method để tạo URL đầy đủ cho ảnh
const getImageUrl = (imagePath) => {
  try {
    if (!imagePath) return '';
    
    // Nếu đã là URL đầy đủ thì trả về nguyên
    if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
      return imagePath;
    }
    
    // Nếu là đường dẫn tương đối, thêm base URL của backend
    if (imagePath.startsWith('uploads/')) {
      return `http://localhost:8080/${imagePath}`;
    }
    
    // Nếu bắt đầu bằng / thì thêm base URL
    if (imagePath.startsWith('/')) {
      return `http://localhost:8080${imagePath}`;
    }
    
    return imagePath;
  } catch (error) {
    console.error('Error getting image URL:', error)
    return ''
  }
};

// Method để force refresh dữ liệu ảnh
const forceRefreshImageData = async () => {
  try {
    console.log('Force refresh dữ liệu ảnh...')
    
    // Refresh dữ liệu ảnh
    await fetchAllThuocTinh()
    
    // Force Vue re-render bằng cách thay đổi timestamp
    imageDataKey.value = {
      chiTietSanPhamAnhsLength: chiTietSanPhamAnhs.value?.length || 0,
      anhSanPhamsLength: anhSanPhams.value?.length || 0,
      timestamp: Date.now()
    }
    
    console.log('Đã force refresh dữ liệu ảnh thành công')
  } catch (error) {
    console.error('Error force refreshing image data:', error)
  }
}

onMounted(async () => {
  try {
    // Load dữ liệu song song để tăng tốc độ
    await Promise.all([
      fetchChiTietSanPham(),
      fetchAllThuocTinh()
    ])
  } catch (error) {
    console.error('Error loading initial data:', error)
    alert('Có lỗi xảy ra khi tải dữ liệu ban đầu!')
  }
})


</script>

<style scoped>
/* Import Google Fonts */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Poppins:wght@300;400;500;600;700&display=swap');

/* Global font settings */
* {
  font-family: 'Inter', 'Poppins', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* CSS Custom Properties */
:root {
  --border-color: #e5e7eb;
  --secondary-color: #1e293b;
  --light-gray: #f3f4f6;
  --medium-gray: #6b7280;
}

.product-details {
  max-width: 100%;
  margin: 0 auto;
  padding: 0 1rem;
}

.page-header {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  border-radius: 12px;
  padding: 2rem;
  margin-bottom: 2rem;
  color: white;
  box-shadow: 0 10px 25px -5px rgba(34, 197, 94, 0.3);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

.header-text {
  flex: 1;
}

.page-title {
  margin: 0 0 0.5rem 0;
  font-size: 2rem;
  font-weight: 700;
}

.page-subtitle {
  margin: 0;
  opacity: 0.9;
  font-size: 1rem;
}

.header-actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.btn-refresh,
.btn-export {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.75rem 1rem;
  border-radius: 8px;
  font-weight: 600;
  font-family: 'Inter', sans-serif;
  letter-spacing: 0.2px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  backdrop-filter: blur(10px);
}

.btn-add {
  background: rgba(255, 255, 255, 0.3);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.5);
  padding: 0.75rem 1.25rem;
  border-radius: 8px;
  font-weight: 700;
  font-family: 'Inter', sans-serif;
  letter-spacing: 0.3px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  font-size: 0.9rem;
}

.btn-refresh:hover,
.btn-export:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.btn-add:hover {
  background: rgba(255, 255, 255, 0.4);
  border-color: rgba(255, 255, 255, 0.7);
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.btn-icon {
  font-size: 1rem;
}

/* Filter Section - Enhanced */
.filter-section {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 4px 6px -1px rgba(0, 0, 0, 0.06);
  border: 1px solid #e5e7eb;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1.5rem;
  border-bottom: 2px solid #f3f4f6;
}

.filter-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.filter-title h3 {
  margin: 0;
  color: #1e293b;
  font-size: 1.5rem;
  font-weight: 700;
}

.filter-icon {
  font-size: 2rem;
  opacity: 0.9;
}

.filter-stats {
  color: #6b7280;
  font-size: 0.875rem;
  font-weight: 500;
}

.filter-content {
  padding-top: 1rem;
}

.search-section {
  margin-bottom: 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
}

.input-group {
  position: relative;
  max-width: 100%;
  width: 100%;
}

.search-input {
  width: 100%;
  padding: 1.25rem 1rem 1.25rem 4rem;
  border: 2px solid #e5e7eb;
  border-radius: 16px;
  font-size: 1.1rem;
  background: white;
  transition: all 0.3s ease;
  font-weight: 500;
  color: #374151;
  min-height: 64px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.search-input:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 4px rgba(74, 222, 128, 0.1), 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.search-input:hover {
  border-color: #d1d5db;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.input-icon {
  position: absolute;
  left: 1.5rem;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
  font-size: 1.5rem;
  pointer-events: none;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
}

.clear-btn {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: #f3f4f6;
  border: none;
  color: #6b7280;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 8px;
  font-size: 1.2rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
}

.clear-btn:hover {
  background: #e5e7eb;
  color: #374151;
  transform: scale(1.1);
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-top: 1.5rem;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.filter-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 600;
  color: #374151;
  font-size: 0.875rem;
  margin-bottom: 0.25rem;
}

.label-icon {
  font-size: 1.1rem;
  opacity: 0.8;
}

.form-select {
  padding: 0.875rem;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 0.875rem;
  background: white;
  transition: all 0.2s ease;
  font-weight: 500;
  color: #374151;
  cursor: pointer;
}

.form-select:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
  transform: translateY(-1px);
}

.form-select:hover {
  border-color: #d1d5db;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.filter-actions {
  display: flex;
  gap: 1rem;
  align-items: end;
  grid-column: span 4;
  justify-content: flex-end;
  margin-top: 1rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 10px;
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 120px;
  justify-content: center;
}

.btn-primary {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(34, 197, 94, 0.3);
}

.btn-primary:hover {
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(34, 197, 94, 0.4);
}

.btn-outline {
  background: white;
  color: #475569;
  border: 2px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.btn-outline:hover {
  background: #f8fafc;
  border-color: #4ade80;
  color: #4ade80;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* Table Container - Optimized for full width with horizontal scrollbar */
.table-container {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: white;
  overflow-x: auto;
  overflow-y: hidden;
  position: relative;
}

/* Ensure horizontal scrollbar is always visible */
.table-container::-webkit-scrollbar {
  height: 12px;
  background-color: #f1f5f9;
}

.table-container::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 6px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 6px;
  border: 2px solid #f1f5f9;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Product Table - Optimized for full width with horizontal scrollbar */
.product-table {
  width: 100%;
  min-width: 1400px;
  /* Ensure minimum width to show all columns */
  border-collapse: collapse;
  background: white;
  font-size: 0.75rem;
  /* Slightly smaller font for better fit */
  table-layout: fixed;
  /* Fixed table layout for consistent column widths */
}

.product-table th {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  font-weight: 600;
  padding: 0.6rem 0.4rem;
  /* Reduced padding for better fit */
  text-align: center;
  white-space: nowrap;
  border-right: 1px solid rgba(255, 255, 255, 0.2);
  position: sticky;
  top: 0;
  z-index: 10;
}

.product-table th:last-child {
  border-right: none;
}

.product-table td {
  padding: 0.6rem 0.4rem;
  /* Reduced padding for better fit */
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #f1f5f9;
  border-right: 1px solid #f1f5f9;
  background: white;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-table td:last-child {
  border-right: none;
}

.product-table tbody tr:hover {
  background-color: #f8fafc;
}

.no-data {
  font-size: 0.875rem;
  color: #94a3b8;
  font-style: italic;
  text-align: center;
  padding: 2rem;
}

/* Column Widths - Optimized for 100% screen */
.stt-col {
  width: 50px;
  min-width: 50px;
  max-width: 50px;
}

.product-col {
  width: 140px;
  min-width: 140px;
  max-width: 160px;
}

.color-col {
  width: 80px;
  min-width: 80px;
  max-width: 90px;
}

.size-col {
  width: 60px;
  min-width: 60px;
  max-width: 70px;
}

.sole-col {
  width: 85px;
  min-width: 85px;
  max-width: 95px;
}

.material-col {
  width: 85px;
  min-width: 85px;
  max-width: 95px;
}

.insole-col {
  width: 85px;
  min-width: 85px;
  max-width: 95px;
}

.weight-col {
  width: 75px;
  min-width: 75px;
  max-width: 85px;
}

.sport-col {
  width: 85px;
  min-width: 85px;
  max-width: 95px;
}

.season-col {
  width: 75px;
  min-width: 75px;
  max-width: 85px;
}

.durability-col {
  width: 75px;
  min-width: 75px;
  max-width: 85px;
}

.waterproof-col {
  width: 85px;
  min-width: 85px;
  max-width: 95px;
}

.image-col {
  width: 70px;
  min-width: 70px;
  max-width: 80px;
}

.quantity-col {
  width: 60px;
  min-width: 60px;
  max-width: 70px;
}

.price-col {
  width: 90px;
  min-width: 90px;
  max-width: 100px;
}

.status-col {
  width: 90px;
  min-width: 90px;
  max-width: 100px;
}

.action-col {
  width: 80px;
  min-width: 80px;
  max-width: 90px;
}

/* Product Info - Optimized text size */
.product-col {
  text-align: left !important;
}

.product-info strong {
  display: block;
  margin-bottom: 0.25rem;
  font-size: 0.8rem;
  /* Slightly smaller for better fit */
  color: #1e293b;
  line-height: 1.2;
}

/* Color Badge - Enhanced */
.color-badge {
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  color: white;
  font-weight: 500;
  font-size: 0.65rem;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(0, 0, 0, 0.1);
  display: inline-block;
  min-width: 50px;
  max-width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Special color adjustments for light backgrounds */
.color-badge[style*="background-color: #FFFFFF"],
.color-badge[style*="background-color: #FFFF00"],
.color-badge[style*="background-color: #FFC0CB"] {
  color: #000000;
  text-shadow: none;
  border: 1px solid rgba(0, 0, 0, 0.2);
}

/* Size Badge - Optimized size */
.size-badge {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  font-weight: 600;
  font-size: 0.7rem;
  /* Smaller font */
  display: inline-block;
  min-width: 35px;
  max-width: 100%;
}

/* Attribute Text - Optimized for compact display */
.attribute-text {
  font-size: 0.7rem;
  /* Smaller font for better fit */
  color: #475569;
  font-weight: 500;
  display: block;
  line-height: 1.2;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Stock Badge - Optimized size */
.stock-badge {
  background: #10b981;
  color: white;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  font-weight: 600;
  font-size: 0.7rem;
  /* Smaller font */
  display: inline-block;
  min-width: 35px;
  max-width: 100%;
}

.stock-badge.low-stock {
  background: #ef4444;
}

/* Price Text - Optimized size */
.price-text {
  font-weight: 600;
  color: #059669;
  font-size: 0.75rem;
  /* Smaller font */
  white-space: nowrap;
}

/* Status Badge - Optimized size */
.status-badge {
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  font-weight: 500;
  font-size: 0.65rem;
  /* Smaller font */
  display: inline-block;
  min-width: 70px;
  max-width: 100%;
}

.status-active {
  background: #10b981;
  color: white;
}

.status-inactive {
  background: #ef4444;
  color: white;
}

/* Product Images - Optimized size */
.image-col {
  text-align: center;
}

.image-preview {
  position: relative;
  display: inline-block;
}

.product-image {
  width: 30px;
  /* Smaller image */
  height: 30px;
  object-fit: cover;
  border-radius: 4px;
  border: 2px solid #e2e8f0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.image-count {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.6rem;
  font-weight: 600;
  border: 2px solid white;
}

.no-image {
  color: #94a3b8;
  font-size: 0.7rem;
  font-style: italic;
}

/* Action Buttons - Optimized size */
.action-buttons {
  display: flex;
  gap: 0.5rem;
  justify-content: center;
}

.btn-edit,
.btn-delete {
  border: none;
  border-radius: 4px;
  padding: 0.4rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 28px;
  height: 28px;
}

.btn-edit {
  background: #3b82f6;
  color: white;
}

.btn-edit:hover {
  background: #2563eb;
  transform: translateY(-1px);
}

.btn-delete {
  background: #ef4444;
  color: white;
}

.btn-delete:hover {
  background: #dc2626;
  transform: translateY(-1px);
}

.btn-icon {
  font-size: 0.8rem;
}

/* Pagination */
.pagination-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--border-color);
}

.pagination {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.page-info {
  font-weight: 600;
  color: var(--secondary-color);
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: white;
  border-radius: 16px;
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.1);
  animation: modalSlideIn 0.3s ease-out;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-content.large {
  max-width: 1000px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2rem 2rem 1.5rem 2rem;
  border-bottom: 2px solid #f3f4f6;
  background: #22c55e;
  border-radius: 16px 16px 0 0;
}

.modal-header h3 {
  margin: 0;
  color: white;
  font-size: 1.5rem;
  font-weight: 700;
  font-family: 'Inter', sans-serif;
}

.modal-close {
  background: rgba(239, 68, 68, 0.1);
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s ease;
  color: #ef4444;
}

.modal-close:hover {
  background-color: #ef4444;
  color: white;
  transform: scale(1.1);
}

.modal-body {
  padding: 2rem;
  background: white;
}

.modal-footer {
  display: flex;
  justify-content: center;
  gap: 1rem;
  padding: 1.5rem 2rem 2rem 2rem;
  border-top: 2px solid #f3f4f6;
  background: #f8fafc;
  border-radius: 0 0 16px 16px;
}

.btn-save {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  border: none;
  padding: 0.875rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  min-width: 140px;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(34, 197, 94, 0.3);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.btn-save:hover {
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(34, 197, 94, 0.4);
}

.btn-save:active {
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(34, 197, 94, 0.3);
}

.btn-save .btn-icon {
  font-size: 1.1rem;
  transition: transform 0.3s ease;
}

.btn-save:hover .btn-icon {
  transform: scale(1.1);
}

/* Form styles */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  padding: 1rem 0;
}

.form-grid .form-group:nth-child(13),
.form-grid .form-group:nth-child(14) {
  grid-column: span 2;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group.span-2 {
  grid-column: span 2;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 600;
  color: #374151;
  font-size: 0.875rem;
  margin-bottom: 0.25rem;
}

.label-icon {
  font-size: 1.1rem;
  opacity: 0.8;
}

.form-control {
  padding: 0.875rem;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 0.875rem;
  background: white;
  transition: all 0.2s ease;
  font-weight: 500;
  color: #374151;
  min-height: 48px;
}

.form-control:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
  transform: translateY(-1px);
}

.form-control:hover {
  border-color: #d1d5db;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.form-control::placeholder {
  color: #9ca3af;
  font-weight: 400;
}

/* Enhanced form controls */
.form-control select {
  cursor: pointer;
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='m6 8 4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 0.75rem center;
  background-repeat: no-repeat;
  background-size: 1.5em 1.5em;
  padding-right: 2.5rem;
}

.form-control textarea {
  resize: vertical;
  min-height: 80px;
  font-family: inherit;
}

/* Responsive Design - Optimized for 100% screen */
@media (min-width: 1200px) and (max-width: 1600px) {
  .product-table {
    font-size: 0.7rem;
  }

  .product-table th,
  .product-table td {
    padding: 0.5rem 0.3rem;
  }

  /* Adjust column widths for medium screens */
  .stt-col {
    width: 45px;
    min-width: 45px;
  }

  .product-col {
    width: 130px;
    min-width: 130px;
  }

  .color-col {
    width: 75px;
    min-width: 75px;
  }

  .size-col {
    width: 55px;
    min-width: 55px;
  }

  .sole-col {
    width: 80px;
    min-width: 80px;
  }

  .material-col {
    width: 80px;
    min-width: 80px;
  }

  .insole-col {
    width: 80px;
    min-width: 80px;
  }

  .weight-col {
    width: 70px;
    min-width: 70px;
  }

  .sport-col {
    width: 80px;
    min-width: 80px;
  }

  .season-col {
    width: 70px;
    min-width: 70px;
  }

  .durability-col {
    width: 70px;
    min-width: 70px;
  }

  .waterproof-col {
    width: 80px;
    min-width: 80px;
  }

  .image-col {
    width: 65px;
    min-width: 65px;
  }

  .quantity-col {
    width: 55px;
    min-width: 55px;
  }

  .price-col {
    width: 85px;
    min-width: 85px;
  }

  .status-col {
    width: 85px;
    min-width: 85px;
  }

  .action-col {
    width: 75px;
    min-width: 75px;
  }
}

@media (min-width: 1601px) {

  /* For very large screens, increase font size slightly */
  .product-table {
    font-size: 0.8rem;
  }

  .product-table th,
  .product-table td {
    padding: 0.7rem 0.5rem;
  }

  /* Slightly larger column widths for better readability */
  .stt-col {
    width: 55px;
    min-width: 55px;
  }

  .product-col {
    width: 150px;
    min-width: 150px;
  }

  .color-col {
    width: 85px;
    min-width: 85px;
  }

  .size-col {
    width: 65px;
    min-width: 65px;
  }

  .sole-col {
    width: 90px;
    min-width: 90px;
  }

  .material-col {
    width: 90px;
    min-width: 90px;
  }

  .insole-col {
    width: 90px;
    min-width: 90px;
  }

  .weight-col {
    width: 80px;
    min-width: 80px;
  }

  .sport-col {
    width: 90px;
    min-width: 90px;
  }

  .season-col {
    width: 80px;
    min-width: 80px;
  }

  .durability-col {
    width: 80px;
    min-width: 80px;
  }

  .waterproof-col {
    width: 90px;
    min-width: 90px;
  }

  .image-col {
    width: 75px;
    min-width: 75px;
  }

  .quantity-col {
    width: 65px;
    min-width: 65px;
  }

  .price-col {
    width: 95px;
    min-width: 95px;
  }

  .status-col {
    width: 95px;
    min-width: 95px;
  }

  .action-col {
    width: 85px;
    min-width: 85px;
  }
}

/* Responsive Design */
@media (max-width: 1400px) {
  .filters-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 1.25rem;
  }
}

@media (max-width: 1000px) {
  .filters-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
}

@media (max-width: 768px) {
  .page-header {
    padding: 1rem;
  }

  .header-content {
    flex-direction: column;
    align-items: stretch;
    text-align: center;
  }

  .page-title {
    font-size: 1.5rem;
  }

  .header-actions {
    justify-content: center;
    flex-wrap: wrap;
  }

  .search-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .search-section {
    margin: 0 1rem 2rem 1rem;
  }

  .input-group {
    max-width: 100%;
  }

  .search-input {
    min-height: 56px;
    font-size: 1rem;
    padding: 1rem 1rem 1rem 3.5rem;
  }

  .input-icon {
    left: 1.25rem;
    font-size: 1.25rem;
  }

  .clear-btn {
    right: 1rem;
    padding: 0.5rem;
    font-size: 1.1rem;
  }

  .filters-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }

  .filter-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-actions .btn {
    width: 100%;
  }

  .table-container {
    margin: 0 -1rem;
    border-radius: 0;
    overflow-x: auto;
    overflow-y: hidden;
    /* Ensure horizontal scrollbar is visible on mobile */
    -webkit-overflow-scrolling: touch;
  }

  /* Responsive table cho mobile */
  .product-table {
    font-size: 0.65rem;
    min-width: 1200px;
    /* Ensure all columns are visible on mobile */
    table-layout: fixed;
  }

  .product-table th,
  .product-table td {
    padding: 0.4rem 0.2rem;
  }

  /* Giảm width các cột trên mobile */
  .stt-col {
    width: 35px;
    min-width: 35px;
  }

  .product-col {
    width: 100px;
    min-width: 100px;
  }

  .color-col {
    width: 60px;
    min-width: 60px;
  }

  .size-col {
    width: 45px;
    min-width: 45px;
  }

  .sole-col {
    width: 70px;
    min-width: 70px;
  }

  .material-col {
    width: 70px;
    min-width: 70px;
  }

  .insole-col {
    width: 70px;
    min-width: 70px;
  }

  .weight-col {
    width: 60px;
    min-width: 60px;
  }

  .sport-col {
    width: 70px;
    min-width: 70px;
  }

  .season-col {
    width: 60px;
    min-width: 60px;
  }

  .durability-col {
    width: 60px;
    min-width: 60px;
  }

  .waterproof-col {
    width: 70px;
    min-width: 70px;
  }

  .image-col {
    width: 60px;
    min-width: 60px;
  }

  .quantity-col {
    width: 45px;
    min-width: 45px;
  }

  .price-col {
    width: 70px;
    min-width: 70px;
  }

  .status-col {
    width: 70px;
    min-width: 70px;
  }

  .action-col {
    width: 60px;
    min-width: 60px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-grid .form-group:nth-child(13),
  .form-grid .form-group:nth-child(14) {
    grid-column: span 1;
  }

  .modal-overlay {
    padding: 1rem;
  }

  .no-data {
    font-size: 0.875rem;
    color: var(--medium-gray);
    padding: 2rem 1rem;
  }
}

@media (max-width: 480px) {

  /* Responsive table cho mobile nhỏ */
  .product-table {
    font-size: 0.6rem;
    min-width: 1100px;
    /* Even smaller minimum width for very small screens */
    table-layout: fixed;
  }

  .product-table th,
  .product-table td {
    padding: 0.3rem 0.15rem;
  }

  .search-section {
    margin: 0 0.5rem 1.5rem 0.5rem;
  }

  .search-input {
    min-height: 52px;
    font-size: 0.95rem;
    padding: 0.875rem 0.875rem 0.875rem 3rem;
    border-radius: 12px;
  }

  .input-icon {
    left: 1rem;
    font-size: 1.1rem;
  }

  .clear-btn {
    right: 0.75rem;
    padding: 0.4rem;
    font-size: 1rem;
  }

  .filters-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .filter-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .filter-toggle {
    align-self: flex-end;
  }

  /* Giảm width các cột trên mobile nhỏ */
  .stt-col {
    width: 30px;
    min-width: 30px;
  }

  .product-col {
    width: 90px;
    min-width: 90px;
  }

  .color-col {
    width: 55px;
    min-width: 55px;
  }

  .size-col {
    width: 40px;
    min-width: 40px;
  }

  .sole-col {
    width: 65px;
    min-width: 65px;
  }

  .material-col {
    width: 65px;
    min-width: 65px;
  }

  .insole-col {
    width: 65px;
    min-width: 65px;
  }

  .weight-col {
    width: 55px;
    min-width: 55px;
  }

  .sport-col {
    width: 65px;
    min-width: 65px;
  }

  .season-col {
    width: 55px;
    min-width: 55px;
  }

  .durability-col {
    width: 55px;
    min-width: 55px;
  }

  .waterproof-col {
    width: 65px;
    min-width: 65px;
  }

  .image-col {
    width: 55px;
    min-width: 55px;
  }

  .quantity-col {
    width: 40px;
    min-width: 40px;
  }

  .price-col {
    width: 65px;
    min-width: 65px;
  }

  .status-col {
    width: 65px;
    min-width: 65px;
  }

  .action-col {
    width: 55px;
    min-width: 55px;
  }

  .color-badge,
  .size-badge,
  .stock-badge,
  .status-badge {
    padding: 0.15rem 0.3rem;
    font-size: 0.6rem;
  }

  .attribute-text {
    padding: 0.2rem 0.3rem;
    font-size: 0.65rem;
    min-height: 24px;
  }

  .product-image {
    width: 25px;
    height: 25px;
  }

  .btn-edit,
  .btn-delete {
    min-width: 24px;
    height: 24px;
  }

     /* Ensure horizontal scrollbar is visible on very small screens */
   .table-container {
     -webkit-overflow-scrolling: touch;
     overflow-x: auto;
     overflow-y: hidden;
   }
 }

/* Image Upload Styles */
.image-upload-section {
  margin-top: 0.5rem;
}

.image-preview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.image-preview-item {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid #e5e7eb;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image-btn {
  position: absolute;
  top: 4px;
  right: 4px;
  background: rgba(239, 68, 68, 0.9);
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s ease;
}

.remove-image-btn:hover {
  background: #ef4444;
  transform: scale(1.1);
}

.image-upload-btn {
  width: 80px;
  height: 80px;
  border: 2px dashed #d1d5db;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #f9fafb;
}

.image-upload-btn:hover {
  border-color: #4ade80;
  background: #f0fdf4;
  transform: translateY(-2px);
}

.upload-icon {
  font-size: 1.5rem;
  margin-bottom: 0.25rem;
  color: #6b7280;
}

.upload-text {
  font-size: 0.75rem;
  color: #6b7280;
  font-weight: 500;
}

.image-help-text {
  font-size: 0.75rem;
  color: #6b7280;
  margin: 0;
  text-align: center;
}

/* Image Selector Modal */
.modal-content.image-selector {
  max-width: 800px;
  max-height: 80vh;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 1rem;
  max-height: 400px;
  overflow-y: auto;
  padding: 1rem;
}

.image-item {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.2s ease;
}

.image-item:hover {
  border-color: #4ade80;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.image-item.selected {
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.3);
}

.grid-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.image-item.selected .image-overlay {
  opacity: 1;
}

.check-icon {
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
}

.image-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 8px;
  font-size: 0.75rem;
  text-align: center;
}

.image-type {
  font-weight: 500;
}
</style>
