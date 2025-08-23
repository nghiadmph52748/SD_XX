create database GearUp
go
use GearUp
go
create table xuat_xu(
	id int identity(1,1) primary key,
	ma_xuat_xu AS 'XX' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_xuat_xu nvarchar(255),
	deleted bit default 0,
)
go
create table nha_san_xuat(
	id int identity(1,1) primary key,
	ma_nha_san_xuat AS 'NSX' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_nha_san_xuat nvarchar(255),
	deleted bit default 0,
)
go
create table san_pham(
	id int identity(1,1) primary key,
	id_nha_san_xuat int not null,
	id_xuat_xu int not null,
	ma_san_pham AS 'SP' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_san_pham nvarchar(255),
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
	deleted bit default 0,
)
go
create table mau_sac(
	id int identity(1,1) primary key,
	ma_mau_sac AS 'MS' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_mau_sac nvarchar(255),
	deleted bit default 0,
)
go
create table kich_thuoc(
	id int identity(1,1) primary key,
	ma_kich_thuoc AS 'KT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_kich_thuoc nvarchar(255),
	deleted bit default 0,
)
go
create table de_giay(
	id int identity(1,1) primary key,
	ma_de_giay AS 'DG' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_de_giay nvarchar(255),
	deleted bit default 0,
)
go
create table chat_lieu(
	id int identity(1,1) primary key,
	ma_chat_lieu AS 'CL' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_chat_lieu nvarchar(255),
	deleted bit default 0,
)
go
create table dem_giay(
	id int identity(1,1) primary key,
	ma_dem_giay AS 'ĐG' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_dem_giay nvarchar(255),
	deleted bit default 0,
)
go
create table trong_luong(
	id int identity(1,1) primary key,
	ma_trong_luong AS 'TL' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_trong_luong nvarchar(255),
	deleted bit default 0,
)
go
create table mon_the_thao(
	id int identity(1,1) primary key,
	ma_mon_the_thao AS 'MTT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_mon_the_thao nvarchar(255),
	deleted bit default 0,
)
go
create table loai_mua(
	id int identity(1,1) primary key,
	ma_loai_mua AS 'LM' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_loai_mua nvarchar(255),
	deleted bit default 0,
)
go
create table do_ben(
	id int identity(1,1) primary key,
	ma_do_ben AS 'DB' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_do_ben nvarchar(255),
	deleted bit default 0,
)
go
create table chong_nuoc(
	id int identity(1,1) primary key,
	ma_chong_nuoc AS 'CN' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_chong_nuoc nvarchar(255),
	deleted bit default 0,
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
	id_dem_giay int not null,
	id_trong_luong int not null,
	id_mon_the_thao int not null,
	id_loai_mua int not null,
	id_do_ben int not null,
	id_chong_nuoc int not null,
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
	foreign key(id_dem_giay) references dem_giay(id),
	foreign key(id_trong_luong) references trong_luong(id),
	foreign key(id_mon_the_thao) references mon_the_thao(id),
	foreign key(id_loai_mua) references loai_mua(id),
	foreign key(id_do_ben) references do_ben(id),
	foreign key(id_chong_nuoc) references chong_nuoc(id)
)
go
create table chi_tiet_san_pham_anh(
	id int identity(1,1) primary key,
	id_chi_tiet_san_pham int not null,
	id_anh_san_pham int not null,
	deleted bit default 0,
	foreign key(id_chi_tiet_san_pham) references chi_tiet_san_pham(id),
	foreign key(id_anh_san_pham) references anh_san_pham(id)
)
go
create table chi_tiet_dot_giam_gia(
	id int identity(1,1) primary key,
	id_dot_giam_gia int not null,
	id_chi_tiet_san_pham int not null,
	deleted bit default 0,
	foreign key(id_dot_giam_gia) references dot_giam_gia(id),
	foreign key(id_chi_tiet_san_pham) references chi_tiet_san_pham(id)
)
go
create table quyen_han(
	id int identity(1,1) primary key,
	ma_quyen_han AS 'QH' + RIGHT('0' + CAST(ID AS VARCHAR(1)), 1) PERSISTED,
	ten_quyen_han nvarchar(255),
	deleted bit default 0,
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
	anh_nha_vien varchar(255),
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
	foreign key(id_hoa_don) references hoa_don(id),
	foreign key(id_chi_tiet_san_pham) references chi_tiet_san_pham(id)
)
go
create table phuong_thuc_thanh_toan(
	id int identity(1,1) primary key,
	ma_phuong_thuc_thanh_toan AS 'PTTT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_phuong_thuc_thanh_toan nvarchar(255),
	trang_thai bit default 1,
	deleted bit default 0
)
go
create table hinh_thuc_thanh_toan(
	id int identity(1,1) primary key,
	id_hoa_don int not null,
	id_phuong_thuc_thanh_toan int not null,
	ma_hinh_thuc_thanh_toan AS 'HTTT' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	tien_chuyen_khoan decimal(18,2) CHECK (tien_chuyen_khoan >= 0),
	tien_mat decimal(18,2) CHECK (tien_mat >= 0),
	deleted bit default 0,
	foreign key(id_hoa_don) references hoa_don(id),
	foreign key(id_phuong_thuc_thanh_toan) references phuong_thuc_thanh_toan(id)
)
go
create table trang_thai_don_hang(
	id int identity(1,1) primary key,
	ma_trang_thai_don_hang AS 'TTDH' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	ten_trang_thai_don_hang nvarchar(255),
	deleted bit default 0
)
go
create table thong_tin_don_hang(
	id int identity(1,1) primary key,
	id_hoa_don int not null,
	id_trang_thai_don_hang int not null,
	ma_thong_tin_don_hang AS 'TTDH' + RIGHT('00000' + CAST(ID AS VARCHAR(5)), 5) PERSISTED,
	thoi_gian datetime,
	ghi_chu nvarchar(255),
	deleted bit default 0,
	foreign key(id_hoa_don) references hoa_don(id),
	foreign key(id_trang_thai_don_hang) references trang_thai_don_hang(id)
)
go

