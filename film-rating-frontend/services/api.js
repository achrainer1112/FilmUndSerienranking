import axios from 'axios';

// Axios Instanz für API-Aufrufe
const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

export const titleService = {
  getTitlesByType(type) {
    if (type === 'movie') {
      return axios.get('/api/movies');
    } else if (type === 'series') {
      return axios.get('/api/series');
    } else {
      throw new Error('Unbekannter Typ: ' + type);
    }
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