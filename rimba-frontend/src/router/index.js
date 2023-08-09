import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/Home.vue";
import CustomerView from "../views/Customer/Customer.vue";
import AddCustomerView from "../views/Customer/AddCustomer/AddCustomer.vue";
import EditCustomerView from "../views/Customer/EditCustomer/EditCustomer.vue";

import ItemView from "../views/Item/Item.vue";
import AddItemView from "../views/Item/AddItem/AddItem.vue";
import EditItemView from "../views/Item/EditItem/EditItem.vue";

import SalesView from "../views/Sales/Sales.vue";
import AddSalesView from "../views/Sales/AddSales/AddSales.vue";
import ViewSalesView from "../views/Sales/ViewSales/ViewSales.vue";

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
      path: "/customers/editCustomer",
      name: "editCustomer",
      component: EditCustomerView,
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
      path: "/items/editItem",
      name: "editItem",
      component: EditItemView,
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
    {
      path: "/sales/viewSales",
      name: "viewSales",
      component: ViewSalesView,
    },
  ],
});

export default router;
