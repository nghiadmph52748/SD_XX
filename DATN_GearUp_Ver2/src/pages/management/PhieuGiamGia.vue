<template>
  <div class="discount-coupons">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">Quản lý Phiếu giảm giá</h1>
          <p class="page-subtitle">Tạo và quản lý các phiếu giảm giá</p>
        </div>
        <div class="header-actions">

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
            Tạo mới
          </button>
        </div>
      </div>
    </div>

    <!-- Modern Filter Section -->
    <div class="filter-section">
      <div class="filter-card">
        <div class="filter-header">
          <div class="filter-title">
            <span class="filter-icon">🎫</span>
                         <h3 style="font-family: 'Arial', 'Helvetica', sans-serif; font-weight: 700; letter-spacing: 0.5px; color: #4ade80;">Tìm kiếm phiếu giảm giá</h3>
          </div>
          <div class="filter-stats">
            {{ filteredCoupons.length }} / {{ coupons.length }} phiếu
          </div>
        </div>

        <div class="filter-content">
          <div class="search-section">
            <div class="input-group">
              <span class="input-icon">🔍</span>
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Tìm kiếm theo mã hoặc tên phiếu giảm giá..."
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
                <span class="label-icon">💰</span>
                Kiểu giảm giá
              </label>
              <select v-model="selectedType" class="form-select">
                <option value="">Tất cả kiểu</option>
                <option value="percent">📊 Phần trăm (%)</option>
                <option value="fixed">💵 Số tiền cố định</option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🏷️</span>
                Loại phiếu
              </label>
              <select v-model="selectedType2" class="form-select">
                <option value="">Tất cả loại</option>
                <option value="public">🌐 Công khai</option>
                <option value="private">🔒 Cá nhân</option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">📈</span>
                Trạng thái
              </label>
              <select v-model="selectedStatus" class="form-select">
                <option value="">Tất cả trạng thái</option>
                <option value="active">✅ Đang diễn ra</option>
                <option value="expired">❌ Hết hạn</option>
                <option value="upcoming">⏰ Sắp diễn ra</option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">📅</span>
                Từ ngày
              </label>
              <input
                type="date"
                v-model="fromDate"
                class="form-control date-input"
              />
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">📅</span>
                Đến ngày
              </label>
              <input
                type="date"
                v-model="toDate"
                class="form-control date-input"
              />
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🔄</span>
                Làm mới
              </label>
              <button @click="clearFilters" class="btn btn-outline">
                <span class="btn-icon">🔄</span>
                Làm mới
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Coupons Table -->
    <div class="card">
      <div class="card-body">
        <table class="table">
          <thead>
            <tr>
              <th>STT</th>
              <th>Tên</th>
              <th>Đối tượng</th>
              <th>Loại giảm</th>
              <th>Giá trị giảm</th>
              <th>Số tiền tối thiểu</th>
              <th>Số tiền giảm tối đa</th>
              <th>Thời gian</th>
              <th>Số lượng dùng</th>
              <th>Mô tả</th>
              <th>Hiện trạng</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(coupon, index) in filteredCoupons" :key="coupon.id">
              <td>{{ index + 1 }}</td>
              <td class="coupon-code">{{ coupon.tenPhieuGiamGia }}</td>
              <td class="coupon-name">
                {{
                  coupon.idKhachHang && coupon.idKhachHang.length > 0
                    ? `${coupon.idKhachHang.length} khách hàng`
                    : "Mọi người"
                }}
              </td>
              <td class="coupon-name">
                {{ !coupon.loaiPhieuGiamGia ? "%" : "VND" }}
              </td>
              <td>
                {{
                  !coupon.loaiPhieuGiamGia
                    ? coupon.giaTriGiamGia + "%"
                    : formatCurrency(coupon.giaTriGiamGia)
                }}
              </td>
              <td>{{ formatCurrency(coupon.hoaDonToiThieu || 0) }}</td>
              <td>{{ formatCurrency(coupon.soTienToiDa || 0) }}</td>
              <td>
                {{ formatDate(coupon.ngayBatDau) }} -
                {{ formatDate(coupon.ngayKetThuc) }}
              </td>
              <td>{{ coupon.soLuongDung }}</td>
              <td>{{ coupon.moTa }}</td>
                             <td :class="['status-text', coupon.trangThai ? 'text-green' : 'text-red']">
                 {{ coupon.trangThai ? "Đang hoạt động" : "Đã kết thúc" }}
               </td>
               <td :class="['status-text', !coupon.deleted ? 'text-green' : 'text-red']">
                 {{ !coupon.deleted ? "Hoạt động" : "Không hoạt động" }}
               </td>
              <td>
                <div class="action-buttons">
                  <button
                    class="btn-action"
                    @click="viewCoupon(coupon)"
                    title="Xem chi tiết"
                  >
                    👁️
                  </button>
                  <button
                    class="btn-action"
                    @click="editCoupon(coupon)"
                    title="Chỉnh sửa"
                  >
                    ✏️
                  </button>
                  <button
                    class="btn-action"
                    @click="fetchUpdateStatusPGG(coupon.id)"
                    title="Xóa"
                  >
                    🗑️
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Pagination -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của
            {{ totalCoupons }} phiếu giảm giá
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
      <div class="modal-content large" @click.stop>
        <div class="modal-header">
          <h3>
            {{
              showAddModal ? "Tạo phiếu giảm giá" : "Cập nhật phiếu giảm giá"
            }}
          </h3>
          <div v-if="showEditModal" class="debug-info">
            <small
              >Debug: {{ formDebugInfo.formType }} |
              {{ formDebugInfo.selectedCustomersCount }} customers</small
            >
          </div>
          <button class="modal-close" @click="closeModals">✕</button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="saveCoupon">
            <div class="form-sections">
              <!-- Basic Information -->
              <div class="form-section">
                <h4>Thông tin cơ bản</h4>
                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">Tên phiếu giảm giá *</label>
                    <input
                      type="text"
                      v-model="couponForm.tenPhieuGiamGia"
                      class="form-control"
                      placeholder="Nhập tên phiếu giảm giá"
                      required
                    />
                  </div>
                </div>

                <div class="form-group">
                  <label class="form-label">Mô tả</label>
                  <textarea
                    v-model="couponForm.moTa"
                    class="form-control"
                    rows="3"
                    placeholder="Nhập mô tả phiếu giảm giá"
                  ></textarea>
                </div>
              </div>

              <!-- Discount Settings -->
              <div class="form-section">
                <h4>Cài đặt giảm giá</h4>
                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">Kiểu giảm giá *</label>
                    <select
                      v-model="couponForm.loaiPhieuGiamGia"
                      class="form-control"
                      required
                    >
                      <option :value="false">Phần trăm (%)</option>
                      <option :value="true">Số tiền cố định (VND)</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label class="form-label">Giá trị giảm *</label>
                    <input
                      type="number"
                      v-model="couponForm.giaTriGiamGia"
                      class="form-control"
                      :placeholder="
                        !couponForm.loaiPhieuGiamGia
                          ? 'Nhập % giảm (1-100)'
                          : 'Nhập số tiền'
                      "
                      :min="!couponForm.loaiPhieuGiamGia ? 1 : 1000"
                      :max="!couponForm.loaiPhieuGiamGia ? 100 : undefined"
                      required
                    />
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">Hóa đơn tối thiểu</label>
                    <input
                      type="number"
                      v-model="couponForm.hoaDonToiThieu"
                      class="form-control"
                      placeholder="0"
                      min="0"
                    />
                  </div>
                  <div class="form-group" v-if="!couponForm.loaiPhieuGiamGia">
                    <label class="form-label">Số tiền giảm tối đa</label>
                    <input
                      type="number"
                      v-model="couponForm.soTienToiDa"
                      class="form-control"
                      placeholder="Số tiền giảm tối đa"
                      min="0"
                    />
                  </div>
                </div>
              </div>

              <!-- Usage Settings -->
              <div class="form-section">
                <h4>Cài đặt sử dụng</h4>
                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">Số lượng sử dụng *</label>
                    <input
                      type="number"
                      v-model="couponForm.soLuongDung"
                      class="form-control"
                      placeholder="Nhập số lượng"
                      min="1"
                      required
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">Loại phiếu *</label>
                    <select
                      v-model="couponForm.idKhachHang"
                      class="form-control"
                    >
                      <option :value="null">
                        Công khai (tất cả khách hàng)
                      </option>
                      <option value="personal">
                        Áp dụng cho khách hàng cụ thể
                      </option>
                    </select>
                  </div>
                </div>

                <!-- Customer Selection Section -->
                <div
                  v-if="couponForm.idKhachHang === 'personal'"
                  class="customer-selection"
                >
                  <h5>Chọn khách hàng áp dụng:</h5>
                  <div class="customer-search">
                    <input
                      type="text"
                      placeholder="Tìm kiếm khách hàng..."
                      class="form-control"
                      v-model="searchCustomerQuery"
                    />
                  </div>

                  <div class="selection-controls">
                    <button
                      type="button"
                      class="btn btn-outline btn-sm"
                      @click="selectAllCustomers"
                      :disabled="
                        selectedCustomers.length ===
                          availableCustomers.length ||
                        availableCustomers.length === 0
                      "
                    >
                      ✅ Chọn hết
                    </button>
                    <button
                      type="button"
                      class="btn btn-outline btn-sm"
                      @click="clearAllCustomers"
                      :disabled="selectedCustomers.length === 0"
                    >
                      ❌ Bỏ chọn hết
                    </button>
                  </div>

                  <div class="customer-list">
                    <div
                      v-for="customer in availableCustomers"
                      :key="customer.id"
                      class="customer-item"
                      @click="toggleCustomerSelection(customer.id)"
                    >
                      <input
                        type="checkbox"
                        :checked="selectedCustomers.includes(customer.id)"
                        @click.stop="toggleCustomerSelection(customer.id)"
                      />
                      <div class="customer-info">
                        <div class="customer-name">
                          {{ customer.tenKhachHang }}
                        </div>
                        <div class="customer-details">
                          <div class="details-grid">
                            <div class="detail-column">
                              <div class="detail-row" v-if="customer.email">
                                <span class="detail-label">Email:</span>
                                <span class="detail-value">{{
                                  customer.email
                                }}</span>
                              </div>
                              <div
                                class="detail-row"
                                v-if="customer.soDienThoai"
                              >
                                <span class="detail-label">SĐT:</span>
                                <span class="detail-value">{{
                                  customer.soDienThoai
                                }}</span>
                              </div>
                            </div>
                            <div class="detail-column">
                              <div class="detail-row" v-if="customer.gioiTinh">
                                <span class="detail-label">Giới tính:</span>
                                <span class="detail-value">{{
                                  customer.gioiTinh
                                }}</span>
                              </div>
                              <div class="detail-row" v-if="customer.ngaySinh">
                                <span class="detail-label">Ngày sinh:</span>
                                <span class="detail-value">{{
                                  formatDate(customer.ngaySinh)
                                }}</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div
                      v-if="availableCustomers.length === 0"
                      class="empty-customers"
                    >
                      <p>Không có khách hàng nào</p>
                    </div>
                  </div>

                  <div class="selection-summary">
                    <div class="selected-count">
                      Đã chọn: {{ selectedCustomers.length }} khách hàng
                    </div>
                    <div class="available-count">
                      Có sẵn: {{ availableCustomers.length }} /
                      {{ customers.length }} khách hàng
                    </div>
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">Ngày bắt đầu *</label>
                    <input
                      type="date"
                      v-model="couponForm.ngayBatDau"
                      class="form-control"
                      required
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">Ngày kết thúc *</label>
                    <input
                      type="date"
                      v-model="couponForm.ngayKetThuc"
                      class="form-control"
                      required
                    />
                  </div>
                </div>

                <div class="form-row" v-if="showEditModal">
                  <div class="form-group">
                    <label class="form-label">Trạng thái *</label>
                    <select v-model="couponForm.trangThai" class="form-control">
                      <option :value="true">Đang hoạt động</option>
                      <option :value="false">Tạm dừng</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label class="form-label">Hiện trạng *</label>
                    <select v-model="couponForm.deleted" class="form-control">
                      <option :value="false">Hoạt động</option>
                      <option :value="true">Ngừng hoạt động</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>

        <div class="modal-footer">
          <button class="btn-export" @click="closeModals">
            <span class="btn-icon">❌</span>
            Hủy
          </button>
          <button class="btn-export" @click="saveCoupon">
            <span class="btn-icon">💾</span>
            {{ showAddModal ? "Tạo phiếu giảm giá" : "Cập nhật" }}
          </button>
        </div>
      </div>
    </div>
    -->

    <!-- Coupon Detail Modal -->
    <div
      v-if="showDetailModal"
      class="modal-overlay"
      @click="showDetailModal = false"
    >
      <div class="modal-content detail-modal" @click.stop>
        <div class="modal-header detail-header">
          <div class="header-content">
            <div class="coupon-title">
              <span class="coupon-icon">🎫</span>
              <h3>{{ selectedCoupon?.tenPhieuGiamGia }}</h3>
            </div>
            <div class="coupon-status">
              <span :class="['status-badge', getStatusClass(selectedCoupon)]">
                {{ getStatusText(selectedCoupon) }}
              </span>
            </div>
          </div>
          <button class="modal-close" @click="showDetailModal = false">
            ✕
          </button>
        </div>

        <div class="modal-body detail-body" v-if="selectedCoupon">
          <div class="coupon-detail">
            <!-- Basic Information Section -->
            <div class="detail-section">
              <div class="section-header">
                <span class="section-icon">📋</span>
                <h4>Thông tin cơ bản</h4>
              </div>
              <div class="info-grid">
                <div class="info-item" v-if="selectedCoupon.maPhieuGiamGia">
                  <label>Mã phiếu:</label>
                  <span class="coupon-code">{{ selectedCoupon.maPhieuGiamGia }}</span>
                </div>
                <div class="info-item">
                  <label>Mô tả:</label>
                  <span class="description">{{ selectedCoupon.moTa || "Không có mô tả" }}</span>
                </div>
                <div class="info-item">
                  <label>Loại phiếu:</label>
                  <span :class="['badge', getCouponTypeClass(selectedCoupon)]">
                    {{ getCouponTypeText(selectedCoupon) }}
                  </span>
                </div>
                <div class="info-item">
                  <label>Ngày tạo:</label>
                  <span>{{ formatDateTime(selectedCoupon.ngayTao || selectedCoupon.ngayBatDau) }}</span>
                </div>
              </div>
            </div>

            <!-- Discount Information Section -->
            <div class="detail-section">
              <div class="section-header">
                <span class="section-icon">💰</span>
                <h4>Thông tin giảm giá</h4>
              </div>
              <div class="info-grid">
                <div class="info-item">
                  <label>Kiểu giảm giá:</label>
                  <span class="discount-type">
                    {{ !selectedCoupon.loaiPhieuGiamGia ? "Phần trăm (%)" : "Số tiền cố định (VND)" }}
                  </span>
                </div>
                <div class="info-item">
                  <label>Giá trị giảm:</label>
                  <span class="discount-value">
                    {{ !selectedCoupon.loaiPhieuGiamGia ? selectedCoupon.giaTriGiamGia + "%" : formatCurrency(selectedCoupon.giaTriGiamGia) }}
                  </span>
                </div>
                <div class="info-item">
                  <label>Hóa đơn tối thiểu:</label>
                  <span class="min-amount">{{ formatCurrency(selectedCoupon.hoaDonToiThieu || 0) }}</span>
                </div>
                <div class="info-item" v-if="selectedCoupon.soTienToiDa">
                  <label>Giảm tối đa:</label>
                  <span class="max-discount">{{ formatCurrency(selectedCoupon.soTienToiDa) }}</span>
                </div>
              </div>
            </div>

            <!-- Usage Information Section -->
            <div class="detail-section">
              <div class="section-header">
                <span class="section-icon">📊</span>
                <h4>Thông tin sử dụng</h4>
              </div>
              <div class="info-grid">
                <div class="info-item">
                  <label>Số lượng sử dụng:</label>
                  <span class="usage-count">{{ selectedCoupon.soLuongDung }}</span>
                </div>
                <div class="info-item">
                  <label>Đã sử dụng:</label>
                  <span class="used-count">{{ selectedCoupon.soLuongDaDung || 0 }}</span>
                </div>
                <div class="info-item">
                  <label>Còn lại:</label>
                  <span class="remaining-count">{{ (selectedCoupon.soLuongDung || 0) - (selectedCoupon.soLuongDaDung || 0) }}</span>
                </div>
                <div class="info-item">
                  <label>Tỷ lệ sử dụng:</label>
                  <span class="usage-rate">
                    {{ selectedCoupon.soLuongDung ? Math.round(((selectedCoupon.soLuongDaDung || 0) / selectedCoupon.soLuongDung) * 100) : 0 }}%
                  </span>
                </div>
              </div>
            </div>

            <!-- Time Information Section -->
            <div class="detail-section">
              <div class="section-header">
                <span class="section-icon">⏰</span>
                <h4>Thông tin thời gian</h4>
              </div>
              <div class="info-grid">
                <div class="info-item">
                  <label>Ngày bắt đầu:</label>
                  <span class="start-date">{{ formatDateTime(selectedCoupon.ngayBatDau) }}</span>
                </div>
                <div class="info-item">
                  <label>Ngày kết thúc:</label>
                  <span class="end-date">{{ formatDateTime(selectedCoupon.ngayKetThuc) }}</span>
                </div>
                <div class="info-item">
                  <label>Thời gian còn lại:</label>
                  <span class="time-remaining">
                    {{ getTimeRemaining(selectedCoupon.ngayKetThuc) }}
                  </span>
                </div>
                <div class="info-item">
                  <label>Trạng thái hoạt động:</label>
                  <span :class="['badge', selectedCoupon.trangThai ? 'badge-success' : 'badge-danger']">
                    {{ selectedCoupon.trangThai ? "Đang hoạt động" : "Tạm dừng" }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Personal Customers Section -->
            <div
              v-if="getAppliedCustomers(selectedCoupon.id).length > 0"
              class="detail-section"
            >
              <div class="section-header">
                <span class="section-icon">👥</span>
                <h4>Khách hàng được áp dụng</h4>
              </div>
              <div class="customers-info">
                <div class="customers-summary">
                  <span class="customers-count">
                    {{ getAppliedCustomers(selectedCoupon.id).length }} khách hàng
                  </span>
                </div>
                <div class="customer-chips">
                  <span
                    v-for="customer in getAppliedCustomers(selectedCoupon.id)"
                    :key="customer.id"
                    class="customer-chip"
                  >
                    <span class="customer-avatar">👤</span>
                    {{ customer.tenKhachHang }}
                    <span class="customer-email" v-if="customer.email">({{ customer.email }})</span>
                  </span>
                </div>
              </div>
            </div>

            <!-- Statistics Section -->
            <div class="detail-section">
              <div class="section-header">
                <span class="section-icon">📈</span>
                <h4>Thống kê</h4>
              </div>
              <div class="stats-grid">
                <div class="stat-item">
                  <div class="stat-value">{{ selectedCoupon.soLuongDung || 0 }}</div>
                  <div class="stat-label">Tổng số lượng</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ selectedCoupon.soLuongDaDung || 0 }}</div>
                  <div class="stat-label">Đã sử dụng</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ (selectedCoupon.soLuongDung || 0) - (selectedCoupon.soLuongDaDung || 0) }}</div>
                  <div class="stat-label">Còn lại</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">
                    {{ selectedCoupon.soLuongDung ? Math.round(((selectedCoupon.soLuongDaDung || 0) / selectedCoupon.soLuongDung) * 100) : 0 }}%
                  </div>
                  <div class="stat-label">Tỷ lệ sử dụng</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer detail-footer">
          <button class="btn btn-primary" @click="editFromDetail(selectedCoupon)">
            <span class="btn-icon">✏️</span>
            Chỉnh sửa
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import {
  fetchAllPhieuGiamGia,
  fetchCreatePhieuGiamGia,
  fetchUpdatePhieuGiamGia,
  fetchUpdateStatusPhieuGiamGia,
} from "../../services/GiamGia/PhieuGiamGiaService";
import { fetchAllPhieuGiamGiaCaNhan } from "../../services/GiamGia/PhieuGiamGiaCaNhanService";
import { fetchAllKhachHang } from "../../services/KhachHang/KhachHangService";

