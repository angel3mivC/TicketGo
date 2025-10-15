import Ticket from './Ticket.jsx'
import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import DetallesTickets from './DetallesTickets.jsx'
import UsuarioTag from './UsuarioTag.jsx'
import Usuario from './Usuario.jsx'
import Notificacion from '../assets/notificacion.png'
import Historial from '../assets/historial.svg'
import LogOut from '../assets/logout.svg'
import Mas from '../assets/mas.svg'
import Filtro from '../assets/filtro.svg'
import './styles/Admin.css'

const Admin = () => {
  const token = localStorage.getItem("token")
  const userName = localStorage.getItem("user")
  const role = localStorage.getItem("rol")
  const initials = userName ? userName.split(' ').map(n => n[0]).join('').toUpperCase() : ''
  const navigate = useNavigate()

  const [activeTab, setActiveTab] = useState("tickets")
  const [tickets, setTickets] = useState([])
  const [usuarios, setUsuarios] = useState([])
  const [selectedTicket, setSelectedTicket] = useState(null)

  // 🔄 Fetch dinámico según pestaña activa
  useEffect(() => {
    let url = ""

    if (activeTab === "tickets") url = "http://localhost:3000/tickets"
    else if (activeTab === "usuarios") url = "http://localhost:3000/users"
    else if (activeTab === "historial") url = "http://localhost:3000/tickets?estado=3" // 👈 endpoint para historial

    if (!url) return

    fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        if (activeTab === "usuarios") setUsuarios(data)
        else setTickets(data)
      })
      .catch((err) => console.error(`Error al obtener datos (${activeTab}):`, err))
  }, [activeTab, token])

  const logout = () => {
    localStorage.clear()
    navigate("/")
  }

  const getUserRoleLabel = (rol) => {
    const r = Number(rol)
    if (r === 1) return "Administrador"
    if (r === 2) return "Mesa de Trabajo"
    return "Técnico"
  }

  return (
    <div className="admin-container">
      <div className="admin-header">
        <UsuarioTag initials={initials} name={userName} role={getUserRoleLabel(role)} />

        <div className="tabs-container">
          <div
            className={`tab ${activeTab === "tickets" ? "active" : ""}`}
            onClick={() => setActiveTab("tickets")}
          >
            Tickets
          </div>
          <div
            className={`tab ${activeTab === "usuarios" ? "active" : ""}`}
            onClick={() => setActiveTab("usuarios")}
          >
            Usuarios
          </div>
        </div>

        <div className="tools">
          <img src={Notificacion} alt="notificaciones" className="noti" />
          <img
            src={Historial}
            alt="historial"
            className={`historial ${activeTab === "historial" ? "active" : ""}`} // 👈 destacar si está activo
            onClick={() => setActiveTab("historial")}
          />
          <img src={LogOut} alt="logout" className="logout" onClick={logout} />
        </div>
      </div>

      {/* TÍTULOS */}
      {activeTab === "tickets" && (
        <div className="textsAreaTickets">
          <div className="title">Tickets</div>
          <div className="subtitle">Consulta todos los tickets.</div>
        </div>
      )}

      {activeTab === "usuarios" && (
        <div className="textsArea">
          <div className="Titles">
            <div className="title">Mis usuarios</div>
            <div className="subtitle">Consulta todos los usuarios creados.</div>
          </div>
          <div className="buttons">
            <button className="create-User">
              <img src={Mas} /> Crear Usuario
            </button>
            <button className="filters">
              <img src={Filtro} /> Filtrar
            </button>
          </div>
        </div>
      )}

      {activeTab === "historial" && (
        <div className="textsAreaTickets">
          <div className="title">Historial</div>
          <div className="subtitle">Consulta todos los tickets cerrados.</div>
        </div>
      )}

      {/* CONTENIDO SCROLL */}
      <div className="scroll-area">
        {activeTab === "tickets" || activeTab === "historial" ? (
          <div className="tickets-list">
            {tickets.map((ticket) => (
              <Ticket
                key={ticket.id_ticket}
                id_ticket={ticket.id_ticket}
                title={ticket.titulo}
                date={ticket.fecha_creacion}
                status={ticket.estado}
                description={ticket.descripcion}
                category={ticket.categoria}
                priority={ticket.prioridad}
                onClick={() => setSelectedTicket(ticket.id_ticket)}
              />
            ))}
          </div>
        ) : (
          <div className="users-list">
            {usuarios.map((user) => (
              <Usuario
                key={user.id_usuario}
                name={user.nombre}
                role={getUserRoleLabel(user.id_rol)}
              />
            ))}
          </div>
        )}
      </div>

      {/* MODAL DE DETALLES */}
      {selectedTicket && (
        <div className="modal-overlay">
          <div className="modal-content">
            <button className="close-modal" onClick={() => setSelectedTicket(null)}>
              ✕
            </button>
            <DetallesTickets id_ticket={selectedTicket} />
          </div>
        </div>
      )}
    </div>
  )
}

export default Admin