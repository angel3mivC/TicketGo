import { useState, useEffect } from "react";
import "./styles/Gallery.css";

const Gallery = ({ evidencias, ticketId, apiUrl, token }) => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [previewURLs, setPreviewURLs] = useState({});

  const [imageBlobURLs, setImageBlobURLs] = useState({});

  useEffect(() => {
    if (!evidencias) return;

    const newBlobURLs = {};
    evidencias.forEach((file) => {
      if (
        file.id_adjunto &&
        file.tipo_mime?.startsWith("image/") &&
        file.archivo?.data &&
        !imageBlobURLs[file.id_adjunto]
      ) {
        const buffer = file.archivo.data;
        const blob = new Blob([new Uint8Array(buffer)], {
          type: file.tipo_mime,
        });
        const url = URL.createObjectURL(blob);
        newBlobURLs[file.id_adjunto] = url;
      }
    });

    if (Object.keys(newBlobURLs).length > 0) {
      setImageBlobURLs((prev) => ({ ...prev, ...newBlobURLs }));
    }
  }, [evidencias]);

  useEffect(() => {
    return () => {
      const allUrls = [
        ...Object.values(previewURLs),
        ...Object.values(imageBlobURLs),
      ];
      allUrls.forEach((url) => {
        if (url && url.startsWith("blob:")) {
          URL.revokeObjectURL(url);
        }
      });
    };
  }, [previewURLs, imageBlobURLs]);

  const getFileIcon = (mimeType) => {
    if (!mimeType) return "📄";

    if (mimeType.startsWith("image/")) return "🖼️";
    if (mimeType.startsWith("video/")) return "🎬";
    if (mimeType.startsWith("audio/")) return "🎵";
    if (mimeType.includes("pdf")) return "📕";
    if (mimeType.includes("word") || mimeType.includes("document")) return "📘";
    if (mimeType.includes("excel") || mimeType.includes("sheet")) return "📊";
    if (mimeType.includes("powerpoint") || mimeType.includes("presentation"))
      return "📙";
    if (mimeType.includes("zip") || mimeType.includes("rar")) return "🗜️";
    if (mimeType.includes("text")) return "📝";

    return "📄";
  };

  const getFileURL = (file) => {
    const fileId = file.id_adjunto || file.id;

    if (imageBlobURLs[fileId]) {
      return imageBlobURLs[fileId];
    }

    if (file instanceof File) {
      if (!previewURLs[file.id]) {
        // Usamos file.id o file.name para que sea único
        const url = URL.createObjectURL(file);
        setPreviewURLs((prev) => ({ ...prev, [file.id || file.name]: url }));
        return url;
      }
      return previewURLs[file.id || file.name];
    }

    if (file.url) {
      return file.url;
    }

    return `${apiUrl}/tickets/${ticketId}/files/${fileId}/download`;
  };

  const getFilePreview = (file) => {
    const mimeType = file.tipo_mime || file.type;
    const fileURL = getFileURL(file);
    const fileName = getFileName(file);
    if (mimeType?.startsWith("image/")) {
      return (
        <div className="file-preview-image">
          <img
            src={fileURL}
            alt={fileName}
            style={{
              maxWidth: "100%",
              maxHeight: "100%",
              objectFit: "contain",
            }}
            onError={(e) => {
              console.error("Error cargando imagen:", fileName);
              e.target.style.display = "none";
            }}
          />
        </div>
      );
    }
    if (mimeType?.includes("pdf")) {
      return (
        <div className="file-preview-pdf">
          <iframe
            src={`${fileURL}#toolbar=0&navpanes=0&scrollbar=0`}
            title={fileName}
            style={{ width: "100%", height: "100%", border: "none" }}
            onError={(e) => {
              console.error("Error cargando PDF:", fileName);
              e.target.style.display = "none";
            }}
          />
        </div>
      );
    }
    if (mimeType?.startsWith("video/")) {
      return (
        <div className="file-preview-video">
          <video
            src={fileURL}
            controls
            style={{ width: "100%", height: "100%", objectFit: "contain" }}
            onError={(e) => {
              console.error("Error cargando video:", fileName);
              e.target.style.display = "none";
            }}
          />
        </div>
      );
    }
    return (
      <div className="file-preview-icon">
        <span className="file-icon-large">{getFileIcon(mimeType)}</span>
        <p style={{ marginTop: "1rem", color: "#666" }}>
          {getFileExtension(fileName)}
        </p>
      </div>
    );
  };

  const downloadFile = async (fileId, fileName) => {
    try {
      const response = await fetch(
        `${apiUrl}/tickets/${ticketId}/files/${fileId}/download`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (!response.ok) {
        console.error("Error al descargar archivo");
        return;
      }
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = fileName;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error al descargar:", error);
    }
  };

  const formatFileSize = (bytes) => {
    if (!bytes) return "0 KB";
    const kb = bytes / 1024;
    if (kb < 1024) return `${kb.toFixed(1)} KB`;
    const mb = kb / 1024;
    return `${mb.toFixed(1)} MB`;
  };

  const getFileName = (file) => {
    return file.nombre_original || file.name || "archivo";
  };

  const getFileExtension = (fileName) => {
    const parts = fileName.split(".");
    return parts.length > 1 ? parts[parts.length - 1].toUpperCase() : "FILE";
  };

  if (!evidencias || evidencias.length === 0) {
    return (
      <div className="gallery-empty">
        <svg
          width="48"
          height="48"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="1.5"
        >
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
          <circle cx="8.5" cy="8.5" r="1.5"></circle>
          <polyline points="21 15 16 10 5 21"></polyline>
        </svg>
        <p>No hay archivos adjuntos</p>
      </div>
    );
  }

  return (
    <>
      <div className="gallery-grid">
        {evidencias.map((file) => {
          const fileName = getFileName(file);
          const fileId = file.id_adjunto || file.id;
          const mimeType = file.tipo_mime || file.type;
          const isImage = mimeType?.startsWith("image/");
          const isPDF = mimeType?.includes("pdf");
          const isVideo = mimeType?.startsWith("video/");
          const canPreview = isImage || isPDF || isVideo;
          const fileURL = getFileURL(file);

          return (
            <div
              key={fileId}
              className="file-card"
              onClick={() => setSelectedFile(file)}
            >
              <div className="file-preview">
                {canPreview ? (
                  <>
                    {isImage && (
                      <img
                        src={fileURL}
                        alt={fileName}
                        onError={(e) => {
                          e.target.style.display = "none";
                          e.target.nextSibling.style.display = "flex";
                        }}
                      />
                    )}
                    {isPDF && (
                      <iframe
                        src={`${fileURL}#toolbar=0&navpanes=0&scrollbar=0`}
                        title={fileName}
                        style={{
                          width: "100%",
                          height: "100%",
                          border: "none",
                          pointerEvents: "none",
                        }}
                        onError={(e) => {
                          e.target.style.display = "none";
                          e.target.nextSibling.style.display = "flex";
                        }}
                      />
                    )}
                    {isVideo && (
                      <video
                        src={fileURL}
                        style={{
                          width: "100%",
                          height: "100%",
                          objectFit: "cover",
                        }}
                        onError={(e) => {
                          e.target.style.display = "none";
                          e.target.nextSibling.style.display = "flex";
                        }}
                      />
                    )}
                    <div
                      className="file-icon-container"
                      style={{ display: "none" }}
                    >
                      <span className="file-extension">
                        {getFileExtension(fileName)}
                      </span>
                      <span className="file-icon">{getFileIcon(mimeType)}</span>
                    </div>
                  </>
                ) : (
                  <div className="file-icon-container">
                    <span className="file-extension">
                      {getFileExtension(fileName)}
                    </span>
                    <span className="file-icon">{getFileIcon(mimeType)}</span>
                  </div>
                )}
              </div>
              <div className="file-info">
                <p className="file-name" title={fileName}>
                  {fileName}
                </p>
                <p className="file-size">
                  {formatFileSize(file.tamano || file.size)}
                </p>
              </div>
              <button
                className="file-download-btn"
                onClick={(e) => {
                  e.stopPropagation();
                  if (file instanceof File) {
                    const url = getFileURL(file);
                    const link = document.createElement("a");
                    link.href = url;
                    link.download = fileName;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                  } else {
                    downloadFile(fileId, fileName);
                  }
                }}
                title="Descargar archivo"
              >
                <svg
                  width="18"
                  height="18"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                  <polyline points="7 10 12 15 17 10"></polyline>
                  <line x1="12" y1="15" x2="12" y2="3"></line>
                </svg>
              </button>
            </div>
          );
        })}
      </div>
      {selectedFile && (
        <div
          className="file-modal-overlay"
          onClick={() => setSelectedFile(null)}
        >
          <div className="file-modal" onClick={(e) => e.stopPropagation()}>
            <div className="file-modal-header">
              <h3>{getFileName(selectedFile)}</h3>
              <button
                className="modal-close-btn"
                onClick={() => setSelectedFile(null)}
              >
                ✕
              </button>
            </div>
            <div className="file-modal-content">
              {getFilePreview(selectedFile)}
            </div>
            <div className="file-modal-footer">
              <div className="file-modal-info">
                <span className="file-modal-size">
                  {formatFileSize(selectedFile.tamano || selectedFile.size)}
                </span>
                <span className="file-modal-type">
                  {getFileExtension(getFileName(selectedFile))}
                </span>
              </div>
              <button
                className="btn-download-large"
                onClick={() => {
                  const fileId = selectedFile.id_adjunto || selectedFile.id;
                  const fileName = getFileName(selectedFile);
                  if (selectedFile instanceof File) {
                    const url = getFileURL(selectedFile);
                    const link = document.createElement("a");
                    link.href = url;
                    link.download = fileName;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                  } else {
                    downloadFile(fileId, fileName);
                  }
                }}
              >
                <svg
                  width="20"
                  height="20"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                  <polyline points="7 10 12 15 17 10"></polyline>
                  <line x1="12" y1="15" x2="12" y2="3"></line>
                </svg>
                Descargar
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default Gallery;
