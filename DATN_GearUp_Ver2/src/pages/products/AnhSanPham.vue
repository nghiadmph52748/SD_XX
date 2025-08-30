<template>
  <!-- Font Awesome for icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

  <div class="management-header">
    <div class="title-section">
      <h2 class="main-title">Thuộc tính sản phẩm</h2>
      <h3 class="sub-title">Quản lý ảnh</h3>
    </div>
    <button @click="showAddForm = true" class="btn btn-primary">
      <i class="fas fa-plus"></i> Thêm Hình Ảnh Mới
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
          placeholder="Tìm theo loại ảnh hoặc mô tả..."
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
         <h3><i class="fas fa-plus"></i> Thêm Hình Ảnh Sản Phẩm Mới</h3>
         <button @click="closeAddForm" class="modal-close">
           <i class="fas fa-times"></i>
         </button>
       </div>
       <div class="modal-body">
         <form @submit.prevent="fetchCreate">
           <div class="detail-row">
             <div class="detail-label">Chọn ảnh từ máy:</div>
             <div class="detail-value">
               <input
                 type="file"
                 ref="fileInput"
                 @change="handleFileChange"
                 accept="image/*"
                 required
                 class="detail-file-input"
               />
               <img
                 v-if="previewUrl"
                 :src="previewUrl"
                 alt="Preview ảnh"
                 class="detail-preview-image"
               />
             </div>
           </div>
           <div class="detail-row">
             <div class="detail-label">Loại ảnh:</div>
             <div class="detail-value">
               <input v-model="newAnhSanPham.loaiAnh" type="text" required class="detail-input" />
             </div>
           </div>
           <div class="detail-row">
             <div class="detail-label">Mô tả:</div>
             <div class="detail-value">
               <input v-model="newAnhSanPham.moTa" type="text" class="detail-input" />
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
                     v-model="newAnhSanPham.deleted"
                   />
                   <span>Hoạt động</span>
                 </label>
                 <label class="radio-label">
                   <input
                     type="radio"
                     name="Trạng thái"
                     :value="true"
                     v-model="newAnhSanPham.deleted"
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
  <!-- Form chỉnh sửa (mới thêm) -->
  <div class="edit-form" v-if="showEditForm">
    <h3>Chỉnh Sửa Hình Ảnh Sản Phẩm</h3>
    <form @submit.prevent="fetchUpdate">
      <div>
        <label>Ảnh hiện tại:</label>
        <img
          v-if="selectedAnhSanPham.duongDanAnh"
          :src="getImageUrl(selectedAnhSanPham.duongDanAnh)"
          alt="Ảnh hiện tại"
          style="width: 100px; height: auto"
          @error="handleImageError"
        />
      </div>
      <div>
        <label>Chọn ảnh mới (nếu muốn thay đổi):</label>
        <input
          type="file"
          ref="editFileInput"
          @change="handleEditFileChange"
          accept="image/*"
        />
        <img
          v-if="editPreviewUrl"
          :src="editPreviewUrl"
          alt="Preview mới"
          style="width: 100px; height: auto; margin-top: 10px"
        />
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
        <div class="radio-group">
          <label class="radio-label">
            <input
              type="radio"
              name="editTrạng thái"
              :value="false"
              v-model="selectedAnhSanPham.deleted"
            />
            <span>Hoạt động</span>
          </label>
          <label class="radio-label">
            <input
              type="radio"
              name="editTrạng thái"
              :value="true"
              v-model="selectedAnhSanPham.deleted"
            />
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
      <tr v-for="(value, i) in paginatedAnhSanPhams" :key="value.id">
        <td>{{ startIndex + i + 1 }}</td>
        <td>
          <img
            :src="getImageUrl(value.duongDanAnh)"
            alt="Ảnh sản phẩm"
            style="width: 100px; height: auto"
            @error="handleImageError"
          />
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

  <!-- Pagination -->
  <div v-if="totalPages > 1" class="pagination-wrapper">
    <div class="pagination-info">
      Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của {{ totalItems }} ảnh sản phẩm
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
         <h3>Chỉnh Sửa Hình Ảnh Sản Phẩm</h3>
         <button class="modal-close" @click="closeDetailModal">
           <i class="fas fa-times"></i>
         </button>
       </div>
             <div class="modal-body">
         <!-- Edit Mode -->
         <div>
          <div class="detail-row">
            <div class="detail-label">Ảnh hiện tại:</div>
            <div class="detail-value">
              <img :src="getImageUrl(selectedAnhSanPham.duongDanAnh)" alt="Ảnh hiện tại" class="detail-image"
                @error="handleImageError" />
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Chọn ảnh mới:</div>
            <div class="detail-value">
              <input
                type="file"
                ref="editFileInput"
                @change="handleEditFileChange"
                accept="image/*"
                class="detail-file-input"
              />
              <img
                v-if="editPreviewUrl"
                :src="editPreviewUrl"
                alt="Preview mới"
                class="detail-preview-image"
              />
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Loại ảnh:</div>
            <div class="detail-value">
              <input 
                v-model="selectedAnhSanPham.loaiAnh" 
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
                v-model="selectedAnhSanPham.moTa" 
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
                  <input type="radio" name="detailTrạng thái" :value="false" v-model="selectedAnhSanPham.deleted" />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input type="radio" name="detailTrạng thái" :value="true" v-model="selectedAnhSanPham.deleted" />
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
  fetchAllAnhSanPham,
  fetchCreateAnhSanPham,
  fetchUpdateAnhSanPham,
  fetchUpdateStatusAnhSanPham,
} from "../../services/ThuocTinh/AnhSanPhamService";

