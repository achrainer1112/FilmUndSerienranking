package com.example.serienRanking.service;

import com.example.serienRanking.model.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

    public Mono<String> searchTitles(String query) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/search/multi?api_key=" + apiKey + "&query=" + query + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getUpcomingMovies() {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/movie/upcoming?api_key=" + apiKey + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getPopularTvShows() {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/tv/popular?api_key=" + apiKey + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class);
    }


    public Mono<String> getTitleById(String type, String id) {
        String endpoint = type.equals("movie") ? "/movie/" : "/tv/";
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + endpoint + id + "?api_key=" + apiKey + "&language=de-DE")
                .retrieve()
                .bodyToMono(String.class);
    }
}