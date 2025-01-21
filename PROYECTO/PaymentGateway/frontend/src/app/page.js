"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import "./Css/general.css"; // Import the separated CSS file

const MainPage = () => {
  const router = useRouter();
  const [activeSection, setActiveSection] = useState("tables");

  const tables = [
    { id: 1, name: "GtwFacturacionComercio", route: "/GtwFacturacionComercio/components" },
    { id: 2, name: "GtwSeguridadMarca", route: "/GtwSeguridadMarca/components" },
    { id: 3, name: "GtwPosComercio", route: "/GtwPosComercio/components" },
    { id: 4, name: "GtwComercio", route: "/GtwComercio/components" },
    { id: 5, name: "GtwSeguridadProcesador", route: "/GtwSeguridadProcesador/components" },
    { id: 6, name: "GtwComisionSegmento", route: "/GtwComisionSegmento/components" },
    { id: 7, name: "GtwTransaccion", route: "/GtwTransaccion/components" },
    { id: 8, name: "GtwComision", route: "/GtwComision/components" },
    { id: 9, name: "GtwSeguridad", route: "/GtwSeguridad/components" },
  ];

  const handleNavigate = (route) => {
    router.push(route);
  };

  const renderTables = () => (
    <div className="contentArea">
      <header className="header">
        <h1 className="headerTitle">Gestión de Tablas</h1>
        <p className="headerDescription">Selecciona una tabla para gestionar los datos correspondientes.</p>
      </header>
      <div className="grid">
        {tables.map((table) => (
          <div
            key={table.id}
            className="card"
            onClick={() => handleNavigate(table.route)}
          >
            <h2>{table.name}</h2>
            <p>Hacer clic para gestionar</p>
          </div>
        ))}
      </div>
    </div>
  );

  const renderCreateCommerce = () => (
    <div className="contentArea">
      <h1 className="headerTitle">Crear Comercio</h1>
      <form className="form">
        <label className="label">
          Nombre del Comercio:
          <input type="text" name="commerceName" className="input" placeholder="Ejemplo: Tienda XYZ" />
        </label>
        <label className="label">
          Sistema POS Conectado:
          <select name="posSystem" className="select">
            <option value="">Selecciona un sistema POS</option>
            <option value="POS1">POS1</option>
            <option value="POS2">POS2</option>
            <option value="POS3">POS3</option>
          </select>
        </label>
        <button type="submit" className="button">Crear Comercio</button>
      </form>
    </div>
  );

  const renderManageCommissions = () => (
    <div className="contentArea">
      <h1 className="headerTitle">Gestión de Comisiones</h1>
      <div className="grid">
        <div className="card">Crear Comisión</div>
        <div className="card">Actualizar Comisión</div>
        <div className="card">Eliminar Comisión</div>
        <div className="card">Listar Comisiones</div>
      </div>
    </div>
  );

  return (
    <div className="container">
      <aside className="sidebar">
        <div className="logoContainer">
          <img src="Images/logo.jpg" alt="Logo" className="logo" />
        </div>
        <h2 className="sidebarTitle">Menú Principal</h2>
        <ul className="menu">
          <li
            className={activeSection === "tables" ? "menuItemActive" : "menuItem"}
            onClick={() => setActiveSection("tables")}
          >
            Gestión de Tablas
          </li>
          <li
            className={activeSection === "createCommerce" ? "menuItemActive" : "menuItem"}
            onClick={() => setActiveSection("createCommerce")}
          >
            Crear Comercio
          </li>
          <li
            className={activeSection === "manageCommissions" ? "menuItemActive" : "menuItem"}
            onClick={() => setActiveSection("manageCommissions")}
          >
            Gestión de Comisiones
          </li>
        </ul>
      </aside>
      <main className="main">
        {activeSection === "tables" && renderTables()}
        {activeSection === "createCommerce" && renderCreateCommerce()}
        {activeSection === "manageCommissions" && renderManageCommissions()}
      </main>
    </div>
  );
};

export default MainPage;
