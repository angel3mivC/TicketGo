import { useState } from 'react'
import './styles/Filtros.css'

const Filtros = ({ onClose, onApplyFilters, soloCategoria = false }) => {
    const [estadoSeleccionado, setEstadoSeleccionado] = useState('')
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState('')

    const estados = [
        { id: 'Abierto', nombre: 'Abierto' },
        { id: 'En Progreso', nombre: 'En Progreso' },
        { id: 'Pendiente', nombre: 'Pendiente' },
        { id: 'Resuelto', nombre: 'Resuelto' },
        { id: 'Cerrado', nombre: 'Cerrado' }
    ]

    const categorias = [
        { id: 'En proceso', nombre: 'En proceso' },
        { id: 'Garantia', nombre: 'Garantia' },
        { id: 'Daño inducido', nombre: 'Daño inducido' }
    ]

    const handleEstadoClick = (estado) => {
        setEstadoSeleccionado(estado === estadoSeleccionado ? '' : estado)
    }

    const handleCategoriaClick = (categoria) => {
        setCategoriaSeleccionada(categoria === categoriaSeleccionada ? '' : categoria)
    }

    const aplicarFiltros = () => {
        const filtros = {
            categoria: categoriaSeleccionada
        }
        
        if (!soloCategoria) {
            filtros.estado = estadoSeleccionado
        }
        
        onApplyFilters(filtros)
        onClose()
    }

    const limpiarFiltros = () => {
        setEstadoSeleccionado('')
        setCategoriaSeleccionada('')
        
        const filtros = {
            categoria: ''
        }
        
        if (!soloCategoria) {
            filtros.estado = ''
        }
        
        onApplyFilters(filtros)
        onClose()
    }

    return (
        <div className="modal-overlay">
            <div className="modal-filtros">
                <div className="filtros-header">
                    <div className="filtros-title">
                        <img src="/src/assets/filtro.svg" alt="filtro" className="filtro-icon" />
                        <span>Filtrar</span>
                    </div>
                    <button className="close-filtros" onClick={onClose}>✕</button>
                </div>
                
                <div className="filtros-content">
                    {!soloCategoria && (
                        <div className="filtro-section">
                            <div className="filtro-section-title">Estado</div>
                            <div className="filtro-options">
                                {estados.map((estado) => (
                                    <button
                                        key={estado.id}
                                        className={`filtro-option ${estadoSeleccionado === estado.id ? 'selected' : ''}`}
                                        onClick={() => handleEstadoClick(estado.id)}
                                    >
                                        {estado.nombre}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}

                    <div className="filtro-section">
                        <div className="filtro-section-title">Categoría</div>
                        <div className="filtro-options">
                            {categorias.map((categoria) => (
                                <button
                                    key={categoria.id}
                                    className={`filtro-option ${categoriaSeleccionada === categoria.id ? 'selected' : ''}`}
                                    onClick={() => handleCategoriaClick(categoria.id)}
                                >
                                    {categoria.nombre}
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

export default Filtros
