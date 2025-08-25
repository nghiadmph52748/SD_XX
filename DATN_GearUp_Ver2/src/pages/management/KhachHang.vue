<template>
  <div class="customer-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">Quản lý khách hàng</h1>
          <p class="page-subtitle">Quản lý thông tin và hoạt động khách hàng</p>
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
          <button class="btn-export" @click="addCustomer">
            <span class="btn-icon">➕</span>
            Thêm khách hàng
          </button>
        </div>
      </div>
    </div>

    <!-- Search and Filter Section -->
    <div class="filter-section">
      <div class="search-controls">
        <div class="search-box">
          <input
            type="text"
            placeholder="Tìm kiếm tên hoặc sdt hoặc email"
            v-model="searchQuery"
            class="form-control"
          />
          <button class="btn-export">🔍</button>
        </div>

        <div class="filter-controls">
          <select v-model="selectedGender" class="form-control">
            <option value="">Giới tính: Tất cả</option>
            <option value="Nam">Nam</option>
            <option value="Nữ">Nữ</option>
          </select>

          <select v-model="selectedStatus" class="form-control">
            <option value="">Trạng thái: Tất cả</option>
            <option value="active">Hoạt động</option>
            <option value="inactive">Ngừng hoạt động</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Customers Table -->
    <div class="card">
      <div class="card-body">
        <table class="table">
          <thead>
            <tr>
              <th>STT</th>
              <th>Tên khách hàng</th>
              <th>Email</th>
              <th>Số điện thoại</th>
              <th>Giới tính</th>
              <th>Ngày sinh</th>
              <th>Địa chỉ</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(customer, index) in filteredCustomers"
              :key="customer.id"
            >
              <td>{{ index + 1 }}</td>
              <td class="customer-name">{{ customer.tenKhachHang }}</td>
              <td>{{ customer.email }}</td>
              <td>{{ customer.soDienThoai }}</td>
              <td>{{ customer.gioiTinh ? "Nam" : "Nữ" }}</td>
              <td>{{ customer.ngaySinh }}</td>
              <td>
                {{
                  customer.listDiaChi && customer.listDiaChi.length > 0
                    ? customer.listDiaChi[0].diaChiCuThe +
                      ", " +
                      customer.listDiaChi[0].phuong +
                      ", " +
                      customer.listDiaChi[0].quan +
                      ", " +
                      customer.listDiaChi[0].thanhPho
                    : "Chưa cập nhật"
                }}
              </td>
              <td>
                <span
                  :class="[
                    'badge',
                    customer.deleted === false
                      ? 'badge-success'
                      : 'badge-danger',
                  ]"
                >
                  {{
                    customer.deleted === false ? "Hoạt động" : "Ngừng hoạt động"
                  }}
                </span>
              </td>
              <td>
                <ButtonGroup spacing="xs">
                  <ActionButton
                    icon="view"
                    variant="info"
                    size="sm"
                    tooltip="Xem chi tiết"
                    @click="viewCustomer(customer)"
                  />
                  <ActionButton
                    icon="edit"
                    variant="warning"
                    size="sm"
                    tooltip="Chỉnh sửa thông tin"
                    @click="editCustomer(customer)"
                  />
                  <ActionButton
                    icon="delete"
                    variant="danger"
                    size="sm"
                    tooltip="Xóa khách hàng"
                    @click="deleteCustomer(customer.id)"
                  />
                </ButtonGroup>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Pagination -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của
            {{ totalCustomers }} khách hàng
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

    <!-- Customer Detail Modal -->
    <div
      v-if="showDetailModal"
      class="modal-overlay"
      @click="showDetailModal = false"
    >
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Chi tiết khách hàng</h3>
          <button class="modal-close" @click="showDetailModal = false">
            ✕
          </button>
        </div>

        <div class="modal-body" v-if="selectedCustomer">
          <div class="customer-detail">
            <div class="customer-info">
              <h4>{{ selectedCustomer.tenKhachHang }}</h4>
              <div class="info-grid">
                <div class="info-item">
                  <label>Email:</label>
                  <span>{{ selectedCustomer.email }}</span>
                </div>
                <div class="info-item">
                  <label>Số điện thoại:</label>
                  <span>{{ selectedCustomer.soDienThoai }}</span>
                </div>
                <div class="info-item">
                  <label>Ngày sinh:</label>
                  <span>{{ selectedCustomer.ngaySinh }}</span>
                </div>
                <div class="info-item">
                  <label>Giới tính:</label>
                  <span>{{ selectedCustomer.gioiTinh ? "Nam" : "Nữ" }}</span>
                </div>
                <div class="info-item address-list-item">
                  <label>Địa chỉ:</label>
                  <div
                    v-if="
                      selectedCustomer.listDiaChi &&
                      selectedCustomer.listDiaChi.length > 0
                    "
                    class="address-list"
                  >
                    <div
                      v-for="(address, index) in selectedCustomer.listDiaChi"
                      :key="index"
                      class="address-detail-item"
                    >
                      <span class="address-label"
                        >Địa chỉ {{ index + 1 }}:</span
                      >
                      <span class="address-value">
                        {{
                          address.diaChiCuThe +
                          ", " +
                          address.phuong +
                          ", " +
                          address.quan +
                          ", " +
                          address.thanhPho
                        }}
                      </span>
                    </div>
                  </div>
                  <span v-else>Chưa cập nhật</span>
                </div>
                <div class="info-item">
                  <label>Trạng thái:</label>
                  <span
                    :class="[
                      'badge',
                      selectedCustomer.deleted === false
                        ? 'badge-success'
                        : 'badge-danger',
                    ]"
                  >
                    {{
                      selectedCustomer.deleted === false
                        ? "Hoạt động"
                        : "Ngừng hoạt động"
                    }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Add Customer Modal -->
    <div
      v-if="showAddModal"
      class="modal-overlay"
      @click="showAddModal = false"
    >
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Thông tin khách hàng</h3>
          <button class="modal-close" @click="showAddModal = false">✕</button>
        </div>

        <div class="modal-body">
          <div class="form-sections">
            <div class="form-section">
              <h4>Thông tin cá nhân</h4>

              <div class="form-group">
                <label class="form-label">*Họ và tên</label>
                <input
                  type="text"
                  v-model="customerForm.tenKhachHang"
                  class="form-control"
                  required
                />
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Email</label>
                  <input
                    type="email"
                    v-model="customerForm.email"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Số điện thoại</label>
                  <input
                    type="tel"
                    v-model="customerForm.soDienThoai"
                    class="form-control"
                    required
                  />
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Ngày sinh</label>
                  <input
                    type="date"
                    v-model="customerForm.ngaySinh"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Giới tính</label>
                  <select v-model="customerForm.gioiTinh" class="form-control">
                    <option :value="true">Nam</option>
                    <option :value="false">Nữ</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="form-section">
              <div class="address-header">
                <h4>Địa chỉ</h4>
                <button
                  type="button"
                  class="btn-add-address"
                  @click="addAddress"
                  title="Thêm địa chỉ"
                >
                  <span class="btn-icon">➕</span>
                  Thêm địa chỉ
                </button>
              </div>

              <div
                v-for="(address, index) in customerForm.listDiaChi"
                :key="index"
                class="address-item"
              >
                <div class="address-item-header">
                  <h5>Địa chỉ {{ index + 1 }}</h5>
                  <button
                    v-if="customerForm.listDiaChi.length > 1"
                    type="button"
                    class="btn-remove-address"
                    @click="removeAddress(index)"
                    title="Xóa địa chỉ"
                  >
                    <span class="btn-icon">❌</span>
                  </button>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">*Tỉnh/thành phố</label>
                    <input
                      type="text"
                      v-model="address.thanhPho"
                      class="form-control"
                      placeholder="Nhập tên tỉnh/thành phố"
                      required
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">*Quận/huyện</label>
                    <input
                      type="text"
                      v-model="address.quan"
                      class="form-control"
                      placeholder="Nhập tên quận/huyện"
                      required
                    />
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">*Xã/phường/thị trấn</label>
                    <input
                      type="text"
                      v-model="address.phuong"
                      class="form-control"
                      placeholder="Nhập tên xã/phường/thị trấn"
                      required
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">*Địa chỉ cụ thể</label>
                    <input
                      type="text"
                      v-model="address.diaChiCuThe"
                      class="form-control"
                      required
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            @click="showAddModal = false"
          >
            Hủy
          </button>
          <button type="submit" class="btn btn-primary" @click="saveCustomer">
            Thêm khách hàng
          </button>
        </div>
      </div>
    </div>

    <!-- Edit Customer Modal -->
    <div
      v-if="showEditModal"
      class="modal-overlay"
      @click="showEditModal = false"
    >
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Cập nhật thông tin khách hàng</h3>
          <button class="modal-close" @click="showEditModal = false">✕</button>
        </div>

        <div class="modal-body">
          <div class="form-sections">
            <div class="form-section">
              <h4>Thông tin cá nhân</h4>

              <div class="form-group">
                <label class="form-label">*Họ và tên</label>
                <input
                  type="text"
                  v-model="customerForm.tenKhachHang"
                  class="form-control"
                  required
                />
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Email</label>
                  <input
                    type="email"
                    v-model="customerForm.email"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Số điện thoại</label>
                  <input
                    type="tel"
                    v-model="customerForm.soDienThoai"
                    class="form-control"
                    required
                  />
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Ngày sinh</label>
                  <input
                    type="date"
                    v-model="customerForm.ngaySinh"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Giới tính</label>
                  <select v-model="customerForm.gioiTinh" class="form-control">
                    <option :value="true">Nam</option>
                    <option :value="false">Nữ</option>
                  </select>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">*Trạng thái</label>
                <select v-model="customerForm.deleted" class="form-control">
                  <option :value="false">Hoạt động</option>
                  <option :value="true">Ngừng hoạt động</option>
                </select>
              </div>
            </div>

            <div class="form-section">
              <div class="address-header">
                <h4>Địa chỉ</h4>
                <button
                  type="button"
                  class="btn-add-address"
                  @click="addAddress"
                  title="Thêm địa chỉ"
                >
                  <span class="btn-icon">➕</span>
                  Thêm địa chỉ
                </button>
              </div>

              <div
                v-for="(address, index) in customerForm.listDiaChi"
                :key="index"
                class="address-item"
              >
                <div class="address-item-header">
                  <h5>Địa chỉ {{ index + 1 }}</h5>
                  <button
                    v-if="customerForm.listDiaChi.length > 1"
                    type="button"
                    class="btn-remove-address"
                    @click="removeAddress(index)"
                    title="Xóa địa chỉ"
                  >
                    <span class="btn-icon">❌</span>
                  </button>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">*Tỉnh/thành phố</label>
                    <input
                      type="text"
                      v-model="address.thanhPho"
                      class="form-control"
                      placeholder="Nhập tên tỉnh/thành phố"
                      required
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">*Quận/huyện</label>
                    <input
                      type="text"
                      v-model="address.quan"
                      class="form-control"
                      placeholder="Nhập tên quận/huyện"
                      required
                    />
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label class="form-label">*Xã/phường/thị trấn</label>
                    <input
                      type="text"
                      v-model="address.phuong"
                      class="form-control"
                      placeholder="Nhập tên xã/phường/thị trấn"
                      required
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">*Địa chỉ cụ thể</label>
                    <input
                      type="text"
                      v-model="address.diaChiCuThe"
                      class="form-control"
                      required
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            @click="showEditModal = false"
          >
            Hủy
          </button>
          <button type="submit" class="btn btn-primary" @click="saveCustomer">
            Cập nhật khách hàng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import ActionButton from "@/components/ui/NutHanhDong.vue";
import ButtonGroup from "@/components/ui/NhomNut.vue";
import {
  fetchAllKhachHang,
  fetchCreateKhachHang,
  fetchUpdateKhachHang,
  fetchUpdateStatusKhachHang,
} from "../../services/KhachHang/KhachHangService";

// Data
const searchQuery = ref("");
const selectedGender = ref("");
const selectedStatus = ref("");
const showDetailModal = ref(false);
const showAddModal = ref(false);
const showEditModal = ref(false);
const selectedCustomer = ref({});

// Pagination data
const currentPage = ref(1);
const itemsPerPage = ref(10);

// Dữ liệu form khách hàng
const customerForm = ref({
  tenKhachHang: "",
  email: "",
  soDienThoai: "",
  gioiTinh: true,
  ngaySinh: "",
  deleted: false,
  listDiaChi: [
    {
      diaChiCuThe: "",
      thanhPho: "",
      quan: "",
      phuong: "",
    },
  ],
});

// Mock data
const customers = ref([]);

const fetchAll = async () => {
  try {
    const response = await fetchAllKhachHang();
    customers.value = response.data;
  } catch (error) {
    console.error("Error fetching customers:", error);
  }
};
// Computed
const filteredCustomers = computed(() => {
  let filtered = customers.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (customer) =>
        customer.tenKhachHang
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        customer.email
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        customer.soDienThoai.includes(searchQuery.value)
    );
  }

  if (selectedGender.value) {
    const isNam = selectedGender.value === "Nam";
    filtered = filtered.filter((customer) => customer.gioiTinh === isNam);
  }

  if (selectedStatus.value) {
    const isActive = selectedStatus.value === "active";
    filtered = filtered.filter((customer) => customer.deleted === !isActive);
  }

  return filtered.slice(startIndex.value, endIndex.value);
});

