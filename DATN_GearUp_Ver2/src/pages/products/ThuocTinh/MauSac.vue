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
    <router-link :to="`/products/mau-sac`" class="product-link">
      <span class="product-text">Màu sắc</span>
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
          placeholder="Tìm theo tên màu sắc..."
          @input="handleSearch"
        />
      </div>
      <div class="action-group">
        <button @click="showAddForm = true" class="btn-export">
          <i class="fas fa-plus"></i> Thêm Màu Sắc Mới
        </button>
      </div>
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

  <!-- Modal thêm mới -->
  <div v-if="showAddForm" class="modal-overlay" @click="closeAddForm">
    <div class="modal-content add-modal" @click.stop>
      <div class="modal-header add-header">
        <h3><i class="fas fa-plus"></i> Thêm Màu Sắc Mới</h3>
        <button @click="closeAddForm" class="modal-close">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="fetchCreate">
          <div class="detail-row">
            <div class="detail-label">Tên màu sắc:</div>
            <div class="detail-value">
              <input
                v-model="newMauSac.tenMauSac"
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
                    v-model="newMauSac.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="Trạng thái"
                    :value="true"
                    v-model="newMauSac.deleted"
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
        <th>Tên màu sắc</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in paginatedMauSacs" :key="value.id">
        <td>{{ startIndex + i + 1 }}</td>
        <td>{{ value.tenMauSac }}</td>
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
      Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của {{ totalItems }} màu
      sắc
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
        <h3>Chỉnh Sửa Màu Sắc</h3>
        <button class="modal-close" @click="closeDetailModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <!-- Edit Mode -->
        <div>
          <div class="detail-row">
            <div class="detail-label">Tên màu sắc:</div>
            <div class="detail-value">
              <input
                v-model="selectedMauSac.tenMauSac"
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
                    v-model="selectedMauSac.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="detailTrạng thái"
                    :value="true"
                    v-model="selectedMauSac.deleted"
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
  fetchAllMauSac,
  fetchCreateMauSac,
  fetchUpdateMauSac,
  fetchUpdateStatusMauSac,
} from "../../../services/ThuocTinh/MauSacService";

const MauSacs = ref([]);
const showAddForm = ref(false);
const showEditForm = ref(false);
const showDetailModal = ref(false);
const searchQuery = ref("");
const statusFilter = ref("");
const newMauSac = ref({
  tenMauSac: "",
  deleted: false,
});
const selectedMauSac = ref({});
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

// Pagination variables
const currentPage = ref(1);
const pageSize = ref(10);

// Pagination computed properties
const filteredMauSacs = computed(() => {
  let filtered = [...MauSacs.value];

  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter((item) =>
      item.tenMauSac?.toLowerCase().includes(query)
    );
  }

  if (statusFilter.value !== "") {
    filtered = filtered.filter(
      (item) => item.deleted === (statusFilter.value === "true")
    );
  }

  return filtered;
});

const totalItems = computed(() => filteredMauSacs.value.length);
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));
const startIndex = computed(() => (currentPage.value - 1) * pageSize.value);
const endIndex = computed(() => startIndex.value + pageSize.value);
const paginatedMauSacs = computed(() => {
  return filteredMauSacs.value.slice(startIndex.value, endIndex.value);
});

const fetchMauSacs = async () => {
  try {
    const res = await fetchAllMauSac();
    MauSacs.value = res.data;
  } catch (error) {
    console.error("Error fetching colors:", error);
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
  if (!newMauSac.value.tenMauSac) {
    errorMessage.value = "Vui lòng nhập tên màu sắc";
    return;
  }

  uploading.value = true;
  errorMessage.value = null;

  try {
    await fetchCreateMauSac(newMauSac.value);

    // Reset form
    newMauSac.value = {
      tenMauSac: "",
      deleted: false,
    };

    await fetchMauSacs();
    successMessage.value = "Màu sắc đã được thêm thành công!";
    clearSuccessMessage();
    closeAddForm();
  } catch (error) {
    console.error("Error creating color:", error);
    errorMessage.value =
      "Lỗi khi thêm: " + (error.message || "Không thể tạo màu sắc");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedMauSac.value = { ...value };
  showDetailModal.value = true;
};

const openEditForm = (mauSac) => {
  selectedMauSac.value = { ...mauSac };
  showEditForm.value = true;
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;

  try {
    await fetchUpdateMauSac(selectedMauSac.value.id, selectedMauSac.value);
    await fetchMauSacs();
    closeDetailModal();
    editSuccessMessage.value = "Màu sắc đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating color:", error);
    editErrorMessage.value =
      "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật màu sắc");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  // Hiển thị modal xác nhận xóa
  showDeleteModal.value = true;
  deleteItemId.value = id;

  // Lấy tên màu sắc để hiển thị trong thông báo
  const item = MauSacs.value.find((item) => item.id === id);
  if (item) {
    deleteItemName.value = item.tenMauSac || "Màu sắc";
  }
};

const confirmDelete = async () => {
  if (!deleteItemId.value) return;

  try {
    uploading.value = true;
    await fetchUpdateStatusMauSac(deleteItemId.value);
    await fetchMauSacs();
    successMessage.value = "Màu sắc đã được xóa thành công!";
    clearSuccessMessage();
    closeDeleteModal();
  } catch (error) {
    console.error("Error deleting color:", error);
    errorMessage.value =
      "Lỗi khi xóa: " + (error.message || "Không thể xóa màu sắc");
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
  selectedMauSac.value = {};
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedMauSac.value = {};
};

const editFromDetail = () => {
  showDetailModal.value = false;
  openEditForm(selectedMauSac.value);
};

// Đóng modal thêm mới và reset form
const closeAddForm = () => {
  showAddForm.value = false;
  newMauSac.value = {
    tenMauSac: "",
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

onMounted(fetchMauSacs);
</script>

<style scoped>
/* CSS đã được chuyển vào productsUnified.css */
@import "../../../styles/cssSanPham/productsUnified.css";
@import "../../../styles/cssSanPham/quanLySanPham.css";
</style>
