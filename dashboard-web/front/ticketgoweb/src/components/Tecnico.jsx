import Ticket from './Ticket.jsx'
import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import DetallesTickets from './DetallesTickets.jsx'
import UsuarioTag from './UsuarioTag.jsx'
import Notificaciones from './Notificaciones.jsx'
import Filtros from './Filtros.jsx'

import Notificacion from '../assets/notificacion.png'
import Historial from '../assets/historial.svg'
import LogOut from '../assets/logout.svg'
import Filtro from '../assets/filtro.svg'
import './styles/Tecnico.css'

const Tecnico = () => {
  const token = localStorage.getItem("token")
  const userName = localStorage.getItem("user")
  const role = localStorage.getItem("rol")
  const id_usuario = localStorage.getItem("id_usuario")
  const initials = userName ? userName.split(' ').map(n => n[0]).join('').toUpperCase() : ''
  const navigate = useNavigate()

  const [activeTab, setActiveTab] = useState("tickets")
  const [tickets, setTickets] = useState([])
  const [selectedTicket, setSelectedTicket] = useState(null)
  const [showNotificaciones, setShowNotificaciones] = useState(false)
  const [showFiltros, setShowFiltros] = useState(false)
  const [filtrosAplicados, setFiltrosAplicados] = useState({ estado: '', categoria: '' })
  const [ticketsFiltrados, setTicketsFiltrados] = useState([])

  // --- Cargar tickets dinámicamente según pestaña ---
  useEffect(() => {
    let url = ""

    if (activeTab === "tickets")
      url = `http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/tickets?tecnico=${id_usuario}`
    else if (activeTab === "historial")
      url = `http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/tickets/?tecnico=${id_usuario}&estado=Cerrado`

    if (!url) return

    fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then(res => res.json())
      .then(data => {
        setTickets(data)
        setTicketsFiltrados(data)
      })
      .catch(err => console.error(`Error al obtener datos (${activeTab}):`, err))
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

  // --- Aplicar filtros ---
  const aplicarFiltros = (filtros) => {
    setFiltrosAplicados(filtros)

    const sinFiltros =
      (filtros.estado === '' || filtros.estado === undefined) &&
      (filtros.categoria === '' || filtros.categoria === undefined)

    if (sinFiltros) {
      setTicketsFiltrados(tickets)
    } else {
      const filtrados = tickets.filter(ticket => {
        const cumpleEstado = !filtros.estado || ticket.estado === filtros.estado
        const cumpleCategoria = !filtros.categoria || ticket.categoria === filtros.categoria
        return cumpleEstado && cumpleCategoria
      })
      setTicketsFiltrados(filtrados)
    }
  }

  // --- Actualizar tickets filtrados si cambian ---
  useEffect(() => {
    const sinFiltros =
      (filtrosAplicados.estado === '' || filtrosAplicados.estado === undefined) &&
      (filtrosAplicados.categoria === '' || filtrosAplicados.categoria === undefined)

    if (sinFiltros) {
      setTicketsFiltrados(tickets)
    } else {
      aplicarFiltros(filtrosAplicados)
    }
  }, [tickets])

  return (
    <div className="tecnico-container">
      {/* HEADER (fijo) */}
      <div className="tecnico-header">
        <UsuarioTag initials={initials} name={userName} role={getUserRoleLabel(role)} />

        <div className="tabs-container">
          <div
            className={`tab ${activeTab === "tickets" ? "active" : ""}`}
            onClick={() => setActiveTab("tickets")}
          >
            Tickets
          </div>
        </div>

        <div className="tools">
          <img
            src={Notificacion}
            alt="notificaciones"
            className="noti"
            onClick={() => setShowNotificaciones(true)}
          />
          <img
            src={Historial}
            alt="historial"
            className={`historial ${activeTab === "historial" ? "active" : ""}`}
            onClick={() => setActiveTab("historial")}
          />
          <img src={LogOut} alt="logout" className="logout" onClick={logout} />
        </div>
      </div>

      {/* ÁREA PRINCIPAL: header arriba + scroll abajo */}
      <div className="main-area">
        {/* TÍTULO Y BOTÓN FILTRAR (permanece en la zona superior dentro del main) */}
        {activeTab === "tickets" && (
          <div className="textsAreaTickets">
            <div>
              <div className="title">Tickets</div>
              <div className="subtitle">Consulta todos los tickets asignados.</div>
            </div>
            <button className="filters" onClick={() => setShowFiltros(true)}>
              <img src={Filtro} /> Filtrar
            </button>
          </div>
        )}

        {activeTab === "historial" && (
          <div className="textsAreaTickets">
            <div>
              <div className="title">Historial</div>
              <div className="subtitle">Consulta todos los tickets cerrados.</div>
            </div>
            <button className="filters" onClick={() => setShowFiltros(true)}>
              <img src={Filtro} /> Filtrar
            </button>
          </div>
        )}

        {/* --- AREA SCROLLABLE --- */}
        <div className="scroll-area">
          {ticketsFiltrados.length === 0 ? (
            <div className="no-data-message">
              No se encontraron {activeTab === "historial" ? "tickets cerrados." : "tickets."}
            </div>
          ) : (
            <div className="tickets-list">
              {ticketsFiltrados.map((ticket) => (
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
          )}
        </div>
      </div>

      {/* MODALES */}
      {selectedTicket && (
        <div className="modal-overlay">
          <div className="modal-content">
            <button className="close-modal" onClick={() => setSelectedTicket(null)}>✕</button>
            <DetallesTickets id_ticket={selectedTicket} />
          </div>
        </div>
      )}

      {showNotificaciones && (
        <Notificaciones onClose={() => setShowNotificaciones(false)} />
      )}

      {showFiltros && (
        <Filtros
          onClose={() => setShowFiltros(false)}
          onApplyFilters={aplicarFiltros}
          soloCategoria={activeTab === "historial"}
        />
      )}
    </div>
  )
}

export default Tecnico