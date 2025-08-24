<template>
  <!-- Font Awesome for icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

  <div class="add-form">
    <h3>Thêm Hình Ảnh Sản Phẩm Mới</h3>
    <form @submit.prevent="fetchCreate">
      <div>
        <label>Chọn ảnh từ máy:</label>
        <input type="file" ref="fileInput" @change="handleFileChange" accept="image/*" required />
        <img v-if="previewUrl" :src="previewUrl" alt="Preview ảnh"
          style="width: 100px; height: auto; margin-top: 10px" />
      </div>
      <div>
        <label>Loại ảnh:</label>
        <input v-model="newAnhSanPham.loaiAnh" type="text" required />
      </div>
      <div>
        <label>Mô tả:</label>
        <input v-model="newAnhSanPham.moTa" type="text" />
      </div>
      <div>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" :value="false" v-model="newAnhSanPham.deleted" />Hoạt động
        <input type="radio" name="Trạng thái" :value="true" v-model="newAnhSanPham.deleted" />Không hoạt động
      </div>
      <button type="submit" :disabled="uploading" class="btn btn-primary">
        <i class="fas fa-plus"></i> {{ uploading ? 'Đang thêm...' : 'Thêm Mới' }}
      </button>
      <p v-if="errorMessage" style="color: red">{{ errorMessage }}</p>
      <p v-if="successMessage" style="color: green">{{ successMessage }}</p>
    </form>
  </div>
  <!-- Form chỉnh sửa (mới thêm) -->
  <div class="edit-form" v-if="showEditForm">
    <h3>Chỉnh Sửa Hình Ảnh Sản Phẩm</h3>
    <form @submit.prevent="fetchUpdate">
      <div>
        <label>Ảnh hiện tại:</label>
        <img v-if="selectedAnhSanPham.duongDanAnh" :src="getImageUrl(selectedAnhSanPham.duongDanAnh)" alt="Ảnh hiện tại"
          style="width: 100px; height: auto" @error="handleImageError" />
      </div>
      <div>
        <label>Chọn ảnh mới (nếu muốn thay đổi):</label>
        <input type="file" ref="editFileInput" @change="handleEditFileChange" accept="image/*" />
        <img v-if="editPreviewUrl" :src="editPreviewUrl" alt="Preview mới"
          style="width: 100px; height: auto; margin-top: 10px" />
      </div>
      <div>
        <label>Loại ảnh:</label>
        <input v-model="selectedAnhSanPham.loaiAnh" type="text" required />
      </div>
      <div>
        <label>Mô tả:</label>
        <input v-model="selectedAnhSanPham.moTa" type="text" />
      </div>
      <div>
        <label for="">Trạng thái</label>
        <input type="radio" name="editTrạng thái" :value="false" v-model="selectedAnhSanPham.deleted" />Hoạt động
        <input type="radio" name="editTrạng thái" :value="true" v-model="selectedAnhSanPham.deleted" />Không hoạt động
      </div>
      <button type="submit" :disabled="uploading" class="btn btn-success">
        <i class="fas fa-save"></i> {{ uploading ? 'Đang cập nhật...' : 'Cập Nhật' }}
      </button>
      <button type="button" @click="closeEditForm" class="btn btn-secondary">
        <i class="fas fa-times"></i> Đóng
      </button>
      <p v-if="editErrorMessage" style="color: red">{{ editErrorMessage }}</p>
      <p v-if="editSuccessMessage" style="color: green">{{ editSuccessMessage }}</p>
    </form>
  </div>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Ảnh</th>
        <th>Loại ảnh</th>
        <th>Mô tả</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in AnhSanPhams" :key="i">
        <td>{{ i + 1 }}</td>
        <td>
          <img :src="getImageUrl(value.duongDanAnh)" alt="Ảnh sản phẩm" style="width: 100px; height: auto"
            @error="handleImageError" />
        </td>
        <td>{{ value.loaiAnh }}</td>
        <td>{{ value.moTa }}</td>
        <td>{{ value.deleted ? "Không hoạt động" : "Hoạt động" }}</td>
        <td>
          <button v-on:click="fetchDetail(value)" class="btn btn-detail btn-icon btn-sm" title="Xem chi tiết">
            <i class="fas fa-eye"></i>
          </button>
          <button v-on:click="fetchDelete(value.id)" class="btn btn-delete btn-icon btn-sm" :disabled="uploading"
            title="Xóa">
            <i class="fas fa-trash"></i>
          </button>
        </td>
      </tr>
    </tbody>
  </table>

  <!-- Popup Detail Modal -->
  <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>Chi Tiết Hình Ảnh Sản Phẩm</h3>
        <button class="modal-close" @click="closeDetailModal">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <div class="detail-row">
          <div class="detail-label">Ảnh sản phẩm:</div>
          <div class="detail-value">
            <img :src="getImageUrl(selectedAnhSanPham.duongDanAnh)" alt="Ảnh sản phẩm" class="detail-image"
              @error="handleImageError" />
          </div>
        </div>
        <div class="detail-row">
          <div class="detail-label">ID:</div>
          <div class="detail-value">{{ selectedAnhSanPham.id }}</div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Loại ảnh:</div>
          <div class="detail-value">{{ selectedAnhSanPham.loaiAnh }}</div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Mô tả:</div>
          <div class="detail-value">{{ selectedAnhSanPham.moTa || 'Không có mô tả' }}</div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Đường dẫn ảnh:</div>
          <div class="detail-value">{{ selectedAnhSanPham.duongDanAnh }}</div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Trạng thái:</div>
          <div class="detail-value">
            <span :class="selectedAnhSanPham.deleted ? 'status-inactive' : 'status-active'">
              {{ selectedAnhSanPham.deleted ? "Không hoạt động" : "Hoạt động" }}
            </span>
          </div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Ngày tạo:</div>
          <div class="detail-value">{{ formatDate(selectedAnhSanPham.createdAt) || 'Không có thông tin' }}</div>
        </div>
        <div class="detail-row">
          <div class="detail-label">Ngày cập nhật:</div>
          <div class="detail-value">{{ formatDate(selectedAnhSanPham.updatedAt) || 'Không có thông tin' }}</div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-success" @click="editFromDetail">
          <i class="fas fa-edit"></i> Chỉnh sửa
        </button>
        <button class="btn btn-secondary" @click="closeDetailModal">
          <i class="fas fa-times"></i> Đóng
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import {
  fetchAllAnhSanPham,
  fetchCreateAnhSanPham,
  fetchUpdateAnhSanPham,
  fetchUpdateStatusAnhSanPham,
} from "../../services/AnhSanPhamService";

