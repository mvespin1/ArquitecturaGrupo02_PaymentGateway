"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";

const CrudTable = () => {
  const [data, setData] = useState([
    {
      CodigoComercio: "001",
      CodigoInterno: "0009",
      Ruc: "9999999",
      RazonSocial: "aaa",
      NombreComercial: "XXXX",
      FechaCreacion: "01-01-2024",
      CodigoComision: "005",
      PagosAceptados: "$100",
      Estado: "Activo",
      FechaActivacion: "02-01-2024",
      FechaSuspension: "20-12-2024",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleCreate = () => {
    router.push("/GtwComercio/create");
  };

  const handleView = (id) => {
    router.push(`/GtwComercio/read`); // Redirige a la página de visualización
  };

  const handleBackToHome = () => {
    router.push("/"); // Redirige al inicio
  };

  return (
    <main
      style={{
        fontFamily: "Arial, sans-serif",
        backgroundColor: "#1e293b",
        color: "#f8fafc",
        padding: "2rem",
        borderRadius: "8px",
        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
        maxWidth: "1200px",
        margin: "2rem auto",
      }}
    >
      <h1 style={{ textAlign: "center", color: "#38bdf8", marginBottom: "1.5rem" }}>
        Comercio
      </h1>
      <div>
        <h2 style={{ marginBottom: "1rem", color: "#94a3b8" }}>GtwComercio</h2>
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
          Crear Comercio
        </button>
        <table
          style={{
            width: "100%",
            borderCollapse: "collapse",
            marginBottom: "1.5rem",
          }}
        >
          <thead>
            <tr style={{ backgroundColor: "#334155", color: "#ffffff" }}>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Codigo Comercio</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Codigo Interno</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Ruc</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Razon Social</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Nombre Comercial</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Fecha Creacion</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Codigo Comision</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Pagos Aceptados</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Estado</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Fecha Activacion</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Fecha Suspension</th>
              <th style={{ padding: "10px", border: "1px solid #475569" }}>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr
                key={index}
                style={{ backgroundColor: index % 2 === 0 ? "#1e293b" : "#2d3748", color: "#ffffff" }}
              >
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.CodigoComercio}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.CodigoInterno}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.Ruc}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.RazonSocial}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.NombreComercial}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.FechaCreacion}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.CodigoComision}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.PagosAceptados}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.Estado}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.FechaActivacion}</td>
                <td style={{ padding: "10px", border: "1px solid #475569" }}>{item.FechaSuspension}</td>
                <td
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    gap: "10px",
                    padding: "10px",
                    border: "1px solid #475569",
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
                  >
                    <FaEdit />
                  </button>
                  <button
                    style={{
                      backgroundColor: "#ef4444",
                      color: "white",
                      padding: "5px 10px",
                      border: "none",
                      borderRadius: "4px",
                      cursor: "pointer",
                    }}
                  >
                    <FaTrash />
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
                    onClick={() => handleView(item.id)}
                  >
                    <FaEye />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div
          className="pagination"
          style={{
            marginTop: "1rem",
            display: "flex",
            justifyContent: "center",
            gap: "8px",
          }}
        >
          <button
            style={{
              backgroundColor: "#475569",
              color: "#ffffff",
              padding: "8px 16px",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            &lt;
          </button>
          <button
            style={{
              backgroundColor: "#334155",
              color: "#ffffff",
              padding: "8px 16px",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            1
          </button>
          <button
            className="active"
            style={{
              backgroundColor: "#22c55e",
              color: "#ffffff",
              padding: "8px 16px",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            2
          </button>
          <button
            style={{
              backgroundColor: "#334155",
              color: "#ffffff",
              padding: "8px 16px",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            3
          </button>
          <button
            style={{
              backgroundColor: "#475569",
              color: "#ffffff",
              padding: "8px 16px",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            &gt;
          </button>
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
