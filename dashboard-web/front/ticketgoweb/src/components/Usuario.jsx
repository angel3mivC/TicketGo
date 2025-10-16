
import './styles/Usuario.css'

const Usuario = ({id_usuario,name,role, onEdit}) => {

    const EliminateUser = () => {
        fetch(`http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/users/${id_usuario}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("token")}`
            },
        })
        .then((res) => { 
            if (res.ok) {
                alert("Usuario eliminado correctamente");
                window.location.reload(); // Recargar la página para reflejar los cambios
            } else {
                alert("Error al eliminar el usuario");
            }
        })
    }

    return(
        <div className='User'>
            <div className='User-Avatar'/>
            <div className='User-Info'>
                <div className='User-Name'>{name}</div>
                <div className='User-Role'>{role}</div>
            </div>
            <div className='User-Edit' onClick={onEdit}>Editar</div>
            <div className='User-Elimination' onClick={EliminateUser}>✕</div>
        </div>
    )
}

export default Usuario