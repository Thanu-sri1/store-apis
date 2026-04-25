import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

const Register = () => {
  const [formData, setFormData] = useState({ name: '', email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await axios.post('http://localhost:8080/users', formData);
      navigate('/login');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
      setLoading(false);
    }
  };

  return (
    <div className="auth-page container">
      <div className="auth-card glass-card">
        <h2>Create Account</h2>
        <p>Join our premium community today.</p>
        
        {error && <div className="error-msg">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <input 
            className="input-field"
            type="text" 
            placeholder="Full Name"
            value={formData.name}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            required 
          />
          <input 
            className="input-field"
            type="email" 
            placeholder="Email Address"
            value={formData.email}
            onChange={(e) => setFormData({...formData, email: e.target.value})}
            required 
          />
          <input 
            className="input-field"
            type="password" 
            placeholder="Create Password"
            value={formData.password}
            onChange={(e) => setFormData({...formData, password: e.target.value})}
            required 
          />
          <button type="submit" className="btn-primary w-full" disabled={loading}>
            {loading ? 'Creating...' : 'Sign Up'}
          </button>
        </form>
        
        <p className="footer-link">
          Already have an account? <Link to="/login">Sign In</Link>
        </p>
      </div>

      <style jsx>{`
        .auth-page { height: 100vh; display: flex; align-items: center; justify-content: center; }
        .auth-card { width: 100%; max-width: 450px; padding: 3rem; text-align: center; }
        .auth-card h2 { font-size: 2rem; margin-bottom: 0.5rem; }
        .auth-card p { color: var(--text-muted); margin-bottom: 2rem; }
        .error-msg { background: rgba(244, 63, 94, 0.1); color: var(--accent); padding: 0.75rem; border-radius: 0.5rem; margin-bottom: 1.5rem; }
        .footer-link { margin-top: 1.5rem; font-size: 0.9rem; }
        .footer-link a { color: var(--primary); font-weight: 600; }
      `}</style>
    </div>
  );
};

export default Register;
