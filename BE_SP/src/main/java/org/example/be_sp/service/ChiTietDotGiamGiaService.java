package org.example.be_sp.service;

import org.example.be_sp.entity.ChiTietDotGiamGia;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.ChiTietDotGiamGiaRequest;
import org.example.be_sp.model.response.ChiTietDotGiamGiaResponse;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.repository.ChiTietDotGiamGiaRepository;
import org.example.be_sp.repository.ChiTietSanPhamRepository;
import org.example.be_sp.repository.DotGiamGiaRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietDotGiamGiaService {
    @Autowired
    ChiTietDotGiamGiaRepository chiTietDotGiamGiaRepository;
    @Autowired
    DotGiamGiaRepository dotGiamGiaRepository;
    @Autowired
    ChiTietSanPhamRepository chiTietSanPhamRepository;

    public List<ChiTietDotGiamGiaResponse> getAll() {
        return chiTietDotGiamGiaRepository.findAll().stream().map(ChiTietDotGiamGiaResponse::new).toList();
    }

    public PagingResponse<ChiTietDotGiamGiaResponse> paging(int page, int size) {
        return new PagingResponse<>(chiTietDotGiamGiaRepository.findAll(PageRequest.of(page, size)), page);
    }

    public ChiTietDotGiamGiaResponse getById(Integer id) {
        return chiTietDotGiamGiaRepository.findById(id)
                .map(ChiTietDotGiamGiaResponse::new)
                .orElseThrow(() -> new ApiException("Chi tiết đợt giảm giá không tồn tại", "404"));
    }

    public void add(ChiTietDotGiamGiaRequest request) {
        ChiTietDotGiamGia e = MapperUtils.map(request, ChiTietDotGiamGia.class);
        e.setIdChiTietSanPham(chiTietSanPhamRepository.findChiTietSanPhamById(request.getIdChiTietSanPham()));
        e.setIdDotGiamGia(dotGiamGiaRepository.findDotGiamGiaById(request.getIdDotGiamGia()));
        chiTietDotGiamGiaRepository.save(e);
    }

    public void update(Integer id, ChiTietDotGiamGiaRequest request) {
        ChiTietDotGiamGia c = MapperUtils.map(request, ChiTietDotGiamGia.class);
        c.setId(id);
        c.setIdChiTietSanPham(chiTietSanPhamRepository.findChiTietSanPhamById(request.getIdChiTietSanPham()));
        c.setIdDotGiamGia(dotGiamGiaRepository.findDotGiamGiaById(request.getIdDotGiamGia()));
        chiTietDotGiamGiaRepository.save(c);
    }
}
