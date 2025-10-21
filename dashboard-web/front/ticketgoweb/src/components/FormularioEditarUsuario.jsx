import { useState, useRef } from 'react'
import './styles/EditarUsuario.css'

const EditarUsuario = ({usuario, onClose, onSuccess}) => {
    const formulario = useRef(null);
    const [categoria, setCategoria] = useState(usuario.id_rol)

    const editarUsuario = (e) => {
        e.preventDefault(); // Evita que el formulario recargue la página

        const form = formulario.current;
        const nombre = form.nombre.value;
        const correo = form.correo.value;
        const contraseña = form.contraseña.value;

        // Solo incluir contraseña si se proporcionó una nueva
        const datosActualizacion = { 
            nombre, 
            correo, 
            id_rol: categoria 
        };
        
        if (contraseña && contraseña.trim() !== '') {
            datosActualizacion.contraseña = contraseña;
        }

        fetch(`http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/users/${usuario.id_usuario}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(datosActualizacion)
        })
        .then((res) => {
            if (!res.ok) throw new Error("Error en la petición");
            return res.json();
        })
        .then((data) => {
            console.log("Usuario actualizado correctamente");
            onSuccess(); // Refrescar la lista
            onClose(); // Cerrar el modal
        })
        .catch((err) => {
            console.error("Error al actualizar usuario:", err);
            console.log("Hubo un error al actualizar el usuario");
        })
    }

    return(
        <div className="form-edit-user">
            <div className="form-edit-title">Editar Usuario</div>
            <form className="form-edit" ref={formulario}>
                <div className="input-edit">
                    <label>Nombre</label>
                    <input name="nombre" defaultValue={usuario.nombre} type="text"  className="input-editText"/>
                </div>
                <div className="input-edit">
                    <label>Correo</label>
                    <input name="correo" defaultValue={usuario.correo} type="email"  className="input-editText"/>
                </div>
                <div className="input-edit">
                    <label>Contraseña (dejar vacío para no cambiar)</label>
                    <input name="contraseña" type="password" placeholder="Nueva contraseña" className="input-editText"/>
                </div>
                <div className="input-edit">
                    <label>Rol</label>
                    <select className="spinner-edit-user" value={categoria} onChange={(e) => setCategoria(Number(e.target.value))}>
                        <option value=""></option>
                        <option value={1}>Administrador</option>
                        <option value={2}>Mesa de Ayuda</option>
                        <option value={3}>Técnico</option>
                    </select>
                </div>
               
                <button type="submit" className="edit-button" onClick={editarUsuario}>Guardar Cambios</button>
            </form>
        </div>
    )
}

export default EditarUsuario