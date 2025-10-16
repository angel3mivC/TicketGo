import Ticket from './Ticket.jsx'
import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import DetallesTickets from './DetallesTickets.jsx'
import UsuarioTag from './UsuarioTag.jsx'
import Usuario from './Usuario.jsx'
import CrearUsuario from './FormularioCrearUsuario.jsx'
import EditarUsuario from './FormularioEditarUsuario.jsx'
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
  const [selectedForm, setSelectedForm] = useState(null)
  const [selectedUser, setSelectedUser] = useState(null);
  const [refresh, setRefresh] = useState(false); //Cuando se borre un usuario setRefresh(prev => !prev);

  // Fetch dinámico según pestaña activa
  useEffect(() => {
    let url = ""

    if (activeTab === "tickets") url = "http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/tickets"
    else if (activeTab === "usuarios") url = "http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/users"
    else if (activeTab === "historial") url = "http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/tickets?estado=Cerrado" // 👈 endpoint para historial

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
        if (activeTab === "usuarios") {
            // Filtra el usuario actual
            const filtered = data.filter(user => user.nombre !== userName)
            setUsuarios(filtered)
        } else {
            setTickets(data)
        }
      })
      .catch((err) => console.error(`Error al obtener datos (${activeTab}):`, err))
  }, [activeTab, refresh ,token])

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
            className={`historial ${activeTab === "historial" ? "active" : ""}`} // destacar si está activo
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
            <button className="create-User" onClick={() => setSelectedForm("crear-usuario")}>
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
                id_usuario={user.id_usuario}
                name={user.nombre}
                role={getUserRoleLabel(user.id_rol)}
                 onEdit={() => {
                    setSelectedForm("editar-usuario");
                    setSelectedUser(user); // guardamos el usuario actual
                }}
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

      {/* MODAL DE FORMULARIO DE CREACION DE USUARIOS */}
        {selectedForm === "crear-usuario" && (
        <div className="modal-overlay">
            <div className="modal-create-user">
            <button className="close-create-user" onClick={() => setSelectedForm(null)}>✕</button>
            <CrearUsuario onClose={() => setSelectedForm(null)} onSuccess={() => setRefresh(prev => !prev)} />
            </div>
        </div>
        )}

        {selectedForm === "editar-usuario" && selectedUser && (
        <div className="modal-overlay">
            <div className="modal-edit-user">
            <button className="close-edit-user" onClick={() => setSelectedForm(null)}>✕</button>
            <EditarUsuario 
                usuario={selectedUser} 
                onClose={() => setSelectedForm(null)} 
                onSuccess={() => setRefresh(prev => !prev)} 
            />
            </div>
        </div>
        )}
    </div>
  )
}

export default Admin