<template>
  <!-- Font Awesome for icons -->
  <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
  />

  <div class="breadcrumb-nav">
    <RouterLink :to="`/products`" class="product-link">
      <span class="product-text">Sản phẩm</span>
    </RouterLink>
    <span class="breadcrumb-separator"> / </span>
    <router-link :to="`/products/nha-san-xuat`" class="product-link">
      <span class="product-text">Nhà sản xuất</span>
    </router-link>
  </div>

  <!-- Search và Filter -->
  <div class="search-filter-section">
    <div class="search-box">
      <div class="search-input-group">
        <label><i class="fas fa-search"></i> Tìm kiếm:</label>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Tìm theo tên nhà sản xuất..."
          @input="handleSearch"
        />
      </div>
      <div class="action-group">
        <button @click="showAddForm = true" class="btn-export">
          <i class="fas fa-plus"></i> Thêm Nhà Sản Xuất Mới
        </button>
      </div>
      <div class="filter-group">
        <label><i class="fas fa-filter"></i> Lọc theo trạng thái:</label>
        <div class="radio-filter">
          <label class="radio-label">
            <input
              type="radio"
              name="statusFilter"
              value=""
              v-model="statusFilter"
              @change="handleFilter"
            />
            <span>Tất cả</span>
          </label>
          <label class="radio-label">
            <input
              type="radio"
              name="statusFilter"
              value="false"
              v-model="statusFilter"
              @change="handleFilter"
            />
            <span>Hoạt động</span>
          </label>
          <label class="radio-label">
            <input
              type="radio"
              name="statusFilter"
              value="true"
              v-model="statusFilter"
              @change="handleFilter"
            />
            <span>Không hoạt động</span>
          </label>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal thêm mới -->
  <div v-if="showAddForm" class="modal-overlay" @click="closeAddForm">
    <div class="modal-content add-modal" @click.stop>
      <div class="modal-header add-header">
        <h3><i class="fas fa-plus"></i> Thêm Nhà Sản Xuất Mới</h3>
        <button @click="closeAddForm" class="modal-close">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="fetchCreate">
          <div class="detail-row">
            <div class="detail-label">Tên nhà sản xuất:</div>
            <div class="detail-value">
              <input
                v-model="newNSX.tenNhaSanXuat"
                type="text"
                required
                class="detail-input"
              />
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Trạng thái:</div>
            <div class="detail-value">
              <div class="radio-group">
                <label class="radio-label">
                  <input
                    type="radio"
                    name="Trạng thái"
                    :value="false"
                    v-model="newNSX.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="Trạng thái"
                    :value="true"
                    v-model="newNSX.deleted"
                  />
                  <span>Không hoạt động</span>
                </label>
              </div>
            </div>
          </div>

          <!-- Error và Success Message -->
          <div v-if="errorMessage" class="detail-error">
            <p>{{ errorMessage }}</p>
          </div>
          <div v-if="successMessage" class="detail-success">
            <p>{{ successMessage }}</p>
          </div>
        </form>
      </div>
      <div class="modal-footer add-footer">
        <button type="button" @click="closeAddForm" class="btn btn-secondary">
          <i class="fas fa-times"></i> Hủy bỏ
        </button>
        <button
          @click="fetchCreate"
          :disabled="uploading"
          class="btn btn-primary"
        >
          <i class="fas fa-plus"></i>
          {{ uploading ? "Đang thêm..." : "Thêm Mới" }}
        </button>
      </div>
    </div>
  </div>

  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Tên nhà sản xuất</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in paginatedNhaSanXuats" :key="i">
        <td>{{ startIndex + i + 1 }}</td>
        <td>{{ value.tenNhaSanXuat }}</td>
        <td>{{ value.deleted ? "Không hoạt động" : "Hoạt động" }}</td>
        <td>
          <button
            v-on:click="fetchDetail(value)"
            class="btn btn-detail btn-icon btn-sm"
            title="Xem chi tiết"
          >
            <i class="fas fa-eye"></i>
          </button>
          <button
            v-on:click="fetchDelete(value.id)"
            class="btn btn-delete btn-icon btn-sm"
            :disabled="uploading"
            title="Xóa"
          >
            <i class="fas fa-trash"></i>
          </button>
        </td>
      </tr>
    </tbody>
  </table>

  <!-- Pagination -->
  <div v-if="totalPages > 1" class="pagination-wrapper">
    <div class="pagination-info">
      Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của {{ totalItems }} nhà
      sản xuất
    </div>
    <div class="pagination">
      <button
        @click="goToPreviousPage"
        :disabled="currentPage === 1"
        class="btn btn-outline btn-sm"
      >
        ❮ Trước
      </button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <button
        @click="goToNextPage"
        :disabled="currentPage === totalPages"
        class="btn btn-outline btn-sm"
      >
        Sau ❯
      </button>
    </div>
  </div>

  <!-- Popup Detail Modal -->
  <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>Chỉnh Sửa Nhà Sản Xuất</h3>
        <button class="modal-close" @click="closeDetailModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <!-- Edit Mode -->
        <div>
          <div class="detail-row">
            <div class="detail-label">Tên nhà sản xuất:</div>
            <div class="detail-value">
              <input
                v-model="selectedNSX.tenNhaSanXuat"
                type="text"
                required
                class="detail-input"
              />
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Trạng thái:</div>
            <div class="detail-value">
              <div class="radio-group">
                <label class="radio-label">
                  <input
                    type="radio"
                    name="detailTrạng thái"
                    :value="false"
                    v-model="selectedNSX.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="detailTrạng thái"
                    :value="true"
                    v-model="selectedNSX.deleted"
                  />
                  <span>Không hoạt động</span>
                </label>
              </div>
            </div>
          </div>

          <!-- Error Message -->
          <div v-if="editErrorMessage" class="detail-error">
            <p style="color: red">{{ editErrorMessage }}</p>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button
          class="btn btn-success"
          @click="fetchUpdate"
          :disabled="uploading"
        >
          <i class="fas fa-save"></i>
          {{ uploading ? "Đang cập nhật..." : "Lưu thay đổi" }}
        </button>
      </div>
    </div>
  </div>

  <!-- Modal Xác nhận Xóa -->
  <div v-if="showDeleteModal" class="modal-overlay" @click="closeDeleteModal">
    <div class="modal-content delete-modal" @click.stop>
      <div class="modal-header delete-header">
        <h3><i class="fas fa-exclamation-triangle"></i> Xác nhận xóa</h3>
        <button class="modal-close" @click="closeDeleteModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <div class="delete-content">
          <div class="delete-icon">
            <i class="fas fa-trash-alt"></i>
          </div>
          <h4>Bạn có chắc chắn muốn xóa?</h4>
          <p class="delete-message">
            Bạn sắp xóa <strong>"{{ deleteItemName }}"</strong>. Hành động này
            không thể hoàn tác.
          </p>
        </div>
      </div>
      <div class="modal-footer delete-footer">
        <button
          class="btn btn-secondary"
          @click="closeDeleteModal"
          :disabled="uploading"
        >
          <i class="fas fa-times"></i> Hủy bỏ
        </button>
        <button
          class="btn btn-delete"
          @click="confirmDelete"
          :disabled="uploading"
        >
          <i class="fas fa-trash"></i> {{ uploading ? "Đang xóa..." : "Xóa" }}
        </button>
      </div>
    </div>
  </div>

  <!-- Modal Xác nhận Xóa -->
  <div v-if="showDeleteModal" class="modal-overlay" @click="closeDeleteModal">
    <div class="modal-content delete-modal" @click.stop>
      <div class="modal-header delete-header">
        <h3><i class="fas fa-exclamation-triangle"></i> Xác nhận xóa</h3>
        <button class="modal-close" @click="closeDeleteModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <div class="delete-content">
          <div class="delete-icon">
            <i class="fas fa-trash-alt"></i>
          </div>
          <h4>Bạn có chắc chắn muốn xóa?</h4>
          <p class="delete-message">
            Bạn sắp xóa <strong>"{{ deleteItemName }}"</strong>. Hành động này
            không thể hoàn tác.
          </p>
        </div>
      </div>
      <div class="modal-footer delete-footer">
        <button
          class="btn btn-secondary"
          @click="closeDeleteModal"
          :disabled="uploading"
        >
          <i class="fas fa-times"></i> Hủy bỏ
        </button>
        <button
          class="btn btn-delete"
          @click="confirmDelete"
          :disabled="uploading"
        >
          <i class="fas fa-trash"></i> {{ uploading ? "Đang xóa..." : "Xóa" }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import {
  fetchAllNhaSanXuat,
  fetchCreateNhaSanXuat,
  fetchUpdateNhaSanXuat,
  fetchUpdateStatusNhaSanXuat,
} from "../../../services/ThuocTinh/NhaSanXuatService";

const showEditForm = ref(false);
const showAddForm = ref(false);
const newNSX = ref({
  tenNhaSanXuat: "",
  deleted: false,
});
const NhaSanXuats = ref([]);
const selectedNSX = ref({});
const showDetailModal = ref(false);
const uploading = ref(false);
const updating = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);
const successMessage = ref(null);
const editSuccessMessage = ref(null);

