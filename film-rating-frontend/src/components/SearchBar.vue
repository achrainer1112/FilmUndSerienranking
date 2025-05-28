<template>
  <div class="search-bar">
    <form @submit.prevent="submitSearch">
      <!-- Suchfeld mit Zwei-Wege-Bindung -->
      <input 
        v-model="searchQuery" 
        type="text" 
        placeholder="Film oder Serie suchen..." 
        class="search-input"
      >
      <!-- Button mit Such-Icon -->
      <button
        type="submit"
        class="search-button"
      >
        <i class="fas fa-search" />
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter(); // Zugriff auf den Router
const searchQuery = ref(''); // Suchtext als reaktiver Zustand

// Wird beim Absenden des Formulars ausgeführt
const submitSearch = () => {
  if (searchQuery.value.trim()) {
    // Navigiert zur Suchseite mit Query-Parameter
    router.push({ 
      name: 'search', 
      query: { q: searchQuery.value.trim() } 
    });
    searchQuery.value = ''; // Leert das Suchfeld
  }
};
</script>

<style scoped>
.search-bar {
  display: flex;
  max-width: 400px;
  width: 100%;
}

.search-bar form {
  display: flex;
  width: 100%;
}

.search-input {
  flex: 1;
  padding: 0.5rem;
  border: none;
  border-radius: 4px 0 0 4px;
  font-size: 1rem;
}

.search-button {
  background-color: #ffd700;
  color: #003366;
  border: none;
  border-radius: 0 4px 4px 0;
  padding: 0 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #ffcc00;
}

@media (max-width: 768px) {
  .search-bar {
    max-width: 100%;
  }
}
</style>
