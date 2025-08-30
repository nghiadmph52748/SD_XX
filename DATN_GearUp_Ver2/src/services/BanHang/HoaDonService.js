const API = "http://localhost:8080/api/hoa-don-management";

export const fetchAllHoaDon = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch hoa don");
    }
    return response.json();
}
export const fetchGetHoaDonById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch hoa don");
    }
    return response.json();
}
export const fetchCreateHoaDon = async (data) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })  
}
export const fetchUpdateHoaDon = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
}
export const fetchUpdateStatusHoaDon = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    })
}