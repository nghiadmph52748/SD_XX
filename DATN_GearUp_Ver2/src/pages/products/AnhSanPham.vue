<template>
  <div class="add-form">
    <h3>Thêm Hình Ảnh Sản Phẩm Mới</h3>
    <form @submit.prevent="fetchCreate">
      <div>
        <label>Chọn ảnh từ máy:</label>
        <input
          type="file"
          ref="fileInput"
          @change="handleFileChange"
          accept="image/*"
          required
        />
        <img
          v-if="previewUrl"
          :src="previewUrl"
          alt="Preview ảnh"
          style="width: 100px; height: auto; margin-top: 10px"
        />
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
        <input
          type="radio"
          name="Trạng thái"
          value="false"
          v-model="newAnhSanPham.deleted"
        />Hoạt động
        <input
          type="radio"
          name="Trạng thái"
          value="true"
          v-model="newAnhSanPham.deleted"
        />Không hoạt động
      </div>
      <button type="submit" :disabled="uploading">Thêm Mới</button>
      <p v-if="errorMessage" style="color: red">{{ errorMessage }}</p>
    </form>
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
      <button type="submit" :disabled="uploading">Cập Nhật</button>
      <button type="button" @click="closeEditForm">Đóng</button>
      <p v-if="editErrorMessage" style="color: red">{{ editErrorMessage }}</p>
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
          <button v-on:click="fetchDetail(value)">Detail</button> |
          <button v-on:click="fetchDelete(value.id)">Delete</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
import { onMounted, ref } from "vue";
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
const selectedAnhSanPham = ref({});
const file = ref(null);
const previewUrl = ref(null);
const editFile = ref(null);
const editPreviewUrl = ref(null);
const showEditForm = ref(false);
const uploading = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);

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
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value = "Lỗi khi thêm: " + (error.message || "Không thể tạo ảnh sản phẩm");
  } finally {
    uploading.value = false;
  }
};

const fetchDetail = (value) => {
  selectedAnhSanPham.value = { ...value };
  editPreviewUrl.value = value.duongDanAnh;
  showEditForm.value = true;
};

const fetchUpdate = async () => {
  uploading.value = true;
  editErrorMessage.value = null;
  
  try {
    // Nếu có file mới được chọn
    if (editFile.value) {
      const formData = new FormData();
      formData.append('file', editFile.value);
      formData.append('loaiAnh', selectedAnhSanPham.value.loaiAnh);
      formData.append('moTa', selectedAnhSanPham.value.moTa || '');
      formData.append('deleted', selectedAnhSanPham.value.deleted || false);
      
      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, formData);
    } else {
      // Nếu không có file mới, chỉ cập nhật thông tin
      const updateData = {
        loaiAnh: selectedAnhSanPham.value.loaiAnh,
        moTa: selectedAnhSanPham.value.moTa || '',
        deleted: selectedAnhSanPham.value.deleted || false
      };
      
      await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, updateData);
    }
    
    await fetchAll();
    closeEditForm();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value = "Lỗi khi cập nhật: " + (error.message || "Không thể cập nhật ảnh sản phẩm");
  } finally {
    uploading.value = false;
  }
};

const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusAnhSanPham(id);
    await fetchAll();
  } catch (error) {
    console.error("There has been a problem with your fetch operation:", error);
  }
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
