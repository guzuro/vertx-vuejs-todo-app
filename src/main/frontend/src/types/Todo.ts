/* eslint-disable camelcase */
export type ICommentary = {
    id?:number
    text: string
    todo_id:number
    created_at: string
}

export type ITodo = {
    id?: number
    title:string
    description: string
}