const totalCustomers = computed(() => {
  let filtered = customers.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (customer) =>
        customer.tenKhachHang
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        customer.email
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        customer.soDienThoai.includes(searchQuery.value)
    );
  }

  if (selectedGender.value) {
    const isNam = selectedGender.value === "Nam";
    filtered = filtered.filter((customer) => customer.gioiTinh === isNam);
  }

  if (selectedStatus.value) {
    const isActive = selectedStatus.value === "active";
    filtered = filtered.filter((customer) => customer.deleted === !isActive);
  }

  return filtered.length;
});

const totalPages = computed(() =>
  Math.ceil(totalCustomers.value / itemsPerPage.value)
);

const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);
const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, totalCustomers.value)
);

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

const viewCustomer = (customer) => {
  selectedCustomer.value = { ...customer };
  showDetailModal.value = true;
};

const editCustomer = (customer) => {
  // Điền dữ liệu khách hàng vào form
  customerForm.value = {
    ...customer,
    listDiaChi:
      customer.listDiaChi && customer.listDiaChi.length > 0
        ? customer.listDiaChi
        : [
            {
              diaChiCuThe: "",
              thanhPho: "",
              quan: "",
              phuong: "",
            },
          ],
  };
  showEditModal.value = true;
};

const addCustomer = () => {
  resetForm();
  showAddModal.value = true;
};

