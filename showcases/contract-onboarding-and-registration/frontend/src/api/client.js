import { API_URL } from '../config.js';

export async function apiFetch(path, options = {}) {
  const res = await fetch(`${API_URL}${path}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  });

  if (!res.ok) {
    let errorMessage = `HTTP ${res.status}`;
    try {
      const body = await res.json();
      errorMessage = body.message ?? errorMessage;
    } catch {
      // ignore parse errors
    }
    throw new Error(errorMessage);
  }

  if (res.status === 204) return null;
  return res.json();
}
