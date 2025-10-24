import { useState, useEffect } from "react";
import "./styles/CardProgreso.css";

const CardProgreso = () => {
  const [technicians, setTechnicians] = useState([]);

  const getProgress = (resolved, open) =>
    resolved + open > 0 ? Math.round((resolved / (resolved + open)) * 100) : 0;

  const getInitials = (name) =>
    name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase();

  useEffect(() => {
    const token = localStorage.getItem("token");

    const fetchResolved = fetch(
      "https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/dashboard/tecnicos/resueltos",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    ).then((res) => res.json());

    const fetchOpen = fetch(
      "https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/dashboard/tecnicos/abiertos",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    ).then((res) => res.json());

    Promise.all([fetchResolved, fetchOpen])
      .then(([resolvedData, openData]) => {
        const techMap = {};

        resolvedData.forEach((tech) => {
          techMap[tech.id_usuario] = {
            name: tech.nombre,
            resolved: tech.tickets_resueltos,
            open: 0, // por ahora
          };
        });

        openData.forEach((tech) => {
          if (techMap[tech.id_usuario]) {
            techMap[tech.id_usuario].open = tech.tickets_abiertos;
          } else {
            techMap[tech.id_usuario] = {
              name: tech.nombre,
              resolved: 0,
              open: tech.tickets_abiertos,
            };
          }
        });

        setTechnicians(Object.values(techMap));
      })
      .catch((err) => console.error("Error al obtener progreso:", err));
  }, []);

  return (
    <div className="progress-card">
      <h2>Progreso</h2>
      <p className="progress-subtitle">
        Avance en la resolución de tickets resueltos vs abiertos por técnico.
      </p>

      <div className="technician-list">
        {technicians.map((tech, index) => {
          const progress = getProgress(tech.resolved, tech.open);
          const initials = getInitials(tech.name);

          return (
            <div key={index} className="technician-row">
              <div className="technician-info">
                <div className="technician-avatar">{initials}</div>
                <span className="technician-name">{tech.name}</span>
              </div>

              <div className="ticket-count">
                {tech.resolved}/{tech.open}tickets
              </div>

              <div className="progress-bar-container">
                <p className="progress-percentage">{progress}%</p>
                <div className="progress-bar-bg">
                  <div
                    className="progress-bar-fill"
                    style={{ width: `${progress}%` }}
                  ></div>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default CardProgreso;
