import { apiFetch } from './client.js';

export const startProcess = (variables, businessKey) =>
  apiFetch('/processes/start', {
    method: 'POST',
    body: JSON.stringify({ variables, businessKey }),
  });
