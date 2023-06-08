package com.example.rssparser.controller;

import com.example.rssparser.model.ApiResponse;
import com.example.rssparser.model.ResponseBuilder;
import com.example.rssparser.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/item")
    public ResponseEntity<ApiResponse> getLatestItems() {
        return ResponseBuilder.build(ResponseBuilder.success(itemService.getLatestItems()));
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse> getItemsByPageAndSize(@RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam("sort") String sortField,
                                            @RequestParam("direction") String direction) {
        return ResponseBuilder.build(itemService.getItemsByPageAndSize(page, size, sortField, direction));
    }
}
