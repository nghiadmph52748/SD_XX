<template>
  <div class="discount-campaigns">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">🎯 Quản lý chiến dịch khuyến mãi</h1>
          <p class="page-subtitle">Tạo và quản lý các chiến dịch khuyến mãi một cách hiệu quả</p>
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
          <button class="btn-export" @click="exportToExcel">
            <span class="btn-icon">📗</span>
            Xuất Excel
          </button>
          <button class="btn-export" @click="openAddModal">
            <span class="btn-icon">➕</span>
            Tạo đợt giảm giá
          </button>
        </div>
      </div>
    </div>

    <!-- Modern Filter Section -->
    <div class="filter-section">
      <div class="filter-card">
        <div class="filter-header">
          <div class="filter-title">
            <span class="filter-icon">🎯</span>
            <h3>Tìm kiếm chiến dịch</h3>
          </div>
          <div class="filter-stats">
            {{ filteredCampaigns.length }} / {{ campaigns.length }} chiến dịch
          </div>
        </div>

        <div class="filter-content">
          <div class="search-section">
            <div class="input-group">
              <span class="input-icon">🔍</span>
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Tìm kiếm theo tên hoặc mô tả chiến dịch..."
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
                <span class="label-icon">📊</span>
                Trạng thái
              </label>
              <select v-model="statusFilter" class="form-select">
                <option value="">Tất cả trạng thái</option>
                <option value="upcoming">⏰ Sắp diễn ra</option>
                <option value="active">✅ Đang diễn ra</option>
                <option value="expired">🔚 Đã kết thúc</option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">💰</span>
                Loại giảm giá
              </label>
              <select v-model="typeFilter" class="form-select">
                <option value="">Tất cả loại</option>
                <option value="percentage">📊 Phần trăm (%)</option>
                <option value="fixed">💵 Số tiền cố định</option>
              </select>
            </div>

            <div class="filter-actions">
              <button @click="clearFilters" class="btn btn-outline">
                <span class="btn-icon">🔄</span>
                Đặt lại
              </button>
              <button @click="applyFilters" class="btn btn-primary">
                <span class="btn-icon">✨</span>
                Áp dụng
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Campaigns Table -->
    <div class="card">
      <div class="card-body">
        <table class="table">
          <thead>
            <tr>
              <th>STT</th>
              <th>Tên đợt giảm giá</th>
              <th>Giá trị giảm giá</th>
              <th>Thời gian</th>
              <th>Hiện trạng</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(campaign, index) in filteredCampaigns"
              :key="campaign.id"
              class="campaign-row"
            >
              <td class="stt-cell">{{ startIndex + index + 1 }}</td>
              <td>
                <div class="campaign-name">
                  <strong>{{ campaign.tenDotGiamGia }}</strong>
                  <div class="campaign-code">{{ campaign.maDotGiamGia }}</div>
                </div>
              </td>
              <td>
                <div class="campaign-description">
                  <span class="discount-value">{{ campaign.giaTriGiamGia || "0" }}%</span>
                  <div class="discount-type">Giảm giá theo phần trăm</div>
                </div>
              </td>
              <td>
                <div class="date-info">
                  <div class="date-range">
                    📅 {{ formatDateShort(campaign.ngayBatDau) }}
                  </div>
                  <div class="date-separator">↓</div>
                  <div class="date-range">
                    📅 {{ formatDateShort(campaign.ngayKetThuc) }}
                  </div>
                </div>
              </td>
              <td>
                <span
                  :class="[
                    'badge',
                    campaign.trangThai ? 'badge-success' : 'badge-warning',
                  ]"
                >
                  {{ campaign.trangThai ? "Đang diễn ra" : "Sắp diễn ra" }}
                </span>
              </td>
              <td>
                <span
                  :class="[
                    'badge',
                    !campaign.deleted ? 'badge-success' : 'badge-danger',
                  ]"
                >
                  {{ !campaign.deleted ? "Hoạt động" : "Ngừng hoạt động" }}
                </span>
              </td>
              <td>
                <div class="action-buttons">
                  <button
                    class="btn-action"
                    @click="viewCampaign(campaign)"
                    title="Xem chi tiết"
                  >
                    👁️
                  </button>
                  <button
                    class="btn-action"
                    @click="editCampaign(campaign)"
                    title="Chỉnh sửa"
                  >
                    ✏️
                  </button>
                  <button
                    v-if="campaign.status !== 'expired'"
                    class="btn-action btn-delete"
                    @click="deleteCampaign(campaign.id)"
                    title="Xóa"
                  >
                    🗑️
                  </button>
                  <button
                    v-if="campaign.trangThai"
                    class="btn-action btn-apply"
                    @click="openApplyModal(campaign)"
                    title="Áp dụng"
                  >
                    ✅
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="filteredCampaigns.length === 0">
              <td colspan="9" class="text-center empty-state">
                <div class="empty-message">
                  <span class="empty-icon">📭</span>
                  <p>Không có dữ liệu chiến dịch</p>
                  <small>Hãy tạo chiến dịch đầu tiên để bắt đầu</small>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Pagination -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của
            {{ totalCampaigns }} đợt giảm giá
          </div>
          <div class="pagination">
            <button
              class="btn btn-outline btn-sm"
              @click="previousPage"
              :disabled="currentPage === 1"
            >
              ❮ Trước
            </button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button
              class="btn btn-outline btn-sm"
              @click="nextPage"
              :disabled="currentPage === totalPages"
            >
              Sau ❯
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
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>
            {{ showAddModal ? "Tạo đợt giảm giá" : "Cập nhật đợt giảm giá" }}
          </h3>
          <button class="modal-close" @click="closeModals">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label class="form-label">Tên đợt giảm giá *</label>
            <input
              v-model="formData.tenDotGiamGia"
              type="text"
              class="form-control"
              placeholder="Nhập tên đợt giảm giá"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Giá trị giảm giá (%) *</label>
            <input
              v-model.number="formData.giaTriGiamGia"
              type="number"
              class="form-control"
              placeholder="Nhập giá trị giảm giá (0-100)"
              min="0"
              max="100"
              required
            />
            <small class="form-help">Giá trị từ 0% đến 100%</small>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="form-group">
                <label class="form-label">Ngày bắt đầu *</label>
                <input
                  v-model="formData.ngayBatDau"
                  type="date"
                  class="form-control"
                  required
                />
              </div>
            </div>
            <div class="col-6">
              <div class="form-group">
                <label class="form-label">Ngày kết thúc *</label>
                <input
                  v-model="formData.ngayKetThuc"
                  type="date"
                  class="form-control"
                  required
                />
              </div>
            </div>
          </div>

          <div class="form-group" v-if="showEditModal">
            <label class="form-label">Trạng thái *</label>
            <select
              v-model="formData.trangThai"
              class="form-control"
              :disabled="!isWithinCampaignPeriod"
            >
              <option :value="true">Đang diễn ra</option>
              <option :value="false">Sắp diễn ra</option>
            </select>
            <small v-if="!isWithinCampaignPeriod" class="text-muted">
              Chỉ có thể sửa trạng thái khi ngày hiện tại nằm trong thời gian
              đợt giảm giá
            </small>
          </div>

          <div class="form-group" v-if="showEditModal">
            <label class="form-label">Hiện trạng *</label>
            <select v-model="formData.deleted" class="form-control">
              <option :value="false">Hoạt động</option>
              <option :value="true">Ngừng hoạt động</option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-export" @click="closeModals">
            <span class="btn-icon">❌</span>
            Hủy
          </button>
          <button class="btn-export" @click="saveCampaign">
            <span class="btn-icon">💾</span>
            {{ showAddModal ? "Tạo đợt giảm giá" : "Cập nhật" }}
          </button>
        </div>
      </div>
    </div>

    <!-- View Campaign Detail Modal -->
    <div
      v-if="showDetailModal"
      class="modal-overlay"
      @click="showDetailModal = false"
    >
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Chi tiết đợt giảm giá</h3>
          <button class="modal-close" @click="showDetailModal = false">
            ✕
          </button>
        </div>
        <div class="modal-body" v-if="selectedCampaign">
          <div class="campaign-detail">
            <div class="campaign-info">
              <h4>{{ selectedCampaign.tenDotGiamGia }}</h4>
              <div class="info-item">
                <label>Mã đợt giảm giá:</label>
                <span class="campaign-code">{{ selectedCampaign.maDotGiamGia }}</span>
              </div>
              <div class="info-item">
                <label>Giá trị giảm giá:</label>
                <span class="discount-value">
                  {{ selectedCampaign.giaTriGiamGia + "%" }}
                </span>
              </div>
              <div class="info-item">
                <label>Hiện trạng:</label>
                <span
                  class="badge"
                  :class="
                    selectedCampaign.trangThai
                      ? 'badge-success'
                      : 'badge-warning'
                  "
                >
                  {{
                    selectedCampaign.trangThai ? "Hoạt động" : "Ngừng hoạt động"
                  }}
                </span>
              </div>
              <div class="info-item">
                <label>Thời gian bắt đầu:</label>
                <span>{{ formatDate(selectedCampaign.ngayBatDau) }}</span>
              </div>
              <div class="info-item">
                <label>Thời gian kết thúc:</label>
                <span>{{ formatDate(selectedCampaign.ngayKetThuc) }}</span>
              </div>
              <div class="info-item">
                <label>Trạng thái:</label>
                <span>{{
                  selectedCampaign.deleted ? "Ngừng hoạt động" : "Hoạt động"
                }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Apply Discount Modal -->
    <div v-if="showApplyModal" class="modal-overlay" @click="closeApplyModal">
      <div class="modal-content apply-modal" @click.stop>
        <div class="modal-header">
          <h3>Áp dụng đợt giảm giá: {{ applyingCampaign?.tenDotGiamGia }}</h3>
          <button class="modal-close" @click="closeApplyModal">✕</button>
        </div>

        <div class="modal-body">
          <div class="discount-info">
            <div class="info-row">
              <span class="label">Giá trị giảm giá:</span>
              <span class="value">{{ applyingCampaign?.giaTriGiamGia }}%</span>
            </div>
            <div class="info-row">
              <span class="label">Thời gian:</span>
              <span class="value">
                {{ formatDateShort(applyingCampaign?.ngayBatDau) }} -
                {{ formatDateShort(applyingCampaign?.ngayKetThuc) }}
              </span>
            </div>
          </div>

          <div class="product-selection">
            <h4>Chọn sản phẩm chi tiết áp dụng:</h4>
            <div class="product-search">
              <input
                type="text"
                placeholder="Tìm kiếm sản phẩm..."
                class="form-control"
                v-model="searchProductQuery"
              />
            </div>

            <div class="selection-controls">
              <button
                class="btn btn-outline btn-sm"
                @click="selectAllProducts"
                :disabled="
                  selectedProducts.length === availableProducts.length ||
                  availableProducts.length === 0
                "
              >
                ✅ Chọn hết
              </button>
              <button
                class="btn btn-outline btn-sm"
                @click="clearAllProducts"
                :disabled="selectedProducts.length === 0"
              >
                ❌ Bỏ chọn hết
              </button>
            </div>

            <div class="product-list">
              <!-- Products already in current campaign -->
              <div
                v-if="currentCampaignProducts.length > 0"
                class="product-section"
              >
                <h5 class="section-title current-campaign-title">
                  🔄 Sản phẩm đã áp dụng cho đợt này ({{
                    currentCampaignProducts.length
                  }})
                </h5>
                <div
                  v-for="product in currentCampaignProducts"
                  :key="'current-' + product.id"
                  class="product-item current-campaign-product"
                  @click="toggleProductSelection(product.id)"
                >
                  <input
                    type="checkbox"
                    :checked="selectedProducts.includes(product.id)"
                    @click.stop="toggleProductSelection(product.id)"
                  />
                  <div class="product-info">
                    <div class="product-name">{{ product.tenSanPham }}</div>
                    <div class="product-details">
                      <div class="details-grid">
                        <div class="detail-column">
                          <div class="detail-row">
                            <span class="detail-label">Màu sắc:</span>
                            <span class="detail-value">{{
                              product.tenMauSac
                            }}</span>
                          </div>
                          <div class="detail-row">
                            <span class="detail-label">Kích thước:</span>
                            <span class="detail-value">{{
                              product.tenKichThuoc
                            }}</span>
                          </div>
                        </div>
                        <div class="detail-column">
                          <div class="detail-row">
                            <span class="detail-label">Trạng thái:</span>
                            <span class="detail-value current-campaign"
                              >Đã áp dụng</span
                            >
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="product-price">
                      {{
                        new Intl.NumberFormat("vi-VN").format(product.giaBan)
                      }}
                      VNĐ
                    </div>
                  </div>
                </div>
              </div>

              <!-- Available Products -->
              <div
                v-if="availableProducts.length > currentCampaignProducts.length"
                class="product-section"
              >
                <h5 class="section-title available-title">
                  ✅ Sản phẩm có thể áp dụng ({{
                    availableProducts.length - currentCampaignProducts.length
                  }})
                </h5>
                <div
                  v-for="product in availableProducts.filter(
                    (p) => !getProductCurrentCampaignStatus(p.id)
                  )"
                  :key="product.id"
                  class="product-item available-product"
                  @click="toggleProductSelection(product.id)"
                >
                  <input
                    type="checkbox"
                    :checked="selectedProducts.includes(product.id)"
                    @click.stop="toggleProductSelection(product.id)"
                  />
                  <div class="product-info">
                    <div class="product-name">{{ product.tenSanPham }}</div>
                    <div class="product-details">
                      <div class="details-grid">
                        <div class="detail-column">
                          <div class="detail-row">
                            <span class="detail-label">Màu sắc:</span>
                            <span class="detail-value">{{
                              product.tenMauSac
                            }}</span>
                          </div>
                          <div class="detail-row">
                            <span class="detail-label">Kích thước:</span>
                            <span class="detail-value">{{
                              product.tenKichThuoc
                            }}</span>
                          </div>
                          <div class="detail-row" v-if="product.tenChatLieu">
                            <span class="detail-label">Chất liệu:</span>
                            <span class="detail-value">{{
                              product.tenChatLieu
                            }}</span>
                          </div>
                          <div class="detail-row" v-if="product.tenDoBen">
                            <span class="detail-label">Độ bền:</span>
                            <span class="detail-value">{{
                              product.tenDoBen
                            }}</span>
                          </div>
                          <div class="detail-row" v-if="product.tenTrongLuong">
                            <span class="detail-label">Trọng lượng:</span>
                            <span class="detail-value">{{
                              product.tenTrongLuong
                            }}</span>
                          </div>
                        </div>

                        <div class="detail-column">
                          <div class="detail-row" v-if="product.tenDeGiay">
                            <span class="detail-label">Đế giày:</span>
                            <span class="detail-value">{{
                              product.tenDeGiay
                            }}</span>
                          </div>
                          <div class="detail-row" v-if="product.tenDemGiay">
                            <span class="detail-label">Đệm giày:</span>
                            <span class="detail-value">{{
                              product.tenDemGiay
                            }}</span>
                          </div>
                          <div class="detail-row" v-if="product.tenLoaiMua">
                            <span class="detail-label">Loại mùa:</span>
                            <span class="detail-value">{{
                              product.tenLoaiMua
                            }}</span>
                          </div>
                          <div class="detail-row" v-if="product.tenMonTheThao">
                            <span class="detail-label">Môn thể thao:</span>
                            <span class="detail-value">{{
                              product.tenMonTheThao
                            }}</span>
                          </div>
                          <div class="detail-row" v-if="product.tenChongNuoc">
                            <span class="detail-label">Chống nước:</span>
                            <span class="detail-value">{{
                              product.tenChongNuoc
                            }}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="product-price">
                      {{
                        new Intl.NumberFormat("vi-VN").format(product.giaBan)
                      }}
                      VNĐ
                    </div>
                  </div>
                </div>
              </div>

              <!-- Products with existing discount -->
              <div
                v-if="productsWithDiscount.length > 0"
                class="product-section"
              >
                <h5 class="section-title unavailable-title">
                  ❌ Sản phẩm đã có giảm giá ({{ productsWithDiscount.length }})
                </h5>
                <div
                  v-for="product in productsWithDiscount"
                  :key="product.id"
                  class="product-item unavailable-product"
                >
                  <input type="checkbox" disabled class="disabled-checkbox" />
                  <div class="product-info">
                    <div class="product-name disabled-text">
                      {{ product.tenSanPham }}
                    </div>
                    <div class="product-details">
                      <div class="details-grid">
                        <div class="detail-column">
                          <div class="detail-row">
                            <span class="detail-label">Màu sắc:</span>
                            <span class="detail-value">{{
                              product.tenMauSac
                            }}</span>
                          </div>
                          <div class="detail-row">
                            <span class="detail-label">Kích thước:</span>
                            <span class="detail-value">{{
                              product.tenKichThuoc
                            }}</span>
                          </div>
                        </div>
                        <div class="detail-column">
                          <div class="detail-row">
                            <span class="detail-label">Trạng thái:</span>
                            <span class="detail-value discount-active"
                              >Đang giảm giá</span
                            >
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="product-price disabled-text">
                      {{
                        new Intl.NumberFormat("vi-VN").format(product.giaBan)
                      }}
                      VNĐ
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div
              v-if="
                availableProducts.length === 0 &&
                productsWithDiscount.length === 0 &&
                currentCampaignProducts.length === 0
              "
              class="empty-products"
            >
              <p>Không có sản phẩm nào</p>
            </div>

            <div
              v-if="
                availableProducts.length === currentCampaignProducts.length &&
                productsWithDiscount.length > 0
              "
              class="empty-available"
            >
              <p class="warning-text">
                ⚠️ Các sản phẩm còn lại đều đã có đợt giảm giá khác đang hoạt
                động
              </p>
            </div>
          </div>

          <div class="selection-summary">
            <div class="selected-count">
              Đã chọn: {{ selectedProducts.length }} sản phẩm
            </div>
            <div class="available-count">
              Có thể áp dụng: {{ availableProducts.length }} /
              {{ productsDetails.length }} sản phẩm
              <br />
              <small
                >Đã áp dụng: {{ currentCampaignProducts.length }} | Từ đợt khác:
                {{ productsWithDiscount.length }}</small
              >
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeApplyModal">
            Hủy
          </button>
          <button class="btn btn-primary" @click="applyDiscountToCampaign">
            Xác nhận áp dụng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import {
  fetchAllDotGiamGia,
  fetchCreateDotGiamGia,
  fetchUpdateDotGiamGia,
  fetchUpdateStatusDotGiamGia,
} from "../../services/GiamGia/DotGiamGiaService";
import {
  fetchAllChiTietDotGiamGia,
  fetchCreateChiTietDotGiamGia,
  fetchUpdateStatusChiTietDotGiamGia,
} from "../../services/GiamGia/ChiTietDotGiamGiaService";
import { fetchAllChiTietSanPham } from "../../services/SanPham/ChiTietSanPhamService";

