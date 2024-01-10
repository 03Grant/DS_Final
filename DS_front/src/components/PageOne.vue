<template>
  <div class="container">
    <div class="header">
      页面1
    </div>
    <div class="ip-list">
      <div class="ip-row" v-for="(ip, index) in ips" :key="index">
        <label>
          IP{{ index + 1 }} <input type="text" v-model="ips[index]" />
        </label>
      </div>
    </div>
    <button class="start-button" @click="saveIPs">保存</button>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

const ips = reactive(Array(7).fill(''));
function saveIPs() {
  console.log('Saved IPs:', ips);
  localStorage.setItem('savedIPs', JSON.stringify(ips));
  router.push({
    path: '/second',
    query: { ips: JSON.stringify(ips) },
  });
}

const loadIPs = () => {
  const savedIPs = JSON.parse(localStorage.getItem('savedIPs'));
  if (savedIPs) {
    savedIPs.forEach((ip, index) => {
      if (ips[index] !== undefined) {
        ips[index] = ip;
      }
    });
  }
};
loadIPs();
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: auto;
  border: 2px solid #0000FF;
  padding: 20px;
  text-align: center;
}

.header {
  font-size: 24px;
  margin-bottom: 20px;
}

.ip-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 20px;
}

.ip-row {
  flex-basis: 100%;
  display: flex;
  justify-content: space-around;
  margin-bottom: 10px;
}

.ip-row label {
  display: flex;
  flex-direction: column;
}

.start-button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>
