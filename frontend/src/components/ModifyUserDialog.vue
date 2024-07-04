<template>
  <el-dialog title="修改用户信息" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="用户名" class="form-item">
        <el-input v-model="form.username" placeholder="请输入用户名" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="用户昵称" :required="true" class="form-item">
        <el-input v-model="form.nickname" placeholder="请输入用户昵称"></el-input>
      </el-form-item>
      <el-form-item label="手机号" :required="true" class="form-item">
        <el-input v-model="form.telephone" placeholder="请输入手机号"></el-input>
      </el-form-item>
      <el-form-item label="归属部门" class="form-item">
        <el-select v-model="form.apartmentname" placeholder="请选择归属部门">
          <el-option
            v-for="apartment in apartmentchoose"
            :key="apartment.value"
            :label="apartment.label"
            :value="apartment.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="性别" class="form-item">
        <el-select v-model="form.gender" placeholder="请选择性别">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" class="form-item">
        <el-radio-group v-model="form.status">
          <el-radio label="1">正常</el-radio>
          <el-radio label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="电子邮件" class="form-item">
        <el-input v-model="form.email" placeholder="请输入电子邮件"></el-input>
      </el-form-item>
      <el-form-item label="角色" class="form-item">
        <el-select v-model="form.role" placeholder="请选择角色">
          <el-option label="租户管理员" value="租户管理员"></el-option>
          <el-option v-if="store.state.user.role === '超级管理员'" label="超级管理员" value="超级管理员"></el-option>
          <el-option label="普通用户" value="普通用户"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="岗位" class="form-item">
        <el-select v-model="form.post" placeholder="请选择岗位">
          <el-option label="项目经理" value="项目经理"></el-option>
          <el-option label="大堂经理" value="大堂经理"></el-option>
          <el-option label="企业老板" value="企业老板"></el-option>
          <el-option label="普通员工" value="普通员工"></el-option>
          <el-option label="工程师" value="工程师"></el-option>
        </el-select>
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
      <el-form-item label="用户图像" class="form-item">
        <el-upload
          class="avatar-uploader"
          action="https://app6457.acapp.acwing.com.cn/api/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.photo"><Plus style="width:20px;color: #dcdfe6;"/></div>
              <div v-if="form.photo" class="uploaded-image-container">
                <img :src="form.photo" class="avatar" @click="previewImage">
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
      <el-form-item class="button-container">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>

    <el-image-viewer
      v-if="imageViewerVisible"
      :url-list="[form.photo]"
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
    user: {
      type: Object,
      default: null
    },
    apartmentchoose: {
      type: Array,
      required: true
    }
  },
  data() {
    const store = useStore();
    return {
      store,
      internalDialogVisible: this.dialogVisible,
      form: {
        userid: null,
        username: '',
        nickname: '',
        telephone: '',
        apartmentname: '',
        gender: '',
        status: 1,
        email: '',
        role: '',
        note: '',
        post: '',
        photo: ''
      },
      organizationOptions: [],
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
    user: {
      handler(newVal) {
        if (newVal) {
          this.form = { ...newVal };
          this.form.userid = newVal.userId;
          console.log(this.form);
          if (this.$refs.myQuillEditor) {
            this.$refs.myQuillEditor.quill.root.innerHTML = newVal.note;
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
    fetchOrganizations() {
      return $.ajax({
        url: "https://app6457.acapp.acwing.com.cn/api/organization/list",
        type: "get",
        headers: {
          Authorization: "Bearer " + this.store.state.user.token,
        }
      });
    },
    async loadOrganizations() {
      try {
        const response = await this.fetchOrganizations();
        this.organizationOptions = response.organizations;
      } catch (error) {
        console.error("Error fetching organizations:", error);
      }
    },
    uploadImage(file) {
      const formData = new FormData();
      formData.append('file', file);

      axios.post('https://app6457.acapp.acwing.com.cn/api/upload', formData)
        .then(response => {
          const range = this.$refs.myQuillEditor.quill.getSelection();
          const url = response.data.url;
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
      this.form.photo = '';
    },
    onEditorBlur(quill) {
      console.log('editor blur!', quill);
    },
    onEditorFocus(quill) {
      console.log('editor focus!', quill);
    },
    onEditorReady(quill) {
      console.log('editor ready!', quill);
      if (this.form.note) {
        this.$refs.myQuillEditor.quill.root.innerHTML = this.form.note;
      }
    },
    handleAvatarSuccess(response) {
      this.form.photo = response.url;
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
      if (this.form.username && this.form.nickname && this.form.telephone) {
        this.form.note = this.$refs.myQuillEditor.quill.root.innerHTML;

        $.ajax({
          url: 'https://app6457.acapp.acwing.com.cn/api/user/modify',
          type: 'POST',
          data: {
            userid: this.form.userid,
            nickname: this.form.nickname,
            telephone: this.form.telephone,
            apartmentname: this.form.apartmentname,
            gender: this.form.gender,
            status: this.form.status,
            email: this.form.email,
            role: this.form.role,
            note: this.form.note,
            post: this.form.post,
            photo: this.form.photo
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
          success: (response) => {
            if (response.error_message === 'success') {
              ElMessage.success('用户信息修改成功');
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
        userid: null,
        username: '',
        nickname: '',
        telephone: '',
        apartmentname: '',
        gender: '',
        status: 1,
        email: '',
        role: '',
        note: '',
        post: '',
        photo: ''
      };
      this.internalDialogVisible = false;
    }
  },
  mounted() {
    this.loadOrganizations();
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