// Reactive data
const searchQuery = ref("");
const statusFilter = ref("");
const typeFilter = ref("");
const showAddModal = ref(false);
const showEditModal = ref(false);
const showDetailModal = ref(false);
const showApplyModal = ref(false);
const editingCampaign = ref(null);
const selectedCampaign = ref(null);
const applyingCampaign = ref(null);
const selectedProducts = ref([]);

// Pagination data
const currentPage = ref(1);
const itemsPerPage = ref(10);

// Form data for campaign
const formData = ref({
  tenDotGiamGia: "",
  giaTriGiamGia: 0,
  ngayBatDau: "",
  ngayKetThuc: "",
  trangThai: true,
  deleted: false,
});

// Mock data
const campaigns = ref([]);
const campaignDetail = ref([]);
const productsDetails = ref([]);

// Apply form data
const applyFormData = ref({
  idDotGiamGia: null,
  selectedProductIds: [],
});
const fetchDGG = async () => {
  try {
    const res = await fetchAllDotGiamGia();
    // Validate and update status for all campaigns
    campaigns.value = res.data.map((campaign) =>
      validateCampaignStatus(campaign)
    );

    // After loading campaigns, validate inactive ones and update their details
    setTimeout(async () => {
      await validateInactiveCampaigns();
    }, 500); // Small delay to ensure campaign details are loaded
  } catch (error) {
    console.error("Error fetching dot giam gia:", error);
  }
};
const fetchChiTietDGG = async () => {
  try {
    const res = await fetchAllChiTietDotGiamGia();
    campaignDetail.value = res.data;
  } catch (error) {
    console.error("Error fetching chi tiet dot giam gia: ", error);
  }
};
const fetchProductsDetails = async () => {
  try {
    const res = await fetchAllChiTietSanPham();
    productsDetails.value = res.data;
  } catch (error) {
    console.error("Error fetching products details: ", error);
  }
};
// Computed
const filteredCampaigns = computed(() => {
  let filtered = campaigns.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (campaign) =>
        campaign.tenDotGiamGia
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        campaign.maDotGiamGia
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())
    );
  }

  if (statusFilter.value) {
    const isActive = statusFilter.value === "active";
    filtered = filtered.filter((campaign) => campaign.trangThai === isActive);
  }

  return filtered.slice(startIndex.value, endIndex.value);
});

