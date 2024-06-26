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
      <el-form-item label="密码" class="form-item">
        <div>默认密码为租户创建后的租户标识</div>
      </el-form-item>
      <el-form-item label="备注" class="form-item">
          <quill-editor
            ref="myQuillEditor"
            v-model="form.note"
            :options="editorOption"
            @blur="onEditorBlur($event)"
            @focus="onEditorFocus($event)"
            @ready="onEditorReady($event)"
          />
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
import { Plus } from '@element-plus/icons-vue'; // 引入图标
import axios from 'axios';

export default {
  components: {
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
      store,
      editorOption: { // Move the editorOption initialization here
        modules: {
          toolbar: {
            container: [
              // include other toolbar items
              ['bold', 'italic', 'underline', 'strike'],
              ['blockquote', 'code-block'],
              [{ 'header': 1 }, { 'header': 2 }],
              [{ 'list': 'ordered'}, { 'list': 'bullet' }],
              [{ 'script': 'sub'}, { 'script': 'super' }],
              [{ 'indent': '-1'}, { 'indent': '+1' }],
              [{ 'direction': 'rtl' }],
              [{ 'size': ['small', false, 'large', 'huge'] }],
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
              [{ 'color': [] }, { 'background': [] }],
              [{ 'font': [] }],
              [{ 'align': [] }],
              ['image', 'video'], 
              ['clean']
            ],
            handlers: {
              'image': () => { 
                this.handleImageInsertion();
              }
            }
          }
        }
      }
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
  computed: {
    editor() {
      return this.$refs.myQuillEditor.quill;
    }
  },
  methods: {
    uploadImage(file) {
      const formData = new FormData();
      formData.append('file', file);

      axios.post('http://127.0.0.1:3000/upload', formData)
        .then(response => {
          const range = this.$refs.myQuillEditor.quill.getSelection();
          const url = response.data.url; // URL from server response
          this.$refs.myQuillEditor.quill.insertEmbed(range.index, 'image', url);
        })
        .catch(error => {
          console.error('Error uploading image: ', error);
          ElMessage.error('Image upload failed');
        });
    },
    handleImageInsertion() {
      const input = document.createElement('input');
      input.setAttribute('type', 'file');
      input.setAttribute('accept', 'image/png, image/gif, image/jpeg, image/bmp, image/x-icon');
      input.classList.add('ql-image');
      
      input.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
          this.uploadImage(file);
        }
        input.value = '';
      });

      input.click();
    },
    onEditorBlur(quill) {
      console.log('editor blur!', quill);
    },
    onEditorFocus(quill) {
      console.log('editor focus!', quill);
    },
    onEditorReady(quill) {
      console.log('editor ready!', quill);
    },
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
        this.form.note = this.$refs.myQuillEditor.quill.root.innerHTML;
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
