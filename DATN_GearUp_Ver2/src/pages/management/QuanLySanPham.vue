<template>
  <div class="product-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">Quản lý sản phẩm</h1>
          <p class="page-subtitle">Quản lý thông tin và trạng thái sản phẩm</p>
        </div>
        <div class="header-actions">
          <button class="btn-refresh" @click="refreshData">
            <span class="btn-icon">🔄</span>
            Làm mới
          </button>
          <button class="btn-export" @click="exportData">
            <span class="btn-icon">📊</span>
            Xuất báo cáo
          </button>
          <button class="btn-export" @click="exportProductsToExcel">
            <span class="btn-icon">📗</span>
            Xuất Excel
          </button>
          <button class="btn-export" @click="showAddModal = true">
            <span class="btn-icon">➕</span>
            Thêm sản phẩm
          </button>
        </div>
      </div>
    </div>

    <!-- Modern Filter Section -->
    <div class="filter-section">
      <div class="filter-card">
        <div class="filter-header">
          <div class="filter-title">
            <span class="filter-icon">🔍</span>
            <h3>Tìm kiếm & Lọc sản phẩm</h3>
          </div>
          <div class="filter-stats">
            {{ totalProducts }} / {{ products.length }} sản phẩm
          </div>
        </div>

        <div class="filter-content">
          <div class="search-section">
            <div class="input-group">
              <span class="input-icon">🔍</span>
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Tìm kiếm tên sản phẩm, mã sản phẩm, mô tả..."
                class="form-control search-input"
              />
              <button
                v-if="searchQuery"
                @click="searchQuery = ''"
                class="clear-btn"
              >
                <span>✕</span>
              </button>
            </div>
          </div>

          <div class="filters-grid">
            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">📦</span>
                Danh mục
              </label>
              <select v-model="selectedCategory" class="form-select">
                <option value="">Tất cả danh mục</option>
                <option value="giay-the-thao">⚽ Giày thể thao</option>
                <option value="giay-luoi">👞 Giày lười</option>
                <option value="giay-cao-co">🥾 Giày cao cổ</option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">🏷️</span>
                Thương hiệu
              </label>
              <select v-model="selectedBrand" class="form-select">
                <option value="">Tất cả thương hiệu</option>
                <option value="balenciaga">👑 Balenciaga</option>
                <option value="converse">⭐ Converse</option>
                <option value="nike">✓ Nike</option>
                <option value="adidas">🔥 Adidas</option>
              </select>
            </div>

            <div class="filter-group">
              <label class="filter-label">
                <span class="label-icon">⚡</span>
                Trạng thái
              </label>
              <select v-model="selectedStatus" class="form-select">
                <option value="">Tất cả trạng thái</option>
                <option value="active">✅ Hoạt động</option>
                <option value="inactive">❌ Ngừng hoạt động</option>
              </select>
            </div>

            <div class="filter-actions">
              <button @click="clearFilters" class="btn btn-outline">
                <span class="btn-icon">🔄</span>
                Đặt lại
              </button>
              <button @click="applyFilters" class="btn btn-primary">
                <span class="btn-icon">🔍</span>
                Áp dụng
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Products Table -->
    <div class="card">
      <div class="card-body">
        <table class="table">
          <thead>
            <tr>
              <th>STT</th>
              <th>Mã sản phẩm</th>
              <th>Tên sản phẩm</th>
              <th>Nhà sản xuất</th>
              <th>Xuất xứ</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, i) in filteredProducts" :key="i">
              <td>{{ startIndex + i + 1 }}</td>
              <td>{{ product.maSanPham }}</td>
              <td>{{ product.tenSanPham }}</td>
              <td>{{ product.tenNhaSanXuat }}</td>
              <td>{{ product.tenXuatXu }}</td>
              <td>{{ product.deleted ? "Không hoạt động" : "Hoạt động" }}</td>
              <td>
                <ButtonGroup spacing="xs">
                  <button
                    class="btn btn-secondary"
                    @click="viewProduct(product)"
                  >
                    👁️
                  </button>
                  <button
                    class="btn btn-secondary"
                    @click="editProduct(product)"
                  >
                    ✏️
                  </button>
                  <button
                    class="btn btn-danger"
                    @click="deleteProduct(product)"
                  >
                    🗑️
                  </button>
                </ButtonGroup>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Pagination -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            Hiển thị {{ startIndex + 1 }} - {{ endIndex }} của
            {{ totalProducts }} sản phẩm
          </div>
          <div class="pagination">
            <button
              class="btn btn-outline btn-sm"
              @click="previousPage"
              :disabled="currentPage === 1"
            >
              ❮ Trước
            </button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button
              class="btn btn-outline btn-sm"
              @click="nextPage"
              :disabled="currentPage === totalPages"
            >
              Sau ❯
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modern Add/Edit Product Modal -->
    <div
      v-if="showAddModal || showEditModal"
      class="modern-modal-overlay"
      @click="closeModals"
    >
      <div class="modern-modal-content" @click.stop>
        <!-- Modal Header -->
        <div class="modern-modal-header">
          <div class="header-content">
            <div class="header-icon">
              <svg
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="currentColor"
              >
                <path
                  d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"
                />
              </svg>
            </div>
            <div class="header-text">
              <h2>
                {{ showAddModal ? "Thêm sản phẩm mới" : "Chỉnh sửa sản phẩm" }}
              </h2>
              <p>
                {{
                  showAddModal
                    ? "Tạo sản phẩm mới với thể loại 8-core attributes và SKU auto-generation"
                    : "Cập nhật thông tin sản phẩm"
                }}
              </p>
            </div>
          </div>
          <button class="modern-close-btn" @click="closeModals">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
              <path
                d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
              />
            </svg>
          </button>
        </div>

        <div class="modern-modal-body">
          <!-- Basic Information Section -->
          <div class="form-section">
            <div class="section-header">
              <div class="section-icon">📋</div>
              <h3>Thông tin cơ bản</h3>
            </div>
            <div class="section-content">
              <div class="form-grid">
                <div class="form-field">
                  <label class="modern-label">Tên sản phẩm *</label>
                  <input
                    type="text"
                    v-model="productForm.tenSanPham"
                    class="modern-input"
                    placeholder="Nhập tên sản phẩm"
                    required
                  />
                </div>

                <div class="form-field">
                  <label class="modern-label">Nhà sản xuất</label>
                  <select
                    v-model="productForm.maNhaSanXuat"
                    class="modern-select"
                    required
                  >
                    <option
                      v-for="value in NhaSanXuats"
                      :key="value.id"
                      :value="value.maNhaSanXuat"
                    >
                      {{ value.tenNhaSanXuat }}
                    </option>
                  </select>
                </div>

                <div class="form-field">
                  <label class="modern-label">Xuất xứ</label>
                  <select
                    v-model="productForm.maXuatXu"
                    class="modern-select"
                    required
                  >
                    <option
                      v-for="value in XuatXus"
                      :key="value.id"
                      :value="value.maXuatXu"
                    >
                      {{ value.tenXuatXu }}
                    </option>
                  </select>
                </div>

                <div class="form-field span-2">
                  <label class="modern-label">Trạng thái</label>
                  <div class="toggle-group">
                    <label
                      class="toggle-option"
                      :class="{ active: productForm.deleted === false }"
                    >
                      <input
                        class="toggle-indicator"
                        type="radio"
                        value="false"
                        v-model="productForm.deleted"
                      />
                      Hoạt động
                    </label>
                    <label
                      class="toggle-option"
                      :class="{ active: productForm.deleted === true }"
                    >
                      <input
                        class="toggle-indicator"
                        type="radio"
                        value="true"
                        v-model="productForm.deleted"
                      />
                      Không hoạt động
                    </label>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Modal Footer -->
        <div class="modern-modal-footer">
          <button type="button" class="btn-secondary" @click="closeModals">
            Hủy bỏ
          </button>
          <button type="button" class="btn-primary" @click="saveProduct">
            {{ showAddModal ? "Tạo sản phẩm" : "Cập nhật" }}
          </button>
        </div>
      </div>
    </div>

    <!-- Product Detail Modal -->
    <div
      v-if="showDetailModal"
      class="modal-overlay"
      @click="showDetailModal = false"
    >
      <div class="modal-content large" @click.stop>
        <div class="modal-header">
          <h3>Chi tiết sản phẩm</h3>
          <button class="modal-close" @click="showDetailModal = false">
            ✕
          </button>
        </div>

        <div class="modal-body" v-if="selectedProduct">
          <div class="product-detail">
            <div class="product-detail-info">
              <h4>{{ selectedProduct.tenSanPham }}</h4>
              <p>
                <strong>Mã sản phẩm:</strong> {{ selectedProduct.maSanPham }}
              </p>
              <p>
                <strong>Nhà sản xuất:</strong>
                {{ selectedProduct.tenNhaSanXuat }}
              </p>
              <p><strong>Xuất xứ:</strong> {{ selectedProduct.tenXuatXu }}</p>
              <p>
                <strong>Trạng thái:</strong>
                <span
                  :class="[
                    'badge',
                    selectedProduct.deleted === false
                      ? 'badge-success'
                      : 'badge-danger',
                  ]"
                >
                  {{
                    selectedProduct.deleted === false
                      ? "Hoạt động"
                      : "Ngừng hoạt động"
                  }}
                </span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
