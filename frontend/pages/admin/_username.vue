<template>
  <v-container
    column
    justify-center
    align-center
    :v-show="dataLoading"
  >
    <h4 class="display-1">Administrator Details:</h4>
    <v-row>
      <v-col cols="6">
        <span class="body-1">
          <p class="mt-5">Name: {{ admin.name }}</p>
          <p>Username: {{ admin.username }}</p>
          <p>Email: {{ admin.email }} </p>
          <p class="mb-5">Birthday: {{ admin.birthday }}</p>
        </span>
      </v-col>
    </v-row>
    <v-row class="mb-5 ml-1">
          <v-dialog v-model="dialog" persistent max-width="800">
            <template v-slot:activator="{ on }">
               <v-btn text icon append v-on="on">Edit Administrator <v-icon class="ml-2">mdi-pencil</v-icon></v-btn>
            </template>
            <v-card>
              <v-card-title class="headline">Edit Administrator</v-card-title>
              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <v-form ref="form">
                     

                      <v-text-field
                        v-model="admin.name"
                        label="Name"
                        required
                      ></v-text-field>

                      <v-text-field
                        v-model="admin.email"
                        label="E-mail"
                        required
                      ></v-text-field>

                       <v-text-field
                        v-model="admin.password"
                        label="Password"
                        type="password"
                        required
                      ></v-text-field>

                      <v-menu
                        ref="menu"
                        v-model="menu"
                        :close-on-content-click="false"
                        transition="scale-transition"
                        offset-y
                        min-width="290px"
                      >
                        <template v-slot:activator="{ on }">
                          <v-text-field
                            v-model="birthdayToFormat"
                            label="Choose Birthday"
                            readonly
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker v-model="birthdayToFormat" no-title scrollable>
                          <v-spacer></v-spacer>
                          <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                          <v-btn text color="primary" @click="$refs.menu.save(birthdayToFormat)">OK</v-btn>
                        </v-date-picker>
                      </v-menu>
                    </v-form>
                  </v-col>
                </v-row>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text @click="dialog = false">Cancel</v-btn>
                <v-btn color="green darken-1" text @click="updateUser">Save</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-row>
  </v-container>
</template>

<script>
  export default {
    name: "sportInfo",
    data() {
      return {
        admin: {
        },
        dialog: false,
        dataLoading: false,
        birthdayToFormat: "",
        menu: false
      }
    },
    created() {
      this.dataLoading = true;
      this.$axios.$get(`/api/administrators/${this.username}`)
        .then((admin) => {
          this.admin = admin
        })
        .finally(() => {
          this.dataLoading = false;
        });
    },
    watch: {
      'birthdayToFormat'(newValue) {
        this.admin.birthday = this.$moment(newValue, 'YYYY-MM-DD').format('DD/MM/YYYY')
      }
    },
    computed: {
      username() {
        return this.$route.params.username
      }
    },
    methods: {
      updateUser(){
        this.$axios.$put(`/api/administrators/${this.admin.username}`, {
          name: this.admin.name, 
          email: this.admin.email, 
          password: this.admin.password, 
          birthday: this.admin.birthday
        }).then((response) => {
          this.dialog = false;
          this.$toast.success("Updated with sucess!",  {duration: 3000});
        }).catch(error => {
          this.$toast.error(error.response.data, {duration: 3000});
        })
      }
    },
  }
</script>

<style scoped>

</style>
