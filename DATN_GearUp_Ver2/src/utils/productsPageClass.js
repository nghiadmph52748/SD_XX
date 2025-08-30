// Products Page Class Manager
// Tự động áp dụng class products-page vào tất cả products pages

export function addProductsPageClass() {
  // Kiểm tra nếu đang ở trong products route
  const isProductsRoute = () => {
    return window.location.pathname.includes('/products') || 
           window.location.pathname === '/products';
  };

  // Thêm class products-page vào body
  const addProductsPageClass = () => {
    if (isProductsRoute()) {
      document.body.classList.add('products-page');
      document.documentElement.classList.add('products-active');
      
      // Thêm data-route attribute để CSS có thể target
      document.body.setAttribute('data-route', window.location.pathname);
    } else {
      document.body.classList.remove('products-page');
      document.documentElement.classList.remove('products-active');
      document.body.removeAttribute('data-route');
    }
  };

  // Khởi tạo
  const init = () => {
    addProductsPageClass();
  };

  // Chạy khi route thay đổi
  const handleRouteChange = () => {
    setTimeout(() => {
      init();
    }, 100);
  };

  // Lắng nghe route changes
  if (typeof window !== 'undefined') {
    // Chạy lần đầu
    init();
    
    // Lắng nghe popstate (back/forward button)
    window.addEventListener('popstate', handleRouteChange);
    
    // Lắng nghe pushstate (programmatic navigation)
    const originalPushState = history.pushState;
    history.pushState = function(...args) {
      originalPushState.apply(this, args);
      handleRouteChange();
    };
    
    // Lắng nghe replaceState
    const originalReplaceState = history.replaceState;
    history.replaceState = function(...args) {
      originalReplaceState.apply(this, args);
      handleRouteChange();
    };
  }

  return {
    init,
    addClass: addProductsPageClass
  };
}

// Auto-initialize nếu được import trực tiếp
if (typeof window !== 'undefined') {
  addProductsPageClass();
}
