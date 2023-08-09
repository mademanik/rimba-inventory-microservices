<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>
          Sales
          <RouterLink
            to="/sales/addSales"
            class="btn btn-primary float-end mx-2"
          >
            <i class="fas fa-plus"></i> Sales
          </RouterLink>
          <!-- <RouterLink to="/sales" class="btn btn-warning float-end">
            Refresh
          </RouterLink> -->
          <button
            type="button"
            class="btn btn-warning float-end"
            @click="refreshSales()"
          >
            Refresh
          </button>
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
              <th>Kode Transaksi</th>
              <th>Tgl Transaksi</th>
              <th>Cust. Name</th>
              <th>Total Diskon</th>
              <th>Total Harga</th>
              <th>Total Bayar</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody v-if="this.sales.length > 0">
            <tr v-for="(sale, index) in this.sales" :key="index">
              <td>
                <small> {{ sale.kodeTransaksi }}</small>
              </td>
              <td>{{ sale.tglTransaksi }}</td>
              <td>{{ sale.customer.name }}</td>
              <td>{{ sale.totalDiskon }}</td>
              <td>{{ sale.totalHarga }}</td>
              <td>{{ sale.totalBayar }}</td>
              <td>
                <button
                  type="button"
                  class="btn btn-success"
                  @click="viewSales(sale.id)"
                >
                  Detail
                </button>
                <button
                  type="button"
                  class="btn btn-danger mx-2"
                  @click="deleteSales(sale.id)"
                >
                  Delete
                </button>
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
  name: "sales",
  data() {
    return {
      sales: [],
      alertMessage: "",
      deleteMessage: "",
    };
  },
  mounted() {
    this.getSales();
  },
  computed: {
    message() {
      return this.$route.query.message;
    },
    alertClasses() {
      const isSuccess = this.$route.query.responseCode === "200";

      return {
        alert: true,
        "alert-success": isSuccess,
        "alert-danger": !isSuccess,
      };
    },
  },
  methods: {
    getSales() {
      try {
        axios.get("http://localhost/api/sales").then((res) => {
          this.sales = res.data;
        });
      } catch (err) {
        console.log("getSales failed");
      }
    },
    deleteSales(id) {
      if (window.confirm("Are you sure ? ")) {
        try {
          axios.delete(`http://localhost/api/sales/${id}`).then((res) => {
            console.log(res);
            this.deleteMessage = "Sales deleted successfully!";
            this.getSales();
            setTimeout(() => {
              this.deleteMessage = "";
            }, 3000);
          });
        } catch (err) {
          console.log("delete sales failed");
        }
      }
    },
    refreshSales() {
      this.getSales();
      this.$router.push({
        name: "sales",
      });
    },
    viewSales(id) {
      this.$router.push({
        name: "viewSales",
        query: { salesId: id },
      });
    },
  },
};
</script>
