<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col col-lg-6">
        <div class="card">
          <div class="card-header">
            <h4>Add Item</h4>
          </div>
          <div class="card-body">
            <form @submit.prevent="submitForm">
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span> Nama
                  Item</label
                >
                <input
                  type="text"
                  class="form-control"
                  id="namaItem"
                  name="namaItem"
                  v-model="formData.namaItem"
                  required
                />
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span
                  >Unit</label
                >
                <select class="form-select" v-model="formData.unit" required>
                  <option value="" selected>--Pilih Unit--</option>
                  <option value="kg">Kg</option>
                  <option value="pcs">Pcs</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span
                  >Stock</label
                >
                <input
                  type="text"
                  class="form-control"
                  id="stock"
                  name="stock"
                  v-model="formData.stock"
                  required
                />
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span
                  >Harga Satuan</label
                >
                <input
                  type="text"
                  class="form-control"
                  id="hargaSatuan"
                  name="hargaSatuan"
                  v-model="formData.hargaSatuan"
                  required
                />
              </div>
              <div class="mb-3">
                <label for="formFile" class="form-label"
                  ><span class="text-danger"><strong>*</strong></span
                  >Barang (Image)</label
                >
                <input
                  class="form-control"
                  type="file"
                  id="formFile"
                  ref="imageInput"
                  @change="handleImageUpload"
                  accept="image/*"
                />
              </div>
              <div class="mb-3">
                <div v-if="previewImage">
                  <img
                    :src="previewImage"
                    style="width: 30%; height: 30%"
                    alt="Preview"
                  />
                </div>
              </div>
              <button type="submit" class="btn btn-primary float-end mx-2">
                Submit
              </button>
              <RouterLink to="/items" class="btn btn-danger float-end">
                Batal
              </RouterLink>
            </form>
          </div>
        </div>
      </div>
      <div class="col col-lg-auto"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "addItems",
  data() {
    return {
      formData: {
        namaItem: "",
        unit: "",
        stock: "",
        hargaSatuan: "",
        barang: null,
      },
      selectedFile: null,
      previewImage: null,
    };
  },
  methods: {
    handleImageUpload(event) {
      this.formData.barang = event.target.files[0];
      this.selectedFile = event.target.files[0];
      this.previewImage = URL.createObjectURL(this.selectedFile);
    },
    async submitForm() {
      const jsonItems = {
        namaItem: this.formData.namaItem,
        unit: this.formData.unit,
        stock: this.formData.stock,
        hargaSatuan: this.formData.hargaSatuan,
      };

      const formData = new FormData();
      formData.append("item", JSON.stringify(jsonItems));
      formData.append("barang", this.formData.barang);

      try {
        const response = await axios.post(
          "http://localhost/api/item",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        if (response.status === 201) {
          const alertMessage = "Item added successfully!";
          this.$router.push({
            name: "items",
            query: { message: alertMessage, responseCode: response.status },
          });
          console.log("Item added successfully");
        } else {
          const alertMessage = "Failed to add item!";
          this.$router.push({
            name: "items",
            query: { message: alertMessage, responseCode: response.status },
          });
          console.error("Failed to add item");
        }
      } catch (error) {
        console.error("Error:", error);
      }
    },
  },
};
</script>
