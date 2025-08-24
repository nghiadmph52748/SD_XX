package org.example.be_sp.controller;

import org.example.be_sp.model.request.PhieuGiamGiaRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phieu-giam-gia-management")
@CrossOrigin(origins = "*")
public class PhieuGiamGiaController {
    @Autowired
    PhieuGiamGiaService service;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(service.getAll());
    }

    @GetMapping("/paging")
    public ResponseObject<?> paging(@RequestParam Integer page, @RequestParam Integer size) {
        return new ResponseObject<>(service.paging(page, size));
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> detail(@PathVariable Integer id) {
        return new ResponseObject<>(service.getByIdResponse(id));
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody PhieuGiamGiaRequest request) {
        service.add(request);
        return new ResponseObject<>(null, "Add success");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@RequestBody PhieuGiamGiaRequest request, @PathVariable Integer id) {
        service.update(id, request);
        return new ResponseObject<>(null, "Update success");
    }

    @PutMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@PathVariable Integer id) {
        service.updateStatus(id);
        return new ResponseObject<>(null, "Update status success");
    }
}
