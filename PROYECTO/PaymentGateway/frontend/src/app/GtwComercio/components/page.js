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
      alert(`Respuesta de la API: ${JSON.stringify(result)}`);
      router.push(`/GtwComercio/create/${item.id}`);
    } catch (error) {
      console.error("Error al enviar los datos:", error);
      alert("Ocurrió un error al enviar los datos. Inténtalo nuevamente.");
    }
  };

  const handleBackToHome = () => {
    router.push("/");
  };

  return (
    <main className="main-container">
      <h1 className="main-title">Comercio</h1>
      <div>
        <h2 className="section-title">GtwComercio</h2>
        <button className="create-button" onClick={handleCreate}>
          <FaPlus /> Crear Comercio
        </button>
        <table className="data-table">
          <thead>
            <tr>
              <th>Codigo Comercio</th>
              <th>Codigo Interno</th>
              <th>Ruc</th>
              <th>Razon Social</th>
              <th>Nombre Comercial</th>
              <th>Fecha Creacion</th>
              <th>Codigo Comision</th>
              <th>Pagos Aceptados</th>
              <th>Estado</th>
              <th>Fecha Activacion</th>
              <th>Fecha Suspension</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={index} className={index % 2 === 0 ? "row-even" : "row-odd"}>
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
                  <div className="action-buttons">
                    <button className="view-button" onClick={() => handleView(item)}>
                      <FaEye />
                    </button>
                    <button className="edit-button" onClick={() => handleView(item)}>
                      <FaEdit />
                    </button>
                    <button className="delete-button" onClick={() => handleView(item)}>
                      <FaTrash />
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
