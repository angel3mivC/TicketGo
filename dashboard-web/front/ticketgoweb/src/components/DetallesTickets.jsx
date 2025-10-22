import { useEffect, useState, useRef } from "react";
import "./styles/DetallesTickets.css";
import Gallery from "./Gallery.jsx";

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
  const [fileToUpload, setFileToUpload] = useState(null);

  // Nuevos estados para asignación de técnico
  const [showAssignPanel, setShowAssignPanel] = useState(false);
  const [technicians, setTechnicians] = useState([]);
  const [selectedTechId, setSelectedTechId] = useState(null);

  const token = localStorage.getItem("token");
  const comentarioInput = useRef(null);

  // Decodifica payload del JWT (intenta obtener id_rol)
  const getRoleFromToken = (t) => {
    try {
      if (!t) return null;
      const payload = t.split(".")[1];
      const json = JSON.parse(atob(payload));
      return json.id_rol || json.role || null;
    } catch {
      return null;
    }
  };
  const role = getRoleFromToken(token);

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

        // Si el ticket trae asignado el id y nombre, inicializa selectedTechId
        if (ticketData.asignado_id) setSelectedTechId(ticketData.asignado_id);
      } catch (err) {
        console.error("Error al obtener datos del ticket:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id_ticket, token]);

  const refreshFiles = async () => {
    try {
      const headers = { Authorization: `Bearer ${token}` };
      const res = await fetch(`${API_URL}/tickets/${id_ticket}/files`, {
        headers,
      });
      const data = await res.json();
      setEvidencias(data);
    } catch (err) {
      console.error("Error al refrescar archivos:", err);
    }
  };

  const fetchTechnicians = async () => {
    try {
      const headers = { Authorization: `Bearer ${token}` };
      let res = await fetch(`${API_URL}/users?rol=3`, { headers });
      if (!res.ok) {
        res = await fetch(`${API_URL}/users`, { headers });
      }
      if (!res.ok) {
        console.error("No se pudo obtener la lista de técnicos");
        setTechnicians([]);
        return;
      }
      const list = await res.json();
      const techs = list.filter(
        (u) => u.id_rol === 3 || u.rol === 3 || u.role === 3
      );
      setTechnicians(techs.length ? techs : list);
    } catch (err) {
      console.error("Error al obtener técnicos:", err);
      setTechnicians([]);
    }
  };

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

  const asignarTecnico = async (tecnico_id) => {
    try {
      const response = await fetch(`${API_URL}/tickets/${id_ticket}/assign`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ tecnico_id }), // si tecnico_id es null => quitar asignación
      });
      const data = await response.json();
      if (response.ok) {
        // Actualiza la UI: usa nombre disponible en la respuesta o busca en lista local
        const techObj = technicians.find(
          (t) => t.id_usuario === tecnico_id || t.id === tecnico_id
        );
        const nombre =
          tecnico_id === null
            ? null
            : techObj?.nombre || techObj?.name || data.asignado || "Técnico";
        setTicket((prev) => ({ ...prev, asignado_a: nombre }));
        setSelectedTechId(tecnico_id);
        setShowAssignPanel(false);
      } else {
        console.error("Error al asignar técnico:", data);
      }
    } catch (err) {
      console.error("Error al asignar técnico:", err);
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
            usuario: localStorage.getItem("user") || "Yo",
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

  const subirArchivo = async (e) => {
    e.preventDefault();
    if (!fileToUpload) return;

    const form = new FormData();
    form.append("file", fileToUpload);

    try {
      const res = await fetch(`${API_URL}/tickets/${id_ticket}/files`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
        body: form,
      });

      const data = await res.json();
      if (res.ok) {
        await refreshFiles();
        setFileToUpload(null);
      } else {
        console.error("Error al subir archivo:", data);
      }
    } catch (err) {
      console.error("Error al subir archivo:", err);
    }
  };

  const toggleAssignPanel = async () => {
    // Solo Admin (1) y Mesa (2) pueden abrir el panel
    if (role === 1 || role === 2) {
      if (!showAssignPanel) await fetchTechnicians();
      setShowAssignPanel((v) => !v);
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

          {menuAbierto && (role === 1 || role === 2 || role === 3) && (
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

      {/* Mostrar nombre del técnico y botón para cambiar (solo Admin y Mesa) */}
      <div className="ticket-tecnico">
        <div className="tecnico-info">
          <div className="user-avatar small"></div>
          <div className="tecnico-text">
            <div className="tecnico-nombre">
              {ticket.asignado_a || "— Sin asignar —"}
            </div>
          </div>
        </div>

        {(role === 1 || role === 2) && (
          <div className="tecnico-actions">
            <button onClick={toggleAssignPanel}>
              {showAssignPanel ? "Cerrar" : "Asignar / Reasignar"}
            </button>
          </div>
        )}
      </div>

      {/* Panel de asignación (visible para Admin/Mesa) */}
      {showAssignPanel && (role === 1 || role === 2) && (
        <div className="assign-panel">
          <label>Seleccionar técnico:</label>
          <select
            value={selectedTechId ?? ""}
            onChange={(e) =>
              setSelectedTechId(e.target.value ? Number(e.target.value) : null)
            }
          >
            <option value="">-- Sin asignar --</option>
            {technicians.map((t) => (
              <option key={t.id_usuario ?? t.id} value={t.id_usuario ?? t.id}>
                {t.nombre ??
                  t.name ??
                  `${t.email ?? "Técnico"} (${t.id_usuario ?? t.id})`}
              </option>
            ))}
          </select>
          <div className="assign-buttons">
            <button onClick={() => asignarTecnico(selectedTechId ?? null)}>
              Aplicar
            </button>
            <button onClick={() => asignarTecnico(null)}>
              Quitar asignación
            </button>
          </div>
        </div>
      )}

      <p className="ticket-description">{ticket.descripcion}</p>

      {/* Galería - visible para todos los perfiles */}
      <div className="ticket-evidencias">
        <div className="evidencias-header">
          <h3>Evidencias</h3>
        </div>
        <Gallery
          evidencias={evidencias}
          ticketId={id_ticket}
          apiUrl={API_URL}
          token={token}
        />
      </div>

      {/* Subida de archivos - solo para Técnico */}
      {role === 3 && (
        <div className="upload-section">
          <form onSubmit={subirArchivo}>
            <input
              type="file"
              onChange={(e) => setFileToUpload(e.target.files[0])}
            />
            <button type="submit">Subir archivo</button>
          </form>
        </div>
      )}

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

      {/* Agregar comentario - Admin/Mesa/Técnico */}
      {(role === 1 || role === 2 || role === 3) && (
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
      )}
    </div>
  );
};

export default DetallesTickets;
