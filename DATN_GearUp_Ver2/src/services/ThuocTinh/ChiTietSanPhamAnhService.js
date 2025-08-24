const API = "http://localhost:8080/api/chi-tiet-san-pham-anh-management";

export const fetchAllChiTietSanPhamAnh = async () => {
    console.log('Fetching all ChiTietSanPhamAnh...');
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch product detail images");
    }
    const result = await res.json();
    console.log('Fetched ChiTietSanPhamAnh result:', result);
    return result;
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
    console.log('Creating ChiTietSanPhamAnh with data:', data);
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
    const result = await res.json();
    console.log('Created ChiTietSanPhamAnh result:', result);
    return result;
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
    console.log('Deleting ChiTietSanPhamAnh with ID:', id);
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        throw new Error("Failed to delete product detail image");
    }
    const result = await res.json();
    console.log('Deleted ChiTietSanPhamAnh result:', result);
    return result;
}