const saveCustomer = async () => {
  try {
    if (showAddModal.value) {
      await fetchCreateKhachHang(customerForm.value);
      currentPage.value = 1;
    } else if (showEditModal.value) {
      await fetchUpdateKhachHang(customerForm.value.id, customerForm.value);
    }
    showAddModal.value = false;
    showEditModal.value = false;
    await fetchAll();
    resetForm();
  } catch (error) {
    console.error("Lỗi khi lưu khách hàng:", error);
    alert("Có lỗi xảy ra khi lưu thông tin khách hàng");
  }
};

const deleteCustomer = async (id) => {
  await fetchUpdateStatusKhachHang(id);
  await fetchAll();
};

const addAddress = () => {
  customerForm.value.listDiaChi.push({
    diaChiCuThe: "",
    thanhPho: "",
    quan: "",
    phuong: "",
  });
};

const removeAddress = (index) => {
  if (customerForm.value.listDiaChi.length > 1) {
    customerForm.value.listDiaChi.splice(index, 1);
  }
};

const resetForm = () => {
  customerForm.value = {
    tenKhachHang: "",
    email: "",
    soDienThoai: "",
    gioiTinh: true,
    ngaySinh: "",
    deleted: false,
    listDiaChi: [
      {
        diaChiCuThe: "",
        thanhPho: "",
        quan: "",
        phuong: "",
      },
    ],
  };
};

