import { useState, useRef } from "react"
import './styles/CrearUsuario.css'

const CrearUsuario = () => {
    const formulario = useRef(null);
    const [categoria, setCategoria] = useState("")

    const crearUsuario = (e) => {
        e.preventDefault(); // Evita que el formulario recargue la página

        const form = formulario.current;
        const nombre = form.nombre.value;
        const correo = form.correo.value;
        const contraseña = form.contraseña.value;

       fetch(`http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/users`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify({ nombre, correo, contraseña, id_rol: categoria })
        })
        .then((res) => {
            if (!res.ok) throw new Error("Error en la petición"); // Status HTTP
            return res.json();
        })
        .then((data) => {
            console.log("Usuario creado correctamente");
            window.location.reload();
        })
        .catch((err) => {
            console.error("Error al crear usuario:", err);
            console.log("Hubo un error al crear el usuario");
        })

    }

    return(
        <div className="form-create-user">
            <div className="form-create-title">Crear Usuario</div>
            <div className="form-create-description">
                Completa los datos para generar un nuevo usuario en la plataforma.
            </div>
            <form className="form-create" ref={formulario}>
                <input type="text" name="nombre" placeholder="Nombre" className="input-create"/>
                <input type="email" name="correo" placeholder="Correo" className="input-create"/>
                <input type="password" name="contraseña" placeholder="Contraseña" className="input-create"/>
                <select className="spinner-create-user" value={categoria} onChange={(e) => setCategoria(Number(e.target.value))}>
                    <option value="">Rol</option>
                    <option value={1}>Administrador</option>
                    <option value={2}>Mesa de Ayuda</option>
                    <option value={3}>Técnico</option>
                </select>
                <button type="submit" className="create-button" onClick={crearUsuario}>Crear</button>
            </form>
        </div>
    )
}

export default CrearUsuario