// Biến cho modal xóa
const showDeleteModal = ref(false);
const deleteItemId = ref(null);
const deleteItemName = ref("");

// Search và Filter
const searchQuery = ref("");
const statusFilter = ref("");

// Pagination variables
const currentPage = ref(1);
const pageSize = ref(10);

// Pagination computed properties
const filteredNhaSanXuats = computed(() => {
  let filtered = [...NhaSanXuats.value];

  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter((item) =>
      item.tenNhaSanXuat?.toLowerCase().includes(query)
    );
  }

  if (statusFilter.value !== "") {
    filtered = filtered.filter(
      (item) => item.deleted === (statusFilter.value === "true")
    );
  }

  return filtered;
});

const totalItems = computed(() => filteredNhaSanXuats.value.length);
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));
const startIndex = computed(() => (currentPage.value - 1) * pageSize.value);
const endIndex = computed(() => startIndex.value + pageSize.value);
const paginatedNhaSanXuats = computed(() => {
  return filteredNhaSanXuats.value.slice(startIndex.value, endIndex.value);
});

const fetchAll = async () => {
  try {
    const response = await fetchAllNhaSanXuat();
    NhaSanXuats.value = response.data;
    currentPage.value = 1; // Reset to first page when data changes
  } catch (error) {
    console.error("Error fetching manufacturers:", error);
  }
};

