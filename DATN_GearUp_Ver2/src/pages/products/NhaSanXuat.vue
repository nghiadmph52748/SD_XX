<template>
  <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên nhà sản xuất</label>
        <input type="text" v-model="newNSX.tenNhaSanXuat" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newNSX.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newNSX.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã nhà sản xuất</label>
        <input type="text" v-model="selectedNSX.maNhaSanXuat" disabled /><br>
        <label for="">Tên nhà sản xuất</label>
        <input type="text" v-model="selectedNSX.tenNhaSanXuat" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedNSX.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedNSX.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã nhà sản xuất</th>
        <th>Tên nhà sản xuất</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in NSXs" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maNhaSanXuat }}</td>
        <td>{{ value.tenNhaSanXuat }}</td>
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
import { fetchAllNhaSanXuat, fetchCreateNhaSanXuat, fetchUpdateNhaSanXuat, fetchUpdateStatusNhaSanXuat } from '../../services/ThuocTinh/NhaSanXuatService';
const newNSX = ref({});
const selectedNSX = ref({});
const NSXs = ref([]);
const fetchNSX = async () => {
    try {
        const res = await fetchAllNhaSanXuat();
        NSXs.value = res.data;
    } catch (error) {
        console.error('Error fetching manufacturers:', error);
    }
};
const fetchCreate = async () => {
    try {
        await fetchCreateNhaSanXuat(newNSX.value);
        await fetchNSX();
    } catch (error) {
        console.error('Error creating manufacturer:', error);
    }
};
const fetchDetail = (nsx) => {
    selectedNSX.value = { ...nsx };
};
const fetchEdit = async () => {
    try {
        await fetchUpdateNhaSanXuat(selectedNSX.value.id, selectedNSX.value);
        await fetchNSX();
    } catch (error) {
        console.error('Error updating manufacturer:', error);
    }
};
const fetchDelete = async (id) => {
    try {
        await fetchUpdateStatusNhaSanXuat(id);
        await fetchNSX();
    } catch (error) {
        console.error('Error deleting manufacturer:', error);
    }
};
onMounted(fetchNSX);
</script>

<style scoped>

</style>