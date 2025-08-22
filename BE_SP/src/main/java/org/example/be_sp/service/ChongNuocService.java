package org.example.be_sp.service;

import org.example.be_sp.entity.ChongNuoc;
import org.example.be_sp.entity.DoBen;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.ChongNuocRequest;
import org.example.be_sp.model.response.ChongNuocResponse;
import org.example.be_sp.repository.ChongNuocRepository;
import org.example.be_sp.util.GenericCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ChongNuocService extends GenericCrudService<ChongNuoc, Integer, ChongNuocResponse, ChongNuocRequest> {
    @Autowired
    private ChongNuocRepository chongNuocRepository;

    public ChongNuocService(Class<ChongNuoc> entity, Class<ChongNuocResponse> chongNuocResponseClass, Class<ChongNuocRequest> chongNuocRequestClass, JpaRepository<ChongNuoc, Integer> chongNuocRepository) {
        super(entity, chongNuocResponseClass, chongNuocRequestClass, chongNuocRepository);
    }
    public void updateStatus(Integer id) {
        ChongNuoc chongNuoc = chongNuocRepository.findById(id).orElseThrow(() -> new ApiException("ChongNuoc not found", "404"));
        chongNuoc.setDeleted(true);
        chongNuocRepository.save(chongNuoc);
    }
}
