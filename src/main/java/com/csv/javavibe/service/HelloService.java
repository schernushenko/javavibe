package com.csv.javavibe.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    /**
     * Returns a greeting message.
     * @return The greeting string.
     */
    public String getGreeting() {
        return "Hello World from Service!";
    }
}