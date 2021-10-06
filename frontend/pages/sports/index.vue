<template>
  <v-container
    column
    justify-center
    align-center
  >
    <v-data-table
      :headers="headers"
      :items="sports"
      item-key="code"
      :items-per-page="5"
      class="elevation-1"
      :loading="dataLoading"
      loading-text="Loading... Please wait"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Sports</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          />
          <v-spacer/>
          <v-dialog v-model="dialog" max-width="600px" light>
            <template v-slot:activator="{ on }">
              <v-btn color="primary" dark class="mb-2" v-on="on">New Sport</v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-form
                  ref="form"
                  v-model="valid">
                  <v-container>
                    <v-row>
                      <v-col cols="12">
                        <v-text-field v-model="editedItem.name" label="Sport name" autofocus :rules="requiredRule"/>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-currency-field v-model="editedItem.registrationPrice" label="Registration price"
                                          append-icon="mdi-currency-eur"
                        />
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-currency-field v-model="editedItem.membershipPrice" label="Membership price"
                                          append-icon="mdi-currency-eur"/>
                      </v-col>

                    </v-row>
                  </v-container>
                </v-form>
              </v-card-text>

              <v-card-actions>
                <v-spacer/>
                <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                <v-btn color="blue darken-1" text @click="save">Save</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:item.actions="{item}">
        <v-btn text icon append :to="`./${item.code}`">
          <v-icon>mdi-information-outline</v-icon>
        </v-btn>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
  export default {
    name: "index.vue",
    data() {
      return {
        dialog: false,
        valid: false,
        headers: [
          {
            text: "Code",
            value: "code"
          },
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
        sports: [],
        dataLoading: false,
        editedIndex: -1,
        editedItem: {
          name: "",
          registrationPrice: 0,
          membershipPrice: 0,
        },
        defaultItem: {
          name: "",
          registrationPrice: 0,
          membershipPrice: 0,
        },
        requiredRule: [
          v => !!v || 'Field is required',
        ]
      }
    },
    created() {
      this.dataLoading = true;
      this.getSports();
    },
    computed: {
      formTitle() {
        return this.editedIndex === -1 ? 'New Sport' : 'Edit Sport'
      },
    },
    watch: {
      dialog(val) {
        val || this.close()
      },
    },
    methods: {
      getSports() {
        this.$axios.$get('/api/sports')
          .then((sports) => {
            this.sports = sports;
          })
          .finally(() => {
            this.dataLoading = false;
          });
      },
      close() {
        this.dialog = false;
        setTimeout(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        });
      },
      save() {
        if (this.editedIndex > -1) {
        } else {
          this.$axios.post('/api/sports', this.editedItem)
            .then((resp) => {
              this.$toast.success("Created sport successfully", {duration: 3000});
            })
            .catch((error) => {
              this.$toast.error("Error creating sport: " + error.response.data, {duration: 10000});
            });
        }
        this.getSports();
        this.close()
      }
    }
  }
</script>

<style scoped>

</style>
