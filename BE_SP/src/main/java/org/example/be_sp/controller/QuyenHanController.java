package org.example.be_sp.controller;

import org.example.be_sp.model.request.QuyenHanRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.QuyenHanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/quyen-han-management")
public class QuyenHanController {
    @Autowired
    private QuyenHanService service;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(service.getAll());
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> getById(@PathVariable("id") Integer id) {
        return new ResponseObject<>(service.getById(id));
    }

    @GetMapping("/paging")
    public ResponseObject<?> paging(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return new ResponseObject<>(service.paging(page, size));
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody QuyenHanRequest request) {
        service.add(request);
        return new ResponseObject<>(null,"Thêm mới quyền hạn thành công");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@RequestBody QuyenHanRequest request, @PathVariable("id") Integer id) {
        service.update(id, request);
        return new ResponseObject<>(null, "Cập nhật quyền hạn thành công");
    }

    @PutMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@RequestBody QuyenHanRequest request, @PathVariable("id") Integer id) {
        service.updateStatus(id);
        return new ResponseObject<>(null, "Xoá quyền hạn thành công");
    }
}
