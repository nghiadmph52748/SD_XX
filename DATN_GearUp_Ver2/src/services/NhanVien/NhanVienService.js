const API = "http://localhost:8080/api/nhan-vien-management"

export const fetchAllNhanVien = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error(res.message)
    }
    return res.json();
}

export const fetchNhanVienById = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error(res.message)
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
        throw new Error(res.message)
    }
}
export const fetchUpdateNhanVien = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error(res.message)
    }
}

export const fetchPagingNhanVien = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error(res.message)
    }
    return res.json();
}
export const fetchUpdateStatusNhanVien = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (!res.ok) {
        throw new Error(res.message)
    }
}
