<template>
  <div class="todo">

        <div class="card todo__form">
          <header class="card-header">
            <h3 class="card-header-title">Добавить задачу</h3>
          </header>
          <div class="card-content">
            <b-field label="Название">
              <b-input v-model="todo.title"></b-input>
            </b-field>
            <b-field label="Описание">
              <b-input v-model="todo.description"></b-input>
            </b-field>
            <b-button
              :disabled="isAddButtonDisabled"
              class="is-success"
              @click="addTodo"
            >
              Добавить
            </b-button>
          </div>
        </div>
      </div>

</template>

<script lang="ts">
/* eslint-disable */
import Vue from 'vue'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator';
import { ITodo } from '../types/Todo'

@Component
export default class TodoForm extends Vue {

  todo: ITodo = {
    title: '',
    description: ''
  };

  addTodo ():void {
    this.$emit('addTodo', this.todo)
    this.todo.title = ''
    this.todo.description = ''
  }

  get isAddButtonDisabled (): boolean {
    return !this.todo.title.length || !this.todo.description.length
  }
}
</script>
