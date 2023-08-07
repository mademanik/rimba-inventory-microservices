<template>
    <div class="container mt-5">
      <div class="card">
        <div class="card-header">
          <h4>
            Customers
            <RouterLink to="/student/create" class="btn btn-primary float-end">
              Add Customer
            </RouterLink>
          </h4>
        </div>
        <div class="card-body">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Website</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Created At</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody v-if="this.students.length > 0">
              <tr v-for="(student, index) in this.students" :key="index">
                <td>{{ student.id }}</td>
                <td>{{ student.name }}</td>
                <td>{{ student.website }}</td>
                <td>{{ student.email }}</td>
                <td>{{ student.phone }}</td>
                <td>Now</td>
                <td>
                  <RouterLink to="/edit" class="btn btn-success ml-2 mr-2"
                    >Edit</RouterLink
                  >
                  <button type="button" class="btn btn-danger ml-2 mr-2">
                    Delete
                  </button>
                </td>
              </tr>
            </tbody>
            <tbody v-else>
              <tr>
                <td colspan="7">Loading...</td>
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
    name: "students",
    data() {
      return {
        students: [],
      };
    },
    mounted() {
      // console.log("Made Manik");
      this.getStudents();
    },
    methods: {
      getStudents() {
        axios.get("https://jsonplaceholder.typicode.com/users").then((res) => {
          this.students = res.data;
          console.log(this.students);
        });
      },
    },
  };
  </script>
  