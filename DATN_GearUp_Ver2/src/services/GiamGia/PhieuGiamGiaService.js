import api from '../response/api.js'

const API = "/phieu-giam-gia-management";

export const fetchAllPhieuGiamGia = async () => {
    const response = await api.get(`${API}/playlist`);
    return { data: response };
}

export const fetchPhieuGiamGiaById = async (id) => {
    const response = await api.get(`${API}/detail/${id}`);
    return { data: response };
}

export const fetchCreatePhieuGiamGia = async (phieuGiamGia) => {
    const response = await api.post(`${API}/add`, phieuGiamGia);
    return response;
}

export const fetchUpdatePhieuGiamGia = async (id, phieuGiamGia) => {
    const response = await api.put(`${API}/update/${id}`, phieuGiamGia);
    return response;
}

export const fetchUpdateStatusPhieuGiamGia = async (id) => {
    const response = await api.put(`${API}/update/status/${id}`);
    return response;
}

export const fetchDeletePhieuGiamGia = async (id) => {
    const response = await api.delete(`${API}/delete/${id}`);
    return response;
}

export const fetchActivePhieuGiamGiaForCustomer = async (customerId) => {
    const response = await api.get(`${API}/list/khach-hang/active/${customerId}`);
    return { data: response };
}