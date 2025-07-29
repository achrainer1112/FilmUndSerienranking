# FilmUndSerienranking
# Movie Ranking Application

Welcome to the Movie Ranking application! This project offers a dynamic web application where users can individually rate their favorite movies and series, creating a personalized ranking system to discover and revisit their all-time favorites.

## Table of Contents
- [Purpose](#purpose)
- [Features](#features)
- [How it Works](#how-it-works)
- [Technologies Used](#technologies-used)
- [Challenges](#challenges)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
- [Contributions](#contributions)
- [License](#license)

## Purpose
The aim of this project is to provide an engaging platform that allows users to evaluate and rank movies and TV series based on their personal preferences. Unlike generic online rankings, this application offers a unique way for users to express their tastes and preferences.

## Features
- **Personal Rating System**: Users can rate movies and series in multiple categories: 
  - Story
  - Characters
  - Acting
  - Visual & Audio
  - Entertainment  
- **Dynamic Rankings**: Users can view their rated titles in a personalized ranking system.
- **Latest Releases**: Stay updated with the latest movie and series releases.
- **Search Functionality**: Look for specific movies or series using a robust search feature.
- **Cross-platform**: Compatible with both desktop and mobile devices.

## How it Works
1. **User Input**: On the homepage, users can enter the name of a movie or series.
2. **API Integration**: The application interacts with a public API (TMDB) to suggest titles as users type.
3. **Rating System**: Upon selecting a title, the user can rate it across five categories. Each category allows ratings from 1 to 5 stars, and an overall rating is calculated based on these inputs.
4. **Storing Data**: Ratings are stored in a database, and users can see all their previously rated titles, sorted based on their personal evaluations.
5. **Current Releases**: The application provides a section to discover new releases, helping users find something new to watch.

## Technologies Used
- **Frontend**: [Vue.js](https://vuejs.org/)
- **Backend**: [Java with Spring Boot](https://spring.io/projects/spring-boot)
- **Database**: [MySQL](https://www.mysql.com/) or H2 Database with JPA
- **API**: TMDB API (The Movie Database) for accessing movie and series data
- **Communication**: HTTP with JSON data format between frontend and backend

## Challenges
- Ensuring a clean separation between frontend and backend while utilizing the API.
- Dynamically displaying and toggling between movies and series in the rankings.
- Efficiently calculating and storing ratings in the database.
- Designing an intuitive and appealing user interface.

## Installation and Setup
To run this application locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the `serienRanking` directory and set up the database using MySQL or H2.
3. Update the configuration in `src/main/resources/application.properties` with your database credentials.
4. Build the project using Maven:
   ```bash
   ./mvnw clean install
   ```
5. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
6. Open the frontend application:
   - Navigate to the `film-rating-frontend` directory.
   - Install the dependencies and run the application using:
     ```bash
     npm install
     npm run serve
     ```
   - The application will typically be accessible at `http://localhost:8081`.

## Usage
- Open your web browser and go to the application's URL. 
- Use the input field to search for movies or series and start rating them based on your preferences.
- View your personal rankings and discover the latest releases.

## Contributions
Contributions are welcome! If you'd like to contribute to the project, please fork the repository and submit a pull request. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

---

Feel free to explore, rate, and discover your next favorite show or movie with our Movie Ranking application!

---
*README generated from 41 files (37,217 tokens)*