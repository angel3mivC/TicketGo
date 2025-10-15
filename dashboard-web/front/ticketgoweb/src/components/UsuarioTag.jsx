import './styles/UsuarioTag.css';

const UsuarioTag = ({initials,name,role}) => {



  return (
    <div className="user-badge">
      <div className="user-avat">{initials}</div>
      <div className="user-info">
        <div className="user-name">{name}</div>
        <div className="user-role">{role}</div>
      </div>
    </div>
  );
}

export default UsuarioTag
