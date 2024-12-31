"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";

const CrudTable = () => {
  const [data, setData] = useState([
    {
      id: 1,
      modelo: "Modelo A",
      codigoPos: "FC001",
      codComercio: "Comercio A",
      direccionMac: "00:1B:44:11:3A:B7",
      estado: "Activo",
      fechaActivacion: "2023-01-01",
      ultimoUso: "2023-12-31",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleCreate = () => {
    router.push("/GtwPosComercio/create");
  };

  const handleUpdate = (id) => {
    router.push(`/GtwPosComercio/update/`); // Redirige al formulario de actualización
  };

  const handleView = (id) => {
    router.push(`/GtwPosComercio/read`); // Redirige a la página de visualización
  };

  const handleBackToHome = () => {
    router.push("/"); // Redirige al inicio
  };

  return (
    <main>
      <h1 style={{ textAlign: "center", color: "#94a3b8" }}>Gestión de POS Comercio</h1>
      <div>
        <h2 style={{ marginBottom: "1rem", color: "#e2e8f0" }}>POS Comercio</h2>      
        <table>
          <thead>
            <tr>
              <th>Modelo</th>
              <th>Código POS</th>
              <th>Comercio</th>
              <th>Dirección MAC</th>
              <th>Estado</th>
              <th>Fecha Activación</th>
              <th>Último Uso</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item) => (
              <tr key={item.id}>
                <td>{item.modelo}</td>
                <td>{item.codigoPos}</td>
                <td>{item.codComercio}</td>
                <td>{item.direccionMac}</td>
                <td>{item.estado}</td>
                <td>{item.fechaActivacion}</td>
                <td>{item.ultimoUso}</td>
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
