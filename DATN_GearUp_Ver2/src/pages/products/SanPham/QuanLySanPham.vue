<template>
  <div class="them-san-pham-page">
    <!-- Breadcrumb Navigation -->
    <div class="breadcrumb-nav">
      <RouterLink :to="`/products`" class="product-link">
        <span class="product-text">Sản phẩm</span>
      </RouterLink>
    </div>

    <div class="product-management">
      <!-- Modern Filter Section -->
      <div class="filter-section">
        <div class="filter-card">
          <div class="filter-content">
            <!-- Search và Actions cùng dòng -->
            <div class="search-actions-row">
              <div class="search-section">
                <input
                  v-model="searchQuery"
                  type="text"
                  placeholder="Tìm kiếm theo mã SP, tên SP"
                  class="search-input"
                />
                <button
                  v-if="searchQuery"
                  @click="searchQuery = ''"
                  class="clear-btn"
                ></button>
              </div>
              <div class="header-actions">
                <button class="btn-export" @click="showExportConfirm">
                  Xuất Excel
                </button>
                <router-link to="/products/add" class="btn-export">
                  Thêm sản phẩm
                </router-link>
              </div>
            </div>
            <div class="filter-group">
              <label class="filter-label"> Trạng thái: </label>
              <div class="radio-group">
                <label class="radio-item">
                  <input
                    type="radio"
                    v-model="selectedTrangThai"
                    value=""
                    name="trangThai"
                  />
                  <span class="radio-text">Tất cả trạng thái</span>
                </label>
                <label class="radio-item">
                  <input
                    type="radio"
                    v-model="selectedTrangThai"
                    value="true"
                    name="trangThai"
                  />
                  <span class="radio-text">Đang bán</span>
                </label>
                <label class="radio-item">
                  <input
                    type="radio"
                    v-model="selectedTrangThai"
                    value="false"
                    name="trangThai"
                  />
                  <span class="radio-text">Tạm ngừng bán</span>
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Products Table -->
    <div class="card">
      <div class="card-header">
        <div class="table-header-content">
          <h3 class="table-title">Danh sách sản phẩm</h3>
          <div class="table-actions">
            <button
              v-if="hasCheckedProducts"
              @click="showUpdateConfirm"
              class="btn-save-all"
              title="Cập nhật tất cả sản phẩm đã chọn"
            >
              Hoàn thành cập nhật ({{ checkedProductsCount }})
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <table class="table">
          <thead>
            <tr>
              <th>
                <input
                  type="checkbox"
                  :checked="isAllSelected"
                  :indeterminate="isIndeterminate"
                  @change="toggleSelectAll"
                  title="Chọn tất cả sản phẩm"
                />
              </th>
              <th>STT</th>
              <th>Mã sản phẩm</th>
              <th>Tên sản phẩm</th>
              <th>Số lượng biến thể</th>
              <th>Ngày tạo</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody v-if="paginatedProducts.length > 0">
            <tr
              v-for="(product, i) in paginatedProducts"
              :key="i"
              :class="{ 'editing-row': editingProducts.has(product.id) }"
            >
              <td>
                <input
                  type="checkbox"
                  :checked="selectedProducts.includes(product.id)"
                  @change="toggleProductSelection(product.id)"
                  @click.stop
                />
              </td>
              <td>{{ startIndex + i + 1 }}</td>
              <td>{{ product.maSanPham }}</td>
              <td>
                <div v-if="editingProducts.has(product.id)" class="inline-edit">
                  <input
                    v-model="editingProducts.get(product.id).tenSanPham"
                    class="edit-input"
                    :data-product-id="product.id"
                    @keyup.enter="saveInlineEdit(product.id)"
                    @keyup.esc="cancelInlineEdit(product.id)"
                    ref="editInput"
                  />
                </div>
                <div v-else class="product-cell">
                  {{ product.tenSanPham }}
                </div>
              </td>
              <td style="text-align: center !important">
                {{ product.soLuongBienThe }}
              </td>
              <td>{{ product.createAt }}</td>
              <td>
                <div v-if="editingProducts.has(product.id)" class="inline-edit">
                  <select
                    v-model="editingProducts.get(product.id).trangThai"
                    class="edit-select"
                    :data-product-id="product.id"
                    @keyup.esc="cancelInlineEdit(product.id)"
                  >
                    <option :value="true">Đang bán</option>
                    <option :value="false">Tạm ngừng bán</option>
                  </select>
                </div>
                <div v-else class="product-cell">
                  {{ product.trangThai ? "Đang bán" : "Tạm ngừng bán" }}
                </div>
              </td>
              <td>
                <div class="action-buttons">
                  <router-link :to="`/products/details/${product.id}`">
                    <button class="btn btn-secondary">👁️</button>
                  </router-link>
                </div>
              </td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr>
              <td colspan="8" class="text-center">Không có dữ liệu</td>
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

    <!-- Success Notification Modal -->
    <div
      v-if="showSuccessModal"
      class="success-modal-overlay"
      @click="closeSuccessModal"
    >
      <div class="success-modal-content" @click.stop>
        <div class="success-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="currentColor">
            <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
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

    <!-- Confirm Modal cho Hoàn thành cập nhật -->
    <div
      v-if="showConfirmUpdateModal"
      class="custom-confirm-dialog"
      @click="closeConfirmUpdateModal"
    >
      <div class="confirm-content" @click.stop>
        <h3>Xác nhận cập nhật</h3>
        <p>
          Bạn có chắc chắn muốn cập nhật {{ checkedProductsCount }} sản phẩm đã
          chọn?
        </p>
        <div class="confirm-buttons">
          <button class="btn btn-secondary" @click="closeConfirmUpdateModal">
            Hủy
          </button>
          <button class="btn btn-primary" @click="confirmUpdateProducts">
            Xác nhận
          </button>
        </div>
      </div>
    </div>

    <!-- Confirm Modal cho Xuất Excel -->
    <div
      v-if="showConfirmExportModal"
      class="custom-confirm-dialog"
      @click="closeConfirmExportModal"
    >
      <div class="confirm-content" @click.stop>
        <h3>Xác nhận xuất Excel</h3>
        <p>Bạn có chắc chắn muốn xuất danh sách sản phẩm ra file Excel?</p>
        <div class="confirm-buttons">
          <button class="btn btn-secondary" @click="closeConfirmExportModal">
            Hủy
          </button>
          <button class="btn btn-primary" @click="confirmExportExcel">
            Xác nhận
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, onUnmounted, ref } from "vue";
import {
  fetchAllSanPham,
  fetchUpdateSanPham,
} from "../../../services/SanPham/SanPhamService";
import "../../../styles/cssSanPham/popupAttributes.css";
import "../../../styles/cssSanPham/productManagement.css";
import "../../../styles/cssSanPham/productsUnified.css";
import "../../../styles/cssSanPham/quanLySanPham.css";
import { exportToExcel } from "../../../utils/xuatExcel";

