// 导入 Vue 和 VueRouter
import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router'

// 导入你的组件
import PageOne from '../components/PageOne.vue'
import PageTwo from '../components/PageTwo.vue'
import PageThree from '../components/PageThree.vue'
import PageFour from '../components/PageFour.vue'
import NumQuery from '../components/NumQuery.vue'

// 定义路由
const routes = [
  { path: '/first', component: PageOne },
  { path: '/second', component: PageTwo },
  { path: '/third', component: PageThree },
  { path: '/fourth', component: PageFour },
  { path: '/query', component: NumQuery },
]

// 创建 router 实例
const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

// 创建 Vue 应用
const app = createApp({})

// 使用 router
app.use(router)

// 挂载应用
app.mount('#app')

export default router
