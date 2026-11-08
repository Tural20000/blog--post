export default function Spinner({ label = 'Yüklənir...' }) {
  return (
    <div className="flex items-center justify-center gap-3 py-16 text-stone-500 dark:text-stone-400">
      <span className="h-5 w-5 animate-spin rounded-full border-2 border-stone-300 border-t-clay" />
      <span>{label}</span>
    </div>
  )
}
