<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên môn thể thao</label>
        <input type="text" v-model="newMonTheThao.tenMonTheThao" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newMonTheThao.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newMonTheThao.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã môn thể thao</label>
        <input type="text" v-model="selectedMonTheThao.maMonTheThao" disabled /><br>
        <label for="">Tên môn thể thao</label>
        <input type="text" v-model="selectedMonTheThao.tenMonTheThao" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedMonTheThao.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedMonTheThao.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã môn thể thao</th>
        <th>Tên môn thể thao</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in MonTheThaos" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maMonTheThao }}</td>
        <td>{{ value.tenMonTheThao }}</td>
        <td>{{ value.deleted ? "Không hoạt động" : "Hoạt động" }}</td>
        <td>
          <button v-on:click="fetchDetail(value)">Detail</button> |
          <button v-on:click="fetchDelete(value.id)">Delete</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { fetchAllMonTheThao, fetchCreateMonTheThao, fetchUpdateMonTheThao, fetchUpdateStatusMonTheThao } from '../../services/ThuocTinh/MonTheThaoService';
const MonTheThaos = ref([]);
const newMonTheThao = ref({});
const selectedMonTheThao = ref({});
const fetchAll = async () => {
  try {
    const res = await fetchAllMonTheThao();
    MonTheThaos.value = res.data;
  } catch (error) {
    console.error("Error fetching sports:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateMonTheThao(newMonTheThao.value);
    await fetchAll();
  } catch (error) {
    console.error("Error creating sport:", error);    
  }
};
const fetchDetail = (item) => {
  selectedMonTheThao.value = { ...item };
};
const fetchEdit = async () => {
  try {
    await fetchUpdateMonTheThao(selectedMonTheThao.value.id, selectedMonTheThao.value);
    await fetchAll();
  } catch (error) {
    console.error("Error updating sport:", error);
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusMonTheThao(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting sport:", error);
  }
};
onMounted(fetchAll);
</script>

<style scoped>

</style>