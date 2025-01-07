"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { FaEdit, FaEye } from "react-icons/fa";
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

  const handleBackToHome = () => {
    router.push("/");
  };

  return (
    <main className="main-container">
      <h1 className="main-title">Gestión de POS Comercio</h1>
      <div>
        <h2 className="section-title">POS Comercio</h2>
        <table className="data-table">
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
              <tr key={item.id || index} className={index % 2 === 0 ? "row-even" : "row-odd"}>
                <td>{item.modelo}</td>
                <td>{item.codigoPos}</td>
                <td>{item.codComercio}</td>
                <td>{item.direccionMac}</td>
                <td>{item.estado}</td>
                <td>{item.fechaActivacion}</td>
                <td>{item.ultimoUso}</td>
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
