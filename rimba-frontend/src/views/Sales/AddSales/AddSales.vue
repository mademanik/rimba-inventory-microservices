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
              <table class="table table-bordered">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nama</th>
                    <th>Harga satuan</th>
                    <th>Qty</th>
                    <th>Aksi</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in selectedItems" :key="item.id">
                    <td>{{ item.id }}</td>
                    <td>{{ item.namaItem }}</td>
                    <td>{{ item.hargaSatuan }}</td>
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
              <p class="mb-5 text-danger">
                <span class="text-primary">*</span>Mohon mengisi quantity item
              </p>

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
                  ><span class="text-danger"><strong>*</strong></span> Nama
                  Customer
                </label>
                <select class="form-select" v-model="formData.customerId">
                  <option value="" :selected="!formData.customerId">
                    --Pilih Customers--
                  </option>
                  <option
                    v-for="customer in customers"
                    :key="customer.id"
                    :value="customer.id"
                  >
                    {{ customer.name }} (tipe diskon: {{ customer.tipeDiskon }},
                    diskon: {{ customer.diskon }})
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
          <div class="card-body">
            <form @submit.prevent="submitPayment">
              <div class="mb-3">
                <label class="form-label">Tgl Transaksi</label>
                <input
                  type="text"
                  class="form-control"
                  id="tglTransaksi"
                  v-model="formData.tglTransaksiInquiry"
                  disabled
                />
              </div>
              <div class="mb-3">
                <label class="form-label">Nama Customer</label>
                <input
                  type="text"
                  class="form-control"
                  id="tglTransaksi"
                  v-model="formData.customerNameInquiry"
                  disabled
                />
              </div>
              <div class="mb-3">
                <label class="form-label">Total Diskon</label>
                <input
                  type="text"
                  class="form-control"
                  id="totalDiskon"
                  v-model="formData.totalDiskon"
                  disabled
                />
              </div>
              <div class="mb-3">
                <label class="form-label">Total Harga</label>
                <input
                  type="text"
                  class="form-control"
                  id="totalHarga"
                  v-model="formData.totalHarga"
                  disabled
                />
              </div>
              <div class="mb-3">
                <label class="form-label">Total Bayar</label>
                <input
                  type="text"
                  class="form-control"
                  id="totalBayar"
                  v-model="formData.totalBayar"
                  disabled
                />
              </div>

              <button type="submit" class="btn btn-success float-end mx-2 mt-4">
                Payment
              </button>
            </form>
          </div>
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
        tglTransaksiInquiry: "",
        customerNameInquiry: "",
        customerIdInquiry: "",
        totalDiskon: 0,
        totalHarga: 0,
        totalBayar: 0,
      },
      // selectedItems: [],
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

        this.formData.tglTransaksiInquiry = response.data.tglTransaksi;
        this.formData.customerNameInquiry = response.data.customer.name;
        this.formData.customerIdInquiry = response.data.customer.id;
        this.formData.totalDiskon = response.data.totalDiskon;
        this.formData.totalHarga = response.data.totalHarga;
        this.formData.totalBayar = response.data.totalBayar;
      } catch (error) {
        console.error("Error:", error);
      }
    },

    async submitPayment() {
      if (window.confirm("Are you sure ? ")) {
        const formData = new FormData();

        const itemSales = this.selectedItems.map((item) => {
          return {
            itemId: item.id,
            qty: item.qty,
          };
        });

        this.formData.itemSales = itemSales;

        const jsonItems = {
          tglTransaksi: this.formData.tglTransaksiInquiry,
          customerId: this.formData.customerIdInquiry,
          itemSales: this.formData.itemSales,
        };

        formData.append("sales", JSON.stringify(jsonItems));

        try {
          const response = await axios.post(
            "http://localhost/api/sales/",
            formData,
            {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            }
          );

          console.log(response);

          if (response.status === 200) {
            const alertMessage = "New Sales added successfully!";
            this.$router.push({
              name: "sales",
              query: { message: alertMessage, responseCode: response.status },
            });
            console.log(alertMessage);
          } else {
            const alertMessage = "Failed to add New Sales!";
            this.$router.push({
              name: "sales",
              query: { message: alertMessage, responseCode: response.status },
            });
            console.log(alertMessage);
          }
        } catch (error) {
          console.error("Error:", error);
        }
      }
    },
  },
};
</script>
