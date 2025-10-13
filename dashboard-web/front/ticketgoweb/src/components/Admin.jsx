import Ticket from './Ticket.jsx'
import { useState, useEffect } from 'react'
import DetallesTickets from './DetallesTickets.jsx'
import './styles/Admin.css'

const Admin = () => {
    const token = localStorage.getItem('token')
    const [tickets, setTickets] = useState([])
    const [selectedTicket, setSelectedTicket] = useState(null)
    useEffect(() => {
    fetch(`http://localhost:3000/tickets`,{
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })
    .then(data => data.json())
    .then((data) => {
        setTickets(data)
    })
    }, [])

    return (
        <div className='admin-container'>
            <div className='tickets-list'>
                {tickets.map(ticket => (
                    <Ticket 
                        key = {ticket.id_ticket}
                        id_ticket={ticket.id_ticket}
                        title={ticket.titulo}
                        date={ticket.fecha_creacion}
                        status={ticket.estado}
                        description={ticket.descripcion}
                        category={ticket.categoria}
                        priority={ticket.prioridad}
                        onClick={() => setSelectedTicket(ticket.id_ticket)}
                    />)
                )}
            </div>
            <div className='ticket-details'>
                {selectedTicket && (
                  <div className="modal-overlay">
                    <div className="modal-content">
                      <button className="close-modal" onClick={() => setSelectedTicket(null)}>✕</button>
                      <DetallesTickets id_ticket={selectedTicket} />
                    </div>
                  </div>
                )}
            </div>
        </div>
    )
}

export default Admin