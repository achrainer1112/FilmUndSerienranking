<template>
  <div class="title-list">
    <div
      v-if="loading"
      class="loading"
    >
      <i class="fas fa-spinner fa-spin" /> Lade Inhalte...
    </div>
    <div
      v-else-if="titles.length === 0"
      class="no-titles"
    >
      <p>{{ emptyMessage }}</p>
    </div>
    <div
      v-else
      class="grid"
    >
      <MovieCard 
        v-for="title in titles" 
        :key="title.id" 
        :title="title" 
        :show-rating="true"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import MovieCard from './MovieCard.vue';
import { titleService } from '../services/api';

const props = defineProps({
  type: {
    type: String,
    default: null, // Wenn null, werden alle Titel angezeigt
    validator: value => value === null || value === 'movie' || value === 'series'
  },
  emptyMessage: {
    type: String,
    default: 'Keine bewerteten Titel gefunden.'
  }
});

const titles = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const response = props.type 
      ? await titleService.getTitlesByType(props.type) 
      : await titleService.getAllTitles();
    
    titles.value = response.data;
  } catch (error) {
    console.error('Fehler beim Laden der Titel:', error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.loading, .no-titles {
  text-align: center;
  padding: 2rem;
  font-size: 1.1rem;
  color: #666;
}

.loading i {
  margin-right: 0.5rem;
}
</style>