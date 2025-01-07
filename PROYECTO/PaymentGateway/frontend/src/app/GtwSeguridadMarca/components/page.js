"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEye } from "react-icons/fa";
import "../../Css/general.css"; // Importa el archivo de estilos generales

const CrudTable = () => {
  const [data, setData] = useState([
    {
      marca: "VISA",
      clave: "123456789",
      fechaActualizacion: "2023-01-01",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleView = async (item) => {
    // Crear el objeto transactionPayload con los datos de la fila seleccionada
    const transactionPayload = {
      marca: item.marca,
      clave: item.clave,
      fechaActualizacion: item.fechaActualizacion,
    };

    console.log("Payload para visualización:", transactionPayload);

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
      router.push(`/GtwSeguridadMarca/read/${item.id}`); // Redirige a la página de visualización
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
      <h1 className="main-title">Gestión de Seguridad Marca</h1>
      <div>
        <h2 className="section-title">Seguridad Marca</h2>
        <table className="data-table">
          <thead>
            <tr>
              <th>Marca</th>
              <th>Clave</th>
              <th>Fecha Actualización</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index} className={index % 2 === 0 ? "row-even" : "row-odd"}>
                <td>{item.marca}</td>
                <td>{item.clave}</td>
                <td>{item.fechaActualizacion}</td>
                <td>
                  <div className="action-buttons">
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
