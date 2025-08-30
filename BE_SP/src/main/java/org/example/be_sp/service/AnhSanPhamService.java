package org.example.be_sp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.example.be_sp.entity.AnhSanPham;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.AnhSanPhamRequest;
import org.example.be_sp.model.response.AnhSanPhamResponse;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.repository.AnhSanPhamRepository;
import org.example.be_sp.util.GenericCrudService;
import org.example.be_sp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnhSanPhamService extends GenericCrudService<AnhSanPham, Integer, AnhSanPhamResponse, AnhSanPhamRequest> {
    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;

    // Thư mục lưu file upload
    private static final String UPLOAD_DIR = "uploads/images/";

    public AnhSanPhamService(Class<AnhSanPham> entity, Class<AnhSanPhamResponse> anhSanPhamResponseClass, Class<AnhSanPhamRequest> anhSanPhamRequestClass, JpaRepository<AnhSanPham, Integer> anhSanPhamRepository) {
        super(entity, anhSanPhamResponseClass, anhSanPhamRequestClass, anhSanPhamRepository);
    }

    public List<AnhSanPhamResponse> getAllAnhSanPham() {
        return anhSanPhamRepository.findAllByDeletedFalse(false).stream().map(AnhSanPhamResponse::new).toList();
    }

    public PagingResponse<AnhSanPhamResponse> pagingAnhSanPham(int page, int size) {
        return new PagingResponse<>(anhSanPhamRepository.findAll(PageRequest.of(page, size)), page);
    }

    public AnhSanPhamResponse getAnhSanPhamById(int id) {
        return anhSanPhamRepository.findById(id).map(AnhSanPhamResponse::new).orElseThrow(() -> new ApiException("Không tìm thấy ảnh sản phẩm với id: " + id, "404"));
    }

    public void updateStatus(int id) {
        AnhSanPham existing = anhSanPhamRepository.findById(id).orElseThrow(() -> new ApiException("Không tìm thấy ảnh sản phẩm với id: " + id, "404"));
        existing.setDeleted(true);
        anhSanPhamRepository.save(existing);
    }

    /**
     * Thêm ảnh sản phẩm mới và trả về entity đã lưu
     */
    public AnhSanPham addAnhSanPham(AnhSanPhamRequest request) {
        try {
            AnhSanPham entity = MapperUtils.map(request, AnhSanPham.class);
            AnhSanPham savedEntity = anhSanPhamRepository.save(entity);
            System.out.println("✅ Đã tạo ảnh sản phẩm với ID: " + savedEntity.getId());
            return savedEntity;
        } catch (Exception e) {
            System.err.println("❌ Lỗi tạo ảnh sản phẩm: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cập nhật ảnh sản phẩm và trả về entity đã cập nhật
     */
    public AnhSanPham updateAnhSanPham(int id, AnhSanPhamRequest request) {
        AnhSanPham entity = MapperUtils.map(request, AnhSanPham.class);
        if (!anhSanPhamRepository.existsById(id)) {
            throw new ApiException("Không tìm thấy ảnh sản phẩm với id: " + id, "404");
        }
        entity.setId(id);
        return anhSanPhamRepository.save(entity);
    }

    /**
     * Upload file và trả về đường dẫn
     */
    public String uploadFile(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Tạo tên file duy nhất
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + fileExtension;

        // Lưu file
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        Files.write(filePath, file.getBytes());

        // Trả về đường dẫn tương đối để lưu vào database
        // Sử dụng đường dẫn tương đối để dễ dàng serve
        return "uploads/images/" + filename;
    }
}
