const API = "http://localhost:8080/api/phieu-giam-gia-management";

export const fetchAllPhieuGiamGia = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch phieu giam gia");
    }
    return response.json();
}
export const fetchPhieuGiamGiaById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch phieu giam gia");
    }
    return response.json();
}
export const fetchCreatePhieuGiamGia = async (phieuGiamGia) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(phieuGiamGia)
    });
}
export const fetchUpdatePhieuGiamGia = async (id, phieuGiamGia) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(phieuGiamGia)
    });
}
export const fetchUpdateStatusPhieuGiamGia = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
    });
}