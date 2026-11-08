import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import Alert from '../components/Alert'
import { useAuth } from '../context/AuthContext'

export default function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      await register(form)
      navigate('/', { replace: true })
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="mx-auto max-w-md">
      <div className="rounded-3xl border border-stone-200 bg-white p-8 shadow-sm dark:border-stone-800 dark:bg-[#1a1917]">
        <p className="text-sm uppercase tracking-[0.2em] text-clay">Yeni hesab</p>
        <h1 className="mt-2 font-serif text-4xl">Qeydiyyat</h1>
        <p className="mt-2 text-stone-500 dark:text-stone-400">Qeydiyyatdan sonra avtomatik daxil olacaqsan.</p>

        <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
          {error && <Alert>{error}</Alert>}
          <label className="block text-sm font-medium">
            İstifadəçi adı
            <input
              required
              minLength={3}
              value={form.username}
              onChange={(e) => setForm((f) => ({ ...f, username: e.target.value }))}
              className="mt-1 w-full rounded-2xl border border-stone-200 bg-paper px-4 py-3 outline-none focus:border-clay dark:border-stone-700 dark:bg-stone-900"
            />
          </label>
          <label className="block text-sm font-medium">
            Şifrə
            <input
              required
              minLength={4}
              type="password"
              value={form.password}
              onChange={(e) => setForm((f) => ({ ...f, password: e.target.value }))}
              className="mt-1 w-full rounded-2xl border border-stone-200 bg-paper px-4 py-3 outline-none focus:border-clay dark:border-stone-700 dark:bg-stone-900"
            />
          </label>
          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-2xl bg-clay py-3 font-medium text-white disabled:opacity-60"
          >
            {loading ? 'Yaradılır...' : 'Hesab yarat'}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-stone-500">
          Artıq hesabın var?{' '}
          <Link to="/login" className="text-clay hover:underline">
            Daxil ol
          </Link>
        </p>
      </div>
    </div>
  )
}
