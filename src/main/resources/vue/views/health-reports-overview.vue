<template id="health-reports-overview">
  <app-layout>
    <div class="card bg-light mb-3">

    </div>
  </app-layout>
</template>
<script>
Vue.component("health-reports-overview", {
  template: "#health-reports-overview",
  data: () => ({
    healthReports: []
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/health-reports")
          .then(res => this.healthReports = res.data)
          .catch(() => alert("Error while fetching health reports"));
    }
  }
});
</script>