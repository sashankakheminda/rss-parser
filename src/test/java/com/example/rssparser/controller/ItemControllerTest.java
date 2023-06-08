package com.example.rssparser.controller;

import com.example.rssparser.model.ApiResponse;
import com.example.rssparser.model.Item;
import com.example.rssparser.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    public void testGetLatestItems() {
        // Arrange
        List<Item> mockItems = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setTitle("Example Title 1");
        item1.setDescription("Example Description 1");
        item1.setPublicationDate(new Date());
        item1.setUpdatedDate(null);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setTitle("Example Title 2");
        item2.setDescription("Example Description 2");
        item2.setPublicationDate(new Date());
        item2.setUpdatedDate(null);

        mockItems.add(0, item1);
        mockItems.add(1, item2);

        when(itemService.getLatestItems()).thenReturn(mockItems);

        // Act
        ResponseEntity<ApiResponse> response = itemController.getLatestItems();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockItems, response.getBody().getData());

        // Verify
        verify(itemService).getLatestItems();
    }

    @Test
    public void testGetItemsByPageAndSize() {
        // Arrange
        int page = 1;
        int size = 10;
        String sortField = "updated_date";
        String direction = "asc";
        List<Item> mockItems = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setTitle("Example Title 1");
        item1.setDescription("Example Description 1");
        item1.setPublicationDate(new Date());
        item1.setUpdatedDate(null);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setTitle("Example Title 2");
        item2.setDescription("Example Description 2");
        item2.setPublicationDate(new Date());
        item2.setUpdatedDate(null);

        mockItems.add(item1);
        mockItems.add(item2);

        ApiResponse<List<Item>> mockApiResponse = new ApiResponse<>(200, "success", mockItems);

        when(itemService.getItemsByPageAndSize(page, size, sortField, direction)).thenReturn(mockApiResponse);

        // Act
        ResponseEntity<ApiResponse> response = itemController.getItemsByPageAndSize(page, size, sortField, direction);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockApiResponse, response.getBody());

        // Verify
        verify(itemService).getItemsByPageAndSize(page, size, sortField, direction);
    }
}
