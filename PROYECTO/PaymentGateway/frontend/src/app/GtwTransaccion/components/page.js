"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEye } from "react-icons/fa";
import "../../Css/general.css"; // Importa el archivo de estilos generales

const CrudTable = () => {
  const [data, setData] = useState([
    {
      CodigoTransaccion: "001",
      CodigoComercio: "CC001",
      FacturacionComercio: "FC001",
      Tipo: "Factura",
      Marca: "Visa",
      Detalle: "Pago de servicios",
      Monto: "$100",
      COdigoUnicoTransaccion: "CUT001",
      Fecha: "2023-01-01",
      Estado: "Aprobado",
      Moneda: "USD",
      Pais: "Ecuador",
      Tarjeta: "**** **** **** 1234",
      FechaEjecucionRecurrencia: "2025-01-01",
      FechaFinRecurrencia: "2025-12-31",
    },
  ]);

  const router = useRouter(); // Inicializa el router para redirigir

  const handleView = (id) => {
    router.push(`/GtwTransaccion/read`); // Redirige a la página de visualización
  };

  const handleBackToHome = () => {
    router.push("/"); // Redirige al inicio
  };

  return (
    <main className="main-container">
      <h1 className="main-title">Gestión de Transacciones</h1>
      <div>
        <h2 className="section-title">Transacciones</h2>
        <table className="data-table">
          <thead>
            <tr>
              <th>Código Transacción</th>
              <th>Código Comercio</th>
              <th>Facturación Comercio</th>
              <th>Marca</th>
              <th>Estado</th>
              <th>Monto</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index} className={index % 2 === 0 ? "row-even" : "row-odd"}>
                <td>{item.CodigoTransaccion}</td>
                <td>{item.CodigoComercio}</td>
                <td>{item.FacturacionComercio}</td>
                <td>{item.Marca}</td>
                <td>{item.Estado}</td>
                <td>{item.Monto}</td>
                <td className="action-buttons">
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
