"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import "./page.css"; // Importamos el CSS

const UpdatePage = () => {
  const [form, setForm] = useState({
    CodigoComision: "001",
    TransaccionDesde: "1000",
    TransaccionHasta: "",
    Monto: "",
  });

  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "TransaccionHasta" && parseInt(value, 10) < parseInt(form.TransaccionDesde, 10)) {
      alert("Transacción Hasta debe ser mayor que Transacción Desde.");
      return; 
    }

    if (name === "Monto") {
      const montoRegex = /^\d{0,16}(\.\d{0,4})?$/;
      if (!montoRegex.test(value)) {
        alert("Monto debe tener hasta 16 dígitos antes del punto y hasta 4 después.");
        return;
      }
    }

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
    <main className={styles.main}>
      <h1 className={styles.h1}>Actualizar Comisión</h1>
      <form onSubmit={handleSubmit} className={styles.form}>
        {Object.keys(form).map((field) => (
          <div key={field} className={styles.field}>
            <label htmlFor={field}>
              {field.replace(/([A-Z])/g, " $1").replace(/^./, (str) => str.toUpperCase())}
            </label>
            <input
              id={field}
              type={field === "Monto" || field === "TransaccionHasta" ? "number" : "text"}
              name={field}
              value={form[field]}
              onChange={handleChange}
              disabled={field !== "TransaccionHasta" && field !== "Monto"}
              placeholder={`Ingrese ${field.replace(/([A-Z])/g, " $1").toLowerCase()}`}
              maxLength={field === "TransaccionHasta" ? 9 : undefined}
              step={field === "Monto" ? "0.0001" : undefined}
            />
          </div>
        ))}
        <div className={styles.buttons}>
          <button type="submit" className={styles.save}>
            Guardar
          </button>
          <button type="button" onClick={handleCancel} className={styles.cancel}>
            Cancelar
          </button>
        </div>
      </form>
    </main>
  );
};

export default UpdatePage;
