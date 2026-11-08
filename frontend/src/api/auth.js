import { api, persistSession } from './client'

export async function registerUser({ username, password }) {
  const { data } = await api.post('/api/auth/register', { username, password })
  return data
}

export async function loginUser({ username, password }) {
  const { data } = await api.post('/api/auth/login', { username, password })
  persistSession(data.accessToken, username)
  return data
}