// Total campaigns after filtering (for pagination)
const totalCampaigns = computed(() => {
  let filtered = campaigns.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (campaign) =>
        campaign.tenDotGiamGia
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        campaign.maDotGiamGia
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())
    );
  }

  if (statusFilter.value) {
    const isActive = statusFilter.value === "active";
    filtered = filtered.filter((campaign) => campaign.trangThai === isActive);
  }
  return filtered.length;
});

// Pagination computed properties
const totalPages = computed(() =>
  Math.ceil(totalCampaigns.value / itemsPerPage.value)
);

const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);

const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, totalCampaigns.value)
);

// Check if current date is within campaign period
const isWithinCampaignPeriod = computed(() => {
  if (!editingCampaign.value) return false;

  const now = new Date();
  const startDate = new Date(editingCampaign.value.ngayBatDau);
  const endDate = new Date(editingCampaign.value.ngayKetThuc);

  return now >= startDate && now <= endDate;
});

// Validate and auto-update campaign status based on dates
const validateCampaignStatus = (campaign) => {
  const now = new Date();
  const startDate = new Date(campaign.ngayBatDau);
  const endDate = new Date(campaign.ngayKetThuc);

  // If current date is not within campaign period, set status to false
  if (!(now >= startDate && now <= endDate)) {
    campaign.trangThai = false;
  }

  return campaign;
};

