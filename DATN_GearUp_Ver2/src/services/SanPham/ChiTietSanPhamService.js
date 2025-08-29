const API = "http://localhost:8080/api/chi-tiet-san-pham-management";

export const fetchAllChiTietSanPham = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch product details");
    }
    return res.json();
}

export const fetchAllChiTietSanPhamBySanPhamId = async (id) => {
    const res = await fetch(`${API}/list/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch product details");
    }
    return res.json();
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
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        idSanPham: data.idSanPham,
        idMauSac: data.idMauSac,
        idKichThuoc: data.idKichThuoc,
        idDeGiay: data.idDeGiay,
        idChatLieu: data.idChatLieu,
        idTrongLuong: data.idTrongLuong,
        soLuong: data.soLuong,
        giaBan: data.giaBan,
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
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
    });
    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to create product detail");
    }
    return res.json();
}

export const fetchUpdateChiTietSanPham = async (id, data) => {
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        idSanPham: data.idSanPham,
        idMauSac: data.idMauSac,
        idKichThuoc: data.idKichThuoc,
        idDeGiay: data.idDeGiay,
        idChatLieu: data.idChatLieu,
        idTrongLuong: data.idTrongLuong,
        soLuong: data.soLuong,
        giaBan: data.giaBan,
        trangThai: data.trangThai,
        deleted: data.deleted,
        updateAt: new Date().toISOString().split('T')[0],
        updateBy: data.updateBy || 1
    };

    console.log(`🔄 Đang gọi API update chi tiết sản phẩm ID: ${id}`);
    console.log(`📤 Dữ liệu gửi đi:`, JSON.stringify(requestData, null, 2));

    try {
        const res = await fetch(`${API}/update/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        });

        console.log(`📥 Response status: ${res.status}`);
        console.log(`📥 Response headers:`, res.headers);

        if (!res.ok) {
            const errorText = await res.text();
            console.error(`❌ API Error ${res.status}:`, errorText);
            throw new Error(`Failed to update product detail: ${res.status} - ${errorText}`);
        }

        const responseData = await res.json();
        console.log(`✅ Update thành công:`, responseData);
        return responseData;
    } catch (error) {
        console.error("❌ Error in fetchUpdateChiTietSanPham:", error);
        throw error;
    }
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
    return res.json();
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
    return res.json();
}