// Data
const searchQuery = ref("");
const fromDate = ref("");
const toDate = ref("");
const selectedType = ref("");
const selectedType2 = ref("");
const selectedStatus = ref("");
const showAddModal = ref(false);
const showEditModal = ref(false);
const showDetailModal = ref(false);
const selectedCoupon = ref(null);
const editingCoupon = ref(null);
// Pagination data
const currentPage = ref(1);
const itemsPerPage = ref(10);

const couponForm = ref({
  maPhieuGiamGia: "",
  tenPhieuGiamGia: "",
  moTa: "",
  loaiPhieuGiamGia: false, // false = %, true = VND
  giaTriGiamGia: 0,
  hoaDonToiThieu: 0,
  soTienToiDa: 0,
  soLuongDung: 1,
  ngayBatDau: "",
  ngayKetThuc: "",
  trangThai: true,
  deleted: false,
  idKhachHang: [],
});

const coupons = ref([]);
const personalCoupons = ref([]);
const customers = ref([]);

// Customer selection for form
const searchCustomerQuery = ref("");
const selectedCustomers = ref([]);
// fetch data
const fetchPGG = async () => {
  try {
    const res = await fetchAllPhieuGiamGia();
    coupons.value = res.data;
  } catch (error) {
    console.error("Error fetching phieu giam gia: ", error);
  }
};
const fetchCustomers = async () => {
  try {
    const res = await fetchAllKhachHang();
    customers.value = res.data;
  } catch (error) {
    console.error("Error fetching customers: ", error);
  }
};
const fetchPersonalPGG = async () => {
  try {
    const res = await fetchAllPhieuGiamGiaCaNhan();
    personalCoupons.value = res.data;
  } catch (error) {
    console.error("Error fetching phieu giam gia ca nhan: ", error);
  }
};
const fetchAll = async () => {
  await fetchPGG();
  await fetchCustomers();
  await fetchPersonalPGG();
};
// Apply all filters first
const allFilteredCoupons = computed(() => {
  let filtered = coupons.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (coupon) =>
        coupon.tenPhieuGiamGia
          ?.toLowerCase()
          .includes(searchQuery.value.toLowerCase())
    );
  }

  return filtered;
});

