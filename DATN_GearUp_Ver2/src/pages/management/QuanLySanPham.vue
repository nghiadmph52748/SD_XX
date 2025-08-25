<template>
  <div class="product-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">Quản Lý Sản Phẩm</h1>
          <p class="page-subtitle">Quản lý thông tin và trạng thái sản phẩm</p>
        </div>
                 <div class="header-actions">
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
                 placeholder="Tìm kiếm theo mã SP, tên SP, nhà sản xuất, xuất xứ..."
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
               <th class="sortable" @click="handleSort('NhaSanXuat')">
                 Nhà sản xuất
                 <span v-if="sortBy === 'NhaSanXuat'" class="sort-icon">
                   {{ sortOrder === 'asc' ? '↑' : '↓' }}
                 </span>
               </th>
               <th class="sortable" @click="handleSort('XuatXu')">
                 Xuất xứ
                 <span v-if="sortBy === 'XuatXu'" class="sort-icon">
                   {{ sortOrder === 'asc' ? '↑' : '↓' }}
                 </span>
               </th>
               <th class="sortable" @click="handleSort('TrangThai')">
                 Trạng thái
                 <span v-if="sortBy === 'TrangThai'" class="sort-icon">
                   {{ sortOrder === 'asc' ? '↑' : '↓' }}
                 </span>
               </th>
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
           <button type="button" class="btn-primary" @click="saveProduct">
             {{ showAddModal ? "Tạo sản phẩm" : "Cập nhật" }}
           </button>
         </div>
      </div>
    </div>

         <!-- Success Notification Modal -->
     <div
       v-if="showSuccessModal"
       class="success-modal-overlay"
       @click="closeSuccessModal"
     >
       <div class="success-modal-content" @click.stop>
         <div class="success-icon">
           <svg width="64" height="64" viewBox="0 0 24 24" fill="currentColor">
             <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
           </svg>
         </div>
         <div class="success-content">
           <h2 class="success-title">Thành công!</h2>
           <p class="success-message">{{ successMessage }}</p>
         </div>
         <button class="success-close-btn" @click="closeSuccessModal">
           Đóng
         </button>
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
const sortBy = ref(""); // NhaSanXuat, XuatXu, TrangThai
const sortOrder = ref("asc"); // asc, desc
const activeSortDropdown = ref(""); // Để kiểm soát dropdown nào đang mở
const showAddModal = ref(false);
const showEditModal = ref(false);
const showDetailModal = ref(false);
const showSuccessModal = ref(false);
const successMessage = ref("");
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
    const searchTerm = searchQuery.value.toLowerCase();
    filtered = filtered.filter(
      (product) =>
        product.maSanPham?.toLowerCase().includes(searchTerm) ||
        product.tenSanPham?.toLowerCase().includes(searchTerm) ||
        product.tenNhaSanXuat?.toLowerCase().includes(searchTerm) ||
        product.tenXuatXu?.toLowerCase().includes(searchTerm)
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

  // Sắp xếp dữ liệu
  if (sortBy.value) {
    filtered.sort((a, b) => {
      let aValue, bValue;
      
      switch (sortBy.value) {
        case "NhaSanXuat":
          aValue = a.tenNhaSanXuat?.toLowerCase() || "";
          bValue = b.tenNhaSanXuat?.toLowerCase() || "";
          break;
        case "XuatXu":
          aValue = a.tenXuatXu?.toLowerCase() || "";
          bValue = b.tenXuatXu?.toLowerCase() || "";
          break;
        
        default:
          return 0;
      }
      
      if (sortOrder.value === "asc") {
        return aValue.localeCompare(bValue, 'vi');
      } else {
        return bValue.localeCompare(aValue, 'vi');
      }
    });
  }

  return filtered.slice(startIndex.value, endIndex.value);
});

