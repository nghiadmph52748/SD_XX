<template>
    <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên đế giày</label>
        <input type="text" v-model="newDeGiay.tenDeGiay" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newDeGiay.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newDeGiay.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã đế giày</label>
        <input type="text" v-model="selectedDeGiay.maDeGiay" disabled /><br>
        <label for="">Tên đế giày</label>
        <input type="text" v-model="selectedDeGiay.tenDeGiay" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedDeGiay.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedDeGiay.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã đế giày</th>
        <th>Tên đế giày</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in DeGiays" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maDeGiay }}</td>
        <td>{{ value.tenDeGiay }}</td>
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
import { fetchAllDeGiay, fetchCreateDeGiay, fetchUpdateDeGiay, fetchUpdateStatusDeGiay } from "../../services/ThuocTinh/DeGiayService";

const DeGiays = ref([]);
const newDeGiay = ref({});
const selectedDeGiay = ref({});

const fetchAll = async () => {
  try {
    const res = await fetchAllDeGiay();
    DeGiays.value = res.data;
  } catch (error) {
    console.error("Error fetching shoe tongues:", error);
  }
};

const fetchCreate = async () => {
  try {
    await fetchCreateDeGiay(newDeGiay.value);
    await fetchAll();
  } catch (error) {
    console.error("Error creating shoe tongue:", error);
  }
};
const fetchDetail = (deGiay) => {
  selectedDeGiay.value = { ...deGiay };
};
const fetchEdit = async () => {
  try {
    await fetchUpdateDeGiay(selectedDeGiay.value.id, selectedDeGiay.value);
    await fetchAll();
  } catch (error) {
    console.error("Error updating shoe tongue:", error);
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusDeGiay(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting shoe tongue:", error);
  }
};
onMounted(fetchAll);
</script>

<style scoped></style>
