"use client";

import { useRouter } from "next/navigation";

const MainPage = () => {
  const router = useRouter(); // Inicializa el router para la navegación

  // Lista de tablas con sus rutas correspondientes
  const tables = [
    { id: 1, name: "GtwFacturacionComercio", route: "/GtwFacturacionComercio/components" },
    { id: 2, name: "Tabla 2 - Productos", route: "/Productos" },
    { id: 3, name: "Tabla 3 - Clientes", route: "/Clientes" },
    { id: 4, name: "GtwComercio", route: "/GtwComercio/components" },
    { id: 5, name: "GtwSeguridadGateway", route: "/GtwSeguridadGateway/components" },
    { id: 6, name: "GtwComisionSegmento", route: "/GtwComisionSegmento/components" },
    { id: 7, name: "Tabla 7 - Usuarios", route: "/Usuarios" },
    { id: 8, name: "Tabla 8 - Reportes", route: "/Reportes" },
    { id: 9, name: "Tabla 9 - Configuración", route: "/Configuracion" },
    
  ];

  const handleNavigate = (route) => {
    router.push(route); // Redirige a la ruta seleccionada
  };

  return (
    <main
      style={{
        maxWidth: "900px",
        margin: "3rem auto",
        padding: "2rem",
        backgroundColor: "#ffffff",
        borderRadius: "12px",
        boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)",
        fontFamily: "'Inter', sans-serif",
      }}
    >
      <h1
        style={{
          textAlign: "center",
          color: "#1e3a8a",
          marginBottom: "2rem",
          fontSize: "2rem",
          fontWeight: "bold",
        }}
      >
        Gestión de Tablas
      </h1>
      <p
        style={{
          textAlign: "center",
          color: "#6b7280",
          marginBottom: "2rem",
          fontSize: "1.1rem",
        }}
      >
        Selecciona una tabla para gestionar los datos correspondientes.
      </p>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
          gap: "1.5rem",
        }}
      >
        {tables.map((table) => (
          <button
            key={table.id}
            style={{
              backgroundColor: "#3b82f6",
              color: "#ffffff",
              padding: "15px",
              border: "none",
              borderRadius: "8px",
              cursor: "pointer",
              textAlign: "center",
              fontSize: "1rem",
              fontWeight: "500",
              transition: "transform 0.2s, background-color 0.2s",
              boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
            }}
            onClick={() => handleNavigate(table.route)}
            onMouseEnter={(e) => (e.target.style.backgroundColor = "#2563eb")}
            onMouseLeave={(e) => (e.target.style.backgroundColor = "#3b82f6")}
          >
            {table.name}
          </button>
        ))}
      </div>
    </main>
  );
};

export default MainPage;
