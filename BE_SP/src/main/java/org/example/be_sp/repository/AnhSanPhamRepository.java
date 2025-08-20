package org.example.be_sp.repository;

import org.example.be_sp.entity.AnhSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham, Integer> {
    @Query("select n from AnhSanPham n where n.deleted = ?1")
    List<AnhSanPham> findAllByDeletedFalse(Boolean deleted);
}
