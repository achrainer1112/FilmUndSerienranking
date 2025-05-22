<template>
  <div class="series-list">
    <h1 class="section-title">
      Serien
    </h1>

    <ul class="tabs">
      <li :class="{ active: activeTab === 'rated' }">
        <a
          href="#"
          @click.prevent="activeTab = 'rated'"
        >Deine Bewertungen</a>
      </li>
      <li :class="{ active: activeTab === 'popular' }">
        <a
          href="#"
          @click.prevent="activeTab = 'popular'"
        >Beliebte Serien</a>
      </li>
    </ul>

    <div
      v-if="activeTab === 'rated'"
      class="tab-content"
    >
      <TitleList
        type="series"
        empty-message="Du hast noch keine Serien bewertet."
      />
    </div>

    <div
      v-else-if="activeTab === 'popular'"
      class="tab-content"
    >
      <div
        v-if="loadingPopular"
        class="loading"
      >
        <i class="fas fa-spinner fa-spin" /> Lade Serien...
      </div>

      <div
        v-else-if="popularSeries.length === 0"
        class="no-content card"
      >
        <p>Keine beliebten Serien gefunden oder API-Schlüssel nicht konfiguriert.</p>
      </div>

      <div
        v-else
        class="grid"
      >
        <MovieCard 
          v-for="series in popularSeries" 
          :key="series.id" 
          :title="series"
          :is-rated="isRated(series)"
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
const popularSeries = ref([]);
const loadingPopular = ref(false);
const ratedTitles = ref([]);

onMounted(async () => {
  // Lade bewertete Serien für den Vergleich
  try {
    const response = await titleService.getTitlesByType('series');
    ratedTitles.value = response.data;
  } catch (error) {
    console.error('Fehler beim Laden bewerteter Serien:', error);
  }

  // Lade beliebte Serien
  loadPopularSeries();
});

const loadPopularSeries = async () => {
  loadingPopular.value = true;
  try {
    popularSeries.value = await TmdbService.getPopularTvShows();
  } catch (error) {
    console.error('Fehler beim Laden beliebter Serien:', error);
  } finally {
    loadingPopular.value = false;
  }
};

const isRated = (series) => {
  return ratedTitles.value.some(title => title.tmdbId === series.id);
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