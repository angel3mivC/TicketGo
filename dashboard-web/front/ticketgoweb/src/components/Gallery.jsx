import React from "react";

const Gallery = ({ evidencias = [], ticketId, apiUrl }) => {
  if (!evidencias || evidencias.length === 0) {
    return <p>No hay evidencias adjuntas.</p>;
  }

  const downloadUrl = (fileId) =>
    `${apiUrl}/tickets/${ticketId}/files/${fileId}/download`;

  return (
    <div className="evidencias-grid">
      {evidencias.map((file) => {
        const isImage = file.tipo_mime && file.tipo_mime.startsWith("image/");
        const fileUrl = downloadUrl(file.id_adjunto);

        return (
          <div key={file.id_adjunto} className="evidencia-card">
            {isImage ? (
              <img
                src={fileUrl}
                alt={file.nombre_original}
                className="evidencia-preview"
              />
            ) : (
              <div className="evidencia-file">
                <i className="fa-solid fa-file"></i>
                <span>
                  {file.nombre_original.split(".").pop().toUpperCase()}
                </span>
              </div>
            )}

            <div className="evidencia-footer">
              <p className="evidencia-nombre">{file.nombre_original}</p>
              <a
                href={fileUrl}
                download={file.nombre_original}
                className="evidencia-descargar"
              >
                Descargar
              </a>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default Gallery;
