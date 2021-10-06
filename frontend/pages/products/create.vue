<template>
  <v-container
    column
    justify-center
    align-center
  >
    <div>
      <h2 class="text-center">Create a New Product</h2>
    </div>
    <v-row>
      <v-col cols="12">
        <v-form ref="form">
          <v-text-field
            v-model="product.description"
            label="Description"
            required
          ></v-text-field>

          <v-text-field
            v-model="product.value"
            label="Value"
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

          <v-btn
            color="primary"
            class="mr-4"
            @click="createProduct"
          >
            Create Product
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
        product: {
          description: "",
          value: "",
          typeCode: "",
        },
        types: [],
      }
    },
    created() {
      this.getTypes();
    },
    methods: {
      createProduct() {
        this.$axios.$post('/api/products', this.product).then((response) => {
          this.$toast.success("Created Product with Success", {duration: 3000});
          this.$router.push({path: `/products/all`});
        }).catch((error) => {
          this.$toast.error(error.response.data);
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
