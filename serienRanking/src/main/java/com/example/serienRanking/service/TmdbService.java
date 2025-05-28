package com.example.serienRanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TmdbService {

    private final WebClient.Builder webClientBuilder;
    private final String apiKey;
    private final String baseUrl;

    @Autowired
    public TmdbService(WebClient.Builder webClientBuilder,
                       @Value("${tmdb.api.key}") String apiKey,
                       @Value("${tmdb.api.baseurl}") String baseUrl) {
        this.webClientBuilder = webClientBuilder;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    // Sucht Titel (Filme, Serien, Personen) anhand eines Suchbegriffs
    public Mono<String> searchTitles(String query) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/search/multi?api_key=" + apiKey + "&query=" + query + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class); // Antwort als JSON-String
    }

    // Holt die Liste an kommenden Filmen (Upcoming Movies)
    public Mono<String> getUpcomingMovies() {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/movie/upcoming?api_key=" + apiKey + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class);
    }

    // Holt beliebte Serien (Popular TV Shows)
    public Mono<String> getPopularTvShows() {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/tv/popular?api_key=" + apiKey + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class);
    }

    // Holt Detailinformationen zu einem bestimmten Film oder einer Serie anhand der ID
    public Mono<String> getTitleById(String type, String id) {
        // Wählt das passende API-Endpoint basierend auf dem Typ ("movie" oder "tv")
        String endpoint = type.equals("movie") ? "/movie/" : "/tv/";
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + endpoint + id + "?api_key=" + apiKey + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class);
    }
}
