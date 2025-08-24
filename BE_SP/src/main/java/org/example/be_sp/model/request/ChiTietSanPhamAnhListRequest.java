package org.example.be_sp.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietSanPhamAnhListRequest {
    Integer idChiTietSanPham;
    List<Integer> idAnhSanPhamList;
    Boolean deleted;
}
