package org.example.be_sp.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PhieuGiamGiaRequest {
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;
    private Boolean loaiPhieuGiamGia; // 0 = percentage, 1 = fixed amount
    private BigDecimal giaTriGiamGia;
    private BigDecimal soTienToiDa;
    private BigDecimal hoaDonToiThieu;
    private Integer soLuongDung;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private Boolean trangThai;
    private String moTa;
    private List<Integer> idKhachHang; // Customer IDs for personal coupons
}