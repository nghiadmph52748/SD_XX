<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên chống nước</label>
        <input type="text" v-model="newChongNuoc.tenChongNuoc" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newChongNuoc.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newChongNuoc.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã chống nước</label>
        <input type="text" v-model="selectedChongNuoc.maChongNuoc" disabled /><br>
        <label for="">Tên chống nước</label>
        <input type="text" v-model="selectedChongNuoc.tenChongNuoc" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedChongNuoc.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedChongNuoc.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã chống nước</th>
        <th>Tên chống nước</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in ChongNuocs" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maChongNuoc }}</td>
        <td>{{ value.tenChongNuoc }}</td>
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
import { fetchAllChongNuoc, fetchCreateChongNuoc, fetchUpdateChongNuoc, fetchUpdateStatusChongNuoc } from '../../services/ThuocTinh/ChongNuocService';
const ChongNuocs = ref([]);
const newChongNuoc = ref({});
const selectedChongNuoc = ref({});
const fetchAll = async () => {
  try {
    const res = await fetchAllChongNuoc();
    ChongNuocs.value = res.data;
  } catch (error) {
    console.error("Error fetching purchase types:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateChongNuoc(newChongNuoc.value);
    await fetchAll();
  } catch (error) {
    console.error("Error creating purchase type:", error);
  }
};
const fetchEdit = async () => {
  try {
    await fetchUpdateChongNuoc(selectedChongNuoc.value.id, selectedChongNuoc.value);
    await fetchAll();
  } catch (error) {
    console.error("Error updating purchase type:", error);
  }
};
const fetchDetail = (value) => {
  selectedChongNuoc.value = { ...value };
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusChongNuoc(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting purchase type:", error);
  }
};
onMounted(fetchAll);
</script>

<style scoped>

</style>