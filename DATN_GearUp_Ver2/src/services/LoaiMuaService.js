const API = "http://localhost:8080/api/categories";

export const fetchAllLoaiMua = async () => {
    const res = await fetch(`${API}/seasons`);
    if (!res.ok) {
        throw new Error("Failed to fetch seasons");
    }
    return res.json();
}

export const fetchOneLoaiMua = async (id) => {
    const res = await fetch(`${API}/seasons`);
    if (!res.ok) {
        throw new Error("Failed to fetch season");
    }
    const data = await res.json();
    return data.find(item => item.id === parseInt(id));
}

export const fetchPagingLoaiMua = async (page, size) => {
    const res = await fetch(`${API}/seasons`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated seasons");
    }
    const data = await res.json();
    const start = (page - 1) * size;
    const end = start + size;
    return {
        data: data.slice(start, end),
        total: data.length,
        page: page,
        size: size
    };
}

export const fetchCreateLoaiMua = async (data) => {
    const res = await fetch(`${API}/seasons`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to create season");
    }
    return res.json();
}

export const fetchUpdateLoaiMua = async (id, data) => {
    const res = await fetch(`${API}/seasons/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update season");
    }
    return res.json();
}

export const fetchUpdateStatusLoaiMua = async (id) => {
    const res = await fetch(`${API}/seasons/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update season status");
    }
    return res.json();
}