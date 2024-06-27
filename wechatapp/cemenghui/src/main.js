import { createSSRApp } from 'vue';
import App from './App.vue';
import store from './store';

export function createApp() {
	const app = createSSRApp(App);
	app.use(store);
	app.config.globalProperties.$store = store;
	return {
		app,
	};
}
