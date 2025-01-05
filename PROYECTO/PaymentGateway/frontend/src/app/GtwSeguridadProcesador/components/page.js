"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";

const CrudTable = () => {
  const [data, setData] = useState([
    {
      CodigoSeguridadProcesador: "001",
      Clave: "xxxx",
      FechaActualizacion: "2023-01-01",
      FechaActivacion: "2023-01-12",
      Estado: "Activo",
    },
  ]);

  const router = useRouter();

  const handleView = async (item) => {
    const transactionPayload = {
      CodigoSeguridadProcesador: item.CodigoSeguridadProcesador,
      Clave: item.Clave,
      FechaActualizacion: item.FechaActualizacion,
      FechaActivacion: item.FechaActivacion,
      Estado: item.Estado,
    };

    console.log("Payload para visualización:", transactionPayload);

    try {
      const response = await fetch("http://localhost:8082/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(transactionPayload),
      });

      if (!response.ok) {
        throw new Error(`Error en la API: ${response.statusText}`);
      }

      const result = await response.json();
      alert(`Respuesta de la API: ${JSON.stringify(result)}`);
      router.push(`/GtwSeguridadProcesador/read/${item.id}`);
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  const handleCreate = () => {
    console.log("Crear Comisión");
  };

  const handleBackToHome = () => {
    router.push("/");
  };

  return (
    <main>
      <h1 style={{ textAlign: "center", color: "#94a3b8" }}>Gestión de Comisiones</h1>
      <div>
        <h2 style={{ marginBottom: "1rem", color: "#e2e8f0" }}>Comisiones por Comercio</h2>
        <button
          className="crear-factura"
          style={{
            marginBottom: "1rem",
            backgroundColor: "#22c55e",
            color: "#ffffff",
            padding: "10px 20px",
            border: "none",
            borderRadius: "6px",
            cursor: "pointer",
            display: "flex",
            alignItems: "center",
            gap: "8px",
            justifyContent: "center",
          }}
          onClick={handleCreate}
        >
          <FaPlus />
          Crear Comisión
        </button>
        <table>
          <thead>
            <tr>
              <th>Codigo Seguridad Procesador</th>
              <th>Clave</th>
              <th>Fecha Actualización</th>
              <th>Fecha Activación</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index}>
                <td>{item.CodigoSeguridadProcesador}</td>
                <td>{item.Clave}</td>
                <td>{item.FechaActualizacion}</td>
                <td>{item.FechaActivacion}</td>
                <td>{item.Estado}</td>
                <td>
                  <div style={{ display: "flex", gap: "10px", justifyContent: "center" }}>
                    <button style={{ backgroundColor: "#3b82f6", color: "white", padding: "5px 10px", border: "none", borderRadius: "4px", cursor: "pointer" }}>
                      <FaEdit />
                    </button>
                    <button style={{ backgroundColor: "#ef4444", color: "white", padding: "5px 10px", border: "none", borderRadius: "4px", cursor: "pointer" }}>
                      <FaTrash />
                    </button>
                    <button
                      style={{ backgroundColor: "#38bdf8", color: "white", padding: "5px 10px", border: "none", borderRadius: "4px", cursor: "pointer" }}
                      onClick={() => handleView(item)}
                    >
                      <FaEye />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div className="pagination" style={{ marginTop: "1rem" }}>
          <button>&lt;</button>
          <button>1</button>
          <button className="active">2</button>
          <button>3</button>
          <button>&gt;</button>
        </div>
        <button
          style={{
            marginTop: "2rem",
            backgroundColor: "#3b82f6",
            color: "#ffffff",
            padding: "10px 20px",
            border: "none",
            borderRadius: "6px",
            cursor: "pointer",
            width: "100%",
            textAlign: "center",
          }}
          onClick={handleBackToHome}
        >
          Volver al Inicio
        </button>
      </div>
    </main>
  );
};

export default CrudTable;
