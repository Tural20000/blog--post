import axios from 'axios'

const TOKEN_KEY = 'blog_access_token'
const USER_KEY = 'blog_username'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? '',
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  if (config.data instanceof FormData) {
    delete config.headers['Content-Type']
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.message ||
      error.response?.data ||
      error.message ||
      'Gözlənilməz xəta baş verdi'
    const normalized = typeof message === 'string' ? message : JSON.stringify(message)
    return Promise.reject(new Error(normalized))
  },
)

export function getStoredToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function getStoredUsername() {
  return localStorage.getItem(USER_KEY)
}

export function persistSession(token, username) {
  localStorage.setItem(TOKEN_KEY, token)
  if (username) {
    localStorage.setItem(USER_KEY, username)
  }
}

export function clearSession() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

export function resolveImageUrl(imageUrl) {
  if (!imageUrl) return null
  if (imageUrl.startsWith('http://') || imageUrl.startsWith('https://')) {
    return imageUrl
  }
  const base = import.meta.env.VITE_API_URL ?? ''
  return `${base}${imageUrl}`
}
