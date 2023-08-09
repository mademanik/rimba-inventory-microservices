<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col col-lg-6">
        <div class="card">
          <div class="card-header">
            <h4>Add Customer</h4>
          </div>
          <div class="card-body">
            <form @submit.prevent="submitFormCustomer">
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span>
                  Nama</label
                >
                <input
                  type="text"
                  class="form-control"
                  id="name"
                  name="name"
                  v-model="customer.name"
                  required
                />
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span>
                  Contact</label
                >
                <input
                  type="text"
                  class="form-control"
                  id="contact"
                  name="contact"
                  v-model="customer.contact"
                  required
                />
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span>
                  Email</label
                >
                <input
                  type="email"
                  class="form-control"
                  id="email"
                  name="email"
                  v-model="customer.email"
                  required
                />
              </div>
              <div class="mb-3">
                <label class="form-label">Tipe Diskon</label>
                <select class="form-select" v-model="customer.tipeDiskon">
                  <option value="" selected>--Pilih Tipe Diskon--</option>
                  <option value="persentase">Persentase</option>
                  <option value="fix">Fix</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label"> Diskon</label>
                <input
                  type="text"
                  class="form-control"
                  id="diskon"
                  name="diskon"
                  v-model="customer.diskon"
                />
              </div>
              <div class="mb-3">
                <label class="form-label"
                  ><span class="text-danger"><strong>*</strong></span>
                  Alamat</label
                >
                <textarea
                  class="form-control"
                  id="alamat"
                  v-model="customer.alamat"
                  rows="3"
                ></textarea>
              </div>
              <div class="mb-3">
                <label for="formFile" class="form-label"
                  ><span class="text-danger"><strong>*</strong></span
                  >Ktp (Image)</label
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
              <RouterLink to="/customers" class="btn btn-danger float-end">
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
import { ref } from "vue";
import { useRouter } from "vue-router";

export default {
  name: "editCustomer",
  data() {
    return {
      customer: {},
      selectedFile: null,
      previewImage: null,
    };
  },
  mounted() {
    this.getCustomerById(this.$route.query.customerId);
  },
  methods: {
    handleImageUpload(event) {
      this.customer.ktp = event.target.files[0];
      this.selectedFile = event.target.files[0];
      this.previewImage = URL.createObjectURL(this.selectedFile);
    },
    getCustomerById(id) {
      try {
        axios.get(`http://localhost/api/customer/${id}`).then((res) => {
          this.customer = res.data;
        });
      } catch (err) {
        console.log("getCustomerById failed");
      }
    },
    async submitFormCustomer() {
      const jsonItems = {
        name: this.customer.name,
        contact: this.customer.contact,
        email: this.customer.email,
        alamat: this.customer.alamat,
        diskon: this.customer.diskon,
        tipeDiskon: this.customer.tipeDiskon,
      };

      const formData = new FormData();
      formData.append("customer", JSON.stringify(jsonItems));

      if (this.selectedFile) {
        formData.append("ktp", this.customer.ktp);
      }

      try {
        const response = await axios.put(
          `http://localhost/api/customer/${this.customer.id}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        if (response.status === 200) {
          const alertMessage = "Customer updated successfully!";
          this.$router.push({
            name: "customers",
            query: { updateMessage: alertMessage, responseCode: response.status },
          });
          console.log("Customers updated successfully");
        } else {
          const alertMessage = "Failed to update customer!";
          this.$router.push({
            name: "customers",
            query: { updateMessage: alertMessage, responseCode: response.status },
          });
          console.error("Failed to update customer");
        }
      } catch (error) {
        console.error("Error:", error);
      }
    },
  },
};
</script>
