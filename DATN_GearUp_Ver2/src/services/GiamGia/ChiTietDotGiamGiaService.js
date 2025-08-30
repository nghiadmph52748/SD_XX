const API = "http://localhost:8080/api/chi-tiet-dot-giam-gia-management";

export const fetchAllChiTietDotGiamGia = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch chi tiet dot giam gia");
    }
    return response.json();
}
export const fetchChiTietDotGiamGiaById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch chi tiet dot giam gia");
    }
    return response.json();
}
export const fetchCreateChiTietDotGiamGia = async (chiTietDotGiamGia) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(chiTietDotGiamGia)
    });
}
export const fetchUpdateChiTietDotGiamGia = async (id, chiTietDotGiamGia) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(chiTietDotGiamGia)
    });
}
export const fetchUpdateStatusChiTietDotGiamGia = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
    });
}