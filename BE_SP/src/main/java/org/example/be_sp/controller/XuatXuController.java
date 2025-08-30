package org.example.be_sp.controller;

import jakarta.validation.Valid;
import org.example.be_sp.model.request.XuatXuRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.XuatXuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xuat-xu-management")
@CrossOrigin(origins = "*")
public class XuatXuController {
    @Autowired
    XuatXuService xuatXuService;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(xuatXuService.getAllXuatXu());
    }

    @GetMapping("/paging")
    public ResponseObject<?> paging(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseObject<>(xuatXuService.paging(page, size));
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> detail(@PathVariable Integer id) {
        return new ResponseObject<>(xuatXuService.getById(id));
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody @Valid XuatXuRequest xuatXuRequest) {
        xuatXuService.add(xuatXuRequest);
        return new ResponseObject<>(null,"Thêm xuất xứ thành công");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@RequestBody @Valid XuatXuRequest xuatXuRequest, @PathVariable Integer id) {
        xuatXuService.update(id, xuatXuRequest);
        return new ResponseObject<>(null, "Cập nhật xuất xứ thành công");
    }

    @PutMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@PathVariable Integer id) {
        xuatXuService.updateStatus(id);
        return new ResponseObject<>(null, "Cập nhật trạng thái xuất xứ thành công");
    }
}
