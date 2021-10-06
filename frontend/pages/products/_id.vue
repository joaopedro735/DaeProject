<template>
  <v-container
    column
    justify-center
    align-center
    :v-show="dataLoading"
  >
    <h4 class="display-1">Product Details:</h4>
    <v-row>
      <v-col cols="6">
        <span class="body-1">
          <p class="mt-5">Description: {{ product.description }}</p>
          <p>Type: {{ product.typeName }}</p>
          <p>Value: {{ product.value }} </p>
        </span>
      </v-col>
    </v-row>
    <v-row class="mb-5 ml-1">
          <v-dialog v-model="dialog" persistent max-width="800">
            <template v-slot:activator="{ on }">
               <v-btn text icon append v-on="on">Edit Product <v-icon class="ml-2">mdi-pencil</v-icon></v-btn>
            </template>
            <v-card>
              <v-card-title class="headline">Edit Product</v-card-title>
              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <v-form ref="form">
                     

                      <v-text-field
                        v-model="product.description"
                        label="Description"
                        required
                      ></v-text-field>

                      <v-select
                        v-model="product.typeCode"
                        :items="types"
                        item-value="id"
                        item-text="name"
                        label="Type"
                        required
                      ></v-select>

                       <v-text-field
                        v-model="product.value"
                        label="Value"
                        required
                      ></v-text-field>
                    </v-form>
                  </v-col>
                </v-row>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text @click="dialog = false">Cancel</v-btn>
                <v-btn color="green darken-1" text @click="updateProduct">Save</v-btn>
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
        product: {},
        types: [],
        dialog: false,
        dataLoading: false,
        menu: false
      }
    },
    created() {
      this.dataLoading = true;
      this.$axios.$get(`/api/products/product/${this.id}`)
        .then((product) => {
          this.product = product
        })
        .finally(() => {
          this.dataLoading = false;
        });

      this.getTypes();
    },
    computed: {
      id() {
        return this.$route.params.id
      }
    },
    methods: {
      updateProduct(){
        this.$axios.$put(`/api/products/${this.product.id}`, {
          description: this.product.description, 
          value: this.product.value, 
          typeCode: this.product.typeCode, 
        }).then((response) => {
           this.dialog = false;
          this.$toast.success("Updated with sucess!",  {duration: 3000});
        }).catch(error => {
          this.$toast.error(error.response.data, {duration: 3000});
        })
      },
      getTypes(){
        this.dataLoading = true;
        this.$axios.$get(`/api/types`)
        .then((types) => {
          this.types = types
        })
        .finally(() => {
          this.dataLoading = false;
        });
      }
    },
  }
</script>

<style scoped>

</style>
