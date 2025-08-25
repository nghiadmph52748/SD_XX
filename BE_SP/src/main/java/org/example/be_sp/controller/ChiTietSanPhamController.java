package org.example.be_sp.controller;

import org.example.be_sp.model.request.ChiTietSanPhamRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chi-tiet-san-pham-management")
@CrossOrigin(origins = "*")
public class ChiTietSanPhamController {
    @Autowired
    ChiTietSanPhamService service;

    @GetMapping("/paging")
    public ResponseObject<?> pagingAll(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseObject<>(service.paging(page, size));
    }

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(service.getAll());
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> detail(@PathVariable Integer id) {
        return new ResponseObject<>(service.getById(id));
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody ChiTietSanPhamRequest request) {
        Integer id = service.add(request);
        return new ResponseObject<>(id, "Thêm chi tiết sản phẩm thành công");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@RequestBody ChiTietSanPhamRequest request, @PathVariable Integer id) {
        service.update(request, id);
        return new ResponseObject<>(null, "Cập nhật chi tiết sản phẩm thành công");
    }

    @GetMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@PathVariable Integer id) {
        service.updateStatus(id);
        return new ResponseObject<>(null, "Cập nhật trạng thái chi tiết sản phẩm thành công");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return new ResponseObject<>(null, "Xóa chi tiết sản phẩm thành công");
    }

    @GetMapping("/full-info")
    public ResponseObject<?> getAllWithFullInfo() {
        return new ResponseObject<>(service.getAllWithFullInfo());
    }

    @GetMapping("/full-info/paging")
    public ResponseObject<?> pagingWithFullInfo(@RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseObject<>(service.pagingWithFullInfo(page, size));
    }
}
