import React from 'react';
import { Link } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { ShoppingCart, User, LogOut, Search } from 'lucide-react';
import { logout } from '../features/authSlice';

const Navbar = () => {
  const { user } = useSelector((state) => state.auth);
  const { items } = useSelector((state) => state.cart);
  const dispatch = useDispatch();

  return (
    <nav className="navbar glass-card">
      <div className="container nav-content">
        <Link to="/" className="logo">
          STORE<span>.</span>
        </Link>

        <div className="search-bar">
          <Search size={18} />
          <input type="text" placeholder="Search products..." />
        </div>

        <div className="nav-links">
          <Link to="/products">Shop</Link>
          <Link to="/cart" className="cart-icon">
            <ShoppingCart size={22} />
            {items.length > 0 && <span className="badge">{items.length}</span>}
          </Link>
          
          {user ? (
            <div className="user-menu">
              <Link to="/profile"><User size={22} /></Link>
              <button onClick={() => dispatch(logout())}><LogOut size={22} /></button>
            </div>
          ) : (
            <Link to="/login" className="btn-primary">Login</Link>
          )}
        </div>
      </div>

      <style jsx>{`
        .navbar {
          position: fixed;
          top: 1rem;
          left: 50%;
          transform: translateX(-50%);
          width: 90%;
          max-width: 1200px;
          z-index: 1000;
          padding: 0.75rem 2rem;
        }
        .nav-content {
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
        .logo {
          font-size: 1.5rem;
          font-weight: 800;
          letter-spacing: -1px;
        }
        .logo span { color: var(--primary); }
        .search-bar {
          background: rgba(255,255,255,0.05);
          border-radius: 2rem;
          padding: 0.5rem 1rem;
          display: flex;
          align-items: center;
          gap: 0.5rem;
          flex: 0.6;
        }
        .search-bar input {
          background: none;
          border: none;
          color: white;
          width: 100%;
        }
        .nav-links {
          display: flex;
          align-items: center;
          gap: 1.5rem;
        }
        .cart-icon { position: relative; }
        .badge {
          position: absolute;
          top: -8px;
          right: -8px;
          background: var(--accent);
          font-size: 0.7rem;
          padding: 2px 6px;
          border-radius: 50%;
        }
        .user-menu {
          display: flex;
          gap: 1rem;
        }
      `}</style>
    </nav>
  );
};

export default Navbar;
