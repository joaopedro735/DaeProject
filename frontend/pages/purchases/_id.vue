<template>
  <v-container
    column
    justify-center
    align-center
    :v-show="dataLoading"
  >
    <h4 class="display-1">Purchase Details:</h4>
    <v-row>
      <v-col cols="6">
        <span class="body-1">
          <p class="mt-5">Purchase Date: {{ purchase.purchaseDate }}</p>
          <p>Total: {{ purchase.totalEuros }}€</p>
          <p>Username: {{ purchase.name }} </p>
          <p>Total Paid: {{totalPaid}}</p>
        </span>
      </v-col>
    </v-row>
    <div>
      <h3 class="text-center">Payments</h3>
    </div>
    <v-row>
      <v-col cols="12">
        <v-data-table
          :headers="headersPayments"
          :items="purchase.payments"
          item-key="id"
          :items-per-page="5"
          class="elevation-1 mt-5"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
        </v-data-table>
      </v-col>
    </v-row>

     <v-row class="mb-5 ml-1">
          <v-dialog v-model="dialog" persistent max-width="800">
            <template v-slot:activator="{ on }">
               <v-btn text icon append v-on="on">Add new Payment <v-icon class="ml-2">mdi-pencil</v-icon></v-btn>
            </template>
            <v-card>
              <v-card-title class="headline">New Payment</v-card-title>
              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <v-form ref="form">
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
                            v-model="limitDayToFormat"
                            label="Choose Limit Day of Payment"
                            readonly
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker v-model="limitDayToFormat" no-title scrollable>
                          <v-spacer></v-spacer>
                          <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                          <v-btn text color="primary" @click="$refs.menu.save(limitDayToFormat)">OK</v-btn>
                        </v-date-picker>
                      </v-menu>

                      <v-text-field
                        v-model="payment.paymentMethod"
                        label="Payment Method"
                        required
                      ></v-text-field>

                       <v-text-field
                        v-model="payment.value"
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
                <v-btn color="green darken-1" text @click="addPayment">Add Payment</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-row>
    
    <div>
      <h3 class="text-center">Products Purchases</h3>
    </div>
    <v-row>
      <v-col cols="12">
        <v-data-table
          :headers="headersProductsPurchases"
          :items="purchase.productPurchases"
          item-key="id"
          :items-per-page="5"
          class="elevation-1 mt-5"
          :loading="dataLoading"
          loading-text="Loading... Please wait"
        >
        </v-data-table>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: "PurchaseInfo",
    data() {
      return {
        purchase: {},
        dialog: false,
        dataLoading: false,
        limitDayToFormat: "",
        menu: false,
        totalPaid: 0,
        payment: {
          limitDayPayment: "",
          paymentMethod: "",
          value: null
        },
        headersPayments: [
          {
            text: "Date of Payment",
            value: "datePayment"
          },
          {
            text: "Limite Day Payment",
            value: "limitDayPayment"
          },
          {
            text: "Payment Method",
            value: "paymentMethod"
          },
          {
            text: "State",
            value: "state"
          }, 
          {
            text: "Value",
            value: "value"
          }
        ],
        headersProductsPurchases: [
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
        ]
      }
    },
    created() {
      this.dataLoading = true;
      this.$axios.$get(`/api/purchases/${this.id}`)
        .then((purchase) => {
          this.purchase = purchase
          this.purchase.payments.forEach(element => {
          this.totalPaid += element.value;
        });
        })
        .finally(() => {
          this.dataLoading = false;
        });

       
    },
    computed: {
      id() {
        return this.$route.params.id
      }
    },
    methods: {
      addPayment(){
        this.$axios.$post(`/api/purchases/${this.purchase.id}/payment`, this.payment).then((response) => {
           this.dialog = false;
           this.purchase.payments.push(response);
          this.$toast.success("Payment added with sucess!",  {duration: 3000});
        }).catch(error => {
          this.$toast.error(error.response.data, {duration: 3000});
        })
      },
    },
    watch: {
      'limitDayToFormat'(newValue) {
        this.payment.limitDayPayment = this.$moment(newValue, 'YYYY-MM-DD').format('DD/MM/YYYY')
      }
    },
  }
</script>

<style scoped>

</style>
