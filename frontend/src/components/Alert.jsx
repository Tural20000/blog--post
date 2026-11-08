export default function Alert({ type = 'error', children }) {
  const styles =
    type === 'success'
      ? 'border-emerald-300 bg-emerald-50 text-emerald-800 dark:border-emerald-800 dark:bg-emerald-950/60 dark:text-emerald-200'
      : 'border-rose-300 bg-rose-50 text-rose-800 dark:border-rose-900 dark:bg-rose-950/50 dark:text-rose-200'

  return (
    <div className={`rounded-2xl border px-4 py-3 text-sm leading-6 ${styles}`} role="alert">
      {children}
    </div>
  )
}
