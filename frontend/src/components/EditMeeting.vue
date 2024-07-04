<template>
    <el-dialog title="修改会议" v-model="internalDialogVisible" @close="closeModal">
        <el-form ref="form" :model="meetingForm" label-width="120px">
            <el-form-item label="会议名称" :required="true" class="form-item">
                <el-input v-model="meetingForm.name" placeholder="请输入会议名称"></el-input>
            </el-form-item>
            <el-form-item label="会议封面" class="form-item">
                <el-upload
                    class="avatar-uploader"
                    action="https://app6457.acapp.acwing.com.cn/api/upload"
                    :show-file-list="false"
                    :on-success="handleCoverSuccess"
                    :before-upload="beforeCoverUpload">
                    <template #trigger>
                        <div class="upload-trigger">
                            <div v-if="!meetingForm.photo" class="placeholder">请选择图片</div>
                            <img v-if="meetingForm.photo" :src="meetingForm.photo" class="avatar">
                        </div>
                    </template>
                </el-upload>
                <div class="upload-description">
                    请上传 大小不超过 <span class="red-bold">2MB</span> 格式为
                    <span class="red-bold">png/jpg/jpeg</span> 的文件
                </div>
            </el-form-item>
            <el-form-item label="创建人" :required="true" class="form-item">
                <el-input v-model="meetingForm.creator" placeholder="请输入创建人"></el-input>
            </el-form-item>
            <el-form-item label="会议内容" class="form-item">
                <quill-editor
                    ref="quillEditorRef"
                    v-model="meetingForm.content"
                    :options="editorOption"
                    @ready="onEditorReady"
                />
            </el-form-item>
            <el-form-item label="开始时间" :required="true" class="form-item">
                <el-date-picker
                    v-model="meetingForm.startTime"
                    type="datetime"
                    value-format="YYYY/MM/DD HH:mm"
                    placeholder="选择开始时间">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="结束时间" :required="true" class="form-item">
                <el-date-picker
                    v-model="meetingForm.endTime"
                    type="datetime"
                    value-format="YYYY/MM/DD HH:mm"
                    placeholder="选择结束时间">
                </el-date-picker>
            </el-form-item>
            <el-form-item class="button-container">
                <el-button type="primary" @click="submitForm">保存修改</el-button>
                <el-button @click="closeModal">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>
<script>
import { ref, watch, reactive } from 'vue';
import { ElMessage, ElDatePicker, ElUpload } from 'element-plus';
import { QuillEditor } from 'vue3-quill';
import axios from 'axios';

export default {
    components: {
        ElDatePicker,
        ElUpload,
        QuillEditor
    },
    props: {
        modelValue: {
            type: Boolean,
            required: true
        },
        meeting: {
            type: Object,
            required: true
        }
    },
    setup(props, { emit }) {
        const internalDialogVisible = ref(props.modelValue);
        const meetingForm = reactive({ ...props.meeting });
        const quillEditorRef = ref(null);
        const editorOption = {
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
                            handleImageInsertion();
                        }
                    }
                }
            }
        };

        watch(() => props.modelValue, (newVal) => {
            internalDialogVisible.value = newVal;
        });

        watch(internalDialogVisible, (newVal) => {
            emit('update:modelValue', newVal);
        });

        const closeModal = () => {
            internalDialogVisible.value = false;
            emit('close');
        };

        const submitForm = () => {
            if (!meetingForm.name || !meetingForm.creator || !meetingForm.startTime || !meetingForm.endTime) {
                ElMessage.error('会议名称、创建人、开始时间和结束时间不能为空！');
                return;
            }

            if (new Date(meetingForm.endTime) <= new Date(meetingForm.startTime)) {
                ElMessage.error('结束时间必须在开始时间之后！');
                return;
            }

            if (quillEditorRef.value) {
                meetingForm.content = quillEditorRef.value.root.innerHTML;
            }
            emit('update', { ...meetingForm });
            closeModal();
        };

        const handleCoverSuccess = (response) => {
            meetingForm.photo = response.url;
        };

        const beforeCoverUpload = (file) => {
            const isImage = file.type === 'image/jpeg' || file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isImage) {
                ElMessage.error('上传图片只能是 JPG/PNG 格式!');
            }
            if (!isLt2M) {
                ElMessage.error('上传图片大小不能超过 2MB!');
            }
            return isImage && isLt2M;
        };

        const handleImageInsertion = () => {
            const input = document.createElement('input');
            input.setAttribute('type', 'file');
            input.setAttribute('accept', 'image/png, image/gif, image/jpeg, image/bmp, image/x-icon');
            input.classList.add('ql-image');

            input.addEventListener('change', (e) => {
                const file = e.target.files[0];
                if (file) {
                    uploadImage(file);
                }
                input.value = '';
            });

            input.click();
        };

        const uploadImage = (file) => {
            const formData = new FormData();
            formData.append('file', file);

            axios.post('https://app6457.acapp.acwing.com.cn/api/upload', formData)
                .then(response => {
                    const url = response.data.url;
                    const range = quillEditorRef.value.getSelection();
                    quillEditorRef.value.insertEmbed(range.index, 'image', url);
                })
                .catch(error => {
                    console.error('Error uploading image:', error);
                    ElMessage.error('图片上传失败');
                });
        };

        const onEditorReady = (editor) => {
            quillEditorRef.value = editor;
            if (meetingForm.content) {
                editor.root.innerHTML = meetingForm.content;
            }
        };

        return {
            internalDialogVisible,
            meetingForm,
            quillEditorRef,
            editorOption,
            closeModal,
            submitForm,
            handleCoverSuccess,
            beforeCoverUpload,
            handleImageInsertion,
            uploadImage,
            onEditorReady
        };
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
