import Vue from 'vue'

const errorNotification = (_error:any):void => {
  const errorMessage = _error.response.message
  Vue.prototype.$buefy.toast.open({
    duration: 5000,
    message: errorMessage,
    position: 'is-bottom',
    type: 'is-danger'
  })
}

const successNotification = (message:string):void => {
  Vue.prototype.$buefy.toast.open({
    duration: 5000,
    message: message,
    position: 'is-bottom',
    type: 'is-success'
  })
}
export { errorNotification, successNotification }
