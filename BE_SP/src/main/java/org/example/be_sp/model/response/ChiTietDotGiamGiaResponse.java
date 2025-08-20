package org.example.be_sp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.be_sp.entity.ChiTietDotGiamGia;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietDotGiamGiaResponse {
    Integer id;
    Integer idDotGiamGia;
    Integer idChiTietSanPham;
    Boolean deleted;

    public ChiTietDotGiamGiaResponse(ChiTietDotGiamGia c) {
        this.id = c.getId();
        this.idDotGiamGia = c.getIdDotGiamGia().getId();
        this.idChiTietSanPham = c.getIdChiTietSanPham().getId();
        this.deleted = c.getDeleted();
    }
}
