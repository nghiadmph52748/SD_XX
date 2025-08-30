const API = "http://localhost:8080/api/quyen-han-management";

export const fetchAllQuyenHan = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch quyen han");
    }
    return res.json();
};
export const fetchCreateQuyenHan = async (data) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}

export const fetchOneQuyenHan = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch quyen han");
    }
    return res.json();
}

export const fetchUpdateQuyenHan = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}
export const fetchUpdateStatusQuyenHan = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
}
