
<template>
  <span class="os-icon-wrapper" :style="style">
    <fa-icon :icon="name" />
  </span>
</template>

<script>
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

export default {
  props: ['name', 'size'],

  components: {
    'fa-icon': FontAwesomeIcon
  },

  computed: {
    style: function() {
      if (this.size) {
        return {'font-size': this.size};
      }

      return {};
    }
  }
}
</script>

<style scoped>
  .os-icon-wrapper.pad-left {
    padding-left: 4px;
  }

  .os-icon-wrapper.pad-right {
    padding-right: 4px;
  }
</style>
