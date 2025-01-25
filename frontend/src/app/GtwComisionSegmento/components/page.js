"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";

const CrudTable = () => {
  const [data, setData] = useState([
    {
      CodigoComision: "001",
      TransaccionDesde: "xxx",
      TransaccionHasta: "xxx",
      Monto: "10",
      id: "1", // Asegúrate de tener un id único para cada elemento
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleUpdate = async (item) => {
    // Crear el objeto transactionPayload con los datos de la fila seleccionada
    const transactionPayload = {
      CodigoComision: item.CodigoComision,
      TransaccionDesde: item.TransaccionDesde,
      TransaccionHasta: item.TransaccionHasta,
      Monto: item.Monto,
    };

    console.log("Payload para actualización:", transactionPayload);

    // Simular envío de datos (puedes usar fetch para un POST real)
    try {
      const response = await fetch("http://localhost:8082", {
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
      router.push(`/GtwComisionSegmento/update/${item.id}`); // Redirige al formulario de actualización
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  const handleCreate = () => {
    router.push("/GtwComisionSegmento/create"); // Redirige a la página de creación
  };

  const handleBackToHome = () => {
    router.push("/"); // Redirige al inicio
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
          Crear Comision
        </button>
        <table>
          <thead>
            <tr>
              <th>Codigo Comision</th>
              <th>Transaccion Desde</th>
              <th>Transaccion Hasta</th>
              <th>Monto </th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index}>
                <td>{item.CodigoComision}</td>
                <td>{item.TransaccionDesde}</td>
                <td>{item.TransaccionHasta}</td>
                <td>{item.Monto}</td>
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
                      onClick={() => handleUpdate(item)}
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
                      onClick={() => router.push(`/GtwComisionSegmento/view/${item.id}`)} // Redirige a la página de visualización
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
