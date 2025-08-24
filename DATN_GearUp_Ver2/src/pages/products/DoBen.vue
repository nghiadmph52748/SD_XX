<template>
  <!-- Font Awesome for icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

  <div class="management-header">
    <div class="title-section">
      <h2 class="main-title">Thuộc tính sản phẩm</h2>
      <h3 class="sub-title">Quản lý độ bền</h3>
    </div>
    <button @click="showAddForm = true" class="btn btn-primary">
      <i class="fas fa-plus"></i> Thêm Độ Bền Mới
    </button>
  </div>

  <!-- Search và Filter -->
  <div class="search-filter-section">
    <div class="search-box">
      <div class="search-input-group">
        <label><i class="fas fa-search"></i> Tìm kiếm:</label>
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="Tìm theo tên độ bền hoặc mô tả..."
          @input="handleSearch"
        />
      </div>
      <div class="filter-group">
        <label><i class="fas fa-filter"></i> Lọc theo trạng thái:</label>
        <select v-model="statusFilter" @change="handleFilter">
          <option value="">Tất cả</option>
          <option value="false">Hoạt động</option>
          <option value="true">Không hoạt động</option>
        </select>
      </div>
    </div>
  </div>

  <!-- Modal thêm mới -->
  <div v-if="showAddForm" class="modal-overlay" @click="closeAddForm">
    <div class="modal-content add-modal" @click.stop>
      <div class="modal-header add-header">
        <h3><i class="fas fa-plus"></i> Thêm Độ Bền Mới</h3>
        <button @click="closeAddForm" class="modal-close">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="fetchCreate">
          <div class="detail-row">
            <div class="detail-label">Tên độ bền:</div>
            <div class="detail-value">
              <input v-model="newDoBen.tenDoBen" type="text" required class="detail-input" />
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Mô tả:</div>
            <div class="detail-value">
              <input v-model="newDoBen.moTa" type="text" class="detail-input" />
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
                    v-model="newDoBen.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="Trạng thái"
                    :value="true"
                    v-model="newDoBen.deleted"
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
        <button @click="fetchCreate" :disabled="uploading" class="btn btn-primary">
          <i class="fas fa-plus"></i> {{ uploading ? 'Đang thêm...' : 'Thêm Mới' }}
        </button>
      </div>
    </div>
  </div>



  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Tên độ bền</th>
        <th>Mô tả</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in paginatedDoBens" :key="value.id">
        <td>{{ startIndex + i + 1 }}</td>
        <td>{{ value.tenDoBen }}</td>
        <td>{{ value.moTa }}</td>
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
      Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của {{ totalItems }} độ bền
    </div>
    <div class="pagination">
      <button @click="goToPreviousPage" :disabled="currentPage === 1" class="btn btn-outline btn-sm">
        ❮ Trước
      </button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <button @click="goToNextPage" :disabled="currentPage === totalPages" class="btn btn-outline btn-sm">
        Sau ❯
      </button>
    </div>
  </div>

  <!-- Popup Detail Modal -->
  <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>Chỉnh Sửa Độ Bền</h3>
        <button class="modal-close" @click="closeDetailModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <!-- Edit Mode -->
        <div>
          <div class="detail-row">
            <div class="detail-label">Tên độ bền:</div>
            <div class="detail-value">
              <input 
                v-model="selectedDoBen.tenDoBen" 
                type="text" 
                required 
                class="detail-input"
              />
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Mô tả:</div>
            <div class="detail-value">
              <input 
                v-model="selectedDoBen.moTa" 
                type="text" 
                class="detail-input"
              />
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Trạng thái:</div>
            <div class="detail-value">
              <div class="radio-group">
                <label class="radio-label">
                  <input type="radio" name="detailTrạng thái" :value="false" v-model="selectedDoBen.deleted" />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input type="radio" name="detailTrạng thái" :value="true" v-model="selectedDoBen.deleted" />
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
        <button class="btn btn-success" @click="saveChanges" :disabled="uploading">
          <i class="fas fa-save"></i> {{ uploading ? 'Đang cập nhật...' : 'Lưu thay đổi' }}
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
            Bạn sắp xóa <strong>"{{ deleteItemName }}"</strong>. Hành động này không thể hoàn tác.
          </p>
        </div>
      </div>
      <div class="modal-footer delete-footer">
        <button class="btn btn-secondary" @click="closeDeleteModal" :disabled="uploading">
          <i class="fas fa-times"></i> Hủy bỏ
        </button>
        <button class="btn btn-delete" @click="confirmDelete" :disabled="uploading">
          <i class="fas fa-trash"></i> {{ uploading ? 'Đang xóa...' : 'Xóa' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import {
  fetchAllDoBen,
  fetchCreateDoBen,
  fetchUpdateDoBen,
  fetchUpdateStatusDoBen,
} from "../../services/ThuocTinh/DoBenService";
const DoBens = ref([]);
const newDoBen = ref({
  tenDoBen: "",
  moTa: "",
  deleted: false,
});
const selectedDoBen = ref({});
const showEditForm = ref(false);
const showDetailModal = ref(false);
const uploading = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);
const successMessage = ref(null);
const editSuccessMessage = ref(null);

// Biến cho modal xóa
const showDeleteModal = ref(false);
const deleteItemId = ref(null);
const deleteItemName = ref('');

// Biến cho form thêm mới
const showAddForm = ref(false);

// Biến cho tìm kiếm và lọc
const searchQuery = ref('');
const statusFilter = ref('');

// Pagination variables
const currentPage = ref(1);
const pageSize = ref(10);

// Computed properties cho tìm kiếm, lọc và phân trang
const filteredDoBens = computed(() => {
  let filtered = [...DoBens.value];
  
  // Tìm kiếm theo tên độ bền hoặc mô tả
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(item => 
      item.tenDoBen?.toLowerCase().includes(query) ||
      item.moTa?.toLowerCase().includes(query)
    );
  }
  
  // Lọc theo trạng thái
  if (statusFilter.value !== '') {
    filtered = filtered.filter(item => 
      item.deleted === (statusFilter.value === 'true')
    );
  }
  
  return filtered;
});

