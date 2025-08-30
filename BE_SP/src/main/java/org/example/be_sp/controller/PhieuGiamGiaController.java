package org.example.be_sp.controller;

import org.example.be_sp.model.request.PhieuGiamGiaRequest;
import org.example.be_sp.model.response.ResponseObject;
import org.example.be_sp.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phieu-giam-gia-management")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class PhieuGiamGiaController {
    
    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping("/playlist")
    public ResponseObject<?> getAll() {
        return new ResponseObject<>(phieuGiamGiaService.getAll());
    }

    @GetMapping("/paging")
    public ResponseObject<?> paging(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        return new ResponseObject<>(phieuGiamGiaService.paging(page, size));
    }

    @GetMapping("/detail/{id}")
    public ResponseObject<?> detail(@PathVariable Integer id) {
        return new ResponseObject<>(phieuGiamGiaService.getById(id));
    }

    @PostMapping("/add")
    public ResponseObject<?> add(@RequestBody PhieuGiamGiaRequest request) {
        System.out.println("=== CONTROLLER ADD CALLED ===");
        System.out.println("Add request received");
        
        // Print individual fields since toString() isn't helpful
        if (request != null) {
            System.out.println("Request fields:");
            System.out.println("  tenPhieuGiamGia: " + request.getTenPhieuGiamGia());
            System.out.println("  trangThai: " + request.getTrangThai());
            System.out.println("  loaiPhieuGiamGia: " + request.getLoaiPhieuGiamGia());
            System.out.println("  giaTriGiamGia: " + request.getGiaTriGiamGia());
            System.out.println("  idKhachHang: " + (request.getIdKhachHang() != null ? 
                request.getIdKhachHang().size() + " customers" : "null"));
        }
        
        try {
            phieuGiamGiaService.add(request);
            System.out.println("✅ Controller add completed successfully");
            return new ResponseObject<>(null, "Thêm phiếu giảm giá thành công");
        } catch (Exception e) {
            System.err.println("❌ Error in controller add: " + e.getMessage());
            e.printStackTrace();
            return ResponseObject.error("Lỗi khi thêm phiếu giảm giá: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseObject<?> update(@RequestBody PhieuGiamGiaRequest request, @PathVariable Integer id) {
        System.out.println("=== CONTROLLER UPDATE CALLED ===");
        System.out.println("Update coupon ID: " + id);
        System.out.println("About to call service.update()");
        
        // Print individual fields since toString() isn't helpful
        if (request != null) {
            System.out.println("Request fields:");
            System.out.println("  tenPhieuGiamGia: " + request.getTenPhieuGiamGia());
            System.out.println("  trangThai: " + request.getTrangThai());
            System.out.println("  loaiPhieuGiamGia: " + request.getLoaiPhieuGiamGia());
            System.out.println("  giaTriGiamGia: " + request.getGiaTriGiamGia());
            System.out.println("  idKhachHang: " + (request.getIdKhachHang() != null ? 
                request.getIdKhachHang().size() + " customers" : "null"));
        }
        
        try {
            phieuGiamGiaService.update(id, request);
            System.out.println("✅ Service update completed successfully");
            return new ResponseObject<>(null, "Cập nhật phiếu giảm giá thành công");
        } catch (Exception e) {
            System.err.println("❌ Error updating coupon: " + e.getMessage());
            e.printStackTrace();
            return ResponseObject.error("Lỗi khi cập nhật phiếu giảm giá: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<?> delete(@PathVariable Integer id) {
        phieuGiamGiaService.delete(id);
        return new ResponseObject<>(null, "Xóa phiếu giảm giá thành công");
    }
    
    @PutMapping("/update/status/{id}")
    public ResponseObject<?> updateStatus(@PathVariable Integer id) {
        try {
            phieuGiamGiaService.updateStatus(id);
            return new ResponseObject<>(null, "Cập nhật trạng thái phiếu giảm giá thành công");
        } catch (Exception e) {
            System.err.println("Error updating coupon status: " + e.getMessage());
            e.printStackTrace();
            return ResponseObject.error("Lỗi khi cập nhật trạng thái phiếu giảm giá: " + e.getMessage());
        }
    }
    
    @GetMapping("/active-vouchers")
    public ResponseObject<?> getActiveVouchers() {
        return new ResponseObject<>(phieuGiamGiaService.getActiveVouchers());
    }
}