// import ActionButton from '@/components/ui/ActionButton.vue'
import ButtonGroup from "@/components/ui/NhomNut.vue";
import {
  fetchAllSanPham,
  fetchCreateSanPham,
  fetchUpdateSanPham,
  fetchUpdateStatusSanPham,
} from "../../services/SanPham/SanPhamService";
import { fetchAllNhaSanXuat } from "../../services/ThuocTinh/NhaSanXuatService";
import { fetchAllXuatXu } from "../../services/ThuocTinh/XuatXuService";

const searchQuery = ref("");
const selectedCategory = ref("");
const selectedBrand = ref("");
const selectedStatus = ref("");
const currentPage = ref(1);
const itemsPerPage = ref(10);
const showAddModal = ref(false);
const showEditModal = ref(false);
const showDetailModal = ref(false);
const selectedProduct = ref(null);
const activeVariantTab = ref("auto");
const productForm = ref({});

const products = ref([]);
const NhaSanXuats = ref([]);
const XuatXus = ref([]);
const fetch = async () => {
  await fetchAllData();
  await fetchAllNSX();
  await fetchAllXX();
};
const fetchAllData = async () => {
  try {
    const res = await fetchAllSanPham();
    products.value = res.data;
  } catch (error) {
    console.error("Error fetching products:", error);
  }
};
const fetchAllNSX = async () => {
  try {
    const res = await fetchAllNhaSanXuat();
    NhaSanXuats.value = res.data;
  } catch (error) {
    console.error("Error fetching manufacturers:", error);
  }
};
const fetchAllXX = async () => {
  try {
    const res = await fetchAllXuatXu();
    XuatXus.value = res.data;
  } catch (error) {
    console.error("Error fetching origins:", error);
  }
};
const fetchCreate = async (data) => {
  try {
    await fetchCreateSanPham(data);
    await fetchAllData();
  } catch (error) {
    console.error("Error creating product:", error);
  }
};
const fetchUpdate = async (id, data) => {
  try {
    await fetchUpdateSanPham(id, data);
  } catch (error) {
    console.error("Error updating product:", error);
  }
};
const getOneXX = (ma) => {
  try {
    const selectedXX = XuatXus.value.find((item) => item.maXuatXu === ma);
    return selectedXX.id;
  } catch (error) {
    console.error("Error fetching origin:", error);
  }
};
const getOneNSX = (ma) => {
  try {
    const selectedNSX = NhaSanXuats.value.find(
      (item) => item.maNhaSanXuat === ma
    );
    return selectedNSX.id;
  } catch (error) {
    console.error("Error fetching manufacturer:", error);
  }
};
// Computed
const filteredProducts = computed(() => {
  let filtered = products.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (product) =>
        product.maSanPham
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        product.tenSanPham
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())
    );
  }

  if (selectedCategory.value) {
    filtered = filtered.filter(
      (product) =>
        product.tenNhaSanXuat.toLowerCase() === selectedCategory.value
    );
  }

  if (selectedBrand.value) {
    filtered = filtered.filter(
      (product) => product.tenXuatXu.toLowerCase() === selectedBrand.value
    );
  }

  if (selectedStatus.value) {
    const statusValue = selectedStatus.value === "active" ? false : true;
    filtered = filtered.filter((product) => product.deleted === statusValue);
  }

  return filtered.slice(startIndex.value, endIndex.value);
});

