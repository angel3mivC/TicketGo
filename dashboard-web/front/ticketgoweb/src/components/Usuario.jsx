
import './styles/Usuario.css'

const Usuario = ({name,role}) => {


    return(
        <div className='User'>
            <div className='User-Avatar'/>
            <div className='User-Info'>
                <div className='User-Name'>{name}</div>
                <div className='User-Role'>{role}</div>
            </div>
            <div className='User-Edit'>Editar</div>
            <div className='User-Elimination'>✕</div>
        </div>
    )
}

export default Usuario