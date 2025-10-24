import { useEffect, useState } from "react";
import "./styles/Notificaciones.css";

const Notificaciones = ({ onClose }) => {
  const [notificaciones, setNotificaciones] = useState([]);
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) return;

    fetch(
      `https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/notifications`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    )
      .then((res) => res.json())
      .then((data) => setNotificaciones(data))
      .catch((err) => console.error("Error al obtener notificaciones:", err));
  }, [token]);

  const marcarComoLeida = (idNotificacion) => {
    fetch(
      `https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/notifications/${idNotificacion}/read`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    )
      .then((res) => {
        if (res.ok) {
          setNotificaciones((prev) =>
            prev.filter((n) => n.id_notificacion !== idNotificacion)
          );
        }
      })
      .catch((err) => console.error("Error al marcar como leída:", err));
  };

  return (
    <div className="notificaciones-panel">
      <div className="notificaciones-header">
        <h3>Notificaciones</h3>
        <div className="close-noti" onClick={onClose}>
          ✕
        </div>
      </div>

      <div className="notificaciones-list">
        {notificaciones.length === 0 ? (
          <p className="no-notificaciones">No hay notificaciones recientes.</p>
        ) : (
          notificaciones.map((n, index) => (
            <div key={index} className="notificacion-item">
              <div className="notificacion-info">
                <div className="notificacion-mensaje">{n.mensaje}</div>
                <div className="notificacion-detalles">
                  <span>ID Ticket: {n.id_ticket}</span>
                  <span>{new Date(n.fecha).toLocaleString()}</span>
                </div>
              </div>
              <div
                className="notificacion-cerrar"
                onClick={() => marcarComoLeida(n.id_notificacion)}
              >
                ✕
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Notificaciones;
