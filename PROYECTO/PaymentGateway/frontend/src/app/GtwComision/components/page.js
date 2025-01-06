"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEdit, FaTrash, FaPlus, FaEye } from "react-icons/fa";
import "../../Css/general.css"; // Importa el archivo de estilos

const CrudTable = () => {
  const [data, setData] = useState([
    {
      CodigoComision: "001",
      Tipo: "Comision",
      MontoBase: "$100",
      TransaccionesBase: "10",
      ManejaSegmenos: "Si",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleCreate = () => {
    router.push("/GtwComision/create");
  };

  const handleView = (id) => {
    router.push(`/GtwComision/read`); // Redirige a la página de visualización
  };

  const handleBackToHome = () => {
    router.push("/"); // Redirige al inicio
  };

  return (
    <main className="main-container">
      <h1 className="main-title">Gestión de Comisiones</h1>
      <div>
        <h2 className="section-title">Comisiones por Comercio</h2>
        <button className="create-button" onClick={handleCreate}>
          <FaPlus   /> Crear Comisión
        </button>
        <table className="data-table">
          <thead>
            <tr>
              <th>Código Comisión</th>
              <th>Tipo</th>
              <th>Monto Base</th>
              <th>Transacciones Base</th>
              <th>Maneja Segmentos</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index} className={index % 2 === 0 ? "row-even" : "row-odd"}>
                <td>{item.CodigoComision}</td>
                <td>{item.Tipo}</td>
                <td>{item.MontoBase}</td>
                <td>{item.TransaccionesBase}</td>
                <td>{item.ManejaSegmenos}</td>
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
