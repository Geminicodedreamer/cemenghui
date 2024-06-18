<template>
    <ContentField>
		<NewsManagement></NewsManagement>
	</ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import NewsManagement from './NewsManagement.vue'

export default {
    components: {
        ContentField,
		NewsManagement,
    }
}
</script>

<style scoped>
</style>