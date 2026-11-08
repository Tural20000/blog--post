import fs from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

const projectRoot = path.dirname(fileURLToPath(import.meta.url))
const uploadsRoot = path.resolve(projectRoot, '../uploads')

function serveUploads() {
  const mimeTypes = {
    '.jpg': 'image/jpeg',
    '.jpeg': 'image/jpeg',
    '.png': 'image/png',
    '.gif': 'image/gif',
    '.webp': 'image/webp',
    '.svg': 'image/svg+xml',
  }

  return {
    name: 'serve-uploads',
    configureServer(server) {
      server.middlewares.use((req, res, next) => {
        if (!req.url?.startsWith('/uploads/')) {
          next()
          return
        }

        const relativePath = decodeURIComponent(req.url.replace('/uploads/', '').split('?')[0])
        const filePath = path.resolve(uploadsRoot, relativePath)
        if (!filePath.startsWith(uploadsRoot) || !fs.existsSync(filePath) || !fs.statSync(filePath).isFile()) {
          next()
          return
        }

        const ext = path.extname(filePath).toLowerCase()
        res.setHeader('Content-Type', mimeTypes[ext] || 'application/octet-stream')
        fs.createReadStream(filePath).pipe(res)
      })
    },
  }
}

export default defineConfig({
  plugins: [react(), tailwindcss(), serveUploads()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8086',
        changeOrigin: true,
      },
      '/uploads': {
        target: 'http://localhost:8086',
        changeOrigin: true,
      },
    },
  },
})
