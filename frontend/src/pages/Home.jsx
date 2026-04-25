import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchProducts } from '../features/productSlice';
import { ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';

const Home = () => {
  const dispatch = useDispatch();
  const { items, loading } = useSelector((state) => state.products);

  useEffect(() => {
    dispatch(fetchProducts());
  }, [dispatch]);

  return (
    <div className="home-page">
      <section className="hero">
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="container hero-content"
        >
          <h1>Elevate Your Style <span>Seamlessly.</span></h1>
          <p>Discover our curated collection of premium essentials designed for the modern individual.</p>
          <Link to="/products" className="btn-primary">
            Explore Collection <ArrowRight size={18} />
          </Link>
        </motion.div>
      </section>

      <section className="featured container">
        <div className="section-header">
          <h2>New Arrivals</h2>
          <Link to="/products">View All</Link>
        </div>
        
        <div className="product-grid">
          {items.slice(0, 4).map((product) => (
            <Link to={`/products/${product.id}`} key={product.id} className="product-card glass-card">
              <div className="product-img">
                {/* Image placeholder or real image */}
                <div className="placeholder"></div>
              </div>
              <div className="product-info">
                <h3>{product.name}</h3>
                <p className="price">${product.price}</p>
              </div>
            </Link>
          ))}
        </div>
      </section>

      <style jsx>{`
        .hero {
          height: 80vh;
          display: flex;
          align-items: center;
          background: radial-gradient(circle at top right, rgba(99, 102, 241, 0.15), transparent);
        }
        .hero-content { text-align: center; max-width: 800px; }
        .hero h1 { font-size: 4rem; line-height: 1.1; margin-bottom: 1.5rem; }
        .hero h1 span { color: var(--primary); }
        .hero p { font-size: 1.2rem; color: var(--text-muted); margin-bottom: 2.5rem; }
        
        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-end;
          margin-bottom: 2rem;
        }
        .product-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
          gap: 2rem;
        }
        .product-card {
          padding: 1rem;
          transition: transform 0.3s ease;
        }
        .product-card:hover { transform: translateY(-10px); }
        .product-img {
          height: 300px;
          background: rgba(255,255,255,0.05);
          border-radius: 0.5rem;
          margin-bottom: 1rem;
        }
        .product-info h3 { font-size: 1.1rem; margin-bottom: 0.5rem; }
        .product-info .price { color: var(--primary); font-weight: 700; }
      `}</style>
    </div>
  );
};

export default Home;
