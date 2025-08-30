package org.example.be_sp.repository;

import org.example.be_sp.entity.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {
    ChiTietSanPham findChiTietSanPhamById(Integer id);
}
