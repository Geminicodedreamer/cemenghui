<template>
    <ContentField>行业动态</ContentField>
	<IndustryListView />
	<ModifyNewsDialog />
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import IndustryListView from './IndustryListView.vue'
import ModifyNewsDialog from '../../components/ModifyNewsDialog.vue'

export default {
    components: {
        ContentField,
		IndustryListView,
		ModifyNewsDialog,
    }
}
</script>

<style scoped>
</style>