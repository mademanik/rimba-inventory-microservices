<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>
          Items List
          <RouterLink
            to="/items/addItem"
            class="btn btn-primary float-end mx-2"
          >
            <i class="fas fa-plus"></i> Items
          </RouterLink>
          <!-- <RouterLink to="/items" class="btn btn-warning float-end">
            Refresh
          </RouterLink> -->
          <button
            type="button"
            class="btn btn-warning float-end"
            @click="refreshItems()"
          >
            Refresh
          </button>
        </h4>
      </div>
      <div class="card-body">
        <div v-if="message" :class="alertClasses" role="alert">
          {{ message }}
        </div>
        <div v-if="updateMessage" :class="alertClasses" role="alert">
          {{ updateMessage }}
        </div>
        <div v-if="deleteMessage" class="alert alert-success">
          {{ deleteMessage }}
        </div>
        <table class="table table-bordered" style="width: 100%">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nama Item</th>
              <th>Unit</th>
              <th>Stock</th>
              <th>Harga Satuan</th>
              <th>Barang</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody v-if="this.items.length > 0">
            <tr v-for="(item, index) in this.items" :key="index">
              <td>{{ item.id }}</td>
              <td>{{ item.namaItem }}</td>
              <td>{{ item.unit }}</td>
              <td>{{ item.stock }}</td>
              <td>{{ item.hargaSatuan }}</td>
              <td>
                <img
                  :src="getImageUrl(item.id, item.barang)"
                  alt=""
                  class="img-thumbnail"
                  style="width: 80px"
                />
              </td>
              <td>
                <div class="justify-content-between">
                  <!-- <RouterLink to="/editItem" class="btn btn-success"
                    >Edit</RouterLink
                  > -->
                  <button
                    type="button"
                    class="btn btn-success"
                    @click="editItems(item.id)"
                  >
                    Edit
                  </button>
                  <button
                    type="button"
                    class="btn btn-danger mx-2"
                    @click="deleteItems(item.id)"
                  >
                    Delete
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr>
              <td colspan="7">No Data Found...</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "items",
  data() {
    return {
      items: [],
      alertMessage: "",
      deleteMessage: "",
    };
  },
  mounted() {
    this.getItems();
  },
  computed: {
    message() {
      return this.$route.query.message;
    },
    updateMessage() {
      return this.$route.query.updateMessage;
    },
    alertClasses() {
      const isSuccess =
        this.$route.query.responseCode === "201" ||
        this.$route.query.responseCode === "200";

      return {
        alert: true,
        "alert-success": isSuccess,
        "alert-danger": !isSuccess,
      };
    },
  },
  methods: {
    getItems() {
      try {
        axios.get("http://localhost/api/item").then((res) => {
          this.items = res.data;
        });
      } catch (err) {
        console.log("getItems failed");
      }
    },
    deleteItems(id) {
      if (window.confirm("Are you sure ? ")) {
        try {
          axios.delete(`http://localhost/api/item/${id}`).then((res) => {
            console.log(res);
            this.deleteMessage = "Item deleted successfully!";
            this.getItems();
            setTimeout(() => {
              this.deleteMessage = "";
            }, 3000);
          });
        } catch (err) {
          console.log("delete item failed");
        }
      }
    },
    getImageUrl(id, barang) {
      return `http://localhost/api/item/barang/${id}/${barang}`;
    },
    editItems(id) {
      this.$router.push({
        name: "editItem",
        query: { itemId: id },
      });
    },
    refreshItems() {
      this.getItems();
      this.$router.push({
        name: "items",
      });
    },
  },
};
</script>
