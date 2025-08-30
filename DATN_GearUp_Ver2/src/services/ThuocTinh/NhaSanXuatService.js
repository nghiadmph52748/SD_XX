const API = "http://localhost:8080/api/nha-san-xuat-management";

export const fetchAllNhaSanXuat = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch manufacturers");
    }
    return res.json();
}

export const fetchOneNhaSanXuat = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch manufacturer details");
    }
    return res.json();
}
export const fetchPagingNhaSanXuat = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated manufacturers");
    }
    return res.json();
}
export const fetchCreateNhaSanXuat = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to create manufacturer");
    }
    return res.json();
}
export const fetchUpdateNhaSanXuat = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to update manufacturer");
    }
}
export const fetchUpdateStatusNhaSanXuat = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        throw new Error("Failed to update manufacturer status");
    }
}