const totalProducts = computed(() => {
  let filtered = products.value;

  if (searchQuery.value) {
    filtered = filtered.filter(
      (product) =>
        product.maSanPham
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        product.tenSanPham
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())
    );
  }

  if (selectedCategory.value) {
    filtered = filtered.filter(
      (product) =>
        product.tenNhaSanXuat.toLowerCase() === selectedCategory.value
    );
  }

  if (selectedBrand.value) {
    filtered = filtered.filter(
      (product) => product.tenXuatXu.toLowerCase() === selectedBrand.value
    );
  }

  if (selectedStatus.value) {
    const statusValue = selectedStatus.value === "active" ? false : true;
    filtered = filtered.filter((product) => product.deleted === statusValue);
  }

  return filtered.length;
});

const totalPages = computed(() =>
  Math.ceil(totalProducts.value / itemsPerPage.value)
);

const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);
const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, totalProducts.value)
);

const previousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const viewProduct = (product) => {
  selectedProduct.value = product;
  showDetailModal.value = true;
};

const editProduct = (product) => {
  productForm.value = { ...product };
  showEditModal.value = true;
};
const deleteProduct = async (product) => {
  if (confirm(`Bạn có chắc chắn muốn xóa sản phẩm "${product.tenSanPham}"?`)) {
    await fetchUpdateStatus(product);
    await fetch();
  }
};
const fetchUpdateStatus = async (product) => {
  try {
    await fetchUpdateStatusSanPham(product.id);
    await fetchAllData();
  } catch (error) {
    console.error("Error updating product status:", error);
  }
};
const closeModals = () => {
  showAddModal.value = false;
  showEditModal.value = false;
  resetForm();
};
const resetForm = () => {
  productForm.value = {
    tenSanPham: "",
    maNhaSanXuat: "",
    maXuatXu: "",
    deleted: false,
  };
  activeVariantTab.value = "auto";
};
const saveProduct = async () => {
  if (showAddModal.value) {
    const newProduct = {
      tenSanPham: productForm.value.tenSanPham,
      idNhaSanXuat: getOneNSX(productForm.value.maNhaSanXuat),
      idXuatXu: getOneXX(productForm.value.maXuatXu),
      deleted: productForm.value.deleted,
    };
    await fetchCreate(newProduct);
  } else if (showEditModal.value) {
    const newProduct = {
      tenSanPham: productForm.value.tenSanPham,
      idNhaSanXuat: getOneNSX(productForm.value.maNhaSanXuat),
      idXuatXu: getOneXX(productForm.value.maXuatXu),
      deleted: productForm.value.deleted,
    };
    console.log("Updating product:", newProduct);
    await fetchUpdate(productForm.value.id, newProduct);
  }
  closeModals();
  await fetch();
};
const exportData = () => {
  alert("Xuất báo cáo sản phẩm");
};

