<template>
  <div class="admin-login-page">
    <!-- 背景装饰文字 -->
    <div class="bg-decor">
      <span class="bg-text bg-text-1">校园淘宝</span>
      <span class="bg-text bg-text-2">轻松购物</span>
      <span class="bg-text bg-text-3">品质生活</span>
      <span class="bg-text bg-text-4">学生专属</span>
      <span class="bg-text bg-text-5">优惠多多</span>
      <span class="bg-text bg-text-6">好物推荐</span>
      <span class="bg-text bg-text-7">正品保障</span>
      <span class="bg-text bg-text-8">极速发货</span>
      <span class="bg-icon bg-icon-1">🛒</span>
      <span class="bg-icon bg-icon-2">📦</span>
      <span class="bg-icon bg-icon-3">🛍️</span>
      <span class="bg-icon bg-icon-4">💰</span>
      <span class="bg-icon bg-icon-5">🔥</span>
    </div>

    <div class="login-container">
      <div class="login-header">
        <div class="logo">
          <span class="logo-icon">🛒</span>
          <span class="logo-text">校园淘宝</span>
        </div>
        <h1>后台管理系统</h1>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" size="large">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="管理员账号" prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <router-link to="/store">返回前台</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { adminLogin } from '@/api/admin'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入管理员账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await adminLogin(form)
    if (res.code === 200) {
      localStorage.setItem('adminToken', res.data)
      ElMessage.success('登录成功')
      router.push('/admin')
    } else {
      ElMessage.error(res.message)
    }
  } catch (e) {
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.admin-login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #2a3a5c 0%, #1e3a5f 50%, #2d4a6a 100%);
  position: relative;
  overflow: hidden;
}

.bg-decor {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.bg-text {
  position: absolute;
  color: rgba(255, 255, 255, 0.08);
  font-weight: 900;
  white-space: nowrap;

  &.bg-text-1 {
    font-size: 120px;
    top: 10%;
    left: -5%;
    transform: rotate(-10deg);
  }

  &.bg-text-2 {
    font-size: 80px;
    bottom: 20%;
    right: -3%;
    transform: rotate(5deg);
  }

  &.bg-text-3 {
    font-size: 60px;
    top: 60%;
    left: 5%;
    transform: rotate(-5deg);
  }

  &.bg-text-4 {
    font-size: 50px;
    top: 15%;
    right: 10%;
    transform: rotate(8deg);
  }

  &.bg-text-5 {
    font-size: 45px;
    bottom: 10%;
    left: 35%;
    transform: rotate(3deg);
  }

  &.bg-text-6 {
    font-size: 55px;
    top: 75%;
    right: 15%;
    transform: rotate(-7deg);
  }

  &.bg-text-7 {
    font-size: 40px;
    top: 35%;
    left: 15%;
    transform: rotate(12deg);
  }

  &.bg-text-8 {
    font-size: 42px;
    top: 5%;
    left: 50%;
    transform: rotate(-4deg);
  }
}

.bg-icon {
  position: absolute;
  font-size: 60px;
  opacity: 0.08;

  &.bg-icon-1 {
    top: 20%;
    left: 15%;
    transform: rotate(-15deg);
  }

  &.bg-icon-2 {
    bottom: 15%;
    right: 20%;
    transform: rotate(10deg);
  }

  &.bg-icon-3 {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%) rotate(-5deg);
    font-size: 80px;
  }

  &.bg-icon-4 {
    top: 80%;
    left: 50%;
    font-size: 50px;
    transform: rotate(20deg);
  }

  &.bg-icon-5 {
    top: 5%;
    right: 35%;
    font-size: 45px;
    transform: rotate(-10deg);
  }
}

.login-container {
  width: 420px;
  background: #fff;
  border-radius: 12px;
  padding: 48px 40px;
  box-shadow: 0 20px 60px rgba(0,0,0,.3);
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-bottom: 16px;
  cursor: pointer;

  .logo-icon {
    font-size: 36px;
  }

  .logo-text {
    font-size: 28px;
    font-weight: bold;
    color: #FF4400;
  }
}

.login-header h1 {
  font-size: 18px;
  color: #999;
  font-weight: normal;
  letter-spacing: 4px;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  background: linear-gradient(135deg, #FF4400, #FF6633);
  border: none;
  letter-spacing: 8px;

  &:hover {
    background: linear-gradient(135deg, #FF6633, #FF8866);
  }
}

.login-footer {
  text-align: center;
  margin-top: 24px;

  a {
    color: #999;
    font-size: 14px;
    transition: color .3s;

    &:hover {
      color: #FF4400;
    }
  }
}
</style>
