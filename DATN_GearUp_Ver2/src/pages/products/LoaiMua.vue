<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên loại mùa</label>
        <input type="text" v-model="newLoaiMua.tenLoaiMua" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newLoaiMua.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newLoaiMua.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã loại mùa</label>
        <input type="text" v-model="selectedLoaiMua.maLoaiMua" disabled /><br>
        <label for="">Tên loại mùa</label>
        <input type="text" v-model="selectedLoaiMua.tenLoaiMua" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedLoaiMua.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedLoaiMua.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã loại mùa</th>
        <th>Tên loại mùa</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in LoaiMuas" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maLoaiMua }}</td>
        <td>{{ value.tenLoaiMua }}</td>
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
import { fetchAllLoaiMua, fetchCreateLoaiMua, fetchUpdateLoaiMua, fetchUpdateStatusLoaiMua } from '../../services/LoaiMuaService';
const LoaiMuas = ref([]);
const newLoaiMua = ref({});
const selectedLoaiMua = ref({});
const fetchAll = async () => {
  try {
    const res = await fetchAllLoaiMua();
    LoaiMuas.value = res.data;
  } catch (error) {
    console.error("Error fetching purchase types:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateLoaiMua(newLoaiMua.value);
    await fetchAll();
  } catch (error) {
    console.error("Error creating purchase type:", error);    
  }
};
const fetchDetail = (item) => {
  selectedLoaiMua.value = { ...item };
};
const fetchEdit = async () => {
  try {
    await fetchUpdateLoaiMua(selectedLoaiMua.value.id, selectedLoaiMua.value);
    await fetchAll();
  } catch (error) {
    console.error("Error updating purchase type:", error);
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusLoaiMua(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting purchase type:", error);
  }
};
onMounted(fetchAll);
</script>

<style scoped>

</style>