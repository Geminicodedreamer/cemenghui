import { createStore } from 'vuex';
import ModuleUser from './user';

export default createStore({
    modules: {
        user: {
            namespaced: true,
            ...ModuleUser,
        },
    },
});
