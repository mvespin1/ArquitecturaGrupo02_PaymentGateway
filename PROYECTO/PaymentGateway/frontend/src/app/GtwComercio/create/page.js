"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

const CreatePage = () => {
  const [form, setForm] = useState({
    // COD_COMERCIO: "", // Comentado y no aparece en el formulario
    CODIGO_INTERNO: "",
    RUC: "",
    RAZON_SOCIAL: "",
    NOMBRE_COMERCIAL: "",
    FECHA_CREACION: "",
    COD_COMISION: "",
    PAGOS_ACEPTADOS: "",
    ESTADO: "pendiente",
    FECHA_ACTIVACION: "",
    FECHA_SUSPENSION: "",
  });

  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;

    // Validations
    if (name === "RUC" && value.length > 13) return;
    if ((name === "RAZON_SOCIAL" || name === "NOMBRE_COMERCIAL") && value.length > 100) return;

    setForm({ ...form, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!form.RAZON_SOCIAL.trim() || !form.NOMBRE_COMERCIAL.trim()) {
      alert("Razón Social y Nombre Comercial no pueden estar vacíos.");
      return;
    }

    console.log("Registro creado:", form);
    router.push("/");
  };

  const handleCancel = () => {
    router.push("/GtwComercio/components");
  };

  const tiposComision = ["Tipo 1", "Tipo 2", "Tipo 3"]; // Ejemplo de datos para el combo

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
        Crear Nuevo Comercio
      </h1>
      <form
        onSubmit={handleSubmit}
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: "1rem",
        }}
      >
        {Object.keys(form).map((field) => (
          <div
            key={field}
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: "0.5rem",
              width: "100%",
              maxWidth: "400px",
            }}
          >
            <label
              htmlFor={field}
              style={{
                fontWeight: "bold",
                color: "#1f2937",
                alignSelf: "flex-start",
              }}
            >
              {field
                .replace(/_/g, " ")
                .replace(/([A-Z])/g, " $1")
                .replace(/^./, (str) => str.toUpperCase())}
            </label>
            {field === "COD_COMISION" ? (
              <select
                id={field}
                name={field}
                value={form[field]}
                onChange={handleChange}
                style={{
                  padding: "10px",
                  fontSize: "1rem",
                  borderRadius: "4px",
                  border: "1px solid #d1d5db",
                  backgroundColor: "#ffffff",
                  width: "100%",
                }}
              >
                <option value="">Seleccione una opción</option>
                {tiposComision.map((tipo, index) => (
                  <option key={index} value={tipo}>
                    {tipo}
                  </option>
                ))}
              </select>
            ) : (
              <input
                id={field}
                type={field.includes("FECHA") ? "date" : "text"}
                name={field}
                value={form[field]}
                onChange={handleChange}
                disabled={
                  [
                    "CODIGO_INTERNO",
                    "FECHA_CREACION",
                    "PAGOS_ACEPTADOS",
                    "ESTADO",
                    "FECHA_ACTIVACION",
                    "FECHA_SUSPENSION",
                  ].includes(field)
                }
                style={{
                  padding: "10px",
                  fontSize: "1rem",
                  borderRadius: "4px",
                  border: "1px solid #d1d5db",
                  backgroundColor: [
                    "CODIGO_INTERNO",
                    "FECHA_CREACION",
                    "PAGOS_ACEPTADOS",
                    "ESTADO",
                    "FECHA_ACTIVACION",
                    "FECHA_SUSPENSION",
                  ].includes(field)
                    ? "#f3f4f6"
                    : "#ffffff",
                  width: "100%",
                }}
                placeholder={`Ingrese ${field
                  .replace(/_/g, " ")
                  .replace(/([A-Z])/g, " $1")
                  .toLowerCase()}`}
              />
            )}
          </div>
        ))}
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            gap: "1rem",
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
