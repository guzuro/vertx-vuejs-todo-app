<template>
  <div id="app" class="mt-5">
    <div class="container">
      <div class="columns">
        <div class="column is-three-fifths">
          <b-button
            class="is-primary"
            @click="
              (isComponentModalActive = !isComponentModalActive),
                (formType = 'create')
            "
          >
            Создать новую задачу
          </b-button>
          <div v-if="todos.length" class="mt-5">
            <p class="is-size-5 has-text-centered mb-5">Список задач</p>
            <div
              class="todo_item card mb-5"
              v-for="(todo, index) in todos"
              :key="index"
              @click="selectedTodo = todo.id"
            >
              <todo-card
                :todo="todo"
                @deleteTodo="deleteTodo"
                @openEditTodoModal="openEditTodoModal"
              />
            </div>
          </div>
          <div v-else>Список пуст</div>
        </div>
        <div class="column">
          <commentary v-if="selectedTodo" :selectedTodoId="selectedTodo"/>
        </div>
      </div>
    </div>
    <b-modal
      v-model="isModalActive"
      :destroy-on-hide="true"
      aria-role="dialog"
      aria-label="todo-form"
      aria-modal
    >
      <template #default="props">
        <todo-form
          :form-type="formType"
          @addTodo="addTodo"
          @editTodo="editTodo"
          @close="props.close"
          :todo-to-edit="todoToEdit"
        ></todo-form>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import TodoForm from './components/TodoForm.vue'
import { ITodo } from './types/Todo'
import Api from './Api'
import TodoCard from './components/TodoCard.vue'
import {
  errorNotification,
  successNotification
} from './utils/NotificationService'
import { FormType } from './types/FormType'
import Commentary from './components/Commentary.vue'

@Component({
  components: {
    TodoForm,
    TodoCard,
    Commentary
  }
})
export default class App extends Vue {
  todos: Array<ITodo> = [];

  todoToEdit = {} as ITodo;

  formType = '';

  selectedTodo: number | null = null;

  isModalActive = false;

  @Watch('isModalActive')
  onComponentModalActiveChange (value: boolean): void {
    if (!value) this.todoToEdit = { title: '', description: '' } as ITodo
  }

  async addTodo (todo: ITodo): Promise<void> {
    const loader = this.$buefy.loading.open({})
    try {
      const data: ITodo = await Api.addTodo(todo)
      this.todos.push(data)
      successNotification('Добавлено')
    } catch (error: any) {
      errorNotification(error)
    } finally {
      this.isModalActive = false
      setTimeout(() => {
        loader.close()
      }, 500)
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
      setTimeout(() => {
        loader.close()
      }, 500)
    }
  }

  async editTodo (todoToEdit: ITodo): Promise<void> {
    const loader = this.$buefy.loading.open({})
    try {
      const updatedTodo = await Api.editTodo(todoToEdit)
      this.todos = this.todos.map((todo: ITodo) => {
        if (updatedTodo.id === todo.id) {
          todo.title = updatedTodo.title
          todo.description = updatedTodo.description
        }
        return todo
      })
      this.isModalActive = false
      successNotification('Обновлено')
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => {
        loader.close()
      }, 500)
    }
  }

  openEditTodoModal (todoFromCard: ITodo): void {
    this.todoToEdit = todoFromCard
    this.formType = FormType.Edit
    this.isModalActive = true
  }

  async mounted (): Promise<void> {
    const loader = this.$buefy.loading.open({})
    try {
      const data: Array<ITodo> = await Api.getTodos()
      this.todos = data
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => {
        loader.close()
      }, 500)
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

.todo_item {
  &:hover {
    cursor: pointer;
  .card {
        opacity: 0.7;
  }
  }
}
</style>
