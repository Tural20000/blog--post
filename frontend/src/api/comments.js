import { api } from './client'

export async function fetchComments(postId, { page = 0, size = 10 } = {}) {
  const { data } = await api.get(`/api/posts/${postId}/comments`, {
    params: { page, size, sortBy: 'createdAt', direction: 'DESC' },
  })
  return {
    items: data.content ?? [],
    page: data.page?.number ?? data.number ?? 0,
    totalPages: data.page?.totalPages ?? data.totalPages ?? 0,
    totalElements: data.page?.totalElements ?? data.totalElements ?? 0,
    last: data.last ?? (data.page ? data.page.number + 1 >= data.page.totalPages : true),
  }
}

export async function addComment(postId, content) {
  const { data } = await api.post(`/api/posts/${postId}/comments`, { content })
  return data
}

export async function deleteComment(commentId) {
  const { data } = await api.delete(`/api/posts/comments/${commentId}`)
  return data
}
