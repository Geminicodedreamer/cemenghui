<template>
  <swiper indicator-dots="true" indicator-color="white" indicator-active-color="grey" class="banner" :autoplay="true">
    <swiper-item v-for="item in pictures" :key="item.id">
      <image @tap="onPreviewImage(item.url)" :src="item.url"></image>
    </swiper-item>
  </swiper>

  <view class="warning">
    <text >
      首页
    </text>
  </view>
  <view class="commit">
    <text>
      不知道放什么
    </text>
  </view>
</template>

<script>
export  default {
  data(){
    return{
      pictures: [
        {id: '1' , url:"https://cdn.max-c.com/heybox/dailynews/img/45125a36dcd060618cb89e70d132bfd8.jpg"},
        {id: '2' , url:"https://tse2-mm.cn.bing.net/th/id/OIP-C.gICCbkQ2MKPStJn5Bwfb7gHaEo?rs=1&pid=ImgDetMain"},
        {id: '3' , url:"https://tse4-mm.cn.bing.net/th/id/OIP-C.SYmgDiCWoD5PidsjvVXHswHaEK?rs=1&pid=ImgDetMain"},
        {id: '4' , url:"https://ts1.cn.mm.bing.net/th/id/R-C.e614ae6e42d36f6198d958621eded587?rik=7Pif%2bhY5bv01WQ&riu=http%3a%2f%2fi2.hdslb.com%2fbfs%2farchive%2f395ada4fd33ab5427427a40509fe6fd5ac9da88c.jpg&ehk=F4VNJg1MYgnC49qC4HKAtCKkTTMIEfChYCSz2XfTQmM%3d&risl=&pid=ImgRaw&r=0"},
        {id: '5' , url:"https://pic1.zhimg.com/70/v2-15f60b69642ba46964509c51d2645202_1440w.avis?source=172ae18b&biz_tag=Post"},
      ]
    }
  },
  onLoad() {

  },
  methods: {
    onPreviewImage(url){
      uni.previewImage({
        urls : this.pictures.map(v=>v.url),
        current: url
      })
    }
  },
}
</script>


<style scoped>
.banner{
  width: 750rpx;
  height: 450rpx;
  margin-top: 15px;
  align-items: center;
}

.banner image{
  margin-left: 2.5%;
  height: 100%;
  width: 95%;
  border-radius: 35px;
}


.warning {
  font-size: 70px;
  text-align: center;
  color: red;
}
.commit{
  margin-top: 50%;
  color: blue;
  text-align: center;
}
</style>