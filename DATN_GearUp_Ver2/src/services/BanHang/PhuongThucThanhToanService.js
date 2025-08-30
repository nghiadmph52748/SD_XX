const API = "http://localhost:8080/api/phuong-thuc-thanh-toan-management";

export const fetchAllPhuongThucThanhToan = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch phuong thuc thanh toan");
    }
    return response.json();
}
export const fetchGetPhuongThucThanhToanById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);    
    if (!response.ok) {
        throw new Error("Failed to fetch phuong thuc thanh toan");
    }
    return response.json();
}
export const fetchCreatePhuongThucThanhToan = async (data) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
}
export const fetchUpdatePhuongThucThanhToan = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
}
export const fetchUpdateStatusPhuongThucThanhToan = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    })
}