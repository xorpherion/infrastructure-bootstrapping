<template>
  <div id="app">
    <div class="container">
      <h1>Infrastructure Bootstrapping</h1>
      <router-link to="/" class="col-auto"><a>Home</a></router-link>
      <router-link to="/start" class="col-auto"><a>Wizard</a></router-link>
      <router-link to="/manage" class="col-auto"><a>Manage objects</a></router-link>
      <hr/>    
      <router-view/>
    </div>    
  </div>
</template>

