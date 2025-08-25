package org.example.be_sp.service;

import org.example.be_sp.entity.DiaChiKhachHang;
import org.example.be_sp.entity.KhachHang;
import org.example.be_sp.model.DiaChi;
import org.example.be_sp.model.request.KhachHangRequest;
import org.example.be_sp.model.response.KhachHangResponse;
import org.example.be_sp.repository.DiaChiKhachHangRepository;
import org.example.be_sp.repository.KhachHangRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class KhachHangService {
    @Autowired
    private DiaChiKhachHangRepository repository;
    @Autowired
    private KhachHangRepository khachHangRepository;

    public List<KhachHangResponse> findAll() {
        return khachHangRepository.findAll().stream().map(KhachHangResponse::new).toList();
    }

    public KhachHangResponse findById(Integer id) {
        return khachHangRepository.findById(id).map(KhachHangResponse::new).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với id: " + id));
    }

    public void save(KhachHangRequest request) {
        if (khachHangRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại: " + request.getEmail());
        }
        KhachHang khachHang = MapperUtils.map(request, KhachHang.class);
        KhachHang saved = khachHangRepository.save(khachHang);
        List<DiaChi> listDiaChi = request.getListDiaChi();
        if (listDiaChi != null) {
            for (DiaChi data : listDiaChi) {
                DiaChiKhachHang diaChi = new DiaChiKhachHang();
                diaChi.setThanhPho(data.getThanhPho());
                diaChi.setQuan(data.getQuan());
                diaChi.setPhuong(data.getPhuong());
                diaChi.setDiaChiCuThe(data.getDiaChiCuThe());
                diaChi.setIdKhachHang(saved);
                repository.save(diaChi);
            }
        }
    }

    public void update(Integer id, KhachHangRequest request) {
        KhachHang khachHang = MapperUtils.map(request, KhachHang.class);
        khachHang.setId(id);
        KhachHang saved = khachHangRepository.save(khachHang);
        List<DiaChi> listDiaChi = request.getListDiaChi();
        if (listDiaChi != null) {
            for (DiaChiKhachHang dckh : repository.findAllById(id)) {
                for (DiaChi data : listDiaChi) {
                    if (Objects.equals(dckh.getDiaChiCuThe(), data.getDiaChiCuThe())
                            && Objects.equals(dckh.getThanhPho(), data.getThanhPho())
                            && Objects.equals(dckh.getQuan(), data.getQuan())
                            && Objects.equals(dckh.getPhuong(), data.getPhuong())) {
                        listDiaChi.remove(data);
                        break;
                    }
                    DiaChiKhachHang diaChi = new DiaChiKhachHang();
                    diaChi.setThanhPho(data.getThanhPho());
                    diaChi.setQuan(data.getQuan());
                    diaChi.setPhuong(data.getPhuong());
                    diaChi.setDiaChiCuThe(data.getDiaChiCuThe());
                    diaChi.setIdKhachHang(saved);
                    repository.save(diaChi);
                }
            }
        }
    }

    public void updateStatus(Integer id) {
        KhachHang kh = khachHangRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với id: " + id));
        kh.setDeleted(true); // xóa mềm
        khachHangRepository.save(kh);
    }
}

