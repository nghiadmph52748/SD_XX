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