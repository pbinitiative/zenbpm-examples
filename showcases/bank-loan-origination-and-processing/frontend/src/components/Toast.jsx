export function ToastContainer({ toasts }) {
  return (
    <div style={{ position: 'fixed', bottom: 24, right: 24, display: 'flex', flexDirection: 'column', gap: 8, zIndex: 1000 }}>
      {toasts.map(t => (
        <div key={t.id} style={{
          padding: '12px 20px',
          borderRadius: 6,
          color: '#fff',
          background: t.type === 'error' ? '#e53e3e' : t.type === 'success' ? '#38a169' : '#3182ce',
          boxShadow: '0 2px 8px rgba(0,0,0,0.2)',
          minWidth: 240,
        }}>
          {t.message}
        </div>
      ))}
    </div>
  );
}
