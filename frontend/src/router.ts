import Vue from 'vue';
import Router from 'vue-router';
import Start from './views/Start.vue';
import NewInfrastructure from './views/NewInfrastructure.vue';
import ManageObjects from './views/ManageObjects.vue';
import Home from './views/Home.vue';

Vue.use(Router);

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/new',
      name: 'newInfrastructure',
      component: NewInfrastructure,
    },
    {
      path: '/manage',
      name: 'manageObjects',
      component: ManageObjects,
    },
    {
      path: '/start',
      name: 'start',
      component: Start,
    },
    {
      path: '**',
      name: 'home',
      component: Home,
    },
  ],
});
