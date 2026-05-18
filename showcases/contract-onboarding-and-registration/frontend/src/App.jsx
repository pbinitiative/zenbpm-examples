import { useState } from 'react';
import { useTasks } from './hooks/useTasks.js';
import { useToast } from './hooks/useToast.js';
import { TaskList } from './components/TaskList.jsx';
import { TaskFormModal } from './components/TaskFormModal.jsx';
import { StartProcessModal } from './components/StartProcessModal.jsx';
import { ToastContainer } from './components/Toast.jsx';

export default function App() {
  const { tasks, loading, error, refresh } = useTasks();
  const { toasts, addToast } = useToast();
  const [activeTask, setActiveTask] = useState(null);
  const [showStartProcess, setShowStartProcess] = useState(false);

  return (
    <div style={{ fontFamily: 'system-ui, sans-serif', maxWidth: 800, margin: '0 auto', padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h1 style={{ margin: 0, fontSize: 24, fontWeight: 700 }}>Task Portal</h1>
        <button onClick={() => setShowStartProcess(true)} style={{
          padding: '9px 18px', background: '#38a169', color: '#fff',
          border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 500,
        }}>
          + Start Process
        </button>
      </div>

      <TaskList tasks={tasks} loading={loading} error={error} onOpen={setActiveTask} />

      {activeTask && (
        <TaskFormModal
          task={activeTask}
          onClose={() => setActiveTask(null)}
          onCompleted={() => { setActiveTask(null); refresh(); }}
          addToast={addToast}
        />
      )}

      {showStartProcess && (
        <StartProcessModal
          onClose={() => setShowStartProcess(false)}
          onStarted={refresh}
          addToast={addToast}
        />
      )}

      <ToastContainer toasts={toasts} />
    </div>
  );
}
