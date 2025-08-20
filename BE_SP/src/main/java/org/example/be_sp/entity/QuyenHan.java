package org.example.be_sp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "quyen_han")
public class QuyenHan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'QH'+right('0'+CONVERT([varchar](1), [ID]), 1)")
    @Column(name = "ma_quyen_han", length = 3)
    private String maQuyenHan;

    @Nationalized
    @Column(name = "ten_quyen_han")
    private String tenQuyenHan;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idQuyenHan")
    private Set<NhanVien> nhanViens = new LinkedHashSet<>();

}