const exportProductsToExcel = () => {
  try {
    const headerMapping = {
      id: "ID",
      name: "Tên sản phẩm",
      brand: "Thương hiệu",
      category: "Danh mục",
      price: "Giá (VND)",
      stock: "Tồn kho",
      status: "Trạng thái",
      created_date: "Ngày tạo",
    };

    const filteredData = filteredProducts.value.map((item) => ({
      id: item.id || "N/A",
      name: item.name || "N/A",
      brand: item.brand || "N/A",
      category: item.category || "N/A",
      price: item.price
        ? new Intl.NumberFormat("vi-VN").format(item.price)
        : "N/A",
      stock: item.stock || 0,
      status: item.status === "active" ? "Hoạt động" : "Tạm dừng",
      created_date: item.created_date
        ? new Date(item.created_date).toLocaleDateString("vi-VN")
        : "N/A",
    }));

    const result = exportToExcel(
      filteredData,
      "Product_Management",
      "Danh sách sản phẩm",
      headerMapping
    );

    if (result && result.success) {
      alert(`✅ ${result.message}`);
    } else {
      alert(
        `❌ ${result ? result.message : "Có lỗi xảy ra khi xuất file Excel"}`
      );
    }
  } catch (error) {
    console.error("Error exporting to Excel:", error);
    alert(`❌ Có lỗi xảy ra khi xuất file Excel: ${error.message}`);
  }
};

