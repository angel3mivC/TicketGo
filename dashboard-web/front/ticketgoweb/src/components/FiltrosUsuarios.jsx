import { useState } from 'react'
import './styles/Filtros.css'

const FiltrosUsuarios = ({ onClose, onApplyFilters }) => {
    const [rolSeleccionado, setRolSeleccionado] = useState('')

    const roles = [
        { id: '1', nombre: 'Administrador' },
        { id: '2', nombre: 'Mesa de Trabajo' },
        { id: '3', nombre: 'Técnico' }
    ]

    const handleRolClick = (rol) => {
        setRolSeleccionado(rol === rolSeleccionado ? '' : rol)
    }

    const aplicarFiltros = () => {
        const filtros = { rol: rolSeleccionado }
        onApplyFilters(filtros)
        onClose()
    }

    const limpiarFiltros = () => {
        setRolSeleccionado('')
        onApplyFilters({ rol: '' })
        onClose()
    }

    return (
        <div className="modal-overlay">
            <div className="modal-filtros">
                <div className="filtros-header">
                    <div className="filtros-title">
                        <img src="/src/assets/filtro.svg" alt="filtro" className="filtro-icon" />
                        <span>Filtrar Usuarios</span>
                    </div>
                    <button className="close-filtros" onClick={onClose}>✕</button>
                </div>

                <div className="filtros-content">
                    <div className="filtro-section">
                        <div className="filtro-section-title">Rol</div>
                        <div className="filtro-options">
                            {roles.map((rol) => (
                                <button
                                    key={rol.id}
                                    className={`filtro-option ${rolSeleccionado === rol.id ? 'selected' : ''}`}
                                    onClick={() => handleRolClick(rol.id)}
                                >
                                    {rol.nombre}
                                </button>
                            ))}
                        </div>
                    </div>
                </div>

                <div className="filtros-actions">
                    <button className="btn-limpiar" onClick={limpiarFiltros}>
                        Limpiar
                    </button>
                    <button className="btn-aplicar" onClick={aplicarFiltros}>
                        Aplicar
                    </button>
                </div>
            </div>
        </div>
    )
}

export default FiltrosUsuarios
