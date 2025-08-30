package org.example.be_sp.controller;

import org.example.be_sp.model.request.ChiTietDotGiamGiaRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.ChiTietDotGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chi-tiet-dot-giam-gia-management")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class ChiTietDotGiamGiaController {
    @Autowired
    ChiTietDotGiamGiaService service;

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
    public ResponseObject<?> add(@RequestBody ChiTietDotGiamGiaRequest request) {
        service.add(request);
        return new ResponseObject<>(null, "Thêm chi tiết đợt giảm giá thành công");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@RequestBody ChiTietDotGiamGiaRequest request, @PathVariable Integer id) {
        service.update(id, request);
        return new ResponseObject<>(null, "Cập nhật chi tiết đợt giảm giá thành công");
    }

    @PutMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@PathVariable Integer id) {
        try {
            service.updateStatus(id);
            return new ResponseObject<>(null, "Cập nhật trạng thái chi tiết đợt giảm giá thành công");
        } catch (Exception e) {
            System.err.println("Error updating chi tiet dot giam gia status: " + e.getMessage());
            e.printStackTrace();
            return ResponseObject.error("Lỗi khi cập nhật trạng thái chi tiết đợt giảm giá: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return new ResponseObject<>(null, "Xóa chi tiết đợt giảm giá thành công");
        } catch (Exception e) {
            System.err.println("Error deleting chi tiet dot giam gia: " + e.getMessage());
            e.printStackTrace();
            return ResponseObject.error("Lỗi khi xóa chi tiết đợt giảm giá: " + e.getMessage());
        }
    }
}
