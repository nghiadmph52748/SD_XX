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
    private List<String> anhSanPham;
    private String tenNhaSanXuat;
    private String tenXuatXu;
    private String tenDeGiay;
    private String tenChatLieu;
    private String tenMauSac;
    private String tenKichThuoc;
    private String tenTrongLuong;
    private Integer soLuong;
    private BigDecimal giaBan;
    private Integer idDotGiamGia;
    private String tenDotGiamGia;
    private Integer giaTriGiamGia;
    private Boolean trangThai;

    public ChiTietSanPhamFullResponse(ChiTietSanPham s) {
        this.id = s.getId();
        if (s.getChiTietSanPhamAnhs() != null) {
            this.anhSanPham = s.getChiTietSanPhamAnhs().stream()
                    .filter(anh -> anh != null && anh.getIdAnhSanPham() != null && anh.getTrangThai() == true && anh.getDeleted() == false)
                    .map(anh -> anh.getIdAnhSanPham().getDuongDanAnh())
                    .filter(duongDan -> duongDan != null)
                    .collect(Collectors.toList());
        } else {
            this.anhSanPham = Collections.emptyList();
        }
        if (s.getIdSanPham().getIdNhaSanXuat() != null) {
            this.tenNhaSanXuat = s.getIdSanPham().getIdNhaSanXuat().getTenNhaSanXuat();
        } else {
            this.tenNhaSanXuat = null;
        }
        if (s.getIdSanPham().getIdXuatXu() != null) {
            this.tenXuatXu = s.getIdSanPham().getIdXuatXu().getTenXuatXu();
        } else {
            this.tenXuatXu = null;
        }
        if (s.getIdDeGiay() != null) {
            this.tenDeGiay = s.getIdDeGiay().getTenDeGiay();
        } else {
            this.tenDeGiay = null;
        }
        if (s.getIdChatLieu() != null) {
            this.tenChatLieu = s.getIdChatLieu().getTenChatLieu();
        } else {
            this.tenChatLieu = null;
        }
        if (s.getIdMauSac() != null) {
            this.tenMauSac = s.getIdMauSac().getTenMauSac();
        } else {
            this.tenMauSac = null;
        }
        if (s.getIdKichThuoc() != null) {
            this.tenKichThuoc = s.getIdKichThuoc().getTenKichThuoc();
        } else {
            this.tenKichThuoc = null;
        }
        if (s.getIdTrongLuong() != null) {
            this.tenTrongLuong = s.getIdTrongLuong().getTenTrongLuong();
        } else {
            this.tenTrongLuong = null;
        }
        this.soLuong = s.getSoLuong();
        this.giaBan = s.getGiaBan();
        this.trangThai = s.getTrangThai();
        if (s.getChiTietDotGiamGias() != null) {
            this.tenDotGiamGia = s.getChiTietDotGiamGias().stream().filter(ct -> ct != null && ct.getIdDotGiamGia() != null && ct.getDeleted() == false && ct.getTrangThai() == true)
                    .map(ct -> ct.getIdDotGiamGia().getTenDotGiamGia())
                    .filter(ten -> ten != null)
                    .findFirst()
                    .orElse(null);
            this.giaTriGiamGia = s.getChiTietDotGiamGias().stream().filter(ct -> ct != null && ct.getIdDotGiamGia() != null && ct.getDeleted() == false && ct.getTrangThai() == true)
                    .map(ct -> ct.getIdDotGiamGia().getGiaTriGiamGia())
                    .filter(giaTri -> giaTri != null)
                    .findFirst()
                    .orElse(null);
        } else {
            this.tenDotGiamGia = null;
            this.giaTriGiamGia = null;
        }
        if (s.getChiTietDotGiamGias() != null) {
            this.idDotGiamGia = s.getChiTietDotGiamGias().stream().filter(ct -> ct != null && ct.getIdDotGiamGia() != null && ct.getDeleted() == false && ct.getTrangThai() == true)
                    .map(ct -> ct.getIdDotGiamGia().getId())
                    .filter(id -> id != null)
                    .findFirst()
                    .orElse(null);
        } else {
            this.idDotGiamGia = null;
        }
    }
}
