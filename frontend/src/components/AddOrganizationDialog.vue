<template>
  <el-dialog title="添加部门" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="上级部门" prop="uporganization" class="form-item">
        <el-input v-model="form.uporganization" placeholder="请输入上级部门名称"></el-input>
      </el-form-item>
      <el-form-item label="部门名称" prop="organizationname" class="form-item">
        <el-input v-model="form.organizationname" placeholder="请输入部门名称"></el-input>
      </el-form-item>
      <el-form-item label="负责人" prop="charger" class="form-item">
        <el-input v-model="form.charger" placeholder="请输入负责人"></el-input>
      </el-form-item>
      <el-form-item label="联系电话" prop="telephone" class="form-item">
        <el-input v-model="form.telephone" placeholder="请输入联系电话"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email" class="form-item">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="部门状态" prop="status" class="form-item">
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
    company: {
      type: String,
      default: null,
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
        id: '',
        charger: '',
        telephone: '',
        email: '',
        status: '正常',
        creattime: ''
      },
      rules: {
        uporganization: [{ required: true, message: '请输入上级部门名称', trigger: 'blur' }],
        organizationname: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
        charger: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
        telephone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        status: [{ required: true, message: '请选择部门状态', trigger: 'change' }]
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
    company(val)
    {
      this.form.uporganization = val;
    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.form.creattime = new Date().toISOString().slice(0, 19).replace('T', ' ');
          $.ajax({
            url: 'https://app6457.acapp.acwing.com.cn/api/organization/add',
            type: 'POST',
            data: this.form,
            headers: {
              Authorization: "Bearer " + this.store.state.user.token,
            },
            success: (response) => {
              console.error(response.error_message);
              if (response.error_message === 'success') {
                ElMessage.success('表单提交成功');
                console.log(this.form.uporganization)
                if(this.form.uporganization === "测盟会")
                {
                  $.ajax({
                    url: 'https://app6457.acapp.acwing.com.cn/api/company/add/', // 后端添加公司信息的接口
                    type: 'POST',
                    data: {
                      companyname: this.form.organizationname,
                      ownername: "admin",
                      telephone: "18624430996",
                      adminname: "admin",
                    },
                    headers: {
                      Authorization: "Bearer " + this.store.state.user.token,
                    },
                    success: (response) => {
                      console.log(response);
                    },
                    error: (error) => {
                      console.error(error);
                    }
                  });
                }
                this.internalDialogVisible = false;
                this.$emit('update'); // 触发 update 事件
                this.resetForm();
              } else {
                ElMessage.error(response.error_message);
              }
            },
            error: (error) => {
              console.log(error);
              ElMessage.error('提交失败');
            }
          });
          
          

        } else {
          ElMessage.error('请完整填写表单');
        }
      });
    },
    resetForm() {
      this.$refs.form.resetFields();
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
