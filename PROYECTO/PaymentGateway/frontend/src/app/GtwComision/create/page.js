"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

const CreatePage = () => {
  const [form, setForm] = useState({
    Tipo: "",
    MontoBase: "",
    TransaccionesBase: "",
    ManejaSegmentos: "",
  });

  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "ManejaSegmentos") {
      setForm({ ...form, [name]: value });

      if (value === "si") {
        // Redirige a la ruta de GTW_COMISION_SEGMENTOS
        router.push(
          "/GtwComisionSegmento/components/"
        );
      }
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Registro creado:", form);
    router.push("/");
  };

  const handleCancel = () => {
    router.push("/GtwComision/components");
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
          color: "#000000",
          marginBottom: "1.5rem",
        }}
      >
        Crear Nueva Comisión
      </h1>
      <form
        onSubmit={handleSubmit}
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "1rem",
        }}
      >
        {/* Tipo */}
        <div>
          <label style={{ fontWeight: "bold", color: "#000000" }}>
            Tipo
          </label>
          <div style={{ display: "flex", gap: "1rem" }}>
            <label style={{ color: "#000000" }}>
              <input
                type="radio"
                name="Tipo"
                value="porcentual"
                checked={form.Tipo === "porcentual"}
                onChange={handleChange}
              />
              Porcentual
            </label>
            <label style={{ color: "#000000" }}>
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
        </div>

        {/* Monto Base */}
        <div>
          <label
            htmlFor="MontoBase"
            style={{ fontWeight: "bold", color: "#000000" }}
          >
            Monto Base
          </label>
          <input
            id="MontoBase"
            type="number"
            name="MontoBase"
            value={form.MontoBase}
            onChange={handleChange}
            style={{
              padding: "10px",
              fontSize: "1rem",
              borderRadius: "4px",
              border: "1px solid #d1d5db",
              backgroundColor: "#ffffff",
              color: "#000000",
            }}
            placeholder="Ingrese monto base"
            step="0.0001"
            max="9999999999999999.9999"
          />
        </div>

        {/* Transacciones Base */}
        <div>
          <label
            htmlFor="TransaccionesBase"
            style={{ fontWeight: "bold", color: "#000000" }}
          >
            Transacciones Base
          </label>
          <input
            id="TransaccionesBase"
            type="number"
            name="TransaccionesBase"
            value={form.TransaccionesBase}
            onChange={handleChange}
            style={{
              padding: "10px",
              fontSize: "1rem",
              borderRadius: "4px",
              border: "1px solid #d1d5db",
              backgroundColor: "#ffffff",
              color: "#000000",
            }}
            placeholder="Ingrese transacciones base"
            max="999999999"
          />
        </div>

        {/* Maneja Segmentos */}
        <div>
          <label style={{ fontWeight: "bold", color: "#000000" }}>
            Maneja Segmentos
          </label>
          <div style={{ display: "flex", gap: "1rem" }}>
            <label style={{ color: "#000000" }}>
              <input
                type="radio"
                name="ManejaSegmentos"
                value="si"
                checked={form.ManejaSegmentos === "si"}
                onChange={handleChange}
              />
              Sí
            </label>
            <label style={{ color: "#000000" }}>
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
              color: "#000000",
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
              color: "#000000",
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
