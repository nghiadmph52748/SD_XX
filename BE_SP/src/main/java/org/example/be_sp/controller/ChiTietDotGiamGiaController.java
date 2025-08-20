package org.example.be_sp.controller;

import org.example.be_sp.model.request.ChiTietDotGiamGiaRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.ChiTietDotGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chi-tiet-dot-giam-gia-management")
public class ChiTietDotGiamGiaController {
    @Autowired
    private ChiTietDotGiamGiaService chiTietDotGiamGiaService;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(chiTietDotGiamGiaService.getAll(), "Hien thi thanh cong");
    }

    @GetMapping("/paging")
    public ResponseObject<?> getAllPaging(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseObject<>(chiTietDotGiamGiaService.paging(page, size), "Hien thi thanh cong");
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> getById(@PathVariable Integer id) {
        return new ResponseObject<>(chiTietDotGiamGiaService.getById(id), "Hien thi thanh cong");
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody ChiTietDotGiamGiaRequest request) {
        chiTietDotGiamGiaService.add(request);
        return new ResponseObject<>(null, "Them moi thanh cong");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@PathVariable Integer id, @RequestBody ChiTietDotGiamGiaRequest request) {
        chiTietDotGiamGiaService.update(id, request);
        return new ResponseObject<>(null, "Cap nhat thanh cong");
    }
}