// Paginated results from filtered data
const filteredCoupons = computed(() => {
  return allFilteredCoupons.value.slice(startIndex.value, endIndex.value);
});

// Total coupons after filtering (for pagination)
const totalCoupons = computed(() => {
  return allFilteredCoupons.value.length;
});

// Pagination computed properties
const totalPages = computed(() =>
  Math.ceil(totalCoupons.value / itemsPerPage.value)
);

const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);

const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, totalCoupons.value)
);

// Customer selection computed properties
const availableCustomers = computed(() => {
  let filtered = customers.value;

  if (searchCustomerQuery.value) {
    filtered = filtered.filter(
      (customer) =>
        customer.tenKhachHang
          ?.toLowerCase()
          .includes(searchCustomerQuery.value.toLowerCase()) ||
        customer.email
          ?.toLowerCase()
          .includes(searchCustomerQuery.value.toLowerCase()) ||
        customer.soDienThoai?.includes(searchCustomerQuery.value)
    );
  }

  return filtered;
});

// Methods
const formatCurrency = (amount) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    minimumFractionDigits: 0,
  })
    .format(amount)
    .replace("₫", " đ");
};

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("vi-VN");
};

const formatDateTime = (dateString) => {
  return new Date(dateString).toLocaleString("vi-VN");
};

