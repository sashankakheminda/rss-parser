package com.example.rssparser.service.impl;

import com.example.rssparser.exception.InvalidSortFieldException;
import com.example.rssparser.model.ApiResponse;
import com.example.rssparser.model.Item;
import com.example.rssparser.model.ResponseBuilder;
import com.example.rssparser.repository.ItemRepository;
import com.example.rssparser.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getLatestItems() {
        return itemRepository.findTop10ByOrderByPublicationDateDesc();
    }

    @Override
    public ApiResponse<List<Item>> getItemsByPageAndSize(int page, int size, String sortField, String direction) {
        ApiResponse<List<Item>> response;

        try {
            Pageable pageable = getPageable(page, size, sortField, direction);
            Page<Item> itemPage = itemRepository.findAll(pageable);
            response = ResponseBuilder.success(itemPage.getContent());
        }catch (Exception ex){
            response = ResponseBuilder.failed(null, ex);
        }
        return response;
    }

    private Pageable getPageable(int page, int size, String sortField, String direction) {
        String column = null;

        switch (sortField) {
            case "id":
                column = "id";
                break;
            case "title":
                column = "title";
                break;
            case "publication_date":
                column = "publicationDate";
                break;
            case "updated_date":
                column = "updatedDate";
                break;

            default:
                throw new InvalidSortFieldException("Invalid sort field: " + sortField);
        }
        Sort sort = Sort.by(column);

        if (direction.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }

        return PageRequest.of(page, size, sort);
    }
}
