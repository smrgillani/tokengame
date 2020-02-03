/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.netdokan;

import com.example.netdokan.jpa.GreetingRepository;
import com.example.netdokan.model.Greeting;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shahzadmasud
 */
@RestController
public class NetdokanRESTController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final List<Greeting> tokens = new ArrayList<>();
    private final List<Greeting> consumed = new ArrayList<>();

    @Autowired
    private GreetingRepository greetingRepository;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/generate")
    public Greeting generate(@RequestParam(value = "count", defaultValue = "10") String count) {
//        Random rand = new Random(Long.MAX_VALUE);
        int total = Integer.parseInt(count);
        for (int i = 0; i < total; i++) {
            //tokens.add(new Greeting(rand.nextLong(), UUID.randomUUID().toString()));
            Greeting g = new Greeting(UUID.randomUUID().toString());
            greetingRepository.save(g);
        }
        return new Greeting(total, "Success");
    }

    @RequestMapping("/consume")
    public Greeting consume(@RequestParam(value = "tokenid", required = true) String tokenid) {
        long total = Long.parseLong(tokenid);
        Greeting g = greetingRepository.findOne(total);
//        int idx = tokens.indexOf(new Greeting(total, tokenid));
//        if (idx != -1) {
        if (g != null) {
//            Greeting g = tokens.remove(idx);
            g.setConsumedAt();
//            consumed.add(g);
            greetingRepository.save(g);
            return g;
        }
        return new Greeting(0, "Invalid Token Id or already consumed");
    }

    @RequestMapping("/alltokens")
    public Iterable<Greeting> alltokens() {
        return greetingRepository.findAll();
    }

    @RequestMapping("/allconsumed")
    public Iterable<Greeting> allconsumed() {
        return greetingRepository.findByConsumed(Boolean.TRUE);
    }

}
