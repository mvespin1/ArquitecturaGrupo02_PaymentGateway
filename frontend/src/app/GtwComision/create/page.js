"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { API_URLS, fetchWithConfig } from "../../config/api";
import "../../Css/general.css";

const CreatePage = () => {
  const [form, setForm] = useState({
    tipo: "",
    montoBase: "",
    transaccionesBase: "",
    manejaSegmentos: false
  });

  const [errors, setErrors] = useState({});
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    
    if (type === "radio" && name === "tipo") {
      setForm({ ...form, [name]: value === "porcentual" ? "POR" : "FIJ" });
    } else if (type === "radio" && name === "manejaSegmentos") {
      setForm({ ...form, [name]: value === "si" });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!form.tipo) newErrors.tipo = "Seleccione un tipo.";
    if (!form.montoBase || parseFloat(form.montoBase) <= 0) {
      newErrors.montoBase = "Ingrese un monto base válido.";
    }
    if (!form.transaccionesBase || parseInt(form.transaccionesBase, 10) <= 0) {
      newErrors.transaccionesBase = "Ingrese un número válido de transacciones base.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      const dataToSend = {
        tipo: form.tipo,
        montoBase: parseFloat(form.montoBase),
        transaccionesBase: parseInt(form.transaccionesBase, 10),
        manejaSegmentos: form.manejaSegmentos
      };

      try {
        const response = await fetchWithConfig(API_URLS.COMISION.CREATE, {
          method: "POST",
          body: JSON.stringify(dataToSend),
        });

        if (response.ok) {
          router.push("/GtwComision/components");
        }
      } catch (error) {
        console.error("Error al crear la comisión:", error);
        setErrors({ submit: "Error al crear la comisión. Por favor, intente nuevamente." });
      }
    }
  };

  const handleCancel = () => {
    router.push("/GtwComision/components");
  };

  return (
    <main className="main-container">
      <h1 className="form-title">Crear Nueva Comisión</h1>
      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-group">
          <label className="form-label">Tipo</label>
          <div className="radio-group">
            <label>
              <input
                type="radio"
                name="tipo"
                value="porcentual"
                checked={form.tipo === "POR"}
                onChange={handleChange}
              />
              Porcentual
            </label>
            <label>
              <input
                type="radio"
                name="tipo"
                value="fijo"
                checked={form.tipo === "FIJ"}
                onChange={handleChange}
              />
              Fijo
            </label>
          </div>
          {errors.tipo && <p className="error-text">{errors.tipo}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="montoBase" className="form-label">
            Monto Base
          </label>
          <input
            id="montoBase"
            type="number"
            name="montoBase"
            value={form.montoBase}
            onChange={handleChange}
            className="form-input"
            placeholder="Ingrese monto base"
            step="0.0001"
          />
          {errors.montoBase && <p className="error-text">{errors.montoBase}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="transaccionesBase" className="form-label">
            Transacciones Base
          </label>
          <input
            id="transaccionesBase"
            type="number"
            name="transaccionesBase"
            value={form.transaccionesBase}
            onChange={handleChange}
            className="form-input"
            placeholder="Ingrese transacciones base"
          />
          {errors.transaccionesBase && (
            <p className="error-text">{errors.transaccionesBase}</p>
          )}
        </div>

        <div className="form-group">
          <label className="form-label">Maneja Segmentos</label>
          <div className="radio-group">
            <label>
              <input
                type="radio"
                name="manejaSegmentos"
                value="si"
                checked={form.manejaSegmentos === true}
                onChange={handleChange}
              />
              Sí
            </label>
            <label>
              <input
                type="radio"
                name="manejaSegmentos"
                value="no"
                checked={form.manejaSegmentos === false}
                onChange={handleChange}
              />
              No
            </label>
          </div>
          {errors.manejaSegmentos && (
            <p className="error-text">{errors.manejaSegmentos}</p>
          )}
        </div>

        {errors.submit && <p className="error-text">{errors.submit}</p>}

        <div className="button-group">
          <button type="submit" className="button save">
            Guardar
          </button>
          <button
            type="button"
            onClick={handleCancel}
            className="button cancel"
          >
            Cancelar
          </button>
        </div>
      </form>
    </main>
  );
};

export default CreatePage;
