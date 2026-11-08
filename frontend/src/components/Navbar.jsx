import { Link, NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useTheme } from '../context/ThemeContext'

export default function Navbar() {
  const { isAuthenticated, username, logout } = useAuth()
  const { theme, toggleTheme } = useTheme()

  return (
    <header className="sticky top-0 z-30 border-b border-stone-200/80 bg-paper/85 backdrop-blur-md dark:border-stone-800 dark:bg-[#121110]/85">
      <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 py-4">
        <Link to="/" className="flex items-baseline gap-2">
          <span className="font-serif text-3xl tracking-tight text-clay">Lumen</span>
          <span className="hidden text-sm text-stone-500 sm:inline dark:text-stone-400">blog</span>
        </Link>

        <nav className="flex items-center gap-2 sm:gap-3">
          <NavLink
            to="/"
            className={({ isActive }) =>
              `rounded-full px-3 py-1.5 text-sm ${isActive ? 'bg-mist text-ink dark:bg-stone-800 dark:text-stone-100' : 'text-stone-600 hover:text-ink dark:text-stone-400 dark:hover:text-stone-100'}`
            }
          >
            Feed
          </NavLink>

          {isAuthenticated && (
            <NavLink
              to="/create"
              className={({ isActive }) =>
                `rounded-full px-3 py-1.5 text-sm ${isActive ? 'bg-clay text-white' : 'bg-ink text-white hover:bg-stone-800 dark:bg-stone-100 dark:text-ink dark:hover:bg-white'}`
              }
            >
              Yeni post
            </NavLink>
          )}

          <button
            type="button"
            onClick={toggleTheme}
            className="rounded-full border border-stone-200 px-3 py-1.5 text-sm text-stone-600 hover:bg-mist dark:border-stone-700 dark:text-stone-300 dark:hover:bg-stone-800"
            aria-label="Tema dəyiş"
          >
            {theme === 'dark' ? 'İşıq' : 'Qaranlıq'}
          </button>

          {isAuthenticated ? (
            <div className="flex items-center gap-2">
              <span className="hidden max-w-28 truncate text-sm text-stone-500 sm:inline dark:text-stone-400">
                {username}
              </span>
              <button
                type="button"
                onClick={logout}
                className="rounded-full px-3 py-1.5 text-sm text-stone-600 hover:text-clay dark:text-stone-300"
              >
                Çıxış
              </button>
            </div>
          ) : (
            <Link
              to="/login"
              className="rounded-full border border-stone-300 px-3 py-1.5 text-sm hover:border-clay hover:text-clay dark:border-stone-600"
            >
              Daxil ol
            </Link>
          )}
        </nav>
      </div>
    </header>
  )
}