INSERT INTO xuat_xu (ten_xuat_xu, deleted) VALUES
(N'Anh', 0),
(N'Tây Ban Nha', 0),
(N'Bồ Đào Nha', 0),
(N'Ấn Độ', 0),
(N'Brazil', 0),
(N'Argentina', 0),
(N'Úc', 0),
(N'Canada', 0),
(N'Nga', 0),
(N'Thụy Điển', 0);
go

INSERT INTO nha_san_xuat (ten_nha_san_xuat, deleted) VALUES
(N'Fila', 0),
(N'Converse', 0),
(N'Vans', 0),
(N'Skechers', 0),
(N'Brooks', 0),
(N'Hoka One One', 0),
(N'Saucony', 0),
(N'On Running', 0),
(N'Altra', 0),
(N'Merrell', 0);
go

INSERT INTO san_pham (id_nha_san_xuat, id_xuat_xu, ten_san_pham, deleted, create_at, create_by, update_at, update_by) VALUES
(11, 11, N'Giày Fila Disruptor', 0, '2025-01-11', NULL, '2025-01-11', NULL),
(12, 12, N'Giày Converse Chuck Taylor', 0, '2025-01-12', NULL, '2025-01-12', NULL),
(13, 13, N'Giày Vans Old Skool', 0, '2025-01-13', NULL, '2025-01-13', NULL),
(14, 14, N'Giày Skechers GOwalk', 0, '2025-01-14', NULL, '2025-01-14', NULL),
(15, 15, N'Giày Brooks Ghost', 0, '2025-01-15', NULL, '2025-01-15', NULL),
(16, 16, N'Giày Hoka Bondi', 0, '2025-01-16', NULL, '2025-01-16', NULL),
(17, 17, N'Giày Saucony Guide', 0, '2025-01-17', NULL, '2025-01-17', NULL),
(18, 18, N'Giày On Cloud', 0, '2025-01-18', NULL, '2025-01-18', NULL),
(19, 19, N'Giày Altra Lone Peak', 0, '2025-01-19', NULL, '2025-01-19', NULL),
(20, 20, N'Giày Merrell Moab', 0, '2025-01-20', NULL, '2025-01-20', NULL);
go

INSERT INTO anh_san_pham (duong_dan_anh, loai_anh, mo_ta, deleted) VALUES
('/images/fila_disruptor.jpg', 'Main', N'Ảnh chính Fila', 0),
('/images/converse_ct.jpg', 'Main', N'Ảnh chính Converse', 0),
('/images/vans_os.jpg', 'Main', N'Ảnh chính Vans', 0),
('/images/skechers_gw.jpg', 'Main', N'Ảnh chính Skechers', 0),
('/images/brooks_ghost.jpg', 'Main', N'Ảnh chính Brooks', 0),
('/images/hoka_bondi.jpg', 'Main', N'Ảnh chính Hoka', 0),
('/images/saucony_guide.jpg', 'Main', N'Ảnh chính Saucony', 0),
('/images/on_cloud.jpg', 'Main', N'Ảnh chính On', 0),
('/images/altra_lp.jpg', 'Main', N'Ảnh chính Altra', 0),
('/images/merrell_moab.jpg', 'Main', N'Ảnh chính Merrell', 0);
go

