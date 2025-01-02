"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importamos useRouter para navegación

const UpdatePage = () => {
  const [form, setForm] = useState({
    CodigoComision: "001", // Campo inicializado
    TransaccionDesde: "1000", // Valor inicial para pruebas
    TransaccionHasta: "",
    Monto: "",
  });

  const router = useRouter(); // Inicializamos el router para redirigir

  const handleChange = (e) => {
    const { name, value } = e.target;

    // Validaciones para TransaccionHasta y Monto
    if (name === "TransaccionHasta") {
      // Validar que sea mayor al valor de TransaccionDesde
      if (parseInt(value, 10) < parseInt(form.TransaccionDesde, 10)) {
        alert("Transacción Hasta debe ser mayor que Transacción Desde.");
        return;
      }
      if (value.length > 9) {
        alert("Transacción Hasta acepta un máximo de 9 dígitos.");
        return;
      }
    }

    if (name === "Monto") {
      // Validar formato de Monto (máx 20 dígitos, 16 antes del punto y 4 después)
      const montoRegex = /^\d{0,16}(\.\d{0,4})?$/;
      if (!montoRegex.test(value)) {
        alert(
          "Monto debe tener hasta 16 dígitos antes del punto y hasta 4 después."
        );
        return;
      }
    }

    setForm({ ...form, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Registro actualizado:", form);
    router.push("/"); // Redirige a la página principal después de guardar
  };

  const handleCancel = () => {
    router.push("/"); // Redirige a la página principal sin guardar
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
        Actualizar Comisión
      </h1>
      <form
        onSubmit={handleSubmit}
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "1rem",
        }}
      >
        {Object.keys(form).map((field) => {
          const isDisabled =
            field !== "TransaccionHasta" && field !== "Monto"; // Solo habilitamos los campos TransaccionHasta y Monto

          return (
            <div
              key={field}
              style={{
                display: "flex",
                flexDirection: "column",
                gap: "0.5rem",
              }}
            >
              <label
                htmlFor={field}
                style={{
                  fontWeight: "bold",
                  color: "#1f2937",
                }}
              >
                {field
                  .replace(/([A-Z])/g, " $1")
                  .replace(/^./, (str) => str.toUpperCase())}
              </label>
              <input
                id={field}
                type={
                  field === "Monto" || field === "TransaccionHasta"
                    ? "number"
                    : "text"
                }
                name={field}
                value={form[field]}
                onChange={handleChange}
                disabled={isDisabled} // Habilita/deshabilita según la lógica
                style={{
                  padding: "10px",
                  fontSize: "1rem",
                  borderRadius: "4px",
                  border: "1px solid #d1d5db",
                  backgroundColor: isDisabled ? "#e5e7eb" : "#ffffff", // Diferencia visual para deshabilitados
                }}
                placeholder={`Ingrese ${field
                  .replace(/([A-Z])/g, " $1")
                  .toLowerCase()}`}
                maxLength={field === "TransaccionHasta" ? 9 : undefined} // Longitud máxima de TransaccionHasta
                step={field === "Monto" ? "0.0001" : undefined} // Paso para Monto
              />
            </div>
          );
        })}
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

export default UpdatePage;
