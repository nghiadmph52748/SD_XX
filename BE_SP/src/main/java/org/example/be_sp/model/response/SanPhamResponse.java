package org.example.be_sp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.be_sp.entity.ChiTietSanPham;
import org.example.be_sp.entity.SanPham;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SanPhamResponse {
    private Integer id;
    private String maSanPham;
    private String tenSanPham;
    private Integer soLuongBienThe;
    private Boolean trangThai;
    private Boolean deleted;
    private Integer idNhaSanXuat;
    private Integer idXuatXu;
    private LocalDate createAt;
    private Integer createBy;
    private LocalDate updateAt;
    private Integer updateBy;

    public SanPhamResponse(SanPham sp) {
        this.id = sp.getId();
        this.maSanPham = sp.getMaSanPham();
        this.tenSanPham = sp.getTenSanPham();
        this.soLuongBienThe = sp.getChiTietSanPhams().size();
        this.trangThai = sp.getTrangThai();
        this.deleted = sp.getDeleted();
        this.idNhaSanXuat = sp.getIdNhaSanXuat().getId();
        this.idXuatXu = sp.getIdXuatXu().getId();
        this.createAt = sp.getCreateAt();
        this.createBy = sp.getCreateBy();
        this.updateAt = sp.getUpdateAt();
        this.updateBy = sp.getUpdateBy();
    }
}