const AnhSanPhams = ref([]);
const newAnhSanPham = ref({
  loaiAnh: "",
  moTa: "",
  deleted: false
});
const showDetailModal = ref(false);
const selectedAnhSanPham = ref({});
const file = ref(null);
const previewUrl = ref(null);
const editFile = ref(null);
const editPreviewUrl = ref(null);
const showEditForm = ref(false);
const uploading = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);
const successMessage = ref(null);
const editSuccessMessage = ref(null);
const isEditing = ref(false);
const originalData = ref({});

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

// Refs cho file input
const fileInput = ref(null);
const editFileInput = ref(null);

// Computed properties cho tìm kiếm, lọc và phân trang
const filteredAnhSanPhams = computed(() => {
  let filtered = [...AnhSanPhams.value];
  
  // Tìm kiếm theo loại ảnh hoặc mô tả
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(item => 
      item.loaiAnh?.toLowerCase().includes(query) ||
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

const totalItems = computed(() => filteredAnhSanPhams.value.length);
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));
const startIndex = computed(() => (currentPage.value - 1) * pageSize.value);
const endIndex = computed(() => startIndex.value + pageSize.value);
const paginatedAnhSanPhams = computed(() => {
  return filteredAnhSanPhams.value.slice(startIndex.value, endIndex.value);
});

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
     closeAddForm(); // Đóng form sau khi thêm thành công
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value = "Lỗi khi thêm: " + (error.message || "Không thể tạo ảnh sản phẩm");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedAnhSanPham.value = { ...value };
  originalData.value = { ...value };
  isEditing.value = true; // Mặc định hiển thị form edit luôn
  editFile.value = null;
  editPreviewUrl.value = null;
  showDetailModal.value = true;
};

const openEditForm = (value) => {
  selectedAnhSanPham.value = { ...value };
  showEditForm.value = true;
};

const startEditing = () => {
  isEditing.value = true;
  editFile.value = null;
  editPreviewUrl.value = null;
};

const cancelEditing = () => {
  selectedAnhSanPham.value = { ...originalData.value };
  editFile.value = null;
  editPreviewUrl.value = null;
  editErrorMessage.value = null;
};

