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
    <router-link :to="`/products/anh-san-pham`" class="product-link">
      <span class="product-text">Ảnh sản phẩm</span>
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
          placeholder="Tìm theo loại ảnh hoặc mô tả..."
          @input="handleSearch"
        />
      </div>
      <div class="action-group">
        <button @click="showAddForm = true" class="btn-export">
          <i class="fas fa-plus"></i> Thêm Hình Ảnh Mới
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
              <input
                v-model="newAnhSanPham.loaiAnh"
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
                v-model="newAnhSanPham.moTa"
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
        <i class="fas fa-save"></i>
        {{ uploading ? "Đang cập nhật..." : "Cập Nhật" }}
      </button>
      <button type="button" @click="closeEditForm" class="btn btn-secondary">
        <i class="fas fa-times"></i> Đóng
      </button>
      <p v-if="editErrorMessage" style="color: red">{{ editErrorMessage }}</p>
      <p v-if="editSuccessMessage" style="color: green">
        {{ editSuccessMessage }}
      </p>
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
      Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của {{ totalItems }} ảnh
      sản phẩm
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
              <img
                :src="getImageUrl(selectedAnhSanPham.duongDanAnh)"
                alt="Ảnh hiện tại"
                class="detail-image"
                @error="handleImageError"
              />
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
                  <input
                    type="radio"
                    name="detailTrạng thái"
                    :value="false"
                    v-model="selectedAnhSanPham.deleted"
                  />
                  <span>Hoạt động</span>
                </label>
                <label class="radio-label">
                  <input
                    type="radio"
                    name="detailTrạng thái"
                    :value="true"
                    v-model="selectedAnhSanPham.deleted"
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
          @click="saveChanges"
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
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import {
  fetchAllAnhSanPham,
  fetchCreateAnhSanPham,
  fetchUpdateAnhSanPham,
  fetchUpdateStatusAnhSanPham,
} from "../../../services/ThuocTinh/AnhSanPhamService";

