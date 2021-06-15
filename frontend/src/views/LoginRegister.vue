<template>
    <div class="container">
        <div class="link">
        </div>
        <router-view class="lr-form"/>
    </div>
</template>

<script>
    export default {
        name: "LoginRegister"
    }
</script>

<style scoped>
.container {
  width: 40%;
  margin: 50px 30%;
  border: solid 1px gray;
  border-radius: 10px;
}
.lr-form {
  margin: 20px 10%;
  text-align: center;
}
.link {
  margin: 10px 0 0 0;
}
a {
  text-decoration: none;
  color: lightskyblue;
  font-weight: bold;
}
.router-link-active {
  color: deepskyblue;
}
</style>
