import { createRouter, createWebHistory } from 'vue-router';
import HomeView from './views/HomeView.vue';
import MovieList from './views/MovieList.vue';
import SeriesList from './views/SeriesList.vue';
import RatingView from './views/RatingView.vue';
import SearchResults from './views/SearchResults.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/movies',
    name: 'movies',
    component: MovieList
  },
  {
    path: '/series',
    name: 'series',
    component: SeriesList
  },
  {
    path: '/rate/:type/:id',
    name: 'rate',
    component: RatingView,
    props: true
  },
  {
    path: '/search',
    name: 'search',
    component: SearchResults,
    props: route => ({ query: route.query.q })
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;