import { createRouter, createWebHistory } from 'vue-router'
import CustomerIndexView from '../views/Customer/CustomerIndexView.vue'
import IndustryIndexView from '../views/Industry/IndustryIndexView.vue'
import LessonIndexView from '../views/Lesson/LessonIndexView'
import MeetingIndexView from '../views/Meeting/MeetingIndexView'
import OrganizationIndexView from '../views/Organization/OrganizationIndexView'
import NotFound from '../views/error/NotFound'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
import HomeView from '../views/Home/HomeView'
import store from '../store/index'

const routes = [
  {
    path: "/home/",
    name: "home",
    component: HomeView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/customer/",
    name: "customer",
    component: CustomerIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/industry/",
    name: "industry",
    component: IndustryIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/lesson/",
    name: "lesson",
    component: LessonIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/meeting/",
    name: "meeting",
    component: MeetingIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/organization/",
    name: "organization",
    component: OrganizationIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({name: "user_account_login"});
  } else {
    next();
  }
})

export default router
