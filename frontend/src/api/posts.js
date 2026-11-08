import { api } from './client'

export async function fetchPosts() {
  const { data } = await api.get('/api/posts')
  return data
}

export async function searchPosts(keyword) {
  const { data } = await api.get('/api/posts/search', { params: { keyword } })
  return data
}

export async function fetchPostById(id) {
  const { data } = await api.get(`/api/posts/${id}`)
  return data
}

export async function createPost({ title, content, file }) {
  const formData = new FormData()
  formData.append('title', title)
  formData.append('content', content)
  if (file) {
    formData.append('file', file)
  }
  const { data } = await api.post('/api/posts', formData)
  return data
}

export async function updatePost(id, { title, content }) {
  const { data } = await api.put(`/api/posts/${id}`, { title, content })
  return data
}

export async function deletePost(id) {
  const { data } = await api.delete(`/api/posts/${id}`)
  return data
}