const saveChanges = async () => {
  uploading.value = true;
  editErrorMessage.value = null;
  
  try {
    // Nếu có file mới được chọn thì gửi FormData
    if (editFile.value) {
      const formData = new FormData();
      formData.append('file', editFile.value);
      formData.append('loaiAnh', selectedAnhSanPham.value.loaiAnh);
      formData.append('moTa', selectedAnhSanPham.value.moTa || '');
      formData.append('deleted', selectedAnhSanPham.value.deleted || false);
      
      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    } else {
      // Nếu không có file mới, chỉ cập nhật thông tin
      // Tạo một file rỗng để backend không báo lỗi
      const emptyFile = new Blob([''], { type: 'image/png' });
      const formData = new FormData();
      formData.append('file', emptyFile, 'empty.png');
      formData.append('loaiAnh', selectedAnhSanPham.value.loaiAnh);
      formData.append('moTa', selectedAnhSanPham.value.moTa || '');
      formData.append('deleted', selectedAnhSanPham.value.deleted || false);
      
      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    }
    
         await fetchAll();
     closeDetailModal();
     editSuccessMessage.value = "Ảnh sản phẩm đã được cập nhật thành công!";
     clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value = "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật ảnh sản phẩm");
  } finally {
    uploading.value = false;
  }
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;
  
  try {
    // Nếu có file mới được chọn thì gửi FormData
    if (editFile.value) {
      const formData = new FormData();
      formData.append('file', editFile.value);
      formData.append('loaiAnh', selectedAnhSanPham.value.loaiAnh);
      formData.append('moTa', selectedAnhSanPham.value.moTa || '');
      formData.append('deleted', selectedAnhSanPham.value.deleted || false);
      
      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    } else {
      // Nếu không có file mới, chỉ cập nhật thông tin
      // Tạo một file rỗng để backend không báo lỗi
      const emptyFile = new Blob([''], { type: 'image/png' });
      const formData = new FormData();
      formData.append('file', emptyFile, 'empty.png');
      formData.append('loaiAnh', selectedAnhSanPham.value.loaiAnh);
      formData.append('moTa', selectedAnhSanPham.value.moTa || '');
      formData.append('deleted', selectedAnhSanPham.value.deleted || false);
      
      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    }
    
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
  // Hiển thị modal xác nhận xóa
  showDeleteModal.value = true;
  deleteItemId.value = id;
  
  // Lấy tên ảnh để hiển thị trong thông báo
  const item = AnhSanPhams.value.find(item => item.id === id);
  if (item) {
    deleteItemName.value = item.loaiAnh || 'Ảnh sản phẩm';
  }
};

