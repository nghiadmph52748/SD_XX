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

    console.log(`🔄 Đang gọi API tạo chi tiết sản phẩm với dữ liệu:`, JSON.stringify(requestData, null, 2));

    const res = await fetch(`${API}/add`, {
        method: "POST",
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
        throw new Error(`Failed to create product detail: ${res.status} - ${errorText}`);
    }

    const responseData = await res.json();
    console.log(`✅ Response từ tạo chi tiết sản phẩm:`, responseData);

    // Kiểm tra format response từ backend
    if (responseData && typeof responseData === 'object') {
        // Backend trả về format: { data: id, message: "..." }
        if (responseData.data !== undefined) {
            console.log(`📝 Response từ backend - data:`, responseData.data);
            console.log(`📝 Response từ backend - message:`, responseData.message);

            // Nếu data là ID (number)
            if (typeof responseData.data === 'number' && responseData.data > 0) {
                console.log(`✅ Chi tiết sản phẩm được tạo với ID: ${responseData.data}`);
                // Trả về object chứa ID để tương thích với frontend
                return {
                    id: responseData.data,
                    message: responseData.message
                };
            }

            // Nếu data là null hoặc không phải ID
            if (responseData.data === null || responseData.data === 0) {
                console.error(`❌ Backend trả về ID null hoặc 0:`, responseData);
                return null;
            }

            // Nếu data là object khác
            return responseData.data;
        }

        // Nếu response trực tiếp là object chứa ID
        if (responseData.id) {
            console.log(`📝 Response trực tiếp chứa ID:`, responseData.id);
            return responseData;
        }
    }

    // Fallback: trả về response gốc
    console.log(`📝 Trả về response gốc:`, responseData);
    return responseData;
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