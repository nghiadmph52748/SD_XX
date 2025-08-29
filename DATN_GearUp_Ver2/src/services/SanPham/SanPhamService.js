const API = "http://localhost:8080/api/san-pham-management";

export const fetchPageAllSanPham = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch products");
    }
    return res.json();
};

export const fetchAllSanPham = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch products");
    }
    return res.json();
}

export const fetchCreateSanPham = async (data) => {
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        idNhaSanXuat: data.idNhaSanXuat,
        idXuatXu: data.idXuatXu,
        tenSanPham: data.tenSanPham,
        trangThai: data.trangThai || true,
        deleted: data.deleted || false,
        createAt: data.createAt || new Date().toISOString().split('T')[0], // Format: YYYY-MM-DD
        createBy: data.createBy || 1, // Default value
        updateAt: data.updateAt || new Date().toISOString().split('T')[0],
        updateBy: data.updateBy || 1 // Default value
    };

    const response = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to create product");
    }
    return response.json();
}

export const fetchUpdateSanPham = async (id, data) => {
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        idNhaSanXuat: data.idNhaSanXuat,
        idXuatXu: data.idXuatXu,
        tenSanPham: data.tenSanPham,
        trangThai: data.trangThai,
        deleted: data.deleted,
        updateAt: new Date().toISOString().split('T')[0],
        updateBy: data.updateBy || 1
    };

    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
    });
    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to update product");
    }
    return res.json();
}

export const fetchUpdateStatusSanPham = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        throw new Error("Failed to update product status");
    }
    return res.json();
}