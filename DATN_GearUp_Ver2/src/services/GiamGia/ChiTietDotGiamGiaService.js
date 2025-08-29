const API = "http://localhost:8080/api/chi-tiet-dot-giam-gia-management";

export const fetchAllChiTietDotGiamGia = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch chi tiet dot giam gia");
    }
    return response.json();
}

export const fetchChiTietDotGiamGiaById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch chi tiet dot giam gia");
    }
    return response.json();
}

export const fetchCreateChiTietDotGiamGia = async (chiTietDotGiamGia) => {
    const response = await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(chiTietDotGiamGia)
    });

    if (!response.ok) {
        throw new Error(`Failed to create chi tiet dot giam gia: ${response.status} ${response.statusText}`);
    }

    return response.json();
}

export const fetchUpdateChiTietDotGiamGia = async (id, chiTietDotGiamGia) => {
    const response = await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(chiTietDotGiamGia)
    });

    if (!response.ok) {
        throw new Error(`Failed to update chi tiet dot giam gia: ${response.status} ${response.statusText}`);
    }

    return response.json();
}

export const fetchUpdateStatusChiTietDotGiamGia = async (id) => {
    const response = await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
    });

    if (!response.ok) {
        throw new Error(`Failed to update status chi tiet dot giam gia: ${response.status} ${response.statusText}`);
    }

    return response.json();
}