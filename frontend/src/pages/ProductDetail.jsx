import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { fetchProductById } from '../features/productSlice';
import { addToCart, createCart } from '../features/cartSlice';
import { ShoppingBag, ChevronLeft } from 'lucide-react';
import { Link } from 'react-router-dom';

const ProductDetail = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const { selectedProduct: product, loading } = useSelector((state) => state.products);
  const { id: cartId } = useSelector((state) => state.cart);

  useEffect(() => {
    dispatch(fetchProductById(id));
  }, [dispatch, id]);

  const handleAddToCart = async () => {
    let currentCartId = cartId;
    if (!currentCartId) {
      const res = await dispatch(createCart());
      currentCartId = res.payload.id;
    }
    dispatch(addToCart({ cartId: currentCartId, productId: product.id }));
  };

  if (loading || !product) return <div className="container">Loading...</div>;

  return (
    <div className="product-detail container">
      <Link to="/products" className="back-link"><ChevronLeft size={20}/> Back to shop</Link>
      
      <div className="detail-grid">
        <div className="product-gallery glass-card">
          <div className="main-img"></div>
        </div>
        
        <div className="product-info-side">
          <span className="category-tag">Premium Collection</span>
          <h1>{product.name}</h1>
          <p className="price">${product.price}</p>
          <p className="description">
            {product.description || "Experience the pinnacle of craftsmanship with this essential piece. Meticulously designed for those who value both form and function."}
          </p>
          
          <div className="actions">
            <button className="btn-primary" onClick={handleAddToCart}>
              <ShoppingBag size={20} /> Add to Cart
            </button>
          </div>
          
          <div className="benefits">
            <div className="benefit-item">✓ Free Express Shipping</div>
            <div className="benefit-item">✓ 30-Day Premium Returns</div>
            <div className="benefit-item">✓ Secure Checkout</div>
          </div>
        </div>
      </div>

      <style jsx>{`
        .product-detail { padding-top: 2rem; }
        .back-link { display: flex; align-items: center; gap: 0.5rem; color: var(--text-muted); margin-bottom: 2rem; }
        .detail-grid {
          display: grid;
          grid-template-columns: 1.2fr 1fr;
          gap: 4rem;
        }
        .product-gallery { height: 600px; }
        .category-tag { 
          color: var(--primary); 
          font-weight: 600; 
          text-transform: uppercase; 
          font-size: 0.8rem; 
          letter-spacing: 1px;
        }
        .product-info-side h1 { font-size: 3rem; margin: 0.5rem 0 1rem; }
        .product-info-side .price { font-size: 2rem; font-weight: 700; margin-bottom: 1.5rem; }
        .product-info-side .description { color: var(--text-muted); line-height: 1.7; margin-bottom: 2.5rem; }
        .actions { margin-bottom: 3rem; }
        .actions button { display: flex; align-items: center; gap: 1rem; padding: 1rem 2.5rem; font-size: 1.1rem; }
        .benefits { display: grid; gap: 0.75rem; border-top: 1px solid var(--glass-border); pt: 2rem; }
        .benefit-item { color: var(--text-muted); font-size: 0.9rem; }
      `}</style>
    </div>
  );
};

export default ProductDetail;
