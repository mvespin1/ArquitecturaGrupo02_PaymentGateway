"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { API_URLS, fetchWithConfig } from "../../config/api";
import "../../Css/general.css";

const CreateComercio = () => {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [comisiones, setComisiones] = useState([]);
  const [formData, setFormData] = useState({
    codigoInterno: "",
    ruc: "",
    razonSocial: "",
    nombreComercial: "",
    pagosAceptados: "SIM",
    numeroCuenta: "",
    comision: {
      codigo: ""
    }
  });

  useEffect(() => {
    const fetchComisiones = async () => {
      try {
        const response = await fetchWithConfig(API_URLS.COMISION.BASE);
        const data = await response.json();
        setComisiones(data);
      } catch (err) {
        console.error("Error al cargar comisiones:", err);
      }
    };
    fetchComisiones();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "codigoComision") {
      setFormData(prev => ({
        ...prev,
        comision: { codigo: value }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await fetchWithConfig(API_URLS.COMERCIO.BASE, {
        method: "POST",
        body: JSON.stringify(formData)
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
            maxLength="10"
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
            maxLength="13"
            pattern="[0-9]{13}"
            title="El RUC debe tener 13 dígitos numéricos"
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
            maxLength="100"
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
            maxLength="50"
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="pagosAceptados">Pagos Aceptados:</label>
          <select
            id="pagosAceptados"
            name="pagosAceptados"
            value={formData.pagosAceptados}
            onChange={handleChange}
            required
            className="form-select"
          >
            <option value="SIM">Simulación</option>
            <option value="REA">Real</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="numeroCuenta">Número de Cuenta:</label>
          <input
            type="number"
            id="numeroCuenta"
            name="numeroCuenta"
            value={formData.numeroCuenta}
            onChange={handleChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="codigoComision">Comisión:</label>
          <select
            id="codigoComision"
            name="codigoComision"
            value={formData.comision.codigo}
            onChange={handleChange}
            required
            className="form-select"
          >
            <option value="">Seleccione una comisión</option>
            {comisiones.map(comision => (
              <option key={comision.codigo} value={comision.codigo}>
                {`${comision.tipo} - ${comision.montoBase}${comision.tipo === 'POR' ? '%' : ' USD'}`}
              </option>
            ))}
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
