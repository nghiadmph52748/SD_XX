package org.example.be_sp.repository;

import org.example.be_sp.entity.DotGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> {
    DotGiamGia findDotGiamGiaById(Integer id);
    
    @Query("SELECT d FROM DotGiamGia d WHERE d.deleted = false ORDER BY d.id DESC")
    List<DotGiamGia> findAllActive();
    
    @Query("SELECT d FROM DotGiamGia d WHERE d.deleted = false ORDER BY d.id DESC")  
    Page<DotGiamGia> findAllActive(Pageable pageable);
    
    @Query("SELECT d FROM DotGiamGia d WHERE d.deleted = false AND d.id = :id")
    DotGiamGia findActiveById(Integer id);
}
