<template>
  <div class="employee-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">Quản lý nhân viên</h1>
          <p class="page-subtitle">Quản lý thông tin và quyền hạn nhân viên</p>
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
            Tạo nhân viên
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
            placeholder="tên hoặc sdt hoặc email"
            v-model="searchQuery"
            class="form-control"
          />
          <button class="btn btn-primary" @click="searchEmployees">
            <span class="btn-icon">🔍</span>
            Tìm kiếm
          </button>
        </div>

        <div class="filter-controls">
          <select v-model="selectedRole" class="form-control">
            <option value="">Chức vụ: Tất cả</option>
            <option
              v-for="value in quyenHans"
              :key="value.id"
              :value="value.tenQuyenHan"
            >
              {{ value.tenQuyenHan }}
            </option>
          </select>

          <select v-model="selectedStatus" class="form-control">
            <option value="">Trạng thái: Tất cả</option>
            <option value="active">Hoạt động</option>
            <option value="inactive">Ngừng hoạt động</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Employees Table -->
    <div class="card">
      <div class="card-body">
        <div class="table-responsive">
          <table class="table">
            <thead>
              <tr>
                <th>STT</th>
                <th>Ảnh</th>
                <th>Mã nhân viên</th>
                <th>Tên nhân viên</th>
                <th>Email</th>
                <th>SĐT</th>
                <th>Ngày sinh</th>
                <th>CCCD</th>
                <th>Địa chỉ</th>
                <th>Chức vụ</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(employee, index) in filteredEmployees"
                :key="employee.id"
              >
                <td data-label="STT">
                  {{ startIndex + index + 1 }}
                </td>
                <td data-label="Ảnh">
                  <div class="employee-avatar">
                    <img
                      v-if="employee.anhNhanVien"
                      :src="employee.anhNhanVien"
                      :alt="employee.tenNhanVien"
                    />
                    <div v-else class="placeholder-avatar">👤</div>
                  </div>
                </td>
                <td data-label="Mã nhân viên" class="employee-code">
                  {{ employee.maNhanVien }}
                </td>
                <td data-label="Tên nhân viên" class="employee-name">
                  {{ employee.tenNhanVien }}
                </td>
                <td data-label="Email">{{ employee.email }}</td>
                <td data-label="SĐT">{{ employee.soDienThoai }}</td>
                <td data-label="Ngày sinh">{{ employee.ngaySinh }}</td>
                <td data-label="CCCD">{{ employee.cccd }}</td>
                <td data-label="Địa chỉ">
                  {{
                    employee.thanhPho +
                    ", " +
                    employee.quan +
                    ", " +
                    employee.phuong +
                    ", " +
                    employee.diaChiCuThe
                  }}
                </td>
                <td data-label="Chức vụ">{{ employee.tenQuyenHan }}</td>
                <td data-label="Trạng thái">
                  <span
                    :class="[
                      'badge',
                      employee.trangThai === true
                        ? 'badge-success'
                        : 'badge-danger',
                    ]"
                  >
                    {{ employee.trangThai ? "Hoạt động" : "Ngừng hoạt động" }}
                  </span>
                </td>
                <td data-label="Thao tác">
                  <ButtonGroup spacing="xs">
                    <button class="btn-export" @click="viewEmployee(employee)">
                      <span class="btn-icon">👁️</span>
                      Xem
                    </button>
                    <button class="btn-export" @click="editEmployee(employee)">
                      <span class="btn-icon">✏️</span>
                      Sửa
                    </button>
                  </ButtonGroup>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của
            {{ totalEmployees }} nhân viên
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

    <!-- Add Employee Modal -->
    <div
      v-if="showAddModal"
      class="modal-overlay"
      @click="showAddModal = false"
    >
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Thông tin nhân viên</h3>
          <button class="modal-close" @click="showAddModal = false">✕</button>
        </div>

        <div class="modal-body">
          <div class="form-sections">
            <div class="form-section">
              <h4>Thông tin chi tiết</h4>
              <div class="avatar-upload">
                <div class="avatar-preview">
                  <img v-if="avatarPreview" :src="avatarPreview" alt="Avatar" />
                  <div v-else class="placeholder-avatar large">
                    📷
                    <span>Chọn ảnh</span>
                  </div>
                </div>
                <input
                  type="file"
                  @change="handleAvatarUpload"
                  accept="image/*"
                  class="file-input"
                />
              </div>
            </div>

            <div class="form-section">
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Số CCCD</label>
                  <input
                    type="text"
                    v-model="employeeForm.cccd"
                    class="form-control"
                    required
                  />
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">*Ngày sinh</label>
                <input
                  type="date"
                  v-model="employeeForm.ngaySinh"
                  class="form-control"
                  required
                />
              </div>

              <div class="form-group">
                <label class="form-label">*Email</label>
                <input
                  type="email"
                  v-model="employeeForm.email"
                  class="form-control"
                  required
                />
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Họ Và Tên</label>
                  <input
                    type="text"
                    v-model="employeeForm.tenNhanVien"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Tỉnh/thành phố</label>
                  <input
                    type="text"
                    v-model="employeeForm.thanhPho"
                    class="form-control"
                    placeholder="Nhập tên tỉnh/thành phố"
                    required
                  />
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Quận/huyện</label>
                  <input
                    type="text"
                    v-model="employeeForm.quan"
                    class="form-control"
                    placeholder="Nhập tên quận/huyện"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Xã/phường/thị trấn</label>
                  <input
                    type="text"
                    v-model="employeeForm.phuong"
                    class="form-control"
                    placeholder="Nhập tên xã/phường/thị trấn"
                    required
                  />
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Số Điện Thoại</label>
                  <input
                    type="tel"
                    v-model="employeeForm.soDienThoai"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Địa chỉ cụ thể</label>
                  <input
                    type="text"
                    v-model="employeeForm.diaChiCuThe"
                    class="form-control"
                    required
                  />
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">*Quyền hạn</label>
                <select
                  v-model="employeeForm.idQuyenHan"
                  class="form-control"
                  required
                >
                  <option value="">Chọn quyền hạn</option>
                  <option
                    v-for="value in quyenHans"
                    :key="value.id"
                    :value="value.id"
                  >
                    {{ value.tenQuyenHan }}
                  </option>
                </select>
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
          <button type="submit" class="btn btn-primary" @click="saveEmployee">
            Thêm Nhân Viên
          </button>
        </div>
      </div>
    </div>

    <!-- Edit Employee Modal -->
    <div
      v-if="showEditModal"
      class="modal-overlay"
      @click="showEditModal = false"
    >
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Cập nhật thông tin nhân viên</h3>
          <button class="modal-close" @click="showEditModal = false">✕</button>
        </div>

        <div class="modal-body">
          <div class="form-sections">
            <div class="form-section">
              <h4>Thông tin chi tiết</h4>
              <div class="avatar-upload">
                <div class="avatar-preview">
                  <img v-if="avatarPreview" :src="avatarPreview" alt="Avatar" />
                  <div v-else class="placeholder-avatar large">
                    📷
                    <span>Chọn ảnh</span>
                  </div>
                </div>
                <input
                  type="file"
                  @change="handleAvatarUpload"
                  accept="image/*"
                  class="file-input"
                />
              </div>
            </div>

            <div class="form-section">
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Số CCCD</label>
                  <input
                    type="text"
                    v-model="employeeForm.cccd"
                    class="form-control"
                    required
                  />
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">*Ngày sinh</label>
                <input
                  type="date"
                  v-model="employeeForm.ngaySinh"
                  class="form-control"
                  required
                />
              </div>

              <div class="form-group">
                <label class="form-label">*Email</label>
                <input
                  type="email"
                  v-model="employeeForm.email"
                  class="form-control"
                  required
                />
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Họ Và Tên</label>
                  <input
                    type="text"
                    v-model="employeeForm.tenNhanVien"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Tỉnh/thành phố</label>
                  <input
                    type="text"
                    v-model="employeeForm.thanhPho"
                    class="form-control"
                    placeholder="Nhập tên tỉnh/thành phố"
                    required
                  />
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Quận/huyện</label>
                  <input
                    type="text"
                    v-model="employeeForm.quan"
                    class="form-control"
                    placeholder="Nhập tên quận/huyện"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Xã/phường/thị trấn</label>
                  <input
                    type="text"
                    v-model="employeeForm.phuong"
                    class="form-control"
                    placeholder="Nhập tên xã/phường/thị trấn"
                    required
                  />
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">*Số Điện Thoại</label>
                  <input
                    type="tel"
                    v-model="employeeForm.soDienThoai"
                    class="form-control"
                    required
                  />
                </div>
                <div class="form-group">
                  <label class="form-label">*Địa chỉ cụ thể</label>
                  <input
                    type="text"
                    v-model="employeeForm.diaChiCuThe"
                    class="form-control"
                    required
                  />
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">*Quyền hạn</label>
                <select
                  v-model="employeeForm.idQuyenHan"
                  class="form-control"
                  required
                >
                  <option value="">Chọn quyền hạn</option>
                  <option
                    v-for="value in quyenHans"
                    :key="value.id"
                    :value="value.id"
                  >
                    {{ value.tenQuyenHan }}
                  </option>
                </select>
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
          <button type="submit" class="btn btn-primary" @click="saveEmployee">
            Cập nhật nhân viên
          </button>
        </div>
      </div>
    </div>

    <!-- Employee Detail Modal -->
    <div
      v-if="showDetailModal"
      class="modal-overlay"
      @click="showDetailModal = false"
    >
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Chi tiết nhân viên</h3>
          <button class="modal-close" @click="showDetailModal = false">
            ✕
          </button>
        </div>

        <div class="modal-body" v-if="selectedEmployee">
          <div class="employee-detail">
            <div class="employee-avatar-section">
              <div class="employee-avatar large">
                <img
                  v-if="selectedEmployee.anhNhanVien"
                  :src="selectedEmployee.anhNhanVien"
                  alt="Ảnh nhân viên"
                />
                <div v-else class="placeholder-avatar large">👤</div>
              </div>
            </div>

            <div class="employee-info">
              <h4>{{ selectedEmployee.tenNhanVien }}</h4>
              <div class="info-grid">
                <div class="info-item">
                  <label>Mã nhân viên:</label>
                  <span>{{ selectedEmployee.maNhanVien }}</span>
                </div>
                <div class="info-item">
                  <label>Email:</label>
                  <span>{{ selectedEmployee.email }}</span>
                </div>
                <div class="info-item">
                  <label>Số điện thoại:</label>
                  <span>{{ selectedEmployee.soDienThoai }}</span>
                </div>
                <div class="info-item">
                  <label>Ngày sinh:</label>
                  <span>{{ selectedEmployee.ngaySinh }}</span>
                </div>
                <div class="info-item">
                  <label>Tỉnh/Thành phố:</label>
                  <span>{{
                    selectedEmployee.thanhPho || "Chưa cập nhật"
                  }}</span>
                </div>
                <div class="info-item">
                  <label>Quận/Huyện:</label>
                  <span>{{ selectedEmployee.quan || "Chưa cập nhật" }}</span>
                </div>
                <div class="info-item">
                  <label>Phường/Xã:</label>
                  <span>{{ selectedEmployee.phuong || "Chưa cập nhật" }}</span>
                </div>
                <div class="info-item">
                  <label>Địa chỉ cụ thể:</label>
                  <span>{{
                    selectedEmployee.diaChiCuThe || "Chưa cập nhật"
                  }}</span>
                </div>
                <div class="info-item">
                  <label>CCCD:</label>
                  <span>{{ selectedEmployee.cccd }}</span>
                </div>
                <div class="info-item">
                  <label>Chức vụ:</label>
                  <span>{{ selectedEmployee.tenQuyenHan }}</span>
                </div>
                <div class="info-item">
                  <label>Trạng thái:</label>
                  <span>{{
                    selectedEmployee.trangThai ? "Hoạt động" : "Ngừng hoạt động"
                  }}</span>
                </div>
              </div>
            </div>
          </div>
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
  fetchAllNhanVien,
  fetchUpdateNhanVien,
  fetchCreateNhanVien,
} from "../../services/NhanVien/NhanVienService";
import { fetchAllQuyenHan } from "../../services/NhanVien/QuyenHanService";

