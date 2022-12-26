<template id="user-activity-overview">
  <app-layout>
    <div>
      <h3>Activities list </h3>
      <ul>
        <li v-for="activity in activities">
          {{ activity.id }}: {{ activity.description }} for {{ activity.duration }} minutes
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
Vue.component("user-activity-overview", {
  template: "#user-activity-overview",
  data: () => ({
    activities: [],
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
  },
});
</script>