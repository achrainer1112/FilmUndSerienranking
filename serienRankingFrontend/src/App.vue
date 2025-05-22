<template>
  <div class="app-container">
    <header class="header">
      <div class="logo">
        <router-link to="/">
          Movie Ranking
        </router-link>
      </div>
      <nav class="nav">
        <router-link to="/">
          Home
        </router-link>
        <router-link to="/movies">
          Filme
        </router-link>
        <router-link to="/series">
          Serien
        </router-link>
      </nav>
      <SearchBar />
    </header>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <p>&copy; {{ new Date().getFullYear() }} Movie Ranking App</p>
    </footer>
  </div>
</template>

<script setup>
import SearchBar from './components/SearchBar.vue';
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
  color: #333;
  line-height: 1.6;
}

.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  background-color: #003366;
  color: white;
  padding: 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo a {
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  text-decoration: none;
}

.nav {
  display: flex;
  gap: 1.5rem;
}

.nav a {
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.nav a:hover,
.nav a.router-link-active {
  color: #ffd700;
}

.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
}

.footer {
  background-color: #003366;
  color: white;
  text-align: center;
  padding: 1rem;
  margin-top: auto;
}

.section-title {
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
  color: #003366;
}

.card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.btn {
  display: inline-block;
  padding: 0.5rem 1rem;
  background-color: #003366;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.btn:hover {
  background-color: #004d99;
}

.btn-secondary {
  background-color: #6c757d;
}

.btn-secondary:hover {
  background-color: #5a6268;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 1rem;
  }
  
  .nav {
    margin: 0.5rem 0;
  }
}
</style>