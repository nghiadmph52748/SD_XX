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
    <router-link to="/products/de-giay" class="product-link">
      <span class="product-text">Đế giày</span>
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
          placeholder="Tìm theo tên đế giày..."
          @input="handleSearch"
        />
      </div>
      <div class="action-group">
        <button @click="showAddForm = true" class="btn-export">
          <i class="fas fa-plus"></i> Thêm Đế Giày Mới
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
        <h3><i class="fas fa-plus"></i> Thêm Đế Giày Mới</h3>
        <button @click="closeAddForm" class="modal-close">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="fetchCreate">
          <div class="detail-row">
            <div class="detail-label">Tên đế giày:</div>
            <div class="detail-value">
              <input
                v-model="newDeGiay.tenDeGiay"
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
                    v-model="newDeGiay.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="Trạng thái"
                    :value="true"
                    v-model="newDeGiay.deleted"
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
        <th>Tên đế giày</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in paginatedDeGiays" :key="value.id">
        <td>{{ startIndex + i + 1 }}</td>
        <td>{{ value.tenDeGiay }}</td>
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
      Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của {{ totalItems }} đế
      giày
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
        <h3>Chỉnh Sửa Đế Giày</h3>
        <button class="modal-close" @click="closeDetailModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <!-- Edit Mode -->
        <div>
          <div class="detail-row">
            <div class="detail-label">Tên đế giày:</div>
            <div class="detail-value">
              <input
                v-model="selectedDeGiay.tenDeGiay"
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
                    v-model="selectedDeGiay.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="detailTrạng thái"
                    :value="true"
                    v-model="selectedDeGiay.deleted"
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
  fetchAllDeGiay,
  fetchCreateDeGiay,
  fetchUpdateDeGiay,
  fetchUpdateStatusDeGiay,
} from "../../../services/ThuocTinh/DeGiayService";

const DeGiays = ref([]);
const showAddForm = ref(false);
const showEditForm = ref(false);
const showDetailModal = ref(false);
const searchQuery = ref("");
const statusFilter = ref("");
const newDeGiay = ref({
  tenDeGiay: "",
  deleted: false,
});
const selectedDeGiay = ref({});
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
const filteredDeGiays = computed(() => {
  let filtered = [...DeGiays.value];

  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter((item) =>
      item.tenDeGiay?.toLowerCase().includes(query)
    );
  }

  if (statusFilter.value !== "") {
    filtered = filtered.filter(
      (item) => item.deleted === (statusFilter.value === "true")
    );
  }

  return filtered;
});

const totalItems = computed(() => filteredDeGiays.value.length);
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));
const startIndex = computed(() => (currentPage.value - 1) * pageSize.value);
const endIndex = computed(() => startIndex.value + pageSize.value);
const paginatedDeGiays = computed(() => {
  return filteredDeGiays.value.slice(startIndex.value, endIndex.value);
});

const fetchAll = async () => {
  try {
    const response = await fetchAllDeGiay();
    DeGiays.value = response.data;
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
  if (!newDeGiay.value.tenDeGiay) {
    errorMessage.value = "Vui lòng nhập tên đế giày";
    return;
  }

  uploading.value = true;
  errorMessage.value = null;

  try {
    await fetchCreateDeGiay(newDeGiay.value);

    // Reset form
    newDeGiay.value = {
      tenDeGiay: "",
      deleted: false,
    };

    await fetchAll();
    successMessage.value = "Đế giày đã được thêm thành công!";
    clearSuccessMessage();
    closeAddForm();
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value =
      "Lỗi khi thêm: " + (error.message || "Không thể tạo đế giày");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedDeGiay.value = { ...value };
  showDetailModal.value = true;
};

const openEditForm = (value) => {
  selectedDeGiay.value = { ...value };
  showEditForm.value = true;
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;

  try {
    await fetchUpdateDeGiay(selectedDeGiay.value.id, selectedDeGiay.value);

    await fetchAll();
    closeDetailModal();
    editSuccessMessage.value = "Đế giày đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value =
      "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật đế giày");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  // Hiển thị modal xác nhận xóa
  showDeleteModal.value = true;
  deleteItemId.value = id;

  // Lấy tên đế giày để hiển thị trong thông báo
  const item = DeGiays.value.find((item) => item.id === id);
  if (item) {
    deleteItemName.value = item.tenDeGiay || "Đế giày";
  }
};

const confirmDelete = async () => {
  if (!deleteItemId.value) return;

  try {
    uploading.value = true;
    await fetchUpdateStatusDeGiay(deleteItemId.value);
    await fetchAll();
    successMessage.value = "Đế giày đã được xóa thành công!";
    clearSuccessMessage();
    closeDeleteModal();
  } catch (error) {
    console.error("There has been a problem with your fetch operation:", error);
    errorMessage.value =
      "Lỗi khi xóa: " + (error.message || "Không thể xóa đế giày");
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
  selectedDeGiay.value = {};
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedDeGiay.value = {};
};

const editFromDetail = () => {
  showDetailModal.value = false;
  openEditForm(selectedDeGiay.value);
};

// Đóng modal thêm mới và reset form
const closeAddForm = () => {
  showAddForm.value = false;
  newDeGiay.value = {
    tenDeGiay: "",
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