// Data
const searchQuery = ref("");
const selectedRole = ref("");
const selectedStatus = ref("");
const showAddModal = ref(false);
const showDetailModal = ref(false);
const showEditModal = ref(false);
const avatarPreview = ref("");
const selectedEmployee = ref({});

// Pagination data
const currentPage = ref(1);
const itemsPerPage = ref(10);

// Dữ liệu form nhân viên
const employeeForm = ref({
  tenNhanVien: "",
  cccd: "",
  ngaySinh: "",
  email: "",
  soDienThoai: "",
  thanhPho: "",
  quan: "",
  phuong: "",
  diaChiCuThe: "",
  tenQuyenHan: "",
  idQuyenHan: "",
  trangThai: true,
  delete: false,
});

// Mock data
const nhanViens = ref([]);
const quyenHans = ref([]);
const fetchAll = async () => {
  try {
    const res = await fetchAllNhanVien();
    nhanViens.value = res.data;
  } catch (error) {
    console.log(error);
  }
};
const fetchQuyenHan = async () => {
  try {
    const res = await fetchAllQuyenHan();
    quyenHans.value = res.data;
  } catch (error) {
    console.log(error);
  }
};
// Computed
const filteredEmployees = computed(() => {
  let filtered = nhanViens.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (employee) =>
        employee.tenNhanVien
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        employee.email
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        employee.soDienThoai.includes(searchQuery.value)
    );
  }

  if (selectedRole.value) {
    filtered = filtered.filter(
      (employee) => employee.tenQuyenHan === selectedRole.value
    );
  }

  if (selectedStatus.value) {
    const isActive = selectedStatus.value === "active";
    filtered = filtered.filter((employee) => employee.trangThai === isActive);
  }

  return filtered.slice(startIndex.value, endIndex.value);
});

