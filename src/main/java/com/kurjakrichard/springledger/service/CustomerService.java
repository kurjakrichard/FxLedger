/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurjakrichard.springledger.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author sire
 */
@Scope("prototype")
@Service
public class CustomerService {
    
    public String Customer(){
        return "Az én nevem " + Math.random();
    }
}
