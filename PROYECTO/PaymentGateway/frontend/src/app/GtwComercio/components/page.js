"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";
import "../../Css/general.css";

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

  const router = useRouter();

  const handleCreate = async (item) => {
    const transactionPayload = {
      CodigoComercio: item.CodigoComercio,
      CodigoInterno: item.CodigoInterno,
      Ruc: item.Ruc,
      RazonSocial: item.RazonSocial,
      NombreComercial: item.NombreComercial,
      FechaCreacion: item.FechaCreacion,
      CodigoComision: item.CodigoComision,
      PagosAceptados: item.PagosAceptados,
      Estado: item.Estado,
      FechaActivacion: item.FechaActivacion,
      FechaSuspension: item.FechaSuspension,
    };

    console.log("Payload para crear:", transactionPayload);

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
      console.log("Respuesta de la API:", result);
      router.push('/GtwComercio/create');
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  const handleRowClick = (item) => {
    router.push(`/GtwComercio/read/${item.CodigoComercio}`);
  };

  return (
    <div className="main-container">
      <div className="table-header">
        <div className="header-content">
          <h1 className="table-title">Comercios Registrados</h1>
        </div>
        <div className="table-summary">
          <div className="summary-item">
            <span className="summary-label">Total Comercios:</span>
            <span className="summary-value">{data.length}</span>
          </div>
          <div className="summary-item">
            <span className="summary-label">Comercios Activos:</span>
            <span className="summary-value">
              {data.filter(item => item.Estado === "Activo").length}
            </span>
          </div>
        </div>
      </div>

      <div className="table-responsive">
        <button className="create-button" onClick={() => handleCreate(data[0])}>
          <FaPlus className="button-icon" />
          <span> Nuevo</span>
        </button>
        <table className="modern-table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Código Interno</th>
              <th>RUC</th>
              <th>Razón Social</th>
              <th>Nombre Comercial</th>
              <th>Código Comisión</th>
              <th>Pagos Aceptados</th>
              <th>Estado</th>
              <th>Fecha Creación</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr
                key={item.CodigoComercio}
                className={`table-row ${index % 2 === 0 ? 'row-even' : 'row-odd'}`}
                onClick={() => handleRowClick(item)}
                style={{ cursor: 'pointer' }}
              >
                <td className="code-cell">{item.CodigoComercio}</td>
                <td className="code-cell">{item.CodigoInterno}</td>
                <td className="code-cell">{item.Ruc}</td>
                <td>{item.RazonSocial}</td>
                <td>{item.NombreComercial}</td>
                <td className="code-cell">{item.CodigoComision}</td>
                <td className="amount-cell">{item.PagosAceptados}</td>
                <td className="status-cell">
                  <span className={`status-badge ${item.Estado.toLowerCase()}`}>
                    {item.Estado}
                  </span>
                </td>
                <td className="date-cell">{item.FechaCreacion}</td>
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
