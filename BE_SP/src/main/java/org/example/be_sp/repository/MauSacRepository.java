package org.example.be_sp.repository;

import org.example.be_sp.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    MauSac findMauSacById(Integer id);
}