onMounted(fetch);
</script>

<style scoped>
.product-management {
  max-width: 1400px;
  margin: 0 auto;
}

/* page-header styles are now defined in globals.css */
/* .page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-header h2 {
  margin: 0;
  color: var(--secondary-color);
} */

/* Filter Section */
.filter-section {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: var(--shadow);
}

.search-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  display: flex;
  gap: 0.5rem;
  flex: 1;
  min-width: 300px;
}

.filter-controls {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.filter-controls select {
  min-width: 150px;
}

/* Table Styles */
.table {
  margin: 0;
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.table th {
  background-color: #4ade80;
  color: white;
  font-weight: 600;
  padding: 1rem;
  text-align: center;
  font-size: 0.875rem;
  white-space: nowrap;
  position: sticky;
  top: 0;
  z-index: 10;
}

.table td {
  padding: 1rem;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid var(--border-color);
  font-size: 0.875rem;
}

.product-image {
  width: 50px;
  height: 50px;
  margin: 0 auto;
  border-radius: 8px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--light-gray);
  font-size: 1.5rem;
}

.placeholder-image.large {
  width: 200px;
  height: 200px;
  font-size: 3rem;
}

.product-info {
  text-align: left;
}

.product-info strong {
  display: block;
  margin-bottom: 0.25rem;
}

.product-code {
  color: var(--medium-gray);
  font-size: 0.875rem;
  margin: 0;
}

.price {
  font-weight: 600;
  color: #4ade80;
}

.stock {
  font-weight: 600;
}

.stock.low-stock {
  color: var(--danger-color);
}

/* Pagination */
.pagination-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--border-color);
}

