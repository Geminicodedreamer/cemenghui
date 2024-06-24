<template>
    <ContentField>
        <div>
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
            <div class="button-group">
                <button class="btn btn-primary btn-sm btn-margin" @click="filterList">查询</button>
                <button class="btn btn-warning btn-sm btn-margin" @click="resetFilters">重置</button>
                <button class="btn btn-success btn-sm btn-margin" @click="showAddMeetingModal">新增会议</button>
                <button class="btn btn-danger btn-sm btn-margin" @click="deleteSelectedMeetings">删除选中会议</button>
                <button class="btn btn-info btn-sm btn-margin" @click="exportToExcel">导出会议数据</button>

            </div>
        </div>

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
                        <td>{{ meeting.content }}</td>
                        <td>{{ meeting.startTime }}</td>
                        <td>{{ meeting.endTime }}</td>
                        <td>
                            <button @click="editMeeting(meeting.id)" class="btn btn-outline-primary btn-sm">修改</button>
                            <button @click="deleteMeeting(meeting.id)" class="btn btn-outline-danger btn-sm">删除</button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div v-else>
                <p>没有找到符合条件的会议。</p>
            </div>
        </div>
        
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
		<EditMeeting v-if="showEditModal" :meeting="editedMeeting" @close="showEditModal = false" @update="handleUpdateMeeting" />
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue';
import * as XLSX from 'xlsx'; // 确保正确引入 xlsx 库
import AddMeeting from '../../components/AddMeeting.vue';
import EditMeeting from '../../components/EditMeeting.vue';