const AnhSanPhams = ref([]);
const newAnhSanPham = ref({
  loaiAnh: "",
  moTa: "",
  deleted: false
});
const selectedAnhSanPham = ref({});
const file = ref(null);
const previewUrl = ref(null);
const editFile = ref(null);
const editPreviewUrl = ref(null);
const showEditForm = ref(false);
const showDetailModal = ref(false);
const uploading = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);
const successMessage = ref(null);
const editSuccessMessage = ref(null);

// Refs cho file input
const fileInput = ref(null);
const editFileInput = ref(null);

const handleFileChange = (event) => {
  const selectedFile = event.target.files[0];
  if (selectedFile) {
    if (!selectedFile.type.startsWith("image/")) {
      errorMessage.value = "Vui lòng chọn file ảnh (jpg, png, etc.)";
      return;
    }
    if (selectedFile.size > 5 * 1024 * 1024) {
      // 5MB limit
      errorMessage.value = "File quá lớn (tối đa 5MB)";
      return;
    }
    file.value = selectedFile;
    previewUrl.value = URL.createObjectURL(selectedFile);
    errorMessage.value = null;
  }
};

const handleEditFileChange = (event) => {
  const selectedFile = event.target.files[0];
  if (selectedFile) {
    if (!selectedFile.type.startsWith("image/")) {
      editErrorMessage.value = "Vui lòng chọn file ảnh (jpg, png, etc.)";
      return;
    }
    if (selectedFile.size > 5 * 1024 * 1024) {
      editErrorMessage.value = "File quá lớn (tối đa 5MB)";
      return;
    }
    editFile.value = selectedFile;
    editPreviewUrl.value = URL.createObjectURL(selectedFile);
    editErrorMessage.value = null;
  }
};

