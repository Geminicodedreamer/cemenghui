<template>
  <div>
    <ContentField> 	
	<ImageCarousel />
	</ContentField>
  </div>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import ImageCarousel from '../../components/ImageCarousel.vue'

export default {
  components: {
    ContentField,
    ImageCarousel,
  }
}
</script>

<style scoped>
</style>
