<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col col-lg-6">
        <div class="card">
          <div class="card-header">
            <h4 style="display: inline">Sales Detail</h4>
            <RouterLink to="/sales" class="btn btn-danger float-end">
              Kembali
            </RouterLink>
          </div>
          <div class="card-body">
            <div class="mb-3">
              <label class="form-label">Kode Transaksi</label>
              <input
                class="form-control"
                id="kodeTransaksi"
                v-model="sales.kodeTransaksi"
                disabled
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Tgl Transaksi</label>
              <input
                class="form-control"
                id="tglTransaksi"
                v-model="sales.tglTransaksi"
                disabled
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Total Diskon</label>
              <input
                class="form-control"
                id="totalDiskon"
                v-model="sales.totalDiskon"
                disabled
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Total Harga</label>
              <input
                class="form-control"
                id="totalHarga"
                v-model="sales.totalHarga"
                disabled
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Total Bayar</label>
              <input
                class="form-control"
                id="totalBayar"
                v-model="sales.totalBayar"
                disabled
              />
            </div>
          </div>
        </div>
      </div>
      <div class="col col-lg-6">
        <div class="card">
          <div class="card-header">
            <h4>Item Detail</h4>
          </div>
          <div class="card-body">
            <table class="table table-bordered">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nama</th>
                  <th>Harga satuan</th>
                  <th>Qty</th>
                  <th>Unit</th>
                  <th>Harga Satuan</th>
                  <th>Barang</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in sales.item" :key="item.id">
                  <td>{{ item.id }}</td>
                  <td>{{ item.namaItem }}</td>
                  <td>{{ item.hargaSatuan }}</td>
                  <td>{{ item.stock }}</td>
                  <td>{{ item.unit }}</td>
                  <td>{{ item.hargaSatuan }}</td>
                  <td>
                    <img
                      :src="getImageUrl(item.id, item.barang)"
                      alt=""
                      class="img-thumbnail"
                      style="width: 60px"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "viewSales",
  data() {
    return {
      sales: {},
    };
  },
  mounted() {
    this.getSalesById(this.$route.query.salesId);
  },
  methods: {
    getSalesById(id) {
      try {
        axios.get(`http://localhost/api/sales/${id}`).then((res) => {
          this.sales = res.data;
          console.log(this.sales);
        });
      } catch (err) {
        console.log("getSalesById failed");
      }
    },
    getImageUrl(id, barang) {
      return `http://localhost/api/item/barang/${id}/${barang}`;
    },
  },
};
</script>
