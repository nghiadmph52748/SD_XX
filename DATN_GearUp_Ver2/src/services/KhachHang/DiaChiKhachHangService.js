const API = "http://localhost:8080/api/dia-chi-khach-hang-management";

export const fetchAllDiaChiKhachHang = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch dia chi khach hang");
    }
    return response.json();
}
export const fetchGetDiaChiKhachHangById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch dia chi khach hang");
    }
    return response.json();
}
export const fetchCreateDiaChiKhachHang = async (data) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}
export const fetchUpdateDiaChiKhachHang = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}
export const fetchUpdateStatusDiaChiKhachHang = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
}
