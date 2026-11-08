import { createContext, useContext, useMemo, useState } from 'react'
import { clearSession, getStoredToken, getStoredUsername } from '../api/client'
import { loginUser, registerUser } from '../api/auth'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => getStoredToken())
  const [username, setUsername] = useState(() => getStoredUsername())

  const value = useMemo(
    () => ({
      token,
      username,
      isAuthenticated: Boolean(token),
      async login(credentials) {
        await loginUser(credentials)
        setToken(getStoredToken())
        setUsername(credentials.username)
      },
      async register(credentials) {
        await registerUser(credentials)
        await loginUser(credentials)
        setToken(getStoredToken())
        setUsername(credentials.username)
      },
      logout() {
        clearSession()
        setToken(null)
        setUsername(null)
      },
    }),
    [token, username],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth AuthProvider daxilində istifadə olunmalıdır')
  }
  return context
}