.pagination {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.page-info {
  font-weight: 600;
  color: var(--secondary-color);
}

/* btn-export styles now in globals.css */

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.modal-content.large {
  max-width: 800px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  margin: 0;
  color: var(--secondary-color);
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.3s ease;
}

.modal-close:hover {
  background-color: var(--light-gray);
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid var(--border-color);
}

/* Form Styles */
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.image-upload {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.image-preview {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid var(--border-color);
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Product Detail */
.product-detail {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 2rem;
}

.product-detail-image {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid var(--border-color);
}

.product-detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-detail-info h4 {
  margin: 0 0 1rem 0;
  color: var(--secondary-color);
}

.product-detail-info p {
  margin: 0.5rem 0;
}

.description {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color);
}

.description p {
  color: var(--medium-gray);
  line-height: 1.6;
}

/* Responsive Design */
@media (max-width: 1200px) {
  .product-management {
    padding: 0 1rem;
  }
}

@media (max-width: 1024px) {
  .search-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    min-width: auto;
  }

  .product-detail {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .table {
    font-size: 0.875rem;
  }

  .table th,
  .table td {
    padding: 0.75rem 0.5rem;
  }
}

@media (max-width: 768px) {
  /* .page-header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  } */

  .filter-controls {
    flex-direction: column;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .pagination-wrapper {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }

  .modal-overlay {
    padding: 1rem;
  }

  .table {
    font-size: 0.8125rem;
  }

  .table th,
  .table td {
    padding: 0.5rem 0.25rem;
  }

  /* Hide less important columns on mobile */
  .table th:nth-child(2),
  .table td:nth-child(2),
  .table th:nth-child(5),
  .table td:nth-child(5) {
    display: none;
  }
}

@media (max-width: 480px) {
  .product-management {
    padding: 0 0.5rem;
  }

  .filter-section {
    padding: 1rem;
  }

  .table {
    font-size: 0.75rem;
  }

  /* Hide more columns on very small screens */
  .table th:nth-child(4),
  .table td:nth-child(4),
  .table th:nth-child(7),
  .table td:nth-child(7) {
    display: none;
  }

  .card {
    margin: 0 -0.5rem;
    border-radius: 0;
  }
}

/* Modern Modal Styles */
.modern-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
}

.modern-modal-content {
  background: #ffffff;
  border-radius: 16px;
  width: 100%;
  max-width: 900px;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
  display: flex;
  flex-direction: column;
}

.modern-modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2rem 2rem 1.5rem 2rem;
  border-bottom: 1px solid #f1f5f9;
  background: linear-gradient(135deg, #f8fafc 0%, #ffffff 100%);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.header-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.header-text h2 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
  line-height: 1.2;
}

.header-text p:not(.page-subtitle) {
  margin: 0.25rem 0 0 0;
  font-size: 0.875rem;
  color: #64748b;
  line-height: 1.4;
}

.modern-close-btn {
  width: 40px;
  height: 40px;
  border: none;
  background: #f1f5f9;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #64748b;
}

.modern-close-btn:hover {
  background: #e2e8f0;
  color: #334155;
  transform: scale(1.05);
}

.modern-modal-body {
  flex: 1;
  overflow-y: auto;
  padding: 0 2rem 2rem 2rem;
}

.modern-modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem 2rem 2rem 2rem;
  border-top: 1px solid #f1f5f9;
  background: #fafbfc;
}

/* Form Section Styles */
.form-section {
  margin-bottom: 2rem;
  background: #ffffff;
  border: 1px solid #f1f5f9;
  border-radius: 12px;
  overflow: hidden;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-bottom: 1px solid #e2e8f0;
}

.section-icon {
  font-size: 1.25rem;
}

.section-header h3 {
  margin: 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: #334155;
  flex: 1;
}

.section-badge {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  cursor: help;
}

.section-content {
  padding: 1.5rem;
}

/* Form Grid and Fields */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.form-field {
  display: flex;
  flex-direction: column;
}

.form-field.span-2 {
  grid-column: span 2;
}

.modern-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.modern-input,
.modern-select,
.modern-textarea {
  padding: 0.75rem 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  font-size: 0.875rem;
  transition: all 0.2s ease;
  background: #ffffff;
}

.modern-input:focus,
.modern-select:focus,
.modern-textarea:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.input-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.input-group .modern-input {
  flex: 1;
}

.input-addon-btn {
  padding: 0.75rem;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  background: #f9fafb;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 1rem;
}

.input-addon-btn:hover {
  background: #f3f4f6;
  border-color: #d1d5db;
}

.field-hint {
  margin-top: 0.5rem;
  font-size: 0.75rem;
  color: #6b7280;
  line-height: 1.4;
}

/* Toggle Group */
.toggle-group {
  display: flex;
  gap: 1rem;
}

.toggle-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #ffffff;
  font-size: 0.875rem;
  font-weight: 500;
}

.toggle-option.active {
  border-color: #3b82f6;
  background: #eff6ff;
  color: #1d4ed8;
}

