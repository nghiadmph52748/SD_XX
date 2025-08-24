<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên đệm giày</label>
        <input type="text" v-model="newDemGiay.tenDemGiay" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newDemGiay.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newDemGiay.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã đệm giày</label>
        <input type="text" v-model="selectedDemGiay.maDemGiay" disabled /><br>
        <label for="">Tên đệm giày</label>
        <input type="text" v-model="selectedDemGiay.tenDemGiay" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedDemGiay.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedDemGiay.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã đệm giày</th>
        <th>Tên đệm giày</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in DemGiays" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maDemGiay }}</td>
        <td>{{ value.tenDemGiay }}</td>
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
import { fetchAllDemGiay, fetchCreateDemGiay, fetchUpdateDemGiay, fetchUpdateStatusDemGiay } from '../../services/ThuocTinh/DemGiayService';

const DemGiays = ref([]);
const newDemGiay = ref({});
const selectedDemGiay = ref({});
const fetchAll = async () => {
  try {
    const res = await fetchAllDemGiay();
    DemGiays.value = res.data;
  } catch (error) {
    console.error("Error fetching insoles:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateDemGiay(newDemGiay.value);
    await fetchAll();
  } catch (error) {
    console.error("Error creating insole:", error);
  }
};
const fetchDetail = (demGiay) => {
  selectedDemGiay.value = { ...demGiay };
};
const fetchEdit = async () => {
  try {
    await fetchUpdateDemGiay(selectedDemGiay.value.id, selectedDemGiay.value);
    await fetchAll();
  } catch (error) {
    console.error("Error updating insole:", error);
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusDemGiay(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting insole:", error);
  }
};
onMounted(fetchAll);
</script>

<style scoped>

</style>