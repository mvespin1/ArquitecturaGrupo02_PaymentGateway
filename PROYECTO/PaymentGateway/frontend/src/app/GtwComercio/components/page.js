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
      RazoSocial: "aaa",
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
    <main>
      <h1 style={{ textAlign: "center", color: "#94a3b8" }}>Comercio</h1>
      <div>
        <h2 style={{ marginBottom: "1rem", color: "#e2e8f0" }}>GtwComercio</h2>
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
        <table>
          <thead>
            <tr>
              <th>Codigo Comercio</th>
              <th>Codigo Interno</th>
              <th>Ruc</th>
              <th>Razon Social</th>
              <th>Nombre Comercial</th>
              <th>FechaC reacion</th>
              <th>Codigo Comision</th>
              <th>PagosAceptados</th>
              <th>Estado</th>
              <th>Fecha Activacion</th>
              <th>Fecha Suspension</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index}>
                <td>{item.CodigoComercio}</td>
                <td>{item.CodigoInterno}</td>
                <td>{item.Ruc}</td>
                <td>{item.RazonSocial}</td>
                <td>{item.NombreComercial}</td>
                <td>{item.FechaCreacion}</td>
                <td>{item.CodigoComision}</td>
                <td>{item.PagosAceptados}</td>
                <td>{item.Estado}</td>
                <td>{item.FechaActivacion}</td>
                <td>{item.FechaSuspension}</td>
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