const getCouponStatus = (coupon) => {
  const now = new Date();
  const startDate = new Date(coupon.ngayBatDau);
  const endDate = new Date(coupon.ngayKetThuc);

  if (now < startDate) {
    return "upcoming";
  } else if (now > endDate) {
    return "expired";
  } else {
    return "active";
  }
};

const getStatusClass = (coupon) => {
  const status = getCouponStatus(coupon);
  const statusClasses = {
    active: "badge-success",
    expired: "badge-danger",
    upcoming: "badge-warning",
  };
  return statusClasses[status] || "badge-secondary";
};

const getStatusText = (coupon) => {
  const status = getCouponStatus(coupon);
  const statusTexts = {
    active: "Đang diễn ra",
    expired: "Hết hạn",
    upcoming: "Sắp diễn ra",
  };
  return statusTexts[status] || "Không xác định";
};

const getTimeRemaining = (endDate) => {
  if (!endDate) return "Không xác định";
  
  const now = new Date();
  const end = new Date(endDate);
  const diff = end - now;
  
  if (diff <= 0) return "Đã hết hạn";
  
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
  
  if (days > 0) {
    return `${days} ngày ${hours} giờ`;
  } else if (hours > 0) {
    return `${hours} giờ ${minutes} phút`;
  } else {
    return `${minutes} phút`;
  }
};

