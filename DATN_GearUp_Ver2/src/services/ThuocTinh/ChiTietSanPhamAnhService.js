const API = "http://localhost:8080/api/chi-tiet-san-pham-anh-management";

export const fetchAllChiTietSanPhamAnh = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch product image details");
    }
    return res.json();
}

export const fetchAllChiTietSanPhamAnhByChiTietSanPhamId = async (id) => {
    const res = await fetch(`${API}/list/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch product image details");
    }
    return res.json();
}

export const fetchOneChiTietSanPhamAnh = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch product image detail");
    }
    return res.json();
}

export const fetchPagingChiTietSanPhamAnh = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated product image details");
    }
    return res.json();
}

export const fetchCreateChiTietSanPhamAnh = async (data) => {
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        idChiTietSanPham: data.idChiTietSanPham,
        idAnhSanPham: data.idAnhSanPham,
        trangThai: data.trangThai || true,
        deleted: data.deleted || false,
        createAt: new Date().toISOString().split('T')[0], // Format: YYYY-MM-DD
        createBy: data.createBy || 1, // Default value
        updateAt: new Date().toISOString().split('T')[0],
        updateBy: data.updateBy || 1 // Default value
    };

    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
    });
    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to create product image detail");
    }
    return res.json();
}

export const fetchUpdateChiTietSanPhamAnh = async (id, data) => {
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        idChiTietSanPham: data.idChiTietSanPham,
        idAnhSanPham: data.idAnhSanPham,
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
        throw new Error(errorData.message || "Failed to update product image detail");
    }
    return res.json();
}

export const fetchCreateMultipleChiTietSanPhamAnh = async (data) => {
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        idChiTietSanPham: data.idChiTietSanPham,
        idAnhSanPhamList: data.idAnhSanPhamList || [],
        trangThai: data.trangThai || true,
        deleted: data.deleted || false,
        createAt: new Date().toISOString().split('T')[0], // Format: YYYY-MM-DD
        createBy: data.createBy || 1, // Default value
        updateAt: new Date().toISOString().split('T')[0],
        updateBy: data.updateBy || 1 // Default value
    };

    const res = await fetch(`${API}/add-multiple`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
    });
    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to create multiple product image details");
    }
    return res.json();
}

export const fetchUpdateStatusChiTietSanPhamAnh = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT"
        // Không cần headers vì không có body
    });
    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to update product image detail status");
    }
    return res.json();
}

export const fetchDeleteChiTietSanPhamAnh = async (id) => {
    // Backend không có endpoint DELETE, sử dụng update status để soft delete
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT"
    });
    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to delete product image detail");
    }
    return res.json();
}
