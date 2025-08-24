<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên trọng lượng</label>
        <input type="text" v-model="newTrongLuong.tenTrongLuong" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newTrongLuong.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newTrongLuong.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã trọng lượng</label>
        <input type="text" v-model="selectedTrongLuong.maTrongLuong" disabled /><br>
        <label for="">Tên trọng lượng</label>
        <input type="text" v-model="selectedTrongLuong.tenTrongLuong" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedTrongLuong.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedTrongLuong.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã trọng lượng</th>
        <th>Tên trọng lượng</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in TrongLuongs" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maTrongLuong }}</td>
        <td>{{ value.tenTrongLuong }}</td>
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
import { fetchAllTrongLuong, fetchCreateTrongLuong, fetchUpdateStatusTrongLuong, fetchUpdateTrongLuong } from '../../services/ThuocTinh/TrongLuongService';

const TrongLuongs = ref([]);
const newTrongLuong = ref({});
const selectedTrongLuong = ref({});
const fetchAll = async () => {
  try {
    const res = await fetchAllTrongLuong();
    TrongLuongs.value = res.data;
  } catch (error) {
    console.error("Error fetching weights:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateTrongLuong(newTrongLuong.value);
    await fetchAll();
    newTrongLuong.value = {};
  } catch (error) {
    console.error("Error creating weight:", error);
  }
};
const fetchDetail = (item) => {
  selectedTrongLuong.value = { ...item };
};
const fetchEdit = async () => {
  try {
    await fetchUpdateTrongLuong(selectedTrongLuong.value.id, selectedTrongLuong.value);
    await fetchAll();
    selectedTrongLuong.value = {};
  } catch (error) {
    console.error("Error updating weight:", error);
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusTrongLuong(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting weight:", error);
  }
};
onMounted(fetchAll);
</script>

<style scoped>

</style>