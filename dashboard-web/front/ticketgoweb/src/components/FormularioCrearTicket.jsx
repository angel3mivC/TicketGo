import "./styles/CrearTicket.css";
import { useRef } from "react";

const CrearTicket = () => {
  const formulario = useRef(null);

  const CreateTicket = async (e) => {
    e.preventDefault(); // Evita recargar la página
    const form = formulario.current;

    const titulo = form.titulo.value.trim();
    const descripcion = form.descripcion.value.trim();
    const prioridad = form.prioridad.value;
    const categoria = form.categoria.value;
    const comentarios = form.comentarios.value.trim();

    try {
      console.log("📤 Enviando datos del ticket...");

      // Crear el ticket primero
      const resTicket = await fetch(
        `https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/tickets`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
          body: JSON.stringify({
            titulo,
            descripcion,
            id_categoria: categoria,
            id_prioridad: prioridad,
          }),
        }
      );

      if (!resTicket.ok) throw new Error("Error al crear el ticket");
      const ticketData = await resTicket.json();

      console.log("✅ Ticket creado correctamente:", ticketData);

      // Si hay comentario, agregarlo al ticket recién creado
      if (comentarios !== "") {
        console.log("🗨️ Agregando comentario al ticket...");
        const resComentario = await fetch(
          `https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/tickets/${ticketData.ticket_id}/comments`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            body: JSON.stringify({
              comentario: comentarios,
            }),
          }
        );

        if (!resComentario.ok)
          throw new Error("Error al agregar el comentario");
        const commentData = await resComentario.json();
        console.log("💬 Comentario agregado correctamente:", commentData);
      } else {
        console.log("📝 No se agregó comentario (campo vacío).");
      }

      console.log("🎉 Proceso completado con éxito.");
      window.location.reload();
    } catch (err) {
      console.error("❌ Error en el proceso de creación:", err);
    }
  };

  return (
    <div className="form-create-ticket">
      <div className="form-create-title">Crea ticket</div>
      <div className="form-create-description">
        Completa el formulario con todos los detalles para generar el ticket
      </div>

      <form className="form-create-forms" ref={formulario}>
        <div className="input-row">
          <input
            type="text"
            name="titulo"
            placeholder="Título"
            className="input-create"
            required
          />
          <input
            type="text"
            name="descripcion"
            placeholder="Descripción"
            className="input-create"
            required
          />
        </div>

        <div className="input-row">
          <select name="prioridad" className="spinner-create" required>
            <option value="">Prioridad</option>
            <option value={1}>Alta</option>
            <option value={2}>Media</option>
            <option value={3}>Baja</option>
          </select>

          <select name="categoria" className="spinner-create" required>
            <option value="">Categoría</option>
            <option value={1}>En Proceso</option>
            <option value={2}>Garantía</option>
            <option value={3}>Daño Inducido</option>
          </select>
        </div>

        <textarea
          name="comentarios"
          placeholder="Comentarios (opcional)"
          className="textarea-create"
        />

        <button type="submit" className="create-button" onClick={CreateTicket}>
          Crear
        </button>
      </form>
    </div>
  );
};

export default CrearTicket;
