package org.example.be_sp.service;

import org.example.be_sp.entity.DemGiay;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.DemGiayRequest;
import org.example.be_sp.model.response.DemGiayResponse;
import org.example.be_sp.repository.DemGiayRepository;
import org.example.be_sp.util.GenericCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DemGiayService extends GenericCrudService<DemGiay, Integer, DemGiayResponse, DemGiayRequest> {
    @Autowired
    private DemGiayRepository repository;
    public DemGiayService(Class<DemGiay> entity, Class<DemGiayResponse> demGiayResponseClass, Class<DemGiayRequest> demGiayRequestClass, JpaRepository<DemGiay, Integer> repository) {
        super(entity, demGiayResponseClass, demGiayRequestClass, repository);
    }
    public void updateStatus(Integer id) {
        DemGiay demGiay = repository.findById(id).orElseThrow(() -> new ApiException("DemGiay not found", "404"));
        demGiay.setDeleted(true);
        repository.save(demGiay);
    }
}
