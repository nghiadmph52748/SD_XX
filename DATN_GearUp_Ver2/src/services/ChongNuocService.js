const API = "http://localhost:8080/api/chong-nuoc-management";

export const fetchAllChongNuoc = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch waterproof types");
    }
    return res.json();
}
export const fetchOneChongNuoc = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch waterproof type details");
    }
    return res.json();
}
export const fetchPagingChongNuoc = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated waterproof types");
    }
    return res.json();
}
export const fetchCreateChongNuoc = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to create waterproof type");
    }
}
export const fetchUpdateChongNuoc = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to update waterproof type");
    }
}
export const fetchUpdateStatusChongNuoc = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        throw new Error("Failed to update waterproof type status");
    }
}