<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên kích thước</label>
        <input type="text" v-model="newKichThuoc.tenKichThuoc" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newKichThuoc.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newKichThuoc.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã kích thước</label>
        <input type="text" v-model="selectedKichThuoc.maKichThuoc" disabled /><br>
        <label for="">Tên kích thước</label>
        <input type="text" v-model="selectedKichThuoc.tenKichThuoc" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedKichThuoc.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedKichThuoc.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã kích thước</th>
        <th>Tên kích thước</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in KichThuocs" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maKichThuoc }}</td>
        <td>{{ value.tenKichThuoc }}</td>
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
import { fetchAllKichThuoc, fetchCreateKichThuoc, fetchUpdateKichThuoc, fetchUpdateStatusKichThuoc } from '../../services/ThuocTinh/KichThuocService';

const KichThuocs = ref([]);
const newKichThuoc = ref({});
const selectedKichThuoc = ref({});

const fetchAll = async () => {
    try {
        const res = await fetchAllKichThuoc();
        KichThuocs.value = res.data;
    } catch (error) {
        console.error('Error fetching sizes:', error);
    }
}
const fetchCreate = async () => {
    try {
        await fetchCreateKichThuoc(newKichThuoc.value);
        await fetchAll();
    } catch (error) {
        console.error('Error creating size:', error);
    }
}
const fetchDetail = (kichThuoc) => {
    selectedKichThuoc.value = { ...kichThuoc };
}
const fetchEdit = async () => {
    try {
        await fetchUpdateKichThuoc(selectedKichThuoc.value.id, selectedKichThuoc.value);
        await fetchAll();
    } catch (error) {
        console.error('Error updating size:', error);
    }
}
const fetchDelete = async (id) => {
    try {
        await fetchUpdateStatusKichThuoc(id);
        await fetchAll();
    } catch (error) {
        console.error('Error deleting size:', error);
    }
}
onMounted(fetchAll);
</script>

<style scoped></style>
