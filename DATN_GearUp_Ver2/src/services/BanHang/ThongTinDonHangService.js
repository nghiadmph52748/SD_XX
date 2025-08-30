const API = "http://localhost:8080/api/thong-tin-don-hang-management";

export const fetchAllThongTinDonHang = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch thong tin don hang");
    }
    return response.json();
}
export const fetchGetThongTinDonHangById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch thong tin don hang");
    }
    return response.json();
}
export const fetchCreateThongTinDonHang = async (data) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
}
export const fetchUpdateThongTinDonHang = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })  
}
export const fetchUpdateStatusThongTinDonHang = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    })
}