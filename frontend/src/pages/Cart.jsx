import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Trash2, Plus, Minus, ShoppingBag } from 'lucide-react';
import { Link } from 'react-router-dom';

const Cart = () => {
  const { items, totalPrice } = useSelector((state) => state.cart);
  const dispatch = useDispatch();

  if (items.length === 0) {
    return (
      <div className="cart-empty container">
        <ShoppingBag size={64} color="var(--text-muted)" />
        <h2>Your cart is empty</h2>
        <p>Looks like you haven't added anything to your cart yet.</p>
        <Link to="/products" className="btn-primary">Start Shopping</Link>
      </div>
    );
  }

  return (
    <div className="cart-page container">
      <h1>Shopping Bag</h1>
      
      <div className="cart-grid">
        <div className="cart-items">
          {items.map((item) => (
            <div key={item.id} className="cart-item glass-card">
              <div className="item-img"></div>
              <div className="item-info">
                <h3>{item.productName || 'Product'}</h3>
                <p className="item-price">${item.unitPrice}</p>
                
                <div className="item-controls">
                  <div className="quantity-selector">
                    <button><Minus size={16} /></button>
                    <span>{item.quantity}</span>
                    <button><Plus size={16} /></button>
                  </div>
                  <button className="remove-btn"><Trash2 size={18} /></button>
                </div>
              </div>
              <p className="item-total">${item.totalPrice}</p>
            </div>
          ))}
        </div>
        
        <div className="cart-summary glass-card">
          <h2>Summary</h2>
          <div className="summary-row">
            <span>Subtotal</span>
            <span>${totalPrice}</span>
          </div>
          <div className="summary-row">
            <span>Shipping</span>
            <span>Calculated at checkout</span>
          </div>
          <div className="total-row">
            <span>Total</span>
            <span>${totalPrice}</span>
          </div>
          <Link to="/checkout" className="btn-primary w-full">Proceed to Checkout</Link>
        </div>
      </div>

      <style jsx>{`
        .cart-empty { text-align: center; padding: 100px 0; }
        .cart-empty h2 { margin: 2rem 0 0.5rem; }
        .cart-empty p { color: var(--text-muted); margin-bottom: 2rem; }
        
        .cart-grid { display: grid; grid-template-columns: 1fr 350px; gap: 2rem; margin-top: 2rem; }
        .cart-item { display: flex; align-items: center; gap: 1.5rem; padding: 1.5rem; margin-bottom: 1rem; }
        .item-img { width: 100px; height: 100px; background: rgba(255,255,255,0.05); border-radius: 0.5rem; }
        .item-info { flex: 1; }
        .item-info h3 { font-size: 1.1rem; margin-bottom: 0.25rem; }
        .item-price { color: var(--text-muted); margin-bottom: 1rem; }
        .item-controls { display: flex; align-items: center; gap: 2rem; }
        .quantity-selector { display: flex; align-items: center; gap: 1rem; background: var(--bg-dark); padding: 0.25rem 0.75rem; border-radius: 2rem; }
        .remove-btn { color: var(--accent); opacity: 0.6; transition: opacity 0.3s; }
        .remove-btn:hover { opacity: 1; }
        .item-total { font-weight: 700; font-size: 1.1rem; }
        
        .cart-summary { padding: 2rem; height: fit-content; position: sticky; top: 120px; }
        .summary-row { display: flex; justify-content: space-between; margin-bottom: 1rem; color: var(--text-muted); }
        .total-row { display: flex; justify-content: space-between; margin-top: 1.5rem; pt: 1.5rem; border-top: 1px solid var(--glass-border); font-size: 1.25rem; font-weight: 800; margin-bottom: 2rem; }
      `}</style>
    </div>
  );
};

export default Cart;
