import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';

const API_URL = 'http://localhost:8080/cart';

export const createCart = createAsyncThunk('cart/create', async (_, thunkAPI) => {
  try {
    const token = localStorage.getItem('token');
    const response = await axios.post(API_URL, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
    localStorage.setItem('cartId', response.data.id);
    return response.data;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response.data);
  }
});

export const fetchCart = createAsyncThunk('cart/fetch', async (cartId, thunkAPI) => {
  try {
    const token = localStorage.getItem('token');
    const response = await axios.get(`${API_URL}/${cartId}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response.data);
  }
});

export const addToCart = createAsyncThunk('cart/addItem', async ({ cartId, productId }, thunkAPI) => {
  try {
    const token = localStorage.getItem('token');
    const response = await axios.post(`${API_URL}/${cartId}/items`, { productId }, {
      headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response.data);
  }
});

const cartSlice = createSlice({
  name: 'cart',
  initialState: {
    id: localStorage.getItem('cartId'),
    items: [],
    totalPrice: 0,
    loading: false,
    error: null,
  },
  reducers: {
    clearCartLocal: (state) => {
      state.items = [];
      state.totalPrice = 0;
      state.id = null;
      localStorage.removeItem('cartId');
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(createCart.fulfilled, (state, action) => {
        state.id = action.payload.id;
      })
      .addCase(fetchCart.fulfilled, (state, action) => {
        state.items = action.payload.items;
        state.totalPrice = action.payload.price;
      })
      .addCase(addToCart.fulfilled, (state, action) => {
        // Optimistic update or just wait for re-fetch
      });
  },
});

export const { clearCartLocal } = cartSlice.actions;
export default cartSlice.reducer;
