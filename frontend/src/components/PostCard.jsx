import { Link } from 'react-router-dom'
import { resolveImageUrl } from '../api/client'
import { formatDate, parseTags } from '../utils/format'

export default function PostCard({ post }) {
  const image = resolveImageUrl(post.imageUrl)
  const tags = parseTags(post.tags)

  return (
    <article className="group overflow-hidden rounded-3xl border border-stone-200 bg-white shadow-sm transition hover:-translate-y-0.5 hover:shadow-md dark:border-stone-800 dark:bg-[#1a1917]">
      <Link to={`/posts/${post.id}`} className="block">
        {image ? (
          <div className="h-52 overflow-hidden bg-mist dark:bg-stone-900">
            <img
              src={image}
              alt={post.title}
              className="h-full w-full object-cover transition duration-500 group-hover:scale-[1.03]"
            />
          </div>
        ) : (
          <div className="flex h-36 items-end bg-[radial-gradient(circle_at_top_left,_#e8d9c4,_#f7f3ee)] px-6 py-5 dark:bg-[radial-gradient(circle_at_top_left,_#2a241f,_#1a1917)]">
            <p className="font-serif text-2xl text-stone-500 italic dark:text-stone-400">Yazı</p>
          </div>
        )}

        <div className="space-y-4 p-6">
          <div className="flex flex-wrap items-center gap-2 text-xs uppercase tracking-wide text-stone-500 dark:text-stone-400">
            <span>{post.authorUsername}</span>
            <span>·</span>
            <time dateTime={post.createdAt}>{formatDate(post.createdAt)}</time>
          </div>

          <h2 className="font-serif text-2xl leading-tight text-ink group-hover:text-clay dark:text-stone-50">
            {post.title}
          </h2>

          {post.aiSummary && (
            <p className="rounded-2xl bg-mist/80 px-4 py-3 text-sm leading-6 text-stone-700 dark:bg-stone-900 dark:text-stone-300">
              <span className="mr-2 font-semibold text-clay">AI Summary</span>
              {post.aiSummary}
            </p>
          )}

          {post.seoDescription && (
            <p className="text-sm leading-6 text-stone-600 dark:text-stone-400">{post.seoDescription}</p>
          )}

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

          <div className="flex items-center gap-4 pt-1 text-sm text-stone-500 dark:text-stone-400">
            <span>👍 {post.likeCount ?? 0}</span>
            <span>👎 {post.dislikeCount ?? 0}</span>
          </div>
        </div>
      </Link>
    </article>
  )
}
