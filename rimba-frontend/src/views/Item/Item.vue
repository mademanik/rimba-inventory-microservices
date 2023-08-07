<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>
          Items List
          <RouterLink to="/items/addItem" class="btn btn-primary float-end">
            Add Items
          </RouterLink>
        </h4>
      </div>
      <div class="card-body">
        <div v-if="message" :class="alertClasses" role="alert">
          {{ message }}
        </div>
        <div v-if="deleteMessage" class="alert alert-success">
          {{ deleteMessage }}
        </div>
        <table class="table table-bordered">
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
              <td>{{ item.barang }}</td>
              <td>
                <div class="justify-content-between">
                  <RouterLink to="/edit" class="btn btn-success"
                    >Edit</RouterLink
                  >
                  <button
                    type="button"
                    class="btn btn-danger"
                    style="margin-left: 10px"
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
    alertClasses() {
      const isSuccess = this.$route.query.responseCode === "201";

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
            }, 3000); // Clear the success message after 3 seconds
          });
        } catch (err) {
          console.log("delete item failed");
        }
      }
    },
  },
};
</script>
