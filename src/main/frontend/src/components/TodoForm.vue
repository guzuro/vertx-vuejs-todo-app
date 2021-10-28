<template>
  <div class="todo">
    <div class="card todo__form">
      <header class="card-header">
        <h3 class="card-header-title">{{ modalTitle }}</h3>
      </header>
      <div class="card-content">
        <b-field label="Название">
          <b-input v-model="todo.title"></b-input>
        </b-field>
        <b-field label="Описание">
          <b-input type="textarea" v-model="todo.description"></b-input>
        </b-field>
        <b-button
          :disabled="isAddButtonDisabled"
          class="is-success"
          @click="handleSaveButtonClick"
        >
          Сохранить
        </b-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
/* eslint-disable */
import { FormType } from "@/types/FormType";
import Vue from "vue";
import Component from "vue-class-component";
import { Prop } from "vue-property-decorator";
import { ITodo } from "../types/Todo";

@Component
export default class TodoForm extends Vue {
  @Prop() todoToEdit!: ITodo;

  @Prop() formType!: string;

  todo: ITodo = {
    title: "",
    description: "",
  };

  handleSaveButtonClick():void {
    if (this.formType === FormType.Create) {
      this.addTodo()
    } if (this.formType === FormType.Edit) {
      this.$emit('editTodo', this.todo)
    }
  }

  addTodo(): void {
    this.$emit("addTodo", this.todo);
    this.todo.title = "";
    this.todo.description = "";
  }

  mounted(): void {
    if (this.todoToEdit.id) {
    this.todo = this.todoToEdit;
    }
  }

  get modalTitle(): string {
    if (this.formType === "create") {
      return "Создать";
    }
    if (this.formType === "edit") {
      return "Редактировать";
    }
    return "";
  }

  get isAddButtonDisabled(): boolean {
    return !this.todo.title.length || !this.todo.description.length;
  }
}
</script>
