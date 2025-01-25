"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { API_URLS } from "../../config/api";
import "../../Css/general.css";

const ViewComercio = ({ params }) => {
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [comercio, setComercio] = useState(null);

  useEffect(() => {
    const fetchComercio = async () => {
      try {
        const response = await fetch(API_URLS.COMERCIO.GET_BY_CODE(params.codigo));
        if (!response.ok) throw new Error("Error al cargar el comercio");
        const data = await response.json();
        setComercio(data);
        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (params.codigo) {
      fetchComercio();
    }
  }, [params.codigo]);

  const handleBack = () => {
    router.push("/GtwComercio/components");
  };

  if (loading) return <div className="loading">Cargando comercio...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!comercio) return <div className="error">No se encontró el comercio</div>;

  return (
    <div className="form-container">
      <h1 className="form-title">Detalles del Comercio</h1>
      
      <div className="detail-card">
        <div className="detail-section">
          <h2 className="section-title">Información General</h2>
          
          <div className="detail-group">
            <label>Código:</label>
            <span>{comercio.codigo}</span>
          </div>
          
          <div className="detail-group">
            <label>Código Interno:</label>
            <span>{comercio.codigoInterno}</span>
          </div>
          
          <div className="detail-group">
            <label>RUC:</label>
            <span>{comercio.ruc}</span>
          </div>
          
          <div className="detail-group">
            <label>Razón Social:</label>
            <span>{comercio.razonSocial}</span>
          </div>
          
          <div className="detail-group">
            <label>Nombre Comercial:</label>
            <span>{comercio.nombreComercial}</span>
          </div>
        </div>

        <div className="detail-section">
          <h2 className="section-title">Configuración</h2>
          
          <div className="detail-group">
            <label>Código Comisión:</label>
            <span>{comercio.codigoComision}</span>
          </div>
          
          <div className="detail-group">
            <label>Pagos Aceptados:</label>
            <span>{comercio.pagosAceptados}</span>
          </div>
          
          <div className="detail-group">
            <label>Estado:</label>
            <span className={`status-badge ${comercio.estado.toLowerCase()}`}>
              {comercio.estado === 'ACT' ? 'Activo' : 
               comercio.estado === 'INA' ? 'Inactivo' : 'Suspendido'}
            </span>
          </div>
        </div>

        <div className="detail-section">
          <h2 className="section-title">Fechas</h2>
          
          <div className="detail-group">
            <label>Fecha Creación:</label>
            <span>{new Date(comercio.fechaCreacion).toLocaleDateString()}</span>
          </div>
          
          {comercio.fechaActivacion && (
            <div className="detail-group">
              <label>Fecha Activación:</label>
              <span>{new Date(comercio.fechaActivacion).toLocaleDateString()}</span>
            </div>
          )}
          
          {comercio.fechaSuspension && (
            <div className="detail-group">
              <label>Fecha Suspensión:</label>
              <span>{new Date(comercio.fechaSuspension).toLocaleDateString()}</span>
            </div>
          )}
        </div>
      </div>

      <div className="form-buttons">
        <button onClick={handleBack} className="cancel-button">
          Volver
        </button>
      </div>
    </div>
  );
};

export default ViewComercio; 