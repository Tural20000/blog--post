import { useCallback, useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { addComment, fetchComments } from '../api/comments'
import { resolveImageUrl } from '../api/client'
import { deletePost, fetchPostById } from '../api/posts'
import { reactToComment, reactToPost } from '../api/reactions'
import Alert from '../components/Alert'
import Spinner from '../components/Spinner'
import { useAuth } from '../context/AuthContext'
import { formatDate, parseTags } from '../utils/format'

export default function PostDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { isAuthenticated, username } = useAuth()
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [page, setPage] = useState(0)
  const [hasMore, setHasMore] = useState(false)
  const [commentText, setCommentText] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [commentError, setCommentError] = useState('')
  const [commentSuccess, setCommentSuccess] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const loadPost = useCallback(async () => {
    const data = await fetchPostById(id)
    setPost(data)
  }, [id])

  const loadComments = useCallback(
    async (nextPage = 0, append = false) => {
      const data = await fetchComments(id, { page: nextPage, size: 8 })
      setComments((current) => (append ? [...current, ...data.items] : data.items))
      setPage(data.page)
      setHasMore(data.page + 1 < data.totalPages)
    },
    [id],
  )

  useEffect(() => {
    let cancelled = false
    async function load() {
      setLoading(true)
      setError('')
      try {
        await loadPost()
        await loadComments(0, false)
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
  }, [loadComments, loadPost])

  async function handleReaction(type) {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: `/posts/${id}` } })
      return
    }
    try {
      await reactToPost(id, type)
      await loadPost()
    } catch (err) {
      setError(err.message)
    }
  }

  async function handleCommentReaction(commentId, type) {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: `/posts/${id}` } })
      return
    }
    try {
      await reactToComment(commentId, type)
      await loadComments(0, false)
    } catch (err) {
      setCommentError(err.message)
    }
  }

  async function handleDelete() {
    if (!window.confirm('Bu postu silmək istəyirsən?')) return
    try {
      await deletePost(id)
      navigate('/', { replace: true })
    } catch (err) {
      setError(err.message)
    }
  }

  async function handleCommentSubmit(event) {
    event.preventDefault()
    setCommentError('')
    setCommentSuccess('')
    if (!isAuthenticated) {
      navigate('/login', { state: { from: `/posts/${id}` } })
      return
    }
    setSubmitting(true)
    try {
      await addComment(id, commentText)
      setCommentText('')
      setCommentSuccess('Komment əlavə olundu.')
      await loadComments(0, false)
    } catch (err) {
      setCommentError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) return <Spinner label="Post açılır..." />
  if (error && !post) return <Alert>{error}</Alert>
  if (!post) return null

  const image = resolveImageUrl(post.imageUrl)
  const tags = parseTags(post.tags)
  const canDelete = username && username === post.authorUsername

  return (
    <article className="mx-auto max-w-3xl space-y-8">
      <Link to="/" className="text-sm text-stone-500 hover:text-clay">
        ← Feed-ə qayıt
      </Link>

      {error && <Alert>{error}</Alert>}

      {image && (
        <img src={image} alt={post.title} className="max-h-[420px] w-full rounded-3xl object-cover" />
      )}

      <header className="space-y-4">
        <div className="flex flex-wrap items-center gap-3 text-sm text-stone-500 dark:text-stone-400">
          <span>{post.authorUsername}</span>
          <span>·</span>
          <time dateTime={post.createdAt}>{formatDate(post.createdAt)}</time>
          {canDelete && (
            <button type="button" onClick={handleDelete} className="ml-auto text-rose-600 hover:underline">
              Sil
            </button>
          )}
        </div>
        <h1 className="font-serif text-5xl leading-tight">{post.title}</h1>
        {tags.length > 0 && (
          <div className="flex flex-wrap gap-2">
            {tags.map((tag) => (
              <span
                key={tag}
                className="rounded-full bg-mist px-3 py-1 text-xs font-medium text-clay-dark dark:bg-stone-800 dark:text-amber-200"
              >
                {tag}
              </span>
            ))}
          </div>
        )}
      </header>

      {post.aiSummary && (
        <aside className="rounded-3xl border border-amber-200 bg-amber-50 px-5 py-4 text-sm leading-7 text-amber-950 dark:border-amber-900/60 dark:bg-amber-950/30 dark:text-amber-100">
          <p className="mb-1 text-xs font-semibold uppercase tracking-wider text-clay">AI Summary</p>
          {post.aiSummary}
        </aside>
      )}

      {post.seoDescription && (
        <p className="rounded-2xl bg-mist/70 px-4 py-3 text-sm italic text-stone-600 dark:bg-stone-900 dark:text-stone-400">
          SEO: {post.seoDescription}
        </p>
      )}

      <div className="whitespace-pre-wrap text-lg leading-8 text-stone-800 dark:text-stone-200">{post.content}</div>

      <div className="flex gap-3">
        <button
          type="button"
          onClick={() => handleReaction('LIKE')}
          className="rounded-full border border-stone-200 px-4 py-2 text-sm hover:border-clay hover:text-clay dark:border-stone-700"
        >
          👍 {post.likeCount ?? 0}
        </button>
        <button
          type="button"
          onClick={() => handleReaction('DISLIKE')}
          className="rounded-full border border-stone-200 px-4 py-2 text-sm hover:border-clay hover:text-clay dark:border-stone-700"
        >
          👎 {post.dislikeCount ?? 0}
        </button>
      </div>

      <section className="space-y-5 border-t border-stone-200 pt-8 dark:border-stone-800">
        <h2 className="font-serif text-3xl">Kommentlər</h2>

        <form onSubmit={handleCommentSubmit} className="space-y-3">
          {commentError && <Alert>{commentError}</Alert>}
          {commentSuccess && <Alert type="success">{commentSuccess}</Alert>}
          <textarea
            required
            rows={3}
            value={commentText}
            onChange={(e) => setCommentText(e.target.value)}
            placeholder={isAuthenticated ? 'Fikrini yaz...' : 'Komment üçün daxil ol'}
            className="w-full rounded-2xl border border-stone-200 bg-white px-4 py-3 outline-none focus:border-clay dark:border-stone-700 dark:bg-[#1a1917]"
          />
          <button
            type="submit"
            disabled={submitting}
            className="rounded-2xl bg-ink px-5 py-2.5 text-white disabled:opacity-60 dark:bg-stone-100 dark:text-ink"
          >
            {submitting ? 'Göndərilir...' : 'Komment yaz'}
          </button>
        </form>

        <ul className="space-y-4">
          {comments.length === 0 && (
            <li className="rounded-2xl border border-dashed border-stone-300 px-4 py-8 text-center text-stone-500 dark:border-stone-700">
              Hələ komment yoxdur.
            </li>
          )}
          {comments.map((comment) => (
            <li
              key={comment.id}
              className="rounded-2xl border border-stone-200 bg-white p-4 dark:border-stone-800 dark:bg-[#1a1917]"
            >
              <div className="mb-2 flex items-center justify-between text-sm text-stone-500">
                <span className="font-medium text-ink dark:text-stone-100">{comment.authorUsername}</span>
                <time>{formatDate(comment.createdAt)}</time>
              </div>
              <p className="leading-7">{comment.content}</p>
              <div className="mt-3 flex gap-2">
                <button
                  type="button"
                  onClick={() => handleCommentReaction(comment.id, 'LIKE')}
                  className="text-sm text-stone-500 hover:text-clay"
                >
                  👍 {comment.likeCount ?? 0}
                </button>
                <button
                  type="button"
                  onClick={() => handleCommentReaction(comment.id, 'DISLIKE')}
                  className="text-sm text-stone-500 hover:text-clay"
                >
                  👎 {comment.dislikeCount ?? 0}
                </button>
              </div>
            </li>
          ))}
        </ul>

        {hasMore && (
          <button
            type="button"
            onClick={() => loadComments(page + 1, true)}
            className="w-full rounded-2xl border border-stone-200 py-3 text-sm dark:border-stone-700"
          >
            Daha çox
          </button>
        )}
      </section>
    </article>
  )
}
