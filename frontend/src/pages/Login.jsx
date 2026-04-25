import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { login } from '../features/authSlice';
import { useNavigate, Link } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { loading, error } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(login({ email, password })).then((res) => {
      if (!res.error) navigate('/');
    });
  };

  return (
    <div className="auth-page container">
      <div className="auth-card glass-card">
        <h2>Welcome Back</h2>
        <p>Please enter your details to sign in.</p>
        
        {error && <div className="error-msg">{error.message || 'Login failed'}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input 
              className="input-field"
              type="email" 
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              placeholder="name@example.com"
              required 
            />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input 
              className="input-field"
              type="password" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              placeholder="••••••••"
              required 
            />
          </div>
          <button type="submit" className="btn-primary w-full" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
        
        <p className="footer-link">
          Don't have an account? <Link to="/register">Sign Up</Link>
        </p>
      </div>

      <style jsx>{`
        .auth-page {
          height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
        }
        .auth-card {
          width: 100%;
          max-width: 450px;
          padding: 3rem;
          text-align: center;
        }
        .auth-card h2 { font-size: 2rem; margin-bottom: 0.5rem; }
        .auth-card p { color: var(--text-muted); margin-bottom: 2rem; }
        .form-group { text-align: left; margin-bottom: 1rem; }
        .form-group label { display: block; margin-bottom: 0.5rem; font-size: 0.9rem; }
        .error-msg {
          background: rgba(244, 63, 94, 0.1);
          color: var(--accent);
          padding: 0.75rem;
          border-radius: 0.5rem;
          margin-bottom: 1.5rem;
        }
        .w-full { width: 100%; }
        .footer-link { margin-top: 1.5rem; font-size: 0.9rem; }
        .footer-link a { color: var(--primary); font-weight: 600; }
      `}</style>
    </div>
  );
};

export default Login;
