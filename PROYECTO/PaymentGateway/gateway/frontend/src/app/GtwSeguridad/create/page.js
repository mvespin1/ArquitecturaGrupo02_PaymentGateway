"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import "./page.css";

const CreatePage = () => {
  const [form, setForm] = useState({
    CLAVE: "",
    FECHA_CREACION: "",
    FECHA_ACTIVACION: "",
    ESTADO: "pendiente",
  });

  const [errors, setErrors] = useState({});
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const validateForm = () => {
    const newErrors = {};

    if (!form.CLAVE.trim()) {
      newErrors.CLAVE = "La clave es obligatoria.";
    }
    if (!form.ESTADO) {
      newErrors.ESTADO = "Seleccione un estado.";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      const dataToSend = {
        ...form, // Datos del formulario
        FECHA_CREACION: new Date().toISOString().split("T")[0], // Fecha actual como ejemplo
      };

      try {
        const response = await fetch("https://localhost:8082", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(dataToSend),
        });

        if (!response.ok) {
          throw new Error(`Error en la solicitud: ${response.statusText}`);
        }

        console.log("Datos enviados exitosamente:", dataToSend);
        router.push("/"); // Redirige a la página principal después del envío
      } catch (error) {
        console.error("Error al enviar los datos:", error.message);
      }
    }
  };

  const handleCancel = () => {
    router.push("/GtwSeguridad/components"); // Redirige a la página principal sin guardar
  };

  return (
    <main className="main-container">
      <h1 className="form-title">Crear Nueva Seguridad</h1>
      <form onSubmit={handleSubmit} className="form">
        {/* CLAVE */}
        <div className="form-group">
          <label htmlFor="CLAVE" className="form-label">
            Clave
          </label>
          <input
            id="CLAVE"
            type="text"
            name="CLAVE"
            value={form.CLAVE}
            onChange={handleChange}
            className={`form-input ${errors.CLAVE ? "error-border" : ""}`}
            placeholder="Ingrese clave"
            maxLength="128"
          />
          {errors.CLAVE && <p className="error-text">{errors.CLAVE}</p>}
        </div>

        {/* FECHA_CREACION */}
        <div className="form-group">
          <label htmlFor="FECHA_CREACION" className="form-label">
            Fecha Creación
          </label>
          <input
            id="FECHA_CREACION"
            type="date"
            name="FECHA_CREACION"
            value={form.FECHA_CREACION}
            onChange={handleChange}
            className="form-input disabled-input"
            disabled
          />
        </div>

        {/* FECHA_ACTIVACION */}
        <div className="form-group">
          <label htmlFor="FECHA_ACTIVACION" className="form-label">
            Fecha Activación
          </label>
          <input
            id="FECHA_ACTIVACION"
            type="date"
            name="FECHA_ACTIVACION"
            value={form.FECHA_ACTIVACION}
            onChange={handleChange}
            className="form-input disabled-input"
            disabled
          />
        </div>

        {/* ESTADO */}
        <div className="form-group">
          <label htmlFor="ESTADO" className="form-label">
            Estado
          </label>
          <select
            id="ESTADO"
            name="ESTADO"
            value={form.ESTADO}
            onChange={handleChange}
            className={`form-input ${errors.ESTADO ? "error-border" : ""}`}
          >
            <option value="pendiente">Pendiente</option>
            <option value="activo">Activo</option>
            <option value="inactivo">Inactivo</option>
          </select>
          {errors.ESTADO && <p className="error-text">{errors.ESTADO}</p>}
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