export default {
    components: {
        ContentField,
		AddMeeting,
		EditMeeting,
    },
    data() {
        return {
            meetings: [
                { id: 1, name: '产品发布会', creator: '张三', content: '讨论新产品发布计划', startTime: '2023-05-01', endTime: '2023-05-01' },
                { id: 2, name: '年度总结会', creator: '李四', content: '总结年度工作，展望新年计划', startTime: '2023-06-15', endTime: '2023-06-15' },
                { id: 3, name: '项目启动会', creator: '王五', content: '启动新项目，分配任务', startTime: '2023-07-20', endTime: '2023-07-20' },
                { id: 4, name: '季度会议', creator: '赵六', content: '总结季度工作', startTime: '2023-08-10', endTime: '2023-08-10' },
                { id: 5, name: '战略会议', creator: '孙七', content: '讨论公司战略', startTime: '2023-09-05', endTime: '2023-09-05' },
                { id: 6, name: '客户交流会', creator: '周八', content: '与客户交流', startTime: '2023-10-15', endTime: '2023-10-15' },
                { id: 7, name: '技术研讨会', creator: '吴九', content: '技术讨论', startTime: '2023-11-20', endTime: '2023-11-20' },
                { id: 8, name: '产品培训会', creator: '郑十', content: '产品培训', startTime: '2023-12-01', endTime: '2023-12-01' },
                { id: 9, name: '年度盛典', creator: '钱十一', content: '年度庆典活动', startTime: '2024-01-15', endTime: '2024-01-15' },
                { id: 10, name: '项目复盘会', creator: '王十二', content: '项目复盘', startTime: '2024-02-20', endTime: '2024-02-20' },
                { id: 11, name: '财务分析会', creator: '李十三', content: '财务分析', startTime: '2024-03-10', endTime: '2024-03-10' },
                { id: 12, name: '业务发展会', creator: '赵十四', content: '业务发展讨论', startTime: '2024-04-05', endTime: '2024-04-05' },
                { id: 13, name: '投资者大会', creator: '孙十五', content: '投资者见面会', startTime: '2024-05-15', endTime: '2024-05-15' },
                { id: 14, name: '市场分析会', creator: '周十六', content: '市场分析', startTime: '2024-06-20', endTime: '2024-06-20' },
                { id: 15, name: '产品评审会', creator: '吴十七', content: '产品评审', startTime: '2024-07-10', endTime: '2024-07-10' },
                { id: 16, name: '技术评估会', creator: '郑十八', content: '技术评估', startTime: '2024-08-01', endTime: '2024-08-01' },
                { id: 17, name: '季度总结会', creator: '钱十九', content: '季度总结', startTime: '2024-09-15', endTime: '2024-09-15' },
                { id: 18, name: '年度计划会', creator: '王二十', content: '制定年度计划', startTime: '2024-10-20', endTime: '2024-10-20' },
                { id: 19, name: '市场推广会', creator: '李二十一', content: '市场推广讨论', startTime: '2024-11-25', endTime: '2024-11-25' },
                { id: 20, name: '客户回访会', creator: '赵二十二', content: '客户回访总结', startTime: '2024-12-05', endTime: '2024-12-05' }
            ], // 增加的测试数据
            currentPage: 1,
            pageSize: 10, // 每页显示的行数
            searchName: '', // 搜索的会议名称
            searchCreator: '', // 搜索的创建人
            searchDate: null, // 搜索的会议日期
            value1: null, // 会议日期
            sortDirection: 'asc', // 排序方向
            selectedMeetings: [], // 选中的会议ID列表
            originalMeetings: [], // 原始会议数据副本
			showModal: false,
			showEditModal: false, // 控制编辑会议信息弹窗显示与隐藏
			editedMeeting: null // 当前编辑的会议信息
        };
    },
    computed: {
        totalPages() {
            return Math.ceil(this.filteredMeetings.length / this.pageSize);
        },
        currentPageData() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = this.currentPage * this.pageSize;
            return this.filteredMeetings.slice(start, end);
        },
        filteredMeetings() {
            return this.meetings.filter(meeting => {
                return (
                    (this.searchName === '' || meeting.name.includes(this.searchName)) &&
                    (this.searchCreator === '' || meeting.creator.includes(this.searchCreator)) &&
                    (this.searchDate === null || this.formatDate(meeting.startTime) === this.formatDate(this.searchDate))
                );
            });
        },

    },
    methods: {
        changePage(page) {
            if (page > 0 && page <= this.totalPages) {
                this.currentPage = page;
            }
        },
        // editMeeting(id) {
        //     // 编辑会议的逻辑
        //     console.log(`修改会议 ID: ${id}`);
        // },
        deleteMeeting(id) {
            // 删除会议的逻辑
            this.meetings = this.meetings.filter(meeting => meeting.id !== id);
            this.filterList(); // 删除后重新过滤列表
        },
        deleteSelectedMeetings() {
            this.meetings = this.meetings.filter(meeting => !this.selectedMeetings.includes(meeting.id));
            this.selectedMeetings = []; // 清空选中列表
            this.filterList(); // 删除后重新过滤列表
        },
        addMeeting(meeting) {
            // 检查会议是否已经存在
            if (this.meetings.some(m => m.name === meeting.name && m.startTime === meeting.startTime && m.endTime === meeting.endTime)) {
                // 如果会议已经存在，则不进行添加
                return;
            }
        
            const reader = new FileReader();
            reader.onload = (e) => {
                const base64Image = e.target.result;
                this.meetings.push({
                    id: this.meetings.length + 1,
                    name: meeting.name,
                    cover: base64Image, // 保存 Base64 格式的图片
                    creator: meeting.creator,
                    content: meeting.content,
                    startTime: meeting.startTime, // 直接使用传入的 startTime
                    endTime: meeting.endTime // 直接使用传入的 endTime
                });
                this.showModal = false;
            };
            if (meeting.cover) {
                reader.readAsDataURL(meeting.cover);
            } else {
                this.meetings.push({
                    id: this.meetings.length + 1,
                    name: meeting.name,
                    cover: '', // 没有上传图片时，封面为空
                    creator: meeting.creator,
                    content: meeting.content,
                    startTime: meeting.startTime, // 直接使用传入的 startTime
                    endTime: meeting.endTime // 直接使用传入的 endTime
                });
                this.showModal = false;
            }
        },

        exportToExcel() {
            const wb = XLSX.utils.book_new();
            const ws = XLSX.utils.json_to_sheet(this.meetings);
            XLSX.utils.book_append_sheet(wb, ws, 'Meetings');
            XLSX.writeFile(wb, 'meetings.xlsx');
        },
        selectAllMeetings(event) {
            if (event.target.checked) {
                this.selectedMeetings = this.currentPageData.map(meeting => meeting.id);
            } else {
                this.selectedMeetings = [];
            }
        },
        filterList() {
            this.currentPage = 1; // 查询后重置为第一页
        },
        resetFilters() {
            this.searchName = '';
            this.searchCreator = '';
            this.searchDate = null;
            this.meetings = this.originalMeetings;
            this.filterList();
        },
        sortByStartTime() {
            this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
            this.meetings.sort((a, b) => {
                const dateA = new Date(a.startTime);
                const dateB = new Date(b.startTime);
                return this.sortDirection === 'asc' ? dateA - dateB : dateB - dateA;
            });
        },
        formatDate(date) {
            const d = new Date(date);
            const month = '' + (d.getMonth() + 1);
            const day = '' + d.getDate();
            const year = d.getFullYear();
            return [year, month.padStart(2, '0'), day.padStart(2, '0')].join('-');
        },
        getStatus(startTime, endTime) {
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
        },
		showAddMeetingModal() {
            this.showModal = true;
        },
        handleCreateMeeting(newMeeting) {
            // 在这里处理创建会议的逻辑，比如将新会议添加到列表中
            this.meetings.push({
                id: this.meetings.length + 1,
                name: newMeeting.name,
                content: newMeeting.content,
                cover: newMeeting.cover,
                creator: newMeeting.creator,
                startTime: newMeeting.startTime, // 直接使用传入的 startTime
                endTime: newMeeting.endTime // 直接使用传入的 endTime
            });
            this.showModal = false; // 创建成功后关闭弹窗
            this.filterList(); // 重新过滤列表
        },
		editMeeting(meeting) {
			this.editedMeeting = { ...meeting }; // 复制会议信息，避免直接修改原始数据
			this.showEditModal = true; // 打开编辑会议信息弹窗
		},
		handleUpdateMeeting(updatedMeeting) {
		// 在这里处理更新会议信息的逻辑，比如将更新后的会议信息保存到列表中
			const index = this.meetings.findIndex(meeting => meeting.id === updatedMeeting.id);
			if (index !== -1) {
				this.meetings.splice(index, 1, updatedMeeting);
			}
		}

    },
    mounted() {
        this.originalMeetings = JSON.parse(JSON.stringify(this.meetings)); // 存储原始会议数据副本
        this.filterList(); // 初始化时过滤列表
    }
}
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
    margin-top:5px;
}
</style>
