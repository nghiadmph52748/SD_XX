const API = "http://localhost:8080/api/dem-giay-management";

export const fetchAllDemGiay = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch insoles");
    }
    return res.json();
}
export const fetchOneDemGiay = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch insole details");
    }
    return res.json();
}
export const fetchPagingDemGiay = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated insoles");
    }
    return res.json();
}
export const fetchCreateDemGiay = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to create insole");
    }
}
export const fetchUpdateDemGiay = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to update insole");
    }
}
export const fetchUpdateStatusDemGiay = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        throw new Error("Failed to update insole status");
    }
}