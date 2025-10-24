import { Doughnut } from 'react-chartjs-2'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import "./styles/TecnicosCard.css";

ChartJS.register(ArcElement, Tooltip, Legend)
const CardTecnicos = ({ activos, inactivos }) => {
  const data = {
    labels: ['Inactivos', 'Activos'],
    datasets: [
      {
        data: [inactivos, activos],
        backgroundColor: ['#E5E5E5', '#FF0000'], 
        borderWidth: 0,
      },
    ],
  }

  const total = activos + inactivos
  const porcentaje = total === 0 ? 0 : (activos / total) * 100

  const options = {
    cutout: '70%',
    plugins: { legend: { display: false } },
  }

  return (
    <div className="card-progress-container">
      <h2>Técnicos</h2>
      <div className="chart-placeholder-progreso">
        <Doughnut data={data} options={options} />
        <div className="chart-text">
          <div className="percentage">{porcentaje.toFixed(0)}%</div>
        </div>
      </div>

      <div className="legend">
        <div className="legend-item">
          <span className="dot inactivo"></span> Inactivo
        </div>
        <div className="legend-item">
          <span className="dot activo"></span> Activo
        </div>
      </div>
    </div>
  )
}

export default CardTecnicos
