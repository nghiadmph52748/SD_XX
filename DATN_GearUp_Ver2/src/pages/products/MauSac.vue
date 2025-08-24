<template>
  <!-- Font Awesome for icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

  <div class="add-form">
    <h3>Thêm Màu Sắc Mới</h3>
    <form @submit.prevent="fetchCreate">
      <div class="form-group">
        <label>Tên màu:</label>
        <input v-model="newMauSac.tenMauSac" type="text" required placeholder="Nhập tên màu..."
          class="color-name-input" />
      </div>

      <div class="form-group">
        <label>Trạng thái:</label>
        <div class="radio-group">
          <label class="radio-label">
            <input type="radio" name="status" :value="false" v-model="newMauSac.deleted" />
            <span>Hoạt động</span>
          </label>
          <label class="radio-label">
            <input type="radio" name="status" :value="true" v-model="newMauSac.deleted" />
            <span>Không hoạt động</span>
          </label>
        </div>
      </div>

      <button type="submit" :disabled="!newMauSac.tenMauSac || uploading" class="btn btn-primary">
        <i class="fas fa-plus"></i> {{ uploading ? 'Đang thêm...' : 'Thêm Mới' }}
      </button>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
    </form>
  </div>

  <!-- Form chỉnh sửa -->
  <div class="edit-form" v-if="showEditForm">
    <h3>Chỉnh Sửa Màu Sắc</h3>
    <form @submit.prevent="fetchUpdate">
      <div class="form-group">
        <label>Tên màu:</label>
        <input v-model="selectedMauSac.tenMauSac" type="text" required />
      </div>

      <div class="form-group">
        <label>Trạng thái:</label>
        <div class="radio-group">
          <label class="radio-label">
            <input type="radio" name="editStatus" :value="false" v-model="selectedMauSac.deleted" />
            <span>Hoạt động</span>
          </label>
          <label class="radio-label">
            <input type="radio" name="editStatus" :value="true" v-model="selectedMauSac.deleted" />
            <span>Không hoạt động</span>
          </label>
        </div>
      </div>

      <button type="submit" :disabled="uploading" class="btn btn-success">
        <i class="fas fa-save"></i> {{ uploading ? 'Đang cập nhật...' : 'Cập Nhật' }}
      </button>

      <button type="button" @click="closeEditForm" class="btn btn-secondary">
        <i class="fas fa-times"></i> Đóng
      </button>

      <p v-if="editErrorMessage" class="error-message">{{ editErrorMessage }}</p>
      <p v-if="editSuccessMessage" class="success-message">{{ editSuccessMessage }}</p>
    </form>
  </div>

  <!-- Bảng hiển thị màu sắc -->
  <div class="table-container">
    <h3>Danh Sách Màu Sắc</h3>
    <table class="table">
      <thead>
        <tr>
          <th>STT</th>
          <th>Tên màu</th>
          <th>Trạng thái</th>
          <th>Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(value, i) in MauSacs" :key="i">
          <td>{{ i + 1 }}</td>
          <td>{{ value.tenMauSac }}</td>
          <td>
            <span :class="value.deleted ? 'status-inactive' : 'status-active'">
              {{ value.deleted ? "Không hoạt động" : "Hoạt động" }}
            </span>
          </td>
          <td>
            <button @click="openDetailModal(value)" class="btn btn-detail btn-sm" title="Xem chi tiết">
              <i class="fas fa-eye"></i>
            </button>
            <button @click="fetchDelete(value.id)" class="btn btn-delete btn-sm" title="Xóa">
              <i class="fas fa-trash"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- Popup Detail Modal -->
  <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>Chi Tiết Màu Sắc</h3>
        <button class="modal-close" @click="closeDetailModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <div class="detail-row">
          <div class="detail-label">ID:</div>
          <div class="detail-value">{{ selectedMauSac.id }}</div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Tên màu:</div>
          <div class="detail-value">
            <input v-model="selectedMauSac.tenMauSac" type="text" class="detail-input" />
          </div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Trạng thái:</div>
          <div class="detail-value">
            <div class="radio-group">
              <label class="radio-label">
                <input type="radio" name="detailStatus" :value="false" v-model="selectedMauSac.deleted" />
                <span>Hoạt động</span>
              </label>
              <label class="radio-label">
                <input type="radio" name="detailStatus" :value="true" v-model="selectedMauSac.deleted" />
                <span>Không hoạt động</span>
              </label>
            </div>
          </div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Ngày tạo:</div>
          <div class="detail-value">{{ formatDate(selectedMauSac.createdAt) || 'Không có thông tin' }}</div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Ngày cập nhật:</div>
          <div class="detail-value">{{ formatDate(selectedMauSac.updatedAt) || 'Không có thông tin' }}</div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-success" @click="updateFromDetail" :disabled="updating">
          <i class="fas fa-save"></i> {{ updating ? 'Đang cập nhật...' : 'Cập Nhật' }}
        </button>
        <button class="btn btn-secondary" @click="closeDetailModal">
          <i class="fas fa-times"></i> Đóng
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { fetchAllMauSac, fetchCreateMauSac, fetchUpdateMauSac, fetchUpdateStatusMauSac } from '../../services/MauSacService';

const MauSacs = ref([]);
const newMauSac = ref({
  tenMauSac: '',
  deleted: false
});
const selectedMauSac = ref({});
const showEditForm = ref(false);
const showDetailModal = ref(false);
const uploading = ref(false);
const updating = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);
const successMessage = ref(null);
const editSuccessMessage = ref(null);

