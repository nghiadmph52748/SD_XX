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
    const res = await fetch(`${API}/paging?page=${page}&limit=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated product images");
    }
    return res.json();
}

export const fetchCreateAnhSanPham = async (formData) => {
    // Kiểm tra nếu formData là FormData (upload file) hoặc object thường
    if (formData instanceof FormData) {
        // Upload file - không cần set Content-Type, browser sẽ tự động set
        const res = await fetch(`${API}/add`, {
            method: "POST",
            body: formData
        });
        if (!res.ok) {
            const errorData = await res.json();
            throw new Error(errorData.message || "Failed to create product image");
        }
        return res.json();
    } else {
        // Tạo ảnh với dữ liệu JSON thông thường - cần chuyển thành FormData
        const formDataForUpload = new FormData();

        // Tạo file ảnh từ đường dẫn (nếu có)
        if (formData.duongDanAnh) {
            // Nếu có đường dẫn ảnh, tạo file từ đó
            try {
                const response = await fetch(formData.duongDanAnh);
                const blob = await response.blob();
                const file = new File([blob], formData.duongDanAnh.split('/').pop(), { type: 'image/*' });
                formDataForUpload.append("file", file);
            } catch (error) {
                console.error("Error creating file from URL:", error);
                throw new Error("Không thể tạo file từ đường dẫn ảnh");
            }
        }

        formDataForUpload.append("loaiAnh", formData.loaiAnh || "PRODUCT");
        if (formData.moTa) {
            formDataForUpload.append("moTa", formData.moTa);
        }
        if (formData.deleted !== undefined) {
            formDataForUpload.append("deleted", formData.deleted);
        }

        const res = await fetch(`${API}/add`, {
            method: "POST",
            body: formDataForUpload
        });
        if (!res.ok) {
            const errorData = await res.json();
            throw new Error(errorData.message || "Failed to create product image");
        }
        return res.json();
    }
}

export const fetchUpdateAnhSanPham = async (id, formData) => {
    // Chuẩn bị dữ liệu theo backend entity
    const requestData = {
        duongDanAnh: formData.duongDanAnh,
        loaiAnh: formData.loaiAnh,
        moTa: formData.moTa,
        trangThai: formData.trangThai,
        deleted: formData.deleted,
        updateAt: new Date().toISOString().split('T')[0],
        updateBy: formData.updateBy || 1
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
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to update product image status");
    }
    return res.json();
}