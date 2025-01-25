"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { API_URLS } from "../../config/api";
import "../../Css/general.css";

const CreateComercio = () => {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [formData, setFormData] = useState({
    codigoInterno: "",
    ruc: "",
    razonSocial: "",
    nombreComercial: "",
    pagosAceptados: "",
    estado: "ACT",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await fetch(API_URLS.COMERCIO.BASE, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Error al crear el comercio");
      }

      router.push("/GtwComercio/components");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    router.push("/GtwComercio/components");
  };

  if (loading) return <div className="loading">Creando comercio...</div>;

  return (
    <div className="form-container">
      <h1 className="form-title">Crear Nuevo Comercio</h1>
      {error && <div className="error">{error}</div>}
      
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="codigoInterno">Código Interno:</label>
          <input
            type="text"
            id="codigoInterno"
            name="codigoInterno"
            value={formData.codigoInterno}
            onChange={handleChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="ruc">RUC:</label>
          <input
            type="text"
            id="ruc"
            name="ruc"
            value={formData.ruc}
            onChange={handleChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="razonSocial">Razón Social:</label>
          <input
            type="text"
            id="razonSocial"
            name="razonSocial"
            value={formData.razonSocial}
            onChange={handleChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="nombreComercial">Nombre Comercial:</label>
          <input
            type="text"
            id="nombreComercial"
            name="nombreComercial"
            value={formData.nombreComercial}
            onChange={handleChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="pagosAceptados">Pagos Aceptados:</label>
          <input
            type="text"
            id="pagosAceptados"
            name="pagosAceptados"
            value={formData.pagosAceptados}
            onChange={handleChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="estado">Estado:</label>
          <select
            id="estado"
            name="estado"
            value={formData.estado}
            onChange={handleChange}
            className="form-select"
          >
            <option value="ACT">Activo</option>
            <option value="INA">Inactivo</option>
            <option value="SUS">Suspendido</option>
          </select>
        </div>

        <div className="form-buttons">
          <button type="submit" className="submit-button">
            Crear Comercio
          </button>
          <button type="button" onClick={handleCancel} className="cancel-button">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateComercio;
