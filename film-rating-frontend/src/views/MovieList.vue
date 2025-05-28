<template>
  <div class="movie-list">
    <h1 class="section-title">
      Filme
    </h1>

    <ul class="tabs">
      <li :class="{ active: activeTab === 'rated' }">
        <a
          href="#"
          @click.prevent="activeTab = 'rated'"
        >Deine Bewertungen</a>
      </li>
      <li :class="{ active: activeTab === 'upcoming' }">
        <a
          href="#"
          @click.prevent="activeTab = 'upcoming'"
        >Kommende Filme</a>
      </li>
    </ul>

    <div
      v-if="activeTab === 'rated'"
      class="tab-content"
    >
      <TitleList
        type="movie"
        empty-message="Du hast noch keine Filme bewertet."
      />
    </div>

    <div
      v-else-if="activeTab === 'upcoming'"
      class="tab-content"
    >
      <div
        v-if="loadingUpcoming"
        class="loading"
      >
        <i class="fas fa-spinner fa-spin" /> Lade Filme...
      </div>

      <div
        v-else-if="upcomingMovies.length === 0"
        class="no-content card"
      >
        <p>Keine kommenden Filme gefunden oder API-Schlüssel nicht konfiguriert.</p>
      </div>

      <div
        v-else
        class="grid"
      >
        <MovieCard 
          v-for="movie in upcomingMovies" 
          :key="movie.id" 
          :title="movie"
          :is-rated="isRated(movie)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import TitleList from '../components/TitleList.vue';
import MovieCard from '../components/MovieCard.vue';
import { TmdbService } from '../services/TmdbService';
import { titleService } from '../services/api';

const activeTab = ref('rated');
const upcomingMovies = ref([]);
const loadingUpcoming = ref(false);
const ratedTitles = ref([]);

onMounted(async () => {
  // Lade bewertete Filme für den Vergleich
  try {
    const response = await titleService.getTitlesByType('movie');
    ratedTitles.value = response.data;
  } catch (error) {
    console.error('Fehler beim Laden bewerteter Filme:', error);
  }

  // Lade kommende Filme
  loadUpcomingMovies();
});

const loadUpcomingMovies = async () => {
  loadingUpcoming.value = true;
  try {
    upcomingMovies.value = await TmdbService.getUpcomingMovies();
  } catch (error) {
    console.error('Fehler beim Laden kommender Filme:', error);
  } finally {
    loadingUpcoming.value = false;
  }
};

const isRated = (movie) => {
  return ratedTitles.value.some(title => title.tmdbId === movie.id);
};
</script>

<style scoped>
.tabs {
  display: flex;
  list-style: none;
  margin-bottom: 1.5rem;
  border-bottom: 2px solid #e0e0e0;
}

.tabs li {
  margin-right: 1.5rem;
}

.tabs li a {
  display: block;
  padding: 0.75rem 0;
  color: #666;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.tabs li.active a {
  color: #003366;
  border-bottom: 2px solid #003366;
  margin-bottom: -2px;
}

.tab-content {
  padding: 1rem 0;
}

.loading, .no-content {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.loading i {
  margin-right: 0.5rem;
}
</style>