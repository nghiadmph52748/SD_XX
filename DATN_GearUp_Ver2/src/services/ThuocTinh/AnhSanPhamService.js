const API = "http://localhost:8080/api/anh-san-pham-management";

export const fetchAllAnhSanPham = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch product images");
    }
    return res.json();
}

export const fetchOneAnhSanPham = async (id) => {
    // Use the existing images endpoint with filtering
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch product image");
    }
    return res.json();
}

export const fetchPagingAnhSanPham = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&limit=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated product images");
    }
    return res.json();
}

export const fetchCreateAnhSanPham = async (formData) => {
    // Use the existing upload endpoint for products
    const res = await fetch(`${API}/add`, {
        method: "POST",
        body: formData, // Không cần Content-Type header khi gửi FormData
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to create product image");
    }
    return res.json();
}

export const fetchUpdateAnhSanPham = async (id, formData) => {
    // The backend always expects FormData for updates
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        body: formData, // Always send FormData
    });
    
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update product image");
    }
    return res.json();
}

export const fetchUpdateStatusAnhSanPham = async (id) => {
    // Use the existing delete endpoint for status update
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update product image status");
    }
    return res.json();
}