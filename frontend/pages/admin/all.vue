<template>
  <v-container
    column
    justify-center
    align-center
  >
  <v-row>
    <v-col cols="9">
       <v-text-field
            v-model="nameToSearch"
            label="Write the name to search"
          ></v-text-field>
    </v-col>
    <v-col cols="1">
      <v-btn icon color="normal" class="mt-3" @click="getAllAdministrators(); nameToSearch=''"><v-icon>mdi-close</v-icon></v-btn>
    </v-col>
    <v-col cols="2">
      <v-btn block color="normal" class="mt-3" @click="getAdministratorsByName(nameToSearch)">Search</v-btn>
    </v-col>
  </v-row>
    <v-data-table
      :headers="headers"
      :items="administrators"
      item-key="username"
      :items-per-page="5"
      class="elevation-1 mt-5"
      :loading="dataLoading"
      loading-text="Loading... Please wait"
    >
      <template v-slot:item.actions="{item}">
        <v-btn text icon append :to="`../${item.username}`"><v-icon>mdi-information-outline</v-icon></v-btn>
      </template>
       <template v-slot:item.remove="{item}">
        <v-btn text icon append @click="removeAdministrator(item.username)"><v-icon>mdi-account-remove</v-icon></v-btn>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
  export default {
    name: "all.vue",
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
          },
          {
            text: "See",
            value: "actions"
          }, 
          {
            text: "Remove",
            value: "remove"
          }
        ],
        administrators: [],
        dataLoading: false, 
        nameToSearch: ""
      }
    },
    created() {
      this.getAllAdministrators();
    }, 
    methods: {
      getAdministratorsByName(username){
        this.dataLoading = true;
        this.$axios.$get('/api/administrators/name/' + username).then((administrators) => {
          this.administrators = administrators;
        })
        .finally(() => {
          this.dataLoading = false;
        });
      }, 
      getAllAdministrators(){
        this.dataLoading = true;
        this.$axios.$get('/api/administrators')
        .then((administrators) => {
          this.administrators = administrators
        })
        .finally(() => {
          this.dataLoading = false;
        });
      },
      removeAdministrator(username){
        this.dataLoading = true;
        this.$axios.$delete('/api/administrators/' + username).then((response => {
          this.administrators = this.administrators.filter((item) => item.username !== username);
          this.$toast.success("Deleted with sucess");
        })).catch((error) => {
           this.$toast.error(error.response.data, {duration: 3000});
        });
        this.dataLoading = false;
      }  
    },
  }
</script>

<style scoped>

</style>