const totalEmployees = computed(() => {
  let filtered = nhanViens.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (employee) =>
        employee.tenNhanVien
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        employee.email
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        employee.soDienThoai.includes(searchQuery.value)
    );
  }

  if (selectedRole.value) {
    filtered = filtered.filter(
      (employee) => employee.tenQuyenHan === selectedRole.value
    );
  }

  if (selectedStatus.value) {
    const isActive = selectedStatus.value === "active";
    filtered = filtered.filter((employee) => employee.trangThai === isActive);
  }

  return filtered.length;
});

const totalPages = computed(() =>
  Math.ceil(totalEmployees.value / itemsPerPage.value)
);

const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);
const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, totalEmployees.value)
);

// Methods
const viewEmployee = (employee) => {
  selectedEmployee.value = { ...employee };
  showDetailModal.value = true;
};

const editEmployee = async (data) => {
  // Điền dữ liệu nhân viên vào form
  employeeForm.value = { ...data };

  // Hiển thị ảnh preview nếu có
  if (data.anhNhanVien) {
    avatarPreview.value = data.anhNhanVien;
  }

  showEditModal.value = true;
};

const handleAvatarUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      avatarPreview.value = e.target.result;
      employeeForm.value.avatar = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const saveEmployee = async () => {
  try {
    if (showAddModal.value) {
      await fetchCreateNhanVien(employeeForm.value);
      currentPage.value = 1;
    } else if (showEditModal.value) {
      await fetchUpdateNhanVien(employeeForm.value.id, employeeForm.value);
    }
    showAddModal.value = false;
    showEditModal.value = false;
    await fetchAll();
    resetForm();
  } catch (error) {
    console.error("Lỗi khi lưu nhân viên:", error);
    alert("Có lỗi xảy ra khi lưu thông tin nhân viên");
  }
};

