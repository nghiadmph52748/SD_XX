const API = "http://localhost:8080/api/hoa-don-chi-tiet-management";

export const fetchAllHoaDonChiTiet = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch hoa don chi tiet");
    }
    return response.json();
}
export const fetchGetHoaDonChiTietById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch hoa don chi tiet");
    }
    return response.json();
}
export const fetchCreateHoaDonChiTiet = async (data) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}
export const fetchUpdateHoaDonChiTiet = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}
export const fetchUpdateStatusHoaDonChiTiet = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
}
