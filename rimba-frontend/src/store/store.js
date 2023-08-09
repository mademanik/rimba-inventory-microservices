// store.js
import { createStore } from "vuex";

const store = createStore({
  state: {
    tglTransaksi: "dd/mm/yyyy",
    customerId: "",
    selectedItems: [],
  },
  mutations: {
    updateInitInquiry(state, { tglTransaksi, customerId }) {
      state.tglTransaksi = tglTransaksi;
      state.customerId = customerId;
      console.log(state);
    },
    addItem(state, item) {
      item.qty = "1";
      
      const existingItem = state.selectedItems.find((i) => i.id === item.id);

      if (!existingItem) {
        state.selectedItems.push(item);
      } else {
        alert("Item sudah ditambahkan");
      }

      // state.selectedItems.push(item);
    },
    clearState(state) {
      console.log(state.selectedItems);
      state.selectedItems = [];
    },
    deleteItem(state, itemId) {
      state.selectedItems = state.selectedItems.filter(
        (item) => item.id !== itemId
      );
    },
    updateQuantity(state, { itemId, qty }) {
      const item = state.selectedItems.find((p) => p.id === itemId);
      if (item) {
        item.qty = qty;
      }
    },
  },
});

export default store;
