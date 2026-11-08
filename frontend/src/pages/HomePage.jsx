import { useEffect, useState } from 'react'
import { fetchPosts, searchPosts } from '../api/posts'
import Alert from '../components/Alert'
import PostCard from '../components/PostCard'
import Spinner from '../components/Spinner'

export default function HomePage() {
  const [posts, setPosts] = useState([])
  const [keyword, setKeyword] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [searching, setSearching] = useState(false)

  useEffect(() => {
    let cancelled = false
    async function load() {
      setLoading(true)
      setError('')
      try {
        const data = await fetchPosts()
        if (!cancelled) setPosts(data)
      } catch (err) {
        if (!cancelled) setError(err.message)
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [])

  async function handleSearch(event) {
    event.preventDefault()
    const query = keyword.trim()
    setSearching(true)
    setError('')
    try {
      const data = query ? await searchPosts(query) : await fetchPosts()
      setPosts(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setSearching(false)
    }
  }

  return (
    <div className="space-y-8">
      <section className="flex flex-col gap-6 md:flex-row md:items-end md:justify-between">
        <div>
          <p className="text-sm uppercase tracking-[0.25em] text-clay">Kəşf et</p>
          <h1 className="mt-2 font-serif text-5xl leading-none">Bütün yazılar</h1>
          <p className="mt-3 max-w-xl text-stone-600 dark:text-stone-400">
            AI xülasə, teqlər və SEO təsviri ilə səliqəli oxu lenti.
          </p>
        </div>

        <form onSubmit={handleSearch} className="flex w-full max-w-md gap-2">
          <input
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            placeholder="Açar söz ilə axtar..."
            className="w-full rounded-2xl border border-stone-200 bg-white px-4 py-3 outline-none focus:border-clay dark:border-stone-700 dark:bg-[#1a1917]"
          />
          <button
            type="submit"
            disabled={searching}
            className="rounded-2xl bg-ink px-5 py-3 text-white disabled:opacity-60 dark:bg-stone-100 dark:text-ink"
          >
            {searching ? '...' : 'Axtar'}
          </button>
        </form>
      </section>

      {error && <Alert>{error}</Alert>}
      {loading ? (
        <Spinner label="Postlar yüklənir..." />
      ) : posts.length === 0 ? (
        <div className="rounded-3xl border border-dashed border-stone-300 px-6 py-16 text-center text-stone-500 dark:border-stone-700">
          Hələ heç bir post tapılmadı.
        </div>
      ) : (
        <div className="grid gap-6 md:grid-cols-2">
          {posts.map((post) => (
            <PostCard key={post.id} post={post} />
          ))}
        </div>
      )}
    </div>
  )
}
