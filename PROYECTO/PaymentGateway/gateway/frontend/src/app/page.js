"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import "./Css/general.css"; // Import the separated CSS file
import CrudTable from "./GtwComision/components/page";
import ComercioTable from "./GtwComercio/components/page";
import PosComercioTable from "./GtwPosComercio/components/page";

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
          <CrudTable />
        )}
        {activeSection === "manageCommerce" && (
          <ComercioTable />
        )}
        {activeSection === "managePos" && (
          <PosComercioTable />
        )}
      </main>
    </div>
  );
};

export default MainPage;