INSERT INTO mau_sac (ten_mau_sac, deleted) VALUES
(N'Nâu', 0),
(N'Xanh ngọc', 0),
(N'Bạc', 0),
(N'Vàng kim', 0),
(N'Đen bóng', 0),
(N'Trắng ngà', 0),
(N'Đỏ rượu', 0),
(N'Xanh dương nhạt', 0),
(N'Vàng chanh', 0),
(N'Xám tro', 0);
go

INSERT INTO kich_thuoc (ten_kich_thuoc, deleted) VALUES
(N'35', 0),
(N'36.5', 0),
(N'37.5', 0),
(N'38.5', 0),
(N'39.5', 0),
(N'40.5', 0),
(N'41.5', 0),
(N'42.5', 0),
(N'43.5', 0),
(N'44.5', 0);
go

INSERT INTO de_giay (ten_de_giay, deleted) VALUES
(N'Cao su đúc', 0),
(N'EVA nhẹ', 0),
(N'PU mềm', 0),
(N'TPU bền', 0),
(N'Phylon nhẹ', 0),
(N'Cao su chống trượt', 0),
(N'EVA cao cấp', 0),
(N'Cao su tự nhiên cao cấp', 0),
(N'Gel chống sốc', 0),
(N'Foam đàn hồi', 0);
go

INSERT INTO chat_lieu (ten_chat_lieu, deleted) VALUES
(N'Da bò', 0),
(N'Vải lưới thoáng khí', 0),
(N'Da lộn cao cấp', 0),
(N'Vải tổng hợp bền', 0),
(N'Da nhân tạo mềm', 0),
(N'Vải canvas chắc', 0),
(N'Cao su linh hoạt', 0),
(N'Nhựa nhẹ', 0),
(N'Vải dệt kim', 0),
(N'Polyester chống nước', 0);
go

INSERT INTO dem_giay (ten_dem_giay, deleted) VALUES
(N'EVA đàn hồi', 0),
(N'Gel thoải mái', 0),
(N'Memory Foam cao cấp', 0),
(N'PU nhẹ', 0),
(N'Ortholite thoáng', 0),
(N'Cao su mềm', 0),
(N'Vải lưới', 0),
(N'TPU đàn hồi', 0),
(N'Phylon thoải mái', 0),
(N'Latex tự nhiên', 0);
go

INSERT INTO trong_luong (ten_trong_luong, deleted) VALUES
(N'150g', 0),
(N'220g', 0),
(N'280g', 0),
(N'320g', 0),
(N'380g', 0),
(N'420g', 0),
(N'480g', 0),
(N'520g', 0),
(N'580g', 0),
(N'620g', 0);
go

INSERT INTO mon_the_thao (ten_mon_the_thao, deleted) VALUES
(N'Bơi lội', 0),
(N'Đạp xe', 0),
(N'Võ thuật', 0),
(N'Golf', 0),
(N'Khiêu vũ', 0),
(N'Leo tường', 0),
(N'Patin', 0),
(N'Skateboard', 0),
(N'Cưỡi ngựa', 0),
(N'Chèo thuyền', 0);
go

INSERT INTO loai_mua (ten_loai_mua, deleted) VALUES
(N'Mùa xuân hè', 0),
(N'Mùa thu đông', 0),
(N'Mùa mưa', 0),
(N'Mùa khô', 0),
(N'Mùa nắng', 0),
(N'Mùa lạnh', 0),
(N'Mùa nóng', 0),
(N'Mùa gió', 0),
(N'Mùa sương mù', 0),
(N'Mùa bão', 0);
go

INSERT INTO do_ben (ten_do_ben, deleted) VALUES
(N'Rất thấp', 0),
(N'Bền lâu', 0),
(N'Chịu lực tốt', 0),
(N'Không bền', 0),
(N'Bền trung bình', 0),
(N'Bền cao cấp', 0),
(N'Bền đặc biệt', 0),
(N'Bền tạm thời', 0),
(N'Bền vĩnh cửu', 0),
(N'Bền cơ bản', 0);
go

INSERT INTO chong_nuoc (ten_chong_nuoc, deleted) VALUES
(N'IPX1', 0),
(N'IPX2', 0),
(N'IPX3', 0),
(N'IP67', 0),
(N'IP68', 0),
(N'Chống thấm nhẹ', 0),
(N'Chống nước sâu', 0),
(N'Chống nước mưa', 0),
(N'Chống nước bắn', 0),
(N'Chống nước toàn diện', 0);
go

