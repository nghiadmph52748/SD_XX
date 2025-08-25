package org.example.be_sp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.be_sp.entity.KhachHang;
import org.example.be_sp.model.DiaChi;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangResponse {
    private Integer id;
    private String maKhachHang;
    private String tenKhachHang;
    private String email;
    private String soDienThoai;
    private Boolean gioiTinh;
    private LocalDate ngaySinh;
    private Boolean deleted;
    private List<DiaChi> listDiaChi;

    public KhachHangResponse(KhachHang e) {
        this.id = e.getId();
        this.maKhachHang = e.getMaKhachHang();
        this.tenKhachHang = e.getTenKhachHang();
        this.email = e.getEmail();
        this.soDienThoai = e.getSoDienThoai();
        this.gioiTinh = e.getGioiTinh();
        this.ngaySinh = e.getNgaySinh();
        this.deleted = e.getDeleted();
        this.listDiaChi = e.getDiaChiKhachHangs().stream().map(diaChi -> {
            if (!diaChi.getDeleted()) {
                return new DiaChi(diaChi.getDiaChiCuThe(), diaChi.getThanhPho(), diaChi.getQuan(), diaChi.getPhuong());
            }
            return null;
        }).toList();
    }
}
