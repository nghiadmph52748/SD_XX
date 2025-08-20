package org.example.be_sp.service;

import org.example.be_sp.entity.ChiTietSanPhamAnh;
import org.example.be_sp.model.response.ChiTietSanPhamAnhResponse;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.repository.AnhSanPhamRepository;
import org.example.be_sp.repository.ChiTietAnhSanPhamRepository;
import org.example.be_sp.repository.ChiTietSanPhamRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietSanPhamAnhService {
    @Autowired
    ChiTietAnhSanPhamRepository repository;
    @Autowired
    AnhSanPhamRepository anhSanPhamRepository;
    @Autowired
    ChiTietSanPhamRepository chiTietSanPhamRepository;

    public List<ChiTietSanPhamAnhResponse> getAll() {
        return repository.findAll().stream().map(ChiTietSanPhamAnhResponse::new).toList();
    }

    public ChiTietSanPhamAnhResponse getById(Integer id) {
        return repository.findById(id)
                .map(ChiTietSanPhamAnhResponse::new)
                .orElseThrow(() -> new RuntimeException("ChiTietSanPhamAnh not found with id: " + id));
    }

    public PagingResponse<ChiTietSanPhamAnhResponse> paging(Integer page, Integer size) {
        return new PagingResponse<>(repository.findAll(PageRequest.of(page, size)).map(ChiTietSanPhamAnhResponse::new), page);
    }

    public void add(ChiTietSanPhamAnhResponse request) {
        ChiTietSanPhamAnh e = MapperUtils.map(request, ChiTietSanPhamAnh.class);
        e.setIdAnhSanPham(anhSanPhamRepository.findAnhSanPhamById(request.getIdAnhSanPham()));
        e.setIdChiTietSanPham(chiTietSanPhamRepository.findChiTietSanPhamById(request.getIdChiTietSanPham()));
        repository.save(e);
    }

    public void update(Integer id, ChiTietSanPhamAnhResponse request) {
        ChiTietSanPhamAnh e = MapperUtils.map(request, ChiTietSanPhamAnh.class);
        e.setIdAnhSanPham(anhSanPhamRepository.findAnhSanPhamById(request.getIdChiTietSanPham()));
        e.setIdChiTietSanPham(chiTietSanPhamRepository.findChiTietSanPhamById(request.getIdChiTietSanPham()));
        e.setId(id);
        repository.save(e);
    }
}