const fetchAll = async () => {
  try {
    const response = await fetchAllAnhSanPham();
    AnhSanPhams.value = response.data;
  } catch (error) {
    console.error("Error fetching:", error);
  }
};

const fetchCreate = async () => {
  if (!file.value) {
    errorMessage.value = "Vui lòng chọn file ảnh";
    return;
  }

  if (!newAnhSanPham.value.loaiAnh) {
    errorMessage.value = "Vui lòng nhập loại ảnh";
    return;
  }

  uploading.value = true;
  errorMessage.value = null;

  try {
    // Tạo FormData để gửi file
    const formData = new FormData();
    formData.append('file', file.value);
    formData.append('loaiAnh', newAnhSanPham.value.loaiAnh);
    formData.append('moTa', newAnhSanPham.value.moTa || '');
    formData.append('deleted', newAnhSanPham.value.deleted || false);

    await fetchCreateAnhSanPham(formData);

    // Reset form
    newAnhSanPham.value = {
      loaiAnh: "",
      moTa: "",
      deleted: false
    };
    file.value = null;
    previewUrl.value = null;

    // Reset file input
    if (fileInput.value) {
      fileInput.value.value = '';
    }

    await fetchAll();
    successMessage.value = "Ảnh sản phẩm đã được thêm thành công!";
    clearSuccessMessage();
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value = "Lỗi khi thêm: " + (error.message || "Không thể tạo ảnh sản phẩm");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedAnhSanPham.value = { ...value };
  showDetailModal.value = true;
};

const openEditForm = (value) => {
  selectedAnhSanPham.value = { ...value };
  editPreviewUrl.value = value.duongDanAnh;
  showEditForm.value = true;
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;

  try {
    // Tạo FormData để gửi dữ liệu
    const formData = new FormData();

    // Nếu có file mới được chọn
    if (editFile.value) {
      formData.append('file', editFile.value);
    }

    formData.append('loaiAnh', selectedAnhSanPham.value.loaiAnh);
    formData.append('moTa', selectedAnhSanPham.value.moTa || '');
    formData.append('deleted', selectedAnhSanPham.value.deleted || false);

    await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);

    await fetchAll();
    closeEditForm();
    editSuccessMessage.value = "Ảnh sản phẩm đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value = "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật ảnh sản phẩm");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  if (!confirm('Bạn có chắc chắn muốn xóa ảnh sản phẩm này?')) {
    return;
  }

  try {
    await fetchUpdateStatusAnhSanPham(id);
    await fetchAll();
    successMessage.value = "Ảnh sản phẩm đã được xóa thành công!";
    clearSuccessMessage();
  } catch (error) {
    console.error("There has been a problem with your fetch operation:", error);
    errorMessage.value = "Lỗi khi xóa: " + (error.message || "Không thể xóa ảnh sản phẩm");
    setTimeout(() => {
      errorMessage.value = null;
    }, 3000);
  }
};

const closeEditForm = () => {
  showEditForm.value = false;
  editFile.value = null;
  editPreviewUrl.value = null;
  editErrorMessage.value = null;
  editSuccessMessage.value = null;
  selectedAnhSanPham.value = {};

  // Reset edit file input
  if (editFileInput.value) {
    editFileInput.value.value = '';
  }
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedAnhSanPham.value = {};
};

const editFromDetail = () => {
  showDetailModal.value = false; // Đóng popup detail
  openEditForm(selectedAnhSanPham.value); // Mở form edit
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

// Method để tạo URL đầy đủ cho ảnh
const getImageUrl = (imagePath) => {
  if (!imagePath) return '';

  // Nếu đã là URL đầy đủ thì trả về nguyên
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath;
  }

  // Nếu là đường dẫn tương đối, thêm base URL của backend
  if (imagePath.startsWith('uploads/')) {
    return `http://localhost:8080/${imagePath}`;
  }

  // Nếu bắt đầu bằng / thì thêm base URL
  if (imagePath.startsWith('/')) {
    return `http://localhost:8080${imagePath}`;
  }

  return imagePath;
};

