import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Particles from 'vue3-particles'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

createApp(App).use(store).use(router).use(Particles).use(ElementPlus).mount('#app')
