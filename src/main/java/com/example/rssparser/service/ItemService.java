package com.example.rssparser.service;

import com.example.rssparser.model.ApiResponse;
import com.example.rssparser.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getLatestItems();
    ApiResponse<List<Item>> getItemsByPageAndSize(int page, int size, String sortField, String direction);
}