const viewCoupon = (coupon) => {
  selectedCoupon.value = coupon;
  showDetailModal.value = true;
};

const editCoupon = (coupon) => {
  // Ensure customers data is loaded
  if (customers.value.length === 0) {
    console.warn("Customers data not loaded yet, loading...");
    fetchCustomers().then(() => {
      editCoupon(coupon); // Retry after loading
    });
    return;
  }

  editingCoupon.value = coupon;
  couponForm.value = {
    maPhieuGiamGia: coupon.maPhieuGiamGia || "",
    tenPhieuGiamGia: coupon.tenPhieuGiamGia || "",
    moTa: coupon.moTa || "",
    loaiPhieuGiamGia: coupon.loaiPhieuGiamGia || false,
    giaTriGiamGia: coupon.giaTriGiamGia || 0,
    hoaDonToiThieu: coupon.hoaDonToiThieu || 0,
    soTienToiDa: coupon.soTienToiDa || 0,
    soLuongDung: coupon.soLuongDung || 1,
    ngayBatDau: coupon.ngayBatDau ? coupon.ngayBatDau.split("T")[0] : "",
    ngayKetThuc: coupon.ngayKetThuc ? coupon.ngayKetThuc.split("T")[0] : "",
    trangThai: coupon.trangThai !== undefined ? coupon.trangThai : true,
    deleted: coupon.deleted || false,
    idKhachHang: coupon.idKhachHang || [], // Changed to match API structure
  };

  // Reset selectedCustomers first
  selectedCustomers.value = [];

  // Set form type and load customers based on coupon type
  if (coupon.idKhachHang && coupon.idKhachHang.length > 0) {
    // Primary: Has specific customers in idKhachHang array
    couponForm.value.idKhachHang = "personal";
    selectedCustomers.value = [...coupon.idKhachHang];
  } else {
    // Check legacy personal coupons table
    const appliedCustomers = personalCoupons.value
      .filter((pc) => pc.idPhieuGiamGia === coupon.id && !pc.deleted)
      .map((pc) => pc.idKhachHang)
      .filter((id) => id !== null);

    if (appliedCustomers.length > 0) {
      // Legacy: customers applied via personal coupons table
      couponForm.value.idKhachHang = "personal";
      selectedCustomers.value = [...appliedCustomers];
    } else {
      // Public coupon
      couponForm.value.idKhachHang = null;
    }
  }

  searchCustomerQuery.value = "";

  // Debug logging
  console.log("Edit Coupon Debug:", {
    originalCoupon: coupon,
    couponIdKhachHang: coupon.idKhachHang,
    formType: couponForm.value.idKhachHang,
    selectedCustomers: selectedCustomers.value,
    customersData: customers.value.filter((c) =>
      selectedCustomers.value.includes(c.id)
    ),
  });

  showEditModal.value = true;
};

const editFromDetail = (coupon) => {
  // Đóng popup xem chi tiết
  showDetailModal.value = false;
  
  // Gọi function editCoupon để mở popup chỉnh sửa
  editCoupon(coupon);
};

const fetchUpdateStatusPGG = async (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa phiếu giảm giá này?")) {
    try {
      await fetchUpdateStatusPhieuGiamGia(id);
      await fetchPGG(); // Reload data after delete
      alert("Xóa phiếu giảm giá thành công!");
    } catch (error) {
      console.error("Lỗi khi xóa phiếu giảm giá:", error);
      alert("Có lỗi xảy ra khi xóa phiếu giảm giá");
    }
  }
};
const fetchCreatePGG = async () => {
  try {
    const couponData = { ...couponForm.value };

    // Handle idKhachHang based on type
    if (couponData.idKhachHang === "personal") {
      // Set as array of selected customers
      couponData.idKhachHang =
        selectedCustomers.value.length > 0 ? selectedCustomers.value : [];
    } else {
      // Public coupon
      couponData.idKhachHang = [];
    }

    console.log("Creating coupon with data:", couponData);
    await fetchCreatePhieuGiamGia(couponData);
    console.log("Create successful");
  } catch (error) {
    console.error("Error creating phieu giam gia: ", error);
    throw error; // Re-throw to handle in saveCoupon
  }
};
const fetchUpdatePGG = async (id) => {
  try {
    const couponData = { ...couponForm.value };

    // Handle idKhachHang based on type
    if (couponData.idKhachHang === "personal") {
      // Set as array of selected customers
      couponData.idKhachHang =
        selectedCustomers.value.length > 0 ? selectedCustomers.value : [];
    } else {
      // Public coupon
      couponData.idKhachHang = [];
    }

    console.log("Updating coupon with data:", couponData);
    await fetchUpdatePhieuGiamGia(id, couponData);
    console.log("Update successful");
  } catch (error) {
    console.error("Error updating phieu giam gia: ", error);
    throw error; // Re-throw to handle in saveCoupon
  }
};
const saveCoupon = async () => {
  try {
    if (!couponForm.value.tenPhieuGiamGia.trim()) {
      alert("Vui lòng nhập tên phiếu giảm giá");
      return;
    }

    if (
      !couponForm.value.giaTriGiamGia ||
      couponForm.value.giaTriGiamGia <= 0
    ) {
      alert("Vui lòng nhập giá trị giảm giá hợp lệ");
      return;
    }

    if (!couponForm.value.ngayBatDau || !couponForm.value.ngayKetThuc) {
      alert("Vui lòng chọn ngày bắt đầu và kết thúc");
      return;
    }

    if (
      new Date(couponForm.value.ngayBatDau) >=
      new Date(couponForm.value.ngayKetThuc)
    ) {
      alert("Ngày kết thúc phải sau ngày bắt đầu");
      return;
    }

    // Validate personal coupon must have customers selected
    if (
      couponForm.value.idKhachHang === "personal" &&
      selectedCustomers.value.length === 0
    ) {
      alert("Vui lòng chọn ít nhất một khách hàng cho phiếu giảm giá cá nhân");
      return;
    }

    if (showAddModal.value) {
      // Call create API
      await fetchCreatePGG();
      currentPage.value = 1; // Reset to first page
      // Close modals and reset form
      closeModals();
      await fetchAll(); // Refresh data
      alert("Thêm phiếu giảm giá thành công!");
    } else if (showEditModal.value && editingCoupon.value) {
      // Call update API
      await fetchUpdatePGG(editingCoupon.value.id);
      // Close modals and reset form
      closeModals();
      await fetchAll(); // Refresh data
      alert("Cập nhật phiếu giảm giá thành công!");
    }
  } catch (error) {
    console.error("Lỗi khi lưu phiếu giảm giá:", error);
    alert("Có lỗi xảy ra khi lưu thông tin phiếu giảm giá");
  }
};

