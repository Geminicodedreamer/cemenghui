<template>
    <ContentField>
        <div>
            <!-- 搜索条件 -->
            <div class="input-group input-group-sm mb-3">
                <span class="input-group-text" id="inputGroup-sizing-sm">会议名称</span>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-sm" v-model="searchName">
            </div>

            <div class="input-group input-group-sm mb-3">
                <span class="input-group-text" id="inputGroup-sizing-default">创建人</span>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" v-model="searchCreator">
            </div>

            <div class="demo-date-picker">
                <div class="block">
                    <span class="demonstration">会议日期</span>
                    <el-date-picker
                        v-model="searchDate"
                        type="date"
                        placeholder="Pick a day"
                        :size="'default'"
                    />
                </div>
            </div>

            <!-- 操作按钮 -->
            <div class="button-group">
                <button class="btn btn-primary btn-sm btn-margin" @click="filterList">查询</button>
                <button class="btn btn-warning btn-sm btn-margin" @click="resetFilters">重置</button>
                <button class="btn btn-success btn-sm btn-margin" @click="showAddMeetingModal">新增会议</button>
                <button class="btn btn-danger btn-sm btn-margin" @click="deleteSelectedMeetings">删除选中会议</button>
                <button class="btn btn-info btn-sm btn-margin" @click="exportToExcel">导出会议数据</button>
            </div>
        </div>

        <!-- 会议列表 -->
        <div>
            <table class="table" v-if="filteredMeetings.length > 0">
                <thead>
                    <tr>
                        <th scope="col"><input type="checkbox" @change="selectAllMeetings($event)"></th>
                        <th scope="col">会议ID</th>
                        <th scope="col">会议名称</th>
                        <th scope="col">创建人</th>
                        <th scope="col">会议状态</th>
                        <th scope="col">会议内容</th>
                        <th scope="col">
                            <a href="#" @click.prevent="sortByStartTime">开始时间</a>
                        </th>
                        <th scope="col">结束时间</th>
                        <th scope="col">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(meeting, index) in currentPageData" :key="index">
                        <td><input type="checkbox" v-model="selectedMeetings" :value="meeting.id"></td>
                        <th scope="row">{{ meeting.id }}</th>
                        <td>{{ meeting.name }}</td>
                        <td>{{ meeting.creator }}</td>
                        <td>{{ getStatus(meeting.startTime, meeting.endTime) }}</td>
                        <td><button @click="showMeetingDetail(meeting)" class="btn btn-outline-info btn-sm">查看详情</button></td>
                        <td>{{ formatDate(meeting.startTime) }}</td>
                        <td>{{ formatDate(meeting.endTime) }}</td>
                        <td>
                            <button @click="editMeeting(meeting)" class="btn btn-outline-primary btn-sm">修改</button>
                            <button @click="deleteMeeting(meeting.id)" class="btn btn-outline-danger btn-sm">删除</button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div v-else>
                <p>没有找到符合条件的会议。</p>
            </div>
        </div>

        <!-- 分页 -->
        <div v-if="filteredMeetings.length > 0">
            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <li class="page-item" :class="{ disabled: currentPage === 1 }">
                        <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">Previous</a>
                    </li>
                    <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }">
                        <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                    </li>
                    <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                        <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">Next</a>
                    </li>
                </ul>
            </nav>
        </div>

        <AddMeeting v-if="showModal" @close="showModal = false" @create="handleCreateMeeting" />
        <EditMeeting v-if="showEditModal" :modelValue="showEditModal" :meeting="editedMeeting" @close="showEditModal = false" @update="handleUpdateMeeting" />
        <MeetingDetail v-model="showDetailModal" :meeting="selectedMeeting" />

    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue';
import * as XLSX from 'xlsx'; // 确保正确引入 xlsx 库
import AddMeeting from '../../components/AddMeeting.vue';
import EditMeeting from '../../components/EditMeeting.vue';
import MeetingDetail from '../../components/MeetingDetail.vue'; // 新增
import { ref, computed, onMounted } from 'vue';
import { ElMessage , ElMessageBox} from 'element-plus';
import $ from 'jquery';
import { useStore } from 'vuex';

