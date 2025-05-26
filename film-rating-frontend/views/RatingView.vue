<template>
  <div class="rating-view">
    <div
      v-if="loading"
      class="loading"
    >
      <i class="fas fa-spinner fa-spin" /> Lade Inhalt...
    </div>
    
    <div
      v-else-if="error"
      class="error card"
    >
      <h2>Fehler beim Laden</h2>
      <p>{{ error }}</p>
      <router-link
        to="/"
        class="btn"
      >
        Zurück zur Startseite
      </router-link>
    </div>
    
    <div
      v-else
      class="rating-container"
    >
      <div class="card">
        <div class="media-info">
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
            <h1 class="title">
              {{ title.name }}
            </h1>
            <p
              v-if="title.overview"
              class="overview"
            >
              {{ title.overview }}
            </p>
            <div class="meta">
              <span class="type-badge">{{ typeLabel }}</span>
              <span
                v-if="title.releaseDate"
                class="release-date"
              >
                {{ formatReleaseDate(title.releaseDate) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <h2>Deine Bewertung</h2>
        <RatingInput v-model:ratings="ratings" />
        
        <div class="actions">
          <button
            class="btn btn-secondary"
            @click="goBack"
          >
            Abbrechen
          </button>
          <button
            class="btn"
            :disabled="saving"
            @click="saveRating"
          >
            <span v-if="saving">
              <i class="fas fa-spinner fa-spin" /> Speichern...
            </span>
            <span v-else>Bewertung speichern</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import RatingInput from '../components/RatingInput.vue';
import { TmdbService } from '../services/TmdbService';


const router = useRouter();
const route = useRoute();

const title = ref({});
const loading = ref(true);
const error = ref(null);
const saving = ref(false);

const ratings = ref({
  storyRating: 5,
  characterRating: 5,
  actingRating: 5,
  visualAudioRating: 5,
  entertainmentRating: 5,
  overallRating: 5
});

const typeLabel = computed(() => {
  return route.params.type === 'movie' ? 'Film' : 'Serie';
});

const posterUrl = computed(() => {
  return title.value.posterPath 
    ? TmdbService.getPosterUrl(title.value.posterPath) 
    : null;
});

onMounted(async () => {
  const { type, id } = route.params;
  
  if (!type || !id) {
    error.value = 'Ungültige Anfrage. Typ oder ID fehlt.';
    loading.value = false;
    return;
  }

  try {
    // Rufe den Film direkt über seine ID ab, nicht über die Suche
    const mediaData = await TmdbService.getTitleById(type, id);
    
    if (!mediaData) {
      error.value = `${typeLabel.value} mit ID ${id} nicht gefunden.`;
    } else {
      // Transformiere die Daten in dein lokales Format
      title.value = {
        id: mediaData.id,
        name: type === 'movie' ? mediaData.title : mediaData.name,
        type: type,
        posterPath: mediaData.poster_path,
        overview: mediaData.overview,
        releaseDate: type === 'movie' ? mediaData.release_date : mediaData.first_air_date
      };
    }
  } catch (e) {
    console.error('Error fetching title:', e);
    error.value = `Fehler beim Laden: ${e.message}`;
  } finally {
    loading.value = false;
  }
});

const formatReleaseDate = (dateString) => {
  if (!dateString) return '';
  
  try {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('de-DE', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    }).format(date);
  } catch (e) {
    return dateString;
  }
};



const saveRating = async () => {
  saving.value = true;
  // Berechne den Gesamtwert direkt
  const overallRating =
    ratings.value.storyRating +
    ratings.value.characterRating +
    ratings.value.actingRating +
    ratings.value.visualAudioRating +
    ratings.value.entertainmentRating;
 
  try {
    // Debugging: Überprüfe die tatsächlichen Werte von title.value
    console.log("title.value:", title.value);
    console.log("Eigenschaften von title.value:", Object.keys(title.value || {}));
   
    // Bei Vue 3 Reactive-Objekten muss man manchmal toRaw verwenden
    const titleData = title.value || {};
   
    // Versuche alternative Wege, die ID zu finden
    const possibleIds = ['tmdbId', 'id', 'tmdb_id', 'tmdb'];
    let tmdbIdValue = null;
   
    for (const idKey of possibleIds) {
      if (titleData[idKey] !== undefined && titleData[idKey] !== null) {
        console.log(`ID gefunden unter Eigenschaft: ${idKey} = ${titleData[idKey]}`);
        tmdbIdValue = titleData[idKey];
        break;
      }
    }
   
    if (tmdbIdValue === null) {
      console.error("Keine ID-Eigenschaft gefunden. title.value Inhalt:", JSON.stringify(titleData));
      throw new Error("Die Film/Serien-ID (tmdbId) konnte nicht gefunden werden!");
    }
   
    const tmdbIdNumber = Number(tmdbIdValue);
    if (isNaN(tmdbIdNumber)) {
      console.error("tmdbId ist keine gültige Nummer:", tmdbIdValue);
      throw new Error("Die Film/Serien-ID (tmdbId) ist keine gültige Nummer!");
    }
   
    // Bestimme den Typ basierend auf der Route
    const contentType = route.params.type === 'movie' ? 'movie' : 'tv';
    
    // Erstelle das zu sendende Objekt
    const ratedTitle = {
      tmdbId: tmdbIdNumber,
      // Für Filme: title, für Serien: name (aber sende beide zur Sicherheit)
      name: titleData.name || titleData.title || "",
      title: titleData.title || titleData.name || "",
      type: contentType,
      posterPath: titleData.posterPath || titleData.poster_path || "",
      overview: titleData.overview || "",
      storyRating: ratings.value.storyRating,
      characterRating: ratings.value.characterRating,
      actingRating: ratings.value.actingRating,
      visualAudioRating: ratings.value.visualAudioRating,
      entertainmentRating: ratings.value.entertainmentRating,
      overallRating: overallRating
    };
   
    console.log("Sende Bewertung:", ratedTitle);
    console.log("tmdbId Typ:", typeof tmdbIdNumber, "Wert:", tmdbIdNumber);

    const endpoint = contentType === 'movie' ? '/api/movies/rate' : '/api/series/rate';
   
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(ratedTitle)
    });
   
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Fehlerantwort vom Server:", response.status, errorText);
      throw new Error(`HTTP error! status: ${response.status}, Fehler: ${errorText}`);
    }
   
    const savedTitle = await response.json();
    console.log("Gespeicherte Bewertung:", savedTitle);
    
    // Erfolgreiche Speicherung - zurück zur Liste
    router.push(route.params.type === 'movie' ? '/movies' : '/series');
    
  } catch (e) {
    console.error("Fehler beim Speichern:", e);
    alert(`Fehler beim Speichern der Bewertung: ${e.message}`);
  } finally {
    saving.value = false;
  }
};


const goBack = () => {
  router.back();
};
</script>

<style scoped>
.loading, .error {
  text-align: center;
  padding: 2rem;
}

.error {
  color: #721c24;
  background-color: #f8d7da;
  border-color: #f5c6cb;
}

.rating-container {
  max-width: 800px;
  margin: 0 auto;
}

.media-info {
  display: flex;
  gap: 1.5rem;
}

.poster {
  flex-shrink: 0;
  width: 180px;
  height: 270px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
}

.poster-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-poster {
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
  flex: 1;
}

.title {
  color: #003366;
  margin-bottom: 1rem;
}

.overview {
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.meta {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.type-badge {
  background-color: #003366;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 4px;
  font-size: 0.9rem;
}

.release-date {
  color: #666;
  font-style: italic;
}

.actions {
  margin-top: 2rem;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

@media (max-width: 600px) {
  .media-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .poster {
    margin-bottom: 1.5rem;
  }
  
  .meta {
    justify-content: center;
  }
}
</style>