const closeModals = () => {
  showAddModal.value = false;
  showEditModal.value = false;
  showDetailModal.value = false;
  editingCoupon.value = null;
  selectedCoupon.value = null;
  selectedCustomers.value = [];
  searchCustomerQuery.value = "";
  resetForm();
};

const openAddModal = () => {
  resetForm();
  selectedCustomers.value = [];
  searchCustomerQuery.value = "";
  showAddModal.value = true;
};

// Customer selection methods
const toggleCustomerSelection = (customerId) => {
  const index = selectedCustomers.value.indexOf(customerId);
  if (index > -1) {
    selectedCustomers.value.splice(index, 1);
  } else {
    selectedCustomers.value.push(customerId);
  }
};

const selectAllCustomers = () => {
  selectedCustomers.value = availableCustomers.value.map(
    (customer) => customer.id
  );
};

const clearAllCustomers = () => {
  selectedCustomers.value = [];
};

// Reset pagination when filters change
const resetPagination = () => {
  currentPage.value = 1;
};

const getPersonalCustomerCount = (couponId) => {
  return personalCoupons.value.filter(
    (pc) => pc.idPhieuGiamGia === couponId && !pc.deleted
  ).length;
};

// Get coupon type text and class
const getCouponTypeText = (coupon) => {
  if (coupon.idKhachHang && coupon.idKhachHang.length > 0) {
    if (coupon.idKhachHang.length === 1) {
      const customer = customers.value.find(
        (c) => c.id === coupon.idKhachHang[0]
      );
      return customer
        ? `Cá nhân: ${customer.tenKhachHang}`
        : "Khách hàng cụ thể";
    } else {
      return `Cá nhân: ${coupon.idKhachHang.length} khách hàng`;
    }
  }

  return "Công khai";
};

const getCouponTypeClass = (coupon) => {
  if (coupon.idKhachHang && coupon.idKhachHang.length > 0) {
    return "badge-warning";
  }
  return "badge-success";
};

// Get list of customers applied to this coupon
const getAppliedCustomers = (couponId) => {
  // Find the coupon first
  const coupon = coupons.value.find((c) => c.id === couponId);

  let appliedCustomerIds = [];

  // If coupon has idKhachHang array, use that
  if (coupon && coupon.idKhachHang && coupon.idKhachHang.length > 0) {
    appliedCustomerIds = [...coupon.idKhachHang];
  }

  // Also check personal coupons table (for legacy data)
  const personalCustomerIds = personalCoupons.value
    .filter((pc) => pc.idPhieuGiamGia === couponId && !pc.deleted)
    .map((pc) => pc.idKhachHang)
    .filter((id) => id !== null);

  // Combine and deduplicate
  appliedCustomerIds = [
    ...new Set([...appliedCustomerIds, ...personalCustomerIds]),
  ];

  return customers.value.filter((customer) =>
    appliedCustomerIds.includes(customer.id)
  );
};

// Check if coupon should show customer selection in form
const shouldShowCustomerSelection = computed(() => {
  return couponForm.value.idKhachHang === "personal";
});

// Debug computed for form state
const formDebugInfo = computed(() => {
  return {
    formType: couponForm.value.idKhachHang,
    selectedCustomersCount: selectedCustomers.value.length,
    selectedCustomerNames: customers.value
      .filter((c) => selectedCustomers.value.includes(c.id))
      .map((c) => c.tenKhachHang),
  };
});

