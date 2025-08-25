package org.example.be_sp.service;

import org.example.be_sp.entity.KhachHang;
import org.example.be_sp.entity.PhieuGiamGia;
import org.example.be_sp.entity.PhieuGiamGiaCaNhan;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.PhieuGiamGiaRequest;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.model.response.PhieuGiamGiaResponse;
import org.example.be_sp.repository.KhachHangRepository;
import org.example.be_sp.repository.PhieuGiamGiaCaNhanRepository;
import org.example.be_sp.repository.PhieuGiamGiaRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhieuGiamGiaService {
    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;
    @Autowired
    private PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;

    public List<PhieuGiamGiaResponse> getAll() {
        return new ArrayList<>(phieuGiamGiaRepository.findAll().stream().map(PhieuGiamGiaResponse::new).toList());
    }

    public PhieuGiamGia getById(Integer id) {
        return phieuGiamGiaRepository.getById(id);
    }

    public PhieuGiamGiaResponse getByIdResponse(Integer id) {
        return phieuGiamGiaRepository.findById(id).map(PhieuGiamGiaResponse::new).orElseThrow(() -> new ApiException("PhieuGiamGia not found", "404"));
    }

    public PagingResponse<PhieuGiamGiaResponse> paging(Integer page, Integer size) {
        return new PagingResponse<>(phieuGiamGiaRepository.findAll(PageRequest.of(page, size)).map(PhieuGiamGiaResponse::new), page);
    }

    public void add(PhieuGiamGiaRequest request) {
        PhieuGiamGia pgg = MapperUtils.map(request, PhieuGiamGia.class);
        phieuGiamGiaRepository.save(pgg);
        if (request.getIdKhachHang() != null) {
            for (Integer idKhachHang : request.getIdKhachHang()) {
                PhieuGiamGiaCaNhan pggcn = new PhieuGiamGiaCaNhan();
                pggcn.setIdKhachHang(khachHangRepository.getById(idKhachHang));
                pggcn.setIdPhieuGiamGia(pgg);
                pggcn.setTenPhieuGiamGiaCaNhan(request.getTenPhieuGiamGia());
                pggcn.setNgayNhan(request.getNgayBatDau());
                pggcn.setNgayHetHan(request.getNgayKetThuc());
                pggcn.setTrangThai(true);
                pggcn.setDeleted(false);
                phieuGiamGiaCaNhanRepository.save(pggcn);
            }
        }
    }

    public void update(Integer id, PhieuGiamGiaRequest request) {
        PhieuGiamGia pgg = MapperUtils.map(request, PhieuGiamGia.class);
        pgg.setId(id);
        PhieuGiamGia saved = phieuGiamGiaRepository.save(pgg);

        List<PhieuGiamGiaCaNhan> existingPersonalCoupons = phieuGiamGiaCaNhanRepository.findByIdPhieuGiamGiaId(id);
        for (PhieuGiamGiaCaNhan existing : existingPersonalCoupons) {
            existing.setDeleted(true);
            phieuGiamGiaCaNhanRepository.save(existing);
        }
        
        // Create new personal coupons if specified
        if (request.getIdKhachHang() != null && !request.getIdKhachHang().isEmpty()) {
            for (Integer idKhachHang : request.getIdKhachHang()) {
                PhieuGiamGiaCaNhan pggcn = new PhieuGiamGiaCaNhan();
                pggcn.setIdKhachHang(khachHangRepository.getById(idKhachHang));
                pggcn.setIdPhieuGiamGia(saved);
                pggcn.setTenPhieuGiamGiaCaNhan(request.getTenPhieuGiamGia());
                pggcn.setNgayNhan(request.getNgayBatDau());
                pggcn.setNgayHetHan(request.getNgayKetThuc());
                pggcn.setTrangThai(true);
                pggcn.setDeleted(false);
                phieuGiamGiaCaNhanRepository.save(pggcn);
            }
        }
    }

    public void updateStatus(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getById(id);
        phieuGiamGia.setDeleted(!phieuGiamGia.getDeleted()); // Toggle deleted status
        phieuGiamGiaRepository.save(phieuGiamGia);
        
        // Also update personal coupons if they exist
        if (phieuGiamGia.getPhieuGiamGiaCaNhans() != null && !phieuGiamGia.getPhieuGiamGiaCaNhans().isEmpty()) {
            for (PhieuGiamGiaCaNhan pggcn : phieuGiamGia.getPhieuGiamGiaCaNhans()) {
                pggcn.setDeleted(phieuGiamGia.getDeleted());
                phieuGiamGiaCaNhanRepository.save(pggcn);
            }
        }
    }
}
