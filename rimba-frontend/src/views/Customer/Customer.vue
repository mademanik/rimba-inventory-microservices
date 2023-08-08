<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h5>
          Customers
          <RouterLink
            to="/customers/addCustomer"
            class="btn btn-primary float-end mx-2"
          >
            <i class="fas fa-plus"></i> Customer
          </RouterLink>
          <!-- <RouterLink to="/customers" class="btn btn-warning float-end">
            Refresh
          </RouterLink> -->
          <button
            type="button"
            class="btn btn-warning float-end"
            @click="refreshCustomers()"
          >
            Refresh
          </button>
        </h5>
      </div>
      <div class="card-body">
        <div v-if="messageCustomer" :class="alertCustomer" role="alert">
          {{ messageCustomer }}
        </div>
        <div v-if="deleteMessage" class="alert alert-success">
          {{ deleteMessage }}
        </div>

        <table class="table table-bordered">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Contact</th>
              <th>Email</th>
              <th>Alamat</th>
              <th>Diskon</th>
              <th>Tipe Diskon</th>
              <th>KTP</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody v-if="this.customers.length > 0">
            <tr v-for="(customer, index) in this.customers" :key="index">
              <td>{{ customer.id }}</td>
              <td>{{ customer.name }}</td>
              <td>{{ customer.contact }}</td>
              <td>{{ customer.email }}</td>
              <td>{{ customer.alamat }}</td>
              <td>{{ customer.diskon }}</td>
              <td>{{ customer.tipeDiskon }}</td>
              <td>
                <img
                  :src="getImageUrl(customer.id, customer.ktp)"
                  alt=""
                  class="img-thumbnail"
                  style="width: 80px"
                />
              </td>
              <td>
                <RouterLink to="/editCustomer" class="btn btn-success"
                  >Edit</RouterLink
                >
                <button
                  type="button"
                  class="btn btn-danger mx-2"
                  @click="deleteCustomer(customer.id)"
                >
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr>
              <td colspan="9">No Data Found...</td>
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
  name: "customers",
  data() {
    return {
      customers: [],
      alertMessage: "",
      deleteMessage: "",
    };
  },
  mounted() {
    this.getCustomers();
  },
  computed: {
    messageCustomer() {
      return this.$route.query.message;
    },
    alertCustomer() {
      const isSuccess = this.$route.query.responseCode === "201";

      return {
        alert: true,
        "alert-success": isSuccess,
        "alert-danger": !isSuccess,
      };
    },
  },
  methods: {
    getCustomers() {
      try {
        axios.get("http://localhost/api/customer").then((res) => {
          this.customers = res.data;
        });
      } catch (err) {
        console.log("getCustomers failed");
      }
    },
    deleteCustomer(id) {
      if (window.confirm("Are you sure ? ")) {
        try {
          axios.delete(`http://localhost/api/customer/${id}`).then((res) => {
            console.log(res);
            this.deleteMessage = "Customer deleted successfully!";
            this.getCustomers();
            setTimeout(() => {
              this.deleteMessage = "";
            }, 3000);
          });
        } catch (err) {
          console.log("delete customer failed");
        }
      }
    },
    getImageUrl(id, barang) {
      return `http://localhost/api/customer/ktp/${id}/${barang}`;
    },
    refreshCustomers() {
      this.getCustomers();
      this.$router.push({
        name: "customers",
      });
    },
  },
};
</script>