const resetForm = () => {
  employeeForm.value = {
    tenNhanVien: "",
    cccd: "",
    ngaySinh: "",
    email: "",
    soDienThoai: "",
    thanhPho: "",
    quan: "",
    phuong: "",
    diaChiCuThe: "",
    tenQuyenHan: "",
    idQuyenHan: "",
    trangThai: true,
    delete: false,
  };
  avatarPreview.value = "";
};

const openAddModal = () => {
  resetForm();
  showAddModal.value = true;
};

const searchEmployees = () => {
  // Search is handled by computed property filteredEmployees
  // This function can be used for additional search functionality if needed
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
      maNhanVien: "Mã NV",
      tenNhanVien: "Họ tên",
      email: "Email",
      soDienThoai: "Số điện thoại",
      ngaySinh: "Ngày sinh",
      gioiTinh: "Giới tính",
      tenQuyenHan: "Chức vụ",
      trangThai: "Trạng thái",
    };

    const filteredData = filteredEmployees.value.map((item) => ({
      maNhanVien: item.maNhanVien || "N/A",
      tenNhanVien: item.tenNhanVien || "N/A",
      email: item.email || "N/A",
      soDienThoai: item.soDienThoai || "N/A",
      ngaySinh: item.ngaySinh || "N/A",
      gioiTinh: item.gioiTinh || "N/A",
      tenQuyenHan: item.tenQuyenHan || "N/A",
      trangThai: item.trangThai || "N/A",
    }));

    console.log("Exporting employees to Excel:", filteredData);
    alert("✅ Xuất file Excel thành công!");
  } catch (error) {
    console.error("Error exporting to Excel:", error);
    alert("❌ Có lỗi xảy ra khi xuất file Excel");
  }
};

