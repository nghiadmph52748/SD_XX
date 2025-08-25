package org.example.be_sp.service;

import java.util.List;

import org.example.be_sp.entity.ChiTietSanPham;
import org.example.be_sp.exception.ApiException;
import org.example.be_sp.model.request.ChiTietSanPhamRequest;
import org.example.be_sp.model.response.ChiTietSanPhamFullResponse;
import org.example.be_sp.model.response.ChiTietSanPhamResponse;
import org.example.be_sp.model.response.PagingResponse;
import org.example.be_sp.repository.ChatLieuRepository;
import org.example.be_sp.repository.ChiTietSanPhamRepository;
import org.example.be_sp.repository.ChongNuocRepository;
import org.example.be_sp.repository.DeGiayRepository;
import org.example.be_sp.repository.DemGiayRepository;
import org.example.be_sp.repository.DoBenRepository;
import org.example.be_sp.repository.KichThuocRepository;
import org.example.be_sp.repository.LoaiMuaRepository;
import org.example.be_sp.repository.MauSacRepository;
import org.example.be_sp.repository.MonTheThaoRepository;
import org.example.be_sp.repository.SanPhamRepository;
import org.example.be_sp.repository.TrongLuongRepository;
import org.example.be_sp.util.MapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChiTietSanPhamService {
    private static final Logger logger = LoggerFactory.getLogger(ChiTietSanPhamService.class);
    @Autowired
    ChiTietSanPhamRepository repository;
    @Autowired
    SanPhamRepository sanPham;
    @Autowired
    MauSacRepository mauSac;
    @Autowired
    KichThuocRepository kichThuoc;
    @Autowired
    DeGiayRepository deGiay;
    @Autowired
    ChatLieuRepository chatLieu;
    @Autowired
    DemGiayRepository demGiay;
    @Autowired
    TrongLuongRepository trongLuong;
    @Autowired
    MonTheThaoRepository monTheThao;
    @Autowired
    LoaiMuaRepository loaiMua;
    @Autowired
    DoBenRepository doBen;
    @Autowired
    ChongNuocRepository chongNuoc;

    public List<ChiTietSanPhamResponse> getAll() {
        logger.info("Bắt đầu lấy tất cả chi tiết sản phẩm");
        try {
            List<ChiTietSanPham> allChiTietSanPham = repository.findAllWithValidData();
            logger.info("Tìm thấy {} chi tiết sản phẩm hợp lệ từ database", allChiTietSanPham.size());
            
            List<ChiTietSanPhamResponse> result = allChiTietSanPham.stream()
                    .map(ChiTietSanPhamResponse::new)
                    .toList();
            
            logger.info("Trả về {} chi tiết sản phẩm", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Lỗi khi lấy tất cả chi tiết sản phẩm: {}", e.getMessage(), e);
            throw e;
        }
    }

    public ChiTietSanPhamResponse getById(Integer id) {
        return repository.findById(id).map(ChiTietSanPhamResponse::new).orElseThrow(() -> new ApiException("Chi tiết sản phẩm không tồn tại", "404"));
    }

    public PagingResponse<ChiTietSanPhamResponse> paging(Integer page, Integer size) {
        return new PagingResponse<>(repository.findAll(PageRequest.of(page, size)).map(ChiTietSanPhamResponse::new), page);
    }

    public Integer add(ChiTietSanPhamRequest request) {
        ChiTietSanPham c = MapperUtils.map(request, ChiTietSanPham.class);
        c.setIdSanPham(sanPham.findSanPhamById(request.getIdSanPham()));
        c.setIdMauSac(mauSac.findMauSacById(request.getIdMauSac()));
        c.setIdKichThuoc(kichThuoc.findKichThuocById(request.getIdKichThuoc()));
        c.setIdDeGiay(deGiay.findDeGiayById(request.getIdDeGiay()));
        c.setIdChatLieu(chatLieu.findChatLieuById(request.getIdChatLieu()));
        c.setIdDemGiay(demGiay.findDemGiayById(request.getIdDemGiay()));
        c.setIdTrongLuong(trongLuong.findTrongLuongById(request.getIdTrongLuong()));
        c.setIdMonTheThao(monTheThao.findMonTheThaoById(request.getIdMonTheThao()));
        c.setIdLoaiMua(loaiMua.findLoaiMuaById(request.getIdLoaiMua()));
        c.setIdDoBen(doBen.findDoBenById(request.getIdDoBen()));
        c.setIdChongNuoc(chongNuoc.findChongNuocById(request.getIdChongNuoc()));
        ChiTietSanPham saved = repository.save(c);
        return saved.getId();
    }

    public void update(ChiTietSanPhamRequest request, Integer id) {
        ChiTietSanPham e = MapperUtils.map(request, ChiTietSanPham.class);
        e.setId(id);
        e.setIdSanPham(sanPham.findSanPhamById(request.getIdSanPham()));
        e.setIdMauSac(mauSac.findMauSacById(request.getIdMauSac()));
        e.setIdKichThuoc(kichThuoc.findKichThuocById(request.getIdKichThuoc()));
        e.setIdDeGiay(deGiay.findDeGiayById(request.getIdDeGiay()));
        e.setIdChatLieu(chatLieu.findChatLieuById(request.getIdChatLieu()));
        e.setIdDemGiay(demGiay.findDemGiayById(request.getIdDemGiay()));
        e.setIdTrongLuong(trongLuong.findTrongLuongById(request.getIdTrongLuong()));
        e.setIdMonTheThao(monTheThao.findMonTheThaoById(request.getIdMonTheThao()));
        e.setIdLoaiMua(loaiMua.findLoaiMuaById(request.getIdLoaiMua()));
        e.setIdDoBen(doBen.findDoBenById(request.getIdDoBen()));
        e.setIdChongNuoc(chongNuoc.findChongNuocById(request.getIdChongNuoc()));
        repository.save(e);
    }

    public void updateStatus(Integer id) {
        ChiTietSanPham chiTietSanPham = repository.findById(id).orElseThrow(() -> new ApiException("Chi tiết sản phẩm không tồn tại", "404"));
        chiTietSanPham.setDeleted(true);
        repository.save(chiTietSanPham);
    }

    @Transactional
    public void delete(Integer id) {
        ChiTietSanPham chiTietSanPham = repository.findById(id).orElseThrow(() -> new ApiException("Chi tiết sản phẩm không tồn tại", "404"));
        
        // Xóa chi tiết sản phẩm (các bản ghi liên quan sẽ được xử lý bởi cascade hoặc database constraints)
        repository.delete(chiTietSanPham);
    }

    public List<ChiTietSanPhamFullResponse> getAllWithFullInfo() {
        return repository.findAllWithValidData().stream()
                .map(ChiTietSanPhamFullResponse::new)
                .toList();
    }

    public PagingResponse<ChiTietSanPhamFullResponse> pagingWithFullInfo(Integer page, Integer size) {
        return new PagingResponse<>(
                repository.findAll(PageRequest.of(page, size))
                        .map(ChiTietSanPhamFullResponse::new), 
                page
        );
    }
}
