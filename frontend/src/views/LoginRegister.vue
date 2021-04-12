<template>
    <div class="container">
        <div class="link">
            <router-link to="/register">注册</router-link> |
            <router-link to="/login">登录</router-link>
        </div>
        <router-view class="form"/>
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
        margin: 10px 30%;
        border: solid 1px gray;
        border-radius: 10px;
    }
    .form {
        margin: 20px 10%
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