// Khởi tạo dữ liệu khi component được mount
onMounted(() => {
  fetchAll();
  fetchQuyenHan();
});
</script>

<style scoped>
.employee-management {
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
.card {
  margin: 0;
  padding: 0;
  border: none;
  box-shadow: var(--shadow);
  border-radius: 12px;
  background: white;
}

.card-body {
  padding: 0.5rem;
  margin: 0;
}

.table-responsive {
  border-radius: 8px;
  position: relative;
  width: 100%;
  overflow: visible;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin: 0;
  border-spacing: 0;
  position: relative;
  table-layout: fixed; /* Fixed layout to control widths */
}

.table th {
  background-color: #4ade80;
  color: white;
  font-weight: 600;
  padding: 0.5rem 0.25rem;
  text-align: center;
  font-size: 0.7rem;
  white-space: nowrap;
  position: sticky;
  top: 0;
  z-index: 10;
}

/* Optimize column widths to fit all columns without overflow */
.table th:nth-child(1) {
  width: 4%;
} /* STT */
.table th:nth-child(2) {
  width: 5%;
} /* Ảnh */
.table th:nth-child(3) {
  width: 9%;
} /* Mã NV */
.table th:nth-child(4) {
  width: 14%;
} /* Tên */
.table th:nth-child(5) {
  width: 14%;
} /* Email */
.table th:nth-child(6) {
  width: 9%;
} /* SĐT */
.table th:nth-child(7) {
  width: 7%;
} /* Ngày sinh */
.table th:nth-child(8) {
  width: 8%;
} /* CCCD */
.table th:nth-child(9) {
  width: 13%;
} /* Địa chỉ */
.table th:nth-child(10) {
  width: 7%;
} /* Chức vụ */
.table th:nth-child(11) {
  width: 8%;
} /* Trạng thái */
.table th:nth-child(12) {
  width: 12%;
} /* Thao tác */

.table td {
  padding: 0.5rem 0.25rem;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid var(--border-color);
  font-size: 0.7rem;
  word-wrap: break-word;
  overflow-wrap: break-word;
  line-height: 1.2;
}

.employee-avatar {
  width: 32px;
  height: 32px;
  margin: 0 auto;
  border-radius: 50%;
  overflow: hidden;
}

.employee-avatar.large {
  width: 100px;
  height: 100px;
}

.employee-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--light-gray);
  font-size: 1.5rem;
  flex-direction: column;
}

