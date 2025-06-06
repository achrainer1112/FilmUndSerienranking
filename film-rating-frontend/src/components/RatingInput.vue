<template>
  <div class="rating-input">
    <!-- Durchläuft jede Bewertungskategorie -->
    <div
      v-for="(category, index) in categories"
      :key="index"
      class="rating-category"
    >
      <div class="category-label">
        {{ category.label }}:
      </div>
      <div class="rating-slider">
        <!-- Slider mit direkter Bindung an den jeweiligen Rating-Wert -->
        <input 
          v-model.number="ratings[category.key]" 
          type="range" 
          min="1" 
          max="10" 
          step="1" 
          class="slider"
        >
        <!-- Zeigt den aktuellen Wert an -->
        <div class="rating-value">
          {{ ratings[category.key] }}
        </div>
      </div>
    </div>

    <!-- Zeigt die Gesamtbewertung (Summe aller Einzelbewertungen) -->
    <div class="average-rating">
      <strong>Gesamtbewertung:</strong> {{ totalRating }}
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

// Props für Anfangswerte der Bewertung (Default: alle 5)
const props = defineProps({
  initialRatings: {
    type: Object,
    default: () => ({
      storyRating: 5,
      characterRating: 5,
      actingRating: 5,
      visualAudioRating: 5,
      entertainmentRating: 5
    })
  }
});

// Ermöglicht das Senden von Events an die übergeordnete Komponente
const emit = defineEmits(['update:ratings']);

// Definition der Bewertungskategorien und zugehörigen Labels
const categories = [
  { key: 'storyRating', label: 'Story' },
  { key: 'characterRating', label: 'Charaktere' },
  { key: 'actingRating', label: 'Schauspiel' },
  { key: 'visualAudioRating', label: 'Visuell/Audio' },
  { key: 'entertainmentRating', label: 'Unterhaltung' }
];

// Lokaler Reactive-State für die Bewertungen
const ratings = ref({...props.initialRatings});

// Gesamtbewertung berechnet als Summe aller Einzelwerte
const totalRating = computed(() => {
  const values = Object.values(ratings.value);
  return values.reduce((sum, val) => sum + val, 0);
});

// Reagiert auf Änderungen und sendet neue Bewertung samt Gesamtwert
watch(ratings, (newRatings) => {
  emit('update:ratings', { ...newRatings, overallRating: totalRating.value });
}, { deep: true });
</script>

<style scoped>
.rating-input {
  margin-bottom: 1.5rem;
}

.rating-category {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.category-label {
  width: 120px;
  font-weight: bold;
}

.rating-slider {
  flex: 1;
  display: flex;
  align-items: center;
}

.slider {
  flex: 1;
  height: 8px;
  -webkit-appearance: none;
  appearance: none;
  background: #e0e0e0;
  outline: none;
  border-radius: 4px;
  margin-right: 10px;
}

.slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #003366;
  cursor: pointer;
}

.slider::-moz-range-thumb {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #003366;
  cursor: pointer;
  border: none;
}

.rating-value {
  font-weight: bold;
  min-width: 30px;
  text-align: center;
}

.average-rating {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #f0f0f0;
  border-radius: 4px;
  text-align: center;
  font-size: 1.2rem;
}
</style>