const fetchMauSacs = async () => {
  try {
    const res = await fetchAllMauSac();
    MauSacs.value = res.data;
  } catch (error) {
    console.error('Error fetching colors:', error);
  }
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
      tenMauSac: '',
      deleted: false
    };

    await fetchMauSacs();
    successMessage.value = "Màu sắc đã được thêm thành công!";
    clearSuccessMessage();
  } catch (error) {
    console.error('Error creating color:', error);
    errorMessage.value = "Lỗi khi thêm: " + (error.message || "Không thể tạo màu sắc");
  } finally {
    uploading.value = false;
  }
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
    closeEditForm();
    editSuccessMessage.value = "Màu sắc đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error('Error updating color:', error);
    editErrorMessage.value = "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật màu sắc");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  if (!confirm('Bạn có chắc chắn muốn xóa màu sắc này?')) {
    return;
  }

  try {
    await fetchUpdateStatusMauSac(id);
    await fetchMauSacs();
    successMessage.value = "Màu sắc đã được xóa thành công!";
    clearSuccessMessage();
  } catch (error) {
    console.error('Error deleting color:', error);
    errorMessage.value = "Lỗi khi xóa: " + (error.message || "Không thể xóa màu sắc");
    setTimeout(() => {
      errorMessage.value = null;
    }, 3000);
  }
};

const closeEditForm = () => {
  showEditForm.value = false;
  editErrorMessage.value = null;
  editSuccessMessage.value = null;
  selectedMauSac.value = {};
};

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

const openDetailModal = (mauSac) => {
  selectedMauSac.value = { ...mauSac };
  showDetailModal.value = true;
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedMauSac.value = {};
};

const updateFromDetail = async () => {
  if (!selectedMauSac.value.tenMauSac) {
    return;
  }

  updating.value = true;

  try {
    await fetchUpdateMauSac(selectedMauSac.value.id, selectedMauSac.value);
    await fetchMauSacs();
    successMessage.value = "Màu sắc đã được cập nhật thành công!";
    clearSuccessMessage();
    closeDetailModal();
  } catch (error) {
    console.error('Error updating color:', error);
    errorMessage.value = "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật màu sắc");
  } finally {
    updating.value = false;
  }
};

// Format date function
const formatDate = (dateString) => {
  if (!dateString) return null;
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch (error) {
    return dateString;
  }
};

onMounted(fetchMauSacs);
</script>

<style scoped>
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

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #4ade80;
  font-size: 16px;
}

.color-name-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #d4edda;
  border-radius: 8px;
  font-size: 16px;
  background-color: #f8fff9;
  color: #495057;
}

.color-name-input:focus {
  outline: none;
  border-color: #4ade80;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

.radio-group {
  display: flex;
  gap: 20px;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-weight: 500;
  color: #495057;
}

.radio-label input[type="radio"] {
  accent-color: #4ade80;
  transform: scale(1.2);
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

.btn-sm {
  padding: 8px 12px;
  font-size: 14px;
  margin-right: 8px;
}

.btn-edit {
  background: linear-gradient(135deg, #17a2b8, #138496);
  color: white;
}

.btn-edit:hover {
  background: linear-gradient(135deg, #138496, #117a8b);
}

.btn-delete {
  background: linear-gradient(135deg, #dc3545, #c82333);
  color: white;
}

.btn-delete:hover {
  background: linear-gradient(135deg, #c82333, #a71e2a);
}

.btn-detail {
  background: linear-gradient(135deg, #17a2b8, #138496);
  color: white;
}

.btn-detail:hover {
  background: linear-gradient(135deg, #138496, #117a8b);
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 20px rgba(74, 222, 128, 0.1);
}

.table-container h3 {
  color: #4ade80;
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 600;
}

.table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.table th,
.table td {
  border: 1px solid #d4edda;
  padding: 16px;
  text-align: left;
}

.table th {
  background: linear-gradient(135deg, #4ade80 0%, #22c55e);
  color: white;
  font-weight: 600;
  font-size: 16px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.table tr:nth-child(even) {
  background-color: #f8fff9;
}

.table tr:hover {
  background-color: #d4edda;
  transition: background-color 0.3s ease;
}

.status-active {
  color: #22c55e;
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

.error-message {
  color: #dc3545;
  margin-top: 15px;
  font-size: 16px;
  font-weight: 500;
  padding: 15px;
  background-color: #fff5f5;
  border: 1px solid #fed7d7;
  border-radius: 8px;
  border-left: 4px solid #dc3545;
}

.success-message {
  color: #28a745;
  margin-top: 15px;
  font-size: 16px;
  font-weight: 500;
  padding: 15px;
  background-color: #f0fff4;
  border: 1px solid #c6f6d5;
  border-radius: 8px;
  border-left: 4px solid #28a745;
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
  background: linear-gradient(135deg, #4ade80 0%, #22c55e);
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
  color: #4ade80;
  font-size: 16px;
  flex-shrink: 0;
}

.detail-value {
  flex: 1;
  color: #495057;
  font-size: 16px;
  word-break: break-word;
}

.detail-input {
  width: 100%;
  padding: 8px 12px;
  border: 2px solid #d4edda;
  border-radius: 6px;
  font-size: 16px;
  background-color: #f8fff9;
  color: #495057;
}

.detail-input:focus {
  outline: none;
  border-color: #4ade80;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
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

/* Responsive design */
@media (max-width: 768px) {

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

  .radio-group {
    flex-direction: column;
    gap: 10px;
  }
}
</style>