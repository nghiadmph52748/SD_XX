#!/usr/bin/env python3
import os
import re

def update_colors_in_file(file_path):
    """Update colors in a single file"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Color replacements
        replacements = [
            ('#32CD32', '#4ade80'),
            ('#28a745', '#22c55e'),
            ('#1e7e34', '#16a34a'),
            ('#d4edda', '#dcfce7'),
            ('#f8fff9', '#f0fdf4'),
            ('#f0fff4', '#f0fdf4'),
            ('#c6f6d5', '#bbf7d0'),
            ('rgba(50, 205, 50,', 'rgba(74, 222, 128,'),
            ('rgba(40, 167, 69,', 'rgba(34, 197, 94,'),
        ]
        
        original_content = content
        for old_color, new_color in replacements:
            content = content.replace(old_color, new_color)
        
        # Update rgba values
        content = re.sub(r'rgba\(50, 205, 50,', 'rgba(74, 222, 128,', content)
        content = re.sub(r'rgba\(40, 167, 69,', 'rgba(34, 197, 94,', content)
        
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Updated: {file_path}")
        else:
            print(f"No changes needed: {file_path}")
            
    except Exception as e:
        print(f"Error processing {file_path}: {e}")

def main():
    """Main function to update all Vue components"""
    # List of components to update
    components = [
        'DATN_GearUp_Ver2/src/pages/products/DoBen.vue',
        'DATN_GearUp_Ver2/src/pages/products/LoaiMua.vue',
        'DATN_GearUp_Ver2/src/pages/products/MonTheThao.vue',
        'DATN_GearUp_Ver2/src/pages/products/TrongLuong.vue',
        'DATN_GearUp_Ver2/src/pages/products/DemGiay.vue',
        'DATN_GearUp_Ver2/src/pages/products/ChatLieu.vue',
        'DATN_GearUp_Ver2/src/pages/products/DeGiay.vue',
        'DATN_GearUp_Ver2/src/pages/products/KichThuoc.vue',
    ]
    
    print("Updating color scheme in Vue components...")
    print("New colors: #4ade80 (primary), #22c55e (secondary)")
    print("-" * 50)
    
    for component in components:
        if os.path.exists(component):
            update_colors_in_file(component)
        else:
            print(f"File not found: {component}")
    
    print("-" * 50)
    print("Color update completed!")

if __name__ == "__main__":
    main()