const searchQuery = ref("");
const selectedTrangThai = ref("");
const currentPage = ref(1);
const itemsPerPage = ref(10);
const showSuccessModal = ref(false);
const successMessage = ref("");
const products = ref([]);

// Inline editing variables
const editingProducts = ref(new Map()); // Map để lưu nhiều sản phẩm đang edit
const selectedProducts = ref([]);
const originalProducts = ref(new Map()); // Map để lưu dữ liệu gốc của nhiều sản phẩm

// Modal confirm variables
const showConfirmUpdateModal = ref(false);
const showConfirmExportModal = ref(false);
const fetch = async () => {
  await fetchAllData();
};
const fetchAllData = async () => {
  try {
    const res = await fetchAllSanPham();
    products.value = res.data;
  } catch (error) {
    console.error("Error fetching products:", error);
  }
};

const fetchUpdate = async (id, data) => {
  try {
    await fetchUpdateSanPham(id, data);
  } catch (error) {
    console.error("Error updating product:", error);
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
        product.tenSanPham?.toLowerCase().includes(searchTerm)
    );
  }

  if (selectedTrangThai.value !== "") {
    const statusValue = selectedTrangThai.value === "true";
    filtered = filtered.filter((product) => product.trangThai === statusValue);
  }

  return filtered;
});

const paginatedProducts = computed(() => {
  return filteredProducts.value.slice(startIndex.value, endIndex.value);
});

const totalProducts = computed(() => {
  return filteredProducts.value.length;
});

const totalPages = computed(() =>
  Math.ceil(totalProducts.value / itemsPerPage.value)
);

const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value);
const endIndex = computed(() =>
  Math.min(startIndex.value + itemsPerPage.value, totalProducts.value)
);

// Checkbox selection computed
const hasCheckedProducts = computed(() => {
  return selectedProducts.value.length > 0;
});

const checkedProductsCount = computed(() => {
  return selectedProducts.value.length;
});

const isAllSelected = computed(() => {
  return (
    paginatedProducts.value.length > 0 &&
    paginatedProducts.value.every((product) =>
      selectedProducts.value.includes(product.id)
    )
  );
});

