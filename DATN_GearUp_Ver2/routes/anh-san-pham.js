const express = require('express');
const router = express.Router();

// Route to handle CORS for existing API calls
// This redirects to the existing /api/upload/images endpoint
router.get('/playlist', async (req, res) => {
  try {
    // Redirect to the existing API endpoint that already handles ảnh sản phẩm
    // The actual API is at /api/upload/images
    res.json({
      success: true,
      message: 'Use /api/upload/images endpoint for product images',
      note: 'This route exists to handle CORS. The actual API is at /api/upload/images',
      existingEndpoint: '/api/upload/images'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Server error',
      error: error.message
    });
  }
});

module.exports = router;
