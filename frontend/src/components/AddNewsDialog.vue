<template>
  <el-dialog title="添加资讯管理" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="新闻标题" :required="true" class="form-item">
        <el-input v-model="form.title" placeholder="请输入新闻标题"></el-input>
      </el-form-item>
      <el-form-item label="新闻图片路径" class="form-item">
        <el-upload
          class="avatar-uploader"
          action="https://app6457.acapp.acwing.com.cn/api/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.imagePath" ><Plus style="width:20px;color: #dcdfe6;"/></div>
              <img v-if="form.imagePath" :src="form.imagePath" class="avatar">
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">5MB</span> 格式为 
          <span class="red-bold">png/jpg/jpeg/gif</span> 的文件
        </div>
      </el-form-item>
      
		<el-form-item label="新闻内容" class="form-item">
		<quill-editor
		ref="myQuillEditor"
		v-model="form.content"
		:options="editorOption"
		@blur="onEditorBlur($event)"
		@focus="onEditorFocus($event)"
		@ready="onEditorReady($event)"
		/>
		</el-form-item>
      <el-form-item label="作者" :required="true" class="form-item">
        <el-input v-model="form.author" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item label="新闻简介" :required="true" class="form-item">
        <el-input v-model="form.summary" placeholder="请输入新闻简介"></el-input>
      </el-form-item>
      <el-form-item v-if="userType === 'user' && store.state.user.role === '超级管理员'" label="选择租户" :required="true" class="form-item">
        <el-select v-model="form.tenant" placeholder="请选择租户">
          <el-option
            v-for="tenant in tenantchoose"
            :key="tenant.companyId"
            :label="tenant.companyName"
            :value="tenant.companyName">
          </el-option>
        </el-select>
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
import { ref, onMounted } from 'vue';
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
        title: '',
		summary: '',
        imagePath: '', // 用于存储上传后的图片URL
		content:'',
        author: '',
        tenant: ''
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
  setup() {
    const store = useStore();
    const tenantchoose = ref([]);
    const userType = localStorage.getItem("userType");

    const fetchTenants = () => {
      $.ajax({
        url: 'https://app6457.acapp.acwing.com.cn/api/company/list/',
        type: 'GET',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          tenantchoose.value = resp.companys;
        }
      });
    };

    onMounted(() => {
      fetchTenants();
    });

    return {
      tenantchoose,
      userType
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

      axios.post('https://app6457.acapp.acwing.com.cn/api/upload', formData)
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
      this.form.imagePath = response.url; // 假设后端返回的图片 URL 在 response.url 中
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
      if(this.userType === 'user' && this.store.state.user.role === '租户管理员') this.form.tenant = this.store.state.user.companyname;
      if(this.userType === 'company') this.form.tenant = this.store.state.user.username;
      if (this.form.title && this.form.author && this.form.summary && this.form.tenant) {
        this.form.content = this.$refs.myQuillEditor.quill.root.innerHTML;
        $.ajax({
          url: 'https://app6457.acapp.acwing.com.cn/api/news/add/', // 后端添加资讯信息的接口
          type: 'POST',
          data: {
            title: this.form.title,
            photo: this.form.imagePath, // 使用上传后的图片 URL
            author: this.form.author,
            summary: this.form.summary,
            tenant: this.form.tenant,
            content: this.form.content
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
      this.form.title= '';
      this.form.summary= '';
      this.form.imagePath= ''; // 用于存储上传后的图片URL
      this.form.content='';
      this.form.author= '';
      this.form.tenant= '';
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
.content-description {
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
