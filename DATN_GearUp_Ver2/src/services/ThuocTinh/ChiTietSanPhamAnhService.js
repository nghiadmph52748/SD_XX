const API = "http://localhost:8080/api/chi-tiet-san-pham-anh-management";

export const fetchAllChiTietSanPhamAnh = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch product detail images");
    }
    return res.json();
}

export const fetchChiTietSanPhamAnhByChiTietSanPham = async (idChiTietSanPham) => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch product detail images");
    }
    const data = await res.json();
    
    // Lọc theo idChiTietSanPham
    const filteredData = data.data.filter(item => 
        item.idChiTietSanPham === idChiTietSanPham && !item.deleted
    );
    
    return { data: filteredData };
}

export const fetchCreateChiTietSanPhamAnh = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to create product detail image");
    }
    return res.json();
}

export const fetchUpdateChiTietSanPhamAnh = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to update product detail image");
    }
    return res.json();
}

export const fetchDeleteChiTietSanPhamAnh = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        throw new Error("Failed to delete product detail image");
    }
    return res.json();
}
