#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script để xóa hover effect và thay đổi màu sắc trong QuanLySanPham.vue
"""

import re

def clean_quanly_css():
    """Xóa hover effect và thay đổi màu sắc"""
    file_path = "DATN_GearUp_Ver2/src/pages/management/QuanLySanPham.vue"
    
    print(f"Đang xử lý: {file_path}")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Xóa tất cả hover effect
    hover_patterns = [
        r'\.sortable-header:hover\s*\{[^}]*\}',
        r'\.sortable-header:hover\s*\.sort-icon\s*\{[^}]*\}',
        r'\.clear-btn:hover\s*\{[^}]*\}',
        r'\.table th\.sortable:hover\s*\{[^}]*\}',
        r'\.btn-export:hover\s*\{[^}]*\}',
        r'\.btn-outline:hover\s*\{[^}]*\}',
        r'\.btn-primary:hover\s*\{[^}]*\}',
        r'\.success-close-btn:hover\s*\{[^}]*\}',
        r'\.sort-option:hover\s*\{[^}]*\}',
        r'\.sort-option:hover\s*\.sort-arrow\s*\{[^}]*\}',
        r'\.sort-value-item:hover\s*\{[^}]*\}',
        r'\.sort-item:hover\s*\{[^}]*\}',
        r'\.editable-cell:hover\s*\{[^}]*\}',
        r'\.btn-save:hover\s*\{[^}]*\}',
        r'\.btn-cancel:hover\s*\{[^}]*\}',
        r'\.btn-save-all:hover\s*\{[^}]*\}',
        r'\.table td:first-child input\[type="checkbox"\]:hover::after\s*\{[^}]*\}'
    ]
    
    for pattern in hover_patterns:
        content = re.sub(pattern, '/* Đã xóa hover effect */', content, flags=re.MULTILINE | re.DOTALL)
    
    # Thay đổi màu sắc theo màu logo (xanh lá)
    color_replacements = [
        (r'#4ade80', '#22c55e'),  # Màu xanh lá đậm hơn
        (r'#22c55e', '#16a34a'),  # Màu xanh lá đậm hơn nữa
        (r'#10b981', '#059669'),  # Màu xanh lá đậm
        (r'#059669', '#047857'),  # Màu xanh lá đậm nhất
        (r'rgba\(74, 222, 128', 'rgba(34, 197, 94'),  # Màu xanh lá với opacity
        (r'rgba\(16, 185, 129', 'rgba(5, 150, 105'),  # Màu xanh lá với opacity
    ]
    
    for old_color, new_color in color_replacements:
        content = content.replace(old_color, new_color)
    
    # Ghi file
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print("Đã xóa hover effect và thay đổi màu sắc thành công!")

if __name__ == "__main__":
    clean_quanly_css()
