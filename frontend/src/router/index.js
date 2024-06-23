import { createRouter, createWebHashHistory } from 'vue-router'
import CustomerIndexView from '../views/Customer/CustomerIndexView.vue'
import IndustryIndexView from '../views/Industry/IndustryIndexView.vue'
import LessonIndexView from '../views/Lesson/LessonIndexView'
import MeetingIndexView from '../views/Meeting/MeetingIndexView'
import OrganizationIndexView from '../views/Organization/OrganizationIndexView'
import NotFound from '../views/error/NotFound'
import CompanyAccountLoginView from '../views/company/account/CompanyAccountLoginView'
import CompanyAccountRegisterView from '../views/company/account/CompanyAccountRegisterView'
import CustomerAccountLoginView from '../views/Customer/account/CustomerAccountLoginView.vue'
import LessonAdd from'../views/Lesson/LessonAdd'
import LessonModify from'../views/Lesson/LessonModify'
import HomeView from '../views/Home/HomeView'
import store from '../store/index'
import CompanyListView from '../views/company/list/CompanyListView.vue'
// import AddMeeting from '../views/Meeting/AddMeeting'

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/lesson/lessonAdd/",
    name: "lessonAdd",
    component: LessonAdd,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/lesson/lessonModify/",
    name: "lessonModify",
    component: LessonModify,
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
    path: "/company/list/",
    name: "company",
    component: CompanyListView,
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
    path: "/company/account/login/",
    name: "company_account_login",
    component: CompanyAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/company/account/register/",
    name: "company_account_register",
    component: CompanyAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: CustomerAccountLoginView,
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
  },
  // {
  //   path: "/meeting/addMeeting/",
  //   name: "addMeeting",
  //   component: AddMeeting,
  //   meta: {
  //     requestAuth: true,
  //   }
  // }
]

const router = createRouter({
  mode:'history',
  history: createWebHashHistory(process.env.BASE_URL),  
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