const refreshData = async () => {
  await fetchAll();
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

const exportData = () => {
  alert("Chức năng xuất báo cáo đang được phát triển");
};

const exportToExcel = () => {
  try {
    const headerMapping = {
      name: "Họ tên",
      email: "Email",
      phone: "Số điện thoại",
      birthDate: "Ngày sinh",
      gender: "Giới tính",
      status: "Trạng thái",
      address: "Địa chỉ",
      totalOrders: "Tổng đơn hàng",
      totalSpent: "Tổng chi tiêu",
    };

    const filteredData = filteredCustomers.value.map((item) => ({
      name: item.name || "N/A",
      email: item.email || "N/A",
      phone: item.phone || "N/A",
      birthDate: item.birthDate || "N/A",
      gender: item.gender || "N/A",
      status: item.status === "active" ? "Hoạt động" : "Ngừng hoạt động",
      address: item.address || "N/A",
      totalOrders: item.totalOrders || 0,
      totalSpent: formatCurrency(item.totalSpent || 0),
    }));

    console.log("Exporting customers to Excel:", filteredData);
    alert("✅ Xuất file Excel thành công!");
  } catch (error) {
    console.error("Error exporting to Excel:", error);
    alert("❌ Có lỗi xảy ra khi xuất file Excel");
  }
};
onMounted(fetchAll);
</script>

<style scoped>
.customer-management {
  max-width: 1400px;
  margin: 0 auto;
}

/* page-header styles are now defined in globals.css */

/* Filter Section */
.filter-section {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: var(--shadow);
}

.search-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  display: flex;
  gap: 0.5rem;
  flex: 1;
  min-width: 300px;
}

