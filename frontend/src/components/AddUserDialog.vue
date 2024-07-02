<template>
  <el-dialog title="添加用户" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="用户名" :required="true" class="form-item">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
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
          <el-option label="超级管理员" value="超级管理员"></el-option>
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
      <el-form-item label="所属公司名称" class="form-item">
        <el-input v-model="form.companyname" :placeholder="selectedCompanyName" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="用户头像" class="form-item">
        <el-upload
          class="avatar-uploader"
          action="http://127.0.0.1:3000/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.photo" ><Plus style="width:20px;color: #dcdfe6;"/></div>
              <img v-if="form.photo" :src="form.photo" class="avatar">
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">5MB</span> 格式为 
          <span class="red-bold">png/jpg/jpeg/gif</span> 的文件
        </div>
      </el-form-item>
      <el-form-item label="密码" :required="true" class="form-item">
        <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
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
    },
    selectedCompanyName: {
      type: String,
      required: true
    },
    apartmentchoose: {
      type: Array,
      required: true
    }
  },
  data() {
    const store = useStore();
    return {
      internalDialogVisible: this.dialogVisible,
      form: {
        username: '',
        nickname: '',
        telephone: '',
        apartmentname: '',
        gender: '',
        status: "1",
        email: '',
        role: '',
        note: '',
        companyname: this.selectedCompanyName,
        post: '',
        createtime: '',
        photo: '',
        password: ''
      },
      organizationOptions: [], // 存储组织结构数据
      store,
      editorOption: {
        modules: {
          toolbar: {
            container: [
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
      if (this.form.username && this.form.nickname && this.form.telephone && this.form.password) {
        this.form.note = this.$refs.myQuillEditor.quill.root.innerHTML;
        const currentDatetime = new Date();
        const yyyy = currentDatetime.getFullYear();
        const MM = String(currentDatetime.getMonth() + 1).padStart(2, '0');
        const dd = String(currentDatetime.getDate()).padStart(2, '0');
        const hh = String(currentDatetime.getHours()).padStart(2, '0');
        const mm = String(currentDatetime.getMinutes()).padStart(2, '0');
        const ss = String(currentDatetime.getSeconds()).padStart(2, '0');
        this.form.createtime = `${yyyy}-${MM}-${dd} ${hh}:${mm}:${ss}`;
        $.ajax({
          url: 'http://127.0.0.1:3000/user/add/', // 后端添加用户信息的接口
          type: 'POST',
          data: {
            username: this.form.username,
            nickname: this.form.nickname,
            password: this.form.password,
            telephone: this.form.telephone,
            apartmentname: this.form.apartmentname,
            gender: this.form.gender,
            status: this.form.status,
            email: this.form.email,
            role: this.form.role,
            note: this.form.note,
            companyname: this.form.companyname,
            post: this.form.post,
            photo: this.form.photo,
            createtime: this.form.createtime
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
          success: (response) => {
            console.log(response);
            if (response.error_message === 'success') {
              ElMessage.success('用户添加成功');
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
        this.$refs.form.resetFields(); // 重置表单字段
        this.form = {
        username: '',
        nickname: '',
        telephone: '',
        apartmentname: '',
        gender: '',
        status: 1, // 设置为默认值
        email: '',
        role: '',
        note: '',
        companyname: this.selectedCompanyName, // 设置为默认公司名
        post: '',
        createtime: '',
        photo: '',
        password: ''
        };
        this.internalDialogVisible = false; // 关闭对话框
    },
  },
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
