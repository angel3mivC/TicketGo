import "./styles/Usuario.css";
import { useState } from "react";

const Usuario = ({ id_usuario, name, role, onEdit }) => {
  const userName = name;
  const initials = userName
    ? userName
        .split(" ")
        .map((n) => n[0])
        .join("")
        .toUpperCase()
    : "";
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const showDeleteConfirmation = () => {
    setShowConfirmModal(true);
  };

  const confirmDelete = () => {
    fetch(
      `https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/users/${id_usuario}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    )
      .then((res) => {
        if (res.ok) {
          console.log("Usuario eliminado correctamente");
          window.location.reload(); // Recargar la página para reflejar los cambios
        } else {
          console.log("Error al eliminar el usuario");
        }
      })
      .finally(() => {
        setShowConfirmModal(false);
      });
  };

  const cancelDelete = () => {
    setShowConfirmModal(false);
  };

  return (
    <>
      <div className="User">
        <div className="User-Avatar">{initials}</div>
        <div className="User-Info">
          <div className="User-Name">{name}</div>
          <div className="User-Role">{role}</div>
        </div>
        <div className="User-Edit" onClick={onEdit}>
          Editar
        </div>
        <div className="User-Elimination" onClick={showDeleteConfirmation}>
          ✕
        </div>
      </div>

      {showConfirmModal && (
        <div className="modal-overlay-confirm">
          <div className="modal-content-confirm">
            <h3>Confirmar eliminación</h3>
            <p>
              ¿Estás seguro de que quieres eliminar al usuario{" "}
              <strong>{name}</strong>?
            </p>
            <p>Esta acción no se puede deshacer.</p>
            <div className="modal-buttons">
              <button className="btn-cancel" onClick={cancelDelete}>
                Cancelar
              </button>
              <button className="btn-confirm" onClick={confirmDelete}>
                Eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default Usuario;
