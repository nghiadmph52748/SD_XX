<template>
  <form v-on:submit="fetchCreate()">
        <h2>Form add</h2>
        <label for="">Tên chất liệu</label>
        <input type="text" v-model="newChatLieu.tenChatLieu" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="newChatLieu.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="newChatLieu.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Add</button>
    </form>
    <form v-on:submit="fetchEdit()">
        <h2>Form update</h2>
        <label for="">Mã chất liệu</label>
        <input type="text" v-model="selectedChatLieu.maChatLieu" disabled /><br>
        <label for="">Tên chất liệu</label>
        <input type="text" v-model="selectedChatLieu.tenChatLieu" /><br>
        <label for="">Trạng thái</label>
        <input type="radio" name="Trạng thái" v-model="selectedChatLieu.deleted" :value="false" checked />Hoạt động
        <input type="radio" name="Trạng thái" v-model="selectedChatLieu.deleted" :value="true" />Không hoạt động <br>
        <button type="submit">Update</button>
    </form>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã chất liệu</th>
        <th>Tên chất liệu</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(value, i) in ChatLieus" :key="i">
        <td>{{ i + 1 }}</td>
        <td>{{ value.maChatLieu }}</td>
        <td>{{ value.tenChatLieu }}</td>
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
import { fetchAllChatLieu, fetchCreateChatLieu, fetchUpdateChatLieu, fetchUpdateStatusChatLieu } from '../../services/ChatLieuService';

const ChatLieus = ref([]);
const newChatLieu = ref({});
const selectedChatLieu = ref({});

const fetchAll = async () => {
  try {
    const res = await fetchAllChatLieu();
    ChatLieus.value = res.data;
  } catch (error) {
    console.error("Error fetching materials:", error);
  }
};
const fetchCreate = async () => {
  try {
    await fetchCreateChatLieu(newChatLieu.value);
    await fetchAll();
  } catch (error) {
    console.error("Error creating material:", error);
  }
};
const fetchDetail = (chatLieu) => {
  selectedChatLieu.value = { ...chatLieu };
};

const fetchEdit = async () => {
  try {
    await fetchUpdateChatLieu(selectedChatLieu.value.id, selectedChatLieu.value);
    await fetchAll();
  } catch (error) {
    console.error("Error updating material:", error);
  }
};
const fetchDelete = async (id) => {
  try {
    await fetchUpdateStatusChatLieu(id);
    await fetchAll();
  } catch (error) {
    console.error("Error deleting material:", error);
  }
};
onMounted(fetchAll);
</script>

<style scoped>

</style>