import { useState } from 'react'
import './styles/EditarUsuario.css'

const EditarUsuario = ({usuario}) => {
    const nombre = usuario.nombre
    const correo = usuario.correo

    const [categoria, setCategoria] = useState(usuario.id_rol)

    return(
        <div className="form-edit-user">
            <div className="form-edit-title">Editar Usuario</div>
            <form className="form-edit">
                <div className="input-edit">
                    <label>Nombre</label>
                    <input defaultValue={nombre} type="text"  className="input-editText"/>
                </div>
                <div className="input-edit">
                    <label>Correo</label>
                    <input defaultValue={correo} type="email"  className="input-editText"/>
                </div>
                <div className="input-edit">
                    <label>Contraseña</label>
                    <input type="password"  className="input-editText"/>
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
               
                <button className="edit-button">Guardar Cambios</button>
            </form>
        </div>
    )
}

export default EditarUsuario