// Update campaign detail status when campaign becomes inactive
const updateCampaignDetailsStatus = async (campaignId) => {
  try {
    // Find all campaign details for this campaign
    const relatedDetails = campaignDetail.value.filter(
      (detail) => detail.idDotGiamGia === campaignId && !detail.deleted
    );

    // Update status for each detail
    for (const detail of relatedDetails) {
      await fetchUpdateStatusChiTietDotGiamGia(detail.id);
    }
  } catch (error) {
    console.error("Error updating campaign details status:", error);
  }
};

// Validate inactive campaigns and update their details
const validateInactiveCampaigns = async () => {
  const inactiveCampaigns = campaigns.value.filter(
    (campaign) => !campaign.trangThai
  );

  for (const campaign of inactiveCampaigns) {
    await updateCampaignDetailsStatus(campaign.id);
  }

  if (inactiveCampaigns.length > 0) {
    console.log(
      `Validated and updated ${inactiveCampaigns.length} inactive campaigns`
    );
    // Refresh campaign details after updates
    await fetchChiTietDGG();
  }
};

// Methods
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("vi-VN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const formatDateShort = (dateString) => {
  return new Date(dateString).toLocaleDateString("vi-VN", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
};

const viewCampaign = (campaign) => {
  selectedCampaign.value = campaign;
  showDetailModal.value = true;
};

const editCampaign = (campaign) => {
  // Validate and auto-update campaign status before editing
  const validatedCampaign = validateCampaignStatus({ ...campaign });

  editingCampaign.value = validatedCampaign;
  formData.value = {
    tenDotGiamGia: validatedCampaign.tenDotGiamGia,
    giaTriGiamGia: validatedCampaign.giaTriGiamGia,
    ngayBatDau: validatedCampaign.ngayBatDau,
    ngayKetThuc: validatedCampaign.ngayKetThuc,
    trangThai: validatedCampaign.trangThai,
    deleted: validatedCampaign.deleted || false,
  };
  showEditModal.value = true;
};

const deleteCampaign = async (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa đợt giảm giá này?")) {
    try {
      await fetchUpdateStatusDotGiamGia(id);
      await fetchDGG();
      alert("Xóa đợt giảm giá thành công!");
    } catch (error) {
      console.error("Lỗi khi xóa đợt giảm giá:", error);
      alert("Có lỗi xảy ra khi xóa đợt giảm giá");
    }
  }
};

