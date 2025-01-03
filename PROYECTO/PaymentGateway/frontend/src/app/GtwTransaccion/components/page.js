"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // Importa el hook para navegación
import { FaEye } from "react-icons/fa";

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
    <main>
      <h1 style={{ textAlign: "center", color: "#94a3b8" }}>Gestión de Transacciones</h1>
      <div>
        <h2 style={{ marginBottom: "1rem", color: "#e2e8f0" }}>Transacciones</h2>
        <table>
          <thead>
            <tr>
              <th>Codigo Transaccion</th>
              <th>Codigo Comercio</th>
              <th>Facturacion Comercio</th>
              <th>Marca</th>
              <th>Estado</th>
              <th>Monto</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr key={item.id || index}>
                <td>{item.CodigoTransaccion}</td>
                <td>{item.CodigoComercio}</td>
                <td>{item.FacturacionComercio}</td>
                <td>{item.Marca}</td>
                <td>{item.Estado}</td>
                <td>{item.Monto}</td>
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
                        backgroundColor: "#38bdf8",
                        color: "white",
                        padding: "5px 10px",
                        border: "none",
                        borderRadius: "4px",
                        cursor: "pointer",
                      }}
                      onClick={() => handleView(item.id)} // Redirige a la página de visualización
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
