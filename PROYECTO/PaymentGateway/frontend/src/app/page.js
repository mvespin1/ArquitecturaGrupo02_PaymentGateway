"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import "./Css/general.css"; // Import the separated CSS file

const MainPage = () => {
  const router = useRouter();
  const [activeSection, setActiveSection] = useState("manageCommissions");

  const handleNavigate = (section) => {
    setActiveSection(section);
  };

  return (
    <div className="container">
      <aside className="sidebar">
        <div className="logoContainer">
          <img src="Images/logo.jpg" alt="Logo" className="logo" />
        </div>
        <h2 className="sidebarTitle">Menú Principal</h2>
        <ul className="menu" style={{ listStyleType: "none", paddingLeft: 0 }}>
          <li
            className="menuItem"
            onClick={() => handleNavigate("manageCommissions")}
          >
            Gestión de Comisiones
          </li>
          <li
            className="menuItem"
            onClick={() => handleNavigate("manageCommerce")}
          >
            Gestión de Comercios
          </li>
          <li
            className="menuItem"
            onClick={() => handleNavigate("managePos")}
          >
            Gestión de POS
          </li>
        </ul>
      </aside>
      <main className="main">
        {activeSection === "manageCommissions" && (
          <div className="contentArea">
            <h1 className="headerTitle">Gestión de Comisiones</h1>
            <div className="grid">
              <div className="card">Crear Comisión</div>
              <div className="card">Actualizar Comisión</div>
              <div className="card">Eliminar Comisión</div>
              <div className="card">Listar Comisiones</div>
            </div>
          </div>
        )}
        {activeSection === "manageCommerce" && (
          <div className="contentArea">
            <h1 className="headerTitle">Gestión de Comercios</h1>
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
        )}
        {activeSection === "managePos" && (
          <div className="contentArea">
            <h1 className="headerTitle">Gestión de POS</h1>
            <div className="grid">
              <div className="card">Agregar POS</div>
              <div className="card">Actualizar POS</div>
              <div className="card">Eliminar POS</div>
              <div className="card">Listar POS</div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
};

export default MainPage;
