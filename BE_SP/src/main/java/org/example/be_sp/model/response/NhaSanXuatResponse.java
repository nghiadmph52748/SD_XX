package org.example.be_sp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.be_sp.entity.NhaSanXuat;

@Getter
@Setter
@NoArgsConstructor
public class NhaSanXuatResponse {
    private Integer id;
    private String maNhaSanXuat;
    private String tenNhaSanXuat;
    private Boolean deleted;

    public NhaSanXuatResponse(NhaSanXuat nhaSanXuat) {
        this.id = nhaSanXuat.getId();
        this.maNhaSanXuat = nhaSanXuat.getMaNhaSanXuat();
        this.tenNhaSanXuat = nhaSanXuat.getTenNhaSanXuat();
        this.deleted = nhaSanXuat.getDeleted();
    }
}