export default {
    components: {
        ContentField,
        AddMeeting,
        EditMeeting,
        MeetingDetail // 新增
    },
    setup() {
        const store = useStore();
        const meetings = ref([]);
        const currentPage = ref(1);
        const pageSize = ref(10);
        const searchName = ref('');
        const searchCreator = ref('');
        const searchDate = ref(null);
        const sortDirection = ref('asc');
        const selectedMeetings = ref([]);
        const showModal = ref(false);
        const showEditModal = ref(false);
        const showDetailModal = ref(false); // 新增
        const editedMeeting = ref(null);
        const selectedMeeting = ref(null); // 新增

        const totalPages = computed(() => Math.ceil(filteredMeetings.value.length / pageSize.value));
        const currentPageData = computed(() => {
            const start = (currentPage.value - 1) * pageSize.value;
            const end = currentPage.value * pageSize.value;
            return filteredMeetings.value.slice(start, end);
        });
        const filteredMeetings = computed(() => {
            return meetings.value.filter(meeting => {
                return (
                    (searchName.value === '' || meeting.name.includes(searchName.value)) &&
                    (searchCreator.value === '' || meeting.creator.includes(searchCreator.value)) &&
                    (searchDate.value === null || formatDate(meeting.startTime) === formatDate(searchDate.value))
                );
            });
        });

        const fetchMeetings = () => {
			console.log("dsfasasdasd")
            $.ajax({
                url: 'http://127.0.0.1:3000/meeting/list',
                type: 'GET',
                headers: {
                    Authorization: `Bearer ${store.state.user.token}`,
                },
                success: (resp) => {
                    if (resp.meetings) {
                        meetings.value = resp.meetings.map(meeting => ({
                            id: meeting.meetingid,
                            name: meeting.meetingname,
                            creator: meeting.creator,
                            content: meeting.content,
                            startTime: meeting.starttime,
                            endTime: meeting.endtime,
                            photo: meeting.photo,
                        }));
                    } else {
                        ElMessage.error('获取会议列表失败');
                    }
                },
                error: (error) => {
                    console.error("Error fetching meetings:", error);
                    ElMessage.error('获取会议列表失败');
                }
            });
        };

        const changePage = (page) => {
            if (page > 0 && page <= totalPages.value) {
                currentPage.value = page;
            }
        };

        const deleteMeeting = (id) => {
            ElMessageBox.confirm('是否删除').then(() =>{
            $.ajax({
                url: `http://127.0.0.1:3000/meeting/del`,
                type: "GET",
                data: { meetingid: id },
                headers: {
                    Authorization: `Bearer ${store.state.user.token}`,
                },
                success: (resp) => {
                    if (resp.error_message === "success") {
                        const index = meetings.value.findIndex(meeting => meeting.id === id);
                        if (index !== -1) {
                            meetings.value.splice(index, 1);
                        }

                        ElMessage({
                            message: '删除成功',
                            type: 'success',
                            duration: 2000
                        });
                        fetchMeetings(); // 删除成功后重新获取数据
                    } else {
                        ElMessage.error('发生错误');
                    }
                },
                error: () => {
                    ElMessage.error('未成功删除');
                }
            });});
        };

        const deleteSelectedMeetings = () => {
            selectedMeetings.value.forEach(id => deleteMeeting(id));
            selectedMeetings.value = [];
        };

        const addMeeting = (meeting) => {
            console.log(meeting)
            const formData = new FormData();
            formData.append('meetingname', meeting.name);
            formData.append('content', meeting.content); // 确保内容被正确添加
            formData.append('photo', meeting.cover);
            formData.append('creator', meeting.creator);
            formData.append('starttime', meeting.startTime);
            formData.append('endtime', meeting.endTime);
        
            console.log('FormData to be sent:', {
                meetingname: meeting.name,
                content: meeting.content,
                photo: meeting.cover,
                creator: meeting.creator,
                starttime: meeting.startTime,
                endtime: meeting.endTime
            });
        
            $.ajax({
                url: 'http://127.0.0.1:3000/meeting/add/',  // 确保路径正确
                type: 'POST',
                headers: {
                    Authorization: `Bearer ${store.state.user.token}`,
                },
                data: formData,
                processData: false,
                contentType: false,
                success: (resp) => {
                    console.log('Response from server:', resp);
                    if (resp.error_message === "success") {
                        meetings.value.push({
                            id: resp.meetingid,  // 确保使用的是后端返回的会议ID
                            name: meeting.name,
                            creator: meeting.creator,
                            content: meeting.content,
                            startTime: meeting.startTime,
                            endTime: meeting.endTime,
                            photo: meeting.cover,
                        });
                        showModal.value = false;
                        ElMessage({
                            message: '添加成功',
                            type: 'success',
                            duration: 2000
                        });
                    } else {
                        console.error('Error message from server:', resp.error_message);
                        ElMessage.error('发生错误: ' + resp.error_message);
                    }
                },
                error: (error) => {
                    console.error("Error adding meeting:", error);
                    ElMessage.error('添加失败: ' + error.responseText);
                }
            });
        };

        const editMeeting = (meeting) => {
            editedMeeting.value = { ...meeting };
            showEditModal.value = true;
        };

        const handleUpdateMeeting = (updatedMeeting) => {
            $.ajax({
                url: `http://127.0.0.1:3000/meeting/modify`,
                type: 'POST',
                headers: {
                    Authorization: `Bearer ${store.state.user.token}`,
                },
                data: updatedMeeting,
                success: (resp) => {
					console.log(resp)
                    if (resp.error_message === "success") {
                        const index = meetings.value.findIndex(meeting => meeting.id === updatedMeeting.meetingid);
                        if (index !== -1) {
                            meetings.value.splice(index, 1, {
                                id: updatedMeeting.meetingid,
                                name: updatedMeeting.meetingname,
                                creator: updatedMeeting.creator,
                                content: updatedMeeting.content,
                                startTime: updatedMeeting.starttime,
                                endTime: updatedMeeting.endtime,
                                photo: updatedMeeting.photo,
                            });
                        }
						
                        showEditModal.value = false;
						fetchMeetings();
                        ElMessage({
                            message: '修改成功',
                            type: 'success',
                            duration: 2000
                        });
                    } else {
                        ElMessage.error('发生错误');
                    }
                },
                error: (error) => {
                    console.error("Error updating meeting:", error);
                    ElMessage.error('修改失败');
                }
            });
        };

        const exportToExcel = () => {
            const data = filteredMeetings.value.map(meeting => ({
                '会议ID': meeting.id,
                '会议名称': meeting.name,
                '创建人': meeting.creator,
                '会议内容': meeting.content,
                '开始时间': formatDate(meeting.startTime),
                '结束时间': formatDate(meeting.endTime),
            }));
            const wb = XLSX.utils.book_new();
            const ws = XLSX.utils.json_to_sheet(data);
            XLSX.utils.book_append_sheet(wb, ws, 'Meetings');
            XLSX.writeFile(wb, 'meetings.xlsx');
        };

        const selectAllMeetings = (event) => {
            if (event.target.checked) {
                selectedMeetings.value = currentPageData.value.map(meeting => meeting.id);
            } else {
                selectedMeetings.value = [];
            }
        };

        const filterList = () => {
            currentPage.value = 1; // 查询后重置为第一页
        };

        const resetFilters = () => {
            searchName.value = '';
            searchCreator.value = '';
            searchDate.value = null;
            filterList();
        };

        const sortByStartTime = () => {
            sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
            meetings.value.sort((a, b) => {
                const dateA = new Date(a.startTime);
                const dateB = new Date(b.startTime);
                return sortDirection.value === 'asc' ? dateA - dateB : dateB - dateA;
            });
        };

        const formatDate = (date) => {
            const d = new Date(date);
            const month = '' + (d.getMonth() + 1).toString().padStart(2, '0');
            const day = '' + d.getDate().toString().padStart(2, '0');
            const year = d.getFullYear();
            return [year, month, day].join('-');
        };

        const getStatus = (startTime, endTime) => {
            const now = new Date();
            const meetingStartTime = new Date(startTime);
            const meetingEndTime = new Date(endTime);

            if (meetingEndTime < now) {
                return '已结束';
            } else if (meetingStartTime > now) {
                return '未开始';
            } else {
                return '进行中';
            }
        };

        const showAddMeetingModal = () => {
            showModal.value = true;
        };

        const showMeetingDetail = (meeting) => {
            selectedMeeting.value = meeting;
            showDetailModal.value = true;
        };

        const handleCreateMeeting = (newMeeting) => {
            console.log('父组件接收到的新会议数据:', newMeeting); // 调试：输出接收到的数据
            addMeeting(newMeeting);
        };

        onMounted(() => {
            fetchMeetings();
        });

        return {
            meetings,
            currentPage,
            pageSize,
            searchName,
            searchCreator,
            searchDate,
            sortDirection,
            selectedMeetings,
            showModal,
            showEditModal,
            showDetailModal,
            editedMeeting,
            selectedMeeting,
            totalPages,
            currentPageData,
            filteredMeetings,
            changePage,
            deleteMeeting,
            deleteSelectedMeetings,
            addMeeting,
            editMeeting,
            handleUpdateMeeting,
            exportToExcel,
            selectAllMeetings,
            filterList,
            resetFilters,
            sortByStartTime,
            formatDate,
            getStatus,
            showAddMeetingModal,
            showMeetingDetail,
            handleCreateMeeting,
        };
    }
};
</script>

<style scoped>
.search-conditions {
  margin-bottom: 20px;
}
.search-conditions .form-group {
  margin-bottom: 15px;
}
.btn-margin {
    margin-right: 5px; /* 调整按钮之间的间距 */
    margin-top: 5px;
}
</style>
