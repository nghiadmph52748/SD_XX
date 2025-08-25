const API = "http://localhost:8080/api/hinh-thuc-thanh-toan-management";

export const fetchAllHinhThucThanhToan = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch hinh thuc thanh toan");
    }
    return response.json();
}
export const fetchGetHinhThucThanhToanById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch hinh thuc thanh toan");
    }
    return response.json();
}
export const fetchCreateHinhThucThanhToan = async (data) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}   
export const fetchUpdateHinhThucThanhToan = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}
export const fetchUpdateStatusHinhThucThanhToan = async (id) => {   
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
}