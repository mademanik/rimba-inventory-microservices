import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/Home.vue";
import CustomerView from "../views/Customer/Customer.vue";
import ItemView from "../views/Item/Item.vue";
import SalesView from "../views/Sales/Sales.vue";
import AddItemView from "../views/Item/AddItem/AddItem.vue";
import AddCustomerView from "../views/Customer/AddCustomer/AddCustomer.vue";
import AddSalesView from "../views/Sales/AddSales/AddSales.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/customers",
      name: "customers",
      component: CustomerView,
    },
    {
      path: "/customers/addCustomer",
      name: "addCustomer",
      component: AddCustomerView,
    },
    {
      path: "/items",
      name: "items",
      component: ItemView,
    },
    {
      path: "/items/addItem",
      name: "addItem",
      component: AddItemView,
    },
    {
      path: "/sales",
      name: "sales",
      component: SalesView,
    },
    {
      path: "/sales/addSales",
      name: "addSales",
      component: AddSalesView,
    },
  ],
});

export default router;
