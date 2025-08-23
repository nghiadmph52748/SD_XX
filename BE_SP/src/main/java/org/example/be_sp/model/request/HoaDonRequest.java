package org.example.be_sp.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class HoaDonRequest {
    private Integer idKhachHang;
    private Integer idPhieuGiamGia;
    private Integer idNhanVien;
    private String tenHoaDon;
    private Boolean loaiDon;
    private BigDecimal phiVanChuyen;
    private BigDecimal tongTien;
    private BigDecimal tongTienSauGiam;
    private String ghiChu;
    private String tenNguoiNhan;
    private String diaChiNhanHang;
    private String soDienThoaiNguoiNhan;
    private String emailNguoiNhan;
    private LocalDate ngayTao;
    private LocalDate ngayThanhToan;
    private Boolean trangThai;
    private Boolean deleted;
}
