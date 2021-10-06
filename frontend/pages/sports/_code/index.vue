<template>
  <v-container
    column
    justify-center
    align-center
    :v-show="dataLoading"
  >
    <h4 class="display-1">Sport Details:</h4>
    <span class="body-1">
      <v-row no-gutters>
        <v-col
          mb-6
        >
          <p>Name: {{ sport.name }}</p>
          <p v-if="check(sport.timeTables)">Number of timetables: {{ sport.timeTables.length }}</p>
          <p v-if="check(sport.trainers)">Number of trainers: {{ sport.trainers.length }}</p>
          <p v-if="check(sport.athletes)">Number of athletes: {{ sport.athletes.length }}</p>
          <p v-if="check(sport.partners)">Number of partners: {{ sport.partners.length }}</p>
          <p v-if="check(sport.ranks)">Number of ranks: {{ sport.ranks.length }}</p>
          <p v-if="check(sport.graduations)">Number of graduations: {{ sport.graduations.length }}</p>
        </v-col>
      </v-row>
    </span>

    <v-card class="mb-2" :light="true">
      <v-card-title>Timetables
        <v-spacer/>
        <v-btn color="primary" fab small dark class="mr-2">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <v-btn
          icon
          @click.stop="show.timetables = !show.timetables"
        >
          <v-icon>{{ show.timetables ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-title>

      <div v-show="show.timetables">
        <v-data-table
          :headers="timeTableHeaders"
          :items="sport.timeTables"
          item-key="id"
          :items-per-page="5"
          class="elevation-1"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
          <template v-slot:item.actions="{item}">
            <v-btn text icon append>
              <v-icon>mdi-information-outline</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </div>
    </v-card>

    <v-card class="mb-2" :light="true" >
      <v-card-title>Trainers
        <v-spacer/>
        <v-btn
          icon
          @click="show.trainers = !show.trainers"
        >
          <v-icon>{{ show.trainers ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-title>

      <div v-show="show.trainers">
        <v-data-table
          :headers="userHeaders"
          :items="sport.trainers"
          item-key="id"
          :items-per-page="5"
          class="elevation-1"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
          <template v-slot:item.actions="{item}">
            <v-btn text icon append>
              <v-icon>mdi-information-outline</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </div>
    </v-card>

    <v-card class="mb-2" :light="true">
      <v-card-title>Athletes
        <v-spacer/>
        <v-btn color="primary" fab small dark class="mr-2" append to="athletes/enroll">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <v-btn
          icon
          @click="show.athletes = !show.athletes"
        >
          <v-icon>{{ show.athletes ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-title>

      <div v-show="show.athletes">
        <v-data-table
          :headers="userHeaders"
          :items="sport.athletes"
          item-key="id"
          :items-per-page="5"
          class="elevation-1"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
          <template v-slot:item.actions="{item}">
            <v-btn text icon append>
              <v-icon>mdi-information-outline</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </div>
    </v-card>

    <v-card class="mb-2" :light="true">
      <v-card-title>Partners
        <v-spacer/>
        <v-btn color="primary" fab small dark class="mr-2" append to="partner/enroll">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <v-btn
          icon
          @click="show.partners = !show.partners"
        >
          <v-icon>{{ show.partners ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-title>

      <div v-show="show.partners">
        <v-data-table
          :headers="userHeaders"
          :items="sport.partners"
          item-key="id"
          :items-per-page="5"
          class="elevation-1"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
          <template v-slot:item.actions="{item}">
            <v-btn text icon append>
              <v-icon>mdi-information-outline</v-icon>
            </v-btn>
            <v-btn text icon :to="`/users/${item.username}/send-email`">
              <v-icon>mdi-email-plus</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </div>
    </v-card>

    <v-card class="mb-2" :light="true"
    >
      <v-card-title>Ranks
        <v-spacer/>
        <v-btn color="primary" fab small dark class="mr-2" append to="rank/create">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <v-btn
          icon
          @click="show.ranks = !show.ranks"
        >
          <v-icon>{{ show.ranks ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-title>

      <div v-show="show.ranks">
        <v-data-table
          :headers="rankGraduationHeaders"
          :items="sport.ranks"
          item-key="id"
          :items-per-page="5"
          class="elevation-1"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
          <template v-slot:item.actions="{item}">
            <v-btn text icon append>
              <v-icon>mdi-information-outline</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </div>
    </v-card>

    <v-card class="mb-2" :light="true"
    >
      <v-card-title>Graduations
        <v-spacer/>
        <v-btn color="primary" fab small dark class="mr-2" append to="./graduation/create">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <v-btn
          icon
          @click="show.graduations = !show.graduations"
        >
          <v-icon>{{ show.graduations ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-title>

      <div v-show="show.graduations">
        <v-data-table
          :headers="rankGraduationHeaders"
          :items="sport.graduations"
          item-key="id"
          :items-per-page="5"
          class="elevation-1"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
          <template v-slot:item.actions="{item}">
            <v-btn text icon append>
              <v-icon>mdi-information-outline</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </div>
    </v-card>


  </v-container>
</template>

<script>
  export default {
    name: "sportInfo",
    data() {
      return {
        sport: {
          timeTables: [],
          athletes: [],
          partners: [],
          trainers: [],
          ranks: [],
          graduations: []
        },
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
        userHeaders: [
          {
            text: "Name",
            value: "name"
          },
          {
            text: "Username",
            value: "username"
          },
          {
            text: "Email",
            value: "email"
          },
          {
            text: "Actions",
            value: "actions",
            align: "center"
          }
        ],
        rankGraduationHeaders: [
          {
            text: "Name",
            value: "name"
          },
          {
            text: "Actions",
            value: "actions",
            align: "center"
          }
        ],
        dataLoading: false,
        show: {
          timetables: false,
          trainers: false,
          athletes: false,
          partners: false,
          ranks: false,
          graduations: false
        }
      }
    },
    created() {
      this.dataLoading = true;
      this.$axios.$get(`/api/sports/${this.code}`)
        .then((sport) => {
          this.sport = sport
        })
        .finally(() => {
          this.dataLoading = false;
        });
    },
    computed: {
      code() {
        return this.$route.params.code
      }
    },
    methods: {
      check(object) {
        return (typeof object != "undefined" && object.length > 0);
      }
    }
  }
</script>

<style scoped>

</style>
