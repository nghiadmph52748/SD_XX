const API = "http://localhost:8080/api/trang-thai-don-hang-management";

export const fetchAllTrangThaiDonHang = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch trang thai don hang");
    }
    return response.json();
}
export const fetchGetTrangThaiDonHangById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch trang thai don hang");
    }
    return response.json();
}
export const fetchCreateTrangThaiDonHang = async (data) => {
       await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
}
export const fetchUpdateTrangThaiDonHang = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
}
export const fetchUpdateStatusTrangThaiDonHang = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    })
}