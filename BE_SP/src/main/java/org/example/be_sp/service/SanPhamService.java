package org.example.be_sp.service;

import org.example.be_sp.entity.SanPham;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.SanPhamRequest;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.model.response.SanPhamResponse;
import org.example.be_sp.repository.NhaSanXuatRepository;
import org.example.be_sp.repository.SanPhamRepository;
import org.example.be_sp.repository.XuatXuRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository repository;
    @Autowired
    private NhaSanXuatRepository nsxService;
    @Autowired
    private XuatXuRepository xxService;

    public void add(SanPhamRequest sanPhamRequest) {
        SanPham sp = MapperUtils.map(sanPhamRequest, SanPham.class);
        sp.setIdXuatXu(xxService.findXuatXuById(sanPhamRequest.getIdXuatXu()));
        sp.setIdNhaSanXuat(nsxService.findNhaSanXuatById(sanPhamRequest.getIdNhaSanXuat()));
        repository.save(sp);
    }

    public void update(SanPhamRequest sanPhamRequest, Integer id) {
        SanPham existing = repository.findById(id).orElseThrow(() -> new ApiException("SanPham not found", "404"));
        MapperUtils.mapToExisting(sanPhamRequest, existing);
        existing.setId(id);
        existing.setIdXuatXu(xxService.findXuatXuById(sanPhamRequest.getIdXuatXu()));
        existing.setIdNhaSanXuat(nsxService.findNhaSanXuatById(sanPhamRequest.getIdNhaSanXuat()));
        repository.save(existing);
    }
    
    public List<SanPhamResponse> getAll() {
        return repository.findAllByDeleted(false).stream()
                .map(SanPhamResponse::new)
                .toList();
    }

    public PagingResponse<SanPhamResponse> paging(Integer no, Integer size) {
        Pageable page = PageRequest.of(no, size);
        return new PagingResponse<>(repository.findAll(page).map(SanPhamResponse::new), no);
    }

    public SanPhamResponse getById(Integer id) {
        SanPham sanPham = repository.findSanPhamById(id);
        return new SanPhamResponse(sanPham);
    }

    public void updateStatus(Integer id) {
        SanPham sanPham = repository.findById(id).orElseThrow(()-> new ApiException("SanPham not found", "404"));
        sanPham.setDeleted(true);
        repository.save(sanPham);
    }
}
