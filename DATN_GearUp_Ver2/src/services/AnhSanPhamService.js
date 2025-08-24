const API = "http://localhost:8080/api/upload";

export const fetchAllAnhSanPham = async () => {
    const res = await fetch(`${API}/images`);
    if (!res.ok) {
        throw new Error("Failed to fetch product images");
    }
    return res.json();
}

export const fetchOneAnhSanPham = async (id) => {
    // Use the existing images endpoint with filtering
    const res = await fetch(`${API}/images?id=${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch product image");
    }
    return res.json();
}

export const fetchPagingAnhSanPham = async (page, size) => {
    const res = await fetch(`${API}/images?page=${page}&limit=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated product images");
    }
    return res.json();
}

export const fetchCreateAnhSanPham = async (formData) => {
    // Use the existing upload endpoint for products
    const res = await fetch(`${API}/product/1/variant/1`, {
        method: "POST",
        body: formData, // Không cần Content-Type header khi gửi FormData
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to create product image");
    }
    return res.json();
}

export const fetchUpdateAnhSanPham = async (id, data) => {
    // Use the existing update endpoint in upload.js
    let url = `${API}/images/${id}`;
    let options = {
        method: "PUT",
    };
    
    // Nếu data là FormData (có file), gửi FormData
    if (data instanceof FormData) {
        options.body = data;
    } else {
        // Nếu data là object thường, gửi JSON
        options.headers = {
            "Content-Type": "application/json",
        };
        options.body = JSON.stringify(data);
    }
    
    const res = await fetch(url, options);
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update product image");
    }
    return res.json();
}

export const fetchUpdateStatusAnhSanPham = async (id) => {
    // Use the existing delete endpoint for status update
    const res = await fetch(`${API}/image/${id}`, {
        method: "DELETE",
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