// Method xử lý lỗi khi ảnh không load được
const handleImageError = (event) => {
  console.error('Không thể load ảnh:', event.target.src);
  // Có thể set một ảnh mặc định
  event.target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik0zMCAzMEg3MFY3MEgzMFYzMFoiIGZpbGw9IiNEN0Q5RDEiLz4KPHN2ZyB4PSIzNSIgeT0iMzUiIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPgo8cGF0aCBkPSJNMTIgMkM2LjQ4IDIgMiA2LjQ4IDIgMTJzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwUzE3LjUyIDIgMTIgMnptMCAxOGMtNC40MSAwLTgtMy41OS04LTggczMuNTktOCA4LTggOCAzLjU5IDggOC0zLjU5IDgtOCA4eiIgZmlsbD0iI0E5QUFCRCIvPgo8cGF0aCBkPSJNMTIgNkM5Ljc5IDYgOCA3Ljc5IDggMTBzMS43OSA0IDQgNCA0LTEuNzkgNC00LTEuNzktNC00LTR6bTAgNmMtMS4xIDAtMi0uOS0yLTIgMC0xLjEuOS0yIDItMiAxLjEgMCAyIC45IDIgMiAwIDEuMS0uOSAyLTIgMnoiIGZpbGw9IiNBOUFBQkQiLz4KPC9zdmc+Cjwvc3ZnPgo=';
};

onMounted(fetchAll);
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
  border: 2px solid #dcfce7;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
  background-color: #f0fdf4;
}

.add-form input[type="text"]:focus,
.edit-form input[type="text"]:focus {
  outline: none;
  border-color: #4ade80;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

.add-form input[type="file"],
.edit-form input[type="file"] {
  padding: 10px 0;
  border: 2px dashed #4ade80;
  border-radius: 8px;
  background-color: #f0fdf4;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-form input[type="file"]:hover,
.edit-form input[type="file"]:hover {
  background-color: #dcfce7;
  border-color: #22c55e;
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

.btn-warning {
  background: linear-gradient(135deg, #ffc107, #e0a800);
  color: #212529;
}

.btn-warning:hover {
  background: linear-gradient(135deg, #e0a800, #c69500);
}

.btn-danger {
  background: linear-gradient(135deg, #dc3545, #c82333);
  color: white;
}

.btn-danger:hover {
  background: linear-gradient(135deg, #c82333, #a71e2a);
}

.btn-info {
  background: linear-gradient(135deg, #17a2b8, #138496);
  color: white;
}

.btn-info:hover {
  background: linear-gradient(135deg, #138496, #117a8b);
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
  border: 1px solid #dcfce7;
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

.table img {
  border-radius: 8px;
  border: 2px solid #dcfce7;
  transition: all 0.3s ease;
}

.table img:hover {
  border-color: #4ade80;
  transform: scale(1.05);
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

/* Form enhancements */
.add-form input[type="radio"]+label,
.edit-form input[type="radio"]+label {
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

/* Image preview enhancements */
.add-form img,
.edit-form img {
  border: 2px solid #dcfce7;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.add-form img:hover,
.edit-form img:hover {
  border-color: #4ade80;
  transform: scale(1.05);
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
  border-bottom: 2px solid #dcfce7;
  background: linear-gradient(135deg, #4ade80, #22c55e);
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

.detail-image {
  width: 200px;
  height: auto;
  border-radius: 8px;
  border: 2px solid #dcfce7;
  transition: all 0.3s ease;
}

.detail-image:hover {
  border-color: #4ade80;
  transform: scale(1.05);
}

.status-active {
  color: #22c55e;
  font-weight: 600;
  padding: 4px 12px;
  background-color: #dcfce7;
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
  border-top: 2px solid #dcfce7;
  display: flex;
  justify-content: space-between;
  background-color: #f0fdf4;
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

  .detail-image {
    width: 100%;
    max-width: 200px;
  }
}
</style>
