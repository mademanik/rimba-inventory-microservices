<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col col-lg-6">
        <div class="card">
          <div class="card-header">
            <h4>Inquiry</h4>
          </div>
          <div class="card-body">
            <form @submit.prevent="submitInquiry">
              <button @click="pilihItem" class="btn btn-secondary my-3">
                Pilih Item
              </button>
              <button @click="clearState" class="btn btn-warning my-3 mx-2">
                Reset
              </button>
              <table class="table table-bordered mb-5">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nama</th>
                    <th>Jumlah</th>
                    <th>Aksi</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in selectedItems" :key="item.id">
                    <td>{{ item.id }}</td>
                    <td>{{ item.namaItem }}</td>
                    <input
                      class="form-control"
                      type="number"
                      v-model="item.qty"
                      @input="onQuantityChange(item)"
                    />
                    <td>
                      <button
                        @click="deleteItem(item.id)"
                        class="btn btn-danger btn-sm"
                      >
                        <i class="fas fa-trash-alt"></i>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>

              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span> Tgl
                  Transaksi</label
                >
                <input
                  type="date"
                  class="form-control"
                  id="tglTransaksi"
                  v-model="formData.tglTransaksi"
                />
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span> Customer
                  Name</label
                >
                <select class="form-select" v-model="formData.customerId">
                  <option value="" :selected="!formData.customerId">
                    --Pilih Customers--
                  </option>
                  <option
                    v-for="customer in customers"
                    :key="customer.id"
                    :value="customer.id"
                  >
                    {{ customer.id }} - {{ customer.name }} - (tipe diskon:
                    {{ customer.tipeDiskon }}) - (diskon: {{ customer.diskon }})
                  </option>
                </select>
              </div>

              <button type="submit" class="btn btn-primary float-end mx-2 mt-4">
                Inquiry
              </button>
              <RouterLink to="/sales" class="btn btn-danger float-end mt-4">
                Batal
              </RouterLink>
            </form>
          </div>
        </div>
      </div>
      <div class="col col-lg-6">
        <div class="card">
          <div class="card-header">
            <h4>Payment</h4>
          </div>
          <div class="card-body"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "addSales",
  data() {
    return {
      formData: {
        tglTransaksi: "",
        customerId: "",
        itemSales: [],
        customers: [],
      },
    };
  },
  computed: {
    selectedItems() {
      return this.$store.state.selectedItems;
    },
    tglTransaksi() {
      return this.$store.state.tglTransaksi;
    },
    customerId() {
      return this.$store.state.customerId;
    },
  },
  mounted() {
    this.fetchCustomers();
  },
  methods: {
    async fetchCustomers() {
      try {
        const response = await axios.get("http://localhost/api/customer");
        this.customers = response.data;
        console.log(this.customers);
      } catch (error) {
        console.error("Error fetching customer data:", error);
      }
    },
    pilihItem() {
      this.$store.commit("updateInitInquiry", {
        tglTransaksi: this.formData.tglTransaksi,
        customerId: this.formData.customerId,
      });
      this.$router.push("/");
    },
    clearState() {
      this.$store.commit("clearState");
    },
    deleteItem(itemId) {
      this.$store.commit("deleteItem", itemId);
    },
    onQuantityChange(item) {
      item.qty = parseInt(item.qty);
      this.$store.commit("updateQuantity", {
        itemId: item.id,
        qty: item.qty,
      });
    },
    async submitInquiry() {
      const formData = new FormData();

      const itemSales = this.selectedItems.map((item) => {
        return {
          itemId: item.id,
          qty: item.qty,
        };
      });

      this.formData.itemSales = itemSales;

      const jsonItems = {
        tglTransaksi: this.formData.tglTransaksi,
        customerId: this.formData.customerId,
        itemSales: this.formData.itemSales,
      };

      formData.append("sales", JSON.stringify(jsonItems));

      try {
        const response = await axios.post(
          "http://localhost/api/sales/inquiry",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        console.log(response);
      } catch (error) {
        console.error("Error:", error);
      }
    },
  },
};
</script>
