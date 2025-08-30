const API = "http://localhost:8080/api/chi-tiet-san-pham-management";

export const fetchAllChiTietSanPham = async () => {
    try {
        console.log("Đang gọi API:", `${API}/playlist`);
        const res = await fetch(`${API}/playlist`);
        console.log("Response status:", res.status);
        
        if (!res.ok) {
            const errorText = await res.text();
            console.error("API Error:", errorText);
            throw new Error(`Failed to fetch product details: ${res.status} ${res.statusText}`);
        }
        
        const data = await res.json();
        console.log("API Response data:", data);
        return data;
    } catch (error) {
        console.error("Error in fetchAllChiTietSanPham:", error);
        throw error;
    }
}
export const fetchOneChiTietSanPham = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch product detail");
    }
    return res.json();
}
export const fetchPagingChiTietSanPham = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated product details");
    }
    return res.json();
}
export const fetchCreateChiTietSanPham = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to create product detail");
    }
    return res.json();
}
export const fetchUpdateChiTietSanPham = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to update product detail");
    }
    return res.json();
}
export const fetchUpdateStatusChiTietSanPham = async (id) => {
    console.log("Đang gọi API cập nhật trạng thái chi tiết sản phẩm ID:", id);
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    console.log("Response status:", res.status);
    if (!res.ok) {
        const errorText = await res.text();
        console.error("API Error:", errorText);
        throw new Error("Failed to update product detail status");
    }
    console.log("Cập nhật trạng thái thành công cho ID:", id);
}

export const fetchRestoreStatusChiTietSanPham = async (id) => {
    console.log("Đang gọi API khôi phục trạng thái chi tiết sản phẩm ID:", id);
    const res = await fetch(`${API}/restore/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    console.log("Response status:", res.status);
    if (!res.ok) {
        const errorText = await res.text();
        console.error("API Error:", errorText);
        throw new Error("Failed to restore product detail status");
    }
    console.log("Khôi phục trạng thái thành công cho ID:", id);
}