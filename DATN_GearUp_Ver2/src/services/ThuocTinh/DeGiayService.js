const API = "http://localhost:8080/api/de-giay-management";

export const fetchAllDeGiay = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch insoles");
    }
    return res.json();
}
export const fetchOneDeGiay = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch insole details");
    }
    return res.json();
}
export const fetchPagingDeGiay = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated insoles");
    }
    return res.json();
}
export const fetchCreateDeGiay = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`Failed to create insole: ${res.status} - ${errorText}`);
    }

    const responseData = await res.json();

    // Backend trả về format: { data: id, message: "..." }
    if (responseData && responseData.data !== undefined) {
        // Nếu data là ID (number)
        if (typeof responseData.data === 'number' && responseData.data > 0) {
            return {
                id: responseData.data,
                message: responseData.message
            };
        }
        // Nếu data là null hoặc 0
        if (responseData.data === null || responseData.data === 0) {
            throw new Error("Backend returned null or zero ID for created insole");
        }
        return responseData.data;
    }

    return responseData;
}
export const fetchUpdateDeGiay = async (id, data) => {
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
export const fetchUpdateStatusDeGiay = async (id) => {
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