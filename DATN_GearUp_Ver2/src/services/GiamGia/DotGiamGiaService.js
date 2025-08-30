const API = "http://localhost:8080/api/dot-giam-gia-management";

export const fetchAllDotGiamGia = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch dot giam gia");
    }
    return response.json();
}
export const fetchDotGiamGiaById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch dot giam gia");
    }
    return response.json();
}
export const fetchCreateDotGiamGia = async (dotGiamGia) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dotGiamGia)
    });
}
export const fetchUpdateDotGiamGia = async (id, dotGiamGia) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: { 
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dotGiamGia)
    });
}
export const fetchUpdateStatusDotGiamGia = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
    });
}

export const fetchDeleteDotGiamGia = async (id) => {
    const response = await fetch(`${API}/delete/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
    });
    if (!response.ok) {
        throw new Error("Failed to delete dot giam gia");
    }
    return response.json();
}
