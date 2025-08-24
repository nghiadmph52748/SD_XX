#!/bin/bash

echo "Updating color scheme in all Vue components..."
echo "New colors: #4ade80 (primary), #22c55e (secondary)"

# List of components to update
components=(
    "DATN_GearUp_Ver2/src/pages/products/MonTheThao.vue"
    "DATN_GearUp_Ver2/src/pages/products/TrongLuong.vue"
    "DATN_GearUp_Ver2/src/pages/products/DemGiay.vue"
    "DATN_GearUp_Ver2/src/pages/products/ChatLieu.vue"
    "DATN_GearUp_Ver2/src/pages/products/DeGiay.vue"
    "DATN_GearUp_Ver2/src/pages/products/KichThuoc.vue"
)

# Color replacements
for component in "${components[@]}"; do
    if [ -f "$component" ]; then
        echo "Updating: $component"
        
        # Replace colors using sed
        sed -i 's/#32CD32/#4ade80/g' "$component"
        sed -i 's/#28a745/#22c55e/g' "$component"
        sed -i 's/#1e7e34/#16a34a/g' "$component"
        sed -i 's/#d4edda/#dcfce7/g' "$component"
        sed -i 's/#f8fff9/#f0fdf4/g' "$component"
        sed -i 's/#f0fff4/#f0fdf4/g' "$component"
        sed -i 's/#c6f6d5/#bbf7d0/g' "$component"
        sed -i 's/rgba(50, 205, 50,/rgba(74, 222, 128,/g' "$component"
        sed -i 's/rgba(40, 167, 69,/rgba(34, 197, 94,/g' "$component"
        
        echo "  ✓ Updated"
    else
        echo "  ✗ File not found: $component"
    fi
done

echo "Color update completed!"
