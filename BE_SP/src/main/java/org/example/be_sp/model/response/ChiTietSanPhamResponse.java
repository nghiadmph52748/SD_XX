package org.example.be_sp.model.response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.example.be_sp.entity.ChiTietSanPham;
import org.example.be_sp.entity.DotGiamGia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietSanPhamResponse {
    Integer id;
    String duongDanAnh;
    String maSanPham;
    String tenSanPham;
    String tenNhaSanXuat;
    String tenXuatXu;
    String tenMauSac;
    String tenKichThuoc;
    String tenDeGiay;
    String tenChatLieu;
    String tenDemGiay;
    String tenTrongLuong;
    String tenMonTheThao;
    String tenLoaiMua;
    String tenDoBen;
    String tenChongNuoc;
    Integer soLuong;
    BigDecimal giaBan;
    String tenDotGiamGia;
    Integer giaTriGiamGia;
    BigDecimal giaSauGiam;
    Boolean trangThai;
    String ghiChu;
    Boolean deleted;
    LocalDate createAt;
    Integer createBy;
    LocalDate updateAt;
    Integer updateBy;

    public ChiTietSanPhamResponse(ChiTietSanPham s) {
        this.id = s.getId();
        // Xử lý ảnh một cách nhẹ nhàng, không throw exception nếu không có ảnh
        this.duongDanAnh = s.getChiTietSanPhamAnhs() != null ? 
            s.getChiTietSanPhamAnhs().stream()
                .filter(a -> !Boolean.TRUE.equals(a.getDeleted()))
                .map(a -> a.getIdAnhSanPham() != null ? a.getIdAnhSanPham().getDuongDanAnh() : null)
                .filter(duongDan -> duongDan != null)
                .findFirst()
                .orElse("") : "";
        
        // Kiểm tra null trước khi truy cập các thuộc tính
        if (s.getIdSanPham() != null) {
            this.maSanPham = s.getIdSanPham().getMaSanPham();
            this.tenSanPham = s.getIdSanPham().getTenSanPham();
            if (s.getIdSanPham().getIdNhaSanXuat() != null) {
                this.tenNhaSanXuat = s.getIdSanPham().getIdNhaSanXuat().getTenNhaSanXuat();
            }
            if (s.getIdSanPham().getIdXuatXu() != null) {
                this.tenXuatXu = s.getIdSanPham().getIdXuatXu().getTenXuatXu();
            }
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
        
        this.soLuong = s.getSoLuong();
        this.giaBan = s.getGiaBan();
        
        // Xử lý giảm giá
        DotGiamGia applied = null;
        if (s.getChiTietDotGiamGias() != null && !s.getChiTietDotGiamGias().isEmpty()) {
            LocalDate today = LocalDate.now();
            applied = s.getChiTietDotGiamGias().stream()
                    .filter(p -> !Boolean.TRUE.equals(p.getDeleted()))
                    .map(p -> p.getIdDotGiamGia())
                    .filter(dgg -> dgg != null
                            && !Boolean.TRUE.equals(dgg.getDeleted())
                            && Boolean.TRUE.equals(dgg.getTrangThai())
                            && (dgg.getNgayBatDau() == null || !today.isBefore(dgg.getNgayBatDau()))
                            && (dgg.getNgayKetThuc() == null || !today.isAfter(dgg.getNgayKetThuc())))
                    .findFirst()
                    .orElse(null);
        }
        this.tenDotGiamGia = applied != null ? applied.getTenDotGiamGia() : "Không có đợt giảm giá";
        this.giaTriGiamGia = applied != null && applied.getGiaTriGiamGia() != null ? applied.getGiaTriGiamGia() : 0;
        if (this.giaBan != null && this.giaTriGiamGia != null && this.giaTriGiamGia > 0) {
            BigDecimal discount = this.giaBan.multiply(BigDecimal.valueOf(this.giaTriGiamGia))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            this.giaSauGiam = this.giaBan.subtract(discount);
        } else {
            this.giaSauGiam = this.giaBan;
        }
        this.trangThai = s.getTrangThai();
        this.ghiChu = s.getGhiChu();
        this.deleted = s.getDeleted();
        this.createAt = s.getCreateAt();
        this.createBy = s.getCreateBy();
        this.updateAt = s.getUpdateAt();
        this.updateBy = s.getUpdateBy();
    }
}
