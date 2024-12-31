"use client";

import { useParams, useRouter } from "next/navigation";

const ViewPage = () => {
  const { id } = useParams(); // Obtiene el ID del registro de la URL
  const router = useRouter(); // Inicializa el router para redirigir

  // Datos de ejemplo
  const mockData = {
    CodigoComercio: "",
    CodigoInterno: "",
    Ruc: "",
    RazoSocial: "",
    NombreComercial: "",
    FechaCreacion: "",
    CodigoComision: "",
    PagosAceptados: "",
    Estado: "",
    FechaActivacion: "",
    FechaSuspension: "",
  };

  const handleCancel = () => {
    router.push("/GtwComercio/components"); // Redirige a la página principal sin guardar
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
      <h1 style={{ textAlign: "center", color: "#1e40af", marginBottom: "1.5rem" }}>
        Visualizar Comisiones
      </h1>
      <div style={{ display: "flex", flexDirection: "column", gap: "1rem" }}>
        {Object.entries(mockData).map(([key, value]) => (
          <div key={key}>
            <strong style={{ color: "#1f2937" }}>{key.replace(/([A-Z])/g, " $1")}: </strong>
            <span>{value}</span>
          </div>
        ))}
      </div>

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
              marginTop: "1rem",
            }}
          >
            Volver
          </button>
    </main>
  );
};

export default ViewPage;
