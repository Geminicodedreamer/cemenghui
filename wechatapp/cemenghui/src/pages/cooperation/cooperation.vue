<template>
  <view class="myContent">
    <!-- 分类信息展示 -->
    <view class="categoryGrid">
      <view v-for="(category, index) in categories" :key="index" class="categoryItem" @click="handleCategoryClick(category.name)">
        <view class="categoryIcon">{{ category.icon }}</view>
        <view class="categoryInfo">
          <text class="categoryName">{{ category.name }}</text>
          <text class="categoryDescription">{{ category.description }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userType: null,
      categories: [
        {
          name: "会议研讨",
          description: "各类会议和研讨会的信息展示。",
          icon: "🗣️"
        },
        {
          name: "标准定制",
          description: "标准定制服务的简介和信息展示。",
          icon: "📏"
        },
        {
          name: "技术培训",
          description: "技术培训课程的简介和报名信息。",
          icon: "📚"
        },
        {
          name: "工具研发",
          description: "工具研发项目的进展和成果展示。",
          icon: "🔧"
        },
        {
          name: "公益行动",
          description: "各类公益行动的简介和参与方式。",
          icon: "🌍"
        }
      ],
    }
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      this.userType = wx.getStorageSync('userType');
      if(this.userType === "tourist") return;
      const token = wx.getStorageSync('jwt_token');
      if (!token) {
        wx.navigateTo({
          url: '/pages/login/login'
        });
      } else {
        this.$store.dispatch('user/getinfo', {
          success: (res) => {
            console.log('User info fetched successfully:', res);
          },
          error: (err) => {
            console.error('Failed to fetch user info:', err);
            wx.navigateTo({
              url: '/pages/login/login'
            });
          }
        });
      }
    },
    handleCategoryClick(categoryName) {
      if (categoryName === '会议研讨') {
        wx.navigateTo({
          url: '/pages/cooperation/meetingList'
        });
      }
      // 其他分类的处理逻辑可以在这里添加
    }
  }
}
</script>

<style scoped>
.myContent {
  padding: 20px;
  background-color: #f0f0f0; /* Light background color for the page */
}

.categoryGrid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 10px; /* Space between the grid items */
}

.categoryItem {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 48%; /* Adjusted to fit two items per row with some gap */
  box-sizing: border-box; /* Ensure padding and border are included in the width */
  padding: 20px; /* Increased padding for a more spacious look */
  margin-bottom: 20px; /* Space between rows */
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #fff; /* White background for the items */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow for better appearance */
  text-align: center;
}

.categoryIcon {
  font-size: 36px;
  margin-bottom: 15px; /* More space between the icon and the name */
}

.categoryInfo {
  width: 100%; /* Make info take the full width */
}

.categoryName {
  font-size: 20px; /* Larger font size for the name */
  font-weight: bold;
  margin-bottom: 10px; /* Space between the name and the description */
  padding-bottom: 5px; /* Padding below the name */
  display: block; /* Ensure it takes the full width */
  text-align: center; /* Center the text */
}

.categoryDescription {
  font-size: 14px;
  color: #666;
  text-align: center; /* Center the description text */
}
</style>