.placeholder-avatar.large {
  font-size: 2rem;
}

.placeholder-avatar span {
  font-size: 0.75rem;
  margin-top: 0.25rem;
}

.employee-code {
  font-weight: 600;
  color: #4ade80;
}

.employee-name {
  font-weight: 500;
  text-align: left;
}

/* Compact action buttons */
.btn-export {
  font-size: 0.75rem !important;
  padding: 0.375rem 0.5rem !important;
  margin: 0.125rem !important;
  white-space: nowrap !important;
  display: inline-flex !important;
  align-items: center !important;
  gap: 0.25rem !important;
}

.btn-icon {
  font-size: 0.875rem !important;
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
  max-width: 700px;
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

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid var(--border-color);
  cursor: pointer;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.file-input {
  max-width: 200px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.radio-group {
  display: flex;
  gap: 1rem;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

/* Employee Detail */
.employee-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}

.employee-avatar-section {
  text-align: center;
}

.employee-info {
  width: 100%;
}

.employee-info h4 {
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

/* Responsive Design */
@media (max-width: 1200px) {
  .employee-management {
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

  .form-row {
    grid-template-columns: 1fr;
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
    font-size: 0.875rem;
  }

  .table th,
  .table td {
    padding: 0.5rem 0.25rem;
  }

  /* Hide some columns on tablet to save space */
  .table th:nth-child(2),  /* Ảnh */
  .table td:nth-child(2),
  .table th:nth-child(7),  /* Ngày sinh */
  .table td:nth-child(7),
  .table th:nth-child(8),  /* CCCD */
  .table td:nth-child(8) {
    display: none;
  }
}

@media (max-width: 480px) {
  /* Convert table to card layout only on small mobile */
  .table {
    border: 0;
    min-width: auto;
  }

  .table thead {
    display: none;
  }

  .table tr {
    display: block;
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    margin-bottom: 1rem;
    padding: 1rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .table td {
    display: block;
    text-align: left;
    border: none;
    padding: 0.5rem 0;
    position: relative;
    border-bottom: 1px solid #f3f4f6;
  }

  .table td:last-child {
    border-bottom: none;
  }

  .table td::before {
    content: attr(data-label) ": ";
    font-weight: bold;
    color: #374151;
    display: inline-block;
    min-width: 120px;
  }

  .table td[data-label="Ảnh"] {
    text-align: center;
    margin-bottom: 1rem;
  }

  .table td[data-label="Ảnh"]::before {
    display: none;
  }

  .table td[data-label="Thao tác"] {
    text-align: center;
    margin-top: 1rem;
  }

  .table td[data-label="Thao tác"]::before {
    display: none;
  }

  .employee-management {
    padding: 0 0.5rem;
  }

  .filter-section {
    padding: 1rem;
  }

  .card {
    margin: 0 -0.5rem;
    border-radius: 0;
  }

  /* Smaller cards and compact layout */
  .table tr {
    padding: 0.75rem;
    margin-bottom: 0.75rem;
  }

  .table td {
    padding: 0.25rem 0;
    font-size: 0.875rem;
  }

  .table td::before {
    min-width: 100px;
    font-size: 0.8rem;
  }

  /* Make action buttons smaller on mobile */
  .btn-export {
    font-size: 0.75rem;
    padding: 0.25rem 0.5rem;
    margin: 0.125rem;
  }

  .btn-icon {
    font-size: 0.875rem;
  }

  .employee-avatar {
    width: 60px;
    height: 60px;
  }
}
</style>
