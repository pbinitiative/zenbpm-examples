import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import '@bpmn-io/form-js/dist/assets/form-js.css';
import App from './App.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>
);
