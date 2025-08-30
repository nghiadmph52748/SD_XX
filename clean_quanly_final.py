#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script để xóa toàn bộ CSS local trong QuanLySanPham.vue và thay thế bằng import
"""

import re

def clean_quanly_final():
    """Xóa toàn bộ CSS local và thay thế bằng import"""
    file_path = "DATN_GearUp_Ver2/src/pages/management/QuanLySanPham.vue"
    
    print(f"Đang xử lý: {file_path}")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Tìm phần <style scoped>
    style_start = content.find('<style scoped>')
    if style_start == -1:
        print(f"  Không tìm thấy <style scoped> trong {file_path}")
        return
    
    style_end = content.find('</style>', style_start)
    if style_end == -1:
        print(f"  Không tìm thấy </style> trong {file_path}")
        return
    
    # Xóa toàn bộ CSS và thay thế bằng import
    new_style = '''<style scoped>
/* CSS đã được chuyển vào productsUnified.css */
@import "../../styles/productsUnified.css";
</style>'''
    
    old_style = content[style_start:style_end + 8]
    new_content = content.replace(old_style, new_style)
    
    # Ghi file
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    print(f"  Đã xóa toàn bộ CSS local và thay thế bằng import")

if __name__ == "__main__":
    clean_quanly_final()
