package com.clecio.orderhub.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.clecio.orderhub.dto.CreateOrderRequestDTO;
import com.clecio.orderhub.dto.CustomerDTO;
import com.clecio.orderhub.dto.OrderItemDTO;
import com.clecio.orderhub.dto.OrderResponseDTO;
import com.clecio.orderhub.dto.OrderStatusDTO;
import com.clecio.orderhub.entity.Customer;
import com.clecio.orderhub.entity.Order;
import com.clecio.orderhub.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Customer mappings
    CustomerDTO toCustomerDTO(Customer customer);
    Customer toCustomerEntity(CustomerDTO customerDTO);
    List<CustomerDTO> toCustomerDTOList(List<Customer> customers);

    // OrderItem mappings
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
    OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO);
    List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems);

    // Order mappings
    @Mapping(target = "statusUrl", source = "id", qualifiedByName = "generateStatusUrl")
    OrderResponseDTO toOrderResponseDTO(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "upskillTransactionId", ignore = true)
    @Mapping(target = "paymentLink", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    Order toOrderEntity(CreateOrderRequestDTO createOrderRequestDTO);

    @Mapping(target = "statusDescription", source = "status", qualifiedByName = "getStatusDescription")
    @Mapping(target = "customerEmail", source = "customer.email")
    OrderStatusDTO toOrderStatusDTO(Order order);

    List<OrderResponseDTO> toOrderResponseDTOList(List<Order> orders);

    @Named("generateStatusUrl")
    default String generateStatusUrl(Long orderId) {
        return "/public/orders/" + orderId + "/status";
    }

     @Named("getStatusDescription")
    default String getStatusDescription(com.clecio.orderhub.entity.OrderStatus status) {
        return status != null ? status.getDescription() : null;
    }
}

