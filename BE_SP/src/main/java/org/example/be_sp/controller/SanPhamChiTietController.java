package org.example.be_sp.controller;

import org.example.be_sp.model.request.ChiTietSanPhamRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/san-pham-chi-tiet-management")
public class SanPhamChiTietController   {
    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(chiTietSanPhamService.getAll(), "Hiển thị thành công");
    }

    @GetMapping("/paging")
    public ResponseObject<?> phanTrang(@RequestParam(defaultValue = "0") Integer no, @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseObject<>(chiTietSanPhamService.paging(no, size), "Trang" + no);
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> getById(@RequestParam Integer id) {
        return new ResponseObject<>(chiTietSanPhamService.getById(id), "Hiển thị thành công");
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody ChiTietSanPhamRequest chiTietSanPhamRequest) {
        chiTietSanPhamService.add(chiTietSanPhamRequest);
        return new ResponseObject<>(null, "Thêm Thành Công");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@PathVariable("id") Integer id, @RequestBody ChiTietSanPhamRequest chiTietSanPhamRequest) {
        chiTietSanPhamService.update(chiTietSanPhamRequest, id);
        return new ResponseObject<>(null, "Cập nhật thành công");
    }
}
