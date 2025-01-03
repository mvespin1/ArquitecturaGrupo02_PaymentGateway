"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaEye, FaPlus } from "react-icons/fa";

const CrudTable = () => {
  const [data, setData] = useState([
    {
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

  const handleUpdate = async (item) => {
    // Crear el objeto transactionPayload con los datos de la fila seleccionada
    const transactionPayload = {
      modelo: item.modelo,
      codigoPos: item.codigoPos,
      codComercio: item.codComercio,
      direccionMac: item.direccionMac,
      estado: item.estado,
      fechaActivacion: item.fechaActivacion,
      ultimoUso: item.ultimoUso,
    };

    console.log("Payload para actualización:", transactionPayload);

    // Simular envío de datos (puedes usar fetch para un POST real)
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
      router.push(`/GtwPosComercio/update/${item.id}`); // Redirige al formulario de actualización
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  const handleView = async (item) => {
    // Crear el objeto transactionPayload con los datos de la fila seleccionada para visualizar
    const transactionPayload = {
      modelo: item.modelo,
      codigoPos: item.codigoPos,
      codComercio: item.codComercio,
      direccionMac: item.direccionMac,
      estado: item.estado,
      fechaActivacion: item.fechaActivacion,
      ultimoUso: item.ultimoUso,
    };

    console.log("Payload para visualización:", transactionPayload);

    // Simular envío de datos
    try {
      const response = await fetch("http://localhost:8082/api/pos/comercio/procesar", {
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
      router.push(`/GtwPosComercio/read/${item.id}`); // Redirige a la página de visualización
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
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
            {data.map((item, index) => (
              <tr key={item.id || index}>
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
                      onClick={() => handleUpdate(item)} // Redirige al formulario de actualización
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
                      onClick={() => handleView(item)} // Redirige a la página de visualización
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
