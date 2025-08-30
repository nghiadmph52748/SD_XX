const API = "http://localhost:8080/api/chong-nuoc-management";

export const fetchAllChongNuoc = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch waterproof");
    }
    return res.json();
}

export const fetchOneChongNuoc = async (id) => {
    const res = await fetch(`${API}/detail/{id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch waterproof");
    }
    const data = await res.json();
    return data.find(item => item.id === parseInt(id));
}

export const fetchPagingChongNuoc = async (page, size) => {
    const res = await fetch(`${API}/paging`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated waterproof");
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

export const fetchCreateChongNuoc = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to create waterproof");
    }
    return res.json();
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
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update waterproof");
    }
    return res.json();
}

export const fetchUpdateStatusChongNuoc = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update waterproof status");
    }
    return res.json();
}