const isIndeterminate = computed(() => {
  const selectedCount = paginatedProducts.value.filter((product) =>
    selectedProducts.value.includes(product.id)
  ).length;
  // Khi có ít nhất 1 sản phẩm được chọn nhưng chưa phải tất cả
  return selectedCount > 0 && selectedCount < paginatedProducts.value.length;
});

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
const closeSuccessModal = () => {
  showSuccessModal.value = false;
  successMessage.value = "";
};
const exportData = () => {
  alert("Xuất báo cáo sản phẩm");
};

const exportProductsToExcel = () => {
  try {
    const headerMapping = {
      id: "ID",
      tenSanPham: "Tên sản phẩm",
      idNhaSanXuat: "Nhà sản xuất",
      idXuatXu: "Xuất xứ",
      soLuongBienThe: "Số lượng biến thể",
      trangThai: "Trạng thái",
      createAt: "Ngày tạo",
      updateAt: "Ngày cập nhật",
    };

    const filteredData = paginatedProducts.value.map((item) => ({
      id: item.id || "N/A",
      tenSanPham: item.tenSanPham || "N/A",
      idNhaSanXuat: item.idNhaSanXuat || "N/A",
      idXuatXu: item.idXuatXu || "N/A",
      soLuongBienThe: item.soLuongBienThe || "N/A",
      trangThai: item.trangThai === true ? "Đang bán" : "Tạm ngừng bán",
      createAt: item.createAt
        ? new Date(item.createAt).toLocaleDateString("vi-VN")
        : "N/A",
      updateAt: item.updateAt
        ? new Date(item.updateAt).toLocaleDateString("vi-VN")
        : "N/A",
    }));

    const result = exportToExcel(
      filteredData,
      "Product_Management",
      "Danh sách sản phẩm",
      headerMapping
    );

    // Silent export - no alerts
  } catch (error) {
    console.error("Error exporting to Excel:", error);
  }
};

const clearFilters = () => {
  searchQuery.value = "";
  selectedTrangThai.value = "";
  currentPage.value = 1;
};

// Inline editing functions
const toggleEditMode = (product) => {
  if (editingProducts.value.has(product.id)) {
    // Nếu đang edit thì tắt edit mode cho sản phẩm này
    editingProducts.value.delete(product.id);
    originalProducts.value.delete(product.id);
  } else {
    // Nếu chưa edit thì bật edit mode cho sản phẩm này
    editingProducts.value.set(product.id, { ...product });
    originalProducts.value.set(product.id, { ...product });
    // Focus on input after next tick
    nextTick(() => {
      const editInput = document.querySelector(
        `[data-product-id="${product.id}"]`
      );
      if (editInput) editInput.focus();
    });
  }
};

const startEdit = (product) => {
  editingProducts.value.set(product.id, { ...product });
  originalProducts.value.set(product.id, { ...product });
  // Focus on input after next tick
  nextTick(() => {
    const editInput = document.querySelector(
      `[data-product-id="${product.id}"]`
    );
    if (editInput) editInput.focus();
  });
};

const saveInlineEdit = async (productId) => {
  const editingProduct = editingProducts.value.get(productId);
  if (!editingProduct) return;

  try {
    const updatedData = {
      tenSanPham: editingProduct.tenSanPham,
      idNhaSanXuat: editingProduct.idNhaSanXuat,
      idXuatXu: editingProduct.idXuatXu,
      trangThai: editingProduct.trangThai,
      deleted: false,
      createAt: editingProduct.createAt,
      updateAt: new Date().toISOString(),
    };

    await fetchUpdate(productId, updatedData);

    // Reset editing state cho sản phẩm này
    editingProducts.value.delete(productId);
    originalProducts.value.delete(productId);

    // Refresh data
    await fetchAllData();
  } catch (error) {
    console.error("Error saving inline edit:", error);
  }
};

const cancelInlineEdit = (productId) => {
  editingProducts.value.delete(productId);
  originalProducts.value.delete(productId);
};

