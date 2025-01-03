"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaEye } from "react-icons/fa";

const CrudTable = () => {
  const [data, setData] = useState([
    {
      facturacionComercio: "FC001",
      comercio: "Comercio A",
      fechaInicio: "2023-01-01",
      fechaFin: "2023-12-31",
      transaccionesProcesadas: "150",
      transaccionesAutorizadas: "140",
      transaccionesRechazadas: "10",
      transaccionesReversadas: "5",
      codComision: "COM001",
      valor: "500",
      estado: "Activo",
      codigoFacturacion: "COD123",
      fechaFacturacion: "2023-10-01",
      fechaPago: "2023-10-15",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleUpdate = async (item) => {
    // Crear el objeto transactionPayload con los datos de la fila seleccionada
    const transactionPayload = {
      facturacionComercio: item.facturacionComercio,
      comercio: item.comercio,
      fechaInicio: item.fechaInicio,
      fechaFin: item.fechaFin,
      transaccionesProcesadas: item.transaccionesProcesadas,
      transaccionesAutorizadas: item.transaccionesAutorizadas,
      transaccionesRechazadas: item.transaccionesRechazadas,
      transaccionesReversadas: item.transaccionesReversadas,
      codComision: item.codComision,
      valor: parseFloat(item.valor), // Convertir el valor a número flotante
      estado: item.estado,
      codigoFacturacion: item.codigoFacturacion,
      fechaFacturacion: item.fechaFacturacion,
      fechaPago: item.fechaPago,
    };

    console.log("Payload para actualización:", transactionPayload);

    // Simular envío de datos (puedes usar fetch para un POST real)
    try {
      const response = await fetch("http://localhost:8082/api/pagos/procesar", {
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
      router.push(`/GtwFacturacionComercio/update/${item.id}`); // Redirige al formulario de actualización
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  const handleView = async (item) => {
    // Crear el objeto transactionPayload con los datos de la fila seleccionada para visualizar
    const transactionPayload = {
      facturacionComercio: item.facturacionComercio,
      comercio: item.comercio,
      fechaInicio: item.fechaInicio,
      fechaFin: item.fechaFin,
      transaccionesProcesadas: item.transaccionesProcesadas,
      transaccionesAutorizadas: item.transaccionesAutorizadas,
      transaccionesRechazadas: item.transaccionesRechazadas,
      transaccionesReversadas: item.transaccionesReversadas,
      codComision: item.codComision,
      valor: parseFloat(item.valor), // Convertir el valor a número flotante
      estado: item.estado,
      codigoFacturacion: item.codigoFacturacion,
      fechaFacturacion: item.fechaFacturacion,
      fechaPago: item.fechaPago,
    };

    console.log("Payload para visualización:", transactionPayload);

    // Simular envío de datos
    try {
      const response = await fetch("http://localhost:8082/api/pagos/procesar", {
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
      router.push(`/GtwFacturacionComercio/read/${item.id}`); // Redirige a la página de visualización
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
            {data.map((item) => (
              <tr key={item.id}>
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
