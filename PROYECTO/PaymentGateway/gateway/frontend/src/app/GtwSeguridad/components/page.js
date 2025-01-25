"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";
import "../../Css/general.css"; // Importa el archivo de estilos

const CrudTable = () => {
  const [data, setData] = useState([
    {
      CodigoClaveGateway: "001",
      Clave: "1234567890",
      FechasCreacion: "2023-01-01",
      FechaActivacion: "2023-01-01",
      Estado: "Activo",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleCreate = () => {
    router.push("/GtwSeguridad/create");
  };

  const handleView = (id) => {
    router.push(`/GtwSeguridad/read`); // Redirige a la página de visualización
  };

  const handleBackToHome = () => {
    router.push("/"); // Redirige al inicio
  };

  return (
    <main className="main-container">
      <h1 className="main-title">Gestión de Seguridad</h1>
      <div>
        <h2 className="section-title">Seguridad</h2>
        <button className="create-button" onClick={handleCreate}>
          <FaPlus   />
          Crear Seguridad
        </button>
        <table className="data-table">
          <thead>
            <tr>
              <th>Código Clave Gateway</th>
              <th>Clave</th>
              <th>Fechas Creación</th>
              <th>Fecha Activación</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index} className={index % 2 === 0 ? "row-even" : "row-odd"}>
                <td>{item.CodigoClaveGateway}</td>
                <td>{item.Clave}</td>
                <td>{item.FechasCreacion}</td>
                <td>{item.FechaActivacion}</td>
                <td>{item.Estado}</td>
                <td className="action-buttons">
                  <button className="edit-button">
                    <FaEdit />
                  </button>
                  <button className="delete-button">
                    <FaTrash />
                  </button>
                  <button className="view-button" onClick={() => handleView(item.id)}>
                    <FaEye />
                  </button>
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
