<template>
  <v-container
    column
    justify-center
    align-center
  >
    <v-data-table
      :headers="headers"
      :items="purchases"
      item-key="id"
      :items-per-page="5"
      class="elevation-1 mt-5"
      :loading="dataLoading"
      loading-text="Loading... Please wait"
    >
      <template v-slot:item.actions="{item}">
        <v-btn text icon append :to="`../${item.id}`"><v-icon>mdi-information-outline</v-icon></v-btn>
      </template>
       <template v-slot:item.remove="{item}">
        <v-btn text icon append @click="removePurchase(item.id)"><v-icon>mdi-close-circle</v-icon></v-btn>
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
            text: "Name of User",
            value: "name"
          },
          {
            text: "Purchase Date",
            value: "purchaseDate"
          },
          {
            text: "Total €",
            value: "totalEuros"
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
        purchases: [],
        dataLoading: false, 
      }
    },
    created() {
      this.getAllPurchases();
    }, 
    methods: {
      getAllPurchases(){
        this.dataLoading = true;
        this.$axios.$get('/api/purchases')
        .then((purchases) => {
          this.purchases = purchases
        })
        .finally(() => {
          this.dataLoading = false;
        });
      },
      removePurchase(id){
        /*this.dataLoading = true;
        this.$axios.$delete('/api/products/' + id).then((response => {
          this.products = this.products.filter((item) => item.id !== id);
          this.$toast.success("Deleted with sucess");
        })).catch((error) => {
           this.$toast.error(error.response.data, {duration: 3000});
        });
        this.dataLoading = false;*/
      }  
    },
  }
</script>

<style scoped>

</style>
