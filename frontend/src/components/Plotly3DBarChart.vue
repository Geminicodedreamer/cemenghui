<template>
  <div ref="chartContainer" class="plotly-chart-container"></div>
</template>

<script>
import Plotly from 'plotly.js-dist-min';

export default {
  mounted() {
    this.initPlotlyChart();
  },
  methods: {
    initPlotlyChart() {
      const trace1 = {
        x: ['一月', '二月', '三月', '四月', '五月'], // x 轴标签
        y: [1, 2, 3, 4, 5], // y 轴标签 (这里可以是类别或其他描述)
        z: [10, 20, 30, 40, 50], // z 轴数据
        type: 'bar3d',
        opacity: 0.8,
        marker: {
          color: 'rgb(255,127,14)',
          line: {
            width: 1.5
          }
        }
      };

      const data = [trace1];

      const layout = {
        title: '3D柱状图',
        scene: {
          xaxis: { title: '月份' },
          yaxis: { title: '类别' },
          zaxis: { title: '数量' }
        },
        margin: {
          l: 0,
          r: 0,
          b: 0,
          t: 0
        }
      };

      Plotly.newPlot(this.$refs.chartContainer, data, layout);
    }
  }
};
</script>

<style scoped>
.plotly-chart-container {
  width: 100%;
  height: 400px;
}
</style>
