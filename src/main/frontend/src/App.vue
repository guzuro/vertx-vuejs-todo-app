<template>
  <div id="app" class="mt-5">
    <div class="container">
      <div class="columns">
        <div class="column is-half is-offset-one-quarter">
          <todo-form @addTodo="addTodo" />
          <div v-if="todos.length" class="mt-5">
            <p class="is-size-5 has-text-centered mb-5">Список задач</p>
            <div
              class="todo_item card mb-5"
              v-for="(todo, index) in todos"
              :key="index"
            >
              <todo-card :todo="todo" @deleteTodo="deleteTodo" />
            </div>
          </div>
          <div v-else>Список пуст</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import TodoForm from './components/TodoForm.vue'
import { ITodo } from './types/Todo'
import Api from './Api'
import TodoCard from './components/TodoCard.vue'
import { errorNotification, successNotification } from './utils/NotificationService'

@Component({
  components: {
    TodoForm,
    TodoCard
  }
})
export default class App extends Vue {
  todos: Array<ITodo> = [];

  async addTodo (todo: ITodo): Promise<void> {
    const loader = this.$buefy.loading.open({})
    try {
      const data: ITodo = await Api.addTodo(todo)
      this.todos.push(data)
      successNotification('Добавлено')
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => { loader.close() }, 500)
    }
  }

  async deleteTodo (todoId: number): Promise<void> {
    const loader = this.$buefy.loading.open({})
    try {
      await Api.deleteTodo(todoId)
      this.todos = this.todos.filter((todo: ITodo) => todo.id !== todoId)
      successNotification('Удалено')
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => { loader.close() }, 500)
    }
  }

  async mounted (): Promise<void> {
    const loader = this.$buefy.loading.open({})
    try {
      const data: Array<ITodo> = await Api.getTodos()
      this.todos = data
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => { loader.close() }, 500)
    }
  }
}
</script>

<style lang="scss">
* {
  -moz-box-sizing: border-box;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
</style>