INSERT INTO dot_giam_gia (ten_dot_giam_gia, gia_tri_giam_gia, ngay_bat_dau, ngay_ket_thuc, trang_thai, deleted) VALUES
(N'Sale Giáng Sinh 2025', 25, '2025-12-20', '2025-12-25', 1, 0),
(N'Khuyến mãi Valentine 2025', 10, '2025-02-10', '2025-02-14', 1, 0),
(N'Sale Trung Thu 2025', 15, '2025-09-15', '2025-09-20', 1, 0),
(N'Flash Sale Tháng 3', 30, '2025-03-01', '2025-03-05', 1, 0),
(N'Khuyến mãi Tháng 4', 20, '2025-04-01', '2025-04-10', 1, 0),
(N'Sale Tháng 5', 25, '2025-05-01', '2025-05-15', 1, 0),
(N'Giảm giá Tháng 7', 15, '2025-07-01', '2025-07-31', 1, 0),
(N'Sale Tháng 8', 10, '2025-08-01', '2025-08-15', 1, 0),
(N'Khuyến mãi Tháng 9', 20, '2025-09-01', '2025-09-10', 1, 0),
(N'Sale Tháng 10', 25, '2025-10-01', '2025-10-20', 1, 0);
go

INSERT INTO chi_tiet_san_pham (id_san_pham, id_mau_sac, id_kich_thuoc, id_de_giay, id_chat_lieu, id_dem_giay, id_trong_luong, id_mon_the_thao, id_loai_mua, id_do_ben, id_chong_nuoc, so_luong, gia_ban, trang_thai, ghi_chu, deleted, create_at, create_by, update_at, update_by) VALUES
(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 100, 1500000.00, 1, N'Ghi chú 1', 0, '2025-01-01', NULL, '2025-01-01', NULL),
(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 150, 2000000.00, 1, N'Ghi chú 2', 0, '2025-01-02', NULL, '2025-01-02', NULL),
(3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 200, 1800000.00, 1, N'Ghi chú 3', 0, '2025-01-03', NULL, '2025-01-03', NULL),
(4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 120, 2200000.00, 1, N'Ghi chú 4', 0, '2025-01-04', NULL, '2025-01-04', NULL),
(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 80, 2500000.00, 1, N'Ghi chú 5', 0, '2025-01-05', NULL, '2025-01-05', NULL),
(6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 90, 1700000.00, 1, N'Ghi chú 6', 0, '2025-01-06', NULL, '2025-01-06', NULL),
(7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 110, 1900000.00, 1, N'Ghi chú 7', 0, '2025-01-07', NULL, '2025-01-07', NULL),
(8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 130, 2100000.00, 1, N'Ghi chú 8', 0, '2025-01-08', NULL, '2025-01-08', NULL),
(9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 140, 2300000.00, 1, N'Ghi chú 9', 0, '2025-01-09', NULL, '2025-01-09', NULL),
(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 160, 2400000.00, 1, N'Ghi chú 10', 0, '2025-01-10', NULL, '2025-01-10', NULL);
go

INSERT INTO chi_tiet_san_pham_anh (id_chi_tiet_san_pham, id_anh_san_pham, deleted) VALUES
(1, 1, 0),
(2, 2, 0),
(3, 3, 0),
(4, 4, 0),
(5, 5, 0),
(6, 6, 0),
(7, 7, 0),
(8, 8, 0),
(9, 9, 0),
(10, 10, 0);
go

INSERT INTO chi_tiet_dot_giam_gia (id_dot_giam_gia, id_chi_tiet_san_pham, deleted) VALUES
(1, 1, 0),
(2, 2, 0),
(3, 3, 0),
(4, 4, 0),
(1, 5, 0),
(2, 6, 0),
(3, 7, 0),
(4, 8, 0),
(1, 9, 0),
(2, 10, 0);
go

INSERT INTO quyen_han (ten_quyen_han, deleted) VALUES
(N'Admin', 0),
(N'Quản lý', 0),
(N'Nhân viên bán hàng', 0),
(N'Nhân viên kho', 0),
(N'Khách hàng VIP', 0),
(N'Nhân viên hỗ trợ', 0),
(N'Quản trị viên hệ thống', 0),
(N'Nhân viên marketing', 0),
(N'Giám đốc', 0),
(N'Nhân viên kế toán', 0);
go

