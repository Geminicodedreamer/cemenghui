<template>
  <tr>
    <td><input type="checkbox" v-model="selectedOrganizations" :value="organization.id"></td>
    <td :style="{ paddingLeft: `${depth * 20}px` }">{{ organization.organizationname }}</td>
    <td>{{ organization.id }}</td>
    <td>{{ organization.status }}</td>
    <td>{{ organization.creattime }}</td>
    <td>
      <el-button @click="handleModify" type="primary" circle>修改</el-button>
      <el-button @click="handleDelete" type="danger" circle>删除</el-button>
    </td>
  </tr>
  <template v-for="child in organization.children" :key="child.id">
    <TreeRow :organization="child" :depth="depth + 1" @showModifyDialog="handleModify" @confirmDelete="handleDelete" />
  </template>
</template>

<script>
import { ref } from 'vue';

export default {
  emits: ['showModifyDialog', 'confirmDelete'],
  props: {
    organization: {
      type: Object,
      required: true
    },
    depth: {
      type: Number,
      required: true
    },
  },
  setup(props, { emit }) {
    const selectedOrganizations = ref([]);

    const handleModify = () => {
      emit('showModifyDialog', props.organization);
    };

    const handleDelete = () => {
      emit('confirmDelete', props.organization);
    };

    return {
      selectedOrganizations,
      handleModify,
      handleDelete
    };
  }
};
</script>
