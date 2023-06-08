package com.example.rssparser.repository;

import com.example.rssparser.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findTop10ByOrderByPublicationDateDesc();

    Optional<Item> findByTitle(String title);
}
