const API = "http://localhost:8080/api/chat-lieu-management";

export const fetchAllChatLieu = async () => {
    const res = await fetch(`${API}/playlist`);
    if (!res.ok) {
        throw new Error("Failed to fetch materials");
    }
    return res.json();
}
export const fetchOneChatLieu = async (id) => {
    const res = await fetch(`${API}/detail/${id}`);
    if (!res.ok) {
        throw new Error("Failed to fetch material details");
    }
    return res.json();
}
export const fetchPagingChatLieu = async (page, size) => {
    const res = await fetch(`${API}/paging?page=${page}&size=${size}`);
    if (!res.ok) {
        throw new Error("Failed to fetch paginated materials");
    }
    return res.json();
}
export const fetchCreateChatLieu = async (data) => {
    const res = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`Failed to create material: ${res.status} - ${errorText}`);
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
            throw new Error("Backend returned null or zero ID for created material");
        }
        return responseData.data;
    }

    return responseData;
}
export const fetchUpdateChatLieu = async (id, data) => {
    const res = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        throw new Error("Failed to update material");
    }
}
export const fetchUpdateStatusChatLieu = async (id) => {
    const res = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!res.ok) {
        throw new Error("Failed to update material status");
    }
}