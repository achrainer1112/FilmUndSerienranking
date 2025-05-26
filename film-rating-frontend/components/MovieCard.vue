<template>
  <div class="movie-card">
    <div class="poster">
      <img 
        v-if="posterUrl" 
        :src="posterUrl" 
        :alt="title.name" 
        class="poster-image"
      >
      <div
        v-else
        class="no-poster"
      >
        <i class="fas fa-film" />
      </div>
    </div>
    <div class="info">
      <h3 class="title">
        {{ title.name }}
      </h3>
      <div
        v-if="showRating"
        class="rating"
      >
        <div class="overall-rating">
          {{ formattedRating }}
        </div>
        <div class="rating-details">
          <div class="rating-item">
            <span>Story:</span> {{ title.storyRating }}/10
          </div>
          <div class="rating-item">
            <span>Charaktere:</span> {{ title.characterRating }}/10
          </div>
          <div class="rating-item">
            <span>Schauspiel:</span> {{ title.actingRating }}/10
          </div>
          <div class="rating-item">
            <span>Visuell/Audio:</span> {{ title.visualAudioRating }}/10
          </div>
          <div class="rating-item">
            <span>Unterhaltung:</span> {{ title.entertainmentRating }}/10
          </div>
        </div>
      </div>
      <p
        v-if="title.overview"
        class="overview"
      >
        {{ truncateOverview }}
      </p>
      <div class="actions">
        <slot name="actions" />
        <router-link 
          v-if="!showRating && !isRated" 
          :to="`/rate/${title.type}/${title.id}`" 
          class="btn"
        >
          Bewerten
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { TmdbService } from '../services/TmdbService';

const props = defineProps({
  title: {
    type: Object,
    required: true
  },
  isRated: {
    type: Boolean,
    default: false
  },
  showRating: {
    type: Boolean,
    default: false
  }
});

const posterUrl = computed(() => {
  return props.title.posterPath 
    ? TmdbService.getPosterUrl(props.title.posterPath) 
    : null;
});

const truncateOverview = computed(() => {
  const overview = props.title.overview || '';
  return overview.length > 150 ? overview.substring(0, 150) + '...' : overview;
});

const formattedRating = computed(() => {
  return props.title.overallRating 
    ? props.title.overallRating.toFixed(1) + ' / 50' 
    : 'Nicht bewertet';
});
</script>

<style scoped>
.movie-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  transition: transform 0.3s, box-shadow 0.3s;
}

.movie-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.poster {
  position: relative;
  width: 100%;
  height: 0;
  padding-bottom: 150%;
  overflow: hidden;
}

.poster-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-poster {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e0e0e0;
  color: #666;
  font-size: 3rem;
}

.info {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.title {
  margin-bottom: 0.5rem;
  font-size: 1.2rem;
  color: #003366;
}

.overview {
  margin-bottom: 1rem;
  font-size: 0.9rem;
  color: #555;
  flex: 1;
}

.rating {
  margin-bottom: 1rem;
}

.overall-rating {
  font-size: 1.5rem;
  font-weight: bold;
  color: #003366;
  margin-bottom: 0.5rem;
}

.rating-details {
  font-size: 0.85rem;
}

.rating-item {
  margin-bottom: 0.25rem;
  color: #666;
}

.rating-item span {
  font-weight: bold;
  color: #444;
}

.actions {
  margin-top: auto;
  display: flex;
  justify-content: flex-end;
}

.actions .btn {
  font-size: 0.9rem;
}
</style>