<template>
  <!-- Breadcrumb Navigation -->
  <div class="breadcrumb-nav">
    <RouterLink :to="`/products`" class="product-link">
      <span class="product-text">Sản phẩm</span>
    </RouterLink>
    <span class="breadcrumb-separator"> / </span>
    <router-link to="/products/add" class="product-link">
      <span class="product-text">Thêm sản phẩm</span>
    </router-link>
  </div>
  <div class="them-san-pham-page">
    <!-- Form Container -->
    <div class="form-container">
      <div class="form-card">
        <!-- Basic Information Section -->
        <div class="form-section">
          <div class="section-header">
            <h3>Thông tin cơ bản</h3>
          </div>
          <div class="section-content">
            <div class="product-form-grid">
              <div class="product-form-field">
                <label class="modern-label">Tên sản phẩm *</label>
                <input
                  type="text"
                  v-model="productForm.tenSanPham"
                  class="modern-input"
                  placeholder="Nhập tên sản phẩm"
                  required
                />
              </div>

              <div class="product-form-field">
                <label class="modern-label">Nhà sản xuất *</label>
                <div class="input-with-dropdown">
                  <input
                    type="text"
                    v-model="productForm.tenNhaSanXuat"
                    class="modern-input"
                    placeholder="Nhập hoặc chọn nhà sản xuất"
                    @input="filterNhaSanXuat"
                    @focus="showNhaSanXuatDropdown = true"
                  />
                  <div v-if="showNhaSanXuatDropdown" class="dropdown-list">
                    <div
                      v-for="nhaSanXuat in filteredNhaSanXuats"
                      :key="nhaSanXuat.id"
                      class="dropdown-item"
                      @click="selectNhaSanXuat(nhaSanXuat)"
                    >
                      {{ nhaSanXuat.tenNhaSanXuat }}
                    </div>
                    <div
                      v-if="
                        productForm.tenNhaSanXuat &&
                        !filteredNhaSanXuats.find(
                          (item) =>
                            item.tenNhaSanXuat.toLowerCase() ===
                            productForm.tenNhaSanXuat.toLowerCase()
                        )
                      "
                      class="dropdown-item create-new"
                      @click="createNewNhaSanXuat"
                    >
                      Thêm mới "{{ productForm.tenNhaSanXuat }}"
                    </div>
                  </div>
                </div>
              </div>

              <div class="product-form-field">
                <label class="modern-label">Xuất xứ *</label>
                <div class="input-with-dropdown">
                  <input
                    type="text"
                    v-model="productForm.tenXuatXu"
                    class="modern-input"
                    placeholder="Nhập hoặc chọn xuất xứ"
                    @input="filterXuatXu"
                    @focus="showXuatXuDropdown = true"
                  />
                  <div v-if="showXuatXuDropdown" class="dropdown-list">
                    <div
                      v-for="xuatXu in filteredXuatXus"
                      :key="xuatXu.id"
                      class="dropdown-item"
                      @click="selectXuatXu(xuatXu)"
                    >
                      {{ xuatXu.tenXuatXu }}
                    </div>
                    <div
                      v-if="
                        productForm.tenXuatXu &&
                        !filteredXuatXus.find(
                          (item) =>
                            item.tenXuatXu.toLowerCase() ===
                            productForm.tenXuatXu.toLowerCase()
                        )
                      "
                      class="dropdown-item create-new"
                      @click="createNewXuatXu"
                    >
                      Thêm mới "{{ productForm.tenXuatXu }}"
                    </div>
                  </div>
                </div>
              </div>

              <div class="product-form-field">
                <label class="modern-label">Chất liệu *</label>
                <div class="input-with-dropdown">
                  <input
                    type="text"
                    v-model="productForm.tenChatLieu"
                    class="modern-input"
                    placeholder="Nhập hoặc chọn chất liệu"
                    @input="filterChatLieu"
                    @focus="showChatLieuDropdown = true"
                  />
                  <div v-if="showChatLieuDropdown" class="dropdown-list">
                    <div
                      v-for="chatLieu in filteredChatLieus"
                      :key="chatLieu.id"
                      class="dropdown-item"
                      @click="selectChatLieu(chatLieu)"
                    >
                      {{ chatLieu.tenChatLieu }}
                    </div>
                    <div
                      v-if="
                        productForm.tenChatLieu &&
                        !filteredChatLieus.find(
                          (item) =>
                            item.tenChatLieu.toLowerCase() ===
                            productForm.tenChatLieu.toLowerCase()
                        )
                      "
                      class="dropdown-item create-new"
                      @click="createNewChatLieu"
                    >
                      Thêm mới "{{ productForm.tenChatLieu }}"
                    </div>
                  </div>
                </div>
              </div>

              <div class="product-form-field">
                <label class="modern-label">Đế giày *</label>
                <div class="input-with-dropdown">
                  <input
                    type="text"
                    v-model="productForm.tenDeGiay"
                    class="modern-input"
                    placeholder="Nhập hoặc chọn đế giày"
                    @input="filterDeGiay"
                    @focus="showDeGiayDropdown = true"
                  />
                  <div v-if="showDeGiayDropdown" class="dropdown-list">
                    <div
                      v-for="deGiay in filteredDeGiays"
                      :key="deGiay.id"
                      class="dropdown-item"
                      @click="selectDeGiay(deGiay)"
                    >
                      {{ deGiay.tenDeGiay }}
                    </div>
                    <div
                      v-if="
                        productForm.tenDeGiay &&
                        !filteredDeGiays.find(
                          (item) =>
                            item.tenDeGiay.toLowerCase() ===
                            productForm.tenDeGiay.toLowerCase()
                        )
                      "
                      class="dropdown-item create-new"
                      @click="createNewDeGiay"
                    >
                      Thêm mới "{{ productForm.tenDeGiay }}"
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Product Attributes Section -->
        <div class="form-section">
          <div class="section-header">
            <h3>Thuộc tính sản phẩm</h3>
          </div>
          <div class="section-content">
            <div class="product-form-grid">
              <div class="product-form-field">
                <label class="modern-label">Màu sắc: *</label>
                <span class="attribute-selector">
                  <button
                    @click="showMauSacPopup = true"
                    class="add-attribute-btn"
                    type="button"
                  >
                    <span class="add-icon">+</span>
                    Thêm màu sắc
                  </button>
                  <span class="selected-attributes">
                    <span
                      v-for="(mauSac, index) in selectedMauSacs"
                      :key="index"
                      class="selected-attribute-tag"
                    >
                      {{ mauSac.tenMauSac }}
                      <button
                        @click="removeMauSac(index)"
                        class="remove-attribute-btn"
                      >
                        ×
                      </button>
                    </span>
                  </span>
                </span>
              </div>

              <div class="product-form-field">
                <label class="modern-label">Kích thước: *</label>
                <span class="attribute-selector">
                  <button
                    @click="showKichThuocPopup = true"
                    class="add-attribute-btn"
                    type="button"
                  >
                    <span class="add-icon">+</span>
                    Thêm kích thước
                  </button>
                  <span class="selected-attributes">
                    <span
                      v-for="(kichThuoc, index) in selectedKichThuocs"
                      :key="index"
                      class="selected-attribute-tag"
                    >
                      {{ kichThuoc.tenKichThuoc }}
                      <button
                        @click="removeKichThuoc(index)"
                        class="remove-attribute-btn"
                      >
                        ×
                      </button>
                    </span>
                  </span>
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Product Variants Section -->
        <div class="form-section">
          <div class="section-header">
            <h3>Biến thể sản phẩm</h3>
          </div>
          <div class="section-content">
            <div
              v-for="(mauSac, mauIndex) in selectedMauSacs"
              :key="mauIndex"
              class="variant-table-container"
            >
              <div class="variant-header">
                <h4 class="variant-table-title">
                  Biến thể sản phẩm - {{ mauSac.tenMauSac }}
                </h4>
                <button
                  v-if="getSelectedVariantsCount(mauIndex) >= 2"
                  @click="showQuickEditPopup(mauIndex)"
                  class="btn-outline"
                  type="button"
                >
                  Sửa nhanh
                </button>
              </div>
              <div class="variant-table">
                <table>
                  <thead>
                    <tr>
                      <th width="50">
                        <input
                          type="checkbox"
                          :checked="isAllVariantsSelected(mauIndex)"
                          @change="toggleAllVariants(mauIndex, $event)"
                        />
                      </th>
                      <th width="10%">Tên sản phẩm</th>
                      <th width="10%">Kích thước</th>
                      <th width="10%">Trọng lượng</th>
                      <th width="10%">Số lượng</th>
                      <th width="15%">Giá bán</th>
                      <th width="35%">Ảnh (0/5)</th>
                      <th width="10%">Thao tác</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr
                      v-for="(kichThuoc, kichIndex) in selectedKichThuocs"
                      :key="kichIndex"
                    >
                      <td>
                        <input
                          type="checkbox"
                          :checked="isVariantSelected(mauIndex, kichIndex)"
                          @change="toggleVariant(mauIndex, kichIndex, $event)"
                        />
                      </td>
                      <td>
                        <input
                          v-model="
                            productVariants[mauIndex][kichIndex].tenSanPham
                          "
                          type="text"
                          class="variant-input-s"
                          placeholder="Nhập tên sản phẩm"
                          readonly
                        />
                      </td>
                      <td>
                        <input
                          v-model="
                            productVariants[mauIndex][kichIndex].kichThuoc
                          "
                          type="text"
                          class="variant-input-s"
                          placeholder="Nhập kích thước"
                          readonly
                        />
                      </td>
                      <td>
                        <input
                          v-model="
                            productVariants[mauIndex][kichIndex].trongLuong
                          "
                          type="text"
                          class="variant-input"
                          placeholder="0"
                        />
                      </td>
                      <td>
                        <input
                          v-model="productVariants[mauIndex][kichIndex].soLuong"
                          type="number"
                          min="0"
                          class="variant-input"
                          placeholder="0"
                        />
                      </td>
                      <td>
                        <input
                          v-model="productVariants[mauIndex][kichIndex].giaBan"
                          type="number"
                          min="0"
                          step="1000"
                          class="variant-input"
                          placeholder="0"
                        />
                      </td>
                      <td>
                        <div class="variant-image-section">
                          <div class="image-preview-grid">
                            <div
                              v-for="(anh, anhIndex) in productVariants[
                                mauIndex
                              ][kichIndex].anh"
                              :key="anhIndex"
                              class="image-preview-item"
                            >
                              <img
                                :src="getAnhUrl(anh)"
                                alt="Ảnh sản phẩm"
                                class="variant-thumbnail"
                              />
                              <button
                                @click="
                                  removeVariantImage(
                                    mauIndex,
                                    kichIndex,
                                    anhIndex
                                  )
                                "
                                class="remove-image-btn"
                                type="button"
                              >
                                ×
                              </button>
                            </div>
                            <button
                              v-if="
                                productVariants[mauIndex][kichIndex].anh
                                  .length < 5
                              "
                              @click="selectVariantImage(mauIndex, kichIndex)"
                              class="add-image-btn"
                              type="button"
                            >
                              <span class="add-icon">+</span>
                              Thêm ảnh
                            </button>
                          </div>
                        </div>
                      </td>
                      <td>
                        <button
                          @click="removeVariant(mauIndex, kichIndex)"
                          class="remove-variant-btn"
                          type="button"
                        >
                          Xóa
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        <!-- Form Actions -->
        <div class="form-actions">
          <button type="button" class="btn-outline" @click="saveProduct">
            Tạo sản phẩm
          </button>
        </div>
      </div>
    </div>

    <!-- Success Notification -->
    <div
      v-if="showSuccessModal"
      class="success-modal-overlay"
      @click="closeSuccessModal"
    >
      <div class="success-modal-content" @click.stop>
        <div class="success-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="currentColor">
            <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
          </svg>
        </div>
        <div class="success-content">
          <h2 class="success-title">Thành công!</h2>
          <p class="success-message">{{ successMessage }}</p>
        </div>
        <div class="success-actions">
          <button class="success-close-btn" @click="closeSuccessModal">
            Đóng
          </button>
          <router-link to="/products" class="btn-primary">
            Xem danh sách
          </router-link>
        </div>
      </div>
    </div>

    <!-- Popup chọn màu sắc -->
    <div
      v-if="showMauSacPopup"
      class="attribute-popup-overlay"
      @click="closeMauSacPopup"
    >
      <div class="attribute-popup-content" @click.stop>
        <div class="attribute-popup-header">
          <h3>Chọn màu sắc</h3>
          <button @click="closeMauSacPopup" class="attribute-close-btn">
            ×
          </button>
        </div>
        <div class="attribute-popup-body">
          <div class="attribute-search-section">
            <input
              v-model="mauSacSearch"
              type="text"
              placeholder="Tìm kiếm màu sắc..."
              class="attribute-search-input"
            />
          </div>
          <div class="attribute-existing-attributes">
            <h4>Màu sắc có sẵn</h4>
            <div class="attribute-list">
              <div
                v-for="mauSac in filteredMauSacsForPopup"
                :key="mauSac.id"
                class="attribute-item"
                :class="{ selected: isMauSacSelected(mauSac) }"
                @click="selectMauSacFromPopup(mauSac)"
              >
                <span
                  v-if="isMauSacSelected(mauSac)"
                  class="attribute-checkmark"
                  >✓</span
                >
                {{ mauSac.tenMauSac }}
              </div>
            </div>
          </div>
          <div class="attribute-create-new-section">
            <h4>Tạo màu sắc mới</h4>
            <div class="attribute-create-new-form">
              <input
                v-model="newMauSacName"
                type="text"
                placeholder="Nhập tên màu sắc mới"
                class="attribute-create-new-input"
              />
              <button
                @click="createNewMauSacFromPopup"
                class="attribute-create-new-btn"
                type="button"
              >
                Tạo mới
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Popup chọn kích thước -->
    <div
      v-if="showKichThuocPopup"
      class="attribute-popup-overlay"
      @click="closeKichThuocPopup"
    >
      <div class="attribute-popup-content" @click.stop>
        <div class="attribute-popup-header">
          <h3>Chọn kích thước</h3>
          <button @click="closeKichThuocPopup" class="attribute-close-btn">
            ×
          </button>
        </div>
        <div class="attribute-popup-body">
          <div class="attribute-search-section">
            <input
              v-model="kichThuocSearch"
              type="text"
              placeholder="Tìm kiếm kích thước..."
              class="attribute-search-input"
            />
          </div>
          <div class="attribute-existing-attributes">
            <h4>Kích thước có sẵn</h4>
            <div class="attribute-list">
              <div
                v-for="kichThuoc in filteredKichThuocsForPopup"
                :key="kichThuoc.id"
                class="attribute-item"
                :class="{ selected: isKichThuocSelected(kichThuoc) }"
                @click="selectKichThuocFromPopup(kichThuoc)"
              >
                <span
                  v-if="isKichThuocSelected(kichThuoc)"
                  class="attribute-checkmark"
                  >✓</span
                >
                {{ kichThuoc.tenKichThuoc }}
              </div>
            </div>
          </div>
          <div class="attribute-create-new-section">
            <h4>Tạo kích thước mới</h4>
            <div class="attribute-create-new-form">
              <input
                v-model="newKichThuocName"
                type="text"
                placeholder="Nhập tên kích thước mới"
                class="attribute-create-new-input"
              />
              <button
                @click="createNewKichThuocFromPopup"
                class="attribute-create-new-btn"
                type="button"
              >
                Tạo mới
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Popup sửa nhanh biến thể -->
    <div
      v-if="showQuickEditModal"
      class="popup-overlay"
      @click="closeQuickEditModal"
    >
      <div class="popup-content quick-edit-popup" @click.stop>
        <div class="popup-header">
          <h3>Sửa nhanh biến thể sản phẩm</h3>
          <button @click="closeQuickEditModal" class="close-btn">×</button>
        </div>
        <div class="popup-body">
          <div class="quick-edit-form">
            <div class="form-field">
              <label class="form-label">Trọng lượng:</label>
              <input
                v-model="quickEditForm.trongLuong"
                type="text"
                class="form-input"
                placeholder="Nhập trọng lượng"
              />
            </div>
            <div class="form-field">
              <label class="form-label">Số lượng:</label>
              <input
                v-model="quickEditForm.soLuong"
                type="number"
                min="0"
                class="form-input"
                placeholder="0"
              />
            </div>
            <div class="form-field">
              <label class="form-label">Đơn giá:</label>
              <input
                v-model="quickEditForm.giaBan"
                type="number"
                min="0"
                step="1000"
                class="form-input"
                placeholder="0"
              />
            </div>
          </div>
        </div>
        <div class="popup-footer">
          <button @click="closeQuickEditModal" class="btn-outline">Đóng</button>
          <button @click="applyQuickEdit" class="btn-primary">Áp dụng</button>
        </div>
      </div>
    </div>

    <!-- Popup confirm xóa biến thể -->
    <div
      v-if="showDeleteConfirmModal"
      class="popup-overlay"
      @click="closeDeleteConfirmModal"
    >
      <div class="popup-content delete-confirm-popup" @click.stop>
        <div class="popup-header">
          <h3>Xác nhận xóa</h3>
          <button @click="closeDeleteConfirmModal" class="close-btn">×</button>
        </div>
        <div class="popup-body">
          <p class="confirm-message">
            Bạn có chắc muốn xóa biến thể sản phẩm này không?
          </p>
          <p class="variant-info">
            {{ deleteConfirmInfo }}
          </p>
        </div>
        <div class="popup-footer">
          <button @click="closeDeleteConfirmModal" class="btn-outline">
            Hủy
          </button>
          <button @click="confirmDeleteVariant" class="btn-danger">Xóa</button>
        </div>
      </div>
    </div>

    <!-- Popup chọn ảnh -->
    <div v-if="showAnhPopup" class="popup-overlay" @click="closeAnhPopup">
      <div class="popup-content anh-popup" @click.stop>
        <div class="popup-header">
          <h3>Thêm ảnh sản phẩm</h3>
          <button @click="closeAnhPopup" class="close-btn">×</button>
        </div>
        <div class="popup-body">
          <!-- Title row -->
          <div class="anh-title-row">
            <h4>Ảnh có sẵn</h4>
            <button
              @click="$refs.fileInput.click()"
              class="btn-outline"
              type="button"
            >
              <span class="upload-icon">📁</span>
              Thêm ảnh
            </button>
          </div>

          <!-- Content row -->
          <div class="anh-content-row">
            <!-- Phần ảnh từ database -->
            <div class="anh-database-section">
              <div class="anh-grid">
                <div
                  v-for="anh in filteredAnhSanPhams"
                  :key="anh.id"
                  class="anh-item"
                  :class="{ selected: isAnhSelected(anh) }"
                  @click="selectAnhFromDatabase(anh)"
                >
                  <img
                    :src="getAnhUrl(anh)"
                    :alt="getAnhName(anh)"
                    class="anh-thumbnail"
                  />
                  <div class="anh-overlay">
                    <span v-if="isAnhSelected(anh)" class="checkmark">✓</span>
                  </div>
                  <div class="anh-name">{{ getAnhName(anh) }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- Hidden file input -->
          <input
            ref="fileInput"
            type="file"
            multiple
            accept="image/*"
            @change="handleFileSelect"
            class="file-input"
          />

          <!-- Hiển thị ảnh đã chọn -->
          <div class="selected-anh-display">
            <h4>Ảnh đã chọn:</h4>
            <div class="selected-anh-grid">
              <!-- Ảnh từ database -->
              <div
                v-for="anh in selectedAnhFromDatabase"
                :key="anh.id"
                class="selected-anh-item"
              >
                <img
                  :src="getAnhUrl(anh)"
                  :alt="getAnhName(anh)"
                  class="selected-anh-thumbnail"
                />
                <span class="selected-anh-name">{{ getAnhName(anh) }}</span>
              </div>
              <!-- Ảnh từ file -->
              <div
                v-for="(file, index) in selectedAnhFromFile"
                :key="`file-${index}`"
                class="selected-anh-item"
              >
                <img
                  :src="getFileUrl(file)"
                  :alt="file.name"
                  class="selected-anh-thumbnail"
                />
                <span class="selected-anh-name">{{ file.name }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="popup-footer">
          <!-- Thông tin ảnh đã chọn -->
          <div class="selected-anh-info">
            <h4>Ảnh đã chọn ({{ getTotalSelectedAnhCount() }}/5)</h4>
            <div class="selected-anh-summary">
              <span v-if="selectedAnhFromDatabase.length > 0">
                Từ database: {{ selectedAnhFromDatabase.length }} ảnh
              </span>
              <span v-if="selectedAnhFromFile.length > 0">
                Từ máy: {{ selectedAnhFromFile.length }} ảnh
              </span>
            </div>
          </div>
          <button @click="closeAnhPopup" class="btn-outline">Đóng</button>
          <button @click="applyAnhSelection" class="btn-outline">
            Áp dụng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from "vue";
import { useRouter } from "vue-router";
import { fetchCreateChiTietSanPham } from "../../../services/SanPham/ChiTietSanPhamService";
import {
  fetchAllSanPham,
  fetchCreateSanPham,
} from "../../../services/SanPham/SanPhamService";
import {
  fetchAllAnhSanPham,
  fetchCreateAnhSanPham,
} from "../../../services/ThuocTinh/AnhSanPhamService";
import {
  fetchAllChatLieu,
  fetchCreateChatLieu,
} from "../../../services/ThuocTinh/ChatLieuService";
import { fetchCreateChiTietSanPhamAnh } from "../../../services/ThuocTinh/ChiTietSanPhamAnhService";
import {
  fetchAllDeGiay,
  fetchCreateDeGiay,
} from "../../../services/ThuocTinh/DeGiayService";
import {
  fetchAllKichThuoc,
  fetchCreateKichThuoc,
} from "../../../services/ThuocTinh/KichThuocService";
import {
  fetchAllMauSac,
  fetchCreateMauSac,
} from "../../../services/ThuocTinh/MauSacService";
import {
  fetchAllNhaSanXuat,
  fetchCreateNhaSanXuat,
} from "../../../services/ThuocTinh/NhaSanXuatService";
import {
  fetchAllTrongLuong,
  fetchCreateTrongLuong,
} from "../../../services/ThuocTinh/TrongLuongService";
import {
  fetchAllXuatXu,
  fetchCreateXuatXu,
} from "../../../services/ThuocTinh/XuatXuService";

const router = useRouter();
const showSuccessModal = ref(false);
const successMessage = ref("");
const productForm = ref({
  tenSanPham: "",
  tenNhaSanXuat: "",
  tenXuatXu: "",
  tenChatLieu: "",
  tenDeGiay: "",
  trangThai: true,
  deleted: false,
});

// Data cho các thuộc tính
const nhaSanXuats = ref([]);
const xuatXus = ref([]);
const chatLieus = ref([]);
const deGiays = ref([]);
const mauSacs = ref([]);
const kichThuocs = ref([]);
const tenSanPhams = ref([]);
const anhSanPhams = ref([]);
const trongLuongs = ref([]);

// Dropdown states
const showNhaSanXuatDropdown = ref(false);
const showXuatXuDropdown = ref(false);
const showChatLieuDropdown = ref(false);
const showDeGiayDropdown = ref(false);
const showTenSanPhamDropdown = ref(false);

// Popup states
const showMauSacPopup = ref(false);
const showKichThuocPopup = ref(false);
const showAnhPopup = ref(false);

// Selected attributes
const selectedMauSacs = ref([]);
const selectedKichThuocs = ref([]);

// Data cho ảnh sản phẩm
const currentEditingVariant = ref({ mauIndex: -1, kichIndex: -1 });
const selectedAnhFromDatabase = ref([]);
const selectedAnhFromFile = ref([]);

// Search for popups
const mauSacSearch = ref("");
const kichThuocSearch = ref("");
const anhSearch = ref("");

// Input values for attributes
const mauSacInput = ref("");
const kichThuocInput = ref("");

// New attribute names
const newMauSacName = ref("");
const newKichThuocName = ref("");
const newAnhName = ref("");

// Functions để xóa thuộc tính
const removeMauSac = (index) => {
  selectedMauSacs.value.splice(index, 1);
};

const removeKichThuoc = (index) => {
  selectedKichThuocs.value.splice(index, 1);
};

// Functions để kiểm tra thuộc tính đã chọn
const isMauSacSelected = (mauSac) => {
  return selectedMauSacs.value.some((item) => item.id === mauSac.id);
};

const isKichThuocSelected = (kichThuoc) => {
  return selectedKichThuocs.value.some((item) => item.id === kichThuoc.id);
};

// Function kiểm tra ảnh đã chọn
const isAnhSelected = (anh) => {
  return selectedAnhFromDatabase.value.some((item) => item.id === anh.id);
};

// Function chọn ảnh từ database (alias cho toggleAnhFromDatabase)
const selectAnhFromDatabase = (anh) => {
  toggleAnhFromDatabase(anh);
};

// Product variants
const productVariants = ref([]);
const editingVariants = ref(new Set());

const resetForm = () => {
  productForm.value = {
    tenSanPham: "",
    tenNhaSanXuat: "",
    tenXuatXu: "",
    tenChatLieu: "",
    tenDeGiay: "",
    trangThai: true,
    deleted: false,
  };

  // Reset dropdown states
  showTenSanPhamDropdown.value = false;
  showNhaSanXuatDropdown.value = false;
  showXuatXuDropdown.value = false;
  showChatLieuDropdown.value = false;
  showDeGiayDropdown.value = false;

  selectedMauSacs.value = [];
  selectedKichThuocs.value = [];
  productVariants.value = [];
  quickEditState.value.clear();

  // Reset quick edit modal
  showQuickEditModal.value = false;
  currentEditingMauIndex.value = null;
  quickEditForm.value = {
    trongLuong: "",
    soLuong: 0,
    giaBan: 0,
  };

  mauSacSearch.value = "";
  kichThuocSearch.value = "";
  anhSearch.value = "";
  newMauSacName.value = "";
  newKichThuocName.value = "";
  newAnhName.value = "";
};

// Computed properties cho việc filter
const filteredTenSanPhams = computed(() => {
  if (!productForm.value.tenSanPham) return tenSanPhams.value;
  return tenSanPhams.value.filter((item) =>
    item.tenSanPham
      .toLowerCase()
      .includes(productForm.value.tenSanPham.toLowerCase())
  );
});

const filteredNhaSanXuats = computed(() => {
  if (!productForm.value.tenNhaSanXuat) return nhaSanXuats.value;
  return nhaSanXuats.value.filter((item) =>
    item.tenNhaSanXuat
      .toLowerCase()
      .includes(productForm.value.tenNhaSanXuat.toLowerCase())
  );
});

const filteredXuatXus = computed(() => {
  if (!productForm.value.tenXuatXu) return xuatXus.value;
  return xuatXus.value.filter((item) =>
    item.tenXuatXu
      .toLowerCase()
      .includes(productForm.value.tenXuatXu.toLowerCase())
  );
});

const filteredChatLieus = computed(() => {
  if (!productForm.value.tenChatLieu) return chatLieus.value;
  return chatLieus.value.filter((item) =>
    item.tenChatLieu
      .toLowerCase()
      .includes(productForm.value.tenChatLieu.toLowerCase())
  );
});

const filteredDeGiays = computed(() => {
  if (!productForm.value.tenDeGiay) return deGiays.value;
  return deGiays.value.filter((item) =>
    item.tenDeGiay
      .toLowerCase()
      .includes(productForm.value.tenDeGiay.toLowerCase())
  );
});

// Computed properties cho popup
const filteredMauSacsForPopup = computed(() => {
  if (!mauSacSearch.value) return mauSacs.value;
  return mauSacs.value.filter((item) =>
    item.tenMauSac.toLowerCase().includes(mauSacSearch.value.toLowerCase())
  );
});

const filteredKichThuocsForPopup = computed(() => {
  if (!kichThuocSearch.value) return kichThuocs.value;
  return kichThuocs.value.filter((item) =>
    item.tenKichThuoc
      .toLowerCase()
      .includes(kichThuocSearch.value.toLowerCase())
  );
});

const filteredAnhSanPhams = computed(() => {
  if (!anhSearch.value) return anhSanPhams.value;
  return anhSanPhams.value.filter((item) =>
    item.tenAnh.toLowerCase().includes(anhSearch.value.toLowerCase())
  );
});

// Fetch data cho các thuộc tính
const fetchTenSanPham = async () => {
  try {
    const response = await fetchAllSanPham();
    tenSanPhams.value = response.data || [];
  } catch (error) {
    console.error("Error fetching ten san pham:", error);
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

const fetchChatLieu = async () => {
  try {
    const response = await fetchAllChatLieu();
    chatLieus.value = response.data || [];
  } catch (error) {
    console.error("Error fetching chat lieu:", error);
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

const fetchTrongLuong = async () => {
  try {
    const response = await fetchAllTrongLuong();
    trongLuongs.value = response.data || [];
  } catch (error) {
    console.error("Error fetching trong luong:", error);
  }
};

const fetchAnhSanPham = async () => {
  try {
    const response = await fetchAllAnhSanPham();
    anhSanPhams.value = response.data || [];
    console.log("📸 Dữ liệu ảnh từ API:", response);
    console.log("📸 Danh sách ảnh đã load:", anhSanPhams.value);
    if (anhSanPhams.value.length > 0) {
      console.log("📸 Ảnh đầu tiên:", anhSanPhams.value[0]);
      console.log("📸 URL ảnh đầu tiên:", getAnhUrl(anhSanPhams.value[0]));
    }
  } catch (error) {
    console.error("Error fetching anh san pham:", error);
  }
};

// Initialize product variants when attributes change
const initializeProductVariants = () => {
  productVariants.value = [];

  for (let i = 0; i < selectedMauSacs.value.length; i++) {
    productVariants.value[i] = [];
    for (let j = 0; j < selectedKichThuocs.value.length; j++) {
      productVariants.value[i][j] = {
        tenSanPham: productForm.value.tenSanPham,
        kichThuoc: selectedKichThuocs.value[j].tenKichThuoc,
        trongLuong: "",
        soLuong: 0,
        giaBan: 0,
        anh: [],
        trangThai: true,
        deleted: false,
      };
    }
  }
};

// Watch for changes in selected attributes
watch(
  [selectedMauSacs, selectedKichThuocs],
  () => {
    initializeProductVariants();
  },
  { deep: true }
);

// Filter functions
const filterTenSanPham = () => {
  showTenSanPhamDropdown.value = true;
};

const filterNhaSanXuat = () => {
  showNhaSanXuatDropdown.value = true;
};

const filterXuatXu = () => {
  showXuatXuDropdown.value = true;
};

const filterChatLieu = () => {
  showChatLieuDropdown.value = true;
};

const filterDeGiay = () => {
  showDeGiayDropdown.value = true;
};

// Select functions
const selectTenSanPham = (tenSanPham) => {
  productForm.value.tenSanPham = tenSanPham.tenSanPham;
  showTenSanPhamDropdown.value = false;
};

const selectNhaSanXuat = (nhaSanXuat) => {
  productForm.value.tenNhaSanXuat = nhaSanXuat.tenNhaSanXuat;
  showNhaSanXuatDropdown.value = false;
};

const selectXuatXu = (xuatXu) => {
  productForm.value.tenXuatXu = xuatXu.tenXuatXu;
  showXuatXuDropdown.value = false;
};

const selectChatLieu = (chatLieu) => {
  productForm.value.tenChatLieu = chatLieu.tenChatLieu;
  showChatLieuDropdown.value = false;
};

const selectDeGiay = (deGiay) => {
  productForm.value.tenDeGiay = deGiay.tenDeGiay;
  showDeGiayDropdown.value = false;
};

// Create new functions
const createNewTenSanPham = async () => {
  try {
    const newTenSanPham = {
      tenSanPham: productForm.value.tenSanPham,
      trangThai: true,
      deleted: false,
    };
    await fetchCreateSanPham(newTenSanPham);
    await fetchTenSanPham();
    showTenSanPhamDropdown.value = false;
  } catch (error) {
    console.error("Error creating ten san pham:", error);
    alert("Có lỗi xảy ra khi tạo tên sản phẩm!");
  }
};

const createNewNhaSanXuat = async () => {
  try {
    const newNhaSanXuat = {
      tenNhaSanXuat: productForm.value.tenNhaSanXuat,
      trangThai: true,
      deleted: false,
    };
    await fetchCreateNhaSanXuat(newNhaSanXuat);
    await fetchNhaSanXuat();
    showNhaSanXuatDropdown.value = false;
  } catch (error) {
    console.error("Error creating nha san xuat:", error);
    alert("Có lỗi xảy ra khi tạo nhà sản xuất!");
  }
};

const createNewXuatXu = async () => {
  try {
    const newXuatXu = {
      tenXuatXu: productForm.value.tenXuatXu,
      trangThai: true,
      deleted: false,
    };
    await fetchCreateXuatXu(newXuatXu);
    await fetchXuatXu();
    showXuatXuDropdown.value = false;
  } catch (error) {
    console.error("Error creating xuat xu:", error);
    alert("Có lỗi xảy ra khi tạo xuất xứ!");
  }
};

const createNewChatLieu = async () => {
  try {
    const newChatLieu = {
      tenChatLieu: productForm.value.tenChatLieu,
      trangThai: true,
      deleted: false,
    };
    await fetchCreateChatLieu(newChatLieu);
    await fetchChatLieu();
    showChatLieuDropdown.value = false;
  } catch (error) {
    console.error("Error creating chat lieu:", error);
    alert("Có lỗi xảy ra khi tạo chất liệu!");
  }
};

const createNewDeGiay = async () => {
  try {
    const newDeGiay = {
      tenDeGiay: productForm.value.tenDeGiay,
      trangThai: true,
      deleted: false,
    };
    await fetchCreateDeGiay(newDeGiay);
    await fetchDeGiay();
    showDeGiayDropdown.value = false;
  } catch (error) {
    console.error("Error creating de giay:", error);
    alert("Có lỗi xảy ra khi tạo đế giày!");
  }
};

// Popup functions
const closeMauSacPopup = () => {
  showMauSacPopup.value = false;
  mauSacSearch.value = "";
  newMauSacName.value = "";
};

const closeKichThuocPopup = () => {
  showKichThuocPopup.value = false;
  kichThuocSearch.value = "";
  newKichThuocName.value = "";
};

const selectMauSacFromPopup = (mauSac) => {
  const existingIndex = selectedMauSacs.value.findIndex(
    (item) => item.id === mauSac.id
  );
  if (existingIndex === -1) {
    // Nếu chưa có thì thêm vào
    selectedMauSacs.value.push(mauSac);
  } else {
    // Nếu đã có thì xóa khỏi danh sách (bỏ tích)
    selectedMauSacs.value.splice(existingIndex, 1);
  }
  // Không đóng popup để có thể chọn/bỏ chọn nhiều
};

const selectKichThuocFromPopup = (kichThuoc) => {
  const existingIndex = selectedKichThuocs.value.findIndex(
    (item) => item.id === kichThuoc.id
  );
  if (existingIndex === -1) {
    // Nếu chưa có thì thêm vào
    selectedKichThuocs.value.push(kichThuoc);
  } else {
    // Nếu đã có thì xóa khỏi danh sách (bỏ tích)
    selectedKichThuocs.value.splice(existingIndex, 1);
  }
  // Không đóng popup để có thể chọn/bỏ chọn nhiều
};

const createNewMauSacFromPopup = async () => {
  if (!newMauSacName.value.trim()) {
    alert("Vui lòng nhập tên màu sắc!");
    return;
  }

  try {
    const newMauSac = {
      tenMauSac: newMauSacName.value,
      trangThai: true,
      deleted: false,
    };
    await fetchCreateMauSac(newMauSac);
    await fetchMauSac();

    // Auto-select the newly created color
    const createdMauSac = mauSacs.value.find(
      (item) => item.tenMauSac === newMauSacName.value
    );
    if (
      createdMauSac &&
      !selectedMauSacs.value.find((item) => item.id === createdMauSac.id)
    ) {
      selectedMauSacs.value.push(createdMauSac);
    }

    closeMauSacPopup();
  } catch (error) {
    console.error("Error creating mau sac:", error);
    alert("Có lỗi xảy ra khi tạo màu sắc!");
  }
};

const createNewKichThuocFromPopup = async () => {
  if (!newKichThuocName.value.trim()) {
    alert("Vui lòng nhập tên kích thước!");
    return;
  }

  try {
    const newKichThuoc = {
      tenKichThuoc: newKichThuocName.value,
      trangThai: true,
      deleted: false,
    };
    await fetchCreateKichThuoc(newKichThuoc);
    await fetchKichThuoc();

    // Auto-select the newly created size
    const createdKichThuoc = kichThuocs.value.find(
      (item) => item.tenKichThuoc === newKichThuocName.value
    );
    if (
      createdKichThuoc &&
      !selectedKichThuocs.value.find((item) => item.id === createdKichThuoc.id)
    ) {
      selectedKichThuocs.value.push(createdKichThuoc);
    }

    closeKichThuocPopup();
  } catch (error) {
    console.error("Error creating kich thuoc:", error);
    alert("Có lỗi xảy ra khi tạo kích thước!");
  }
};

// Quick edit state
const quickEditState = ref(new Set());

// Quick edit modal state
const showQuickEditModal = ref(false);
const showDeleteConfirmModal = ref(false);
const currentEditingMauIndex = ref(null);
const quickEditForm = ref({
  trongLuong: "",
  soLuong: 0,
  giaBan: 0,
});

// Data cho popup confirm xóa
const deleteConfirmInfo = ref("");
const deleteConfirmData = ref({ mauIndex: -1, kichIndex: -1 });

// Variant management functions
const isVariantSelected = (mauIndex, kichIndex) => {
  return quickEditState.value.has(`${mauIndex}-${kichIndex}`);
};

const isAllVariantsSelected = (mauIndex) => {
  return selectedKichThuocs.value.every((_, kichIndex) =>
    isVariantSelected(mauIndex, kichIndex)
  );
};

const getSelectedVariantsCount = (mauIndex) => {
  let count = 0;
  selectedKichThuocs.value.forEach((_, kichIndex) => {
    if (isVariantSelected(mauIndex, kichIndex)) {
      count++;
    }
  });
  return count;
};

const toggleVariant = (mauIndex, kichIndex, event) => {
  const key = `${mauIndex}-${kichIndex}`;
  if (event.target.checked) {
    quickEditState.value.add(key);
  } else {
    quickEditState.value.delete(key);
  }
};

const toggleAllVariants = (mauIndex, event) => {
  if (event.target.checked) {
    selectedKichThuocs.value.forEach((_, kichIndex) => {
      quickEditState.value.add(`${mauIndex}-${kichIndex}`);
    });
  } else {
    selectedKichThuocs.value.forEach((_, kichIndex) => {
      quickEditState.value.delete(`${mauIndex}-${kichIndex}`);
    });
  }
};

// Quick edit functions
const showQuickEditPopup = (mauIndex) => {
  currentEditingMauIndex.value = mauIndex;

  // Lấy giá trị từ biến thể đầu tiên đã chọn để làm mặc định
  const firstSelectedVariant = getFirstSelectedVariant(mauIndex);
  if (firstSelectedVariant) {
    // Đọc trọng lượng từ table, nếu chưa có thì để trống
    const trongLuongFromTable = getTrongLuongFromTable(mauIndex, 0); // Lấy từ kích thước đầu tiên
    quickEditForm.value.trongLuong =
      trongLuongFromTable || firstSelectedVariant.trongLuong || "";
    quickEditForm.value.soLuong = firstSelectedVariant.soLuong || 0;
    quickEditForm.value.giaBan = firstSelectedVariant.giaBan || 0;
  }

  showQuickEditModal.value = true;
};

const getFirstSelectedVariant = (mauIndex) => {
  for (
    let kichIndex = 0;
    kichIndex < selectedKichThuocs.value.length;
    kichIndex++
  ) {
    if (isVariantSelected(mauIndex, kichIndex)) {
      return productVariants.value[mauIndex][kichIndex];
    }
  }
  return null;
};

const closeQuickEditModal = () => {
  showQuickEditModal.value = false;
  currentEditingMauIndex.value = null;
  quickEditForm.value = {
    trongLuong: "",
    soLuong: 0,
    giaBan: 0,
  };
};

const applyQuickEdit = () => {
  if (currentEditingMauIndex.value === null) return;

  const mauIndex = currentEditingMauIndex.value;

  // Áp dụng giá trị cho tất cả biến thể đã chọn
  selectedKichThuocs.value.forEach((_, kichIndex) => {
    if (isVariantSelected(mauIndex, kichIndex)) {
      // Đọc trọng lượng từ table trước, nếu chưa có thì sử dụng giá trị từ form
      const existingTrongLuong = getTrongLuongFromTable(mauIndex, kichIndex);
      productVariants.value[mauIndex][kichIndex].trongLuong =
        existingTrongLuong || quickEditForm.value.trongLuong;
      productVariants.value[mauIndex][kichIndex].soLuong =
        quickEditForm.value.soLuong;
      productVariants.value[mauIndex][kichIndex].giaBan =
        quickEditForm.value.giaBan;
    }
  });

  closeQuickEditModal();
};

// Image management functions
const selectVariantImage = (mauIndex, kichIndex) => {
  currentEditingVariant.value = { mauIndex, kichIndex };
  selectedAnhFromDatabase.value = [];
  selectedAnhFromFile.value = [];
  showAnhPopup.value = true;
};

const removeVariantImage = (mauIndex, kichIndex, anhIndex) => {
  if (
    productVariants.value[mauIndex] &&
    productVariants.value[mauIndex][kichIndex]
  ) {
    productVariants.value[mauIndex][kichIndex].anh.splice(anhIndex, 1);
  }
};

// Function xóa biến thể sản phẩm
const removeVariant = (mauIndex, kichIndex) => {
  // Hiển thị popup confirm
  deleteConfirmInfo.value = `Màu: ${
    selectedMauSacs.value[mauIndex]?.tenMauSac || "N/A"
  } - Kích thước: ${
    selectedKichThuocs.value[kichIndex]?.tenKichThuoc || "N/A"
  }`;
  deleteConfirmData.value = { mauIndex, kichIndex };
  showDeleteConfirmModal.value = true;
};

// Function đóng popup confirm xóa
const closeDeleteConfirmModal = () => {
  showDeleteConfirmModal.value = false;
  deleteConfirmInfo.value = "";
  deleteConfirmData.value = { mauIndex: -1, kichIndex: -1 };
};

// Function xác nhận xóa biến thể
const confirmDeleteVariant = () => {
  const { mauIndex, kichIndex } = deleteConfirmData.value;

  if (mauIndex >= 0 && kichIndex >= 0) {
    // Xóa biến thể khỏi mảng
    productVariants.value[mauIndex].splice(kichIndex, 1);

    // Xóa kích thước khỏi danh sách đã chọn
    selectedKichThuocs.value.splice(kichIndex, 1);

    // Nếu không còn kích thước nào, xóa cả màu sắc
    if (selectedKichThuocs.value.length === 0) {
      selectedMauSacs.value.splice(mauIndex, 1);
      productVariants.value.splice(mauIndex, 1);
    }
  }

  // Đóng popup
  closeDeleteConfirmModal();
};

const formatPrice = (price) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
};

// Helper functions để lấy ID từ tên
const getNhaSanXuatId = async (tenNhaSanXuat) => {
  if (!tenNhaSanXuat || !tenNhaSanXuat.trim()) return null;

  // Tìm trong danh sách hiện có
  const existingNhaSanXuat = nhaSanXuats.value.find(
    (item) => item.tenNhaSanXuat.toLowerCase() === tenNhaSanXuat.toLowerCase()
  );

  if (existingNhaSanXuat) {
    return existingNhaSanXuat.id;
  }

  // Nếu chưa có thì tạo mới
  try {
    const newNhaSanXuat = {
      tenNhaSanXuat: tenNhaSanXuat,
      trangThai: true,
      deleted: false,
      createAt: new Date().toISOString().split("T")[0],
      createBy: 1,
      updateAt: new Date().toISOString().split("T")[0],
      updateBy: 1,
    };

    const createdNhaSanXuat = await fetchCreateNhaSanXuat(newNhaSanXuat);
    await fetchNhaSanXuat(); // Refresh danh sách

    return createdNhaSanXuat.id;
  } catch (error) {
    console.error("Error creating nha san xuat:", error);
    return null;
  }
};

const getXuatXuId = async (tenXuatXu) => {
  if (!tenXuatXu || !tenXuatXu.trim()) return null;

  // Tìm trong danh sách hiện có
  const existingXuatXu = xuatXus.value.find(
    (item) => item.tenXuatXu.toLowerCase() === tenXuatXu.toLowerCase()
  );

  if (existingXuatXu) {
    return existingXuatXu.id;
  }

  // Nếu chưa có thì tạo mới
  try {
    const newXuatXu = {
      tenXuatXu: tenXuatXu,
      trangThai: true,
      deleted: false,
      createAt: new Date().toISOString().split("T")[0],
      createBy: 1,
      updateAt: new Date().toISOString().split("T")[0],
      updateBy: 1,
    };

    const createdXuatXu = await fetchCreateXuatXu(newXuatXu);
    await fetchXuatXu(); // Refresh danh sách

    return createdXuatXu.id;
  } catch (error) {
    console.error("Error creating xuat xu:", error);
    return null;
  }
};

const getChatLieuId = async (tenChatLieu) => {
  if (!tenChatLieu || !tenChatLieu.trim()) return null;

  // Tìm trong danh sách hiện có
  const existingChatLieu = chatLieus.value.find(
    (item) => item.tenChatLieu.toLowerCase() === tenChatLieu.toLowerCase()
  );

  if (existingChatLieu) {
    return existingChatLieu.id;
  }

  // Nếu chưa có thì tạo mới
  try {
    const newChatLieu = {
      tenChatLieu: tenChatLieu,
      trangThai: true,
      deleted: false,
      createAt: new Date().toISOString().split("T")[0],
      createBy: 1,
      updateAt: new Date().toISOString().split("T")[0],
      updateBy: 1,
    };

    const createdChatLieu = await fetchCreateChatLieu(newChatLieu);
    await fetchChatLieu(); // Refresh danh sách

    return createdChatLieu.id;
  } catch (error) {
    console.error("Error creating chat lieu:", error);
    return null;
  }
};

const getDeGiayId = async (tenDeGiay) => {
  if (!tenDeGiay || !tenDeGiay.trim()) return null;

  // Tìm trong danh sách hiện có
  const existingDeGiay = deGiays.value.find(
    (item) => item.tenDeGiay.toLowerCase() === tenDeGiay.toLowerCase()
  );

  if (existingDeGiay) {
    return existingDeGiay.id;
  }

  // Nếu chưa có thì tạo mới
  try {
    const newDeGiay = {
      tenDeGiay: tenDeGiay,
      trangThai: true,
      deleted: false,
      createAt: new Date().toISOString().split("T")[0],
      createBy: 1,
      updateAt: new Date().toISOString().split("T")[0],
      updateBy: 1,
    };

    const createdDeGiay = await fetchCreateDeGiay(newDeGiay);
    await fetchDeGiay(); // Refresh danh sách

    return createdDeGiay.id;
  } catch (error) {
    console.error("Error creating de giay:", error);
    return null;
  }
};

const getTrongLuongId = async (trongLuong) => {
  if (!trongLuong || !trongLuong.trim()) return null;

  // Tìm trong danh sách hiện có
  const existingTrongLuong = trongLuongs.value.find(
    (item) => item.tenTrongLuong.toLowerCase() === trongLuong.toLowerCase()
  );

  if (existingTrongLuong) {
    return existingTrongLuong.id;
  }

  // Nếu chưa có thì tạo mới
  try {
    const newTrongLuong = {
      tenTrongLuong: trongLuong,
      trangThai: true,
      deleted: false,
      createAt: new Date().toISOString().split("T")[0],
      createBy: 1,
      updateAt: new Date().toISOString().split("T")[0],
      updateBy: 1,
    };

    // Gọi API tạo trọng lượng mới (API không trả về dữ liệu, chỉ thông báo thành công)
    await fetchCreateTrongLuong(newTrongLuong);

    // Refresh danh sách để lấy trọng lượng vừa tạo
    await fetchTrongLuong();

    // Tìm lại trọng lượng vừa tạo trong danh sách đã refresh
    const newlyCreatedTrongLuong = trongLuongs.value.find(
      (item) => item.tenTrongLuong.toLowerCase() === trongLuong.toLowerCase()
    );

    if (newlyCreatedTrongLuong) {
      return newlyCreatedTrongLuong.id;
    } else {
      throw new Error(`Không thể tìm thấy trọng lượng vừa tạo: ${trongLuong}`);
    }
  } catch (error) {
    console.error("Error creating trong luong:", error);
    throw new Error(
      `Không thể tạo trọng lượng: ${trongLuong} - ${error.message}`
    );
  }
};

// Helper function để đọc trọng lượng từ table và thêm vào nếu chưa có
const getTrongLuongFromTable = (mauIndex, kichIndex) => {
  if (
    productVariants.value[mauIndex] &&
    productVariants.value[mauIndex][kichIndex]
  ) {
    const trongLuong = productVariants.value[mauIndex][kichIndex].trongLuong;
    if (trongLuong && trongLuong.trim()) {
      return trongLuong.trim();
    }
  }
  return null;
};

// Đóng tất cả dropdown
const closeAllDropdowns = () => {
  showNhaSanXuatDropdown.value = false;
  showXuatXuDropdown.value = false;
  showChatLieuDropdown.value = false;
  showDeGiayDropdown.value = false;
  showTenSanPhamDropdown.value = false;
};

// Click outside để đóng dropdown
const handleClickOutside = (event) => {
  // Kiểm tra xem có phải click vào input hoặc dropdown của phần thông tin cơ bản không
  const isClickOnBasicInfoInput = event.target.closest(".input-with-dropdown");
  const isClickOnBasicInfoDropdown = event.target.closest(".dropdown-list");

  // Nếu click ra ngoài cả input và dropdown của phần thông tin cơ bản thì đóng tất cả dropdown
  if (!isClickOnBasicInfoInput && !isClickOnBasicInfoDropdown) {
    closeAllDropdowns();
    return;
  }

  // Nếu click vào input khác thì đóng dropdown hiện tại
  if (isClickOnBasicInfoInput && !isClickOnBasicInfoDropdown) {
    const clickedInput = event.target.closest(".input-with-dropdown");
    if (clickedInput && event.target.tagName === "INPUT") {
      // Kiểm tra xem input này có phải là input đang active không
      const isCurrentlyActive =
        (showTenSanPhamDropdown.value &&
          clickedInput.querySelector("input") === event.target) ||
        (showNhaSanXuatDropdown.value &&
          clickedInput.querySelector("input") === event.target) ||
        (showXuatXuDropdown.value &&
          clickedInput.querySelector("input") === event.target) ||
        (showChatLieuDropdown.value &&
          clickedInput.querySelector("input") === event.target) ||
        (showDeGiayDropdown.value &&
          clickedInput.querySelector("input") === event.target);

      // Nếu không phải input đang active thì đóng tất cả dropdown
      if (!isCurrentlyActive) {
        closeAllDropdowns();
      }
    }
  }
};

// Save product function
const saveProduct = async () => {
  try {
    // Validate form
    if (!productForm.value.tenSanPham.trim()) {
      alert("Vui lòng nhập tên sản phẩm!");
      return;
    }

    if (!productForm.value.tenNhaSanXuat.trim()) {
      alert("Vui lòng nhập hoặc chọn nhà sản xuất!");
      return;
    }

    if (!productForm.value.tenXuatXu.trim()) {
      alert("Vui lòng nhập hoặc chọn xuất xứ!");
      return;
    }

    if (!productForm.value.tenChatLieu.trim()) {
      alert("Vui lòng nhập hoặc chọn chất liệu!");
      return;
    }

    if (!productForm.value.tenDeGiay.trim()) {
      alert("Vui lòng nhập hoặc chọn đế giày!");
      return;
    }

    if (selectedMauSacs.value.length === 0) {
      alert("Vui lòng chọn ít nhất một màu sắc!");
      return;
    }

    if (selectedKichThuocs.value.length === 0) {
      alert("Vui lòng chọn ít nhất một kích thước!");
      return;
    }

    // Validate variants
    for (let i = 0; i < selectedMauSacs.value.length; i++) {
      for (let j = 0; j < selectedKichThuocs.value.length; j++) {
        const variant = productVariants.value[i][j];
        // Không cần validate tenSanPham vì nó được tự động điền từ form chính
        if (!variant.trongLuong || !variant.trongLuong.trim()) {
          alert(
            `Vui lòng nhập trọng lượng cho biến thể ${selectedMauSacs.value[i].tenMauSac} - ${selectedKichThuocs.value[j].tenKichThuoc}`
          );
          return;
        }
        if (variant.soLuong <= 0) {
          alert(
            `Vui lòng nhập số lượng > 0 cho biến thể ${selectedMauSacs.value[i].tenMauSac} - ${selectedKichThuocs.value[j].tenKichThuoc}`
          );
          return;
        }
        if (variant.giaBan <= 0) {
          alert(
            `Vui lòng nhập giá bán > 0 cho biến thể ${selectedMauSacs.value[i].tenMauSac} - ${selectedKichThuocs.value[j].tenKichThuoc}`
          );
          return;
        }
      }
    }

    // Tạo dữ liệu sản phẩm chính trước
    const sanPhamData = {
      tenSanPham: productForm.value.tenSanPham,
      idNhaSanXuat: await getNhaSanXuatId(productForm.value.tenNhaSanXuat),
      idXuatXu: await getXuatXuId(productForm.value.tenXuatXu),
      trangThai: true,
      deleted: false,
      createAt: new Date().toISOString().split("T")[0], // Format: YYYY-MM-DD
      createBy: 1, // Default value
      updateAt: new Date().toISOString().split("T")[0],
      updateBy: 1, // Default value
    };

    console.error("Data being sent to API:", sanPhamData);

    // Kiểm tra dữ liệu trước khi gửi API
    if (!sanPhamData.idNhaSanXuat) {
      throw new Error("Không thể tạo sản phẩm: Thiếu thông tin nhà sản xuất!");
    }
    if (!sanPhamData.idXuatXu) {
      throw new Error("Không thể tạo sản phẩm: Thiếu thông tin xuất xứ!");
    }

    const createdSanPham = await fetchCreateSanPham(sanPhamData);
    console.error("API Response from fetchCreateSanPham:", createdSanPham);

    // Kiểm tra response từ API
    if (!createdSanPham) {
      throw new Error("API không trả về dữ liệu");
    }

    // Kiểm tra success flag
    if (createdSanPham.success === false) {
      throw new Error(
        `API trả về lỗi: ${createdSanPham.message || "Không xác định"}`
      );
    }

    // Backend trả về data: null khi tạo thành công, cần lấy ID từ request
    // Vì backend không trả về ID, ta sẽ sử dụng ID từ request hoặc tìm sản phẩm vừa tạo
    console.error(
      "Backend tạo sản phẩm thành công, nhưng không trả về ID. Cần tìm sản phẩm vừa tạo..."
    );

    // Tìm sản phẩm vừa tạo bằng tên và các thuộc tính khác
    const allSanPhams = await fetchAllSanPham();
    const newlyCreatedSanPham = allSanPhams.data?.find(
      (sanPham) =>
        sanPham.tenSanPham === productForm.value.tenSanPham &&
        sanPham.idNhaSanXuat === sanPhamData.idNhaSanXuat &&
        sanPham.idXuatXu === sanPhamData.idXuatXu
    );

    if (!newlyCreatedSanPham || !newlyCreatedSanPham.id) {
      throw new Error("Không thể tìm thấy sản phẩm vừa tạo");
    }

    // Lấy dữ liệu từ sản phẩm vừa tạo
    const newSanPhamData = newlyCreatedSanPham;

    // Lấy ID từ response
    const sanPhamId = newSanPhamData.id;
    for (let i = 0; i < selectedMauSacs.value.length; i++) {
      for (let j = 0; j < selectedKichThuocs.value.length; j++) {
        const variant = productVariants.value[i][j];

        // Lấy ID của TrongLuong (có thể cần tạo mới)
        const trongLuongId = await getTrongLuongId(variant.trongLuong);
        // getTrongLuongId sẽ throw error nếu không thể tạo hoặc tìm trọng lượng

        // Tạo dữ liệu biến thể
        const variantData = {
          idSanPham: sanPhamId,
          idKichThuoc: selectedKichThuocs.value[j].id,
          idMauSac: selectedMauSacs.value[i].id,
          idDeGiay: await getDeGiayId(productForm.value.tenDeGiay),
          idChatLieu: await getChatLieuId(productForm.value.tenChatLieu),
          idTrongLuong: trongLuongId,
          soLuong: variant.soLuong,
          giaBan: variant.giaBan,
          trangThai: true,
          deleted: false,
          createAt: new Date().toISOString().split("T")[0], // Format: YYYY-MM-DD
          createBy: 1, // Default value
          updateAt: new Date().toISOString().split("T")[0],
          updateBy: 1, // Default value
        };

        // Kiểm tra dữ liệu biến thể trước khi gửi API
        if (!variantData.idDeGiay) {
          throw new Error(
            `Không thể tạo biến thể ${i}-${j}: Thiếu thông tin đế giày!`
          );
        }
        if (!variantData.idChatLieu) {
          throw new Error(
            `Không thể tạo biến thể ${i}-${j}: Thiếu thông tin chất liệu!`
          );
        }
        if (!variantData.idTrongLuong) {
          throw new Error(
            `Không thể tạo biến thể ${i}-${j}: Thiếu thông tin trọng lượng!`
          );
        }

        // Gọi API tạo biến thể sản phẩm
        const createdVariant = await fetchCreateChiTietSanPham(variantData);

        // Xử lý ảnh cho biến thể nếu cần
        if (variant.anh && variant.anh.length > 0) {
          // Tạo liên kết ảnh cho biến thể
          for (const anh of variant.anh) {
            try {
              let anhSanPhamId;

              if (anh.type === "database") {
                // Ảnh từ database
                anhSanPhamId = anh.id;
              } else if (anh.type === "file") {
                // Ảnh từ file - cần tạo mới AnhSanPham trước
                const anhSanPhamData = {
                  duongDanAnh: anh.url || anh.duongDanAnh,
                  loaiAnh: "PRODUCT",
                  moTa: `Ảnh biến thể ${selectedMauSacs.value[i].tenMauSac} - ${selectedKichThuocs.value[j].tenKichThuoc}`,
                  trangThai: true,
                  deleted: false,
                  createAt: new Date().toISOString().split("T")[0],
                  createBy: 1,
                  updateAt: new Date().toISOString().split("T")[0],
                  updateBy: 1,
                };

                const createdAnh = await fetchCreateAnhSanPham(anhSanPhamData);
                anhSanPhamId = createdAnh.id;
              }

              if (anhSanPhamId) {
                // Tạo liên kết ChiTietSanPhamAnh
                const chiTietSanPhamAnhData = {
                  idChiTietSanPham: createdVariant.id,
                  idAnhSanPham: anhSanPhamId,
                  trangThai: true,
                  deleted: false,
                  createAt: new Date().toISOString().split("T")[0],
                  createBy: 1,
                  updateAt: new Date().toISOString().split("T")[0],
                  updateBy: 1,
                };

                await fetchCreateChiTietSanPhamAnh(chiTietSanPhamAnhData);
              }
            } catch (error) {
              console.error(
                `❌ Lỗi khi tạo liên kết ảnh cho biến thể ${i}-${j}:`,
                error
              );
              // Không throw error để tiếp tục tạo các biến thể khác
            }
          }
        }
      }
    }

    successMessage.value = "Sản phẩm đã được tạo thành công!";
    showSuccessModal.value = true;

    // Reset form after successful creation
    resetForm();
  } catch (error) {
    console.error("❌ Error creating product:", error);
    console.error("Error details:", {
      message: error.message,
      stack: error.stack,
      response: error.response,
    });

    // Hiển thị thông báo lỗi chi tiết hơn
    let errorMessage = "Có lỗi xảy ra khi tạo sản phẩm!";
    if (error.message.includes("trọng lượng")) {
      errorMessage = error.message;
    } else if (error.message.includes("Failed to create")) {
      errorMessage = "Lỗi kết nối API. Vui lòng kiểm tra lại!";
    }

    alert(errorMessage);
  }
};

const closeSuccessModal = () => {
  showSuccessModal.value = false;
  successMessage.value = "";
};

onMounted(async () => {
  // Initialize form
  resetForm();

  // Fetch data cho các thuộc tính
  await Promise.all([
    fetchTenSanPham(),
    fetchNhaSanXuat(),
    fetchXuatXu(),
    fetchChatLieu(),
    fetchDeGiay(),
    fetchTrongLuong(),
    fetchMauSac(),
    fetchKichThuoc(),
    fetchAnhSanPham(),
  ]);

  // Add click outside listener
  document.addEventListener("click", handleClickOutside);
});

// Cleanup event listener
onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});

// Popup functions cho ảnh
const closeAnhPopup = () => {
  showAnhPopup.value = false;
  currentEditingVariant.value = { mauIndex: -1, kichIndex: -1 };
  selectedAnhFromDatabase.value = [];
  selectedAnhFromFile.value = [];
};

const toggleAnhFromDatabase = (anh) => {
  const index = selectedAnhFromDatabase.value.findIndex(
    (item) => item.id === anh.id
  );
  if (index === -1) {
    // Kiểm tra giới hạn 5 ảnh
    if (getTotalSelectedAnhCount() >= 5) {
      alert("Chỉ được chọn tối đa 5 ảnh!");
      return;
    }
    selectedAnhFromDatabase.value.push(anh);
  } else {
    selectedAnhFromDatabase.value.splice(index, 1);
  }
};

const handleFileSelect = (event) => {
  const files = Array.from(event.target.files);
  const currentTotal = getTotalSelectedAnhCount();
  const maxFiles = 5 - currentTotal;

  if (files.length > maxFiles) {
    alert(`Chỉ được chọn tối đa ${maxFiles} ảnh từ máy!`);
    return;
  }

  selectedAnhFromFile.value = files;
};

const applyAnhSelection = () => {
  const { mauIndex, kichIndex } = currentEditingVariant.value;

  if (mauIndex >= 0 && kichIndex >= 0) {
    // Lấy URL từ ảnh database
    const anhUrls = selectedAnhFromDatabase.value.map((anh) => getAnhUrl(anh));

    // Lấy URL từ ảnh file (convert to base64)
    const filePromises = selectedAnhFromFile.value.map((file) => {
      return new Promise((resolve) => {
        const reader = new FileReader();
        reader.onload = (e) => resolve(e.target.result);
        reader.readAsDataURL(file);
      });
    });

    Promise.all(filePromises).then((fileUrls) => {
      // Cập nhật ảnh cho biến thể
      productVariants.value[mauIndex][kichIndex].anh = [
        ...anhUrls,
        ...fileUrls,
      ];
      closeAnhPopup();
    });
  }
};

// Function xóa file đã chọn
const removeSelectedFile = (index) => {
  selectedAnhFromFile.value.splice(index, 1);
};

// Function đếm tổng số ảnh đã chọn
const getTotalSelectedAnhCount = () => {
  return (
    selectedAnhFromDatabase.value.length + selectedAnhFromFile.value.length
  );
};

// Function tạo URL cho file
const getFileUrl = (file) => {
  if (file.url) {
    return file.url; // Nếu file đã có URL
  }
  if (file instanceof File) {
    return URL.createObjectURL(file); // Tạo URL cho File object
  }
  return file.duongDanAnh || file.src || ""; // Fallback
};

// Function tạo URL cho ảnh (database hoặc file)
const getAnhUrl = (anh) => {
  if (typeof anh === "string") {
    return anh; // Nếu anh là URL string
  }
  if (anh && anh.duongDan) {
    return anh.duongDan; // Nếu anh có duongDan
  }
  if (anh && anh.duongDanAnh) {
    return anh.duongDanAnh; // Nếu anh có duongDanAnh (theo backend entity)
  }
  if (anh && anh.url) {
    return anh.url; // Nếu anh có url
  }
  if (anh && anh.src) {
    return anh.src; // Nếu anh có src
  }
  return ""; // Fallback
};

// Function lấy tên ảnh
const getAnhName = (anh) => {
  if (typeof anh === "string") {
    return "Ảnh sản phẩm"; // Fallback cho string
  }
  if (anh && anh.tenAnh) {
    return anh.tenAnh; // Nếu anh có tenAnh
  }
  if (anh && anh.moTa) {
    return anh.moTa; // Nếu anh có moTa
  }
  if (anh && anh.name) {
    return anh.name; // Nếu anh có name (file)
  }
  return "Ảnh sản phẩm"; // Fallback
};

// Function xử lý lỗi ảnh
const handleImageError = (event) => {
  console.log("🖼️ Lỗi load ảnh:", event.target.src);
  // Thay thế bằng ảnh placeholder hoặc ẩn ảnh
  event.target.style.display = "none";
  // Hoặc có thể thay thế bằng ảnh placeholder:
  // event.target.src = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI2RkZCIvPjx0ZXh0IHg9IjUwIiB5PSI1NSIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjOTk5IiB0ZXh0LWFuY2hvcj0ibWlkZGxlIj5LaG9uZyBsb2FkIMSRxrDhu6NjPC90ZXh0Pjwvc3ZnPg==";
};
</script>

<style scoped>
/* CSS đã được chuyển vào productsUnified.css */
@import "../../../styles/cssSanPham/productsUnified.css";
@import "../../../styles/cssSanPham/quanLySanPham.css";

/* CSS cho dropdown */
.input-with-dropdown {
  position: relative;
}

.dropdown-list {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  max-height: 200px;
  overflow-y: auto;
}

.dropdown-item {
  padding: 8px 12px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
  transition: background-color 0.2s;
}

.dropdown-item:hover {
  background-color: #f5f5f5;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item.create-new {
  background-color: #e3f2fd;
  color: #1976d2;
  font-weight: 500;
  border-top: 2px solid #1976d2;
}

.dropdown-item.create-new:hover {
  background-color: #bbdefb;
}

/* Đóng dropdown khi click ra ngoài */
.dropdown-list:focus-within {
  outline: none;
}

/* Click outside để đóng dropdown */
.click-outside {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 999;
}

/* CSS cho product form grid */
.product-form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* CSS cho phần thuộc tính sản phẩm - sử dụng flex column */
.product-form-grid:has(.attribute-selector) {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* CSS cho attribute selector */
.attribute-selector {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 15px;
  flex: 1;
}

/* CSS cho phần thuộc tính sản phẩm - label và input cùng hàng */
.product-form-field {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.product-form-field:last-child {
  margin-bottom: 0;
}

.product-form-field .modern-label {
  min-width: 80px;
  margin: 0;
  flex-shrink: 0;
}

.selected-attributes {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 32px;
  align-items: center;
  justify-content: flex-start;
  margin-right: 15px;
  order: 1;
}

.selected-attribute-tag {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 4px 8px;
  background-color: #e3f2fd;
  color: #1976d2;
  border-radius: 16px;
  font-size: 14px;
}

.remove-attribute-btn {
  background: none;
  border: none;
  color: #1976d2;
  cursor: pointer;
  font-size: 16px;
  padding: 0;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.remove-attribute-btn:hover {
  color: #d32f2f;
}

.add-attribute-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background-color: #f5f5f5;
  border: 2px dashed #ddd;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 13px;
  color: #666;
  height: 32px;
  flex-shrink: 0;
  order: 2;
}

.add-attribute-btn:hover {
  background-color: #e0e0e0;
  border-color: #999;
  color: #333;
}

.add-icon {
  font-size: 16px;
  font-weight: bold;
}

/* CSS cho popup */
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.popup-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.popup-header h3 {
  margin: 0;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  color: #333;
}

.popup-body {
  padding: 20px;
}

.search-section {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.existing-attributes {
  margin-bottom: 20px;
}

.existing-attributes h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.attribute-list {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #eee;
  border-radius: 4px;
}

.attribute-item {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
  transition: background-color 0.2s;
}

.attribute-item:hover {
  background-color: #f5f5f5;
}

.attribute-item.selected {
  background-color: #e3f2fd;
  color: #1976d2;
  font-weight: 500;
}

.checkmark {
  color: #1976d2;
  font-weight: bold;
  margin-right: 8px;
}

.attribute-item:last-child {
  border-bottom: none;
}

.create-new-section h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.create-new-form {
  display: flex;
  gap: 10px;
}

.create-new-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.create-new-btn {
  padding: 10px 16px;
  background-color: #1976d2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.create-new-btn:hover {
  background-color: #1565c0;
}

/* CSS cho variant table */
.variant-table-container {
  margin-bottom: 30px;
}

.variant-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.variant-table-title {
  margin: 0;
  color: #333;
  font-size: 18px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.variant-table {
  overflow-x: auto;
}

.variant-table table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  table-layout: fixed;
}

.variant-table th,
.variant-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
  word-wrap: break-word;
}

.variant-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
  text-align: center;
  font-size: 14px;
}

.variant-table td {
  font-size: 12px;
}

.variant-table tr:hover {
  background-color: #f8f9fa;
}

.variant-input {
  width: 100%;
  padding: 6px 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 12px;
  box-sizing: border-box;
}

/* CSS cho variant-input-s - hiển thị như text */
.variant-input-s {
  width: 100%;
  padding: 8px 12px;
  border: none;
  background-color: #f8f9fa;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  font-weight: 500;
  text-align: center;
  cursor: default;
  outline: none;
  box-sizing: border-box;
}

.variant-input-s:focus {
  outline: none;
  box-shadow: none;
}

/* CSS cho width của các cột */
.variant-table th:nth-child(1),
.variant-table td:nth-child(1) {
  width: 50px;
}

.variant-table th:nth-child(2),
.variant-table td:nth-child(2) {
  width: 10%;
}

.variant-table th:nth-child(3),
.variant-table td:nth-child(3) {
  width: 10%;
}

.variant-table th:nth-child(4),
.variant-table td:nth-child(4) {
  width: 10%;
}

.variant-table th:nth-child(5),
.variant-table td:nth-child(5) {
  width: 10%;
}

.variant-table th:nth-child(6),
.variant-table td:nth-child(6) {
  width: 15%;
}

.variant-table th:nth-child(7),
.variant-table td:nth-child(7) {
  width: 35%;
  min-width: 180px;
}

.variant-table th:nth-child(8),
.variant-table td:nth-child(8) {
  width: 10%;
}

.variant-image-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
}

.image-preview-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
  justify-content: center;
}

.image-preview-item {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.variant-thumbnail {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.remove-image-btn {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: #d32f2f;
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
}

.remove-image-btn:hover {
  background-color: #b71c1c;
}

.add-image-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  background-color: #f5f5f5;
  border: 2px dashed #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  color: #666;
  transition: all 0.2s;
  min-width: 80px;
  justify-content: center;
}

.add-image-btn:hover {
  background-color: #e0e0e0;
  border-color: #999;
  color: #333;
}

.add-image-btn .add-icon {
  font-size: 14px;
  font-weight: bold;
}

/* CSS cho popup sửa nhanh */
.quick-edit-popup {
  max-width: 400px;
}

.quick-edit-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.form-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-input:focus {
  outline: none;
  border-color: #1976d2;
  box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.2);
}

.popup-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px;
  border-top: 1px solid #eee;
}

.btn-primary {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  background: var(--accent-color);
  color: white;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  box-shadow: 0 2px 8px rgba(34, 197, 94, 0.2);
  white-space: nowrap;
}

.btn-primary:hover {
  background: var(--accent-color-dark);
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(34, 197, 94, 0.4);
}

.btn-outline {
  padding: 0.75rem 1.5rem;
  border: 2px solid var(--accent-color);
  border-radius: 8px;
  background: white;
  color: var(--accent-color);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  box-shadow: 0 2px 8px rgba(34, 197, 94, 0.1);
  white-space: nowrap;
}

.btn-outline:hover {
  background: var(--accent-color);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(34, 197, 94, 0.3);
}

/* CSS để tất cả button có size giống nhau */
.popup-footer .btn-outline {
  min-width: 80px;
  justify-content: center;
  padding: 0.5rem 1rem;
  font-size: 0.8rem;
}

.anh-title-row .btn-outline {
  min-width: 120px;
  justify-content: center;
}

/* CSS để hiển thị đơn vị bên trong input */
.variant-table td:nth-child(4) .variant-input {
  padding-right: 30px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='20' height='20' viewBox='0 0 20 20'%3E%3Ctext x='10' y='15' font-family='Arial' font-size='14' fill='%23666' text-anchor='middle'%3Eg%3C/text%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 20px;
}

.variant-table td:nth-child(6) .variant-input {
  padding-right: 30px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='20' height='20' viewBox='0 0 20 20'%3E%3Ctext x='10' y='15' font-family='Arial' font-size='14' fill='%23666' text-anchor='middle'%3E₫%3C/text%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 20px;
}

/* CSS cho popup sửa nhanh - thêm đơn vị */
.quick-edit-popup .form-field:nth-child(1) .form-input {
  padding-right: 30px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='20' height='20' viewBox='0 0 20 20'%3E%3Ctext x='10' y='15' font-family='Arial' font-size='14' fill='%23666' text-anchor='middle'%3Eg%3C/text%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 20px;
}

.quick-edit-popup .form-field:nth-child(3) .form-input {
  padding-right: 30px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='20' height='20' viewBox='0 0 20 20'%3E%3Ctext x='10' y='15' font-family='Arial' font-size='14' fill='%23666' text-anchor='middle'%3E₫%3C/text%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 20px;
}

/* CSS cho nút xóa biến thể */
.remove-variant-btn {
  width: 60px;
  height: 32px;
  background-color: #d32f2f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
  margin: 0 auto;
}

.remove-variant-btn:hover {
  background-color: #b71c1c;
}

/* CSS cho popup confirm xóa */
.delete-confirm-popup {
  max-width: 400px;
}

.confirm-message {
  font-size: 16px;
  color: #333;
  margin-bottom: 15px;
  text-align: center;
}

.variant-info {
  font-size: 14px;
  color: #666;
  background-color: #f8f9fa;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #e9ecef;
  margin-bottom: 20px;
  text-align: center;
}

.btn-danger {
  background-color: #d32f2f;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.btn-danger:hover {
  background-color: #b71c1c;
}

/* CSS cho popup ảnh */
.anh-popup {
  max-width: 900px;
  max-height: 90vh;
}

/* Title row */
.anh-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 10px;
}