INSERT INTO nhan_vien (id_quyen_han, ten_nhan_vien, ten_tai_khoan, mat_khau, email, so_dien_thoai, anh_nha_vien, ngay_sinh, ghi_chu, thanh_pho, quan, phuong, dia_chi_cu_the, cccd, trang_thai, deleted, create_at, create_by, update_at, update_by) VALUES
(1, N'Nguyễn Văn A', 'nva', 'password1', 'nva@email.com', '0123456789', '/images/nva.jpg', '1990-01-01', N'Ghi chú A', N'Hà Nội', N'Ba Đình', 'Phường 1', 'Địa chỉ 1', '123456789012', 1, 0, '2025-01-01', NULL, '2025-01-01', NULL),
(2, N'Trần Thị B', 'ttb', 'password2', 'ttb@email.com', '0123456790', '/images/ttb.jpg', '1991-02-02', N'Ghi chú B', N'TP.HCM', N'Quận 1', 'Phường 2', 'Địa chỉ 2', '123456789013', 1, 0, '2025-01-02', NULL, '2025-01-02', NULL),
(3, N'Lê Văn C', 'lvc', 'password3', 'lvc@email.com', '0123456791', '/images/lvc.jpg', '1992-03-03', N'Ghi chú C', N'Đà Nẵng', N'Hải Châu', 'Phường 3', 'Địa chỉ 3', '123456789014', 1, 0, '2025-01-03', NULL, '2025-01-03', NULL),
(4, N'Phạm Thị D', 'ptd', 'password4', 'ptd@email.com', '0123456792', '/images/ptd.jpg', '1993-04-04', N'Ghi chú D', N'Hà Nội', N'Hoàn Kiếm', 'Phường 4', 'Địa chỉ 4', '123456789015', 1, 0, '2025-01-04', NULL, '2025-01-04', NULL),
(5, N'Hoàng Văn E', 'hve', 'password5', 'hve@email.com', '0123456793', '/images/hve.jpg', '1994-05-05', N'Ghi chú E', N'TP.HCM', N'Quận 3', 'Phường 5', 'Địa chỉ 5', '123456789016', 1, 0, '2025-01-05', NULL, '2025-01-05', NULL),
(6, N'Vũ Thị F', 'vtf', 'password6', 'vtf@email.com', '0123456794', '/images/vtf.jpg', '1995-06-06', N'Ghi chú F', N'Đà Nẵng', N'Sơn Trà', 'Phường 6', 'Địa chỉ 6', '123456789017', 1, 0, '2025-01-06', NULL, '2025-01-06', NULL),
(7, N'Đặng Văn G', 'dvg', 'password7', 'dvg@email.com', '0123456795', '/images/dvg.jpg', '1996-07-07', N'Ghi chú G', N'Hà Nội', N'Cầu Giấy', 'Phường 7', 'Địa chỉ 7', '123456789018', 1, 0, '2025-01-07', NULL, '2025-01-07', NULL),
(8, N'Ngô Thị H', 'nth', 'password8', 'nth@email.com', '0123456796', '/images/nth.jpg', '1997-08-08', N'Ghi chú H', N'TP.HCM', N'Quận 5', 'Phường 8', 'Địa chỉ 8', '123456789019', 1, 0, '2025-01-08', NULL, '2025-01-08', NULL),
(9, N'Bùi Văn I', 'bvi', 'password9', 'bvi@email.com', '0123456797', '/images/bvi.jpg', '1998-09-09', N'Ghi chú I', N'Đà Nẵng', N'Liên Chiểu', 'Phường 9', 'Địa chỉ 9', '123456789020', 1, 0, '2025-01-09', NULL, '2025-01-09', NULL),
(10, N'Lý Thị K', 'ltk', 'password10', 'ltk@email.com', '0123456798', '/images/ltk.jpg', '1999-10-10', N'Ghi chú K', N'Hà Nội', N'Đống Đa', 'Phường 10', 'Địa chỉ 10', '123456789021', 1, 0, '2025-01-10', NULL, '2025-01-10', NULL);
go

INSERT INTO khach_hang (ten_khach_hang, ten_tai_khoan, mat_khau, email, so_dien_thoai, gioi_tinh, ngay_sinh, deleted, create_at, create_by, update_at, update_by) VALUES
(N'Khách A', 'kha', 'pass1', 'kha@email.com', '0987654321', 1, '2000-01-01', 0, '2025-01-01', NULL, '2025-01-01', NULL),
(N'Khách B', 'khb', 'pass2', 'khb@email.com', '0987654322', 0, '2001-02-02', 0, '2025-01-02', NULL, '2025-01-02', NULL),
(N'Khách C', 'khc', 'pass3', 'khc@email.com', '0987654323', 1, '2002-03-03', 0, '2025-01-03', NULL, '2025-01-03', NULL),
(N'Khách D', 'khd', 'pass4', 'khd@email.com', '0987654324', 0, '2003-04-04', 0, '2025-01-04', NULL, '2025-01-04', NULL),
(N'Khách E', 'khe', 'pass5', 'khe@email.com', '0987654325', 1, '2004-05-05', 0, '2025-01-05', NULL, '2025-01-05', NULL),
(N'Khách F', 'khf', 'pass6', 'khf@email.com', '0987654326', 0, '2005-06-06', 0, '2025-01-06', NULL, '2025-01-06', NULL),
(N'Khách G', 'khg', 'pass7', 'khg@email.com', '0987654327', 1, '2006-07-07', 0, '2025-01-07', NULL, '2025-01-07', NULL),
(N'Khách H', 'khh', 'pass8', 'khh@email.com', '0987654328', 0, '2007-08-08', 0, '2025-01-08', NULL, '2025-01-08', NULL),
(N'Khách I', 'khi', 'pass9', 'khi@email.com', '0987654329', 1, '2008-09-09', 0, '2025-01-09', NULL, '2025-01-09', NULL),
(N'Khách K', 'khk', 'pass10', 'khk@email.com', '0987654330', 0, '2009-10-10', 0, '2025-01-10', NULL, '2025-01-10', NULL);
go

