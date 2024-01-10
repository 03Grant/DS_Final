<template>
  <div class="container">
    <div v-for="(server, index) in servers" :key="index">
      <button @click="toggleServer(index)">
        {{ server.status ? `关闭${server.name}` : `打开${server.name}` }}
      </button>
    </div>
    
    <div class="input-group">
      <input v-model="author" placeholder="Enter author name" required>
      <input v-model="startYear" placeholder="Enter start year" type="number">
      <input v-model="endYear" placeholder="Enter end year" type="number">
      <button @click="fetchPaperCount('global')" :disabled="!author">全局扫描查询</button>
      <button @click="fetchPaperCount('local')" :disabled="!author">哈希索引查询</button>
    </div>
    
    <div v-if="loading">Loading...</div>
    <div v-if="error">{{ error }}</div>
    <div v-if="queryResult  !== null">
      <div>查询结果: {{ queryResult }}</div>
      <div>查询耗时: {{ queryTime }}s</div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { reactive, computed } from 'vue';
export default {
  setup() {
    const serverStatus = ref(false);
    const author = ref('');
    const startYear = ref('');
    const endYear = ref('');
    const queryResult = ref();
    const queryTime = ref();
    const loading = ref(false);
    const error = ref(null);
    let totalTime = ref();
    const serverData = ref([]);

    const total = ref(0);


    // 累加数据
    const accumulateData = () => {
      const addedBlocknums = reactive(new Set());
        serverData.value.forEach(server => {
            server.forEach(data => {
                let blocknum = data.blocknum;
                let num = data.num;
                if (num !== -1 && num !== 0 && !addedBlocknums.has(blocknum)) {
                    total.value += num;
                    console.log(total.value)
                    addedBlocknums.add(blocknum);
                }
            });
        });
        queryResult.value = result.value
        serverData.value = [];
    };

    // 计算属性，获取最终结果
    const result = computed(() => total.value);

    // 当组件加载时，执行数据累加
    accumulateData();

    const servers = ref([
      { name: 'DS1', ip: '8.136.125.35', status: true },
      { name: 'DS2', ip: '8.136.123.0', status: true },
      { name: 'DS3', ip: '8.136.118.46', status: true },
      { name: 'DS4', ip: '43.142.91.204', status: true },
      { name: 'DS5', ip: '47.99.166.45', status: true },
      { name: 'DS6', ip: '123.206.121.97', status: true },
      { name: 'DS7', ip: '47.97.117.9', status: true }
    ]);
    const toggleServer = async (index) => {
      const server = servers.value[index];
      server.status = !server.status;
      const serverAction = server.status ? 'on' : 'off';
      await fetch(`http://${server.ip}:2054/api/server/${serverAction}`);
    };

    const fetchPaperCount = async (type) => {
      if (!author.value) {
        error.value = "Author name is required";
        return;
      }
      if (servers.value.every(server => !server.status)) {
        error.value = "请至少打开一个服务器";
        return;
      }
      loading.value = true;
      error.value = null;

      try {
        const startTime = Date.now(); // 记录起始时间
        const promises = servers.value.filter(server => server.status).map(server => {
          let apiUrl = '';
          if (type === 'global') {
            apiUrl = `http://${server.ip}:2054/final1/directscan`;
          } else if (type === 'local') {
            apiUrl = `http://${server.ip}:2054/final1/hashscan`;
          }
          return fetch(`${apiUrl}?author=${encodeURIComponent(author.value)}&beginyear=${startYear.value}&endyear=${endYear.value}`);
        });

        const responses = await Promise.all(promises);
        for (const response of responses) {
          if (response.ok) {
            const data = await response.json();
            console.log(data)
            serverData.value.push(data)
          } 
        }
        const endTime = Date.now(); // 记录结束时间
        totalTime = (endTime - startTime)/1000; // 计算总耗时
        queryTime.value = totalTime;
      } catch (err) {
        error.value = err.message;
      } finally {
        loading.value = false;
      }
      console.log(serverData)

      accumulateData();
    };

    return { servers,serverStatus, author, startYear, endYear, fetchPaperCount, queryTime ,queryResult, loading, error, toggleServer };
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
}

.input-group {
  display: flex;
  flex-direction: column;
  margin: 10px 0;
}

input {
  margin: 5px 0;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 250px;
}

button {
  margin: 10px 0;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0056b3;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
