package com.example.rssparser.service;

import com.example.rssparser.model.Item;
import com.example.rssparser.repository.ItemRepository;
import com.example.rssparser.service.impl.RSSServiceImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;


import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(RSSServiceImpl.class)
public class RSSServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RSSServiceImpl rssService;

    @Mock
    private SyndFeedInput syndFeedInput;

    @Before
    public void setup() {
        // Define mock behavior for ItemRepository
        Item item1 = new Item();
        item1.setId(1L);
        item1.setTitle("Example Title 1");
        item1.setDescription("Example Description 1");
        item1.setPublicationDate(new Date());
        item1.setUpdatedDate(null);

        Mockito.when(itemRepository.findByTitle("Example Title")).thenReturn(Optional.of(item1));
    }


    @Test
    public void testPollRSSFeed() throws Exception {

        // Mock the URL
        URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments("https://feeds.simplecast.com/54nAGcIl").thenReturn(mockUrl);

        // Mock the SyndFeed
        SyndFeed mockFeed = Mockito.mock(SyndFeed.class);
        Mockito.when(syndFeedInput.build(Mockito.any(XmlReader.class))).thenReturn(mockFeed);

        // Create a mock instance of SyndEntry
        SyndEntry mockEntry = Mockito.mock(SyndEntry.class);

        // Stub the behavior of getUpdatedDate() with a specific Date object
        Date mockUpdatedDate = new Date();
        Mockito.doReturn(mockUpdatedDate).when(mockEntry).getUpdatedDate();

        // Mock the conversion of SyndEntry to Item
        Item mockItem = new Item();
        Mockito.when(rssService.convertToItem(mockEntry)).thenReturn(mockItem);

        // Mock the saveOrUpdateItem method
        Mockito.doNothing().when(rssService).saveOrUpdateItem(mockItem);

        // Mock the SyndFeed's getEntries() method to return the mockEntry
        Mockito.when(mockFeed.getEntries()).thenReturn(Collections.singletonList(mockEntry));

        // Create an instance of the RSSService class
        RSSServiceImpl rssService = new RSSServiceImpl(itemRepository);

        // Call the method under test
        rssService.pollRSSFeed();


        // Verify the URL creation and usage
        PowerMockito.verifyNew(URL.class).withArguments("https://feeds.simplecast.com/54nAGcIl");
        Mockito.verify(syndFeedInput).build(Mockito.any(XmlReader.class));

        // Verify the conversion of SyndEntry to Item
        Mockito.verify(rssService).convertToItem(mockEntry);

        // Verify the saveOrUpdateItem method call
        Mockito.verify(rssService).saveOrUpdateItem(mockItem);
    }
}
