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

// GET all origins (xuất xứ) - playlist
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
      SELECT * FROM xuat_xu 
      WHERE deleted = 0 
      ORDER BY ten_xuat_xu
    `);

    res.json({
      success: true,
      data: result.recordset
    });
  } catch (error) {
    console.error('Get origins error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// GET origin by ID
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
      SELECT * FROM xuat_xu 
      WHERE id = @id AND deleted = 0
    `);

    if (result.recordset.length === 0) {
      return res.status(404).json({
        success: false,
        message: 'Xuất xứ không tồn tại'
      });
    }

    res.json({
      success: true,
      data: result.recordset[0]
    });
  } catch (error) {
    console.error('Get origin error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// POST create new origin
router.post('/add', async (req, res) => {
  try {
    const { ten_xuat_xu } = req.body;
    
    if (!ten_xuat_xu) {
      return res.status(400).json({
        success: false,
        message: 'Tên xuất xứ không được để trống'
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
    request.input('ten_xuat_xu', sql.NVarChar, ten_xuat_xu);
    
    const result = await request.query(`
      INSERT INTO xuat_xu (ten_xuat_xu)
      OUTPUT INSERTED.id
      VALUES (@ten_xuat_xu)
    `);

    const newId = result.recordset[0].id;

    res.status(201).json({
      success: true,
      message: 'Tạo xuất xứ thành công',
      data: { id: newId }
    });
  } catch (error) {
    console.error('Create origin error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// PUT update origin
router.put('/update/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const { ten_xuat_xu } = req.body;
    
    if (!ten_xuat_xu) {
      return res.status(400).json({
        success: false,
        message: 'Tên xuất xứ không được để trống'
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
    request.input('ten_xuat_xu', sql.NVarChar, ten_xuat_xu);
    
    const result = await request.query(`
      UPDATE xuat_xu
      SET ten_xuat_xu = @ten_xuat_xu
      WHERE id = @id AND deleted = 0;
      
      SELECT @@ROWCOUNT as affectedRows;
    `);

    if (result.recordset[0].affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Xuất xứ không tồn tại'
      });
    }

    res.json({
      success: true,
      message: 'Cập nhật xuất xứ thành công'
    });
  } catch (error) {
    console.error('Update origin error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

// DELETE origin (soft delete)
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
      UPDATE xuat_xu
      SET deleted = 1
      WHERE id = @id AND deleted = 0;
      
      SELECT @@ROWCOUNT as affectedRows;
    `);

    if (result.recordset[0].affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Xuất xứ không tồn tại'
      });
    }

    res.json({
      success: true,
      message: 'Xóa xuất xứ thành công'
    });
  } catch (error) {
    console.error('Delete origin error:', error);
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

module.exports = router;
