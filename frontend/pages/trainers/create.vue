<template>
  <v-container
    column
    justify-center
    align-center
  >
    <div>
      <h2 class="text-center">Create a New Trainer</h2>
    </div>
    <v-row>
      <v-col cols="12">
        <v-form ref="form">
          <v-text-field
            v-model="trainer.username"
            label="Username"
            required
          ></v-text-field>

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

          <v-btn
            color="primary"
            class="mr-4"
            @click="createTrainer"
          >
            Create Trainer
          </v-btn>

        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { timeout } from 'q';
  export default {
    name: "create.vue",
    data() {
      return {
        trainer: {
          username: "",
          email: "",
          password: "",
          name: ""
        },
        birthdayToFormat: "",
        menu: false
      }
    },
    methods: {
      createTrainer() {
        this.$axios.$post('/api/trainers', this.trainer).then((response) => {
          this.$toast.success("Created Trainer with Success", {duration: 3000});
          this.$router.push({path: `/trainers/all`});
        }).catch((error) => {
          this.$toast.error(error.response.data);
        })
      }
    },
    watch: {
      'birthdayToFormat'(newValue) {
        this.trainer.birthday = this.$moment(newValue, 'YYYY-MM-DD').format('DD/MM/YYYY')
      }
    }
  }
</script>

<style scoped>

</style>
