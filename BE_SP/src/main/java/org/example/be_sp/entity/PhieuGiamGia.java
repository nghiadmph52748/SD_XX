package org.example.be_sp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ma_phieu_giam_gia", length = 8, updatable = false, insertable = false)
    private String maPhieuGiamGia;

    @Nationalized
    @Column(name = "ten_phieu_giam_gia")
    private String tenPhieuGiamGia;

    @ColumnDefault("0")
    @Column(name = "loai_phieu_giam_gia")
    private Boolean loaiPhieuGiamGia;

    @Column(name = "gia_tri_giam_gia", precision = 18, scale = 2)
    private BigDecimal giaTriGiamGia;

    @Column(name = "so_tien_toi_da", precision = 18, scale = 2)
    private BigDecimal soTienToiDa;

    @Column(name = "hoa_don_toi_thieu", precision = 18, scale = 2)
    private BigDecimal hoaDonToiThieu;

    @Column(name = "so_luong_dung")
    private Integer soLuongDung;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idPhieuGiamGia")
    @JsonIgnore
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idPhieuGiamGia")
    @JsonIgnore
    private Set<PhieuGiamGiaCaNhan> phieuGiamGiaCaNhans = new LinkedHashSet<>();

}