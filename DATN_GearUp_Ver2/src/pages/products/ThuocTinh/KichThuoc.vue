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
    <router-link :to="`/products/kich-thuoc`" class="product-link">
      <span class="product-text">Kích thước</span>
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
          placeholder="Tìm theo tên kích thước..."
          @input="handleSearch"
        />
      </div>
      <div class="action-group">
        <button @click="showAddForm = true" class="btn-export">
          <i class="fas fa-plus"></i> Thêm Kích Thước Mới
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
        <h3><i class="fas fa-plus"></i> Thêm Kích Thước Mới</h3>
        <button @click="closeAddForm" class="modal-close">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="fetchCreate">
          <div class="detail-row">
            <div class="detail-label">Tên kích thước:</div>
            <div class="detail-value">
              <input
                v-model="newKichThuoc.tenKichThuoc"
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
                    v-model="newKichThuoc.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="Trạng thái"
                    :value="true"
                    v-model="newKichThuoc.deleted"
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
        <th>Tên kích thước</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in paginatedKichThuocs" :key="value.id">
        <td>{{ startIndex + i + 1 }}</td>
        <td>{{ value.tenKichThuoc }}</td>
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
      Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của {{ totalItems }} kích
      thước
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
        <h3>Chỉnh Sửa Kích Thước</h3>
        <button class="modal-close" @click="closeDetailModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <!-- Edit Mode -->
        <div>
          <div class="detail-row">
            <div class="detail-label">Tên kích thước:</div>
            <div class="detail-value">
              <input
                v-model="selectedKichThuoc.tenKichThuoc"
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
                    v-model="selectedKichThuoc.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="detailTrạng thái"
                    :value="true"
                    v-model="selectedKichThuoc.deleted"
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
  fetchAllKichThuoc,
  fetchCreateKichThuoc,
  fetchUpdateKichThuoc,
  fetchUpdateStatusKichThuoc,
} from "../../../services/ThuocTinh/KichThuocService";

const KichThuocs = ref([]);
const showAddForm = ref(false);
const showEditForm = ref(false);
const showDetailModal = ref(false);
const searchQuery = ref("");
const statusFilter = ref("");
const newKichThuoc = ref({
  tenKichThuoc: "",
  deleted: false,
});
const selectedKichThuoc = ref({});
const uploading = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);
const successMessage = ref(null);
const editSuccessMessage = ref(null);

// Biến cho modal xóa
const showDeleteModal = ref(false);
const deleteItemId = ref(null);
const deleteItemName = ref("");

// Pagination variables
const currentPage = ref(1);
const pageSize = ref(10);

// Pagination computed properties
const filteredKichThuocs = computed(() => {
  let filtered = [...KichThuocs.value];

  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter((item) =>
      item.tenKichThuoc?.toLowerCase().includes(query)
    );
  }

  if (statusFilter.value !== "") {
    filtered = filtered.filter(
      (item) => item.deleted === (statusFilter.value === "true")
    );
  }

  return filtered;
});

const totalItems = computed(() => filteredKichThuocs.value.length);
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));
const startIndex = computed(() => (currentPage.value - 1) * pageSize.value);
const endIndex = computed(() => startIndex.value + pageSize.value);
const paginatedKichThuocs = computed(() => {
  return filteredKichThuocs.value.slice(startIndex.value, endIndex.value);
});

const fetchAll = async () => {
  try {
    const response = await fetchAllKichThuoc();
    KichThuocs.value = response.data;
  } catch (error) {
    console.error("Error fetching:", error);
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
  if (!newKichThuoc.value.tenKichThuoc) {
    errorMessage.value = "Vui lòng nhập tên kích thước";
    return;
  }

  uploading.value = true;
  errorMessage.value = null;

  try {
    await fetchCreateKichThuoc(newKichThuoc.value);

    // Reset form
    newKichThuoc.value = {
      tenKichThuoc: "",
      deleted: false,
    };

    await fetchAll();
    successMessage.value = "Kích thước đã được thêm thành công!";
    clearSuccessMessage();
    closeAddForm();
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value =
      "Lỗi khi thêm: " + (error.message || "Không thể tạo kích thước");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedKichThuoc.value = { ...value };
  showDetailModal.value = true;
};

const openEditForm = (value) => {
  selectedKichThuoc.value = { ...value };
  showEditForm.value = true;
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;

  try {
    await fetchUpdateKichThuoc(
      selectedKichThuoc.value.id,
      selectedKichThuoc.value
    );

    await fetchAll();
    closeDetailModal();
    editSuccessMessage.value = "Kích thước đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value =
      "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật kích thước");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  // Hiển thị modal xác nhận xóa
  showDeleteModal.value = true;
  deleteItemId.value = id;

  // Lấy tên kích thước để hiển thị trong thông báo
  const item = KichThuocs.value.find((item) => item.id === id);
  if (item) {
    deleteItemName.value = item.tenKichThuoc || "Kích thước";
  }
};

const confirmDelete = async () => {
  if (!deleteItemId.value) return;

  try {
    uploading.value = true;
    await fetchUpdateStatusKichThuoc(deleteItemId.value);
    await fetchAll();
    successMessage.value = "Kích thước đã được xóa thành công!";
    clearSuccessMessage();
    closeDeleteModal();
  } catch (error) {
    console.error("There has been a problem with your fetch operation:", error);
    errorMessage.value =
      "Lỗi khi xóa: " + (error.message || "Không thể xóa kích thước");
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
  selectedKichThuoc.value = {};
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedKichThuoc.value = {};
};

const editFromDetail = () => {
  showDetailModal.value = false;
  openEditForm(selectedKichThuoc.value);
};

// Đóng modal thêm mới và reset form
const closeAddForm = () => {
  showAddForm.value = false;
  newKichThuoc.value = {
    tenKichThuoc: "",
    deleted: false,
  };
  errorMessage.value = null;
  successMessage.value = null;
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

onMounted(fetchAll);
</script>

<style scoped>
/* CSS đã được chuyển vào productsUnified.css */
@import "../../../styles/cssSanPham/productsUnified.css";
@import "../../../styles/cssSanPham/quanLySanPham.css";
</style>
