"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import "./page.css"; // Importar el archivo CSS

const CreatePage = () => {
  const [form, setForm] = useState({
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

  const tiposComision = ["Tipo 1", "Tipo 2", "Tipo 3"];

  return (
    <main>
      <h1>Crear Nuevo Comercio</h1>
      <form onSubmit={handleSubmit}>
        {Object.keys(form).map((field) => (
          <div className="form-group" key={field}>
            <label htmlFor={field}>
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
                disabled={[
                  "CODIGO_INTERNO",
                  "FECHA_CREACION",
                  "PAGOS_ACEPTADOS",
                  "ESTADO",
                  "FECHA_ACTIVACION",
                  "FECHA_SUSPENSION",
                ].includes(field)}
                placeholder={`Ingrese ${field
                  .replace(/_/g, " ")
                  .replace(/([A-Z])/g, " $1")
                  .toLowerCase()}`}
              />
            )}
          </div>
        ))}
        <div className="buttons">
          <button type="submit">Guardar</button>
          <button type="button" onClick={handleCancel}>
            Cancelar
          </button>
        </div>
      </form>
    </main>
  );
};

export default CreatePage;
