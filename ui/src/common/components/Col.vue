
<template>
  <div :class="widthClass">
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    width: {
      type: Number,
      default: 12
    }
  },

  computed: {
    widthClass() {
      return 'p-col-' + this.width + ' p-md-' + this.width;
    }
  }
}
</script>
