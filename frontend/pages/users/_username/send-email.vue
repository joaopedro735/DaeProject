<template>
  <v-card>
    <v-toolbar
      flat
      dark
      color="blue accent-3"
    >
      <v-toolbar-title>Compose</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-icon
        :disabled="!valid"
        @click="onSubmit"
      >
        mdi-send
      </v-icon>
    </v-toolbar>
    <v-form
      ref="form"
      v-model="valid"
    >
      <v-text-field
        v-model="username"
        label="Username"
        prepend-inner-icon="mdi-account"
        required
        disabled
        outlined
        single-line
        full-width
        hide-details
      />
      <v-divider></v-divider>
      <v-text-field
        v-model="subject"
        :rules="subjectRules"
        label="Subject"
        required
        single-line
        full-width
        hide-details
      />
      <v-divider></v-divider>
      <v-textarea
        v-model="message"
        :rules="messageRules"
        label="Message"
        counter
        auto-grow
        clearable
        required
        full-width
        single-line
      />
    </v-form>
  </v-card>
</template>

<script>
  export default {
    name: "sendMail",
    data() {
      return {
        valid: true,
        subject: '',
        subjectRules: [
          v => !!v || 'Subject is required',
        ],
        message: '',
        messageRules: [
          v => !!v || 'Message is required',
        ],
      }
    },
    computed: {
      username() {
        return this.$route.params.username
      }
    },
    methods: {
      onSubmit() {
        if (!this.$refs.form.validate()) {
          this.$toast.error("", {
            duration: 3000
          })
          return;
        }
        this.$axios.$post(`/api/users/${this.username}/email/send`, {
          subject: this.subject,
          message: this.message
        })
          .then(msg => {
            this.$toast.success(msg)
          })
          .catch(error => {
            this.$toast.error('error sending the e-mail')
          })
      }
    },
  }
</script>

<style scoped>

</style>
