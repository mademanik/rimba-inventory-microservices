<template>
  <main>
    <div class="container py-5">
      <h2 class="text-center">Rimba Inventory Penjualan</h2>
      <div
        class="row row-cols-1 row-cols-md-4 g-4 py-5"
        v-if="this.items.length > 0"
      >
        <div class="col" v-for="(item, index) in this.items" :key="index">
          <div class="card">
            <img
              :src="getImageUrl(item.id, item.barang)"
              class="card-img-top"
              alt="..."
            />
            <div class="card-body">
              <h5 class="card-title">{{ item.namaItem }}</h5>
            </div>
            <div class="mb-5 d-flex justify-content-around">
              <h5>Rp. {{ item.hargaSatuan }}</h5>
              <button class="btn btn-primary" @click="buyNow(item)">
                Buy Now
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<style>
body {
  background-color: #f9f6f2;
}
.card-img-top {
  border-radius: 50px;
  padding: 20px;
}
.card {
  border-radius: 15px;
  overflow: hidden;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;
}
.card-body {
  padding: 25px;
  margin-top: -15px;
}
.btn-primary {
  width: 120px;
}
.btn-primary:hover {
  background-color: black;
  border: none;
}
h3,
h5 {
  color: rgb(0, 91, 228);
}
</style>

<script>
import axios from "axios";

export default {
  name: "home",
  data() {
    return {
      items: [],
    };
  },
  mounted() {
    this.getItems();
  },
  methods: {
    getItems() {
      try {
        axios.get("http://localhost/api/item").then((res) => {
          this.items = res.data;
          this.items = this.items.map((obj) => ({
            ...obj,
            qty: 0,
          }));
        });
      } catch (err) {
        console.log("getItems failed");
      }
    },
    getImageUrl(id, barang) {
      return `http://localhost/api/item/barang/${id}/${barang}`;
    },
    buyNow(item) {
      this.$store.commit("addItem", item);
      this.$router.push("/sales/addSales");
    },
  },
};
</script>
