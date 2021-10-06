<template>
  <v-form
    ref="form"
    v-model="valid"
  >

      <v-text-field
      v-model="username"
      :rules="usernameRules"
      label="Username"
      required
    />

    <v-text-field
      v-model="password"
      :rules="passwordRules"
      label="Password"
      :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
      :type="showPassword ? 'text' : 'password'"
      hint="At least 3 characters"
      counter
      required
      @click:append="showPassword = !showPassword"
      v-on:keypress.enter="onSubmit"
    />

    <v-btn
      :disabled="!valid"
      color="success"
      class="mr-4"
      @click="onSubmit"
    >
      Login
    </v-btn>

    <v-btn
      color="error"
      class="mr-4"
      @click="reset"
    >
      Reset
    </v-btn>


  </v-form>
</template>

<script>
  export default {
    name: "login",
    data() {
      return {
        valid: true,
        username: '',
        usernameRules: [
          v => !!v || 'Username is required',
        ],
        password: '',
        passwordRules: [
          v => !!v || 'Password is required',
          v => (v && v.length >= 3) || 'Password must be greater than 3 characters',
        ],
        showPassword: false
      }
    },
    methods: {
      onSubmit () {
        if (!this.$refs.form.validate()) {
          this.$toast.error("", {
            duration: 3000
          })
          return;
        }
        let promise = this.$auth.loginWith('local', {
          data: {
            username: this.username,
            password: this.password
          }
        })

        promise.then(() => {
          this.$toast.success('You are logged in', {
            duration: 3000
          });
          //this.$router.push({ path: '/' + this.user.groups[0] + 's/' + this.user.sub })
        })

        promise.catch(() => {
          this.$toast.error("Sorry, you can't login. Ensure your credentials are correct", {
            duration: 3000
          })
        })
      },
      reset () {
        this.$refs.form.reset()
      },
    },
  }
</script>

<style scoped>

</style>