const saveCampaign = async () => {
  try {
    // Validate form data
    if (!formData.value.tenDotGiamGia.trim()) {
      alert("Vui lòng nhập tên đợt giảm giá");
      return;
    }

    if (!formData.value.giaTriGiamGia || formData.value.giaTriGiamGia <= 0) {
      alert("Vui lòng nhập giá trị giảm giá hợp lệ");
      return;
    }

    if (!formData.value.ngayBatDau || !formData.value.ngayKetThuc) {
      alert("Vui lòng chọn ngày bắt đầu và kết thúc");
      return;
    }

    if (
      new Date(formData.value.ngayBatDau) >=
      new Date(formData.value.ngayKetThuc)
    ) {
      alert("Ngày kết thúc phải sau ngày bắt đầu");
      return;
    }

    // Auto-validate status based on dates before saving
    const now = new Date();
    const startDate = new Date(formData.value.ngayBatDau);
    const endDate = new Date(formData.value.ngayKetThuc);

    // If current date is not within campaign period, force status to false
    if (!(now >= startDate && now <= endDate)) {
      formData.value.trangThai = false;
    }

    if (showAddModal.value) {
      await fetchCreateDotGiamGia(formData.value);
      currentPage.value = 1; // Reset to first page
      alert("Thêm đợt giảm giá thành công!");
    } else if (showEditModal.value && editingCampaign.value) {
      const oldStatus = editingCampaign.value.trangThai;
      await fetchUpdateDotGiamGia(editingCampaign.value.id, formData.value);

      // If campaign status changed from true to false, update related details
      if (oldStatus && !formData.value.trangThai) {
        await updateCampaignDetailsStatus(editingCampaign.value.id);
        alert(
          "Cập nhật đợt giảm giá thành công! Các sản phẩm liên quan đã được vô hiệu hóa."
        );
      } else {
        alert("Cập nhật đợt giảm giá thành công!");
      }
    }

    // Refresh data after save
    await fetchDGG();
    closeModals();
  } catch (error) {
    console.error("Lỗi khi lưu đợt giảm giá:", error);
    alert("Có lỗi xảy ra khi lưu thông tin đợt giảm giá");
  }
};

const closeModals = () => {
  showAddModal.value = false;
  showEditModal.value = false;
  showDetailModal.value = false;
  editingCampaign.value = null;
  selectedCampaign.value = null;
  resetForm();
};

const resetForm = () => {
  formData.value = {
    tenDotGiamGia: "",
    giaTriGiamGia: 0,
    ngayBatDau: "",
    ngayKetThuc: "",
    trangThai: true,
    deleted: false,
  };
};

// Search for products in apply modal
const searchProductQuery = ref("");

// Check if product already has active discount
const getProductDiscountStatus = (productId) => {
  return campaignDetail.value.find(
    (detail) => detail.idChiTietSanPham === productId && !detail.deleted
  );
};

// Check if product belongs to current campaign
const getProductCurrentCampaignStatus = (productId) => {
  if (!applyingCampaign.value) return null;
  return campaignDetail.value.find(
    (detail) =>
      detail.idChiTietSanPham === productId &&
      detail.idDotGiamGia === applyingCampaign.value.id &&
      !detail.deleted
  );
};

// Get available products (not having active discount from OTHER campaigns)
const availableProducts = computed(() => {
  return productsDetails.value.filter((product) => {
    const discountStatus = getProductDiscountStatus(product.id);
    const currentCampaignStatus = getProductCurrentCampaignStatus(product.id);

    // Allow if no discount OR discount is from current campaign
    return !discountStatus || currentCampaignStatus;
  });
});

// Get products with existing discount from OTHER campaigns
const productsWithDiscount = computed(() => {
  return productsDetails.value.filter((product) => {
    const discountStatus = getProductDiscountStatus(product.id);
    const currentCampaignStatus = getProductCurrentCampaignStatus(product.id);

    // Show as unavailable only if has discount AND not from current campaign
    return discountStatus && !currentCampaignStatus;
  });
});

// Get products already applied to current campaign
const currentCampaignProducts = computed(() => {
  return productsDetails.value.filter((product) =>
    getProductCurrentCampaignStatus(product.id)
  );
});

const clearFilters = () => {
  searchQuery.value = "";
  statusFilter.value = "";
  typeFilter.value = "";
};

const applyFilters = () => {
  // Filters are already applied through computed property
  console.log("Filters applied");
};

const exportData = () => {
  alert("Xuất báo cáo chiến dịch khuyến mãi");
};

const exportToExcel = () => {
  try {
    const headerMapping = {
      id: "ID",
      name: "Tên chiến dịch",
      description: "Mô tả",
      discount_type: "Loại giảm giá",
      discount_value: "Giá trị giảm",
      min_order_value: "Giá trị đơn tối thiểu",
      max_uses: "Số lần sử dụng tối đa",
      used_count: "Đã sử dụng",
      start_date: "Ngày bắt đầu",
      end_date: "Ngày kết thúc",
      status: "Trạng thái",
    };

    const filteredData = filteredCampaigns.value.map((item) => ({
      id: item.id || "N/A",
      name: item.name || "N/A",
      description: item.description || "N/A",
      discount_type:
        item.discount_type === "percentage" ? "Phần trăm" : "Số tiền cố định",
      discount_value:
        item.discount_type === "percentage"
          ? `${item.discount_value}%`
          : new Intl.NumberFormat("vi-VN").format(item.discount_value),
      min_order_value: item.min_order_value
        ? new Intl.NumberFormat("vi-VN").format(item.min_order_value)
        : "Không giới hạn",
      max_uses: item.max_uses || "Không giới hạn",
      used_count: item.used_count || 0,
      start_date: item.start_date
        ? new Date(item.start_date).toLocaleDateString("vi-VN")
        : "N/A",
      end_date: item.end_date
        ? new Date(item.end_date).toLocaleDateString("vi-VN")
        : "N/A",
      status:
        item.status === "active"
          ? "Đang diễn ra"
          : item.status === "upcoming"
          ? "Sắp diễn ra"
          : "Đã kết thúc",
    }));

    // In a real application, you would use a library like xlsx
    console.log("Export data:", filteredData);
    alert("Xuất Excel thành công! (Chức năng đang được phát triển)");
    return;

    if (result && result.success) {
      alert(`✅ ${result.message}`);
    } else {
      alert(
        `❌ ${result ? result.message : "Có lỗi xảy ra khi xuất file Excel"}`
      );
    }
  } catch (error) {
    console.error("Error exporting to Excel:", error);
    alert(`Có lỗi xảy ra khi xuất file Excel: ${error.message}`);
  }
};

