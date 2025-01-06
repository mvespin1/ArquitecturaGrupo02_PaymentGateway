"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import "./page.css"; // Importamos el CSS como módulo


const UpdatePage = () => {
  const [form, setForm] = useState({
    CodigoComercio: "001",
    CodigoInterno: "0009",
    Ruc: "9999999999999",
    RazoSocial: "Razon Social de Prueba",
    NombreComercial: "Nombre Comercial",
    FechaCreacion: "2024-01-01",
    CodigoComision: "1",
    PagosAceptados: "$100",
    Estado: "Pendiente",
    FechaActivacion: "2024-02-01",
    FechaSuspension: "2024-12-20",
  });

  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Registro actualizado:", form);
    router.push("/");
  };

  const handleCancel = () => {
    router.push("/");
  };

  return (
    <main className="update-container">
      <h1 className="update-title">Actualizar Comercio</h1>
      <form onSubmit={handleSubmit} className="update-form">
        {Object.keys(form).map((field) => {
          if (field === "Estado") {
            return (
              <div key={field} className="form-group">
                <label htmlFor={field} className="form-label">
                  Estado
                </label>
                <select
                  id={field}
                  name={field}
                  value={form[field]}
                  onChange={handleChange}
                  className="form-select"
                >
                  <option value="Activo">Activo</option>
                  <option value="Inactivo">Inactivo</option>
                  <option value="Pendiente">Pendiente</option>
                  <option value="Suspendido">Suspendido</option>
                </select>
              </div>
            );
          } else if (field === "CodigoComision") {
            return (
              <div key={field} className="form-group">
                <label htmlFor={field} className="form-label">
                  Código Comisión
                </label>
                <select
                  id={field}
                  name={field}
                  value={form[field]}
                  onChange={handleChange}
                  className="form-select"
                >
                  <option value="1">Comisión 1</option>
                  <option value="2">Comisión 2</option>
                  <option value="3">Comisión 3</option>
                </select>
              </div>
            );
          } else {
            return (
              <div key={field} className="form-group">
                <label htmlFor={field} className="form-label">
                  {field
                    .replace(/([A-Z])/g, " $1")
                    .replace(/^./, (str) => str.toUpperCase())}
                </label>
                <input
                  id={field}
                  type={field.includes("Fecha") ? "date" : "text"}
                  name={field}
                  value={form[field]}
                  onChange={handleChange}
                  disabled
                  className="form-input disabled-input"
                />
              </div>
            );
          }
        })}
        <div className="form-actions">
          <button type="submit" className="btn btn-success">
            Guardar
          </button>
          <button type="button" onClick={handleCancel} className="btn btn-danger">
            Cancelar
          </button>
        </div>
      </form>
    </main>
  );
};

export default UpdatePage;
