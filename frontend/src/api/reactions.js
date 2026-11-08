import { api } from './client'

export async function reactToPost(postId, type) {
  const { data } = await api.post(`/api/reactions/posts/${postId}`, null, {
    params: { type },
  })
  return data
}

export async function reactToComment(commentId, type) {
  const { data } = await api.post(`/api/reactions/comments/${commentId}`, null, {
    params: { type },
  })
  return data
}
