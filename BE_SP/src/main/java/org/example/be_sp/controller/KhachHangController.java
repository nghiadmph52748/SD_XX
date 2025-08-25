package org.example.be_sp.controller;

import org.example.be_sp.model.request.KhachHangRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/khach-hang-management")
@CrossOrigin(origins = "*")
public class KhachHangController {
    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(khachHangService.findAll());
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> getById(@PathVariable Integer id) {
        return new ResponseObject<>(khachHangService.findById(id));
    }

    @PostMapping("/add")
    public ResponseObject<?> create(@RequestBody KhachHangRequest request) {
        khachHangService.save(request);
        return new ResponseObject<>(null, "Thêm khách hàng thành công!");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@PathVariable Integer id, @RequestBody KhachHangRequest request) {
        khachHangService.update(id, request);
        return new ResponseObject<>(null, "Cập nhật khách hàng thành công!");
    }

    @PutMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@PathVariable Integer id) {
        khachHangService.updateStatus(id);
        return new ResponseObject<>(null, "Xoá khách hàng thành công!");
    }
}