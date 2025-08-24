<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên xuất xứ</label>
        <input type="text" v-model="newXuatXu.tenXuatXu" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newXuatXu.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newXuatXu.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã xuất xứ</label>
        <input type="text" v-model="selectedXuatXu.maXuatXu" disabled /><br>
        <label for="">Tên xuất xứ</label>
        <input type="text" v-model="selectedXuatXu.tenXuatXu" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedXuatXu.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedXuatXu.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã xuất xứ</th>
        <th>Tên xuất xứ</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in XuatXus" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maXuatXu }}</td>
        <td>{{ value.tenXuatXu }}</td>
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
import { onMounted, ref } from "vue";
import {
  fetchAllXuatXu,
  fetchCreateXuatXu,
  fetchUpdateStatusXuatXu,
  fetchUpdateXuatXu,
} from "../../services/ThuocTinh/XuatXuService";
const XuatXus = ref([]);
const newXuatXu = ref({});
const selectedXuatXu = ref({});
const fetchXuatXu = async () => {
  try {
    const data = await fetchAllXuatXu();
    XuatXus.value = await data.data;
  } catch (error) {
    console.error("Error fetching origins:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateXuatXu(newXuatXu.value);
    await fetchXuatXu();
  } catch (error) {
    console.error("Error creating origin:", error);
  }
};
const fetchDetail = (xuatxu) => {
  selectedXuatXu.value = { ...xuatxu };
};
const fetchEdit = async () => {
    try {
        await fetchUpdateXuatXu(selectedXuatXu.value.id, selectedXuatXu.value);
        await fetchXuatXu();
    } catch (error) {
        console.error("Error updating origin:", error);
    }
};
const fetchDelete = async (id) => {
    try {
        await fetchUpdateStatusXuatXu(id);
        await fetchXuatXu();
    } catch (error) {
        console.error("Error deleting origin:", error);
    }
};
onMounted(fetchXuatXu);
</script>

<style scoped></style>
