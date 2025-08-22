package org.example.be_sp.service;

import org.example.be_sp.entity.MonTheThao;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.MonTheThaoRequest;
import org.example.be_sp.model.response.MonTheThaoResponse;
import org.example.be_sp.repository.MonTheThaoRepository;
import org.example.be_sp.util.GenericCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MonTheThaoService extends GenericCrudService<MonTheThao, Integer, MonTheThaoResponse, MonTheThaoRequest> {
    @Autowired
    private MonTheThaoRepository repository;
    public MonTheThaoService(Class<MonTheThao> entity, Class<MonTheThaoResponse> monTheThaoResponseClass, Class<MonTheThaoRequest> monTheThaoRequestClass, JpaRepository<MonTheThao, Integer> repository) {
        super(entity, monTheThaoResponseClass, monTheThaoRequestClass, repository);
    }
    public void updateStatus(Integer id) {
        MonTheThao monTheThao = repository.findById(id).orElseThrow(() -> new ApiException("MonTheThao not found","404"));
        monTheThao.setDeleted(true);
        repository.save(monTheThao);
    }
}
