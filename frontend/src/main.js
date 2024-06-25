import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Particles from 'vue3-particles'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import VueQuillEditor from 'vue-quill-editor/src'

import 'quill/dist/quill.core.css' // 引入样式
import 'quill/dist/quill.snow.css' // snow theme
import 'quill/dist/quill.bubble.css' // bubble theme

createApp(App)
  .use(store)
  .use(router)
  .use(Particles)
  .use(ElementPlus)
  .use(VueQuillEditor)
  .mount('#app')
