<template>
  <v-container
    column
    justify-center
    align-center
    :v-show="dataLoading"
  >
    <h4 class="display-1">Partner Details:</h4>
    <v-row>
      <v-col cols="6">
        <span class="body-1">
          <p class="mt-5">Name: {{ trainer.name }}</p>
          <p>Username: {{ trainer.username }}</p>
          <p>Email: {{ trainer.email }} </p>
          <p class="mb-5">Birthday: {{ trainer.birthday }}</p>
        </span>
      </v-col>
    </v-row>
    <v-row class="mb-5 ml-1">
          <v-dialog v-model="dialog" persistent max-width="800">
            <template v-slot:activator="{ on }">
               <v-btn text icon append v-on="on">Edit Trainer <v-icon class="ml-2">mdi-pencil</v-icon></v-btn>
            </template>
            <v-card>
              <v-card-title class="headline">Edit Trainer</v-card-title>
              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <v-form ref="form">
                     

                      <v-text-field
                        v-model="trainer.name"
                        label="Name"
                        required
                      ></v-text-field>

                      <v-text-field
                        v-model="trainer.email"
                        label="E-mail"
                        required
                      ></v-text-field>

                       <v-text-field
                        v-model="trainer.password"
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
                <v-btn color="green darken-1" text @click="updateTrainer">Save</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-row>

    <v-list light>
      <v-list-group v-for="sportRegistration in trainer.sportRegistrations" :key="sportRegistration.id">
        <template v-slot:activator>
          <v-list-item-content>
            <v-list-item-title v-text="sportRegistration.sportName"/>
            <v-list-item-subtitle v-if="typeof (sportRegistration.rank)!= 'undefined'"
                                  v-text="'Rank:'+sportRegistration.rank.name"/>
            <v-list-item-subtitle v-if="typeof (sportRegistration.graduation)!= 'undefined'"
                                  v-text="'Graduation:'+sportRegistration.graduation.name"/>
          </v-list-item-content>
        </template>
        <v-data-table
          v-if="typeof (sportRegistration.timeTables)!= 'undefined'"
          :headers="timeTableHeaders"
          :items="sportRegistration.timeTables"
          item-key="id"
          :items-per-page="5"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
          <template v-slot:item.actions="{item}">
            <v-btn text icon append>
              <v-icon>mdi-information-outline</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </v-list-group>
    </v-list>
  </v-container>
</template>

<script>
  export default {
    name: "sportInfo",
    data() {
      return {
        trainer: {
          sportRegistrations: []
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
          },
        ],
        dialog: false,
        dataLoading: false,
        birthdayToFormat: "",
        menu: false
      }
    },
    created() {
      this.dataLoading = true;
      this.$axios.$get(`/api/trainers/${this.username}`)
        .then((trainer) => {
          this.trainer = trainer
        })  
        .finally(() => {
          this.dataLoading = false;
        });
    },
    watch: {
      'birthdayToFormat'(newValue) {
        this.trainer.birthday = this.$moment(newValue, 'YYYY-MM-DD').format('DD/MM/YYYY')
      }
    },
    computed: {
      username() {
        return this.$route.params.username
      }
    },
    methods: {
      updateTrainer(){
        this.$axios.$put(`/api/trainers/${this.trainer.username}`, {
          name: this.trainer.name, 
          email: this.trainer.email, 
          password: this.trainer.password, 
          birthday: this.trainer.birthday
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