const refreshData = async () => {
  await fetchDGG();
  await fetchChiTietDGG();
  await fetchProductsDetails();
};

// Apply campaign methods
const openApplyModal = (campaign) => {
  applyingCampaign.value = campaign;
  applyFormData.value.idDotGiamGia = campaign.id;

  // Pre-select products already applied to this campaign
  const currentlyAppliedProducts = campaignDetail.value
    .filter((detail) => detail.idDotGiamGia === campaign.id && !detail.deleted)
    .map((detail) => detail.idChiTietSanPham);

  selectedProducts.value = [...currentlyAppliedProducts];
  showApplyModal.value = true;
};

const toggleProductSelection = (productId) => {
  const index = selectedProducts.value.indexOf(productId);
  if (index > -1) {
    selectedProducts.value.splice(index, 1);
  } else {
    selectedProducts.value.push(productId);
  }
};

const applyDiscountToCampaign = async () => {
  // Validate if any selected product already has discount from other campaigns
  const conflictProducts = validateProductSelection();
  if (conflictProducts.length > 0) {
    alert(
      `Các sản phẩm sau đã có đợt giảm giá đang hoạt động:\n${conflictProducts.join(
        ", "
      )}\n\nVui lòng bỏ chọn các sản phẩm này!`
    );
    return;
  }

  try {
    // Get currently applied products for this campaign
    const currentlyAppliedProducts = campaignDetail.value
      .filter(
        (detail) =>
          detail.idDotGiamGia === applyingCampaign.value.id && !detail.deleted
      )
      .map((detail) => detail.idChiTietSanPham);

    // Find products to add (selected but not currently applied)
    const productsToAdd = selectedProducts.value.filter(
      (productId) => !currentlyAppliedProducts.includes(productId)
    );

    // Find products to remove (currently applied but not selected)
    const productsToRemove = currentlyAppliedProducts.filter(
      (productId) => !selectedProducts.value.includes(productId)
    );

    // Add new products
    for (const productId of productsToAdd) {
      const chiTietData = {
        idDotGiamGia: applyFormData.value.idDotGiamGia,
        idChiTietSanPham: productId,
        deleted: false,
      };
      await fetchCreateChiTietDotGiamGia(chiTietData);
    }

    // Remove unselected products
    for (const productId of productsToRemove) {
      const detailToRemove = campaignDetail.value.find(
        (detail) =>
          detail.idDotGiamGia === applyingCampaign.value.id &&
          detail.idChiTietSanPham === productId &&
          !detail.deleted
      );
      if (detailToRemove) {
        await fetchUpdateStatusChiTietDotGiamGia(detailToRemove.id);
      }
    }

    // Check if campaign status is false and update if needed
    if (!applyingCampaign.value.trangThai) {
      await fetchUpdateStatusChiTietDotGiamGia(applyingCampaign.value.id);
    }

    const addedCount = productsToAdd.length;
    const removedCount = productsToRemove.length;
    let message = "Cập nhật đợt giảm giá thành công!";

    if (addedCount > 0 && removedCount > 0) {
      message += ` Đã thêm ${addedCount} và xóa ${removedCount} sản phẩm.`;
    } else if (addedCount > 0) {
      message += ` Đã thêm ${addedCount} sản phẩm.`;
    } else if (removedCount > 0) {
      message += ` Đã xóa ${removedCount} sản phẩm.`;
    }

    alert(message);
    showApplyModal.value = false;
    await refreshData();
  } catch (error) {
    console.error("Lỗi khi áp dụng đợt giảm giá:", error);
    alert("Có lỗi xảy ra khi áp dụng đợt giảm giá");
  }
};

const closeApplyModal = () => {
  showApplyModal.value = false;
  applyingCampaign.value = null;
  selectedProducts.value = [];
  applyFormData.value = {
    idDotGiamGia: null,
    selectedProductIds: [],
  };
};

// Select all products (only available ones)
const selectAllProducts = () => {
  selectedProducts.value = availableProducts.value.map((product) => product.id);
};

// Clear all selected products
const clearAllProducts = () => {
  selectedProducts.value = [];
};

// Validation before applying discount
const validateProductSelection = () => {
  const conflictProducts = [];

  for (const productId of selectedProducts.value) {
    const existingDiscount = getProductDiscountStatus(productId);
    const currentCampaignDiscount = getProductCurrentCampaignStatus(productId);

    // Only conflict if has discount from OTHER campaign
    if (existingDiscount && !currentCampaignDiscount) {
      const product = productsDetails.value.find((p) => p.id === productId);
      conflictProducts.push(product?.tenSanPham || `ID: ${productId}`);
    }
  }

  return conflictProducts;
};

// Pagination methods
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

const openAddModal = () => {
  resetForm();
  showAddModal.value = true;
};
onMounted(async () => {
  await fetchDGG();
  await fetchChiTietDGG();
  await fetchProductsDetails();

  // Run validation after all data is loaded
  setTimeout(async () => {
    await validateInactiveCampaigns();
  }, 1000);
});
</script>

<style scoped>
.discount-campaigns {
  max-width: 1400px;
  margin: 0 auto;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

/* page-header styles are now defined in globals.css */

/* Modern Filter Section */
.filter-section {
  margin-bottom: 2rem;
}

.filter-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(74, 222, 128, 0.1);
  transition: all 0.3s ease;
}

