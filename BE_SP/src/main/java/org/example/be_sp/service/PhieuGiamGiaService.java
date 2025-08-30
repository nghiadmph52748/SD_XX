package org.example.be_sp.service;

import org.example.be_sp.entity.PhieuGiamGia;
import org.example.be_sp.entity.PhieuGiamGiaCaNhan;
import org.example.be_sp.entity.KhachHang;
import org.example.be_sp.model.request.PhieuGiamGiaRequest;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.model.response.PhieuGiamGiaResponse;
import org.example.be_sp.repository.PhieuGiamGiaRepository;
import org.example.be_sp.repository.PhieuGiamGiaCaNhanRepository;
import org.example.be_sp.repository.KhachHangRepository;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaService {
    
    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;
    
    @Autowired
    private PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;
    
    @Autowired
    private KhachHangRepository khachHangRepository;
    
    public List<PhieuGiamGiaResponse> getAll() {
        List<PhieuGiamGia> list = phieuGiamGiaRepository.findAllActive();
        return list.stream()
                .map(item -> MapperUtils.map(item, PhieuGiamGiaResponse.class))
                .collect(Collectors.toList());
    }
    
    public PagingResponse<PhieuGiamGiaResponse> paging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PhieuGiamGia> pageData = phieuGiamGiaRepository.findAllActive(pageable);
        
        Page<PhieuGiamGiaResponse> responsePage = pageData.map(item -> MapperUtils.map(item, PhieuGiamGiaResponse.class));
        
        return new PagingResponse<>(responsePage, page);
    }
    
    public PhieuGiamGiaResponse getById(Integer id) {
        PhieuGiamGia entity = phieuGiamGiaRepository.findActiveById(id);
        if (entity == null) {
            throw new RuntimeException("Không tìm thấy phiếu giảm giá với id: " + id);
        }
        return MapperUtils.map(entity, PhieuGiamGiaResponse.class);
    }

    public PhieuGiamGia getEntityById(Integer id) {
        return phieuGiamGiaRepository.findActiveById(id);
    }
    
    @Transactional
    public void add(PhieuGiamGiaRequest request) {
        System.out.println("🔥 ADD COUPON SERVICE METHOD CALLED");
        System.out.println("Request received: " + (request != null ? "not null" : "null"));
        
        try {
            PhieuGiamGia entity = MapperUtils.map(request, PhieuGiamGia.class);
            entity.setDeleted(false);
            entity.setTrangThai(true);
            
            // CRITICAL: Don't set maPhieuGiamGia at all since it's updatable=false
            entity.setMaPhieuGiamGia(null);
            
            System.out.println("About to save new coupon entity...");
            System.out.println("Entity fields: name=" + entity.getTenPhieuGiamGia() + 
                             ", value=" + entity.getGiaTriGiamGia() + 
                             ", type=" + entity.getLoaiPhieuGiamGia());
            
            // Log customer information
            if (request.getIdKhachHang() != null && !request.getIdKhachHang().isEmpty()) {
                System.out.println("🎯 Personal coupon with " + request.getIdKhachHang().size() + " customers");
            } else {
                System.out.println("🌐 Public coupon");
            }
            
            // Save the entity - database should generate the ma_phieu_giam_gia automatically
            PhieuGiamGia savedEntity = phieuGiamGiaRepository.save(entity);
            
            System.out.println("✅ Coupon created successfully with ID: " + savedEntity.getId());
            System.out.println("Generated maPhieuGiamGia: " + savedEntity.getMaPhieuGiamGia());
            
            // Create personal coupon assignments if customers are selected
            if (request.getIdKhachHang() != null && !request.getIdKhachHang().isEmpty()) {
                System.out.println("📋 Creating personal coupon assignments...");
                createPersonalCoupons(savedEntity, request.getIdKhachHang());
                System.out.println("✅ Personal coupon assignments created successfully");
            }
            
        } catch (Exception e) {
            System.err.println("💥 EXCEPTION IN ADD SERVICE:");
            System.err.println("Exception type: " + e.getClass().getSimpleName());
            System.err.println("Exception message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm phiếu giảm giá: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public void update(Integer id, PhieuGiamGiaRequest request) {
        System.out.println("🔥 SERVICE UPDATE METHOD CALLED");
        System.out.println("Service received ID: " + id);
        System.out.println("Service received request: " + (request != null ? "not null" : "null"));
        
        try {
            PhieuGiamGia existing = phieuGiamGiaRepository.findActiveById(id);
            System.out.println("🔍 Found existing coupon: " + (existing != null ? existing.getId() : "null"));
            
            if (existing == null) {
                System.out.println("❌ No coupon found with id: " + id);
                throw new RuntimeException("Không tìm thấy phiếu giảm giá với id: " + id);
            }
            
            System.out.println("=== UPDATE COUPON DEBUG ===");
            System.out.println("Coupon ID: " + id);
            System.out.println("Current trangThai: " + existing.getTrangThai());
            System.out.println("New trangThai: " + request.getTrangThai());
            System.out.println("About to update fields...");
            
            // Update fields one by one with proper null handling
            // DO NOT update maPhieuGiamGia - it's a computed column in database
            existing.setTenPhieuGiamGia(request.getTenPhieuGiamGia());
            existing.setLoaiPhieuGiamGia(request.getLoaiPhieuGiamGia());
            existing.setGiaTriGiamGia(request.getGiaTriGiamGia());
            existing.setSoTienToiDa(request.getSoTienToiDa());
            existing.setHoaDonToiThieu(request.getHoaDonToiThieu());
            existing.setSoLuongDung(request.getSoLuongDung());
            existing.setNgayBatDau(request.getNgayBatDau());
            existing.setNgayKetThuc(request.getNgayKetThuc());
            existing.setTrangThai(request.getTrangThai());
            existing.setMoTa(request.getMoTa());
            // Explicitly set deleted to false to ensure it's not soft-deleted
            existing.setDeleted(false);
            // Keep existing maPhieuGiamGia unchanged (computed column)
            
            System.out.println("🔧 Updated all fields, about to save...");
            
            PhieuGiamGia saved = phieuGiamGiaRepository.save(existing);
            System.out.println("After save trangThai: " + saved.getTrangThai());
            
            // Update personal coupon assignments
            updatePersonalCoupons(saved, request.getIdKhachHang());
            
            System.out.println("=== END UPDATE DEBUG ===");
        } catch (Exception e) {
            System.err.println("💥 EXCEPTION IN SERVICE UPDATE:");
            System.err.println("Exception type: " + e.getClass().getSimpleName());
            System.err.println("Exception message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật phiếu giảm giá: " + e.getMessage(), e);
        }
    }
    
    public void updateStatus(Integer id) {
        try {
            PhieuGiamGia existing = phieuGiamGiaRepository.findActiveById(id);
            if (existing == null) {
                throw new RuntimeException("Không tìm thấy phiếu giảm giá với id: " + id);
            }
            
            System.out.println("=== UPDATE STATUS DEBUG ===");
            System.out.println("Coupon ID: " + id);
            System.out.println("Current trangThai: " + existing.getTrangThai());
            
            // Toggle status
            existing.setTrangThai(!existing.getTrangThai());
            PhieuGiamGia saved = phieuGiamGiaRepository.save(existing);
            
            System.out.println("New trangThai: " + saved.getTrangThai());
            System.out.println("=== END STATUS UPDATE DEBUG ===");
        } catch (Exception e) {
            System.err.println("Status update error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật trạng thái phiếu giảm giá: " + e.getMessage(), e);
        }
    }
    
    public void delete(Integer id) {
        PhieuGiamGia existing = phieuGiamGiaRepository.findActiveById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy phiếu giảm giá với id: " + id);
        }
        
        existing.setDeleted(true);
        phieuGiamGiaRepository.save(existing);
    }
    
    public List<PhieuGiamGiaResponse> getActiveVouchers() {
        LocalDate currentDate = LocalDate.now();
        List<PhieuGiamGia> list = phieuGiamGiaRepository.findActiveVouchers(currentDate);
        return list.stream()
                .map(item -> MapperUtils.map(item, PhieuGiamGiaResponse.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Creates personal coupon assignments for selected customers
     * @param coupon The main coupon entity
     * @param customerIds List of customer IDs to assign the coupon to
     */
    private void createPersonalCoupons(PhieuGiamGia coupon, List<Integer> customerIds) {
        System.out.println("🎯 Creating personal coupons for " + customerIds.size() + " customers");
        
        for (Integer customerId : customerIds) {
            try {
                // Get the customer entity
                KhachHang customer = khachHangRepository.findById(customerId).orElse(null);
                if (customer == null) {
                    System.err.println("⚠️ Customer not found with ID: " + customerId);
                    continue;
                }
                
                // Create personal coupon assignment
                PhieuGiamGiaCaNhan personalCoupon = new PhieuGiamGiaCaNhan();
                personalCoupon.setIdKhachHang(customer);
                personalCoupon.setIdPhieuGiamGia(coupon);
                personalCoupon.setTenPhieuGiamGiaCaNhan(coupon.getTenPhieuGiamGia() + " - " + customer.getTenKhachHang());
                personalCoupon.setNgayNhan(LocalDate.now());
                personalCoupon.setNgayHetHan(coupon.getNgayKetThuc());
                personalCoupon.setTrangThai(true);
                personalCoupon.setDeleted(false);
                
                PhieuGiamGiaCaNhan saved = phieuGiamGiaCaNhanRepository.save(personalCoupon);
                System.out.println("✅ Created personal coupon for customer: " + customer.getTenKhachHang() + 
                                 " (ID: " + saved.getId() + ")");
                
            } catch (Exception e) {
                System.err.println("❌ Error creating personal coupon for customer " + customerId + ": " + e.getMessage());
                e.printStackTrace();
                // Continue with other customers even if one fails
            }
        }
    }
    
    /**
     * Updates personal coupon assignments for a coupon
     * @param coupon The main coupon entity
     * @param customerIds List of customer IDs to assign the coupon to
     */
    private void updatePersonalCoupons(PhieuGiamGia coupon, List<Integer> customerIds) {
        System.out.println("🔄 Updating personal coupons for coupon ID: " + coupon.getId());
        
        // Get existing personal coupons
        List<PhieuGiamGiaCaNhan> existingPersonalCoupons = phieuGiamGiaCaNhanRepository.findByPhieuGiamGiaId(coupon.getId());
        
        // Mark all existing as deleted first
        for (PhieuGiamGiaCaNhan existing : existingPersonalCoupons) {
            existing.setDeleted(true);
            phieuGiamGiaCaNhanRepository.save(existing);
        }
        
        // Create new personal coupons if customers are provided
        if (customerIds != null && !customerIds.isEmpty()) {
            createPersonalCoupons(coupon, customerIds);
        }
        
        System.out.println("✅ Personal coupons updated successfully");
    }
}