package org.example.be_sp.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuGiamGiaRequest {
    String tenPhieuGiamGia;
    Boolean loaiPhieuGiamGia;
    BigDecimal giaTriGiamGia;
    BigDecimal soTienToiDa;
    BigDecimal hoaDonToiThieu;
    Integer soLuongDung;
    LocalDate ngayBatDau;
    LocalDate ngayKetThuc;
    Boolean trangThai;
    String moTa;
    Boolean deleted;
}
