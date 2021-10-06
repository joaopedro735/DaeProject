<template>

</template>

<script>
  export default {
    name: "logout",
    mounted() {
      let promise = this.$auth.logout();

      promise.then(() => {
        this.$toast.success('You are logged out', {
          duration: 3000
        });
      });
      promise.catch(() => {
        this.$toast.error("Sorry, you can't logout!")
      })
    }
  }
</script>

<style scoped>
</style>
