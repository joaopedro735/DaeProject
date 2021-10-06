<template>
  <v-container
    column
    justify-center
    align-center
  >
    <h2 class="center">Enroll athlete</h2>
    <v-row>
      <v-col cols="12" md="10" sm="12">
        <v-text-field
          v-model="nameToSearch"
          clearable
          @click:clear="getAthletes"
          @keypress.enter="getAthletes"
          label="Write the name to search"
        />
      </v-col>
      <v-col cols="12" md="2" sm="12">
        <v-btn block color="normal" class="mt-3" @click="getAthletes">Search</v-btn>
      </v-col>
    </v-row>
    <v-data-table
      v-model="selectedAthlete"
      :headers="headers"
      :items="athletes"
      item-key="username"
      :items-per-page="5"
      class="elevation-1 mt-5 mb-4"
      :loading="dataLoading"
      loading-text="Loading... Please wait"
      show-select
      single-select
    >
      <template v-slot:item.actions="{item}">
        <v-btn text icon append :to="`../${item.username}`">
          <v-icon>mdi-information-outline</v-icon>
        </v-btn>
      </template>
    </v-data-table>
    <v-data-table
      v-model="selectedTimetables"
      :headers="timeTableHeaders"
      :items="timetables"
      item-key="id"
      :items-per-page="5"
      class="elevation-1"
      :loading="dataLoading"
      loading-text="Loading... Please wait"
      show-select
    >
      <template v-slot:item.actions="{item}">
        <v-btn text icon append>
          <v-icon>mdi-information-outline</v-icon>
        </v-btn>
      </template>
    </v-data-table>
    <v-row>
      <v-col>
        <v-btn color="success" @click="onSubmit">Enroll Athlete</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: "enrollAthlete",
    data() {
      return {
        headers: [
          {
            text: "Username",
            value: "username"
          },
          {
            text: "Name",
            value: "name"
          },
          {
            text: "Email",
            value: "email"
          },
          {
            text: "Birthday",
            value: "birthday"
          }
        ],
        timeTableHeaders: [
          {
            text: "Day",
            value: "day"
          },
          {
            text: "Start",
            value: "start"
          },
          {
            text: "End",
            value: "end"
          },
          {
            text: "Actions",
            value: "actions",
            align: "center"
          }
        ],
        timetables: [],
        athletes: [],
        dataLoading: false,
        nameToSearch: "",
        selectedAthlete: [],
        selectedTimetables: []
      }
    },
    methods: {
      getAthletes() {
        this.dataLoading = true;
        this.$axios.$get(`/api/athletes/availableToEnroll/${this.code}`, {params: {name: this.nameToSearch}})
          .then((athletes) => {
            this.athletes = athletes;
            if (this.athletes.length === 0) {
              this.$toast.info("No athletes available to enroll", {iconPack: "mdi", icon: 'alert', duration: 10000})
            }
          })
          .catch((error) => {
            this.$toast.error("Error getting athletes")
          })
          .finally(() => {
            this.dataLoading = false;
          });
      },
      getTimeTables() {
        this.dataLoading = true;
        this.$axios.$get(`/api/sports/${this.code}/timetables`)
          .then((timetables) => {
            this.timetables = timetables
            if (this.timetables.length === 0) {
              this.$toast.info("No timetables in the selected sport", {iconPack: "mdi", icon: 'alert', duration: 10000})
            }
          })
          .catch((error) => {
            this.$toast.error("Error getting timetables")
          })
          .finally(() => {
            this.dataLoading = false;
          });
      },
      onSubmit() {
        if (this.selectedAthlete.length !== 1) {
          this.$toast.error("You need to select a athlete", {duration: 10000});
          return;
        }
        if (this.selectedAthlete.length < 1) {
          this.$toast.error("You need to select at least 1 timetable", {duration: 10000});
          return;
        }
        this.$axios.put(`/api/sports/${this.code}/athletes/${this.selectedAthlete[0].username}/enroll`, this.selectedTimetables)
          .then((resp) => {
            console.log()
            this.$toast.success("Athlete enrolled with success", {duration: 3000});
            this.$router.push({path: `/sports/${this.code}`});
          })
          .catch((error) => {
            this.$toast.error(error.response.data, {duration: 10000});
          });
      }
    },
    computed: {
      code() {
        return this.$route.params.code
      }
    },
    created() {
      this.getAthletes();
      this.getTimeTables();
    }
  }
</script>

<style scoped>

</style>