const resetForm = () => {
  couponForm.value = {
    maPhieuGiamGia: "",
    tenPhieuGiamGia: "",
    moTa: "",
    loaiPhieuGiamGia: false,
    giaTriGiamGia: 0,
    hoaDonToiThieu: 0,
    soTienToiDa: 0,
    soLuongDung: 1,
    ngayBatDau: "",
    ngayKetThuc: "",
    trangThai: true,
    deleted: false,
    idKhachHang: null, // This will be set to array in API calls
  };
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

const clearFilters = () => {
  searchQuery.value = "";
  selectedType.value = "";
  selectedType2.value = "";
  selectedStatus.value = "";
  fromDate.value = "";
  toDate.value = "";
};

const applyFilters = () => {
  // Filters are already applied through computed property
  console.log("Filters applied");
};

const refreshData = () => {
  // Simulate data refresh
  console.log("Refreshing discount coupons data...");
};

const exportData = () => {
  alert("Xuất báo cáo phiếu giảm giá");
};

const exportToExcel = () => {
  try {
    const headerMapping = {
      id: "ID",
      ma_phieu: "Mã phiếu",
      ten_phieu: "Tên phiếu",
      loai_giam_gia: "Loại giảm giá",
      gia_tri: "Giá trị",
      so_luong: "Số lượng",
      da_su_dung: "Đã sử dụng",
      ngay_bat_dau: "Ngày bắt đầu",
      ngay_ket_thuc: "Ngày kết thúc",
      trang_thai: "Trạng thái",
    };

    const filteredData = filteredCoupons.value.map((item) => ({
      id: item.id || "N/A",
      ma_phieu: item.ma_phieu || "N/A",
      ten_phieu: item.ten_phieu || "N/A",
      loai_giam_gia:
        item.loai_giam_gia === "percent" ? "Phần trăm" : "Số tiền cố định",
      gia_tri:
        item.loai_giam_gia === "percent"
          ? `${item.gia_tri}%`
          : new Intl.NumberFormat("vi-VN").format(item.gia_tri),
      so_luong: item.so_luong || 0,
      da_su_dung: item.da_su_dung || 0,
      ngay_bat_dau: item.ngay_bat_dau
        ? new Date(item.ngay_bat_dau).toLocaleDateString("vi-VN")
        : "N/A",
      ngay_ket_thuc: item.ngay_ket_thuc
        ? new Date(item.ngay_ket_thuc).toLocaleDateString("vi-VN")
        : "N/A",
      trang_thai: item.trang_thai === "active" ? "Hoạt động" : "Tạm dừng",
    }));

    const result = exportToExcel(
      filteredData,
      "Discount_Coupons",
      "Danh sách phiếu giảm giá",
      headerMapping
    );

    if (result && result.success) {
      alert(`✅ ${result.message}`);
    } else {
      alert(
        `❌ ${result ? result.message : "Có lỗi xảy ra khi xuất file Excel"}`
      );
    }
  } catch (error) {
    console.error("Error exporting to Excel:", error);
    alert(`❌ Có lỗi xảy ra khi xuất file Excel: ${error.message}`);
  }
};

// Watch filters and reset pagination
watch(
  [searchQuery, selectedType, selectedType2, selectedStatus, fromDate, toDate],
  () => {
    resetPagination();
  }
);

onMounted(() => {
  // Set default dates
  const today = new Date();
  const nextWeek = new Date(today.getTime() + 7 * 24 * 60 * 60 * 1000);

  fromDate.value = today.toISOString().split("T")[0];
  toDate.value = nextWeek.toISOString().split("T")[0];
  fetchPGG();
  fetchPersonalPGG();
  fetchCustomers();
});
</script>

<style scoped>
/* Import Google Fonts */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Poppins:wght@300;400;500;600;700&display=swap');

/* Global font settings */
* {
  font-family: 'Inter', 'Poppins', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.discount-coupons {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 2rem;
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  border-radius: 16px;
  padding: 2rem;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

.page-title {
  font-size:2rem;
  font-weight: 700;
  margin: 0;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  font-family: 'Arial', 'Helvetica', sans-serif;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.page-subtitle {
  font-size: 1.125rem;
  margin: 0.5rem 0 0 0;
  opacity: 0.9;
  font-family: 'Inter', sans-serif;
  letter-spacing: 0.2px;
}

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
  font-family: 'Poppins', sans-serif;
  letter-spacing: -0.3px;
}

.filter-stats {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
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
}

.search-input:focus {
  outline: none;
  border-color: #4ade80;
  background: white;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
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
}

.label-icon {
  font-size: 1rem;
}

/* Action Button Styles */
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

.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
}

.form-select,
.date-input {
  padding: 0.75rem 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 0.875rem;
  transition: all 0.3s ease;
  background: white;
  color: #374151;
}

.form-select:focus,
.date-input:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

.filter-actions {
  grid-column: 1 / -1;
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
}

.btn-outline {
  background: white;
  border: 2px solid #e5e7eb;
  color: #6b7280;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.btn-outline:hover {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  border-color: #22c55e;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(34, 197, 94, 0.3);
}

.btn-outline:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
.table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(74, 222, 128, 0.1);
}

.table th {
  background-color: #4ade80;
  color: white;
  font-weight: 700;
  padding: 0.75rem 0.5rem;
  text-align: center;
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  position: relative;
  border-right: none;
  border-left: none;
  font-family: 'Arial', 'Helvetica', sans-serif;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.table th:first-child {
  border-top-left-radius: 20px;
}

.table th:last-child {
  border-top-right-radius: 20px;
}



.table td {
  padding: 1.25rem 1rem;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid rgba(74, 222, 128, 0.1);
  transition: all 0.3s ease;
  font-size: 0.875rem;
}

.table tbody tr {
  transition: all 0.3s ease;
  cursor: pointer;
}

.table tbody tr:hover {
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.05) 0%, rgba(34, 197, 94, 0.05) 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(74, 222, 128, 0.15);
}

.table tbody tr:last-child td {
  border-bottom: none;
}

.coupon-code {
  font-weight: 700;
  color: #000000;
  font-size: 1rem;
  text-shadow: none;
}

.coupon-name {
  font-weight: 600;
  text-align: left;
  color: #374151;
}

.action-buttons {
  display: flex;
  gap: 0.75rem;
  justify-content: center;
  align-items: center;
}

/* Status badges */
.status-badge {
  display: inline-block;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.status-badge.active {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.status-badge.inactive {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

.status-badge.pending {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(245, 158, 11, 0.3);
}

/* Table responsive */
@media (max-width: 1024px) {
  .table {
    font-size: 0.8rem;
  }
  
  .table th,
  .table td {
    padding: 1rem 0.75rem;
  }
}

/* Card Styles */
.card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(74, 222, 128, 0.15);
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.12);
}

.card-body {
  padding: 2rem;
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.02) 0%, rgba(34, 197, 94, 0.02) 100%);
}

@media (max-width: 768px) {
  .table {
    border-radius: 12px;
  }
  
  .table th,
  .table td {
    padding: 0.75rem 0.5rem;
    font-size: 0.75rem;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 0.5rem;
  }
}

/* Pagination */
.pagination-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 2px solid rgba(74, 222, 128, 0.2);
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.05) 0%, rgba(34, 197, 94, 0.05) 100%);
  padding: 1.5rem 2rem;
  border-radius: 16px;
  margin-left: -2rem;
  margin-right: -2rem;
  margin-bottom: -2rem;
}

.pagination-info {
  font-weight: 600;
  color: #374151;
  font-size: 0.875rem;
  background: white;
  padding: 0.75rem 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(74, 222, 128, 0.1);
}

.pagination {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: white;
  padding: 0.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(74, 222, 128, 0.1);
}

