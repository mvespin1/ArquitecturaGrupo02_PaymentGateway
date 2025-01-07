"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaEye } from "react-icons/fa";
import "../../Css/general.css"; // Importa el archivo de estilos generales

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
    <main className="main-container">
      <h1 className="main-title">Gestión de Facturación</h1>
      <div>
        <h2 className="section-title">Facturación por Comercio</h2>
        <table className="data-table">
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
              <tr key={item.id || index}className={index % 2 === 0 ? "row-even" : "row-odd"}>
                <td>{item.facturacionComercio}</td>
                <td>{item.comercio}</td>
                <td>{item.fechaInicio}</td>
                <td>{item.fechaFin}</td>
                <td>{item.codComision}</td>
                <td>{item.valor}</td>
                <td>{item.estado}</td>
                <td>
                  <div className="action-buttons">
                    <button className="edit-button" onClick={() => handleUpdate(item)}>
                      <FaEdit />
                    </button>
                    <button className="view-button" onClick={() => handleView(item)}>
                      <FaEye />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div className="pagination">
          <button>&lt;</button>
          <button>1</button>
          <button className="active">2</button>
          <button>3</button>
          <button>&gt;</button>
        </div>
        <button className="back-button" onClick={handleBackToHome}>
          Volver al Inicio
        </button>
      </div>
    </main>
  );
};

export default CrudTable;
