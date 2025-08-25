const API = "http://localhost:8080/api/khach-hang-management";

export const fetchAllKhachHang = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error(response.message);
    }
    return response.json();
}
export const fetchGetKhachHangById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error(response.message);
    }
    return response.json();
}
export const fetchCreateKhachHang = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error(res.message);
    }
}
export const fetchUpdateKhachHang = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error(res.message);
    }
}
export const fetchUpdateStatusKhachHang = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (!res.ok) {
        throw new Error(res.message);
    }
}