.toggle-indicator {
  width: 16px;
  height: 16px;
  border: 2px solid #d1d5db;
  border-radius: 50%;
  position: relative;
  transition: all 0.2s ease;
}

.toggle-option.active .toggle-indicator {
  border-color: #3b82f6;
  background: #3b82f6;
}

.toggle-option.active .toggle-indicator::after {
  content: "";
  width: 6px;
  height: 6px;
  background: #ffffff;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

/* Image Upload Grid */
.image-upload-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.image-upload-item {
  aspect-ratio: 1;
  border: 2px dashed #d1d5db;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
}

.image-upload-item:hover {
  border-color: #3b82f6;
  background: #f8fafc;
}

.image-upload-item.main-image {
  grid-row: span 2;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: #6b7280;
  text-align: center;
  padding: 1rem;
}

.upload-placeholder.small {
  padding: 0.5rem;
}

.upload-placeholder p {
  margin: 0;
  font-size: 0.875rem;
  font-weight: 600;
}

.upload-placeholder span {
  font-size: 0.75rem;
  color: #9ca3af;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  position: relative;
}

.uploaded-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  width: 24px;
  height: 24px;
  border: none;
  background: rgba(239, 68, 68, 0.9);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 0.75rem;
  opacity: 0;
  transition: all 0.2s ease;
}

.uploaded-image:hover .remove-image {
  opacity: 1;
}

.remove-image:hover {
  background: #dc2626;
  transform: scale(1.1);
}

.upload-guidelines {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 1rem;
}

.upload-guidelines p {
  margin: 0 0 0.5rem 0;
  font-size: 0.875rem;
  font-weight: 600;
  color: #374151;
}

.upload-guidelines ul {
  margin: 0;
  padding-left: 1rem;
  font-size: 0.75rem;
  color: #6b7280;
  line-height: 1.5;
}

/* Variant Configuration */
.variant-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.variant-tab {
  padding: 0.75rem 1.5rem;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  color: #6b7280;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
}

.variant-tab.active {
  color: #3b82f6;
  border-bottom-color: #3b82f6;
}

.variant-tab:hover:not(.active) {
  color: #374151;
}

.variant-panel {
  min-height: 300px;
}

.auto-variant-section h4 {
  margin: 0 0 1.5rem 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: #374151;
}

.create-variant-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: 1rem;
}

.create-variant-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.variant-note {
  display: block;
  margin-top: 0.5rem;
  font-size: 0.75rem;
  color: #6b7280;
  line-height: 1.4;
}

/* Modal Footer Buttons */
.btn-secondary,
.btn-primary {
  padding: 0.75rem 2rem;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  border: none;
}

.btn-secondary {
  background: #f1f5f9;
  color: #475569;
  border: 1px solid #e2e8f0;
}

.btn-secondary:hover {
  background: #e2e8f0;
}

.btn-primary {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

/* Responsive Design for Modern Modal */
@media (max-width: 768px) {
  .modern-modal-overlay {
    padding: 1rem;
  }

  .modern-modal-content {
    max-width: 100%;
  }

  .modern-modal-header,
  .modern-modal-body,
  .modern-modal-footer {
    padding-left: 1rem;
    padding-right: 1rem;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .image-upload-grid {
    grid-template-columns: 1fr 1fr;
    gap: 0.75rem;
  }

  .image-upload-item.main-image {
    grid-column: span 2;
    grid-row: span 1;
  }

  .variant-tabs {
    flex-direction: column;
  }

  .variant-tab {
    text-align: left;
    border-bottom: 1px solid #e5e7eb;
    border-right: 2px solid transparent;
  }

  .variant-tab.active {
    border-bottom-color: #e5e7eb;
    border-right-color: #3b82f6;
  }
}

@media (max-width: 480px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }

  .modern-modal-footer {
    flex-direction: column-reverse;
  }

  .btn-secondary,
  .btn-primary {
    width: 100%;
    justify-content: center;
  }
}
</style>
