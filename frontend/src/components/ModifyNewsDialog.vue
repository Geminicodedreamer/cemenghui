<template>
  <el-dialog title="修改资讯管理" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="新闻标题" :required="true" class="form-item">
        <el-input v-model="form.title" placeholder="请输入新闻标题"></el-input>
      </el-form-item>
      <el-form-item label="新闻图片" class="form-item">
       <el-upload
          class="avatar-uploader"
          action="http://127.0.0.1:3000/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.imagePath">
                <Plus style="width:20px;color: #dcdfe6;"/>
              </div>
              <div v-if="form.imagePath" class="uploaded-image-container">
                <img :src="form.imagePath" class="avatar" @click="previewImage">
                  <div class="image-actions">
                    <ZoomIn @click.stop="previewImage" class="icon-button"/>
                    <Delete @click.stop="deleteImage" class="icon-button"/>
                  </div>
              </div>
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">5MB</span> 格式为 
          <span class="red-bold">png/jpg/jpeg/gif</span> 的文件
        </div>
      </el-form-item>
      
      <el-form-item label="作者" :required="true" class="form-item">
        <el-input v-model="form.author" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item label="新闻简介" :required="true" class="form-item">
        <el-input v-model="form.summary" placeholder="请输入新闻简介"></el-input>
      </el-form-item>
      <!-- <el-form-item label="管理员" :required="true" class="form-item">
        <div>{{ form.adminname }}</div>
      </el-form-item> -->
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
      <el-form-item class="button-container">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>


     <el-image-viewer
      v-if="imageViewerVisible"
      :url-list="[form.imagePath]"
      :initial-index="0"
      @close="imageViewerVisible = false"
    />

  </el-dialog>
</template>

<script>
import { ElMessage, ElImageViewer } from 'element-plus';
import { Plus, ZoomIn, Delete } from '@element-plus/icons-vue'; 
import axios from 'axios';
import $ from 'jquery';
import { useStore } from 'vuex';


export default {
  components: {
    Plus,
    ZoomIn,
    Delete,
    ElImageViewer,
  },
  props: {
    dialogVisible: {
      type: Boolean,
      required: true
    },
    News: {
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
        newsid: null,
        title: '',
        photo: '', // 用于存储上传后的图片URL
        author: '',
        summary: '',
        // adminname: '',
        content: ''
      },
      editorOption: {
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
      },
      imageViewerVisible: false
    };
  },
  watch: {
    dialogVisible(val) {
      this.internalDialogVisible = val;
    },
    internalDialogVisible(val) {
      this.$emit('update:dialogVisible', val);
    },
    News: {
      handler(newVal) {
        if (newVal) {
			console.log('New News data received:', newVal); // 调试输出
          this.form = { ...newVal }; // 将选中的咨询信息填充到表单中
          console.log(this.form); //
          // 手动设置富文本编辑器内容
          if (this.$refs.myQuillEditor) {
            this.$refs.myQuillEditor.quill.root.innerHTML = newVal.content;
          }
        } else {
          this.resetForm();
        }
      },
      immediate: true
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
    previewImage() {
      this.imageViewerVisible = true;
    },
    deleteImage() {
      this.form.photo = ''; // 清空 photo
    },
    onEditorBlur(quill) {
      console.log('editor blur!', quill);
    },
    onEditorFocus(quill) {
      console.log('editor focus!', quill);
    },
    onEditorReady(quill) {
      console.log('editor ready!', quill);
      if (this.form.content) {
        this.$refs.myQuillEditor.quill.root.innerHTML = this.form.content;
      }
    },
    handleAvatarSuccess(response) {
      this.form.photo = response.url; // 假设后端返回的图片 URL 在 response.url 中
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
      if (this.form.title && this.form.author && this.form.summary) {
        this.form.content = this.$refs.myQuillEditor.quill.root.innerHTML;
		console.log('提交的表单数据:', this.form); // 检查表单数据

        $.ajax({
          url: 'http://127.0.0.1:3000/news/modify', // 后端修改资讯信息的接口
          type: 'POST',
          data: {
            newsid: this.form.newsid,
            title: this.form.title,
            photo: this.form.photo,
            author: this.form.author,
            summary: this.form.summary,
            content: this.form.content
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
          success: (response) => {
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
        newsid: null,
        title: '',
        photo: '',
        author: '',
        summary: '',
        // adminname: '',
        content: ''
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
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
  cursor: pointer;
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

.uploaded-image-container {
  position: relative;
  width: 120px;
  height: 120px;
}

.image-actions {
  display: flex;
  justify-content: space-around;
  position: absolute;
  bottom: 0%;
  left: 0;
  right: 0;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}

.icon-button {
  margin-top: 40%;
  color: #fff;
  width: 20px;
  height: 20px;
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