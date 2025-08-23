<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên độ bền</label>
        <input type="text" v-model="newDoBen.tenDoBen" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newDoBen.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newDoBen.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã độ bền</label>
        <input type="text" v-model="selectedDoBen.maDoBen" disabled /><br>
        <label for="">Tên độ bền</label>
        <input type="text" v-model="selectedDoBen.tenDoBen" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedDoBen.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedDoBen.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã độ bền</th>
        <th>Tên độ bền</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in DoBens" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maDoBen }}</td>
        <td>{{ value.tenDoBen }}</td>
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
import { fetchAllDoBen, fetchCreateDoBen, fetchUpdateDoBen, fetchUpdateStatusDoBen } from '../../services/DoBenService';
const DoBens = ref([]);
const newDoBen = ref({});
const selectedDoBen = ref({});
const fetchAll = async () => {
  try {
    const res = await fetchAllDoBen();
    DoBens.value = res.data;
  } catch (error) {
    console.error("Error fetching purchase types:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateDoBen(newDoBen.value);
    await fetchAll();
    newDoBen.value = {};
  } catch (error) {
    console.error("Error creating purchase type:", error);
  }
};
const fetchDetail = (value) => {
  selectedDoBen.value = { ...value };
};
const fetchEdit = async () => {
  try {
    await fetchUpdateDoBen(selectedDoBen.value);
    await fetchAll();
  } catch (error) {
    console.error("Error updating purchase type:", error);    
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusDoBen(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting purchase type:", error);    
  }
};
onMounted(fetchAll);
</script>

<style scoped>

</style>