const totalItems = computed(() => filteredDoBens.value.length);
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));
const startIndex = computed(() => (currentPage.value - 1) * pageSize.value);
const endIndex = computed(() => startIndex.value + pageSize.value);
const paginatedDoBens = computed(() => {
  return filteredDoBens.value.slice(startIndex.value, endIndex.value);
});

const fetchAll = async () => {
  try {
    const response = await fetchAllDoBen();
    DoBens.value = response.data;
  } catch (error) {
    console.error("Error fetching:", error);
  }
};

const fetchCreate = async () => {
  if (!newDoBen.value.tenDoBen) {
    errorMessage.value = "Vui lòng nhập tên độ bền";
    return;
  }

  uploading.value = true;
  errorMessage.value = null;

  try {
    await fetchCreateDoBen(newDoBen.value);

    // Reset form
    newDoBen.value = {
      tenDoBen: "",
      moTa: "",
      deleted: false,
    };

    await fetchAll();
    successMessage.value = "Độ bền đã được thêm thành công!";
    clearSuccessMessage();
    closeAddForm(); // Đóng form sau khi thêm thành công
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value =
      "Lỗi khi thêm: " + (error.message || "Không thể tạo độ bền");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedDoBen.value = { ...value };
  showDetailModal.value = true;
};

const openEditForm = (value) => {
  selectedDoBen.value = { ...value };
  showEditForm.value = true;
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;

  try {
    await fetchUpdateDoBen(selectedDoBen.value.id, selectedDoBen.value);

    await fetchAll();
    closeEditForm();
    editSuccessMessage.value = "Độ bền đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value =
      "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật độ bền");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  // Hiển thị modal xác nhận xóa
  showDeleteModal.value = true;
  deleteItemId.value = id;
  
  // Lấy tên độ bền để hiển thị trong thông báo
  const item = DoBens.value.find(item => item.id === id);
  if (item) {
    deleteItemName.value = item.tenDoBen || 'Độ bền';
  }
};

const confirmDelete = async () => {
  if (!deleteItemId.value) return;
  
  try {
    uploading.value = true;
    await fetchUpdateStatusDoBen(deleteItemId.value);
    await fetchAll();
    successMessage.value = "Độ bền đã được xóa thành công!";
    clearSuccessMessage();
    closeDeleteModal();
  } catch (error) {
    console.error("There has been a problem with your fetch operation:", error);
    errorMessage.value =
      "Lỗi khi xóa: " + (error.message || "Không thể xóa độ bền");
    setTimeout(() => {
      errorMessage.value = null;
    }, 3000);
  } finally {
    uploading.value = false;
  }
};

const closeAddForm = () => {
  showAddForm.value = false;
  // Reset form
  newDoBen.value = {
    tenDoBen: "",
    moTa: "",
    deleted: false,
  };
  errorMessage.value = null;
  successMessage.value = null;
};

const closeEditForm = () => {
  showEditForm.value = false;
  editErrorMessage.value = null;
  editSuccessMessage.value = null;
  selectedDoBen.value = {};
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedDoBen.value = {};
};

// Hàm xử lý tìm kiếm
const handleSearch = () => {
  currentPage.value = 1; // Reset về trang đầu tiên
};

// Hàm xử lý lọc
const handleFilter = () => {
  currentPage.value = 1; // Reset về trang đầu tiên
};

const closeDeleteModal = () => {
  showDeleteModal.value = false;
  deleteItemId.value = null;
  deleteItemName.value = '';
};

const saveChanges = async () => {
  uploading.value = true;
  editErrorMessage.value = null;
  
  try {
    await fetchUpdateDoBen(selectedDoBen.value.id, selectedDoBen.value);
    
    await fetchAll();
    closeDetailModal();
    editSuccessMessage.value = "Độ bền đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value = "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật độ bền");
  } finally {
    uploading.value = false;
  }
};

const editFromDetail = () => {
  showDetailModal.value = false; // Đóng popup detail
  openEditForm(selectedDoBen.value); // Mở form edit
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
/* Import Google Fonts */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Poppins:wght@300;400;500;600;700&display=swap');

/* Global font settings */
* {
  font-family: 'Inter', 'Poppins', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* Header quản lý */
.management-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding: 20px;
  background: linear-gradient(135deg, #4ade80, #22c55e);
  border-radius: 12px;
  color: white;
}

.title-section {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.main-title {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  color: white;
  font-family: 'Arial', 'Helvetica', sans-serif;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.sub-title {
  margin: 0;
  font-size: 20px;
  font-weight: 400;
  color: white;
  opacity: 0.9;
  font-family: 'Inter', sans-serif;
  letter-spacing: 0.2px;
}

/* Search và Filter Section */
.search-filter-section {
  background: #ffffff;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 25px;
  border: 2px solid #d4edda;
  box-shadow: 0 2px 8px rgba(74, 222, 128, 0.1);
  width: 100%;
  box-sizing: border-box;
}

.search-box {
  display: flex;
  gap: 20px;
  align-items: flex-end;
  flex-wrap: nowrap;
  width: 100%;
}

.search-input-group,
.filter-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.search-input-group {
  flex: 4;
  min-width: 0;
}

.filter-group {
  flex: 1;
  min-width: 180px;
}

.search-input-group label,
.filter-group label {
  font-weight: 600;
  color: #4ade80;
  font-size: 14px;
  font-family: 'Inter', sans-serif;
  letter-spacing: 0.3px;
}

.search-input-group input,
.filter-group select {
  padding: 12px 16px;
  border: 2px solid #d4edda;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: #f8fff9;
  width: 100%;
  box-sizing: border-box;
}

.search-input-group input:focus,
.filter-group select:focus {
  outline: none;
  border-color: #4ade80;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

.add-form,
.edit-form {
  background: #ffffff;
  padding: 25px;
  border-radius: 12px;
  margin-bottom: 25px;
  border: 2px solid #4ade80;
  box-shadow: 0 4px 12px rgba(74, 222, 128, 0.1);
}

.add-form h3,
.edit-form h3 {
  color: #4ade80;
  margin-bottom: 25px;
  font-size: 24px;
  font-weight: 600;
  text-align: center;
}

.add-form div,
.edit-form div {
  margin-bottom: 20px;
}

.add-form label,
.edit-form label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #4ade80;
  font-size: 16px;
}

.add-form input[type="text"],
.edit-form input[type="text"] {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #d4edda;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
  background-color: #f8fff9;
}

.add-form input[type="text"]:focus,
.edit-form input[type="text"]:focus {
  outline: none;
  border-color: #4ade80;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

.add-form input[type="radio"],
.edit-form input[type="radio"] {
  margin-right: 8px;
  margin-left: 20px;
  accent-color: #4ade80;
  transform: scale(1.2);
}

.add-form input[type="radio"]:first-of-type,
.edit-form input[type="radio"]:first-of-type {
  margin-left: 0;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
  margin-right: 15px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.btn-primary {
  background: linear-gradient(135deg, #4ade80, #22c55e);
  color: white;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #22c55e, #16a34a);
}

.btn-success {
  background: linear-gradient(135deg, #4ade80, #22c55e);
  color: white;
}

.btn-success:hover {
  background: linear-gradient(135deg, #22c55e, #16a34a);
}

.btn-secondary {
  background: linear-gradient(135deg, #6c757d, #5a6268);
  color: white;
}

.btn-secondary:hover {
  background: linear-gradient(135deg, #5a6268, #495057);
}

.btn-warning {
  background: linear-gradient(135deg, #ffc107, #e0a800);
  color: #212529;
}

.btn-warning:hover {
  background: linear-gradient(135deg, #e0a800, #c69500);
}

.btn-sm {
  padding: 8px 12px;
  font-size: 14px;
}

.btn-icon {
  width: 40px;
  height: 40px;
  padding: 0;
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  font-size: 18px;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 25px;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(74, 222, 128, 0.1);
}

.table th,
.table td {
  border: 1px solid #d4edda;
  padding: 16px;
  text-align: left;
}

.table th {
  background: linear-gradient(135deg, #4ade80, #22c55e);
  color: white;
  font-weight: 600;
  font-size: 16px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.table tr:nth-child(even) {
  background-color: #f0fdf4;
}

.table tr:hover {
  background-color: #dcfce7;
  transition: background-color 0.3s ease;
}

p[style*="color: red"] {
  margin-top: 15px;
  font-size: 16px;
  font-weight: 500;
  padding: 12px;
  background-color: #fff5f5;
  border: 1px solid #fed7d7;
  border-radius: 8px;
}

p[style*="color: green"] {
  margin-top: 15px;
  font-size: 16px;
  font-weight: 500;
  padding: 12px;
  background-color: #f0fdf4;
  border: 1px solid #bbf7d0;
  border-radius: 8px;
}

/* Icon button styles */
.btn-detail {
  background: linear-gradient(135deg, #17a2b8, #138496);
  color: white;
}

.btn-detail:hover {
  background: linear-gradient(135deg, #138496, #117a8b);
}

.btn-delete {
  background: linear-gradient(135deg, #dc3545, #c82333);
  color: white;
}

.btn-delete:hover {
  background: linear-gradient(135deg, #c82333, #a71e2a);
}

/* Enhanced button styles */
.btn i {
  margin-right: 8px;
}

.btn-icon i {
  margin-right: 0;
}

/* Loading state for buttons */
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* Detail Edit Mode Styles */
.detail-input {
  width: 100%;
  padding: 8px 12px;
  border: 2px solid #d4edda;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: #f8fff9;
}

.detail-input:focus {
  outline: none;
  border-color: #4ade80;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

/* CSS cho radio button trong detail modal */
.detail-value .radio-group {
  display: flex;
  align-items: center;
  gap: 20px;
}

.detail-value .radio-label {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  font-size: 14px;
  color: #333;
}

/* CSS cho radio button trong form */
input[type="radio"] {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  width: 18px;
  height: 18px;
  border: 2px solid #ddd;
  border-radius: 50%;
  outline: none;
  cursor: pointer;
  position: relative;
  margin-right: 8px;
  vertical-align: middle;
}

input[type="radio"]:checked {
  border-color: #4ade80;
  background-color: #4ade80;
}

input[type="radio"]:checked::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 6px;
  height: 6px;
  background-color: white;
  border-radius: 50%;
}

input[type="radio"]:hover {
  border-color: #4ade80;
}

/* Style cho label của radio button */
.radio-label {
  display: inline-flex;
  align-items: center;
  margin-right: 20px;
  cursor: pointer;
  font-size: 14px;
  color: #333;
}

.radio-group {
  display: flex;
  align-items: center;
  margin: 10px 0;
}

.detail-error {
  margin-top: 15px;
  padding: 12px;
  background-color: #fff5f5;
  border: 1px solid #fed7d7;
  border-radius: 8px;
}

.detail-error p {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
}

.detail-success {
  margin-top: 15px;
  padding: 12px;
  background-color: #f0fdf4;
  border: 1px solid #bbf7d0;
  border-radius: 8px;
}

.detail-success p {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
  color: #22c55e;
}

/* Form enhancements */
.add-form input[type="radio"] + label,
.edit-form input[type="radio"] + label {
  display: inline;
  margin-left: 5px;
  font-weight: 500;
  color: #495057;
}

/* Table enhancements */
.table tbody tr:hover {
  background-color: #dcfce7;
  transition: background-color 0.3s ease;
  cursor: pointer;
}

/* Success and error message enhancements */
p[style*="color: red"],
p[style*="color: green"] {
  margin-top: 15px;
  font-size: 16px;
  font-weight: 500;
  padding: 15px;
  border-radius: 8px;
  border-left: 4px solid;
}

p[style*="color: red"] {
  background-color: #fff5f5;
  border-color: #dc3545;
  color: #dc3545;
}

p[style*="color: green"] {
  background-color: #f0fdf4;
  border-color: #22c55e;
  color: #22c55e;
}

/* Responsive design */
@media (max-width: 768px) {
  .management-header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }

  .management-header h2 {
    font-size: 24px;
  }

  .search-filter-section {
    padding: 15px;
    margin-bottom: 20px;
  }

  .search-box {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
    flex-wrap: wrap;
  }

  .search-input-group,
  .filter-group {
    min-width: auto;
    flex: none;
  }

  .add-form,
  .edit-form {
    padding: 20px;
    margin-bottom: 20px;
  }

  .btn {
    padding: 10px 20px;
    font-size: 14px;
    margin-bottom: 10px;
  }

  .table th,
  .table td {
    padding: 12px 8px;
    font-size: 14px;
  }
}

/* Pagination enhancements */
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 1.5rem;
  gap: 20px;
}

.pagination-info {
  display: none;
}

.pagination {
  display: flex;
  align-items: center;
  gap: 20px;
}

.page-info {
  font-weight: 600;
  color: #495057;
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 8px;
  padding: 8px 16px;
  min-width: 80px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.btn-outline {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  color: #495057;
  border-radius: 8px;
  min-width: 80px;
  height: 40px;
  padding: 8px 16px;
  font-weight: 600;
  font-size: 14px;
  transition: none;
  display: flex;
  align-items: center;
  justify-content: center;
  outline: none;
  margin: 0;
}

.btn-outline:hover:not(:disabled) {
  background-color: #f8f9fa;
  color: #495057;
  transform: none;
  box-shadow: none;
  border: 1px solid #dee2e6;
}

.btn-outline:focus {
  outline: none;
  border: 1px solid #dee2e6;
  box-shadow: none;
}

.btn-outline:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
  border-color: #9ca3af;
  color: #9ca3af;
}

/* Modal Popup Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  animation: slideIn 0.3s ease;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  border-bottom: 2px solid #d4edda;
  background: linear-gradient(135deg, #32cd32, #28a745);
  color: white;
  border-radius: 12px 12px 0 0;
}

.modal-header h3 {
  margin: 0;
  color: white;
  font-size: 20px;
}

.modal-close {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  width: 35px;
  height: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.modal-close:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: scale(1.1);
}

.modal-body {
  padding: 25px;
}

.detail-row {
  display: flex;
  margin-bottom: 20px;
  align-items: flex-start;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 15px;
}

.detail-row:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.detail-label {
  width: 150px;
  font-weight: 600;
  color: #32cd32;
  font-size: 16px;
  flex-shrink: 0;
}

.detail-value {
  flex: 1;
  color: #495057;
  font-size: 16px;
  word-break: break-word;
}

.status-active {
  color: #28a745;
  font-weight: 600;
  padding: 4px 12px;
  background-color: #d4edda;
  border-radius: 20px;
  font-size: 14px;
}

.status-inactive {
  color: #dc3545;
  font-weight: 600;
  padding: 4px 12px;
  background-color: #f8d7da;
  border-radius: 20px;
  font-size: 14px;
}

.modal-footer {
  padding: 20px 25px;
  border-top: 2px solid #d4edda;
  display: flex;
  justify-content: space-between;
  background-color: #f8fff9;
  border-radius: 0 0 12px 12px;
}

/* Modal Animations */
@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(-50px);
    opacity: 0;
  }

  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* CSS cho Modal Thêm Mới */
.add-modal {
  max-width: 600px;
}

.add-header {
  background: linear-gradient(135deg, #4ade80, #22c55e);
}

.add-footer {
  justify-content: space-between;
  padding: 20px 25px;
}

.add-footer .btn {
  min-width: 120px;
}

/* CSS cho Modal Xóa */
.delete-modal {
  max-width: 500px;
}

.delete-header {
  background: linear-gradient(135deg, #dc3545, #c82333);
}

.delete-content {
  text-align: center;
  padding: 20px 0;
}

.delete-icon {
  font-size: 60px;
  color: #dc3545;
  margin-bottom: 20px;
}

.delete-content h4 {
  color: #dc3545;
  font-size: 20px;
  margin-bottom: 15px;
  font-weight: 600;
}

.delete-message {
  color: #6c757d;
  font-size: 16px;
  line-height: 1.5;
  margin-bottom: 0;
}

.delete-footer {
  justify-content: space-between;
  padding: 20px 25px;
}

.delete-footer .btn {
  min-width: 120px;
}

.delete-footer .btn-delete {
  background: linear-gradient(135deg, #dc3545, #c82333);
  color: white;
}

.delete-footer .btn-delete:hover {
  background: linear-gradient(135deg, #c82333, #a71e2a);
}

.delete-footer .btn-delete:disabled {
  background: linear-gradient(135deg, #6c757d, #5a6268);
  opacity: 0.6;
}

/* Responsive Modal */
@media (max-width: 768px) {
  .modal-content {
    width: 95%;
    margin: 20px;
  }

  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 15px 20px;
  }

  .detail-row {
    flex-direction: column;
    margin-bottom: 15px;
  }

  .detail-label {
    width: 100%;
    margin-bottom: 8px;
  }
}
</style>