INSERT INTO dia_chi_khach_hang (id_khach_hang, ten_dia_chi, thanh_pho, quan, phuong, dia_chi_cu_the, deleted) VALUES
(1, N'Địa chỉ 1', N'Hà Nội', N'Ba Đình', 'Phường 1', 'Chi tiết 1', 0),
(2, N'Địa chỉ 2', N'TP.HCM', N'Quận 1', 'Phường 2', 'Chi tiết 2', 0),
(3, N'Địa chỉ 3', N'Đà Nẵng', N'Hải Châu', 'Phường 3', 'Chi tiết 3', 0),
(4, N'Địa chỉ 4', N'Hà Nội', N'Hoàn Kiếm', 'Phường 4', 'Chi tiết 4', 0),
(5, N'Địa chỉ 5', N'TP.HCM', N'Quận 3', 'Phường 5', 'Chi tiết 5', 0),
(6, N'Địa chỉ 6', N'Đà Nẵng', N'Sơn Trà', 'Phường 6', 'Chi tiết 6', 0),
(7, N'Địa chỉ 7', N'Hà Nội', N'Cầu Giấy', 'Phường 7', 'Chi tiết 7', 0),
(8, N'Địa chỉ 8', N'TP.HCM', N'Quận 5', 'Phường 8', 'Chi tiết 8', 0),
(9, N'Địa chỉ 9', N'Đà Nẵng', N'Liên Chiểu', 'Phường 9', 'Chi tiết 9', 0),
(10, N'Địa chỉ 10', N'Hà Nội', N'Đống Đa', 'Phường 10', 'Chi tiết 10', 0);
go

INSERT INTO phieu_giam_gia (ten_phieu_giam_gia, loai_phieu_giam_gia, gia_tri_giam_gia, so_tien_toi_da, hoa_don_toi_thieu, so_luong_dung, ngay_bat_dau, ngay_ket_thuc, trang_thai, mo_ta, deleted) VALUES
(N'Phiếu 1', 0, 10.00, 500000.00, 1000000.00, 100, '2025-01-01', '2025-12-31', 1, N'Mô tả 1', 0),
(N'Phiếu 2', 1, 50000.00, 100000.00, 500000.00, 200, '2025-02-01', '2025-12-31', 1, N'Mô tả 2', 0),
(N'Phiếu 3', 0, 15.00, 300000.00, 800000.00, 150, '2025-03-01', '2025-12-31', 1, N'Mô tả 3', 0),
(N'Phiếu 4', 1, 100000.00, 200000.00, 1000000.00, 120, '2025-04-01', '2025-12-31', 1, N'Mô tả 4', 0),
(N'Phiếu 5', 0, 20.00, 400000.00, 1500000.00, 80, '2025-05-01', '2025-12-31', 1, N'Mô tả 5', 0),
(N'Phiếu 6', 1, 20000.00, 50000.00, 300000.00, 90, '2025-06-01', '2025-12-31', 1, N'Mô tả 6', 0),
(N'Phiếu 7', 0, 5.00, 100000.00, 500000.00, 110, '2025-07-01', '2025-12-31', 1, N'Mô tả 7', 0),
(N'Phiếu 8', 1, 30000.00, 60000.00, 400000.00, 130, '2025-08-01', '2025-12-31', 1, N'Mô tả 8', 0),
(N'Phiếu 9', 0, 25.00, 500000.00, 2000000.00, 140, '2025-09-01', '2025-12-31', 1, N'Mô tả 9', 0),
(N'Phiếu 10', 1, 150000.00, 300000.00, 1500000.00, 160, '2025-10-01', '2025-12-31', 1, N'Mô tả 10', 0);
go

