import "./styles/DetallesTickets.css"

const getStatusClass = (status) => {
    switch (status) {
        case 'Abierto': return 'status-open'
        case 'En Progreso': return 'status-inprogress'
        case 'Resuelto': return 'status-resolved'
        case 'Cerrado': return 'status-closed'
        case 'Reabierto': return 'status-reopened'
        default: return ''
    }
}

const getPriorityClass = (priority) => {
    switch (priority) {
        case 'Alta': return 'priority-high'
        case 'Baja': return 'priority-low'
        case 'Media': return 'priority-medium'
        default: return ''
    }
}

const DetallesTickets = ({ id_ticket }) => {
    const ticket = {
    titulo: "Título",
    fecha: "2 sept 2023",
    hora: "10:00 pm",
    estado: "Abierto",
    categoria: "Garantía",
    prioridad: "Alta",
    usuario: "Nombre",
    descripcion:
      "Descripción. Lorem Ipsum Lorem Ipsum. Lorem Lorem Lorem Lorem Lorem",
    evidencias: [1, 2, 3], // simulando imágenes
    comentarios: [
      {
        usuario: "Nombre",
        fecha: "1 sept. 2025",
        hora: "10:00 pm",
        descripcion: "Descripción Comentario",
      },
    ],
  }

  return (
    <div className="detalles-ticket-container">
      <div className="ticket-header">
        <div>
          <h2 className="ticket-title">{ticket.titulo}</h2>
          <p className="ticket-date">
            {ticket.fecha} &nbsp; {ticket.hora}
          </p>
        </div>
        <div className={`ticket-status ${getStatusClass(ticket.estado)}`}>
          <span className="status-dot"></span>
          {ticket.estado}
        </div>
      </div>

      <div className="ticket-tags">
        <span className="tag categoria">{ticket.categoria}</span>
        <span className="tag prioridad">{ticket.prioridad}</span>
      </div>

      <div className="ticket-user">
        <div className="user-avatar"></div>
        <div className="user-info">
          <p className="user-name">{ticket.usuario}</p>
        </div>
      </div>

      <p className="ticket-description">{ticket.descripcion}</p>

      <div className="ticket-evidencias">
        <div className="evidencias-header">
          <h3>Evidencias</h3>
          <a href="#">Ver Todos &gt;</a>
        </div>
        <div className="evidencias-grid">
          {ticket.evidencias.map((_, index) => (
            <div key={index} className="evidencia-placeholder"></div>
          ))}
        </div>
      </div>

      <hr className="divider" />

      {ticket.comentarios.map((comentario, index) => (
        <div key={index} className="ticket-comentario">
          <div className="comentario-header">
            <div className="user-avatar"></div>
            <div className="comentario-info">
              <p className="user-name">{comentario.usuario}</p>
              <p className="comentario-date">
                {comentario.fecha} &nbsp; {comentario.hora}
              </p>
            </div>
          </div>
          <p className="comentario-text">{comentario.descripcion}</p>
        </div>
      ))}

      <div className="comentario-input">
        <input
          type="text"
          placeholder="Ingresa un comentario"
          className="input-text"
        />
        <button className="send-button">↑</button>
      </div>
    </div>
    )
}

export default DetallesTickets