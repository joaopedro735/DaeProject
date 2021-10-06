<template>
  <v-container
    column
    justify-center
    align-center
  >
    <div>
      <h2 class="text-center">Products</h2>
    </div>

    <v-row>
      <v-col cols="12" sm="6">
        <v-card
          class="mx-auto mt-5"
          outlined
        >
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="headline mb-1">See All Products</v-list-item-title>
              <v-list-item-subtitle>If you would like to see all the information about the Products in the club</v-list-item-subtitle>
            </v-list-item-content>
            <v-avatar>
            <v-icon dark>mdi-shoe-print</v-icon>
            </v-avatar>
          </v-list-item>

          <v-card-actions class="justify-end">
            <v-btn text icon append :to="`./all`"><v-icon>mdi-arrow-right</v-icon></v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6">
        <v-card
          class="mx-auto mt-5"
          outlined
        >
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="headline mb-1">Create a new Product</v-list-item-title>
              <v-list-item-subtitle>If you would like to create a new Product</v-list-item-subtitle>
            </v-list-item-content>
            <v-avatar>
            <v-icon dark>mdi-account-multiple-plus</v-icon>
            </v-avatar>
          </v-list-item>

          <v-card-actions class="justify-end">
            <v-btn text icon append :to="`./create`"><v-icon>mdi-plus</v-icon></v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: "index.vue",
  }
</script>

<style scoped>

</style>
