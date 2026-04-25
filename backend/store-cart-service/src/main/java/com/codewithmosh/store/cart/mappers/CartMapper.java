package com.codewithmosh.store.cart.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithmosh.store.cart.dto.CartDto;
import com.codewithmosh.store.cart.dto.CartItemDto;
import com.codewithmosh.store.cart.entities.Cart;
import com.codewithmosh.store.cart.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "price", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
