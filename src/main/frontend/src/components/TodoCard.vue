<template>
  <div class="card mb-5 relative">
    <div class="card__remove-btn" @click="$emit('deleteTodo', todo.id)">
      <b-icon icon="delete" size="is-small" type="is-danger"> </b-icon>
    </div>
    <div
      class="card__edit-btn"
      @click="$emit('openEditTodoModal', todo)"
    >
      <b-icon icon="pencil" size="is-small" type="is-warning"> </b-icon>
    </div>
    <div class="card-header mt-2 ml-2 has-text-weight-medium">
      {{ todo.title }}
    </div>
    <div class="card-content">
      {{ todo.description }}
    </div>
  </div>
</template>

<script lang="ts">
import { ITodo } from '@/types/Todo'
import Vue from 'vue'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'

@Component
export default class TodoCard extends Vue {
  @Prop() todo!: ITodo;
}
</script>

<style lang="scss">
.card {
  &__remove-btn, &__edit-btn {
    position: absolute;
    cursor: pointer;
    &:hover {
      opacity: 0.7;
    }
  }
  &__remove-btn {
    right: 5px;
  }

  &__edit-btn {
    right:20px
  }
}
</style>
