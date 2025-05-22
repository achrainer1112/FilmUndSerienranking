import axios from 'axios';

// Axios Instanz für API-Aufrufe
const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Titel-Services
export const titleService = {
  // Alle Titel abrufen
  getAllTitles() {
    return api.get('/titles');
  },

  // Titel nach Typ abrufen (Filme oder Serien)
  getTitlesByType(type) {
    return api.get(`/titles/type/${type}`);
  },

  // Neuen Titel mit Bewertung speichern
  saveTitle(title) {
    return api.post('/titles', title);
  }
};

// TMDB-Services
export const tmdbService = {
  // Suche nach Titel (Filme und Serien)
  searchTitles(query) {
    return api.get(`/tmdb/search?query=${encodeURIComponent(query)}`);
  },

  // Kommende Filme abrufen
  getUpcomingMovies() {
    return api.get('/tmdb/upcoming');
  },

  // Beliebte Serien abrufen
  getPopularTvShows() {
    return api.get('/tmdb/popular-tv');
  }
};