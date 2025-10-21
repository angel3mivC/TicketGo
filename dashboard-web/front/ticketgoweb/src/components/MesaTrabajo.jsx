import Ticket from './Ticket.jsx'
import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import DetallesTickets from './DetallesTickets.jsx'
import UsuarioTag from './UsuarioTag.jsx'
import Notificaciones from './Notificaciones.jsx'
import Filtros from './Filtros.jsx'

import Notificacion from '../assets/notificacion.png'
import LogOut from '../assets/logout.svg'
import Filtro from '../assets/filtro.svg'
import Mas from '../assets/mas.svg'
import './styles/MesaDeTrabajo.css'

const MesaTrabajo = () => {
  const token = localStorage.getItem("token")
  const userName = localStorage.getItem("user")
  const initials = userName ? userName.split(' ').map(n => n[0]).join('').toUpperCase() : ''
  const navigate = useNavigate()

  const [activeTab, setActiveTab] = useState("tickets")
  const [tickets, setTickets] = useState([])
  const [ticketsFiltrados, setTicketsFiltrados] = useState([])
  const [selectedTicket, setSelectedTicket] = useState(null)
  const [showNotificaciones, setShowNotificaciones] = useState(false)
  const [showFiltros, setShowFiltros] = useState(false)
  const [filtrosAplicados, setFiltrosAplicados] = useState({ estado: '', categoria: '' })

  // --- Cargar tickets según pestaña ---
  useEffect(() => {
    let url = "http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/tickets"
    if (activeTab === "historial") {
      url += "?estado=Cerrado"
    }

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
      .catch(err => console.error("Error al obtener tickets:", err))
  }, [activeTab, token])

  const logout = () => {
    localStorage.clear()
    navigate("/")
  }

  const aplicarFiltros = (filtros) => {
    setFiltrosAplicados(filtros)

    const sinFiltros =
      (!filtros.estado || filtros.estado === '') &&
      (!filtros.categoria || filtros.categoria === '')

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

  useEffect(() => {
    const sinFiltros =
      (!filtrosAplicados.estado || filtrosAplicados.estado === '') &&
      (!filtrosAplicados.categoria || filtrosAplicados.categoria === '')

    if (sinFiltros) {
      setTicketsFiltrados(tickets)
    } else {
      aplicarFiltros(filtrosAplicados)
    }
  }, [tickets])

  return (
    <div className="mesa-container">
      {/* HEADER */}
      <div className="mesa-header">
        <UsuarioTag initials={initials} name={userName} role="Mesa de Trabajo" />

        <div className="tabs-container">
          <div
            className={`tab ${activeTab === "tickets" ? "active" : ""}`}
            onClick={() => setActiveTab("tickets")}
          >
            Tickets
          </div>
          <div
            className={`tab ${activeTab === "historial" ? "active" : ""}`}
            onClick={() => setActiveTab("historial")}
          >
            Historial
          </div>
        </div>

        <div className="tools">
          <img
            src={Notificacion}
            alt="notificaciones"
            className="noti"
            onClick={() => setShowNotificaciones(true)}
          />
          <img src={LogOut} alt="logout" className="logout" onClick={logout} />
        </div>
      </div>

      {/* ÁREA PRINCIPAL */}
      <div className="main-area">
        <div className="textsAreaTickets">
          <div>
            <div className="title">{activeTab === "tickets" ? "Tickets" : "Historial"}</div>
            <div className="subtitle">
              {activeTab === "tickets"
                ? "Consulta todos los tickets asignados a los técnicos."
                : "Consulta todos los tickets cerrados."}
            </div>
          </div>
          <div className="buttons">
            <button className="create-Ticket" onClick={() => setSelectedForm("crear-usuario")}>
              <img src={Mas} /> Crear Ticket
            </button>
            <button className="filters" onClick={() => setShowFiltros(true)}>
              <img src={Filtro} /> Filtrar
            </button>
          </div>
        </div>

        <div className="scroll-area">
          {ticketsFiltrados.length === 0 ? (
            <div className="no-data-message">
              No se encontraron {activeTab === "tickets" ? "tickets." : "tickets cerrados."}
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

export default MesaTrabajo