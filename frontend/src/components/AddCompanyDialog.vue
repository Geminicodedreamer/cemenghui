<template>
  <el-dialog title="添加租户管理" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="租户名称" class="form-item">
        <el-input v-model="form.companyname" placeholder="请输入租户名称"></el-input>
      </el-form-item>
      <el-form-item label="租户图标" class="form-item">
        <el-upload
          class="avatar-uploader"
          action="http://127.0.0.1:3000/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <template #trigger>
            <el-button icon="el-icon-upload2">点击上传</el-button>
          </template>
          <img v-if="form.photoUrl" :src="form.photoUrl" class="avatar">
        </el-upload>
        <div>建议尺寸750px*300px，支持jpg/png/jpeg/gif格式</div>
      </el-form-item>
      <el-form-item label="联系人" class="form-item">
        <el-input v-model="form.ownername" placeholder="请输入联系人"></el-input>
      </el-form-item>
      <el-form-item label="电话" class="form-item">
        <el-input v-model="form.telephone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="管理员" class="form-item">
        <el-input v-model="form.adminname" placeholder="请输入管理员"></el-input>
      </el-form-item>
      <el-form-item label="备注" class="form-item">
        <div class="editor-container">
          <!-- <quill-editor v-model="form.note" ref="quillEditor" :options="editorOptions"></quill-editor> -->
        </div>
      </el-form-item>
      <el-form-item class="button-container">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import $ from 'jquery';
// import { QuillEditor } from 'vue3-quill';

export default {
  components: {
    // QuillEditor
  },
  props: {
    dialogVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    const store = useStore();
    return {
      internalDialogVisible: this.dialogVisible,
      form: {
        companyname: '',
        photoUrl: '', // 用于存储上传后的图片URL
        ownername: '',
        telephone: '',
        adminname: '',
        note: ''
      },
      store
    };
  },
  watch: {
    dialogVisible(val) {
      this.internalDialogVisible = val;
    },
    internalDialogVisible(val) {
      this.$emit('update:dialogVisible', val);
    }
  },
  methods: {
    handleAvatarSuccess(response) {
      this.form.photoUrl = response.url; // 假设后端返回的图片 URL 在 response.url 中
    },
    beforeAvatarUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        ElMessage.error('上传图片只能是 JPG/PNG/GIF 格式!');
      }
      if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过 2MB!');
      }
      return isImage && isLt2M;
    },
    submitForm() {
      if (this.form.companyname && this.form.ownername && this.form.telephone && this.form.adminname) {

        $.ajax({
          url: 'http://127.0.0.1:3000/company/add/', // 后端添加公司信息的接口
          type: 'POST',
          data: {
            companyname: this.form.companyname,
            photo: this.form.photoUrl, // 使用上传后的图片 URL
            ownername: this.form.ownername,
            telephone: this.form.telephone,
            adminname: this.form.adminname,
            note: this.form.note
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
          success: (response) => {
            if (response.error_message === 'success') {
              ElMessage.success('表单提交成功');
              this.internalDialogVisible = false;
              this.$emit('refresh');
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
.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
}
.button-container {
  text-align: right;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 50%;
}
.avatar-uploader-icon {
  font-size: 28px;
  display: block;
  line-height: 100px;
  text-align: center;
}
.ql-container.ql-snow {
  border: none;
}
.ql-editor::before {
  content: normal !important;
}
</style>
