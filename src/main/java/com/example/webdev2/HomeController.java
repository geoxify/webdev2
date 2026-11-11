package com.example.webdev2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Root controller routing root path "/" to "/products".
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }
}