INSERT INTO phieu_giam_gia_ca_nhan (id_khach_hang, id_phieu_giam_gia, ten_phieu_giam_gia_ca_nhan, ngay_nhan, ngay_het_han, trang_thai, deleted) VALUES
(1, 1, N'Phiếu cá nhân 1', '2025-01-01', '2025-12-31', 1, 0),
(2, 2, N'Phiếu cá nhân 2', '2025-02-01', '2025-12-31', 1, 0),
(3, 3, N'Phiếu cá nhân 3', '2025-03-01', '2025-12-31', 1, 0),
(4, 4, N'Phiếu cá nhân 4', '2025-04-01', '2025-12-31', 1, 0),
(5, 5, N'Phiếu cá nhân 5', '2025-05-01', '2025-12-31', 1, 0),
(6, 6, N'Phiếu cá nhân 6', '2025-06-01', '2025-12-31', 1, 0),
(7, 7, N'Phiếu cá nhân 7', '2025-07-01', '2025-12-31', 1, 0),
(8, 8, N'Phiếu cá nhân 8', '2025-08-01', '2025-12-31', 1, 0),
(9, 9, N'Phiếu cá nhân 9', '2025-09-01', '2025-12-31', 1, 0),
(10, 10, N'Phiếu cá nhân 10', '2025-10-01', '2025-12-31', 1, 0);
go

INSERT INTO hoa_don (id_khach_hang, id_phieu_giam_gia, id_nhan_vien, ten_hoa_don, loai_don, phi_van_chuyen, tong_tien, tong_tien_sau_giam, ghi_chu, ten_khach_hang, dia_chi_khach_hang, so_dien_thoai_khach_hang, email_khach_hang, ngay_tao, ngay_thanh_toan, trang_thai, deleted, create_at, create_by, update_at, update_by) VALUES
(1, 1, 1, N'Hóa đơn 1', 0, 30000.00, 1500000.00, 1350000.00, N'Ghi chú HD1', N'Khách A', N'Địa chỉ 1', '0987654321', 'kha@email.com', '2025-01-01', '2025-01-02', 1, 0, '2025-01-01', NULL, '2025-01-01', NULL),
(2, 2, 2, N'Hóa đơn 2', 1, 0.00, 2000000.00, 1900000.00, N'Ghi chú HD2', N'Khách B', N'Địa chỉ 2', '0987654322', 'khb@email.com', '2025-01-02', '2025-01-03', 1, 0, '2025-01-02', NULL, '2025-01-02', NULL),
(3, NULL, 3, N'Hóa đơn 3', 0, 30000.00, 1800000.00, 1800000.00, N'Ghi chú HD3', N'Khách C', N'Địa chỉ 3', '0987654323', 'khc@email.com', '2025-01-03', '2025-01-04', 1, 0, '2025-01-03', NULL, '2025-01-03', NULL),
(4, 4, 4, N'Hóa đơn 4', 1, 0.00, 2200000.00, 1980000.00, N'Ghi chú HD4', N'Khách D', N'Địa chỉ 4', '0987654324', 'khd@email.com', '2025-01-04', '2025-01-05', 1, 0, '2025-01-04', NULL, '2025-01-04', NULL),
(5, 5, 5, N'Hóa đơn 5', 0, 30000.00, 2500000.00, 2250000.00, N'Ghi chú HD5', N'Khách E', N'Địa chỉ 5', '0987654325', 'khe@email.com', '2025-01-05', '2025-01-06', 1, 0, '2025-01-05', NULL, '2025-01-05', NULL),
(6, NULL, 6, N'Hóa đơn 6', 1, 0.00, 1700000.00, 1700000.00, N'Ghi chú HD6', N'Khách F', N'Địa chỉ 6', '0987654326', 'khf@email.com', '2025-01-06', '2025-01-07', 1, 0, '2025-01-06', NULL, '2025-01-06', NULL),
(7, 7, 7, N'Hóa đơn 7', 0, 30000.00, 1900000.00, 1710000.00, N'Ghi chú HD7', N'Khách G', N'Địa chỉ 7', '0987654327', 'khg@email.com', '2025-01-07', '2025-01-08', 1, 0, '2025-01-07', NULL, '2025-01-07', NULL),
(8, 8, 8, N'Hóa đơn 8', 1, 0.00, 2100000.00, 1890000.00, N'Ghi chú HD8', N'Khách H', N'Địa chỉ 8', '0987654328', 'khh@email.com', '2025-01-08', '2025-01-09', 1, 0, '2025-01-08', NULL, '2025-01-08', NULL),
(9, 9, 9, N'Hóa đơn 9', 0, 30000.00, 2300000.00, 2070000.00, N'Ghi chú HD9', N'Khách I', N'Địa chỉ 9', '0987654329', 'khi@email.com', '2025-01-09', '2025-01-10', 1, 0, '2025-01-09', NULL, '2025-01-09', NULL),
(10, 10, 10, N'Hóa đơn 10', 1, 0.00, 2400000.00, 2160000.00, N'Ghi chú HD10', N'Khách K', N'Địa chỉ 10', '0987654330', 'khk@email.com', '2025-01-10', '2025-01-11', 1, 0, '2025-01-10', NULL, '2025-01-10', NULL);
go

