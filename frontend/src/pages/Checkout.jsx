import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import axios from 'axios';
import { CreditCard, Truck, ShieldCheck } from 'lucide-react';

const Checkout = () => {
  const { id: cartId, totalPrice } = useSelector((state) => state.cart);
  const { token } = useSelector((state) => state.auth);
  const [loading, setLoading] = useState(false);

  const handleCheckout = async () => {
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:8080/checkout', 
        { cartId },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      // Redirect to Stripe
      window.location.href = response.data.url;
    } catch (error) {
      console.error('Checkout failed', error);
      setLoading(false);
    }
  };

  return (
    <div className="checkout-page container">
      <div className="checkout-grid">
        <div className="checkout-form">
          <div className="section-card glass-card">
            <h3><Truck size={20} /> Shipping Information</h3>
            <div className="form-grid">
              <input type="text" placeholder="Full Name" className="input-field" />
              <input type="text" placeholder="Address" className="input-field" />
              <div className="row">
                <input type="text" placeholder="City" className="input-field" />
                <input type="text" placeholder="Zip Code" className="input-field" />
              </div>
            </div>
          </div>
          
          <div className="section-card glass-card">
            <h3><CreditCard size={20} /> Payment Method</h3>
            <p className="payment-note">You will be redirected to Stripe's secure payment page.</p>
          </div>
        </div>
        
        <div className="order-summary glass-card">
          <h2>Order Details</h2>
          <div className="summary-row">
            <span>Amount</span>
            <span>${totalPrice}</span>
          </div>
          <div className="summary-row">
            <span>Shipping</span>
            <span>Free</span>
          </div>
          <div className="total-row">
            <span>Total to Pay</span>
            <span>${totalPrice}</span>
          </div>
          <button 
            className="btn-primary w-full" 
            onClick={handleCheckout}
            disabled={loading}
          >
            {loading ? 'Processing...' : 'Pay with Stripe'}
          </button>
          <div className="security-note">
            <ShieldCheck size={16} /> Secure encrypted payment
          </div>
        </div>
      </div>

      <style jsx>{`
        .checkout-page { padding-top: 2rem; }
        .checkout-grid { display: grid; grid-template-columns: 1fr 380px; gap: 2rem; }
        .section-card { padding: 2rem; margin-bottom: 1.5rem; }
        .section-card h3 { display: flex; align-items: center; gap: 0.75rem; margin-bottom: 1.5rem; }
        .row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
        .payment-note { color: var(--text-muted); font-size: 0.9rem; }
        
        .order-summary { padding: 2rem; height: fit-content; }
        .summary-row { display: flex; justify-content: space-between; margin-bottom: 1rem; color: var(--text-muted); }
        .total-row { display: flex; justify-content: space-between; border-top: 1px solid var(--glass-border); pt: 1.5rem; margin-top: 1.5rem; font-size: 1.2rem; font-weight: 800; margin-bottom: 2rem; }
        .security-note { display: flex; align-items: center; justify-content: center; gap: 0.5rem; color: var(--text-muted); font-size: 0.8rem; margin-top: 1rem; }
      `}</style>
    </div>
  );
};

export default Checkout;
