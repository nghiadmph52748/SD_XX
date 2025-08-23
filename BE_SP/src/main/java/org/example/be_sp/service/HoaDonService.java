package org.example.be_sp.service;

import org.example.be_sp.entity.HoaDon;
import org.example.be_sp.entity.KhachHang;
import org.example.be_sp.entity.PhieuGiamGia;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.HoaDonRequest;
import org.example.be_sp.model.response.HoaDonResponse;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.repository.HoaDonRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NhanVienService nhanVienService;
    @Autowired
    PhieuGiamGiaService phieuGiamGiaService;


    public List<HoaDonResponse> getAll() {
        return hoaDonRepository.findAll().stream().map(HoaDonResponse::new).toList();
    }
    public PagingResponse<HoaDonResponse> phanTrang(Integer no, Integer size) {
        Pageable page = PageRequest.of(no, size);
        return new PagingResponse<>(hoaDonRepository.findAll(page).map(HoaDonResponse::new), no);
    }

    public HoaDonResponse getByid(Integer id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow(() -> new ApiException("Không tìm thấy hóa đơn","404"));
        return new HoaDonResponse(hoaDon);
    }
    public void add(HoaDonRequest request) {
        HoaDon hd = MapperUtils.map(request, HoaDon.class);
        hd.setIdKhachHang(khachHangService.findById(request.getIdKhachHang()));
        hd.setIdPhieuGiamGia(phieuGiamGiaService.getById(request.getIdPhieuGiamGia()));
        hd.setIdNhanVien(nhanVienService.getNhanVienById(request.getIdNhanVien()));
        hoaDonRepository.save(hd);
    }
    public void update(Integer id, HoaDonRequest request) {
        HoaDon hd = hoaDonRepository.findById(id).orElseThrow(() -> new ApiException("Không tìm thấy hóa đơn","404"));
        MapperUtils.mapToExisting(request, hd);
        hd.setIdKhachHang(khachHangService.findById(request.getIdKhachHang()));
        hd.setIdPhieuGiamGia(phieuGiamGiaService.getById(request.getIdPhieuGiamGia()));
        hd.setIdNhanVien(nhanVienService.getNhanVienById(request.getIdNhanVien()));
        hoaDonRepository.save(hd);
    }
    public void delete(Integer id) {
        HoaDon hd = hoaDonRepository.findById(id).orElseThrow(() -> new ApiException("Không tìm thấy hóa đơn","404"));
        hd.setDeleted(true);
        hoaDonRepository.save(hd);
    }

}
