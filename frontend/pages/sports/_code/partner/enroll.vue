<template>
  <v-container
    column
    justify-center
    align-center
  >
    <h2 class="center">Enroll partner</h2>
    <v-row>
      <v-col cols="12" md="10" sm="12">
        <v-text-field
          v-model="nameToSearch"
          clearable
          @click:clear="getPartners"
          @keypress.enter="getPartners"
          label="Write the name to search"
        />
      </v-col>
      <v-col cols="12" md="2" sm="12">
        <v-btn block color="normal" class="mt-3" @click="getPartners">Search</v-btn>
      </v-col>
    </v-row>
    <v-data-table
      v-model="selectedPartner"
      :headers="headers"
      :items="partners"
      item-key="username"
      :items-per-page="5"
      class="elevation-1 mt-5"
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
    <v-row>
      <v-col>
        <v-btn color="success" @click="onSubmit">Enroll Partner</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: "enrollPartner",
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
        partners: [],
        dataLoading: false,
        nameToSearch: "",
        selectedPartner: []
      }
    },
    methods: {
      getPartners() {
        this.dataLoading = true;
        this.$axios.$get(`/api/partners/availableToEnroll/${this.code}`, {params: {name: this.nameToSearch}})
          .then((partners) => {
            this.partners = partners
          })
          .catch()
          .finally(() => {
            this.dataLoading = false;
          });
      },
      onSubmit() {
        if (this.selectedPartner.length !== 1) {
          this.$toast.error("You need to select a partner", {duration: 10000});
          return;
        }
        this.$axios.put(`/api/sports/${this.code}/partners/${this.selectedPartner[0].username}/enroll`)
          .then((resp) => {
            this.$toast.success(resp.data, {duration: 3000});
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
      this.getPartners();
    }
  }
</script>

<style scoped>

</style>
