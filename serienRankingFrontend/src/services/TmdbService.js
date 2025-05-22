import { tmdbService } from './api';

export const TmdbService = {
  // Bild-URL für Poster zusammenbauen
  getPosterUrl(path, size = 'w500') {
    if (!path) return null;
    return `https://image.tmdb.org/t/p/${size}${path}`;
  },

  // Formatierung der Suchergebnisse
  formatSearchResults(results) {
    if (!results || !results.results) return [];
    
    return results.results
      .filter(item => item.media_type === 'movie' || item.media_type === 'tv')
      .map(item => {
        return {
          id: item.id.toString(),
          name: item.title || item.name,
          type: item.media_type === 'movie' ? 'movie' : 'series',
          posterPath: item.poster_path,
          overview: item.overview || 'Keine Beschreibung verfügbar',
          releaseDate: item.release_date || item.first_air_date
        };
      });
  },

  // Formatierung der Trendfilme/Serien
  formatTrendingResults(results, type) {
    if (!results || !results.results) return [];
    
    return results.results.map(item => {
      return {
        id: item.id.toString(),
        name: type === 'movie' ? item.title : item.name,
        type: type,
        posterPath: item.poster_path,
        overview: item.overview || 'Keine Beschreibung verfügbar',
        releaseDate: type === 'movie' ? item.release_date : item.first_air_date
      };
    });
  },

  async getTitleById(type, id) {
    const response = await fetch(`/api/tmdb/title/${type}/${id}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
  },

  // Suche durchführen
  async searchMedia(query) {
    try {
      const response = await tmdbService.searchTitles(query);
      return this.formatSearchResults(response.data);
    } catch (error) {
      console.error('Fehler bei der Suche:', error);
      return [];
    }
  },

  // Kommende Filme abrufen
  async getUpcomingMovies() {
    try {
      const response = await tmdbService.getUpcomingMovies();
      return this.formatTrendingResults(response.data, 'movie');
    } catch (error) {
      console.error('Fehler beim Abrufen kommender Filme:', error);
      return [];
    }
  },

  // Beliebte Serien abrufen
  async getPopularTvShows() {
    try {
      const response = await tmdbService.getPopularTvShows();
      return this.formatTrendingResults(response.data, 'series');
    } catch (error) {
      console.error('Fehler beim Abrufen beliebter Serien:', error);
      return [];
    }
  }
};