const confirmDelete = async () => {
  if (!deleteItemId.value) return;
  
  try {
    uploading.value = true;
    await fetchUpdateStatusAnhSanPham(deleteItemId.value);
    await fetchAll();
    successMessage.value = "Ảnh sản phẩm đã được xóa thành công!";
    clearSuccessMessage();
    closeDeleteModal();
  } catch (error) {
    console.error("There has been a problem with your fetch operation:", error);
    errorMessage.value = "Lỗi khi xóa: " + (error.message || "Không thể xóa ảnh sản phẩm");
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
  deleteItemName.value = '';
};

const closeAddForm = () => {
  showAddForm.value = false;
  // Reset form
  newAnhSanPham.value = {
    loaiAnh: "",
    moTa: "",
    deleted: false
  };
  file.value = null;
  previewUrl.value = null;
  errorMessage.value = null;
  successMessage.value = null;
  
  // Reset file input
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};

// Hàm xử lý tìm kiếm
const handleSearch = () => {
  currentPage.value = 1; // Reset về trang đầu tiên
};

// Hàm xử lý lọc
const handleFilter = () => {
  currentPage.value = 1; // Reset về trang đầu tiên
};



const closeEditForm = () => {
  showEditForm.value = false;
  editFile.value = null;
  editPreviewUrl.value = null;
  editErrorMessage.value = null;
  selectedAnhSanPham.value = {};
  
  // Reset edit file input
  if (editFileInput.value) {
    editFileInput.value.value = '';
  }
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  isEditing.value = false;
  selectedAnhSanPham.value = {};
  originalData.value = {};
  editFile.value = null;
  editPreviewUrl.value = null;
  editErrorMessage.value = null;
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

// Pagination methods
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

/* Form thêm mới */
.add-form,
.edit-form {
  background: #ffffff;
  padding: 25px;
  border-radius: 12px;
  margin-bottom: 25px;
  border: 2px solid #4ade80;
  box-shadow: 0 4px 12px rgba(74, 222, 128, 0.1);
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 2px solid #d4edda;
}

 .form-header h3 {
   margin: 0;
   color: #4ade80;
   font-size: 24px;
   font-weight: 600;
   font-family: 'Poppins', sans-serif;
   letter-spacing: -0.3px;
 }

.btn-close {
  background: none;
  border: none;
  color: #6c757d;
  font-size: 20px;
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  width: 35px;
  height: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.btn-close:hover {
  background-color: #f8f9fa;
  color: #dc3545;
  transform: scale(1.1);
}

.form-actions {
  display: flex;
  gap: 15px;
  margin-top: 20px;
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
   font-family: 'Inter', sans-serif;
   letter-spacing: 0.2px;
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

.add-form input[type="file"],
.edit-form input[type="file"] {
  padding: 10px 0;
  border: 2px dashed #4ade80;
  border-radius: 8px;
  background-color: #f8fff9;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-form input[type="file"]:hover,
.edit-form input[type="file"]:hover {
  background-color: #d4edda;
  border-color: #22c55e;
}

/* CSS cho radio button đã được thay thế bằng CSS mới ở dưới */

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
   font-family: 'Inter', sans-serif;
   letter-spacing: 0.2px;
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
   font-family: 'Inter', sans-serif;
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
  border: 2px solid #d4edda;
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
/* CSS cho radio button đã được thay thế bằng CSS mới */

/* Table enhancements */
.table tbody tr:hover {
  background-color: #d4edda;
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

  .form-actions {
    flex-direction: column;
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
  background: linear-gradient(135deg, #32CD32, #28a745);
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
  color: #32CD32;
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
  border: 2px solid #d4edda;
  transition: all 0.3s ease;
}

.detail-image:hover {
  border-color: #4ade80;
  transform: scale(1.05);
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

.detail-file-input {
  padding: 8px 0;
  border: 2px dashed #4ade80;
  border-radius: 6px;
  background-color: #f8fff9;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
}

.detail-file-input:hover {
  background-color: #d4edda;
  border-color: #22c55e;
}

.detail-preview-image {
  width: 100px;
  height: auto;
  border-radius: 6px;
  border: 2px solid #d4edda;
  margin-top: 8px;
  transition: all 0.3s ease;
}

.detail-preview-image:hover {
  border-color: #4ade80;
  transform: scale(1.05);
}

.detail-radio {
  display: inline-block;
  margin-right: 20px;
  font-weight: 500;
  color: #495057;
  cursor: pointer;
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
  justify-content: center;
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

  .detail-image {
    width: 100%;
    max-width: 200px;
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
 
 /* Form groups trong modal */
 .form-group {
   margin-bottom: 20px;
 }
 
 .form-group label {
   display: block;
   margin-bottom: 8px;
   font-weight: 600;
   color: #4ade80;
   font-size: 16px;
   font-family: 'Inter', sans-serif;
   letter-spacing: 0.2px;
 }
 
 .modal-input {
   width: 100%;
   padding: 12px 16px;
   border: 2px solid #d4edda;
   border-radius: 8px;
   font-size: 16px;
   transition: all 0.3s ease;
   background-color: #f8fff9;
   box-sizing: border-box;
 }
 
 .modal-input:focus {
   outline: none;
   border-color: #4ade80;
   background-color: #ffffff;
   box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
 }
 
 .modal-file-input {
   padding: 10px 0;
   border: 2px dashed #4ade80;
   border-radius: 8px;
   background-color: #f8fff9;
   cursor: pointer;
   transition: all 0.3s ease;
   width: 100%;
   box-sizing: border-box;
 }
 
 .modal-file-input:hover {
   background-color: #d4edda;
   border-color: #22c55e;
 }
 
 .modal-preview-image {
   width: 100px;
   height: auto;
   border-radius: 8px;
   border: 2px solid #d4edda;
   margin-top: 10px;
   transition: all 0.3s ease;
 }
 
 .modal-preview-image:hover {
   border-color: #4ade80;
   transform: scale(1.05);
 }
 
 /* Error và Success message trong modal */
 .modal-error {
   margin-top: 15px;
   padding: 12px;
   background-color: #fff5f5;
   border: 1px solid #fed7d7;
   border-radius: 8px;
 }
 
 .modal-error p {
   margin: 0;
   font-size: 14px;
   font-weight: 500;
   color: #dc3545;
 }
 
 .modal-success {
   margin-top: 15px;
   padding: 12px;
   background-color: #f0fdf4;
   border: 1px solid #bbf7d0;
   border-radius: 8px;
 }
 
 .modal-success p {
   margin: 0;
   font-size: 14px;
   font-weight: 500;
   color: #22c55e;
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
</style>
