import { useState } from 'react';
import { useTasks } from './hooks/useTasks.js';
import { useToast } from './hooks/useToast.js';
import { TaskList } from './components/TaskList.jsx';
import { TaskDetail } from './components/TaskDetail.jsx';
import { StartProcessModal } from './components/StartProcessModal.jsx';
import { ToastContainer } from './components/Toast.jsx';

export default function App() {
  const { tasks, loading, error, refresh } = useTasks();
  const { toasts, addToast } = useToast();
  const [selectedTask, setSelectedTask] = useState(null);
  const [showStartProcess, setShowStartProcess] = useState(false);

  function handleTaskCompleted() {
    setSelectedTask(null);
    refresh();
  }

  return (
    <div style={{ fontFamily: 'system-ui, sans-serif', height: '100%', display: 'flex', flexDirection: 'column', background: '#f7fafc' }}>

      {/* Header */}
      <header style={{
        display: 'flex', justifyContent: 'space-between', alignItems: 'center',
        padding: '0 24px', height: 56, background: '#fff',
        borderBottom: '1px solid #e2e8f0', flexShrink: 0,
      }}>
        <h1 style={{ margin: 0, fontSize: 20, fontWeight: 700, color: '#1a202c' }}>
          Contract Closing & Commission Settlement
        </h1>
        <button
          onClick={() => setShowStartProcess(true)}
          style={{
            padding: '7px 16px', background: '#38a169', color: '#fff',
            border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 500, fontSize: 14,
          }}
        >
          + Start Process
        </button>
      </header>

      {/* Two-panel body */}
      <div style={{ flex: 1, display: 'flex', overflow: 'hidden' }}>

        {/* Left panel — task list */}
        <div style={{
          width: 300, flexShrink: 0,
          borderRight: '1px solid #e2e8f0',
          background: '#fff',
          display: 'flex', flexDirection: 'column',
          overflowY: 'auto',
        }}>
          <div style={{
            padding: '14px 16px 10px',
            fontSize: 11, fontWeight: 600, color: '#a0aec0',
            textTransform: 'uppercase', letterSpacing: '0.07em',
            borderBottom: '1px solid #e2e8f0', flexShrink: 0,
          }}>
            Active Tasks
          </div>
          <TaskList
            tasks={tasks}
            loading={loading}
            error={error}
            selectedTask={selectedTask}
            onSelect={setSelectedTask}
          />
        </div>

        {/* Right panel — task detail */}
        <div style={{ flex: 1, overflowY: 'auto' }}>
          <TaskDetail
            task={selectedTask}
            onClose={() => setSelectedTask(null)}
            onCompleted={handleTaskCompleted}
            addToast={addToast}
          />
        </div>
      </div>

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
