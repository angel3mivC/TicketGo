import { useEffect, useState, useRef } from "react";
import "./styles/DetallesTickets.css";

const estadosDisponibles = [
  { id: 1, nombre: "Abierto", color: "limegreen" },
  { id: 2, nombre: "En Progreso", color: "orange" },
  { id: 3, nombre: "Resuelto", color: "dodgerblue" },
  { id: 4, nombre: "Cerrado", color: "indianred" },
  { id: 5, nombre: "Reabierto", color: "gray" },
];

const API_URL = "http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com";

const DetallesTickets = ({ id_ticket }) => {
  const [ticket, setTicket] = useState(null);
  const [comentarios, setComentarios] = useState([]);
  const [evidencias, setEvidencias] = useState([]);
  const [nuevoComentario, setNuevoComentario] = useState("");
  const [menuAbierto, setMenuAbierto] = useState(false);
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("token");
  const comentarioInput = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const headers = { Authorization: `Bearer ${token}` };

        const [ticketRes, commentsRes, filesRes] = await Promise.all([
          fetch(`${API_URL}/tickets/${id_ticket}`, { headers }),
          fetch(`${API_URL}/tickets/${id_ticket}/comments`, { headers }),
          fetch(`${API_URL}/tickets/${id_ticket}/files`, { headers }),
        ]);

        const ticketData = await ticketRes.json();
        const commentsData = await commentsRes.json();
        const filesData = await filesRes.json();

        const normalizedComments = commentsData.map((c) => ({
          usuario: c.autor || "Usuario desconocido",
          descripcion: c.comentario || c.descripcion || "",
          fecha: new Date(c.fecha).toLocaleDateString(),
          hora: new Date(c.fecha).toLocaleTimeString(),
        }));

        setTicket(ticketData);
        setComentarios(normalizedComments);
        setEvidencias(filesData);
      } catch (err) {
        console.error("Error al obtener datos del ticket:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id_ticket, token]);

  const cambiarEstado = async (nuevoEstado) => {
    try {
      const response = await fetch(`${API_URL}/tickets/${id_ticket}/state`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ estado_id: nuevoEstado.id }),
      });

      const data = await response.json();
      if (response.ok) {
        setTicket((prev) => ({ ...prev, estado: nuevoEstado.nombre }));
        setMenuAbierto(false);
      } else {
        console.error("Error al actualizar estado:", data.message);
      }
    } catch (error) {
      console.error("Error de conexión:", error);
    }
  };

  const enviarComentario = async () => {
    const comentario = nuevoComentario.trim();
    if (!comentario) return;

    try {
      const response = await fetch(`${API_URL}/tickets/${id_ticket}/comments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ comentario }),
      });

      const data = await response.json();
      if (response.ok) {
        setComentarios((prev) => [
          ...prev,
          {
            usuario: localStorage.getItem("user"),
            fecha: new Date().toLocaleDateString(),
            hora: new Date().toLocaleTimeString(),
            descripcion: comentario,
          },
        ]);
        setNuevoComentario("");
        comentarioInput.current.value = "";
      } else {
        console.error("Error al crear comentario:", data.message);
      }
    } catch (error) {
      console.error("Error al enviar comentario:", error);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case "Abierto":
        return "status-open";
      case "En Progreso":
        return "status-inprogress";
      case "Resuelto":
        return "status-resolved";
      case "Cerrado":
        return "status-closed";
      case "Reabierto":
        return "status-reopened";
      default:
        return "";
    }
  };

  const getPriorityClass = (priority) => {
    switch (priority) {
      case "Alta":
        return "priority-high";
      case "Media":
        return "priority-medium";
      case "Baja":
        return "priority-low";
      default:
        return "";
    }
  };

  if (loading) return <p>Cargando detalles del ticket...</p>;
  if (!ticket) return <p>No se encontró el ticket.</p>;

  return (
    <div className="detalles-ticket-container">
      <div className="ticket-header">
        <div>
          <h2 className="ticket-title">{ticket.titulo}</h2>
          <p className="ticket-date">
            {new Date(ticket.fecha_creacion).toLocaleDateString()} &nbsp;
            {new Date(ticket.fecha_creacion).toLocaleTimeString()}
          </p>
        </div>

        {/* Dropdown de estado */}
        <div className="status-dropdown">
          <div
            className={`ticket-status ${getStatusClass(ticket.estado)}`}
            onClick={() => setMenuAbierto((prev) => !prev)}
          >
            <span className="status-dot"></span>
            {ticket.estado}
          </div>

          {menuAbierto && (
            <div className="status-menu">
              {estadosDisponibles.map((estado) => (
                <button
                  key={estado.id}
                  onClick={() => cambiarEstado(estado)}
                  disabled={estado.nombre === ticket.estado}
                >
                  <span
                    className="status-dot-option"
                    style={{ backgroundColor: estado.color }}
                  ></span>
                  {estado.nombre}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      <div className="ticket-tags">
        <span className="tag categoria">{ticket.categoria}</span>
        <span className={`tag prioridad ${getPriorityClass(ticket.prioridad)}`}>
          {ticket.prioridad}
        </span>
      </div>

      <p className="ticket-description">{ticket.descripcion}</p>

      {/* Evidencias */}
      <div className="ticket-evidencias">
        <div className="evidencias-header">
          <h3>Evidencias</h3>
        </div>
        <div className="evidencias-grid">
          {evidencias.length > 0 ? (
            evidencias.map((file, index) => {
              const isImage = file.tipo_mime.startsWith("image/");
              const isPDF = file.tipo_mime === "application/pdf";

              return (
                <div key={index} className="evidencia-card">
                  {isImage ? (
                    <img
                      src={`data:${file.tipo_mime};base64,${file.archivo}`}
                      alt={file.nombre_original}
                      className="evidencia-preview"
                    />
                  ) : isPDF ? (
                    <div className="evidencia-pdf">
                      <i className="fa-solid fa-file-pdf"></i>
                      <span>PDF</span>
                    </div>
                  ) : (
                    <div className="evidencia-file">
                      <i className="fa-solid fa-file"></i>
                      <span>
                        {file.nombre_original.split(".").pop().toUpperCase()}
                      </span>
                    </div>
                  )}

                  <div className="evidencia-footer">
                    <p className="evidencia-nombre">{file.nombre_original}</p>
                    <a
                      href={`data:${file.tipo_mime};base64,${file.archivo}`}
                      download={file.nombre_original}
                      className="evidencia-descargar"
                    >
                      Descargar
                    </a>
                  </div>
                </div>
              );
            })
          ) : (
            <p>No hay evidencias adjuntas.</p>
          )}
        </div>
      </div>

      <hr className="divider" />

      {/* Comentarios */}
      {comentarios.map((comentario, index) => (
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

      {/* Agregar comentario */}
      <div className="comentario-input">
        <input
          type="text"
          placeholder="Ingresa un comentario"
          className="input-text"
          ref={comentarioInput}
          onChange={(e) => setNuevoComentario(e.target.value)}
        />
        <button className="send-button" onClick={enviarComentario}>
          ↑
        </button>
      </div>
    </div>
  );
};

export default DetallesTickets;
