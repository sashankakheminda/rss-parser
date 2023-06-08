package com.example.rssparser.repository;

import com.example.rssparser.model.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ItemRepositoryTest {
    @Mock
    private ItemRepository itemRepository;


    @Test
    public void testFindTop10ByOrderByPublicationDateDesc() {
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

        when(itemRepository.findTop10ByOrderByPublicationDateDesc()).thenReturn(mockItems);

        // Act
        List<Item> result = itemRepository.findTop10ByOrderByPublicationDateDesc();

        // Assert
        assertEquals(mockItems, result);
    }

    @Test
    public void testFindByTitle() {
        // Arrange
        String title = "Example Title";
        List<Item> mockItem = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setTitle("Example Title 1");
        item1.setDescription("Example Description 1");
        item1.setPublicationDate(new Date());
        item1.setUpdatedDate(null);

        mockItem.add(0, item1);

        when(itemRepository.findByTitle(title)).thenReturn(Optional.of(item1));

        // Act
        Optional<Item> result = itemRepository.findByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(Collections.singletonList(item1), Collections.singletonList(result.get()));

        // Verify
        verify(itemRepository).findByTitle(title);
    }
}
