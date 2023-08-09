<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col col-lg-6">
        <div class="card">
          <div class="card-header">
            <h4>Edit Item</h4>
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
                  v-model="item.namaItem"
                  required
                />
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span
                  >Unit</label
                >
                <select class="form-select" v-model="item.unit" required>
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
                  v-model="item.stock"
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
                  v-model="item.hargaSatuan"
                  required
                />
              </div>
              <div class="mb-3">
                <label for="formFile" class="form-label">Barang (Image)</label>
                <input
                  class="form-control"
                  type="file"
                  id="formFile"
                  ref="imageInput"
                  @change="handleImageUpload"
                  accept="image/*"
                />
                <span class="text-primary">*optional</span>
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
  name: "editItems",
  data() {
    return {
      item: {},
      selectedFile: null,
      previewImage: null,
    };
  },
  mounted() {
    this.getItemById(this.$route.query.itemId);
  },
  methods: {
    handleImageUpload(event) {
      this.item.barang = event.target.files[0];
      this.selectedFile = event.target.files[0];
      this.previewImage = URL.createObjectURL(this.selectedFile);
    },
    getItemById(id) {
      try {
        axios.get(`http://localhost/api/item/${id}`).then((res) => {
          this.item = res.data;
        });
      } catch (err) {
        console.log("getItemById failed");
      }
    },
    getImageUrl(id, barang) {
      return `http://localhost/api/item/barang/${id}/${barang}`;
    },
    async submitForm() {
      const jsonItems = {
        namaItem: this.item.namaItem,
        unit: this.item.unit,
        stock: this.item.stock,
        hargaSatuan: this.item.hargaSatuan,
      };

      const formData = new FormData();
      formData.append("item", JSON.stringify(jsonItems));

      if (this.selectedFile) {
        formData.append("barang", this.item.barang);
      }

      try {
        const response = await axios.put(
          `http://localhost/api/item/${this.item.id}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        if (response.status === 200) {
          const alertMessage = "Item updated successfully!";
          this.$router.push({
            name: "items",
            query: {
              updateMessage: alertMessage,
              responseCode: response.status,
            },
          });
          console.log("Item updated successfully");
        } else {
          const alertMessage = "Failed to updated item!";
          this.$router.push({
            name: "items",
            query: {
              updateMessage: alertMessage,
              responseCode: response.status,
            },
          });
          console.error("Failed to updated item");
        }
      } catch (error) {
        console.error("Error:", error);
      }
    },
  },
};
</script>
