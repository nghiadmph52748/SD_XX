package org.example.be_sp.model.response;

import java.math.BigDecimal;
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
        this.tenSanPham = s.getIdSanPham().getTenSanPham();
        this.tenMauSac = s.getIdMauSac().getTenMauSac();
        this.tenKichThuoc = s.getIdKichThuoc().getTenKichThuoc();
        this.tenDeGiay = s.getIdDeGiay().getTenDeGiay();
        this.tenChatLieu = s.getIdChatLieu().getTenChatLieu();
        this.tenDemGiay = s.getIdDemGiay().getTenDemGiay();
        this.tenTrongLuong = s.getIdTrongLuong().getTenTrongLuong();
        this.tenMonTheThao = s.getIdMonTheThao().getTenMonTheThao();
        this.tenLoaiMua = s.getIdLoaiMua().getTenLoaiMua();
        this.tenDoBen = s.getIdDoBen().getTenDoBen();
        this.tenChongNuoc = s.getIdChongNuoc().getTenChongNuoc();
        this.anhSanPham = s.getChiTietSanPhamAnhs().stream()
                .map(anh -> anh.getIdAnhSanPham().getDuongDanAnh())
                .collect(Collectors.toList());
        this.soLuong = s.getSoLuong();
        this.giaBan = s.getGiaBan();
        this.trangThai = s.getTrangThai();
    }
}
