"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { FaEdit, FaEye, FaPlus } from "react-icons/fa";
import "../../Css/general.css";

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

  const router = useRouter();

  const handleUpdate = async (item) => {
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
      router.push(`/GtwPosComercio/update/${item.id}`);
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  const handleView = async (item) => {
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
      router.push(`/GtwPosComercio/read/${item.id}`);
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  return (
    <div className="main-container">
      <div className="table-header">
        <div className="header-content">
          <h1 className="table-title">POS Comercio Registrados</h1>
        </div>
        <div className="table-summary">
          <div className="summary-item">
            <span className="summary-label">Total POS:</span>
            <span className="summary-value">{data.length}</span>
          </div>
          <div className="summary-item">
            <span className="summary-label">POS Activos:</span>
            <span className="summary-value">
              {data.filter(item => item.estado === "Activo").length}
            </span>
          </div>
        </div>
      </div>

      <div className="table-responsive">
        <button className="create-button" onClick={() => router.push('/GtwPosComercio/create')}>
          <FaPlus className="button-icon" />
          <span> Nuevo</span>
        </button>
        <table className="modern-table">
          <thead>
            <tr>
              <th>Modelo</th>
              <th>Código POS</th>
              <th>Comercio</th>
              <th>Dirección MAC</th>
              <th>Estado</th>
              <th>Fecha Activación</th>
              <th>Último Uso</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.codigoPos} className={`table-row ${index % 2 === 0 ? 'row-even' : 'row-odd'}`}>
                <td>{item.modelo}</td>
                <td className="code-cell">{item.codigoPos}</td>
                <td>{item.codComercio}</td>
                <td className="code-cell">{item.direccionMac}</td>
                <td className="status-cell">
                  <span className={`status-badge ${item.estado.toLowerCase()}`}>
                    {item.estado}
                  </span>
                </td>
                <td className="date-cell">{item.fechaActivacion}</td>
                <td className="date-cell">{item.ultimoUso}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="table-footer">
        <div className="pagination">
          <button className="pagination-button">&lt;</button>
          <button className="pagination-button active">1</button>
          <button className="pagination-button">2</button>
          <button className="pagination-button">3</button>
          <button className="pagination-button">&gt;</button>
        </div>
      </div>
    </div>
  );
};

export default CrudTable;
