package com.example.spring_elastic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SyncfusionController {

    @GetMapping("/syncfusion")
    public String showSyncfusionComponents(Model model) {
        // You can pass data to the view via the model if needed
        return "syncfusion-view";
    }
}