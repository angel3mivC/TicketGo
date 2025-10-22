import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

import Login from './components/Login.jsx'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import Admin from './components/Admin.jsx'
import Tecnico from './components/Tecnico.jsx'
import MesaTrabajo from './components/MesaTrabajo.jsx'


const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login/>}/>
        <Route path="/admin" element={<ProtectedRoute><Admin/></ProtectedRoute>}/>
        <Route path="/mesatrabajo" element={<ProtectedRoute><MesaTrabajo/></ProtectedRoute>}/>
        <Route path="/tecnico" element={<ProtectedRoute><Tecnico/></ProtectedRoute>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
