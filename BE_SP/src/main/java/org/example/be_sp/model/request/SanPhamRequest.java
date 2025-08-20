package org.example.be_sp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.be_sp.entity.NhaSanXuat;
import org.example.be_sp.entity.XuatXu;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SanPhamRequest {
    private Integer idNhaSanXuat;
    private Integer idXuatXu;
    private String tenSanPham;
    private Boolean deleted;
    private LocalDate createAt;
    private Integer createBy;
    private LocalDate updateAt;
    private Integer updateBy;
}
