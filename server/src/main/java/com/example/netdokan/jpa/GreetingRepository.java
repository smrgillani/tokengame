/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.netdokan.jpa;

//import org.springframework

import com.example.netdokan.model.Greeting;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


/**
 *
 * @author shahzadmasud
 */
public interface GreetingRepository extends CrudRepository<Greeting, Long>{
    
    List<Greeting> findByConsumed(Boolean consumed);
}
