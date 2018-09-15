package com.bornium.boostrappingascode.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cloud")
public class CloudController {

    @GetMapping
    public void listClouds(){

    }

    @GetMapping("/{pid}")
    public void getCloud(@RequestParam("pid") String pid){
        System.out.println(pid);
    }

    @PutMapping
    public void putCloud(){

    }

    @DeleteMapping("/{pid}")
    public void deleteCloud(@RequestParam("pid") String pid){

    }
}