const AnhSanPhams = ref([]);
const newAnhSanPham = ref({
  loaiAnh: "",
  moTa: "",
  deleted: false,
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
const deleteItemName = ref("");

// Biến cho form thêm mới
const showAddForm = ref(false);

// Biến cho tìm kiếm và lọc
const searchQuery = ref("");
const statusFilter = ref("");

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
    filtered = filtered.filter(
      (item) =>
        item.loaiAnh?.toLowerCase().includes(query) ||
        item.moTa?.toLowerCase().includes(query)
    );
  }

  // Lọc theo trạng thái
  if (statusFilter.value !== "") {
    filtered = filtered.filter(
      (item) => item.deleted === (statusFilter.value === "true")
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
    formData.append("file", file.value);
    formData.append("loaiAnh", newAnhSanPham.value.loaiAnh);
    formData.append("moTa", newAnhSanPham.value.moTa || "");
    formData.append("deleted", newAnhSanPham.value.deleted || false);

    await fetchCreateAnhSanPham(formData);

    // Reset form
    newAnhSanPham.value = {
      loaiAnh: "",
      moTa: "",
      deleted: false,
    };
    file.value = null;
    previewUrl.value = null;

    // Reset file input
    if (fileInput.value) {
      fileInput.value.value = "";
    }

    await fetchAll();
    successMessage.value = "Ảnh sản phẩm đã được thêm thành công!";
    clearSuccessMessage();
    closeAddForm(); // Đóng form sau khi thêm thành công
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value =
      "Lỗi khi thêm: " + (error.message || "Không thể tạo ảnh sản phẩm");
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
      formData.append("file", editFile.value);
      formData.append("loaiAnh", selectedAnhSanPham.value.loaiAnh);
      formData.append("moTa", selectedAnhSanPham.value.moTa || "");
      formData.append("deleted", selectedAnhSanPham.value.deleted || false);

      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    } else {
      // Nếu không có file mới, chỉ cập nhật thông tin
      // Tạo một file rỗng để backend không báo lỗi
      const emptyFile = new Blob([""], { type: "image/png" });
      const formData = new FormData();
      formData.append("file", emptyFile, "empty.png");
      formData.append("loaiAnh", selectedAnhSanPham.value.loaiAnh);
      formData.append("moTa", selectedAnhSanPham.value.moTa || "");
      formData.append("deleted", selectedAnhSanPham.value.deleted || false);

      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    }

    await fetchAll();
    closeDetailModal();
    editSuccessMessage.value = "Ảnh sản phẩm đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value =
      "Lỗi khi cập nhật: " +
      (error.message || "Không thể cập nhật ảnh sản phẩm");
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
      formData.append("file", editFile.value);
      formData.append("loaiAnh", selectedAnhSanPham.value.loaiAnh);
      formData.append("moTa", selectedAnhSanPham.value.moTa || "");
      formData.append("deleted", selectedAnhSanPham.value.deleted || false);

      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    } else {
      // Nếu không có file mới, chỉ cập nhật thông tin
      // Tạo một file rỗng để backend không báo lỗi
      const emptyFile = new Blob([""], { type: "image/png" });
      const formData = new FormData();
      formData.append("file", emptyFile, "empty.png");
      formData.append("loaiAnh", selectedAnhSanPham.value.loaiAnh);
      formData.append("moTa", selectedAnhSanPham.value.moTa || "");
      formData.append("deleted", selectedAnhSanPham.value.deleted || false);

      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    }

    await fetchAll();
    closeEditForm();
    editSuccessMessage.value = "Ảnh sản phẩm đã được cập nhật thành công!";
    clearEditSuccessMessage();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value =
      "Lỗi khi cập nhật: " +
      (error.message || "Không thể cập nhật ảnh sản phẩm");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  // Hiển thị modal xác nhận xóa
  showDeleteModal.value = true;
  deleteItemId.value = id;

  // Lấy tên ảnh để hiển thị trong thông báo
  const item = AnhSanPhams.value.find((item) => item.id === id);
  if (item) {
    deleteItemName.value = item.loaiAnh || "Ảnh sản phẩm";
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
    errorMessage.value =
      "Lỗi khi xóa: " + (error.message || "Không thể xóa ảnh sản phẩm");
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

const closeAddForm = () => {
  showAddForm.value = false;
  // Reset form
  newAnhSanPham.value = {
    loaiAnh: "",
    moTa: "",
    deleted: false,
  };
  file.value = null;
  previewUrl.value = null;
  errorMessage.value = null;
  successMessage.value = null;

  // Reset file input
  if (fileInput.value) {
    fileInput.value.value = "";
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
    editFileInput.value.value = "";
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
  if (!imagePath) return "";

  // Nếu đã là URL đầy đủ thì trả về nguyên
  if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
    return imagePath;
  }

  // Nếu là đường dẫn tương đối, thêm base URL của backend
  if (imagePath.startsWith("uploads/")) {
    return `http://localhost:8080/${imagePath}`;
  }

  // Nếu bắt đầu bằng / thì thêm base URL
  if (imagePath.startsWith("/")) {
    return `http://localhost:8080${imagePath}`;
  }

  return imagePath;
};

// Method xử lý lỗi khi ảnh không load được
const handleImageError = (event) => {
  console.error("Không thể load ảnh:", event.target.src);
  // Có thể set một ảnh mặc định
  event.target.src =
    "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik0zMCAzMEg3MFY3MEgzMFYzMFoiIGZpbGw9IiNEN0Q5RDEiLz4KPHN2ZyB4PSIzNSIgeT0iMzUiIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPgo8cGF0aCBkPSJNMTIgMkM2LjQ4IDIgMiA2LjQ4IDIgMTJzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwUzE3LjUyIDIgMTIgMnptMCAxOGMtNC40MSAwLTgtMy41OS04LTggczMuNTktOCA4LTggOCAzLjU5IDggOC0zLjU5IDgtOCA4eiIgZmlsbD0iI0E5QUFCRCIvPgo8cGF0aCBkPSJNMTIgNkM5Ljc5IDYgOCA3Ljc5IDggMTBzMS43OSA0IDQgNCA0LTEuNzkgNC00LTEuNzktNC00LTR6bTAgNmMtMS4xIDAtMi0uOS0yLTIgMC0xLjEuOS0yIDItMiAxLjEgMCAyIC45IDIgMiAwIDEuMS0uOSAyLTIgMnoiIGZpbGw9IiNBOUFBQkQiLz4KPC9zdmc+Cjwvc3ZnPgo=";
};

onMounted(fetchAll);
</script>

<style scoped>
/* CSS đã được chuyển vào productsUnified.css */
@import "../../../styles/cssSanPham/quanLySanPham.css";
@import "../../../styles/cssSanPham/productsUnified.css";
</style>
