<!-- DialogForm.vue -->
<template>
  <div>
    <el-dialog
      title="表单"
      :visible="DialogFlag"
      width="45%"
      @close="handleClose"
    >
      <span>子组件弹出框</span>
      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">关 闭</el-button>
          <el-button type="primary" @click="handleClose">确 认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>


<script>
export default {
  name: 'DialogForm',
  props: {
    DialogFlag: {
      type: Boolean,
      default: false
    },
    lackincidentid: {
      type: [String, Number],
      default: ''
    }
  },
  watch: {
    DialogFlag: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          console.log('id', newVal)
        }
      }
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:DialogFlag', false)
    },
  }
}
</script>
