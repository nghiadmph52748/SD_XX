<template>
  <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên màu sắc</label>
        <input type="text" v-model="newMauSac.tenMauSac" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newMauSac.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newMauSac.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã màu sắc</label>
        <input type="text" v-model="selectedMauSac.maMauSac" disabled /><br>
        <label for="">Tên màu sắc</label>
        <input type="text" v-model="selectedMauSac.tenMauSac" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedMauSac.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedMauSac.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã màu sắc</th>
        <th>Tên màu sắc</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in MauSacs" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maMauSac }}</td>
        <td>{{ value.tenMauSac }}</td>
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
import { fetchAllMauSac, fetchCreateMauSac, fetchUpdateMauSac, fetchUpdateStatusMauSac } from '../../services/ThuocTinh/MauSacService';

const MauSacs = ref([]);
const newMauSac = ref({});
const selectedMauSac = ref({});

const fetchMauSacs = async () => {
  try {
    const res = await fetchAllMauSac();
    MauSacs.value = res.data;
  } catch (error) {
    console.error('Error fetching colors:', error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateMauSac(newMauSac.value);
    await fetchMauSacs();
  } catch (error) {
    console.error('Error creating color:', error);
  }
};
const fetchDetail = (mauSac) => {
  selectedMauSac.value = { ...mauSac };
};
const fetchEdit = async () => {
  try {
    await fetchUpdateMauSac(selectedMauSac.value.id, selectedMauSac.value);
    await fetchMauSacs();
  } catch (error) {
    console.error('Error updating color:', error);
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusMauSac(id);
    await fetchMauSacs();
  } catch (error) {
    console.error('Error deleting color:', error);
  }
};
onMounted(fetchMauSacs);
</script>
<style scoped>

</style>