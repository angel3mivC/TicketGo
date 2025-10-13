import { useNavigate } from 'react-router-dom'

import './styles/Ticket.css'

const formatDateTime = (isoString) => {
    const dateObj = new Date(isoString)
    const fecha = dateObj.toLocaleDateString('es-MX', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    })
    const hora = dateObj.toLocaleTimeString('es-MX', {
        hour: '2-digit',
        minute: '2-digit'
    })
    return `${fecha} • ${hora}`
}

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

const Ticket = ({title,date,status,description,category,priority, onClick}) => {

    return (
        <div className="ticket-card --flex-column" onClick={onClick}>
            {/* Parte superior */}
            <div className="top --flex-column">
                <div className="ticket-header">
                <div>
                    <div className="ticket-title">{title}</div>
                    <div className="ticket-date">{formatDateTime(date)}</div>
                </div>
                <div className={`ticket-status ${getStatusClass(status)}`}>
                    <span className={`status-dot ${getStatusClass(status)}`}></span>
                    {status}
                </div>
                </div>

                <div className="ticket-description">
                {description}
                </div>

                {/* Mover el separador aquí, ENTRE descripción y tags */}
                <div className="ticket-divider"></div>

                <div className="ticket-tags">
                <span className="tag warranty">{category}</span>
                <span className= {`tag priority ${getPriorityClass(priority)}`}>
                    <span className={`priority-dot ${getPriorityClass(priority)}`}></span> 
                    {priority}
                </span>
                </div>
            </div>
        </div>
    )
} 

export default Ticket;