// Hàm xử lý tìm kiếm
const handleSearch = () => {
  currentPage.value = 1;
};

// Hàm xử lý lọc
const handleFilter = () => {
  currentPage.value = 1;
};

const fetchCreate = async () => {
  if (!newNSX.value.tenNhaSanXuat) {
    errorMessage.value = "Vui lòng nhập tên nhà sản xuất";
    return;
  }

  uploading.value = true;
  errorMessage.value = null;

  try {
    await fetchCreateNhaSanXuat(newNSX.value);

    // Reset form
    newNSX.value = {
      tenNhaSanXuat: "",
      deleted: false,
    };

    await fetchAll();
    successMessage.value = "Nhà sản xuất đã được thêm thành công!";
    clearSuccessMessage();
    closeAddForm();
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value =
      "Lỗi khi thêm: " + (error.message || "Không thể tạo nhà sản xuất");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedNSX.value = { ...value };
  showDetailModal.value = true;
};

const openEditForm = (value) => {
  selectedNSX.value = { ...value };
  showEditForm.value = true;
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;

  try {
    await fetchUpdateNhaSanXuat(selectedNSX.value.id, selectedNSX.value);

    await fetchAll();
    closeDetailModal();
    editSuccessMessage.value = "Nhà sản xuất đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value =
      "Lỗi khi cập nhật: " +
      (error.message || "Không thể cập nhật nhà sản xuất");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  // Hiển thị modal xác nhận xóa
  showDeleteModal.value = true;
  deleteItemId.value = id;

  // Lấy tên nhà sản xuất để hiển thị trong thông báo
  const item = NhaSanXuats.value.find((item) => item.id === id);
  if (item) {
    deleteItemName.value = item.tenNhaSanXuat || "Nhà sản xuất";
  }
};

const confirmDelete = async () => {
  if (!deleteItemId.value) return;

  try {
    uploading.value = true;
    await fetchUpdateStatusNhaSanXuat(deleteItemId.value);
    await fetchAll();
    successMessage.value = "Nhà sản xuất đã được xóa thành công!";
    clearSuccessMessage();
    closeDeleteModal();
  } catch (error) {
    console.error("Error deleting manufacturer:", error);
    errorMessage.value =
      "Lỗi khi xóa: " + (error.message || "Không thể xóa nhà sản xuất");
    setTimeout(() => {
      errorMessage.value = null;
    }, 3000);
  } finally {
    uploading.value = false;
  }
};

const closeDeleteModal = () => {
  showDeleteModal.value = false;
  deleteItemId.value = null;
  deleteItemName.value = "";
};

const closeEditForm = () => {
  showEditForm.value = false;
  editErrorMessage.value = null;
  editSuccessMessage.value = null;
  selectedNSX.value = {};
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedNSX.value = {};
};

const editFromDetail = () => {
  showDetailModal.value = false; // Đóng popup detail
  openEditForm(selectedNSX.value); // Mở form edit
};

// Đóng modal thêm mới và reset form
const closeAddForm = () => {
  showAddForm.value = false;
  newNSX.value = {
    tenNhaSanXuat: "",
    deleted: false,
  };
  errorMessage.value = null;
  successMessage.value = null;
};

const goToPreviousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const goToNextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

// Format date function
const formatDate = (dateString) => {
  if (!dateString) return null;
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString("vi-VN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch (error) {
    return dateString;
  }
};

// Clear success messages after 3 seconds
const clearSuccessMessage = () => {
  setTimeout(() => {
    successMessage.value = null;
  }, 3000);
};

const clearEditSuccessMessage = () => {
  setTimeout(() => {
    editSuccessMessage.value = null;
  }, 3000);
};

onMounted(fetchAll);
</script>

<style scoped>
/* CSS đã được chuyển vào productsUnified.css */
@import "../../../styles/cssSanPham/productsUnified.css";
@import "../../../styles/cssSanPham/quanLySanPham.css";
</style>
