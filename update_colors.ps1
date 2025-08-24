# PowerShell script to update color scheme in Vue components
Write-Host "Updating color scheme in all Vue components..." -ForegroundColor Green
Write-Host "New colors: #4ade80 (primary), #22c55e (secondary)" -ForegroundColor Cyan

# List of components to update
$components = @(
    "DATN_GearUp_Ver2/src/pages/products/TrongLuong.vue"
    "DATN_GearUp_Ver2/src/pages/products/DemGiay.vue"
    "DATN_GearUp_Ver2/src/pages/products/ChatLieu.vue"
    "DATN_GearUp_Ver2/src/pages/products/DeGiay.vue"
    "DATN_GearUp_Ver2/src/pages/products/KichThuoc.vue"
)

# Color replacements
$replacements = @{
    '#32CD32' = '#4ade80'
    '#28a745' = '#22c55e'
    '#1e7e34' = '#16a34a'
    '#d4edda' = '#dcfce7'
    '#f8fff9' = '#f0fdf4'
    '#f0fff4' = '#f0fdf4'
    '#c6f6d5' = '#bbf7d0'
    'rgba(50, 205, 50,' = 'rgba(74, 222, 128,'
    'rgba(40, 167, 69,' = 'rgba(34, 197, 94,'
}

foreach ($component in $components) {
    if (Test-Path $component) {
        Write-Host "Updating: $component" -ForegroundColor Yellow
        
        $content = Get-Content $component -Raw -Encoding UTF8
        
        foreach ($oldColor in $replacements.Keys) {
            $newColor = $replacements[$oldColor]
            $content = $content -replace [regex]::Escape($oldColor), $newColor
        }
        
        Set-Content $component $content -Encoding UTF8
        Write-Host "  ✓ Updated" -ForegroundColor Green
    } else {
        Write-Host "  ✗ File not found: $component" -ForegroundColor Red
    }
}

Write-Host "Color update completed!" -ForegroundColor Green
