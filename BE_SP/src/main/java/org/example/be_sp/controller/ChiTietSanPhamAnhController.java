package org.example.be_sp.controller;

import java.util.List;

import org.example.be_sp.entity.ChiTietSanPhamAnh;
import org.example.be_sp.model.request.ChiTietSanPhamAnhListRequest;
import org.example.be_sp.model.request.ChiTietSanPhamAnhRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.ChiTietSanPhamAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chi-tiet-san-pham-anh-management")
@CrossOrigin(origins = "*")
public class ChiTietSanPhamAnhController {
    @Autowired
    ChiTietSanPhamAnhService service;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(service.getAll());
    }

    @GetMapping("/paging")
    public ResponseObject<?> paging(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseObject<>(service.paging(page, size));
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> detail(@PathVariable Integer id) {
        return new ResponseObject<>(service.getById(id));
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody ChiTietSanPhamAnhRequest request) {
        ChiTietSanPhamAnh result = service.add(request);
        return new ResponseObject<>(result, "Thêm thành công");
    }

    @PostMapping("/add-multiple")
    public ResponseObject<?> addMultiple(@RequestBody ChiTietSanPhamAnhListRequest request) {
        List<ChiTietSanPhamAnh> result = service.addMultiple(request);
        return new ResponseObject<>(result, "Thêm nhiều ảnh thành công");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@RequestBody ChiTietSanPhamAnhRequest request, @PathVariable Integer id) {
        service.update(id, request);
        return new ResponseObject<>(null, "Cập nhật thành công");
    }

    @PutMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@PathVariable Integer id) {
        service.updateStatus(id);
        return new ResponseObject<>(null, "Xoá thành công");
    }
}
