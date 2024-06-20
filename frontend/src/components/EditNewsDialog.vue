<template>
  <el-dialog :title="'修改资讯管理'" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="新闻标题" class="form-item">
        <el-input v-model="form.title" placeholder="请输入新闻标题"></el-input>
      </el-form-item>
      <el-form-item label="新闻图片路径" class="form-item">
        <el-upload
          class="avatar-uploader"
          action="#"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <div>建议尺寸750px*300px，支持jpg/png/jpeg/gif格式</div>
      </el-form-item>
      <el-form-item label="新闻内容" class="form-item">
        <div class="editor-container">
          <quill-editor
            v-model="form.content"
            ref="quillEditor"
            :options="editorOptions">
          </quill-editor>
        </div>
      </el-form-item>
      <el-form-item label="作者" class="form-item">
        <el-input v-model="form.author" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item label="新闻简介" class="form-item">
        <el-input v-model="form.summary" placeholder="请输入新闻简介"></el-input>
      </el-form-item>
      <el-form-item label="选择租户" class="form-item">
        <el-select v-model="form.tenant" placeholder="请选择租户">
          <el-option label="租户1" value="tenant1"></el-option>
          <el-option label="租户2" value="tenant2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="button-container">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { ElDialog, ElForm, ElFormItem, ElInput, ElUpload, ElButton, ElSelect, ElOption } from 'element-plus';
import { QuillEditor } from 'vue3-quill';
import 'quill/dist/quill.snow.css';

export default {
  components: {
    ElDialog,
    ElForm,
    ElFormItem,
    ElInput,
    ElUpload,
    ElButton,
    ElSelect,
    ElOption,
    QuillEditor
  },
  props: {
    dialogVisible: {
      type: Boolean,
      required: true
    },
    newsData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      internalDialogVisible: this.dialogVisible,
      form: {
        title: '',
        imageUrl: '',
        content: '',
        author: '',
        summary: '',
        tenant: ''
      },
      editorOptions: {
        theme: 'snow'
      }
    };
  },
  watch: {
    dialogVisible(val) {
      this.internalDialogVisible = val;
      if (val) {
        this.form = { ...this.newsData };
      }
    },
    internalDialogVisible(val) {
      this.$emit('update:dialogVisible', val);
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.form.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('上传图片只能是 JPG/PNG/GIF 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      return isImage && isLt2M;
    },
    submitForm() {
      console.log('提交表单', this.form);
      // 在此处添加提交表单的逻辑
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
