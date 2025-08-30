<template>
  <div class="product-management">
    <div class="breadcrumb-nav">
      <RouterLink :to="`/products`" class="product-link">
        <span class="product-text">Sản phẩm</span>
      </RouterLink>
      <span class="breadcrumb-separator">/</span>
      <span class="current-breadcrumb">{{ currentProductName }}</span>
    </div>
  </div>

  <!-- Modern Filter Section -->
  <div class="filter-section">
    <div class="filter-card">
      <div class="filter-content">
        <div class="search-section">
          <div class="input-group">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Tìm kiếm tên sản phẩm, màu sắc, kích thước..."
              class="form-control search-input"
            />
            <button
              v-if="searchQuery"
              @click="searchQuery = ''"
              class="clear-btn"
            >
              <span>✕</span>
            </button>
          </div>
        </div>

        <div class="filters-grid">
          <div class="filter-group">
            <label class="filter-label">Màu sắc</label>
            <select v-model="selectedMauSac" class="form-select">
              <option value="">Tất cả màu sắc</option>
              <option
                v-for="color in mauSacs"
                :key="color.id"
                :value="color.id"
              >
                {{ color.tenMauSac }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label class="filter-label">Kích thước</label>
            <select v-model="selectedKichThuoc" class="form-select">
              <option value="">Tất cả kích thước</option>
              <option
                v-for="size in kichThuocs"
                :key="size.id"
                :value="size.id"
              >
                {{ size.tenKichThuoc }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label class="filter-label">Chất liệu</label>
            <select v-model="selectedChatLieu" class="form-select">
              <option value="">Tất cả chất liệu</option>
              <option
                v-for="material in chatLieus"
                :key="material.id"
                :value="material.id"
              >
                {{ material.tenChatLieu }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label class="filter-label">Đế giày</label>
            <select v-model="selectedDeGiay" class="form-select">
              <option value="">Tất cả đế giày</option>
              <option v-for="sole in deGiays" :key="sole.id" :value="sole.id">
                {{ sole.tenDeGiay }}
              </option>
            </select>
          </div>
        </div>

        <div class="filters-grid-2">
          <div class="filter-group">
            <label class="filter-label">Trọng lượng</label>
            <select v-model="selectedTrongLuong" class="form-select">
              <option value="">Tất cả trọng lượng</option>
              <option
                v-for="weight in trongLuongs"
                :key="weight.id"
                :value="weight.id"
              >
                {{ weight.tenTrongLuong }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label class="filter-label">Trạng thái</label>
            <select v-model="statusFilter" class="form-select">
              <option value="">Tất cả trạng thái</option>
              <option value="false">Hoạt động</option>
              <option value="true">Ngừng hoạt động</option>
            </select>
          </div>

          <div class="filter-actions">
            <button @click="clearFiltersForEdit" class="btn btn-outline">
              Đặt lại
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Product Details Table -->
  <div class="card">
    <div class="card-header">
      <div class="table-header-content">
        <h3 class="table-title">Danh sách biến thể sản phẩm</h3>
        <div class="table-actions">
          <button
            v-if="hasCheckedChiTietSanPhams"
            @click="saveAllCheckedChiTietSanPhamsFromPopup"
            class="btn-save-all"
            title="Cập nhật tất cả chi tiết sản phẩm đã chọn"
          >
            Hoàn thành cập nhật ({{ checkedChiTietSanPhamsCount }})
          </button>
        </div>
      </div>
    </div>
    <div class="table-content-wrapper">
      <div class="table-container">
        <table class="product-table">
          <thead>
            <tr>
              <th class="stt-col">
                <input
                  type="checkbox"
                  :checked="isAllSelected"
                  :indeterminate="isIndeterminate"
                  @change="toggleSelectAll"
                  title="Chọn tất cả chi tiết sản phẩm"
                />
              </th>
              <th class="image-col">Ảnh sản phẩm</th>
              <th class="nha-san-xuat-col">Nhà SX</th>
              <th class="xuat-xu-col">Xuất xứ</th>
              <th class="color-col">Màu sắc</th>
              <th class="size-col">Kích thước</th>
              <th class="sole-col">Đế giày</th>
              <th class="material-col">Chất liệu</th>
              <th class="weight-col">Trọng lượng</th>
              <th class="quantity-col">Số lượng</th>
              <th class="price-col">Giá bán</th>
              <th class="giam-gia-col">Giảm giá</th>
              <th class="gia-sau-giam-col">Giá sau giảm</th>
              <th class="status-col">Trạng thái</th>
              <th class="action-col">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(detail, index) in paginatedDetails"
              :key="detail.id"
              :class="{ 'editing-row': editingChiTietSanPhams.has(detail.id) }"
            >
              <td class="stt-col">
                <input
                  type="checkbox"
                  :checked="selectedChiTietSanPhams.includes(detail.id)"
                  @change="toggleChiTietSanPhamSelection(detail.id)"
                  @click.stop
                />
              </td>
              <td
                class="image-col"
                :key="`image-${detail.id}-${imageDataKey.timestamp}`"
              >
                <div
                  v-if="getImagesForChiTietSanPhamForEdit(detail.id).length > 0"
                  class="image-preview"
                  :key="`image-preview-${detail.id}-${imageDataKey.timestamp}`"
                >
                  <img
                    :src="
                      getImageUrlForEdit(
                        getImagesForChiTietSanPhamForEdit(detail.id)[0]
                          .duongDanAnh
                      )
                    "
                    :alt="detail.tenSanPham || detail.sanPham?.tenSanPham"
                    class="product-image"
                    :key="`image-${detail.id}-${imageDataKey.timestamp}`"
                  />
                  <span
                    v-if="
                      getImagesForChiTietSanPhamForEdit(detail.id).length > 1
                    "
                    class="image-count"
                    :key="`count-${detail.id}-${imageDataKey.timestamp}`"
                  >
                    +{{
                      getImagesForChiTietSanPhamForEdit(detail.id).length - 1
                    }}
                  </span>
                </div>
                <span v-else class="no-image">Không có ảnh</span>
              </td>
              <td class="nha-san-xuat-col">
                <span class="nha-san-xuat-text">{{
                  detail.tenNhaSanXuat || "N/A"
                }}</span>
              </td>
              <td class="xuat-xu-col">
                <span class="xuat-xu-text">{{
                  detail.tenXuatXu || "N/A"
                }}</span>
              </td>
              <td class="color-col">
                <span class="color-badge">
                  {{ detail.tenMauSac || "N/A" }}
                </span>
              </td>
              <td class="size-col">
                <span class="size-badge">{{
                  detail.tenKichThuoc || "N/A"
                }}</span>
              </td>
              <td class="sole-col">
                <span class="attribute-text">{{
                  detail.tenDeGiay || "N/A"
                }}</span>
              </td>
              <td class="material-col">
                <span class="attribute-text">{{
                  detail.tenChatLieu || "N/A"
                }}</span>
              </td>
              <td class="weight-col">
                <span class="attribute-text">{{
                  detail.tenTrongLuong || "N/A"
                }}</span>
              </td>
              <td class="quantity-col">
                <div
                  v-if="editingChiTietSanPhams.has(detail.id)"
                  class="inline-edit"
                >
                  <input
                    v-model="editingChiTietSanPhams.get(detail.id).soLuong"
                    type="number"
                    class="edit-mode-input edit-input-soLuong"
                    min="0"
                    @keyup.enter="saveInlineEdit(detail.id)"
                    @keyup.esc="cancelInlineEdit(detail.id)"
                  />
                </div>
                <div
                  v-else
                  class="stock-badge"
                  :class="{ 'low-stock': detail.soLuong < 10 }"
                >
                  {{ detail.soLuong }}
                </div>
              </td>
              <td class="price-col">
                <div
                  v-if="editingChiTietSanPhams.has(detail.id)"
                  class="inline-edit"
                >
                  <input
                    v-model="editingChiTietSanPhams.get(detail.id).giaBan"
                    type="number"
                    class="edit-mode-input edit-input-giaBan"
                    min="0"
                    step="1000"
                    @keyup.enter="saveInlineEdit(detail.id)"
                    @keyup.esc="cancelInlineEdit(detail.id)"
                  />
                </div>
                <div v-else class="price-text">
                  {{ formatCurrency(detail.giaBan) }}
                </div>
              </td>
              <td class="giam-gia-col">
                <span v-if="detail.giaTriGiamGia > 0" class="giam-gia-value">
                  {{ detail.giaTriGiamGia }}%
                </span>
                <span v-else class="giam-gia-text">-</span>
              </td>
              <td class="gia-sau-giam-col">
                <span class="gia-sau-giam-text">{{
                  formatCurrency(
                    (detail.giaBan / 100) * (100 - detail.giaTriGiamGia)
                  )
                }}</span>
              </td>
              <td class="status-col">
                <div
                  v-if="editingChiTietSanPhams.has(detail.id)"
                  class="inline-edit"
                >
                  <select
                    v-model="editingChiTietSanPhams.get(detail.id).trangThai"
                    class="edit-mode-select"
                  >
                    <option :value="true">Hoạt động</option>
                    <option :value="false">Ngừng hoạt động</option>
                  </select>
                </div>
                <span
                  v-else
                  class="status-badge"
                  :class="
                    detail.trangThai ? 'status-active' : 'status-inactive'
                  "
                >
                  {{ detail.trangThai ? "Hoạt động" : "Tạm ngưng bán" }}
                </span>
              </td>
              <td class="action-col">
                <div class="action-buttons">
                  <div v-if="editingChiTietSanPhams.has(detail.id)">
                    <button
                      @click="saveInlineEdit(detail.id)"
                      class="btn btn-success btn-sm"
                      title="Hoàn thành cập nhật"
                    >
                      ✓
                    </button>
                    <button
                      @click="cancelInlineEdit(detail.id)"
                      class="btn btn-danger btn-sm"
                      title="Hủy bỏ"
                    >
                      ✕
                    </button>
                  </div>
                  <button
                    v-else
                    @click="startEdit(detail)"
                    class="btn btn-secondary"
                    title="Chỉnh sửa"
                  >
                    ✏️
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="paginatedDetails.length === 0">
              <td colspan="15" class="no-data">Không có dữ liệu</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="pagination-wrapper">
        <div class="pagination-info">
          Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của
          {{ filteredDetails.length }} chi tiết sản phẩm ({{ pageSize }}
          dòng/trang)
        </div>
        <div class="pagination">
          <button
            @click="previousPageForEdit"
            :disabled="currentPage === 1"
            class="btn btn-outline btn-sm"
          >
            ❮ Trước
          </button>
          <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
          <button
            @click="nextPageForEdit"
            :disabled="currentPage === totalPages"
            class="btn btn-outline btn-sm"
          >
            Sau ❯
          </button>
        </div>
      </div>

      <!-- Pagination Info khi chỉ có 1 trang -->
      <div v-else-if="filteredDetails.length > 0" class="pagination-wrapper">
        <div class="pagination-info">
          Hiển thị tất cả {{ filteredDetails.length }} chi tiết sản phẩm
        </div>
      </div>
    </div>
  </div>

  <!-- Popup thông báo thành công -->
  <div
    v-if="showSuccessPopup"
    class="success-popup-overlay"
    @click="closeSuccessPopupForEdit"
  >
    <div class="success-popup" @click.stop>
      <div class="success-popup-content">
        <h3 class="success-title">Thành công!</h3>
        <p class="success-message">{{ successMessage }}</p>
        <button class="success-btn" @click="closeSuccessPopupForEdit">
          Đóng
        </button>
      </div>
    </div>
  </div>

  <!-- Popup Edit Chi Tiết Sản Phẩm -->
  <div v-if="showEditPopup" class="edit-popup-overlay" @click="closeEditPopup">
    <div class="edit-popup" @click.stop>
      <div class="edit-popup-header">
        <h3>Chỉnh sửa chi tiết sản phẩm</h3>
        <button @click="closeEditPopup" class="close-btn">×</button>
      </div>
      <div class="edit-popup-content">
        <div class="form-row">
          <div class="form-group">
            <label>Nhà sản xuất:</label>
            <select
              v-model="currentEditingDetail.tenNhaSanXuat"
              class="form-select"
            >
              <option value="">Chọn nhà sản xuất</option>
              <option
                v-for="nhaSanXuat in nhaSanXuats"
                :key="nhaSanXuat.id"
                :value="nhaSanXuat.tenNhaSanXuat"
              >
                {{ nhaSanXuat.tenNhaSanXuat }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Xuất xứ:</label>
            <select
              v-model="currentEditingDetail.tenXuatXu"
              class="form-select"
            >
              <option value="">Chọn xuất xứ</option>
              <option
                v-for="xuatXu in xuatXus"
                :key="xuatXu.id"
                :value="xuatXu.tenXuatXu"
              >
                {{ xuatXu.tenXuatXu }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Màu sắc:</label>
            <select
              v-model="currentEditingDetail.tenMauSac"
              class="form-select"
            >
              <option value="">Chọn màu sắc</option>
              <option
                v-for="mauSac in mauSacs"
                :key="mauSac.id"
                :value="mauSac.tenMauSac"
              >
                {{ mauSac.tenMauSac }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Kích thước:</label>
            <select
              v-model="currentEditingDetail.tenKichThuoc"
              class="form-select"
            >
              <option value="">Chọn kích thước</option>
              <option
                v-for="kichThuoc in kichThuocs"
                :key="kichThuoc.id"
                :value="kichThuoc.tenKichThuoc"
              >
                {{ kichThuoc.tenKichThuoc }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Chất liệu:</label>
            <select
              v-model="currentEditingDetail.tenChatLieu"
              class="form-select"
            >
              <option value="">Chọn chất liệu</option>
              <option
                v-for="chatLieu in chatLieus"
                :key="chatLieu.id"
                :value="chatLieu.tenChatLieu"
              >
                {{ chatLieu.tenChatLieu }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Đế giày:</label>
            <select
              v-model="currentEditingDetail.tenDeGiay"
              class="form-select"
            >
              <option value="">Chọn đế giày</option>
              <option
                v-for="deGiay in deGiays"
                :key="deGiay.id"
                :value="deGiay.tenDeGiay"
              >
                {{ deGiay.tenDeGiay }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Trọng lượng:</label>
            <select
              v-model="currentEditingDetail.tenTrongLuong"
              class="form-select"
            >
              <option value="">Chọn trọng lượng</option>
              <option
                v-for="trongLuong in trongLuongs"
                :key="trongLuong.id"
                :value="trongLuong.tenTrongLuong"
              >
                {{ trongLuong.tenTrongLuong }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Đợt giảm giá:</label>
            <input
              v-model="currentEditingDetail.tenDotGiamGia"
              type="text"
              class="form-input"
              readonly
              placeholder="Đợt giảm giá hiện tại"
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Số lượng:</label>
            <input
              v-model="currentEditingDetail.soLuong"
              type="number"
              min="0"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>Giá bán:</label>
            <input
              v-model="currentEditingDetail.giaBan"
              type="number"
              min="0"
              step="1000"
              class="form-input"
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Trạng thái:</label>
            <select
              v-model="currentEditingDetail.trangThai"
              class="form-select"
            >
              <option :value="true">Hoạt động</option>
              <option :value="false">Ngừng hoạt động</option>
            </select>
          </div>
          <div class="form-group">
            <label>Ảnh sản phẩm:</label>
            <div class="image-selector-section">
              <div class="current-images">
                <div
                  v-for="(image, index) in currentEditingDetail.images"
                  :key="index"
                  class="image-item"
                >
                  <img
                    :src="getImageDisplayUrl(image)"
                    :alt="`Ảnh ${index + 1}`"
                    class="thumbnail"
                  />
                  <button
                    @click="removeImageFromPopup(index)"
                    class="remove-image-btn"
                    type="button"
                  >
                    ×
                  </button>
                </div>
              </div>
              <div class="image-actions">
                <button
                  @click="openImageSelectorForEdit"
                  class="btn btn-outline"
                  type="button"
                  :disabled="
                    currentEditingDetail.images &&
                    currentEditingDetail.images.length >= 5
                  "
                >
                  Chọn ảnh có sẵn
                </button>
                <div class="upload-section">
                  <input
                    ref="fileInput"
                    type="file"
                    accept="image/*"
                    multiple
                    @change="handleFileUploadForEdit"
                    class="file-input"
                    :disabled="
                      currentEditingDetail.images &&
                      currentEditingDetail.images.length >= 5
                    "
                  />
                  <button
                    @click="triggerFileUploadForEdit"
                    class="btn btn-outline"
                    type="button"
                    :disabled="
                      currentEditingDetail.images &&
                      currentEditingDetail.images.length >= 5
                    "
                  >
                    Upload ảnh mới
                  </button>
                </div>
              </div>
              <span
                v-if="
                  currentEditingDetail.images &&
                  currentEditingDetail.images.length >= 5
                "
                class="image-limit-info"
              >
                Tối đa 5 ảnh
              </span>
            </div>
          </div>
        </div>

        <div class="edit-popup-actions">
          <button @click="saveEditPopupFromPopup" class="btn btn-primary">
            Lưu
          </button>
          <button @click="closeEditPopup" class="btn btn-outline">Hủy</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Popup chọn ảnh cho edit -->
  <div
    v-if="showImageSelectorForEdit"
    class="image-selector-overlay"
    @click="closeImageSelectorForEdit"
  >
    <div class="image-selector-popup" @click.stop>
      <div class="image-selector-header">
        <h3>Chọn ảnh sản phẩm</h3>
        <button @click="closeImageSelectorForEdit" class="close-btn">×</button>
      </div>
      <div class="image-selector-content">
        <div class="selected-images-preview">
          <h4>Ảnh đã chọn ({{ selectedImagesForEdit.length }}/5):</h4>
          <div class="selected-images-grid">
            <div
              v-for="(image, index) in selectedImagesForEdit"
              :key="index"
              class="selected-image-item"
            >
              <img
                :src="getImageUrlForEdit(image.duongDanAnh)"
                :alt="`Ảnh ${index + 1}`"
                class="preview-image"
              />
              <button
                @click="removeSelectedImageForEditPopup(index)"
                class="remove-selected-btn"
                type="button"
              >
                ×
              </button>
            </div>
          </div>
        </div>
        <div class="available-images-section">
          <h4>Ảnh có sẵn:</h4>
          <div class="available-images-grid">
            <div
              v-for="image in availableImagesForEdit"
              :key="image.id"
              class="available-image-item"
              :class="{ selected: isImageSelectedForEdit(image.id) }"
              @click="toggleImageSelectionForEdit(image)"
            >
              <img
                :src="getImageUrlForEdit(image.duongDanAnh)"
                :alt="image.moTa || 'Ảnh sản phẩm'"
                class="available-image"
              />
              <div class="image-overlay">
                <span
                  v-if="isImageSelectedForEdit(image.id)"
                  class="selected-indicator"
                  >✓</span
                >
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="image-selector-actions">
        <button
          @click="confirmImageSelectionForEditPopup"
          class="btn btn-primary"
        >
          Xác nhận
        </button>
        <button @click="closeImageSelectorForEdit" class="btn btn-outline">
          Hủy
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import {
  fetchAllChiTietDotGiamGia,
  fetchUpdateChiTietDotGiamGia,
} from "../../../services/GiamGia/ChiTietDotGiamGiaService";
import { fetchAllDotGiamGia } from "../../../services/GiamGia/DotGiamGiaService";
import {
  fetchAllChiTietSanPham,
  fetchAllChiTietSanPhamBySanPhamId,
  fetchCreateChiTietSanPham,
  fetchUpdateChiTietSanPham,
  fetchUpdateStatusChiTietSanPham,
} from "../../../services/SanPham/ChiTietSanPhamService";
import {
  fetchAllSanPham,
  fetchUpdateSanPham,
} from "../../../services/SanPham/SanPhamService";
import {
  fetchAllAnhSanPham,
  fetchCreateAnhSanPham,
  fetchOneAnhSanPham,
  fetchUpdateAnhSanPham,
  fetchUpdateStatusAnhSanPham,
} from "../../../services/ThuocTinh/AnhSanPhamService";
import { fetchAllChatLieu } from "../../../services/ThuocTinh/ChatLieuService";
import {
  fetchAllChiTietSanPhamAnh,
  fetchCreateMultipleChiTietSanPhamAnh,
  fetchDeleteChiTietSanPhamAnh,
} from "../../../services/ThuocTinh/ChiTietSanPhamAnhService";
import { fetchAllDeGiay } from "../../../services/ThuocTinh/DeGiayService";
import { fetchAllKichThuoc } from "../../../services/ThuocTinh/KichThuocService";
import { fetchAllMauSac } from "../../../services/ThuocTinh/MauSacService";
import { fetchAllNhaSanXuat } from "../../../services/ThuocTinh/NhaSanXuatService";
import { fetchAllTrongLuong } from "../../../services/ThuocTinh/TrongLuongService";
import { fetchAllXuatXu } from "../../../services/ThuocTinh/XuatXuService";
// Reactive data
const searchQuery = ref("");
const selectedSanPham = ref("");
const selectedMauSac = ref("");
const selectedKichThuoc = ref("");
const selectedChatLieu = ref("");
const selectedDeGiay = ref("");
const selectedTrongLuong = ref("");
const selectedDotGiamGia = ref("");
const statusFilter = ref("");
const showAddModal = ref(false);
const showEditModal = ref(false);
const showImageSelector = ref(false);
const currentPage = ref(1);
const pageSize = ref(10);
const selectedImages = ref([]);
const selectedImageIds = ref([]);
const availableImages = ref([]);
const showSuccessPopup = ref(false);
const successMessage = ref("");
const sanPhams = ref([]);

// Inline editing variables
const editingChiTietSanPhams = ref(new Map()); // Map để lưu nhiều chi tiết sản phẩm đang edit
const selectedChiTietSanPhams = ref([]);
const originalChiTietSanPhams = ref(new Map()); // Map để lưu dữ liệu gốc
const isEditing = ref(false); // Biến để kiểm tra trạng thái edit

// Biến cho popup edit
const showEditPopup = ref(false);
const currentEditingDetail = ref({});

// Biến cho popup chọn ảnh edit
const showImageSelectorForEdit = ref(false);
const selectedImagesForEdit = ref([]);
const availableImagesForEdit = ref([]);

// Biến cho file upload
const fileInput = ref(null);
const uploadedImages = ref([]);
const newChiTietSanPham = ref({
  id: 0,
  idSanPham: 0,
  idMauSac: 0,
  idKichThuoc: 0,
  idDeGiay: 0,
  idChatLieu: 0,
  idTrongLuong: 0,
  idDotGiamGia: 0,
  soLuong: 0,
  giaBan: 0,
  trangThai: false,
  deleted: false,
  createdAt: "",
  createBy: 0,
  updatedAt: "",
  updateBy: 0,
});

// Data for dropdowns
const anhSanPhams = ref([]);
const chiTietSanPhams = ref([]);
const chiTietSanPhamAnhs = ref([]);
const mauSacs = ref([]);
const kichThuocs = ref([]);
const chatLieus = ref([]);
const deGiays = ref([]);
const trongLuongs = ref([]);
const nhaSanXuats = ref([]);
const xuatXus = ref([]);
const dotGiamGias = ref([]);
const chiTietDotGiamGias = ref([]);

// ========================================
// SETUP & LIFECYCLE
// ========================================

onMounted(async () => {
  const id = route.params.id;

  if (!id) {
    alert("Không tìm thấy ID sản phẩm!");
    return;
  }

  try {
    // 1. Fetch sản phẩm và các thuộc tính cơ bản
    await fetchSanPham();
    await fetchAnhSanPham();
    await fetchChiTietSanPhamAnh();
    await fetchMauSac();
    await fetchKichThuoc();
    await fetchDeGiay();
    await fetchChatLieu();
    await fetchTrongLuong();
    await fetchNhaSanXuat();
    await fetchXuatXu();
    await fetchDotGiamGia();

    // 2. Fetch chi tiết sản phẩm TRƯỚC
    await fetchChiTietSanPham(id);

    // 3. Fetch chi tiết đợt giảm giá SAU khi đã có chi tiết sản phẩm
    await fetchChiTietDotGiamGia();
  } catch (error) {
    console.error("onMounted error:", error);
    alert("Có lỗi xảy ra khi tải dữ liệu ban đầu!");
  }
});

// ========================================
// COMPUTED PROPERTIES
// ========================================

const currentProduct = computed(() => {
  return sanPhams.value.find(
    (sanPham) => sanPham.id === parseInt(route.params.id)
  );
});

const currentEditingDetailIndex = computed(() => {
  return chiTietSanPhams.value.findIndex(
    (chiTiet) => chiTiet.id === parseInt(route.params.id)
  );
});

const currentEditingDetailImages = computed(() => {
  return currentEditingDetail.value?.images || [];
});

const currentEditingDetailImagesCount = computed(() => {
  return currentEditingDetailImages.value.length;
});

const canAddMoreImages = computed(() => {
  return currentEditingDetailImagesCount.value < 5;
});

const imageLimitReached = computed(() => {
  return currentEditingDetailImagesCount.value >= 5;
});

// ========================================
// DATA FETCHING FUNCTIONS
// ========================================

const fetchSanPham = async () => {
  try {
    const response = await fetchAllSanPham();
    sanPhams.value = response.data || [];
  } catch (error) {
    console.error("Error fetching san pham:", error);
  }
};

const fetchAnhSanPham = async () => {
  try {
    const response = await fetchAllAnhSanPham();
    anhSanPhams.value = response.data || [];
  } catch (error) {
    console.error("Error fetching anh san pham:", error);
  }
};

const fetchChiTietSanPhamAnh = async () => {
  try {
    const response = await fetchAllChiTietSanPhamAnh();
    chiTietSanPhamAnhs.value = response.data || [];
  } catch (error) {
    console.error("Error fetching chi tiet san pham anh:", error);
  }
};

const fetchMauSac = async () => {
  try {
    const response = await fetchAllMauSac();
    mauSacs.value = response.data || [];
  } catch (error) {
    console.error("Error fetching mau sac:", error);
  }
};

const fetchKichThuoc = async () => {
  try {
    const response = await fetchAllKichThuoc();
    kichThuocs.value = response.data || [];
  } catch (error) {
    console.error("Error fetching kich thuoc:", error);
  }
};

const fetchDeGiay = async () => {
  try {
    const response = await fetchAllDeGiay();
    deGiays.value = response.data || [];
  } catch (error) {
    console.error("Error fetching de giay:", error);
  }
};

const fetchChatLieu = async () => {
  try {
    const response = await fetchAllChatLieu();
    chatLieus.value = response.data || [];
  } catch (error) {
    console.error("Error fetching chat lieu:", error);
  }
};

const fetchTrongLuong = async () => {
  try {
    const response = await fetchAllTrongLuong();
    trongLuongs.value = response.data || [];
  } catch (error) {
    console.error("Error fetching trong luong:", error);
  }
};

const fetchNhaSanXuat = async () => {
  try {
    const response = await fetchAllNhaSanXuat();
    nhaSanXuats.value = response.data || [];
  } catch (error) {
    console.error("Error fetching nha san xuat:", error);
  }
};

const fetchXuatXu = async () => {
  try {
    const response = await fetchAllXuatXu();
    xuatXus.value = response.data || [];
  } catch (error) {
    console.error("Error fetching xuat xu:", error);
  }
};

const fetchDotGiamGia = async () => {
  try {
    const response = await fetchAllDotGiamGia();
    dotGiamGias.value = response.data || [];
  } catch (error) {
    console.error("Error fetching dot giam gia:", error);
  }
};

const fetchChiTietSanPham = async (id) => {
  try {
    if (id) {
      // Fetch all chi tiet san pham by san pham ID
      const response = await fetchAllChiTietSanPhamBySanPhamId(id);
      if (response.data) {
        chiTietSanPhams.value = response.data || [];
      }
    } else {
      // Fetch all chi tiet san pham
      const response = await fetchAllChiTietSanPham();
      chiTietSanPhams.value = response.data || [];
    }
  } catch (error) {
    console.error("Error fetching chi tiet san pham:", error);
  }
};

const fetchChiTietDotGiamGia = async () => {
  try {
    const response = await fetchAllChiTietDotGiamGia();
    chiTietDotGiamGias.value = response.data || [];
  } catch (error) {
    console.error("Error fetching chi tiet dot giam gia:", error);
  }
};

const fetchAll = async () => {
  try {
    await Promise.all([
      fetchSanPham(),
      fetchAnhSanPham(),
      fetchChiTietSanPhamAnh(),
      fetchMauSac(),
      fetchKichThuoc(),
      fetchDeGiay(),
      fetchChatLieu(),
      fetchTrongLuong(),
      fetchNhaSanXuat(),
      fetchXuatXu(),
      fetchDotGiamGia(),
      fetchChiTietDotGiamGia(),
    ]);
  } catch (error) {
    console.error("Error fetching all data:", error);
  }
};

const fetchAllThuocTinh = async () => {
  try {
    await Promise.all([
      fetchAnhSanPham(),
      fetchChiTietSanPhamAnh(),
      fetchMauSac(),
      fetchKichThuoc(),
      fetchDeGiay(),
      fetchChatLieu(),
      fetchTrongLuong(),
      fetchNhaSanXuat(),
      fetchXuatXu(),
    ]);
  } catch (error) {
    console.error("Error fetching all thuoc tinh:", error);
  }
};

// ========================================
// IMAGE HANDLING FUNCTIONS
// ========================================

const uploadNewImagesForPopup = async (imageFiles, chiTietSanPhamId) => {
  const uploadedImages = [];

  for (const file of imageFiles) {
    try {
      if (!validateImageFile(file)) {
        continue;
      }

      const formData = new FormData();
      formData.append("file", file);
      formData.append("loaiAnh", file.name.split(".").pop());

      const response = await fetchCreateAnhSanPham(formData);

      if (response.success && response.data) {
        const imageId = response.data;

        // Tự động tạo liên kết trong ChiTietSanPhamAnh
        try {
          const linkData = {
            idChiTietSanPham: chiTietSanPhamId,
            idAnhSanPhamList: [imageId], // Chuyển thành mảng để phù hợp với backend
            trangThai: true,
            deleted: false,
          };

          const linkResponse = await fetchCreateMultipleChiTietSanPhamAnh(
            linkData
          );

          if (linkResponse.success) {
            uploadedImages.push({
              id: imageId,
              file: file,
              url: URL.createObjectURL(file),
            });
          } else {
            console.error("Failed to create link for image ID:", imageId);
          }
        } catch (linkError) {
          console.error(
            "Error creating link for image ID:",
            imageId,
            linkError
          );
        }
      } else {
        console.error("Upload failed - response:", response);
      }
    } catch (error) {
      console.error("Error uploading image:", error);
    }
  }

  return uploadedImages;
};

const updateProductImage = async (imageId, imageData) => {
  try {
    const response = await fetchUpdateAnhSanPham(imageId, imageData);
    return response.success;
  } catch (error) {
    console.error("Error updating product image:", error);
    return false;
  }
};

const deleteProductImage = async (imageId) => {
  try {
    const response = await fetchUpdateStatusAnhSanPham(imageId);
    return response.success;
  } catch (error) {
    console.error("Error deleting product image:", error);
    return false;
  }
};

const validateImageFile = (file) => {
  // Kiểm tra file có tồn tại không
  if (!file) {
    console.error("File is undefined or null");
    return false;
  }

  // Kiểm tra xem file có phải là File object không
  if (!(file instanceof File)) {
    console.error("File is not a valid File object:", file);
    return false;
  }

  const allowedTypes = ["image/jpeg", "image/jpg", "image/png", "image/webp"];
  const maxSize = 5 * 1024 * 1024; // 5MB

  if (!file.type || !allowedTypes.includes(file.type)) {
    console.error("Invalid file type:", file.type);
    return false;
  }

  if (!file.size || file.size > maxSize) {
    console.error("File too large:", file.size);
    return false;
  }

  return true;
};

const handleImageUploadError = (error, fileName) => {
  console.error(`Error uploading ${fileName}:`, error);
  alert(`Lỗi khi upload ảnh ${fileName}: ${error.message}`);
};

const refreshImageData = async () => {
  try {
    await fetchChiTietSanPhamAnh();
    await fetchAnhSanPham();
  } catch (error) {
    console.error("Error refreshing image data:", error);
  }
};

const checkImageStatus = async (imageId) => {
  try {
    const response = await fetchOneAnhSanPham(imageId);
    return response.success && response.data;
  } catch (error) {
    console.error("Error checking image status:", error);
    return false;
  }
};

const deleteAllImagesForChiTietSanPham = async (chiTietSanPhamId) => {
  try {
    const chiTietAnhSanPhams = chiTietSanPhamAnhs.value.filter(
      (item) => item.idChiTietSanPham === chiTietSanPhamId
    );

    for (const chiTietAnh of chiTietAnhSanPhams) {
      await fetchDeleteChiTietSanPhamAnh(chiTietAnh.id);
    }
  } catch (error) {
    console.error("Error deleting all images for chi tiet san pham:", error);
  }
};

const updateImagesForChiTietSanPham = async (chiTietSanPhamId, newImageIds) => {
  try {
    // Xóa tất cả ảnh cũ
    await deleteAllImagesForChiTietSanPham(chiTietSanPhamId);

    // Thêm ảnh mới
    if (newImageIds.length > 0) {
      const requestData = {
        idChiTietSanPham: chiTietSanPhamId,
        idAnhSanPhamList: newImageIds,
        trangThai: true,
        deleted: false,
      };

      const response = await fetchCreateMultipleChiTietSanPhamAnh(requestData);

      if (!response.success) {
        throw new Error(response.message || "Failed to create image links");
      }

      // Refresh dữ liệu ảnh sau khi tạo mới
      await refreshImageData();

      // Cập nhật lại ảnh trong currentEditingDetail
      if (
        currentEditingDetail.value &&
        currentEditingDetail.value.id === chiTietSanPhamId
      ) {
        await loadImagesForChiTietSanPham(chiTietSanPhamId);
      }

      // Nếu thành công, trả về thông báo thành công
      return response.message || "Cập nhật ảnh thành công";
    }

    return "Không có ảnh nào để cập nhật";
  } catch (error) {
    console.error("Error updating images for chi tiet san pham:", error);
    throw error;
  }
};

const handleProductImagesForPopup = async (
  chiTietSanPhamId,
  existingImages,
  newImageFiles
) => {
  try {
    const imageIds = [];

    // Xử lý ảnh có sẵn
    if (existingImages && existingImages.length > 0) {
      for (const image of existingImages) {
        if (image.id && image.duongDanAnh) {
          imageIds.push(image.id);
        }
      }
    }

    // Upload ảnh mới
    if (newImageFiles && newImageFiles.length > 0) {
      // Validate tất cả files trước khi upload
      const validFiles = newImageFiles.filter((file) => {
        if (!file || !file.file) {
          return false;
        }
        return true;
      });

      if (validFiles.length > 0) {
        const uploadedImages = await uploadNewImagesForPopup(
          validFiles,
          chiTietSanPhamId
        );

        for (const uploadedImage of uploadedImages) {
          if (uploadedImage.id) {
            imageIds.push(uploadedImage.id);
          }
        }
      }
    }

    // Cập nhật liên kết ảnh
    if (imageIds.length > 0) {
      const result = await updateImagesForChiTietSanPham(
        chiTietSanPhamId,
        imageIds
      );
    }

    return imageIds;
  } catch (error) {
    console.error("Error handling product images:", error);
    throw error;
  }
};

// ========================================
// CRUD OPERATIONS
// ========================================

const saveEditPopupFromPopup = async () => {
  if (!currentEditingDetail.value) {
    alert("Không tìm thấy chi tiết sản phẩm để cập nhật!");
    return;
  }

  try {
    // Lấy thông tin nhà sản xuất và xuất xứ từ chi tiết sản phẩm
    // Tìm dựa trên tên nếu không có id
    const nhaSanXuat =
      nhaSanXuats.value.find(
        (nsx) => nsx.id === currentEditingDetail.value.idNhaSanXuat?.id
      ) ||
      nhaSanXuats.value.find(
        (nsx) => nsx.tenNhaSanXuat === currentEditingDetail.value.tenNhaSanXuat
      );
    const xuatXu =
      xuatXus.value.find(
        (xx) => xx.id === currentEditingDetail.value.idXuatXu?.id
      ) ||
      xuatXus.value.find(
        (xx) => xx.tenXuatXu === currentEditingDetail.value.tenXuatXu
      );

    // Tìm thông tin chất liệu dựa trên tên nếu không có id
    const chatLieu =
      chatLieus.value.find(
        (cl) => cl.id === currentEditingDetail.value.idChatLieu?.id
      ) ||
      chatLieus.value.find(
        (cl) => cl.tenChatLieu === currentEditingDetail.value.tenChatLieu
      );

    // Tìm thông tin đế giày dựa trên tên nếu không có id
    const deGiay =
      deGiays.value.find(
        (dg) => dg.id === currentEditingDetail.value.idDeGiay?.id
      ) ||
      deGiays.value.find(
        (dg) => dg.tenDeGiay === currentEditingDetail.value.tenDeGiay
      );

    // Tìm thông tin kích thước dựa trên tên nếu không có id
    const kichThuoc =
      kichThuocs.value.find(
        (kt) => kt.id === currentEditingDetail.value.idKichThuoc?.id
      ) ||
      kichThuocs.value.find(
        (kt) => kt.tenKichThuoc === currentEditingDetail.value.tenKichThuoc
      );

    // Tìm thông tin màu sắc dựa trên tên nếu không có id
    const mauSac =
      mauSacs.value.find(
        (ms) => ms.id === currentEditingDetail.value.idMauSac?.id
      ) ||
      mauSacs.value.find(
        (ms) => ms.tenMauSac === currentEditingDetail.value.tenMauSac
      );

    // Tìm thông tin trọng lượng dựa trên tên nếu không có id
    const trongLuong =
      trongLuongs.value.find(
        (tl) => tl.id === currentEditingDetail.value.idTrongLuong?.id
      ) ||
      trongLuongs.value.find(
        (tl) => tl.tenTrongLuong === currentEditingDetail.value.tenTrongLuong
      );

    // Chuẩn bị dữ liệu sản phẩm với fallback logic
    const sanPhamData = {
      id: currentProduct.value?.id,
      tenSanPham: currentProduct.value?.tenSanPham || "Sản phẩm",
      moTa: currentProduct.value?.moTa || "",
      giaBan: currentProduct.value?.giaBan || 0,
      idNhaSanXuat:
        currentProduct.value?.idNhaSanXuat?.id ||
        currentEditingDetail.value?.idNhaSanXuat?.id ||
        nhaSanXuat?.id ||
        null,
      idXuatXu:
        currentProduct.value?.idXuatXu?.id ||
        currentEditingDetail.value?.idXuatXu?.id ||
        xuatXu?.id ||
        null,
      trangThai: currentProduct.value?.trangThai || 1,
      deleted: currentProduct.value?.deleted || false,
    };

    // Kiểm tra dữ liệu trước khi update
    if (!sanPhamData.idNhaSanXuat) {
      console.error("❌ idNhaSanXuat is null! Cannot update product.");
      alert("Không thể cập nhật sản phẩm: Thiếu thông tin nhà sản xuất!");
      return;
    }

    if (!sanPhamData.idXuatXu) {
      console.error("❌ idXuatXu is null! Cannot update product.");
      alert("Không thể cập nhật sản phẩm: Thiếu thông tin xuất xứ!");
      return;
    }

    // Kiểm tra chất liệu
    if (!chatLieu?.id) {
      console.error("❌ idChatLieu is null! Cannot update product detail.");
      alert("Không thể cập nhật chi tiết sản phẩm: Thiếu thông tin chất liệu!");
      return;
    }

    // Kiểm tra đế giày
    if (!deGiay?.id) {
      console.error("❌ idDeGiay is null! Cannot update product detail.");
      alert("Không thể cập nhật chi tiết sản phẩm: Thiếu thông tin đế giày!");
      return;
    }

    // Kiểm tra kích thước
    if (!kichThuoc?.id) {
      console.error("❌ idKichThuoc is null! Cannot update product detail.");
      alert(
        "Không thể cập nhật chi tiết sản phẩm: Thiếu thông tin kích thước!"
      );
      return;
    }

    // Kiểm tra màu sắc
    if (!mauSac?.id) {
      console.error("❌ idMauSac is null! Cannot update product detail.");
      alert("Không thể cập nhật chi tiết sản phẩm: Thiếu thông tin màu sắc!");
      return;
    }

    // Kiểm tra trọng lượng
    if (!trongLuong?.id) {
      console.error("❌ idTrongLuong is null! Cannot update product detail.");
      alert(
        "Không thể cập nhật chi tiết sản phẩm: Thiếu thông tin trọng lượng!"
      );
      return;
    }

    // Cập nhật sản phẩm
    if (sanPhamData.id) {
      await fetchUpdateSanPham(sanPhamData.id, sanPhamData);
    }

    // Chuẩn bị dữ liệu chi tiết sản phẩm
    const chiTietSanPhamData = {
      id: currentEditingDetail.value.id,
      idSanPham: currentProduct.value?.id,
      idMauSac: mauSac?.id || null,
      idKichThuoc: kichThuoc?.id || null,
      idDeGiay: deGiay?.id || null,
      idChatLieu: chatLieu?.id || null,
      idTrongLuong: trongLuong?.id || null,
      giaBan: currentEditingDetail.value.giaBan || 0,
      soLuong: currentEditingDetail.value.soLuong || 0,
      trangThai: currentEditingDetail.value.trangThai || 1,
      deleted: currentEditingDetail.value.deleted || false,
    };

    // Cập nhật chi tiết sản phẩm
    await fetchUpdateChiTietSanPham(chiTietSanPhamData.id, chiTietSanPhamData);

    // Xử lý ảnh sản phẩm
    if (
      currentEditingDetail.value.images &&
      currentEditingDetail.value.images.length > 0
    ) {
      // Lọc ảnh có sẵn (có id và duongDanAnh) và ảnh mới (có file)
      const existingImages = currentEditingDetail.value.images.filter(
        (img) => img.id && img.duongDanAnh && !img.file
      );
      const newImageFiles = currentEditingDetail.value.images.filter(
        (img) => img.file
      );

      await handleProductImagesForPopup(
        currentEditingDetail.value.id,
        existingImages,
        newImageFiles
      );
    } // <-- Added this closing brace for the images if

    // Xử lý đợt giảm giá
    if (currentEditingDetail.value.idDotGiamGia) {
      const dotGiamGia = dotGiamGias.value.find(
        (dg) => dg.id === currentEditingDetail.value.idDotGiamGia
      );

      if (dotGiamGia) {
        // Tìm chi tiết đợt giảm giá

        const chiTietDotGiamGia = chiTietDotGiamGias.value.find(
          (chiTietDotGiamGia) =>
            chiTietDotGiamGia.idChiTietSanPham ===
              currentEditingDetail.value.id &&
            chiTietDotGiamGia.idDotGiamGia ===
              currentEditingDetail.value.idDotGiamGia
        );

        // Nếu không tìm thấy, thử tìm chỉ với idChiTietSanPham
        let fallbackChiTietDotGiamGia = null;
        if (!chiTietDotGiamGia) {
          fallbackChiTietDotGiamGia = chiTietDotGiamGias.value.find(
            (item) => item.idChiTietSanPham === currentEditingDetail.value.id
          );
        }

        // Sử dụng fallback nếu cần
        const finalChiTietDotGiamGia =
          chiTietDotGiamGia || fallbackChiTietDotGiamGia;

        // Chỉ kiểm tra chiTietDotGiamGia nếu có đợt giảm giá
        if (finalChiTietDotGiamGia) {
          const dotGiamGiaData = {
            id: finalChiTietDotGiamGia.id,
            idDotGiamGia: dotGiamGia.id,
            idChiTietSanPham: currentEditingDetail.value.id,
            trangThai: finalChiTietDotGiamGia.trangThai || 1,
            deleted: finalChiTietDotGiamGia.deleted || false,
            createdAt:
              finalChiTietDotGiamGia.createdAt || new Date().toISOString(),
            updatedAt: new Date().toISOString(),
          };

          // Chỉ cập nhật dotGiamGia nếu có dữ liệu
          if (dotGiamGiaData) {
            await fetchUpdateChiTietDotGiamGia(
              dotGiamGiaData.id,
              dotGiamGiaData
            );
          }
        }
      }
    }

    // Refresh dữ liệu - chỉ lấy chi tiết sản phẩm của sản phẩm hiện tại
    await refreshImageData();
    await fetchChiTietSanPham(route.params.id);

    // Hiển thị thông báo thành công
    showSuccessPopupForEdit("Cập nhật chi tiết sản phẩm thành công!");

    // Đóng popup
    closeEditPopup();
  } catch (error) {
    console.error("Error saving edit popup:", error);
    alert("Có lỗi xảy ra khi cập nhật: " + error.message);
  }
};
const saveAllCheckedChiTietSanPhamsFromPopup = async () => {
  try {
    // Kiểm tra xem có item nào được chọn không
    if (
      !selectedChiTietSanPhams.value ||
      selectedChiTietSanPhams.value.length === 0
    ) {
      showSuccessNotificationForEdit(
        "Vui lòng chọn ít nhất một chi tiết sản phẩm để cập nhật!"
      );
      return;
    }

    // Hiển thị popup confirm
    const confirmDialog = document.createElement("div");
    confirmDialog.className = "custom-confirm-dialog";
    confirmDialog.style.cssText = `
      position: fixed !important;
      top: 0 !important;
      left: 0 !important;
      width: 100vw !important;
      height: 100vh !important;
      z-index: 999999 !important;
      display: flex !important;
      justify-content: center !important;
      align-items: center !important;
      background: rgba(0, 0, 0, 0.6) !important;
    `;

    confirmDialog.innerHTML = `
            <div class="confirm-content" style="
              position: relative !important;
              z-index: 1000000 !important;
              transform: translateZ(0) !important;
              will-change: transform !important;
              background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
              padding: 2.5rem;
              border-radius: 20px;
              box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15), 0 0 0 1px rgba(255, 255, 255, 0.1);
              text-align: center;
              max-width: 450px;
              width: 90%;
              border: 1px solid rgba(255, 255, 255, 0.2);
            ">
                <h3>Xác nhận cập nhật</h3>
                <p>Bạn có chắc chắn muốn cập nhật ${selectedChiTietSanPhams.value.length} chi tiết sản phẩm đã chọn?</p>
                <div class="confirm-buttons">
                    <button class="btn btn-secondary" id="cancelUpdate">Hủy</button>
                    <button class="btn btn-primary" id="confirmUpdate">Xác nhận</button>
                </div>
            </div>
        `;

    // Đảm bảo popup được append vào body và hiển thị đúng vị trí
    document.body.appendChild(confirmDialog);

    // Force reflow để đảm bảo styles được áp dụng
    confirmDialog.offsetHeight;

    // Xử lý sự kiện confirm
    return new Promise((resolve) => {
      document
        .getElementById("confirmUpdate")
        .addEventListener("click", async () => {
          document.body.removeChild(confirmDialog);

          try {
            // Bước 1: Chuẩn bị dữ liệu cho tất cả các item đã chọn
            const updatePromises = [];
            const itemsToUpdate = [];

            for (const selectedId of selectedChiTietSanPhams.value) {
              // Tìm item trong editingChiTietSanPhams (sử dụng Map.get)
              const editingItem = editingChiTietSanPhams.value.get(selectedId);
              const originalItem =
                originalChiTietSanPhams.value.get(selectedId);

              if (editingItem && originalItem) {
                // Chỉ cập nhật 3 trường: soLuong, giaBan, trangThai
                // Các trường khác giữ nguyên từ dữ liệu gốc
                const updatedData = {
                  idSanPham: originalItem.idSanPham || 1,
                  idMauSac: originalItem.idMauSac || 1,
                  idKichThuoc: originalItem.idKichThuoc || 1,
                  idDeGiay: originalItem.idDeGiay || 1,
                  idChatLieu: originalItem.idChatLieu || 1,
                  idTrongLuong: originalItem.idTrongLuong || 1,
                  soLuong: editingItem.soLuong || originalItem.soLuong || 0,
                  giaBan: editingItem.giaBan || originalItem.giaBan || 0,
                  trangThai:
                    editingItem.trangThai !== undefined
                      ? editingItem.trangThai
                      : originalItem.trangThai !== undefined
                      ? originalItem.trangThai
                      : true,
                  ghiChu: originalItem.ghiChu || "",
                  deleted:
                    originalItem.deleted !== undefined
                      ? originalItem.deleted
                      : false,
                  createdAt: originalItem.createdAt || new Date(),
                  createBy: originalItem.createBy || 1,
                  updatedAt: new Date(),
                  updateBy: 1,
                };

                // Xử lý ảnh sản phẩm nếu có
                if (
                  editingItem.chiTietSanPhamAnhs &&
                  editingItem.chiTietSanPhamAnhs.length > 0
                ) {
                  try {
                    await handleProductImagesForInline(editingItem);
                  } catch (imageError) {
                    console.error(
                      `Lỗi xử lý ảnh cho item ${selectedId}:`,
                      imageError
                    );
                  }
                }

                itemsToUpdate.push({
                  id: selectedId,
                  data: updatedData,
                });

                // Tạo promise cập nhật
                updatePromises.push(
                  fetchUpdateChiTietSanPham(selectedId, updatedData)
                    .then((response) => {
                      return { id: selectedId, success: true, response };
                    })
                    .catch((error) => {
                      console.error(
                        `❌ Lỗi cập nhật item ${selectedId}:`,
                        error
                      );
                      return { id: selectedId, success: false, error };
                    })
                );
              }
            }

            if (itemsToUpdate.length === 0) {
              showSuccessNotificationForEdit(
                "Không có dữ liệu nào để cập nhật!"
              );
              resolve();
              return;
            }

            // Bước 2: Thực hiện cập nhật tất cả items
            const results = await Promise.all(updatePromises);

            // Bước 3: Xử lý kết quả
            const successfulUpdates = results.filter(
              (result) => result.success
            );
            const failedUpdates = results.filter((result) => !result.success);

            if (failedUpdates.length > 0) {
              showSuccessNotificationForEdit(
                `Cập nhật ${successfulUpdates.length}/${itemsToUpdate.length} items thành công. ${failedUpdates.length} items thất bại.`
              );
            } else {
              showSuccessNotificationForEdit(
                `Cập nhật thành công ${successfulUpdates.length} chi tiết sản phẩm!`
              );
            }

            // Bước 4: Refresh dữ liệu và reset trạng thái
            await fetchChiTietSanPham(route.params.id);

            // Reset các trạng thái
            editingChiTietSanPhams.value.clear();
            originalChiTietSanPhams.value.clear();
            selectedChiTietSanPhams.value = [];

            // Thoát khỏi edit mode
            isEditing.value = false;
          } catch (error) {
            console.error("❌ Lỗi trong quá trình cập nhật hàng loạt:", error);
            showSuccessNotificationForEdit(
              "Có lỗi xảy ra trong quá trình cập nhật!"
            );
          }

          resolve();
        });

      document.getElementById("cancelUpdate").addEventListener("click", () => {
        document.body.removeChild(confirmDialog);
        resolve();
      });
    });
  } catch (error) {
    console.error(
      "❌ Lỗi trong saveAllCheckedChiTietSanPhamsFromPopup:",
      error
    );
    showSuccessNotificationForEdit("Có lỗi xảy ra!");
  }
};

// ========================================
// UI HELPER FUNCTIONS
// ========================================

const showSuccessPopupForEdit = (message) => {
  successMessage.value = message;
  showSuccessPopup.value = true;

  setTimeout(() => {
    showSuccessPopup.value = false;
  }, 3000);
};

const closeEditPopupForEdit = () => {
  showEditPopup.value = false;
  currentEditingDetail.value = {};
};

const openEditPopupForEdit = (chiTietSanPham) => {
  currentEditingDetail.value = { ...chiTietSanPham };

  // Đảm bảo images luôn là array
  if (!currentEditingDetail.value.images) {
    currentEditingDetail.value.images = [];
  }

  // Đảm bảo mỗi image có cấu trúc đúng
  if (currentEditingDetail.value.images.length > 0) {
    currentEditingDetail.value.images = currentEditingDetail.value.images
      .map((img) => {
        if (img && typeof img === "object") {
          return {
            id: img.id || null,
            duongDanAnh: img.duongDanAnh || null,
            file: img.file || null,
            url: img.url || null,
            isNew: img.isNew || false,
          };
        }
        return null;
      })
      .filter((img) => img !== null);
  }

  showEditPopup.value = true;
};

const toggleCheckboxForEdit = (chiTietSanPham) => {
  chiTietSanPham.checked = !chiTietSanPham.checked;
};

const selectAllForEdit = () => {
  chiTietSanPhams.value.forEach((chiTiet) => {
    chiTiet.checked = true;
  });
};

const deselectAllForEdit = () => {
  chiTietSanPhams.value.forEach((chiTiet) => {
    chiTiet.checked = false;
  });
};

const getCheckedCountForEdit = () => {
  return chiTietSanPhams.value.filter((chiTiet) => chiTiet.checked).length;
};

const getTotalCountForEdit = () => {
  return chiTietSanPhams.value.length;
};

const getCheckedPercentageForEdit = () => {
  if (getTotalCountForEdit() === 0) return 0;
  return Math.round((getCheckedCountForEdit() / getTotalCountForEdit()) * 100);
};

// ========================================
// IMAGE UI FUNCTIONS
// ========================================

const openImageSelectorForAdd = () => {
  if (imageLimitReached.value) {
    alert("Đã đủ 5 ảnh, không thể thêm nữa!");
    return;
  }
  document.getElementById("imageSelector").click();
};

const openImageUploaderForEdit = () => {
  if (imageLimitReached.value) {
    alert("Đã đủ 5 ảnh, không thể thêm nữa!");
    return;
  }
  document.getElementById("imageUploader").click();
};

const handleImageSelectionForEdit = (event) => {
  const selectedFiles = Array.from(event.target.files);

  if (currentEditingDetailImagesCount.value + selectedFiles.length > 5) {
    alert("Chỉ được chọn tối đa 5 ảnh!");
    return;
  }

  selectedFiles.forEach((file) => {
    if (validateImageFile(file)) {
      const newImage = {
        id: null,
        file: file,
        url: URL.createObjectURL(file),
        isNew: true,
      };

      if (currentEditingDetail.value) {
        if (!currentEditingDetail.value.images) {
          currentEditingDetail.value.images = [];
        }
        currentEditingDetail.value.images.push(newImage);
      }
    }
  });

  event.target.value = "";
};

const handleImageUploadForEdit = (event) => {
  const uploadedFiles = Array.from(event.target.files);

  if (currentEditingDetailImagesCount.value + uploadedFiles.length > 5) {
    alert("Chỉ được upload tối đa 5 ảnh!");
    return;
  }

  uploadedFiles.forEach((file) => {
    if (validateImageFile(file)) {
      const newImage = {
        id: null,
        file: file,
        url: URL.createObjectURL(file),
        isNew: true,
      };

      if (currentEditingDetail.value) {
        if (!currentEditingDetail.value.images) {
          currentEditingDetail.value.images = [];
        }
        currentEditingDetail.value.images.push(newImage);
      }
    }
  });

  event.target.value = "";
};

const removeImageFromEdit = (index) => {
  if (currentEditingDetail.value && currentEditingDetail.value.images) {
    const imageToRemove = currentEditingDetail.value.images[index];

    // Revoke object URL nếu là ảnh mới
    if (imageToRemove.url && imageToRemove.isNew) {
      URL.revokeObjectURL(imageToRemove.url);
    }

    currentEditingDetail.value.images.splice(index, 1);
  }
};

const removeImageSimple = (index) => {
  if (currentEditingDetail.value && currentEditingDetail.value.images) {
    const imageToRemove = currentEditingDetail.value.images[index];

    // Revoke object URL nếu là ảnh mới
    if (imageToRemove.url && imageToRemove.isNew) {
      URL.revokeObjectURL(imageToRemove.url);
    }

    currentEditingDetail.value.images.splice(index, 1);
  }
};

const getImageDisplayName = (image) => {
  if (image.file) {
    return image.file.name;
  }
  if (image.tenAnh) {
    return image.tenAnh;
  }
  return "Ảnh sản phẩm";
};

const getImageUrl = (image) => {
  if (image.url) {
    return image.url;
  }
  if (image.duongDan) {
    return image.duongDan;
  }
  if (image.duongDanAnh) {
    return image.duongDanAnh;
  }
  return "";
};

// ========================================
// EVENT HANDLERS
// ========================================

const handleSaveClick = () => {
  if (getCheckedCount() === 0) {
    alert("Vui lòng chọn ít nhất một chi tiết sản phẩm để cập nhật!");
    return;
  }

  if (getCheckedCount() === 1) {
    const checkedChiTiet = chiTietSanPhams.value.find((ct) => ct.checked);
    openEditPopup(checkedChiTiet);
  } else {
    saveAllCheckedChiTietSanPhamsFromInline();
  }
};

const handleEditClick = (chiTietSanPham) => {
  openEditPopup(chiTietSanPham);
};

const handleDeleteClick = async (chiTietSanPham) => {
  if (confirm("Bạn có chắc chắn muốn xóa chi tiết sản phẩm này?")) {
    try {
      await fetchUpdateStatusChiTietSanPham(chiTietSanPham.id);

      // Refresh dữ liệu - chỉ lấy chi tiết sản phẩm của sản phẩm hiện tại
      await fetchChiTietSanPham(route.params.id);

      alert("Xóa chi tiết sản phẩm thành công!");
    } catch (error) {
      console.error("Error deleting chi tiet san pham:", error);
      alert("Có lỗi xảy ra khi xóa: " + error.message);
    }
  }
};

const handleImageSelectorChangeForEdit = (event) => {
  handleImageSelectionForEdit(event);
};

const handleImageUploaderChangeForEdit = (event) => {
  handleImageUploadForEdit(event);
};

const handleSuccessPopupCloseForEdit = () => {
  showSuccessPopup.value = false;
};

// ========================================
// UTILITY FUNCTIONS
// ========================================

const formatCurrency = (amount) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount);
};

const formatCurrencyForEdit = (amount) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount);
};

const formatDateForEdit = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleDateString("vi-VN");
};

const getStatusTextForEdit = (status) => {
  switch (status) {
    case 1:
      return "Hoạt động";
    case 0:
      return "Không hoạt động";
    default:
      return "Không xác định";
  }
};

const getStatusClassForEdit = (status) => {
  switch (status) {
    case 1:
      return "status-active";
    case 0:
      return "status-inactive";
    default:
      return "status-unknown";
  }
};

const route = useRoute();

const filteredDetails = computed(() => {
  const filtered = chiTietSanPhams.value.filter((detail) => {
    const matchesSearch =
      !searchQuery.value ||
      (detail.tenSanPham || detail.sanPham?.tenSanPham || "")
        ?.toLowerCase()
        .includes(searchQuery.value.toLowerCase()) ||
      (detail.tenMauSac || detail.mauSac?.tenMauSac || "")
        ?.toLowerCase()
        .includes(searchQuery.value.toLowerCase()) ||
      (detail.tenKichThuoc || detail.kichThuoc?.tenKichThuoc || "")
        ?.toLowerCase()
        .includes(searchQuery.value.toLowerCase());

    // Sử dụng logic tìm kiếm dựa trên tên thay vì ID để nhất quán với hiển thị
    const matchesProduct =
      !selectedSanPham.value ||
      detail.tenSanPham ===
        sanPhams.value.find((p) => p.id === selectedSanPham.value)
          ?.tenSanPham ||
      detail.sanPham?.tenSanPham ===
        sanPhams.value.find((p) => p.id === selectedSanPham.value)?.tenSanPham;

    const matchesColor =
      !selectedMauSac.value ||
      detail.tenMauSac ===
        mauSacs.value.find((m) => m.id === selectedMauSac.value)?.tenMauSac ||
      detail.mauSac?.tenMauSac ===
        mauSacs.value.find((m) => m.id === selectedMauSac.value)?.tenMauSac;

    const matchesSize =
      !selectedKichThuoc.value ||
      detail.tenKichThuoc ===
        kichThuocs.value.find((k) => k.id === selectedKichThuoc.value)
          ?.tenKichThuoc ||
      detail.kichThuoc?.tenKichThuoc ===
        kichThuocs.value.find((k) => k.id === selectedKichThuoc.value)
          ?.tenKichThuoc;

    const matchesChatLieu =
      !selectedChatLieu.value ||
      detail.tenChatLieu ===
        chatLieus.value.find((c) => c.id === selectedChatLieu.value)
          ?.tenChatLieu ||
      detail.chatLieu?.tenChatLieu ===
        chatLieus.value.find((c) => c.id === selectedChatLieu.value)
          ?.tenChatLieu;

    const matchesDeGiay =
      !selectedDeGiay.value ||
      detail.tenDeGiay ===
        deGiays.value.find((d) => d.id === selectedDeGiay.value)?.tenDeGiay ||
      detail.deGiay?.tenDeGiay ===
        deGiays.value.find((d) => d.id === selectedDeGiay.value)?.tenDeGiay;

    const matchesTrongLuong =
      !selectedTrongLuong.value ||
      detail.tenTrongLuong ===
        trongLuongs.value.find((t) => t.id === selectedTrongLuong.value)
          ?.tenTrongLuong ||
      detail.trongLuong?.tenTrongLuong ===
        trongLuongs.value.find((t) => t.id === selectedTrongLuong.value)
          ?.tenTrongLuong;

    const matchesStatus =
      statusFilter.value === "" || detail.trangThai == statusFilter.value;

    const result =
      matchesSearch &&
      matchesProduct &&
      matchesColor &&
      matchesSize &&
      matchesChatLieu &&
      matchesDeGiay &&
      matchesTrongLuong &&
      matchesStatus;

    return result;
  });

  return filtered;
});

const totalPages = computed(() =>
  Math.ceil(filteredDetails.value.length / pageSize.value)
);
const startIndex = computed(() => (currentPage.value - 1) * pageSize.value);
const endIndex = computed(() =>
  Math.min(startIndex.value + pageSize.value, filteredDetails.value.length)
);

const paginatedDetails = computed(() => {
  const paginated = filteredDetails.value.slice(
    startIndex.value,
    startIndex.value + pageSize.value
  );
  return paginated;
});

// Checkbox selection computed
const hasCheckedChiTietSanPhams = computed(() => {
  return selectedChiTietSanPhams.value.length > 0;
});

const checkedChiTietSanPhamsCount = computed(() => {
  return selectedChiTietSanPhams.value.length;
});

const isAllSelected = computed(() => {
  return (
    paginatedDetails.value.length > 0 &&
    paginatedDetails.value.every((detail) =>
      selectedChiTietSanPhams.value.includes(detail.id)
    )
  );
});

const isIndeterminate = computed(() => {
  const selectedCount = paginatedDetails.value.filter((detail) =>
    selectedChiTietSanPhams.value.includes(detail.id)
  ).length;
  return selectedCount > 0 && selectedCount < paginatedDetails.value.length;
});

// Computed để lấy thông tin sản phẩm hiện tại
const currentProductName = computed(() => {
  return currentProduct.value?.tenSanPham || "Không xác định";
});

// Ref để theo dõi thay đổi dữ liệu ảnh và đảm bảo table re-render
const imageDataKey = ref({
  chiTietSanPhamAnhsLength: 0,
  anhSanPhamsLength: 0,
  chiTietSanPhamsLength: 0,
  timestamp: Date.now(),
});

// Methods

const getColorCodeForEdit = (colorName) => {
  if (!colorName) return "#E5E7EB";

  const colorMap = {
    Đen: "#000000",
    Trắng: "#FFFFFF",
    Đỏ: "#FF0000",
    Xanh: "#0000FF",
    Vàng: "#FFFF00",
    Xám: "#808080",
    Nâu: "#8B4513",
    Hồng: "#FFC0CB",
    "Xanh dương": "#0066CC",
    "Xanh lá": "#00CC00",
    Cam: "#FF8C00",
    Tím: "#800080",
    "Xanh ngọc": "#00CED1",
    "Xanh navy": "#000080",
    "Xanh lá cây": "#228B22",
  };

  // Exact match first
  if (colorMap[colorName]) {
    return colorMap[colorName];
  }

  // Fuzzy matching for similar names
  const normalizedColorName = colorName.toLowerCase();
  for (const [key, value] of Object.entries(colorMap)) {
    if (
      normalizedColorName.includes(key.toLowerCase()) ||
      key.toLowerCase().includes(normalizedColorName)
    ) {
      return value;
    }
  }

  return "#E5E7EB"; // Default color
};

const editDetailForEdit = async (data) => {
  try {
    // Đảm bảo dữ liệu dropdown đã được load
    if (
      mauSacs.value.length === 0 ||
      kichThuocs.value.length === 0 ||
      chatLieus.value.length === 0 ||
      deGiays.value.length === 0 ||
      trongLuongs.value.length === 0
    ) {
      await fetchAll();
    }

    // Tìm ID tương ứng từ các mảng dropdown
    const productId =
      sanPhams.value.find(
        (p) =>
          p.tenSanPham === data.tenSanPham ||
          p.tenSanPham === data.sanPham?.tenSanPham
      )?.id || "";
    const mauSacId =
      mauSacs.value.find(
        (m) =>
          m.tenMauSac === data.tenMauSac ||
          m.tenMauSac === data.mauSac?.tenMauSac
      )?.id || "";
    const kichThuocId =
      kichThuocs.value.find(
        (k) =>
          k.tenKichThuoc === data.tenKichThuoc ||
          k.tenKichThuoc === data.kichThuoc?.tenKichThuoc
      )?.id || "";
    const chatLieuId =
      chatLieus.value.find(
        (c) =>
          c.tenChatLieu === data.tenChatLieu ||
          c.tenChatLieu === data.chatLieu?.tenChatLieu
      )?.id || "";
    const deGiayId =
      deGiays.value.find(
        (d) =>
          d.tenDeGiay === data.tenDeGiay ||
          d.tenDeGiay === data.deGiay?.tenDeGiay
      )?.id || "";
    const trongLuongId =
      trongLuongs.value.find(
        (t) =>
          t.tenTrongLuong === data.tenTrongLuong ||
          t.tenTrongLuong === data.trongLuong?.tenTrongLuong
      )?.id || "";
    newChiTietSanPham.value = {
      id: data.id,
      idSanPham: productId,
      idMauSac: mauSacId,
      idKichThuoc: kichThuocId,
      idChatLieu: chatLieuId,
      idDeGiay: deGiayId,
      idTrongLuong: trongLuongId,
      idDotGiamGia: dotGiamGiaId,
      idNhaSanXuat: nhaSanXuatId,
      idXuatXu: xuatXuId,
      idChiTietDotGiamGia: chiTietDotGiamGiaId,
      soLuong: data.soLuong || 0,
      giaBan: data.giaBan || 0,
      trangThai: data.trangThai,
      deleted: false,
      createdAt: data.createdAt,
      createBy: data.createBy,
    };
    // Load ảnh sản phẩm từ AnhSanPham
    const images = getImagesForChiTietSanPhamForEdit(data.id);
    if (images.length > 0) {
      selectedImages.value = images.map((img) => img.duongDanAnh);
      selectedImageIds.value = images.map((img) => img.id);
    } else {
      selectedImages.value = [];
      selectedImageIds.value = [];
    }

    showEditModal.value = true;
  } catch (error) {
    console.error("Error in editDetail:", error);
    alert("Có lỗi xảy ra khi mở form chỉnh sửa!");
  }
};

const saveDetailForEdit = async () => {
  try {
    // Validate required fields
    if (
      !newChiTietSanPham.value.idSanPham ||
      !newChiTietSanPham.value.idMauSac ||
      !newChiTietSanPham.value.idKichThuoc ||
      !newChiTietSanPham.value.idChatLieu ||
      !newChiTietSanPham.value.idDeGiay ||
      !newChiTietSanPham.value.idTrongLuong ||
      !newChiTietSanPham.value.idDotGiamGia ||
      !newChiTietSanPham.value.giaBan ||
      !newChiTietSanPham.value.soLuong ||
      !newChiTietSanPham.value.idNhaSanXuat ||
      !newChiTietSanPham.value.idXuatXu ||
      !newChiTietSanPham.value.idChiTietDotGiamGia
    ) {
      alert("Vui lòng nhập đầy đủ thông tin bắt buộc");
      return;
    }

    // Chuẩn bị dữ liệu để gửi, đảm bảo trạng thái đúng định dạng
    const dataToSend = { ...newChiTietSanPham.value };
    let chiTietSanPhamId = null;
    if (showAddModal.value) {
      // Create new
      const response = await fetchCreateChiTietSanPham(dataToSend);
      showSuccessNotificationForEdit("Thêm chi tiết sản phẩm thành công!");
      // Lấy ID của chi tiết sản phẩm vừa tạo từ response.data
      chiTietSanPhamId = response?.data;
    } else if (showEditModal.value) {
      // Update existing
      await fetchUpdateChiTietSanPham(dataToSend.id, dataToSend);
      showSuccessNotificationForEdit("Cập nhật chi tiết sản phẩm thành công!");
      chiTietSanPhamId = dataToSend.id;
    }

    // Xử lý ảnh sản phẩm
    if (chiTietSanPhamId && selectedImageIds.value.length > 0) {
      try {
        // Xóa tất cả liên kết ảnh cũ
        const existingImages = chiTietSanPhamAnhs.value.filter(
          (item) => item.idChiTietSanPham === chiTietSanPhamId
        );

        for (const existingImage of existingImages) {
          if (existingImage.id) {
            await fetchDeleteChiTietSanPhamAnh(existingImage.id);
          }
        }

        // Tạo liên kết ảnh mới - sử dụng method tạo nhiều ảnh cùng lúc
        await fetchCreateMultipleChiTietSanPhamAnh({
          idChiTietSanPham: chiTietSanPhamId,
          idAnhSanPhamList: selectedImageIds.value,
          deleted: false,
        });
      } catch (imageError) {
        console.error("Error handling images:", imageError);
      }
    }

    // Refresh data từ server để đảm bảo đồng bộ - chỉ lấy chi tiết sản phẩm của sản phẩm hiện tại

    // Đảm bảo thứ tự refresh để dữ liệu ảnh được cập nhật đúng
    await fetchChiTietSanPham(route.params.id);
    // Force refresh dữ liệu ảnh để đảm bảo table được cập nhật
    await forceRefreshImageData();
    closeModals();
  } catch (error) {
    alert("Có lỗi xảy ra khi lưu dữ liệu!");
  }
};

const closeModalsForEdit = () => {
  showAddModal.value = false;
  showEditModal.value = false;
  // Reset form về trạng thái ban đầu
  const id = route.params.id;
  newChiTietSanPham.value = {
    id: "",
    idSanPham: id ? parseInt(id) : "",
    idMauSac: "",
    idKichThuoc: "",
    idChatLieu: "",
    idDeGiay: "",
    idTrongLuong: "",
    idDotGiamGia: "",
    idNhaSanXuat: "",
    idXuatXu: "",
    idChiTietDotGiamGia: "",
    soLuong: 0,
    giaBan: 0,
    trangThai: false,
    deleted: false,
    createdAt: "",
  };
  selectedImages.value = ref([]);
  selectedImageIds.value = ref([]);
};

// Hàm hiển thị popup thành công
const showSuccessNotificationForEdit = (message) => {
  successMessage.value = message;
  showSuccessPopup.value = true;
  // Tự động đóng popup sau 3 giây
  setTimeout(() => {
    showSuccessPopup.value = false;
  }, 3000);
};

// Hàm đóng popup thành công
const closeSuccessPopupForEdit = () => {
  showSuccessPopup.value = false;
};

const clearFiltersForEdit = () => {
  searchQuery.value = "";
  selectedSanPham.value = "";
  selectedMauSac.value = "";
  selectedKichThuoc.value = "";
  selectedChatLieu.value = "";
  selectedDeGiay.value = "";
  selectedTrongLuong.value = "";
  selectedDotGiamGia.value = "";
  statusFilter.value = "";
  currentPage.value = 1;
};

const applyFiltersForEdit = () => {
  currentPage.value = 1;
};

const previousPageForEdit = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPageForEdit = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const exportDataForEdit = () => {
  alert("Xuất báo cáo thành công! (Chức năng đang được phát triển)");
};

const exportDetailsToExcelForEdit = () => {
  alert("Xuất Excel thành công! (Chức năng đang được phát triển)");
};

const refreshDataForEdit = async () => {
  try {
    // Reset về trang đầu tiên
    currentPage.value = 1;

    // Load lại dữ liệu
    const id = route.params.id;
    if (id) {
      await Promise.all([fetchChiTietSanPham(id), fetchAll()]);
    } else {
      await fetchAll();
    }

    // Clear các filter
    clearFiltersForEdit();

    alert("Làm mới dữ liệu thành công!");
  } catch (error) {
    alert("Có lỗi xảy ra khi làm mới dữ liệu!");
  }
};

const openAddModalForEdit = () => {
  // Reset form về trạng thái ban đầu
  const id = route.params.id;
  newChiTietSanPham.value = {
    id: "",
    idSanPham: id ? parseInt(id) : "",
    idMauSac: "",
    idKichThuoc: "",
    idChatLieu: "",
    idDeGiay: "",
    idTrongLuong: "",
    idDotGiamGia: "",
    idNhaSanXuat: "",
    idXuatXu: "",
    idChiTietDotGiamGia: "",
    soLuong: 0,
    giaBan: 0,
    trangThai: true,
    deleted: false,
    createdAt: "",
  };
  selectedImages.value = [];
  selectedImageIds.value = [];
  showAddModal.value = true;
};

const openImageSelectorForModal = () => {
  try {
    // Chỉ hiển thị những ảnh chưa bị xóa
    availableImages.value = anhSanPhams.value.filter((img) => !img.deleted);
    showImageSelector.value = true;
  } catch (error) {
    alert("Có lỗi khi mở image selector!");
  }
};

const closeImageSelectorForModal = () => {
  showImageSelector.value = false;
};

const toggleImageSelectionForModal = (imageId) => {
  const index = selectedImageIds.value.indexOf(imageId);
  if (index > -1) {
    // Bỏ chọn ảnh
    selectedImageIds.value.splice(index, 1);
    selectedImages.value.splice(index, 1);
  } else {
    // Chọn ảnh mới
    if (selectedImageIds.value.length < 5) {
      const image = anhSanPhams.value.find((img) => img.id === imageId);
      if (image) {
        selectedImageIds.value.push(imageId);
        selectedImageIds.value.push(image.duongDanAnh);
      }
    } else {
      alert("Chỉ được chọn tối đa 5 ảnh!");
    }
  }
};

const confirmImageSelectionForModal = () => {
  showImageSelector.value = false;
};

// Hàm xử lý ảnh cho edit
const openImageSelectorForEdit = () => {
  try {
    // Lấy ảnh hiện tại của chi tiết sản phẩm
    const currentImages = getImagesForChiTietSanPhamForEdit(
      currentEditingDetail.value.id
    );
    selectedImagesForEdit.value = [...currentImages];

    // Lọc ra những ảnh chưa được sử dụng hoặc chưa bị xóa
    availableImagesForEdit.value = anhSanPhams.value.filter(
      (img) => !img.deleted
    );

    showImageSelectorForEdit.value = true;
  } catch (error) {
    alert("Có lỗi khi mở image selector!");
  }
};

const closeImageSelectorForEdit = () => {
  showImageSelectorForEdit.value = false;
};

const toggleImageSelectionForEdit = (image) => {
  const index = selectedImagesForEdit.value.findIndex(
    (img) => img.id === image.id
  );

  if (index > -1) {
    // Bỏ chọn ảnh
    selectedImagesForEdit.value.splice(index, 1);
  } else {
    // Chọn ảnh mới (kiểm tra giới hạn 5 ảnh)
    if (selectedImagesForEdit.value.length < 5) {
      selectedImagesForEdit.value.push(image);
    } else {
      alert("Chỉ được chọn tối đa 5 ảnh!");
    }
  }
};

const isImageSelectedForEdit = (imageId) => {
  return selectedImagesForEdit.value.some((img) => img.id === imageId);
};

const removeSelectedImageForEditPopup = (index) => {
  selectedImagesForEdit.value.splice(index, 1);
};

const removeImageSimpleForEditPopup = (index) => {
  if (currentEditingDetail.value.images) {
    const imageToRemove = currentEditingDetail.value.images[index];

    // Revoke object URL nếu là ảnh mới
    if (imageToRemove.url && imageToRemove.isNew) {
      URL.revokeObjectURL(imageToRemove.url);
    }

    currentEditingDetail.value.images.splice(index, 1);
  }
};

const confirmImageSelectionForEditPopup = () => {
  // Cập nhật ảnh trong currentEditingDetail
  currentEditingDetail.value.images = [...selectedImagesForEdit.value];
  closeImageSelectorForEdit();
};

// Hàm xử lý file upload
const triggerFileUploadForEdit = () => {
  fileInput.value?.click();
};

const handleFileUploadForEdit = async (event) => {
  const files = event.target.files;
  if (!files || files.length === 0) return;

  // Kiểm tra số lượng ảnh hiện tại
  const currentImageCount = currentEditingDetail.value.images?.length || 0;
  const remainingSlots = 5 - currentImageCount;

  if (remainingSlots <= 0) {
    alert("Đã đủ 5 ảnh, không thể thêm ảnh mới!");
    return;
  }

  // Giới hạn số lượng file có thể upload
  const filesToProcess = Array.from(files).slice(0, remainingSlots);

  for (const file of filesToProcess) {
    try {
      // Validate file trước khi xử lý
      validateImageFileForInline(file);

      // Tạo URL preview ngay lập tức để hiển thị
      const imageUrl = URL.createObjectURL(file);

      // Tạo object ảnh tạm thời để hiển thị ngay
      const tempImage = {
        id: `temp_${Date.now()}_${Math.random()}`,
        duongDanAnh: file.name,
        loaiAnh: "product",
        url: imageUrl,
        isNew: true, // Đánh dấu là ảnh mới
        file: file,
      };

      // Thêm vào danh sách ảnh hiện tại ngay lập tức để hiển thị
      if (!currentEditingDetail.value.images) {
        currentEditingDetail.value.images = [];
      }
      currentEditingDetail.value.images.push(tempImage);

      // Upload ảnh lên server
      const formData = new FormData();
      formData.append("file", file);
      formData.append("loaiAnh", file.name.split(".").pop());

      const uploadResponse = await fetchCreateAnhSanPham(formData);

      if (uploadResponse.success && uploadResponse.data) {
        const imageId = uploadResponse.data;

        // Tự động tạo liên kết trong ChiTietSanPhamAnh
        try {
          const linkData = {
            idChiTietSanPham: currentEditingDetail.value.id,
            idAnhSanPhamList: [imageId], // Chuyển thành mảng để phù hợp với backend
            trangThai: true,
            deleted: false,
            createAt: new Date().toISOString().split("T")[0],
          };

          const linkResponse = await fetchCreateMultipleChiTietSanPhamAnh(
            linkData
          );

          if (linkResponse.success) {
            // Cập nhật ảnh tạm thành ảnh thật
            const imageIndex = currentEditingDetail.value.images.findIndex(
              (img) => img.id === tempImage.id
            );
            if (imageIndex !== -1) {
              currentEditingDetail.value.images[imageIndex] = {
                ...tempImage,
                id: imageId,
                isNew: false, // Không phải ảnh tạm nữa
              };
            }
          } else {
            console.error(
              "Failed to create link in edit popup for image ID:",
              imageId
            );
            // Nếu tạo link thất bại, xóa ảnh tạm
            const imageIndex = currentEditingDetail.value.images.findIndex(
              (img) => img.id === tempImage.id
            );
            if (imageIndex !== -1) {
              currentEditingDetail.value.images.splice(imageIndex, 1);
            }
            URL.revokeObjectURL(imageUrl);
          }
        } catch (linkError) {
          console.error(
            "Error creating link in edit popup for image ID:",
            imageId,
            linkError
          );
          // Nếu có lỗi, xóa ảnh tạm
          const imageIndex = currentEditingDetail.value.images.findIndex(
            (img) => img.id === tempImage.id
          );
          if (imageIndex !== -1) {
            currentEditingDetail.value.images.splice(imageIndex, 1);
          }
          URL.revokeObjectURL(imageUrl);
        }
      } else {
        console.error(
          "Upload failed in edit popup - response:",
          uploadResponse
        );
        // Nếu upload thất bại, xóa ảnh tạm
        const imageIndex = currentEditingDetail.value.images.findIndex(
          (img) => img.id === tempImage.id
        );
        if (imageIndex !== -1) {
          currentEditingDetail.value.images.splice(imageIndex, 1);
        }
        URL.revokeObjectURL(imageUrl);
      }
    } catch (error) {
      console.error("Error processing file in edit popup:", error);
      handleImageUploadErrorForInline(error, file.name);
    }
  }

  // Reset input file
  event.target.value = "";
};

// Hàm xóa ảnh (cả ảnh có sẵn và ảnh mới upload)
const removeImageFromPopup = (index) => {
  if (currentEditingDetail.value.images) {
    const imageToRemove = currentEditingDetail.value.images[index];

    // Nếu là ảnh mới upload, giải phóng URL object
    if (imageToRemove.isNew && imageToRemove.url) {
      URL.revokeObjectURL(imageToRemove.url);
    }

    // Nếu là ảnh có sẵn, có thể xóa khỏi server (tùy chọn)
    if (!imageToRemove.isNewUpload && imageToRemove.id) {
      // Có thể gọi API để xóa ảnh khỏi server nếu cần
      removeProductImageForEdit(imageToRemove.id);
    }

    currentEditingDetail.value.images.splice(index, 1);
  }
};

// Hàm xóa ảnh sản phẩm hoàn chỉnh (bao gồm cả server)
const removeProductImageForEdit = async (imageId) => {
  try {
    if (imageId && !imageId.toString().startsWith("temp_")) {
      // Xóa ảnh khỏi server
      await fetchUpdateStatusAnhSanPham(imageId);
    }

    // Refresh dữ liệu ảnh
    await refreshImageData();

    return true;
  } catch (error) {
    console.error(`Error removing product image ${imageId}:`, error);
    throw error;
  }
};

// Method để lấy ảnh cho một chi tiết sản phẩm
const getImagesForChiTietSanPhamForEdit = (chiTietSanPhamId) => {
  try {
    console.log(
      "🔍 getImagesForChiTietSanPhamForEdit called with ID:",
      chiTietSanPhamId
    );

    // Tìm chi tiết sản phẩm trong danh sách đã fetch
    const chiTietSanPham = chiTietSanPhams.value.find(
      (item) => item.id === chiTietSanPhamId
    );

    if (!chiTietSanPham) {
      console.log("🔍 ChiTietSanPham not found for ID:", chiTietSanPhamId);
      return [];
    }

    // Kiểm tra xem chi tiết sản phẩm có trường anhSanPham không
    if (chiTietSanPham.anhSanPham && Array.isArray(chiTietSanPham.anhSanPham)) {
      console.log(
        "🔍 Using anhSanPham from chiTietSanPham:",
        chiTietSanPham.anhSanPham
      );

      // Chuyển đổi array đường dẫn ảnh thành format tương thích với UI
      const images = chiTietSanPham.anhSanPham.map((duongDanAnh, index) => ({
        id: `direct_${chiTietSanPhamId}_${index}`, // Tạo ID giả
        duongDanAnh: duongDanAnh,
        loaiAnh: "product",
        moTa: `Ảnh ${index + 1} của chi tiết sản phẩm ${chiTietSanPhamId}`,
        file: null,
        url: null,
        isNew: false,
      }));

      console.log(
        "🔍 Converted images for chiTietSanPhamId",
        chiTietSanPhamId,
        ":",
        images
      );
      return images;
    }

    // Fallback: sử dụng cách cũ nếu không có anhSanPham trực tiếp
    console.log(
      "🔍 Fallback: using old method for chiTietSanPhamId",
      chiTietSanPhamId
    );

    // Đảm bảo dữ liệu đã được load
    if (!chiTietSanPhamAnhs.value || !anhSanPhams.value) {
      console.log("🔍 Data not loaded yet:", {
        chiTietSanPhamAnhsLength: chiTietSanPhamAnhs.value?.length || 0,
        anhSanPhamsLength: anhSanPhams.value?.length || 0,
      });
      return [];
    }

    // Lọc các liên kết ảnh cho chi tiết sản phẩm này
    const imageLinks = chiTietSanPhamAnhs.value.filter(
      (item) => item.idChiTietSanPham === chiTietSanPhamId && !item.deleted
    );

    console.log(
      "🔍 Found image links for chiTietSanPhamId",
      chiTietSanPhamId,
      ":",
      imageLinks
    );

    // Map để lấy thông tin ảnh đầy đủ
    const images = imageLinks
      .map((item) => {
        const anhSanPham = anhSanPhams.value.find(
          (anh) => anh.id === item.idAnhSanPham
        );
        console.log(
          "🔍 Looking for anhSanPham with ID:",
          item.idAnhSanPham,
          "Found:",
          anhSanPham
        );
        if (anhSanPham) {
          return {
            id: anhSanPham.id,
            duongDanAnh: anhSanPham.duongDanAnh,
            loaiAnh: anhSanPham.loaiAnh,
            moTa: anhSanPham.moTa,
            file: null,
            url: null,
            isNew: false,
          };
        } else {
          console.log("⚠️ AnhSanPham not found for ID:", item.idAnhSanPham);
          return null;
        }
      })
      .filter((img) => img !== null);

    console.log(
      "🔍 Final images for chiTietSanPhamId",
      chiTietSanPhamId,
      ":",
      images
    );
    return images;
  } catch (error) {
    console.error("Error getting images for chi tiet san pham:", error);
    return [];
  }
};

// Method để tạo URL đầy đủ cho ảnh
const getImageUrlForEdit = (imagePath) => {
  try {
    console.log("🔍 getImageUrlForEdit called with:", imagePath);

    if (!imagePath) {
      console.log("🔍 imagePath is empty");
      return "";
    }

    // Nếu đã là URL đầy đủ thì trả về nguyên
    if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
      console.log("🔍 imagePath is already full URL:", imagePath);
      return imagePath;
    }

    // Nếu là đường dẫn tương đối, thêm base URL của backend
    if (imagePath.startsWith("uploads/")) {
      const fullUrl = `http://localhost:8080/${imagePath}`;
      console.log("🔍 Converted uploads path to full URL:", fullUrl);
      return fullUrl;
    }

    // Nếu bắt đầu bằng / thì thêm base URL
    if (imagePath.startsWith("/")) {
      const fullUrl = `http://localhost:8080${imagePath}`;
      console.log("🔍 Converted absolute path to full URL:", fullUrl);
      return fullUrl;
    }

    console.log("🔍 Returning imagePath as is:", imagePath);
    return imagePath;
  } catch (error) {
    console.error("Error getting image URL:", error);
    return "";
  }
};

// Method để lấy URL hiển thị ảnh trong form edit (ưu tiên url trước, sau đó mới đến duongDanAnh)
const getImageDisplayUrl = (image) => {
  try {
    console.log("🔍 getImageDisplayUrl called with:", image);

    if (!image) {
      console.log("🔍 image is null/undefined");
      return "";
    }

    // Ưu tiên url (object URL) trước - dành cho ảnh mới upload
    if (image.url) {
      console.log("🔍 Using image.url:", image.url);
      return image.url;
    }

    // Nếu không có url thì dùng duongDanAnh (ảnh có sẵn)
    if (image.duongDanAnh) {
      console.log("🔍 Using image.duongDanAnh:", image.duongDanAnh);
      return getImageUrlForEdit(image.duongDanAnh);
    }

    console.log("🔍 No valid URL found in image object");
    return "";
  } catch (error) {
    console.error("Error getting image display URL:", error);
    return "";
  }
};

// Method để force refresh dữ liệu ảnh
const forceRefreshImageDataForEdit = async () => {
  try {
    // Refresh dữ liệu ảnh
    await fetchAllThuocTinh();
    await fetchChiTietSanPham(route.params.id);

    // Force Vue re-render bằng cách thay đổi timestamp
    imageDataKey.value = {
      chiTietSanPhamAnhsLength: chiTietSanPhamAnhs.value?.length || 0,
      anhSanPhamsLength: anhSanPhams.value?.length || 0,
      chiTietSanPhamsLength: chiTietSanPhams.value?.length || 0,
      timestamp: Date.now(),
    };
  } catch (error) {
    // Xử lý lỗi silently
  }
};

// Hàm xử lý upload ảnh mới lên server
const uploadNewImagesForInline = async (imageFiles) => {
  try {
    const uploadPromises = imageFiles.map(async (file) => {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("loaiAnh", "product");
      formData.append("moTa", file.name || "Ảnh sản phẩm");

      const response = await fetchCreateAnhSanPham(formData);

      // Đảm bảo trả về đúng ID từ response
      if (response && response.data) {
        return { id: response.data, file: file };
      } else {
        throw new Error(
          `Không nhận được ID ảnh từ server: ${JSON.stringify(response)}`
        );
      }
    });

    const uploadedImages = await Promise.all(uploadPromises);
    return uploadedImages;
  } catch (error) {
    console.error("Error uploading new images:", error);
    throw new Error("Không thể upload ảnh mới lên server");
  }
};

// Hàm xử lý cập nhật ảnh sản phẩm
const updateProductImageForInline = async (imageId, file) => {
  try {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("loaiAnh", "product");
    formData.append("moTa", file.name || "Ảnh sản phẩm");

    const response = await fetchUpdateAnhSanPham(imageId, formData);
    return response.data;
  } catch (error) {
    console.error("Error updating product image:", error);
    throw new Error("Không thể cập nhật ảnh sản phẩm");
  }
};

// Hàm xử lý xóa ảnh sản phẩm (soft delete)
const deleteProductImageForInline = async (imageId) => {
  try {
    const response = await fetchUpdateStatusAnhSanPham(imageId);
    return response.data;
  } catch (error) {
    console.error("Error deleting product image:", error);
    throw new Error("Không thể xóa ảnh sản phẩm");
  }
};

// Hàm xử lý ảnh sản phẩm hoàn chỉnh sử dụng API mới
const handleProductImagesForInline = async (
  chiTietSanPhamId,
  currentImages,
  newImageFiles
) => {
  try {
    // 1. Upload ảnh mới lên server
    let uploadedImageIds = [];
    if (newImageFiles && newImageFiles.length > 0) {
      const uploadedImages = await uploadNewImagesForInline(newImageFiles);
      uploadedImageIds = uploadedImages.map((img) => img.id);
    }

    // 2. Lấy danh sách ID ảnh cuối cùng (ảnh có sẵn + ảnh mới upload)
    const existingImageIds = currentImages
      .filter((img) => !img.isNewUpload && img.id)
      .map((img) => img.id);

    const finalImageIds = [...existingImageIds, ...uploadedImageIds];

    // 3. Sử dụng API mới để cập nhật toàn bộ ảnh
    if (finalImageIds.length > 0) {
      await fetchUpdateAllChiTietSanPhamAnh(chiTietSanPhamId, finalImageIds);
    } else {
      // Nếu không có ảnh nào, xóa tất cả liên kết cũ
      await fetchDeleteAllChiTietSanPhamAnhByChiTietSanPhamId(chiTietSanPhamId);
    }

    // 4. Refresh dữ liệu ảnh để đảm bảo UI được cập nhật
    await refreshImageDataForInline();

    // 5. Kiểm tra trạng thái liên kết ảnh sau khi xử lý
    const finalStatus = await checkImageStatusForInline(chiTietSanPhamId);

    return true;
  } catch (error) {
    console.error("Error handling product images:", error);
    throw error;
  }
};

// Hàm kiểm tra và validate ảnh trước khi upload
const validateImageFileForInline = (file) => {
  // Kiểm tra file có tồn tại không
  if (!file) {
    throw new Error("File không tồn tại!");
  }

  // Kiểm tra xem file có phải là File object không
  if (!(file instanceof File)) {
    throw new Error("File không hợp lệ!");
  }

  // Kiểm tra loại file
  if (!file.type || !file.type.startsWith("image/")) {
    throw new Error(`File ${file.name} không phải là ảnh!`);
  }

  // Kiểm tra kích thước file (giới hạn 5MB)
  if (!file.size || file.size > 5 * 1024 * 1024) {
    throw new Error(
      `File ${file.name} quá lớn! Vui lòng chọn file nhỏ hơn 5MB.`
    );
  }

  // Kiểm tra tên file
  if (!file.name || file.name.length > 100) {
    throw new Error(
      `Tên file ${file.name} quá dài! Vui lòng đổi tên file ngắn hơn.`
    );
  }

  return true;
};

// Hàm xử lý lỗi upload ảnh
const handleImageUploadErrorForInline = (error, fileName) => {
  const errorMessage = error.message || "Lỗi không xác định";
  alert(`Không thể upload ảnh ${fileName}: ${errorMessage}`);
};

// Hàm refresh dữ liệu ảnh sau khi xử lý

// Hàm kiểm tra trạng thái ảnh của chi tiết sản phẩm
const checkImageStatusForInline = async (chiTietSanPhamId) => {
  try {
    const status = await fetchCheckChiTietSanPhamAnhStatus(chiTietSanPhamId);
    return status;
  } catch (error) {
    console.error("Error checking image status:", error);
    return null;
  }
};

// Hàm xóa tất cả ảnh của một chi tiết sản phẩm
const deleteAllImagesForChiTietSanPhamForInline = async (chiTietSanPhamId) => {
  try {
    const result = await fetchDeleteAllChiTietSanPhamAnhByChiTietSanPhamId(
      chiTietSanPhamId
    );
    return result;
  } catch (error) {
    console.error(
      "Error deleting all images for chiTietSanPhamId:",
      chiTietSanPhamId,
      ":",
      error
    );
    throw error;
  }
};

// Hàm cập nhật ảnh cho chi tiết sản phẩm (wrapper function)
const updateImagesForChiTietSanPhamForInline = async (
  chiTietSanPhamId,
  imageIds
) => {
  try {
    const result = await fetchUpdateAllChiTietSanPhamAnh(
      chiTietSanPhamId,
      imageIds
    );
    return result;
  } catch (error) {
    console.error(
      "Error updating images for chiTietSanPhamId:",
      chiTietSanPhamId,
      ":",
      error
    );
    throw error;
  }
};
const refreshImageDataForInline = async () => {
  try {
    // Refresh dữ liệu ảnh và liên kết
    await fetchAnhSanPham();
    await fetchChiTietSanPhamAnh();

    // Force Vue re-render
    imageDataKey.value = {
      chiTietSanPhamAnhsLength: chiTietSanPhamAnhs.value?.length || 0,
      anhSanPhamsLength: anhSanPhams.value?.length || 0,
      timestamp: Date.now(),
    };
  } catch (error) {
    console.error("Error refreshing image data:", error);
  }
};

// Inline editing functions
const toggleEditMode = (detail) => {
  if (editingChiTietSanPhams.value.has(detail.id)) {
    // Nếu đang edit thì tắt edit mode cho chi tiết sản phẩm này
    editingChiTietSanPhams.value.delete(detail.id);
    originalChiTietSanPhams.value.delete(detail.id);
  } else {
    // Nếu chưa edit thì bật edit mode cho chi tiết sản phẩm này
    editingChiTietSanPhams.value.set(detail.id, { ...detail });
    originalChiTietSanPhams.value.set(detail.id, { ...detail });
  }
};

const startEdit = (detail) => {
  // Chỉ mở popup edit, không kích hoạt inline edit
  showEditPopup.value = true;

  // Lấy ảnh hiện tại của chi tiết sản phẩm
  const currentImages = getImagesForChiTietSanPhamForEdit(detail.id);

  currentEditingDetail.value = {
    ...detail,
    images: currentImages,
  };

  // Đảm bảo images luôn là array
  if (!currentEditingDetail.value.images) {
    currentEditingDetail.value.images = [];
  }

  // Đảm bảo mỗi image có cấu trúc đúng
  if (currentEditingDetail.value.images.length > 0) {
    currentEditingDetail.value.images = currentEditingDetail.value.images
      .map((img) => {
        if (img && typeof img === "object") {
          return {
            id: img.id || null,
            duongDanAnh: img.duongDanAnh || null,
            file: img.file || null,
            url: img.url || null,
            isNew: img.isNew || false,
          };
        }
        return null;
      })
      .filter((img) => img !== null);
  }
};

// Hàm đóng popup edit
const closeEditPopup = () => {
  showEditPopup.value = false;
  currentEditingDetail.value = {};
};

// Hàm lưu từ popup edit
const saveEditPopupFromInline = async () => {
  try {
    // Tìm các thuộc tính cần thiết
    const mauSac = mauSacs.value.find(
      (mauSac) => mauSac.tenMauSac === currentEditingDetail.value.tenMauSac
    );

    if (!mauSac) {
      alert("Không tìm thấy màu sắc!");
      return;
    }

    const kichThuoc = kichThuocs.value.find(
      (kichThuoc) =>
        kichThuoc.tenKichThuoc === currentEditingDetail.value.tenKichThuoc
    );

    if (!kichThuoc) {
      alert("Không tìm thấy kích thước!");
      return;
    }

    const chatLieu = chatLieus.value.find(
      (chatLieu) =>
        chatLieu.tenChatLieu === currentEditingDetail.value.tenChatLieu
    );

    if (!chatLieu) {
      alert("Không tìm thấy chất liệu!");
      return;
    }

    const deGiay = deGiays.value.find(
      (deGiay) => deGiay.tenDeGiay === currentEditingDetail.value.tenDeGiay
    );

    if (!deGiay) {
      alert("Không tìm thấy đế giày!");
      return;
    }

    const trongLuong = trongLuongs.value.find(
      (trongLuong) =>
        trongLuong.tenTrongLuong === currentEditingDetail.value.tenTrongLuong
    );

    if (!trongLuong) {
      alert("Không tìm thấy trọng lượng!");
      return;
    }

    const chiTietDotGiamGia = chiTietDotGiamGias.value.find(
      (chiTietDotGiamGia) =>
        chiTietDotGiamGia.idChiTietSanPham === currentEditingDetail.value.id &&
        chiTietDotGiamGia.idDotGiamGia ===
          currentEditingDetail.value.idDotGiamGia
    );

    if (!chiTietDotGiamGia) {
      alert("Không tìm thấy chi tiết đợt giảm giá!");
      return;
    }

    const dotGiamGia = dotGiamGias.value.find(
      (dotGiamGia) => dotGiamGia.id === currentEditingDetail.value.idDotGiamGia
    );

    if (!dotGiamGia) {
      alert("Không tìm thấy đợt giảm giá!");
      return;
    }

    const nhaSanXuat = nhaSanXuats.value.find(
      (nhaSanXuat) =>
        nhaSanXuat.tenNhaSanXuat === currentEditingDetail.value.tenNhaSanXuat
    );

    if (!nhaSanXuat) {
      alert("Không tìm thấy nhà sản xuất!");
      return;
    }

    const xuatXu = xuatXus.value.find(
      (xuatXu) => xuatXu.tenXuatXu === currentEditingDetail.value.tenXuatXu
    );

    if (!xuatXu) {
      alert("Không tìm thấy xuất xứ!");
      return;
    }

    const sanPham = sanPhams.value.find(
      (sanPham) => sanPham.tenSanPham === currentEditingDetail.value.tenSanPham
    );

    if (!sanPham) {
      alert("Không tìm thấy sản phẩm!");
      return;
    }

    const chiTietSanPhamData = {
      id: currentEditingDetail.value.id,
      idSanPham: parseInt(route.params.id),
      idMauSac: mauSac.id,
      idKichThuoc: kichThuoc.id,
      idChatLieu: chatLieu.id,
      idDeGiay: deGiay.id,
      idTrongLuong: trongLuong.id,
      soLuong: currentEditingDetail.value.soLuong,
      giaBan: currentEditingDetail.value.giaBan,
      trangThai: currentEditingDetail.value.trangThai,
      deleted: false,
      createdAt: currentEditingDetail.value.createdAt,
      updatedAt: new Date().toISOString(),
    };
    // Chỉ cập nhật dotGiamGiaData nếu có đợt giảm giá
    let dotGiamGiaData = null;
    if (dotGiamGia) {
      dotGiamGiaData = {
        id: dotGiamGia.id,
        tenDotGiamGia: dotGiamGia.tenDotGiamGia,
        giaTriGiamGia: dotGiamGia.giaTriGiamGia,
        ngayBatDau: dotGiamGia.ngayBatDau,
        ngayKetThuc: dotGiamGia.ngayKetThuc,
        trangThai: dotGiamGia.trangThai,
        deleted: dotGiamGia.deleted,
      };
    }

    const sanPhamData = {
      id: parseInt(route.params.id),
      idNhaSanXuat: nhaSanXuat.id,
      idXuatXu: xuatXu.id,
      tenSanPham: sanPham.tenSanPham,
      trangThai: sanPham.trangThai,
      deleted: sanPham.deleted,
      createdAt: sanPham.createdAt,
      updatedAt: new Date().toISOString(),
    };

    await fetchUpdateChiTietSanPham(chiTietSanPhamData.id, chiTietSanPhamData);
    await fetchUpdateSanPham(sanPhamData.id, sanPhamData);
    if (dotGiamGiaData) {
      await fetchUpdateChiTietDotGiamGia(dotGiamGiaData.id, dotGiamGiaData);
    }
    // Xử lý ảnh sản phẩm
    if (currentEditingDetail.value.images) {
      try {
        // Phân loại ảnh
        const existingImages = currentEditingDetail.value.images.filter(
          (img) => !img.isNewUpload
        );
        const newImageFiles = currentEditingDetail.value.images
          .filter((img) => img.isNewUpload)
          .map((img) => img.file);

        // Sử dụng hàm xử lý ảnh hoàn chỉnh
        await handleProductImagesForInline(
          currentEditingDetail.value.id,
          existingImages,
          newImageFiles
        );
      } catch (imageError) {
        console.error("Error handling images:", imageError);
        // Không dừng quá trình lưu nếu có lỗi xử lý ảnh
        alert(`Cảnh báo: Có lỗi xảy ra khi xử lý ảnh: ${imageError.message}`);
      }
    }

    // Refresh data (ảnh đã được refresh trong handleProductImages)
    await fetchAll();

    // Force refresh table để hiển thị ảnh mới
    imageDataKey.value = {
      chiTietSanPhamAnhsLength: chiTietSanPhamAnhs.value?.length || 0,
      chiTietSanPhamAnhsLength: chiTietSanPhamAnhs.value?.length || 0,
      timestamp: Date.now(),
    };

    // Đóng popup
    closeEditPopup();

    showSuccessNotificationForEdit("Cập nhật chi tiết sản phẩm thành công!");
  } catch (error) {
    console.error("Error saving edit popup:", error);
    alert("Có lỗi xảy ra khi cập nhật!");
  }
};

// Hàm lưu inline edit
const saveInlineEdit = async (detailId) => {
  const editingDetail = editingChiTietSanPhams.value.get(detailId);
  if (!editingDetail) return;

  try {
    // Lấy dữ liệu gốc để có đầy đủ thông tin
    const originalDetail = originalChiTietSanPhams.value.get(detailId);

    // Chỉ cập nhật 3 trường: soLuong, giaBan, trangThai
    // Các trường khác giữ nguyên từ dữ liệu gốc
    const updatedData = {
      idSanPham: parseInt(route.params.id),
      idMauSac: originalDetail?.idMauSac || 1,
      idKichThuoc: originalDetail?.idKichThuoc || 1,
      idDeGiay: originalDetail?.idDeGiay || 1,
      idChatLieu: originalDetail?.idChatLieu || 1,
      idTrongLuong: originalDetail?.idTrongLuong || 1,
      soLuong: editingDetail.soLuong || originalDetail?.soLuong || 0,
      giaBan: editingDetail.giaBan || originalDetail?.giaBan || 0,
      trangThai:
        editingDetail.trangThai !== undefined
          ? editingDetail.trangThai
          : originalDetail?.trangThai !== undefined
          ? originalDetail?.trangThai
          : true,
      ghiChu: originalDetail?.ghiChu || "",
      deleted: originalDetail?.deleted || false,
      createdAt: originalDetail?.createdAt || new Date().toISOString(),
      createBy: originalDetail?.createBy || 1,
      updatedAt: new Date().toISOString(),
      updateBy: 1,
    };

    const response = await fetchUpdateChiTietSanPham(detailId, updatedData);

    // Reset editing state cho chi tiết sản phẩm này
    editingChiTietSanPhams.value.delete(detailId);
    originalChiTietSanPhams.value.delete(detailId);

    // Refresh data
    await fetchChiTietSanPham(route.params.id);

    showSuccessNotificationForEdit("Cập nhật chi tiết sản phẩm thành công!");
  } catch (error) {
    console.error("Error saving inline edit:", error);
    alert("Có lỗi xảy ra khi cập nhật!");
  }
};

// Hàm hủy inline edit
const cancelInlineEdit = (detailId) => {
  // Khôi phục dữ liệu gốc
  const originalDetail = originalChiTietSanPhams.value.get(detailId);
  if (originalDetail) {
    editingChiTietSanPhams.value.set(detailId, { ...originalDetail });
  }

  // Tắt edit mode
  editingChiTietSanPhams.value.delete(detailId);
  originalChiTietSanPhams.value.delete(detailId);
};

// Function để hủy tất cả chỉnh sửa
const cancelAllEdits = () => {
  editingChiTietSanPhams.value.clear();
  originalChiTietSanPhams.value.clear();
  selectedChiTietSanPhams.value = [];
};

// Function để lưu tất cả các thay đổi đã chọn
const saveAllCheckedChiTietSanPhamsFromInline = async () => {
  // Kiểm tra xem có chi tiết nào được chọn không
  if (selectedChiTietSanPhams.value.length === 0) {
    alert("Vui lòng chọn ít nhất một chi tiết sản phẩm để cập nhật!");
    return;
  }

  // Hiển thị popup confirm
  const confirmMessage = `Bạn có chắc chắn muốn cập nhật ${selectedChiTietSanPhams.value.length} chi tiết sản phẩm đã chọn?`;
  if (!confirm(confirmMessage)) {
    return; // Người dùng hủy
  }

  try {
    const promises = [];

    // Lưu từng chi tiết sản phẩm đã chọn
    for (const detailId of selectedChiTietSanPhams.value) {
      const editingDetail = editingChiTietSanPhams.value.get(detailId);
      if (editingDetail) {
        // Lấy dữ liệu gốc để có đầy đủ thông tin
        const originalDetail = originalChiTietSanPhams.value.get(detailId);

        // Chỉ cập nhật 3 trường: soLuong, giaBan, trangThai
        // Các trường khác giữ nguyên từ dữ liệu gốc
        const chiTietSanPhamData = {
          id: editingDetail.id,
          idSanPham: parseInt(route.params.id),
          idMauSac: originalDetail?.idMauSac || 1,
          idKichThuoc: originalDetail?.idKichThuoc || 1,
          idChatLieu: originalDetail?.idChatLieu || 1,
          idDeGiay: originalDetail?.idDeGiay || 1,
          idTrongLuong: originalDetail?.idTrongLuong || 1,
          idDotGiamGia: originalDetail?.idDotGiamGia,
          soLuong: editingDetail.soLuong || originalDetail?.soLuong || 0,
          giaBan: editingDetail.giaBan || originalDetail?.giaBan || 0,
          trangThai:
            editingDetail.trangThai !== undefined
              ? editingDetail.trangThai
              : originalDetail?.trangThai !== undefined
              ? originalDetail?.trangThai
              : true,
          deleted: false,
          createdAt: originalDetail?.createdAt || new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        };
        // Tìm chi tiết đợt giảm giá
        const chiTietDotGiamGia = chiTietDotGiamGias.value.find(
          (chiTietDotGiamGia) =>
            chiTietDotGiamGia.idChiTietSanPham === editingDetail.id &&
            chiTietDotGiamGia.idDotGiamGia === editingDetail.idDotGiamGia
        );

        if (!chiTietDotGiamGia) {
          continue; // Bỏ qua chi tiết sản phẩm này
        }

        // Tìm nhà sản xuất
        const nhaSanXuat = nhaSanXuats.value.find(
          (nhaSanXuat) =>
            nhaSanXuat.tenNhaSanXuat ===
            currentEditingDetail.value.tenNhaSanXuat
        );

        if (!nhaSanXuat) {
          alert("Không tìm thấy nhà sản xuất!");
          return;
        }

        // Tìm xuất xứ
        const xuatXu = xuatXus.value.find(
          (xuatXu) => xuatXu.tenXuatXu === currentEditingDetail.value.tenXuatXu
        );

        if (!xuatXu) {
          alert("Không tìm thấy xuất xứ!");
          return;
        }

        // Chỉ cập nhật dotGiamGiaData nếu có đợt giảm giá
        let dotGiamGiaData = null;
        if (dotGiamGia && chiTietDotGiamGia) {
          dotGiamGiaData = {
            id: chiTietDotGiamGia.id,
            idDotGiamGia: dotGiamGia.id,
            idChiTietSanPham: currentEditingDetail.value.id,
            trangThai: currentEditingDetail.value.trangThai,
            deleted: false,
            createdAt: currentEditingDetail.value.createdAt,
            updatedAt: new Date().toISOString(),
          };
        }

        const sanPhamData = {
          id: parseInt(route.params.id),
          idNhaSanXuat: nhaSanXuat.id,
          idXuatXu: xuatXu.id,
          tenSanPham: currentProductName.value,
          trangThai: currentEditingDetail.value.trangThai,
          deleted: false,
          createdAt: currentEditingDetail.value.createdAt,
          updatedAt: new Date().toISOString(),
        };

        promises.push(fetchUpdateChiTietSanPham(detailId, chiTietSanPhamData));
        promises.push(
          fetchUpdateChiTietDotGiamGia(dotGiamGiaData.id, dotGiamGiaData)
        );
        promises.push(fetchUpdateSanPham(sanPhamData.id, sanPhamData));
      }
    }

    // Chờ tất cả API calls hoàn thành
    await Promise.all(promises);

    // Reset tất cả trạng thái
    editingChiTietSanPhams.value.clear();
    originalChiTietSanPhams.value.clear();
    selectedChiTietSanPhams.value = []; // Xóa toàn bộ checkbox đã tích

    // Refresh data
    await fetchChiTietSanPham(route.params.id);

    showSuccessNotificationForEdit(
      "Cập nhật tất cả chi tiết sản phẩm đã chọn thành công!"
    );
  } catch (error) {
    console.error(
      "Error saving all checked chi tiết sản phẩms from inline:",
      error
    );
    alert("Có lỗi xảy ra khi cập nhật!");
  }
};

// Checkbox selection functions
const toggleChiTietSanPhamSelection = (detailId) => {
  const index = selectedChiTietSanPhams.value.indexOf(detailId);
  if (index > -1) {
    // Bỏ chọn - xóa khỏi selectedChiTietSanPhams và tắt edit mode
    selectedChiTietSanPhams.value.splice(index, 1);
    editingChiTietSanPhams.value.delete(detailId);
    originalChiTietSanPhams.value.delete(detailId);
  } else {
    // Chọn - thêm vào selectedChiTietSanPhams và bật edit mode cho 3 trường
    selectedChiTietSanPhams.value.push(detailId);
    const detail = chiTietSanPhams.value.find((d) => d.id === detailId);
    if (detail) {
      editingChiTietSanPhams.value.set(detailId, { ...detail });
      originalChiTietSanPhams.value.set(detailId, { ...detail });
    }
  }
};

const toggleSelectAll = () => {
  if (selectedChiTietSanPhams.value.length === paginatedDetails.value.length) {
    // Bỏ chọn tất cả
    selectedChiTietSanPhams.value = [];
    editingChiTietSanPhams.value.clear();
    originalChiTietSanPhams.value.clear();
  } else {
    // Chọn tất cả
    selectedChiTietSanPhams.value = paginatedDetails.value.map(
      (detail) => detail.id
    );
    // Đưa tất cả chi tiết sản phẩm vào chế độ edit
    paginatedDetails.value.forEach((detail) => {
      editingChiTietSanPhams.value.set(detail.id, { ...detail });
      originalChiTietSanPhams.value.set(detail.id, { ...detail });
    });
  }
};

// Hàm load ảnh cho chi tiết sản phẩm
const loadImagesForChiTietSanPham = async (chiTietSanPhamId) => {
  try {
    // Lấy tất cả liên kết ảnh của chi tiết sản phẩm này
    const chiTietAnhSanPhams = chiTietSanPhamAnhs.value.filter(
      (item) => item.idChiTietSanPham === chiTietSanPhamId && !item.deleted
    );

    // Lấy thông tin ảnh thật
    const images = [];
    for (const chiTietAnh of chiTietAnhSanPhams) {
      try {
        const anhSanPham = anhSanPhams.value.find(
          (anh) => anh.id === chiTietAnh.idAnhSanPham
        );
        if (anhSanPham && !anhSanPham.deleted) {
          images.push({
            id: anhSanPham.id,
            duongDanAnh: anhSanPham.duongDanAnh,
            loaiAnh: anhSanPham.loaiAnh,
            trangThai: anhSanPham.trangThai,
            deleted: anhSanPham.deleted,
            createAt: anhSanPham.createAt,
            createBy: 1,
            updateAt: new Date().toISOString(),
            updateBy: 1,
          });
        }
      } catch (error) {
        console.error(`Error loading image ${chiTietAnh.idAnhSanPham}:`, error);
      }
    }

    // Cập nhật ảnh trong currentEditingDetail
    if (
      currentEditingDetail.value &&
      currentEditingDetail.value.id === chiTietSanPhamId
    ) {
      currentEditingDetail.value.images = images;
    }

    return images;
  } catch (error) {
    console.error("Error loading images for chi tiet san pham:", error);
    return [];
  }
};
</script>

<style scoped>
/* CSS đã được chuyển vào productsUnified.css */
@import "../../../styles/cssSanPham/productsUnified.css";
@import "../../../styles/cssSanPham/quanLySanPham.css";
@import "../../../styles/cssSanPham/productManagement.css";
@import "../../../styles/cssSanPham/popupAttributes.css";

/* CSS cho chức năng chọn ảnh edit */
.image-selector-section {
  margin-top: 10px;
}

.image-actions {
  display: flex;
  flex-direction: row;
  gap: 10px;
  margin-bottom: 10px;
  align-items: center;
}

.upload-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

.file-input {
  display: none;
}

.upload-section .btn {
  flex: 1;
  max-width: 150px;
}

.current-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 10px;
}

.image-item {
  position: relative;
  display: inline-block;
}

.thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 2px solid #e5e7eb;
}

.remove-image-btn {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #ef4444;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
}

.image-limit-info {
  font-size: 12px;
  color: #6b7280;
  margin-left: 10px;
}

.image-selector-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.image-selector-popup {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
}

.image-selector-header {
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.image-selector-content {
  padding: 20px;
}

.selected-images-preview {
  margin-bottom: 20px;
}

.selected-images-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.selected-image-item {
  position: relative;
  display: inline-block;
}

.preview-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  border: 2px solid #10b981;
}

.remove-selected-btn {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #ef4444;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
}

.available-images-section h4 {
  margin-bottom: 10px;
}

.available-images-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 10px;
  max-height: 300px;
  overflow-y: auto;
}

.available-image-item {
  position: relative;
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
  transition: transform 0.2s;
}

.available-image-item.selected {
  border: 3px solid #10b981;
}

.available-image {
  width: 100%;
  height: 100px;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.available-image-item.selected .image-overlay {
  opacity: 1;
}

.selected-indicator {
  color: white;
  font-size: 24px;
  font-weight: bold;
}

.image-selector-actions {
  padding: 20px;
  border-top: 1px solid #e5e7eb;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* CSS cho popup thông báo thành công */
.success-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.success-popup {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);
  max-width: 400px;
  width: 90%;
  overflow: hidden;
  animation: slideIn 0.3s ease-out;
}

.success-popup-content {
  padding: 30px;
  text-align: center;
}

.success-title {
  color: #059669;
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 15px 0;
}

.success-message {
  color: #374151;
  font-size: 16px;
  margin: 0 0 25px 0;
  line-height: 1.5;
}

.success-btn {
  background: #059669;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.success-btn:hover {
  background: #047857;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* CSS để căn giữa dòng 2 của filter - hiển thị 3 cột ngang hàng */
.filter-row-2 {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 2rem;
  width: 100%;
  margin-top: 1.5rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.filter-row-2 .filter-group {
  margin: 0;
  width: 200px;
  text-align: center;
}

.filter-row-2 .filter-actions {
  margin: 0;
  width: 200px;
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-row-2 .filter-label {
  display: block;
  margin-bottom: 0.75rem;
  font-weight: 600;
  color: #1f2937;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.filter-row-2 .form-select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: white;
  font-size: 14px;
  transition: border-color 0.2s ease;
}

.filter-row-2 .form-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.filter-row-2 .btn {
  width: 100%;
  padding: 0.75rem 1rem;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
}

/* CSS cho popup confirm đã được chuyển vào productsUnified.css */

.custom-confirm-dialog h3 {
  margin: 0 0 1.5rem 0;
  color: #22c55e;
  font-size: 1.5rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.custom-confirm-dialog h3::before {
  content: "⚠️";
  font-size: 1.75rem;
}

.custom-confirm-dialog p {
  margin: 0 0 2rem 0;
  color: #475569;
  line-height: 1.6;
  font-size: 1rem;
  background: rgba(59, 130, 246, 0.05);
  padding: 1rem;
  border-radius: 12px;
  border-left: 4px solid #3b82f6;
}

.custom-confirm-dialog .confirm-buttons {
  display: flex !important;
  gap: 1rem !important;
  justify-content: center !important;
  flex-wrap: nowrap !important;
}

.custom-confirm-dialog .btn {
  padding: 0.875rem 2rem !important;
  border-radius: 12px !important;
  cursor: pointer !important;
  font-weight: 600 !important;
  border: none !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  font-size: 0.95rem !important;
  min-width: 120px !important;
  position: relative !important;
  overflow: hidden !important;
}

.custom-confirm-dialog .btn::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transition: left 0.5s;
}

.custom-confirm-dialog .btn:hover::before {
  left: 100%;
}

.custom-confirm-dialog .btn-primary {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
}

.custom-confirm-dialog .btn-primary:hover {
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.5);
}

.custom-confirm-dialog .btn-secondary {
  background: linear-gradient(135deg, #64748b 0%, #475569 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(100, 116, 139, 0.4);
}

.custom-confirm-dialog .btn-secondary:hover {
  background: linear-gradient(135deg, #475569 0%, #334155 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(100, 116, 139, 0.5);
}

/* Animations */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* Responsive design */
@media (max-width: 480px) {
  .custom-confirm-dialog .confirm-content {
    padding: 2rem 1.5rem;
    margin: 1rem;
    border-radius: 16px;
  }

  .custom-confirm-dialog h3 {
    font-size: 1.25rem;
  }

  .custom-confirm-dialog p {
    font-size: 0.9rem;
    padding: 0.875rem;
  }

  .custom-confirm-dialog .btn {
    padding: 0.75rem 1.5rem;
    min-width: 100px;
    font-size: 0.9rem;
  }

  .custom-confirm-dialog .confirm-buttons {
    flex-direction: row !important;
    gap: 0.75rem !important;
  }
}

/* CSS bổ sung để đảm bảo popup luôn hiển thị ở giữa trang web */
.custom-confirm-dialog {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100vw !important;
  height: 100vh !important;
  z-index: 999999 !important;
  display: flex !important;
  justify-content: center !important;
  align-items: center !important;
  background: rgba(0, 0, 0, 0.6) !important;
  pointer-events: auto !important;
}

.custom-confirm-dialog .confirm-content {
  position: relative !important;
  z-index: 1000000 !important;
  transform: translateZ(0) !important;
  will-change: transform !important;
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%) !important;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.1) !important;
}

/* Đảm bảo không có element nào che khuất popup */
.custom-confirm-dialog * {
  position: relative !important;
  z-index: inherit !important;
}

/* Đảm bảo popup luôn ở trên cùng */
body {
  overflow: hidden !important;
}

.custom-confirm-dialog {
  overflow: visible !important;
}

/* Đảm bảo popup luôn ở giữa màn hình */
.custom-confirm-dialog {
  position: fixed !important;
  top: 50% !important;
  left: 50% !important;
  transform: translate(-50%, -50%) !important;
  width: 100vw !important;
  height: 100vh !important;
}

.custom-confirm-dialog .confirm-content {
  position: absolute !important;
  top: 50% !important;
  left: 50% !important;
  transform: translate(-50%, -50%) !important;
  z-index: 1000000 !important;
}

/* CSS cho các nút cơ bản */
.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  transition: all 0.2s ease;
  line-height: 1.4;
}

.btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
  margin: 0 2px;
}

.btn-success {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 2px 4px rgba(16, 185, 129, 0.3);
}

.btn-success:hover {
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
  box-shadow: 0 4px 8px rgba(16, 185, 129, 0.4);
}

.btn-danger {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
  box-shadow: 0 2px 4px rgba(239, 68, 68, 0.3);
}

.btn-danger:hover {
  background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%);
  box-shadow: 0 4px 8px rgba(239, 68, 68, 0.4);
}

.btn-secondary {
  background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
  color: white;
  box-shadow: 0 2px 4px rgba(107, 114, 128, 0.3);
}

.btn-secondary:hover {
  background: linear-gradient(135deg, #4b5563 0%, #374151 100%);
  box-shadow: 0 4px 8px rgba(107, 114, 128, 0.4);
}

.btn-outline {
  background: transparent;
  border: 1px solid #d1d5db;
  color: #6b7280;
}

.btn-outline:hover {
  background: #f9fafb;
  border-color: #9ca3af;
  color: #374151;
}

/* CSS cho action buttons trong inline edit */
.action-buttons {
  display: flex;
  gap: 4px;
  align-items: center;
  justify-content: center;
}

.action-buttons .btn {
  min-width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
