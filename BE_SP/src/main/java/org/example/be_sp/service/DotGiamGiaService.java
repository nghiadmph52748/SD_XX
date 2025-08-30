package org.example.be_sp.service;

import org.example.be_sp.entity.DotGiamGia;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.DotGiamGiaRequest;
import org.example.be_sp.model.response.DotGiamGiaResponse;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.repository.DotGiamGiaRepository;
import org.example.be_sp.util.GenericCrudService;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DotGiamGiaService extends GenericCrudService<DotGiamGia, Integer, DotGiamGiaResponse, DotGiamGiaRequest> {
    
    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;
    
    public DotGiamGiaService(Class<DotGiamGia> entity, Class<DotGiamGiaResponse> dotGiamGiaResponseClass, Class<DotGiamGiaRequest> dotGiamGiaRequestClass, JpaRepository<DotGiamGia, Integer> repository) {
        super(entity, dotGiamGiaResponseClass, dotGiamGiaRequestClass, repository);
    }
    
    // Override to use active-only queries
    @Override
    public List<DotGiamGiaResponse> getAll() {
        List<DotGiamGia> list = dotGiamGiaRepository.findAllActive();
        return list.stream()
                .map(item -> MapperUtils.map(item, DotGiamGiaResponse.class))
                .toList();
    }
    
    @Override 
    public PagingResponse<DotGiamGiaResponse> paging(int page, int size) {
        var pageData = dotGiamGiaRepository.findAllActive(PageRequest.of(page, size));
        var responsePage = pageData.map(item -> MapperUtils.map(item, DotGiamGiaResponse.class));
        return new PagingResponse<>(responsePage, page);
    }
    
    @Override
    public DotGiamGiaResponse getById(Integer id) {
        DotGiamGia entity = dotGiamGiaRepository.findActiveById(id);
        if (entity == null) {
            throw new ApiException("Không tìm thấy đợt giảm giá với id: " + id, "404");
        }
        return MapperUtils.map(entity, DotGiamGiaResponse.class);
    }
    
    @Transactional
    public void updateStatus(Integer id) {
        DotGiamGia existing = dotGiamGiaRepository.findActiveById(id);
        if (existing == null) {
            throw new ApiException("Đợt giảm giá không tồn tại với id: " + id, "404");
        }
        
        // Toggle status
        existing.setTrangThai(!existing.getTrangThai());
        dotGiamGiaRepository.save(existing);
    }
    
    @Transactional
    public void delete(Integer id) {
        DotGiamGia existing = dotGiamGiaRepository.findActiveById(id);
        if (existing == null) {
            throw new ApiException("Đợt giảm giá không tồn tại với id: " + id, "404");
        }
        
        existing.setDeleted(true);
        dotGiamGiaRepository.save(existing);
    }
}
