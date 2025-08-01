package com.clecio.orderhub.service;

import com.clecio.orderhub.dto.CustomerDTO;
import com.clecio.orderhub.entity.Customer;
import com.clecio.orderhub.mapper.OrderMapper;
import com.clecio.orderhub.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class CustomerService {

        private final CustomerRepository customerRepository;
        private final OrderMapper orderMapper;

        @Transactional
        public Customer getOrCreateCustomer(CustomerDTO customerDTO) {
            Optional<Customer> existingCustomer = customerRepository.findByEmail(customerDTO.getEmail());

            if (existingCustomer.isPresent()) {
                Customer customer = existingCustomer.get();
                if (!customer.getName().equals(customerDTO.getName()) ||
                        !customer.getPhone().equals(customerDTO.getPhone()) ||
                        !customer.getDocument().equals(customerDTO.getDocument())) {
                    customer.setName(customerDTO.getName());
                    customer.setPhone(customerDTO.getPhone());
                    customer.setDocument(customerDTO.getDocument());
                    customer = customerRepository.save(customer);
                    log.info("Cliente atualizado: {}", customer.getEmail());
                }
                return customer;
            } else {
                Customer newCustomer = orderMapper.toCustomerEntity(customerDTO);
                newCustomer = customerRepository.save(newCustomer);
                log.info("Novo cliente criado: {}", newCustomer.getEmail());
                return newCustomer;
            }
        }

        public Optional<CustomerDTO> findByEmail(String email) {
            return customerRepository.findByEmail(email)
                    .map(orderMapper::toCustomerDTO);
        }

        public boolean existsByEmail(String email) {
            return customerRepository.existsByEmail(email);
        }
    }