const totalProducts = computed(() => {
  let filtered = products.value;

  if (searchQuery.value) {
    const searchTerm = searchQuery.value.toLowerCase();
    filtered = filtered.filter(
      (product) =>
        product.maSanPham?.toLowerCase().includes(searchTerm) ||
        product.tenSanPham?.toLowerCase().includes(searchTerm) ||
        product.tenNhaSanXuat?.toLowerCase().includes(searchTerm) ||
        product.tenXuatXu?.toLowerCase().includes(searchTerm)
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

const handleSort = (field, order = null) => {
  if (order) {
    sortBy.value = field;
    sortOrder.value = order;
  } else {
    if (sortBy.value === field) {
      sortOrder.value = sortOrder.value === "asc" ? "desc" : "asc";
    } else {
      sortBy.value = field;
      sortOrder.value = "asc";
    }
  }
  currentPage.value = 1; // Reset về trang đầu khi sắp xếp
  activeSortDropdown.value = ""; // Đóng dropdown sau khi chọn
};

const toggleSortDropdown = (field) => {
  if (activeSortDropdown.value === field) {
    activeSortDropdown.value = "";
  } else {
    activeSortDropdown.value = field;
  }
};

const filterByNhaSanXuat = (nsx) => {
  selectedCategory.value = nsx;
  currentPage.value = 1;
  activeSortDropdown.value = "";
};

const filterByXuatXu = (xx) => {
  selectedBrand.value = xx;
  currentPage.value = 1;
  activeSortDropdown.value = "";
};

const filterByTrangThai = (status) => {
  selectedStatus.value = status ? "inactive" : "active";
  currentPage.value = 1;
  activeSortDropdown.value = "";
};

const clearSort = () => {
  sortBy.value = "";
  sortOrder.value = "asc";
  currentPage.value = 1;
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

const closeSuccessModal = () => {
  showSuccessModal.value = false;
  successMessage.value = "";
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
  try {
    if (showAddModal.value) {
      const newProduct = {
        tenSanPham: productForm.value.tenSanPham,
        idNhaSanXuat: getOneNSX(productForm.value.maNhaSanXuat),
        idXuatXu: getOneXX(productForm.value.maXuatXu),
        deleted: productForm.value.deleted,
      };
      await fetchCreate(newProduct);
      successMessage.value = `Sản phẩm "${productForm.value.tenSanPham}" đã được tạo thành công!`;
      showSuccessModal.value = true;
    } else if (showEditModal.value) {
      const newProduct = {
        tenSanPham: productForm.value.tenSanPham,
        idNhaSanXuat: getOneNSX(productForm.value.maNhaSanXuat),
        idXuatXu: getOneXX(productForm.value.maXuatXu),
        deleted: productForm.value.deleted,
      };
      console.log("Updating product:", newProduct);
      await fetchUpdate(productForm.value.id, newProduct);
      successMessage.value = `Sản phẩm "${productForm.value.tenSanPham}" đã được cập nhật thành công!`;
      showSuccessModal.value = true;
    }
    closeModals();
    await fetch();
  } catch (error) {
    console.error("Error saving product:", error);
    alert("Có lỗi xảy ra khi lưu sản phẩm!");
  }
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

 .table th.sortable {
   cursor: pointer;
   user-select: none;
   transition: background-color 0.2s ease;
 }

 .table th.sortable:hover {
   background-color: #22c55e;
 }

 .sort-icon {
   margin-left: 0.5rem;
   font-weight: bold;
   font-size: 1rem;
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
  backdrop-filter: blur(4px);
}

.modern-modal-content {
  background: white;
  border-radius: 16px;
  width: 100%;
  max-width: 900px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.1);
  animation: modalSlideIn 0.3s ease-out;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modern-modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2rem 2rem 1.5rem 2rem;
  border-bottom: 2px solid #f3f4f6;
  background: #22c55e;
  border-radius: 16px 16px 0 0;
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
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  backdrop-filter: blur(10px);
}

.header-text h2 {
  margin: 0;
  color: white;
  font-size: 1.5rem;
  font-weight: 700;
  font-family: 'Inter', sans-serif;
  line-height: 1.2;
}

.header-text p:not(.page-subtitle) {
  margin: 0.25rem 0 0 0;
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.4;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  margin-bottom: 8px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.page-subtitle {
  font-size: 1.1rem;
  color: #e5e7eb;
  margin: 0;
  font-weight: 400;
  line-height: 1.5;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.modern-close-btn {
  background: rgba(239, 68, 68, 0.1);
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s ease;
  color: #ef4444;
}

.modern-close-btn:hover {
  background-color: #ef4444;
  color: white;
  transform: scale(1.1);
}

.modern-modal-body {
  flex: 1;
  overflow-y: auto;
  padding: 2rem;
  background: white;
}

.modern-modal-footer {
  display: flex;
  justify-content: center;
  gap: 1rem;
  padding: 1.5rem 2rem 2rem 2rem;
  border-top: 2px solid #f3f4f6;
  background: #f9fafb;
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
  background: #22c55e;
  color: white;
  border: none;
  padding: 1rem 2.5rem;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 700;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  position: relative;
  overflow: hidden;
}

.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.4);
  background: #059669;
}

.btn-primary:active {
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
}

.btn-primary::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.btn-primary:hover::before {
  left: 100%;
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

 /* Success Modal Styles */
 .success-modal-overlay {
   position: fixed;
   top: 0;
   left: 0;
   right: 0;
   bottom: 0;
   background-color: rgba(0, 0, 0, 0.6);
   display: flex;
   align-items: center;
   justify-content: center;
   z-index: 2000;
   padding: 2rem;
   backdrop-filter: blur(4px);
 }

 .success-modal-content {
   background: white;
   border-radius: 20px;
   width: 100%;
   max-width: 500px;
   padding: 3rem 2rem 2rem 2rem;
   text-align: center;
   box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
   border: 1px solid rgba(255, 255, 255, 0.1);
   animation: successSlideIn 0.4s ease-out;
   position: relative;
   overflow: hidden;
 }

 @keyframes successSlideIn {
   from {
     opacity: 0;
     transform: translateY(-30px) scale(0.9);
   }
   to {
     opacity: 1;
     transform: translateY(0) scale(1);
   }
 }

 .success-icon {
   width: 80px;
   height: 80px;
   margin: 0 auto 1.5rem auto;
   background: linear-gradient(135deg, #10b981 0%, #059669 100%);
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   color: white;
   box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
   animation: iconBounce 0.6s ease-out 0.2s both;
 }

 @keyframes iconBounce {
   0%, 20%, 50%, 80%, 100% {
     transform: translateY(0);
   }
   40% {
     transform: translateY(-10px);
   }
   60% {
     transform: translateY(-5px);
   }
 }

 .success-content {
   margin-bottom: 2rem;
 }

 .success-title {
   font-size: 2rem;
   font-weight: 700;
   color: #10b981;
   margin: 0 0 1rem 0;
   font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
 }

 .success-message {
   font-size: 1.1rem;
   color: #6b7280;
   margin: 0;
   line-height: 1.6;
   font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
 }

 .success-close-btn {
   background: linear-gradient(135deg, #10b981 0%, #059669 100%);
   color: white;
   border: none;
   padding: 1rem 2.5rem;
   border-radius: 12px;
   font-size: 1rem;
   font-weight: 600;
   font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
   cursor: pointer;
   transition: all 0.3s ease;
   box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
 }

 .success-close-btn:hover {
   transform: translateY(-2px);
   box-shadow: 0 8px 25px rgba(16, 185, 129, 0.4);
   background: linear-gradient(135deg, #059669 0%, #047857 100%);
 }

 .success-close-btn:active {
   transform: translateY(0);
 }

 /* Responsive Design for Success Modal */
 @media (max-width: 768px) {
   .success-modal-overlay {
     padding: 1rem;
   }

   .success-modal-content {
     max-width: 100%;
     padding: 2rem 1.5rem 1.5rem 1.5rem;
   }

   .success-title {
     font-size: 1.75rem;
   }

   .success-message {
     font-size: 1rem;
   }

   .success-close-btn {
     width: 100%;
     padding: 1rem;
   }
 }

   @media (max-width: 480px) {
    .success-icon {
      width: 60px;
      height: 60px;
    }

    .success-title {
      font-size: 1.5rem;
    }

    .success-message {
      font-size: 0.95rem;
    }
  }

     /* Search and Sort Section Styles */
   .search-section {
     margin-bottom: 1.5rem;
   }

   .sort-section {
     display: flex;
     gap: 1rem;
     margin-bottom: 1.5rem;
   }

   .sort-item {
     flex: 1;
     position: relative;
     background: white;
     border: 2px solid #d4edda;
     border-radius: 12px;
     transition: all 0.3s ease;
     min-height: 48px;
     box-shadow: 0 2px 8px rgba(74, 222, 128, 0.1);
   }

   .sort-header {
     display: flex;
     align-items: center;
     justify-content: space-between;
     padding: 0.75rem 1rem;
     cursor: pointer;
     min-height: 48px;
     background: linear-gradient(135deg, #f8fff9 0%, #d4edda 100%);
     border-radius: 12px;
   }

   .sort-dropdown {
     position: absolute;
     top: 100%;
     left: 0;
     right: 0;
     background: white;
     border: 2px solid #d4edda;
     border-top: none;
     border-radius: 0 0 12px 12px;
     box-shadow: 0 8px 25px rgba(74, 222, 128, 0.2);
     z-index: 100;
     max-height: 400px;
     overflow-y: auto;
     animation: slideIn 0.3s ease-out;
   }

   .sort-options {
     padding: 1rem;
     border-bottom: 1px solid #d4edda;
     background: #f8fff9;
   }

   .sort-option {
     display: flex;
     align-items: center;
     gap: 0.5rem;
     padding: 0.75rem 1rem;
     cursor: pointer;
     border-radius: 8px;
     transition: all 0.3s ease;
     font-size: 0.875rem;
     color: #333;
     margin-bottom: 0.5rem;
   }

   .sort-option:hover {
     background: #4ade80;
     color: white;
     transform: translateX(5px);
     box-shadow: 0 4px 12px rgba(74, 222, 128, 0.3);
   }

   .sort-arrow {
     font-weight: bold;
     color: #4ade80;
     font-size: 1rem;
     transition: all 0.3s ease;
   }

   .sort-option:hover .sort-arrow {
     color: white;
   }

   .sort-values {
     padding: 1rem;
     background: white;
   }

   .sort-values-title {
     font-size: 0.75rem;
     font-weight: 600;
     color: #4ade80;
     margin-bottom: 0.75rem;
     text-transform: uppercase;
     letter-spacing: 0.05em;
     text-align: center;
   }

   .sort-values-list {
     display: flex;
     flex-wrap: wrap;
     gap: 0.5rem;
     justify-content: center;
   }

   .sort-value-item {
     padding: 0.5rem 1rem;
     background: #f8fff9;
     border: 2px solid #d4edda;
     border-radius: 25px;
     font-size: 0.875rem;
     color: #4ade80;
     cursor: pointer;
     transition: all 0.3s ease;
     white-space: nowrap;
     font-weight: 500;
   }

   .sort-value-item:hover {
     background: #4ade80;
     color: white;
     border-color: #4ade80;
     transform: translateY(-2px);
     box-shadow: 0 6px 20px rgba(74, 222, 128, 0.4);
   }

   .sort-item:hover {
     border-color: #4ade80;
     background: #f8fff9;
     transform: translateY(-2px);
     box-shadow: 0 8px 25px rgba(74, 222, 128, 0.2);
   }

   .sort-item:active {
     transform: translateY(0);
   }

   .sort-label {
     font-weight: 600;
     color: #4ade80;
     font-size: 0.875rem;
     font-family: 'Inter', sans-serif;
   }

   .sort-indicator {
     font-weight: bold;
     font-size: 1.25rem;
     color: #4ade80;
     animation: sortPulse 0.3s ease-out;
   }

   .sort-placeholder {
     font-size: 1.25rem;
     color: #9ca3af;
     opacity: 0.7;
   }

   @keyframes sortPulse {
     0% { transform: scale(1); }
     50% { transform: scale(1.2); }
     100% { transform: scale(1); }
   }

   @keyframes slideIn {
     from {
       opacity: 0;
       transform: translateY(-10px);
     }
     to {
       opacity: 1;
       transform: translateY(0);
     }
   }

  .input-group {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    background: white;
    border: 2px solid #e5e7eb;
    border-radius: 12px;
    padding: 0.5rem;
    transition: all 0.2s ease;
  }

  .input-group:focus-within {
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  }

  .search-input {
    flex: 1;
    border: none;
    outline: none;
    font-size: 0.875rem;
    background: transparent;
  }

  .clear-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 0.25rem;
    border-radius: 4px;
    color: #6b7280;
    transition: all 0.2s ease;
  }

  .clear-btn:hover {
    background: #f3f4f6;
    color: #374151;
  }

  /* Responsive for sort section */
  @media (max-width: 768px) {
    .sort-section {
      flex-direction: column;
      gap: 0.75rem;
    }
    
    .sort-item {
      min-height: 44px;
    }

    .sort-dropdown {
      position: fixed;
      top: auto;
      left: 1rem;
      right: 1rem;
      max-height: 60vh;
    }
  }

  /* Click outside to close dropdown */
  .sort-item:focus-within .sort-dropdown {
    display: block;
  }
  </style>
