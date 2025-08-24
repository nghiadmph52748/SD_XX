const express = require('express');
const router = express.Router();
const sql = require('mssql');

// Import pool from main server
let pool;

// Function to get pool from main server
const getPool = () => {
  if (!pool) {
    try {
      const serverModule = require('../server');
      pool = serverModule.pool;
    } catch (error) {
      console.error('Could not get pool from main server:', error);
    }
  }
  return pool;
};

// GET all manufacturers (nhà sản xuất) - playlist
router.get('/playlist', async (req, res) => {
  try {
    const pool = getPool();
    if (!pool) {
      return res.status(500).json({
        success: false,
        message: 'Database connection not available'
      });
    }

    const request = pool.request();
    const result = await request.query(`
      SELECT * FROM nha_san_xuat 
      WHERE deleted = 0 
      ORDER BY ten_nha_san_xuat
    `);

    res.json({
      success: true,
      data: result.recordset
    });
  } catch (error) {
    console.error('Get manufacturers error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// GET manufacturer by ID
router.get('/detail/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const pool = getPool();
    if (!pool) {
      return res.status(500).json({
        success: false,
        message: 'Database connection not available'
      });
    }

    const request = pool.request();
    request.input('id', sql.Int, id);
    const result = await request.query(`
      SELECT * FROM nha_san_xuat 
      WHERE id = @id AND deleted = 0
    `);

    if (result.recordset.length === 0) {
      return res.status(404).json({
        success: false,
        message: 'Nhà sản xuất không tồn tại'
      });
    }

    res.json({
      success: true,
      data: result.recordset[0]
    });
  } catch (error) {
    console.error('Get manufacturer error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// POST create new manufacturer
router.post('/add', async (req, res) => {
  try {
    const { ten_nha_san_xuat } = req.body;
    
    if (!ten_nha_san_xuat) {
      return res.status(400).json({
        success: false,
        message: 'Tên nhà sản xuất không được để trống'
      });
    }

    const pool = getPool();
    if (!pool) {
      return res.status(500).json({
        success: false,
        message: 'Database connection not available'
      });
    }

    const request = pool.request();
    request.input('ten_nha_san_xuat', sql.NVarChar, ten_nha_san_xuat);
    
    const result = await request.query(`
      INSERT INTO nha_san_xuat (ten_nha_san_xuat)
      OUTPUT INSERTED.id
      VALUES (@ten_nha_san_xuat)
    `);

    const newId = result.recordset[0].id;

    res.status(201).json({
      success: true,
      message: 'Tạo nhà sản xuất thành công',
      data: { id: newId }
    });
  } catch (error) {
    console.error('Create manufacturer error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// PUT update manufacturer
router.put('/update/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const { ten_nha_san_xuat } = req.body;
    
    if (!ten_nha_san_xuat) {
      return res.status(400).json({
        success: false,
        message: 'Tên nhà sản xuất không được để trống'
      });
    }

    const pool = getPool();
    if (!pool) {
      return res.status(500).json({
        success: false,
        message: 'Database connection not available'
      });
    }

    const request = pool.request();
    request.input('id', sql.Int, id);
    request.input('ten_nha_san_xuat', sql.NVarChar, ten_nha_san_xuat);
    
    const result = await request.query(`
      UPDATE nha_san_xuat
      SET ten_nha_san_xuat = @ten_nha_san_xuat
      WHERE id = @id AND deleted = 0;
      
      SELECT @@ROWCOUNT as affectedRows;
    `);

    if (result.recordset[0].affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Nhà sản xuất không tồn tại'
      });
    }

    res.json({
      success: true,
      message: 'Cập nhật nhà sản xuất thành công'
    });
  } catch (error) {
    console.error('Update manufacturer error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// DELETE manufacturer (soft delete)
router.delete('/delete/:id', async (req, res) => {
  try {
    const { id } = req.params;
    
    const pool = getPool();
    if (!pool) {
      return res.status(500).json({
        success: false,
        message: 'Database connection not available'
      });
    }

    const request = pool.request();
    request.input('id', sql.Int, id);
    
    const result = await request.query(`
      UPDATE nha_san_xuat
      SET deleted = 1
      WHERE id = @id AND deleted = 0;
      
      SELECT @@ROWCOUNT as affectedRows;
    `);

    if (result.recordset[0].affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Nhà sản xuất không tồn tại'
      });
    }

    res.json({
      success: true,
      message: 'Xóa nhà sản xuất thành công'
    });
  } catch (error) {
    console.error('Delete manufacturer error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

module.exports = router;
