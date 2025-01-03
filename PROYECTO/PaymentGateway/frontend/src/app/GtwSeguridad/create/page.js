"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importamos useRouter para navegación

const CreatePage = () => {
  const [form, setForm] = useState({
    CLAVE: "",
    FECHA_CREACION: "",
    FECHA_ACTIVACION: "",
    ESTADO: "pendiente",
  });

  const router = useRouter(); // Inicializamos el router para redirigir

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Registro creado:", form);
    router.push("/"); // Redirige a la página principal después de guardar
  };

  const handleCancel = () => {
    router.push("/GtwSeguridad/components"); // Redirige a la página principal sin guardar
  };

  return (
    <main
      style={{
        maxWidth: "600px",
        margin: "2rem auto",
        padding: "2rem",
        backgroundColor: "#f9fafb",
        borderRadius: "8px",
        boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
      }}
    >
      <h1
        style={{
          textAlign: "center",
          color: "#1e40af",
          marginBottom: "1.5rem",
        }}
      >
        Crear Nueva Seguridad
      </h1>
      <form
        onSubmit={handleSubmit}
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "1rem",
        }}
      >
        {/* CLAVE */}
        <div>
          <label
            htmlFor="CLAVE"
            style={{
              fontWeight: "bold",
              color: "#1f2937",
            }}
          >
            Clave
          </label>
          <input
            id="CLAVE"
            type="text"
            name="CLAVE"
            value={form.CLAVE}
            onChange={handleChange}
            style={{
              padding: "10px",
              fontSize: "1rem",
              borderRadius: "4px",
              border: "1px solid #d1d5db",
              backgroundColor: "#ffffff",
            }}
            placeholder="Ingrese clave"
            maxLength="128" // Longitud máxima de 128 caracteres
          />
        </div>

        {/* FECHA_CREACION */}
        <div>
          <label
            htmlFor="FECHA_CREACION"
            style={{
              fontWeight: "bold",
              color: "#1f2937",
            }}
          >
            Fecha Creación
          </label>
          <input
            id="FECHA_CREACION"
            type="date"
            name="FECHA_CREACION"
            value={form.FECHA_CREACION}
            onChange={handleChange}
            style={{
              padding: "10px",
              fontSize: "1rem",
              borderRadius: "4px",
              border: "1px solid #d1d5db",
              backgroundColor: "#e5e7eb", // Fondo gris para campos deshabilitados
            }}
            disabled // Campo deshabilitado
          />
        </div>

        {/* FECHA_ACTIVACION */}
        <div>
          <label
            htmlFor="FECHA_ACTIVACION"
            style={{
              fontWeight: "bold",
              color: "#1f2937",
            }}
          >
            Fecha Activación
          </label>
          <input
            id="FECHA_ACTIVACION"
            type="date"
            name="FECHA_ACTIVACION"
            value={form.FECHA_ACTIVACION}
            onChange={handleChange}
            style={{
              padding: "10px",
              fontSize: "1rem",
              borderRadius: "4px",
              border: "1px solid #d1d5db",
              backgroundColor: "#e5e7eb", // Fondo gris para campos deshabilitados
            }}
            disabled // Campo deshabilitado
          />
        </div>

        {/* ESTADO */}
        <div>
          <label
            htmlFor="ESTADO"
            style={{
              fontWeight: "bold",
              color: "#1f2937",
            }}
          >
            Estado
          </label>
          <select
            id="ESTADO"
            name="ESTADO"
            value={form.ESTADO}
            onChange={handleChange}
            style={{
              padding: "10px",
              fontSize: "1rem",
              borderRadius: "4px",
              border: "1px solid #d1d5db",
              backgroundColor: "#ffffff",
            }}
          >
            <option value="pendiente">Pendiente</option>
            <option value="activo">Activo</option>
            <option value="inactivo">Inactivo</option>
          </select>
        </div>

        {/* Botones */}
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            marginTop: "1rem",
          }}
        >
          <button
            type="submit"
            style={{
              backgroundColor: "#10b981",
              color: "#ffffff",
              padding: "10px 20px",
              fontSize: "1rem",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            Guardar
          </button>
          <button
            type="button"
            onClick={handleCancel}
            style={{
              backgroundColor: "#ef4444",
              color: "#ffffff",
              padding: "10px 20px",
              fontSize: "1rem",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            Cancelar
          </button>
        </div>
      </form>
    </main>
  );
};

export default CreatePage;
