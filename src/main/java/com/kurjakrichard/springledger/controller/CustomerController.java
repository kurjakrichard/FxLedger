/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurjakrichard.springledger.controller;

import com.kurjakrichard.springledger.domain.Customer;
import com.kurjakrichard.springledger.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author balza
 */
@RestController
public class CustomerController {

    private CustomerService customerService;
    
    @Value("${customer.firstname}")
    private String message;
    

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/")
    public String customerName() {
        return message;
//        return customerService.Customer();
    }
}
