
<template>
  <div class="p-grid p-fluid">
    <slot></slot>
  </div>
</template>

<script>
export default {
}
</script>

<style scoped>
  .p-grid {
    margin-bottom: 20px;
  }

  [dense].p-grid {
    margin-bottom: 10px;
  }

  [dense].p-grid :deep(.p-float-label input:focus ~ label), 
  [dense].p-grid :deep(.p-float-label input.p-filled ~ label), 
  [dense].p-grid :deep(.p-float-label textarea:focus ~ label), 
  [dense].p-grid :deep(.p-float-label textarea.p-filled ~ label), 
  [dense].p-grid :deep(.p-float-label .p-inputwrapper-focus ~ label), 
  [dense].p-grid :deep(.p-float-label .p-inputwrapper-filled ~ label) {
    top: -.25rem;
  }
</style>
