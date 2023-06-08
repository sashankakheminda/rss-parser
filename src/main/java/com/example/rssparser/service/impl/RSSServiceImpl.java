package com.example.rssparser.service.impl;

import com.example.rssparser.model.Item;
import com.example.rssparser.repository.ItemRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RSSServiceImpl {
    private final ItemRepository itemRepository;

    private final SyndFeedInput syndFeedInput;

    @Autowired
    public RSSServiceImpl(ItemRepository itemRepository, SyndFeedInput syndFeedInput) {
        this.itemRepository = itemRepository;
        this.syndFeedInput = syndFeedInput;
    }

    @SneakyThrows
    @Scheduled(fixedRate = 60000) // Run every 5 minutes
    public void pollRSSFeed() {
        System.out.println("running cron job");
        try {
            // Replace the URL with the RSS feed URL you want to consume
            URL feedUrl = new URL("https://feeds.simplecast.com/54nAGcIl");

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            List<Item> newItems = feed.getEntries().stream()
                    .map(this::convertToItem)
                    .collect(Collectors.toList());

            newItems.forEach(this::saveOrUpdateItem);
        }catch (IOException ex) {
            throw new IOException("Error connecting to the RSS feed: " + ex.getMessage());
        }catch (FeedException ex){
            throw new FeedException("Feed exception occurred: "+ ex.getMessage());
        } catch (Exception ex) {
            throw new Exception("exception occurred: "+ ex.getMessage());
        }
    }

    public Item convertToItem(SyndEntry entry) {
        Item item = new Item();
        item.setTitle(entry.getTitle());
        if (entry.getDescription() != null) {
            item.setDescription(entry.getDescription().getValue());
        }
        item.setPublicationDate(new Date());
        Date entryUpdatedDate = entry.getUpdatedDate();
        item.setUpdatedDate(entryUpdatedDate != null ? entryUpdatedDate : null);
        return item;
    }

    public void saveOrUpdateItem(Item item) {
        if (item.getDescription() != null && item.getDescription().length() > 1000) {
            item.setDescription(item.getDescription().substring(0, 1000)); // Truncate the description to fit within the column size limit
        }
        if (item.getTitle() != null) {
            Optional<Item> existingItem = itemRepository.findByTitle(item.getTitle());

            if (existingItem.isPresent()) {
                // Item already exists in the database, update its fields
                Item currentItem = existingItem.get();
                currentItem.setTitle(item.getTitle());
                currentItem.setDescription(item.getDescription());
                currentItem.setPublicationDate(item.getPublicationDate());
                currentItem.setUpdatedDate(item.getUpdatedDate());
                itemRepository.save(currentItem);
            } else {
                // Item does not exist in the database, save it as a new item
                itemRepository.save(item);
            }
        } else {
            // Item title is null, treat it as a new item and save it
            itemRepository.save(item);
        }
    }
}