.filter-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  background: linear-gradient(
    135deg,
    rgba(74, 222, 128, 0.05) 0%,
    rgba(34, 197, 94, 0.05) 100%
  );
  border-bottom: 1px solid rgba(74, 222, 128, 0.15);
}

.filter-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.filter-icon {
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  border-radius: 10px;
}

.filter-title h3 {
  margin: 0;
  color: #374151;
  font-size: 1.25rem;
  font-weight: 600;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.filter-stats {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.filter-content {
  padding: 1.5rem;
}

.search-section {
  margin-bottom: 1.5rem;
}

.input-group {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 1rem;
  font-size: 1.25rem;
  z-index: 1;
}

.search-input {
  width: 100%;
  padding: 0.875rem 3rem;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #f9fafb;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.search-input:focus {
  outline: none;
  border-color: #4ade80;
  background: white;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
  transform: translateY(-1px);
}

.clear-btn {
  position: absolute;
  right: 1rem;
  background: #ef4444;
  border: none;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: white;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.clear-btn:hover {
  background: #dc2626;
  transform: scale(1.1);
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.25rem;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: #6b7280;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.label-icon {
  font-size: 1rem;
}

.form-select {
  padding: 0.75rem 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 0.875rem;
  transition: all 0.3s ease;
  background: white;
  color: #374151;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.form-select:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
  transform: translateY(-1px);
}

.filter-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  padding-top: 0.5rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 10px;
  font-weight: 500;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  border: none;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.btn-outline {
  background: white;
  border: 2px solid #e5e7eb;
  color: #6b7280;
}

.btn-outline:hover {
  background: #f3f4f6;
  border-color: #d1d5db;
  color: #374151;
  transform: translateY(-1px);
}

.btn-primary {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  border: 2px solid transparent;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(34, 197, 94, 0.3);
}

/* Table Styles */
.table th {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  font-weight: 600;
  padding: 1rem;
  text-align: center;
  font-size: 0.875rem;
  white-space: nowrap;
  position: sticky;
  top: 0;
  z-index: 10;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.table td {
  padding: 1rem;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid var(--border-color);
  font-size: 0.875rem;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

/* Campaign Row Styles */
.campaign-row {
  transition: all 0.3s ease;
}

.campaign-row:hover {
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.05) 0%, rgba(34, 197, 94, 0.05) 100%);
  transform: scale(1.01);
}

.stt-cell {
  font-weight: 600;
  color: #4ade80;
  font-size: 1rem;
}

.discount-value {
  font-weight: 700;
  color: #22c55e;
  font-size: 1.125rem;
  display: block;
  margin-bottom: 0.25rem;
}

.discount-type {
  color: #6b7280;
  font-size: 0.75rem;
  font-weight: 500;
}

.campaign-code {
  color: #6b7280;
  font-size: 0.75rem;
  font-weight: 500;
  margin-top: 0.25rem;
  font-family: 'SF Mono', 'Monaco', 'Inconsolata', 'Roboto Mono', monospace;
}

/* Table Content Styles */
.campaign-name {
  text-align: left;
}

.campaign-name strong {
  color: #374151;
  font-size: 0.9375rem;
  font-weight: 600;
}

.campaign-description {
  color: #6b7280;
  font-size: 0.875rem;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.value-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.min-order {
  color: #9ca3af;
  font-size: 0.75rem;
  display: block;
}

.date-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.875rem;
}

.date-range {
  color: #6b7280;
  white-space: nowrap;
}

.date-separator {
  color: #22c55e;
  font-weight: bold;
  font-size: 0.75rem;
}

/* Action Buttons */
.action-buttons {
  display: flex;
  gap: 0.5rem;
  justify-content: center;
}

.btn-action {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
  background: white;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 1rem;
}

.btn-action:hover {
  transform: scale(1.1);
  background: #f3f4f6;
  border-color: #22c55e;
}

.btn-apply {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  border-color: #10b981;
}

.btn-apply:hover {
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
  border-color: #059669;
}

.btn-delete {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
  border-color: #ef4444;
}

.btn-delete:hover {
  background: linear-gradient(135deg, #dc2626 0%, #991b1b 100%);
  border-color: #dc2626;
}

/* Empty State */
.empty-state {
  padding: 2rem !important;
}

.empty-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
}

.empty-icon {
  font-size: 2rem;
  opacity: 0.5;
}

.empty-message p {
  margin: 0;
  color: #6b7280;
  font-weight: 500;
}

.empty-message small {
  color: #9ca3af;
  font-size: 0.875rem;
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
  backdrop-filter: blur(4px);
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
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

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.modal-header h3 {
  margin: 0;
  color: var(--secondary-color);
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
  font-weight: 600;
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
  color: #6b7280;
}

.modal-close:hover {
  background-color: var(--light-gray);
  color: #374151;
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
  background: #f8fafc;
}

/* Campaign Detail */
.campaign-detail {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.campaign-info h4 {
  text-align: center;
  margin: 0 0 2rem 0;
  color: var(--secondary-color);
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
  font-weight: 600;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

.info-item label {
  font-weight: 500;
  color: var(--medium-gray);
  font-size: 0.875rem;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.info-item span {
  color: var(--secondary-color);
  font-weight: 500;
}

/* Responsive Design */
@media (max-width: 1200px) {
  .discount-campaigns {
    padding: 0 1rem;
  }
}

@media (max-width: 1024px) {
  .search-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    min-width: auto;
  }

  .table {
    font-size: 0.875rem;
  }

  .table th,
  .table td {
    padding: 0.75rem 0.5rem;
  }
}

@media (max-width: 768px) {
  /* page-header responsive styles are handled in globals.css */

  .search-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-controls {
    flex-direction: column;
  }

  .pagination-wrapper {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }

  .modal-overlay {
    padding: 1rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .table {
    font-size: 0.8125rem;
  }

  .table th,
  .table td {
    padding: 0.5rem 0.25rem;
  }

  /* Hide less important columns on mobile */
  .table th:nth-child(4),
  .table td:nth-child(4),
  .table th:nth-child(5),
  .table td:nth-child(5),
  .table th:nth-child(6),
  .table td:nth-child(6),
  .table th:nth-child(7),
  .table td:nth-child(7) {
    display: none;
  }
}

@media (max-width: 480px) {
  .discount-campaigns {
    padding: 0 0.5rem;
  }

  .filter-section {
    padding: 1rem;
  }

  .table {
    font-size: 0.75rem;
  }

  .card {
    margin: 0 -0.5rem;
    border-radius: 0;
  }
}

/* Apply Modal Styles */
.apply-modal {
  max-width: 800px;
}

.discount-info {
  background: #f8fafc;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  border: 1px solid #e5e7eb;
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  font-weight: 500;
  color: #6b7280;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.value {
  font-weight: 600;
  color: #374151;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.product-selection h4 {
  margin-bottom: 1rem;
  color: #374151;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
  font-weight: 600;
}

.product-search {
  margin-bottom: 1rem;
}

.product-list {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 0.5rem;
}

.product-item {
  display: flex;
  align-items: flex-start;
  padding: 0.75rem;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-bottom: 0.5rem;
  border: 1px solid #e5e7eb;
}

.product-item:hover {
  background-color: #f3f4f6;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.product-item:last-child {
  margin-bottom: 0;
}

.product-item input[type="checkbox"] {
  margin-right: 0.75rem;
  margin-top: 0.25rem;
  cursor: pointer;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
}

.product-name {
  font-weight: 500;
  color: #374151;
  margin-bottom: 0.25rem;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.product-details {
  font-size: 0.875rem;
  color: #6b7280;
  margin-bottom: 0.25rem;
}

.details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.5rem;
}

.detail-column {
  display: flex;
  flex-direction: column;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.125rem;
  font-size: 0.8125rem;
}

.detail-label {
  font-weight: 500;
  color: #4b5563;
  min-width: 70px;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.detail-value {
  color: #6b7280;
  text-align: right;
  flex: 1;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.product-price {
  font-weight: 600;
  color: #22c55e;
  font-size: 0.875rem;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.empty-products {
  text-align: center;
  padding: 2rem;
  color: #6b7280;
}

.selection-controls {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.selection-controls .btn {
  font-size: 0.875rem;
  padding: 0.5rem 1rem;
}

.selection-summary {
  margin-top: 1rem;
}

.selected-count {
  padding: 0.75rem;
  background: #ecfdf5;
  border: 1px solid #d1fae5;
  border-radius: 6px;
  font-weight: 500;
  color: #065f46;
  text-align: center;
  margin-bottom: 0.5rem;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.available-count {
  padding: 0.5rem;
  background: #f3f4f6;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.875rem;
  color: #6b7280;
  text-align: center;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.available-count small {
  color: #9ca3af;
  font-size: 0.8125rem;
}

.product-section {
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 0.9375rem;
  font-weight: 600;
  margin-bottom: 0.75rem;
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.available-title {
  background: #ecfdf5;
  color: #065f46;
  border: 1px solid #d1fae5;
}

.current-campaign-title {
  background: #eff6ff;
  color: #1e40af;
  border: 1px solid #bfdbfe;
}

.unavailable-title {
  background: #fef2f2;
  color: #991b1b;
  border: 1px solid #fecaca;
}

.available-product {
  border: 1px solid #d1fae5;
  background: #fafafa;
}

.available-product:hover {
  background: #f0fdf4;
  border-color: #a7f3d0;
}

.current-campaign-product {
  border: 1px solid #bfdbfe;
  background: #f8fafc;
}

.current-campaign-product:hover {
  background: #eff6ff;
  border-color: #93c5fd;
}

.unavailable-product {
  border: 1px solid #fecaca;
  background: #fef2f2;
  cursor: not-allowed;
  opacity: 0.7;
}

.disabled-checkbox {
  opacity: 0.5;
  cursor: not-allowed;
}

.disabled-text {
  color: #9ca3af;
}

.discount-active {
  color: #dc2626;
  font-weight: 600;
}

.current-campaign {
  color: #1d4ed8;
  font-weight: 600;
}

.empty-available {
  text-align: center;
  padding: 2rem;
  background: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: 8px;
  margin: 1rem 0;
}

.warning-text {
  color: #dc2626;
  font-weight: 500;
  margin: 0;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

@media (max-width: 768px) {
  .apply-modal {
    max-width: 95vw;
    margin: 1rem;
  }

  .product-list {
    max-height: 250px;
  }

  .info-row {
    flex-direction: column;
    gap: 0.25rem;
  }

  .details-grid {
    grid-template-columns: 1fr;
  }

  .detail-label {
    min-width: 90px;
  }
}

/* Form styling improvements */
.form-group {
  margin-bottom: 1.5rem;
}

.form-group:last-child {
  margin-bottom: 0;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #374151;
  font-size: 0.875rem;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.875rem;
  transition: all 0.2s ease;
  background-color: #fff;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

.form-control:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
  transform: translateY(-1px);
}

.form-control:disabled {
  background-color: #f3f4f6;
  color: #9ca3af;
  cursor: not-allowed;
}

.form-control.is-invalid {
  border-color: var(--error);
}

.form-control.is-valid {
  border-color: var(--success);
}

.form-help {
  display: block;
  margin-top: 0.25rem;
  font-size: 0.75rem;
  color: #6b7280;
  font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif;
}

/* Row and column layout */
.row {
  display: flex;
  margin: 0 -0.75rem;
  margin-bottom: 1rem;
}

.col-6 {
  flex: 0 0 50%;
  max-width: 50%;
  padding: 0 0.75rem;
}

.col-6 .form-group {
  margin-bottom: 0;
}

/* Form sections spacing */
.modal-body .form-group {
  margin-bottom: 1.25rem;
}

.modal-body .row {
  margin-bottom: 1.25rem;
}

.modal-body .row:last-child {
  margin-bottom: 0;
}

/* Mobile responsive form */
@media (max-width: 480px) {
  .row {
    flex-direction: column;
    margin: 0;
    margin-bottom: 1rem;
  }

  .col-6 {
    flex: none;
    max-width: 100%;
    padding: 0;
  }

  .col-6 .form-group {
    margin-bottom: 1rem;
  }

  .col-6:last-child .form-group {
    margin-bottom: 0;
  }
}
</style>
