import "./styles/TotalCard.css";

const TotalCard = ({ title, count, icon, circleColor }) => {
  return (
    <div className="total-card">
      <h3 className="totales-card-title">{title}</h3>
      <div className="total-card-content">
        <div className="total-card-icon-circle" style={{ backgroundColor: circleColor }}>
          <img src={icon} alt={title} className="total-card-icon "/>
        </div>
        <span className="total-card-count">{count}</span>
      </div>
    </div>
  )
}

export default TotalCard
