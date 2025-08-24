<template>
  <div class="product-details">
    <!-- Modern Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">Quản lý Chi tiết sản phẩm</h1>
          <p class="page-subtitle">
            Quản lý biến thể sản phẩm, giá bán và số lượng tồn kho
          </p>
        </div>
        <div class="header-actions">
          <button class="btn-refresh" @click="refreshData">
            <span class="btn-icon">🔄</span>
            Làm mới
          </button>
          <button class="btn-export" @click="exportData">
            <span class="btn-icon">📊</span>
            Xuất báo cáo
          </button>
          <button class="btn-export" @click="exportDetailsToExcel">
            <span class="btn-icon">📗</span>
            Xuất Excel
          </button>
          <button
            class="btn-export"
            @click="loadSampleData"
            title="Load dữ liệu mẫu"
          >
            <span class="btn-icon">📊</span>
            Load mẫu
          </button>
          <button class="btn-export" @click="showAddModal = true">
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
            <!-- {{ filteredDetails.length }} / {{ productDetails.length }} chi tiết sản phẩm -->
          </div>
        </div>

        <div class="filter-content">
          <div class="search-section">
            <div class="input-group">
              <span class="input-icon">🔍</span>
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
              <label class="filter-label">
                <span class="label-icon">📦</span>
                Sản phẩm
              </label>
              <select v-model="selectedProduct" class="form-select">
                <option value="">Tất cả sản phẩm</option>
                <option
                  v-for="product in products"
                  :key="product.id"
                  :value="product.id"
                >
                  {{ product.tenSanPham }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🎨</span>
                Màu sắc
              </label>
              <select v-model="selectedColor" class="form-select">
                <option value="">Tất cả màu sắc</option>
                <option
                  v-for="color in colors"
                  :key="color.id"
                  :value="color.id"
                >
                  {{ color.tenMauSac }}
                </option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">📏</span>
                Kích thước
              </label>
              <select v-model="selectedSize" class="form-select">
                <option value="">Tất cả kích thước</option>
                <option v-for="size in sizes" :key="size.id" :value="size.id">
                  {{ size.tenKichThuoc }}
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
              <button @click="applyFilters" class="btn btn-primary">
                <span class="btn-icon">🔍</span>
                Áp dụng
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
                <td class="stt-col">
                  {{ (currentPage - 1) * itemsPerPage + index + 1 }}
                </td>
                <td class="product-col">
                  <div class="product-info">
                    <strong>{{
                      detail.tenSanPham || detail.sanPham?.tenSanPham || "N/A"
                    }}</strong>
                  </div>
                </td>
                <td class="color-col">
                  <span
                    class="color-badge"
                    :style="{
                      backgroundColor: getColorCode(
                        detail.tenMauSac || detail.mauSac?.tenMauSac
                      ),
                    }"
                  >
                    {{ detail.tenMauSac || detail.mauSac?.tenMauSac || "N/A" }}
                  </span>
                </td>
                <td class="size-col">
                  <span class="size-badge">{{
                    detail.tenKichThuoc ||
                    detail.kichThuoc?.tenKichThuoc ||
                    "N/A"
                  }}</span>
                </td>
                <td class="sole-col">
                  <span class="attribute-text">{{
                    detail.tenDeGiay || detail.deGiay?.tenDeGiay || "N/A"
                  }}</span>
                </td>
                <td class="material-col">
                  <span class="attribute-text">{{
                    detail.tenChatLieu || detail.chatLieu?.tenChatLieu || "N/A"
                  }}</span>
                </td>
                <td class="insole-col">
                  <span class="attribute-text">{{
                    detail.tenDemGiay || detail.demGiay?.tenDemGiay || "N/A"
                  }}</span>
                </td>
                <td class="weight-col">
                  <span class="attribute-text">{{
                    detail.tenTrongLuong ||
                    detail.trongLuong?.tenTrongLuong ||
                    "N/A"
                  }}</span>
                </td>
                <td class="sport-col">
                  <span class="attribute-text">{{
                    detail.tenMonTheThao ||
                    detail.monTheThao?.tenMonTheThao ||
                    "N/A"
                  }}</span>
                </td>
                <td class="season-col">
                  <span class="attribute-text">{{
                    detail.tenLoaiMua || detail.loaiMua?.tenLoaiMua || "N/A"
                  }}</span>
                </td>
                <td class="durability-col">
                  <span class="attribute-text">{{
                    detail.tenDoBen || detail.doBen?.tenDoBen || "N/A"
                  }}</span>
                </td>
                <td class="waterproof-col">
                  <span class="attribute-text">{{
                    detail.tenChongNuoc ||
                    detail.chongNuoc?.tenChongNuoc ||
                    "N/A"
                  }}</span>
                </td>
                <td class="image-col">
                  <div
                    v-if="detail.anhSanPham && detail.anhSanPham.length > 0"
                    class="image-preview"
                  >
                    <img
                      :src="detail.anhSanPham[0]"
                      :alt="detail.tenSanPham"
                      class="product-image"
                    />
                    <span
                      v-if="detail.anhSanPham.length > 1"
                      class="image-count"
                      >+{{ detail.anhSanPham.length - 1 }}</span
                    >
                  </div>
                  <div
                    v-else-if="
                      detail.chiTietSanPhamAnhs &&
                      detail.chiTietSanPhamAnhs.length > 0
                    "
                    class="image-preview"
                  >
                    <img
                      :src="
                        detail.chiTietSanPhamAnhs[0].idAnhSanPham?.duongDanAnh
                      "
                      :alt="detail.tenSanPham"
                      class="product-image"
                    />
                    <span
                      v-if="detail.chiTietSanPhamAnhs.length > 1"
                      class="image-count"
                      >+{{ detail.chiTietSanPhamAnhs.length - 1 }}</span
                    >
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
            <button
              class="btn-export"
              @click="previousPage"
              :disabled="currentPage === 1"
            >
              <span class="btn-icon">❮</span>
            </button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button
              class="btn-export"
              @click="nextPage"
              :disabled="currentPage === totalPages"
            >
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
              <label class="form-label">Sản phẩm *</label>
              <select
                v-model="formData.id_san_pham"
                class="form-control"
                required
              >
                <option value="">Chọn sản phẩm</option>
                <option
                  v-for="product in products"
                  :key="product.id"
                  :value="product.id"
                >
                  {{ product.tenSanPham }} ({{ product.maSanPham }})
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Màu sắc *</label>
              <select
                v-model="formData.id_mau_sac"
                class="form-control"
                required
              >
                <option value="">Chọn màu sắc</option>
                <option
                  v-for="color in colors"
                  :key="color.id"
                  :value="color.id"
                >
                  {{ color.tenMauSac }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Kích thước *</label>
              <select
                v-model="formData.id_kich_thuoc"
                class="form-control"
                required
              >
                <option value="">Chọn kích thước</option>
                <option v-for="size in sizes" :key="size.id" :value="size.id">
                  {{ size.tenKichThuoc }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Chất liệu *</label>
              <select
                v-model="formData.id_chat_lieu"
                class="form-control"
                required
              >
                <option value="">Chọn chất liệu</option>
                <option
                  v-for="material in materials"
                  :key="material.id"
                  :value="material.id"
                >
                  {{ material.tenChatLieu }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Đế giày</label>
              <select v-model="formData.id_de_giay" class="form-control">
                <option value="">Chọn đế giày</option>
                <option v-for="sole in soles" :key="sole.id" :value="sole.id">
                  {{ sole.tenDeGiay }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Đệm giày</label>
              <select v-model="formData.id_dem_giay" class="form-control">
                <option value="">Chọn đệm giày</option>
                <option
                  v-for="insole in insoles"
                  :key="insole.id"
                  :value="insole.id"
                >
                  {{ insole.tenDemGiay }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Trọng lượng</label>
              <select v-model="formData.id_trong_luong" class="form-control">
                <option value="">Chọn trọng lượng</option>
                <option
                  v-for="weight in weights"
                  :key="weight.id"
                  :value="weight.id"
                >
                  {{ weight.tenTrongLuong }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Môn thể thao</label>
              <select v-model="formData.id_mon_the_thao" class="form-control">
                <option value="">Chọn môn thể thao</option>
                <option
                  v-for="sport in sports"
                  :key="sport.id"
                  :value="sport.id"
                >
                  {{ sport.tenMonTheThao }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Loại mùa</label>
              <select v-model="formData.id_loai_mua" class="form-control">
                <option value="">Chọn loại mùa</option>
                <option
                  v-for="season in seasons"
                  :key="season.id"
                  :value="season.id"
                >
                  {{ season.tenLoaiMua }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Độ bền</label>
              <select v-model="formData.id_do_ben" class="form-control">
                <option value="">Chọn độ bền</option>
                <option
                  v-for="durability in durabilities"
                  :key="durability.id"
                  :value="durability.id"
                >
                  {{ durability.tenDoBen }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Chống nước</label>
              <select v-model="formData.id_chong_nuoc" class="form-control">
                <option value="">Chọn chống nước</option>
                <option
                  v-for="waterproof in waterprooves"
                  :key="waterproof.id"
                  :value="waterproof.id"
                >
                  {{ waterproof.tenChongNuoc }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Giá bán *</label>
              <input
                v-model="formData.gia_ban"
                type="number"
                class="form-control"
                placeholder="Nhập giá bán"
                min="0"
                step="1000"
                required
              />
            </div>

            <div class="form-group">
              <label class="form-label">Số lượng *</label>
              <input
                v-model="formData.so_luong"
                type="number"
                class="form-control"
                placeholder="Nhập số lượng"
                min="0"
                required
              />
            </div>

            <div class="form-group">
              <label class="form-label">Trạng thái</label>
              <select v-model="formData.trang_thai" class="form-control">
                <option :value="1">Hoạt động</option>
                <option :value="0">Ngừng hoạt động</option>
              </select>
            </div>

            <div class="form-group span-2">
              <label class="form-label">Ghi chú</label>
              <textarea
                v-model="formData.ghi_chu"
                class="form-control"
                rows="3"
                placeholder="Nhập ghi chú..."
              ></textarea>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-export" @click="closeModals">
            <span class="btn-icon">❌</span>
            Hủy
          </button>
          <button class="btn-export" @click="saveDetail">
            <span class="btn-icon">💾</span>
            {{ showAddModal ? "Thêm" : "Cập nhật" }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import ButtonGroup from "@/components/ui/NhomNut.vue";
import { fetchAllChiTietSanPham } from "../../services/SanPham/ChiTietSanPhamService";
import { fetchAllSanPham } from "../../services/SanPham/SanPhamService";
import { fetchAllMauSac } from "../../services/ThuocTinh/MauSacService";
import { fetchAllKichThuoc } from "../../services/ThuocTinh/KichThuocService";
import { fetchAllChatLieu } from "../../services/ThuocTinh/ChatLieuService";
import { fetchAllDeGiay } from "../../services/ThuocTinh/DeGiayService";
import { fetchAllDemGiay } from "../../services/ThuocTinh/DemGiayService";
import { fetchAllTrongLuong } from "../../services/ThuocTinh/TrongLuongService";
import { fetchAllMonTheThao } from "../../services/ThuocTinh/MonTheThaoService";
import { fetchAllLoaiMua } from "../../services/ThuocTinh/LoaiMuaService";
import { fetchAllDoBen } from "../../services/ThuocTinh/DoBenService";
import { fetchAllChongNuoc } from "../../services/ThuocTinh/ChongNuocService";

// Reactive data
const searchQuery = ref("");
const selectedProduct = ref("");
const selectedColor = ref("");
const selectedSize = ref("");
const statusFilter = ref("");
const showAddModal = ref(false);
const showEditModal = ref(false);
const editingDetail = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(10);

const formData = ref({});
const productDetails = ref([]);
const newProductDetail = ref({});
const selectedProductDetail = ref({});

// Data for dropdowns
const products = ref([]);
const colors = ref([]);
const sizes = ref([]);
const materials = ref([]);
const soles = ref([]);
const insoles = ref([]);
const weights = ref([]);
const sports = ref([]);
const seasons = ref([]);
const durabilities = ref([]);
const waterprooves = ref([]);
// API call
const fetchProductDetails = async () => {
  try {
    const response = await fetchAllChiTietSanPham();
    productDetails.value = response.data;
  } catch (error) {
    console.error("Error fetching product details:", error);
    loadSampleProductDetails();
  }
};

// Load data for dropdowns
const loadDropdownData = async () => {
  try {
    let res1 = await fetchAllSanPham();
    products.value = res1.data;
    let res2 = await fetchAllMauSac();
    colors.value = res2.data;
    let res3 = await fetchAllKichThuoc();
    sizes.value = res3.data;
    let res4 = await fetchAllChatLieu();
    materials.value = res4.data;
    let res5 = await fetchAllDeGiay();
    soles.value = res5.data;
    let res6 = await fetchAllDemGiay();
    insoles.value = res6.data;
    let res7 = await fetchAllTrongLuong();
    weights.value = res7.data;
    let res8 = await fetchAllMonTheThao();
    sports.value = res8.data;
    let res9 = await fetchAllLoaiMua();
    seasons.value = res9.data;
    let res10 = await fetchAllDoBen();
    durabilities.value = res10.data;
    let res11 = await fetchAllChongNuoc();
    waterprooves.value = res11.data;
  } catch (error) {
    console.error("Error loading dropdown data:", error);
  }
};

// Computed
const filteredDetails = computed(() => {
  return productDetails.value.filter((detail) => {
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

    const matchesProduct =
      !selectedProduct.value || detail.idSanPham == selectedProduct.value;
    const matchesColor =
      !selectedColor.value || detail.idMauSac == selectedColor.value;
    const matchesSize =
      !selectedSize.value || detail.idKichThuoc == selectedSize.value;
    const matchesStatus =
      statusFilter.value === "" || detail.trangThai == statusFilter.value;

    return (
      matchesSearch &&
      matchesProduct &&
      matchesColor &&
      matchesSize &&
      matchesStatus
    );
  });
});

const totalPages = computed(() =>
  Math.ceil(filteredDetails.value.length / itemsPerPage.value)
);
const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);
const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, filteredDetails.value.length)
);

const paginatedDetails = computed(() => {
  return filteredDetails.value.slice(
    startIndex.value,
    startIndex.value + itemsPerPage.value
  );
});

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
  const colorMap = {
    Đen: "#000000",
    Trắng: "#FFFFFF",
    Đỏ: "#FF0000",
    Xanh: "#0000FF",
    Vàng: "#FFFF00",
    Xám: "#808080",
    Nâu: "#8B4513",
    Hồng: "#FFC0CB",
  };
  return colorMap[colorName] || "#E5E7EB";
};

const editDetail = (detail) => {
  editingDetail.value = detail;
  formData.value = {
    ...detail,
    id_san_pham: detail.id_san_pham,
    id_mau_sac: detail.id_mau_sac,
    id_kich_thuoc: detail.id_kich_thuoc,
    id_chat_lieu: detail.id_chat_lieu,
    id_de_giay: detail.id_de_giay,
    id_dem_giay: detail.id_dem_giay,
    id_trong_luong: detail.id_trong_luong,
    id_mon_the_thao: detail.id_mon_the_thao,
    id_loai_mua: detail.id_loai_mua,
    id_do_ben: detail.id_do_ben,
    id_chong_nuoc: detail.id_chong_nuoc,
  };
  showEditModal.value = true;
};

const deleteDetail = (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa chi tiết sản phẩm này?")) {
    const index = productDetails.value.findIndex((d) => d.id === id);
    if (index > -1) {
      productDetails.value.splice(index, 1);
    }
  }
};

const saveDetail = () => {
  if (
    !formData.value.id_san_pham ||
    !formData.value.id_mau_sac ||
    !formData.value.id_kich_thuoc
  ) {
    alert("Vui lòng nhập đầy đủ thông tin bắt buộc");
    return;
  }

  if (showAddModal.value) {
    const newDetail = {
      ...formData.value,
      id: Date.now(),
      tenSanPham: products.value.find((p) => p.id == formData.value.id_san_pham)
        ?.tenSanPham,
      tenMauSac: colors.value.find((c) => c.id == formData.value.id_mau_sac)
        ?.tenMauSac,
      tenKichThuoc: sizes.value.find(
        (s) => s.id == formData.value.id_kich_thuoc
      )?.tenKichThuoc,
      tenChatLieu: materials.value.find(
        (m) => m.id == formData.value.id_chat_lieu
      )?.tenChatLieu,
    };
    productDetails.value.unshift(newDetail);
  } else if (showEditModal.value && editingDetail.value) {
    const index = productDetails.value.findIndex(
      (d) => d.id === editingDetail.value.id
    );
    if (index > -1) {
      productDetails.value[index] = {
        ...editingDetail.value,
        ...formData.value,
        tenSanPham: products.value.find(
          (p) => p.id == formData.value.id_san_pham
        )?.tenSanPham,
        tenMauSac: colors.value.find((c) => c.id == formData.value.id_mau_sac)
          ?.tenMauSac,
        tenKichThuoc: sizes.value.find(
          (s) => s.id == formData.value.id_kich_thuoc
        )?.tenKichThuoc,
        tenChatLieu: materials.value.find(
          (m) => m.id == formData.value.id_chat_lieu
        )?.tenChatLieu,
      };
    }
  }

  closeModals();
};

const closeModals = () => {
  showAddModal.value = false;
  showEditModal.value = false;
  editingDetail.value = null;
  formData.value = {
    id_san_pham: "",
    id_mau_sac: "",
    id_kich_thuoc: "",
    id_chat_lieu: "",
    id_de_giay: "",
    id_dem_giay: "",
    id_trong_luong: "",
    id_mon_the_thao: "",
    id_loai_mua: "",
    id_do_ben: "",
    id_chong_nuoc: "",
    so_luong: 0,
    gia_ban: 0,
    trang_thai: 1,
    ghi_chu: "",
  };
};

const clearFilters = () => {
  searchQuery.value = "";
  selectedProduct.value = "";
  selectedColor.value = "";
  selectedSize.value = "";
  statusFilter.value = "";
  currentPage.value = 1;
};

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
};

const refreshData = () => {
  console.log("Refreshing product details data...");
  fetchProductDetails();
};

const exportData = () => {
  console.log("Exporting product details report...");
  alert("Xuất báo cáo thành công! (Chức năng đang được phát triển)");
};

const exportDetailsToExcel = () => {
  console.log("Exporting product details to Excel...");
  alert("Xuất Excel thành công! (Chức năng đang được phát triển)");
};

onMounted(() => {
  fetchProductDetails();
  loadDropdownData();
  setTimeout(() => {
    if (products.value.length === 0) loadSampleData();
  }, 2000);
});
</script>

<style scoped>
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
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  backdrop-filter: blur(10px);
}

.btn-refresh:hover,
.btn-export:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.btn-icon {
  font-size: 1rem;
}

.search-section {
  margin-bottom: 1.5rem;
}

.input-group {
  position: relative;
  max-width: 500px;
}

.search-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 3rem;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 1rem;
  background: white;
  transition: all 0.2s ease;
}

.search-input:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

.input-icon {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
  font-size: 1.1rem;
}

.clear-btn {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.clear-btn:hover {
  background: #f3f4f6;
  color: #6b7280;
}

/* Filter Section */
.filter-section {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.filter-header {
  margin-bottom: 1.5rem;
}

.filter-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.filter-title h3 {
  margin: 0;
  color: #1e293b;
  font-size: 1.25rem;
}

.filter-icon {
  font-size: 1.5rem;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}

.filter-group {
  display: flex;
  flex-direction: column;
}

.filter-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #475569;
  font-size: 0.875rem;
}

.label-icon {
  font-size: 1rem;
}

.form-select {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.875rem;
  background: white;
  transition: border-color 0.2s ease;
}

.form-select:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

.filter-actions {
  display: flex;
  gap: 0.75rem;
  align-items: end;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  font-weight: 500;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
}

.btn-primary {
  background: #4ade80;
  color: white;
}

.btn-primary:hover {
  background: #22c55e;
  transform: translateY(-1px);
}

.btn-outline {
  background: white;
  color: #475569;
  border: 1px solid #d1d5db;
}

.btn-outline:hover {
  background: #f8fafc;
  border-color: #9ca3af;
}

/* Table Container */
.table-container {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: white;
  overflow: hidden;
}

/* Product Table */
.product-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  font-size: 0.8rem;
}

.product-table th {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  font-weight: 600;
  padding: 0.75rem 0.5rem;
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
  padding: 0.75rem 0.5rem;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #f1f5f9;
  border-right: 1px solid #f1f5f9;
  background: white;
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

/* Column Widths */
.stt-col {
  width: 60px;
  min-width: 60px;
}
.product-col {
  width: 180px;
  min-width: 180px;
}
.color-col {
  width: 90px;
  min-width: 90px;
}
.size-col {
  width: 70px;
  min-width: 70px;
}
.sole-col {
  width: 100px;
  min-width: 100px;
}
.material-col {
  width: 100px;
  min-width: 100px;
}
.insole-col {
  width: 100px;
  min-width: 100px;
}
.weight-col {
  width: 90px;
  min-width: 90px;
}
.sport-col {
  width: 100px;
  min-width: 100px;
}
.season-col {
  width: 90px;
  min-width: 90px;
}
.durability-col {
  width: 90px;
  min-width: 90px;
}
.waterproof-col {
  width: 100px;
  min-width: 100px;
}
.image-col {
  width: 90px;
  min-width: 90px;
}
.quantity-col {
  width: 70px;
  min-width: 70px;
}
.price-col {
  width: 100px;
  min-width: 100px;
}
.status-col {
  width: 100px;
  min-width: 100px;
}
.action-col {
  width: 100px;
  min-width: 100px;
}

/* Product Info */
.product-col {
  text-align: left !important;
}

.product-info strong {
  display: block;
  margin-bottom: 0.25rem;
  font-size: 0.85rem;
  color: #1e293b;
}

/* Color Badge */
.color-badge {
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  color: white;
  font-weight: 500;
  font-size: 0.7rem;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(0, 0, 0, 0.1);
  display: inline-block;
  min-width: 60px;
}

/* Size Badge */
.size-badge {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.75rem;
  display: inline-block;
  min-width: 40px;
}

/* Attribute Text */
.attribute-text {
  font-size: 0.75rem;
  color: #475569;
  font-weight: 500;
  display: block;
  line-height: 1.3;
}

/* Stock Badge */
.stock-badge {
  background: #10b981;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.75rem;
  display: inline-block;
  min-width: 40px;
}

.stock-badge.low-stock {
  background: #ef4444;
}

/* Price Text */
.price-text {
  font-weight: 600;
  color: #059669;
  font-size: 0.8rem;
}

/* Status Badge */
.status-badge {
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  font-weight: 500;
  font-size: 0.7rem;
  display: inline-block;
  min-width: 80px;
}

.status-active {
  background: #10b981;
  color: white;
}

.status-inactive {
  background: #ef4444;
  color: white;
}

/* Product Images */
.image-col {
  text-align: center;
}

.image-preview {
  position: relative;
  display: inline-block;
}

.product-image {
  width: 35px;
  height: 35px;
  object-fit: cover;
  border-radius: 4px;
  border: 2px solid #e2e8f0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.image-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.65rem;
  font-weight: 600;
  border: 2px solid white;
}

.no-image {
  color: #94a3b8;
  font-size: 0.7rem;
  font-style: italic;
}

/* Action Buttons */
.action-buttons {
  display: flex;
  gap: 0.5rem;
  justify-content: center;
}

.btn-edit,
.btn-delete {
  border: none;
  border-radius: 6px;
  padding: 0.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 32px;
  height: 32px;
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
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.modal-content.large {
  max-width: 1000px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  margin: 0;
  color: var(--secondary-color);
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.3s ease;
}

.modal-close:hover {
  background-color: var(--light-gray);
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid var(--border-color);
}

/* Form styles */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-grid .form-group:nth-child(13),
.form-grid .form-group:nth-child(14) {
  grid-column: span 2;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group.span-2 {
  grid-column: span 2;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: var(--secondary-color);
}

/* Responsive Design */
@media (max-width: 1200px) {
  .product-details {
    padding: 0 1rem;
  }

  .page-header {
    padding: 1.5rem;
  }

  .page-title {
    font-size: 1.75rem;
  }

  .header-actions {
    gap: 0.5rem;
  }

  .btn-refresh,
  .btn-export {
    padding: 0.6rem 0.8rem;
    font-size: 0.875rem;
  }

  /* Responsive table cho tablet */
  .product-table {
    font-size: 0.75rem;
  }

  .product-table th,
  .product-table td {
    padding: 0.6rem 0.4rem;
  }

  /* Giảm width các cột trên tablet */
  .stt-col {
    width: 50px;
    min-width: 50px;
  }
  .product-col {
    width: 160px;
    min-width: 160px;
  }
  .color-col {
    width: 80px;
    min-width: 80px;
  }
  .size-col {
    width: 60px;
    min-width: 60px;
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
    width: 80px;
    min-width: 80px;
  }
  .quantity-col {
    width: 60px;
    min-width: 60px;
  }
  .price-col {
    width: 90px;
    min-width: 90px;
  }
  .status-col {
    width: 90px;
    min-width: 90px;
  }
  .action-col {
    width: 90px;
    min-width: 90px;
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

  .filters-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .table-container {
    margin: 0 -1rem;
    border-radius: 0;
  }

  /* Responsive table cho mobile */
  .product-table {
    font-size: 0.7rem;
  }

  .product-table th,
  .product-table td {
    padding: 0.5rem 0.25rem;
  }

  /* Giảm width các cột trên mobile */
  .stt-col {
    width: 40px;
    min-width: 40px;
  }
  .product-col {
    width: 120px;
    min-width: 120px;
  }
  .color-col {
    width: 70px;
    min-width: 70px;
  }
  .size-col {
    width: 50px;
    min-width: 50px;
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
    width: 70px;
    min-width: 70px;
  }
  .quantity-col {
    width: 50px;
    min-width: 50px;
  }
  .price-col {
    width: 80px;
    min-width: 80px;
  }
  .status-col {
    width: 80px;
    min-width: 80px;
  }
  .action-col {
    width: 80px;
    min-width: 80px;
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
    font-size: 0.65rem;
  }

  .product-table th,
  .product-table td {
    padding: 0.4rem 0.2rem;
  }

  /* Giảm width các cột trên mobile nhỏ */
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
    width: 70px;
    min-width: 70px;
  }

  .color-badge,
  .size-badge,
  .stock-badge,
  .status-badge {
    padding: 0.2rem 0.4rem;
    font-size: 0.65rem;
  }

  .product-image {
    width: 30px;
    height: 30px;
  }

  .btn-edit,
  .btn-delete {
    min-width: 28px;
    height: 28px;
  }
}
</style>
