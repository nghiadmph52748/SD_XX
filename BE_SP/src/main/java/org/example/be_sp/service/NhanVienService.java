package org.example.be_sp.service;

import org.example.be_sp.entity.NhanVien;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.NhanVienRequest;
import org.example.be_sp.model.response.NhanVienResponse;
import org.example.be_sp.repository.NhanVienRepository;
import org.example.be_sp.repository.QuyenHanRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhanVienService {
    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    QuyenHanRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<NhanVienResponse> getAllNhanVien() {
        return nhanVienRepository.findAll().stream().map(NhanVienResponse::new).toList();
    }

    public NhanVienResponse getNhanVienById(Integer id) {
        return nhanVienRepository.findById(id).map(NhanVienResponse::new).orElseThrow(() -> new ApiException("NhanVien not found", "404"));
    }

    public NhanVienResponse getNhanVienByEmail(String email) {
        return nhanVienRepository.findByEmail(email).map(NhanVienResponse::new).orElseThrow(() -> new ApiException("NhanVien not found", "404"));
    }

    public NhanVienResponse getNhanVienByTenTaiKhoan(String tenTaiKhoan) {
        return nhanVienRepository.findByTenTaiKhoan(tenTaiKhoan).map(NhanVienResponse::new).orElseThrow(() -> new ApiException("NhanVien not found", "404"));
    }

    public void saveNhanVien(NhanVienRequest request) {
        NhanVien nv = MapperUtils.map(request, NhanVien.class);
        nv.setIdQuyenHan(repository.findById(request.getIdQuyenHan()).orElseThrow(() -> new ApiException("QuyenHan not found", "404")));
        nhanVienRepository.save(nv);
    }

    public void updateNhanVien(Integer id, NhanVienRequest request) {
        NhanVien e = MapperUtils.map(request, NhanVien.class);
        e.setId(id);
        e.setIdQuyenHan(repository.findById(request.getIdQuyenHan()).orElseThrow(() -> new ApiException("QuyenHan not found", "404")));
        nhanVienRepository.save(e);
    }

    public boolean existsByEmail(String email) {
        return nhanVienRepository.existsByEmail(email);
    }

    public boolean existsByTenTaiKhoan(String tenTaiKhoan) {
        return nhanVienRepository.existsByTenTaiKhoan(tenTaiKhoan);
    }

    public void updateStatus(Integer id){
        NhanVien c = nhanVienRepository.findById(id).orElseThrow(() -> new ApiException("NhanVien not found", "404"));
        c.setDeleted(true);
        nhanVienRepository.save(c);
    }

}
