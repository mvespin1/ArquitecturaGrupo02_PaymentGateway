"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";

const CrudTable = () => {
  const [data, setData] = useState([
    {
      id: 1,
      facturacionComercio: "FC001",
      comercio: "Comercio A",
      fechaInicio: "2023-01-01",
      fechaFin: "2023-12-31",
      comision: "5%",
      valor: "$500",
      estado: "Activo",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleUpdate = (id) => {
    router.push(`/GtwFacturacionComercio/update/`); // Redirige al formulario de actualización
  };

  const handleView = (id) => {
    router.push(`/GtwFacturacionComercio/read/`); // Redirige a la página de visualización
  };

  const handleBackToHome = () => {
    router.push("/GtwFacturacionComercio/components/"); // Redirige al inicio
  };

  return (
    <main>
      <h1 style={{ textAlign: "center", color: "#94a3b8" }}>Gestión de Facturación</h1>
      <div>
        <h2 style={{ marginBottom: "1rem", color: "#e2e8f0" }}>Facturación por Comercio</h2>
        <table>
          <thead>
            <tr>
              <th>Facturación Comercio</th>
              <th>Comercio</th>
              <th>Fecha Inicio</th>
              <th>Fecha Fin</th>
              <th>Comisión</th>
              <th>Valor</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index}>
                <td>{item.facturacionComercio}</td>
                <td>{item.comercio}</td>
                <td>{item.fechaInicio}</td>
                <td>{item.fechaFin}</td>
                <td>{item.comision}</td>
                <td>{item.valor}</td>
                <td>{item.estado}</td>
                <td>
                  <div
                    style={{
                      display: "flex",
                      gap: "10px",
                      justifyContent: "center",
                    }}
                  >
                    <button
                      style={{
                        backgroundColor: "#3b82f6",
                        color: "white",
                        padding: "5px 10px",
                        border: "none",
                        borderRadius: "4px",
                        cursor: "pointer",
                      }}
                      onClick={() => handleUpdate(item.id)} // Redirige al formulario de actualización
                    >
                      <FaEdit />
                    </button>
                    <button
                      style={{
                        backgroundColor: "#38bdf8",
                        color: "white",
                        padding: "5px 10px",
                        border: "none",
                        borderRadius: "4px",
                        cursor: "pointer",
                      }}
                      onClick={() => handleView(item.id)} // Redirige a la página de visualización
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
