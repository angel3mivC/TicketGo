import { useEffect, useState, useRef } from "react";
import "./styles/DetallesTickets.css";
import Gallery from "./Gallery.jsx";
import FileCard from "./FileCard";

const estadosDisponibles = [
  { id: 1, nombre: "Abierto", color: "limegreen" },
  { id: 2, nombre: "En Progreso", color: "orange" },
  { id: 3, nombre: "Cerrado", color: "indianred" },
  { id: 4, nombre: "Resuelto", color: "dodgerblue" },
  { id: 5, nombre: "Reabierto", color: "gray" },
];

const prioridadesDisponibles = [
  { id: 1, nombre: "Alta", color: "#ff6b6b" },
  { id: 2, nombre: "Media", color: "#ffa500" },
  { id: 3, nombre: "Baja", color: "#4caf50" },
];

const API_URL = "https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com";

const DetallesTickets = ({ id_ticket }) => {
  const [ticket, setTicket] = useState(null);
  const [comentarios, setComentarios] = useState([]);
  const [evidencias, setEvidencias] = useState([]);
  const [nuevoComentario, setNuevoComentario] = useState("");
  const [menuAbierto, setMenuAbierto] = useState(false);
  const [loading, setLoading] = useState(true);
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [filesToUpload, setFilesToUpload] = useState([]);
  const [menuPrioridadAbierto, setMenuPrioridadAbierto] = useState(false);

  const [menuCategoriaAbierto, setMenuCategoriaAbierto] = useState(false);
  const [categorias, setCategorias] = useState([]);

  const [showAssignPanel, setShowAssignPanel] = useState(false);
  const [technicians, setTechnicians] = useState([]);
  const [selectedTechId, setSelectedTechId] = useState(null);

  const [searchTerm, setSearchTerm] = useState("");

  const token = localStorage.getItem("token");
  const comentarioInput = useRef(null);

  const [showGalleryModal, setShowGalleryModal] = useState(false);

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

        if (ticketData.asignado_id) setSelectedTechId(ticketData.asignado_id);
      } catch (err) {
        console.error("Error al obtener datos del ticket:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id_ticket, token]);

  useEffect(() => {
    const fetchCategorias = async () => {
      try {
        const res = await fetch(`${API_URL}/catalogs/categorias`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (!res.ok) {
          console.error("Error al obtener categorías:", res.statusText);
          setCategorias([]);
          return;
        }
        const data = await res.json();
        setCategorias(data || []);
      } catch (err) {
        console.error("Error obteniendo categorías:", err);
        setCategorias([]);
      }
    };

    fetchCategorias();
  }, [token]);

  useEffect(() => {
    const cerrar = (e) => {
      const dropdown = document.querySelector(".categoria-dropdown");
      if (!dropdown) return;
      if (!dropdown.contains(e.target)) {
        setMenuCategoriaAbierto(false);
      }
    };
    document.addEventListener("click", cerrar);
    return () => document.removeEventListener("click", cerrar);
  }, []);

  useEffect(() => {
    const cerrar = (e) => {
      const dropdownPrioridad = document.querySelector(".prioridad-dropdown");
      if (dropdownPrioridad && !dropdownPrioridad.contains(e.target)) {
        setMenuPrioridadAbierto(false);
      }
    };
    document.addEventListener("click", cerrar);
    return () => document.removeEventListener("click", cerrar);
  }, []);

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

  const cambiarPrioridad = async (nuevaPrioridad) => {
    try {
      const response = await fetch(`${API_URL}/tickets/${id_ticket}/priority`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ prioridad_id: nuevaPrioridad.id }),
      });

      const data = await response.json();
      if (response.ok) {
        setTicket((prev) => ({ ...prev, prioridad: nuevaPrioridad.nombre }));
        setMenuPrioridadAbierto(false);
      } else {
        console.error("Error al actualizar prioridad:", data.message);
      }
    } catch (error) {
      console.error("Error de conexión:", error);
    }
  };

  const cambiarCategoria = async (categoria_id, categoria_nombre) => {
    try {
      if (categoria_nombre === ticket.categoria) {
        setMenuCategoriaAbierto(false);
        return;
      }

      const res = await fetch(`${API_URL}/tickets/${id_ticket}/category`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ categoria_id }),
      });

      const data = await res.json();
      if (res.ok) {
        setTicket((prev) => ({ ...prev, categoria: categoria_nombre }));
        setMenuCategoriaAbierto(false);
      } else {
        console.error("Error cambiando categoría:", data.message || data);
      }
    } catch (err) {
      console.error("Error al cambiar categoría:", err);
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
      // Restricción para técnicos: solo pueden cambiar entre "En Progreso" y "Resuelto"
      if (role === 3) {
        const estadosPermitidos = ["En Progreso", "Resuelto"];
        if (!estadosPermitidos.includes(nuevoEstado.nombre)) {
          alert(
            "Los técnicos solo pueden cambiar entre 'En Progreso' y 'Resuelto'"
          );
          return;
        }
      }

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

  const getEstadosDisponibles = () => {
    if (role === 3) {
      return estadosDisponibles.filter(
        (estado) =>
          estado.nombre === "En Progreso" || estado.nombre === "Resuelto"
      );
    }
    return estadosDisponibles;
  };

  const getEvidenciasLimitadas = () => {
    if (role === 3) {
      return evidencias.slice(-1);
    } else if (role === 1 || role === 2) {
      return evidencias.slice(-2);
    }
    return evidencias;
  };

  const getTechniciansFiltrados = () => {
    if (!searchTerm.trim()) {
      return technicians;
    }

    return technicians.filter((tech) => {
      const techName = tech.nombre ?? tech.name ?? tech.email ?? "";
      return techName.toLowerCase().includes(searchTerm.toLowerCase());
    });
  };

  const asignarTecnico = async (tecnico_id) => {
    try {
      const response = await fetch(`${API_URL}/tickets/${id_ticket}/assign`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ tecnico_id }),
      });

      const data = await response.json();

      if (response.ok) {
        let nombre = null;

        if (tecnico_id !== null) {
          const techObj = technicians.find(
            (t) => t.id_usuario === tecnico_id || t.id === tecnico_id
          );
          nombre =
            techObj?.nombre || techObj?.name || data.asignado || "Técnico";
        }

        setTicket((prev) => ({
          ...prev,
          asignado_a: nombre,
          asignado_id: tecnico_id,
        }));
        setSelectedTechId(tecnico_id);
        setShowAssignPanel(false);

        const mensaje =
          tecnico_id === null
            ? "Asignación eliminada correctamente"
            : `Ticket asignado a ${nombre}`;
        console.log(mensaje);
      } else {
        console.error("Error al asignar técnico:", data);
        alert(`Error: ${data.message || "No se pudo asignar el técnico"}`);
      }
    } catch (err) {
      console.error("Error al asignar técnico:", err);
      alert("Error de conexión al asignar técnico");
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

  const getFileIcon = (mimeType) => {
    if (!mimeType) return "📄";

    if (mimeType.startsWith("image/")) return "🖼️";
    if (mimeType.startsWith("video/")) return "🎬";
    if (mimeType.startsWith("audio/")) return "🎵";
    if (mimeType.includes("pdf")) return "📕";
    if (mimeType.includes("word") || mimeType.includes("document")) return "📘";
    if (mimeType.includes("excel") || mimeType.includes("sheet")) return "📊";
    if (mimeType.includes("powerpoint") || mimeType.includes("presentation"))
      return "📙";
    if (
      mimeType.includes("zip") ||
      mimeType.includes("rar") ||
      mimeType.includes("compressed")
    )
      return "🗜️";
    if (mimeType.includes("text")) return "📝";

    return "📄";
  };

  const formatFileSize = (bytes) => {
    if (!bytes) return "0 KB";
    const kb = bytes / 1024;
    if (kb < 1024) return `${kb.toFixed(1)} KB`;
    const mb = kb / 1024;
    return `${mb.toFixed(1)} MB`;
  };

  const toggleAssignPanel = async () => {
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

  const getAvatarColor = (name) => {
    if (!name) return "#999";

    let hash = 0;
    for (let i = 0; i < name.length; i++) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash);
    }

    const hue = Math.abs(hash % 360);
    const saturation = 65 + (Math.abs(hash) % 20);
    const lightness = 50 + (Math.abs(hash >> 8) % 15);

    return `hsl(${hue}, ${saturation}%, ${lightness}%)`;
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
              {getEstadosDisponibles().map((estado) => (
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
        {/* Categoría - clickable (solo Admin/Mesa abrirá el dropdown) */}
        <div
          className="categoria-dropdown"
          style={{ position: "relative", display: "inline-block" }}
        >
          <span
            className={`tag categoria clickable ${
              menuCategoriaAbierto ? "open" : ""
            }`}
            onClick={() => {
              if (role === 1) setMenuCategoriaAbierto((v) => !v);
            }}
            role="button"
            tabIndex={0}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                if (role === 1) setMenuCategoriaAbierto((v) => !v);
              }
            }}
          >
            {ticket.categoria}
          </span>

          {menuCategoriaAbierto && role === 1 && (
            <div
              className="categoria-menu"
              style={{
                position: "absolute",
                top: "calc(100% + 8px)",
                left: 0,
                minWidth: 130,
                background: "#fff",
                borderRadius: 20,
                boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
                zIndex: 50,
                padding: "8px",
                maxHeight: 280,
                overflowY: "auto",
                border: "1px solid #e0e0e0",
              }}
            >
              {categorias.length ? (
                categorias.map((cat) => {
                  const isCurrent = cat.nombre === ticket.categoria;
                  return (
                    <button
                      key={cat.id_categoria}
                      className={`categoria-option ${
                        isCurrent ? "disabled" : ""
                      }`}
                      style={{
                        display: "block",
                        width: "100%",
                        padding: "6px 14px",
                        border: isCurrent
                          ? "2px solid #000"
                          : "1px solid #e0e0e0",
                        background: isCurrent ? "#000" : "#fff",
                        color: isCurrent ? "#fff" : "#333",
                        textAlign: "center",
                        cursor: isCurrent ? "not-allowed" : "pointer",
                        borderRadius: 25,
                        marginBottom: "8px",
                        fontSize: "12px",
                        fontWeight: isCurrent ? "600" : "400",
                        transition: "all 0.2s ease",
                      }}
                      onMouseEnter={(e) => {
                        if (!isCurrent) {
                          e.currentTarget.style.background = "#f5f5f5";
                          e.currentTarget.style.borderColor = "#ccc";
                        }
                      }}
                      onMouseLeave={(e) => {
                        if (!isCurrent) {
                          e.currentTarget.style.background = "#fff";
                          e.currentTarget.style.borderColor = "#e0e0e0";
                        }
                      }}
                      disabled={isCurrent}
                      onClick={() =>
                        cambiarCategoria(cat.id_categoria, cat.nombre)
                      }
                    >
                      {cat.nombre}
                    </button>
                  );
                })
              ) : (
                <div
                  style={{ padding: 12, textAlign: "center", color: "#666" }}
                >
                  Cargando categorías...
                </div>
              )}
            </div>
          )}
        </div>

        {/* Prioridad - clickable (solo Admin puede cambiarla) */}
        <div
          className="prioridad-dropdown"
          style={{ position: "relative", display: "inline-block" }}
        >
          <span
            className={`tag priority ${getPriorityClass(ticket.prioridad)} ${
              role === 1 ? "clickable" : ""
            } ${menuPrioridadAbierto ? "open" : ""}`}
            onClick={() => {
              if (role === 1) setMenuPrioridadAbierto((v) => !v);
            }}
            role="button"
            tabIndex={0}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                if (role === 1) setMenuPrioridadAbierto((v) => !v);
              }
            }}
          >
            <span
              className={`priority-dot ${getPriorityClass(ticket.prioridad)}`}
            ></span>
            {ticket.prioridad}
          </span>

          {menuPrioridadAbierto && role === 1 && (
            <div
              className="prioridad-menu"
              style={{
                position: "absolute",
                top: "calc(100% + 8px)",
                left: 0,
                minWidth: 150,
                background: "#fff",
                borderRadius: 12,
                boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
                zIndex: 50,
                padding: "8px",
                border: "1px solid #e0e0e0",
              }}
            >
              {prioridadesDisponibles.map((prioridad) => {
                const isCurrent = prioridad.nombre === ticket.prioridad;
                return (
                  <button
                    key={prioridad.id}
                    onClick={() => cambiarPrioridad(prioridad)}
                    disabled={isCurrent}
                    style={{
                      display: "flex",
                      alignItems: "center",
                      gap: "8px",
                      width: "100%",
                      padding: "8px 12px",
                      border: "none",
                      background: isCurrent ? "#f5f5f5" : "transparent",
                      textAlign: "left",
                      cursor: isCurrent ? "not-allowed" : "pointer",
                      borderRadius: "8px",
                      fontSize: "14px",
                      opacity: isCurrent ? 0.6 : 1,
                      transition: "background 0.2s ease",
                    }}
                    onMouseEnter={(e) => {
                      if (!isCurrent) {
                        e.currentTarget.style.background = "#f5f5f5";
                      }
                    }}
                    onMouseLeave={(e) => {
                      if (!isCurrent) {
                        e.currentTarget.style.background = "transparent";
                      }
                    }}
                  >
                    <span
                      className="priority-dot-option"
                      style={{
                        width: "12px",
                        height: "12px",
                        borderRadius: "50%",
                        backgroundColor: prioridad.color,
                        flexShrink: 0,
                      }}
                    ></span>
                    {prioridad.nombre}
                  </button>
                );
              })}
            </div>
          )}
        </div>
      </div>

      {/* Mostrar nombre del técnico y botón para cambiar (solo Admin y Mesa) */}
      <div className="ticket-tecnico">
        <div
          className="user-avatar small"
          style={{ backgroundColor: getAvatarColor(ticket.asignado_a) }}
        >
          {ticket.asignado_a
            ? ticket.asignado_a
                .split(" ")
                .map((n) => n[0])
                .join("")
                .toUpperCase()
            : "?"}
        </div>

        <div className="tecnico-nombre">
          {ticket.asignado_a || "Sin asignar"}
        </div>

        {(role === 1 || role === 2) && (
          <button className="btn-asignar" onClick={toggleAssignPanel}>
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
            >
              <path d="M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5L17 3z" />
            </svg>
          </button>
        )}
      </div>

      {/* Modal de asignación (visible para Admin/Mesa) */}
      {showAssignPanel && (role === 1 || role === 2) && (
        <div
          className="modal-overlay"
          onClick={() => setShowAssignPanel(false)}
        >
          <div className="modal-assign" onClick={(e) => e.stopPropagation()}>
            {/* Header del modal */}
            <div className="modal-header">
              <h2>Técnicos Disponibles</h2>
              <button
                className="modal-close"
                onClick={() => setShowAssignPanel(false)}
              >
                ✕
              </button>
            </div>

            {/* Buscador */}
            <div className="modal-search">
              <svg
                width="20"
                height="20"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
              >
                <circle cx="11" cy="11" r="8"></circle>
                <path d="m21 21-4.35-4.35"></path>
              </svg>
              <input
                type="text"
                placeholder="Buscar"
                className="search-input"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>

            {/* Lista de técnicos */}
            <div className="technicians-list">
              {getTechniciansFiltrados().length > 0 ? (
                getTechniciansFiltrados().map((tech) => {
                  const techId = tech.id_usuario ?? tech.id;
                  const techName =
                    tech.nombre ??
                    tech.name ??
                    tech.email ??
                    `Técnico ${techId}`;
                  const isSelected = selectedTechId === techId;

                  return (
                    <div
                      key={techId}
                      className={`technician-item ${
                        isSelected ? "selected" : ""
                      }`}
                    >
                      <div
                        className="tech-avatar"
                        style={{ backgroundColor: getAvatarColor(techName) }}
                      >
                        {techName
                          .split(" ")
                          .map((n) => n[0])
                          .join("")
                          .toUpperCase()}
                      </div>
                      <div className="tech-info">
                        <div className="tech-name">{techName}</div>
                        <div className="tech-status">
                          <span className="status-dot active"></span>
                          Activo
                        </div>
                      </div>
                      <button
                        className="btn-add"
                        onClick={() => {
                          setSelectedTechId(techId);
                          asignarTecnico(techId);
                        }}
                      >
                        <svg
                          width="20"
                          height="20"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                        >
                          <line x1="12" y1="5" x2="12" y2="19"></line>
                          <line x1="5" y1="12" x2="19" y2="12"></line>
                        </svg>
                      </button>
                    </div>
                  );
                })
              ) : (
                <div className="no-technicians">
                  {searchTerm
                    ? "No se encontraron técnicos"
                    : "No hay técnicos disponibles"}
                </div>
              )}
            </div>

            {/* Botón para quitar asignación */}
            {ticket.asignado_a && (
              <div className="modal-footer">
                <button
                  className="btn-unassign"
                  onClick={() => {
                    asignarTecnico(null);
                  }}
                >
                  Quitar asignación
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      <p className="ticket-description">{ticket.descripcion}</p>

      {/* Galería - visible para todos los perfiles */}
      <div className="ticket-evidencias">
        <div className="evidencias-header">
          <h3>Evidencias</h3>
          <button
            className="btn-ver-todos"
            onClick={() => setShowGalleryModal(true)}
          >
            Ver Todos
            <svg
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
            >
              <polyline points="9 18 15 12 9 6"></polyline>
            </svg>
          </button>
        </div>

        <div
          className={`evidencias-content ${
            role === 1 || role === 2 ? "content-admin-mesa" : ""
          }`}
        >
          <Gallery
            evidencias={getEvidenciasLimitadas()}
            ticketId={id_ticket}
            apiUrl={API_URL}
            token={token}
          />

          {/* Botón de agregar - solo para Técnico */}
          {role === 3 && (
            <div className="upload-section">
              <button
                className="add-file-button"
                onClick={() => setShowUploadModal(true)}
              >
                <svg
                  width="80"
                  height="80"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <line x1="12" y1="5" x2="12" y2="19"></line>
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
                <span className="add-file-text">Agregar</span>
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Modal de Galería Completa */}
      {showGalleryModal && (
        <div
          className="modal-overlay"
          onClick={() => setShowGalleryModal(false)}
        >
          <div className="modal-gallery" onClick={(e) => e.stopPropagation()}>
            {/* Header */}
            <div className="modal-header">
              <h2>Galería</h2>
              <button
                className="modal-close"
                onClick={() => setShowGalleryModal(false)}
              >
                ✕
              </button>
            </div>

            {/* Subtitle */}
            <p className="gallery-subtitle">
              Consulta todas las evidencias cargadas.
            </p>

            {/* Grid de evidencias */}
            {evidencias.length > 0 ? (
              <Gallery
                evidencias={evidencias}
                ticketId={id_ticket}
                apiUrl={API_URL}
                token={token}
              />
            ) : (
              <div className="gallery-empty">
                <div className="empty-icon">
                  <svg
                    width="120"
                    height="120"
                    viewBox="0 0 120 120"
                    fill="none"
                  >
                    <rect
                      x="20"
                      y="30"
                      width="35"
                      height="45"
                      rx="4"
                      fill="#E8E8E8"
                      transform="rotate(-5 20 30)"
                    />
                    <rect
                      x="50"
                      y="25"
                      width="35"
                      height="45"
                      rx="4"
                      fill="#D0D0D0"
                    />
                    <rect
                      x="35"
                      y="35"
                      width="15"
                      height="15"
                      rx="2"
                      fill="#FFFFFF"
                    />
                    <path
                      d="M40 55L45 50L50 55"
                      stroke="#FFFFFF"
                      strokeWidth="2"
                      strokeLinecap="round"
                    />
                  </svg>
                </div>
                <h3>Sin archivos</h3>
                <p>Cuando subas evidencias, aparecerán en este espacio.</p>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Subida de archivos - solo para Técnico */}
      {role === 3 && (
        <>
          {/* Modal de subida de archivos */}
          {showUploadModal && (
            <div
              className="modal-overlay"
              onClick={() => setShowUploadModal(false)}
            >
              <div
                className="modal-upload"
                onClick={(e) => e.stopPropagation()}
              >
                {/* Header */}
                <div className="modal-header">
                  <h2>Subir Archivos</h2>
                  <button
                    className="modal-close"
                    onClick={() => {
                      setShowUploadModal(false);
                      setFilesToUpload([]);
                    }}
                  >
                    ✕
                  </button>
                </div>

                {/* Drop Zone */}
                <div
                  className="upload-drop-zone"
                  onClick={() =>
                    document.getElementById("file-upload-modal").click()
                  }
                >
                  <input
                    type="file"
                    id="file-upload-modal"
                    multiple
                    style={{ display: "none" }}
                    onChange={(e) => {
                      const newFiles = Array.from(e.target.files);
                      setFilesToUpload((prev) => [...prev, ...newFiles]);
                    }}
                  />

                  <div className="drop-zone-icon">
                    <svg
                      width="48"
                      height="48"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                    >
                      <line x1="12" y1="5" x2="12" y2="19"></line>
                      <line x1="5" y1="12" x2="19" y2="12"></line>
                    </svg>
                  </div>

                  <h3>Agregar Archivos</h3>
                  <p className="upload-subtitle">
                    Tamaño máximo permitido en total: 100 MB
                  </p>
                </div>

                {/* Lista de archivos seleccionados */}
                {filesToUpload.length > 0 && (
                  <div className="files-list">
                    {filesToUpload.map((file, index) => {
                      const fileIcon = getFileIcon(file.type);

                      return (
                        <div key={index} className="file-item-upload">
                          <div className="file-item-icon">
                            <span>{fileIcon}</span>
                          </div>
                          <div className="file-item-info">
                            <p className="file-item-name">{file.name}</p>
                            <p className="file-item-size">
                              {formatFileSize(file.size)}
                            </p>
                          </div>
                          <button
                            className="file-item-remove"
                            onClick={() => {
                              setFilesToUpload((prev) =>
                                prev.filter((_, i) => i !== index)
                              );
                            }}
                          >
                            ✕
                          </button>
                        </div>
                      );
                    })}
                  </div>
                )}

                {/* Botón de cargar */}
                {filesToUpload.length > 0 && (
                  <div className="modal-footer">
                    <button
                      className="btn-upload-files"
                      onClick={async () => {
                        try {
                          for (const file of filesToUpload) {
                            const form = new FormData();
                            form.append("file", file);

                            const res = await fetch(
                              `${API_URL}/tickets/${id_ticket}/files`,
                              {
                                method: "POST",
                                headers: {
                                  Authorization: `Bearer ${token}`,
                                },
                                body: form,
                              }
                            );

                            if (!res.ok) {
                              const errorData = await res.json();
                              console.error(
                                `Error al subir ${file.name}:`,
                                errorData
                              );
                              alert(
                                `Error al subir ${file.name}: ${
                                  errorData.message || errorData.error
                                }`
                              );
                              continue;
                            }

                            const data = await res.json();
                            console.log(
                              `Archivo ${file.name} subido correctamente:`,
                              data
                            );
                          }

                          await refreshFiles();
                          setFilesToUpload([]);
                          setShowUploadModal(false);

                          alert(
                            "Todos los archivos se han subido correctamente"
                          );
                        } catch (err) {
                          console.error(
                            "Error general al subir archivos:",
                            err
                          );
                          alert(
                            "Hubo un error al subir los archivos. Por favor intenta de nuevo."
                          );
                        }
                      }}
                    >
                      Cargar evidencia
                    </button>
                  </div>
                )}
              </div>
            </div>
          )}
        </>
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