.filter-controls {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.filter-controls select {
  min-width: 150px;
}

/* Table Styles */
.table th {
  background-color: #4ade80;
  color: white;
  font-weight: 600;
  padding: 1rem;
  text-align: center;
  font-size: 0.875rem;
  white-space: nowrap;
  position: sticky;
  top: 0;
  z-index: 10;
}

.table td {
  padding: 1rem;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid var(--border-color);
  font-size: 0.875rem;
}

.customer-name {
  font-weight: 500;
  text-align: left;
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
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
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

/* Customer Detail */
.customer-detail {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.customer-info h4 {
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

.total-orders {
  font-weight: 600;
  color: var(--info-color);
}

.total-spent {
  font-weight: 600;
  color: var(--success-color);
}

/* Modal Footer */
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

.form-section h4 {
  margin: 0 0 1rem 0;
  color: var(--secondary-color);
  font-size: 1.125rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-weight: 500;
  color: var(--medium-gray);
  font-size: 0.875rem;
}

.form-control {
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 0.875rem;
  transition: border-color 0.3s ease;
}

.form-control:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(79, 172, 254, 0.1);
}

/* Address Components */
.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.btn-add-address {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: #4ade80;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: background-color 0.3s ease;
}

.btn-add-address:hover {
  background-color: #22c55e;
}

.address-item {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1rem;
  background-color: #f9fafb;
}

.address-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.address-item-header h5 {
  margin: 0;
  color: var(--secondary-color);
  font-size: 1rem;
  font-weight: 600;
}

.btn-remove-address {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background-color: #ef4444;
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 0.75rem;
  transition: background-color 0.3s ease;
}

.btn-remove-address:hover {
  background-color: #dc2626;
}

.btn-remove-address .btn-icon {
  font-size: 0.875rem;
}

/* Address Detail Display */
.address-list-item {
  grid-column: 1 / -1;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.address-detail-item {
  padding: 0.75rem;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.address-label {
  font-weight: 500;
  color: var(--medium-gray);
  font-size: 0.8rem;
}

.address-value {
  color: var(--secondary-color);
  font-size: 0.875rem;
}

/* Responsive Design */
@media (max-width: 1200px) {
  .customer-management {
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

  .form-row {
    grid-template-columns: 1fr;
  }

  .address-header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .btn-add-address {
    justify-content: center;
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
  .table th:nth-child(6),
  .table td:nth-child(6) {
    display: none;
  }
}

@media (max-width: 480px) {
  .customer-management {
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
</style>
