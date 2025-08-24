package org.example.be_sp.service;

import org.example.be_sp.entity.PhieuGiamGia;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.PhieuGiamGiaRequest;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.model.response.PhieuGiamGiaResponse;
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

    public void add(PhieuGiamGiaRequest request){
        PhieuGiamGia phieuGiamGia = MapperUtils.map(request, PhieuGiamGia.class);
        phieuGiamGiaRepository.save(phieuGiamGia);
    }

    public void update(Integer id, PhieuGiamGiaRequest phieuGiamGia){
        PhieuGiamGia c = MapperUtils.map(phieuGiamGia, PhieuGiamGia.class);
        c.setId(id);
        phieuGiamGiaRepository.save(c);
    }

    public void updateStatus(Integer id){
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getById(id);
        phieuGiamGia.setTrangThai(true);
        phieuGiamGiaRepository.save(phieuGiamGia);
    }
}
