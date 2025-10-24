import { useState, useEffect } from 'react'
import TotalCard from './TotalCard.jsx'
import TecnicosCard from './CardTecnicos.jsx'
import CardNuevosTickets from './CardNuevosTickets.jsx'
import CardProgreso from './CardProgreso.jsx'
import './styles/Dashboard.css'

import IconTecnico from '../assets/icon-tecnico.png'
import IconTicketGris from '../assets/icon-ticket-gris.png'
import IconTicketCerrado from '../assets/icon-ticket-rojo.png'
import IconTicketAbierto from '../assets/icon-ticket-verde.png'

const Dashboard = () => {
    const [totales, setTotales] = useState({totalTecnicos: 0,totalTickets: 0,ticketsAbiertos: 0,ticketsCerrados: 0});
    const [tecnicosData, setTecnicosData] = useState({activos: 0,inactivos: 0,})
    const [refresh, setRefresh] = useState(false);

    // Función para obtener los datos de los totales
    useEffect(() => {
    const token = localStorage.getItem("token");

    const fetchSummary = fetch("https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/dashboard/summary", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    }).then(res => res.json());

    const fetchTecnicos = fetch("https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/dashboard/tecnicos/total", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    }).then(res => res.json());

    Promise.all([fetchSummary, fetchTecnicos])
      .then(([summaryData, tecnicosData]) => {
        setTotales({
          totalTickets: summaryData.total || 0,
          ticketsAbiertos: summaryData.abiertos || 0,
          ticketsCerrados: summaryData.cerrados || 0,
          totalTecnicos: tecnicosData.total_tecnicos || 0
        });
      })
      .catch(err => console.error("Error al obtener dashboard:", err));
  }, [refresh]);

  // Fetch de técnicos activos e inactivos
  useEffect(() => {
    const token = localStorage.getItem("token");

    const fetchActivos = fetch("https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/dashboard/tecnicos/activos", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    }).then(res => res.json());

    const fetchInactivos = fetch("https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/dashboard/tecnicos/inactivos", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    }).then(res => res.json());

    Promise.all([fetchActivos, fetchInactivos])
      .then(([activosData, inactivosData]) => {
        setTecnicosData({
          activos: activosData.tecnicos_activos || 0,
          inactivos: inactivosData.tecnicos_inactivos || 0
        });
      })
      .catch(err => console.error("Error al obtener técnicos activos/inactivos:", err));
  }, [refresh]);

  return (
    <div className = "dashboard-container">   
        <div className="dashboard-totales">
            <TotalCard title="Total de Técnicos" count={totales.totalTecnicos} icon={IconTecnico} circleColor="#E5E5E5" />
            <TotalCard title="Total de Tickets" count={totales.totalTickets} icon={IconTicketGris} circleColor="#E5E5E5" />
            <TotalCard title="Tickets Abiertos" count={totales.ticketsAbiertos} icon={IconTicketAbierto} circleColor="#DFF4CC" />
            <TotalCard title="Tickets Cerrados" count={totales.ticketsCerrados} icon={IconTicketCerrado} circleColor="#FFCCCC" />
        </div>

        <div className="dashboard-graficas">
            <CardNuevosTickets />
            <TecnicosCard activos={tecnicosData.activos} inactivos={tecnicosData.inactivos} />
        </div>

        <div className="dashboard-progreso">
            <CardProgreso />
        </div>
    </div>
  )
}

export default Dashboard