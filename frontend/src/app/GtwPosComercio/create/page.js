"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { API_URLS, fetchWithConfig } from "../../config/api";
import "../../Css/general.css";

const CreatePos = () => {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [comercios, setComercios] = useState([]);
  const [formData, setFormData] = useState({
    pk: {
      codigo: "",
      modelo: ""
    },
    direccionMac: "",
    estado: "INA",
    comercio: {
      codigo: ""
    }
  });

  useEffect(() => {
    const fetchComercios = async () => {
      try {
        const response = await fetchWithConfig(API_URLS.COMERCIO.BASE);
        const data = await response.json();
        setComercios(data);
      } catch (err) {
        console.error("Error al cargar comercios:", err);
      }
    };
    fetchComercios();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "codigoComercio") {
      setFormData(prev => ({
        ...prev,
        comercio: { codigo: parseInt(value) }
      }));
    } else if (name === "codigo" || name === "modelo") {
      setFormData(prev => ({
        ...prev,
        pk: {
          ...prev.pk,
          [name]: value
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const validateMacAddress = (mac) => {
    const macRegex = /^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$/;
    return macRegex.test(mac);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateMacAddress(formData.direccionMac)) {
      setError("La dirección MAC no tiene un formato válido (XX:XX:XX:XX:XX:XX)");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await fetchWithConfig(API_URLS.POS_COMERCIO.CREATE, {
        method: "POST",
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        throw new Error("Error al crear el POS");
      }

      router.push("/GtwPosComercio/components");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    router.push("/GtwPosComercio/components");
  };

  if (loading) return <div className="loading">Creando POS...</div>;

  return (
    <div className="form-container">
      <h1 className="form-title">Crear Nuevo POS</h1>
      {error && <div className="error">{error}</div>}
      
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="codigo">Código POS:</label>
          <input
            type="text"
            id="codigo"
            name="codigo"
            value={formData.pk.codigo}
            onChange={handleChange}
            required
            pattern="POS[0-9]{7}"
            title="El código debe tener el formato POS seguido de 7 números"
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="modelo">Modelo:</label>
          <input
            type="text"
            id="modelo"
            name="modelo"
            value={formData.pk.modelo}
            onChange={handleChange}
            required
            maxLength="20"
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="direccionMac">Dirección MAC:</label>
          <input
            type="text"
            id="direccionMac"
            name="direccionMac"
            value={formData.direccionMac}
            onChange={handleChange}
            required
            placeholder="XX:XX:XX:XX:XX:XX"
            pattern="^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
            title="Formato válido: XX:XX:XX:XX:XX:XX donde X es un dígito hexadecimal"
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="codigoComercio">Comercio:</label>
          <select
            id="codigoComercio"
            name="codigoComercio"
            value={formData.comercio.codigo}
            onChange={handleChange}
            required
            className="form-select"
          >
            <option value="">Seleccione un comercio</option>
            {comercios.map(comercio => (
              <option key={comercio.codigo} value={comercio.codigo}>
                {`${comercio.nombreComercial} (${comercio.codigoInterno})`}
              </option>
            ))}
          </select>
        </div>

        <div className="form-buttons">
          <button type="submit" className="submit-button">
            Crear POS
          </button>
          <button type="button" onClick={handleCancel} className="cancel-button">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreatePos; 