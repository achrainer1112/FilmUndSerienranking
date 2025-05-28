<template>
  <div class="home-view">
    <!-- Begrüßungsabschnitt -->
    <section class="welcome-section">
      <div class="card">
        <h1>Willkommen bei Movie Ranking</h1>
        <p>
          Entdecke und bewerte Filme und Serien nach deinen persönlichen Kriterien.
          Erstelle deine eigene Rangliste und halte fest, was dir wirklich gefallen hat.
        </p>
      </div>
    </section>

    <!-- Abschnitt für top-bewertete Serien -->
    <section
      v-if="!loading"
      class="trending-section"
    >
      <h2 class="section-title">
        Deine Lieblingsserien
      </h2>

      <!-- Hinweis, wenn noch keine Bewertungen vorhanden sind -->
      <div
        v-if="topRatedTitles.length === 0"
        class="no-ratings-card card"
      >
        <p>Du hast noch keine Filme oder Serien bewertet!</p>
        <p>Entdecke neue Inhalte und beginne mit dem Bewerten, um deine persönliche Rangliste zu erstellen.</p>
        <div class="action-buttons">
          <router-link
            to="/movies"
            class="btn"
          >
            Filme entdecken
          </router-link>
          <router-link
            to="/series"
            class="btn btn-secondary"
          >
            Serien entdecken
          </router-link>
        </div>
      </div>

      <!-- Zeige die Top 4 bewerteten Serien -->
      <div
        v-else
        class="grid"
      >
        <MovieCard 
          v-for="title in topRatedTitles.slice(0, 4)" 
          :key="title.id" 
          :title="title" 
          :show-rating="true"
        />
      </div>
    </section>

    <!-- Weitere Entdeckungsmöglichkeiten -->
    <section class="discover-section">
      <div class="row">
        <div class="discover-card card">
          <h2>Kommende Filme</h2>
          <p>Entdecke die neuesten Filme, die bald in die Kinos kommen, und füge sie zu deiner Watchlist hinzu.</p>
          <div class="card-action">
            <router-link
              to="/movies"
              class="btn"
            >
              Filme entdecken
            </router-link>
          </div>
        </div>

        <div class="discover-card card">
          <h2>Beliebte Serien</h2>
          <p>Sieh dir an, welche Serien gerade im Trend sind, und finde neue Lieblingsinhalte zum Bewerten.</p>
          <div class="card-action">
            <router-link
              to="/series"
              class="btn"
            >
              Serien entdecken
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import MovieCard from '../components/MovieCard.vue';
import { titleService } from '../services/api';

const loading = ref(true); // Zustand, ob noch geladen wird
const topRatedTitles = ref([]); // Top-bewertete Serien

// Daten beim Mounten der Komponente laden
onMounted(async () => {
  try {
    const seriesResponse = await titleService.getTitlesByType('series');

    // Titel aus der API-Antwort speichern
    topRatedTitles.value = seriesResponse.data;

    console.log('Kombinierte API Antwort:', topRatedTitles.value);
  } catch (error) {
    console.error('Fehler beim Laden der Top-Bewertungen:', error);
  } finally {
    loading.value = false; // Ladeanzeige beenden
  }
});
</script>

<style scoped>
.welcome-section {
  margin-bottom: 2.5rem;
}

.welcome-section h1 {
  color: #003366;
  margin-bottom: 1rem;
}

.trending-section {
  margin-bottom: 2.5rem;
}

.no-ratings-card {
  text-align: center;
  padding: 2rem;
}

.action-buttons {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.discover-section .row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.discover-card {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.discover-card h2 {
  color: #003366;
  margin-bottom: 1rem;
}

.card-action {
  margin-top: auto;
  padding-top: 1.5rem;
}

@media (max-width: 768px) {
  .discover-section .row {
    grid-template-columns: 1fr;
  }
}
</style>
