import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createPost } from '../api/posts'
import Alert from '../components/Alert'

export default function CreatePostPage() {
  const navigate = useNavigate()
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const [file, setFile] = useState(null)
  const [preview, setPreview] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  function handleFileChange(event) {
    const nextFile = event.target.files?.[0] ?? null
    setFile(nextFile)
    setPreview(nextFile ? URL.createObjectURL(nextFile) : null)
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      await createPost({ title, content, file })
      navigate('/', { replace: true })
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="mx-auto max-w-2xl">
      <p className="text-sm uppercase tracking-[0.25em] text-clay">Yarat</p>
      <h1 className="mt-2 font-serif text-5xl">Yeni post</h1>
      <p className="mt-3 text-stone-600 dark:text-stone-400">
        Başlıq, mətn və istəyə bağlı şəkil. Backend AI xülasə, teq və SEO təsvirini özü yaradacaq.
      </p>

      <form
        onSubmit={handleSubmit}
        className="mt-8 space-y-5 rounded-3xl border border-stone-200 bg-white p-6 shadow-sm dark:border-stone-800 dark:bg-[#1a1917] sm:p-8"
      >
        {error && <Alert>{error}</Alert>}

        <label className="block text-sm font-medium">
          Başlıq
          <input
            required
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="mt-1 w-full rounded-2xl border border-stone-200 bg-paper px-4 py-3 outline-none focus:border-clay dark:border-stone-700 dark:bg-stone-900"
          />
        </label>

        <label className="block text-sm font-medium">
          Məzmun
          <textarea
            required
            rows={10}
            value={content}
            onChange={(e) => setContent(e.target.value)}
            className="mt-1 w-full resize-y rounded-2xl border border-stone-200 bg-paper px-4 py-3 outline-none focus:border-clay dark:border-stone-700 dark:bg-stone-900"
          />
        </label>

        <label className="block text-sm font-medium">
          Şəkil
          <input
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            className="mt-2 block w-full text-sm file:mr-3 file:rounded-full file:border-0 file:bg-mist file:px-4 file:py-2 file:text-ink dark:file:bg-stone-800 dark:file:text-stone-100"
          />
        </label>

        {preview && (
          <img src={preview} alt="Önizləmə" className="max-h-64 w-full rounded-2xl object-cover" />
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-2xl bg-clay py-3 font-medium text-white disabled:opacity-60"
        >
          {loading ? 'Yüklənir...' : 'Paylaş'}
        </button>
      </form>
    </div>
  )
}