INSERT INTO hoa_don_chi_tiet (id_hoa_don, id_chi_tiet_san_pham, so_luong, gia_ban, thanh_tien, trang_thai, ghi_chu, deleted) VALUES
(1, 1, 2, 1500000.00, 3000000.00, 1, N'Ghi chú HDCT1', 0),
(2, 2, 1, 2000000.00, 2000000.00, 1, N'Ghi chú HDCT2', 0),
(3, 3, 3, 1800000.00, 5400000.00, 1, N'Ghi chú HDCT3', 0),
(4, 4, 2, 2200000.00, 4400000.00, 1, N'Ghi chú HDCT4', 0),
(5, 5, 1, 2500000.00, 2500000.00, 1, N'Ghi chú HDCT5', 0),
(6, 6, 4, 1700000.00, 6800000.00, 1, N'Ghi chú HDCT6', 0),
(7, 7, 2, 1900000.00, 3800000.00, 1, N'Ghi chú HDCT7', 0),
(8, 8, 1, 2100000.00, 2100000.00, 1, N'Ghi chú HDCT8', 0),
(9, 9, 3, 2300000.00, 6900000.00, 1, N'Ghi chú HDCT9', 0),
(10, 10, 2, 2400000.00, 4800000.00, 1, N'Ghi chú HDCT10', 0);
go

INSERT INTO phuong_thuc_thanh_toan (ten_phuong_thuc_thanh_toan, trang_thai, deleted) VALUES
(N'Tiền mặt', 1, 0),
(N'Chuyển khoản ngân hàng', 1, 0),
(N'Thẻ tín dụng', 1, 0),
(N'Ví điện tử', 1, 0),
(N'PayPal', 1, 0),
(N'VNPAY', 1, 0),
(N'Momo', 1, 0),
(N'ZaloPay', 1, 0),
(N'Internet Banking', 1, 0),
(N'Thanh toán khi nhận hàng', 1, 0);
go

INSERT INTO hinh_thuc_thanh_toan (id_hoa_don, id_phuong_thuc_thanh_toan, tien_chuyen_khoan, tien_mat, deleted) VALUES
(1, 1, 0.00, 1350000.00, 0),
(2, 2, 1900000.00, 0.00, 0),
(3, 3, 1800000.00, 0.00, 0),
(4, 4, 0.00, 1980000.00, 0),
(5, 5, 2250000.00, 0.00, 0),
(6, 6, 0.00, 1700000.00, 0),
(7, 7, 1710000.00, 0.00, 0),
(8, 8, 0.00, 1890000.00, 0),
(9, 9, 2070000.00, 0.00, 0),
(10, 10, 0.00, 2160000.00, 0);
go

INSERT INTO trang_thai_don_hang (ten_trang_thai_don_hang, deleted) VALUES
(N'Chờ xác nhận', 0),
(N'Đang chuẩn bị', 0),
(N'Đang giao hàng', 0),
(N'Đã giao hàng', 0),
(N'Hoàn thành', 0),
(N'Đã hủy', 0),
(N'Chờ thanh toán', 0),
(N'Đang xử lý', 0),
(N'Hoàn tiền', 0),
(N'Đang kiểm tra', 0);
go

INSERT INTO thong_tin_don_hang (id_hoa_don, id_trang_thai_don_hang, thoi_gian, ghi_chu, deleted) VALUES
(1, 1, '2025-01-01 10:00:00', N'Ghi chú TT1', 0),
(2, 2, '2025-01-02 11:00:00', N'Ghi chú TT2', 0),
(3, 3, '2025-01-03 12:00:00', N'Ghi chú TT3', 0),
(4, 4, '2025-01-04 13:00:00', N'Ghi chú TT4', 0),
(5, 5, '2025-01-05 14:00:00', N'Ghi chú TT5', 0),
(6, 6, '2025-01-06 15:00:00', N'Ghi chú TT6', 0),
(7, 7, '2025-01-07 16:00:00', N'Ghi chú TT7', 0),
(8, 8, '2025-01-08 17:00:00', N'Ghi chú TT8', 0),
(9, 9, '2025-01-09 18:00:00', N'Ghi chú TT9', 0),
(10, 10, '2025-01-10 19:00:00', N'Ghi chú TT10', 0);
go