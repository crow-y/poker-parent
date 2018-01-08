package org.tc.moonlighting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Crow on 2018/1/8.
 *
 * @author Crow
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }
}
