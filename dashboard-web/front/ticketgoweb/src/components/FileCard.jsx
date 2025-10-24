import React, { useState } from "react";
import {
  FileText,
  Image,
  File,
  Download,
  Calendar,
  AlertCircle,
  Search,
} from "lucide-react";

const FileCard = () => {
  const [token, setToken] = useState("");
  const [ticketId, setTicketId] = useState("");
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchFiles = async (e) => {
    e.preventDefault();

    if (!token || !ticketId) {
      setError("Por favor completa todos los campos");
      return;
    }

    try {
      setLoading(true);
      setError(null);

      const response = await fetch(
        `https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/tickets/${ticketId}/files`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Error al cargar los archivos");
      }

      const data = await response.json();
      setFiles(data);
    } catch (err) {
      setError(err.message);
      setFiles([]);
    } finally {
      setLoading(false);
    }
  };

  const getFileIcon = (tipoMime) => {
    if (tipoMime.startsWith("image/")) {
      return <Image className="w-8 h-8 text-blue-500" />;
    } else if (tipoMime.includes("pdf")) {
      return <FileText className="w-8 h-8 text-red-500" />;
    } else {
      return <File className="w-8 h-8 text-gray-500" />;
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("es-MX", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const handleDownload = (idAdjunto, nombreOriginal) => {
    const url = `https://e3ljh2zo7k.execute-api.us-east-1.amazonaws.com/tickets/${ticketId}/files/${idAdjunto}`;

    fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => response.blob())
      .then((blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = nombreOriginal;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      })
      .catch((err) => console.error("Error al descargar:", err));
  };

  return (
    <div className="max-w-6xl mx-auto p-6 space-y-6">
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">
          Visualizador de Archivos
        </h2>

        <form onSubmit={fetchFiles} className="space-y-4">
          <div>
            <label
              htmlFor="token"
              className="block text-sm font-medium text-gray-700 mb-2"
            >
              Token de Autenticación
            </label>
            <input
              type="text"
              id="token"
              value={token}
              onChange={(e) => setToken(e.target.value)}
              placeholder="Ingresa tu token JWT"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            />
          </div>

          <div>
            <label
              htmlFor="ticketId"
              className="block text-sm font-medium text-gray-700 mb-2"
            >
              ID del Ticket
            </label>
            <input
              type="text"
              id="ticketId"
              value={ticketId}
              onChange={(e) => setTicketId(e.target.value)}
              placeholder="Ejemplo: 123"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-500 hover:bg-blue-600 disabled:bg-gray-400 text-white font-medium py-3 px-6 rounded-lg transition-colors duration-200 flex items-center justify-center gap-2"
          >
            {loading ? (
              <>
                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                Cargando...
              </>
            ) : (
              <>
                <Search className="w-5 h-5" />
                Buscar Archivos
              </>
            )}
          </button>
        </form>

        {error && (
          <div className="mt-4 bg-red-50 border border-red-200 rounded-lg p-4 flex items-center gap-3">
            <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0" />
            <p className="text-red-700">{error}</p>
          </div>
        )}
      </div>

      {files.length > 0 && (
        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">
            Archivos Adjuntos ({files.length})
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {files.map((file) => (
              <div
                key={file.id_adjunto}
                className="bg-gray-50 border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow duration-200"
              >
                <div className="flex items-start gap-3">
                  <div className="flex-shrink-0">
                    {getFileIcon(file.tipo_mime)}
                  </div>

                  <div className="flex-1 min-w-0">
                    <h4
                      className="text-sm font-medium text-gray-900 truncate"
                      title={file.nombre_original}
                    >
                      {file.nombre_original}
                    </h4>

                    <p className="text-xs text-gray-500 mt-1">
                      {file.tipo_archivo.toUpperCase()}
                    </p>

                    <p className="text-xs text-gray-400 mt-1">
                      ID: {file.id_adjunto}
                    </p>

                    <div className="flex items-center gap-1 mt-2 text-xs text-gray-400">
                      <Calendar className="w-3 h-3" />
                      <span>{formatDate(file.fecha)}</span>
                    </div>
                  </div>
                </div>

                <button
                  onClick={() =>
                    handleDownload(file.id_adjunto, file.nombre_original)
                  }
                  className="mt-4 w-full flex items-center justify-center gap-2 bg-blue-500 hover:bg-blue-600 text-white text-sm font-medium py-2 px-4 rounded-lg transition-colors duration-200"
                >
                  <Download className="w-4 h-4" />
                  Descargar
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {!loading && !error && files.length === 0 && ticketId && (
        <div className="bg-white rounded-lg shadow-md p-8 text-center">
          <File className="w-12 h-12 text-gray-400 mx-auto mb-3" />
          <p className="text-gray-500">
            No se encontraron archivos adjuntos para este ticket
          </p>
        </div>
      )}
    </div>
  );
};

export default FileCard;
