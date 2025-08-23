const API = "http://localhost:8080/api/anh-san-pham-management";

export const fetchAllAnhSanPham = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch product images");
    }
    return res.json();
}
export const fetchOneAnhSanPham = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch product image");
    }
    return res.json();
}
export const fetchPagingAnhSanPham = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated product images");
    }
    return res.json();
}
export const fetchCreateAnhSanPham = async (formData) => {
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
export const fetchUpdateAnhSanPham = async (id, data) => {
    let url = `${API}/update/${id}`;
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
}