<template>
  <view class="receipt-form">
    <text class="form-title">填写参会回执</text>
    <view class="form-item">
      <input placeholder="单位" v-model="company" class="form-input" />
    </view>
    <view class="form-item">
      <input placeholder="姓名" v-model="name" class="form-input" />
    </view>
    <view class="form-item">
      <picker mode="selector" :range="genderOptions" @change="onGenderChange" class="form-picker">
        <view class="picker">{{ genderIndex !== null ? genderOptions[genderIndex] : '选择性别' }}</view>
      </picker>
    </view>
    <view class="form-item">
      <input placeholder="手机号码" v-model="phone" class="form-input" />
    </view>
    <view class="form-item">
      <input placeholder="电子邮箱" v-model="email" class="form-input" />
    </view>
    <view class="form-item">
      <picker mode="selector" :range="transportationOptions" @change="onTransportationChange" class="form-picker">
        <view class="picker">{{ transportationIndex !== null ? transportationOptions[transportationIndex] : '选择到达方式' }}</view>
      </picker>
    </view>
    <view class="form-item">
      <picker mode="date" @change="onArrivalTimeChange" class="form-picker">
        <view class="picker">{{ arrivalTime ? arrivalTime : '选择到达时间' }}</view>
      </picker>
    </view>
    <view class="form-item">
      <textarea placeholder="留言" v-model="message" class="form-textarea"></textarea>
    </view>
    <button @click="submitReceipt" class="submit-button">提交</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      company: '',
      name: '',
      genderIndex: null,
      genderOptions: ['男', '女'],
      phone: '',
      email: '',
      transportationIndex: null,
      transportationOptions: ['飞机', '高铁', '汽车'],
      arrivalTime: '',
      message: ''
    }
  },
  methods: {
    onGenderChange(e) {
      this.genderIndex = e.detail.value;
    },
    onTransportationChange(e) {
      this.transportationIndex = e.detail.value;
    },
    onArrivalTimeChange(e) {
      this.arrivalTime = e.detail.value;
    },
    submitReceipt() {
      // 验证表单
      if (!this.company || !this.name || this.genderIndex === null || !this.phone || !this.email || this.transportationIndex === null || !this.arrivalTime) {
        wx.showToast({
          title: '请填写所有必填项',
          icon: 'none'
        });
        return;
      }

      // 处理表单提交
      const receipt = {
        company: this.company,
        name: this.name,
        gender: this.genderOptions[this.genderIndex],
        phone: this.phone,
        email: this.email,
        transportation: this.transportationOptions[this.transportationIndex],
        arrivalTime: this.arrivalTime,
        message: this.message
      };
      console.log('Receipt submitted:', receipt);

      // 模拟提交表单后返回会议详情页面
      wx.showToast({
        title: '提交成功',
        icon: 'success',
        duration: 2000,
        success: () => {
          setTimeout(() => {
            wx.navigateBack();
          }, 2000);
        }
      });
    }
  }
}
</script>

<style scoped>
.receipt-form {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 26px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.form-item {
  margin-bottom: 15px;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  background-color: #fff;
}

.form-textarea {
  height: 100px;
  resize: none;
}

.form-picker {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #fff;
}

.picker {
  font-size: 16px;
  color: #666;
}

.submit-button {
  width: 100%;
  padding: 14px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  margin-top: 20px;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #0056b3;
}
</style>
