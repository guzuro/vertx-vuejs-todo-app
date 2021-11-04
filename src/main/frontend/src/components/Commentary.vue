<template>
  <div class="commentary">
    <b-field label="Добавить комментарий">
      <b-input
        type="textarea"
        v-model="commentary"
        @keypress.enter="addCommentary"
      ></b-input>
    </b-field>
    <b-button :disabled="!commentary" class="is-success" @click="addCommentary">
      Добавить комментарий
    </b-button>
    <div v-if="commentaries.length" class="mt-5">
    <div v-for="(commentary, index) in commentaries" :key="index">
      <commentary-item
        :commentary="commentary"
        @removeCommentary="removeCommentary"
      />
    </div>
    </div>
    <div v-else class="mt-5 is-size-5 has-text-centered">Комментариев пока нет...</div>
  </div>
</template>

<script lang="ts">
import { ICommentary } from '@/types/Todo'
import { errorNotification, successNotification } from '@/utils/NotificationService'
import Vue from 'vue'
import Component from 'vue-class-component'
import { Prop, Watch } from 'vue-property-decorator'
import Api from '../Api'
import CommentaryItem from './CommentaryItem.vue'
import moment from 'moment'

@Component({
  components: {
    CommentaryItem
  }
})
export default class Commentary extends Vue {
  commentary = '';

  commentaries: Array<ICommentary> = [];

  @Prop({ default: 0 }) selectedTodoId!: number;

  @Watch('selectedTodoId')
  onSelectedTodoIdChange (value: number): void {
    this.loadComments(value)
  }

  async loadComments (selectedTodoId: number): Promise<void> {
    this.commentaries = []
    const loader = this.$buefy.loading.open({})
    try {
      const response = await Api.loadComments(selectedTodoId)
      const commentaries = response.map((c:any) => {
        if (c.created_at) {
          c.created_at = moment(c.created_at.split('T').join(', ')).format('LLL')
        }
        return c
      })
      this.commentaries.push(...commentaries)
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => {
        loader.close()
      }, 500)
    }
  }

  async addCommentary (): Promise<void> {
    const loader = this.$buefy.loading.open({})
    const date = moment().format('YYYY-MM-DDTHH:mm:ss')

    const commentary: ICommentary = {
      text: this.commentary,
      todo_id: this.selectedTodoId,
      created_at: date
    }
    try {
      await Api.addCommentary(commentary)
      await this.loadComments(commentary.todo_id)
      successNotification('Комментарий добавлен')
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => {
        this.commentary = ''
        loader.close()
      }, 500)
    }
  }

  async removeCommentary (commentaryId: number): Promise<void> {
    const loader = this.$buefy.loading.open({})
    try {
      await Api.removeCommentary(commentaryId)
      this.loadComments(this.selectedTodoId)
      successNotification('Комментарий удален')
    } catch (error: any) {
      errorNotification(error)
    } finally {
      setTimeout(() => {
        loader.close()
      }, 500)
    }
  }

  mounted (): void {
    this.loadComments(this.selectedTodoId)
  }
}
</script>
