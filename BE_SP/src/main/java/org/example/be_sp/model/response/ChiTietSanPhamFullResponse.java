package org.example.be_sp.model.response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.example.be_sp.entity.ChiTietSanPham;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietSanPhamFullResponse {
    private Integer id;
    private String tenSanPham;
    private String tenMauSac;
    private String tenKichThuoc;
    private String tenDeGiay;
    private String tenChatLieu;
    private String tenDemGiay;
    private String tenTrongLuong;
    private String tenMonTheThao;
    private String tenLoaiMua;
    private String tenDoBen;
    private String tenChongNuoc;
    private List<String> anhSanPham;
    private Integer soLuong;
    private BigDecimal giaBan;
    private Boolean trangThai;

    public ChiTietSanPhamFullResponse(ChiTietSanPham s) {
        this.id = s.getId();
        
        // Kiểm tra null trước khi truy cập các thuộc tính
        if (s.getIdSanPham() != null) {
            this.tenSanPham = s.getIdSanPham().getTenSanPham();
        }
        if (s.getIdMauSac() != null) {
            this.tenMauSac = s.getIdMauSac().getTenMauSac();
        }
        if (s.getIdKichThuoc() != null) {
            this.tenKichThuoc = s.getIdKichThuoc().getTenKichThuoc();
        }
        if (s.getIdDeGiay() != null) {
            this.tenDeGiay = s.getIdDeGiay().getTenDeGiay();
        }
        if (s.getIdChatLieu() != null) {
            this.tenChatLieu = s.getIdChatLieu().getTenChatLieu();
        }
        if (s.getIdDemGiay() != null) {
            this.tenDemGiay = s.getIdDemGiay().getTenDemGiay();
        }
        if (s.getIdTrongLuong() != null) {
            this.tenTrongLuong = s.getIdTrongLuong().getTenTrongLuong();
        }
        if (s.getIdMonTheThao() != null) {
            this.tenMonTheThao = s.getIdMonTheThao().getTenMonTheThao();
        }
        if (s.getIdLoaiMua() != null) {
            this.tenLoaiMua = s.getIdLoaiMua().getTenLoaiMua();
        }
        if (s.getIdDoBen() != null) {
            this.tenDoBen = s.getIdDoBen().getTenDoBen();
        }
        if (s.getIdChongNuoc() != null) {
            this.tenChongNuoc = s.getIdChongNuoc().getTenChongNuoc();
        }
        
        // Xử lý ảnh sản phẩm một cách an toàn
        if (s.getChiTietSanPhamAnhs() != null) {
            this.anhSanPham = s.getChiTietSanPhamAnhs().stream()
                    .filter(anh -> anh != null && anh.getIdAnhSanPham() != null)
                    .map(anh -> anh.getIdAnhSanPham().getDuongDanAnh())
                    .filter(duongDan -> duongDan != null)
                    .collect(Collectors.toList());
        } else {
            this.anhSanPham = Collections.emptyList();
        }
        
        this.soLuong = s.getSoLuong();
        this.giaBan = s.getGiaBan();
        this.trangThai = s.getTrangThai();
    }
}