.anh-title-row h4 {
  margin: 0;
  color: #333;
  font-size: 18px;
  font-weight: 600;
  flex: 1;
  text-align: center;
}

.anh-title-row h4:first-child {
  text-align: left;
}

.anh-title-row h4:last-child {
  text-align: right;
}

/* Content row */
.anh-content-row {
  display: flex;
  gap: 30px;
  margin-bottom: 25px;
}

.anh-database-section,
.anh-file-section {
  flex: 1;
  margin-bottom: 0;
}

.anh-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 12px;
  max-height: 250px;
  overflow-y: auto;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 15px;
  background-color: #f8f9fa;
}

.anh-item {
  position: relative;
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.anh-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.anh-item.selected {
  border-color: #1976d2;
  box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.2);
}

.anh-thumbnail {
  width: 100%;
  height: 80px;
  object-fit: cover;
  display: block;
}

.anh-overlay {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 24px;
  height: 24px;
  background-color: #1976d2;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 14px;
}

.anh-name {
  padding: 8px;
  background-color: white;
  font-size: 12px;
  text-align: center;
  color: #333;
  border-top: 1px solid #eee;
  word-break: break-word;
}

.file-upload-area {
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  background-color: #f8f9fa;
  transition: all 0.2s;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.file-upload-area:hover {
  border-color: #1976d2;
  background-color: #e3f2fd;
}

.file-input {
  display: none;
}

.file-upload-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 15px 30px;
  background-color: #1976d2;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.3);
}

