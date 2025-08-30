package org.example.be_sp.controller;


import org.example.be_sp.model.request.MauSacRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mau-sac-management")
@CrossOrigin(origins = "*")
public class MauSacController {
    @Autowired
    MauSacService mauSacService;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(mauSacService.getAll());
    }

    @GetMapping("/paging")
    public ResponseObject<?> paging(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return new ResponseObject<>(mauSacService.paging(page, size));
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> detail(@PathVariable int id) {
        return new ResponseObject<>(mauSacService.getById(id));
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody MauSacRequest mauSacRequest) {
        mauSacService.add(mauSacRequest);
        return new ResponseObject<>(null, "Thêm mới màu sắc thành công");
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@PathVariable Integer id, @RequestBody MauSacRequest mauSacRequest) {
        mauSacService.update(id, mauSacRequest);
        return new ResponseObject<>(null, "Cập nhật màu sắc thành công");
    }
}
