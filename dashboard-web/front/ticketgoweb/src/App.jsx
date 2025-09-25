import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

import Login from './components/Login.jsx'

//import './App.css'

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login/>}/>
        <Route path="/admin" element={<h1>Admin</h1>}/>
        <Route path="/mesatrabajo" element={<h1>MesaDeTrabajo</h1>}/>
        <Route path="/tecnico" element={<h1>Tecnico</h1>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
