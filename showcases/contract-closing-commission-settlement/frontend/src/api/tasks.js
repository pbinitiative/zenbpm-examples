import { apiFetch } from './client.js';

export const fetchTasks = () =>
  apiFetch('/tasks');

export const completeTask = (key, variables) =>
  apiFetch(`/tasks/${key}/complete`, {
    method: 'POST',
    body: JSON.stringify({ variables }),
  });
