const API = "http://localhost:8080/api/phieu-giam-gia-ca-nhan-management";

export const fetchAllPhieuGiamGiaCaNhan = async () => {
    const response = await fetch(`${API}/playlist`);
    if (!response.ok) {
        throw new Error("Failed to fetch phieu giam gia ca nhan");
    }
    return response.json();
}
export const fetchPhieuGiamGiaCaNhanById = async (id) => {
    const response = await fetch(`${API}/detail/${id}`);
    if (!response.ok) {
        throw new Error("Failed to fetch phieu giam gia ca nhan");
    }
    return response.json();
}
export const fetchCreatePhieuGiamGiaCaNhan = async (phieuGiamGiaCaNhan) => {
    await fetch(`${API}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(phieuGiamGiaCaNhan)
    });
}
export const fetchUpdatePhieuGiamGiaCaNhan = async (id, phieuGiamGiaCaNhan) => {
    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(phieuGiamGiaCaNhan)
    });
}
export const fetchUpdateStatusPhieuGiamGiaCaNhan = async (id) => {
    await fetch(`${API}/update/status/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
    });
}