// Batch update function
const saveAllCheckedProducts = async () => {
  if (editingProducts.value.size === 0) return;

  // Confirm before updating
  const productNames = Array.from(editingProducts.value.values())
    .map((p) => p.tenSanPham)
    .join(", ");
  showConfirmUpdateModal.value = true;
  return;

  try {
    // Cập nhật tất cả sản phẩm đang edit
    const updatePromises = Array.from(editingProducts.value.entries()).map(
      ([productId, editingProduct]) => {
        const updatedData = {
          tenSanPham: editingProduct.tenSanPham,
          idNhaSanXuat: editingProduct.idNhaSanXuat,
          idXuatXu: editingProduct.idXuatXu,
          trangThai: editingProduct.trangThai,
          deleted: false,
          createAt: editingProduct.createAt,
          updateAt: new Date().toISOString(),
        };
        return fetchUpdate(productId, updatedData);
      }
    );

    await Promise.all(updatePromises);

    // Reset editing state cho tất cả sản phẩm
    editingProducts.value.clear();
    originalProducts.value.clear();

    // Refresh data
    await fetchAllData();
  } catch (error) {
    console.error("Error updating products:", error);
  }
};

// Checkbox selection functions
const toggleProductSelection = (productId) => {
  const index = selectedProducts.value.indexOf(productId);
  if (index > -1) {
    // Bỏ chọn - xóa khỏi selectedProducts và tắt edit mode
    selectedProducts.value.splice(index, 1);
    editingProducts.value.delete(productId);
    originalProducts.value.delete(productId);
  } else {
    // Chọn - thêm vào selectedProducts và bật edit mode
    selectedProducts.value.push(productId);
    const product = products.value.find((p) => p.id === productId);
    if (product) {
      editingProducts.value.set(productId, { ...product });
      originalProducts.value.set(productId, { ...product });
    }
  }
};

const toggleSelectAll = () => {
  // Logic cho trường hợp không có sản phẩm trong edit mode
  if (selectedProducts.value.length === paginatedProducts.value.length) {
    // Bỏ chọn tất cả
    selectedProducts.value = [];
    editingProducts.value.clear();
    originalProducts.value.clear();
  } else {
    // Chọn tất cả
    selectedProducts.value = paginatedProducts.value.map(
      (product) => product.id
    );
    // Đưa tất cả sản phẩm vào chế độ edit
    paginatedProducts.value.forEach((product) => {
      editingProducts.value.set(product.id, { ...product });
      originalProducts.value.set(product.id, { ...product });
    });
  }
};

// Function để xử lý click ra ngoài
const handleClickOutside = (event) => {
  if (editingProducts.value.size > 0) {
    // Kiểm tra xem click có phải vào checkbox không
    const isCheckboxClick = event.target.type === "checkbox";

    // Nếu không phải click vào checkbox và không phải click vào input/select đang edit
    if (!isCheckboxClick && !event.target.closest(".inline-edit")) {
      // Chỉ tắt edit mode nếu không có sản phẩm nào được chọn
      // Nếu có sản phẩm được chọn thì giữ nguyên edit mode để user có thể tiếp tục sửa
      if (selectedProducts.value.length === 0) {
        editingProducts.value.clear();
        originalProducts.value.clear();
      }
    }
  }
};

const showUpdateConfirm = () => {
  showConfirmUpdateModal.value = true;
};

const closeConfirmUpdateModal = () => {
  // Chỉ đóng popup, không reset gì cả
  // Giữ nguyên thông tin đã thay đổi và checkbox vẫn được tích
  // User có thể tiếp tục sửa mà không cần tích lại checkbox
  showConfirmUpdateModal.value = false;
};

const showExportConfirm = () => {
  showConfirmExportModal.value = true;
};

const closeConfirmExportModal = () => {
  showConfirmExportModal.value = false;
};

const confirmUpdateProducts = async () => {
  closeConfirmUpdateModal();

  try {
    // Cập nhật tất cả sản phẩm đang edit
    const updatePromises = Array.from(editingProducts.value.entries()).map(
      ([productId, editingProduct]) => {
        const updatedData = {
          tenSanPham: editingProduct.tenSanPham,
          idNhaSanXuat: editingProduct.idNhaSanXuat,
          idXuatXu: editingProduct.idXuatXu,
          trangThai: editingProduct.trangThai,
          deleted: false,
          createAt: editingProduct.createAt,
          updateAt: new Date().toISOString(),
        };
        return fetchUpdate(productId, updatedData);
      }
    );

    await Promise.all(updatePromises);

    // Reset editing state cho tất cả sản phẩm
    editingProducts.value.clear();
    originalProducts.value.clear();

    // Refresh data
    await fetchAllData();
  } catch (error) {
    console.error("Error updating products:", error);
  }
};

const confirmExportExcel = () => {
  closeConfirmExportModal();
  exportProductsToExcel();
};

onMounted(() => {
  fetch();

  // Thêm event listener để click ra ngoài sẽ dừng edit
  document.addEventListener("click", handleClickOutside);
});

// Cleanup event listener khi component unmount
onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>
