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
            label="Write the description to search"
          ></v-text-field>
    </v-col>
    <v-col cols="1">
      <v-btn icon color="normal" class="mt-3" @click="getAllProducts(); nameToSearch=''"><v-icon>mdi-close</v-icon></v-btn>
    </v-col>
    <v-col cols="2">
      <v-btn block color="normal" class="mt-3" @click="getProductsByName(nameToSearch)">Search</v-btn>
    </v-col>
  </v-row>
    <v-data-table
      :headers="headers"
      :items="products"
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
        <v-btn text icon append @click="removeProduct(item.id)"><v-icon>mdi-close-circle</v-icon></v-btn>
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
            text: "Description",
            value: "description"
          },
          {
            text: "Name",
            value: "name"
          },
          {
            text: "Type",
            value: "typeName"
          },
          {
            text: "Value €",
            value: "value"
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
        products: [],
        dataLoading: false, 
        nameToSearch: ""
      }
    },
    created() {
      this.getAllProducts();
    }, 
    methods: {
      getProductsByName(description){
        this.dataLoading = true;
        this.$axios.$get('/api/products/name/' + description).then((products) => {
          this.products = products;
        })
        .finally(() => {
          this.dataLoading = false;
        });
      }, 
      getAllProducts(){
        this.dataLoading = true;
        this.$axios.$get('/api/products')
        .then((products) => {
          this.products = products
        })
        .finally(() => {
          this.dataLoading = false;
        });
      },
      removeProduct(id){
        this.dataLoading = true;
        this.$axios.$delete('/api/products/' + id).then((response => {
          this.products = this.products.filter((item) => item.id !== id);
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
