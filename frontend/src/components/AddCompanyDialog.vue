<template>
  <el-dialog title="添加租户管理" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="租户名称" :required="true" class="form-item">
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
            <div class="upload-trigger">
              <div v-if="!form.photoUrl" ><Plus style="width:20px;color: #dcdfe6;"/></div>
              <img v-if="form.photoUrl" :src="form.photoUrl" class="avatar">
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">5MB</span> 格式为 
          <span class="red-bold">png/jpg/jpeg/gif</span> 的文件
        </div>
      </el-form-item>
      
      <el-form-item label="联系人" :required="true" class="form-item">
        <el-input v-model="form.ownername" placeholder="请输入联系人"></el-input>
      </el-form-item>
      <el-form-item label="电话" :required="true" class="form-item">
        <el-input v-model="form.telephone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="管理员" :required="true" class="form-item">
        <el-input v-model="form.adminname" placeholder="请输入管理员"></el-input>
      </el-form-item>
      <el-form-item label="备注" class="form-item">
        <div class="editor-container">
          <!-- <quill-editor v-model="form.note" ref="quillEditor" :options="editorOptions"></quill-editor> -->
          <textarea v-model="form.note" class="el-textarea__inner" placeholder="请输入备注内容"></textarea>
        </div>
        <div class="note-description">备注是富文本格式可以添加媒体资源</div>
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
import { Plus } from '@element-plus/icons-vue'; // 引入图标

export default {
  components: {
    // QuillEditor
    Plus,
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
            console.log(response);
            if (response.error_message === 'success') {
              ElMessage.success('表单提交成功');
              this.internalDialogVisible = false;
              this.$emit('update');
            } else {
              ElMessage.error(response.error_message);
            }
            this.form.companyname = "";
            this.form.photoUrl = "";
            this.form.ownername = "";
            this.form.telephone = "";
            this.form.adminname = "";
            this.form.note = "";
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
  margin-top: 10px;
}
.upload-trigger {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}


.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}


.upload-description {
  margin-top: 10px;
  font-size: 12px;
  color: #888;
  white-space: pre-line; 
}
.red-bold {
  color: red;
  font-weight: bold;
}
.note-description {
  margin-top: 10px;
  font-size: 12px;
  color: #888;
}
.ql-container.ql-snow {
  border: none;
}
.ql-editor::before {
  content: normal !important;
}
</style>
