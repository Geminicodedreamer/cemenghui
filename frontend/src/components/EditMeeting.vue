<template>
    <div class="modal" tabindex="-1" role="dialog" style="display: block;">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">修改会议</h5>
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
                        <div class="meeting-cover">
                            <img v-if="imageUrl" :src="imageUrl" alt="会议封面">
                            <div v-else class="placeholder">请选择图片</div>
                        </div>
                        <input type="file" class="form-control-file" id="meetingCover" accept="image/png, image/jpeg" @change="handleCoverChange">
                    </div>
                    <!-- 创建人 -->
                    <div class="form-group">
                        <label for="creator">创建人</label>
                        <input type="text" class="form-control" id="creator" v-model="creator">
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
                    <button type="button" class="btn btn-primary" @click="updateMeeting">保存修改</button>
                    <button type="button" class="btn btn-secondary" @click="closeModal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { ElDatePicker } from 'element-plus';

export default {
    components: {
        ElDatePicker
    },
    props: {
        meeting: {
            type: Object,
            required: true
        }
    },
    setup(props, { emit }) {
        const meetingName = ref(props.meeting.name);
        const meetingCover = ref(null);
        const creator = ref(props.meeting.creator);
        const imageUrl = ref(props.meeting.cover);
        const startTime = ref(new Date(props.meeting.startTime));
        const endTime = ref(new Date(props.meeting.endTime));

        const closeModal = () => {
            emit('close');
        };

        const updateMeeting = () => {
            if (!meetingName.value || !creator.value || !startTime.value || !endTime.value) {
                alert('会议名称、创建人、开始时间和结束时间不能为空！');
                return;
            }
        
            if (new Date(endTime.value) <= new Date(startTime.value)) {
                alert('结束时间必须在开始时间之后！');
                return;
            }
        
            const formatOptions = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' };
            const startTimeStr = new Date(startTime.value).toLocaleDateString('zh-CN', formatOptions) + ' ' + new Date(startTime.value).toLocaleTimeString('zh-CN', formatOptions);
            const endTimeStr = new Date(endTime.value).toLocaleDateString('zh-CN', formatOptions) + ' ' + new Date(endTime.value).toLocaleTimeString('zh-CN', formatOptions);
        
            const updatedMeeting = {
                id: props.meeting.id,
                name: meetingName.value,
                cover: imageUrl.value,
                creator: creator.value,
                startTime: startTimeStr,
                endTime: endTimeStr
            };
            emit('update', updatedMeeting);
            closeModal();
        };

        const handleCoverChange = (event) => {
            meetingCover.value = event.target.files[0];
            if (meetingCover.value) {
                imageUrl.value = URL.createObjectURL(meetingCover.value);
            } else {
                imageUrl.value = '';
            }
        };

        return {
            meetingName,
            meetingCover,
            creator,
            imageUrl,
            startTime,
            endTime,
            closeModal,
            updateMeeting,
            handleCoverChange
        };
    }
};
</script>

<style scoped>
.modal-lg {
    max-width: 90%;
}

.meeting-cover {
    width: 100%;
    height: 200px;
    border: 1px solid #ccc;
    border-radius: 4px;
    overflow: hidden;
    position: relative;
}

.meeting-cover img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.placeholder {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: #f0f0f0;
    color: #888;
}

.form-group {
    margin-bottom: 15px;
}
</style>
