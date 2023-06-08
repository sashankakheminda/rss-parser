package com.example.rssparser.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 1000)
    private String description;
    @Column(name = "publication_date")
    private Date  publicationDate;
    @Column(name = "updated_date")
    private Date updatedDate;
}
