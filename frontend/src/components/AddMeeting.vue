<template>
    <div class="modal" tabindex="-1" role="dialog" style="display: block;">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">创建会议</h5>
                    <button type="button" class="close" @click="closeModal">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 会议名称 -->
                    <div class="form-group">
                        <label for="meetingName">会议名称</label>
                        <input type="text" class="form-control" id="meetingName" v-model="meetingName">
                    </div>
                    <!-- 会议封面 -->
                    <div class="form-group">
                        <label for="meetingCover">会议封面</label>
                        <el-upload
                          class="avatar-uploader"
                          action="https://app6457.acapp.acwing.com.cn/api/upload"
                          :show-file-list="false"
                          :on-success="handleCoverSuccess"
                          :before-upload="beforeCoverUpload">
                          <template #trigger>
                            <div class="upload-trigger">
                              <div v-if="!imageUrl" class="placeholder">请选择图片</div>
                              <img v-if="imageUrl" :src="imageUrl" class="avatar">
                            </div>
                          </template>
                        </el-upload>
                        <div class="upload-description">
                            请上传 大小不超过 <span class="red-bold">2MB</span> 格式为
                            <span class="red-bold">png/jpg/jpeg</span> 的文件
                        </div>
                    </div>
                    <!-- 创建人 -->
                    <div class="form-group">
                        <label for="creator">创建人</label>
                        <input type="text" class="form-control" id="creator" v-model="creator">
                    </div>
                    <!-- 会议内容 -->
                    <div class="form-group">
                        <label for="meetingContent">会议内容</label>
                        <quill-editor
                            ref="quillEditorRef"
                            v-model="meetingContent"
                            :options="editorOption"
                            @blur="onEditorBlur"
                            @focus="onEditorFocus"
                            @ready="onEditorReady"
                        />
                    </div>
                    <!-- 开始时间 -->
                    <div class="form-group">
                        <label for="startTime">开始时间</label>
                        <el-date-picker
                            v-model="startTime"
                            type="datetime"
                            placeholder="选择开始时间">
                        </el-date-picker>
                    </div>
                    <!-- 结束时间 -->
                    <div class="form-group">
                        <label for="endTime">结束时间</label>
                        <el-date-picker
                            v-model="endTime"
                            type="datetime"
                            placeholder="选择结束时间">
                        </el-date-picker>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" @click="createMeeting">创建</button>
                    <button type="button" class="btn btn-secondary" @click="closeModal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { ElMessage, ElDatePicker, ElUpload } from 'element-plus';
import axios from 'axios';
import { QuillEditor } from 'vue3-quill';

export default {
    components: {
        ElDatePicker,
        ElUpload,
        QuillEditor
    },
    data() {
        return {
            quillEditorRef: ref(null),
            editorOption: {
                modules: {
                    toolbar: {
                        container: [
                            ['bold', 'italic', 'underline', 'strike'],
                            ['blockquote', 'code-block'],
                            [{ 'header': 1 }, { 'header': 2 }],
                            [{ 'list': 'ordered' }, { 'list': 'bullet' }],
                            [{ 'script': 'sub' }, { 'script': 'super' }],
                            [{ 'indent': '-1' }, { 'indent': '+1' }],
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
            meetingName: '',
            meetingContent: '',
            meetingCover: null,
            creator: '',
            imageUrl: '',
            startTime: new Date(),
            endTime: new Date(),
            editorReady: false
        };
    },
    methods: {
        uploadImage(file) {
            const formData = new FormData();
            formData.append('file', file);

            axios.post('https://app6457.acapp.acwing.com.cn/api/upload', formData)
                .then(response => {
                    const url = response.data.url; // 从服务器响应中获取图片 URL
                    const range = this.quillEditorRef.getSelection();
                    this.quillEditorRef.insertEmbed(range.index, 'image', url);
                })
                .catch(error => {
                    console.error('Error uploading image: ', error);
                    ElMessage.error('图片上传失败');
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
        closeModal() {
            this.$emit('close');
        },
        handleCoverSuccess(response) {
            this.imageUrl = response.url; // 假设后端返回的图片 URL 在 response.url 中
            this.meetingCover = response.url;
        },
        beforeCoverUpload(file) {
            const isImage = file.type === 'image/jpeg' || file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isImage) {
                ElMessage.error('上传图片只能是 JPG/PNG 格式!');
            }
            if (!isLt2M) {
                ElMessage.error('上传图片大小不能超过 2MB!');
            }
            return isImage && isLt2M;
        },
        createMeeting() {
            if (!this.meetingName || !this.creator || !this.meetingCover || !this.startTime || !this.endTime) {
                ElMessage.error('会议名称、会议封面、创建人、开始时间和结束时间不能为空！');
                return;
            }

            if (new Date(this.endTime) <= new Date(this.startTime)) {
                ElMessage.error('结束时间必须在开始时间之后！');
                return;
            }

            const formatOptions = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' };
            const startTimeStr = new Date(this.startTime).toLocaleString('zh-CN', formatOptions);
            const endTimeStr = new Date(this.endTime).toLocaleString('zh-CN', formatOptions);

            if (this.quillEditorRef) {
                this.meetingContent = this.quillEditorRef.root.innerHTML;
            }

            const newMeeting = {
                name: this.meetingName,
                content: this.meetingContent,
                cover: this.meetingCover,
                creator: this.creator,
                startTime: startTimeStr,
                endTime: endTimeStr
            };
            console.log('New meeting to be created:', newMeeting);
            this.$emit('create', newMeeting);
            this.closeModal();
        },
        onEditorReady(quill) {
            this.quillEditorRef = quill;
            this.editorReady = true;
            console.log('Editor is ready', quill);
        },
        onEditorFocus(quill){
            this.quillEditorRef = quill;
            this.meetingContent = this.quillEditorRef.root.innerHTML;
        }
    }
};
</script>

<style scoped>
.modal-lg {
    max-width: 90%;
}

.upload-trigger {
    width: 300px; /* 固定宽度 */
    height: 225px; /* 4:3 比例的高度 */
    border: 1px dashed #dcdfe6;
    border-radius: 4px;
    overflow: hidden;
    position: relative;
    background-color: #f0f0f0; /* 添加背景颜色 */
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}

.upload-trigger .placeholder {
    color: #909399;
    font-size: 14px;
    text-align: center;
}

.upload-trigger .avatar {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.upload-description {
    font-size: 12px;
    color: #909399;
    margin-top: 5px;
}

.red-bold {
    color: #f56c6c;
    font-weight: bold;
}

.meeting-content img {
    max-width: 50px; /* 设置会议内容中图片的最大宽度 */
    max-height: 50px; /* 设置会议内容中图片的最大高度 */
    height: auto;
}
</style>
