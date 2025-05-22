<template>
  <div class="search-results">
    <h1 class="section-title">
      Suchergebnisse für "{{ query }}"
    </h1>
    
    <div
      v-if="loading"
      class="loading"
    >
      <i class="fas fa-spinner fa-spin" /> Suche läuft...
    </div>
    
    <div
      v-else-if="results.length === 0"
      class="no-results card"
    >
      <p>Keine Ergebnisse gefunden für "{{ query }}".</p>
      <p>Versuche es mit einem anderen Suchbegriff oder prüfe die Schreibweise.</p>
      <router-link
        to="/"
        class="btn"
      >
        Zurück zur Startseite
      </router-link>
    </div>
    
    <div v-else>
      <div class="search-filters">
        <button 
          :class="{ active: activeFilter === 'all' }" 
          class="filter-btn"
          @click="activeFilter = 'all'"
        >
          Alle ({{ results.length }})
        </button>
        <button 
          :class="{ active: activeFilter === 'movie' }" 
          class="filter-btn"
          @click="activeFilter = 'movie'"
        >
          Filme ({{ movieCount }})
        </button>
        <button 
          :class="{ active: activeFilter === 'series' }" 
          class="filter-btn"
          @click="activeFilter = 'series'"
        >
          Serien ({{ seriesCount }})
        </button>
      </div>
      
      <div class="grid">
        <MovieCard 
          v-for="item in filteredResults" 
          :key="item.id" 
          :title="item"
          :is-rated="isRated(item)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import MovieCard from '../components/MovieCard.vue';
import { TmdbService } from '../services/TmdbService';
import { titleService } from '../services/api';

const route = useRoute();
const query = ref('');
const results = ref([]);
const loading = ref(true);
const activeFilter = ref('all');
const ratedTitles = ref([]);

// Aktualisiere die Suche, wenn sich der Query-Parameter ändert
watch(() => route.query.q, (newQuery) => {
  if (newQuery && newQuery !== query.value) {
    query.value = newQuery;
    performSearch();
  }
});

onMounted(async () => {
  query.value = route.query.q || '';
  
  // Lade alle bewerteten Titel für den Vergleich
  try {
    const response = await titleService.getAllTitles();
    ratedTitles.value = response.data;
  } catch (error) {
    console.error('Fehler beim Laden der bewerteten Titel:', error);
  }
  
  if (query.value) {
    performSearch();
  } else {
    loading.value = false;
  }
});

const performSearch = async () => {
  loading.value = true;
  results.value = [];
  
  try {
    results.value = await TmdbService.searchMedia(query.value);
  } catch (error) {
    console.error('Fehler bei der Suche:', error);
  } finally {
    loading.value = false;
  }
};

const filteredResults = computed(() => {
  if (activeFilter.value === 'all') {
    return results.value;
  }
  return results.value.filter(item => item.type === activeFilter.value);
});


const movieCount = computed(() => {
  return results.value.filter(item => item.type === 'movie').length;
});

const seriesCount = computed(() => {
  return results.value.filter(item => item.type === 'series').length;
});

const isRated = (item) => {
  return ratedTitles.value.some(title => title.tmdbId === item.id);
};
</script>

<style scoped>
.loading, .no-results {
  text-align: center;
  padding: 2rem;
}

.no-results {
  margin-bottom: 2rem;
}

.no-results .btn {
  margin-top: 1.5rem;
}

.search-filters {
  margin-bottom: 1.5rem;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.filter-btn {
  padding: 0.5rem 1rem;
  background-color: #f0f0f0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
}

.filter-btn.active {
  background-color: #003366;
  color: white;
}

.filter-btn:hover:not(.active) {
  background-color: #e0e0e0;
}
</style>