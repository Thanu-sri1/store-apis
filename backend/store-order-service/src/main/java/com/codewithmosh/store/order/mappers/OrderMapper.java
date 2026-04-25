package com.codewithmosh.store.order.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithmosh.store.order.dto.GetOrderDto;
import com.codewithmosh.store.order.dto.GetOrderItemDto;
import com.codewithmosh.store.order.entities.Order;
import com.codewithmosh.store.order.entities.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    GetOrderDto toGetOrderDto(Order order);

    GetOrderItemDto toGetOrderItemDto(OrderItem orderItem);
}
