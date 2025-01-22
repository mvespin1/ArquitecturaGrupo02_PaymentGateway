"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import "../../Css/general.css";

const CreatePage = () => {
  const [form, setForm] = useState({
    Tipo: "",
    MontoBase: "",
    TransaccionesBase: "",
    ManejaSegmentos: "",
  });

  const [errors, setErrors] = useState({});
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm({ ...form, [name]: value });

    if (name === "ManejaSegmentos" && value === "si") {
      router.push("/GtwComisionSegmento/components/");
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!form.Tipo) newErrors.Tipo = "Seleccione un tipo.";
    if (!form.MontoBase || parseFloat(form.MontoBase) <= 0) {
      newErrors.MontoBase = "Ingrese un monto base válido.";
    }
    if (
      !form.TransaccionesBase ||
      parseInt(form.TransaccionesBase, 10) <= 0
    ) {
      newErrors.TransaccionesBase = "Ingrese un número válido de transacciones base.";
    }
    if (!form.ManejaSegmentos) newErrors.ManejaSegmentos = "Seleccione una opción.";

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      // Datos a enviar
      const dataToSend = {
        Tipo: form.Tipo,
        MontoBase: parseFloat(form.MontoBase),
        TransaccionesBase: parseInt(form.TransaccionesBase, 10),
        ManejaSegmentos: form.ManejaSegmentos === "si",
      };

      try {
        // Envío de datos al endpoint
        const response = await fetch("https://localhost:8082", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(dataToSend),
        });

        if (!response.ok) {
          throw new Error("Error al enviar los datos");
        }

        console.log("Datos enviados exitosamente:", dataToSend);
        router.push("/GtwComision/components"); // Redirige a la página deseada
      } catch (error) {
        console.error("Error en el envío:", error);
      }
    }
  };

  const handleCancel = () => {
    router.push("/");
  };

  return (
    <main className="main-container">
      <h1 className="form-title">Crear Nueva Comisión</h1>
      <form onSubmit={handleSubmit} className="form-container">
        {/* Tipo */}
        <div className="form-group">
          <label className="form-label">Tipo</label>
          <div className="radio-group">
            <label>
              <input
                type="radio"
                name="Tipo"
                value="porcentual"
                checked={form.Tipo === "porcentual"}
                onChange={handleChange}
              />
              Porcentual
            </label>
            <label>
              <input
                type="radio"
                name="Tipo"
                value="fijo"
                checked={form.Tipo === "fijo"}
                onChange={handleChange}
              />
              Fijo
            </label>
          </div>
          {errors.Tipo && <p className="error-text">{errors.Tipo}</p>}
        </div>

        {/* Monto Base */}
        <div className="form-group">
          <label htmlFor="MontoBase" className="form-label">
            Monto Base
          </label>
          <input
            id="MontoBase"
            type="number"
            name="MontoBase"
            value={form.MontoBase}
            onChange={handleChange}
            className="form-input"
            placeholder="Ingrese monto base"
            step="0.0001"
            max="9999999999999999.9999"
          />
          {errors.MontoBase && <p className="error-text">{errors.MontoBase}</p>}
        </div>

        {/* Transacciones Base */}
        <div className="form-group">
          <label htmlFor="TransaccionesBase" className="form-label">
            Transacciones Base
          </label>
          <input
            id="TransaccionesBase"
            type="number"
            name="TransaccionesBase"
            value={form.TransaccionesBase}
            onChange={handleChange}
            className="form-input"
            placeholder="Ingrese transacciones base"
            max="999999999"
          />
          {errors.TransaccionesBase && (
            <p className="error-text">{errors.TransaccionesBase}</p>
          )}
        </div>

        {/* Maneja Segmentos */}
        <div className="form-group">
          <label className="form-label">Maneja Segmentos</label>
          <div className="radio-group">
            <label>
              <input
                type="radio"
                name="ManejaSegmentos"
                value="si"
                checked={form.ManejaSegmentos === "si"}
                onChange={handleChange}
              />
              Sí
            </label>
            <label>
              <input
                type="radio"
                name="ManejaSegmentos"
                value="no"
                checked={form.ManejaSegmentos === "no"}
                onChange={handleChange}
              />
              No
            </label>
          </div>
          {errors.ManejaSegmentos && (
            <p className="error-text">{errors.ManejaSegmentos}</p>
          )}
        </div>

        {/* Botones */}
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
