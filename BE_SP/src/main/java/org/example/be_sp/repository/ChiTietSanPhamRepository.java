package org.example.be_sp.repository;

import java.util.List;

import org.example.be_sp.entity.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {
    ChiTietSanPham findChiTietSanPhamById(Integer id);
    
    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
           "JOIN FETCH ctsp.idSanPham sp " +
           "JOIN FETCH ctsp.idMauSac ms " +
           "JOIN FETCH ctsp.idKichThuoc kt " +
           "JOIN FETCH ctsp.idDeGiay dg " +
           "JOIN FETCH ctsp.idChatLieu cl " +
           "JOIN FETCH ctsp.idDemGiay dmg " +
           "JOIN FETCH ctsp.idTrongLuong tl " +
           "JOIN FETCH ctsp.idMonTheThao mtt " +
           "JOIN FETCH ctsp.idLoaiMua lmu " +
           "JOIN FETCH ctsp.idDoBen db " +
           "JOIN FETCH ctsp.idChongNuoc cn " +
           "WHERE ctsp.deleted = false OR ctsp.deleted IS NULL")
    List<ChiTietSanPham> findAllWithValidData();
}
