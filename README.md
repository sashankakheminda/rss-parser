# RSS Parser

The RSS Parser is a Java application that allows you to parse and extract data from RSS feeds.

## Features

* Retrieve and parse RSS feeds
* Extract item information from feeds
* Store and retrieve parsed items in a database

## Technologies Used

* Java
* Spring Boot
* Maven
* JPA
* JUnit
* Mockito
* H2 

## Getting Started

To run the RSS Parser application, ensure you have the following:

* Java 8 or higher
* H2 Memory Database

## Configuration

1. Clone the repository:

* `git clone https://github.com/sashankakheminda/rss-parser.git`
* Note: please use `master` branch

2. Set the RSS feed URL:

Open the `src/main/resources/application.properties` file.

Update the following property with your desired RSS feed URL:

`rss.feed.url=`

## Build and Run

1. Build the project using Maven:

* `cd rss-parser`

* `mvn clean package`

2. Run the application:

* `java -jar target/rss-parser-0.0.1-SNAPSHOT.jar`

The application will start running and begin parsing the specified RSS feed.

## Usage

Once the application is running, it will automatically retrieve and parse the RSS feed. The parsed items will be stored in the database.

You can access the parsed items by using the following endpoints:

* Retrieving the 10 newest items

#### GET
`http://localhost:8080/item`

* Paginated, with direction based on a given field

#### GET
`http://localhost:8080/items?page=1&size=5&sort=publicationdate&direction=asc`
