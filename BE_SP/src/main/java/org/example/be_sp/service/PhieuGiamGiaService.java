package org.example.be_sp.service;

import org.example.be_sp.entity.PhieuGiamGia;
import org.example.be_sp.repository.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhieuGiamGiaService {
    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    public PhieuGiamGia getById(Integer id) {
        return phieuGiamGiaRepository.getById(id);
    }
}
