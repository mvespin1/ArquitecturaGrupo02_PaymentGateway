"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { API_URLS } from "../../config/api";
import "../../Css/general.css";

const UpdateComercio = ({ params }) => {
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [formData, setFormData] = useState({
    codigoInterno: "",
    ruc: "",
    razonSocial: "",
    nombreComercial: "",
    pagosAceptados: "",
    estado: "",
    codigoComision: "",
  });

  useEffect(() => {
    const fetchComercio = async () => {
      try {
        const response = await fetch(API_URLS.COMERCIO.GET_BY_CODE(params.codigo));
        if (!response.ok) throw new Error("Error al cargar el comercio");
        const data = await response.json();
        setFormData(data);
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
      // Actualizar estado
      const estadoResponse = await fetch(API_URLS.COMERCIO.UPDATE_STATUS(params.codigo), {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ nuevoEstado: formData.estado }),
      });

      if (!estadoResponse.ok) throw new Error("Error al actualizar el estado");

      // Actualizar pagos aceptados
      const pagosResponse = await fetch(API_URLS.COMERCIO.UPDATE_PAGOS(params.codigo), {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ pagosAceptados: formData.pagosAceptados }),
      });

      if (!pagosResponse.ok) throw new Error("Error al actualizar los pagos aceptados");

      // Actualizar comisión si ha cambiado
      if (formData.codigoComision) {
        const comisionResponse = await fetch(
          API_URLS.COMERCIO.ASSIGN_COMISION(params.codigo, formData.codigoComision),
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (!comisionResponse.ok) throw new Error("Error al asignar la comisión");
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

  if (loading) return <div className="loading">Cargando comercio...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="form-container">
      <h1 className="form-title">Actualizar Comercio</h1>
      
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="codigoInterno">Código Interno:</label>
          <input
            type="text"
            id="codigoInterno"
            name="codigoInterno"
            value={formData.codigoInterno}
            disabled
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
            disabled
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
            disabled
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
            disabled
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
          <label htmlFor="codigoComision">Código Comisión:</label>
          <input
            type="text"
            id="codigoComision"
            name="codigoComision"
            value={formData.codigoComision}
            onChange={handleChange}
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
            Actualizar Comercio
          </button>
          <button type="button" onClick={handleCancel} className="cancel-button">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default UpdateComercio;
