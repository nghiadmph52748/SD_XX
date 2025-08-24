package org.example.be_sp.controller;

import org.example.be_sp.model.request.NhanVienRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nhan-vien-management")
@CrossOrigin(origins = "*")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/playlist")
    public ResponseObject<?> getAllNhanVien() {
        return new ResponseObject<>(nhanVienService.getAllNhanVien());
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> getNhanVienById(@PathVariable Integer id) {
        return new ResponseObject<>(nhanVienService.getNhanVienById(id));
    }

    @GetMapping("/detail/email/{email}")
    public ResponseObject<?> getNhanVienByEmail(@PathVariable String email) {
        return new ResponseObject<>(nhanVienService.getNhanVienByEmail(email));
    }

    @GetMapping("/detail/nickname/{tenTaiKhoan}")
    public ResponseObject<?> getNhanVienByTenTaiKhoan(@PathVariable String tenTaiKhoan) {
        return new ResponseObject<>(nhanVienService.getNhanVienByTenTaiKhoan(tenTaiKhoan));
    }

    @PostMapping("/add")
    public ResponseObject<?> createNhanVien(@RequestBody NhanVienRequest request) {
        nhanVienService.saveNhanVien(request);
        return new ResponseObject<>(null, "Add success");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> updateNhanVien(@PathVariable Integer id, @RequestBody NhanVienRequest request) {
        nhanVienService.updateNhanVien(id, request);
        return new ResponseObject<>(null, "Update success");
    }

    @PutMapping("/update/status/{id}")
    public ResponseObject<?> update(@PathVariable Integer id) {
        nhanVienService.updateStatus(id);
        return new ResponseObject<>(null, "Update status success");
    }
}