.file-upload-btn:hover {
  background-color: #1565c0;
}

.upload-icon {
  font-size: 18px;
}

.selected-files {
  margin-top: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.selected-file {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.file-thumbnail {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #ddd;
}

.file-name {
  font-size: 11px;
  color: #666;
  text-align: center;
  max-width: 80px;
  word-break: break-word;
}

.remove-file-btn {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: #d32f2f;
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
}

.remove-file-btn:hover {
  background-color: #b71c1c;
}

.selected-anh-info {
  margin-top: 20px;
  padding: 15px;
  background-color: transparent;
  border-radius: 0;
  border: none;
  box-shadow: none;
}

.selected-anh-info h4 {
  margin: 0 0 10px 0;
  color: #000;
  font-size: 14px;
  text-align: left;
}

.selected-anh-summary {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #000;
  text-align: left;
}

.selected-anh-summary span {
  background-color: transparent;
  padding: 0;
  border-radius: 0;
  border: none;
  box-shadow: none;
}

/* CSS cho phần hiển thị ảnh đã chọn */
.selected-anh-display {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.selected-anh-display h4 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
}

.selected-anh-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.selected-anh-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.selected-anh-thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
  border: 2px solid #ddd;
}

.selected-anh-name {
  font-size: 11px;
  color: #333;
  text-align: center;
  max-width: 60px;
  word-break: break-word;
  font-weight: 500;
}
</style>
