import {useRef, useState} from "react"
import { useNavigate } from "react-router-dom"

import "./styles/Login.css"
import logo from "../assets/logo.png"
import password from "../assets/logo_contraseña.png"
import profile from "../assets/perfil.png"
import icons_error from "../assets/error.png"

const Login = () => {
    const [error, setError] = useState(false)
    const formulario = useRef(null);
    const navigate = useNavigate();
    const login = async (evt) => {
        evt.preventDefault()
        const form = formulario.current
        const correo = form.correo.value
        const contraseña = form.contraseña.value
        const response = await fetch("http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/auth/login", {
            method: "POST",
            headers: {
            "Content-Type": "application/json"
            },
            body: JSON.stringify({ correo, contraseña })
        })
        const data = await response.json()
        if(data.token){
            localStorage.setItem("token", data.token)
            localStorage.setItem("user", data.user.nombre)
            localStorage.setItem("rol", data.user.rol)
            localStorage.setItem("id_usuario", data.user.id)
         }else{
            setError(true)
            return
        }
        switch(data.user.rol){
            case 1:
                navigate("/admin")
                break;
            case 2:
                navigate("/mesatrabajo")
                break;
            case 3:
                navigate("/tecnico")
                break;
            default:
                navigate("/")
                break;
        }
    }

    return (
        <div className="body">
            <img src={logo} className="login-logo" alt="Logo" />
            <div className="login-title">¡Bienvenido!</div>
            <div className="login-subtitle">Inicia sesión en tu cuenta</div>
            <form onSubmit={login} ref={formulario} className="login-form">
                <div className="input-container">
                    <img src={profile} className="login-icons" alt="User" />
                    <input type="email" name="correo" placeholder="Usuario" className="login-input" />
                </div>
                <div className="input-container">
                    <img src={password} className="login-iconspassword" alt="Password" />
                    <input type="password" name="contraseña" placeholder="Contraseña" className="login-input"/>
                </div>
                <button type="button" className="login-forgotpass" onClick={() => navigate("/recuperar-contra")}>¿Olvidaste tu contraseña?</button>
                {error && (<div className="login-error">
                    <img src={icons_error} className="icon-error" alt="error" />
                    Usuario o contraseña incorrectos</div>)}
                <button type="submit" className="login-button">Iniciar Sesión</button>
            </form>
        </div>
    )
}

export default Login