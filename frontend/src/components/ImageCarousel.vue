<template>
  <div class="container">
    <div class="card">
      <div class="card-body">
        <!-- 上方照片部分 -->
        <div class="photo-section relative w-full h-2/3 border rounded-lg overflow-hidden">
          <div class="swiper-container fadein-right absolute inset-0">
            <div class="swiper-wrapper">
              <div class="swiper-slide">
                <img class="swiper-image" src="../assets/1.jpg" alt="成功案例1" />
              </div>
              <div class="swiper-slide">
                <img class="swiper-image" src="../assets/2.jpg" alt="成功案例2" />
              </div>
              <div class="swiper-slide">
                <img class="swiper-image" src="../assets/3.jpg" alt="成功案例3" />
              </div>
              <div class="swiper-slide">
                <img class="swiper-image" src="../assets/4.jpg" alt="成功案例4" />
              </div>
            </div>
            <!-- 添加分页指示器 -->
            <div class="swiper-pagination"></div>
          </div>
        </div>
        <!-- 下方图表和板块部分 -->
        <div class="charts-container mt-4 w-full p-4 flex flex-wrap justify-center">
          <!-- 图表部分 -->
          <div class="chart-wrapper">
            <h2 class="chart-title">近期企业加盟数量 - 饼图</h2>
            <canvas id="pieChart"></canvas>
          </div>
          <div class="chart-wrapper">
            <h2 class="chart-title">近期企业加盟数量 - 柱形图</h2>
            <canvas id="barChart"></canvas>
          </div>
          <div class="chart-wrapper">
            <h2 class="chart-title">趋势分析 - 折线图</h2>
            <canvas id="lineChart"></canvas>
          </div>
          <div class="chart-wrapper">
            <h2 class="chart-title">租户上传课程频率 - 散点图</h2>
            <canvas id="scatterChart"></canvas>
          </div>
          <!-- 工作内容板块 -->
          <div class="content-block">
            <div class="content-image">
              <img src="../assets/1.jpg" alt="工作内容示例" />
            </div>
            <div class="content-text">
              <h2>工作内容</h2>
              <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam lobortis libero nec dolor posuere, id mollis urna
                gravida. Cras eget mauris nec magna interdum aliquet.
              </p>
            </div>
          </div>
          <!-- 关于我们板块 -->
          <div class="about-us-block">
            <div class="about-us-text">
              <h2>关于我们</h2>
              <p>
                Vestibulum sit amet libero vel nunc blandit molestie. In hac habitasse platea dictumst. Morbi vitae quam quam. Cras
                nec purus nec lorem efficitur vestibulum.
              </p>
            </div>
            <div class="about-us-image">
              <img src="../assets/2.jpg" alt="关于我们示例" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Swiper from "swiper";
import "swiper/swiper-bundle.css";
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

export default {
  mounted() {
    this.$nextTick(() => {
      new Swiper(".swiper-container", {
        loop: true,
        effect: "fade",
        autoplay: {
          delay: 2500,
          disableOnInteraction: false,
        },
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        fadeEffect: {
          crossFade: true,
        },
      });
      this.renderCharts();
    });
  },
  methods: {
    generateRandomData(numPoints) {
      const data = [];
      for (let i = 0; i < numPoints; i++) {
        data.push({
          x: Math.floor(Math.random() * 100),
          y: Math.floor(Math.random() * 100)
        });
      }
      return data;
    },
    renderCharts() {
      // Pie Chart
      const pieCtx = document.getElementById('pieChart').getContext('2d');
      new Chart(pieCtx, {
        type: 'pie',
        data: {
          labels: ['一月', '二月', '三月', '四月', '五月'],
          datasets: [{
            data: [10, 20, 30, 40, 50],
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF']
          }]
        },
        options: {
          responsive: true,
        }
      });

      // Bar Chart
      const barCtx = document.getElementById('barChart').getContext('2d');
      new Chart(barCtx, {
        type: 'bar',
        data: {
          labels: ['一月', '二月', '三月', '四月', '五月'],
          datasets: [{
            label: '新增企业数',
            data: [10, 20, 30, 40, 50],
            backgroundColor: '#36A2EB'
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });

      // Line Chart
      const lineCtx = document.getElementById('lineChart').getContext('2d');
      new Chart(lineCtx, {
        type: 'line',
        data: {
          labels: ['一月', '二月', '三月', '四月', '五月'],
          datasets: [{
            label: '趋势分析',
            data: [10, 30, 50, 40, 60],
            borderColor: '#36A2EB',
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            fill: true
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });

      // Scatter Chart
      const scatterCtx = document.getElementById('scatterChart').getContext('2d');
      new Chart(scatterCtx, {
        type: 'scatter',
        data: {
          datasets: [{
            label: '租户上传课程频率',
            data: this.generateRandomData(50),
            backgroundColor: '#FF6384'
          }]
        },
        options: {
          responsive: true,
          scales: {
            x: {
              type: 'linear',
              position: 'bottom',
              beginAtZero: true
            },
            y: {
              beginAtZero: true
            }
          }
        }
      });
    }
  }
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&family=Lobster&display=swap");

body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow-x: hidden;
  font-family: "Poppins", sans-serif;
}

.container, .card, .card-body {
  height: 100%;
  width: 100%;
}

p,
h1,
h2,
h3 {
  font-family: "Poppins", sans-serif;
}

h1 {
  font-family: "Lobster", cursive;
}

.fadein-up {
  opacity: 0;
  animation-name: fadeInUp;
  animation-duration: 0.5s;
  animation-fill-mode: forwards;
  animation-delay: 500ms;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translate3d(0, 15px, 0);
  }
  to {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
}

.fadein-right {
  opacity: 0;
  animation: fadeInRight 0.5s ease-out forwards;
  animation-delay: 500ms;
}

@keyframes fadeInRight {
  0% {
    opacity: 0;
    transform: translateX(100%);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

.photo-section {
  position: relative;
  width: 100%;
  height: 60vh; /* 设置图片部分的高度 */
  border: 1px solid #ddd; /* 添加边框 */
  border-radius: 10px; /* 添加圆角 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 添加阴影 */
  overflow: hidden; /* 确保内容不会溢出 */
}

.swiper-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.swiper-slide {
  display: flex;
  justify-content: center;
  align-items: flex-start; /* 顶部对齐 */
  width: 100%;
  height: 100%;
}

.swiper-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: top; /* 使图片在框内置顶 */
}

.swiper-pagination {
  bottom: 10px;
}

.swiper-pagination-bullet {
  width: 12px;
  height: 12px;
  background: #ffffff;
  opacity: 1;
  border: 2px solid #007bff;
}

.swiper-pagination-bullet-active {
  background: #007bff;
}

.charts-container {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  padding: 20px;
}

.chart-wrapper {
  width: 45%;
  margin: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chart-title {
  text-align: center;
  font-size: 1.2em;
  margin-bottom: 10px;
}

canvas {
  width: 85% !important;
  height: 300px !important;
  margin: auto;
}

.content-block, .about-us-block {
  width: 100%;
  margin: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: row;
  align-items: center;
}

.content-text, .about-us-text {
  width: 45%;
  padding: 0 20px;
}

.content-image, .about-us-image {
  width: 45%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 20px;
}

.content-image img, .about-us-image img {
  max-width: 100%;
  height: auto;
}
</style>
