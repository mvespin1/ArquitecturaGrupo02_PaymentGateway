"use client";

import { useParams, useRouter } from "next/navigation";
import "./page.css";


const ViewPage = () => {
  const { id } = useParams(); // Obtiene el ID del registro de la URL
  const router = useRouter(); // Inicializa el router para redirigir

  // Datos de ejemplo
  const mockData = {
    CodigoSeguridadProcesador: "xx",
    Clave: "xxx",
    FechaActualizacion: "xxx",
    FechaActivacion: "xxx",
    Estado: "xxx",
  };

  const handleCancel = () => {
    router.push("/GtwSeguridadProcesador/components"); // Redirige a la página principal sin guardar
  };

  return (
    <main className={styles.main}>
      <h1 className={styles.title}>Visualizar Comisiones</h1>
      <div className={styles.dataContainer}>
        {Object.entries(mockData).map(([key, value]) => (
          <div key={key}>
            <strong className={styles.label}>
              {key.replace(/([A-Z])/g, " $1")}:{" "}
            </strong>
            <span>{value}</span>
          </div>
        ))}
      </div>
      <button type="button" onClick={handleCancel} className={styles.button}>
        Volver
      </button>
    </main>
  );
};

export default ViewPage;
