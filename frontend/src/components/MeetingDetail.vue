<template>
  <el-dialog v-model="visible" title="会议详情" @close="closeDialog">
    <el-form :model="meetingForm" label-width="120px">
      <el-form-item label="会议ID">
        <span>{{ meetingForm.id }}</span>
      </el-form-item>
      <el-form-item label="会议名称">
        <span>{{ meetingForm.name }}</span>
      </el-form-item>
      <el-form-item label="创建人">
        <span>{{ meetingForm.creator }}</span>
      </el-form-item>
      <el-form-item label="会议内容">
        <quill-editor
          ref="quillEditorRef"
          v-model="meetingForm.content"
          :options="editorOption"
          @ready="onEditorReady"
        />
      </el-form-item>
      <el-form-item label="开始时间">
        <span>{{ formatDateTime(meetingForm.startTime) }}</span>
      </el-form-item>
      <el-form-item label="结束时间">
        <span>{{ formatDateTime(meetingForm.endTime) }}</span>
      </el-form-item>
      <el-form-item label="会议封面">
        <img :src="meetingForm.photo" alt="会议封面" class="cover-image"/>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>
<script>
import { QuillEditor } from 'vue3-quill';

export default {
  components: {
    QuillEditor
  },
  props: {
    modelValue: Boolean,
    meeting: Object
  },
  data() {
    return {
      visible: this.modelValue,
      meetingForm: { ...this.meeting },
      editorOption: {
        readOnly: true,
        theme: 'snow',
        modules: {
          toolbar: false
        }
      }
    };
  },
  watch: {
    modelValue(newVal) {
      this.visible = newVal;
    },
    visible(newVal) {
      this.$emit('update:modelValue', newVal);
    },
    meeting: {
      handler(newVal) {
        this.meetingForm = { ...newVal };
        if (this.quillEditorRef && this.quillEditorRef.root) {
          if(newVal.content === "<p><br></p>") this.setEditorContent("无");
          else this.setEditorContent(newVal.content);
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    closeDialog() {
      this.visible = false;
      this.$emit('close');
    },
    formatDateTime(date) {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = (d.getMonth() + 1).toString().padStart(2, '0');
      const day = d.getDate().toString().padStart(2, '0');
      const hours = d.getHours().toString().padStart(2, '0');
      const minutes = d.getMinutes().toString().padStart(2, '0');
      const seconds = d.getSeconds().toString().padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    onEditorReady(editor) {
      this.quillEditorRef = editor;
      this.quillEditorRef.enable(false); // 确保编辑器是只读的
      this.quillEditorRef.setContents([{ insert: '' }]); // 清除默认内容
      this.setEditorContent(this.meetingForm.content);
    },
    setEditorContent(content) {
      if (content) {
        this.quillEditorRef.setContents(this.quillEditorRef.clipboard.convert(content));
      } else {
        this.quillEditorRef.setContents([{ insert: '\n' }]); // 设置默认空内容
      }
    }
  }
};
</script>
<style scoped>
.cover-image {
  max-width: 350px; /* 设置最大宽度 */
  max-height: 350px; /* 设置最大高度 */
  height: auto;
}
.meeting-content img {
  max-width: 50px; /* 设置会议内容中图片的最大宽度 */
  max-height: 50px; /* 设置会议内容中图片的最大高度 */
  height: auto;
}
</style>
