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
          :src="selectedAnhSanPham.duongDanAnh"
          alt="Ảnh hiện tại"
          style="width: 100px; height: auto"
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
            :src="value.duongDanAnh"
            alt="Ảnh sản phẩm"
            style="width: 100px; height: auto"
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
} from "../../services/AnhSanPhamService";
const AnhSanPhams = ref([]);
const newAnhSanPham = ref({});
const selectedAnhSanPham = ref({});
const file = ref(null);
const previewUrl = ref(null);
const editFile = ref(null);
const editPreviewUrl = ref(null);
const showEditForm = ref(false);
const uploading = ref(false);
const errorMessage = ref(null);
const editErrorMessage = ref(null);

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
  uploading.value = true;
  errorMessage.value = null;
  try {
    newAnhSanPham.value.duongDanAnh = file.value;
    await fetchCreateAnhSanPham(newAnhSanPham.value);
    newAnhSanPham.value = { duongDanAnh: "",loaiAnh: "", moTa: "", deleted: false };
    file.value = null;
    previewUrl.value = null;
    await fetchAll();
  } catch (error) {
    console.error("Error creating:", error);
    errorMessage.value = "Lỗi khi thêm: " + error.message;
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
    selectedAnhSanPham.value.duongDanAnh = editFile
    await fetchUpdateAnhSanPham(selectedAnhSanPham.value.id, selectedAnhSanPham.value);
    await fetchAll();
    closeEditForm();
  } catch (error) {
    console.error("Error updating:", error);
    editErrorMessage.value = "Lỗi khi cập nhật: " + error.message;
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
};
onMounted(fetchAll);
</script>
