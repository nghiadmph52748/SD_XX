create database GearUp
go
use GearUp
go
create table xuat_xu(
	id int identity(1,1) primary key,
	ma_xuat_xu AS 'XX' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_xuat_xu nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table nha_san_xuat(
	id int identity(1,1) primary key,
	ma_nha_san_xuat AS 'NSX' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_nha_san_xuat nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table san_pham(
	id int identity(1,1) primary key,
	id_nha_san_xuat int not null,
	id_xuat_xu int not null,
	ma_san_pham AS 'SP' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_san_pham nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_nha_san_xuat) references nha_san_xuat(id),
	foreign key(id_xuat_xu) references xuat_xu(id)
)
go
create table anh_san_pham(
	id int identity(1,1) primary key,
	duong_dan_anh varchar(255),
	loai_anh varchar(10),
	mo_ta nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table mau_sac(
	id int identity(1,1) primary key,
	ma_mau_sac AS 'MS' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_mau_sac nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table kich_thuoc(
	id int identity(1,1) primary key,
	ma_kich_thuoc AS 'KT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_kich_thuoc nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table de_giay(
	id int identity(1,1) primary key,
	ma_de_giay AS 'DG' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_de_giay nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table chat_lieu(
	id int identity(1,1) primary key,
	ma_chat_lieu AS 'CL' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_chat_lieu nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table trong_luong(
	id int identity(1,1) primary key,
	ma_trong_luong AS 'TL' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_trong_luong nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table dot_giam_gia(
	id int identity(1,1) primary key,
	ma_dot_giam_gia AS 'DGG' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_dot_giam_gia nvarchar(255),
	gia_tri_giam_gia int CHECK (gia_tri_giam_gia >= 0),
	ngay_bat_dau date,
	ngay_ket_thuc date,
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	CHECK (ngay_ket_thuc > ngay_bat_dau)
)
go
create table chi_tiet_san_pham(
	id int identity(1,1) primary key,
	id_san_pham int not null,
	id_mau_sac int not null,
	id_kich_thuoc int not null,
	id_de_giay int not null,
	id_chat_lieu int not null,
	id_trong_luong int not null,
	so_luong int default 0 CHECK (so_luong >= 0),
	gia_ban decimal(18,2) CHECK (gia_ban >= 0),
	trang_thai bit default 1,
	ghi_chu nvarchar(255),
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_san_pham) references san_pham(id),
	foreign key(id_mau_sac) references mau_sac(id),
	foreign key(id_kich_thuoc) references kich_thuoc(id),
	foreign key(id_de_giay) references de_giay(id),
	foreign key(id_chat_lieu) references chat_lieu(id),
	foreign key(id_trong_luong) references trong_luong(id)
)
go
create table chi_tiet_san_pham_anh(
	id int identity(1,1) primary key,
	id_chi_tiet_san_pham int not null,
	id_anh_san_pham int not null,
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_chi_tiet_san_pham) references chi_tiet_san_pham(id),
	foreign key(id_anh_san_pham) references anh_san_pham(id)
)
go
create table chi_tiet_dot_giam_gia(
	id int identity(1,1) primary key,	
	id_dot_giam_gia int not null,
	id_chi_tiet_san_pham int not null,
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_dot_giam_gia) references dot_giam_gia(id),
	foreign key(id_chi_tiet_san_pham) references chi_tiet_san_pham(id)
)
go
create table quyen_han(
	id int identity(1,1) primary key,
	ma_quyen_han AS 'QH' + RIGHT('0' + CAST(ID AS VARCHAR(1)), 1) PERSISTED,
	ten_quyen_han nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table nhan_vien(
	id int identity(1,1) primary key,
	id_quyen_han int not null,
	ma_nhan_vien AS 'NV' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_nhan_vien nvarchar(255),
	ten_tai_khoan varchar(255) unique,
	mat_khau varchar(255),
	email varchar(255) unique,
	so_dien_thoai varchar(12),
	anh_nhan_vien varchar(255),
	ngay_sinh date,
	ghi_chu nvarchar(255),
	thanh_pho nvarchar(255),
	quan nvarchar(255),
	phuong varchar(255),
	dia_chi_cu_the varchar(255),
	cccd varchar(20),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_quyen_han) references quyen_han(id)
)
go
create table khach_hang(
	id int identity(1,1) primary key,
	ma_khach_hang AS 'KH' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_khach_hang nvarchar(255),
	ten_tai_khoan varchar(255) unique,
	mat_khau varchar(255),
	email varchar(255) unique,
	so_dien_thoai varchar(12),
	gioi_tinh bit, -- 0: Nữ, 1: Nam
	ngay_sinh date,
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null
)
go
create table dia_chi_khach_hang(
	id int identity(1,1) primary key,
	id_khach_hang int not null,
	ma_dia_chi AS 'DC' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_dia_chi nvarchar(255),
	thanh_pho nvarchar(255),
	quan nvarchar(255),
	phuong varchar(255),
	dia_chi_cu_the varchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	foreign key(id_khach_hang) references khach_hang(id)
)
go
create table phieu_giam_gia(
	id int identity(1,1) primary key,
	ma_phieu_giam_gia AS 'PGG' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_phieu_giam_gia nvarchar(255),
	loai_phieu_giam_gia bit default 0, -- 0: Giảm giá theo phần trăm, 1: Giảm giá theo số tiền
	gia_tri_giam_gia decimal(18,2) CHECK (gia_tri_giam_gia >= 0),
	so_tien_toi_da decimal(18,2) CHECK (so_tien_toi_da >= 0),
	hoa_don_toi_thieu decimal(18,2) CHECK (hoa_don_toi_thieu >= 0),
	so_luong_dung int CHECK (so_luong_dung >= 0),
	ngay_bat_dau date,
	ngay_ket_thuc date,
	trang_thai bit default 1,
	mo_ta nvarchar(255),
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	CHECK (ngay_ket_thuc > ngay_bat_dau)
)
go
create table phieu_giam_gia_ca_nhan(
	id int identity(1,1) primary key,
	id_khach_hang int not null,
	id_phieu_giam_gia int not null,
	ma_phieu_giam_gia_ca_nhan AS 'PGGCN' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_phieu_giam_gia_ca_nhan nvarchar(255),
	ngay_nhan date,
	ngay_het_han date,
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_khach_hang) references khach_hang(id),
	foreign key(id_phieu_giam_gia) references phieu_giam_gia(id),
	CHECK (ngay_het_han > ngay_nhan)
)
go
create table hoa_don(
	id int identity(1,1) primary key,
	id_khach_hang int not null,
	id_phieu_giam_gia int null,
	id_nhan_vien int null,
	ma_hoa_don AS 'HD' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_hoa_don nvarchar(255),
	loai_don bit default 0, -- 0: Đơn hàng trực tuyến, 1: Đơn hàng tại cửa hàng
	phi_van_chuyen decimal(18,2) CHECK (phi_van_chuyen >= 0),
	tong_tien decimal(18,2) CHECK (tong_tien >= 0),
	tong_tien_sau_giam decimal(18,2) CHECK (tong_tien_sau_giam >= 0),
	ghi_chu nvarchar(255),
	ten_khach_hang nvarchar(255),
	dia_chi_khach_hang nvarchar(255),
	so_dien_thoai_khach_hang varchar(12),
	email_khach_hang varchar(255),
	ngay_tao date,
	ngay_thanh_toan date,
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_khach_hang) references khach_hang(id),
	foreign key(id_phieu_giam_gia) references phieu_giam_gia(id),
	foreign key(id_nhan_vien) references nhan_vien(id),
	CHECK (ngay_thanh_toan >= ngay_tao),
	CHECK (tong_tien_sau_giam <= tong_tien)
)
go
create table hoa_don_chi_tiet(
	id int identity(1,1) primary key,
	id_hoa_don int not null,
	id_chi_tiet_san_pham int not null,
	ma_hoa_don_chi_tiet AS 'HDCT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	so_luong int default 0 CHECK (so_luong >= 0),
	gia_ban decimal(18,2) CHECK (gia_ban >= 0),
	thanh_tien decimal(18,2) CHECK (thanh_tien >= 0),
	trang_thai bit default 1,
	ghi_chu nvarchar(255),
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
	foreign key(id_hoa_don) references hoa_don(id),
	foreign key(id_chi_tiet_san_pham) references chi_tiet_san_pham(id)
)
go
create table phuong_thuc_thanh_toan(
	id int identity(1,1) primary key,
	ma_phuong_thuc_thanh_toan AS 'PTTT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_phuong_thuc_thanh_toan nvarchar(255),
	trang_thai bit default 1,	
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table hinh_thuc_thanh_toan(
	id int identity(1,1) primary key,
	id_hoa_don int not null,
	id_phuong_thuc_thanh_toan int not null,
	ma_hinh_thuc_thanh_toan AS 'HTTT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	tien_chuyen_khoan decimal(18,2) CHECK (tien_chuyen_khoan >= 0),
	tien_mat decimal(18,2) CHECK (tien_mat >= 0),
	trang_thai bit default 1,
	deleted bit default 0,
	foreign key(id_hoa_don) references hoa_don(id),
	foreign key(id_phuong_thuc_thanh_toan) references phuong_thuc_thanh_toan(id)
)
go
create table trang_thai_don_hang(
	id int identity(1,1) primary key,
	ma_trang_thai_don_hang AS 'TTDH' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_trang_thai_don_hang nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	create_at date null,
	create_by int null,
	update_at date null,
	update_by int null,
)
go
create table thong_tin_don_hang(
	id int identity(1,1) primary key,
	id_hoa_don int not null,
	id_trang_thai_don_hang int not null,
	ma_thong_tin_don_hang AS 'TTDH' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	thoi_gian datetime,
	ghi_chu nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0,
	foreign key(id_hoa_don) references hoa_don(id),
	foreign key(id_trang_thai_don_hang) references trang_thai_don_hang(id)
)
go

-- Thêm dữ liệu mẫu cơ bản
-- 1. Dữ liệu xuất xứ
insert into xuat_xu (ten_xuat_xu, trang_thai, deleted, create_at, create_by) values
(N'Việt Nam', 1, 0, GETDATE(), 1),
(N'Trung Quốc', 1, 0, GETDATE(), 1),
(N'Thái Lan', 1, 0, GETDATE(), 1),
(N'Indonesia', 1, 0, GETDATE(), 1),
(N'Malaysia', 1, 0, GETDATE(), 1)
go

-- 2. Dữ liệu nhà sản xuất
insert into nha_san_xuat (ten_nha_san_xuat, trang_thai, deleted, create_at, create_by) values
(N'Nike', 1, 0, GETDATE(), 1),
(N'Adidas', 1, 0, GETDATE(), 1),
(N'Puma', 1, 0, GETDATE(), 1),
(N'New Balance', 1, 0, GETDATE(), 1),
(N'Converse', 1, 0, GETDATE(), 1)
go

-- 3. Dữ liệu sản phẩm
insert into san_pham (id_nha_san_xuat, id_xuat_xu, ten_san_pham, trang_thai, deleted, create_at, create_by) values
(1, 1, N'Giày Nike Air Max 270', 1, 0, GETDATE(), 1),
(2, 2, N'Giày Adidas Ultraboost 22', 1, 0, GETDATE(), 1),
(3, 3, N'Giày Puma RS-X', 1, 0, GETDATE(), 1),
(4, 4, N'Giày New Balance 574', 1, 0, GETDATE(), 1),
(5, 5, N'Giày Converse Chuck Taylor', 1, 0, GETDATE(), 1)
go

-- 4. Dữ liệu màu sắc
insert into mau_sac (ten_mau_sac, trang_thai, deleted, create_at, create_by) values
(N'Đen', 1, 0, GETDATE(), 1),
(N'Trắng', 1, 0, GETDATE(), 1),
(N'Đỏ', 1, 0, GETDATE(), 1),
(N'Xanh dương', 1, 0, GETDATE(), 1),
(N'Xanh lá', 1, 0, GETDATE(), 1)
go

-- 5. Dữ liệu kích thước
insert into kich_thuoc (ten_kich_thuoc, trang_thai, deleted, create_at, create_by) values
(N'39', 1, 0, GETDATE(), 1),
(N'40', 1, 0, GETDATE(), 1),
(N'41', 1, 0, GETDATE(), 1),
(N'42', 1, 0, GETDATE(), 1),
(N'43', 1, 0, GETDATE(), 1)
go

-- 6. Dữ liệu đế giày
insert into de_giay (ten_de_giay, trang_thai, deleted, create_at, create_by) values
(N'Đế cao su', 1, 0, GETDATE(), 1),
(N'Đế EVA', 1, 0, GETDATE(), 1),
(N'Đế PU', 1, 0, GETDATE(), 1),
(N'Đế TPU', 1, 0, GETDATE(), 1),
(N'Đế phylon', 1, 0, GETDATE(), 1)
go

-- 7. Dữ liệu chất liệu
insert into chat_lieu (ten_chat_lieu, trang_thai, deleted, create_at, create_by) values
(N'Da tổng hợp', 1, 0, GETDATE(), 1),
(N'Vải canvas', 1, 0, GETDATE(), 1),
(N'Vải mesh', 1, 0, GETDATE(), 1),
(N'Da thật', 1, 0, GETDATE(), 1),
(N'Vải knit', 1, 0, GETDATE(), 1)
go

-- 8. Dữ liệu trọng lượng
insert into trong_luong (ten_trong_luong, trang_thai, deleted, create_at, create_by) values
(N'250g', 1, 0, GETDATE(), 1),
(N'300g', 1, 0, GETDATE(), 1),
(N'350g', 1, 0, GETDATE(), 1),
(N'400g', 1, 0, GETDATE(), 1),
(N'450g', 1, 0, GETDATE(), 1)
go

-- 9. Dữ liệu chi tiết sản phẩm
insert into chi_tiet_san_pham (id_san_pham, id_mau_sac, id_kich_thuoc, id_de_giay, id_chat_lieu, id_trong_luong, so_luong, gia_ban, trang_thai, ghi_chu, deleted, create_at, create_by) values
(1, 1, 1, 1, 1, 1, 50, 2500000, 1, N'Giày thể thao cao cấp', 0, GETDATE(), 1),
(1, 2, 2, 1, 1, 1, 45, 2500000, 1, N'Giày thể thao cao cấp', 0, GETDATE(), 1),
(2, 3, 3, 2, 2, 2, 40, 3200000, 1, N'Giày chạy bộ công nghệ cao', 0, GETDATE(), 1),
(3, 4, 4, 3, 3, 3, 35, 1800000, 1, N'Giày thời trang thể thao', 0, GETDATE(), 1),
(4, 5, 5, 4, 4, 4, 30, 2200000, 1, N'Giày casual phong cách', 0, GETDATE(), 1)
go

-- 10. Dữ liệu ảnh sản phẩm
insert into anh_san_pham (duong_dan_anh, loai_anh, mo_ta, trang_thai, deleted, create_at, create_by) values
('/images/nike-airmax-270-black.jpg', 'main', N'Ảnh chính giày Nike đen', 1, 0, GETDATE(), 1),
('/images/nike-airmax-270-white.jpg', 'main', N'Ảnh chính giày Nike trắng', 1, 0, GETDATE(), 1),
('/images/adidas-ultraboost-red.jpg', 'main', N'Ảnh chính giày Adidas đỏ', 1, 0, GETDATE(), 1),
('/images/puma-rsx-blue.jpg', 'main', N'Ảnh chính giày Puma xanh', 1, 0, GETDATE(), 1),
('/images/newbalance-574-green.jpg', 'main', N'Ảnh chính giày New Balance xanh lá', 1, 0, GETDATE(), 1)
go

-- 11. Dữ liệu chi tiết ảnh sản phẩm
insert into chi_tiet_san_pham_anh (id_chi_tiet_san_pham, id_anh_san_pham, trang_thai, deleted, create_at, create_by) values
(1, 1, 1, 0, GETDATE(), 1),
(2, 2, 1, 0, GETDATE(), 1),
(3, 3, 1, 0, GETDATE(), 1),
(4, 4, 1, 0, GETDATE(), 1),
(5, 5, 1, 0, GETDATE(), 1)
go

-- 12. Dữ liệu quyền hạn
insert into quyen_han (ten_quyen_han, trang_thai, deleted, create_at, create_by) values
(N'Admin', 1, 0, GETDATE(), 1),
(N'Nhân viên bán hàng', 1, 0, GETDATE(), 1),
(N'Nhân viên kho', 1, 0, GETDATE(), 1),
(N'Quản lý', 1, 0, GETDATE(), 1)
go

-- 13. Dữ liệu nhân viên
insert into nhan_vien (id_quyen_han, ten_nhan_vien, ten_tai_khoan, mat_khau, email, so_dien_thoai, ngay_sinh, ghi_chu, thanh_pho, quan, phuong, dia_chi_cu_the, cccd, trang_thai, deleted, create_at, create_by) values
(1, N'Nguyễn Văn Admin', 'admin', 'admin123', 'admin@gearup.com', '0123456789', '1990-01-01', N'Quản trị viên hệ thống', N'Hà Nội', N'Cầu Giấy', N'Dịch Vọng', N'Số 1 Đường ABC', '123456789012', 1, 0, GETDATE(), 1),
(2, N'Trần Thị Bán Hàng', 'banhang', 'banhang123', 'banhang@gearup.com', '0987654321', '1995-05-15', N'Nhân viên bán hàng', N'Hà Nội', N'Đống Đa', N'Láng Hạ', N'Số 2 Đường XYZ', '987654321098', 1, 0, GETDATE(), 1),
(3, N'Lê Văn Kho', 'kho', 'kho123', 'kho@gearup.com', '0111222333', '1992-08-20', N'Nhân viên quản lý kho', N'Hà Nội', N'Thanh Xuân', N'Thanh Xuân Bắc', N'Số 3 Đường DEF', '456789123012', 1, 0, GETDATE(), 1)
go

-- 14. Dữ liệu khách hàng
insert into khach_hang (ten_khach_hang, ten_tai_khoan, mat_khau, email, so_dien_thoai, gioi_tinh, ngay_sinh, trang_thai, deleted, create_at, create_by) values
(N'Phạm Văn Khách', 'khach1', 'khach123', 'khach1@gmail.com', '0123456780', 1, '1998-03-10', 1, 0, GETDATE(), 1),
(N'Hoàng Thị Mua', 'khach2', 'khach123', 'khach2@gmail.com', '0987654320', 0, '1996-07-25', 1, 0, GETDATE(), 1),
(N'Vũ Đức Mua', 'khach3', 'khach123', 'khach3@gmail.com', '0111222000', 1, '1994-12-05', 1, 0, GETDATE(), 1)
go

-- 15. Dữ liệu địa chỉ khách hàng
insert into dia_chi_khach_hang (id_khach_hang, ten_dia_chi, thanh_pho, quan, phuong, dia_chi_cu_the, trang_thai, deleted) values
(1, N'Địa chỉ nhà riêng', N'Hà Nội', N'Ba Đình', N'Phúc Xá', N'Số 10 Ngõ ABC', 1, 0),
(2, N'Địa chỉ công ty', N'Hà Nội', N'Hai Bà Trưng', N'Bạch Mai', N'Số 20 Đường XYZ', 1, 0),
(3, N'Địa chỉ nhà bố mẹ', N'Hà Nội', N'Tây Hồ', N'Phú Thượng', N'Số 30 Ngõ DEF', 1, 0)
go

-- 16. Dữ liệu phiếu giảm giá
insert into phieu_giam_gia (ten_phieu_giam_gia, loai_phieu_giam_gia, gia_tri_giam_gia, so_tien_toi_da, hoa_don_toi_thieu, so_luong_dung, ngay_bat_dau, ngay_ket_thuc, trang_thai, mo_ta, deleted, create_at, create_by) values
(N'Giảm giá 10% cho đơn hàng đầu tiên', 0, 10.00, 500000, 1000000, 100, GETDATE(), DATEADD(month, 3, GETDATE()), 1, N'Áp dụng cho khách hàng mới', 0, GETDATE(), 1),
(N'Giảm giá 200.000đ cho đơn hàng từ 2 triệu', 1, 200000, 200000, 2000000, 50, GETDATE(), DATEADD(month, 2, GETDATE()), 1, N'Áp dụng cho tất cả khách hàng', 0, GETDATE(), 1),
(N'Giảm giá 15% cho giày thể thao', 0, 15.00, 300000, 1500000, 30, GETDATE(), DATEADD(month, 1, GETDATE()), 1, N'Chỉ áp dụng cho giày thể thao', 0, GETDATE(), 1)
go

-- 17. Dữ liệu phiếu giảm giá cá nhân
insert into phieu_giam_gia_ca_nhan (id_khach_hang, id_phieu_giam_gia, ten_phieu_giam_gia_ca_nhan, ngay_nhan, ngay_het_han, trang_thai, deleted, create_at, create_by) values
(1, 1, N'Phiếu giảm giá cá nhân - Khách 1', GETDATE(), DATEADD(month, 3, GETDATE()), 1, 0, GETDATE(), 1),
(2, 2, N'Phiếu giảm giá cá nhân - Khách 2', GETDATE(), DATEADD(month, 2, GETDATE()), 1, 0, GETDATE(), 1),
(3, 3, N'Phiếu giảm giá cá nhân - Khách 3', GETDATE(), DATEADD(month, 1, GETDATE()), 1, 0, GETDATE(), 1)
go

-- 18. Dữ liệu đợt giảm giá
insert into dot_giam_gia (ten_dot_giam_gia, gia_tri_giam_gia, ngay_bat_dau, ngay_ket_thuc, trang_thai, deleted, create_at, create_by) values
(N'Khuyến mãi mùa hè', 20, GETDATE(), DATEADD(month, 2, GETDATE()), 1, 0, GETDATE(), 1),
(N'Khuyến mãi cuối năm', 25, DATEADD(month, 1, GETDATE()), DATEADD(month, 3, GETDATE()), 1, 0, GETDATE(), 1),
(N'Khuyến mãi sinh nhật', 30, GETDATE(), DATEADD(month, 1, GETDATE()), 1, 0, GETDATE(), 1)
go

-- 19. Dữ liệu chi tiết đợt giảm giá
insert into chi_tiet_dot_giam_gia (id_dot_giam_gia, id_chi_tiet_san_pham, trang_thai, deleted, create_at, create_by) values
(1, 1, 1, 0, GETDATE(), 1),
(1, 2, 1, 0, GETDATE(), 1),
(2, 3, 1, 0, GETDATE(), 1),
(3, 4, 1, 0, GETDATE(), 1),
(3, 5, 1, 0, GETDATE(), 1)
go

-- 20. Dữ liệu phương thức thanh toán
insert into phuong_thuc_thanh_toan (ten_phuong_thuc_thanh_toan, trang_thai, deleted, create_at, create_by) values
(N'Tiền mặt', 1, 0, GETDATE(), 1),
(N'Chuyển khoản ngân hàng', 1, 0, GETDATE(), 1),
(N'Ví điện tử', 1, 0, GETDATE(), 1),
(N'Thẻ tín dụng', 1, 0, GETDATE(), 1)
go

-- 21. Dữ liệu trạng thái đơn hàng
insert into trang_thai_don_hang (ten_trang_thai_don_hang, trang_thai, deleted, create_at, create_by) values
(N'Chờ xác nhận', 1, 0, GETDATE(), 1),
(N'Đã xác nhận', 1, 0, GETDATE(), 1),
(N'Đang xử lý', 1, 0, GETDATE(), 1),
(N'Đang giao hàng', 1, 0, GETDATE(), 1),
(N'Đã giao hàng', 1, 0, GETDATE(), 1),
(N'Đã hủy', 1, 0, GETDATE(), 1)
go

-- 22. Dữ liệu hóa đơn
insert into hoa_don (id_khach_hang, id_phieu_giam_gia, id_nhan_vien, ten_hoa_don, loai_don, phi_van_chuyen, tong_tien, tong_tien_sau_giam, ghi_chu, ten_khach_hang, dia_chi_khach_hang, so_dien_thoai_khach_hang, email_khach_hang, ngay_tao, ngay_thanh_toan, trang_thai, deleted, create_at, create_by) values
(1, 1, 2, N'Đơn hàng giày Nike', 0, 30000, 2530000, 2277000, N'Giao hàng trong ngày', N'Phạm Văn Khách', N'Số 10 Ngõ ABC, Phúc Xá, Ba Đình, Hà Nội', '0123456780', 'khach1@gmail.com', GETDATE(), GETDATE(), 1, 0, GETDATE(), 1),
(2, 2, 2, N'Đơn hàng giày Adidas', 0, 30000, 3230000, 3030000, N'Giao hàng sáng mai', N'Hoàng Thị Mua', N'Số 20 Đường XYZ, Bạch Mai, Hai Bà Trưng, Hà Nội', '0987654320', 'khach2@gmail.com', GETDATE(), GETDATE(), 1, 0, GETDATE(), 1),
(3, 3, 2, N'Đơn hàng giày Puma', 1, 0, 1800000, 1530000, N'Mua tại cửa hàng', N'Vũ Đức Mua', N'Số 30 Ngõ DEF, Phú Thượng, Tây Hồ, Hà Nội', '0111222000', 'khach3@gmail.com', GETDATE(), GETDATE(), 1, 0, GETDATE(), 1)
go

-- 23. Dữ liệu hóa đơn chi tiết
insert into hoa_don_chi_tiet (id_hoa_don, id_chi_tiet_san_pham, so_luong, gia_ban, thanh_tien, trang_thai, ghi_chu, deleted, create_at, create_by) values
(1, 1, 1, 2500000, 2500000, 1, N'Giày Nike Air Max 270 đen size 39', 0, GETDATE(), 1),
(2, 3, 1, 3200000, 3200000, 1, N'Giày Adidas Ultraboost 22 đỏ size 41', 0, GETDATE(), 1),
(3, 4, 1, 1800000, 1800000, 1, N'Giày Puma RS-X xanh size 42', 0, GETDATE(), 1)
go

-- 24. Dữ liệu hình thức thanh toán
insert into hinh_thuc_thanh_toan (id_hoa_don, id_phuong_thuc_thanh_toan, tien_chuyen_khoan, tien_mat, trang_thai, deleted) values
(1, 2, 2277000, 0, 1, 0),
(2, 2, 3030000, 0, 1, 0),
(3, 1, 0, 1530000, 1, 0)
go

-- 25. Dữ liệu thông tin đơn hàng
insert into thong_tin_don_hang (id_hoa_don, id_trang_thai_don_hang, thoi_gian, ghi_chu, trang_thai, deleted) values
(1, 1, GETDATE(), N'Đơn hàng mới được tạo', 1, 0),
(1, 2, DATEADD(minute, 5, GETDATE()), N'Đơn hàng đã được xác nhận', 1, 0),
(1, 3, DATEADD(minute, 10, GETDATE()), N'Đơn hàng đang được xử lý', 1, 0),
(2, 1, GETDATE(), N'Đơn hàng mới được tạo', 1, 0),
(2, 2, DATEADD(minute, 3, GETDATE()), N'Đơn hàng đã được xác nhận', 1, 0),
(3, 1, GETDATE(), N'Đơn hàng mới được tạo', 1, 0),
(3, 5, DATEADD(minute, 15, GETDATE()), N'Đơn hàng đã hoàn thành', 1, 0)
go
