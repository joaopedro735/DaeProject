<template>
  <div>
    <h2 class="mb-8">Create Rank</h2>
    <v-form
      ref="form"
      v-model="valid"
      v-on:submit.prevent="onSubmit"
    >
      <v-text-field
        v-model="code"
        label="Sport Code"
        readonly
        outlined
      />

      <v-text-field
        v-model="name"
        :rules="nameRules"
        label="Rank Name"
        required
        autofocus
        @keypress.enter="onSubmit"
      />

      <v-btn
        :disabled="!valid"
        color="success"
        class="mr-4"
        @click="onSubmit"
      >
        Create Rank
      </v-btn>

      <v-btn
        color="error"
        class="mr-4"
        @click="reset"
      >
        Reset
      </v-btn>
    </v-form>
  </div>
</template>

<script>
  export default {
    name: "createRank",
    data() {
      return {
        valid: false,
        name: "",
        nameRules: [
          v => !!v || 'Rank name is required',
        ],
      }
    },
    methods: {
      onSubmit() {
        if (!this.$refs.form.validate()) {
          this.$toast.error("All fields are mandatory", {
            duration: 3000
          })
          return;
        }
        this.$axios.post('/api/ranks/', {sportCode: this.code, name: this.name})
          .then((resp) => {
            this.$toast.success("Rank created successfully", {duration: 3000});
            this.$router.push({path: `/sports/${this.code}`});
          })
          .catch((error) => {
            this.$toast.error("Error creating rank: " + error.response.data, {duration: 10000});
          })
      },
      reset() {
        this.$refs.form.resetValidation();
        this.name = "";
      },
    },
    computed: {
      code() {
        return this.$route.params.code
      }
    }
  }
</script>

<style scoped>

</style>
