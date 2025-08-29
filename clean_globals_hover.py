#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script để xóa tất cả hover effect trong globals.css
"""

import re

def clean_globals_hover():
    """Xóa tất cả hover effect"""
    file_path = "DATN_GearUp_Ver2/src/styles/globals.css"
    
    print(f"Đang xử lý: {file_path}")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Xóa tất cả hover effect
    hover_patterns = [
        r'\.clear-btn:hover\s*\{[^}]*\}',
        r'\.btn\.btn-secondary:hover\s*\{[^}]*\}'
    ]
    
    for pattern in hover_patterns:
        content = re.sub(pattern, '/* Đã xóa hover effect */', content, flags=re.MULTILINE | re.DOTALL)
    
    # Ghi file
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print("Đã xóa hover effect thành công!")

if __name__ == "__main__":
    clean_globals_hover()