.page-info {
  font-weight: 700;
  color: #4ade80;
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.1) 0%, rgba(34, 197, 94, 0.1) 100%);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.875rem;
  min-width: 80px;
  text-align: center;
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
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.modal-content.large {
  max-width: 800px;
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

/* Form Sections */
.form-sections {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.form-section {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 1.5rem;
}

.form-section h4 {
  margin: 0 0 1rem 0;
  color: var(--secondary-color);
  font-size: 1.125rem;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 0.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

/* Coupon Detail */
.coupon-detail {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.coupon-info h4 {
  text-align: center;
  margin: 0 0 2rem 0;
  color: var(--secondary-color);
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
}

.info-item label {
  font-weight: 500;
  color: var(--medium-gray);
  font-size: 0.875rem;
}

.info-item span {
  color: var(--secondary-color);
}

.discount-value {
  font-weight: 600;
  color: var(--success-color);
}

/* Responsive Design */
@media (max-width: 1200px) {
  .search-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .date-filters {
    flex-direction: column;
  }

  .filter-controls {
    flex-direction: column;
  }
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }

  .pagination-wrapper {
    flex-direction: column;
    gap: 1rem;
  }

  .modal-overlay {
    padding: 1rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}

/* Personal Customers Section */
.personal-customers-section {
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid #e5e7eb;
}

.personal-customers-section h5 {
  margin-bottom: 1rem;
  color: #374151;
  font-size: 1rem;
  font-weight: 600;
}

.customer-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.customer-chip {
  display: inline-block;
  padding: 0.375rem 0.75rem;
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.customer-chip:hover {
  background: #e5e7eb;
  border-color: #9ca3af;
}

/* Customer Selection Styles */
.customer-selection {
  margin-top: 1.5rem;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 1.5rem;
  background: #f9fafb;
}

.customer-selection h5 {
  margin-bottom: 1rem;
  color: #374151;
  font-size: 1rem;
  font-weight: 600;
}

.customer-search {
  margin-bottom: 1rem;
}

.customer-list {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 0.5rem;
  background: white;
  margin-bottom: 1rem;
}

.customer-item {
  display: flex;
  align-items: flex-start;
  padding: 0.75rem;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease;
  margin-bottom: 0.5rem;
  border: 1px solid #e5e7eb;
}

.customer-item:hover {
  background-color: #f3f4f6;
}

.customer-item:last-child {
  margin-bottom: 0;
}

.customer-item input[type="checkbox"] {
  margin-right: 0.75rem;
  margin-top: 0.25rem;
  cursor: pointer;
  flex-shrink: 0;
}

.customer-info {
  flex: 1;
}

.customer-name {
  font-weight: 500;
  color: #374151;
  margin-bottom: 0.25rem;
}

.customer-details {
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
}

.detail-value {
  color: #6b7280;
  text-align: right;
  flex: 1;
}

.empty-customers {
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
}

.available-count {
  padding: 0.5rem;
  background: #f3f4f6;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.875rem;
  color: #6b7280;
  text-align: center;
}

@media (max-width: 768px) {
  .details-grid {
    grid-template-columns: 1fr;
  }

  .customer-list {
    max-height: 250px;
  }

  .detail-label {
    min-width: 90px;
  }
}

/* Status Text Styles */
.status-text {
  font-weight: 600;
  text-align: center;
  transition: all 0.3s ease;
}

.text-green {
  color: #10b981;
}

.text-red {
  color: #ef4444;
}

.status-text:hover {
  transform: scale(1.05);
}

/* Detail Modal Styles */
.detail-modal {
  max-width: 900px;
  max-height: 90vh;
}

.detail-header {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  border-bottom: none;
}

.detail-header .header-content {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
}

.coupon-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.coupon-icon {
  font-size: 2rem;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.coupon-title h3 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.coupon-status {
  margin-left: auto;
}

.detail-header .status-badge {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.875rem;
}

.detail-body {
  padding: 2rem;
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.02) 0%, rgba(34, 197, 94, 0.02) 100%);
}

.detail-section {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(74, 222, 128, 0.1);
  transition: all 0.3s ease;
}

.detail-section:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid rgba(74, 222, 128, 0.2);
}

.section-icon {
  font-size: 1.5rem;
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.section-header h4 {
  margin: 0;
  color: #374151;
  font-size: 1.25rem;
  font-weight: 600;
  font-family: 'Poppins', sans-serif;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.info-item label {
  font-weight: 600;
  color: #6b7280;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-item span {
  color: #374151;
  font-size: 1rem;
  font-weight: 500;
}

.coupon-code {
  font-weight: 700;
  color: #4ade80;
  font-size: 1.125rem;
  background: rgba(74, 222, 128, 0.1);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  border: 1px solid rgba(74, 222, 128, 0.2);
}

.description {
  font-style: italic;
  color: #6b7280;
}

.discount-type {
  font-weight: 600;
  color: #059669;
}

.discount-value {
  font-weight: 700;
  color: #dc2626;
  font-size: 1.125rem;
}

.min-amount, .max-discount {
  font-weight: 600;
  color: #7c3aed;
}

.usage-count, .used-count, .remaining-count {
  font-weight: 700;
  font-size: 1.125rem;
}

.usage-count {
  color: #059669;
}

.used-count {
  color: #dc2626;
}

.remaining-count {
  color: #7c3aed;
}

.usage-rate {
  font-weight: 700;
  color: #f59e0b;
  font-size: 1.125rem;
}

.start-date, .end-date {
  font-weight: 600;
  color: #059669;
}

.time-remaining {
  font-weight: 700;
  color: #dc2626;
  font-size: 1.125rem;
}

.badge {
  display: inline-block;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  text-align: center;
}

.badge-success {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.badge-danger {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

.badge-warning {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(245, 158, 11, 0.3);
}

.customers-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.customers-summary {
  text-align: center;
}

.customers-count {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  font-weight: 600;
  font-size: 1rem;
  box-shadow: 0 4px 12px rgba(74, 222, 128, 0.3);
}

.customer-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.customer-chip {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
  border-radius: 25px;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.customer-chip:hover {
  background: #e5e7eb;
  border-color: #9ca3af;
  transform: translateY(-1px);
}

.customer-avatar {
  font-size: 1rem;
}

.customer-email {
  color: #6b7280;
  font-size: 0.8125rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1.5rem;
}

.stat-item {
  text-align: center;
  padding: 1.5rem;
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.05) 0%, rgba(34, 197, 94, 0.05) 100%);
  border-radius: 12px;
  border: 1px solid rgba(74, 222, 128, 0.1);
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(74, 222, 128, 0.15);
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: #4ade80;
  margin-bottom: 0.5rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-footer {
  background: linear-gradient(135deg, rgba(74, 222, 128, 0.05) 0%, rgba(34, 197, 94, 0.05) 100%);
  border-top: 1px solid rgba(74, 222, 128, 0.2);
  padding: 1.5rem;
}

.detail-footer .btn {
  padding: 0.875rem 1.75rem;
  font-weight: 600;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.detail-footer .btn-outline {
  background: white;
  border: 2px solid #e5e7eb;
  color: #6b7280;
}

.detail-footer .btn-outline:hover {
  background: #f3f4f6;
  border-color: #9ca3af;
  transform: translateY(-1px);
}

.detail-footer .btn-primary {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
  color: white;
  border: 2px solid transparent;
}

.detail-footer .btn-primary:hover {
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(34, 197, 94, 0.3);
}

/* Responsive Design for Detail Modal */
@media (max-width: 768px) {
  .detail-modal {
    max-width: 95vw;
    margin: 1rem;
  }
  
  .detail-header .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .coupon-status {
    margin-left: 0;
    align-self: flex-end;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .customer-chips {
    justify-content: center;
  }
}
</style>
