<template>
  <v-container
    column
    justify-center
    align-center
  >
    <v-row>
      <v-col cols="10">
        <h2 class="text-center">Create a New Purchase</h2>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-form ref="form">
          <v-row>
            <v-col md="5" sm="10">
              <v-text-field
                v-model="nameToSearchUser"
                label="Write the name to search the user by name"
                clearable
                @click:clear="getUsers"
              ></v-text-field>
            </v-col>
            <v-col md="2">
              <v-btn block color="normal" class="mt-3" @click="getUsersByName">Search</v-btn>
            </v-col>
            <v-col md="5" sm="12">
              <v-select
                v-model="purchase.username"
                :items="users"
                item-value="username"
                item-text="name"
                label="Users"
                required
              ></v-select>
            </v-col>
          </v-row>
          <v-divider/>
          <v-row>
            <v-col md="5" sm="10">
              <v-text-field
                v-model="nameToSearchProd"
                label="Write the name to search the product"
                clearable
                @click:clear="getProducts"
              />
            </v-col>
            <v-col md="2">
              <v-btn block color="normal" class="mt-3" @click="getProductsByName">Search</v-btn>
            </v-col>
            <v-col md="5" sm="12">
              <v-select
                v-model="productPurchase.product.id"
                :items="prods"
                item-value="id"
                item-text="description"
                label="Product"
                required
              />
            </v-col>
          </v-row>
          <v-row>
            <v-col md="4" sm="6">
              <v-text-field
                v-model="productPurchase.quantity"
                label="Quantity"
                required
              />
            </v-col>
            <v-col md="4" sm="6">
              <v-text-field
                v-model="productPurchase.unity"
                label="Unity"
                required
              />
            </v-col>
            <v-col md="4" sm="12">
              <v-btn block color="normal" class="mt-3" @click="addProductPurchaseToPurchase">Adicionar Produto</v-btn>
            </v-col>
          </v-row>
          <v-divider/>
          <v-row>
            <v-col>
              <v-data-table
                :headers="headers"
                :items="prodsPurchasesActive"
                :items-per-page="5"
                class="elevation-1 mb-1"
                loading-text="Loading... Please wait"
              >
                <template v-slot:item.remove="{item}">
                  <v-btn text icon append @click="removeItem(item.product.id, item.quantity)">
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </template>
              </v-data-table>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-btn block color="primary" class="mr-4" @click="createPurchase">
                Create Purchase
              </v-btn>
            </v-col>
          </v-row>
        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: "create.vue",
    data() {
      return {
        productPurchase: {
          product: {
            id: null
          },
          quantity: null,
          unity: ""
        },
        nameToSearch: {
          user: "",
          prods: ""
        },
        nameToSearchProd: "",
        nameToSearchUser: "",
        users: [],
        prods: [],
        prodsPurchasesActive: [],
        purchase: {
          username: "",
          productPurchases: []
        },
        headers: [
          {
            text: "Product",
            value: "product.description"
          },
          {
            text: "Quantity",
            value: "quantity"
          },
          {
            text: "Unity",
            value: "unity"
          },
          {
            text: "Price per Unity",
            value: "product.value"
          },
          {
            text: "Remove",
            value: "remove",
            align: "center"
          }
        ],
        dataLoading: false,
      }
    },
    created() {
      this.getUsers();
      this.getProducts();
    },
    methods: {
      createProduct() {
        this.$axios.$post('/api/products', this.product).then((response) => {
          this.$toast.success("Created Product with Success", {duration: 3000});
          this.$router.push({path: `/purchases/all`});
        }).catch((error) => {
          this.$toast.error(error.response.data);
        })
      },
      getUsersByName() {
        this.$axios.$get('/api/users/name/' + this.nameToSearchUser).then((users) => {
          this.users = users;
        })
      },
      getUsers() {
        this.$axios.$get(`/api/users`)
          .then((users) => {
            this.users = users
          })
      },
      getProducts() {
        this.$axios.$get(`/api/products`)
          .then((prods) => {
            this.prods = prods
          })
      },
      getProductsByName() {
        this.$axios.$get('/api/products/name/' + this.nameToSearchProd).then((prods) => {
          this.prods = prods;
        })
      },
      addProductPurchaseToPurchase() {
        this.purchase.productPurchases.push({
          quantity: this.productPurchase.quantity,
          unity: this.productPurchase.unity,
          product: {
            id: this.productPurchase.product.id
          }
        });
        console.log(this.purchase);
        this.prodsPurchasesActive.push({
          product: this.prods.filter((item) => item.id == this.productPurchase.product.id)[0],
          quantity: this.productPurchase.quantity,
          unity: this.productPurchase.unity
        });
      },
      createPurchase() {
        this.$axios.$post('/api/purchases', this.purchase).then((response) => {
          this.$toast.success("Created Purchase with Success", {duration: 3000});
          this.$router.push({path: `/purchases/all`});
        }).catch((error) => {
          this.$toast.error(error.response.data);
        })
      },
      removeItem(id, quantity) {
        console.log(quantity)
        let m = this.purchase.productPurchases.findIndex((item) => item.product.id === id && item.quantity === quantity)
        let d = this.prodsPurchasesActive.findIndex((item) => item.product.id === id && item.quantity === quantity)
        console.log(m)
        this.purchase.productPurchases.splice(m,1);
        this.prodsPurchasesActive.splice(d, 1);
      }
    },
  }
</script>

<style scoped>

</style>
