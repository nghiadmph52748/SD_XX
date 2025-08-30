<template>
  <div class="customer-management">

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
     <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px;">
        <div style="font-weight: bold; font-size: 16px; display: flex; align-items: center; gap: 6px;">
          📋 Danh sách Khách Hàng
        </div>
      </div>
      <hr style="margin-top: 0; margin-bottom: 15px;" />

      <div style="display: flex; gap: 12px; flex-wrap: wrap; margin-bottom: 20px;">
    <button class="custom-button" @click="showAddModal = true">
      <i class="fas fa-plus-circle"></i> Thêm nhân viên
    </button>
    <button class="custom-button" @click="exportToExcel">
      <i class="fas fa-download"></i> Download template
    </button>
    </div>
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
                      class="action-button-info"
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
                <div class="info-box">
                  <label>Email:</label>
                  <span>{{ selectedCustomer.email }}</span>
                </div>
                <div class="info-box">
                  <label>Số điện thoại:</label>
                  <span>{{ selectedCustomer.soDienThoai }}</span>
                </div>
                <div class="info-box">
                  <label>Ngày sinh:</label>
                  <span>{{ selectedCustomer.ngaySinh }}</span>
                </div>
                <div class="info-box">
                  <label>Giới tính:</label>
                  <span>{{ selectedCustomer.gioiTinh ? "Nam" : "Nữ" }}</span>
                </div>
                <div class="info-box">
                  <label>Tài khoản:</label>
                  <span>{{ selectedCustomer.tenTaiKhoan }}</span>
                </div>
                <div class="info-box">
                  <label>Mật khẩu:</label>
                  <span>{{ selectedCustomer.matKhau }}</span>
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
              <h4>Thông tin tài khoản</h4>
            </div>
            <div class="form-group">
              <label class="form-label">*Tài khoản</label>
              <input
                type="text"
                v-model="customerForm.tenTaiKhoan"
                class="form-control"
                required
              />
            </div>
            <div class="form-group">
              <label class="form-label">*Mật khẩu</label>
              <input
                type="password"
                v-model="customerForm.matKhau"
                class="form-control"
                required
              />
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
              <label class="form-label">*Tài khoản</label>
              <input
                type="text"
                v-model="customerForm.tenTaiKhoan"
                class="form-control"
                required
              />
            </div>
            <div class="form-group"></div>
            <label class="form-label">*Mật khẩu</label>
            <input
              type="password"
              v-model="customerForm.matKhau"
              class="form-control"
              required
            />
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
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import ActionButton from "@/components/ui/NutHanhDong.vue";
import ButtonGroup from "@/components/ui/NhomNut.vue";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";

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
  tenTaiKhoan: "",
  matKhau: "",
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
    customers.value = response.data; // giữ nguyên tất cả, lọc ở computed
  } catch (res) {
    console.log(res.message);
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
  } catch (res) {
    console.log(res.message);
    alert("Có lỗi xảy ra khi lưu thông tin khách hàng");
  }
};

