const API = "http://localhost:8080/api/categories";

export const fetchAllDoBen = async () => {
    const res = await fetch(`${API}/durability`);
    if (!res.ok) {
        throw new Error("Failed to fetch durability");
    }
    return res.json();
}

export const fetchOneDoBen = async (id) => {
    const res = await fetch(`${API}/durability`);
    if (!res.ok) {
        throw new Error("Failed to fetch durability");
    }
    const data = await res.json();
    return data.find(item => item.id === parseInt(id));
}

export const fetchPagingDoBen = async (page, size) => {
    const res = await fetch(`${API}/durability`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated durability");
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

export const fetchCreateDoBen = async (data) => {
    const res = await fetch(`${API}/durability`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to create durability");
    }
    return res.json();
}

export const fetchUpdateDoBen = async (id, data) => {
    const res = await fetch(`${API}/durability/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update durability");
    }
    return res.json();
}

export const fetchUpdateStatusDoBen = async (id) => {
    const res = await fetch(`${API}/durability/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Failed to update durability status");
    }
    return res.json();
}
