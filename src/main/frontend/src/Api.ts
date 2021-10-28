/* eslint-disable */

import Vue from 'vue'
import { ICommentary, ITodo } from './types/Todo'

export default class Api {
    private static apiURL = 'http://localhost:8080'

    static getTodos ():Promise<Array<ITodo>> {
      return new Promise((resolve, reject) => {
        Vue.prototype.axios.get(Api.apiURL)
          .then(({data}:any) => {
          resolve(data)
        }).catch((error:any)=> {
          reject(error)
        })
      })
    }

    static addTodo (todo: ITodo):Promise<ITodo> {
      return new Promise((resolve, reject) => {
        Vue.prototype.axios.post(Api.apiURL, {...todo})
        .then(({data}:any) => {
          resolve(data)
        }).catch((error:any)=> {
          reject(error)
        })
      })
    }

    static editTodo (todo: ITodo):Promise<ITodo> {
      return new Promise((resolve, reject) => {
        Vue.prototype.axios.post(Api.apiURL, {...todo})
        .then(({data}:any) => {
          resolve(data)
        }).catch((error:any)=> {
          reject(error)
        })
      })
    }

      static async deleteTodo (todoId:number):Promise<{result: string}> {
        return new Promise((resolve, reject) => {
          Vue.prototype.axios.delete(`${Api.apiURL}/${todoId}`)
          .then((response:any) => {
          resolve(response)
        }).catch((error:any)=> {
          reject(error)
        })
      })
    }

    
      static async loadComments (todo_id:number):Promise<Array<ICommentary>> {
        return new Promise((resolve, reject) => {
          Vue.prototype.axios.post(`${Api.apiURL}/commentaries`, {todo_id})
          .then(({data}:any) => {
          resolve(data)
        }).catch((error:any)=> {
          reject(error)
        })
      })
    }

    static async addCommentary (commentary:ICommentary):Promise<ICommentary> {
      return new Promise((resolve, reject) => {
        Vue.prototype.axios.post(`${Api.apiURL}/addcommentary`, {...commentary})
        .then(({data}:any) => {
        resolve(data)
      }).catch((error:any)=> {
        reject(error)
      })
    })
  }

    static async removeCommentary (commentaryId:number):Promise<void> {
      return new Promise((resolve, reject) => {
        Vue.prototype.axios.delete(`${Api.apiURL}/commentary/${commentaryId}`)
        .then((response:any) => {
        resolve(response)
      }).catch((error:any)=> {
        reject(error)
      })
    })
  }
}
