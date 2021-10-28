<template>
  <div class="commentary_item mb-2">
    <div class="card relative">
      <div class="card-header">
        <div
          class="is-clickable"
          style="position: absolute; right: 10px; top:10px"
          @click="$emit('removeCommentary', commentary.id)"
        >
      <b-icon icon="delete" size="is-small" type="is-danger"> </b-icon>
        </div>
        <div style="position: absolute; left: 10px; top:10px">
          {{ commentary.created_at }}
        </div>
      </div>
      <div class="card-content mt-5 ">
        {{ commentary.text }}
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ICommentary } from '@/types/Todo'
import Vue from 'vue'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'

@Component
export default class CommentaryItem extends Vue {
  @Prop() commentary!: ICommentary;
}
</script>