const deleteCustomer = async (id) => {
  const confirmDelete = window.confirm("Bạn có chắc chắn muốn xoá khách hàng này không?");
  if (!confirmDelete) return;

  try {
    await fetchUpdateStatusKhachHang(id); // cập nhật deleted = true
    customers.value = customers.value.filter((c) => c.id !== id); // ẩn ngay khỏi bảng
    alert("✅ Đã xoá khách hàng khỏi danh sách hiển thị.");
  } catch (error) {
    console.error("❌ Lỗi khi xoá khách hàng:", error.message);
    alert("❌ Có lỗi xảy ra khi xoá khách hàng.");
  }
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
    tenTaiKhoan: "",
    matKhau: "",
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


const exportToExcel = () => {
  try {
    const headerMapping = {
      tenKhachHang: "Họ tên",
      email: "Email",
      soDienThoai: "Số điện thoại",
      ngaySinh: "Ngày sinh",
      gioiTinh: "Giới tính",
      deleted: "Trạng thái",
    };

    const filteredData = filteredCustomers.value.map((item) => ({
      [headerMapping.tenKhachHang]: item.tenKhachHang || "N/A",
      [headerMapping.email]: item.email || "N/A",
      [headerMapping.soDienThoai]: item.soDienThoai || "N/A",
      [headerMapping.ngaySinh]: item.ngaySinh
        ? formatDate(item.ngaySinh)
        : "N/A",
      [headerMapping.gioiTinh]: item.gioiTinh ? "Nam" : "Nữ",
      [headerMapping.deleted]: item.deleted ? "Ngừng hoạt động" : "Hoạt động",
    }));

    // Tạo worksheet & workbook
    const worksheet = XLSX.utils.json_to_sheet(filteredData);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "KhachHang");

    // Xuất file
    const excelBuffer = XLSX.write(workbook, { bookType: "xlsx", type: "array" });
    const data = new Blob([excelBuffer], { type: "application/octet-stream" });
    saveAs(data, "DanhSachKhachHang.xlsx");

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
  flex-wrap: nowrap;
}

.filter-controls select {
  min-width: 150px;
  padding: 12px 16px;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  font-size: 14px;
  background-color: #f8fff9;
  transition: all 0.3s ease;
  cursor: pointer;
  box-sizing: border-box;
}

.filter-controls select:focus {
  outline: none;
  border-color: #5ebe81;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
  transform: translateY(-1px);
}

.filter-controls select:hover {
  border-color: #d1d5db;
  background-color: #f9fafb;
}


/* Table Styles */
.table th {
  background-color: #9baea2;
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
  background-color: #b27171;
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
.action-buttons-section .btn {
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary {
  background-color: #1f3e72;
  color: white;
}

.btn-secondary {
  background-color: #0f0d24;
  color: white;
}

.custom-button {
  background-color: #1e2d50;  /* Màu xanh đậm */
  color: #ffffff;
  border: 1px solid #1e2d50;
  border-radius: 6px;
  padding: 8px 16px;
  font-weight: 500;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease-in-out;
}

.custom-button:hover {
  background-color: #24365e;  /* Hover sáng hơn nhẹ */
  border-color: #24365e;
  transform: translateY(-1px);
}

.custom-button i {
  font-size: 16px;
}
::v-deep label.form-label {
  color: #000000;
  font-weight: 600;
}
::v-deep input.form-control,
::v-deep select.form-control,
::v-deep textarea.form-control {
  color: #000000;
  font-weight: 500;
}
::v-deep input::placeholder,
::v-deep textarea::placeholder {
  color: #555555;
  opacity: 1;
}
/* Ví dụ style nút "Xem chi tiết" */
.action-button-info {
  background-color: #5f768e; /* màu xanh đậm */
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.action-button-info:hover {
  background-color: #2e6096; /* xanh đậm hơn khi hover */
  cursor: pointer;
}
.modal-content {
  width: 80vw;
  max-width: 900px;
  max-height: 85vh;
  overflow-y: auto;
  padding: 24px;
  font-size: 16px;
  box-sizing: border-box;
}
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  padding: 16px;
  background-color: #f9f9f9;
}

.info-box {
  background-color: #fff;
  padding: 12px 16px;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.08);
  font-size: 14px;
}

.info-box strong {
  color: #444;
  font-weight: 600;
  margin-right: 4px;
}

.badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 600;
}

.badge-success {
  background-color: #4caf50;
  color: white;
}

.badge-danger {
  background-color: #f44336;
  color: white;
}


.modal-content h3,
.modal-content h4 {
  font-weight: 700; /* Tiêu đề đậm hơn */
  color: #111;
}

.modal-content label {
  font-weight: 700; /* Các nhãn (label) đậm */
  color: #333;
  display: inline-block;
  width: 120px; /* Giữ khoảng cách đều */
}

.modal-content span {
  font-weight: 600; /* Nội dung đậm vừa phải */
  color: #444;
}

.address-label {
  font-weight: 700;
  color: #333;
}

.badge-success {
  background-color: #4caf50;
  color: white;
  font-weight: 700;
  padding: 4px 20px;
  border-radius: 12px;
  text-transform: uppercase;
}

.badge-danger {
  background-color: #f44336;
  color: white;
  font-weight: 700;
  padding: 4px 4px;
  border-radius: 15px;
  text-transform: uppercase;
}


</style>
