<template>
  <div class="container">
    <div class="header">页面三-心跳检测</div>
    <div class="button-grid">
      <div v-for="(member, index) in teamMembers" :key="member.id" class="team-member">
        <button
          :disabled="member.disabled || (isAnyMemberSelected && !member.selected)"
          :class="{ 'is-selected': member.selected }"
          @click="selectMember(index)">
          {{ member.label }}
        </button>
      </div>
    </div>
    <button class="confirm-button" @click="confirmSelection">确认</button>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const showModal = ref(false);
const selectedIndex = ref(null);
const isAnyMemberSelected = ref(false);

const ips = ref([]);

onMounted(() => {
  const ipsFromQuery = JSON.parse(router.currentRoute.value.query.ips || '[]');
  ips.value = ipsFromQuery;
});

const teamMembers = reactive([
  { id: 'DS1', label: 'DS1', selected: false , disabled: false},
  { id: 'DS2', label: 'DS2', selected: false , disabled: false},
  { id: 'DS3', label: 'DS3', selected: false , disabled: false},
  { id: 'DS4', label: 'DS4', selected: false , disabled: false},
  { id: 'DS5', label: 'DS5', selected: false , disabled: false},
  { id: 'DS6', label: 'DS6', selected: false , disabled: false},
  { id: 'DS7', label: 'DS7', selected: false , disabled: false}
]);


watch(ips, (newIps) => {
  teamMembers.forEach((member, index) => {
    member.disabled = !newIps[index] || newIps[index] === "";
  });
}, { immediate: true }); // 确保立即执行

function selectMember(index) {
  if (!isAnyMemberSelected.value) {
    showModal.value = true;
    selectedIndex.value = index;
  }
}


async function confirmSelection() {
  const pause = (duration) => new Promise(resolve => setTimeout(resolve, duration));
  for (let i = 0; i < teamMembers.length; i++) {
    if (i !== selectedIndex.value) {
      // Exclude the selected index
      await sendMessageToMembers(ips.value[i]);
      await pause(930);
    }
  }
  router.push({
    path: '/fourth',
    query: { ips: JSON.stringify(ips.value) },
  });
}

async function sendMessageToMembers(ip) {
  try {
    const nonEmptyIpsCount = ips.value.filter(ip => ip !== "").length;
    if(ip!=""){
      const url = `http://${ip}:8001/start-heartbeat?serverNum=${nonEmptyIpsCount}`;
    axios.get(url, {
        headers: {
          'Content-Type': 'text/plain',
        },
      })
        .then(response => {
          console.log(response.data);
        })
        .catch(error => {
          console.error('Error:', error);
        });
      }
  } catch (error) {
    console.error('Error sending message:', error);
  }
}

</script>

<style scoped>
/* Add your CSS styling here */
.button-grid {
display: grid;
grid-template-columns: repeat(4, 1fr);
gap: 10px;
}

.team-member button {
padding: 10px;
border: 1px solid blue;
}

.team-member button.is-selected {
background-color: red;
}

.confirm-button {
margin-top: 20px;
}

.modal {
position: fixed;
top: 50%;
left: 50%;
transform: translate(-50%, -50%);
border: 1px solid #000;
padding: 20px;
background-color: #fff;
z-index: 100;
}

/* Add additional styling as needed */
</style>
