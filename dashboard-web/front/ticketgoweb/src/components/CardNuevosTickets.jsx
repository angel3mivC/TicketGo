import { Bar } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend,} from "chart.js";
import { useState, useEffect } from 'react'
import "./styles/TicketsChartCard.css";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const CardNuevosTickets = () => {
  const [dataSemanal, setDataSemanal] = useState([]);

  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const token = localStorage.getItem("token");

        const res = await fetch(
          "https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/dashboard/tickets/por-dia",
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`, 
            },
          }
        );

        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

        const json = await res.json();
        console.log("Respuesta API:", json);

        const tickets = Array.isArray(json) ? json : json.data;

        const today = new Date();
        const day = today.getDay();
        const monday = new Date(today);
        monday.setDate(today.getDate() - day + 1);
        const sunday = new Date(monday);
        sunday.setDate(monday.getDate() + 6);

        // Crear arreglo de fechas de lunes a domingo
        const weekDays = [];
        for (let i = 0; i < 7; i++) {
          const d = new Date(monday);
          d.setDate(monday.getDate() + i);
          weekDays.push(d.toISOString().split("T")[0]);
        }

        // Mapear tickets a cada día
        const ticketsPorDia = weekDays.map((day) => {

            const ticketsDelDia = tickets.filter(t => t.fecha.startsWith(day));
            return ticketsDelDia.reduce((sum, t) => sum + t.total_tickets, 0);
        });

        setDataSemanal(ticketsPorDia);
      } catch (error) {
        console.error("Error fetching tickets:", error);
      }
    };

    fetchTickets();
  }, []);

  const data = {
    labels: ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"],
    datasets: [
      {
        label: "Tickets",
        data: dataSemanal,
        backgroundColor: "rgba(255, 0, 0, 0.8)",
        borderRadius: 4,
      },
    ],
  };


  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: { backgroundColor: "#333" },
    },
    scales: {
      x: {
        grid: { display: false },
        ticks: {
          color: "#444",
          font: { size: 14, family: "Montserrat, sans-serif" },
        },
      },
      y: {
        grid: { color: "#eee" },
        ticks: {
          color: "#444",
          font: { size: 14, family: "Montserrat, sans-serif" },
        },
      },
    },
  };

  return (
    <div className="tickets-card">
      <h2>Nuevos Tickets</h2>
      <div className="tickets-chart">
        <Bar data={data} options={options} />
      </div>
    </div>
  );
};

export default CardNuevosTickets;