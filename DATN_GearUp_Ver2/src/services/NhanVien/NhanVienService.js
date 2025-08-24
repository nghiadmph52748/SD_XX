const API = "http://localhost:8080/api/nhan-vien-management"

export const fetchAllNhanVien = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch nhan vien")
    }
    return res.json();
}

export const fetchNhanVienById = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch nhan vien")
    }
    return res.json();
}

export const fetchCreateNhanVien = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to create nhan vien")
    }
}
export const fetchUpdateNhanVien = async (id, data) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
}

export const fetchPagingNhanVien = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated nhan vien")
    }
    return res.json();
}
export const fetchUpdateStatusNhanVien = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
}
