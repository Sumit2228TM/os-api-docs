
import authSvc from '@/common/services/Authorization.js';

export default {
  mounted(el, binding) {
    if (binding.value && !authSvc.isAllowed(binding.value)) {
      el.classList.add('os-hide');
    } else {
      el.classList.remove('os-hide');
    }
  }
}
