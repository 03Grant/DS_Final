<template>
<div class="container">
  <div class="header">状态控制</div>
  <div class="status-display">
  <h2>丢包率：</h2>
  <input type="number" v-model="lossRate" placeholder="输入丢包率" />
  <button @click="handleSetLossRate">确认</button>
  <button @click="handleRestart">Restart</button>
  </div>
  <div class="sidebar">
    <div v-for="(member, index) in members" :key="member.id" class="member">
        <div class="member-info">
            <div class="server-icon">🖥️ {{ member.id }}</div>
            <div class="actions">
                <button @click="handleJoin(member,index)" :disabled="!member.canJoin">Join</button>
                <button @click="handleLeave(member, index)" :disabled="!member.canLeave">Leave</button>
                <button @click="handleOn(member,index)" :disabled="!member.isOn">On</button>
                <button @click="handleOff(member,index)" :disabled="!member.canOff">Off</button>
                <button @click="handleLogs(index)">Logs</button>
            </div>
        </div>
    </div>
    <pre>{{ logs }}</pre>
</div>

  <div class="status-display2">
    <div v-for="member in members" :key="member.id" class="status-info">
      <!-- 显示从后端获取的JSON信息 -->
      <pre>{{ member.jsonData }}</pre>
    </div>
  </div>
</div>
</template>

<script setup>
import { reactive,ref,onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios'

const router = useRouter();
const ips = ref([]);

const lossRate = ref();

onMounted(() => {
  const ipsFromQuery = JSON.parse(router.currentRoute.value.query.ips || '[]');
  ips.value = ipsFromQuery;
});

const members = reactive([
  { id: 'DS1', canJoin: false, canLeave: true, isOn: false, canOff: true },
  { id: 'DS2', canJoin: false, canLeave: true, isOn: false, canOff: true },
  { id: 'DS3', canJoin: false, canLeave: true, isOn: false, canOff: true },
  { id: 'DS4', canJoin: false, canLeave: true, isOn: false, canOff: true },
  { id: 'DS5', canJoin: false, canLeave: true, isOn: false, canOff: true },
  { id: 'DS6', canJoin: false, canLeave: true, isOn: false, canOff: true },
  { id: 'DS7', canJoin: false, canLeave: true, isOn: false, canOff: true },
]);

const nonEmptyIpsCount = () => ips.value.filter(ip => ip !== "").length;

const textData = '8.136.123.0';
async function postRequest(url) {
  try {
    const response = axios.post(url, textData, {
        headers: {
          'Content-Type': 'text/plain',
        },
      })
    console.log(response.data);
  } catch (error) {
    console.error('Error sending request:', error);
  }
}

async function sendRequest(url) {
  try {
    const response = await axios.get(url);
    console.log(response.data);
  } catch (error) {
    console.error('Error sending request:', error);
  }
}

function handleRestart() {
  ips.value.forEach(ip => {
    if(ip!=""){
    postRequest(`http://${ip}:8001/restart`); 
}});
router.push({
    path: '/first',
  });
}

// 处理丢包率设置
async function handleSetLossRate() {
  ips.value.forEach(ip => {
  const url = `http://${ip}:8001/config/loss-rate`; // 使用实际的 IP 地址
  try {
    axios.post(url, JSON.stringify(lossRate.value), {
        headers: {
          'Content-Type': 'application/json'
        }
      });
    console.log('丢包率已发送:', lossRate.value);
  } catch (error) {
    console.error('发送丢包率时发生错误:', error);
  }
}
  )
}



function handleOn(member, index) {
  member.isOn = false;
  member.canOff = true;
  member.canJoin = true;

  const ip = ips.value[index];
  if (ip) {
    postRequest(`http://${ip}:8001/config/poweron`);
    sendRequest(`http://${ip}:8001/start-heartbeat?serverNum=${nonEmptyIpsCount()}`);
  }
}

function handleOff(member, index) {
  member.isOn = true;
  member.canOff = false;
  member.canJoin = false;
  member.canLeave = false;

  const ip = ips.value[index];
  if (ip) {
    sendRequest(`http://${ip}:8001/final2/crush`);
  }
}

function handleLeave(member, index) {
  member.canJoin = true;
  member.canLeave = false;

  const ip = ips.value[index];
  if (ip) {
    sendRequest(`http://${ip}:8001/final2/leave-group`);
  }
}

function handleJoin(member, index) {
  member.canJoin = false;
  member.canLeave = true;

  const ip = ips.value[index];
  if (ip) {
    postRequest(`http://${ip}:8001/config/poweron`);
    // sendRequest(`http://${ip}:8001/start-heartbeat?serverNum=${nonEmptyIpsCount()}`);
  }
}
const logs = ref('');

function handleLogs(index) {
  const ip = ips.value[index];

  fetch(`http://${ip}:8001/config/logs/server_logs`)
    .then(response => response.text()) // 将响应内容转换为文本
    .then(data => {
      logs.value = data; // 将获取到的日志信息保存到 logs 变量中
    })
    .catch(error => {
      console.error('获取日志失败:', error);
    });

    showLogsInNewWindow()
}

function showLogsInNewWindow() {
      const newWindow = window.open('', '_blank');
      newWindow.document.write(`<pre>${logs.value}</pre>`);
    }
</script>

<style scoped>
.container {
display: flex;
flex-direction: row;
justify-content: space-between;
}

.header {
text-align: center;
font-size: 2em;
margin-bottom: 1em;
}

.status-display, .status-display2 {
flex: 1;
margin: 1em;
}

.sidebar {
flex: 1;
margin: 1em;
}

.member {
display: flex;
flex-direction: column;
align-items: center;
margin-bottom: 1em;
}

.member-info {
display: flex;
flex-direction: column;
align-items: center;
}

.server-icon {
font-size: 1.5em;
}

.actions {
display: flex;
gap: 1em;
margin-top: 1em;
}

button {
padding: 0.5em 1em;
}
</style>
