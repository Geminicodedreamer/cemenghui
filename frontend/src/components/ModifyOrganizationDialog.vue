<template>
  <el-dialog title="修改部门" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="上级部门" :required="true" class="form-item">
        <el-input v-model="form.uporganization" placeholder="请输入上级部门名称"></el-input>
      </el-form-item>
      <el-form-item label="部门名称" :required="true" class="form-item">
        <el-input v-model="form.organizationname" placeholder="请输入部门名称"></el-input>
      </el-form-item>
      <el-form-item label="负责人" class="form-item">
        <el-input v-model="form.charger" placeholder="请输入负责人"></el-input>
      </el-form-item>
      <el-form-item label="联系电话" class="form-item">
        <el-input v-model="form.telephone" placeholder="请输入联系电话"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" class="form-item">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="部门状态" class="form-item">
        <el-radio-group v-model="form.status">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="停用">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item class="button-container">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
  props: {
    dialogVisible: {
      type: Boolean,
      required: true
    },
    organization: {
      type: Object,
      default: null
    }
  },
  data() {
    const store = useStore();
    return {
      store,
      internalDialogVisible: this.dialogVisible,
      form: {
        uporganization: '',
        organizationname: '',
        id: 1,
        charger: '',
        telephone: '',
        email: '',
        status: '正常'
      }
    };
  },
  watch: {
    dialogVisible(val) {
      this.internalDialogVisible = val;
    },
    internalDialogVisible(val) {
      this.$emit('update:dialogVisible', val);
    },
    organization: {
      handler(newVal) {
        if (newVal) {
          this.form = { ...newVal };
        } else {
          this.resetForm();
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    submitForm() {
      if (this.form.organizationname && this.form.id && this.form.uporganization && this.form.telephone && this.form.charger && this.form.email && this.form.status) {
        $.ajax({
          url: 'http://127.0.0.1:3000/organization/modify', 
          type: 'POST',
          data: {
            id: this.form.id,
            organizationname: this.form.organizationname,
            telephone: this.form.telephone,
            charger: this.form.charger,
            email: this.form.email,
            uporganization: this.form.uporganization,
            status: this.form.status
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
          success: (response) => {
            console.log('hhh');
            if (response.error_message === 'success') {
              ElMessage.success('表单提交成功');
              this.internalDialogVisible = false;
              this.$emit('update');
              this.resetForm();
            } else {
              ElMessage.error(response.error_message);
            }
          },
          error: (error) => {
            console.error(error);
            ElMessage.error('提交失败');
          }
        });
      } else {
        ElMessage.error('请完整填写表单');
      }
    },
    resetForm() {
      this.form = {
        uporganization: '',
        organizationname: '',
        charger: '',
        telephone: '',
        email: '',
        status: '正常'
      };
      this.internalDialogVisible = false;
    }
  }
};
</script>

<style scoped>
.form-item {
  margin-bottom: 20px;
}
.button-container {
  text-align: right;
}
</style>
