package com.example.dulumi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {
    @GetMapping("/map")
    public String map() {
        return "map.tsx";
    }

    @GetMapping("/test/map")
    public String testMap() {
        return "testMap";
    }
}
